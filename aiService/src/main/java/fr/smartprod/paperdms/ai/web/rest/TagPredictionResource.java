package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.TagPredictionRepository;
import fr.smartprod.paperdms.ai.service.TagPredictionService;
import fr.smartprod.paperdms.ai.service.dto.TagPredictionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.TagPrediction}.
 */
@RestController
@RequestMapping("/api/tag-predictions")
public class TagPredictionResource {

    private static final Logger LOG = LoggerFactory.getLogger(TagPredictionResource.class);

    private static final String ENTITY_NAME = "aiServiceTagPrediction";

    @Value("${jhipster.clientApp.name:aiService}")
    private String applicationName;

    private final TagPredictionService tagPredictionService;

    private final TagPredictionRepository tagPredictionRepository;

    public TagPredictionResource(TagPredictionService tagPredictionService, TagPredictionRepository tagPredictionRepository) {
        this.tagPredictionService = tagPredictionService;
        this.tagPredictionRepository = tagPredictionRepository;
    }

    /**
     * {@code POST  /tag-predictions} : Create a new tagPrediction.
     *
     * @param tagPredictionDTO the tagPredictionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagPredictionDTO, or with status {@code 400 (Bad Request)} if the tagPrediction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TagPredictionDTO> createTagPrediction(@Valid @RequestBody TagPredictionDTO tagPredictionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TagPrediction : {}", tagPredictionDTO);
        if (tagPredictionDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagPrediction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tagPredictionDTO = tagPredictionService.save(tagPredictionDTO);
        return ResponseEntity.created(new URI("/api/tag-predictions/" + tagPredictionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tagPredictionDTO.getId().toString()))
            .body(tagPredictionDTO);
    }

    /**
     * {@code PUT  /tag-predictions/:id} : Updates an existing tagPrediction.
     *
     * @param id the id of the tagPredictionDTO to save.
     * @param tagPredictionDTO the tagPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the tagPredictionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagPredictionDTO> updateTagPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TagPredictionDTO tagPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TagPrediction : {}, {}", id, tagPredictionDTO);
        if (tagPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tagPredictionDTO = tagPredictionService.update(tagPredictionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagPredictionDTO.getId().toString()))
            .body(tagPredictionDTO);
    }

    /**
     * {@code PATCH  /tag-predictions/:id} : Partial updates given fields of an existing tagPrediction, field will ignore if it is null
     *
     * @param id the id of the tagPredictionDTO to save.
     * @param tagPredictionDTO the tagPredictionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagPredictionDTO,
     * or with status {@code 400 (Bad Request)} if the tagPredictionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagPredictionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagPredictionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagPredictionDTO> partialUpdateTagPrediction(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TagPredictionDTO tagPredictionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TagPrediction partially : {}, {}", id, tagPredictionDTO);
        if (tagPredictionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagPredictionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagPredictionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagPredictionDTO> result = tagPredictionService.partialUpdate(tagPredictionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagPredictionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tag-predictions} : get all the tagPredictions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagPredictions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TagPredictionDTO>> getAllTagPredictions(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of TagPredictions");
        Page<TagPredictionDTO> page = tagPredictionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tag-predictions/:id} : get the "id" tagPrediction.
     *
     * @param id the id of the tagPredictionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagPredictionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagPredictionDTO> getTagPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TagPrediction : {}", id);
        Optional<TagPredictionDTO> tagPredictionDTO = tagPredictionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagPredictionDTO);
    }

    /**
     * {@code DELETE  /tag-predictions/:id} : delete the "id" tagPrediction.
     *
     * @param id the id of the tagPredictionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagPrediction(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TagPrediction : {}", id);
        tagPredictionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
