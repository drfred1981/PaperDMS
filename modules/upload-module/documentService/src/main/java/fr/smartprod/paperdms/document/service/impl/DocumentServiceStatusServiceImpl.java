package fr.smartprod.paperdms.document.service;

import fr.smartprod.paperdms.common.event.DocumentServiceStatusEvent;
import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import fr.smartprod.paperdms.document.repository.DocumentServiceStatusRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentServiceStatusMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;

/**
 * Service for managing document service status across microservices.
 * Implements idempotent status updates with database-level locking to prevent concurrent processing.
 */
@Service
@Transactional
public class DocumentServiceStatusService {

    private static final Logger log = LoggerFactory.getLogger(DocumentServiceStatusService.class);

    private final DocumentServiceStatusRepository documentServiceStatusRepository;
    private final DocumentServiceStatusMapper documentServiceStatusMapper;
    private final DocumentEventPublisher documentEventPublisher;

    public DocumentServiceStatusService(
        DocumentServiceStatusRepository documentServiceStatusRepository,
        DocumentServiceStatusMapper documentServiceStatusMapper,
        DocumentEventPublisher documentEventPublisher
    ) {
        this.documentServiceStatusRepository = documentServiceStatusRepository;
        this.documentServiceStatusMapper = documentServiceStatusMapper;
        this.documentEventPublisher = documentEventPublisher;
    }

    /**
     * Update service status for a document with database-level locking.
     * This method is idempotent and thread-safe.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @param status The new status
     * @param statusDetails Optional status details
     * @param userId The user who triggered the update
     * @return The updated DocumentServiceStatusDTO
     */
    public DocumentServiceStatusDTO updateServiceStatus(
        Long documentId,
        ServiceType serviceType,
        ServiceStatus status,
        String statusDetails,
        String userId
    ) {
        log.info("Updating service status: documentId={}, serviceType={}, status={}",
            documentId, serviceType, status);

        DocumentServiceStatus serviceStatus = documentServiceStatusRepository
            .findByDocumentIdAndServiceTypeWithLock(documentId, serviceType)
            .orElseGet(() -> {
                log.debug("Creating new service status record: documentId={}, serviceType={}",
                    documentId, serviceType);
                DocumentServiceStatus newStatus = new DocumentServiceStatus();
                newStatus.setDocumentId(documentId);
                newStatus.setServiceType(serviceType);
                newStatus.setRetryCount(0);
                newStatus.setUpdatedDate(Instant.now());
                return newStatus;
            });

        ServiceStatus previousStatus = serviceStatus.getStatus();
        
        if (shouldUpdateStatus(serviceStatus, status)) {
            updateServiceStatusFields(serviceStatus, status, statusDetails, userId);
            DocumentServiceStatus savedStatus = documentServiceStatusRepository.save(serviceStatus);
            
            log.info("Service status updated successfully: documentId={}, serviceType={}, previousStatus={}, newStatus={}",
                documentId, serviceType, previousStatus, status);

            publishServiceStatusEvent(savedStatus);

            return documentServiceStatusMapper.toDto(savedStatus);
        } else {
            log.debug("Service status update skipped (idempotent): documentId={}, serviceType={}, currentStatus={}",
                documentId, serviceType, serviceStatus.getStatus());
            return documentServiceStatusMapper.toDto(serviceStatus);
        }
    }

    /**
     * Update service status with error information.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @param errorMessage The error message
     * @param userId The user ID
     * @return The updated DocumentServiceStatusDTO
     */
    public DocumentServiceStatusDTO updateServiceStatusWithError(
        Long documentId,
        ServiceType serviceType,
        String errorMessage,
        String userId
    ) {
        log.error("Updating service status with error: documentId={}, serviceType={}, error={}",
            documentId, serviceType, errorMessage);

        DocumentServiceStatus serviceStatus = documentServiceStatusRepository
            .findByDocumentIdAndServiceTypeWithLock(documentId, serviceType)
            .orElseGet(() -> {
                DocumentServiceStatus newStatus = new DocumentServiceStatus();
                newStatus.setDocumentId(documentId);
                newStatus.setServiceType(serviceType);
                newStatus.setRetryCount(0);
                newStatus.setUpdatedDate(Instant.now());
                return newStatus;
            });

        serviceStatus.setStatus(ServiceStatus.FAILED);
        serviceStatus.setErrorMessage(errorMessage);
        serviceStatus.setRetryCount(serviceStatus.getRetryCount() != null ? serviceStatus.getRetryCount() + 1 : 1);
        serviceStatus.setUpdatedBy(userId);
        serviceStatus.setUpdatedDate(Instant.now());

        if (serviceStatus.getProcessingStartDate() != null && serviceStatus.getProcessingEndDate() == null) {
            serviceStatus.setProcessingEndDate(Instant.now());
            serviceStatus.setProcessingDuration(
                serviceStatus.getProcessingEndDate().toEpochMilli() - 
                serviceStatus.getProcessingStartDate().toEpochMilli()
            );
        }

        DocumentServiceStatus savedStatus = documentServiceStatusRepository.save(serviceStatus);
        publishServiceStatusEvent(savedStatus);

        return documentServiceStatusMapper.toDto(savedStatus);
    }

    /**
     * Mark service processing as started.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @param jobId Optional job ID
     * @param userId The user ID
     * @return The updated DocumentServiceStatusDTO
     */
    public DocumentServiceStatusDTO markProcessingStarted(
        Long documentId,
        ServiceType serviceType,
        String jobId,
        String userId
    ) {
        log.info("Marking processing started: documentId={}, serviceType={}, jobId={}",
            documentId, serviceType, jobId);

        DocumentServiceStatus serviceStatus = documentServiceStatusRepository
            .findByDocumentIdAndServiceTypeWithLock(documentId, serviceType)
            .orElseGet(() -> {
                DocumentServiceStatus newStatus = new DocumentServiceStatus();
                newStatus.setDocumentId(documentId);
                newStatus.setServiceType(serviceType);
                newStatus.setRetryCount(0);
                newStatus.setUpdatedDate(Instant.now());
                return newStatus;
            });

        serviceStatus.setStatus(ServiceStatus.IN_PROGRESS);
        serviceStatus.setProcessingStartDate(Instant.now());
        serviceStatus.setJobId(jobId);
        serviceStatus.setUpdatedBy(userId);
        serviceStatus.setUpdatedDate(Instant.now());

        DocumentServiceStatus savedStatus = documentServiceStatusRepository.save(serviceStatus);
        publishServiceStatusEvent(savedStatus);

        return documentServiceStatusMapper.toDto(savedStatus);
    }

    /**
     * Mark service processing as completed.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @param statusDetails Optional completion details
     * @param userId The user ID
     * @return The updated DocumentServiceStatusDTO
     */
    public DocumentServiceStatusDTO markProcessingCompleted(
        Long documentId,
        ServiceType serviceType,
        String statusDetails,
        String userId
    ) {
        log.info("Marking processing completed: documentId={}, serviceType={}",
            documentId, serviceType);

        DocumentServiceStatus serviceStatus = documentServiceStatusRepository
            .findByDocumentIdAndServiceTypeWithLock(documentId, serviceType)
            .orElseThrow(() -> new IllegalStateException(
                String.format("Service status not found: documentId=%d, serviceType=%s", documentId, serviceType)
            ));

        serviceStatus.setStatus(ServiceStatus.COMPLETED);
        serviceStatus.setStatusDetails(statusDetails);
        serviceStatus.setProcessingEndDate(Instant.now());
        serviceStatus.setUpdatedBy(userId);
        serviceStatus.setUpdatedDate(Instant.now());

        if (serviceStatus.getProcessingStartDate() != null) {
            serviceStatus.setProcessingDuration(
                serviceStatus.getProcessingEndDate().toEpochMilli() - 
                serviceStatus.getProcessingStartDate().toEpochMilli()
            );
        }

        DocumentServiceStatus savedStatus = documentServiceStatusRepository.save(serviceStatus);
        publishServiceStatusEvent(savedStatus);

        return documentServiceStatusMapper.toDto(savedStatus);
    }

    /**
     * Get service status for a document and service type.
     *
     * @param documentId The document ID
     * @param serviceType The service type
     * @return Optional DocumentServiceStatusDTO
     */
    @Transactional(readOnly = true)
    public Optional<DocumentServiceStatusDTO> getServiceStatus(Long documentId, ServiceType serviceType) {
        log.debug("Getting service status: documentId={}, serviceType={}", documentId, serviceType);
        return documentServiceStatusRepository
            .findByDocumentIdAndServiceType(documentId, serviceType)
            .map(documentServiceStatusMapper::toDto);
    }

    /**
     * Determine if status should be updated based on current state.
     * Implements state machine validation.
     *
     * @param serviceStatus The current service status
     * @param newStatus The proposed new status
     * @return true if update should proceed, false otherwise
     */
    private boolean shouldUpdateStatus(DocumentServiceStatus serviceStatus, ServiceStatus newStatus) {
        ServiceStatus currentStatus = serviceStatus.getStatus();
        
        if (currentStatus == null) {
            return true;
        }

        if (currentStatus == newStatus) {
            log.debug("Status unchanged, skipping update");
            return false;
        }

        if (currentStatus == ServiceStatus.COMPLETED && newStatus != ServiceStatus.RETRYING) {
            log.warn("Cannot change status from COMPLETED to {} (documentId={}, serviceType={})",
                newStatus, serviceStatus.getDocumentId(), serviceStatus.getServiceType());
            return false;
        }

        return true;
    }

    /**
     * Update service status fields.
     *
     * @param serviceStatus The service status entity
     * @param status The new status
     * @param statusDetails Status details
     * @param userId The user ID
     */
    private void updateServiceStatusFields(
        DocumentServiceStatus serviceStatus,
        ServiceStatus status,
        String statusDetails,
        String userId
    ) {
        serviceStatus.setStatus(status);
        serviceStatus.setStatusDetails(statusDetails);
        serviceStatus.setUpdatedBy(userId);
        serviceStatus.setUpdatedDate(Instant.now());
    }

    /**
     * Publish service status change event to Kafka.
     *
     * @param serviceStatus The service status entity
     */
    private void publishServiceStatusEvent(DocumentServiceStatus serviceStatus) {
        DocumentServiceStatusEvent event = new DocumentServiceStatusEvent(
            serviceStatus.getDocumentId(),
            serviceStatus.getServiceType().name(),
            serviceStatus.getStatus().name(),
            "documentService"
        );

        event.setStatusDetails(serviceStatus.getStatusDetails());
        event.setErrorMessage(serviceStatus.getErrorMessage());
        event.setRetryCount(serviceStatus.getRetryCount());
        event.setJobId(serviceStatus.getJobId());
        event.setProcessingStartDate(serviceStatus.getProcessingStartDate());
        event.setProcessingEndDate(serviceStatus.getProcessingEndDate());
        event.setProcessingDuration(serviceStatus.getProcessingDuration());

        documentEventPublisher.publishServiceStatusEvent(event);
    }
}
