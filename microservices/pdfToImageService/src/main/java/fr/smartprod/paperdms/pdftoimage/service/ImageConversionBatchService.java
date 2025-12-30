package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionBatchRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionBatchSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionBatchMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch}.
 */
@Service
@Transactional
public class ImageConversionBatchService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionBatchService.class);

    private final ImageConversionBatchRepository imageConversionBatchRepository;

    private final ImageConversionBatchMapper imageConversionBatchMapper;

    private final ImageConversionBatchSearchRepository imageConversionBatchSearchRepository;

    public ImageConversionBatchService(
        ImageConversionBatchRepository imageConversionBatchRepository,
        ImageConversionBatchMapper imageConversionBatchMapper,
        ImageConversionBatchSearchRepository imageConversionBatchSearchRepository
    ) {
        this.imageConversionBatchRepository = imageConversionBatchRepository;
        this.imageConversionBatchMapper = imageConversionBatchMapper;
        this.imageConversionBatchSearchRepository = imageConversionBatchSearchRepository;
    }

    /**
     * Save a imageConversionBatch.
     *
     * @param imageConversionBatchDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionBatchDTO save(ImageConversionBatchDTO imageConversionBatchDTO) {
        LOG.debug("Request to save ImageConversionBatch : {}", imageConversionBatchDTO);
        ImageConversionBatch imageConversionBatch = imageConversionBatchMapper.toEntity(imageConversionBatchDTO);
        imageConversionBatch = imageConversionBatchRepository.save(imageConversionBatch);
        imageConversionBatchSearchRepository.index(imageConversionBatch);
        return imageConversionBatchMapper.toDto(imageConversionBatch);
    }

    /**
     * Update a imageConversionBatch.
     *
     * @param imageConversionBatchDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionBatchDTO update(ImageConversionBatchDTO imageConversionBatchDTO) {
        LOG.debug("Request to update ImageConversionBatch : {}", imageConversionBatchDTO);
        ImageConversionBatch imageConversionBatch = imageConversionBatchMapper.toEntity(imageConversionBatchDTO);
        imageConversionBatch = imageConversionBatchRepository.save(imageConversionBatch);
        imageConversionBatchSearchRepository.index(imageConversionBatch);
        return imageConversionBatchMapper.toDto(imageConversionBatch);
    }

    /**
     * Partially update a imageConversionBatch.
     *
     * @param imageConversionBatchDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageConversionBatchDTO> partialUpdate(ImageConversionBatchDTO imageConversionBatchDTO) {
        LOG.debug("Request to partially update ImageConversionBatch : {}", imageConversionBatchDTO);

        return imageConversionBatchRepository
            .findById(imageConversionBatchDTO.getId())
            .map(existingImageConversionBatch -> {
                imageConversionBatchMapper.partialUpdate(existingImageConversionBatch, imageConversionBatchDTO);

                return existingImageConversionBatch;
            })
            .map(imageConversionBatchRepository::save)
            .map(savedImageConversionBatch -> {
                imageConversionBatchSearchRepository.index(savedImageConversionBatch);
                return savedImageConversionBatch;
            })
            .map(imageConversionBatchMapper::toDto);
    }

    /**
     * Get one imageConversionBatch by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageConversionBatchDTO> findOne(Long id) {
        LOG.debug("Request to get ImageConversionBatch : {}", id);
        return imageConversionBatchRepository.findById(id).map(imageConversionBatchMapper::toDto);
    }

    /**
     * Delete the imageConversionBatch by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImageConversionBatch : {}", id);
        imageConversionBatchRepository.deleteById(id);
        imageConversionBatchSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imageConversionBatch corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionBatchDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImageConversionBatches for query {}", query);
        return imageConversionBatchSearchRepository.search(query, pageable).map(imageConversionBatchMapper::toDto);
    }
}
