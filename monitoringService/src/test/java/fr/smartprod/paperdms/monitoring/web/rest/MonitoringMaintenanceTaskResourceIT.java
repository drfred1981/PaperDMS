package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTaskAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringMaintenanceTask;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MaintenanceType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.monitoring.repository.MonitoringMaintenanceTaskRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringMaintenanceTaskDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringMaintenanceTaskMapper;
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
 * Integration tests for the {@link MonitoringMaintenanceTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringMaintenanceTaskResourceIT {

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

    private static final String ENTITY_API_URL = "/api/monitoring-maintenance-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringMaintenanceTaskRepository monitoringMaintenanceTaskRepository;

    @Autowired
    private MonitoringMaintenanceTaskMapper monitoringMaintenanceTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringMaintenanceTaskMockMvc;

    private MonitoringMaintenanceTask monitoringMaintenanceTask;

    private MonitoringMaintenanceTask insertedMonitoringMaintenanceTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringMaintenanceTask createEntity() {
        return new MonitoringMaintenanceTask()
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
    public static MonitoringMaintenanceTask createUpdatedEntity() {
        return new MonitoringMaintenanceTask()
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
        monitoringMaintenanceTask = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringMaintenanceTask != null) {
            monitoringMaintenanceTaskRepository.delete(insertedMonitoringMaintenanceTask);
            insertedMonitoringMaintenanceTask = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);
        var returnedMonitoringMaintenanceTaskDTO = om.readValue(
            restMonitoringMaintenanceTaskMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringMaintenanceTaskDTO.class
        );

        // Validate the MonitoringMaintenanceTask in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringMaintenanceTask = monitoringMaintenanceTaskMapper.toEntity(returnedMonitoringMaintenanceTaskDTO);
        assertMonitoringMaintenanceTaskUpdatableFieldsEquals(
            returnedMonitoringMaintenanceTask,
            getPersistedMonitoringMaintenanceTask(returnedMonitoringMaintenanceTask)
        );

        insertedMonitoringMaintenanceTask = returnedMonitoringMaintenanceTask;
    }

    @Test
    @Transactional
    void createMonitoringMaintenanceTaskWithExistingId() throws Exception {
        // Create the MonitoringMaintenanceTask with an existing ID
        monitoringMaintenanceTask.setId(1L);
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setName(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTaskTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setTaskType(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setSchedule(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setStatus(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setIsActive(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setCreatedBy(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringMaintenanceTask.setCreatedDate(null);

        // Create the MonitoringMaintenanceTask, which fails.
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasks() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringMaintenanceTask.getId().intValue())))
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
    void getMonitoringMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get the monitoringMaintenanceTask
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringMaintenanceTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringMaintenanceTask.getId().intValue()))
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
    void getMonitoringMaintenanceTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        Long id = monitoringMaintenanceTask.getId();

        defaultMonitoringMaintenanceTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringMaintenanceTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringMaintenanceTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where name equals to
        defaultMonitoringMaintenanceTaskFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where name in
        defaultMonitoringMaintenanceTaskFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where name is not null
        defaultMonitoringMaintenanceTaskFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where name contains
        defaultMonitoringMaintenanceTaskFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where name does not contain
        defaultMonitoringMaintenanceTaskFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByTaskTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where taskType equals to
        defaultMonitoringMaintenanceTaskFiltering("taskType.equals=" + DEFAULT_TASK_TYPE, "taskType.equals=" + UPDATED_TASK_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByTaskTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where taskType in
        defaultMonitoringMaintenanceTaskFiltering(
            "taskType.in=" + DEFAULT_TASK_TYPE + "," + UPDATED_TASK_TYPE,
            "taskType.in=" + UPDATED_TASK_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByTaskTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where taskType is not null
        defaultMonitoringMaintenanceTaskFiltering("taskType.specified=true", "taskType.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where schedule equals to
        defaultMonitoringMaintenanceTaskFiltering("schedule.equals=" + DEFAULT_SCHEDULE, "schedule.equals=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByScheduleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where schedule in
        defaultMonitoringMaintenanceTaskFiltering(
            "schedule.in=" + DEFAULT_SCHEDULE + "," + UPDATED_SCHEDULE,
            "schedule.in=" + UPDATED_SCHEDULE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByScheduleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where schedule is not null
        defaultMonitoringMaintenanceTaskFiltering("schedule.specified=true", "schedule.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByScheduleContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where schedule contains
        defaultMonitoringMaintenanceTaskFiltering("schedule.contains=" + DEFAULT_SCHEDULE, "schedule.contains=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByScheduleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where schedule does not contain
        defaultMonitoringMaintenanceTaskFiltering(
            "schedule.doesNotContain=" + UPDATED_SCHEDULE,
            "schedule.doesNotContain=" + DEFAULT_SCHEDULE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where status equals to
        defaultMonitoringMaintenanceTaskFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where status in
        defaultMonitoringMaintenanceTaskFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where status is not null
        defaultMonitoringMaintenanceTaskFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where isActive equals to
        defaultMonitoringMaintenanceTaskFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where isActive in
        defaultMonitoringMaintenanceTaskFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where isActive is not null
        defaultMonitoringMaintenanceTaskFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByLastRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where lastRun equals to
        defaultMonitoringMaintenanceTaskFiltering("lastRun.equals=" + DEFAULT_LAST_RUN, "lastRun.equals=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByLastRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where lastRun in
        defaultMonitoringMaintenanceTaskFiltering(
            "lastRun.in=" + DEFAULT_LAST_RUN + "," + UPDATED_LAST_RUN,
            "lastRun.in=" + UPDATED_LAST_RUN
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByLastRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where lastRun is not null
        defaultMonitoringMaintenanceTaskFiltering("lastRun.specified=true", "lastRun.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNextRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where nextRun equals to
        defaultMonitoringMaintenanceTaskFiltering("nextRun.equals=" + DEFAULT_NEXT_RUN, "nextRun.equals=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNextRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where nextRun in
        defaultMonitoringMaintenanceTaskFiltering(
            "nextRun.in=" + DEFAULT_NEXT_RUN + "," + UPDATED_NEXT_RUN,
            "nextRun.in=" + UPDATED_NEXT_RUN
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByNextRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where nextRun is not null
        defaultMonitoringMaintenanceTaskFiltering("nextRun.specified=true", "nextRun.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration equals to
        defaultMonitoringMaintenanceTaskFiltering("duration.equals=" + DEFAULT_DURATION, "duration.equals=" + UPDATED_DURATION);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration in
        defaultMonitoringMaintenanceTaskFiltering(
            "duration.in=" + DEFAULT_DURATION + "," + UPDATED_DURATION,
            "duration.in=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration is not null
        defaultMonitoringMaintenanceTaskFiltering("duration.specified=true", "duration.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration is greater than or equal to
        defaultMonitoringMaintenanceTaskFiltering(
            "duration.greaterThanOrEqual=" + DEFAULT_DURATION,
            "duration.greaterThanOrEqual=" + UPDATED_DURATION
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration is less than or equal to
        defaultMonitoringMaintenanceTaskFiltering(
            "duration.lessThanOrEqual=" + DEFAULT_DURATION,
            "duration.lessThanOrEqual=" + SMALLER_DURATION
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration is less than
        defaultMonitoringMaintenanceTaskFiltering("duration.lessThan=" + UPDATED_DURATION, "duration.lessThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where duration is greater than
        defaultMonitoringMaintenanceTaskFiltering("duration.greaterThan=" + SMALLER_DURATION, "duration.greaterThan=" + DEFAULT_DURATION);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed equals to
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.equals=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.equals=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed in
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.in=" + DEFAULT_RECORDS_PROCESSED + "," + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.in=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed is not null
        defaultMonitoringMaintenanceTaskFiltering("recordsProcessed.specified=true", "recordsProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed is greater than or equal to
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.greaterThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.greaterThanOrEqual=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed is less than or equal to
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.lessThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.lessThanOrEqual=" + SMALLER_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed is less than
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.lessThan=" + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.lessThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByRecordsProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where recordsProcessed is greater than
        defaultMonitoringMaintenanceTaskFiltering(
            "recordsProcessed.greaterThan=" + SMALLER_RECORDS_PROCESSED,
            "recordsProcessed.greaterThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdBy equals to
        defaultMonitoringMaintenanceTaskFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdBy in
        defaultMonitoringMaintenanceTaskFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdBy is not null
        defaultMonitoringMaintenanceTaskFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdBy contains
        defaultMonitoringMaintenanceTaskFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdBy does not contain
        defaultMonitoringMaintenanceTaskFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdDate equals to
        defaultMonitoringMaintenanceTaskFiltering(
            "createdDate.equals=" + DEFAULT_CREATED_DATE,
            "createdDate.equals=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdDate in
        defaultMonitoringMaintenanceTaskFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringMaintenanceTasksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        // Get all the monitoringMaintenanceTaskList where createdDate is not null
        defaultMonitoringMaintenanceTaskFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMonitoringMaintenanceTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringMaintenanceTaskShouldBeFound(shouldBeFound);
        defaultMonitoringMaintenanceTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringMaintenanceTaskShouldBeFound(String filter) throws Exception {
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringMaintenanceTask.getId().intValue())))
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
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringMaintenanceTaskShouldNotBeFound(String filter) throws Exception {
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringMaintenanceTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringMaintenanceTask() throws Exception {
        // Get the monitoringMaintenanceTask
        restMonitoringMaintenanceTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringMaintenanceTask
        MonitoringMaintenanceTask updatedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository
            .findById(monitoringMaintenanceTask.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringMaintenanceTask are not directly saved in db
        em.detach(updatedMonitoringMaintenanceTask);
        updatedMonitoringMaintenanceTask
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
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(updatedMonitoringMaintenanceTask);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringMaintenanceTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringMaintenanceTaskToMatchAllProperties(updatedMonitoringMaintenanceTask);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringMaintenanceTaskDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringMaintenanceTaskWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringMaintenanceTask using partial update
        MonitoringMaintenanceTask partialUpdatedMonitoringMaintenanceTask = new MonitoringMaintenanceTask();
        partialUpdatedMonitoringMaintenanceTask.setId(monitoringMaintenanceTask.getId());

        partialUpdatedMonitoringMaintenanceTask
            .description(UPDATED_DESCRIPTION)
            .schedule(UPDATED_SCHEDULE)
            .status(UPDATED_STATUS)
            .isActive(UPDATED_IS_ACTIVE)
            .duration(UPDATED_DURATION)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED);

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringMaintenanceTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringMaintenanceTask))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringMaintenanceTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringMaintenanceTaskUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringMaintenanceTask, monitoringMaintenanceTask),
            getPersistedMonitoringMaintenanceTask(monitoringMaintenanceTask)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringMaintenanceTaskWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringMaintenanceTask using partial update
        MonitoringMaintenanceTask partialUpdatedMonitoringMaintenanceTask = new MonitoringMaintenanceTask();
        partialUpdatedMonitoringMaintenanceTask.setId(monitoringMaintenanceTask.getId());

        partialUpdatedMonitoringMaintenanceTask
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

        restMonitoringMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringMaintenanceTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringMaintenanceTask))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringMaintenanceTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringMaintenanceTaskUpdatableFieldsEquals(
            partialUpdatedMonitoringMaintenanceTask,
            getPersistedMonitoringMaintenanceTask(partialUpdatedMonitoringMaintenanceTask)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringMaintenanceTaskDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringMaintenanceTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringMaintenanceTask.setId(longCount.incrementAndGet());

        // Create the MonitoringMaintenanceTask
        MonitoringMaintenanceTaskDTO monitoringMaintenanceTaskDTO = monitoringMaintenanceTaskMapper.toDto(monitoringMaintenanceTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringMaintenanceTaskMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringMaintenanceTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringMaintenanceTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringMaintenanceTask() throws Exception {
        // Initialize the database
        insertedMonitoringMaintenanceTask = monitoringMaintenanceTaskRepository.saveAndFlush(monitoringMaintenanceTask);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringMaintenanceTask
        restMonitoringMaintenanceTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringMaintenanceTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringMaintenanceTaskRepository.count();
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

    protected MonitoringMaintenanceTask getPersistedMonitoringMaintenanceTask(MonitoringMaintenanceTask monitoringMaintenanceTask) {
        return monitoringMaintenanceTaskRepository.findById(monitoringMaintenanceTask.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringMaintenanceTaskToMatchAllProperties(
        MonitoringMaintenanceTask expectedMonitoringMaintenanceTask
    ) {
        assertMonitoringMaintenanceTaskAllPropertiesEquals(
            expectedMonitoringMaintenanceTask,
            getPersistedMonitoringMaintenanceTask(expectedMonitoringMaintenanceTask)
        );
    }

    protected void assertPersistedMonitoringMaintenanceTaskToMatchUpdatableProperties(
        MonitoringMaintenanceTask expectedMonitoringMaintenanceTask
    ) {
        assertMonitoringMaintenanceTaskAllUpdatablePropertiesEquals(
            expectedMonitoringMaintenanceTask,
            getPersistedMonitoringMaintenanceTask(expectedMonitoringMaintenanceTask)
        );
    }
}
