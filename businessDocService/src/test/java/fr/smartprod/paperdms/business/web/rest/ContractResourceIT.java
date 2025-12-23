package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.ContractAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.domain.enumeration.ContractStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ContractType;
import fr.smartprod.paperdms.business.repository.ContractRepository;
import fr.smartprod.paperdms.business.repository.search.ContractSearchRepository;
import fr.smartprod.paperdms.business.service.dto.ContractDTO;
import fr.smartprod.paperdms.business.service.mapper.ContractMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ContractResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ContractResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_CONTRACT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CONTRACT_NUMBER = "BBBBBBBBBB";

    private static final ContractType DEFAULT_CONTRACT_TYPE = ContractType.SERVICE_AGREEMENT;
    private static final ContractType UPDATED_CONTRACT_TYPE = ContractType.PURCHASE_CONTRACT;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_A = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_A = "BBBBBBBBBB";

    private static final String DEFAULT_PARTY_B = "AAAAAAAAAA";
    private static final String UPDATED_PARTY_B = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE = LocalDate.ofEpochDay(-1L);

    private static final Boolean DEFAULT_AUTO_RENEW = false;
    private static final Boolean UPDATED_AUTO_RENEW = true;

    private static final Double DEFAULT_CONTRACT_VALUE = 1D;
    private static final Double UPDATED_CONTRACT_VALUE = 2D;
    private static final Double SMALLER_CONTRACT_VALUE = 1D - 1D;

    private static final String DEFAULT_CURRENCY = "AAA";
    private static final String UPDATED_CURRENCY = "BBB";

    private static final ContractStatus DEFAULT_STATUS = ContractStatus.DRAFT;
    private static final ContractStatus UPDATED_STATUS = ContractStatus.UNDER_NEGOTIATION;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/contracts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/contracts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ContractRepository contractRepository;

    @Autowired
    private ContractMapper contractMapper;

    @Autowired
    private ContractSearchRepository contractSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restContractMockMvc;

    private Contract contract;

    private Contract insertedContract;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createEntity() {
        return new Contract()
            .documentId(DEFAULT_DOCUMENT_ID)
            .contractNumber(DEFAULT_CONTRACT_NUMBER)
            .contractType(DEFAULT_CONTRACT_TYPE)
            .title(DEFAULT_TITLE)
            .partyA(DEFAULT_PARTY_A)
            .partyB(DEFAULT_PARTY_B)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .autoRenew(DEFAULT_AUTO_RENEW)
            .contractValue(DEFAULT_CONTRACT_VALUE)
            .currency(DEFAULT_CURRENCY)
            .status(DEFAULT_STATUS)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Contract createUpdatedEntity() {
        return new Contract()
            .documentId(UPDATED_DOCUMENT_ID)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractType(UPDATED_CONTRACT_TYPE)
            .title(UPDATED_TITLE)
            .partyA(UPDATED_PARTY_A)
            .partyB(UPDATED_PARTY_B)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .autoRenew(UPDATED_AUTO_RENEW)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        contract = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedContract != null) {
            contractRepository.delete(insertedContract);
            contractSearchRepository.delete(insertedContract);
            insertedContract = null;
        }
    }

    @Test
    @Transactional
    void createContract() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);
        var returnedContractDTO = om.readValue(
            restContractMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ContractDTO.class
        );

        // Validate the Contract in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedContract = contractMapper.toEntity(returnedContractDTO);
        assertContractUpdatableFieldsEquals(returnedContract, getPersistedContract(returnedContract));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedContract = returnedContract;
    }

    @Test
    @Transactional
    void createContractWithExistingId() throws Exception {
        // Create the Contract with an existing ID
        contract.setId(1L);
        ContractDTO contractDTO = contractMapper.toDto(contract);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setDocumentId(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkContractNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setContractNumber(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkContractTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setContractType(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setTitle(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPartyAIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setPartyA(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPartyBIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setPartyB(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setStartDate(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAutoRenewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setAutoRenew(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setStatus(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        // set the field null
        contract.setCreatedDate(null);

        // Create the Contract, which fails.
        ContractDTO contractDTO = contractMapper.toDto(contract);

        restContractMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllContracts() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partyA").value(hasItem(DEFAULT_PARTY_A)))
            .andExpect(jsonPath("$.[*].partyB").value(hasItem(DEFAULT_PARTY_B)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].autoRenew").value(hasItem(DEFAULT_AUTO_RENEW)))
            .andExpect(jsonPath("$.[*].contractValue").value(hasItem(DEFAULT_CONTRACT_VALUE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get the contract
        restContractMockMvc
            .perform(get(ENTITY_API_URL_ID, contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(contract.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.contractNumber").value(DEFAULT_CONTRACT_NUMBER))
            .andExpect(jsonPath("$.contractType").value(DEFAULT_CONTRACT_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.partyA").value(DEFAULT_PARTY_A))
            .andExpect(jsonPath("$.partyB").value(DEFAULT_PARTY_B))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.autoRenew").value(DEFAULT_AUTO_RENEW))
            .andExpect(jsonPath("$.contractValue").value(DEFAULT_CONTRACT_VALUE))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getContractsByIdFiltering() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        Long id = contract.getId();

        defaultContractFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultContractFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultContractFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId equals to
        defaultContractFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId in
        defaultContractFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId is not null
        defaultContractFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId is greater than or equal to
        defaultContractFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId is less than or equal to
        defaultContractFiltering("documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID, "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId is less than
        defaultContractFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllContractsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where documentId is greater than
        defaultContractFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber equals to
        defaultContractFiltering("contractNumber.equals=" + DEFAULT_CONTRACT_NUMBER, "contractNumber.equals=" + UPDATED_CONTRACT_NUMBER);
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber in
        defaultContractFiltering(
            "contractNumber.in=" + DEFAULT_CONTRACT_NUMBER + "," + UPDATED_CONTRACT_NUMBER,
            "contractNumber.in=" + UPDATED_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber is not null
        defaultContractFiltering("contractNumber.specified=true", "contractNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber contains
        defaultContractFiltering(
            "contractNumber.contains=" + DEFAULT_CONTRACT_NUMBER,
            "contractNumber.contains=" + UPDATED_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractNumberNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractNumber does not contain
        defaultContractFiltering(
            "contractNumber.doesNotContain=" + UPDATED_CONTRACT_NUMBER,
            "contractNumber.doesNotContain=" + DEFAULT_CONTRACT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType equals to
        defaultContractFiltering("contractType.equals=" + DEFAULT_CONTRACT_TYPE, "contractType.equals=" + UPDATED_CONTRACT_TYPE);
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType in
        defaultContractFiltering(
            "contractType.in=" + DEFAULT_CONTRACT_TYPE + "," + UPDATED_CONTRACT_TYPE,
            "contractType.in=" + UPDATED_CONTRACT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractType is not null
        defaultContractFiltering("contractType.specified=true", "contractType.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where title equals to
        defaultContractFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContractsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where title in
        defaultContractFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContractsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where title is not null
        defaultContractFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where title contains
        defaultContractFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllContractsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where title does not contain
        defaultContractFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllContractsByPartyAIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyA equals to
        defaultContractFiltering("partyA.equals=" + DEFAULT_PARTY_A, "partyA.equals=" + UPDATED_PARTY_A);
    }

    @Test
    @Transactional
    void getAllContractsByPartyAIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyA in
        defaultContractFiltering("partyA.in=" + DEFAULT_PARTY_A + "," + UPDATED_PARTY_A, "partyA.in=" + UPDATED_PARTY_A);
    }

    @Test
    @Transactional
    void getAllContractsByPartyAIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyA is not null
        defaultContractFiltering("partyA.specified=true", "partyA.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByPartyAContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyA contains
        defaultContractFiltering("partyA.contains=" + DEFAULT_PARTY_A, "partyA.contains=" + UPDATED_PARTY_A);
    }

    @Test
    @Transactional
    void getAllContractsByPartyANotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyA does not contain
        defaultContractFiltering("partyA.doesNotContain=" + UPDATED_PARTY_A, "partyA.doesNotContain=" + DEFAULT_PARTY_A);
    }

    @Test
    @Transactional
    void getAllContractsByPartyBIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyB equals to
        defaultContractFiltering("partyB.equals=" + DEFAULT_PARTY_B, "partyB.equals=" + UPDATED_PARTY_B);
    }

    @Test
    @Transactional
    void getAllContractsByPartyBIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyB in
        defaultContractFiltering("partyB.in=" + DEFAULT_PARTY_B + "," + UPDATED_PARTY_B, "partyB.in=" + UPDATED_PARTY_B);
    }

    @Test
    @Transactional
    void getAllContractsByPartyBIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyB is not null
        defaultContractFiltering("partyB.specified=true", "partyB.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByPartyBContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyB contains
        defaultContractFiltering("partyB.contains=" + DEFAULT_PARTY_B, "partyB.contains=" + UPDATED_PARTY_B);
    }

    @Test
    @Transactional
    void getAllContractsByPartyBNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where partyB does not contain
        defaultContractFiltering("partyB.doesNotContain=" + UPDATED_PARTY_B, "partyB.doesNotContain=" + DEFAULT_PARTY_B);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate equals to
        defaultContractFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate in
        defaultContractFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is not null
        defaultContractFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is greater than or equal to
        defaultContractFiltering(
            "startDate.greaterThanOrEqual=" + DEFAULT_START_DATE,
            "startDate.greaterThanOrEqual=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is less than or equal to
        defaultContractFiltering("startDate.lessThanOrEqual=" + DEFAULT_START_DATE, "startDate.lessThanOrEqual=" + SMALLER_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is less than
        defaultContractFiltering("startDate.lessThan=" + UPDATED_START_DATE, "startDate.lessThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByStartDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where startDate is greater than
        defaultContractFiltering("startDate.greaterThan=" + SMALLER_START_DATE, "startDate.greaterThan=" + DEFAULT_START_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate equals to
        defaultContractFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate in
        defaultContractFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is not null
        defaultContractFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is greater than or equal to
        defaultContractFiltering("endDate.greaterThanOrEqual=" + DEFAULT_END_DATE, "endDate.greaterThanOrEqual=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is less than or equal to
        defaultContractFiltering("endDate.lessThanOrEqual=" + DEFAULT_END_DATE, "endDate.lessThanOrEqual=" + SMALLER_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is less than
        defaultContractFiltering("endDate.lessThan=" + UPDATED_END_DATE, "endDate.lessThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByEndDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where endDate is greater than
        defaultContractFiltering("endDate.greaterThan=" + SMALLER_END_DATE, "endDate.greaterThan=" + DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByAutoRenewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where autoRenew equals to
        defaultContractFiltering("autoRenew.equals=" + DEFAULT_AUTO_RENEW, "autoRenew.equals=" + UPDATED_AUTO_RENEW);
    }

    @Test
    @Transactional
    void getAllContractsByAutoRenewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where autoRenew in
        defaultContractFiltering("autoRenew.in=" + DEFAULT_AUTO_RENEW + "," + UPDATED_AUTO_RENEW, "autoRenew.in=" + UPDATED_AUTO_RENEW);
    }

    @Test
    @Transactional
    void getAllContractsByAutoRenewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where autoRenew is not null
        defaultContractFiltering("autoRenew.specified=true", "autoRenew.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue equals to
        defaultContractFiltering("contractValue.equals=" + DEFAULT_CONTRACT_VALUE, "contractValue.equals=" + UPDATED_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue in
        defaultContractFiltering(
            "contractValue.in=" + DEFAULT_CONTRACT_VALUE + "," + UPDATED_CONTRACT_VALUE,
            "contractValue.in=" + UPDATED_CONTRACT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue is not null
        defaultContractFiltering("contractValue.specified=true", "contractValue.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue is greater than or equal to
        defaultContractFiltering(
            "contractValue.greaterThanOrEqual=" + DEFAULT_CONTRACT_VALUE,
            "contractValue.greaterThanOrEqual=" + UPDATED_CONTRACT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue is less than or equal to
        defaultContractFiltering(
            "contractValue.lessThanOrEqual=" + DEFAULT_CONTRACT_VALUE,
            "contractValue.lessThanOrEqual=" + SMALLER_CONTRACT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue is less than
        defaultContractFiltering("contractValue.lessThan=" + UPDATED_CONTRACT_VALUE, "contractValue.lessThan=" + DEFAULT_CONTRACT_VALUE);
    }

    @Test
    @Transactional
    void getAllContractsByContractValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where contractValue is greater than
        defaultContractFiltering(
            "contractValue.greaterThan=" + SMALLER_CONTRACT_VALUE,
            "contractValue.greaterThan=" + DEFAULT_CONTRACT_VALUE
        );
    }

    @Test
    @Transactional
    void getAllContractsByCurrencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where currency equals to
        defaultContractFiltering("currency.equals=" + DEFAULT_CURRENCY, "currency.equals=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContractsByCurrencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where currency in
        defaultContractFiltering("currency.in=" + DEFAULT_CURRENCY + "," + UPDATED_CURRENCY, "currency.in=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContractsByCurrencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where currency is not null
        defaultContractFiltering("currency.specified=true", "currency.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByCurrencyContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where currency contains
        defaultContractFiltering("currency.contains=" + DEFAULT_CURRENCY, "currency.contains=" + UPDATED_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContractsByCurrencyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where currency does not contain
        defaultContractFiltering("currency.doesNotContain=" + UPDATED_CURRENCY, "currency.doesNotContain=" + DEFAULT_CURRENCY);
    }

    @Test
    @Transactional
    void getAllContractsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where status equals to
        defaultContractFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllContractsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where status in
        defaultContractFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllContractsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where status is not null
        defaultContractFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllContractsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where createdDate equals to
        defaultContractFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllContractsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where createdDate in
        defaultContractFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllContractsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        // Get all the contractList where createdDate is not null
        defaultContractFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultContractFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultContractShouldBeFound(shouldBeFound);
        defaultContractShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultContractShouldBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partyA").value(hasItem(DEFAULT_PARTY_A)))
            .andExpect(jsonPath("$.[*].partyB").value(hasItem(DEFAULT_PARTY_B)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].autoRenew").value(hasItem(DEFAULT_AUTO_RENEW)))
            .andExpect(jsonPath("$.[*].contractValue").value(hasItem(DEFAULT_CONTRACT_VALUE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultContractShouldNotBeFound(String filter) throws Exception {
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restContractMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingContract() throws Exception {
        // Get the contract
        restContractMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        contractSearchRepository.save(contract);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());

        // Update the contract
        Contract updatedContract = contractRepository.findById(contract.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedContract are not directly saved in db
        em.detach(updatedContract);
        updatedContract
            .documentId(UPDATED_DOCUMENT_ID)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractType(UPDATED_CONTRACT_TYPE)
            .title(UPDATED_TITLE)
            .partyA(UPDATED_PARTY_A)
            .partyB(UPDATED_PARTY_B)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .autoRenew(UPDATED_AUTO_RENEW)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);
        ContractDTO contractDTO = contractMapper.toDto(updatedContract);

        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedContractToMatchAllProperties(updatedContract);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Contract> contractSearchList = Streamable.of(contractSearchRepository.findAll()).toList();
                Contract testContractSearch = contractSearchList.get(searchDatabaseSizeAfter - 1);

                assertContractAllPropertiesEquals(testContractSearch, updatedContract);
            });
    }

    @Test
    @Transactional
    void putNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .partyA(UPDATED_PARTY_A)
            .autoRenew(UPDATED_AUTO_RENEW)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedContract, contract), getPersistedContract(contract));
    }

    @Test
    @Transactional
    void fullUpdateContractWithPatch() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the contract using partial update
        Contract partialUpdatedContract = new Contract();
        partialUpdatedContract.setId(contract.getId());

        partialUpdatedContract
            .documentId(UPDATED_DOCUMENT_ID)
            .contractNumber(UPDATED_CONTRACT_NUMBER)
            .contractType(UPDATED_CONTRACT_TYPE)
            .title(UPDATED_TITLE)
            .partyA(UPDATED_PARTY_A)
            .partyB(UPDATED_PARTY_B)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .autoRenew(UPDATED_AUTO_RENEW)
            .contractValue(UPDATED_CONTRACT_VALUE)
            .currency(UPDATED_CURRENCY)
            .status(UPDATED_STATUS)
            .createdDate(UPDATED_CREATED_DATE);

        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedContract.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedContract))
            )
            .andExpect(status().isOk());

        // Validate the Contract in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertContractUpdatableFieldsEquals(partialUpdatedContract, getPersistedContract(partialUpdatedContract));
    }

    @Test
    @Transactional
    void patchNonExistingContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, contractDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(contractDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamContract() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        contract.setId(longCount.incrementAndGet());

        // Create the Contract
        ContractDTO contractDTO = contractMapper.toDto(contract);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restContractMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(contractDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Contract in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);
        contractRepository.save(contract);
        contractSearchRepository.save(contract);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the contract
        restContractMockMvc
            .perform(delete(ENTITY_API_URL_ID, contract.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(contractSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchContract() throws Exception {
        // Initialize the database
        insertedContract = contractRepository.saveAndFlush(contract);
        contractSearchRepository.save(contract);

        // Search the contract
        restContractMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + contract.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(contract.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].contractNumber").value(hasItem(DEFAULT_CONTRACT_NUMBER)))
            .andExpect(jsonPath("$.[*].contractType").value(hasItem(DEFAULT_CONTRACT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].partyA").value(hasItem(DEFAULT_PARTY_A)))
            .andExpect(jsonPath("$.[*].partyB").value(hasItem(DEFAULT_PARTY_B)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].autoRenew").value(hasItem(DEFAULT_AUTO_RENEW)))
            .andExpect(jsonPath("$.[*].contractValue").value(hasItem(DEFAULT_CONTRACT_VALUE)))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return contractRepository.count();
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

    protected Contract getPersistedContract(Contract contract) {
        return contractRepository.findById(contract.getId()).orElseThrow();
    }

    protected void assertPersistedContractToMatchAllProperties(Contract expectedContract) {
        assertContractAllPropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }

    protected void assertPersistedContractToMatchUpdatableProperties(Contract expectedContract) {
        assertContractAllUpdatablePropertiesEquals(expectedContract, getPersistedContract(expectedContract));
    }
}
