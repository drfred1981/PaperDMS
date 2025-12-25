package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.ContractClauseAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.domain.ContractClause;
import fr.smartprod.paperdms.business.domain.enumeration.ClauseType;
import fr.smartprod.paperdms.business.repository.ContractClauseRepository;
import fr.smartprod.paperdms.business.service.dto.ContractClauseDTO;
import fr.smartprod.paperdms.business.service.mapper.ContractClauseMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContractClauseResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractClauseResourceIT {

    private static final Long DEFAULT_CONTRACT_ID = 1L;
    private static final Long UPDATED_CONTRACT_ID = 2L;

    private static final String DEFAULT_CLAUSE_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CLAUSE_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final ClauseType DEFAULT_CLAUSE_TYPE = ClauseType.GENERAL;
    private static final ClauseType UPDATED_CLAUSE_TYPE = ClauseType.PAYMENT;

    private static final Boolean DEFAULT_IS_MANDATORY = false;
    private static final Boolean UPDATED_IS_MANDATORY = true;

    private static final String ENTITY_API_URL = "/api/contract-clauses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractClauseRepository contractClauseRepository;

    @Autowired
    private ContractClauseMapper contractClauseMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractClauseMockMvc;

    private ContractClause contractClause;

    private ContractClause insertedContractClause;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractClause createEntity(EntityManager em) {
        ContractClause contractClause = new ContractClause()
            .contractId(DEFAULT_CONTRACT_ID)
            .clauseNumber(DEFAULT_CLAUSE_NUMBER)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .clauseType(DEFAULT_CLAUSE_TYPE)
            .isMandatory(DEFAULT_IS_MANDATORY);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createEntity();
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        contractClause.setContract(contract);
        return contractClause;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ContractClause createUpdatedEntity(EntityManager em) {
        ContractClause updatedContractClause = new ContractClause()
            .contractId(UPDATED_CONTRACT_ID)
            .clauseNumber(UPDATED_CLAUSE_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .isMandatory(UPDATED_IS_MANDATORY);
        // Add required entity
        Contract contract;
        if (TestUtil.findAll(em, Contract.class).isEmpty()) {
            contract = ContractResourceIT.createUpdatedEntity();
            em.persist(contract);
            em.flush();
        } else {
            contract = TestUtil.findAll(em, Contract.class).get(0);
        }
        updatedContractClause.setContract(contract);
        return updatedContractClause;
    }

    @BeforeEach
    void initTest() {
        contractClause = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedContractClause != null) {
            contractClauseRepository.delete(insertedContractClause);
            insertedContractClause = null;
        }
    }

    @Test
    @Transactional
    void createContractClause() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);
        var returnedContractClauseDTO = om.readValue(
            restContractClauseMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractClauseDTO.class
        );

        // Validate the ContractClause in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContractClause = contractClauseMapper.toEntity(returnedContractClauseDTO);
        assertContractClauseUpdatableFieldsEquals(returnedContractClause, getPersistedContractClause(returnedContractClause));

        insertedContractClause = returnedContractClause;
    }

    @Test
    @Transactional
    void createContractClauseWithExistingId() throws Exception {
        // Create the ContractClause with an existing ID
        contractClause.setId(1L);
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkContractIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractClause.setContractId(null);

        // Create the ContractClause, which fails.
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        restContractClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClauseNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractClause.setClauseNumber(null);

        // Create the ContractClause, which fails.
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        restContractClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractClause.setTitle(null);

        // Create the ContractClause, which fails.
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        restContractClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsMandatoryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        contractClause.setIsMandatory(null);

        // Create the ContractClause, which fails.
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        restContractClauseMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllContractClauses() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        // Get all the contractClauseList
        restContractClauseMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contractClause.getId().intValue())))
            .andExpect(jsonPath("$.[*].contractId").value(hasItem(DEFAULT_CONTRACT_ID.intValue())))
            .andExpect(jsonPath("$.[*].clauseNumber").value(hasItem(DEFAULT_CLAUSE_NUMBER)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].clauseType").value(hasItem(DEFAULT_CLAUSE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isMandatory").value(hasItem(DEFAULT_IS_MANDATORY)));
    }

    @Test
    @Transactional
    void getContractClause() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        // Get the contractClause
        restContractClauseMockMvc
            .perform(get(ENTITY_API_URL_ID, contractClause.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contractClause.getId().intValue()))
            .andExpect(jsonPath("$.contractId").value(DEFAULT_CONTRACT_ID.intValue()))
            .andExpect(jsonPath("$.clauseNumber").value(DEFAULT_CLAUSE_NUMBER))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.clauseType").value(DEFAULT_CLAUSE_TYPE.toString()))
            .andExpect(jsonPath("$.isMandatory").value(DEFAULT_IS_MANDATORY));
    }

    @Test
    @Transactional
    void getNonExistingContractClause() throws Exception {
        // Get the contractClause
        restContractClauseMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContractClause() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractClause
        ContractClause updatedContractClause = contractClauseRepository.findById(contractClause.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContractClause are not directly saved in db
        em.detach(updatedContractClause);
        updatedContractClause
            .contractId(UPDATED_CONTRACT_ID)
            .clauseNumber(UPDATED_CLAUSE_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .isMandatory(UPDATED_IS_MANDATORY);
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(updatedContractClause);

        restContractClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractClauseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractClauseDTO))
            )
            .andExpect(status().isOk());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractClauseToMatchAllProperties(updatedContractClause);
    }

    @Test
    @Transactional
    void putNonExistingContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractClauseDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractClauseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractClauseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateContractClauseWithPatch() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractClause using partial update
        ContractClause partialUpdatedContractClause = new ContractClause();
        partialUpdatedContractClause.setId(contractClause.getId());

        partialUpdatedContractClause.contractId(UPDATED_CONTRACT_ID).title(UPDATED_TITLE).content(UPDATED_CONTENT);

        restContractClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractClause.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractClause))
            )
            .andExpect(status().isOk());

        // Validate the ContractClause in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractClauseUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedContractClause, contractClause),
            getPersistedContractClause(contractClause)
        );
    }

    @Test
    @Transactional
    void fullUpdateContractClauseWithPatch() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contractClause using partial update
        ContractClause partialUpdatedContractClause = new ContractClause();
        partialUpdatedContractClause.setId(contractClause.getId());

        partialUpdatedContractClause
            .contractId(UPDATED_CONTRACT_ID)
            .clauseNumber(UPDATED_CLAUSE_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .clauseType(UPDATED_CLAUSE_TYPE)
            .isMandatory(UPDATED_IS_MANDATORY);

        restContractClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContractClause.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContractClause))
            )
            .andExpect(status().isOk());

        // Validate the ContractClause in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractClauseUpdatableFieldsEquals(partialUpdatedContractClause, getPersistedContractClause(partialUpdatedContractClause));
    }

    @Test
    @Transactional
    void patchNonExistingContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractClauseDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractClauseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractClauseDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContractClause() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractClause.setId(longCount.incrementAndGet());

        // Create the ContractClause
        ContractClauseDTO contractClauseDTO = contractClauseMapper.toDto(contractClause);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractClauseMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractClauseDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ContractClause in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteContractClause() throws Exception {
        // Initialize the database
        insertedContractClause = contractClauseRepository.saveAndFlush(contractClause);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the contractClause
        restContractClauseMockMvc
            .perform(delete(ENTITY_API_URL_ID, contractClause.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return contractClauseRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ContractClause getPersistedContractClause(ContractClause contractClause) {
        return contractClauseRepository.findById(contractClause.getId()).orElseThrow();
    }

    protected void assertPersistedContractClauseToMatchAllProperties(ContractClause expectedContractClause) {
        assertContractClauseAllPropertiesEquals(expectedContractClause, getPersistedContractClause(expectedContractClause));
    }

    protected void assertPersistedContractClauseToMatchUpdatableProperties(ContractClause expectedContractClause) {
        assertContractClauseAllUpdatablePropertiesEquals(expectedContractClause, getPersistedContractClause(expectedContractClause));
    }
}
