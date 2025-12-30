package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaMetaTagCategoryRepository;
import fr.smartprod.paperdms.document.service.MetaMetaTagCategoryQueryService;
import fr.smartprod.paperdms.document.service.MetaMetaTagCategoryService;
import fr.smartprod.paperdms.document.service.criteria.MetaMetaTagCategoryCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.document.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaMetaTagCategory}.
 */
@RestController
@RequestMapping("/api/meta-meta-tag-categories")
public class MetaMetaTagCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaMetaTagCategoryResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaMetaTagCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaMetaTagCategoryService metaMetaTagCategoryService;

    private final MetaMetaTagCategoryRepository metaMetaTagCategoryRepository;

    private final MetaMetaTagCategoryQueryService metaMetaTagCategoryQueryService;

    public MetaMetaTagCategoryResource(
        MetaMetaTagCategoryService metaMetaTagCategoryService,
        MetaMetaTagCategoryRepository metaMetaTagCategoryRepository,
        MetaMetaTagCategoryQueryService metaMetaTagCategoryQueryService
    ) {
        this.metaMetaTagCategoryService = metaMetaTagCategoryService;
        this.metaMetaTagCategoryRepository = metaMetaTagCategoryRepository;
        this.metaMetaTagCategoryQueryService = metaMetaTagCategoryQueryService;
    }

    /**
     * {@code POST  /meta-meta-tag-categories} : Create a new metaMetaTagCategory.
     *
     * @param metaMetaTagCategoryDTO the metaMetaTagCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaMetaTagCategoryDTO, or with status {@code 400 (Bad Request)} if the metaMetaTagCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaMetaTagCategoryDTO> createMetaMetaTagCategory(
        @Valid @RequestBody MetaMetaTagCategoryDTO metaMetaTagCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MetaMetaTagCategory : {}", metaMetaTagCategoryDTO);
        if (metaMetaTagCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaMetaTagCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaMetaTagCategoryDTO = metaMetaTagCategoryService.save(metaMetaTagCategoryDTO);
        return ResponseEntity.created(new URI("/api/meta-meta-tag-categories/" + metaMetaTagCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaMetaTagCategoryDTO.getId().toString()))
            .body(metaMetaTagCategoryDTO);
    }

    /**
     * {@code PUT  /meta-meta-tag-categories/:id} : Updates an existing metaMetaTagCategory.
     *
     * @param id the id of the metaMetaTagCategoryDTO to save.
     * @param metaMetaTagCategoryDTO the metaMetaTagCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaMetaTagCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the metaMetaTagCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaMetaTagCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaMetaTagCategoryDTO> updateMetaMetaTagCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaMetaTagCategoryDTO metaMetaTagCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaMetaTagCategory : {}, {}", id, metaMetaTagCategoryDTO);
        if (metaMetaTagCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaMetaTagCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaMetaTagCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaMetaTagCategoryDTO = metaMetaTagCategoryService.update(metaMetaTagCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaMetaTagCategoryDTO.getId().toString()))
            .body(metaMetaTagCategoryDTO);
    }

    /**
     * {@code PATCH  /meta-meta-tag-categories/:id} : Partial updates given fields of an existing metaMetaTagCategory, field will ignore if it is null
     *
     * @param id the id of the metaMetaTagCategoryDTO to save.
     * @param metaMetaTagCategoryDTO the metaMetaTagCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaMetaTagCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the metaMetaTagCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaMetaTagCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaMetaTagCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaMetaTagCategoryDTO> partialUpdateMetaMetaTagCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaMetaTagCategoryDTO metaMetaTagCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaMetaTagCategory partially : {}, {}", id, metaMetaTagCategoryDTO);
        if (metaMetaTagCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaMetaTagCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaMetaTagCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaMetaTagCategoryDTO> result = metaMetaTagCategoryService.partialUpdate(metaMetaTagCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaMetaTagCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-meta-tag-categories} : get all the metaMetaTagCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaMetaTagCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaMetaTagCategoryDTO>> getAllMetaMetaTagCategories(
        MetaMetaTagCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaMetaTagCategories by criteria: {}", criteria);

        Page<MetaMetaTagCategoryDTO> page = metaMetaTagCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-meta-tag-categories/count} : count all the metaMetaTagCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaMetaTagCategories(MetaMetaTagCategoryCriteria criteria) {
        LOG.debug("REST request to count MetaMetaTagCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaMetaTagCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-meta-tag-categories/:id} : get the "id" metaMetaTagCategory.
     *
     * @param id the id of the metaMetaTagCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaMetaTagCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaMetaTagCategoryDTO> getMetaMetaTagCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaMetaTagCategory : {}", id);
        Optional<MetaMetaTagCategoryDTO> metaMetaTagCategoryDTO = metaMetaTagCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaMetaTagCategoryDTO);
    }

    /**
     * {@code DELETE  /meta-meta-tag-categories/:id} : delete the "id" metaMetaTagCategory.
     *
     * @param id the id of the metaMetaTagCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaMetaTagCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaMetaTagCategory : {}", id);
        metaMetaTagCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-meta-tag-categories/_search?query=:query} : search for the metaMetaTagCategory corresponding
     * to the query.
     *
     * @param query the query of the metaMetaTagCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaMetaTagCategoryDTO>> searchMetaMetaTagCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaMetaTagCategories for query {}", query);
        try {
            Page<MetaMetaTagCategoryDTO> page = metaMetaTagCategoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
