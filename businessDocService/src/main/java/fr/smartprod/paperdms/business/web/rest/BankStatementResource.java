package fr.smartprod.paperdms.business.web.rest;

import fr.smartprod.paperdms.business.repository.BankStatementRepository;
import fr.smartprod.paperdms.business.service.BankStatementQueryService;
import fr.smartprod.paperdms.business.service.BankStatementService;
import fr.smartprod.paperdms.business.service.criteria.BankStatementCriteria;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
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
 * REST controller for managing {@link fr.smartprod.paperdms.business.domain.BankStatement}.
 */
@RestController
@RequestMapping("/api/bank-statements")
public class BankStatementResource {

    private static final Logger LOG = LoggerFactory.getLogger(BankStatementResource.class);

    private static final String ENTITY_NAME = "businessDocServiceBankStatement";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BankStatementService bankStatementService;

    private final BankStatementRepository bankStatementRepository;

    private final BankStatementQueryService bankStatementQueryService;

    public BankStatementResource(
        BankStatementService bankStatementService,
        BankStatementRepository bankStatementRepository,
        BankStatementQueryService bankStatementQueryService
    ) {
        this.bankStatementService = bankStatementService;
        this.bankStatementRepository = bankStatementRepository;
        this.bankStatementQueryService = bankStatementQueryService;
    }

    /**
     * {@code POST  /bank-statements} : Create a new bankStatement.
     *
     * @param bankStatementDTO the bankStatementDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bankStatementDTO, or with status {@code 400 (Bad Request)} if the bankStatement has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BankStatementDTO> createBankStatement(@Valid @RequestBody BankStatementDTO bankStatementDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save BankStatement : {}", bankStatementDTO);
        if (bankStatementDTO.getId() != null) {
            throw new BadRequestAlertException("A new bankStatement cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bankStatementDTO = bankStatementService.save(bankStatementDTO);
        return ResponseEntity.created(new URI("/api/bank-statements/" + bankStatementDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bankStatementDTO.getId().toString()))
            .body(bankStatementDTO);
    }

    /**
     * {@code PUT  /bank-statements/:id} : Updates an existing bankStatement.
     *
     * @param id the id of the bankStatementDTO to save.
     * @param bankStatementDTO the bankStatementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankStatementDTO,
     * or with status {@code 400 (Bad Request)} if the bankStatementDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bankStatementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BankStatementDTO> updateBankStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BankStatementDTO bankStatementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update BankStatement : {}, {}", id, bankStatementDTO);
        if (bankStatementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankStatementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankStatementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bankStatementDTO = bankStatementService.update(bankStatementDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankStatementDTO.getId().toString()))
            .body(bankStatementDTO);
    }

    /**
     * {@code PATCH  /bank-statements/:id} : Partial updates given fields of an existing bankStatement, field will ignore if it is null
     *
     * @param id the id of the bankStatementDTO to save.
     * @param bankStatementDTO the bankStatementDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bankStatementDTO,
     * or with status {@code 400 (Bad Request)} if the bankStatementDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bankStatementDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bankStatementDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BankStatementDTO> partialUpdateBankStatement(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BankStatementDTO bankStatementDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BankStatement partially : {}, {}", id, bankStatementDTO);
        if (bankStatementDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bankStatementDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bankStatementRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BankStatementDTO> result = bankStatementService.partialUpdate(bankStatementDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bankStatementDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bank-statements} : get all the bankStatements.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bankStatements in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BankStatementDTO>> getAllBankStatements(
        BankStatementCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get BankStatements by criteria: {}", criteria);

        Page<BankStatementDTO> page = bankStatementQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bank-statements/count} : count all the bankStatements.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countBankStatements(BankStatementCriteria criteria) {
        LOG.debug("REST request to count BankStatements by criteria: {}", criteria);
        return ResponseEntity.ok().body(bankStatementQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /bank-statements/:id} : get the "id" bankStatement.
     *
     * @param id the id of the bankStatementDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bankStatementDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BankStatementDTO> getBankStatement(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BankStatement : {}", id);
        Optional<BankStatementDTO> bankStatementDTO = bankStatementService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bankStatementDTO);
    }

    /**
     * {@code DELETE  /bank-statements/:id} : delete the "id" bankStatement.
     *
     * @param id the id of the bankStatementDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBankStatement(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BankStatement : {}", id);
        bankStatementService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
