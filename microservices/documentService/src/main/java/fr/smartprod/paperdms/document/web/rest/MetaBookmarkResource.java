package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaBookmarkRepository;
import fr.smartprod.paperdms.document.service.MetaBookmarkQueryService;
import fr.smartprod.paperdms.document.service.MetaBookmarkService;
import fr.smartprod.paperdms.document.service.criteria.MetaBookmarkCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaBookmarkDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaBookmark}.
 */
@RestController
@RequestMapping("/api/meta-bookmarks")
public class MetaBookmarkResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaBookmarkResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaBookmark";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaBookmarkService metaBookmarkService;

    private final MetaBookmarkRepository metaBookmarkRepository;

    private final MetaBookmarkQueryService metaBookmarkQueryService;

    public MetaBookmarkResource(
        MetaBookmarkService metaBookmarkService,
        MetaBookmarkRepository metaBookmarkRepository,
        MetaBookmarkQueryService metaBookmarkQueryService
    ) {
        this.metaBookmarkService = metaBookmarkService;
        this.metaBookmarkRepository = metaBookmarkRepository;
        this.metaBookmarkQueryService = metaBookmarkQueryService;
    }

    /**
     * {@code POST  /meta-bookmarks} : Create a new metaBookmark.
     *
     * @param metaBookmarkDTO the metaBookmarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaBookmarkDTO, or with status {@code 400 (Bad Request)} if the metaBookmark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaBookmarkDTO> createMetaBookmark(@Valid @RequestBody MetaBookmarkDTO metaBookmarkDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MetaBookmark : {}", metaBookmarkDTO);
        if (metaBookmarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaBookmark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaBookmarkDTO = metaBookmarkService.save(metaBookmarkDTO);
        return ResponseEntity.created(new URI("/api/meta-bookmarks/" + metaBookmarkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaBookmarkDTO.getId().toString()))
            .body(metaBookmarkDTO);
    }

    /**
     * {@code PUT  /meta-bookmarks/:id} : Updates an existing metaBookmark.
     *
     * @param id the id of the metaBookmarkDTO to save.
     * @param metaBookmarkDTO the metaBookmarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaBookmarkDTO,
     * or with status {@code 400 (Bad Request)} if the metaBookmarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaBookmarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaBookmarkDTO> updateMetaBookmark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaBookmarkDTO metaBookmarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaBookmark : {}, {}", id, metaBookmarkDTO);
        if (metaBookmarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaBookmarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaBookmarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaBookmarkDTO = metaBookmarkService.update(metaBookmarkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaBookmarkDTO.getId().toString()))
            .body(metaBookmarkDTO);
    }

    /**
     * {@code PATCH  /meta-bookmarks/:id} : Partial updates given fields of an existing metaBookmark, field will ignore if it is null
     *
     * @param id the id of the metaBookmarkDTO to save.
     * @param metaBookmarkDTO the metaBookmarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaBookmarkDTO,
     * or with status {@code 400 (Bad Request)} if the metaBookmarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaBookmarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaBookmarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaBookmarkDTO> partialUpdateMetaBookmark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaBookmarkDTO metaBookmarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaBookmark partially : {}, {}", id, metaBookmarkDTO);
        if (metaBookmarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaBookmarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaBookmarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaBookmarkDTO> result = metaBookmarkService.partialUpdate(metaBookmarkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaBookmarkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-bookmarks} : get all the metaBookmarks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaBookmarks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaBookmarkDTO>> getAllMetaBookmarks(
        MetaBookmarkCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaBookmarks by criteria: {}", criteria);

        Page<MetaBookmarkDTO> page = metaBookmarkQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-bookmarks/count} : count all the metaBookmarks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaBookmarks(MetaBookmarkCriteria criteria) {
        LOG.debug("REST request to count MetaBookmarks by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaBookmarkQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-bookmarks/:id} : get the "id" metaBookmark.
     *
     * @param id the id of the metaBookmarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaBookmarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaBookmarkDTO> getMetaBookmark(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaBookmark : {}", id);
        Optional<MetaBookmarkDTO> metaBookmarkDTO = metaBookmarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaBookmarkDTO);
    }

    /**
     * {@code DELETE  /meta-bookmarks/:id} : delete the "id" metaBookmark.
     *
     * @param id the id of the metaBookmarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaBookmark(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaBookmark : {}", id);
        metaBookmarkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-bookmarks/_search?query=:query} : search for the metaBookmark corresponding
     * to the query.
     *
     * @param query the query of the metaBookmark search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaBookmarkDTO>> searchMetaBookmarks(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaBookmarks for query {}", query);
        try {
            Page<MetaBookmarkDTO> page = metaBookmarkService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
