package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionConfigRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionConfigSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionConfigDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionConfigMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig}.
 */
@Service
@Transactional
public class ImageConversionConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionConfigService.class);

    private final ImageConversionConfigRepository imageConversionConfigRepository;

    private final ImageConversionConfigMapper imageConversionConfigMapper;

    private final ImageConversionConfigSearchRepository imageConversionConfigSearchRepository;

    public ImageConversionConfigService(
        ImageConversionConfigRepository imageConversionConfigRepository,
        ImageConversionConfigMapper imageConversionConfigMapper,
        ImageConversionConfigSearchRepository imageConversionConfigSearchRepository
    ) {
        this.imageConversionConfigRepository = imageConversionConfigRepository;
        this.imageConversionConfigMapper = imageConversionConfigMapper;
        this.imageConversionConfigSearchRepository = imageConversionConfigSearchRepository;
    }

    /**
     * Save a imageConversionConfig.
     *
     * @param imageConversionConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionConfigDTO save(ImageConversionConfigDTO imageConversionConfigDTO) {
        LOG.debug("Request to save ImageConversionConfig : {}", imageConversionConfigDTO);
        ImageConversionConfig imageConversionConfig = imageConversionConfigMapper.toEntity(imageConversionConfigDTO);
        imageConversionConfig = imageConversionConfigRepository.save(imageConversionConfig);
        imageConversionConfigSearchRepository.index(imageConversionConfig);
        return imageConversionConfigMapper.toDto(imageConversionConfig);
    }

    /**
     * Update a imageConversionConfig.
     *
     * @param imageConversionConfigDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageConversionConfigDTO update(ImageConversionConfigDTO imageConversionConfigDTO) {
        LOG.debug("Request to update ImageConversionConfig : {}", imageConversionConfigDTO);
        ImageConversionConfig imageConversionConfig = imageConversionConfigMapper.toEntity(imageConversionConfigDTO);
        imageConversionConfig = imageConversionConfigRepository.save(imageConversionConfig);
        imageConversionConfigSearchRepository.index(imageConversionConfig);
        return imageConversionConfigMapper.toDto(imageConversionConfig);
    }

    /**
     * Partially update a imageConversionConfig.
     *
     * @param imageConversionConfigDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageConversionConfigDTO> partialUpdate(ImageConversionConfigDTO imageConversionConfigDTO) {
        LOG.debug("Request to partially update ImageConversionConfig : {}", imageConversionConfigDTO);

        return imageConversionConfigRepository
            .findById(imageConversionConfigDTO.getId())
            .map(existingImageConversionConfig -> {
                imageConversionConfigMapper.partialUpdate(existingImageConversionConfig, imageConversionConfigDTO);

                return existingImageConversionConfig;
            })
            .map(imageConversionConfigRepository::save)
            .map(savedImageConversionConfig -> {
                imageConversionConfigSearchRepository.index(savedImageConversionConfig);
                return savedImageConversionConfig;
            })
            .map(imageConversionConfigMapper::toDto);
    }

    /**
     * Get one imageConversionConfig by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageConversionConfigDTO> findOne(Long id) {
        LOG.debug("Request to get ImageConversionConfig : {}", id);
        return imageConversionConfigRepository.findById(id).map(imageConversionConfigMapper::toDto);
    }

    /**
     * Delete the imageConversionConfig by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImageConversionConfig : {}", id);
        imageConversionConfigRepository.deleteById(id);
        imageConversionConfigSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imageConversionConfig corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageConversionConfigDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImageConversionConfigs for query {}", query);
        return imageConversionConfigSearchRepository.search(query, pageable).map(imageConversionConfigMapper::toDto);
    }
}
