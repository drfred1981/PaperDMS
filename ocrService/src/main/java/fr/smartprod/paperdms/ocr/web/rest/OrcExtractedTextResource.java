package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.OrcExtractedTextRepository;
import fr.smartprod.paperdms.ocr.service.OrcExtractedTextQueryService;
import fr.smartprod.paperdms.ocr.service.OrcExtractedTextService;
import fr.smartprod.paperdms.ocr.service.criteria.OrcExtractedTextCriteria;
import fr.smartprod.paperdms.ocr.service.dto.OrcExtractedTextDTO;
import fr.smartprod.paperdms.ocr.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.ocr.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.OrcExtractedText}.
 */
@RestController
@RequestMapping("/api/orc-extracted-texts")
public class OrcExtractedTextResource {

    private static final Logger LOG = LoggerFactory.getLogger(OrcExtractedTextResource.class);

    private static final String ENTITY_NAME = "ocrServiceOrcExtractedText";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OrcExtractedTextService orcExtractedTextService;

    private final OrcExtractedTextRepository orcExtractedTextRepository;

    private final OrcExtractedTextQueryService orcExtractedTextQueryService;

    public OrcExtractedTextResource(
        OrcExtractedTextService orcExtractedTextService,
        OrcExtractedTextRepository orcExtractedTextRepository,
        OrcExtractedTextQueryService orcExtractedTextQueryService
    ) {
        this.orcExtractedTextService = orcExtractedTextService;
        this.orcExtractedTextRepository = orcExtractedTextRepository;
        this.orcExtractedTextQueryService = orcExtractedTextQueryService;
    }

    /**
     * {@code POST  /orc-extracted-texts} : Create a new orcExtractedText.
     *
     * @param orcExtractedTextDTO the orcExtractedTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new orcExtractedTextDTO, or with status {@code 400 (Bad Request)} if the orcExtractedText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OrcExtractedTextDTO> createOrcExtractedText(@Valid @RequestBody OrcExtractedTextDTO orcExtractedTextDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save OrcExtractedText : {}", orcExtractedTextDTO);
        if (orcExtractedTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new orcExtractedText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        orcExtractedTextDTO = orcExtractedTextService.save(orcExtractedTextDTO);
        return ResponseEntity.created(new URI("/api/orc-extracted-texts/" + orcExtractedTextDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, orcExtractedTextDTO.getId().toString()))
            .body(orcExtractedTextDTO);
    }

    /**
     * {@code PUT  /orc-extracted-texts/:id} : Updates an existing orcExtractedText.
     *
     * @param id the id of the orcExtractedTextDTO to save.
     * @param orcExtractedTextDTO the orcExtractedTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcExtractedTextDTO,
     * or with status {@code 400 (Bad Request)} if the orcExtractedTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the orcExtractedTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OrcExtractedTextDTO> updateOrcExtractedText(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OrcExtractedTextDTO orcExtractedTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OrcExtractedText : {}, {}", id, orcExtractedTextDTO);
        if (orcExtractedTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcExtractedTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcExtractedTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        orcExtractedTextDTO = orcExtractedTextService.update(orcExtractedTextDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orcExtractedTextDTO.getId().toString()))
            .body(orcExtractedTextDTO);
    }

    /**
     * {@code PATCH  /orc-extracted-texts/:id} : Partial updates given fields of an existing orcExtractedText, field will ignore if it is null
     *
     * @param id the id of the orcExtractedTextDTO to save.
     * @param orcExtractedTextDTO the orcExtractedTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated orcExtractedTextDTO,
     * or with status {@code 400 (Bad Request)} if the orcExtractedTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the orcExtractedTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the orcExtractedTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OrcExtractedTextDTO> partialUpdateOrcExtractedText(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OrcExtractedTextDTO orcExtractedTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OrcExtractedText partially : {}, {}", id, orcExtractedTextDTO);
        if (orcExtractedTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, orcExtractedTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!orcExtractedTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OrcExtractedTextDTO> result = orcExtractedTextService.partialUpdate(orcExtractedTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, orcExtractedTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /orc-extracted-texts} : get all the orcExtractedTexts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of orcExtractedTexts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OrcExtractedTextDTO>> getAllOrcExtractedTexts(
        OrcExtractedTextCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get OrcExtractedTexts by criteria: {}", criteria);

        Page<OrcExtractedTextDTO> page = orcExtractedTextQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /orc-extracted-texts/count} : count all the orcExtractedTexts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countOrcExtractedTexts(OrcExtractedTextCriteria criteria) {
        LOG.debug("REST request to count OrcExtractedTexts by criteria: {}", criteria);
        return ResponseEntity.ok().body(orcExtractedTextQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /orc-extracted-texts/:id} : get the "id" orcExtractedText.
     *
     * @param id the id of the orcExtractedTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the orcExtractedTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OrcExtractedTextDTO> getOrcExtractedText(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OrcExtractedText : {}", id);
        Optional<OrcExtractedTextDTO> orcExtractedTextDTO = orcExtractedTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(orcExtractedTextDTO);
    }

    /**
     * {@code DELETE  /orc-extracted-texts/:id} : delete the "id" orcExtractedText.
     *
     * @param id the id of the orcExtractedTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrcExtractedText(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OrcExtractedText : {}", id);
        orcExtractedTextService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /orc-extracted-texts/_search?query=:query} : search for the orcExtractedText corresponding
     * to the query.
     *
     * @param query the query of the orcExtractedText search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<OrcExtractedTextDTO>> searchOrcExtractedTexts(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of OrcExtractedTexts for query {}", query);
        try {
            Page<OrcExtractedTextDTO> page = orcExtractedTextService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
