package fr.smartprod.paperdms.pdftoimage.web.rest;

import fr.smartprod.paperdms.pdftoimage.repository.ImagePdfConversionRequestRepository;
import fr.smartprod.paperdms.pdftoimage.service.ImagePdfConversionRequestQueryService;
import fr.smartprod.paperdms.pdftoimage.service.ImagePdfConversionRequestService;
import fr.smartprod.paperdms.pdftoimage.service.criteria.ImagePdfConversionRequestCriteria;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import fr.smartprod.paperdms.pdftoimage.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.pdftoimage.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest}.
 */
@RestController
@RequestMapping("/api/image-pdf-conversion-requests")
public class ImagePdfConversionRequestResource {

    private static final Logger LOG = LoggerFactory.getLogger(ImagePdfConversionRequestResource.class);

    private static final String ENTITY_NAME = "pdfToImageServiceImagePdfConversionRequest";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ImagePdfConversionRequestService imagePdfConversionRequestService;

    private final ImagePdfConversionRequestRepository imagePdfConversionRequestRepository;

    private final ImagePdfConversionRequestQueryService imagePdfConversionRequestQueryService;

    public ImagePdfConversionRequestResource(
        ImagePdfConversionRequestService imagePdfConversionRequestService,
        ImagePdfConversionRequestRepository imagePdfConversionRequestRepository,
        ImagePdfConversionRequestQueryService imagePdfConversionRequestQueryService
    ) {
        this.imagePdfConversionRequestService = imagePdfConversionRequestService;
        this.imagePdfConversionRequestRepository = imagePdfConversionRequestRepository;
        this.imagePdfConversionRequestQueryService = imagePdfConversionRequestQueryService;
    }

    /**
     * {@code POST  /image-pdf-conversion-requests} : Create a new imagePdfConversionRequest.
     *
     * @param imagePdfConversionRequestDTO the imagePdfConversionRequestDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new imagePdfConversionRequestDTO, or with status {@code 400 (Bad Request)} if the imagePdfConversionRequest has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ImagePdfConversionRequestDTO> createImagePdfConversionRequest(
        @Valid @RequestBody ImagePdfConversionRequestDTO imagePdfConversionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save ImagePdfConversionRequest : {}", imagePdfConversionRequestDTO);
        if (imagePdfConversionRequestDTO.getId() != null) {
            throw new BadRequestAlertException("A new imagePdfConversionRequest cannot already have an ID", ENTITY_NAME, "idexists");
        }
        imagePdfConversionRequestDTO = imagePdfConversionRequestService.save(imagePdfConversionRequestDTO);
        return ResponseEntity.created(new URI("/api/image-pdf-conversion-requests/" + imagePdfConversionRequestDTO.getId()))
            .headers(
                HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, imagePdfConversionRequestDTO.getId().toString())
            )
            .body(imagePdfConversionRequestDTO);
    }

    /**
     * {@code PUT  /image-pdf-conversion-requests/:id} : Updates an existing imagePdfConversionRequest.
     *
     * @param id the id of the imagePdfConversionRequestDTO to save.
     * @param imagePdfConversionRequestDTO the imagePdfConversionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePdfConversionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the imagePdfConversionRequestDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the imagePdfConversionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ImagePdfConversionRequestDTO> updateImagePdfConversionRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ImagePdfConversionRequestDTO imagePdfConversionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ImagePdfConversionRequest : {}, {}", id, imagePdfConversionRequestDTO);
        if (imagePdfConversionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePdfConversionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagePdfConversionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        imagePdfConversionRequestDTO = imagePdfConversionRequestService.update(imagePdfConversionRequestDTO);
        return ResponseEntity.ok()
            .headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagePdfConversionRequestDTO.getId().toString())
            )
            .body(imagePdfConversionRequestDTO);
    }

    /**
     * {@code PATCH  /image-pdf-conversion-requests/:id} : Partial updates given fields of an existing imagePdfConversionRequest, field will ignore if it is null
     *
     * @param id the id of the imagePdfConversionRequestDTO to save.
     * @param imagePdfConversionRequestDTO the imagePdfConversionRequestDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated imagePdfConversionRequestDTO,
     * or with status {@code 400 (Bad Request)} if the imagePdfConversionRequestDTO is not valid,
     * or with status {@code 404 (Not Found)} if the imagePdfConversionRequestDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the imagePdfConversionRequestDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ImagePdfConversionRequestDTO> partialUpdateImagePdfConversionRequest(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ImagePdfConversionRequestDTO imagePdfConversionRequestDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ImagePdfConversionRequest partially : {}, {}", id, imagePdfConversionRequestDTO);
        if (imagePdfConversionRequestDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, imagePdfConversionRequestDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!imagePdfConversionRequestRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ImagePdfConversionRequestDTO> result = imagePdfConversionRequestService.partialUpdate(imagePdfConversionRequestDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, imagePdfConversionRequestDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /image-pdf-conversion-requests} : get all the imagePdfConversionRequests.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of imagePdfConversionRequests in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ImagePdfConversionRequestDTO>> getAllImagePdfConversionRequests(
        ImagePdfConversionRequestCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get ImagePdfConversionRequests by criteria: {}", criteria);

        Page<ImagePdfConversionRequestDTO> page = imagePdfConversionRequestQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /image-pdf-conversion-requests/count} : count all the imagePdfConversionRequests.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countImagePdfConversionRequests(ImagePdfConversionRequestCriteria criteria) {
        LOG.debug("REST request to count ImagePdfConversionRequests by criteria: {}", criteria);
        return ResponseEntity.ok().body(imagePdfConversionRequestQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /image-pdf-conversion-requests/:id} : get the "id" imagePdfConversionRequest.
     *
     * @param id the id of the imagePdfConversionRequestDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the imagePdfConversionRequestDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ImagePdfConversionRequestDTO> getImagePdfConversionRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ImagePdfConversionRequest : {}", id);
        Optional<ImagePdfConversionRequestDTO> imagePdfConversionRequestDTO = imagePdfConversionRequestService.findOne(id);
        return ResponseUtil.wrapOrNotFound(imagePdfConversionRequestDTO);
    }

    /**
     * {@code DELETE  /image-pdf-conversion-requests/:id} : delete the "id" imagePdfConversionRequest.
     *
     * @param id the id of the imagePdfConversionRequestDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteImagePdfConversionRequest(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ImagePdfConversionRequest : {}", id);
        imagePdfConversionRequestService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /image-pdf-conversion-requests/_search?query=:query} : search for the imagePdfConversionRequest corresponding
     * to the query.
     *
     * @param query the query of the imagePdfConversionRequest search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<ImagePdfConversionRequestDTO>> searchImagePdfConversionRequests(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of ImagePdfConversionRequests for query {}", query);
        try {
            Page<ImagePdfConversionRequestDTO> page = imagePdfConversionRequestService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
