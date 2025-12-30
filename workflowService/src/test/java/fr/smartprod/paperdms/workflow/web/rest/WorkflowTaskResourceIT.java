package fr.smartprod.paperdms.workflow.web.rest;

import static fr.smartprod.paperdms.workflow.domain.WorkflowTaskAsserts.*;
import static fr.smartprod.paperdms.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.workflow.IntegrationTest;
import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskAction;
import fr.smartprod.paperdms.workflow.domain.enumeration.TaskStatus;
import fr.smartprod.paperdms.workflow.repository.WorkflowTaskRepository;
import fr.smartprod.paperdms.workflow.repository.search.WorkflowTaskSearchRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowTaskDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowTaskMapper;
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
 * Integration tests for the {@link WorkflowTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowTaskResourceIT {

    private static final String DEFAULT_ASSIGNEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_ID = "BBBBBBBBBB";

    private static final TaskStatus DEFAULT_STATUS = TaskStatus.PENDING;
    private static final TaskStatus UPDATED_STATUS = TaskStatus.IN_PROGRESS;

    private static final TaskAction DEFAULT_ACTION = TaskAction.APPROVE;
    private static final TaskAction UPDATED_ACTION = TaskAction.REJECT;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_ASSIGNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_REMINDER_SENT = false;
    private static final Boolean UPDATED_REMINDER_SENT = true;

    private static final String DEFAULT_DELEGATED_TO = "AAAAAAAAAA";
    private static final String UPDATED_DELEGATED_TO = "BBBBBBBBBB";

    private static final Instant DEFAULT_DELEGATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DELEGATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/workflow-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/workflow-tasks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowTaskRepository workflowTaskRepository;

    @Autowired
    private WorkflowTaskMapper workflowTaskMapper;

    @Autowired
    private WorkflowTaskSearchRepository workflowTaskSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowTaskMockMvc;

    private WorkflowTask workflowTask;

    private WorkflowTask insertedWorkflowTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTask createEntity() {
        return new WorkflowTask()
            .assigneeId(DEFAULT_ASSIGNEE_ID)
            .status(DEFAULT_STATUS)
            .action(DEFAULT_ACTION)
            .comment(DEFAULT_COMMENT)
            .assignedDate(DEFAULT_ASSIGNED_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .completedDate(DEFAULT_COMPLETED_DATE)
            .reminderSent(DEFAULT_REMINDER_SENT)
            .delegatedTo(DEFAULT_DELEGATED_TO)
            .delegatedDate(DEFAULT_DELEGATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowTask createUpdatedEntity() {
        return new WorkflowTask()
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .status(UPDATED_STATUS)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .reminderSent(UPDATED_REMINDER_SENT)
            .delegatedTo(UPDATED_DELEGATED_TO)
            .delegatedDate(UPDATED_DELEGATED_DATE);
    }

    @BeforeEach
    void initTest() {
        workflowTask = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowTask != null) {
            workflowTaskRepository.delete(insertedWorkflowTask);
            workflowTaskSearchRepository.delete(insertedWorkflowTask);
            insertedWorkflowTask = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);
        var returnedWorkflowTaskDTO = om.readValue(
            restWorkflowTaskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowTaskDTO.class
        );

        // Validate the WorkflowTask in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowTask = workflowTaskMapper.toEntity(returnedWorkflowTaskDTO);
        assertWorkflowTaskUpdatableFieldsEquals(returnedWorkflowTask, getPersistedWorkflowTask(returnedWorkflowTask));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedWorkflowTask = returnedWorkflowTask;
    }

    @Test
    @Transactional
    void createWorkflowTaskWithExistingId() throws Exception {
        // Create the WorkflowTask with an existing ID
        workflowTask.setId(1L);
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssigneeIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        // set the field null
        workflowTask.setAssigneeId(null);

        // Create the WorkflowTask, which fails.
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        restWorkflowTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssignedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        // set the field null
        workflowTask.setAssignedDate(null);

        // Create the WorkflowTask, which fails.
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        restWorkflowTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkReminderSentIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        // set the field null
        workflowTask.setReminderSent(null);

        // Create the WorkflowTask, which fails.
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        restWorkflowTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllWorkflowTasks() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reminderSent").value(hasItem(DEFAULT_REMINDER_SENT)))
            .andExpect(jsonPath("$.[*].delegatedTo").value(hasItem(DEFAULT_DELEGATED_TO)))
            .andExpect(jsonPath("$.[*].delegatedDate").value(hasItem(DEFAULT_DELEGATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getWorkflowTask() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get the workflowTask
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowTask.getId().intValue()))
            .andExpect(jsonPath("$.assigneeId").value(DEFAULT_ASSIGNEE_ID))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.assignedDate").value(DEFAULT_ASSIGNED_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.completedDate").value(DEFAULT_COMPLETED_DATE.toString()))
            .andExpect(jsonPath("$.reminderSent").value(DEFAULT_REMINDER_SENT))
            .andExpect(jsonPath("$.delegatedTo").value(DEFAULT_DELEGATED_TO))
            .andExpect(jsonPath("$.delegatedDate").value(DEFAULT_DELEGATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getWorkflowTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        Long id = workflowTask.getId();

        defaultWorkflowTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkflowTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkflowTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssigneeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assigneeId equals to
        defaultWorkflowTaskFiltering("assigneeId.equals=" + DEFAULT_ASSIGNEE_ID, "assigneeId.equals=" + UPDATED_ASSIGNEE_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssigneeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assigneeId in
        defaultWorkflowTaskFiltering(
            "assigneeId.in=" + DEFAULT_ASSIGNEE_ID + "," + UPDATED_ASSIGNEE_ID,
            "assigneeId.in=" + UPDATED_ASSIGNEE_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssigneeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assigneeId is not null
        defaultWorkflowTaskFiltering("assigneeId.specified=true", "assigneeId.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssigneeIdContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assigneeId contains
        defaultWorkflowTaskFiltering("assigneeId.contains=" + DEFAULT_ASSIGNEE_ID, "assigneeId.contains=" + UPDATED_ASSIGNEE_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssigneeIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assigneeId does not contain
        defaultWorkflowTaskFiltering(
            "assigneeId.doesNotContain=" + UPDATED_ASSIGNEE_ID,
            "assigneeId.doesNotContain=" + DEFAULT_ASSIGNEE_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where status equals to
        defaultWorkflowTaskFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where status in
        defaultWorkflowTaskFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where status is not null
        defaultWorkflowTaskFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where action equals to
        defaultWorkflowTaskFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where action in
        defaultWorkflowTaskFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where action is not null
        defaultWorkflowTaskFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssignedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assignedDate equals to
        defaultWorkflowTaskFiltering("assignedDate.equals=" + DEFAULT_ASSIGNED_DATE, "assignedDate.equals=" + UPDATED_ASSIGNED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssignedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assignedDate in
        defaultWorkflowTaskFiltering(
            "assignedDate.in=" + DEFAULT_ASSIGNED_DATE + "," + UPDATED_ASSIGNED_DATE,
            "assignedDate.in=" + UPDATED_ASSIGNED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByAssignedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where assignedDate is not null
        defaultWorkflowTaskFiltering("assignedDate.specified=true", "assignedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where dueDate equals to
        defaultWorkflowTaskFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where dueDate in
        defaultWorkflowTaskFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where dueDate is not null
        defaultWorkflowTaskFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByCompletedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where completedDate equals to
        defaultWorkflowTaskFiltering("completedDate.equals=" + DEFAULT_COMPLETED_DATE, "completedDate.equals=" + UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByCompletedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where completedDate in
        defaultWorkflowTaskFiltering(
            "completedDate.in=" + DEFAULT_COMPLETED_DATE + "," + UPDATED_COMPLETED_DATE,
            "completedDate.in=" + UPDATED_COMPLETED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByCompletedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where completedDate is not null
        defaultWorkflowTaskFiltering("completedDate.specified=true", "completedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByReminderSentIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where reminderSent equals to
        defaultWorkflowTaskFiltering("reminderSent.equals=" + DEFAULT_REMINDER_SENT, "reminderSent.equals=" + UPDATED_REMINDER_SENT);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByReminderSentIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where reminderSent in
        defaultWorkflowTaskFiltering(
            "reminderSent.in=" + DEFAULT_REMINDER_SENT + "," + UPDATED_REMINDER_SENT,
            "reminderSent.in=" + UPDATED_REMINDER_SENT
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByReminderSentIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where reminderSent is not null
        defaultWorkflowTaskFiltering("reminderSent.specified=true", "reminderSent.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedToIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedTo equals to
        defaultWorkflowTaskFiltering("delegatedTo.equals=" + DEFAULT_DELEGATED_TO, "delegatedTo.equals=" + UPDATED_DELEGATED_TO);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedToIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedTo in
        defaultWorkflowTaskFiltering(
            "delegatedTo.in=" + DEFAULT_DELEGATED_TO + "," + UPDATED_DELEGATED_TO,
            "delegatedTo.in=" + UPDATED_DELEGATED_TO
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedToIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedTo is not null
        defaultWorkflowTaskFiltering("delegatedTo.specified=true", "delegatedTo.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedToContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedTo contains
        defaultWorkflowTaskFiltering("delegatedTo.contains=" + DEFAULT_DELEGATED_TO, "delegatedTo.contains=" + UPDATED_DELEGATED_TO);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedToNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedTo does not contain
        defaultWorkflowTaskFiltering(
            "delegatedTo.doesNotContain=" + UPDATED_DELEGATED_TO,
            "delegatedTo.doesNotContain=" + DEFAULT_DELEGATED_TO
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedDate equals to
        defaultWorkflowTaskFiltering("delegatedDate.equals=" + DEFAULT_DELEGATED_DATE, "delegatedDate.equals=" + UPDATED_DELEGATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedDate in
        defaultWorkflowTaskFiltering(
            "delegatedDate.in=" + DEFAULT_DELEGATED_DATE + "," + UPDATED_DELEGATED_DATE,
            "delegatedDate.in=" + UPDATED_DELEGATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByDelegatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        // Get all the workflowTaskList where delegatedDate is not null
        defaultWorkflowTaskFiltering("delegatedDate.specified=true", "delegatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByInstanceIsEqualToSomething() throws Exception {
        WorkflowInstance instance;
        if (TestUtil.findAll(em, WorkflowInstance.class).isEmpty()) {
            workflowTaskRepository.saveAndFlush(workflowTask);
            instance = WorkflowInstanceResourceIT.createEntity();
        } else {
            instance = TestUtil.findAll(em, WorkflowInstance.class).get(0);
        }
        em.persist(instance);
        em.flush();
        workflowTask.setInstance(instance);
        workflowTaskRepository.saveAndFlush(workflowTask);
        Long instanceId = instance.getId();
        // Get all the workflowTaskList where instance equals to instanceId
        defaultWorkflowTaskShouldBeFound("instanceId.equals=" + instanceId);

        // Get all the workflowTaskList where instance equals to (instanceId + 1)
        defaultWorkflowTaskShouldNotBeFound("instanceId.equals=" + (instanceId + 1));
    }

    @Test
    @Transactional
    void getAllWorkflowTasksByStepIsEqualToSomething() throws Exception {
        WorkflowStep step;
        if (TestUtil.findAll(em, WorkflowStep.class).isEmpty()) {
            workflowTaskRepository.saveAndFlush(workflowTask);
            step = WorkflowStepResourceIT.createEntity();
        } else {
            step = TestUtil.findAll(em, WorkflowStep.class).get(0);
        }
        em.persist(step);
        em.flush();
        workflowTask.setStep(step);
        workflowTaskRepository.saveAndFlush(workflowTask);
        Long stepId = step.getId();
        // Get all the workflowTaskList where step equals to stepId
        defaultWorkflowTaskShouldBeFound("stepId.equals=" + stepId);

        // Get all the workflowTaskList where step equals to (stepId + 1)
        defaultWorkflowTaskShouldNotBeFound("stepId.equals=" + (stepId + 1));
    }

    private void defaultWorkflowTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkflowTaskShouldBeFound(shouldBeFound);
        defaultWorkflowTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkflowTaskShouldBeFound(String filter) throws Exception {
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reminderSent").value(hasItem(DEFAULT_REMINDER_SENT)))
            .andExpect(jsonPath("$.[*].delegatedTo").value(hasItem(DEFAULT_DELEGATED_TO)))
            .andExpect(jsonPath("$.[*].delegatedDate").value(hasItem(DEFAULT_DELEGATED_DATE.toString())));

        // Check, that the count call also returns 1
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkflowTaskShouldNotBeFound(String filter) throws Exception {
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowTask() throws Exception {
        // Get the workflowTask
        restWorkflowTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowTask() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowTaskSearchRepository.save(workflowTask);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());

        // Update the workflowTask
        WorkflowTask updatedWorkflowTask = workflowTaskRepository.findById(workflowTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowTask are not directly saved in db
        em.detach(updatedWorkflowTask);
        updatedWorkflowTask
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .status(UPDATED_STATUS)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .reminderSent(UPDATED_REMINDER_SENT)
            .delegatedTo(UPDATED_DELEGATED_TO)
            .delegatedDate(UPDATED_DELEGATED_DATE);
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(updatedWorkflowTask);

        restWorkflowTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowTaskToMatchAllProperties(updatedWorkflowTask);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<WorkflowTask> workflowTaskSearchList = Streamable.of(workflowTaskSearchRepository.findAll()).toList();
                WorkflowTask testWorkflowTaskSearch = workflowTaskSearchList.get(searchDatabaseSizeAfter - 1);

                assertWorkflowTaskAllPropertiesEquals(testWorkflowTaskSearch, updatedWorkflowTask);
            });
    }

    @Test
    @Transactional
    void putNonExistingWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowTaskWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowTask using partial update
        WorkflowTask partialUpdatedWorkflowTask = new WorkflowTask();
        partialUpdatedWorkflowTask.setId(workflowTask.getId());

        partialUpdatedWorkflowTask
            .status(UPDATED_STATUS)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .delegatedTo(UPDATED_DELEGATED_TO);

        restWorkflowTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowTask))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowTaskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowTask, workflowTask),
            getPersistedWorkflowTask(workflowTask)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowTaskWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowTask using partial update
        WorkflowTask partialUpdatedWorkflowTask = new WorkflowTask();
        partialUpdatedWorkflowTask.setId(workflowTask.getId());

        partialUpdatedWorkflowTask
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .status(UPDATED_STATUS)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .reminderSent(UPDATED_REMINDER_SENT)
            .delegatedTo(UPDATED_DELEGATED_TO)
            .delegatedDate(UPDATED_DELEGATED_DATE);

        restWorkflowTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowTask))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowTaskUpdatableFieldsEquals(partialUpdatedWorkflowTask, getPersistedWorkflowTask(partialUpdatedWorkflowTask));
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        workflowTask.setId(longCount.incrementAndGet());

        // Create the WorkflowTask
        WorkflowTaskDTO workflowTaskDTO = workflowTaskMapper.toDto(workflowTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteWorkflowTask() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);
        workflowTaskRepository.save(workflowTask);
        workflowTaskSearchRepository.save(workflowTask);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the workflowTask
        restWorkflowTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(workflowTaskSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchWorkflowTask() throws Exception {
        // Initialize the database
        insertedWorkflowTask = workflowTaskRepository.saveAndFlush(workflowTask);
        workflowTaskSearchRepository.save(workflowTask);

        // Search the workflowTask
        restWorkflowTaskMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + workflowTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT.toString())))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].reminderSent").value(hasItem(DEFAULT_REMINDER_SENT)))
            .andExpect(jsonPath("$.[*].delegatedTo").value(hasItem(DEFAULT_DELEGATED_TO)))
            .andExpect(jsonPath("$.[*].delegatedDate").value(hasItem(DEFAULT_DELEGATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return workflowTaskRepository.count();
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

    protected WorkflowTask getPersistedWorkflowTask(WorkflowTask workflowTask) {
        return workflowTaskRepository.findById(workflowTask.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowTaskToMatchAllProperties(WorkflowTask expectedWorkflowTask) {
        assertWorkflowTaskAllPropertiesEquals(expectedWorkflowTask, getPersistedWorkflowTask(expectedWorkflowTask));
    }

    protected void assertPersistedWorkflowTaskToMatchUpdatableProperties(WorkflowTask expectedWorkflowTask) {
        assertWorkflowTaskAllUpdatablePropertiesEquals(expectedWorkflowTask, getPersistedWorkflowTask(expectedWorkflowTask));
    }
}
