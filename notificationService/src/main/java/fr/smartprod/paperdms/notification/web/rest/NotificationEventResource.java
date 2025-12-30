package fr.smartprod.paperdms.notification.web.rest;

import fr.smartprod.paperdms.notification.repository.NotificationEventRepository;
import fr.smartprod.paperdms.notification.service.NotificationEventQueryService;
import fr.smartprod.paperdms.notification.service.NotificationEventService;
import fr.smartprod.paperdms.notification.service.criteria.NotificationEventCriteria;
import fr.smartprod.paperdms.notification.service.dto.NotificationEventDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.notification.domain.NotificationEvent}.
 */
@RestController
@RequestMapping("/api/notification-events")
public class NotificationEventResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationEventResource.class);

    private static final String ENTITY_NAME = "notificationServiceNotificationEvent";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationEventService notificationEventService;

    private final NotificationEventRepository notificationEventRepository;

    private final NotificationEventQueryService notificationEventQueryService;

    public NotificationEventResource(
        NotificationEventService notificationEventService,
        NotificationEventRepository notificationEventRepository,
        NotificationEventQueryService notificationEventQueryService
    ) {
        this.notificationEventService = notificationEventService;
        this.notificationEventRepository = notificationEventRepository;
        this.notificationEventQueryService = notificationEventQueryService;
    }

    /**
     * {@code POST  /notification-events} : Create a new notificationEvent.
     *
     * @param notificationEventDTO the notificationEventDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationEventDTO, or with status {@code 400 (Bad Request)} if the notificationEvent has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationEventDTO> createNotificationEvent(@Valid @RequestBody NotificationEventDTO notificationEventDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save NotificationEvent : {}", notificationEventDTO);
        if (notificationEventDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationEvent cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationEventDTO = notificationEventService.save(notificationEventDTO);
        return ResponseEntity.created(new URI("/api/notification-events/" + notificationEventDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, notificationEventDTO.getId().toString()))
            .body(notificationEventDTO);
    }

    /**
     * {@code PUT  /notification-events/:id} : Updates an existing notificationEvent.
     *
     * @param id the id of the notificationEventDTO to save.
     * @param notificationEventDTO the notificationEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationEventDTO,
     * or with status {@code 400 (Bad Request)} if the notificationEventDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationEventDTO> updateNotificationEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationEventDTO notificationEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationEvent : {}, {}", id, notificationEventDTO);
        if (notificationEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationEventDTO = notificationEventService.update(notificationEventDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationEventDTO.getId().toString()))
            .body(notificationEventDTO);
    }

    /**
     * {@code PATCH  /notification-events/:id} : Partial updates given fields of an existing notificationEvent, field will ignore if it is null
     *
     * @param id the id of the notificationEventDTO to save.
     * @param notificationEventDTO the notificationEventDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationEventDTO,
     * or with status {@code 400 (Bad Request)} if the notificationEventDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationEventDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationEventDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationEventDTO> partialUpdateNotificationEvent(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationEventDTO notificationEventDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update NotificationEvent partially : {}, {}", id, notificationEventDTO);
        if (notificationEventDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationEventDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationEventRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationEventDTO> result = notificationEventService.partialUpdate(notificationEventDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationEventDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-events} : get all the notificationEvents.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationEvents in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificationEventDTO>> getAllNotificationEvents(
        NotificationEventCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NotificationEvents by criteria: {}", criteria);

        Page<NotificationEventDTO> page = notificationEventQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-events/count} : count all the notificationEvents.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotificationEvents(NotificationEventCriteria criteria) {
        LOG.debug("REST request to count NotificationEvents by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationEventQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-events/:id} : get the "id" notificationEvent.
     *
     * @param id the id of the notificationEventDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationEventDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationEventDTO> getNotificationEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NotificationEvent : {}", id);
        Optional<NotificationEventDTO> notificationEventDTO = notificationEventService.findOne(id);
        return ResponseUtil.wrapOrNotFound(notificationEventDTO);
    }

    /**
     * {@code DELETE  /notification-events/:id} : delete the "id" notificationEvent.
     *
     * @param id the id of the notificationEventDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationEvent(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NotificationEvent : {}", id);
        notificationEventService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
