package fr.smartprod.paperdms.pdftoimage.service;

import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.repository.ImagePdfConversionRequestRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImagePdfConversionRequestSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImagePdfConversionRequestMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest}.
 */
@Service
@Transactional
public class ImagePdfConversionRequestService {

    private static final Logger LOG = LoggerFactory.getLogger(ImagePdfConversionRequestService.class);

    private final ImagePdfConversionRequestRepository imagePdfConversionRequestRepository;

    private final ImagePdfConversionRequestMapper imagePdfConversionRequestMapper;

    private final ImagePdfConversionRequestSearchRepository imagePdfConversionRequestSearchRepository;

    public ImagePdfConversionRequestService(
        ImagePdfConversionRequestRepository imagePdfConversionRequestRepository,
        ImagePdfConversionRequestMapper imagePdfConversionRequestMapper,
        ImagePdfConversionRequestSearchRepository imagePdfConversionRequestSearchRepository
    ) {
        this.imagePdfConversionRequestRepository = imagePdfConversionRequestRepository;
        this.imagePdfConversionRequestMapper = imagePdfConversionRequestMapper;
        this.imagePdfConversionRequestSearchRepository = imagePdfConversionRequestSearchRepository;
    }

    /**
     * Save a imagePdfConversionRequest.
     *
     * @param imagePdfConversionRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagePdfConversionRequestDTO save(ImagePdfConversionRequestDTO imagePdfConversionRequestDTO) {
        LOG.debug("Request to save ImagePdfConversionRequest : {}", imagePdfConversionRequestDTO);
        ImagePdfConversionRequest imagePdfConversionRequest = imagePdfConversionRequestMapper.toEntity(imagePdfConversionRequestDTO);
        imagePdfConversionRequest = imagePdfConversionRequestRepository.save(imagePdfConversionRequest);
        imagePdfConversionRequestSearchRepository.index(imagePdfConversionRequest);
        return imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);
    }

    /**
     * Update a imagePdfConversionRequest.
     *
     * @param imagePdfConversionRequestDTO the entity to save.
     * @return the persisted entity.
     */
    public ImagePdfConversionRequestDTO update(ImagePdfConversionRequestDTO imagePdfConversionRequestDTO) {
        LOG.debug("Request to update ImagePdfConversionRequest : {}", imagePdfConversionRequestDTO);
        ImagePdfConversionRequest imagePdfConversionRequest = imagePdfConversionRequestMapper.toEntity(imagePdfConversionRequestDTO);
        imagePdfConversionRequest = imagePdfConversionRequestRepository.save(imagePdfConversionRequest);
        imagePdfConversionRequestSearchRepository.index(imagePdfConversionRequest);
        return imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);
    }

    /**
     * Partially update a imagePdfConversionRequest.
     *
     * @param imagePdfConversionRequestDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ImagePdfConversionRequestDTO> partialUpdate(ImagePdfConversionRequestDTO imagePdfConversionRequestDTO) {
        LOG.debug("Request to partially update ImagePdfConversionRequest : {}", imagePdfConversionRequestDTO);

        return imagePdfConversionRequestRepository
            .findById(imagePdfConversionRequestDTO.getId())
            .map(existingImagePdfConversionRequest -> {
                imagePdfConversionRequestMapper.partialUpdate(existingImagePdfConversionRequest, imagePdfConversionRequestDTO);

                return existingImagePdfConversionRequest;
            })
            .map(imagePdfConversionRequestRepository::save)
            .map(savedImagePdfConversionRequest -> {
                imagePdfConversionRequestSearchRepository.index(savedImagePdfConversionRequest);
                return savedImagePdfConversionRequest;
            })
            .map(imagePdfConversionRequestMapper::toDto);
    }

    /**
     * Get one imagePdfConversionRequest by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ImagePdfConversionRequestDTO> findOne(Long id) {
        LOG.debug("Request to get ImagePdfConversionRequest : {}", id);
        return imagePdfConversionRequestRepository.findById(id).map(imagePdfConversionRequestMapper::toDto);
    }

    /**
     * Delete the imagePdfConversionRequest by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ImagePdfConversionRequest : {}", id);
        imagePdfConversionRequestRepository.deleteById(id);
        imagePdfConversionRequestSearchRepository.deleteFromIndexById(id);
    }

    /**
     * Search for the imagePdfConversionRequest corresponding to the query.
     *
     * @param query the query of the search.
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ImagePdfConversionRequestDTO> search(String query, Pageable pageable) {
        LOG.debug("Request to search for a page of ImagePdfConversionRequests for query {}", query);
        return imagePdfConversionRequestSearchRepository.search(query, pageable).map(imagePdfConversionRequestMapper::toDto);
    }
}
