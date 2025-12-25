package fr.smartprod.paperdms.reporting.web.rest;

import fr.smartprod.paperdms.reporting.repository.DashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.DashboardWidgetService;
import fr.smartprod.paperdms.reporting.service.dto.DashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.reporting.domain.DashboardWidget}.
 */
@RestController
@RequestMapping("/api/dashboard-widgets")
public class DashboardWidgetResource {

    private static final Logger LOG = LoggerFactory.getLogger(DashboardWidgetResource.class);

    private static final String ENTITY_NAME = "reportingServiceDashboardWidget";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DashboardWidgetService dashboardWidgetService;

    private final DashboardWidgetRepository dashboardWidgetRepository;

    public DashboardWidgetResource(DashboardWidgetService dashboardWidgetService, DashboardWidgetRepository dashboardWidgetRepository) {
        this.dashboardWidgetService = dashboardWidgetService;
        this.dashboardWidgetRepository = dashboardWidgetRepository;
    }

    /**
     * {@code POST  /dashboard-widgets} : Create a new dashboardWidget.
     *
     * @param dashboardWidgetDTO the dashboardWidgetDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dashboardWidgetDTO, or with status {@code 400 (Bad Request)} if the dashboardWidget has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DashboardWidgetDTO> createDashboardWidget(@Valid @RequestBody DashboardWidgetDTO dashboardWidgetDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save DashboardWidget : {}", dashboardWidgetDTO);
        if (dashboardWidgetDTO.getId() != null) {
            throw new BadRequestAlertException("A new dashboardWidget cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dashboardWidgetDTO = dashboardWidgetService.save(dashboardWidgetDTO);
        return ResponseEntity.created(new URI("/api/dashboard-widgets/" + dashboardWidgetDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dashboardWidgetDTO.getId().toString()))
            .body(dashboardWidgetDTO);
    }

    /**
     * {@code PUT  /dashboard-widgets/:id} : Updates an existing dashboardWidget.
     *
     * @param id the id of the dashboardWidgetDTO to save.
     * @param dashboardWidgetDTO the dashboardWidgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboardWidgetDTO,
     * or with status {@code 400 (Bad Request)} if the dashboardWidgetDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dashboardWidgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DashboardWidgetDTO> updateDashboardWidget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DashboardWidgetDTO dashboardWidgetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update DashboardWidget : {}, {}", id, dashboardWidgetDTO);
        if (dashboardWidgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboardWidgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardWidgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dashboardWidgetDTO = dashboardWidgetService.update(dashboardWidgetDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dashboardWidgetDTO.getId().toString()))
            .body(dashboardWidgetDTO);
    }

    /**
     * {@code PATCH  /dashboard-widgets/:id} : Partial updates given fields of an existing dashboardWidget, field will ignore if it is null
     *
     * @param id the id of the dashboardWidgetDTO to save.
     * @param dashboardWidgetDTO the dashboardWidgetDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dashboardWidgetDTO,
     * or with status {@code 400 (Bad Request)} if the dashboardWidgetDTO is not valid,
     * or with status {@code 404 (Not Found)} if the dashboardWidgetDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the dashboardWidgetDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DashboardWidgetDTO> partialUpdateDashboardWidget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DashboardWidgetDTO dashboardWidgetDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DashboardWidget partially : {}, {}", id, dashboardWidgetDTO);
        if (dashboardWidgetDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dashboardWidgetDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dashboardWidgetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DashboardWidgetDTO> result = dashboardWidgetService.partialUpdate(dashboardWidgetDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dashboardWidgetDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /dashboard-widgets} : get all the dashboardWidgets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dashboardWidgets in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DashboardWidgetDTO>> getAllDashboardWidgets(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of DashboardWidgets");
        Page<DashboardWidgetDTO> page = dashboardWidgetService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /dashboard-widgets/:id} : get the "id" dashboardWidget.
     *
     * @param id the id of the dashboardWidgetDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dashboardWidgetDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DashboardWidgetDTO> getDashboardWidget(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DashboardWidget : {}", id);
        Optional<DashboardWidgetDTO> dashboardWidgetDTO = dashboardWidgetService.findOne(id);
        return ResponseUtil.wrapOrNotFound(dashboardWidgetDTO);
    }

    /**
     * {@code DELETE  /dashboard-widgets/:id} : delete the "id" dashboardWidget.
     *
     * @param id the id of the dashboardWidgetDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDashboardWidget(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DashboardWidget : {}", id);
        dashboardWidgetService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
