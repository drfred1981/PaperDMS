package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionConfigRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionConfigQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionConfigService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionConfigCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionConfigDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig}.
 */
@RestController
@RequestMapping("/api/image-conversion-configs")
public class ImageConversionConfigResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionConfigResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImageConversionConfig";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageConversionConfigService imageConversionConfigService;

    private final ImageConversionConfigRepository imageConversionConfigRepository;

    private final ImageConversionConfigQueryService imageConversionConfigQueryService;

    public ImageConversionConfigResource(
        ImageConversionConfigService imageConversionConfigService,
        ImageConversionConfigRepository imageConversionConfigRepository,
        ImageConversionConfigQueryService imageConversionConfigQueryService
    ) {
        this.imageConversionConfigService = imageConversionConfigService;
        this.imageConversionConfigRepository = imageConversionConfigRepository;
        this.imageConversionConfigQueryService = imageConversionConfigQueryService;
    }

    /**
     * {@code POST  /image-conversion-configs} : Create a new imageConversionConfig.
     *
     * @param imageConversionConfigDTO the imageConversionConfigDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageConversionConfigDTO, or with status {@code 400 (Bad Request)} if the imageConversionConfig has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageConversionConfigDTO> createImageConversionConfig(
        @Valid @RequestBody ImageConversionConfigDTO imageConversionConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImageConversionConfig : {}", imageConversionConfigDTO);
        if (imageConversionConfigDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageConversionConfig cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageConversionConfigDTO = imageConversionConfigService.save(imageConversionConfigDTO);
        return ResponseEntity.created(new URI("/api/image-conversion-configs/" + imageConversionConfigDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageConversionConfigDTO.getId().toString()))
            .body(imageConversionConfigDTO);
    }

    /**
     * {@code PUT  /image-conversion-configs/:id} : Updates an existing imageConversionConfig.
     *
     * @param id the id of the imageConversionConfigDTO to save.
     * @param imageConversionConfigDTO the imageConversionConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionConfigDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionConfigDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageConversionConfigDTO> updateImageConversionConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageConversionConfigDTO imageConversionConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageConversionConfig : {}, {}", id, imageConversionConfigDTO);
        if (imageConversionConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageConversionConfigDTO = imageConversionConfigService.update(imageConversionConfigDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionConfigDTO.getId().toString()))
            .body(imageConversionConfigDTO);
    }

    /**
     * {@code PATCH  /image-conversion-configs/:id} : Partial updates given fields of an existing imageConversionConfig, field will ignore if it is null
     *
     * @param id the id of the imageConversionConfigDTO to save.
     * @param imageConversionConfigDTO the imageConversionConfigDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionConfigDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionConfigDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageConversionConfigDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionConfigDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageConversionConfigDTO> partialUpdateImageConversionConfig(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageConversionConfigDTO imageConversionConfigDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageConversionConfig partially : {}, {}", id, imageConversionConfigDTO);
        if (imageConversionConfigDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionConfigDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionConfigRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageConversionConfigDTO> result = imageConversionConfigService.partialUpdate(imageConversionConfigDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionConfigDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-conversion-configs} : get all the imageConversionConfigs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageConversionConfigs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageConversionConfigDTO>> getAllImageConversionConfigs(
        ImageConversionConfigCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageConversionConfigs by criteria: {}", criteria);

        Page<ImageConversionConfigDTO> page = imageConversionConfigQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-conversion-configs/count} : count all the imageConversionConfigs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageConversionConfigs(ImageConversionConfigCriteria criteria) {
        LOG.debug("REST request to count ImageConversionConfigs by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageConversionConfigQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-conversion-configs/:id} : get the "id" imageConversionConfig.
     *
     * @param id the id of the imageConversionConfigDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageConversionConfigDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageConversionConfigDTO> getImageConversionConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageConversionConfig : {}", id);
        Optional<ImageConversionConfigDTO> imageConversionConfigDTO = imageConversionConfigService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageConversionConfigDTO);
    }

    /**
     * {@code DELETE  /image-conversion-configs/:id} : delete the "id" imageConversionConfig.
     *
     * @param id the id of the imageConversionConfigDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageConversionConfig(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageConversionConfig : {}", id);
        imageConversionConfigService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-conversion-configs/_search?query=:query} : search for the imageConversionConfig corresponding
     * to the query.
     *
     * @param query the query of the imageConversionConfig search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImageConversionConfigDTO>> searchImageConversionConfigs(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImageConversionConfigs for query {}", query);
        try {
            Page<ImageConversionConfigDTO> page = imageConversionConfigService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
