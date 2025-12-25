package fr.smartprod.paperdms.monitoring.web.rest;

import fr.smartprod.paperdms.monitoring.repository.ServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.ServiceStatusService;
import fr.smartprod.paperdms.monitoring.service.dto.ServiceStatusDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.monitoring.domain.ServiceStatus}.
 */
@RestController
@RequestMapping("/api/service-statuses")
public class ServiceStatusResource {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceStatusResource.class);

    private static final String ENTITY_NAME = "monitoringServiceServiceStatus";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ServiceStatusService serviceStatusService;

    private final ServiceStatusRepository serviceStatusRepository;

    public ServiceStatusResource(ServiceStatusService serviceStatusService, ServiceStatusRepository serviceStatusRepository) {
        this.serviceStatusService = serviceStatusService;
        this.serviceStatusRepository = serviceStatusRepository;
    }

    /**
     * {@code POST  /service-statuses} : Create a new serviceStatus.
     *
     * @param serviceStatusDTO the serviceStatusDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new serviceStatusDTO, or with status {@code 400 (Bad Request)} if the serviceStatus has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ServiceStatusDTO> createServiceStatus(@Valid @RequestBody ServiceStatusDTO serviceStatusDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ServiceStatus : {}", serviceStatusDTO);
        if (serviceStatusDTO.getId() != null) {
            throw new BadRequestAlertException("A new serviceStatus cannot already have an ID", ENTITY_NAME, "idexists");
        }
        serviceStatusDTO = serviceStatusService.save(serviceStatusDTO);
        return ResponseEntity.created(new URI("/api/service-statuses/" + serviceStatusDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, serviceStatusDTO.getId().toString()))
            .body(serviceStatusDTO);
    }

    /**
     * {@code PUT  /service-statuses/:id} : Updates an existing serviceStatus.
     *
     * @param id the id of the serviceStatusDTO to save.
     * @param serviceStatusDTO the serviceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the serviceStatusDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the serviceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ServiceStatusDTO> updateServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ServiceStatusDTO serviceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ServiceStatus : {}, {}", id, serviceStatusDTO);
        if (serviceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        serviceStatusDTO = serviceStatusService.update(serviceStatusDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceStatusDTO.getId().toString()))
            .body(serviceStatusDTO);
    }

    /**
     * {@code PATCH  /service-statuses/:id} : Partial updates given fields of an existing serviceStatus, field will ignore if it is null
     *
     * @param id the id of the serviceStatusDTO to save.
     * @param serviceStatusDTO the serviceStatusDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated serviceStatusDTO,
     * or with status {@code 400 (Bad Request)} if the serviceStatusDTO is not valid,
     * or with status {@code 404 (Not Found)} if the serviceStatusDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the serviceStatusDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ServiceStatusDTO> partialUpdateServiceStatus(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ServiceStatusDTO serviceStatusDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ServiceStatus partially : {}, {}", id, serviceStatusDTO);
        if (serviceStatusDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, serviceStatusDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!serviceStatusRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ServiceStatusDTO> result = serviceStatusService.partialUpdate(serviceStatusDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, serviceStatusDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /service-statuses} : get all the serviceStatuses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of serviceStatuses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ServiceStatusDTO>> getAllServiceStatuses(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ServiceStatuses");
        Page<ServiceStatusDTO> page = serviceStatusService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /service-statuses/:id} : get the "id" serviceStatus.
     *
     * @param id the id of the serviceStatusDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the serviceStatusDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ServiceStatusDTO> getServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ServiceStatus : {}", id);
        Optional<ServiceStatusDTO> serviceStatusDTO = serviceStatusService.findOne(id);
        return ResponseUtil.wrapOrNotFound(serviceStatusDTO);
    }

    /**
     * {@code DELETE  /service-statuses/:id} : delete the "id" serviceStatus.
     *
     * @param id the id of the serviceStatusDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceStatus(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ServiceStatus : {}", id);
        serviceStatusService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
