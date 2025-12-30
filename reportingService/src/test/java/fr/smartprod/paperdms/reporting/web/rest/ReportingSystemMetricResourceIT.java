package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingSystemMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingSystemMetric;
import fr.smartprod.paperdms.reporting.repository.ReportingSystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingSystemMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingSystemMetricMapper;
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
 * Integration tests for the {@link ReportingSystemMetricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingSystemMetricResourceIT {

    private static final String DEFAULT_METRIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CPU_USAGE = 1D;
    private static final Double UPDATED_CPU_USAGE = 2D;
    private static final Double SMALLER_CPU_USAGE = 1D - 1D;

    private static final Double DEFAULT_MEMORY_USAGE = 1D;
    private static final Double UPDATED_MEMORY_USAGE = 2D;
    private static final Double SMALLER_MEMORY_USAGE = 1D - 1D;

    private static final Double DEFAULT_DISK_USAGE = 1D;
    private static final Double UPDATED_DISK_USAGE = 2D;
    private static final Double SMALLER_DISK_USAGE = 1D - 1D;

    private static final Long DEFAULT_NETWORK_IN = 1L;
    private static final Long UPDATED_NETWORK_IN = 2L;
    private static final Long SMALLER_NETWORK_IN = 1L - 1L;

    private static final Long DEFAULT_NETWORK_OUT = 1L;
    private static final Long UPDATED_NETWORK_OUT = 2L;
    private static final Long SMALLER_NETWORK_OUT = 1L - 1L;

    private static final Integer DEFAULT_ACTIVE_CONNECTIONS = 1;
    private static final Integer UPDATED_ACTIVE_CONNECTIONS = 2;
    private static final Integer SMALLER_ACTIVE_CONNECTIONS = 1 - 1;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reporting-system-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingSystemMetricRepository reportingSystemMetricRepository;

    @Autowired
    private ReportingSystemMetricMapper reportingSystemMetricMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingSystemMetricMockMvc;

    private ReportingSystemMetric reportingSystemMetric;

    private ReportingSystemMetric insertedReportingSystemMetric;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingSystemMetric createEntity() {
        return new ReportingSystemMetric()
            .metricName(DEFAULT_METRIC_NAME)
            .cpuUsage(DEFAULT_CPU_USAGE)
            .memoryUsage(DEFAULT_MEMORY_USAGE)
            .diskUsage(DEFAULT_DISK_USAGE)
            .networkIn(DEFAULT_NETWORK_IN)
            .networkOut(DEFAULT_NETWORK_OUT)
            .activeConnections(DEFAULT_ACTIVE_CONNECTIONS)
            .timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingSystemMetric createUpdatedEntity() {
        return new ReportingSystemMetric()
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        reportingSystemMetric = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingSystemMetric != null) {
            reportingSystemMetricRepository.delete(insertedReportingSystemMetric);
            insertedReportingSystemMetric = null;
        }
    }

    @Test
    @Transactional
    void createReportingSystemMetric() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);
        var returnedReportingSystemMetricDTO = om.readValue(
            restReportingSystemMetricMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingSystemMetricDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingSystemMetricDTO.class
        );

        // Validate the ReportingSystemMetric in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingSystemMetric = reportingSystemMetricMapper.toEntity(returnedReportingSystemMetricDTO);
        assertReportingSystemMetricUpdatableFieldsEquals(
            returnedReportingSystemMetric,
            getPersistedReportingSystemMetric(returnedReportingSystemMetric)
        );

        insertedReportingSystemMetric = returnedReportingSystemMetric;
    }

    @Test
    @Transactional
    void createReportingSystemMetricWithExistingId() throws Exception {
        // Create the ReportingSystemMetric with an existing ID
        reportingSystemMetric.setId(1L);
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingSystemMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingSystemMetric.setMetricName(null);

        // Create the ReportingSystemMetric, which fails.
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        restReportingSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingSystemMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingSystemMetric.setTimestamp(null);

        // Create the ReportingSystemMetric, which fails.
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        restReportingSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingSystemMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetrics() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingSystemMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].diskUsage").value(hasItem(DEFAULT_DISK_USAGE)))
            .andExpect(jsonPath("$.[*].networkIn").value(hasItem(DEFAULT_NETWORK_IN.intValue())))
            .andExpect(jsonPath("$.[*].networkOut").value(hasItem(DEFAULT_NETWORK_OUT.intValue())))
            .andExpect(jsonPath("$.[*].activeConnections").value(hasItem(DEFAULT_ACTIVE_CONNECTIONS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getReportingSystemMetric() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get the reportingSystemMetric
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingSystemMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingSystemMetric.getId().intValue()))
            .andExpect(jsonPath("$.metricName").value(DEFAULT_METRIC_NAME))
            .andExpect(jsonPath("$.cpuUsage").value(DEFAULT_CPU_USAGE))
            .andExpect(jsonPath("$.memoryUsage").value(DEFAULT_MEMORY_USAGE))
            .andExpect(jsonPath("$.diskUsage").value(DEFAULT_DISK_USAGE))
            .andExpect(jsonPath("$.networkIn").value(DEFAULT_NETWORK_IN.intValue()))
            .andExpect(jsonPath("$.networkOut").value(DEFAULT_NETWORK_OUT.intValue()))
            .andExpect(jsonPath("$.activeConnections").value(DEFAULT_ACTIVE_CONNECTIONS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getReportingSystemMetricsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        Long id = reportingSystemMetric.getId();

        defaultReportingSystemMetricFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingSystemMetricFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingSystemMetricFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMetricNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where metricName equals to
        defaultReportingSystemMetricFiltering("metricName.equals=" + DEFAULT_METRIC_NAME, "metricName.equals=" + UPDATED_METRIC_NAME);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMetricNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where metricName in
        defaultReportingSystemMetricFiltering(
            "metricName.in=" + DEFAULT_METRIC_NAME + "," + UPDATED_METRIC_NAME,
            "metricName.in=" + UPDATED_METRIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMetricNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where metricName is not null
        defaultReportingSystemMetricFiltering("metricName.specified=true", "metricName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMetricNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where metricName contains
        defaultReportingSystemMetricFiltering("metricName.contains=" + DEFAULT_METRIC_NAME, "metricName.contains=" + UPDATED_METRIC_NAME);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMetricNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where metricName does not contain
        defaultReportingSystemMetricFiltering(
            "metricName.doesNotContain=" + UPDATED_METRIC_NAME,
            "metricName.doesNotContain=" + DEFAULT_METRIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage equals to
        defaultReportingSystemMetricFiltering("cpuUsage.equals=" + DEFAULT_CPU_USAGE, "cpuUsage.equals=" + UPDATED_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage in
        defaultReportingSystemMetricFiltering(
            "cpuUsage.in=" + DEFAULT_CPU_USAGE + "," + UPDATED_CPU_USAGE,
            "cpuUsage.in=" + UPDATED_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage is not null
        defaultReportingSystemMetricFiltering("cpuUsage.specified=true", "cpuUsage.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "cpuUsage.greaterThanOrEqual=" + DEFAULT_CPU_USAGE,
            "cpuUsage.greaterThanOrEqual=" + UPDATED_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage is less than or equal to
        defaultReportingSystemMetricFiltering(
            "cpuUsage.lessThanOrEqual=" + DEFAULT_CPU_USAGE,
            "cpuUsage.lessThanOrEqual=" + SMALLER_CPU_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage is less than
        defaultReportingSystemMetricFiltering("cpuUsage.lessThan=" + UPDATED_CPU_USAGE, "cpuUsage.lessThan=" + DEFAULT_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByCpuUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where cpuUsage is greater than
        defaultReportingSystemMetricFiltering("cpuUsage.greaterThan=" + SMALLER_CPU_USAGE, "cpuUsage.greaterThan=" + DEFAULT_CPU_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage equals to
        defaultReportingSystemMetricFiltering("memoryUsage.equals=" + DEFAULT_MEMORY_USAGE, "memoryUsage.equals=" + UPDATED_MEMORY_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage in
        defaultReportingSystemMetricFiltering(
            "memoryUsage.in=" + DEFAULT_MEMORY_USAGE + "," + UPDATED_MEMORY_USAGE,
            "memoryUsage.in=" + UPDATED_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage is not null
        defaultReportingSystemMetricFiltering("memoryUsage.specified=true", "memoryUsage.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "memoryUsage.greaterThanOrEqual=" + DEFAULT_MEMORY_USAGE,
            "memoryUsage.greaterThanOrEqual=" + UPDATED_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage is less than or equal to
        defaultReportingSystemMetricFiltering(
            "memoryUsage.lessThanOrEqual=" + DEFAULT_MEMORY_USAGE,
            "memoryUsage.lessThanOrEqual=" + SMALLER_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage is less than
        defaultReportingSystemMetricFiltering(
            "memoryUsage.lessThan=" + UPDATED_MEMORY_USAGE,
            "memoryUsage.lessThan=" + DEFAULT_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByMemoryUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where memoryUsage is greater than
        defaultReportingSystemMetricFiltering(
            "memoryUsage.greaterThan=" + SMALLER_MEMORY_USAGE,
            "memoryUsage.greaterThan=" + DEFAULT_MEMORY_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage equals to
        defaultReportingSystemMetricFiltering("diskUsage.equals=" + DEFAULT_DISK_USAGE, "diskUsage.equals=" + UPDATED_DISK_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage in
        defaultReportingSystemMetricFiltering(
            "diskUsage.in=" + DEFAULT_DISK_USAGE + "," + UPDATED_DISK_USAGE,
            "diskUsage.in=" + UPDATED_DISK_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage is not null
        defaultReportingSystemMetricFiltering("diskUsage.specified=true", "diskUsage.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "diskUsage.greaterThanOrEqual=" + DEFAULT_DISK_USAGE,
            "diskUsage.greaterThanOrEqual=" + UPDATED_DISK_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage is less than or equal to
        defaultReportingSystemMetricFiltering(
            "diskUsage.lessThanOrEqual=" + DEFAULT_DISK_USAGE,
            "diskUsage.lessThanOrEqual=" + SMALLER_DISK_USAGE
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage is less than
        defaultReportingSystemMetricFiltering("diskUsage.lessThan=" + UPDATED_DISK_USAGE, "diskUsage.lessThan=" + DEFAULT_DISK_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByDiskUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where diskUsage is greater than
        defaultReportingSystemMetricFiltering("diskUsage.greaterThan=" + SMALLER_DISK_USAGE, "diskUsage.greaterThan=" + DEFAULT_DISK_USAGE);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn equals to
        defaultReportingSystemMetricFiltering("networkIn.equals=" + DEFAULT_NETWORK_IN, "networkIn.equals=" + UPDATED_NETWORK_IN);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn in
        defaultReportingSystemMetricFiltering(
            "networkIn.in=" + DEFAULT_NETWORK_IN + "," + UPDATED_NETWORK_IN,
            "networkIn.in=" + UPDATED_NETWORK_IN
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn is not null
        defaultReportingSystemMetricFiltering("networkIn.specified=true", "networkIn.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "networkIn.greaterThanOrEqual=" + DEFAULT_NETWORK_IN,
            "networkIn.greaterThanOrEqual=" + UPDATED_NETWORK_IN
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn is less than or equal to
        defaultReportingSystemMetricFiltering(
            "networkIn.lessThanOrEqual=" + DEFAULT_NETWORK_IN,
            "networkIn.lessThanOrEqual=" + SMALLER_NETWORK_IN
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn is less than
        defaultReportingSystemMetricFiltering("networkIn.lessThan=" + UPDATED_NETWORK_IN, "networkIn.lessThan=" + DEFAULT_NETWORK_IN);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkInIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkIn is greater than
        defaultReportingSystemMetricFiltering("networkIn.greaterThan=" + SMALLER_NETWORK_IN, "networkIn.greaterThan=" + DEFAULT_NETWORK_IN);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut equals to
        defaultReportingSystemMetricFiltering("networkOut.equals=" + DEFAULT_NETWORK_OUT, "networkOut.equals=" + UPDATED_NETWORK_OUT);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut in
        defaultReportingSystemMetricFiltering(
            "networkOut.in=" + DEFAULT_NETWORK_OUT + "," + UPDATED_NETWORK_OUT,
            "networkOut.in=" + UPDATED_NETWORK_OUT
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut is not null
        defaultReportingSystemMetricFiltering("networkOut.specified=true", "networkOut.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "networkOut.greaterThanOrEqual=" + DEFAULT_NETWORK_OUT,
            "networkOut.greaterThanOrEqual=" + UPDATED_NETWORK_OUT
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut is less than or equal to
        defaultReportingSystemMetricFiltering(
            "networkOut.lessThanOrEqual=" + DEFAULT_NETWORK_OUT,
            "networkOut.lessThanOrEqual=" + SMALLER_NETWORK_OUT
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut is less than
        defaultReportingSystemMetricFiltering("networkOut.lessThan=" + UPDATED_NETWORK_OUT, "networkOut.lessThan=" + DEFAULT_NETWORK_OUT);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByNetworkOutIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where networkOut is greater than
        defaultReportingSystemMetricFiltering(
            "networkOut.greaterThan=" + SMALLER_NETWORK_OUT,
            "networkOut.greaterThan=" + DEFAULT_NETWORK_OUT
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections equals to
        defaultReportingSystemMetricFiltering(
            "activeConnections.equals=" + DEFAULT_ACTIVE_CONNECTIONS,
            "activeConnections.equals=" + UPDATED_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections in
        defaultReportingSystemMetricFiltering(
            "activeConnections.in=" + DEFAULT_ACTIVE_CONNECTIONS + "," + UPDATED_ACTIVE_CONNECTIONS,
            "activeConnections.in=" + UPDATED_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections is not null
        defaultReportingSystemMetricFiltering("activeConnections.specified=true", "activeConnections.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections is greater than or equal to
        defaultReportingSystemMetricFiltering(
            "activeConnections.greaterThanOrEqual=" + DEFAULT_ACTIVE_CONNECTIONS,
            "activeConnections.greaterThanOrEqual=" + UPDATED_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections is less than or equal to
        defaultReportingSystemMetricFiltering(
            "activeConnections.lessThanOrEqual=" + DEFAULT_ACTIVE_CONNECTIONS,
            "activeConnections.lessThanOrEqual=" + SMALLER_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections is less than
        defaultReportingSystemMetricFiltering(
            "activeConnections.lessThan=" + UPDATED_ACTIVE_CONNECTIONS,
            "activeConnections.lessThan=" + DEFAULT_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByActiveConnectionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where activeConnections is greater than
        defaultReportingSystemMetricFiltering(
            "activeConnections.greaterThan=" + SMALLER_ACTIVE_CONNECTIONS,
            "activeConnections.greaterThan=" + DEFAULT_ACTIVE_CONNECTIONS
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where timestamp equals to
        defaultReportingSystemMetricFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where timestamp in
        defaultReportingSystemMetricFiltering(
            "timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP,
            "timestamp.in=" + UPDATED_TIMESTAMP
        );
    }

    @Test
    @Transactional
    void getAllReportingSystemMetricsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        // Get all the reportingSystemMetricList where timestamp is not null
        defaultReportingSystemMetricFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    private void defaultReportingSystemMetricFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingSystemMetricShouldBeFound(shouldBeFound);
        defaultReportingSystemMetricShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingSystemMetricShouldBeFound(String filter) throws Exception {
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingSystemMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].diskUsage").value(hasItem(DEFAULT_DISK_USAGE)))
            .andExpect(jsonPath("$.[*].networkIn").value(hasItem(DEFAULT_NETWORK_IN.intValue())))
            .andExpect(jsonPath("$.[*].networkOut").value(hasItem(DEFAULT_NETWORK_OUT.intValue())))
            .andExpect(jsonPath("$.[*].activeConnections").value(hasItem(DEFAULT_ACTIVE_CONNECTIONS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));

        // Check, that the count call also returns 1
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingSystemMetricShouldNotBeFound(String filter) throws Exception {
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingSystemMetric() throws Exception {
        // Get the reportingSystemMetric
        restReportingSystemMetricMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingSystemMetric() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingSystemMetric
        ReportingSystemMetric updatedReportingSystemMetric = reportingSystemMetricRepository
            .findById(reportingSystemMetric.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedReportingSystemMetric are not directly saved in db
        em.detach(updatedReportingSystemMetric);
        updatedReportingSystemMetric
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(updatedReportingSystemMetric);

        restReportingSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingSystemMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingSystemMetricToMatchAllProperties(updatedReportingSystemMetric);
    }

    @Test
    @Transactional
    void putNonExistingReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingSystemMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingSystemMetricDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingSystemMetricWithPatch() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingSystemMetric using partial update
        ReportingSystemMetric partialUpdatedReportingSystemMetric = new ReportingSystemMetric();
        partialUpdatedReportingSystemMetric.setId(reportingSystemMetric.getId());

        partialUpdatedReportingSystemMetric
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);

        restReportingSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingSystemMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingSystemMetric))
            )
            .andExpect(status().isOk());

        // Validate the ReportingSystemMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingSystemMetricUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingSystemMetric, reportingSystemMetric),
            getPersistedReportingSystemMetric(reportingSystemMetric)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingSystemMetricWithPatch() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingSystemMetric using partial update
        ReportingSystemMetric partialUpdatedReportingSystemMetric = new ReportingSystemMetric();
        partialUpdatedReportingSystemMetric.setId(reportingSystemMetric.getId());

        partialUpdatedReportingSystemMetric
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);

        restReportingSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingSystemMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingSystemMetric))
            )
            .andExpect(status().isOk());

        // Validate the ReportingSystemMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingSystemMetricUpdatableFieldsEquals(
            partialUpdatedReportingSystemMetric,
            getPersistedReportingSystemMetric(partialUpdatedReportingSystemMetric)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingSystemMetricDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingSystemMetric.setId(longCount.incrementAndGet());

        // Create the ReportingSystemMetric
        ReportingSystemMetricDTO reportingSystemMetricDTO = reportingSystemMetricMapper.toDto(reportingSystemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportingSystemMetricDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingSystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingSystemMetric() throws Exception {
        // Initialize the database
        insertedReportingSystemMetric = reportingSystemMetricRepository.saveAndFlush(reportingSystemMetric);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingSystemMetric
        restReportingSystemMetricMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingSystemMetric.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingSystemMetricRepository.count();
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

    protected ReportingSystemMetric getPersistedReportingSystemMetric(ReportingSystemMetric reportingSystemMetric) {
        return reportingSystemMetricRepository.findById(reportingSystemMetric.getId()).orElseThrow();
    }

    protected void assertPersistedReportingSystemMetricToMatchAllProperties(ReportingSystemMetric expectedReportingSystemMetric) {
        assertReportingSystemMetricAllPropertiesEquals(
            expectedReportingSystemMetric,
            getPersistedReportingSystemMetric(expectedReportingSystemMetric)
        );
    }

    protected void assertPersistedReportingSystemMetricToMatchUpdatableProperties(ReportingSystemMetric expectedReportingSystemMetric) {
        assertReportingSystemMetricAllUpdatablePropertiesEquals(
            expectedReportingSystemMetric,
            getPersistedReportingSystemMetric(expectedReportingSystemMetric)
        );
    }
}
