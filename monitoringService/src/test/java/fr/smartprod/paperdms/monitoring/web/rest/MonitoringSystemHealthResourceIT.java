package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealthAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringSystemHealth;
import fr.smartprod.paperdms.monitoring.domain.enumeration.HealthStatus;
import fr.smartprod.paperdms.monitoring.repository.MonitoringSystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringSystemHealthDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringSystemHealthMapper;
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
 * Integration tests for the {@link MonitoringSystemHealthResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringSystemHealthResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final HealthStatus DEFAULT_STATUS = HealthStatus.HEALTHY;
    private static final HealthStatus UPDATED_STATUS = HealthStatus.DEGRADED;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Long DEFAULT_UPTIME = 1L;
    private static final Long UPDATED_UPTIME = 2L;
    private static final Long SMALLER_UPTIME = 1L - 1L;

    private static final Double DEFAULT_CPU_USAGE = 1D;
    private static final Double UPDATED_CPU_USAGE = 2D;
    private static final Double SMALLER_CPU_USAGE = 1D - 1D;

    private static final Double DEFAULT_MEMORY_USAGE = 1D;
    private static final Double UPDATED_MEMORY_USAGE = 2D;
    private static final Double SMALLER_MEMORY_USAGE = 1D - 1D;

    private static final Double DEFAULT_ERROR_RATE = 1D;
    private static final Double UPDATED_ERROR_RATE = 2D;
    private static final Double SMALLER_ERROR_RATE = 1D - 1D;

    private static final Instant DEFAULT_LAST_CHECK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CHECK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/monitoring-system-healths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringSystemHealthRepository monitoringSystemHealthRepository;

    @Autowired
    private MonitoringSystemHealthMapper monitoringSystemHealthMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringSystemHealthMockMvc;

    private MonitoringSystemHealth monitoringSystemHealth;

    private MonitoringSystemHealth insertedMonitoringSystemHealth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringSystemHealth createEntity() {
        return new MonitoringSystemHealth()
            .serviceName(DEFAULT_SERVICE_NAME)
            .status(DEFAULT_STATUS)
            .version(DEFAULT_VERSION)
            .uptime(DEFAULT_UPTIME)
            .cpuUsage(DEFAULT_CPU_USAGE)
            .memoryUsage(DEFAULT_MEMORY_USAGE)
            .errorRate(DEFAULT_ERROR_RATE)
            .lastCheck(DEFAULT_LAST_CHECK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringSystemHealth createUpdatedEntity() {
        return new MonitoringSystemHealth()
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);
    }

    @BeforeEach
    void initTest() {
        monitoringSystemHealth = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringSystemHealth != null) {
            monitoringSystemHealthRepository.delete(insertedMonitoringSystemHealth);
            insertedMonitoringSystemHealth = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);
        var returnedMonitoringSystemHealthDTO = om.readValue(
            restMonitoringSystemHealthMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringSystemHealthDTO.class
        );

        // Validate the MonitoringSystemHealth in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringSystemHealth = monitoringSystemHealthMapper.toEntity(returnedMonitoringSystemHealthDTO);
        assertMonitoringSystemHealthUpdatableFieldsEquals(
            returnedMonitoringSystemHealth,
            getPersistedMonitoringSystemHealth(returnedMonitoringSystemHealth)
        );

        insertedMonitoringSystemHealth = returnedMonitoringSystemHealth;
    }

    @Test
    @Transactional
    void createMonitoringSystemHealthWithExistingId() throws Exception {
        // Create the MonitoringSystemHealth with an existing ID
        monitoringSystemHealth.setId(1L);
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringSystemHealth.setServiceName(null);

        // Create the MonitoringSystemHealth, which fails.
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        restMonitoringSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringSystemHealth.setStatus(null);

        // Create the MonitoringSystemHealth, which fails.
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        restMonitoringSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastCheckIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringSystemHealth.setLastCheck(null);

        // Create the MonitoringSystemHealth, which fails.
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        restMonitoringSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealths() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringSystemHealth.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].uptime").value(hasItem(DEFAULT_UPTIME.intValue())))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].errorRate").value(hasItem(DEFAULT_ERROR_RATE)))
            .andExpect(jsonPath("$.[*].lastCheck").value(hasItem(DEFAULT_LAST_CHECK.toString())));
    }

    @Test
    @Transactional
    void getMonitoringSystemHealth() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get the monitoringSystemHealth
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringSystemHealth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringSystemHealth.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.uptime").value(DEFAULT_UPTIME.intValue()))
            .andExpect(jsonPath("$.cpuUsage").value(DEFAULT_CPU_USAGE))
            .andExpect(jsonPath("$.memoryUsage").value(DEFAULT_MEMORY_USAGE))
            .andExpect(jsonPath("$.errorRate").value(DEFAULT_ERROR_RATE))
            .andExpect(jsonPath("$.lastCheck").value(DEFAULT_LAST_CHECK.toString()));
    }

    @Test
    @Transactional
    void getMonitoringSystemHealthsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        Long id = monitoringSystemHealth.getId();

        defaultMonitoringSystemHealthFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringSystemHealthFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringSystemHealthFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where serviceName equals to
        defaultMonitoringSystemHealthFiltering("serviceName.equals=" + DEFAULT_SERVICE_NAME, "serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where serviceName in
        defaultMonitoringSystemHealthFiltering(
            "serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME,
            "serviceName.in=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where serviceName is not null
        defaultMonitoringSystemHealthFiltering("serviceName.specified=true", "serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where serviceName contains
        defaultMonitoringSystemHealthFiltering(
            "serviceName.contains=" + DEFAULT_SERVICE_NAME,
            "serviceName.contains=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where serviceName does not contain
        defaultMonitoringSystemHealthFiltering(
            "serviceName.doesNotContain=" + UPDATED_SERVICE_NAME,
            "serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where status equals to
        defaultMonitoringSystemHealthFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where status in
        defaultMonitoringSystemHealthFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where status is not null
        defaultMonitoringSystemHealthFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where version equals to
        defaultMonitoringSystemHealthFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where version in
        defaultMonitoringSystemHealthFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where version is not null
        defaultMonitoringSystemHealthFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where version contains
        defaultMonitoringSystemHealthFiltering("version.contains=" + DEFAULT_VERSION, "version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where version does not contain
        defaultMonitoringSystemHealthFiltering("version.doesNotContain=" + UPDATED_VERSION, "version.doesNotContain=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime equals to
        defaultMonitoringSystemHealthFiltering("uptime.equals=" + DEFAULT_UPTIME, "uptime.equals=" + UPDATED_UPTIME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime in
        defaultMonitoringSystemHealthFiltering("uptime.in=" + DEFAULT_UPTIME + "," + UPDATED_UPTIME, "uptime.in=" + UPDATED_UPTIME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime is not null
        defaultMonitoringSystemHealthFiltering("uptime.specified=true", "uptime.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime is greater than or equal to
        defaultMonitoringSystemHealthFiltering(
            "uptime.greaterThanOrEqual=" + DEFAULT_UPTIME,
            "uptime.greaterThanOrEqual=" + UPDATED_UPTIME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime is less than or equal to
        defaultMonitoringSystemHealthFiltering("uptime.lessThanOrEqual=" + DEFAULT_UPTIME, "uptime.lessThanOrEqual=" + SMALLER_UPTIME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime is less than
        defaultMonitoringSystemHealthFiltering("uptime.lessThan=" + UPDATED_UPTIME, "uptime.lessThan=" + DEFAULT_UPTIME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByUptimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where uptime is greater than
        defaultMonitoringSystemHealthFiltering("uptime.greaterThan=" + SMALLER_UPTIME, "uptime.greaterThan=" + DEFAULT_UPTIME);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage equals to
        defaultMonitoringSystemHealthFiltering("cpuUsage.equals=" + DEFAULT_CPU_USAGE, "cpuUsage.equals=" + UPDATED_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage in
        defaultMonitoringSystemHealthFiltering(
            "cpuUsage.in=" + DEFAULT_CPU_USAGE + "," + UPDATED_CPU_USAGE,
            "cpuUsage.in=" + UPDATED_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage is not null
        defaultMonitoringSystemHealthFiltering("cpuUsage.specified=true", "cpuUsage.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage is greater than or equal to
        defaultMonitoringSystemHealthFiltering(
            "cpuUsage.greaterThanOrEqual=" + DEFAULT_CPU_USAGE,
            "cpuUsage.greaterThanOrEqual=" + UPDATED_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage is less than or equal to
        defaultMonitoringSystemHealthFiltering(
            "cpuUsage.lessThanOrEqual=" + DEFAULT_CPU_USAGE,
            "cpuUsage.lessThanOrEqual=" + SMALLER_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage is less than
        defaultMonitoringSystemHealthFiltering("cpuUsage.lessThan=" + UPDATED_CPU_USAGE, "cpuUsage.lessThan=" + DEFAULT_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByCpuUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where cpuUsage is greater than
        defaultMonitoringSystemHealthFiltering("cpuUsage.greaterThan=" + SMALLER_CPU_USAGE, "cpuUsage.greaterThan=" + DEFAULT_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage equals to
        defaultMonitoringSystemHealthFiltering("memoryUsage.equals=" + DEFAULT_MEMORY_USAGE, "memoryUsage.equals=" + UPDATED_MEMORY_USAGE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage in
        defaultMonitoringSystemHealthFiltering(
            "memoryUsage.in=" + DEFAULT_MEMORY_USAGE + "," + UPDATED_MEMORY_USAGE,
            "memoryUsage.in=" + UPDATED_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage is not null
        defaultMonitoringSystemHealthFiltering("memoryUsage.specified=true", "memoryUsage.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage is greater than or equal to
        defaultMonitoringSystemHealthFiltering(
            "memoryUsage.greaterThanOrEqual=" + DEFAULT_MEMORY_USAGE,
            "memoryUsage.greaterThanOrEqual=" + UPDATED_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage is less than or equal to
        defaultMonitoringSystemHealthFiltering(
            "memoryUsage.lessThanOrEqual=" + DEFAULT_MEMORY_USAGE,
            "memoryUsage.lessThanOrEqual=" + SMALLER_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage is less than
        defaultMonitoringSystemHealthFiltering(
            "memoryUsage.lessThan=" + UPDATED_MEMORY_USAGE,
            "memoryUsage.lessThan=" + DEFAULT_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByMemoryUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where memoryUsage is greater than
        defaultMonitoringSystemHealthFiltering(
            "memoryUsage.greaterThan=" + SMALLER_MEMORY_USAGE,
            "memoryUsage.greaterThan=" + DEFAULT_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate equals to
        defaultMonitoringSystemHealthFiltering("errorRate.equals=" + DEFAULT_ERROR_RATE, "errorRate.equals=" + UPDATED_ERROR_RATE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate in
        defaultMonitoringSystemHealthFiltering(
            "errorRate.in=" + DEFAULT_ERROR_RATE + "," + UPDATED_ERROR_RATE,
            "errorRate.in=" + UPDATED_ERROR_RATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate is not null
        defaultMonitoringSystemHealthFiltering("errorRate.specified=true", "errorRate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate is greater than or equal to
        defaultMonitoringSystemHealthFiltering(
            "errorRate.greaterThanOrEqual=" + DEFAULT_ERROR_RATE,
            "errorRate.greaterThanOrEqual=" + UPDATED_ERROR_RATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate is less than or equal to
        defaultMonitoringSystemHealthFiltering(
            "errorRate.lessThanOrEqual=" + DEFAULT_ERROR_RATE,
            "errorRate.lessThanOrEqual=" + SMALLER_ERROR_RATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate is less than
        defaultMonitoringSystemHealthFiltering("errorRate.lessThan=" + UPDATED_ERROR_RATE, "errorRate.lessThan=" + DEFAULT_ERROR_RATE);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByErrorRateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where errorRate is greater than
        defaultMonitoringSystemHealthFiltering(
            "errorRate.greaterThan=" + SMALLER_ERROR_RATE,
            "errorRate.greaterThan=" + DEFAULT_ERROR_RATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByLastCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where lastCheck equals to
        defaultMonitoringSystemHealthFiltering("lastCheck.equals=" + DEFAULT_LAST_CHECK, "lastCheck.equals=" + UPDATED_LAST_CHECK);
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByLastCheckIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where lastCheck in
        defaultMonitoringSystemHealthFiltering(
            "lastCheck.in=" + DEFAULT_LAST_CHECK + "," + UPDATED_LAST_CHECK,
            "lastCheck.in=" + UPDATED_LAST_CHECK
        );
    }

    @Test
    @Transactional
    void getAllMonitoringSystemHealthsByLastCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        // Get all the monitoringSystemHealthList where lastCheck is not null
        defaultMonitoringSystemHealthFiltering("lastCheck.specified=true", "lastCheck.specified=false");
    }

    private void defaultMonitoringSystemHealthFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringSystemHealthShouldBeFound(shouldBeFound);
        defaultMonitoringSystemHealthShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringSystemHealthShouldBeFound(String filter) throws Exception {
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringSystemHealth.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].uptime").value(hasItem(DEFAULT_UPTIME.intValue())))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].errorRate").value(hasItem(DEFAULT_ERROR_RATE)))
            .andExpect(jsonPath("$.[*].lastCheck").value(hasItem(DEFAULT_LAST_CHECK.toString())));

        // Check, that the count call also returns 1
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringSystemHealthShouldNotBeFound(String filter) throws Exception {
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringSystemHealth() throws Exception {
        // Get the monitoringSystemHealth
        restMonitoringSystemHealthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringSystemHealth() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringSystemHealth
        MonitoringSystemHealth updatedMonitoringSystemHealth = monitoringSystemHealthRepository
            .findById(monitoringSystemHealth.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringSystemHealth are not directly saved in db
        em.detach(updatedMonitoringSystemHealth);
        updatedMonitoringSystemHealth
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(updatedMonitoringSystemHealth);

        restMonitoringSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringSystemHealthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringSystemHealthToMatchAllProperties(updatedMonitoringSystemHealth);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringSystemHealthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringSystemHealthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringSystemHealthWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringSystemHealth using partial update
        MonitoringSystemHealth partialUpdatedMonitoringSystemHealth = new MonitoringSystemHealth();
        partialUpdatedMonitoringSystemHealth.setId(monitoringSystemHealth.getId());

        partialUpdatedMonitoringSystemHealth
            .serviceName(UPDATED_SERVICE_NAME)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .lastCheck(UPDATED_LAST_CHECK);

        restMonitoringSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringSystemHealth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringSystemHealth))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringSystemHealth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringSystemHealthUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringSystemHealth, monitoringSystemHealth),
            getPersistedMonitoringSystemHealth(monitoringSystemHealth)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringSystemHealthWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringSystemHealth using partial update
        MonitoringSystemHealth partialUpdatedMonitoringSystemHealth = new MonitoringSystemHealth();
        partialUpdatedMonitoringSystemHealth.setId(monitoringSystemHealth.getId());

        partialUpdatedMonitoringSystemHealth
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);

        restMonitoringSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringSystemHealth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringSystemHealth))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringSystemHealth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringSystemHealthUpdatableFieldsEquals(
            partialUpdatedMonitoringSystemHealth,
            getPersistedMonitoringSystemHealth(partialUpdatedMonitoringSystemHealth)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringSystemHealthDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringSystemHealth.setId(longCount.incrementAndGet());

        // Create the MonitoringSystemHealth
        MonitoringSystemHealthDTO monitoringSystemHealthDTO = monitoringSystemHealthMapper.toDto(monitoringSystemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitoringSystemHealthDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringSystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringSystemHealth() throws Exception {
        // Initialize the database
        insertedMonitoringSystemHealth = monitoringSystemHealthRepository.saveAndFlush(monitoringSystemHealth);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringSystemHealth
        restMonitoringSystemHealthMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringSystemHealth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringSystemHealthRepository.count();
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

    protected MonitoringSystemHealth getPersistedMonitoringSystemHealth(MonitoringSystemHealth monitoringSystemHealth) {
        return monitoringSystemHealthRepository.findById(monitoringSystemHealth.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringSystemHealthToMatchAllProperties(MonitoringSystemHealth expectedMonitoringSystemHealth) {
        assertMonitoringSystemHealthAllPropertiesEquals(
            expectedMonitoringSystemHealth,
            getPersistedMonitoringSystemHealth(expectedMonitoringSystemHealth)
        );
    }

    protected void assertPersistedMonitoringSystemHealthToMatchUpdatableProperties(MonitoringSystemHealth expectedMonitoringSystemHealth) {
        assertMonitoringSystemHealthAllUpdatablePropertiesEquals(
            expectedMonitoringSystemHealth,
            getPersistedMonitoringSystemHealth(expectedMonitoringSystemHealth)
        );
    }
}
