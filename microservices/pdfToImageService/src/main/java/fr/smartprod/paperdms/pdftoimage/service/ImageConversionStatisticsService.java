package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionStatisticsRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionStatisticsSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionStatisticsDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionStatisticsMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics}.
 */
@Service
@Transactional
public class ImageConversionStatisticsService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionStatisticsService.class);

    private final ImageConversionStatisticsRepository imageConversionStatisticsRepository;

    private final ImageConversionStatisticsMapper imageConversionStatisticsMapper;

    private final ImageConversionStatisticsSearchRepository imageConversionStatisticsSearchRepository;

    public ImageConversionStatisticsService(
        ImageConversionStatisticsRepository imageConversionStatisticsRepository,
        ImageConversionStatisticsMapper imageConversionStatisticsMapper,
        ImageConversionStatisticsSearchRepository imageConversionStatisticsSearchRepository
    ) {
        this.imageConversionStatisticsRepository = imageConversionStatisticsRepository;
        this.imageConversionStatisticsMapper = imageConversionStatisticsMapper;
        this.imageConversionStatisticsSearchRepository = imageConversionStatisticsSearchRepository;
    }

    /**
     * Save a imageConversionStatistics.
     *
     * @param imageConversionStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionStatisticsDTO save(ImageConversionStatisticsDTO imageConversionStatisticsDTO) {
        LOG.debug("Request to save ImageConversionStatistics : {}", imageConversionStatisticsDTO);
        ImageConversionStatistics imageConversionStatistics = imageConversionStatisticsMapper.toEntity(imageConversionStatisticsDTO);
        imageConversionStatistics = imageConversionStatisticsRepository.save(imageConversionStatistics);
        imageConversionStatisticsSearchRepository.index(imageConversionStatistics);
        return imageConversionStatisticsMapper.toDto(imageConversionStatistics);
    }

    /**
     * Update a imageConversionStatistics.
     *
     * @param imageConversionStatisticsDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionStatisticsDTO update(ImageConversionStatisticsDTO imageConversionStatisticsDTO) {
        LOG.debug("Request to update ImageConversionStatistics : {}", imageConversionStatisticsDTO);
        ImageConversionStatistics imageConversionStatistics = imageConversionStatisticsMapper.toEntity(imageConversionStatisticsDTO);
        imageConversionStatistics = imageConversionStatisticsRepository.save(imageConversionStatistics);
        imageConversionStatisticsSearchRepository.index(imageConversionStatistics);
        return imageConversionStatisticsMapper.toDto(imageConversionStatistics);
    }

    /**
     * Partially update a imageConversionStatistics.
     *
     * @param imageConversionStatisticsDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageConversionStatisticsDTO> partialUpdate(ImageConversionStatisticsDTO imageConversionStatisticsDTO) {
        LOG.debug("Request to partially update ImageConversionStatistics : {}", imageConversionStatisticsDTO);

        return imageConversionStatisticsRepository
            .findById(imageConversionStatisticsDTO.getId())
            .map(existingImageConversionStatistics -> {
                imageConversionStatisticsMapper.partialUpdate(existingImageConversionStatistics, imageConversionStatisticsDTO);

                return existingImageConversionStatistics;
            })
            .map(imageConversionStatisticsRepository::save)
            .map(savedImageConversionStatistics -> {
                imageConversionStatisticsSearchRepository.index(savedImageConversionStatistics);
                return savedImageConversionStatistics;
            })
            .map(imageConversionStatisticsMapper::toDto);
    }

    /**
     * Get one imageConversionStatistics by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageConversionStatisticsDTO> findOne(Long id) {
        LOG.debug("Request to get ImageConversionStatistics : {}", id);
        return imageConversionStatisticsRepository.findById(id).map(imageConversionStatisticsMapper::toDto);
    }

    /**
     * Delete the imageConversionStatistics by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImageConversionStatistics : {}", id);
        imageConversionStatisticsRepository.deleteById(id);
        imageConversionStatisticsSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imageConversionStatistics corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionStatisticsDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImageConversionStatistics for query {}", query);
        return imageConversionStatisticsSearchRepository.search(query, pageable).map(imageConversionStatisticsMapper::toDto);
    }
}
