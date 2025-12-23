package fr.smartprod.paperdms.document.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.repository.DocumentServiceStatusUploadRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentServiceStatusSearchRepository;
import fr.smartprod.paperdms.document.service.DocumentServiceStatusService;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentServiceStatusMapper;

/**
 * Service Implementation for managing
 * {@link fr.smartprod.paperdms.document.domain.DocumentServiceStatus}.
 */
@Service
@Transactional
public class DocumentServiceStatusServiceImpl implements DocumentServiceStatusService {

	private static final Logger LOG = LoggerFactory.getLogger(DocumentServiceStatusServiceImpl.class);

	private final DocumentServiceStatusUploadRepository documentServiceStatusRepository;

	private final DocumentServiceStatusMapper documentServiceStatusMapper;

	private final DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository;

	public DocumentServiceStatusServiceImpl(DocumentServiceStatusUploadRepository documentServiceStatusRepository,
			DocumentServiceStatusMapper documentServiceStatusMapper,
			DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository) {
		this.documentServiceStatusRepository = documentServiceStatusRepository;
		this.documentServiceStatusMapper = documentServiceStatusMapper;
		this.documentServiceStatusSearchRepository = documentServiceStatusSearchRepository;
	}

	@Override
	public DocumentServiceStatusDTO save(DocumentServiceStatusDTO documentServiceStatusDTO) {
		LOG.debug("Request to save DocumentServiceStatus : {}", documentServiceStatusDTO);
		DocumentServiceStatus documentServiceStatus = documentServiceStatusMapper.toEntity(documentServiceStatusDTO);
		documentServiceStatus = documentServiceStatusRepository.save(documentServiceStatus);
		documentServiceStatusSearchRepository.index(documentServiceStatus);
		return documentServiceStatusMapper.toDto(documentServiceStatus);
	}

	@Override
	public DocumentServiceStatusDTO update(DocumentServiceStatusDTO documentServiceStatusDTO) {
		LOG.debug("Request to update DocumentServiceStatus : {}", documentServiceStatusDTO);
		DocumentServiceStatus documentServiceStatus = documentServiceStatusMapper.toEntity(documentServiceStatusDTO);
		documentServiceStatus = documentServiceStatusRepository.save(documentServiceStatus);
		documentServiceStatusSearchRepository.index(documentServiceStatus);
		return documentServiceStatusMapper.toDto(documentServiceStatus);
	}

	@Override
	public Optional<DocumentServiceStatusDTO> partialUpdate(DocumentServiceStatusDTO documentServiceStatusDTO) {
		LOG.debug("Request to partially update DocumentServiceStatus : {}", documentServiceStatusDTO);

		return documentServiceStatusRepository.findById(documentServiceStatusDTO.getId())
				.map(existingDocumentServiceStatus -> {
					documentServiceStatusMapper.partialUpdate(existingDocumentServiceStatus, documentServiceStatusDTO);

					return existingDocumentServiceStatus;
				}).map(documentServiceStatusRepository::save).map(savedDocumentServiceStatus -> {
					documentServiceStatusSearchRepository.index(savedDocumentServiceStatus);
					return savedDocumentServiceStatus;
				}).map(documentServiceStatusMapper::toDto);
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<DocumentServiceStatusDTO> findOne(Long id) {
		LOG.debug("Request to get DocumentServiceStatus : {}", id);
		return documentServiceStatusRepository.findById(id).map(documentServiceStatusMapper::toDto);
	}

	@Override
	public void delete(Long id) {
		LOG.debug("Request to delete DocumentServiceStatus : {}", id);
		documentServiceStatusRepository.deleteById(id);
		documentServiceStatusSearchRepository.deleteFromIndexById(id);
	}

	@Override
	@Transactional(readOnly = true)
	public Page<DocumentServiceStatusDTO> search(String query, Pageable pageable) {
		LOG.debug("Request to search for a page of DocumentServiceStatuses for query {}", query);
		return documentServiceStatusSearchRepository.search(query, pageable).map(documentServiceStatusMapper::toDto);
	}

}
