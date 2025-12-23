package fr.smartprod.paperdms.business.web.rest;

import fr.smartprod.paperdms.business.repository.ContractClauseRepository;
import fr.smartprod.paperdms.business.service.ContractClauseService;
import fr.smartprod.paperdms.business.service.dto.ContractClauseDTO;
import fr.smartprod.paperdms.business.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link fr.smartprod.paperdms.business.domain.ContractClause}.
 */
@RestController
@RequestMapping("/api/contract-clauses")
public class ContractClauseResource {

    private static final Logger LOG = LoggerFactory.getLogger(ContractClauseResource.class);

    private static final String ENTITY_NAME = "businessDocServiceContractClause";

    @Value("${jhipster.clientApp.name:businessDocService}")
    private String applicationName;

    private final ContractClauseService contractClauseService;

    private final ContractClauseRepository contractClauseRepository;

    public ContractClauseResource(ContractClauseService contractClauseService, ContractClauseRepository contractClauseRepository) {
        this.contractClauseService = contractClauseService;
        this.contractClauseRepository = contractClauseRepository;
    }

    /**
     * {@code POST  /contract-clauses} : Create a new contractClause.
     *
     * @param contractClauseDTO the contractClauseDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new contractClauseDTO, or with status {@code 400 (Bad Request)} if the contractClause has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ContractClauseDTO> createContractClause(@Valid @RequestBody ContractClauseDTO contractClauseDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ContractClause : {}", contractClauseDTO);
        if (contractClauseDTO.getId() != null) {
            throw new BadRequestAlertException("A new contractClause cannot already have an ID", ENTITY_NAME, "idexists");
        }
        contractClauseDTO = contractClauseService.save(contractClauseDTO);
        return ResponseEntity.created(new URI("/api/contract-clauses/" + contractClauseDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, contractClauseDTO.getId().toString()))
            .body(contractClauseDTO);
    }

    /**
     * {@code PUT  /contract-clauses/:id} : Updates an existing contractClause.
     *
     * @param id the id of the contractClauseDTO to save.
     * @param contractClauseDTO the contractClauseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractClauseDTO,
     * or with status {@code 400 (Bad Request)} if the contractClauseDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the contractClauseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ContractClauseDTO> updateContractClause(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ContractClauseDTO contractClauseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ContractClause : {}, {}", id, contractClauseDTO);
        if (contractClauseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractClauseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractClauseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        contractClauseDTO = contractClauseService.update(contractClauseDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractClauseDTO.getId().toString()))
            .body(contractClauseDTO);
    }

    /**
     * {@code PATCH  /contract-clauses/:id} : Partial updates given fields of an existing contractClause, field will ignore if it is null
     *
     * @param id the id of the contractClauseDTO to save.
     * @param contractClauseDTO the contractClauseDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated contractClauseDTO,
     * or with status {@code 400 (Bad Request)} if the contractClauseDTO is not valid,
     * or with status {@code 404 (Not Found)} if the contractClauseDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the contractClauseDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ContractClauseDTO> partialUpdateContractClause(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ContractClauseDTO contractClauseDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ContractClause partially : {}, {}", id, contractClauseDTO);
        if (contractClauseDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, contractClauseDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!contractClauseRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ContractClauseDTO> result = contractClauseService.partialUpdate(contractClauseDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, contractClauseDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /contract-clauses} : get all the contractClauses.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of contractClauses in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ContractClauseDTO>> getAllContractClauses(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ContractClauses");
        Page<ContractClauseDTO> page = contractClauseService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /contract-clauses/:id} : get the "id" contractClause.
     *
     * @param id the id of the contractClauseDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the contractClauseDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ContractClauseDTO> getContractClause(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ContractClause : {}", id);
        Optional<ContractClauseDTO> contractClauseDTO = contractClauseService.findOne(id);
        return ResponseUtil.wrapOrNotFound(contractClauseDTO);
    }

    /**
     * {@code DELETE  /contract-clauses/:id} : delete the "id" contractClause.
     *
     * @param id the id of the contractClauseDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContractClause(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ContractClause : {}", id);
        contractClauseService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
