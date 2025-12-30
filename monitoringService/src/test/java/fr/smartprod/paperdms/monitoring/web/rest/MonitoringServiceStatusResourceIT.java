package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatusAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringServiceStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringServiceStatusType;
import fr.smartprod.paperdms.monitoring.repository.MonitoringServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringServiceStatusMapper;
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
 * Integration tests for the {@link MonitoringServiceStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringServiceStatusResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final MonitoringServiceStatusType DEFAULT_STATUS = MonitoringServiceStatusType.RUNNING;
    private static final MonitoringServiceStatusType UPDATED_STATUS = MonitoringServiceStatusType.STARTING;

    private static final String DEFAULT_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;
    private static final Integer SMALLER_PORT = 1 - 1;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_PING = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_PING = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_HEALTHY = false;
    private static final Boolean UPDATED_IS_HEALTHY = true;

    private static final String ENTITY_API_URL = "/api/monitoring-service-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringServiceStatusRepository monitoringServiceStatusRepository;

    @Autowired
    private MonitoringServiceStatusMapper monitoringServiceStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringServiceStatusMockMvc;

    private MonitoringServiceStatus monitoringServiceStatus;

    private MonitoringServiceStatus insertedMonitoringServiceStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringServiceStatus createEntity() {
        return new MonitoringServiceStatus()
            .serviceName(DEFAULT_SERVICE_NAME)
            .serviceType(DEFAULT_SERVICE_TYPE)
            .status(DEFAULT_STATUS)
            .endpoint(DEFAULT_ENDPOINT)
            .port(DEFAULT_PORT)
            .version(DEFAULT_VERSION)
            .lastPing(DEFAULT_LAST_PING)
            .isHealthy(DEFAULT_IS_HEALTHY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringServiceStatus createUpdatedEntity() {
        return new MonitoringServiceStatus()
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);
    }

    @BeforeEach
    void initTest() {
        monitoringServiceStatus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringServiceStatus != null) {
            monitoringServiceStatusRepository.delete(insertedMonitoringServiceStatus);
            insertedMonitoringServiceStatus = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);
        var returnedMonitoringServiceStatusDTO = om.readValue(
            restMonitoringServiceStatusMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringServiceStatusDTO.class
        );

        // Validate the MonitoringServiceStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringServiceStatus = monitoringServiceStatusMapper.toEntity(returnedMonitoringServiceStatusDTO);
        assertMonitoringServiceStatusUpdatableFieldsEquals(
            returnedMonitoringServiceStatus,
            getPersistedMonitoringServiceStatus(returnedMonitoringServiceStatus)
        );

        insertedMonitoringServiceStatus = returnedMonitoringServiceStatus;
    }

    @Test
    @Transactional
    void createMonitoringServiceStatusWithExistingId() throws Exception {
        // Create the MonitoringServiceStatus with an existing ID
        monitoringServiceStatus.setId(1L);
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringServiceStatus.setServiceName(null);

        // Create the MonitoringServiceStatus, which fails.
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        restMonitoringServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringServiceStatus.setStatus(null);

        // Create the MonitoringServiceStatus, which fails.
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        restMonitoringServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsHealthyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringServiceStatus.setIsHealthy(null);

        // Create the MonitoringServiceStatus, which fails.
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        restMonitoringServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatuses() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringServiceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].lastPing").value(hasItem(DEFAULT_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].isHealthy").value(hasItem(DEFAULT_IS_HEALTHY)));
    }

    @Test
    @Transactional
    void getMonitoringServiceStatus() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get the monitoringServiceStatus
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringServiceStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringServiceStatus.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.lastPing").value(DEFAULT_LAST_PING.toString()))
            .andExpect(jsonPath("$.isHealthy").value(DEFAULT_IS_HEALTHY));
    }

    @Test
    @Transactional
    void getMonitoringServiceStatusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        Long id = monitoringServiceStatus.getId();

        defaultMonitoringServiceStatusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringServiceStatusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringServiceStatusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceName equals to
        defaultMonitoringServiceStatusFiltering("serviceName.equals=" + DEFAULT_SERVICE_NAME, "serviceName.equals=" + UPDATED_SERVICE_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceName in
        defaultMonitoringServiceStatusFiltering(
            "serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME,
            "serviceName.in=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceName is not null
        defaultMonitoringServiceStatusFiltering("serviceName.specified=true", "serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceName contains
        defaultMonitoringServiceStatusFiltering(
            "serviceName.contains=" + DEFAULT_SERVICE_NAME,
            "serviceName.contains=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceName does not contain
        defaultMonitoringServiceStatusFiltering(
            "serviceName.doesNotContain=" + UPDATED_SERVICE_NAME,
            "serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceType equals to
        defaultMonitoringServiceStatusFiltering("serviceType.equals=" + DEFAULT_SERVICE_TYPE, "serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceType in
        defaultMonitoringServiceStatusFiltering(
            "serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE,
            "serviceType.in=" + UPDATED_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceType is not null
        defaultMonitoringServiceStatusFiltering("serviceType.specified=true", "serviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceType contains
        defaultMonitoringServiceStatusFiltering(
            "serviceType.contains=" + DEFAULT_SERVICE_TYPE,
            "serviceType.contains=" + UPDATED_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByServiceTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where serviceType does not contain
        defaultMonitoringServiceStatusFiltering(
            "serviceType.doesNotContain=" + UPDATED_SERVICE_TYPE,
            "serviceType.doesNotContain=" + DEFAULT_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where status equals to
        defaultMonitoringServiceStatusFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where status in
        defaultMonitoringServiceStatusFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where status is not null
        defaultMonitoringServiceStatusFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByEndpointIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where endpoint equals to
        defaultMonitoringServiceStatusFiltering("endpoint.equals=" + DEFAULT_ENDPOINT, "endpoint.equals=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByEndpointIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where endpoint in
        defaultMonitoringServiceStatusFiltering(
            "endpoint.in=" + DEFAULT_ENDPOINT + "," + UPDATED_ENDPOINT,
            "endpoint.in=" + UPDATED_ENDPOINT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByEndpointIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where endpoint is not null
        defaultMonitoringServiceStatusFiltering("endpoint.specified=true", "endpoint.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByEndpointContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where endpoint contains
        defaultMonitoringServiceStatusFiltering("endpoint.contains=" + DEFAULT_ENDPOINT, "endpoint.contains=" + UPDATED_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByEndpointNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where endpoint does not contain
        defaultMonitoringServiceStatusFiltering(
            "endpoint.doesNotContain=" + UPDATED_ENDPOINT,
            "endpoint.doesNotContain=" + DEFAULT_ENDPOINT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port equals to
        defaultMonitoringServiceStatusFiltering("port.equals=" + DEFAULT_PORT, "port.equals=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port in
        defaultMonitoringServiceStatusFiltering("port.in=" + DEFAULT_PORT + "," + UPDATED_PORT, "port.in=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port is not null
        defaultMonitoringServiceStatusFiltering("port.specified=true", "port.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port is greater than or equal to
        defaultMonitoringServiceStatusFiltering("port.greaterThanOrEqual=" + DEFAULT_PORT, "port.greaterThanOrEqual=" + UPDATED_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port is less than or equal to
        defaultMonitoringServiceStatusFiltering("port.lessThanOrEqual=" + DEFAULT_PORT, "port.lessThanOrEqual=" + SMALLER_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port is less than
        defaultMonitoringServiceStatusFiltering("port.lessThan=" + UPDATED_PORT, "port.lessThan=" + DEFAULT_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByPortIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where port is greater than
        defaultMonitoringServiceStatusFiltering("port.greaterThan=" + SMALLER_PORT, "port.greaterThan=" + DEFAULT_PORT);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where version equals to
        defaultMonitoringServiceStatusFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where version in
        defaultMonitoringServiceStatusFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where version is not null
        defaultMonitoringServiceStatusFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where version contains
        defaultMonitoringServiceStatusFiltering("version.contains=" + DEFAULT_VERSION, "version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where version does not contain
        defaultMonitoringServiceStatusFiltering("version.doesNotContain=" + UPDATED_VERSION, "version.doesNotContain=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByLastPingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where lastPing equals to
        defaultMonitoringServiceStatusFiltering("lastPing.equals=" + DEFAULT_LAST_PING, "lastPing.equals=" + UPDATED_LAST_PING);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByLastPingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where lastPing in
        defaultMonitoringServiceStatusFiltering(
            "lastPing.in=" + DEFAULT_LAST_PING + "," + UPDATED_LAST_PING,
            "lastPing.in=" + UPDATED_LAST_PING
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByLastPingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where lastPing is not null
        defaultMonitoringServiceStatusFiltering("lastPing.specified=true", "lastPing.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByIsHealthyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where isHealthy equals to
        defaultMonitoringServiceStatusFiltering("isHealthy.equals=" + DEFAULT_IS_HEALTHY, "isHealthy.equals=" + UPDATED_IS_HEALTHY);
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByIsHealthyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where isHealthy in
        defaultMonitoringServiceStatusFiltering(
            "isHealthy.in=" + DEFAULT_IS_HEALTHY + "," + UPDATED_IS_HEALTHY,
            "isHealthy.in=" + UPDATED_IS_HEALTHY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringServiceStatusesByIsHealthyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        // Get all the monitoringServiceStatusList where isHealthy is not null
        defaultMonitoringServiceStatusFiltering("isHealthy.specified=true", "isHealthy.specified=false");
    }

    private void defaultMonitoringServiceStatusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringServiceStatusShouldBeFound(shouldBeFound);
        defaultMonitoringServiceStatusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringServiceStatusShouldBeFound(String filter) throws Exception {
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringServiceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].lastPing").value(hasItem(DEFAULT_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].isHealthy").value(hasItem(DEFAULT_IS_HEALTHY)));

        // Check, that the count call also returns 1
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringServiceStatusShouldNotBeFound(String filter) throws Exception {
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringServiceStatus() throws Exception {
        // Get the monitoringServiceStatus
        restMonitoringServiceStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringServiceStatus() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringServiceStatus
        MonitoringServiceStatus updatedMonitoringServiceStatus = monitoringServiceStatusRepository
            .findById(monitoringServiceStatus.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringServiceStatus are not directly saved in db
        em.detach(updatedMonitoringServiceStatus);
        updatedMonitoringServiceStatus
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(updatedMonitoringServiceStatus);

        restMonitoringServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringServiceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringServiceStatusToMatchAllProperties(updatedMonitoringServiceStatus);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringServiceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringServiceStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringServiceStatus using partial update
        MonitoringServiceStatus partialUpdatedMonitoringServiceStatus = new MonitoringServiceStatus();
        partialUpdatedMonitoringServiceStatus.setId(monitoringServiceStatus.getId());

        partialUpdatedMonitoringServiceStatus
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .isHealthy(UPDATED_IS_HEALTHY);

        restMonitoringServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringServiceStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringServiceStatus, monitoringServiceStatus),
            getPersistedMonitoringServiceStatus(monitoringServiceStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringServiceStatus using partial update
        MonitoringServiceStatus partialUpdatedMonitoringServiceStatus = new MonitoringServiceStatus();
        partialUpdatedMonitoringServiceStatus.setId(monitoringServiceStatus.getId());

        partialUpdatedMonitoringServiceStatus
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);

        restMonitoringServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringServiceStatusUpdatableFieldsEquals(
            partialUpdatedMonitoringServiceStatus,
            getPersistedMonitoringServiceStatus(partialUpdatedMonitoringServiceStatus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringServiceStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringServiceStatus.setId(longCount.incrementAndGet());

        // Create the MonitoringServiceStatus
        MonitoringServiceStatusDTO monitoringServiceStatusDTO = monitoringServiceStatusMapper.toDto(monitoringServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitoringServiceStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringServiceStatus() throws Exception {
        // Initialize the database
        insertedMonitoringServiceStatus = monitoringServiceStatusRepository.saveAndFlush(monitoringServiceStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringServiceStatus
        restMonitoringServiceStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringServiceStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringServiceStatusRepository.count();
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

    protected MonitoringServiceStatus getPersistedMonitoringServiceStatus(MonitoringServiceStatus monitoringServiceStatus) {
        return monitoringServiceStatusRepository.findById(monitoringServiceStatus.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringServiceStatusToMatchAllProperties(MonitoringServiceStatus expectedMonitoringServiceStatus) {
        assertMonitoringServiceStatusAllPropertiesEquals(
            expectedMonitoringServiceStatus,
            getPersistedMonitoringServiceStatus(expectedMonitoringServiceStatus)
        );
    }

    protected void assertPersistedMonitoringServiceStatusToMatchUpdatableProperties(
        MonitoringServiceStatus expectedMonitoringServiceStatus
    ) {
        assertMonitoringServiceStatusAllUpdatablePropertiesEquals(
            expectedMonitoringServiceStatus,
            getPersistedMonitoringServiceStatus(expectedMonitoringServiceStatus)
        );
    }
}
