package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.PermissionGroupRepository;
import fr.smartprod.paperdms.document.service.PermissionGroupService;
import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.PermissionGroup}.
 */
@RestController
@RequestMapping("/api/permission-groups")
public class PermissionGroupResource {

    private static final Logger LOG = LoggerFactory.getLogger(PermissionGroupResource.class);

    private static final String ENTITY_NAME = "documentServicePermissionGroup";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PermissionGroupService permissionGroupService;

    private final PermissionGroupRepository permissionGroupRepository;

    public PermissionGroupResource(PermissionGroupService permissionGroupService, PermissionGroupRepository permissionGroupRepository) {
        this.permissionGroupService = permissionGroupService;
        this.permissionGroupRepository = permissionGroupRepository;
    }

    /**
     * {@code POST  /permission-groups} : Create a new permissionGroup.
     *
     * @param permissionGroupDTO the permissionGroupDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new permissionGroupDTO, or with status {@code 400 (Bad Request)} if the permissionGroup has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<PermissionGroupDTO> createPermissionGroup(@Valid @RequestBody PermissionGroupDTO permissionGroupDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save PermissionGroup : {}", permissionGroupDTO);
        if (permissionGroupDTO.getId() != null) {
            throw new BadRequestAlertException("A new permissionGroup cannot already have an ID", ENTITY_NAME, "idexists");
        }
        permissionGroupDTO = permissionGroupService.save(permissionGroupDTO);
        return ResponseEntity.created(new URI("/api/permission-groups/" + permissionGroupDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, permissionGroupDTO.getId().toString()))
            .body(permissionGroupDTO);
    }

    /**
     * {@code PUT  /permission-groups/:id} : Updates an existing permissionGroup.
     *
     * @param id the id of the permissionGroupDTO to save.
     * @param permissionGroupDTO the permissionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the permissionGroupDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the permissionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<PermissionGroupDTO> updatePermissionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody PermissionGroupDTO permissionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update PermissionGroup : {}, {}", id, permissionGroupDTO);
        if (permissionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        permissionGroupDTO = permissionGroupService.update(permissionGroupDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionGroupDTO.getId().toString()))
            .body(permissionGroupDTO);
    }

    /**
     * {@code PATCH  /permission-groups/:id} : Partial updates given fields of an existing permissionGroup, field will ignore if it is null
     *
     * @param id the id of the permissionGroupDTO to save.
     * @param permissionGroupDTO the permissionGroupDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated permissionGroupDTO,
     * or with status {@code 400 (Bad Request)} if the permissionGroupDTO is not valid,
     * or with status {@code 404 (Not Found)} if the permissionGroupDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the permissionGroupDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PermissionGroupDTO> partialUpdatePermissionGroup(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody PermissionGroupDTO permissionGroupDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update PermissionGroup partially : {}, {}", id, permissionGroupDTO);
        if (permissionGroupDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, permissionGroupDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!permissionGroupRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PermissionGroupDTO> result = permissionGroupService.partialUpdate(permissionGroupDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, permissionGroupDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /permission-groups} : get all the permissionGroups.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of permissionGroups in body.
     */
    @GetMapping("")
    public ResponseEntity<List<PermissionGroupDTO>> getAllPermissionGroups(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of PermissionGroups");
        Page<PermissionGroupDTO> page = permissionGroupService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /permission-groups/:id} : get the "id" permissionGroup.
     *
     * @param id the id of the permissionGroupDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the permissionGroupDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<PermissionGroupDTO> getPermissionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to get PermissionGroup : {}", id);
        Optional<PermissionGroupDTO> permissionGroupDTO = permissionGroupService.findOne(id);
        return ResponseUtil.wrapOrNotFound(permissionGroupDTO);
    }

    /**
     * {@code DELETE  /permission-groups/:id} : delete the "id" permissionGroup.
     *
     * @param id the id of the permissionGroupDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermissionGroup(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete PermissionGroup : {}", id);
        permissionGroupService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
