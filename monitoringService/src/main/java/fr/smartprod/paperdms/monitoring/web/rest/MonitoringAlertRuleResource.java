package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.MonitoringAlertRuleQueryService;
import fr.smartprod.paperdms.monitoring.service.MonitoringAlertRuleService;
import fr.smartprod.paperdms.monitoring.service.criteria.MonitoringAlertRuleCriteria;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule}.
 */
@RestController
@RequestMapping("/api/monitoring-alert-rules")
public class MonitoringAlertRuleResource {

    private static final Logger LOG = LoggerFactory.getLogger(MonitoringAlertRuleResource.class);

    private static final String ENTITY_NAME = "monitoringServiceMonitoringAlertRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MonitoringAlertRuleService monitoringAlertRuleService;

    private final MonitoringAlertRuleRepository monitoringAlertRuleRepository;

    private final MonitoringAlertRuleQueryService monitoringAlertRuleQueryService;

    public MonitoringAlertRuleResource(
        MonitoringAlertRuleService monitoringAlertRuleService,
        MonitoringAlertRuleRepository monitoringAlertRuleRepository,
        MonitoringAlertRuleQueryService monitoringAlertRuleQueryService
    ) {
        this.monitoringAlertRuleService = monitoringAlertRuleService;
        this.monitoringAlertRuleRepository = monitoringAlertRuleRepository;
        this.monitoringAlertRuleQueryService = monitoringAlertRuleQueryService;
    }

    /**
     * {@code POST  /monitoring-alert-rules} : Create a new monitoringAlertRule.
     *
     * @param monitoringAlertRuleDTO the monitoringAlertRuleDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new monitoringAlertRuleDTO, or with status {@code 400 (Bad Request)} if the monitoringAlertRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<MonitoringAlertRuleDTO> createMonitoringAlertRule(
        @Valid @RequestBody MonitoringAlertRuleDTO monitoringAlertRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save MonitoringAlertRule : {}", monitoringAlertRuleDTO);
        if (monitoringAlertRuleDTO.getId() != null) {
            throw new BadRequestAlertException("A new monitoringAlertRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        monitoringAlertRuleDTO = monitoringAlertRuleService.save(monitoringAlertRuleDTO);
        return ResponseEntity.created(new URI("/api/monitoring-alert-rules/" + monitoringAlertRuleDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, monitoringAlertRuleDTO.getId().toString()))
            .body(monitoringAlertRuleDTO);
    }

    /**
     * {@code PUT  /monitoring-alert-rules/:id} : Updates an existing monitoringAlertRule.
     *
     * @param id the id of the monitoringAlertRuleDTO to save.
     * @param monitoringAlertRuleDTO the monitoringAlertRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringAlertRuleDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringAlertRuleDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the monitoringAlertRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<MonitoringAlertRuleDTO> updateMonitoringAlertRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody MonitoringAlertRuleDTO monitoringAlertRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update MonitoringAlertRule : {}, {}", id, monitoringAlertRuleDTO);
        if (monitoringAlertRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringAlertRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringAlertRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        monitoringAlertRuleDTO = monitoringAlertRuleService.update(monitoringAlertRuleDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringAlertRuleDTO.getId().toString()))
            .body(monitoringAlertRuleDTO);
    }

    /**
     * {@code PATCH  /monitoring-alert-rules/:id} : Partial updates given fields of an existing monitoringAlertRule, field will ignore if it is null
     *
     * @param id the id of the monitoringAlertRuleDTO to save.
     * @param monitoringAlertRuleDTO the monitoringAlertRuleDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated monitoringAlertRuleDTO,
     * or with status {@code 400 (Bad Request)} if the monitoringAlertRuleDTO is not valid,
     * or with status {@code 404 (Not Found)} if the monitoringAlertRuleDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the monitoringAlertRuleDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<MonitoringAlertRuleDTO> partialUpdateMonitoringAlertRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody MonitoringAlertRuleDTO monitoringAlertRuleDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update MonitoringAlertRule partially : {}, {}", id, monitoringAlertRuleDTO);
        if (monitoringAlertRuleDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, monitoringAlertRuleDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!monitoringAlertRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<MonitoringAlertRuleDTO> result = monitoringAlertRuleService.partialUpdate(monitoringAlertRuleDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, monitoringAlertRuleDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /monitoring-alert-rules} : get all the monitoringAlertRules.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of monitoringAlertRules in body.
     */
    @GetMapping("")
    public ResponseEntity<List<MonitoringAlertRuleDTO>> getAllMonitoringAlertRules(
        MonitoringAlertRuleCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get MonitoringAlertRules by criteria: {}", criteria);

        Page<MonitoringAlertRuleDTO> page = monitoringAlertRuleQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /monitoring-alert-rules/count} : count all the monitoringAlertRules.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countMonitoringAlertRules(MonitoringAlertRuleCriteria criteria) {
        LOG.debug("REST request to count MonitoringAlertRules by criteria: {}", criteria);
        return ResponseEntity.ok().body(monitoringAlertRuleQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /monitoring-alert-rules/:id} : get the "id" monitoringAlertRule.
     *
     * @param id the id of the monitoringAlertRuleDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the monitoringAlertRuleDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<MonitoringAlertRuleDTO> getMonitoringAlertRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to get MonitoringAlertRule : {}", id);
        Optional<MonitoringAlertRuleDTO> monitoringAlertRuleDTO = monitoringAlertRuleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(monitoringAlertRuleDTO);
    }

    /**
     * {@code DELETE  /monitoring-alert-rules/:id} : delete the "id" monitoringAlertRule.
     *
     * @param id the id of the monitoringAlertRuleDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMonitoringAlertRule(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete MonitoringAlertRule : {}", id);
        monitoringAlertRuleService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
