package fr.smartprod.paperdms.workflow.web.rest;

import static fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistoryAsserts.*;
import static fr.smartprod.paperdms.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.workflow.IntegrationTest;
import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import fr.smartprod.paperdms.workflow.repository.WorkflowApprovalHistoryRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowApprovalHistorySearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowApprovalHistoryDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowApprovalHistoryMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link WorkflowApprovalHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowApprovalHistoryResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;
    private static final Integer SMALLER_STEP_NUMBER = 1 - 1;

    private static final TaskAction DEFAULT_ACTION = TaskAction.APPROVE;
    private static final TaskAction UPDATED_ACTION = TaskAction.REJECT;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTION_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_BY = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_ASSIGNEE = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_ASSIGNEE = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_TAKEN = 1L;
    private static final Long UPDATED_TIME_TAKEN = 2L;
    private static final Long SMALLER_TIME_TAKEN = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/workflow-approval-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/workflow-approval-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowApprovalHistoryRepository workflowApprovalHistoryRepository;

    @Autowired
    private WorkflowApprovalHistoryMapper workflowApprovalHistoryMapper;

    @Autowired
    private WorkflowApprovalHistorySearchRepository workflowApprovalHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowApprovalHistoryMockMvc;

    private WorkflowApprovalHistory workflowApprovalHistory;

    private WorkflowApprovalHistory insertedWorkflowApprovalHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowApprovalHistory createEntity() {
        return new WorkflowApprovalHistory()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .stepNumber(DEFAULT_STEP_NUMBER)
            .action(DEFAULT_ACTION)
            .comment(DEFAULT_COMMENT)
            .actionDate(DEFAULT_ACTION_DATE)
            .actionBy(DEFAULT_ACTION_BY)
            .previousAssignee(DEFAULT_PREVIOUS_ASSIGNEE)
            .timeTaken(DEFAULT_TIME_TAKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowApprovalHistory createUpdatedEntity() {
        return new WorkflowApprovalHistory()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);
    }

    @BeforeEach
    void initTest() {
        workflowApprovalHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowApprovalHistory != null) {
            workflowApprovalHistoryRepository.delete(insertedWorkflowApprovalHistory);
            workflowApprovalHistorySearchRepository.delete(insertedWorkflowApprovalHistory);
            insertedWorkflowApprovalHistory = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);
        var returnedWorkflowApprovalHistoryDTO = om.readValue(
            restWorkflowApprovalHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowApprovalHistoryDTO.class
        );

        // Validate the WorkflowApprovalHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowApprovalHistory = workflowApprovalHistoryMapper.toEntity(returnedWorkflowApprovalHistoryDTO);
        assertWorkflowApprovalHistoryUpdatableFieldsEquals(
            returnedWorkflowApprovalHistory,
            getPersistedWorkflowApprovalHistory(returnedWorkflowApprovalHistory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWorkflowApprovalHistory = returnedWorkflowApprovalHistory;
    }

    @Test
    @Transactional
    void createWorkflowApprovalHistoryWithExistingId() throws Exception {
        // Create the WorkflowApprovalHistory with an existing ID
        workflowApprovalHistory.setId(1L);
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        // set the field null
        workflowApprovalHistory.setDocumentSha256(null);

        // Create the WorkflowApprovalHistory, which fails.
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        restWorkflowApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        // set the field null
        workflowApprovalHistory.setStepNumber(null);

        // Create the WorkflowApprovalHistory, which fails.
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        restWorkflowApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        // set the field null
        workflowApprovalHistory.setActionDate(null);

        // Create the WorkflowApprovalHistory, which fails.
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        restWorkflowApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkActionByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        // set the field null
        workflowApprovalHistory.setActionBy(null);

        // Create the WorkflowApprovalHistory, which fails.
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        restWorkflowApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistories() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowApprovalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionBy").value(hasItem(DEFAULT_ACTION_BY)))
            .andExpect(jsonPath("$.[*].previousAssignee").value(hasItem(DEFAULT_PREVIOUS_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].timeTaken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())));
    }

    @Test
    @Transactional
    void getWorkflowApprovalHistory() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get the workflowApprovalHistory
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowApprovalHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowApprovalHistory.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.actionBy").value(DEFAULT_ACTION_BY))
            .andExpect(jsonPath("$.previousAssignee").value(DEFAULT_PREVIOUS_ASSIGNEE))
            .andExpect(jsonPath("$.timeTaken").value(DEFAULT_TIME_TAKEN.intValue()));
    }

    @Test
    @Transactional
    void getWorkflowApprovalHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        Long id = workflowApprovalHistory.getId();

        defaultWorkflowApprovalHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkflowApprovalHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkflowApprovalHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where documentSha256 equals to
        defaultWorkflowApprovalHistoryFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where documentSha256 in
        defaultWorkflowApprovalHistoryFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where documentSha256 is not null
        defaultWorkflowApprovalHistoryFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where documentSha256 contains
        defaultWorkflowApprovalHistoryFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where documentSha256 does not contain
        defaultWorkflowApprovalHistoryFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber equals to
        defaultWorkflowApprovalHistoryFiltering("stepNumber.equals=" + DEFAULT_STEP_NUMBER, "stepNumber.equals=" + UPDATED_STEP_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber in
        defaultWorkflowApprovalHistoryFiltering(
            "stepNumber.in=" + DEFAULT_STEP_NUMBER + "," + UPDATED_STEP_NUMBER,
            "stepNumber.in=" + UPDATED_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber is not null
        defaultWorkflowApprovalHistoryFiltering("stepNumber.specified=true", "stepNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber is greater than or equal to
        defaultWorkflowApprovalHistoryFiltering(
            "stepNumber.greaterThanOrEqual=" + DEFAULT_STEP_NUMBER,
            "stepNumber.greaterThanOrEqual=" + UPDATED_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber is less than or equal to
        defaultWorkflowApprovalHistoryFiltering(
            "stepNumber.lessThanOrEqual=" + DEFAULT_STEP_NUMBER,
            "stepNumber.lessThanOrEqual=" + SMALLER_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber is less than
        defaultWorkflowApprovalHistoryFiltering("stepNumber.lessThan=" + UPDATED_STEP_NUMBER, "stepNumber.lessThan=" + DEFAULT_STEP_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByStepNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where stepNumber is greater than
        defaultWorkflowApprovalHistoryFiltering(
            "stepNumber.greaterThan=" + SMALLER_STEP_NUMBER,
            "stepNumber.greaterThan=" + DEFAULT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where action equals to
        defaultWorkflowApprovalHistoryFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where action in
        defaultWorkflowApprovalHistoryFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where action is not null
        defaultWorkflowApprovalHistoryFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionDate equals to
        defaultWorkflowApprovalHistoryFiltering("actionDate.equals=" + DEFAULT_ACTION_DATE, "actionDate.equals=" + UPDATED_ACTION_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionDate in
        defaultWorkflowApprovalHistoryFiltering(
            "actionDate.in=" + DEFAULT_ACTION_DATE + "," + UPDATED_ACTION_DATE,
            "actionDate.in=" + UPDATED_ACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionDate is not null
        defaultWorkflowApprovalHistoryFiltering("actionDate.specified=true", "actionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionBy equals to
        defaultWorkflowApprovalHistoryFiltering("actionBy.equals=" + DEFAULT_ACTION_BY, "actionBy.equals=" + UPDATED_ACTION_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionBy in
        defaultWorkflowApprovalHistoryFiltering(
            "actionBy.in=" + DEFAULT_ACTION_BY + "," + UPDATED_ACTION_BY,
            "actionBy.in=" + UPDATED_ACTION_BY
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionBy is not null
        defaultWorkflowApprovalHistoryFiltering("actionBy.specified=true", "actionBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionByContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionBy contains
        defaultWorkflowApprovalHistoryFiltering("actionBy.contains=" + DEFAULT_ACTION_BY, "actionBy.contains=" + UPDATED_ACTION_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByActionByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where actionBy does not contain
        defaultWorkflowApprovalHistoryFiltering(
            "actionBy.doesNotContain=" + UPDATED_ACTION_BY,
            "actionBy.doesNotContain=" + DEFAULT_ACTION_BY
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByPreviousAssigneeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where previousAssignee equals to
        defaultWorkflowApprovalHistoryFiltering(
            "previousAssignee.equals=" + DEFAULT_PREVIOUS_ASSIGNEE,
            "previousAssignee.equals=" + UPDATED_PREVIOUS_ASSIGNEE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByPreviousAssigneeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where previousAssignee in
        defaultWorkflowApprovalHistoryFiltering(
            "previousAssignee.in=" + DEFAULT_PREVIOUS_ASSIGNEE + "," + UPDATED_PREVIOUS_ASSIGNEE,
            "previousAssignee.in=" + UPDATED_PREVIOUS_ASSIGNEE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByPreviousAssigneeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where previousAssignee is not null
        defaultWorkflowApprovalHistoryFiltering("previousAssignee.specified=true", "previousAssignee.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByPreviousAssigneeContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where previousAssignee contains
        defaultWorkflowApprovalHistoryFiltering(
            "previousAssignee.contains=" + DEFAULT_PREVIOUS_ASSIGNEE,
            "previousAssignee.contains=" + UPDATED_PREVIOUS_ASSIGNEE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByPreviousAssigneeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where previousAssignee does not contain
        defaultWorkflowApprovalHistoryFiltering(
            "previousAssignee.doesNotContain=" + UPDATED_PREVIOUS_ASSIGNEE,
            "previousAssignee.doesNotContain=" + DEFAULT_PREVIOUS_ASSIGNEE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken equals to
        defaultWorkflowApprovalHistoryFiltering("timeTaken.equals=" + DEFAULT_TIME_TAKEN, "timeTaken.equals=" + UPDATED_TIME_TAKEN);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken in
        defaultWorkflowApprovalHistoryFiltering(
            "timeTaken.in=" + DEFAULT_TIME_TAKEN + "," + UPDATED_TIME_TAKEN,
            "timeTaken.in=" + UPDATED_TIME_TAKEN
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken is not null
        defaultWorkflowApprovalHistoryFiltering("timeTaken.specified=true", "timeTaken.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken is greater than or equal to
        defaultWorkflowApprovalHistoryFiltering(
            "timeTaken.greaterThanOrEqual=" + DEFAULT_TIME_TAKEN,
            "timeTaken.greaterThanOrEqual=" + UPDATED_TIME_TAKEN
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken is less than or equal to
        defaultWorkflowApprovalHistoryFiltering(
            "timeTaken.lessThanOrEqual=" + DEFAULT_TIME_TAKEN,
            "timeTaken.lessThanOrEqual=" + SMALLER_TIME_TAKEN
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken is less than
        defaultWorkflowApprovalHistoryFiltering("timeTaken.lessThan=" + UPDATED_TIME_TAKEN, "timeTaken.lessThan=" + DEFAULT_TIME_TAKEN);
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByTimeTakenIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        // Get all the workflowApprovalHistoryList where timeTaken is greater than
        defaultWorkflowApprovalHistoryFiltering(
            "timeTaken.greaterThan=" + SMALLER_TIME_TAKEN,
            "timeTaken.greaterThan=" + DEFAULT_TIME_TAKEN
        );
    }

    @Test
    @Transactional
    void getAllWorkflowApprovalHistoriesByWorkflowInstanceIsEqualToSomething() throws Exception {
        WorkflowInstance workflowInstance;
        if (TestUtil.findAll(em, WorkflowInstance.class).isEmpty()) {
            workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);
            workflowInstance = WorkflowInstanceResourceIT.createEntity();
        } else {
            workflowInstance = TestUtil.findAll(em, WorkflowInstance.class).get(0);
        }
        em.persist(workflowInstance);
        em.flush();
        workflowApprovalHistory.setWorkflowInstance(workflowInstance);
        workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);
        Long workflowInstanceId = workflowInstance.getId();
        // Get all the workflowApprovalHistoryList where workflowInstance equals to workflowInstanceId
        defaultWorkflowApprovalHistoryShouldBeFound("workflowInstanceId.equals=" + workflowInstanceId);

        // Get all the workflowApprovalHistoryList where workflowInstance equals to (workflowInstanceId + 1)
        defaultWorkflowApprovalHistoryShouldNotBeFound("workflowInstanceId.equals=" + (workflowInstanceId + 1));
    }

    private void defaultWorkflowApprovalHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkflowApprovalHistoryShouldBeFound(shouldBeFound);
        defaultWorkflowApprovalHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkflowApprovalHistoryShouldBeFound(String filter) throws Exception {
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowApprovalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionBy").value(hasItem(DEFAULT_ACTION_BY)))
            .andExpect(jsonPath("$.[*].previousAssignee").value(hasItem(DEFAULT_PREVIOUS_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].timeTaken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())));

        // Check, that the count call also returns 1
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkflowApprovalHistoryShouldNotBeFound(String filter) throws Exception {
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowApprovalHistory() throws Exception {
        // Get the workflowApprovalHistory
        restWorkflowApprovalHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowApprovalHistory() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowApprovalHistorySearchRepository.save(workflowApprovalHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());

        // Update the workflowApprovalHistory
        WorkflowApprovalHistory updatedWorkflowApprovalHistory = workflowApprovalHistoryRepository
            .findById(workflowApprovalHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowApprovalHistory are not directly saved in db
        em.detach(updatedWorkflowApprovalHistory);
        updatedWorkflowApprovalHistory
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(updatedWorkflowApprovalHistory);

        restWorkflowApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowApprovalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowApprovalHistoryToMatchAllProperties(updatedWorkflowApprovalHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WorkflowApprovalHistory> workflowApprovalHistorySearchList = Streamable.of(
                    workflowApprovalHistorySearchRepository.findAll()
                ).toList();
                WorkflowApprovalHistory testWorkflowApprovalHistorySearch = workflowApprovalHistorySearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertWorkflowApprovalHistoryAllPropertiesEquals(testWorkflowApprovalHistorySearch, updatedWorkflowApprovalHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowApprovalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowApprovalHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowApprovalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowApprovalHistory using partial update
        WorkflowApprovalHistory partialUpdatedWorkflowApprovalHistory = new WorkflowApprovalHistory();
        partialUpdatedWorkflowApprovalHistory.setId(workflowApprovalHistory.getId());

        partialUpdatedWorkflowApprovalHistory.stepNumber(UPDATED_STEP_NUMBER).comment(UPDATED_COMMENT);

        restWorkflowApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowApprovalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowApprovalHistory))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowApprovalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowApprovalHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowApprovalHistory, workflowApprovalHistory),
            getPersistedWorkflowApprovalHistory(workflowApprovalHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowApprovalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowApprovalHistory using partial update
        WorkflowApprovalHistory partialUpdatedWorkflowApprovalHistory = new WorkflowApprovalHistory();
        partialUpdatedWorkflowApprovalHistory.setId(workflowApprovalHistory.getId());

        partialUpdatedWorkflowApprovalHistory
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);

        restWorkflowApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowApprovalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowApprovalHistory))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowApprovalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowApprovalHistoryUpdatableFieldsEquals(
            partialUpdatedWorkflowApprovalHistory,
            getPersistedWorkflowApprovalHistory(partialUpdatedWorkflowApprovalHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowApprovalHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        workflowApprovalHistory.setId(longCount.incrementAndGet());

        // Create the WorkflowApprovalHistory
        WorkflowApprovalHistoryDTO workflowApprovalHistoryDTO = workflowApprovalHistoryMapper.toDto(workflowApprovalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowApprovalHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWorkflowApprovalHistory() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);
        workflowApprovalHistoryRepository.save(workflowApprovalHistory);
        workflowApprovalHistorySearchRepository.save(workflowApprovalHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the workflowApprovalHistory
        restWorkflowApprovalHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowApprovalHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowApprovalHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWorkflowApprovalHistory() throws Exception {
        // Initialize the database
        insertedWorkflowApprovalHistory = workflowApprovalHistoryRepository.saveAndFlush(workflowApprovalHistory);
        workflowApprovalHistorySearchRepository.save(workflowApprovalHistory);

        // Search the workflowApprovalHistory
        restWorkflowApprovalHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workflowApprovalHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowApprovalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionBy").value(hasItem(DEFAULT_ACTION_BY)))
            .andExpect(jsonPath("$.[*].previousAssignee").value(hasItem(DEFAULT_PREVIOUS_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].timeTaken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())));
    }

    protected long getRepositoryCount() {
        return workflowApprovalHistoryRepository.count();
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

    protected WorkflowApprovalHistory getPersistedWorkflowApprovalHistory(WorkflowApprovalHistory workflowApprovalHistory) {
        return workflowApprovalHistoryRepository.findById(workflowApprovalHistory.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowApprovalHistoryToMatchAllProperties(WorkflowApprovalHistory expectedWorkflowApprovalHistory) {
        assertWorkflowApprovalHistoryAllPropertiesEquals(
            expectedWorkflowApprovalHistory,
            getPersistedWorkflowApprovalHistory(expectedWorkflowApprovalHistory)
        );
    }

    protected void assertPersistedWorkflowApprovalHistoryToMatchUpdatableProperties(
        WorkflowApprovalHistory expectedWorkflowApprovalHistory
    ) {
        assertWorkflowApprovalHistoryAllUpdatablePropertiesEquals(
            expectedWorkflowApprovalHistory,
            getPersistedWorkflowApprovalHistory(expectedWorkflowApprovalHistory)
        );
    }
}
