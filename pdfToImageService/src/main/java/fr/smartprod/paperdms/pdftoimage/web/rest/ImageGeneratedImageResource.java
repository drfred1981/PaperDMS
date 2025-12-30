package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImageGeneratedImageRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImageGeneratedImageQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImageGeneratedImageService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageGeneratedImageCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageGeneratedImageDTO;
import fr.smartprod.paperdms.pdftoimage.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.pdftoimage.web.rest.errors.ElasticsearchExceptionMapper;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage}.
 */
@RestController
@RequestMapping("/api/image-generated-images")
public class ImageGeneratedImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageGeneratedImageResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImageGeneratedImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageGeneratedImageService imageGeneratedImageService;

    private final ImageGeneratedImageRepository imageGeneratedImageRepository;

    private final ImageGeneratedImageQueryService imageGeneratedImageQueryService;

    public ImageGeneratedImageResource(
        ImageGeneratedImageService imageGeneratedImageService,
        ImageGeneratedImageRepository imageGeneratedImageRepository,
        ImageGeneratedImageQueryService imageGeneratedImageQueryService
    ) {
        this.imageGeneratedImageService = imageGeneratedImageService;
        this.imageGeneratedImageRepository = imageGeneratedImageRepository;
        this.imageGeneratedImageQueryService = imageGeneratedImageQueryService;
    }

    /**
     * {@code POST  /image-generated-images} : Create a new imageGeneratedImage.
     *
     * @param imageGeneratedImageDTO the imageGeneratedImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageGeneratedImageDTO, or with status {@code 400 (Bad Request)} if the imageGeneratedImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageGeneratedImageDTO> createImageGeneratedImage(
        @Valid @RequestBody ImageGeneratedImageDTO imageGeneratedImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImageGeneratedImage : {}", imageGeneratedImageDTO);
        if (imageGeneratedImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageGeneratedImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageGeneratedImageDTO = imageGeneratedImageService.save(imageGeneratedImageDTO);
        return ResponseEntity.created(new URI("/api/image-generated-images/" + imageGeneratedImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageGeneratedImageDTO.getId().toString()))
            .body(imageGeneratedImageDTO);
    }

    /**
     * {@code PUT  /image-generated-images/:id} : Updates an existing imageGeneratedImage.
     *
     * @param id the id of the imageGeneratedImageDTO to save.
     * @param imageGeneratedImageDTO the imageGeneratedImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageGeneratedImageDTO,
     * or with status {@code 400 (Bad Request)} if the imageGeneratedImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageGeneratedImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageGeneratedImageDTO> updateImageGeneratedImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageGeneratedImageDTO imageGeneratedImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageGeneratedImage : {}, {}", id, imageGeneratedImageDTO);
        if (imageGeneratedImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageGeneratedImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageGeneratedImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageGeneratedImageDTO = imageGeneratedImageService.update(imageGeneratedImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageGeneratedImageDTO.getId().toString()))
            .body(imageGeneratedImageDTO);
    }

    /**
     * {@code PATCH  /image-generated-images/:id} : Partial updates given fields of an existing imageGeneratedImage, field will ignore if it is null
     *
     * @param id the id of the imageGeneratedImageDTO to save.
     * @param imageGeneratedImageDTO the imageGeneratedImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageGeneratedImageDTO,
     * or with status {@code 400 (Bad Request)} if the imageGeneratedImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageGeneratedImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageGeneratedImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageGeneratedImageDTO> partialUpdateImageGeneratedImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageGeneratedImageDTO imageGeneratedImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageGeneratedImage partially : {}, {}", id, imageGeneratedImageDTO);
        if (imageGeneratedImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageGeneratedImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageGeneratedImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageGeneratedImageDTO> result = imageGeneratedImageService.partialUpdate(imageGeneratedImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageGeneratedImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-generated-images} : get all the imageGeneratedImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageGeneratedImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageGeneratedImageDTO>> getAllImageGeneratedImages(
        ImageGeneratedImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageGeneratedImages by criteria: {}", criteria);

        Page<ImageGeneratedImageDTO> page = imageGeneratedImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-generated-images/count} : count all the imageGeneratedImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageGeneratedImages(ImageGeneratedImageCriteria criteria) {
        LOG.debug("REST request to count ImageGeneratedImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageGeneratedImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-generated-images/:id} : get the "id" imageGeneratedImage.
     *
     * @param id the id of the imageGeneratedImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageGeneratedImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageGeneratedImageDTO> getImageGeneratedImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageGeneratedImage : {}", id);
        Optional<ImageGeneratedImageDTO> imageGeneratedImageDTO = imageGeneratedImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageGeneratedImageDTO);
    }

    /**
     * {@code DELETE  /image-generated-images/:id} : delete the "id" imageGeneratedImage.
     *
     * @param id the id of the imageGeneratedImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageGeneratedImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageGeneratedImage : {}", id);
        imageGeneratedImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-generated-images/_search?query=:query} : search for the imageGeneratedImage corresponding
     * to the query.
     *
     * @param query the query of the imageGeneratedImage search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImageGeneratedImageDTO>> searchImageGeneratedImages(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImageGeneratedImages for query {}", query);
        try {
            Page<ImageGeneratedImageDTO> page = imageGeneratedImageService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
