package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.ExtractedFieldRepository;
import fr.smartprod.paperdms.document.service.ExtractedFieldService;
import fr.smartprod.paperdms.document.service.dto.ExtractedFieldDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.ExtractedField}.
 */
@RestController
@RequestMapping("/api/extracted-fields")
public class ExtractedFieldResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractedFieldResource.class);

    private static final String ENTITY_NAME = "documentServiceExtractedField";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ExtractedFieldService extractedFieldService;

    private final ExtractedFieldRepository extractedFieldRepository;

    public ExtractedFieldResource(ExtractedFieldService extractedFieldService, ExtractedFieldRepository extractedFieldRepository) {
        this.extractedFieldService = extractedFieldService;
        this.extractedFieldRepository = extractedFieldRepository;
    }

    /**
     * {@code POST  /extracted-fields} : Create a new extractedField.
     *
     * @param extractedFieldDTO the extractedFieldDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extractedFieldDTO, or with status {@code 400 (Bad Request)} if the extractedField has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExtractedFieldDTO> createExtractedField(@Valid @RequestBody ExtractedFieldDTO extractedFieldDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExtractedField : {}", extractedFieldDTO);
        if (extractedFieldDTO.getId() != null) {
            throw new BadRequestAlertException("A new extractedField cannot already have an ID", ENTITY_NAME, "idexists");
        }
        extractedFieldDTO = extractedFieldService.save(extractedFieldDTO);
        return ResponseEntity.created(new URI("/api/extracted-fields/" + extractedFieldDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, extractedFieldDTO.getId().toString()))
            .body(extractedFieldDTO);
    }

    /**
     * {@code PUT  /extracted-fields/:id} : Updates an existing extractedField.
     *
     * @param id the id of the extractedFieldDTO to save.
     * @param extractedFieldDTO the extractedFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extractedFieldDTO,
     * or with status {@code 400 (Bad Request)} if the extractedFieldDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extractedFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExtractedFieldDTO> updateExtractedField(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExtractedFieldDTO extractedFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExtractedField : {}, {}", id, extractedFieldDTO);
        if (extractedFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extractedFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extractedFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        extractedFieldDTO = extractedFieldService.update(extractedFieldDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extractedFieldDTO.getId().toString()))
            .body(extractedFieldDTO);
    }

    /**
     * {@code PATCH  /extracted-fields/:id} : Partial updates given fields of an existing extractedField, field will ignore if it is null
     *
     * @param id the id of the extractedFieldDTO to save.
     * @param extractedFieldDTO the extractedFieldDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extractedFieldDTO,
     * or with status {@code 400 (Bad Request)} if the extractedFieldDTO is not valid,
     * or with status {@code 404 (Not Found)} if the extractedFieldDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the extractedFieldDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExtractedFieldDTO> partialUpdateExtractedField(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExtractedFieldDTO extractedFieldDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExtractedField partially : {}, {}", id, extractedFieldDTO);
        if (extractedFieldDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extractedFieldDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extractedFieldRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtractedFieldDTO> result = extractedFieldService.partialUpdate(extractedFieldDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extractedFieldDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /extracted-fields} : get all the extractedFields.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extractedFields in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExtractedFieldDTO>> getAllExtractedFields(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ExtractedFields");
        Page<ExtractedFieldDTO> page = extractedFieldService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /extracted-fields/:id} : get the "id" extractedField.
     *
     * @param id the id of the extractedFieldDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extractedFieldDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtractedFieldDTO> getExtractedField(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExtractedField : {}", id);
        Optional<ExtractedFieldDTO> extractedFieldDTO = extractedFieldService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extractedFieldDTO);
    }

    /**
     * {@code DELETE  /extracted-fields/:id} : delete the "id" extractedField.
     *
     * @param id the id of the extractedFieldDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtractedField(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExtractedField : {}", id);
        extractedFieldService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /extracted-fields/_search?query=:query} : search for the extractedField corresponding
     * to the query.
     *
     * @param query the query of the extractedField search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ExtractedFieldDTO>> searchExtractedFields(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ExtractedFields for query {}", query);
        try {
            Page<ExtractedFieldDTO> page = extractedFieldService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
