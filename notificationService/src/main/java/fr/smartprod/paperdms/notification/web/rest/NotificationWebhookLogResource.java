package fr.smartprod.paperdms.notification.web.rest;

import fr.smartprod.paperdms.notification.repository.NotificationWebhookLogRepository;
import fr.smartprod.paperdms.notification.service.NotificationWebhookLogQueryService;
import fr.smartprod.paperdms.notification.service.NotificationWebhookLogService;
import fr.smartprod.paperdms.notification.service.criteria.NotificationWebhookLogCriteria;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookLogDTO;
import fr.smartprod.paperdms.notification.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookLog}.
 */
@RestController
@RequestMapping("/api/notification-webhook-logs")
public class NotificationWebhookLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebhookLogResource.class);

    private static final String ENTITY_NAME = "notificationServiceNotificationWebhookLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationWebhookLogService notificationWebhookLogService;

    private final NotificationWebhookLogRepository notificationWebhookLogRepository;

    private final NotificationWebhookLogQueryService notificationWebhookLogQueryService;

    public NotificationWebhookLogResource(
        NotificationWebhookLogService notificationWebhookLogService,
        NotificationWebhookLogRepository notificationWebhookLogRepository,
        NotificationWebhookLogQueryService notificationWebhookLogQueryService
    ) {
        this.notificationWebhookLogService = notificationWebhookLogService;
        this.notificationWebhookLogRepository = notificationWebhookLogRepository;
        this.notificationWebhookLogQueryService = notificationWebhookLogQueryService;
    }

    /**
     * {@code POST  /notification-webhook-logs} : Create a new notificationWebhookLog.
     *
     * @param notificationWebhookLogDTO the notificationWebhookLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationWebhookLogDTO, or with status {@code 400 (Bad Request)} if the notificationWebhookLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationWebhookLogDTO> createNotificationWebhookLog(
        @Valid @RequestBody NotificationWebhookLogDTO notificationWebhookLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save NotificationWebhookLog : {}", notificationWebhookLogDTO);
        if (notificationWebhookLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationWebhookLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationWebhookLogDTO = notificationWebhookLogService.save(notificationWebhookLogDTO);
        return ResponseEntity.created(new URI("/api/notification-webhook-logs/" + notificationWebhookLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationWebhookLogDTO.getId().toString()))
            .body(notificationWebhookLogDTO);
    }

    /**
     * {@code PUT  /notification-webhook-logs/:id} : Updates an existing notificationWebhookLog.
     *
     * @param id the id of the notificationWebhookLogDTO to save.
     * @param notificationWebhookLogDTO the notificationWebhookLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationWebhookLogDTO,
     * or with status {@code 400 (Bad Request)} if the notificationWebhookLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationWebhookLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationWebhookLogDTO> updateNotificationWebhookLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationWebhookLogDTO notificationWebhookLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationWebhookLog : {}, {}", id, notificationWebhookLogDTO);
        if (notificationWebhookLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationWebhookLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationWebhookLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationWebhookLogDTO = notificationWebhookLogService.update(notificationWebhookLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationWebhookLogDTO.getId().toString()))
            .body(notificationWebhookLogDTO);
    }

    /**
     * {@code PATCH  /notification-webhook-logs/:id} : Partial updates given fields of an existing notificationWebhookLog, field will ignore if it is null
     *
     * @param id the id of the notificationWebhookLogDTO to save.
     * @param notificationWebhookLogDTO the notificationWebhookLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationWebhookLogDTO,
     * or with status {@code 400 (Bad Request)} if the notificationWebhookLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationWebhookLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationWebhookLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationWebhookLogDTO> partialUpdateNotificationWebhookLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationWebhookLogDTO notificationWebhookLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NotificationWebhookLog partially : {}, {}", id, notificationWebhookLogDTO);
        if (notificationWebhookLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationWebhookLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationWebhookLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationWebhookLogDTO> result = notificationWebhookLogService.partialUpdate(notificationWebhookLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationWebhookLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-webhook-logs} : get all the notificationWebhookLogs.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationWebhookLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificationWebhookLogDTO>> getAllNotificationWebhookLogs(
        NotificationWebhookLogCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NotificationWebhookLogs by criteria: {}", criteria);

        Page<NotificationWebhookLogDTO> page = notificationWebhookLogQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-webhook-logs/count} : count all the notificationWebhookLogs.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotificationWebhookLogs(NotificationWebhookLogCriteria criteria) {
        LOG.debug("REST request to count NotificationWebhookLogs by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationWebhookLogQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-webhook-logs/:id} : get the "id" notificationWebhookLog.
     *
     * @param id the id of the notificationWebhookLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationWebhookLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationWebhookLogDTO> getNotificationWebhookLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NotificationWebhookLog : {}", id);
        Optional<NotificationWebhookLogDTO> notificationWebhookLogDTO = notificationWebhookLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationWebhookLogDTO);
    }

    /**
     * {@code DELETE  /notification-webhook-logs/:id} : delete the "id" notificationWebhookLog.
     *
     * @param id the id of the notificationWebhookLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationWebhookLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NotificationWebhookLog : {}", id);
        notificationWebhookLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
