package fr.smartprod.paperdms.notification.web.rest;

import fr.smartprod.paperdms.notification.repository.NotificationWebhookSubscriptionRepository;
import fr.smartprod.paperdms.notification.service.NotificationWebhookSubscriptionQueryService;
import fr.smartprod.paperdms.notification.service.NotificationWebhookSubscriptionService;
import fr.smartprod.paperdms.notification.service.criteria.NotificationWebhookSubscriptionCriteria;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription}.
 */
@RestController
@RequestMapping("/api/notification-webhook-subscriptions")
public class NotificationWebhookSubscriptionResource {

    private static final Logger LOG = LoggerFactory.getLogger(NotificationWebhookSubscriptionResource.class);

    private static final String ENTITY_NAME = "notificationServiceNotificationWebhookSubscription";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NotificationWebhookSubscriptionService notificationWebhookSubscriptionService;

    private final NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository;

    private final NotificationWebhookSubscriptionQueryService notificationWebhookSubscriptionQueryService;

    public NotificationWebhookSubscriptionResource(
        NotificationWebhookSubscriptionService notificationWebhookSubscriptionService,
        NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository,
        NotificationWebhookSubscriptionQueryService notificationWebhookSubscriptionQueryService
    ) {
        this.notificationWebhookSubscriptionService = notificationWebhookSubscriptionService;
        this.notificationWebhookSubscriptionRepository = notificationWebhookSubscriptionRepository;
        this.notificationWebhookSubscriptionQueryService = notificationWebhookSubscriptionQueryService;
    }

    /**
     * {@code POST  /notification-webhook-subscriptions} : Create a new notificationWebhookSubscription.
     *
     * @param notificationWebhookSubscriptionDTO the notificationWebhookSubscriptionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new notificationWebhookSubscriptionDTO, or with status {@code 400 (Bad Request)} if the notificationWebhookSubscription has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<NotificationWebhookSubscriptionDTO> createNotificationWebhookSubscription(
        @Valid @RequestBody NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save NotificationWebhookSubscription : {}", notificationWebhookSubscriptionDTO);
        if (notificationWebhookSubscriptionDTO.getId() != null) {
            throw new BadRequestAlertException("A new notificationWebhookSubscription cannot already have an ID", ENTITY_NAME, "idexists");
        }
        notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionService.save(notificationWebhookSubscriptionDTO);
        return ResponseEntity.created(new URI("/api/notification-webhook-subscriptions/" + notificationWebhookSubscriptionDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    notificationWebhookSubscriptionDTO.getId().toString()
                )
            )
            .body(notificationWebhookSubscriptionDTO);
    }

    /**
     * {@code PUT  /notification-webhook-subscriptions/:id} : Updates an existing notificationWebhookSubscription.
     *
     * @param id the id of the notificationWebhookSubscriptionDTO to save.
     * @param notificationWebhookSubscriptionDTO the notificationWebhookSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationWebhookSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the notificationWebhookSubscriptionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the notificationWebhookSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<NotificationWebhookSubscriptionDTO> updateNotificationWebhookSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update NotificationWebhookSubscription : {}, {}", id, notificationWebhookSubscriptionDTO);
        if (notificationWebhookSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationWebhookSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationWebhookSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionService.update(notificationWebhookSubscriptionDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(
                    applicationName,
                    true,
                    ENTITY_NAME,
                    notificationWebhookSubscriptionDTO.getId().toString()
                )
            )
            .body(notificationWebhookSubscriptionDTO);
    }

    /**
     * {@code PATCH  /notification-webhook-subscriptions/:id} : Partial updates given fields of an existing notificationWebhookSubscription, field will ignore if it is null
     *
     * @param id the id of the notificationWebhookSubscriptionDTO to save.
     * @param notificationWebhookSubscriptionDTO the notificationWebhookSubscriptionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated notificationWebhookSubscriptionDTO,
     * or with status {@code 400 (Bad Request)} if the notificationWebhookSubscriptionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the notificationWebhookSubscriptionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the notificationWebhookSubscriptionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NotificationWebhookSubscriptionDTO> partialUpdateNotificationWebhookSubscription(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO
    ) throws URISyntaxException {
        LOG.debug(
            "REST request to partial update NotificationWebhookSubscription partially : {}, {}",
            id,
            notificationWebhookSubscriptionDTO
        );
        if (notificationWebhookSubscriptionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, notificationWebhookSubscriptionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!notificationWebhookSubscriptionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NotificationWebhookSubscriptionDTO> result = notificationWebhookSubscriptionService.partialUpdate(
            notificationWebhookSubscriptionDTO
        );

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, notificationWebhookSubscriptionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /notification-webhook-subscriptions} : get all the notificationWebhookSubscriptions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of notificationWebhookSubscriptions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<NotificationWebhookSubscriptionDTO>> getAllNotificationWebhookSubscriptions(
        NotificationWebhookSubscriptionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get NotificationWebhookSubscriptions by criteria: {}", criteria);

        Page<NotificationWebhookSubscriptionDTO> page = notificationWebhookSubscriptionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /notification-webhook-subscriptions/count} : count all the notificationWebhookSubscriptions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countNotificationWebhookSubscriptions(NotificationWebhookSubscriptionCriteria criteria) {
        LOG.debug("REST request to count NotificationWebhookSubscriptions by criteria: {}", criteria);
        return ResponseEntity.ok().body(notificationWebhookSubscriptionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /notification-webhook-subscriptions/:id} : get the "id" notificationWebhookSubscription.
     *
     * @param id the id of the notificationWebhookSubscriptionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the notificationWebhookSubscriptionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<NotificationWebhookSubscriptionDTO> getNotificationWebhookSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to get NotificationWebhookSubscription : {}", id);
        Optional<NotificationWebhookSubscriptionDTO> notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionService.findOne(
            id
        );
        return ResponseUtil.wrapOrNotFound(notificationWebhookSubscriptionDTO);
    }

    /**
     * {@code DELETE  /notification-webhook-subscriptions/:id} : delete the "id" notificationWebhookSubscription.
     *
     * @param id the id of the notificationWebhookSubscriptionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNotificationWebhookSubscription(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete NotificationWebhookSubscription : {}", id);
        notificationWebhookSubscriptionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
