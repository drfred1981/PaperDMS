package fr.smartprod.paperdms.workflow.web.rest;

import static fr.smartprod.paperdms.workflow.domain.WorkflowStepAsserts.*;
import static fr.smartprod.paperdms.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.workflow.IntegrationTest;
import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.domain.enumeration.AssigneeType;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowStepType;
import fr.smartprod.paperdms.workflow.repository.WorkflowStepRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowStepSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowStepDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowStepMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WorkflowStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowStepResourceIT {

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;
    private static final Integer SMALLER_STEP_NUMBER = 1 - 1;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final WorkflowStepType DEFAULT_STEP_TYPE = WorkflowStepType.APPROVAL;
    private static final WorkflowStepType UPDATED_STEP_TYPE = WorkflowStepType.REVIEW;

    private static final AssigneeType DEFAULT_ASSIGNEE_TYPE = AssigneeType.USER;
    private static final AssigneeType UPDATED_ASSIGNEE_TYPE = AssigneeType.GROUP;

    private static final String DEFAULT_ASSIGNEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNEE_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_DUE_IN_DAYS = 1;
    private static final Integer UPDATED_DUE_IN_DAYS = 2;
    private static final Integer SMALLER_DUE_IN_DAYS = 1 - 1;

    private static final Boolean DEFAULT_IS_REQUIRED = false;
    private static final Boolean UPDATED_IS_REQUIRED = true;

    private static final Boolean DEFAULT_CAN_DELEGATE = false;
    private static final Boolean UPDATED_CAN_DELEGATE = true;

    private static final Boolean DEFAULT_CAN_REJECT = false;
    private static final Boolean UPDATED_CAN_REJECT = true;

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workflow-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/workflow-steps/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowStepRepository workflowStepRepository;

    @Autowired
    private WorkflowStepMapper workflowStepMapper;

    @Autowired
    private WorkflowStepSearchRepository workflowStepSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowStepMockMvc;

    private WorkflowStep workflowStep;

    private WorkflowStep insertedWorkflowStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createEntity() {
        return new WorkflowStep()
            .stepNumber(DEFAULT_STEP_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .stepType(DEFAULT_STEP_TYPE)
            .assigneeType(DEFAULT_ASSIGNEE_TYPE)
            .assigneeId(DEFAULT_ASSIGNEE_ID)
            .assigneeGroup(DEFAULT_ASSIGNEE_GROUP)
            .dueInDays(DEFAULT_DUE_IN_DAYS)
            .isRequired(DEFAULT_IS_REQUIRED)
            .canDelegate(DEFAULT_CAN_DELEGATE)
            .canReject(DEFAULT_CAN_REJECT)
            .configuration(DEFAULT_CONFIGURATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createUpdatedEntity() {
        return new WorkflowStep()
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);
    }

    @BeforeEach
    void initTest() {
        workflowStep = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowStep != null) {
            workflowStepRepository.delete(insertedWorkflowStep);
            workflowStepSearchRepository.delete(insertedWorkflowStep);
            insertedWorkflowStep = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowStep() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);
        var returnedWorkflowStepDTO = om.readValue(
            restWorkflowStepMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowStepDTO.class
        );

        // Validate the WorkflowStep in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowStep = workflowStepMapper.toEntity(returnedWorkflowStepDTO);
        assertWorkflowStepUpdatableFieldsEquals(returnedWorkflowStep, getPersistedWorkflowStep(returnedWorkflowStep));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWorkflowStep = returnedWorkflowStep;
    }

    @Test
    @Transactional
    void createWorkflowStepWithExistingId() throws Exception {
        // Create the WorkflowStep with an existing ID
        workflowStep.setId(1L);
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // set the field null
        workflowStep.setStepNumber(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // set the field null
        workflowStep.setName(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // set the field null
        workflowStep.setIsRequired(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCanDelegateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // set the field null
        workflowStep.setCanDelegate(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCanRejectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        // set the field null
        workflowStep.setCanReject(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWorkflowSteps() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepType").value(hasItem(DEFAULT_STEP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeType").value(hasItem(DEFAULT_ASSIGNEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].assigneeGroup").value(hasItem(DEFAULT_ASSIGNEE_GROUP)))
            .andExpect(jsonPath("$.[*].dueInDays").value(hasItem(DEFAULT_DUE_IN_DAYS)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].canReject").value(hasItem(DEFAULT_CAN_REJECT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)));
    }

    @Test
    @Transactional
    void getWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get the workflowStep
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowStep.getId().intValue()))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stepType").value(DEFAULT_STEP_TYPE.toString()))
            .andExpect(jsonPath("$.assigneeType").value(DEFAULT_ASSIGNEE_TYPE.toString()))
            .andExpect(jsonPath("$.assigneeId").value(DEFAULT_ASSIGNEE_ID))
            .andExpect(jsonPath("$.assigneeGroup").value(DEFAULT_ASSIGNEE_GROUP))
            .andExpect(jsonPath("$.dueInDays").value(DEFAULT_DUE_IN_DAYS))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.canDelegate").value(DEFAULT_CAN_DELEGATE))
            .andExpect(jsonPath("$.canReject").value(DEFAULT_CAN_REJECT))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION));
    }

    @Test
    @Transactional
    void getWorkflowStepsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        Long id = workflowStep.getId();

        defaultWorkflowStepFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkflowStepFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkflowStepFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber equals to
        defaultWorkflowStepFiltering("stepNumber.equals=" + DEFAULT_STEP_NUMBER, "stepNumber.equals=" + UPDATED_STEP_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber in
        defaultWorkflowStepFiltering(
            "stepNumber.in=" + DEFAULT_STEP_NUMBER + "," + UPDATED_STEP_NUMBER,
            "stepNumber.in=" + UPDATED_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber is not null
        defaultWorkflowStepFiltering("stepNumber.specified=true", "stepNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber is greater than or equal to
        defaultWorkflowStepFiltering(
            "stepNumber.greaterThanOrEqual=" + DEFAULT_STEP_NUMBER,
            "stepNumber.greaterThanOrEqual=" + UPDATED_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber is less than or equal to
        defaultWorkflowStepFiltering(
            "stepNumber.lessThanOrEqual=" + DEFAULT_STEP_NUMBER,
            "stepNumber.lessThanOrEqual=" + SMALLER_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber is less than
        defaultWorkflowStepFiltering("stepNumber.lessThan=" + UPDATED_STEP_NUMBER, "stepNumber.lessThan=" + DEFAULT_STEP_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepNumber is greater than
        defaultWorkflowStepFiltering("stepNumber.greaterThan=" + SMALLER_STEP_NUMBER, "stepNumber.greaterThan=" + DEFAULT_STEP_NUMBER);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where name equals to
        defaultWorkflowStepFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where name in
        defaultWorkflowStepFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where name is not null
        defaultWorkflowStepFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where name contains
        defaultWorkflowStepFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where name does not contain
        defaultWorkflowStepFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepType equals to
        defaultWorkflowStepFiltering("stepType.equals=" + DEFAULT_STEP_TYPE, "stepType.equals=" + UPDATED_STEP_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepType in
        defaultWorkflowStepFiltering("stepType.in=" + DEFAULT_STEP_TYPE + "," + UPDATED_STEP_TYPE, "stepType.in=" + UPDATED_STEP_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByStepTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where stepType is not null
        defaultWorkflowStepFiltering("stepType.specified=true", "stepType.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeType equals to
        defaultWorkflowStepFiltering("assigneeType.equals=" + DEFAULT_ASSIGNEE_TYPE, "assigneeType.equals=" + UPDATED_ASSIGNEE_TYPE);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeType in
        defaultWorkflowStepFiltering(
            "assigneeType.in=" + DEFAULT_ASSIGNEE_TYPE + "," + UPDATED_ASSIGNEE_TYPE,
            "assigneeType.in=" + UPDATED_ASSIGNEE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeType is not null
        defaultWorkflowStepFiltering("assigneeType.specified=true", "assigneeType.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeId equals to
        defaultWorkflowStepFiltering("assigneeId.equals=" + DEFAULT_ASSIGNEE_ID, "assigneeId.equals=" + UPDATED_ASSIGNEE_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeId in
        defaultWorkflowStepFiltering(
            "assigneeId.in=" + DEFAULT_ASSIGNEE_ID + "," + UPDATED_ASSIGNEE_ID,
            "assigneeId.in=" + UPDATED_ASSIGNEE_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeId is not null
        defaultWorkflowStepFiltering("assigneeId.specified=true", "assigneeId.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeIdContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeId contains
        defaultWorkflowStepFiltering("assigneeId.contains=" + DEFAULT_ASSIGNEE_ID, "assigneeId.contains=" + UPDATED_ASSIGNEE_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeId does not contain
        defaultWorkflowStepFiltering(
            "assigneeId.doesNotContain=" + UPDATED_ASSIGNEE_ID,
            "assigneeId.doesNotContain=" + DEFAULT_ASSIGNEE_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeGroupIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeGroup equals to
        defaultWorkflowStepFiltering("assigneeGroup.equals=" + DEFAULT_ASSIGNEE_GROUP, "assigneeGroup.equals=" + UPDATED_ASSIGNEE_GROUP);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeGroupIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeGroup in
        defaultWorkflowStepFiltering(
            "assigneeGroup.in=" + DEFAULT_ASSIGNEE_GROUP + "," + UPDATED_ASSIGNEE_GROUP,
            "assigneeGroup.in=" + UPDATED_ASSIGNEE_GROUP
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeGroupIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeGroup is not null
        defaultWorkflowStepFiltering("assigneeGroup.specified=true", "assigneeGroup.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeGroupContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeGroup contains
        defaultWorkflowStepFiltering(
            "assigneeGroup.contains=" + DEFAULT_ASSIGNEE_GROUP,
            "assigneeGroup.contains=" + UPDATED_ASSIGNEE_GROUP
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByAssigneeGroupNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where assigneeGroup does not contain
        defaultWorkflowStepFiltering(
            "assigneeGroup.doesNotContain=" + UPDATED_ASSIGNEE_GROUP,
            "assigneeGroup.doesNotContain=" + DEFAULT_ASSIGNEE_GROUP
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays equals to
        defaultWorkflowStepFiltering("dueInDays.equals=" + DEFAULT_DUE_IN_DAYS, "dueInDays.equals=" + UPDATED_DUE_IN_DAYS);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays in
        defaultWorkflowStepFiltering(
            "dueInDays.in=" + DEFAULT_DUE_IN_DAYS + "," + UPDATED_DUE_IN_DAYS,
            "dueInDays.in=" + UPDATED_DUE_IN_DAYS
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays is not null
        defaultWorkflowStepFiltering("dueInDays.specified=true", "dueInDays.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays is greater than or equal to
        defaultWorkflowStepFiltering(
            "dueInDays.greaterThanOrEqual=" + DEFAULT_DUE_IN_DAYS,
            "dueInDays.greaterThanOrEqual=" + UPDATED_DUE_IN_DAYS
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays is less than or equal to
        defaultWorkflowStepFiltering(
            "dueInDays.lessThanOrEqual=" + DEFAULT_DUE_IN_DAYS,
            "dueInDays.lessThanOrEqual=" + SMALLER_DUE_IN_DAYS
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays is less than
        defaultWorkflowStepFiltering("dueInDays.lessThan=" + UPDATED_DUE_IN_DAYS, "dueInDays.lessThan=" + DEFAULT_DUE_IN_DAYS);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByDueInDaysIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where dueInDays is greater than
        defaultWorkflowStepFiltering("dueInDays.greaterThan=" + SMALLER_DUE_IN_DAYS, "dueInDays.greaterThan=" + DEFAULT_DUE_IN_DAYS);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByIsRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where isRequired equals to
        defaultWorkflowStepFiltering("isRequired.equals=" + DEFAULT_IS_REQUIRED, "isRequired.equals=" + UPDATED_IS_REQUIRED);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByIsRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where isRequired in
        defaultWorkflowStepFiltering(
            "isRequired.in=" + DEFAULT_IS_REQUIRED + "," + UPDATED_IS_REQUIRED,
            "isRequired.in=" + UPDATED_IS_REQUIRED
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByIsRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where isRequired is not null
        defaultWorkflowStepFiltering("isRequired.specified=true", "isRequired.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanDelegateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canDelegate equals to
        defaultWorkflowStepFiltering("canDelegate.equals=" + DEFAULT_CAN_DELEGATE, "canDelegate.equals=" + UPDATED_CAN_DELEGATE);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanDelegateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canDelegate in
        defaultWorkflowStepFiltering(
            "canDelegate.in=" + DEFAULT_CAN_DELEGATE + "," + UPDATED_CAN_DELEGATE,
            "canDelegate.in=" + UPDATED_CAN_DELEGATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanDelegateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canDelegate is not null
        defaultWorkflowStepFiltering("canDelegate.specified=true", "canDelegate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanRejectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canReject equals to
        defaultWorkflowStepFiltering("canReject.equals=" + DEFAULT_CAN_REJECT, "canReject.equals=" + UPDATED_CAN_REJECT);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanRejectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canReject in
        defaultWorkflowStepFiltering("canReject.in=" + DEFAULT_CAN_REJECT + "," + UPDATED_CAN_REJECT, "canReject.in=" + UPDATED_CAN_REJECT);
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByCanRejectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList where canReject is not null
        defaultWorkflowStepFiltering("canReject.specified=true", "canReject.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowStepsByWorkflowIsEqualToSomething() throws Exception {
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflowStepRepository.saveAndFlush(workflowStep);
            workflow = WorkflowResourceIT.createEntity();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        em.persist(workflow);
        em.flush();
        workflowStep.setWorkflow(workflow);
        workflowStepRepository.saveAndFlush(workflowStep);
        Long workflowId = workflow.getId();
        // Get all the workflowStepList where workflow equals to workflowId
        defaultWorkflowStepShouldBeFound("workflowId.equals=" + workflowId);

        // Get all the workflowStepList where workflow equals to (workflowId + 1)
        defaultWorkflowStepShouldNotBeFound("workflowId.equals=" + (workflowId + 1));
    }

    private void defaultWorkflowStepFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkflowStepShouldBeFound(shouldBeFound);
        defaultWorkflowStepShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkflowStepShouldBeFound(String filter) throws Exception {
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepType").value(hasItem(DEFAULT_STEP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeType").value(hasItem(DEFAULT_ASSIGNEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].assigneeGroup").value(hasItem(DEFAULT_ASSIGNEE_GROUP)))
            .andExpect(jsonPath("$.[*].dueInDays").value(hasItem(DEFAULT_DUE_IN_DAYS)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].canReject").value(hasItem(DEFAULT_CAN_REJECT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)));

        // Check, that the count call also returns 1
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkflowStepShouldNotBeFound(String filter) throws Exception {
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowStep() throws Exception {
        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStepSearchRepository.save(workflowStep);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());

        // Update the workflowStep
        WorkflowStep updatedWorkflowStep = workflowStepRepository.findById(workflowStep.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowStep are not directly saved in db
        em.detach(updatedWorkflowStep);
        updatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(updatedWorkflowStep);

        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowStepToMatchAllProperties(updatedWorkflowStep);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WorkflowStep> workflowStepSearchList = Streamable.of(workflowStepSearchRepository.findAll()).toList();
                WorkflowStep testWorkflowStepSearch = workflowStepSearchList.get(searchDatabaseSizeAfter - 1);

                assertWorkflowStepAllPropertiesEquals(testWorkflowStepSearch, updatedWorkflowStep);
            });
    }

    @Test
    @Transactional
    void putNonExistingWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowStepWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowStep using partial update
        WorkflowStep partialUpdatedWorkflowStep = new WorkflowStep();
        partialUpdatedWorkflowStep.setId(workflowStep.getId());

        partialUpdatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);

        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowStepUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowStep, workflowStep),
            getPersistedWorkflowStep(workflowStep)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowStepWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowStep using partial update
        WorkflowStep partialUpdatedWorkflowStep = new WorkflowStep();
        partialUpdatedWorkflowStep.setId(workflowStep.getId());

        partialUpdatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);

        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowStepUpdatableFieldsEquals(partialUpdatedWorkflowStep, getPersistedWorkflowStep(partialUpdatedWorkflowStep));
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);
        workflowStepRepository.save(workflowStep);
        workflowStepSearchRepository.save(workflowStep);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the workflowStep
        restWorkflowStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowStepSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);
        workflowStepSearchRepository.save(workflowStep);

        // Search the workflowStep
        restWorkflowStepMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].stepType").value(hasItem(DEFAULT_STEP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeType").value(hasItem(DEFAULT_ASSIGNEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].assigneeGroup").value(hasItem(DEFAULT_ASSIGNEE_GROUP)))
            .andExpect(jsonPath("$.[*].dueInDays").value(hasItem(DEFAULT_DUE_IN_DAYS)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].canReject").value(hasItem(DEFAULT_CAN_REJECT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION.toString())));
    }

    protected long getRepositoryCount() {
        return workflowStepRepository.count();
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

    protected WorkflowStep getPersistedWorkflowStep(WorkflowStep workflowStep) {
        return workflowStepRepository.findById(workflowStep.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowStepToMatchAllProperties(WorkflowStep expectedWorkflowStep) {
        assertWorkflowStepAllPropertiesEquals(expectedWorkflowStep, getPersistedWorkflowStep(expectedWorkflowStep));
    }

    protected void assertPersistedWorkflowStepToMatchUpdatableProperties(WorkflowStep expectedWorkflowStep) {
        assertWorkflowStepAllUpdatablePropertiesEquals(expectedWorkflowStep, getPersistedWorkflowStep(expectedWorkflowStep));
    }
}
