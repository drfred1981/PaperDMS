package fr.smartprod.paperdms.similarity.service;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentFingerprintSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentFingerprintDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentFingerprintMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint}.
 */
@Service
@Transactional
public class SimilarityDocumentFingerprintService {

    private static final Logger LOG = LoggerFactory.getLogger(SimilarityDocumentFingerprintService.class);

    private final SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository;

    private final SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper;

    private final SimilarityDocumentFingerprintSearchRepository similarityDocumentFingerprintSearchRepository;

    public SimilarityDocumentFingerprintService(
        SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository,
        SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper,
        SimilarityDocumentFingerprintSearchRepository similarityDocumentFingerprintSearchRepository
    ) {
        this.similarityDocumentFingerprintRepository = similarityDocumentFingerprintRepository;
        this.similarityDocumentFingerprintMapper = similarityDocumentFingerprintMapper;
        this.similarityDocumentFingerprintSearchRepository = similarityDocumentFingerprintSearchRepository;
    }

    /**
     * Save a similarityDocumentFingerprint.
     *
     * @param similarityDocumentFingerprintDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityDocumentFingerprintDTO save(SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO) {
        LOG.debug("Request to save SimilarityDocumentFingerprint : {}", similarityDocumentFingerprintDTO);
        SimilarityDocumentFingerprint similarityDocumentFingerprint = similarityDocumentFingerprintMapper.toEntity(
            similarityDocumentFingerprintDTO
        );
        similarityDocumentFingerprint = similarityDocumentFingerprintRepository.save(similarityDocumentFingerprint);
        similarityDocumentFingerprintSearchRepository.index(similarityDocumentFingerprint);
        return similarityDocumentFingerprintMapper.toDto(similarityDocumentFingerprint);
    }

    /**
     * Update a similarityDocumentFingerprint.
     *
     * @param similarityDocumentFingerprintDTO the entity to save.
     * @return the persisted entity.
     */
    public SimilarityDocumentFingerprintDTO update(SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO) {
        LOG.debug("Request to update SimilarityDocumentFingerprint : {}", similarityDocumentFingerprintDTO);
        SimilarityDocumentFingerprint similarityDocumentFingerprint = similarityDocumentFingerprintMapper.toEntity(
            similarityDocumentFingerprintDTO
        );
        similarityDocumentFingerprint = similarityDocumentFingerprintRepository.save(similarityDocumentFingerprint);
        similarityDocumentFingerprintSearchRepository.index(similarityDocumentFingerprint);
        return similarityDocumentFingerprintMapper.toDto(similarityDocumentFingerprint);
    }

    /**
     * Partially update a similarityDocumentFingerprint.
     *
     * @param similarityDocumentFingerprintDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<SimilarityDocumentFingerprintDTO> partialUpdate(SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO) {
        LOG.debug("Request to partially update SimilarityDocumentFingerprint : {}", similarityDocumentFingerprintDTO);

        return similarityDocumentFingerprintRepository
            .findById(similarityDocumentFingerprintDTO.getId())
            .map(existingSimilarityDocumentFingerprint -> {
                similarityDocumentFingerprintMapper.partialUpdate(existingSimilarityDocumentFingerprint, similarityDocumentFingerprintDTO);

                return existingSimilarityDocumentFingerprint;
            })
            .map(similarityDocumentFingerprintRepository::save)
            .map(savedSimilarityDocumentFingerprint -> {
                similarityDocumentFingerprintSearchRepository.index(savedSimilarityDocumentFingerprint);
                return savedSimilarityDocumentFingerprint;
            })
            .map(similarityDocumentFingerprintMapper::toDto);
    }

    /**
     * Get one similarityDocumentFingerprint by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SimilarityDocumentFingerprintDTO> findOne(Long id) {
        LOG.debug("Request to get SimilarityDocumentFingerprint : {}", id);
        return similarityDocumentFingerprintRepository.findById(id).map(similarityDocumentFingerprintMapper::toDto);
    }

    /**
     * Delete the similarityDocumentFingerprint by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SimilarityDocumentFingerprint : {}", id);
        similarityDocumentFingerprintRepository.deleteById(id);
        similarityDocumentFingerprintSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the similarityDocumentFingerprint corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SimilarityDocumentFingerprintDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of SimilarityDocumentFingerprints for query {}", query);
        return similarityDocumentFingerprintSearchRepository.search(query, pageable).map(similarityDocumentFingerprintMapper::toDto);
    }
}
