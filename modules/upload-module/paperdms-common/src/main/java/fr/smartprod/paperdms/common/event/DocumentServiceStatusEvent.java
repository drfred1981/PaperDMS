package fr.smartprod.paperdms.common.event;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;

/**
 * Event published when a document's service status changes.
 * This tracks the processing status across different microservices.
 */
public class DocumentServiceStatusEvent extends DocumentEvent {
    
    @JsonProperty("serviceType")
    private String serviceType;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("statusDetails")
    private String statusDetails;
    
    @JsonProperty("errorMessage")
    private String errorMessage;
    
    @JsonProperty("retryCount")
    private Integer retryCount;
    
    @JsonProperty("jobId")
    private String jobId;
    
    @JsonProperty("processingStartDate")
    private Instant processingStartDate;
    
    @JsonProperty("processingEndDate")
    private Instant processingEndDate;
    
    @JsonProperty("processingDuration")
    private Long processingDuration;

    /**
     * Default constructor.
     */
    public DocumentServiceStatusEvent() {
        super();
    }

    /**
     * Constructor with required fields.
     *
     * @param documentId The ID of the document
     * @param serviceType The type of service
     * @param status The new status
     * @param sourceService The service that generated this event
     */
    public DocumentServiceStatusEvent(Long documentId, String serviceType, String status, String sourceService) {
        super(DocumentEventType.DOCUMENT_SERVICE_STATUS_CHANGED, documentId, sourceService);
        this.serviceType = serviceType;
        this.status = status;
    }

    // Getters and Setters

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusDetails() {
        return statusDetails;
    }

    public void setStatusDetails(String statusDetails) {
        this.statusDetails = statusDetails;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(Integer retryCount) {
        this.retryCount = retryCount;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public Instant getProcessingStartDate() {
        return processingStartDate;
    }

    public void setProcessingStartDate(Instant processingStartDate) {
        this.processingStartDate = processingStartDate;
    }

    public Instant getProcessingEndDate() {
        return processingEndDate;
    }

    public void setProcessingEndDate(Instant processingEndDate) {
        this.processingEndDate = processingEndDate;
    }

    public Long getProcessingDuration() {
        return processingDuration;
    }

    public void setProcessingDuration(Long processingDuration) {
        this.processingDuration = processingDuration;
    }

    @Override
    public String toString() {
        return "DocumentServiceStatusEvent{" +
                "serviceType='" + serviceType + '\'' +
                ", status='" + status + '\'' +
                ", statusDetails='" + statusDetails + '\'' +
                ", errorMessage='" + errorMessage + '\'' +
                ", retryCount=" + retryCount +
                ", jobId='" + jobId + '\'' +
                ", processingDuration=" + processingDuration +
                "} " + super.toString();
    }
}
