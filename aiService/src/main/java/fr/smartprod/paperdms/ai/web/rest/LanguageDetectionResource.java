package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.LanguageDetectionRepository;
import fr.smartprod.paperdms.ai.service.LanguageDetectionService;
import fr.smartprod.paperdms.ai.service.dto.LanguageDetectionDTO;
import fr.smartprod.paperdms.ai.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.LanguageDetection}.
 */
@RestController
@RequestMapping("/api/language-detections")
public class LanguageDetectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(LanguageDetectionResource.class);

    private static final String ENTITY_NAME = "aiServiceLanguageDetection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LanguageDetectionService languageDetectionService;

    private final LanguageDetectionRepository languageDetectionRepository;

    public LanguageDetectionResource(
        LanguageDetectionService languageDetectionService,
        LanguageDetectionRepository languageDetectionRepository
    ) {
        this.languageDetectionService = languageDetectionService;
        this.languageDetectionRepository = languageDetectionRepository;
    }

    /**
     * {@code POST  /language-detections} : Create a new languageDetection.
     *
     * @param languageDetectionDTO the languageDetectionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new languageDetectionDTO, or with status {@code 400 (Bad Request)} if the languageDetection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LanguageDetectionDTO> createLanguageDetection(@Valid @RequestBody LanguageDetectionDTO languageDetectionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save LanguageDetection : {}", languageDetectionDTO);
        if (languageDetectionDTO.getId() != null) {
            throw new BadRequestAlertException("A new languageDetection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        languageDetectionDTO = languageDetectionService.save(languageDetectionDTO);
        return ResponseEntity.created(new URI("/api/language-detections/" + languageDetectionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, languageDetectionDTO.getId().toString()))
            .body(languageDetectionDTO);
    }

    /**
     * {@code PUT  /language-detections/:id} : Updates an existing languageDetection.
     *
     * @param id the id of the languageDetectionDTO to save.
     * @param languageDetectionDTO the languageDetectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated languageDetectionDTO,
     * or with status {@code 400 (Bad Request)} if the languageDetectionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the languageDetectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LanguageDetectionDTO> updateLanguageDetection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LanguageDetectionDTO languageDetectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update LanguageDetection : {}, {}", id, languageDetectionDTO);
        if (languageDetectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, languageDetectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageDetectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        languageDetectionDTO = languageDetectionService.update(languageDetectionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, languageDetectionDTO.getId().toString()))
            .body(languageDetectionDTO);
    }

    /**
     * {@code PATCH  /language-detections/:id} : Partial updates given fields of an existing languageDetection, field will ignore if it is null
     *
     * @param id the id of the languageDetectionDTO to save.
     * @param languageDetectionDTO the languageDetectionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated languageDetectionDTO,
     * or with status {@code 400 (Bad Request)} if the languageDetectionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the languageDetectionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the languageDetectionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LanguageDetectionDTO> partialUpdateLanguageDetection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LanguageDetectionDTO languageDetectionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LanguageDetection partially : {}, {}", id, languageDetectionDTO);
        if (languageDetectionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, languageDetectionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!languageDetectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LanguageDetectionDTO> result = languageDetectionService.partialUpdate(languageDetectionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, languageDetectionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /language-detections} : get all the languageDetections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of languageDetections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<LanguageDetectionDTO>> getAllLanguageDetections(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of LanguageDetections");
        Page<LanguageDetectionDTO> page = languageDetectionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /language-detections/:id} : get the "id" languageDetection.
     *
     * @param id the id of the languageDetectionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the languageDetectionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<LanguageDetectionDTO> getLanguageDetection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LanguageDetection : {}", id);
        Optional<LanguageDetectionDTO> languageDetectionDTO = languageDetectionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(languageDetectionDTO);
    }

    /**
     * {@code DELETE  /language-detections/:id} : delete the "id" languageDetection.
     *
     * @param id the id of the languageDetectionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLanguageDetection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LanguageDetection : {}", id);
        languageDetectionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
