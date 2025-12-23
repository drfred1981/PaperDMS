package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.ExtractedTextRepository;
import fr.smartprod.paperdms.ocr.service.ExtractedTextService;
import fr.smartprod.paperdms.ocr.service.dto.ExtractedTextDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.ExtractedText}.
 */
@RestController
@RequestMapping("/api/extracted-texts")
public class ExtractedTextResource {

    private static final Logger LOG = LoggerFactory.getLogger(ExtractedTextResource.class);

    private static final String ENTITY_NAME = "ocrServiceExtractedText";

    @Value("${jhipster.clientApp.name:ocrService}")
    private String applicationName;

    private final ExtractedTextService extractedTextService;

    private final ExtractedTextRepository extractedTextRepository;

    public ExtractedTextResource(ExtractedTextService extractedTextService, ExtractedTextRepository extractedTextRepository) {
        this.extractedTextService = extractedTextService;
        this.extractedTextRepository = extractedTextRepository;
    }

    /**
     * {@code POST  /extracted-texts} : Create a new extractedText.
     *
     * @param extractedTextDTO the extractedTextDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new extractedTextDTO, or with status {@code 400 (Bad Request)} if the extractedText has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ExtractedTextDTO> createExtractedText(@Valid @RequestBody ExtractedTextDTO extractedTextDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ExtractedText : {}", extractedTextDTO);
        if (extractedTextDTO.getId() != null) {
            throw new BadRequestAlertException("A new extractedText cannot already have an ID", ENTITY_NAME, "idexists");
        }
        extractedTextDTO = extractedTextService.save(extractedTextDTO);
        return ResponseEntity.created(new URI("/api/extracted-texts/" + extractedTextDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, extractedTextDTO.getId().toString()))
            .body(extractedTextDTO);
    }

    /**
     * {@code PUT  /extracted-texts/:id} : Updates an existing extractedText.
     *
     * @param id the id of the extractedTextDTO to save.
     * @param extractedTextDTO the extractedTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extractedTextDTO,
     * or with status {@code 400 (Bad Request)} if the extractedTextDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the extractedTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ExtractedTextDTO> updateExtractedText(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ExtractedTextDTO extractedTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ExtractedText : {}, {}", id, extractedTextDTO);
        if (extractedTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extractedTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extractedTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        extractedTextDTO = extractedTextService.update(extractedTextDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extractedTextDTO.getId().toString()))
            .body(extractedTextDTO);
    }

    /**
     * {@code PATCH  /extracted-texts/:id} : Partial updates given fields of an existing extractedText, field will ignore if it is null
     *
     * @param id the id of the extractedTextDTO to save.
     * @param extractedTextDTO the extractedTextDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated extractedTextDTO,
     * or with status {@code 400 (Bad Request)} if the extractedTextDTO is not valid,
     * or with status {@code 404 (Not Found)} if the extractedTextDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the extractedTextDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ExtractedTextDTO> partialUpdateExtractedText(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ExtractedTextDTO extractedTextDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ExtractedText partially : {}, {}", id, extractedTextDTO);
        if (extractedTextDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, extractedTextDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!extractedTextRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ExtractedTextDTO> result = extractedTextService.partialUpdate(extractedTextDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, extractedTextDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /extracted-texts} : get all the extractedTexts.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of extractedTexts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ExtractedTextDTO>> getAllExtractedTexts(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ExtractedTexts");
        Page<ExtractedTextDTO> page = extractedTextService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /extracted-texts/:id} : get the "id" extractedText.
     *
     * @param id the id of the extractedTextDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the extractedTextDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ExtractedTextDTO> getExtractedText(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ExtractedText : {}", id);
        Optional<ExtractedTextDTO> extractedTextDTO = extractedTextService.findOne(id);
        return ResponseUtil.wrapOrNotFound(extractedTextDTO);
    }

    /**
     * {@code DELETE  /extracted-texts/:id} : delete the "id" extractedText.
     *
     * @param id the id of the extractedTextDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExtractedText(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ExtractedText : {}", id);
        extractedTextService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /extracted-texts/_search?query=:query} : search for the extractedText corresponding
     * to the query.
     *
     * @param query the query of the extractedText search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ExtractedTextDTO>> searchExtractedTexts(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ExtractedTexts for query {}", query);
        try {
            Page<ExtractedTextDTO> page = extractedTextService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
