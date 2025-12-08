package com.ged.ocr.web.rest;

import com.ged.ocr.repository.TikaConfigurationRepository;
import com.ged.ocr.service.TikaConfigurationService;
import com.ged.ocr.service.dto.TikaConfigurationDTO;
import com.ged.ocr.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.ged.ocr.domain.TikaConfiguration}.
 */
@RestController
@RequestMapping("/api/tika-configurations")
public class TikaConfigurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(TikaConfigurationResource.class);

    private static final String ENTITY_NAME = "ocrServiceTikaConfiguration";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TikaConfigurationService tikaConfigurationService;

    private final TikaConfigurationRepository tikaConfigurationRepository;

    public TikaConfigurationResource(
        TikaConfigurationService tikaConfigurationService,
        TikaConfigurationRepository tikaConfigurationRepository
    ) {
        this.tikaConfigurationService = tikaConfigurationService;
        this.tikaConfigurationRepository = tikaConfigurationRepository;
    }

    /**
     * {@code POST  /tika-configurations} : Create a new tikaConfiguration.
     *
     * @param tikaConfigurationDTO the tikaConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tikaConfigurationDTO, or with status {@code 400 (Bad Request)} if the tikaConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TikaConfigurationDTO> createTikaConfiguration(@Valid @RequestBody TikaConfigurationDTO tikaConfigurationDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save TikaConfiguration : {}", tikaConfigurationDTO);
        if (tikaConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new tikaConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tikaConfigurationDTO = tikaConfigurationService.save(tikaConfigurationDTO);
        return ResponseEntity.created(new URI("/api/tika-configurations/" + tikaConfigurationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tikaConfigurationDTO.getId().toString()))
            .body(tikaConfigurationDTO);
    }

    /**
     * {@code PUT  /tika-configurations/:id} : Updates an existing tikaConfiguration.
     *
     * @param id the id of the tikaConfigurationDTO to save.
     * @param tikaConfigurationDTO the tikaConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tikaConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the tikaConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tikaConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TikaConfigurationDTO> updateTikaConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TikaConfigurationDTO tikaConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TikaConfiguration : {}, {}", id, tikaConfigurationDTO);
        if (tikaConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tikaConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tikaConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tikaConfigurationDTO = tikaConfigurationService.update(tikaConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tikaConfigurationDTO.getId().toString()))
            .body(tikaConfigurationDTO);
    }

    /**
     * {@code PATCH  /tika-configurations/:id} : Partial updates given fields of an existing tikaConfiguration, field will ignore if it is null
     *
     * @param id the id of the tikaConfigurationDTO to save.
     * @param tikaConfigurationDTO the tikaConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tikaConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the tikaConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tikaConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tikaConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TikaConfigurationDTO> partialUpdateTikaConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TikaConfigurationDTO tikaConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TikaConfiguration partially : {}, {}", id, tikaConfigurationDTO);
        if (tikaConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tikaConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tikaConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TikaConfigurationDTO> result = tikaConfigurationService.partialUpdate(tikaConfigurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tikaConfigurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tika-configurations} : get all the tikaConfigurations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tikaConfigurations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TikaConfigurationDTO>> getAllTikaConfigurations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of TikaConfigurations");
        Page<TikaConfigurationDTO> page = tikaConfigurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tika-configurations/:id} : get the "id" tikaConfiguration.
     *
     * @param id the id of the tikaConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tikaConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TikaConfigurationDTO> getTikaConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TikaConfiguration : {}", id);
        Optional<TikaConfigurationDTO> tikaConfigurationDTO = tikaConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tikaConfigurationDTO);
    }

    /**
     * {@code DELETE  /tika-configurations/:id} : delete the "id" tikaConfiguration.
     *
     * @param id the id of the tikaConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTikaConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TikaConfiguration : {}", id);
        tikaConfigurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
