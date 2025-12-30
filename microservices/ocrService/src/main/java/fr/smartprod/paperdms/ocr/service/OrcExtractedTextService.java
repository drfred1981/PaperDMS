package fr.smartprod.paperdms.ocr.service;

import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import fr.smartprod.paperdms.ocr.repository.OrcExtractedTextRepository;
import fr.smartprod.paperdms.ocr.repository.search.OrcExtractedTextSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OrcExtractedTextDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OrcExtractedTextMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.ocr.domain.OrcExtractedText}.
 */
@Service
@Transactional
public class OrcExtractedTextService {

    private static final Logger LOG = LoggerFactory.getLogger(OrcExtractedTextService.class);

    private final OrcExtractedTextRepository orcExtractedTextRepository;

    private final OrcExtractedTextMapper orcExtractedTextMapper;

    private final OrcExtractedTextSearchRepository orcExtractedTextSearchRepository;

    public OrcExtractedTextService(
        OrcExtractedTextRepository orcExtractedTextRepository,
        OrcExtractedTextMapper orcExtractedTextMapper,
        OrcExtractedTextSearchRepository orcExtractedTextSearchRepository
    ) {
        this.orcExtractedTextRepository = orcExtractedTextRepository;
        this.orcExtractedTextMapper = orcExtractedTextMapper;
        this.orcExtractedTextSearchRepository = orcExtractedTextSearchRepository;
    }

    /**
     * Save a orcExtractedText.
     *
     * @param orcExtractedTextDTO the entity to save.
     * @return the persisted entity.
     */
    public OrcExtractedTextDTO save(OrcExtractedTextDTO orcExtractedTextDTO) {
        LOG.debug("Request to save OrcExtractedText : {}", orcExtractedTextDTO);
        OrcExtractedText orcExtractedText = orcExtractedTextMapper.toEntity(orcExtractedTextDTO);
        orcExtractedText = orcExtractedTextRepository.save(orcExtractedText);
        orcExtractedTextSearchRepository.index(orcExtractedText);
        return orcExtractedTextMapper.toDto(orcExtractedText);
    }

    /**
     * Update a orcExtractedText.
     *
     * @param orcExtractedTextDTO the entity to save.
     * @return the persisted entity.
     */
    public OrcExtractedTextDTO update(OrcExtractedTextDTO orcExtractedTextDTO) {
        LOG.debug("Request to update OrcExtractedText : {}", orcExtractedTextDTO);
        OrcExtractedText orcExtractedText = orcExtractedTextMapper.toEntity(orcExtractedTextDTO);
        orcExtractedText = orcExtractedTextRepository.save(orcExtractedText);
        orcExtractedTextSearchRepository.index(orcExtractedText);
        return orcExtractedTextMapper.toDto(orcExtractedText);
    }

    /**
     * Partially update a orcExtractedText.
     *
     * @param orcExtractedTextDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<OrcExtractedTextDTO> partialUpdate(OrcExtractedTextDTO orcExtractedTextDTO) {
        LOG.debug("Request to partially update OrcExtractedText : {}", orcExtractedTextDTO);

        return orcExtractedTextRepository
            .findById(orcExtractedTextDTO.getId())
            .map(existingOrcExtractedText -> {
                orcExtractedTextMapper.partialUpdate(existingOrcExtractedText, orcExtractedTextDTO);

                return existingOrcExtractedText;
            })
            .map(orcExtractedTextRepository::save)
            .map(savedOrcExtractedText -> {
                orcExtractedTextSearchRepository.index(savedOrcExtractedText);
                return savedOrcExtractedText;
            })
            .map(orcExtractedTextMapper::toDto);
    }

    /**
     * Get one orcExtractedText by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<OrcExtractedTextDTO> findOne(Long id) {
        LOG.debug("Request to get OrcExtractedText : {}", id);
        return orcExtractedTextRepository.findById(id).map(orcExtractedTextMapper::toDto);
    }

    /**
     * Delete the orcExtractedText by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete OrcExtractedText : {}", id);
        orcExtractedTextRepository.deleteById(id);
        orcExtractedTextSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the orcExtractedText corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<OrcExtractedTextDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of OrcExtractedTexts for query {}", query);
        return orcExtractedTextSearchRepository.search(query, pageable).map(orcExtractedTextMapper::toDto);
    }
}
