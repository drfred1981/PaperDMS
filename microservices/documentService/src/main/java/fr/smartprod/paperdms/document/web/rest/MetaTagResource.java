package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaTagRepository;
import fr.smartprod.paperdms.document.service.MetaTagQueryService;
import fr.smartprod.paperdms.document.service.MetaTagService;
import fr.smartprod.paperdms.document.service.criteria.MetaTagCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaTag}.
 */
@RestController
@RequestMapping("/api/meta-tags")
public class MetaTagResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaTagResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaTag";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaTagService metaTagService;

    private final MetaTagRepository metaTagRepository;

    private final MetaTagQueryService metaTagQueryService;

    public MetaTagResource(MetaTagService metaTagService, MetaTagRepository metaTagRepository, MetaTagQueryService metaTagQueryService) {
        this.metaTagService = metaTagService;
        this.metaTagRepository = metaTagRepository;
        this.metaTagQueryService = metaTagQueryService;
    }

    /**
     * {@code POST  /meta-tags} : Create a new metaTag.
     *
     * @param metaTagDTO the metaTagDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaTagDTO, or with status {@code 400 (Bad Request)} if the metaTag has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaTagDTO> createMetaTag(@Valid @RequestBody MetaTagDTO metaTagDTO) throws URISyntaxException {
        LOG.debug("REST request to save MetaTag : {}", metaTagDTO);
        if (metaTagDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaTag cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaTagDTO = metaTagService.save(metaTagDTO);
        return ResponseEntity.created(new URI("/api/meta-tags/" + metaTagDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaTagDTO.getId().toString()))
            .body(metaTagDTO);
    }

    /**
     * {@code PUT  /meta-tags/:id} : Updates an existing metaTag.
     *
     * @param id the id of the metaTagDTO to save.
     * @param metaTagDTO the metaTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaTagDTO,
     * or with status {@code 400 (Bad Request)} if the metaTagDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaTagDTO> updateMetaTag(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaTagDTO metaTagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaTag : {}, {}", id, metaTagDTO);
        if (metaTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaTagDTO = metaTagService.update(metaTagDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaTagDTO.getId().toString()))
            .body(metaTagDTO);
    }

    /**
     * {@code PATCH  /meta-tags/:id} : Partial updates given fields of an existing metaTag, field will ignore if it is null
     *
     * @param id the id of the metaTagDTO to save.
     * @param metaTagDTO the metaTagDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaTagDTO,
     * or with status {@code 400 (Bad Request)} if the metaTagDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaTagDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaTagDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaTagDTO> partialUpdateMetaTag(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaTagDTO metaTagDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaTag partially : {}, {}", id, metaTagDTO);
        if (metaTagDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaTagDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaTagRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaTagDTO> result = metaTagService.partialUpdate(metaTagDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaTagDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-tags} : get all the metaTags.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaTags in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaTagDTO>> getAllMetaTags(
        MetaTagCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaTags by criteria: {}", criteria);

        Page<MetaTagDTO> page = metaTagQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-tags/count} : count all the metaTags.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaTags(MetaTagCriteria criteria) {
        LOG.debug("REST request to count MetaTags by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaTagQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-tags/:id} : get the "id" metaTag.
     *
     * @param id the id of the metaTagDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaTagDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaTagDTO> getMetaTag(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaTag : {}", id);
        Optional<MetaTagDTO> metaTagDTO = metaTagService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaTagDTO);
    }

    /**
     * {@code DELETE  /meta-tags/:id} : delete the "id" metaTag.
     *
     * @param id the id of the metaTagDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaTag(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaTag : {}", id);
        metaTagService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-tags/_search?query=:query} : search for the metaTag corresponding
     * to the query.
     *
     * @param query the query of the metaTag search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaTagDTO>> searchMetaTags(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaTags for query {}", query);
        try {
            Page<MetaTagDTO> page = metaTagService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
