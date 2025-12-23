package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.BankStatementAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.domain.enumeration.StatementStatus;
import fr.smartprod.paperdms.business.repository.BankStatementRepository;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import fr.smartprod.paperdms.business.service.mapper.BankStatementMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link BankStatementResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BankStatementResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BANK_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_STATEMENT_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATEMENT_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATEMENT_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_STATEMENT_PERIOD_START = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATEMENT_PERIOD_START = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATEMENT_PERIOD_START = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_STATEMENT_PERIOD_END = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATEMENT_PERIOD_END = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATEMENT_PERIOD_END = LocalDate.ofEpochDay(-1L);

    private static final Double DEFAULT_OPENING_BALANCE = 1D;
    private static final Double UPDATED_OPENING_BALANCE = 2D;
    private static final Double SMALLER_OPENING_BALANCE = 1D - 1D;

    private static final Double DEFAULT_CLOSING_BALANCE = 1D;
    private static final Double UPDATED_CLOSING_BALANCE = 2D;
    private static final Double SMALLER_CLOSING_BALANCE = 1D - 1D;

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final StatementStatus DEFAULT_STATUS = StatementStatus.DRAFT;
    private static final StatementStatus UPDATED_STATUS = StatementStatus.PENDING;

    private static final Boolean DEFAULT_IS_RECONCILED = false;
    private static final Boolean UPDATED_IS_RECONCILED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/bank-statements";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BankStatementRepository bankStatementRepository;

    @Autowired
    private BankStatementMapper bankStatementMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBankStatementMockMvc;

    private BankStatement bankStatement;

    private BankStatement insertedBankStatement;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankStatement createEntity() {
        return new BankStatement()
            .documentId(DEFAULT_DOCUMENT_ID)
            .accountNumber(DEFAULT_ACCOUNT_NUMBER)
            .bankName(DEFAULT_BANK_NAME)
            .statementDate(DEFAULT_STATEMENT_DATE)
            .statementPeriodStart(DEFAULT_STATEMENT_PERIOD_START)
            .statementPeriodEnd(DEFAULT_STATEMENT_PERIOD_END)
            .openingBalance(DEFAULT_OPENING_BALANCE)
            .closingBalance(DEFAULT_CLOSING_BALANCE)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS)
            .isReconciled(DEFAULT_IS_RECONCILED)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BankStatement createUpdatedEntity() {
        return new BankStatement()
            .documentId(UPDATED_DOCUMENT_ID)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankName(UPDATED_BANK_NAME)
            .statementDate(UPDATED_STATEMENT_DATE)
            .statementPeriodStart(UPDATED_STATEMENT_PERIOD_START)
            .statementPeriodEnd(UPDATED_STATEMENT_PERIOD_END)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .isReconciled(UPDATED_IS_RECONCILED)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        bankStatement = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBankStatement != null) {
            bankStatementRepository.delete(insertedBankStatement);
            insertedBankStatement = null;
        }
    }

    @Test
    @Transactional
    void createBankStatement() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);
        var returnedBankStatementDTO = om.readValue(
            restBankStatementMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BankStatementDTO.class
        );

        // Validate the BankStatement in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBankStatement = bankStatementMapper.toEntity(returnedBankStatementDTO);
        assertBankStatementUpdatableFieldsEquals(returnedBankStatement, getPersistedBankStatement(returnedBankStatement));

        insertedBankStatement = returnedBankStatement;
    }

    @Test
    @Transactional
    void createBankStatementWithExistingId() throws Exception {
        // Create the BankStatement with an existing ID
        bankStatement.setId(1L);
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setDocumentId(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setAccountNumber(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkBankNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setBankName(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatementDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setStatementDate(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatementPeriodStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setStatementPeriodStart(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatementPeriodEndIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setStatementPeriodEnd(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOpeningBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setOpeningBalance(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkClosingBalanceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setClosingBalance(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setCurrency(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setStatus(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsReconciledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setIsReconciled(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bankStatement.setCreatedDate(null);

        // Create the BankStatement, which fails.
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        restBankStatementMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBankStatements() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankStatement.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].statementDate").value(hasItem(DEFAULT_STATEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].statementPeriodStart").value(hasItem(DEFAULT_STATEMENT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].statementPeriodEnd").value(hasItem(DEFAULT_STATEMENT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(DEFAULT_OPENING_BALANCE)))
            .andExpect(jsonPath("$.[*].closingBalance").value(hasItem(DEFAULT_CLOSING_BALANCE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isReconciled").value(hasItem(DEFAULT_IS_RECONCILED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBankStatement() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get the bankStatement
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL_ID, bankStatement.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bankStatement.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.accountNumber").value(DEFAULT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.bankName").value(DEFAULT_BANK_NAME))
            .andExpect(jsonPath("$.statementDate").value(DEFAULT_STATEMENT_DATE.toString()))
            .andExpect(jsonPath("$.statementPeriodStart").value(DEFAULT_STATEMENT_PERIOD_START.toString()))
            .andExpect(jsonPath("$.statementPeriodEnd").value(DEFAULT_STATEMENT_PERIOD_END.toString()))
            .andExpect(jsonPath("$.openingBalance").value(DEFAULT_OPENING_BALANCE))
            .andExpect(jsonPath("$.closingBalance").value(DEFAULT_CLOSING_BALANCE))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isReconciled").value(DEFAULT_IS_RECONCILED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getBankStatementsByIdFiltering() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        Long id = bankStatement.getId();

        defaultBankStatementFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultBankStatementFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultBankStatementFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId equals to
        defaultBankStatementFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId in
        defaultBankStatementFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId is not null
        defaultBankStatementFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId is greater than or equal to
        defaultBankStatementFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId is less than or equal to
        defaultBankStatementFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId is less than
        defaultBankStatementFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllBankStatementsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where documentId is greater than
        defaultBankStatementFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllBankStatementsByAccountNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where accountNumber equals to
        defaultBankStatementFiltering("accountNumber.equals=" + DEFAULT_ACCOUNT_NUMBER, "accountNumber.equals=" + UPDATED_ACCOUNT_NUMBER);
    }

    @Test
    @Transactional
    void getAllBankStatementsByAccountNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where accountNumber in
        defaultBankStatementFiltering(
            "accountNumber.in=" + DEFAULT_ACCOUNT_NUMBER + "," + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.in=" + UPDATED_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByAccountNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where accountNumber is not null
        defaultBankStatementFiltering("accountNumber.specified=true", "accountNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByAccountNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where accountNumber contains
        defaultBankStatementFiltering(
            "accountNumber.contains=" + DEFAULT_ACCOUNT_NUMBER,
            "accountNumber.contains=" + UPDATED_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByAccountNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where accountNumber does not contain
        defaultBankStatementFiltering(
            "accountNumber.doesNotContain=" + UPDATED_ACCOUNT_NUMBER,
            "accountNumber.doesNotContain=" + DEFAULT_ACCOUNT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByBankNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where bankName equals to
        defaultBankStatementFiltering("bankName.equals=" + DEFAULT_BANK_NAME, "bankName.equals=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankStatementsByBankNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where bankName in
        defaultBankStatementFiltering("bankName.in=" + DEFAULT_BANK_NAME + "," + UPDATED_BANK_NAME, "bankName.in=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankStatementsByBankNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where bankName is not null
        defaultBankStatementFiltering("bankName.specified=true", "bankName.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByBankNameContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where bankName contains
        defaultBankStatementFiltering("bankName.contains=" + DEFAULT_BANK_NAME, "bankName.contains=" + UPDATED_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankStatementsByBankNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where bankName does not contain
        defaultBankStatementFiltering("bankName.doesNotContain=" + UPDATED_BANK_NAME, "bankName.doesNotContain=" + DEFAULT_BANK_NAME);
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate equals to
        defaultBankStatementFiltering("statementDate.equals=" + DEFAULT_STATEMENT_DATE, "statementDate.equals=" + UPDATED_STATEMENT_DATE);
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate in
        defaultBankStatementFiltering(
            "statementDate.in=" + DEFAULT_STATEMENT_DATE + "," + UPDATED_STATEMENT_DATE,
            "statementDate.in=" + UPDATED_STATEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate is not null
        defaultBankStatementFiltering("statementDate.specified=true", "statementDate.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate is greater than or equal to
        defaultBankStatementFiltering(
            "statementDate.greaterThanOrEqual=" + DEFAULT_STATEMENT_DATE,
            "statementDate.greaterThanOrEqual=" + UPDATED_STATEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate is less than or equal to
        defaultBankStatementFiltering(
            "statementDate.lessThanOrEqual=" + DEFAULT_STATEMENT_DATE,
            "statementDate.lessThanOrEqual=" + SMALLER_STATEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate is less than
        defaultBankStatementFiltering(
            "statementDate.lessThan=" + UPDATED_STATEMENT_DATE,
            "statementDate.lessThan=" + DEFAULT_STATEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementDate is greater than
        defaultBankStatementFiltering(
            "statementDate.greaterThan=" + SMALLER_STATEMENT_DATE,
            "statementDate.greaterThan=" + DEFAULT_STATEMENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart equals to
        defaultBankStatementFiltering(
            "statementPeriodStart.equals=" + DEFAULT_STATEMENT_PERIOD_START,
            "statementPeriodStart.equals=" + UPDATED_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart in
        defaultBankStatementFiltering(
            "statementPeriodStart.in=" + DEFAULT_STATEMENT_PERIOD_START + "," + UPDATED_STATEMENT_PERIOD_START,
            "statementPeriodStart.in=" + UPDATED_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart is not null
        defaultBankStatementFiltering("statementPeriodStart.specified=true", "statementPeriodStart.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart is greater than or equal to
        defaultBankStatementFiltering(
            "statementPeriodStart.greaterThanOrEqual=" + DEFAULT_STATEMENT_PERIOD_START,
            "statementPeriodStart.greaterThanOrEqual=" + UPDATED_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart is less than or equal to
        defaultBankStatementFiltering(
            "statementPeriodStart.lessThanOrEqual=" + DEFAULT_STATEMENT_PERIOD_START,
            "statementPeriodStart.lessThanOrEqual=" + SMALLER_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart is less than
        defaultBankStatementFiltering(
            "statementPeriodStart.lessThan=" + UPDATED_STATEMENT_PERIOD_START,
            "statementPeriodStart.lessThan=" + DEFAULT_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodStartIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodStart is greater than
        defaultBankStatementFiltering(
            "statementPeriodStart.greaterThan=" + SMALLER_STATEMENT_PERIOD_START,
            "statementPeriodStart.greaterThan=" + DEFAULT_STATEMENT_PERIOD_START
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd equals to
        defaultBankStatementFiltering(
            "statementPeriodEnd.equals=" + DEFAULT_STATEMENT_PERIOD_END,
            "statementPeriodEnd.equals=" + UPDATED_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd in
        defaultBankStatementFiltering(
            "statementPeriodEnd.in=" + DEFAULT_STATEMENT_PERIOD_END + "," + UPDATED_STATEMENT_PERIOD_END,
            "statementPeriodEnd.in=" + UPDATED_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd is not null
        defaultBankStatementFiltering("statementPeriodEnd.specified=true", "statementPeriodEnd.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd is greater than or equal to
        defaultBankStatementFiltering(
            "statementPeriodEnd.greaterThanOrEqual=" + DEFAULT_STATEMENT_PERIOD_END,
            "statementPeriodEnd.greaterThanOrEqual=" + UPDATED_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd is less than or equal to
        defaultBankStatementFiltering(
            "statementPeriodEnd.lessThanOrEqual=" + DEFAULT_STATEMENT_PERIOD_END,
            "statementPeriodEnd.lessThanOrEqual=" + SMALLER_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd is less than
        defaultBankStatementFiltering(
            "statementPeriodEnd.lessThan=" + UPDATED_STATEMENT_PERIOD_END,
            "statementPeriodEnd.lessThan=" + DEFAULT_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatementPeriodEndIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where statementPeriodEnd is greater than
        defaultBankStatementFiltering(
            "statementPeriodEnd.greaterThan=" + SMALLER_STATEMENT_PERIOD_END,
            "statementPeriodEnd.greaterThan=" + DEFAULT_STATEMENT_PERIOD_END
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance equals to
        defaultBankStatementFiltering(
            "openingBalance.equals=" + DEFAULT_OPENING_BALANCE,
            "openingBalance.equals=" + UPDATED_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance in
        defaultBankStatementFiltering(
            "openingBalance.in=" + DEFAULT_OPENING_BALANCE + "," + UPDATED_OPENING_BALANCE,
            "openingBalance.in=" + UPDATED_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance is not null
        defaultBankStatementFiltering("openingBalance.specified=true", "openingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance is greater than or equal to
        defaultBankStatementFiltering(
            "openingBalance.greaterThanOrEqual=" + DEFAULT_OPENING_BALANCE,
            "openingBalance.greaterThanOrEqual=" + UPDATED_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance is less than or equal to
        defaultBankStatementFiltering(
            "openingBalance.lessThanOrEqual=" + DEFAULT_OPENING_BALANCE,
            "openingBalance.lessThanOrEqual=" + SMALLER_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance is less than
        defaultBankStatementFiltering(
            "openingBalance.lessThan=" + UPDATED_OPENING_BALANCE,
            "openingBalance.lessThan=" + DEFAULT_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByOpeningBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where openingBalance is greater than
        defaultBankStatementFiltering(
            "openingBalance.greaterThan=" + SMALLER_OPENING_BALANCE,
            "openingBalance.greaterThan=" + DEFAULT_OPENING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance equals to
        defaultBankStatementFiltering(
            "closingBalance.equals=" + DEFAULT_CLOSING_BALANCE,
            "closingBalance.equals=" + UPDATED_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance in
        defaultBankStatementFiltering(
            "closingBalance.in=" + DEFAULT_CLOSING_BALANCE + "," + UPDATED_CLOSING_BALANCE,
            "closingBalance.in=" + UPDATED_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance is not null
        defaultBankStatementFiltering("closingBalance.specified=true", "closingBalance.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance is greater than or equal to
        defaultBankStatementFiltering(
            "closingBalance.greaterThanOrEqual=" + DEFAULT_CLOSING_BALANCE,
            "closingBalance.greaterThanOrEqual=" + UPDATED_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance is less than or equal to
        defaultBankStatementFiltering(
            "closingBalance.lessThanOrEqual=" + DEFAULT_CLOSING_BALANCE,
            "closingBalance.lessThanOrEqual=" + SMALLER_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance is less than
        defaultBankStatementFiltering(
            "closingBalance.lessThan=" + UPDATED_CLOSING_BALANCE,
            "closingBalance.lessThan=" + DEFAULT_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByClosingBalanceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where closingBalance is greater than
        defaultBankStatementFiltering(
            "closingBalance.greaterThan=" + SMALLER_CLOSING_BALANCE,
            "closingBalance.greaterThan=" + DEFAULT_CLOSING_BALANCE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where currency equals to
        defaultBankStatementFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBankStatementsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where currency in
        defaultBankStatementFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBankStatementsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where currency is not null
        defaultBankStatementFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where currency contains
        defaultBankStatementFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBankStatementsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where currency does not contain
        defaultBankStatementFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where status equals to
        defaultBankStatementFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where status in
        defaultBankStatementFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllBankStatementsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where status is not null
        defaultBankStatementFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByIsReconciledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where isReconciled equals to
        defaultBankStatementFiltering("isReconciled.equals=" + DEFAULT_IS_RECONCILED, "isReconciled.equals=" + UPDATED_IS_RECONCILED);
    }

    @Test
    @Transactional
    void getAllBankStatementsByIsReconciledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where isReconciled in
        defaultBankStatementFiltering(
            "isReconciled.in=" + DEFAULT_IS_RECONCILED + "," + UPDATED_IS_RECONCILED,
            "isReconciled.in=" + UPDATED_IS_RECONCILED
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByIsReconciledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where isReconciled is not null
        defaultBankStatementFiltering("isReconciled.specified=true", "isReconciled.specified=false");
    }

    @Test
    @Transactional
    void getAllBankStatementsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate equals to
        defaultBankStatementFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllBankStatementsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate in
        defaultBankStatementFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllBankStatementsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        // Get all the bankStatementList where createdDate is not null
        defaultBankStatementFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultBankStatementFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultBankStatementShouldBeFound(shouldBeFound);
        defaultBankStatementShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultBankStatementShouldBeFound(String filter) throws Exception {
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bankStatement.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].accountNumber").value(hasItem(DEFAULT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].bankName").value(hasItem(DEFAULT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].statementDate").value(hasItem(DEFAULT_STATEMENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].statementPeriodStart").value(hasItem(DEFAULT_STATEMENT_PERIOD_START.toString())))
            .andExpect(jsonPath("$.[*].statementPeriodEnd").value(hasItem(DEFAULT_STATEMENT_PERIOD_END.toString())))
            .andExpect(jsonPath("$.[*].openingBalance").value(hasItem(DEFAULT_OPENING_BALANCE)))
            .andExpect(jsonPath("$.[*].closingBalance").value(hasItem(DEFAULT_CLOSING_BALANCE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isReconciled").value(hasItem(DEFAULT_IS_RECONCILED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultBankStatementShouldNotBeFound(String filter) throws Exception {
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restBankStatementMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingBankStatement() throws Exception {
        // Get the bankStatement
        restBankStatementMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBankStatement() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankStatement
        BankStatement updatedBankStatement = bankStatementRepository.findById(bankStatement.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBankStatement are not directly saved in db
        em.detach(updatedBankStatement);
        updatedBankStatement
            .documentId(UPDATED_DOCUMENT_ID)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankName(UPDATED_BANK_NAME)
            .statementDate(UPDATED_STATEMENT_DATE)
            .statementPeriodStart(UPDATED_STATEMENT_PERIOD_START)
            .statementPeriodEnd(UPDATED_STATEMENT_PERIOD_END)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .isReconciled(UPDATED_IS_RECONCILED)
            .createdDate(UPDATED_CREATED_DATE);
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(updatedBankStatement);

        restBankStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankStatementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankStatementDTO))
            )
            .andExpect(status().isOk());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBankStatementToMatchAllProperties(updatedBankStatement);
    }

    @Test
    @Transactional
    void putNonExistingBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bankStatementDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankStatementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bankStatementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBankStatementWithPatch() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankStatement using partial update
        BankStatement partialUpdatedBankStatement = new BankStatement();
        partialUpdatedBankStatement.setId(bankStatement.getId());

        partialUpdatedBankStatement
            .documentId(UPDATED_DOCUMENT_ID)
            .bankName(UPDATED_BANK_NAME)
            .statementDate(UPDATED_STATEMENT_DATE)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .currency(UPDATED_CURRENCY)
            .isReconciled(UPDATED_IS_RECONCILED)
            .createdDate(UPDATED_CREATED_DATE);

        restBankStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankStatement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankStatement))
            )
            .andExpect(status().isOk());

        // Validate the BankStatement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankStatementUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedBankStatement, bankStatement),
            getPersistedBankStatement(bankStatement)
        );
    }

    @Test
    @Transactional
    void fullUpdateBankStatementWithPatch() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bankStatement using partial update
        BankStatement partialUpdatedBankStatement = new BankStatement();
        partialUpdatedBankStatement.setId(bankStatement.getId());

        partialUpdatedBankStatement
            .documentId(UPDATED_DOCUMENT_ID)
            .accountNumber(UPDATED_ACCOUNT_NUMBER)
            .bankName(UPDATED_BANK_NAME)
            .statementDate(UPDATED_STATEMENT_DATE)
            .statementPeriodStart(UPDATED_STATEMENT_PERIOD_START)
            .statementPeriodEnd(UPDATED_STATEMENT_PERIOD_END)
            .openingBalance(UPDATED_OPENING_BALANCE)
            .closingBalance(UPDATED_CLOSING_BALANCE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .isReconciled(UPDATED_IS_RECONCILED)
            .createdDate(UPDATED_CREATED_DATE);

        restBankStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBankStatement.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBankStatement))
            )
            .andExpect(status().isOk());

        // Validate the BankStatement in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBankStatementUpdatableFieldsEquals(partialUpdatedBankStatement, getPersistedBankStatement(partialUpdatedBankStatement));
    }

    @Test
    @Transactional
    void patchNonExistingBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bankStatementDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankStatementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bankStatementDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBankStatement() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bankStatement.setId(longCount.incrementAndGet());

        // Create the BankStatement
        BankStatementDTO bankStatementDTO = bankStatementMapper.toDto(bankStatement);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBankStatementMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bankStatementDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the BankStatement in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBankStatement() throws Exception {
        // Initialize the database
        insertedBankStatement = bankStatementRepository.saveAndFlush(bankStatement);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bankStatement
        restBankStatementMockMvc
            .perform(delete(ENTITY_API_URL_ID, bankStatement.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bankStatementRepository.count();
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

    protected BankStatement getPersistedBankStatement(BankStatement bankStatement) {
        return bankStatementRepository.findById(bankStatement.getId()).orElseThrow();
    }

    protected void assertPersistedBankStatementToMatchAllProperties(BankStatement expectedBankStatement) {
        assertBankStatementAllPropertiesEquals(expectedBankStatement, getPersistedBankStatement(expectedBankStatement));
    }

    protected void assertPersistedBankStatementToMatchUpdatableProperties(BankStatement expectedBankStatement) {
        assertBankStatementAllUpdatablePropertiesEquals(expectedBankStatement, getPersistedBankStatement(expectedBankStatement));
    }
}
