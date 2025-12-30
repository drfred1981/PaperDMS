package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionStatisticsRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionStatisticsQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImageConversionStatisticsService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImageConversionStatisticsCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionStatisticsDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics}.
 */
@RestController
@RequestMapping("/api/image-conversion-statistics")
public class ImageConversionStatisticsResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImageConversionStatisticsResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImageConversionStatistics";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImageConversionStatisticsService imageConversionStatisticsService;

    private final ImageConversionStatisticsRepository imageConversionStatisticsRepository;

    private final ImageConversionStatisticsQueryService imageConversionStatisticsQueryService;

    public ImageConversionStatisticsResource(
        ImageConversionStatisticsService imageConversionStatisticsService,
        ImageConversionStatisticsRepository imageConversionStatisticsRepository,
        ImageConversionStatisticsQueryService imageConversionStatisticsQueryService
    ) {
        this.imageConversionStatisticsService = imageConversionStatisticsService;
        this.imageConversionStatisticsRepository = imageConversionStatisticsRepository;
        this.imageConversionStatisticsQueryService = imageConversionStatisticsQueryService;
    }

    /**
     * {@code POST  /image-conversion-statistics} : Create a new imageConversionStatistics.
     *
     * @param imageConversionStatisticsDTO the imageConversionStatisticsDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imageConversionStatisticsDTO, or with status {@code 400 (Bad Request)} if the imageConversionStatistics has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImageConversionStatisticsDTO> createImageConversionStatistics(
        @Valid @RequestBody ImageConversionStatisticsDTO imageConversionStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImageConversionStatistics : {}", imageConversionStatisticsDTO);
        if (imageConversionStatisticsDTO.getId() != null) {
            throw new BadRequestAlertException("A new imageConversionStatistics cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imageConversionStatisticsDTO = imageConversionStatisticsService.save(imageConversionStatisticsDTO);
        return ResponseEntity.created(new URI("/api/image-conversion-statistics/" + imageConversionStatisticsDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imageConversionStatisticsDTO.getId().toString())
            )
            .body(imageConversionStatisticsDTO);
    }

    /**
     * {@code PUT  /image-conversion-statistics/:id} : Updates an existing imageConversionStatistics.
     *
     * @param id the id of the imageConversionStatisticsDTO to save.
     * @param imageConversionStatisticsDTO the imageConversionStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionStatisticsDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImageConversionStatisticsDTO> updateImageConversionStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImageConversionStatisticsDTO imageConversionStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImageConversionStatistics : {}, {}", id, imageConversionStatisticsDTO);
        if (imageConversionStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imageConversionStatisticsDTO = imageConversionStatisticsService.update(imageConversionStatisticsDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionStatisticsDTO.getId().toString())
            )
            .body(imageConversionStatisticsDTO);
    }

    /**
     * {@code PATCH  /image-conversion-statistics/:id} : Partial updates given fields of an existing imageConversionStatistics, field will ignore if it is null
     *
     * @param id the id of the imageConversionStatisticsDTO to save.
     * @param imageConversionStatisticsDTO the imageConversionStatisticsDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imageConversionStatisticsDTO,
     * or with status {@code 400 (Bad Request)} if the imageConversionStatisticsDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imageConversionStatisticsDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imageConversionStatisticsDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImageConversionStatisticsDTO> partialUpdateImageConversionStatistics(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImageConversionStatisticsDTO imageConversionStatisticsDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImageConversionStatistics partially : {}, {}", id, imageConversionStatisticsDTO);
        if (imageConversionStatisticsDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imageConversionStatisticsDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imageConversionStatisticsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImageConversionStatisticsDTO> result = imageConversionStatisticsService.partialUpdate(imageConversionStatisticsDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imageConversionStatisticsDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-conversion-statistics} : get all the imageConversionStatistics.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imageConversionStatistics in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImageConversionStatisticsDTO>> getAllImageConversionStatistics(
        ImageConversionStatisticsCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImageConversionStatistics by criteria: {}", criteria);

        Page<ImageConversionStatisticsDTO> page = imageConversionStatisticsQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-conversion-statistics/count} : count all the imageConversionStatistics.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImageConversionStatistics(ImageConversionStatisticsCriteria criteria) {
        LOG.debug("REST request to count ImageConversionStatistics by criteria: {}", criteria);
        return ResponseEntity.ok().body(imageConversionStatisticsQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-conversion-statistics/:id} : get the "id" imageConversionStatistics.
     *
     * @param id the id of the imageConversionStatisticsDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imageConversionStatisticsDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImageConversionStatisticsDTO> getImageConversionStatistics(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImageConversionStatistics : {}", id);
        Optional<ImageConversionStatisticsDTO> imageConversionStatisticsDTO = imageConversionStatisticsService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imageConversionStatisticsDTO);
    }

    /**
     * {@code DELETE  /image-conversion-statistics/:id} : delete the "id" imageConversionStatistics.
     *
     * @param id the id of the imageConversionStatisticsDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImageConversionStatistics(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImageConversionStatistics : {}", id);
        imageConversionStatisticsService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-conversion-statistics/_search?query=:query} : search for the imageConversionStatistics corresponding
     * to the query.
     *
     * @param query the query of the imageConversionStatistics search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImageConversionStatisticsDTO>> searchImageConversionStatistics(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImageConversionStatistics for query {}", query);
        try {
            Page<ImageConversionStatisticsDTO> page = imageConversionStatisticsService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
