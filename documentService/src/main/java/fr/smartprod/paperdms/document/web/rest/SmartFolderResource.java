package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.SmartFolderRepository;
import fr.smartprod.paperdms.document.service.SmartFolderQueryService;
import fr.smartprod.paperdms.document.service.SmartFolderService;
import fr.smartprod.paperdms.document.service.criteria.SmartFolderCriteria;
import fr.smartprod.paperdms.document.service.dto.SmartFolderDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.SmartFolder}.
 */
@RestController
@RequestMapping("/api/smart-folders")
public class SmartFolderResource {

    private static final Logger LOG = LoggerFactory.getLogger(SmartFolderResource.class);

    private static final String ENTITY_NAME = "documentServiceSmartFolder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SmartFolderService smartFolderService;

    private final SmartFolderRepository smartFolderRepository;

    private final SmartFolderQueryService smartFolderQueryService;

    public SmartFolderResource(
        SmartFolderService smartFolderService,
        SmartFolderRepository smartFolderRepository,
        SmartFolderQueryService smartFolderQueryService
    ) {
        this.smartFolderService = smartFolderService;
        this.smartFolderRepository = smartFolderRepository;
        this.smartFolderQueryService = smartFolderQueryService;
    }

    /**
     * {@code POST  /smart-folders} : Create a new smartFolder.
     *
     * @param smartFolderDTO the smartFolderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new smartFolderDTO, or with status {@code 400 (Bad Request)} if the smartFolder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SmartFolderDTO> createSmartFolder(@Valid @RequestBody SmartFolderDTO smartFolderDTO) throws URISyntaxException {
        LOG.debug("REST request to save SmartFolder : {}", smartFolderDTO);
        if (smartFolderDTO.getId() != null) {
            throw new BadRequestAlertException("A new smartFolder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        smartFolderDTO = smartFolderService.save(smartFolderDTO);
        return ResponseEntity.created(new URI("/api/smart-folders/" + smartFolderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, smartFolderDTO.getId().toString()))
            .body(smartFolderDTO);
    }

    /**
     * {@code PUT  /smart-folders/:id} : Updates an existing smartFolder.
     *
     * @param id the id of the smartFolderDTO to save.
     * @param smartFolderDTO the smartFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smartFolderDTO,
     * or with status {@code 400 (Bad Request)} if the smartFolderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the smartFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<SmartFolderDTO> updateSmartFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody SmartFolderDTO smartFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update SmartFolder : {}, {}", id, smartFolderDTO);
        if (smartFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smartFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smartFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        smartFolderDTO = smartFolderService.update(smartFolderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smartFolderDTO.getId().toString()))
            .body(smartFolderDTO);
    }

    /**
     * {@code PATCH  /smart-folders/:id} : Partial updates given fields of an existing smartFolder, field will ignore if it is null
     *
     * @param id the id of the smartFolderDTO to save.
     * @param smartFolderDTO the smartFolderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated smartFolderDTO,
     * or with status {@code 400 (Bad Request)} if the smartFolderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the smartFolderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the smartFolderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<SmartFolderDTO> partialUpdateSmartFolder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody SmartFolderDTO smartFolderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update SmartFolder partially : {}, {}", id, smartFolderDTO);
        if (smartFolderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, smartFolderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!smartFolderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<SmartFolderDTO> result = smartFolderService.partialUpdate(smartFolderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, smartFolderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /smart-folders} : get all the smartFolders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of smartFolders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SmartFolderDTO>> getAllSmartFolders(
        SmartFolderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get SmartFolders by criteria: {}", criteria);

        Page<SmartFolderDTO> page = smartFolderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /smart-folders/count} : count all the smartFolders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countSmartFolders(SmartFolderCriteria criteria) {
        LOG.debug("REST request to count SmartFolders by criteria: {}", criteria);
        return ResponseEntity.ok().body(smartFolderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /smart-folders/:id} : get the "id" smartFolder.
     *
     * @param id the id of the smartFolderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the smartFolderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SmartFolderDTO> getSmartFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SmartFolder : {}", id);
        Optional<SmartFolderDTO> smartFolderDTO = smartFolderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(smartFolderDTO);
    }

    /**
     * {@code DELETE  /smart-folders/:id} : delete the "id" smartFolder.
     *
     * @param id the id of the smartFolderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSmartFolder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SmartFolder : {}", id);
        smartFolderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
