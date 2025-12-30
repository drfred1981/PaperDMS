package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.MetaSmartFolderRepository;
import fr.smartprod.paperdms.document.service.MetaSmartFolderQueryService;
import fr.smartprod.paperdms.document.service.MetaSmartFolderService;
import fr.smartprod.paperdms.document.service.criteria.MetaSmartFolderCriteria;
import fr.smartprod.paperdms.document.service.dto.MetaSmartFolderDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.MetaSmartFolder}.
 */
@RestController
@RequestMapping("/api/meta-smart-folders")
public class MetaSmartFolderResource {

    private static final Logger LOG = LoggerFactory.getLogger(MetaSmartFolderResource.class);

    private static final String ENTITY_NAME = "documentServiceMetaSmartFolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MetaSmartFolderService metaSmartFolderService;

    private final MetaSmartFolderRepository metaSmartFolderRepository;

    private final MetaSmartFolderQueryService metaSmartFolderQueryService;

    public MetaSmartFolderResource(
        MetaSmartFolderService metaSmartFolderService,
        MetaSmartFolderRepository metaSmartFolderRepository,
        MetaSmartFolderQueryService metaSmartFolderQueryService
    ) {
        this.metaSmartFolderService = metaSmartFolderService;
        this.metaSmartFolderRepository = metaSmartFolderRepository;
        this.metaSmartFolderQueryService = metaSmartFolderQueryService;
    }

    /**
     * {@code POST  /meta-smart-folders} : Create a new metaSmartFolder.
     *
     * @param metaSmartFolderDTO the metaSmartFolderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new metaSmartFolderDTO, or with status {@code 400 (Bad Request)} if the metaSmartFolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MetaSmartFolderDTO> createMetaSmartFolder(@Valid @RequestBody MetaSmartFolderDTO metaSmartFolderDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save MetaSmartFolder : {}", metaSmartFolderDTO);
        if (metaSmartFolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new metaSmartFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        metaSmartFolderDTO = metaSmartFolderService.save(metaSmartFolderDTO);
        return ResponseEntity.created(new URI("/api/meta-smart-folders/" + metaSmartFolderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, metaSmartFolderDTO.getId().toString()))
            .body(metaSmartFolderDTO);
    }

    /**
     * {@code PUT  /meta-smart-folders/:id} : Updates an existing metaSmartFolder.
     *
     * @param id the id of the metaSmartFolderDTO to save.
     * @param metaSmartFolderDTO the metaSmartFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaSmartFolderDTO,
     * or with status {@code 400 (Bad Request)} if the metaSmartFolderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the metaSmartFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MetaSmartFolderDTO> updateMetaSmartFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MetaSmartFolderDTO metaSmartFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MetaSmartFolder : {}, {}", id, metaSmartFolderDTO);
        if (metaSmartFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaSmartFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaSmartFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        metaSmartFolderDTO = metaSmartFolderService.update(metaSmartFolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaSmartFolderDTO.getId().toString()))
            .body(metaSmartFolderDTO);
    }

    /**
     * {@code PATCH  /meta-smart-folders/:id} : Partial updates given fields of an existing metaSmartFolder, field will ignore if it is null
     *
     * @param id the id of the metaSmartFolderDTO to save.
     * @param metaSmartFolderDTO the metaSmartFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated metaSmartFolderDTO,
     * or with status {@code 400 (Bad Request)} if the metaSmartFolderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the metaSmartFolderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the metaSmartFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MetaSmartFolderDTO> partialUpdateMetaSmartFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MetaSmartFolderDTO metaSmartFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MetaSmartFolder partially : {}, {}", id, metaSmartFolderDTO);
        if (metaSmartFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, metaSmartFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!metaSmartFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MetaSmartFolderDTO> result = metaSmartFolderService.partialUpdate(metaSmartFolderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, metaSmartFolderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /meta-smart-folders} : get all the metaSmartFolders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of metaSmartFolders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MetaSmartFolderDTO>> getAllMetaSmartFolders(
        MetaSmartFolderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MetaSmartFolders by criteria: {}", criteria);

        Page<MetaSmartFolderDTO> page = metaSmartFolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /meta-smart-folders/count} : count all the metaSmartFolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMetaSmartFolders(MetaSmartFolderCriteria criteria) {
        LOG.debug("REST request to count MetaSmartFolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(metaSmartFolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /meta-smart-folders/:id} : get the "id" metaSmartFolder.
     *
     * @param id the id of the metaSmartFolderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the metaSmartFolderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MetaSmartFolderDTO> getMetaSmartFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MetaSmartFolder : {}", id);
        Optional<MetaSmartFolderDTO> metaSmartFolderDTO = metaSmartFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(metaSmartFolderDTO);
    }

    /**
     * {@code DELETE  /meta-smart-folders/:id} : delete the "id" metaSmartFolder.
     *
     * @param id the id of the metaSmartFolderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetaSmartFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MetaSmartFolder : {}", id);
        metaSmartFolderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /meta-smart-folders/_search?query=:query} : search for the metaSmartFolder corresponding
     * to the query.
     *
     * @param query the query of the metaSmartFolder search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<MetaSmartFolderDTO>> searchMetaSmartFolders(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of MetaSmartFolders for query {}", query);
        try {
            Page<MetaSmartFolderDTO> page = metaSmartFolderService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
