package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import fr.smartprod.paperdms.pdftoimage.repository.ImageGeneratedImageRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageGeneratedImageSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageGeneratedImageDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageGeneratedImageMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage}.
 */
@Service
@Transactional
public class ImageGeneratedImageService {

    private static final Logger LOG = LoggerFactory.getLogger(ImageGeneratedImageService.class);

    private final ImageGeneratedImageRepository imageGeneratedImageRepository;

    private final ImageGeneratedImageMapper imageGeneratedImageMapper;

    private final ImageGeneratedImageSearchRepository imageGeneratedImageSearchRepository;

    public ImageGeneratedImageService(
        ImageGeneratedImageRepository imageGeneratedImageRepository,
        ImageGeneratedImageMapper imageGeneratedImageMapper,
        ImageGeneratedImageSearchRepository imageGeneratedImageSearchRepository
    ) {
        this.imageGeneratedImageRepository = imageGeneratedImageRepository;
        this.imageGeneratedImageMapper = imageGeneratedImageMapper;
        this.imageGeneratedImageSearchRepository = imageGeneratedImageSearchRepository;
    }

    /**
     * Save a imageGeneratedImage.
     *
     * @param imageGeneratedImageDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageGeneratedImageDTO save(ImageGeneratedImageDTO imageGeneratedImageDTO) {
        LOG.debug("Request to save ImageGeneratedImage : {}", imageGeneratedImageDTO);
        ImageGeneratedImage imageGeneratedImage = imageGeneratedImageMapper.toEntity(imageGeneratedImageDTO);
        imageGeneratedImage = imageGeneratedImageRepository.save(imageGeneratedImage);
        imageGeneratedImageSearchRepository.index(imageGeneratedImage);
        return imageGeneratedImageMapper.toDto(imageGeneratedImage);
    }

    /**
     * Update a imageGeneratedImage.
     *
     * @param imageGeneratedImageDTO the entity to save.
     * @return the persisted entity.
     */
    public ImageGeneratedImageDTO update(ImageGeneratedImageDTO imageGeneratedImageDTO) {
        LOG.debug("Request to update ImageGeneratedImage : {}", imageGeneratedImageDTO);
        ImageGeneratedImage imageGeneratedImage = imageGeneratedImageMapper.toEntity(imageGeneratedImageDTO);
        imageGeneratedImage = imageGeneratedImageRepository.save(imageGeneratedImage);
        imageGeneratedImageSearchRepository.index(imageGeneratedImage);
        return imageGeneratedImageMapper.toDto(imageGeneratedImage);
    }

    /**
     * Partially update a imageGeneratedImage.
     *
     * @param imageGeneratedImageDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImageGeneratedImageDTO> partialUpdate(ImageGeneratedImageDTO imageGeneratedImageDTO) {
        LOG.debug("Request to partially update ImageGeneratedImage : {}", imageGeneratedImageDTO);

        return imageGeneratedImageRepository
            .findById(imageGeneratedImageDTO.getId())
            .map(existingImageGeneratedImage -> {
                imageGeneratedImageMapper.partialUpdate(existingImageGeneratedImage, imageGeneratedImageDTO);

                return existingImageGeneratedImage;
            })
            .map(imageGeneratedImageRepository::save)
            .map(savedImageGeneratedImage -> {
                imageGeneratedImageSearchRepository.index(savedImageGeneratedImage);
                return savedImageGeneratedImage;
            })
            .map(imageGeneratedImageMapper::toDto);
    }

    /**
     * Get one imageGeneratedImage by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImageGeneratedImageDTO> findOne(Long id) {
        LOG.debug("Request to get ImageGeneratedImage : {}", id);
        return imageGeneratedImageRepository.findById(id).map(imageGeneratedImageMapper::toDto);
    }

    /**
     * Delete the imageGeneratedImage by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImageGeneratedImage : {}", id);
        imageGeneratedImageRepository.deleteById(id);
        imageGeneratedImageSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imageGeneratedImage corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImageGeneratedImageDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImageGeneratedImages for query {}", query);
        return imageGeneratedImageSearchRepository.search(query, pageable).map(imageGeneratedImageMapper::toDto);
    }
}
