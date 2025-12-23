package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.BankTransactionAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.domain.BankTransaction;
import fr.smartprod.paperdms.business.repository.BankTransactionRepository;
import fr.smartprod.paperdms.business.service.dto.BankTransactionDTO;
import fr.smartprod.paperdms.business.service.mapper.BankTransactionMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link BankTransactionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankTransactionResourceIT {

    private static final Long DEFAULT_STATEMENT_ID = 1L;
    private static final Long UPDATED_STATEMENT_ID = 2L;

    private static final LocalDate DEFAULT_TRANSACTION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_TRANSACTION_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Double DEFAULT_DEBIT_AMOUNT = 1D;
    private static final Double UPDATED_DEBIT_AMOUNT = 2D;

    private static final Double DEFAULT_CREDIT_AMOUNT = 1D;
    private static final Double UPDATED_CREDIT_AMOUNT = 2D;

    private static final Double DEFAULT_BALANCE = 1D;
    private static final Double UPDATED_BALANCE = 2D;

    private static final Boolean DEFAULT_IS_RECONCILED = false;
    private static final Boolean UPDATED_IS_RECONCILED = true;

    private static final String ENTITY_API_URL = "/api/bank-transactions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankTransactionRepository bankTransactionRepository;

    @Autowired
    private BankTransactionMapper bankTransactionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankTransactionMockMvc;

    private BankTransaction bankTransaction;

    private BankTransaction insertedBankTransaction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransaction createEntity(EntityManager em) {
        BankTransaction bankTransaction = new BankTransaction()
            .statementId(DEFAULT_STATEMENT_ID)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .description(DEFAULT_DESCRIPTION)
            .debitAmount(DEFAULT_DEBIT_AMOUNT)
            .creditAmount(DEFAULT_CREDIT_AMOUNT)
            .balance(DEFAULT_BALANCE)
            .isReconciled(DEFAULT_IS_RECONCILED);
        // Add required entity
        BankStatement bankStatement;
        if (TestUtil.findAll(em, BankStatement.class).isEmpty()) {
            bankStatement = BankStatementResourceIT.createEntity();
            em.persist(bankStatement);
            em.flush();
        } else {
            bankStatement = TestUtil.findAll(em, BankStatement.class).get(0);
        }
        bankTransaction.setStatement(bankStatement);
        return bankTransaction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankTransaction createUpdatedEntity(EntityManager em) {
        BankTransaction updatedBankTransaction = new BankTransaction()
            .statementId(UPDATED_STATEMENT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .balance(UPDATED_BALANCE)
            .isReconciled(UPDATED_IS_RECONCILED);
        // Add required entity
        BankStatement bankStatement;
        if (TestUtil.findAll(em, BankStatement.class).isEmpty()) {
            bankStatement = BankStatementResourceIT.createUpdatedEntity();
            em.persist(bankStatement);
            em.flush();
        } else {
            bankStatement = TestUtil.findAll(em, BankStatement.class).get(0);
        }
        updatedBankTransaction.setStatement(bankStatement);
        return updatedBankTransaction;
    }

    @BeforeEach
    void initTest() {
        bankTransaction = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedBankTransaction != null) {
            bankTransactionRepository.delete(insertedBankTransaction);
            insertedBankTransaction = null;
        }
    }

    @Test
    @Transactional
    void createBankTransaction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);
        var returnedBankTransactionDTO = om.readValue(
            restBankTransactionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankTransactionDTO.class
        );

        // Validate the BankTransaction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBankTransaction = bankTransactionMapper.toEntity(returnedBankTransactionDTO);
        assertBankTransactionUpdatableFieldsEquals(returnedBankTransaction, getPersistedBankTransaction(returnedBankTransaction));

        insertedBankTransaction = returnedBankTransaction;
    }

    @Test
    @Transactional
    void createBankTransactionWithExistingId() throws Exception {
        // Create the BankTransaction with an existing ID
        bankTransaction.setId(1L);
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatementIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setStatementId(null);

        // Create the BankTransaction, which fails.
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setTransactionDate(null);

        // Create the BankTransaction, which fails.
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDescriptionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setDescription(null);

        // Create the BankTransaction, which fails.
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setBalance(null);

        // Create the BankTransaction, which fails.
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsReconciledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankTransaction.setIsReconciled(null);

        // Create the BankTransaction, which fails.
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        restBankTransactionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankTransactions() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        // Get all the bankTransactionList
        restBankTransactionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankTransaction.getId().intValue())))
            .andExpect(jsonPath("$.[*].statementId").value(hasItem(DEFAULT_STATEMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].debitAmount").value(hasItem(DEFAULT_DEBIT_AMOUNT)))
            .andExpect(jsonPath("$.[*].creditAmount").value(hasItem(DEFAULT_CREDIT_AMOUNT)))
            .andExpect(jsonPath("$.[*].balance").value(hasItem(DEFAULT_BALANCE)))
            .andExpect(jsonPath("$.[*].isReconciled").value(hasItem(DEFAULT_IS_RECONCILED)));
    }

    @Test
    @Transactional
    void getBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        // Get the bankTransaction
        restBankTransactionMockMvc
            .perform(get(ENTITY_API_URL_ID, bankTransaction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankTransaction.getId().intValue()))
            .andExpect(jsonPath("$.statementId").value(DEFAULT_STATEMENT_ID.intValue()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.debitAmount").value(DEFAULT_DEBIT_AMOUNT))
            .andExpect(jsonPath("$.creditAmount").value(DEFAULT_CREDIT_AMOUNT))
            .andExpect(jsonPath("$.balance").value(DEFAULT_BALANCE))
            .andExpect(jsonPath("$.isReconciled").value(DEFAULT_IS_RECONCILED));
    }

    @Test
    @Transactional
    void getNonExistingBankTransaction() throws Exception {
        // Get the bankTransaction
        restBankTransactionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction
        BankTransaction updatedBankTransaction = bankTransactionRepository.findById(bankTransaction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBankTransaction are not directly saved in db
        em.detach(updatedBankTransaction);
        updatedBankTransaction
            .statementId(UPDATED_STATEMENT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .balance(UPDATED_BALANCE)
            .isReconciled(UPDATED_IS_RECONCILED);
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(updatedBankTransaction);

        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankTransactionDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankTransactionToMatchAllProperties(updatedBankTransaction);
    }

    @Test
    @Transactional
    void putNonExistingBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankTransactionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction using partial update
        BankTransaction partialUpdatedBankTransaction = new BankTransaction();
        partialUpdatedBankTransaction.setId(bankTransaction.getId());

        partialUpdatedBankTransaction.creditAmount(UPDATED_CREDIT_AMOUNT).balance(UPDATED_BALANCE).isReconciled(UPDATED_IS_RECONCILED);

        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankTransaction))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankTransactionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankTransaction, bankTransaction),
            getPersistedBankTransaction(bankTransaction)
        );
    }

    @Test
    @Transactional
    void fullUpdateBankTransactionWithPatch() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankTransaction using partial update
        BankTransaction partialUpdatedBankTransaction = new BankTransaction();
        partialUpdatedBankTransaction.setId(bankTransaction.getId());

        partialUpdatedBankTransaction
            .statementId(UPDATED_STATEMENT_ID)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION)
            .debitAmount(UPDATED_DEBIT_AMOUNT)
            .creditAmount(UPDATED_CREDIT_AMOUNT)
            .balance(UPDATED_BALANCE)
            .isReconciled(UPDATED_IS_RECONCILED);

        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankTransaction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankTransaction))
            )
            .andExpect(status().isOk());

        // Validate the BankTransaction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankTransactionUpdatableFieldsEquals(
            partialUpdatedBankTransaction,
            getPersistedBankTransaction(partialUpdatedBankTransaction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankTransactionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankTransactionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankTransaction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankTransaction.setId(longCount.incrementAndGet());

        // Create the BankTransaction
        BankTransactionDTO bankTransactionDTO = bankTransactionMapper.toDto(bankTransaction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankTransactionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankTransactionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankTransaction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankTransaction() throws Exception {
        // Initialize the database
        insertedBankTransaction = bankTransactionRepository.saveAndFlush(bankTransaction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bankTransaction
        restBankTransactionMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankTransaction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankTransactionRepository.count();
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

    protected BankTransaction getPersistedBankTransaction(BankTransaction bankTransaction) {
        return bankTransactionRepository.findById(bankTransaction.getId()).orElseThrow();
    }

    protected void assertPersistedBankTransactionToMatchAllProperties(BankTransaction expectedBankTransaction) {
        assertBankTransactionAllPropertiesEquals(expectedBankTransaction, getPersistedBankTransaction(expectedBankTransaction));
    }

    protected void assertPersistedBankTransactionToMatchUpdatableProperties(BankTransaction expectedBankTransaction) {
        assertBankTransactionAllUpdatablePropertiesEquals(expectedBankTransaction, getPersistedBankTransaction(expectedBankTransaction));
    }
}
