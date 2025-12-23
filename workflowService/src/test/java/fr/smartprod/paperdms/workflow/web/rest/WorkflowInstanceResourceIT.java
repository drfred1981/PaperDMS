package fr.smartprod.paperdms.workflow.web.rest;

import static fr.smartprod.paperdms.workflow.domain.WorkflowInstanceAsserts.*;
import static fr.smartprod.paperdms.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.workflow.IntegrationTest;
import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.domain.WorkflowInstance;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowInstanceStatus;
import fr.smartprod.paperdms.workflow.domain.enumeration.WorkflowPriority;
import fr.smartprod.paperdms.workflow.repository.WorkflowInstanceRepository;
import fr.smartprod.paperdms.workflow.service.dto.WorkflowInstanceDTO;
import fr.smartprod.paperdms.workflow.service.mapper.WorkflowInstanceMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
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
 * Integration tests for the {@link WorkflowInstanceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowInstanceResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final WorkflowInstanceStatus DEFAULT_STATUS = WorkflowInstanceStatus.PENDING;
    private static final WorkflowInstanceStatus UPDATED_STATUS = WorkflowInstanceStatus.IN_PROGRESS;

    private static final Integer DEFAULT_CURRENT_STEP_NUMBER = 1;
    private static final Integer UPDATED_CURRENT_STEP_NUMBER = 2;
    private static final Integer SMALLER_CURRENT_STEP_NUMBER = 1 - 1;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_DUE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DUE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CANCELLED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CANCELLED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CANCELLATION_REASON = "AAAAAAAAAA";
    private static final String UPDATED_CANCELLATION_REASON = "BBBBBBBBBB";

    private static final WorkflowPriority DEFAULT_PRIORITY = WorkflowPriority.LOW;
    private static final WorkflowPriority UPDATED_PRIORITY = WorkflowPriority.NORMAL;

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workflow-instances";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowInstanceRepository workflowInstanceRepository;

    @Autowired
    private WorkflowInstanceMapper workflowInstanceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowInstanceMockMvc;

    private WorkflowInstance workflowInstance;

    private WorkflowInstance insertedWorkflowInstance;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowInstance createEntity(EntityManager em) {
        WorkflowInstance workflowInstance = new WorkflowInstance()
            .documentId(DEFAULT_DOCUMENT_ID)
            .status(DEFAULT_STATUS)
            .currentStepNumber(DEFAULT_CURRENT_STEP_NUMBER)
            .startDate(DEFAULT_START_DATE)
            .dueDate(DEFAULT_DUE_DATE)
            .completedDate(DEFAULT_COMPLETED_DATE)
            .cancelledDate(DEFAULT_CANCELLED_DATE)
            .cancellationReason(DEFAULT_CANCELLATION_REASON)
            .priority(DEFAULT_PRIORITY)
            .metadata(DEFAULT_METADATA)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflow = WorkflowResourceIT.createEntity();
            em.persist(workflow);
            em.flush();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        workflowInstance.setWorkflow(workflow);
        return workflowInstance;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowInstance createUpdatedEntity(EntityManager em) {
        WorkflowInstance updatedWorkflowInstance = new WorkflowInstance()
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .currentStepNumber(UPDATED_CURRENT_STEP_NUMBER)
            .startDate(UPDATED_START_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .cancelledDate(UPDATED_CANCELLED_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .priority(UPDATED_PRIORITY)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflow = WorkflowResourceIT.createUpdatedEntity();
            em.persist(workflow);
            em.flush();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        updatedWorkflowInstance.setWorkflow(workflow);
        return updatedWorkflowInstance;
    }

    @BeforeEach
    void initTest() {
        workflowInstance = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowInstance != null) {
            workflowInstanceRepository.delete(insertedWorkflowInstance);
            insertedWorkflowInstance = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowInstance() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);
        var returnedWorkflowInstanceDTO = om.readValue(
            restWorkflowInstanceMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowInstanceDTO.class
        );

        // Validate the WorkflowInstance in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowInstance = workflowInstanceMapper.toEntity(returnedWorkflowInstanceDTO);
        assertWorkflowInstanceUpdatableFieldsEquals(returnedWorkflowInstance, getPersistedWorkflowInstance(returnedWorkflowInstance));

        insertedWorkflowInstance = returnedWorkflowInstance;
    }

    @Test
    @Transactional
    void createWorkflowInstanceWithExistingId() throws Exception {
        // Create the WorkflowInstance with an existing ID
        workflowInstance.setId(1L);
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowInstance.setDocumentId(null);

        // Create the WorkflowInstance, which fails.
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        restWorkflowInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowInstance.setStartDate(null);

        // Create the WorkflowInstance, which fails.
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        restWorkflowInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowInstance.setCreatedBy(null);

        // Create the WorkflowInstance, which fails.
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        restWorkflowInstanceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkflowInstances() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currentStepNumber").value(hasItem(DEFAULT_CURRENT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancelledDate").value(hasItem(DEFAULT_CANCELLED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancellationReason").value(hasItem(DEFAULT_CANCELLATION_REASON)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getWorkflowInstance() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get the workflowInstance
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowInstance.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowInstance.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.currentStepNumber").value(DEFAULT_CURRENT_STEP_NUMBER))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.dueDate").value(DEFAULT_DUE_DATE.toString()))
            .andExpect(jsonPath("$.completedDate").value(DEFAULT_COMPLETED_DATE.toString()))
            .andExpect(jsonPath("$.cancelledDate").value(DEFAULT_CANCELLED_DATE.toString()))
            .andExpect(jsonPath("$.cancellationReason").value(DEFAULT_CANCELLATION_REASON))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getWorkflowInstancesByIdFiltering() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        Long id = workflowInstance.getId();

        defaultWorkflowInstanceFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkflowInstanceFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkflowInstanceFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId equals to
        defaultWorkflowInstanceFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId in
        defaultWorkflowInstanceFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId is not null
        defaultWorkflowInstanceFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId is greater than or equal to
        defaultWorkflowInstanceFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId is less than or equal to
        defaultWorkflowInstanceFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId is less than
        defaultWorkflowInstanceFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where documentId is greater than
        defaultWorkflowInstanceFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where status equals to
        defaultWorkflowInstanceFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where status in
        defaultWorkflowInstanceFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where status is not null
        defaultWorkflowInstanceFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber equals to
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.equals=" + DEFAULT_CURRENT_STEP_NUMBER,
            "currentStepNumber.equals=" + UPDATED_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber in
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.in=" + DEFAULT_CURRENT_STEP_NUMBER + "," + UPDATED_CURRENT_STEP_NUMBER,
            "currentStepNumber.in=" + UPDATED_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber is not null
        defaultWorkflowInstanceFiltering("currentStepNumber.specified=true", "currentStepNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber is greater than or equal to
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.greaterThanOrEqual=" + DEFAULT_CURRENT_STEP_NUMBER,
            "currentStepNumber.greaterThanOrEqual=" + UPDATED_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber is less than or equal to
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.lessThanOrEqual=" + DEFAULT_CURRENT_STEP_NUMBER,
            "currentStepNumber.lessThanOrEqual=" + SMALLER_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber is less than
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.lessThan=" + UPDATED_CURRENT_STEP_NUMBER,
            "currentStepNumber.lessThan=" + DEFAULT_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCurrentStepNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where currentStepNumber is greater than
        defaultWorkflowInstanceFiltering(
            "currentStepNumber.greaterThan=" + SMALLER_CURRENT_STEP_NUMBER,
            "currentStepNumber.greaterThan=" + DEFAULT_CURRENT_STEP_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where startDate equals to
        defaultWorkflowInstanceFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where startDate in
        defaultWorkflowInstanceFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where startDate is not null
        defaultWorkflowInstanceFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDueDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where dueDate equals to
        defaultWorkflowInstanceFiltering("dueDate.equals=" + DEFAULT_DUE_DATE, "dueDate.equals=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDueDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where dueDate in
        defaultWorkflowInstanceFiltering("dueDate.in=" + DEFAULT_DUE_DATE + "," + UPDATED_DUE_DATE, "dueDate.in=" + UPDATED_DUE_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByDueDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where dueDate is not null
        defaultWorkflowInstanceFiltering("dueDate.specified=true", "dueDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCompletedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where completedDate equals to
        defaultWorkflowInstanceFiltering(
            "completedDate.equals=" + DEFAULT_COMPLETED_DATE,
            "completedDate.equals=" + UPDATED_COMPLETED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCompletedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where completedDate in
        defaultWorkflowInstanceFiltering(
            "completedDate.in=" + DEFAULT_COMPLETED_DATE + "," + UPDATED_COMPLETED_DATE,
            "completedDate.in=" + UPDATED_COMPLETED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCompletedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where completedDate is not null
        defaultWorkflowInstanceFiltering("completedDate.specified=true", "completedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCancelledDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where cancelledDate equals to
        defaultWorkflowInstanceFiltering(
            "cancelledDate.equals=" + DEFAULT_CANCELLED_DATE,
            "cancelledDate.equals=" + UPDATED_CANCELLED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCancelledDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where cancelledDate in
        defaultWorkflowInstanceFiltering(
            "cancelledDate.in=" + DEFAULT_CANCELLED_DATE + "," + UPDATED_CANCELLED_DATE,
            "cancelledDate.in=" + UPDATED_CANCELLED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCancelledDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where cancelledDate is not null
        defaultWorkflowInstanceFiltering("cancelledDate.specified=true", "cancelledDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where priority equals to
        defaultWorkflowInstanceFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where priority in
        defaultWorkflowInstanceFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where priority is not null
        defaultWorkflowInstanceFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where createdBy equals to
        defaultWorkflowInstanceFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where createdBy in
        defaultWorkflowInstanceFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where createdBy is not null
        defaultWorkflowInstanceFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where createdBy contains
        defaultWorkflowInstanceFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        // Get all the workflowInstanceList where createdBy does not contain
        defaultWorkflowInstanceFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllWorkflowInstancesByWorkflowIsEqualToSomething() throws Exception {
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflowInstanceRepository.saveAndFlush(workflowInstance);
            workflow = WorkflowResourceIT.createEntity();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        em.persist(workflow);
        em.flush();
        workflowInstance.setWorkflow(workflow);
        workflowInstanceRepository.saveAndFlush(workflowInstance);
        Long workflowId = workflow.getId();
        // Get all the workflowInstanceList where workflow equals to workflowId
        defaultWorkflowInstanceShouldBeFound("workflowId.equals=" + workflowId);

        // Get all the workflowInstanceList where workflow equals to (workflowId + 1)
        defaultWorkflowInstanceShouldNotBeFound("workflowId.equals=" + (workflowId + 1));
    }

    private void defaultWorkflowInstanceFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkflowInstanceShouldBeFound(shouldBeFound);
        defaultWorkflowInstanceShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkflowInstanceShouldBeFound(String filter) throws Exception {
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowInstance.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].currentStepNumber").value(hasItem(DEFAULT_CURRENT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].dueDate").value(hasItem(DEFAULT_DUE_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancelledDate").value(hasItem(DEFAULT_CANCELLED_DATE.toString())))
            .andExpect(jsonPath("$.[*].cancellationReason").value(hasItem(DEFAULT_CANCELLATION_REASON)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkflowInstanceShouldNotBeFound(String filter) throws Exception {
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkflowInstanceMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowInstance() throws Exception {
        // Get the workflowInstance
        restWorkflowInstanceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowInstance() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowInstance
        WorkflowInstance updatedWorkflowInstance = workflowInstanceRepository.findById(workflowInstance.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowInstance are not directly saved in db
        em.detach(updatedWorkflowInstance);
        updatedWorkflowInstance
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .currentStepNumber(UPDATED_CURRENT_STEP_NUMBER)
            .startDate(UPDATED_START_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .cancelledDate(UPDATED_CANCELLED_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .priority(UPDATED_PRIORITY)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY);
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(updatedWorkflowInstance);

        restWorkflowInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowInstanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowInstanceDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowInstanceToMatchAllProperties(updatedWorkflowInstance);
    }

    @Test
    @Transactional
    void putNonExistingWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowInstanceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowInstanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowInstanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowInstanceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowInstance using partial update
        WorkflowInstance partialUpdatedWorkflowInstance = new WorkflowInstance();
        partialUpdatedWorkflowInstance.setId(workflowInstance.getId());

        partialUpdatedWorkflowInstance
            .currentStepNumber(UPDATED_CURRENT_STEP_NUMBER)
            .cancelledDate(UPDATED_CANCELLED_DATE)
            .priority(UPDATED_PRIORITY)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY);

        restWorkflowInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowInstance))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowInstance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowInstanceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowInstance, workflowInstance),
            getPersistedWorkflowInstance(workflowInstance)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowInstanceWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowInstance using partial update
        WorkflowInstance partialUpdatedWorkflowInstance = new WorkflowInstance();
        partialUpdatedWorkflowInstance.setId(workflowInstance.getId());

        partialUpdatedWorkflowInstance
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .currentStepNumber(UPDATED_CURRENT_STEP_NUMBER)
            .startDate(UPDATED_START_DATE)
            .dueDate(UPDATED_DUE_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .cancelledDate(UPDATED_CANCELLED_DATE)
            .cancellationReason(UPDATED_CANCELLATION_REASON)
            .priority(UPDATED_PRIORITY)
            .metadata(UPDATED_METADATA)
            .createdBy(UPDATED_CREATED_BY);

        restWorkflowInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowInstance.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowInstance))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowInstance in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowInstanceUpdatableFieldsEquals(
            partialUpdatedWorkflowInstance,
            getPersistedWorkflowInstance(partialUpdatedWorkflowInstance)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowInstanceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowInstanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowInstanceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowInstance() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowInstance.setId(longCount.incrementAndGet());

        // Create the WorkflowInstance
        WorkflowInstanceDTO workflowInstanceDTO = workflowInstanceMapper.toDto(workflowInstance);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowInstanceMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowInstanceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowInstance in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkflowInstance() throws Exception {
        // Initialize the database
        insertedWorkflowInstance = workflowInstanceRepository.saveAndFlush(workflowInstance);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workflowInstance
        restWorkflowInstanceMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowInstance.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workflowInstanceRepository.count();
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

    protected WorkflowInstance getPersistedWorkflowInstance(WorkflowInstance workflowInstance) {
        return workflowInstanceRepository.findById(workflowInstance.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowInstanceToMatchAllProperties(WorkflowInstance expectedWorkflowInstance) {
        assertWorkflowInstanceAllPropertiesEquals(expectedWorkflowInstance, getPersistedWorkflowInstance(expectedWorkflowInstance));
    }

    protected void assertPersistedWorkflowInstanceToMatchUpdatableProperties(WorkflowInstance expectedWorkflowInstance) {
        assertWorkflowInstanceAllUpdatablePropertiesEquals(
            expectedWorkflowInstance,
            getPersistedWorkflowInstance(expectedWorkflowInstance)
        );
    }
}
