package com.ged.workflow.web.rest;

import static com.ged.workflow.domain.WorkflowAsserts.*;
import static com.ged.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.workflow.IntegrationTest;
import com.ged.workflow.domain.Workflow;
import com.ged.workflow.repository.WorkflowRepository;
import com.ged.workflow.service.dto.WorkflowDTO;
import com.ged.workflow.service.mapper.WorkflowMapper;
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
 * Integration tests for the {@link WorkflowResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;
    private static final Integer SMALLER_VERSION = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_PARALLEL = false;
    private static final Boolean UPDATED_IS_PARALLEL = true;

    private static final Boolean DEFAULT_AUTO_START = false;
    private static final Boolean UPDATED_AUTO_START = true;

    private static final String DEFAULT_TRIGGER_EVENT = "AAAAAAAAAA";
    private static final String UPDATED_TRIGGER_EVENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workflows";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowRepository workflowRepository;

    @Autowired
    private WorkflowMapper workflowMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowMockMvc;

    private Workflow workflow;

    private Workflow insertedWorkflow;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workflow createEntity() {
        return new Workflow()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .version(DEFAULT_VERSION)
            .isActive(DEFAULT_IS_ACTIVE)
            .isParallel(DEFAULT_IS_PARALLEL)
            .autoStart(DEFAULT_AUTO_START)
            .triggerEvent(DEFAULT_TRIGGER_EVENT)
            .configuration(DEFAULT_CONFIGURATION)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Workflow createUpdatedEntity() {
        return new Workflow()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE)
            .isParallel(UPDATED_IS_PARALLEL)
            .autoStart(UPDATED_AUTO_START)
            .triggerEvent(UPDATED_TRIGGER_EVENT)
            .configuration(UPDATED_CONFIGURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        workflow = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflow != null) {
            workflowRepository.delete(insertedWorkflow);
            insertedWorkflow = null;
        }
    }

    @Test
    @Transactional
    void createWorkflow() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);
        var returnedWorkflowDTO = om.readValue(
            restWorkflowMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowDTO.class
        );

        // Validate the Workflow in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflow = workflowMapper.toEntity(returnedWorkflowDTO);
        assertWorkflowUpdatableFieldsEquals(returnedWorkflow, getPersistedWorkflow(returnedWorkflow));

        insertedWorkflow = returnedWorkflow;
    }

    @Test
    @Transactional
    void createWorkflowWithExistingId() throws Exception {
        // Create the Workflow with an existing ID
        workflow.setId(1L);
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setName(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setVersion(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setIsActive(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsParallelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setIsParallel(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAutoStartIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setAutoStart(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setCreatedDate(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflow.setCreatedBy(null);

        // Create the Workflow, which fails.
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        restWorkflowMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkflows() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isParallel").value(hasItem(DEFAULT_IS_PARALLEL)))
            .andExpect(jsonPath("$.[*].autoStart").value(hasItem(DEFAULT_AUTO_START)))
            .andExpect(jsonPath("$.[*].triggerEvent").value(hasItem(DEFAULT_TRIGGER_EVENT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getWorkflow() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get the workflow
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL_ID, workflow.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflow.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isParallel").value(DEFAULT_IS_PARALLEL))
            .andExpect(jsonPath("$.autoStart").value(DEFAULT_AUTO_START))
            .andExpect(jsonPath("$.triggerEvent").value(DEFAULT_TRIGGER_EVENT))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getWorkflowsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        Long id = workflow.getId();

        defaultWorkflowFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWorkflowFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWorkflowFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWorkflowsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where name equals to
        defaultWorkflowFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where name in
        defaultWorkflowFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where name is not null
        defaultWorkflowFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where name contains
        defaultWorkflowFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where name does not contain
        defaultWorkflowFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version equals to
        defaultWorkflowFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version in
        defaultWorkflowFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version is not null
        defaultWorkflowFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version is greater than or equal to
        defaultWorkflowFiltering("version.greaterThanOrEqual=" + DEFAULT_VERSION, "version.greaterThanOrEqual=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version is less than or equal to
        defaultWorkflowFiltering("version.lessThanOrEqual=" + DEFAULT_VERSION, "version.lessThanOrEqual=" + SMALLER_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version is less than
        defaultWorkflowFiltering("version.lessThan=" + UPDATED_VERSION, "version.lessThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByVersionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where version is greater than
        defaultWorkflowFiltering("version.greaterThan=" + SMALLER_VERSION, "version.greaterThan=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isActive equals to
        defaultWorkflowFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isActive in
        defaultWorkflowFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isActive is not null
        defaultWorkflowFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsParallelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isParallel equals to
        defaultWorkflowFiltering("isParallel.equals=" + DEFAULT_IS_PARALLEL, "isParallel.equals=" + UPDATED_IS_PARALLEL);
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsParallelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isParallel in
        defaultWorkflowFiltering(
            "isParallel.in=" + DEFAULT_IS_PARALLEL + "," + UPDATED_IS_PARALLEL,
            "isParallel.in=" + UPDATED_IS_PARALLEL
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByIsParallelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where isParallel is not null
        defaultWorkflowFiltering("isParallel.specified=true", "isParallel.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByAutoStartIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where autoStart equals to
        defaultWorkflowFiltering("autoStart.equals=" + DEFAULT_AUTO_START, "autoStart.equals=" + UPDATED_AUTO_START);
    }

    @Test
    @Transactional
    void getAllWorkflowsByAutoStartIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where autoStart in
        defaultWorkflowFiltering("autoStart.in=" + DEFAULT_AUTO_START + "," + UPDATED_AUTO_START, "autoStart.in=" + UPDATED_AUTO_START);
    }

    @Test
    @Transactional
    void getAllWorkflowsByAutoStartIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where autoStart is not null
        defaultWorkflowFiltering("autoStart.specified=true", "autoStart.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByTriggerEventIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where triggerEvent equals to
        defaultWorkflowFiltering("triggerEvent.equals=" + DEFAULT_TRIGGER_EVENT, "triggerEvent.equals=" + UPDATED_TRIGGER_EVENT);
    }

    @Test
    @Transactional
    void getAllWorkflowsByTriggerEventIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where triggerEvent in
        defaultWorkflowFiltering(
            "triggerEvent.in=" + DEFAULT_TRIGGER_EVENT + "," + UPDATED_TRIGGER_EVENT,
            "triggerEvent.in=" + UPDATED_TRIGGER_EVENT
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByTriggerEventIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where triggerEvent is not null
        defaultWorkflowFiltering("triggerEvent.specified=true", "triggerEvent.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByTriggerEventContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where triggerEvent contains
        defaultWorkflowFiltering("triggerEvent.contains=" + DEFAULT_TRIGGER_EVENT, "triggerEvent.contains=" + UPDATED_TRIGGER_EVENT);
    }

    @Test
    @Transactional
    void getAllWorkflowsByTriggerEventNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where triggerEvent does not contain
        defaultWorkflowFiltering(
            "triggerEvent.doesNotContain=" + UPDATED_TRIGGER_EVENT,
            "triggerEvent.doesNotContain=" + DEFAULT_TRIGGER_EVENT
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdDate equals to
        defaultWorkflowFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdDate in
        defaultWorkflowFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdDate is not null
        defaultWorkflowFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where lastModifiedDate equals to
        defaultWorkflowFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where lastModifiedDate in
        defaultWorkflowFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWorkflowsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where lastModifiedDate is not null
        defaultWorkflowFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdBy equals to
        defaultWorkflowFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdBy in
        defaultWorkflowFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdBy is not null
        defaultWorkflowFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdBy contains
        defaultWorkflowFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWorkflowsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        // Get all the workflowList where createdBy does not contain
        defaultWorkflowFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    private void defaultWorkflowFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWorkflowShouldBeFound(shouldBeFound);
        defaultWorkflowShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWorkflowShouldBeFound(String filter) throws Exception {
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflow.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isParallel").value(hasItem(DEFAULT_IS_PARALLEL)))
            .andExpect(jsonPath("$.[*].autoStart").value(hasItem(DEFAULT_AUTO_START)))
            .andExpect(jsonPath("$.[*].triggerEvent").value(hasItem(DEFAULT_TRIGGER_EVENT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWorkflowShouldNotBeFound(String filter) throws Exception {
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWorkflowMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWorkflow() throws Exception {
        // Get the workflow
        restWorkflowMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflow() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflow
        Workflow updatedWorkflow = workflowRepository.findById(workflow.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflow are not directly saved in db
        em.detach(updatedWorkflow);
        updatedWorkflow
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE)
            .isParallel(UPDATED_IS_PARALLEL)
            .autoStart(UPDATED_AUTO_START)
            .triggerEvent(UPDATED_TRIGGER_EVENT)
            .configuration(UPDATED_CONFIGURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        WorkflowDTO workflowDTO = workflowMapper.toDto(updatedWorkflow);

        restWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowDTO))
            )
            .andExpect(status().isOk());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowToMatchAllProperties(updatedWorkflow);
    }

    @Test
    @Transactional
    void putNonExistingWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflow using partial update
        Workflow partialUpdatedWorkflow = new Workflow();
        partialUpdatedWorkflow.setId(workflow.getId());

        partialUpdatedWorkflow.version(UPDATED_VERSION).createdDate(UPDATED_CREATED_DATE).lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflow))
            )
            .andExpect(status().isOk());

        // Validate the Workflow in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedWorkflow, workflow), getPersistedWorkflow(workflow));
    }

    @Test
    @Transactional
    void fullUpdateWorkflowWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflow using partial update
        Workflow partialUpdatedWorkflow = new Workflow();
        partialUpdatedWorkflow.setId(workflow.getId());

        partialUpdatedWorkflow
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .version(UPDATED_VERSION)
            .isActive(UPDATED_IS_ACTIVE)
            .isParallel(UPDATED_IS_PARALLEL)
            .autoStart(UPDATED_AUTO_START)
            .triggerEvent(UPDATED_TRIGGER_EVENT)
            .configuration(UPDATED_CONFIGURATION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflow.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflow))
            )
            .andExpect(status().isOk());

        // Validate the Workflow in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowUpdatableFieldsEquals(partialUpdatedWorkflow, getPersistedWorkflow(partialUpdatedWorkflow));
    }

    @Test
    @Transactional
    void patchNonExistingWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflow() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflow.setId(longCount.incrementAndGet());

        // Create the Workflow
        WorkflowDTO workflowDTO = workflowMapper.toDto(workflow);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Workflow in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkflow() throws Exception {
        // Initialize the database
        insertedWorkflow = workflowRepository.saveAndFlush(workflow);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workflow
        restWorkflowMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflow.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workflowRepository.count();
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

    protected Workflow getPersistedWorkflow(Workflow workflow) {
        return workflowRepository.findById(workflow.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowToMatchAllProperties(Workflow expectedWorkflow) {
        assertWorkflowAllPropertiesEquals(expectedWorkflow, getPersistedWorkflow(expectedWorkflow));
    }

    protected void assertPersistedWorkflowToMatchUpdatableProperties(Workflow expectedWorkflow) {
        assertWorkflowAllUpdatablePropertiesEquals(expectedWorkflow, getPersistedWorkflow(expectedWorkflow));
    }
}
