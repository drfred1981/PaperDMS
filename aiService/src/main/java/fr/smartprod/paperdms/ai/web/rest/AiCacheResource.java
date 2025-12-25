package fr.smartprod.paperdms.ai.web.rest;

import fr.smartprod.paperdms.ai.repository.AiCacheRepository;
import fr.smartprod.paperdms.ai.service.AiCacheService;
import fr.smartprod.paperdms.ai.service.dto.AiCacheDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.ai.domain.AiCache}.
 */
@RestController
@RequestMapping("/api/ai-caches")
public class AiCacheResource {

    private static final Logger LOG = LoggerFactory.getLogger(AiCacheResource.class);

    private static final String ENTITY_NAME = "aiServiceAiCache";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AiCacheService aiCacheService;

    private final AiCacheRepository aiCacheRepository;

    public AiCacheResource(AiCacheService aiCacheService, AiCacheRepository aiCacheRepository) {
        this.aiCacheService = aiCacheService;
        this.aiCacheRepository = aiCacheRepository;
    }

    /**
     * {@code POST  /ai-caches} : Create a new aiCache.
     *
     * @param aiCacheDTO the aiCacheDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new aiCacheDTO, or with status {@code 400 (Bad Request)} if the aiCache has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AiCacheDTO> createAiCache(@Valid @RequestBody AiCacheDTO aiCacheDTO) throws URISyntaxException {
        LOG.debug("REST request to save AiCache : {}", aiCacheDTO);
        if (aiCacheDTO.getId() != null) {
            throw new BadRequestAlertException("A new aiCache cannot already have an ID", ENTITY_NAME, "idexists");
        }
        aiCacheDTO = aiCacheService.save(aiCacheDTO);
        return ResponseEntity.created(new URI("/api/ai-caches/" + aiCacheDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, aiCacheDTO.getId().toString()))
            .body(aiCacheDTO);
    }

    /**
     * {@code PUT  /ai-caches/:id} : Updates an existing aiCache.
     *
     * @param id the id of the aiCacheDTO to save.
     * @param aiCacheDTO the aiCacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiCacheDTO,
     * or with status {@code 400 (Bad Request)} if the aiCacheDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the aiCacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AiCacheDTO> updateAiCache(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AiCacheDTO aiCacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AiCache : {}, {}", id, aiCacheDTO);
        if (aiCacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiCacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiCacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        aiCacheDTO = aiCacheService.update(aiCacheDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aiCacheDTO.getId().toString()))
            .body(aiCacheDTO);
    }

    /**
     * {@code PATCH  /ai-caches/:id} : Partial updates given fields of an existing aiCache, field will ignore if it is null
     *
     * @param id the id of the aiCacheDTO to save.
     * @param aiCacheDTO the aiCacheDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated aiCacheDTO,
     * or with status {@code 400 (Bad Request)} if the aiCacheDTO is not valid,
     * or with status {@code 404 (Not Found)} if the aiCacheDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the aiCacheDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AiCacheDTO> partialUpdateAiCache(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AiCacheDTO aiCacheDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AiCache partially : {}, {}", id, aiCacheDTO);
        if (aiCacheDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, aiCacheDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!aiCacheRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AiCacheDTO> result = aiCacheService.partialUpdate(aiCacheDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, aiCacheDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /ai-caches} : get all the aiCaches.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of aiCaches in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AiCacheDTO>> getAllAiCaches(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of AiCaches");
        Page<AiCacheDTO> page = aiCacheService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ai-caches/:id} : get the "id" aiCache.
     *
     * @param id the id of the aiCacheDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the aiCacheDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AiCacheDTO> getAiCache(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AiCache : {}", id);
        Optional<AiCacheDTO> aiCacheDTO = aiCacheService.findOne(id);
        return ResponseUtil.wrapOrNotFound(aiCacheDTO);
    }

    /**
     * {@code DELETE  /ai-caches/:id} : delete the "id" aiCache.
     *
     * @param id the id of the aiCacheDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAiCache(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AiCache : {}", id);
        aiCacheService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
