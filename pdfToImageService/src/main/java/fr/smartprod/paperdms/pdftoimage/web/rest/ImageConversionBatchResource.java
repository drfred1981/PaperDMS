package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionBatchRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionBatchQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionBatchService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionBatchCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch}.
 */
@RestController
@RequestMapping("/api/image-conversion-batches")
public class ImageConversionBatchResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionBatchResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImageConversionBatch";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageConversionBatchService imageConversionBatchService;

    private final ImageConversionBatchRepository imageConversionBatchRepository;

    private final ImageConversionBatchQueryService imageConversionBatchQueryService;

    public ImageConversionBatchResource(
        ImageConversionBatchService imageConversionBatchService,
        ImageConversionBatchRepository imageConversionBatchRepository,
        ImageConversionBatchQueryService imageConversionBatchQueryService
    ) {
        this.imageConversionBatchService = imageConversionBatchService;
        this.imageConversionBatchRepository = imageConversionBatchRepository;
        this.imageConversionBatchQueryService = imageConversionBatchQueryService;
    }

    /**
     * {@code POST  /image-conversion-batches} : Create a new imageConversionBatch.
     *
     * @param imageConversionBatchDTO the imageConversionBatchDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageConversionBatchDTO, or with status {@code 400 (Bad Request)} if the imageConversionBatch has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageConversionBatchDTO> createImageConversionBatch(
        @Valid @RequestBody ImageConversionBatchDTO imageConversionBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImageConversionBatch : {}", imageConversionBatchDTO);
        if (imageConversionBatchDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageConversionBatch cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageConversionBatchDTO = imageConversionBatchService.save(imageConversionBatchDTO);
        return ResponseEntity.created(new URI("/api/image-conversion-batches/" + imageConversionBatchDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageConversionBatchDTO.getId().toString()))
            .body(imageConversionBatchDTO);
    }

    /**
     * {@code PUT  /image-conversion-batches/:id} : Updates an existing imageConversionBatch.
     *
     * @param id the id of the imageConversionBatchDTO to save.
     * @param imageConversionBatchDTO the imageConversionBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionBatchDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionBatchDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageConversionBatchDTO> updateImageConversionBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageConversionBatchDTO imageConversionBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageConversionBatch : {}, {}", id, imageConversionBatchDTO);
        if (imageConversionBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageConversionBatchDTO = imageConversionBatchService.update(imageConversionBatchDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionBatchDTO.getId().toString()))
            .body(imageConversionBatchDTO);
    }

    /**
     * {@code PATCH  /image-conversion-batches/:id} : Partial updates given fields of an existing imageConversionBatch, field will ignore if it is null
     *
     * @param id the id of the imageConversionBatchDTO to save.
     * @param imageConversionBatchDTO the imageConversionBatchDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionBatchDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionBatchDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageConversionBatchDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionBatchDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageConversionBatchDTO> partialUpdateImageConversionBatch(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageConversionBatchDTO imageConversionBatchDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageConversionBatch partially : {}, {}", id, imageConversionBatchDTO);
        if (imageConversionBatchDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionBatchDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionBatchRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageConversionBatchDTO> result = imageConversionBatchService.partialUpdate(imageConversionBatchDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionBatchDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-conversion-batches} : get all the imageConversionBatches.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageConversionBatches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageConversionBatchDTO>> getAllImageConversionBatches(
        ImageConversionBatchCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageConversionBatches by criteria: {}", criteria);

        Page<ImageConversionBatchDTO> page = imageConversionBatchQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-conversion-batches/count} : count all the imageConversionBatches.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageConversionBatches(ImageConversionBatchCriteria criteria) {
        LOG.debug("REST request to count ImageConversionBatches by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageConversionBatchQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-conversion-batches/:id} : get the "id" imageConversionBatch.
     *
     * @param id the id of the imageConversionBatchDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageConversionBatchDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageConversionBatchDTO> getImageConversionBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageConversionBatch : {}", id);
        Optional<ImageConversionBatchDTO> imageConversionBatchDTO = imageConversionBatchService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageConversionBatchDTO);
    }

    /**
     * {@code DELETE  /image-conversion-batches/:id} : delete the "id" imageConversionBatch.
     *
     * @param id the id of the imageConversionBatchDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageConversionBatch(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageConversionBatch : {}", id);
        imageConversionBatchService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-conversion-batches/_search?query=:query} : search for the imageConversionBatch corresponding
     * to the query.
     *
     * @param query the query of the imageConversionBatch search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImageConversionBatchDTO>> searchImageConversionBatches(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImageConversionBatches for query {}", query);
        try {
            Page<ImageConversionBatchDTO> page = imageConversionBatchService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
