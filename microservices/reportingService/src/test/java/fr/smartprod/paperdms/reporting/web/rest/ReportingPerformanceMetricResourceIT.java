package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingPerformanceMetric;
import fr.smartprod.paperdms.reporting.domain.enumeration.MetricType;
import fr.smartprod.paperdms.reporting.repository.ReportingPerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingPerformanceMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingPerformanceMetricMapper;
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
 * Integration tests for the {@link ReportingPerformanceMetricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingPerformanceMetricResourceIT {

    private static final String DEFAULT_METRIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_NAME = "BBBBBBBBBB";

    private static final MetricType DEFAULT_METRIC_TYPE = MetricType.UPLOAD_SPEED;
    private static final MetricType UPDATED_METRIC_TYPE = MetricType.OCR_DURATION;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;
    private static final Double SMALLER_VALUE = 1D - 1D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reporting-performance-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingPerformanceMetricRepository reportingPerformanceMetricRepository;

    @Autowired
    private ReportingPerformanceMetricMapper reportingPerformanceMetricMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingPerformanceMetricMockMvc;

    private ReportingPerformanceMetric reportingPerformanceMetric;

    private ReportingPerformanceMetric insertedReportingPerformanceMetric;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingPerformanceMetric createEntity() {
        return new ReportingPerformanceMetric()
            .metricName(DEFAULT_METRIC_NAME)
            .metricType(DEFAULT_METRIC_TYPE)
            .value(DEFAULT_VALUE)
            .unit(DEFAULT_UNIT)
            .serviceName(DEFAULT_SERVICE_NAME)
            .timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingPerformanceMetric createUpdatedEntity() {
        return new ReportingPerformanceMetric()
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        reportingPerformanceMetric = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingPerformanceMetric != null) {
            reportingPerformanceMetricRepository.delete(insertedReportingPerformanceMetric);
            insertedReportingPerformanceMetric = null;
        }
    }

    @Test
    @Transactional
    void createReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);
        var returnedReportingPerformanceMetricDTO = om.readValue(
            restReportingPerformanceMetricMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingPerformanceMetricDTO.class
        );

        // Validate the ReportingPerformanceMetric in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingPerformanceMetric = reportingPerformanceMetricMapper.toEntity(returnedReportingPerformanceMetricDTO);
        assertReportingPerformanceMetricUpdatableFieldsEquals(
            returnedReportingPerformanceMetric,
            getPersistedReportingPerformanceMetric(returnedReportingPerformanceMetric)
        );

        insertedReportingPerformanceMetric = returnedReportingPerformanceMetric;
    }

    @Test
    @Transactional
    void createReportingPerformanceMetricWithExistingId() throws Exception {
        // Create the ReportingPerformanceMetric with an existing ID
        reportingPerformanceMetric.setId(1L);
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingPerformanceMetric.setMetricName(null);

        // Create the ReportingPerformanceMetric, which fails.
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetricTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingPerformanceMetric.setMetricType(null);

        // Create the ReportingPerformanceMetric, which fails.
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingPerformanceMetric.setValue(null);

        // Create the ReportingPerformanceMetric, which fails.
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingPerformanceMetric.setUnit(null);

        // Create the ReportingPerformanceMetric, which fails.
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingPerformanceMetric.setTimestamp(null);

        // Create the ReportingPerformanceMetric, which fails.
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        restReportingPerformanceMetricMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetrics() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingPerformanceMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].metricType").value(hasItem(DEFAULT_METRIC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getReportingPerformanceMetric() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get the reportingPerformanceMetric
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingPerformanceMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingPerformanceMetric.getId().intValue()))
            .andExpect(jsonPath("$.metricName").value(DEFAULT_METRIC_NAME))
            .andExpect(jsonPath("$.metricType").value(DEFAULT_METRIC_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getReportingPerformanceMetricsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        Long id = reportingPerformanceMetric.getId();

        defaultReportingPerformanceMetricFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingPerformanceMetricFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingPerformanceMetricFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricName equals to
        defaultReportingPerformanceMetricFiltering("metricName.equals=" + DEFAULT_METRIC_NAME, "metricName.equals=" + UPDATED_METRIC_NAME);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricName in
        defaultReportingPerformanceMetricFiltering(
            "metricName.in=" + DEFAULT_METRIC_NAME + "," + UPDATED_METRIC_NAME,
            "metricName.in=" + UPDATED_METRIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricName is not null
        defaultReportingPerformanceMetricFiltering("metricName.specified=true", "metricName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricName contains
        defaultReportingPerformanceMetricFiltering(
            "metricName.contains=" + DEFAULT_METRIC_NAME,
            "metricName.contains=" + UPDATED_METRIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricName does not contain
        defaultReportingPerformanceMetricFiltering(
            "metricName.doesNotContain=" + UPDATED_METRIC_NAME,
            "metricName.doesNotContain=" + DEFAULT_METRIC_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricType equals to
        defaultReportingPerformanceMetricFiltering("metricType.equals=" + DEFAULT_METRIC_TYPE, "metricType.equals=" + UPDATED_METRIC_TYPE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricType in
        defaultReportingPerformanceMetricFiltering(
            "metricType.in=" + DEFAULT_METRIC_TYPE + "," + UPDATED_METRIC_TYPE,
            "metricType.in=" + UPDATED_METRIC_TYPE
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByMetricTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where metricType is not null
        defaultReportingPerformanceMetricFiltering("metricType.specified=true", "metricType.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value equals to
        defaultReportingPerformanceMetricFiltering("value.equals=" + DEFAULT_VALUE, "value.equals=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value in
        defaultReportingPerformanceMetricFiltering("value.in=" + DEFAULT_VALUE + "," + UPDATED_VALUE, "value.in=" + UPDATED_VALUE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value is not null
        defaultReportingPerformanceMetricFiltering("value.specified=true", "value.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value is greater than or equal to
        defaultReportingPerformanceMetricFiltering(
            "value.greaterThanOrEqual=" + DEFAULT_VALUE,
            "value.greaterThanOrEqual=" + UPDATED_VALUE
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value is less than or equal to
        defaultReportingPerformanceMetricFiltering("value.lessThanOrEqual=" + DEFAULT_VALUE, "value.lessThanOrEqual=" + SMALLER_VALUE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value is less than
        defaultReportingPerformanceMetricFiltering("value.lessThan=" + UPDATED_VALUE, "value.lessThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByValueIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where value is greater than
        defaultReportingPerformanceMetricFiltering("value.greaterThan=" + SMALLER_VALUE, "value.greaterThan=" + DEFAULT_VALUE);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where unit equals to
        defaultReportingPerformanceMetricFiltering("unit.equals=" + DEFAULT_UNIT, "unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where unit in
        defaultReportingPerformanceMetricFiltering("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT, "unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where unit is not null
        defaultReportingPerformanceMetricFiltering("unit.specified=true", "unit.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where unit contains
        defaultReportingPerformanceMetricFiltering("unit.contains=" + DEFAULT_UNIT, "unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where unit does not contain
        defaultReportingPerformanceMetricFiltering("unit.doesNotContain=" + UPDATED_UNIT, "unit.doesNotContain=" + DEFAULT_UNIT);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByServiceNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where serviceName equals to
        defaultReportingPerformanceMetricFiltering(
            "serviceName.equals=" + DEFAULT_SERVICE_NAME,
            "serviceName.equals=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByServiceNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where serviceName in
        defaultReportingPerformanceMetricFiltering(
            "serviceName.in=" + DEFAULT_SERVICE_NAME + "," + UPDATED_SERVICE_NAME,
            "serviceName.in=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByServiceNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where serviceName is not null
        defaultReportingPerformanceMetricFiltering("serviceName.specified=true", "serviceName.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByServiceNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where serviceName contains
        defaultReportingPerformanceMetricFiltering(
            "serviceName.contains=" + DEFAULT_SERVICE_NAME,
            "serviceName.contains=" + UPDATED_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByServiceNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where serviceName does not contain
        defaultReportingPerformanceMetricFiltering(
            "serviceName.doesNotContain=" + UPDATED_SERVICE_NAME,
            "serviceName.doesNotContain=" + DEFAULT_SERVICE_NAME
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByTimestampIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where timestamp equals to
        defaultReportingPerformanceMetricFiltering("timestamp.equals=" + DEFAULT_TIMESTAMP, "timestamp.equals=" + UPDATED_TIMESTAMP);
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByTimestampIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where timestamp in
        defaultReportingPerformanceMetricFiltering(
            "timestamp.in=" + DEFAULT_TIMESTAMP + "," + UPDATED_TIMESTAMP,
            "timestamp.in=" + UPDATED_TIMESTAMP
        );
    }

    @Test
    @Transactional
    void getAllReportingPerformanceMetricsByTimestampIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        // Get all the reportingPerformanceMetricList where timestamp is not null
        defaultReportingPerformanceMetricFiltering("timestamp.specified=true", "timestamp.specified=false");
    }

    private void defaultReportingPerformanceMetricFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingPerformanceMetricShouldBeFound(shouldBeFound);
        defaultReportingPerformanceMetricShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingPerformanceMetricShouldBeFound(String filter) throws Exception {
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingPerformanceMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].metricType").value(hasItem(DEFAULT_METRIC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));

        // Check, that the count call also returns 1
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingPerformanceMetricShouldNotBeFound(String filter) throws Exception {
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingPerformanceMetric() throws Exception {
        // Get the reportingPerformanceMetric
        restReportingPerformanceMetricMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingPerformanceMetric() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingPerformanceMetric
        ReportingPerformanceMetric updatedReportingPerformanceMetric = reportingPerformanceMetricRepository
            .findById(reportingPerformanceMetric.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedReportingPerformanceMetric are not directly saved in db
        em.detach(updatedReportingPerformanceMetric);
        updatedReportingPerformanceMetric
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(
            updatedReportingPerformanceMetric
        );

        restReportingPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingPerformanceMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingPerformanceMetricToMatchAllProperties(updatedReportingPerformanceMetric);
    }

    @Test
    @Transactional
    void putNonExistingReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingPerformanceMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingPerformanceMetricWithPatch() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingPerformanceMetric using partial update
        ReportingPerformanceMetric partialUpdatedReportingPerformanceMetric = new ReportingPerformanceMetric();
        partialUpdatedReportingPerformanceMetric.setId(reportingPerformanceMetric.getId());

        partialUpdatedReportingPerformanceMetric.metricName(UPDATED_METRIC_NAME).serviceName(UPDATED_SERVICE_NAME);

        restReportingPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingPerformanceMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingPerformanceMetric))
            )
            .andExpect(status().isOk());

        // Validate the ReportingPerformanceMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingPerformanceMetricUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingPerformanceMetric, reportingPerformanceMetric),
            getPersistedReportingPerformanceMetric(reportingPerformanceMetric)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingPerformanceMetricWithPatch() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingPerformanceMetric using partial update
        ReportingPerformanceMetric partialUpdatedReportingPerformanceMetric = new ReportingPerformanceMetric();
        partialUpdatedReportingPerformanceMetric.setId(reportingPerformanceMetric.getId());

        partialUpdatedReportingPerformanceMetric
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);

        restReportingPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingPerformanceMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingPerformanceMetric))
            )
            .andExpect(status().isOk());

        // Validate the ReportingPerformanceMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingPerformanceMetricUpdatableFieldsEquals(
            partialUpdatedReportingPerformanceMetric,
            getPersistedReportingPerformanceMetric(partialUpdatedReportingPerformanceMetric)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingPerformanceMetricDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingPerformanceMetric.setId(longCount.incrementAndGet());

        // Create the ReportingPerformanceMetric
        ReportingPerformanceMetricDTO reportingPerformanceMetricDTO = reportingPerformanceMetricMapper.toDto(reportingPerformanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingPerformanceMetricDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingPerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingPerformanceMetric() throws Exception {
        // Initialize the database
        insertedReportingPerformanceMetric = reportingPerformanceMetricRepository.saveAndFlush(reportingPerformanceMetric);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingPerformanceMetric
        restReportingPerformanceMetricMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingPerformanceMetric.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingPerformanceMetricRepository.count();
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

    protected ReportingPerformanceMetric getPersistedReportingPerformanceMetric(ReportingPerformanceMetric reportingPerformanceMetric) {
        return reportingPerformanceMetricRepository.findById(reportingPerformanceMetric.getId()).orElseThrow();
    }

    protected void assertPersistedReportingPerformanceMetricToMatchAllProperties(
        ReportingPerformanceMetric expectedReportingPerformanceMetric
    ) {
        assertReportingPerformanceMetricAllPropertiesEquals(
            expectedReportingPerformanceMetric,
            getPersistedReportingPerformanceMetric(expectedReportingPerformanceMetric)
        );
    }

    protected void assertPersistedReportingPerformanceMetricToMatchUpdatableProperties(
        ReportingPerformanceMetric expectedReportingPerformanceMetric
    ) {
        assertReportingPerformanceMetricAllUpdatablePropertiesEquals(
            expectedReportingPerformanceMetric,
            getPersistedReportingPerformanceMetric(expectedReportingPerformanceMetric)
        );
    }
}
