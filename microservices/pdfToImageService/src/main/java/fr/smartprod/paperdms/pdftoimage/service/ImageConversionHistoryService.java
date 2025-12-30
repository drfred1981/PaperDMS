package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionHistoryRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionHistorySearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionHistoryDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionHistoryMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory}.
 */
@Service
@Transactional
public class ImageConversionHistoryService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionHistoryService.class);

    private final ImageConversionHistoryRepository imageConversionHistoryRepository;

    private final ImageConversionHistoryMapper imageConversionHistoryMapper;

    private final ImageConversionHistorySearchRepository imageConversionHistorySearchRepository;

    public ImageConversionHistoryService(
        ImageConversionHistoryRepository imageConversionHistoryRepository,
        ImageConversionHistoryMapper imageConversionHistoryMapper,
        ImageConversionHistorySearchRepository imageConversionHistorySearchRepository
    ) {
        this.imageConversionHistoryRepository = imageConversionHistoryRepository;
        this.imageConversionHistoryMapper = imageConversionHistoryMapper;
        this.imageConversionHistorySearchRepository = imageConversionHistorySearchRepository;
    }

    /**
     * Save a imageConversionHistory.
     *
     * @param imageConversionHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionHistoryDTO save(ImageConversionHistoryDTO imageConversionHistoryDTO) {
        LOG.debug("Request to save ImageConversionHistory : {}", imageConversionHistoryDTO);
        ImageConversionHistory imageConversionHistory = imageConversionHistoryMapper.toEntity(imageConversionHistoryDTO);
        imageConversionHistory = imageConversionHistoryRepository.save(imageConversionHistory);
        imageConversionHistorySearchRepository.index(imageConversionHistory);
        return imageConversionHistoryMapper.toDto(imageConversionHistory);
    }

    /**
     * Update a imageConversionHistory.
     *
     * @param imageConversionHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionHistoryDTO update(ImageConversionHistoryDTO imageConversionHistoryDTO) {
        LOG.debug("Request to update ImageConversionHistory : {}", imageConversionHistoryDTO);
        ImageConversionHistory imageConversionHistory = imageConversionHistoryMapper.toEntity(imageConversionHistoryDTO);
        imageConversionHistory = imageConversionHistoryRepository.save(imageConversionHistory);
        imageConversionHistorySearchRepository.index(imageConversionHistory);
        return imageConversionHistoryMapper.toDto(imageConversionHistory);
    }

    /**
     * Partially update a imageConversionHistory.
     *
     * @param imageConversionHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageConversionHistoryDTO> partialUpdate(ImageConversionHistoryDTO imageConversionHistoryDTO) {
        LOG.debug("Request to partially update ImageConversionHistory : {}", imageConversionHistoryDTO);

        return imageConversionHistoryRepository
            .findById(imageConversionHistoryDTO.getId())
            .map(existingImageConversionHistory -> {
                imageConversionHistoryMapper.partialUpdate(existingImageConversionHistory, imageConversionHistoryDTO);

                return existingImageConversionHistory;
            })
            .map(imageConversionHistoryRepository::save)
            .map(savedImageConversionHistory -> {
                imageConversionHistorySearchRepository.index(savedImageConversionHistory);
                return savedImageConversionHistory;
            })
            .map(imageConversionHistoryMapper::toDto);
    }

    /**
     * Get one imageConversionHistory by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageConversionHistoryDTO> findOne(Long id) {
        LOG.debug("Request to get ImageConversionHistory : {}", id);
        return imageConversionHistoryRepository.findById(id).map(imageConversionHistoryMapper::toDto);
    }

    /**
     * Delete the imageConversionHistory by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImageConversionHistory : {}", id);
        imageConversionHistoryRepository.deleteById(id);
        imageConversionHistorySearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imageConversionHistory corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionHistoryDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImageConversionHistories for query {}", query);
        return imageConversionHistorySearchRepository.search(query, pageable).map(imageConversionHistoryMapper::toDto);
    }
}
