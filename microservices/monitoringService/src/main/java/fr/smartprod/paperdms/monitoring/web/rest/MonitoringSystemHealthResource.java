package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringSystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringSystemHealthQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringSystemHealthService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringSystemHealthCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringSystemHealthDTO;
import fr.smartprod.paperdms.monitoring.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth}.
 */
@RestController
@RequestMapping("/api/monitoring-system-healths")
public class MonitoringSystemHealthResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringSystemHealthResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringSystemHealth";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringSystemHealthService monitoringSystemHealthService;

    private final MonitoringSystemHealthRepository monitoringSystemHealthRepository;

    private final MonitoringSystemHealthQueryService monitoringSystemHealthQueryService;

    public MonitoringSystemHealthResource(
        MonitoringSystemHealthService monitoringSystemHealthService,
        MonitoringSystemHealthRepository monitoringSystemHealthRepository,
        MonitoringSystemHealthQueryService monitoringSystemHealthQueryService
    ) {
        this.monitoringSystemHealthService = monitoringSystemHealthService;
        this.monitoringSystemHealthRepository = monitoringSystemHealthRepository;
        this.monitoringSystemHealthQueryService = monitoringSystemHealthQueryService;
    }

    /**
     * {@code POST  /monitoring-system-healths} : Create a new monitoringSystemHealth.
     *
     * @param monitoringSystemHealthDTO the monitoringSystemHealthDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringSystemHealthDTO, or with status {@code 400 (Bad Request)} if the monitoringSystemHealth has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringSystemHealthDTO> createMonitoringSystemHealth(
        @Valid @RequestBody MonitoringSystemHealthDTO monitoringSystemHealthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MonitoringSystemHealth : {}", monitoringSystemHealthDTO);
        if (monitoringSystemHealthDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringSystemHealth cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringSystemHealthDTO = monitoringSystemHealthService.save(monitoringSystemHealthDTO);
        return ResponseEntity.created(new URI("/api/monitoring-system-healths/" + monitoringSystemHealthDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringSystemHealthDTO.getId().toString()))
            .body(monitoringSystemHealthDTO);
    }

    /**
     * {@code PUT  /monitoring-system-healths/:id} : Updates an existing monitoringSystemHealth.
     *
     * @param id the id of the monitoringSystemHealthDTO to save.
     * @param monitoringSystemHealthDTO the monitoringSystemHealthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringSystemHealthDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringSystemHealthDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringSystemHealthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringSystemHealthDTO> updateMonitoringSystemHealth(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringSystemHealthDTO monitoringSystemHealthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringSystemHealth : {}, {}", id, monitoringSystemHealthDTO);
        if (monitoringSystemHealthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringSystemHealthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringSystemHealthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringSystemHealthDTO = monitoringSystemHealthService.update(monitoringSystemHealthDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringSystemHealthDTO.getId().toString()))
            .body(monitoringSystemHealthDTO);
    }

    /**
     * {@code PATCH  /monitoring-system-healths/:id} : Partial updates given fields of an existing monitoringSystemHealth, field will ignore if it is null
     *
     * @param id the id of the monitoringSystemHealthDTO to save.
     * @param monitoringSystemHealthDTO the monitoringSystemHealthDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringSystemHealthDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringSystemHealthDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringSystemHealthDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringSystemHealthDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringSystemHealthDTO> partialUpdateMonitoringSystemHealth(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringSystemHealthDTO monitoringSystemHealthDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringSystemHealth partially : {}, {}", id, monitoringSystemHealthDTO);
        if (monitoringSystemHealthDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringSystemHealthDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringSystemHealthRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringSystemHealthDTO> result = monitoringSystemHealthService.partialUpdate(monitoringSystemHealthDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringSystemHealthDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-system-healths} : get all the monitoringSystemHealths.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringSystemHealths in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringSystemHealthDTO>> getAllMonitoringSystemHealths(
        MonitoringSystemHealthCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringSystemHealths by criteria: {}", criteria);

        Page<MonitoringSystemHealthDTO> page = monitoringSystemHealthQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-system-healths/count} : count all the monitoringSystemHealths.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringSystemHealths(MonitoringSystemHealthCriteria criteria) {
        LOG.debug("REST request to count MonitoringSystemHealths by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringSystemHealthQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-system-healths/:id} : get the "id" monitoringSystemHealth.
     *
     * @param id the id of the monitoringSystemHealthDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringSystemHealthDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringSystemHealthDTO> getMonitoringSystemHealth(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringSystemHealth : {}", id);
        Optional<MonitoringSystemHealthDTO> monitoringSystemHealthDTO = monitoringSystemHealthService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringSystemHealthDTO);
    }

    /**
     * {@code DELETE  /monitoring-system-healths/:id} : delete the "id" monitoringSystemHealth.
     *
     * @param id the id of the monitoringSystemHealthDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringSystemHealth(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringSystemHealth : {}", id);
        monitoringSystemHealthService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
