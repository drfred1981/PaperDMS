package fr.smartprod.paperdms.scan.web.rest;

import fr.smartprod.paperdms.scan.repository.ScannedPageRepository;
import fr.smartprod.paperdms.scan.service.ScannedPageService;
import fr.smartprod.paperdms.scan.service.dto.ScannedPageDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.scan.domain.ScannedPage}.
 */
@RestController
@RequestMapping("/api/scanned-pages")
public class ScannedPageResource {

    private static final Logger LOG = LoggerFactory.getLogger(ScannedPageResource.class);

    private static final String ENTITY_NAME = "scanServiceScannedPage";

    @Value("${jhipster.clientApp.name:scanService}")
    private String applicationName;

    private final ScannedPageService scannedPageService;

    private final ScannedPageRepository scannedPageRepository;

    public ScannedPageResource(ScannedPageService scannedPageService, ScannedPageRepository scannedPageRepository) {
        this.scannedPageService = scannedPageService;
        this.scannedPageRepository = scannedPageRepository;
    }

    /**
     * {@code POST  /scanned-pages} : Create a new scannedPage.
     *
     * @param scannedPageDTO the scannedPageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new scannedPageDTO, or with status {@code 400 (Bad Request)} if the scannedPage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ScannedPageDTO> createScannedPage(@Valid @RequestBody ScannedPageDTO scannedPageDTO) throws URISyntaxException {
        LOG.debug("REST request to save ScannedPage : {}", scannedPageDTO);
        if (scannedPageDTO.getId() != null) {
            throw new BadRequestAlertException("A new scannedPage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        scannedPageDTO = scannedPageService.save(scannedPageDTO);
        return ResponseEntity.created(new URI("/api/scanned-pages/" + scannedPageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, scannedPageDTO.getId().toString()))
            .body(scannedPageDTO);
    }

    /**
     * {@code PUT  /scanned-pages/:id} : Updates an existing scannedPage.
     *
     * @param id the id of the scannedPageDTO to save.
     * @param scannedPageDTO the scannedPageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scannedPageDTO,
     * or with status {@code 400 (Bad Request)} if the scannedPageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the scannedPageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ScannedPageDTO> updateScannedPage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ScannedPageDTO scannedPageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ScannedPage : {}, {}", id, scannedPageDTO);
        if (scannedPageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scannedPageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scannedPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        scannedPageDTO = scannedPageService.update(scannedPageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scannedPageDTO.getId().toString()))
            .body(scannedPageDTO);
    }

    /**
     * {@code PATCH  /scanned-pages/:id} : Partial updates given fields of an existing scannedPage, field will ignore if it is null
     *
     * @param id the id of the scannedPageDTO to save.
     * @param scannedPageDTO the scannedPageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated scannedPageDTO,
     * or with status {@code 400 (Bad Request)} if the scannedPageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the scannedPageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the scannedPageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ScannedPageDTO> partialUpdateScannedPage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ScannedPageDTO scannedPageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ScannedPage partially : {}, {}", id, scannedPageDTO);
        if (scannedPageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, scannedPageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!scannedPageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ScannedPageDTO> result = scannedPageService.partialUpdate(scannedPageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, scannedPageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /scanned-pages} : get all the scannedPages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of scannedPages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ScannedPageDTO>> getAllScannedPages(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of ScannedPages");
        Page<ScannedPageDTO> page = scannedPageService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /scanned-pages/:id} : get the "id" scannedPage.
     *
     * @param id the id of the scannedPageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the scannedPageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ScannedPageDTO> getScannedPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ScannedPage : {}", id);
        Optional<ScannedPageDTO> scannedPageDTO = scannedPageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(scannedPageDTO);
    }

    /**
     * {@code DELETE  /scanned-pages/:id} : delete the "id" scannedPage.
     *
     * @param id the id of the scannedPageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteScannedPage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ScannedPage : {}", id);
        scannedPageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
