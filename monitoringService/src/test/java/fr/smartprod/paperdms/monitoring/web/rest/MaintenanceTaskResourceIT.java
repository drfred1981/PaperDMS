package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MaintenanceTaskAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MaintenanceTask;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MaintenanceType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.monitoring.repository.MaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MaintenanceTaskMapper;
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
 * Integration tests for the {@link MaintenanceTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MaintenanceTaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final MaintenanceType DEFAULT_TASK_TYPE = MaintenanceType.CLEANUP_DELETED;
    private static final MaintenanceType UPDATED_TASK_TYPE = MaintenanceType.REBUILD_INDEX;

    private static final String DEFAULT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBBBBBBB";

    private static final TransformStatus DEFAULT_STATUS = TransformStatus.PENDING;
    private static final TransformStatus UPDATED_STATUS = TransformStatus.PROCESSING;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_LAST_RUN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_RUN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_RUN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_RUN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_DURATION = 1L;
    private static final Long UPDATED_DURATION = 2L;
    private static final Long SMALLER_DURATION = 1L - 1L;

    private static final Integer DEFAULT_RECORDS_PROCESSED = 1;
    private static final Integer UPDATED_RECORDS_PROCESSED = 2;
    private static final Integer SMALLER_RECORDS_PROCESSED = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/maintenance-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MaintenanceTaskRepository maintenanceTaskRepository;

    @Autowired
    private MaintenanceTaskMapper maintenanceTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMaintenanceTaskMockMvc;

    private MaintenanceTask maintenanceTask;

    private MaintenanceTask insertedMaintenanceTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintenanceTask createEntity() {
        return new MaintenanceTask()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .taskType(DEFAULT_TASK_TYPE)
            .schedule(DEFAULT_SCHEDULE)
            .status(DEFAULT_STATUS)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastRun(DEFAULT_LAST_RUN)
            .nextRun(DEFAULT_NEXT_RUN)
            .duration(DEFAULT_DURATION)
            .recordsProcessed(DEFAULT_RECORDS_PROCESSED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MaintenanceTask createUpdatedEntity() {
        return new MaintenanceTask()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .taskType(UPDATED_TASK_TYPE)
            .schedule(UPDATED_SCHEDULE)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .duration(UPDATED_DURATION)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        maintenanceTask = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMaintenanceTask != null) {
            maintenanceTaskRepository.delete(insertedMaintenanceTask);
            insertedMaintenanceTask = null;
        }
    }

    @Test
    @Transactional
    void createMaintenanceTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);
        var returnedMaintenanceTaskDTO = om.readValue(
            restMaintenanceTaskMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MaintenanceTaskDTO.class
        );

        // Validate the MaintenanceTask in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMaintenanceTask = maintenanceTaskMapper.toEntity(returnedMaintenanceTaskDTO);
        assertMaintenanceTaskUpdatableFieldsEquals(returnedMaintenanceTask, getPersistedMaintenanceTask(returnedMaintenanceTask));

        insertedMaintenanceTask = returnedMaintenanceTask;
    }

    @Test
    @Transactional
    void createMaintenanceTaskWithExistingId() throws Exception {
        // Create the MaintenanceTask with an existing ID
        maintenanceTask.setId(1L);
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setName(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaskTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setTaskType(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setSchedule(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setStatus(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setIsActive(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setCreatedBy(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        maintenanceTask.setCreatedDate(null);

        // Create the MaintenanceTask, which fails.
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasks() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenanceTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN.toString())))
            .andExpect(jsonPath("$.[*].nextRun").value(hasItem(DEFAULT_NEXT_RUN.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].recordsProcessed").value(hasItem(DEFAULT_RECORDS_PROCESSED)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get the maintenanceTask
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, maintenanceTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(maintenanceTask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.taskType").value(DEFAULT_TASK_TYPE.toString()))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.lastRun").value(DEFAULT_LAST_RUN.toString()))
            .andExpect(jsonPath("$.nextRun").value(DEFAULT_NEXT_RUN.toString()))
            .andExpect(jsonPath("$.duration").value(DEFAULT_DURATION.intValue()))
            .andExpect(jsonPath("$.recordsProcessed").value(DEFAULT_RECORDS_PROCESSED))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMaintenanceTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        Long id = maintenanceTask.getId();

        defaultMaintenanceTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMaintenanceTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMaintenanceTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where name equals to
        defaultMaintenanceTaskFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where name in
        defaultMaintenanceTaskFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where name is not null
        defaultMaintenanceTaskFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where name contains
        defaultMaintenanceTaskFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where name does not contain
        defaultMaintenanceTaskFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByTaskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where taskType equals to
        defaultMaintenanceTaskFiltering("taskType.equals=" + DEFAULT_TASK_TYPE, "taskType.equals=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByTaskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where taskType in
        defaultMaintenanceTaskFiltering("taskType.in=" + DEFAULT_TASK_TYPE + "," + UPDATED_TASK_TYPE, "taskType.in=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByTaskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where taskType is not null
        defaultMaintenanceTaskFiltering("taskType.specified=true", "taskType.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where schedule equals to
        defaultMaintenanceTaskFiltering("schedule.equals=" + DEFAULT_SCHEDULE, "schedule.equals=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByScheduleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where schedule in
        defaultMaintenanceTaskFiltering("schedule.in=" + DEFAULT_SCHEDULE + "," + UPDATED_SCHEDULE, "schedule.in=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByScheduleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where schedule is not null
        defaultMaintenanceTaskFiltering("schedule.specified=true", "schedule.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByScheduleContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where schedule contains
        defaultMaintenanceTaskFiltering("schedule.contains=" + DEFAULT_SCHEDULE, "schedule.contains=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByScheduleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where schedule does not contain
        defaultMaintenanceTaskFiltering("schedule.doesNotContain=" + UPDATED_SCHEDULE, "schedule.doesNotContain=" + DEFAULT_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where status equals to
        defaultMaintenanceTaskFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where status in
        defaultMaintenanceTaskFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where status is not null
        defaultMaintenanceTaskFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where isActive equals to
        defaultMaintenanceTaskFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where isActive in
        defaultMaintenanceTaskFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where isActive is not null
        defaultMaintenanceTaskFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByLastRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where lastRun equals to
        defaultMaintenanceTaskFiltering("lastRun.equals=" + DEFAULT_LAST_RUN, "lastRun.equals=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByLastRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where lastRun in
        defaultMaintenanceTaskFiltering("lastRun.in=" + DEFAULT_LAST_RUN + "," + UPDATED_LAST_RUN, "lastRun.in=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByLastRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where lastRun is not null
        defaultMaintenanceTaskFiltering("lastRun.specified=true", "lastRun.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNextRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where nextRun equals to
        defaultMaintenanceTaskFiltering("nextRun.equals=" + DEFAULT_NEXT_RUN, "nextRun.equals=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNextRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where nextRun in
        defaultMaintenanceTaskFiltering("nextRun.in=" + DEFAULT_NEXT_RUN + "," + UPDATED_NEXT_RUN, "nextRun.in=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByNextRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where nextRun is not null
        defaultMaintenanceTaskFiltering("nextRun.specified=true", "nextRun.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration equals to
        defaultMaintenanceTaskFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration in
        defaultMaintenanceTaskFiltering("duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION, "duration.in=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration is not null
        defaultMaintenanceTaskFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration is greater than or equal to
        defaultMaintenanceTaskFiltering(
            "duration.greaterThanOrEqual=" + DEFAULT_DURATION,
            "duration.greaterThanOrEqual=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration is less than or equal to
        defaultMaintenanceTaskFiltering("duration.lessThanOrEqual=" + DEFAULT_DURATION, "duration.lessThanOrEqual=" + SMALLER_DURATION);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration is less than
        defaultMaintenanceTaskFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where duration is greater than
        defaultMaintenanceTaskFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed equals to
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.equals=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.equals=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed in
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.in=" + DEFAULT_RECORDS_PROCESSED + "," + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.in=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed is not null
        defaultMaintenanceTaskFiltering("recordsProcessed.specified=true", "recordsProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed is greater than or equal to
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.greaterThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.greaterThanOrEqual=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed is less than or equal to
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.lessThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.lessThanOrEqual=" + SMALLER_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed is less than
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.lessThan=" + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.lessThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByRecordsProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where recordsProcessed is greater than
        defaultMaintenanceTaskFiltering(
            "recordsProcessed.greaterThan=" + SMALLER_RECORDS_PROCESSED,
            "recordsProcessed.greaterThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdBy equals to
        defaultMaintenanceTaskFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdBy in
        defaultMaintenanceTaskFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdBy is not null
        defaultMaintenanceTaskFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdBy contains
        defaultMaintenanceTaskFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdBy does not contain
        defaultMaintenanceTaskFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdDate equals to
        defaultMaintenanceTaskFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdDate in
        defaultMaintenanceTaskFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMaintenanceTasksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        // Get all the maintenanceTaskList where createdDate is not null
        defaultMaintenanceTaskFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMaintenanceTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMaintenanceTaskShouldBeFound(shouldBeFound);
        defaultMaintenanceTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMaintenanceTaskShouldBeFound(String filter) throws Exception {
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(maintenanceTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].taskType").value(hasItem(DEFAULT_TASK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN.toString())))
            .andExpect(jsonPath("$.[*].nextRun").value(hasItem(DEFAULT_NEXT_RUN.toString())))
            .andExpect(jsonPath("$.[*].duration").value(hasItem(DEFAULT_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].recordsProcessed").value(hasItem(DEFAULT_RECORDS_PROCESSED)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMaintenanceTaskShouldNotBeFound(String filter) throws Exception {
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMaintenanceTask() throws Exception {
        // Get the maintenanceTask
        restMaintenanceTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maintenanceTask
        MaintenanceTask updatedMaintenanceTask = maintenanceTaskRepository.findById(maintenanceTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMaintenanceTask are not directly saved in db
        em.detach(updatedMaintenanceTask);
        updatedMaintenanceTask
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .taskType(UPDATED_TASK_TYPE)
            .schedule(UPDATED_SCHEDULE)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .duration(UPDATED_DURATION)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(updatedMaintenanceTask);

        restMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMaintenanceTaskToMatchAllProperties(updatedMaintenanceTask);
    }

    @Test
    @Transactional
    void putNonExistingMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, maintenanceTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(maintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMaintenanceTaskWithPatch() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maintenanceTask using partial update
        MaintenanceTask partialUpdatedMaintenanceTask = new MaintenanceTask();
        partialUpdatedMaintenanceTask.setId(maintenanceTask.getId());

        partialUpdatedMaintenanceTask.name(UPDATED_NAME).schedule(UPDATED_SCHEDULE).status(UPDATED_STATUS).isActive(UPDATED_IS_ACTIVE);

        restMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenanceTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaintenanceTask))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaintenanceTaskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMaintenanceTask, maintenanceTask),
            getPersistedMaintenanceTask(maintenanceTask)
        );
    }

    @Test
    @Transactional
    void fullUpdateMaintenanceTaskWithPatch() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the maintenanceTask using partial update
        MaintenanceTask partialUpdatedMaintenanceTask = new MaintenanceTask();
        partialUpdatedMaintenanceTask.setId(maintenanceTask.getId());

        partialUpdatedMaintenanceTask
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .taskType(UPDATED_TASK_TYPE)
            .schedule(UPDATED_SCHEDULE)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .duration(UPDATED_DURATION)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMaintenanceTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMaintenanceTask))
            )
            .andExpect(status().isOk());

        // Validate the MaintenanceTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMaintenanceTaskUpdatableFieldsEquals(
            partialUpdatedMaintenanceTask,
            getPersistedMaintenanceTask(partialUpdatedMaintenanceTask)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, maintenanceTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(maintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        maintenanceTask.setId(longCount.incrementAndGet());

        // Create the MaintenanceTask
        MaintenanceTaskDTO maintenanceTaskDTO = maintenanceTaskMapper.toDto(maintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMaintenanceTaskMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(maintenanceTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMaintenanceTask = maintenanceTaskRepository.saveAndFlush(maintenanceTask);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the maintenanceTask
        restMaintenanceTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, maintenanceTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return maintenanceTaskRepository.count();
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

    protected MaintenanceTask getPersistedMaintenanceTask(MaintenanceTask maintenanceTask) {
        return maintenanceTaskRepository.findById(maintenanceTask.getId()).orElseThrow();
    }

    protected void assertPersistedMaintenanceTaskToMatchAllProperties(MaintenanceTask expectedMaintenanceTask) {
        assertMaintenanceTaskAllPropertiesEquals(expectedMaintenanceTask, getPersistedMaintenanceTask(expectedMaintenanceTask));
    }

    protected void assertPersistedMaintenanceTaskToMatchUpdatableProperties(MaintenanceTask expectedMaintenanceTask) {
        assertMaintenanceTaskAllUpdatablePropertiesEquals(expectedMaintenanceTask, getPersistedMaintenanceTask(expectedMaintenanceTask));
    }
}
