package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionHistoryRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionHistoryQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionHistoryService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionHistoryCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionHistoryDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory}.
 */
@RestController
@RequestMapping("/api/image-conversion-histories")
public class ImageConversionHistoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionHistoryResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImageConversionHistory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageConversionHistoryService imageConversionHistoryService;

    private final ImageConversionHistoryRepository imageConversionHistoryRepository;

    private final ImageConversionHistoryQueryService imageConversionHistoryQueryService;

    public ImageConversionHistoryResource(
        ImageConversionHistoryService imageConversionHistoryService,
        ImageConversionHistoryRepository imageConversionHistoryRepository,
        ImageConversionHistoryQueryService imageConversionHistoryQueryService
    ) {
        this.imageConversionHistoryService = imageConversionHistoryService;
        this.imageConversionHistoryRepository = imageConversionHistoryRepository;
        this.imageConversionHistoryQueryService = imageConversionHistoryQueryService;
    }

    /**
     * {@code POST  /image-conversion-histories} : Create a new imageConversionHistory.
     *
     * @param imageConversionHistoryDTO the imageConversionHistoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageConversionHistoryDTO, or with status {@code 400 (Bad Request)} if the imageConversionHistory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageConversionHistoryDTO> createImageConversionHistory(
        @Valid @RequestBody ImageConversionHistoryDTO imageConversionHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImageConversionHistory : {}", imageConversionHistoryDTO);
        if (imageConversionHistoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageConversionHistory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageConversionHistoryDTO = imageConversionHistoryService.save(imageConversionHistoryDTO);
        return ResponseEntity.created(new URI("/api/image-conversion-histories/" + imageConversionHistoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageConversionHistoryDTO.getId().toString()))
            .body(imageConversionHistoryDTO);
    }

    /**
     * {@code PUT  /image-conversion-histories/:id} : Updates an existing imageConversionHistory.
     *
     * @param id the id of the imageConversionHistoryDTO to save.
     * @param imageConversionHistoryDTO the imageConversionHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionHistoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageConversionHistoryDTO> updateImageConversionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageConversionHistoryDTO imageConversionHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageConversionHistory : {}, {}", id, imageConversionHistoryDTO);
        if (imageConversionHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageConversionHistoryDTO = imageConversionHistoryService.update(imageConversionHistoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionHistoryDTO.getId().toString()))
            .body(imageConversionHistoryDTO);
    }

    /**
     * {@code PATCH  /image-conversion-histories/:id} : Partial updates given fields of an existing imageConversionHistory, field will ignore if it is null
     *
     * @param id the id of the imageConversionHistoryDTO to save.
     * @param imageConversionHistoryDTO the imageConversionHistoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionHistoryDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionHistoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageConversionHistoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionHistoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageConversionHistoryDTO> partialUpdateImageConversionHistory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageConversionHistoryDTO imageConversionHistoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageConversionHistory partially : {}, {}", id, imageConversionHistoryDTO);
        if (imageConversionHistoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionHistoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionHistoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageConversionHistoryDTO> result = imageConversionHistoryService.partialUpdate(imageConversionHistoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionHistoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-conversion-histories} : get all the imageConversionHistories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageConversionHistories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageConversionHistoryDTO>> getAllImageConversionHistories(
        ImageConversionHistoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageConversionHistories by criteria: {}", criteria);

        Page<ImageConversionHistoryDTO> page = imageConversionHistoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-conversion-histories/count} : count all the imageConversionHistories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageConversionHistories(ImageConversionHistoryCriteria criteria) {
        LOG.debug("REST request to count ImageConversionHistories by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageConversionHistoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-conversion-histories/:id} : get the "id" imageConversionHistory.
     *
     * @param id the id of the imageConversionHistoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageConversionHistoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageConversionHistoryDTO> getImageConversionHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageConversionHistory : {}", id);
        Optional<ImageConversionHistoryDTO> imageConversionHistoryDTO = imageConversionHistoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageConversionHistoryDTO);
    }

    /**
     * {@code DELETE  /image-conversion-histories/:id} : delete the "id" imageConversionHistory.
     *
     * @param id the id of the imageConversionHistoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageConversionHistory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageConversionHistory : {}", id);
        imageConversionHistoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-conversion-histories/_search?query=:query} : search for the imageConversionHistory corresponding
     * to the query.
     *
     * @param query the query of the imageConversionHistory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImageConversionHistoryDTO>> searchImageConversionHistories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImageConversionHistories for query {}", query);
        try {
            Page<ImageConversionHistoryDTO> page = imageConversionHistoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
