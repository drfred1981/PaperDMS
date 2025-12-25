package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.TagCategoryRepository;
import fr.smartprod.paperdms.document.service.TagCategoryQueryService;
import fr.smartprod.paperdms.document.service.TagCategoryService;
import fr.smartprod.paperdms.document.service.criteria.TagCategoryCriteria;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
import fr.smartprod.paperdms.document.web.rest.errors.ElasticsearchExceptionMapper;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.TagCategory}.
 */
@RestController
@RequestMapping("/api/tag-categories")
public class TagCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(TagCategoryResource.class);

    private static final String ENTITY_NAME = "documentServiceTagCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TagCategoryService tagCategoryService;

    private final TagCategoryRepository tagCategoryRepository;

    private final TagCategoryQueryService tagCategoryQueryService;

    public TagCategoryResource(
        TagCategoryService tagCategoryService,
        TagCategoryRepository tagCategoryRepository,
        TagCategoryQueryService tagCategoryQueryService
    ) {
        this.tagCategoryService = tagCategoryService;
        this.tagCategoryRepository = tagCategoryRepository;
        this.tagCategoryQueryService = tagCategoryQueryService;
    }

    /**
     * {@code POST  /tag-categories} : Create a new tagCategory.
     *
     * @param tagCategoryDTO the tagCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new tagCategoryDTO, or with status {@code 400 (Bad Request)} if the tagCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<TagCategoryDTO> createTagCategory(@Valid @RequestBody TagCategoryDTO tagCategoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save TagCategory : {}", tagCategoryDTO);
        if (tagCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new tagCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        tagCategoryDTO = tagCategoryService.save(tagCategoryDTO);
        return ResponseEntity.created(new URI("/api/tag-categories/" + tagCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, tagCategoryDTO.getId().toString()))
            .body(tagCategoryDTO);
    }

    /**
     * {@code PUT  /tag-categories/:id} : Updates an existing tagCategory.
     *
     * @param id the id of the tagCategoryDTO to save.
     * @param tagCategoryDTO the tagCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the tagCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the tagCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<TagCategoryDTO> updateTagCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TagCategoryDTO tagCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update TagCategory : {}, {}", id, tagCategoryDTO);
        if (tagCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        tagCategoryDTO = tagCategoryService.update(tagCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCategoryDTO.getId().toString()))
            .body(tagCategoryDTO);
    }

    /**
     * {@code PATCH  /tag-categories/:id} : Partial updates given fields of an existing tagCategory, field will ignore if it is null
     *
     * @param id the id of the tagCategoryDTO to save.
     * @param tagCategoryDTO the tagCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated tagCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the tagCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the tagCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the tagCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TagCategoryDTO> partialUpdateTagCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TagCategoryDTO tagCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update TagCategory partially : {}, {}", id, tagCategoryDTO);
        if (tagCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, tagCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!tagCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TagCategoryDTO> result = tagCategoryService.partialUpdate(tagCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, tagCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /tag-categories} : get all the tagCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of tagCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<TagCategoryDTO>> getAllTagCategories(
        TagCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get TagCategories by criteria: {}", criteria);

        Page<TagCategoryDTO> page = tagCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tag-categories/count} : count all the tagCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countTagCategories(TagCategoryCriteria criteria) {
        LOG.debug("REST request to count TagCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(tagCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /tag-categories/:id} : get the "id" tagCategory.
     *
     * @param id the id of the tagCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the tagCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<TagCategoryDTO> getTagCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get TagCategory : {}", id);
        Optional<TagCategoryDTO> tagCategoryDTO = tagCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(tagCategoryDTO);
    }

    /**
     * {@code DELETE  /tag-categories/:id} : delete the "id" tagCategory.
     *
     * @param id the id of the tagCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTagCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete TagCategory : {}", id);
        tagCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }

    /**
     * {@code SEARCH  /tag-categories/_search?query=:query} : search for the tagCategory corresponding
     * to the query.
     *
     * @param query the query of the tagCategory search.
     * @param pageable the pagination information.
     * @return the result of the search.
     */
    @GetMapping("/_search")
    public ResponseEntity<List<TagCategoryDTO>> searchTagCategories(
        @RequestParam("query") String query,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to search for a page of TagCategories for query {}", query);
        try {
            Page<TagCategoryDTO> page = tagCategoryService.search(query, pageable);
            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
            return ResponseEntity.ok().headers(headers).body(page.getContent());
        } catch (RuntimeException e) {
            throw ElasticsearchExceptionMapper.mapException(e);
        }
    }
}
