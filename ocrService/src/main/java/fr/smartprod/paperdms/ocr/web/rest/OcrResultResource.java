package fr.smartprod.paperdms.ocr.web.rest;

import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.service.OcrResultService;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import fr.smartprod.paperdms.ocr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ocr.domain.OcrResult}.
 */
@RestController
@RequestMapping("/api/ocr-results")
public class OcrResultResource {

    private static final Logger LOG = LoggerFactory.getLogger(OcrResultResource.class);

    private static final String ENTITY_NAME = "ocrServiceOcrResult";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final OcrResultService ocrResultService;

    private final OcrResultRepository ocrResultRepository;

    public OcrResultResource(OcrResultService ocrResultService, OcrResultRepository ocrResultRepository) {
        this.ocrResultService = ocrResultService;
        this.ocrResultRepository = ocrResultRepository;
    }

    /**
     * {@code POST  /ocr-results} : Create a new ocrResult.
     *
     * @param ocrResultDTO the ocrResultDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ocrResultDTO, or with status {@code 400 (Bad Request)} if the ocrResult has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<OcrResultDTO> createOcrResult(@Valid @RequestBody OcrResultDTO ocrResultDTO) throws URISyntaxException {
        LOG.debug("REST request to save OcrResult : {}", ocrResultDTO);
        if (ocrResultDTO.getId() != null) {
            throw new BadRequestAlertException("A new ocrResult cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ocrResultDTO = ocrResultService.save(ocrResultDTO);
        return ResponseEntity.created(new URI("/api/ocr-results/" + ocrResultDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, ocrResultDTO.getId().toString()))
            .body(ocrResultDTO);
    }

    /**
     * {@code PUT  /ocr-results/:id} : Updates an existing ocrResult.
     *
     * @param id the id of the ocrResultDTO to save.
     * @param ocrResultDTO the ocrResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrResultDTO,
     * or with status {@code 400 (Bad Request)} if the ocrResultDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ocrResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<OcrResultDTO> updateOcrResult(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody OcrResultDTO ocrResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update OcrResult : {}, {}", id, ocrResultDTO);
        if (ocrResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ocrResultDTO = ocrResultService.update(ocrResultDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrResultDTO.getId().toString()))
            .body(ocrResultDTO);
    }

    /**
     * {@code PATCH  /ocr-results/:id} : Partial updates given fields of an existing ocrResult, field will ignore if it is null
     *
     * @param id the id of the ocrResultDTO to save.
     * @param ocrResultDTO the ocrResultDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ocrResultDTO,
     * or with status {@code 400 (Bad Request)} if the ocrResultDTO is not valid,
     * or with status {@code 404 (Not Found)} if the ocrResultDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the ocrResultDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<OcrResultDTO> partialUpdateOcrResult(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody OcrResultDTO ocrResultDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update OcrResult partially : {}, {}", id, ocrResultDTO);
        if (ocrResultDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ocrResultDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ocrResultRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<OcrResultDTO> result = ocrResultService.partialUpdate(ocrResultDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, ocrResultDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ocr-results} : get all the ocrResults.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ocrResults in body.
     */
    @GetMapping("")
    public ResponseEntity<List<OcrResultDTO>> getAllOcrResults(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of OcrResults");
        Page<OcrResultDTO> page = ocrResultService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ocr-results/:id} : get the "id" ocrResult.
     *
     * @param id the id of the ocrResultDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ocrResultDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<OcrResultDTO> getOcrResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to get OcrResult : {}", id);
        Optional<OcrResultDTO> ocrResultDTO = ocrResultService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ocrResultDTO);
    }

    /**
     * {@code DELETE  /ocr-results/:id} : delete the "id" ocrResult.
     *
     * @param id the id of the ocrResultDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOcrResult(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete OcrResult : {}", id);
        ocrResultService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
