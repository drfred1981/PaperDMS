package fr.smartprod.paperdms.notification.web.rest;

import fr.smartprod.paperdms.notification.repository.WebhookLogRepository;
import fr.smartprod.paperdms.notification.service.WebhookLogService;
import fr.smartprod.paperdms.notification.service.dto.WebhookLogDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.notification.domain.WebhookLog}.
 */
@RestController
@RequestMapping("/api/webhook-logs")
public class WebhookLogResource {

    private static final Logger LOG = LoggerFactory.getLogger(WebhookLogResource.class);

    private static final String ENTITY_NAME = "notificationServiceWebhookLog";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebhookLogService webhookLogService;

    private final WebhookLogRepository webhookLogRepository;

    public WebhookLogResource(WebhookLogService webhookLogService, WebhookLogRepository webhookLogRepository) {
        this.webhookLogService = webhookLogService;
        this.webhookLogRepository = webhookLogRepository;
    }

    /**
     * {@code POST  /webhook-logs} : Create a new webhookLog.
     *
     * @param webhookLogDTO the webhookLogDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webhookLogDTO, or with status {@code 400 (Bad Request)} if the webhookLog has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<WebhookLogDTO> createWebhookLog(@Valid @RequestBody WebhookLogDTO webhookLogDTO) throws URISyntaxException {
        LOG.debug("REST request to save WebhookLog : {}", webhookLogDTO);
        if (webhookLogDTO.getId() != null) {
            throw new BadRequestAlertException("A new webhookLog cannot already have an ID", ENTITY_NAME, "idexists");
        }
        webhookLogDTO = webhookLogService.save(webhookLogDTO);
        return ResponseEntity.created(new URI("/api/webhook-logs/" + webhookLogDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, webhookLogDTO.getId().toString()))
            .body(webhookLogDTO);
    }

    /**
     * {@code PUT  /webhook-logs/:id} : Updates an existing webhookLog.
     *
     * @param id the id of the webhookLogDTO to save.
     * @param webhookLogDTO the webhookLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webhookLogDTO,
     * or with status {@code 400 (Bad Request)} if the webhookLogDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webhookLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<WebhookLogDTO> updateWebhookLog(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WebhookLogDTO webhookLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update WebhookLog : {}, {}", id, webhookLogDTO);
        if (webhookLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webhookLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webhookLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        webhookLogDTO = webhookLogService.update(webhookLogDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webhookLogDTO.getId().toString()))
            .body(webhookLogDTO);
    }

    /**
     * {@code PATCH  /webhook-logs/:id} : Partial updates given fields of an existing webhookLog, field will ignore if it is null
     *
     * @param id the id of the webhookLogDTO to save.
     * @param webhookLogDTO the webhookLogDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webhookLogDTO,
     * or with status {@code 400 (Bad Request)} if the webhookLogDTO is not valid,
     * or with status {@code 404 (Not Found)} if the webhookLogDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the webhookLogDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WebhookLogDTO> partialUpdateWebhookLog(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WebhookLogDTO webhookLogDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update WebhookLog partially : {}, {}", id, webhookLogDTO);
        if (webhookLogDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, webhookLogDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!webhookLogRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WebhookLogDTO> result = webhookLogService.partialUpdate(webhookLogDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webhookLogDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /webhook-logs} : get all the webhookLogs.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webhookLogs in body.
     */
    @GetMapping("")
    public ResponseEntity<List<WebhookLogDTO>> getAllWebhookLogs(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of WebhookLogs");
        Page<WebhookLogDTO> page = webhookLogService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /webhook-logs/:id} : get the "id" webhookLog.
     *
     * @param id the id of the webhookLogDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webhookLogDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<WebhookLogDTO> getWebhookLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to get WebhookLog : {}", id);
        Optional<WebhookLogDTO> webhookLogDTO = webhookLogService.findOne(id);
        return ResponseUtil.wrapOrNotFound(webhookLogDTO);
    }

    /**
     * {@code DELETE  /webhook-logs/:id} : delete the "id" webhookLog.
     *
     * @param id the id of the webhookLogDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteWebhookLog(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete WebhookLog : {}", id);
        webhookLogService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
