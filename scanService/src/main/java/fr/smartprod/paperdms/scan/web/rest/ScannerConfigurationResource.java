package fr.smartprod.paperdms.scan.web.rest;

import fr.smartprod.paperdms.scan.repository.ScannerConfigurationRepository;
import fr.smartprod.paperdms.scan.service.ScannerConfigurationService;
import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import fr.smartprod.paperdms.scan.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.scan.domain.ScannerConfiguration}.
 */
@RestController
@RequestMapping("/api/scanner-configurations")
public class ScannerConfigurationResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScannerConfigurationResource.class);

    private static final String ENTITY_NAME = "scanServiceScannerConfiguration";

    @Value("${jhipster.clientApp.name:scanService}")
    private String applicationName;

    private final ScannerConfigurationService scannerConfigurationService;

    private final ScannerConfigurationRepository scannerConfigurationRepository;

    public ScannerConfigurationResource(
        ScannerConfigurationService scannerConfigurationService,
        ScannerConfigurationRepository scannerConfigurationRepository
    ) {
        this.scannerConfigurationService = scannerConfigurationService;
        this.scannerConfigurationRepository = scannerConfigurationRepository;
    }

    /**
     * {@code POST  /scanner-configurations} : Create a new scannerConfiguration.
     *
     * @param scannerConfigurationDTO the scannerConfigurationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scannerConfigurationDTO, or with status {@code 400 (Bad Request)} if the scannerConfiguration has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScannerConfigurationDTO> createScannerConfiguration(
        @Valid @RequestBody ScannerConfigurationDTO scannerConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ScannerConfiguration : {}", scannerConfigurationDTO);
        if (scannerConfigurationDTO.getId() != null) {
            throw new BadRequestAlertException("A new scannerConfiguration cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scannerConfigurationDTO = scannerConfigurationService.save(scannerConfigurationDTO);
        return ResponseEntity.created(new URI("/api/scanner-configurations/" + scannerConfigurationDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scannerConfigurationDTO.getId().toString()))
            .body(scannerConfigurationDTO);
    }

    /**
     * {@code PUT  /scanner-configurations/:id} : Updates an existing scannerConfiguration.
     *
     * @param id the id of the scannerConfigurationDTO to save.
     * @param scannerConfigurationDTO the scannerConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scannerConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the scannerConfigurationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scannerConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScannerConfigurationDTO> updateScannerConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScannerConfigurationDTO scannerConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScannerConfiguration : {}, {}", id, scannerConfigurationDTO);
        if (scannerConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scannerConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scannerConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scannerConfigurationDTO = scannerConfigurationService.update(scannerConfigurationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scannerConfigurationDTO.getId().toString()))
            .body(scannerConfigurationDTO);
    }

    /**
     * {@code PATCH  /scanner-configurations/:id} : Partial updates given fields of an existing scannerConfiguration, field will ignore if it is null
     *
     * @param id the id of the scannerConfigurationDTO to save.
     * @param scannerConfigurationDTO the scannerConfigurationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scannerConfigurationDTO,
     * or with status {@code 400 (Bad Request)} if the scannerConfigurationDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scannerConfigurationDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scannerConfigurationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScannerConfigurationDTO> partialUpdateScannerConfiguration(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScannerConfigurationDTO scannerConfigurationDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScannerConfiguration partially : {}, {}", id, scannerConfigurationDTO);
        if (scannerConfigurationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scannerConfigurationDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scannerConfigurationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScannerConfigurationDTO> result = scannerConfigurationService.partialUpdate(scannerConfigurationDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scannerConfigurationDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scanner-configurations} : get all the scannerConfigurations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scannerConfigurations in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScannerConfigurationDTO>> getAllScannerConfigurations(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ScannerConfigurations");
        Page<ScannerConfigurationDTO> page = scannerConfigurationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scanner-configurations/:id} : get the "id" scannerConfiguration.
     *
     * @param id the id of the scannerConfigurationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scannerConfigurationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScannerConfigurationDTO> getScannerConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScannerConfiguration : {}", id);
        Optional<ScannerConfigurationDTO> scannerConfigurationDTO = scannerConfigurationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scannerConfigurationDTO);
    }

    /**
     * {@code DELETE  /scanner-configurations/:id} : delete the "id" scannerConfiguration.
     *
     * @param id the id of the scannerConfigurationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScannerConfiguration(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScannerConfiguration : {}", id);
        scannerConfigurationService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
