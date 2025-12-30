package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaFolderRepository;
import fr.smartprod.paperdms.document.service.MetaFolderQueryService;
import fr.smartprod.paperdms.document.service.MetaFolderService;
import fr.smartprod.paperdms.document.service.criteria.MetaFolderCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaFolder}.
 */
@RestController
@RequestMapping("/api/meta-folders")
public class MetaFolderResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaFolderResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaFolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaFolderService metaFolderService;

    private final MetaFolderRepository metaFolderRepository;

    private final MetaFolderQueryService metaFolderQueryService;

    public MetaFolderResource(
        MetaFolderService metaFolderService,
        MetaFolderRepository metaFolderRepository,
        MetaFolderQueryService metaFolderQueryService
    ) {
        this.metaFolderService = metaFolderService;
        this.metaFolderRepository = metaFolderRepository;
        this.metaFolderQueryService = metaFolderQueryService;
    }

    /**
     * {@code POST  /meta-folders} : Create a new metaFolder.
     *
     * @param metaFolderDTO the metaFolderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaFolderDTO, or with status {@code 400 (Bad Request)} if the metaFolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaFolderDTO> createMetaFolder(@Valid @RequestBody MetaFolderDTO metaFolderDTO) throws URISyntaxException {
        LOG.debug("REST request to save MetaFolder : {}", metaFolderDTO);
        if (metaFolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaFolderDTO = metaFolderService.save(metaFolderDTO);
        return ResponseEntity.created(new URI("/api/meta-folders/" + metaFolderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaFolderDTO.getId().toString()))
            .body(metaFolderDTO);
    }

    /**
     * {@code PUT  /meta-folders/:id} : Updates an existing metaFolder.
     *
     * @param id the id of the metaFolderDTO to save.
     * @param metaFolderDTO the metaFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaFolderDTO,
     * or with status {@code 400 (Bad Request)} if the metaFolderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaFolderDTO> updateMetaFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaFolderDTO metaFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaFolder : {}, {}", id, metaFolderDTO);
        if (metaFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaFolderDTO = metaFolderService.update(metaFolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaFolderDTO.getId().toString()))
            .body(metaFolderDTO);
    }

    /**
     * {@code PATCH  /meta-folders/:id} : Partial updates given fields of an existing metaFolder, field will ignore if it is null
     *
     * @param id the id of the metaFolderDTO to save.
     * @param metaFolderDTO the metaFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaFolderDTO,
     * or with status {@code 400 (Bad Request)} if the metaFolderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaFolderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaFolderDTO> partialUpdateMetaFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaFolderDTO metaFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaFolder partially : {}, {}", id, metaFolderDTO);
        if (metaFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaFolderDTO> result = metaFolderService.partialUpdate(metaFolderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaFolderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-folders} : get all the metaFolders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaFolders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaFolderDTO>> getAllMetaFolders(
        MetaFolderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaFolders by criteria: {}", criteria);

        Page<MetaFolderDTO> page = metaFolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-folders/count} : count all the metaFolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaFolders(MetaFolderCriteria criteria) {
        LOG.debug("REST request to count MetaFolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaFolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-folders/:id} : get the "id" metaFolder.
     *
     * @param id the id of the metaFolderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaFolderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaFolderDTO> getMetaFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaFolder : {}", id);
        Optional<MetaFolderDTO> metaFolderDTO = metaFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaFolderDTO);
    }

    /**
     * {@code DELETE  /meta-folders/:id} : delete the "id" metaFolder.
     *
     * @param id the id of the metaFolderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaFolder : {}", id);
        metaFolderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-folders/_search?query=:query} : search for the metaFolder corresponding
     * to the query.
     *
     * @param query the query of the metaFolder search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaFolderDTO>> searchMetaFolders(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaFolders for query {}", query);
        try {
            Page<MetaFolderDTO> page = metaFolderService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
