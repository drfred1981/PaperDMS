package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaPermissionGroupRepository;
import fr.smartprod.paperdms.document.service.MetaPermissionGroupQueryService;
import fr.smartprod.paperdms.document.service.MetaPermissionGroupService;
import fr.smartprod.paperdms.document.service.criteria.MetaPermissionGroupCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaPermissionGroup}.
 */
@RestController
@RequestMapping("/api/meta-permission-groups")
public class MetaPermissionGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaPermissionGroupResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaPermissionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaPermissionGroupService metaPermissionGroupService;

    private final MetaPermissionGroupRepository metaPermissionGroupRepository;

    private final MetaPermissionGroupQueryService metaPermissionGroupQueryService;

    public MetaPermissionGroupResource(
        MetaPermissionGroupService metaPermissionGroupService,
        MetaPermissionGroupRepository metaPermissionGroupRepository,
        MetaPermissionGroupQueryService metaPermissionGroupQueryService
    ) {
        this.metaPermissionGroupService = metaPermissionGroupService;
        this.metaPermissionGroupRepository = metaPermissionGroupRepository;
        this.metaPermissionGroupQueryService = metaPermissionGroupQueryService;
    }

    /**
     * {@code POST  /meta-permission-groups} : Create a new metaPermissionGroup.
     *
     * @param metaPermissionGroupDTO the metaPermissionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaPermissionGroupDTO, or with status {@code 400 (Bad Request)} if the metaPermissionGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaPermissionGroupDTO> createMetaPermissionGroup(
        @Valid @RequestBody MetaPermissionGroupDTO metaPermissionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MetaPermissionGroup : {}", metaPermissionGroupDTO);
        if (metaPermissionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaPermissionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaPermissionGroupDTO = metaPermissionGroupService.save(metaPermissionGroupDTO);
        return ResponseEntity.created(new URI("/api/meta-permission-groups/" + metaPermissionGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaPermissionGroupDTO.getId().toString()))
            .body(metaPermissionGroupDTO);
    }

    /**
     * {@code PUT  /meta-permission-groups/:id} : Updates an existing metaPermissionGroup.
     *
     * @param id the id of the metaPermissionGroupDTO to save.
     * @param metaPermissionGroupDTO the metaPermissionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaPermissionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the metaPermissionGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaPermissionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaPermissionGroupDTO> updateMetaPermissionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaPermissionGroupDTO metaPermissionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaPermissionGroup : {}, {}", id, metaPermissionGroupDTO);
        if (metaPermissionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaPermissionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaPermissionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaPermissionGroupDTO = metaPermissionGroupService.update(metaPermissionGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaPermissionGroupDTO.getId().toString()))
            .body(metaPermissionGroupDTO);
    }

    /**
     * {@code PATCH  /meta-permission-groups/:id} : Partial updates given fields of an existing metaPermissionGroup, field will ignore if it is null
     *
     * @param id the id of the metaPermissionGroupDTO to save.
     * @param metaPermissionGroupDTO the metaPermissionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaPermissionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the metaPermissionGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaPermissionGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaPermissionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaPermissionGroupDTO> partialUpdateMetaPermissionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaPermissionGroupDTO metaPermissionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaPermissionGroup partially : {}, {}", id, metaPermissionGroupDTO);
        if (metaPermissionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaPermissionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaPermissionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaPermissionGroupDTO> result = metaPermissionGroupService.partialUpdate(metaPermissionGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaPermissionGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-permission-groups} : get all the metaPermissionGroups.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaPermissionGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaPermissionGroupDTO>> getAllMetaPermissionGroups(
        MetaPermissionGroupCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaPermissionGroups by criteria: {}", criteria);

        Page<MetaPermissionGroupDTO> page = metaPermissionGroupQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-permission-groups/count} : count all the metaPermissionGroups.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaPermissionGroups(MetaPermissionGroupCriteria criteria) {
        LOG.debug("REST request to count MetaPermissionGroups by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaPermissionGroupQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-permission-groups/:id} : get the "id" metaPermissionGroup.
     *
     * @param id the id of the metaPermissionGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaPermissionGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaPermissionGroupDTO> getMetaPermissionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaPermissionGroup : {}", id);
        Optional<MetaPermissionGroupDTO> metaPermissionGroupDTO = metaPermissionGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaPermissionGroupDTO);
    }

    /**
     * {@code DELETE  /meta-permission-groups/:id} : delete the "id" metaPermissionGroup.
     *
     * @param id the id of the metaPermissionGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaPermissionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaPermissionGroup : {}", id);
        metaPermissionGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-permission-groups/_search?query=:query} : search for the metaPermissionGroup corresponding
     * to the query.
     *
     * @param query the query of the metaPermissionGroup search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaPermissionGroupDTO>> searchMetaPermissionGroups(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaPermissionGroups for query {}", query);
        try {
            Page<MetaPermissionGroupDTO> page = metaPermissionGroupService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
