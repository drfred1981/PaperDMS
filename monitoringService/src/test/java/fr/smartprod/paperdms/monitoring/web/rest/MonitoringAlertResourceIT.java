package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlert;
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertMapper;
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
 * Integration tests for the {@link MonitoringAlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringAlertResourceIT {

    private static final Severity DEFAULT_SEVERITY = Severity.LOW;
    private static final Severity UPDATED_SEVERITY = Severity.MEDIUM;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final MonitoringAlertStatus DEFAULT_STATUS = MonitoringAlertStatus.ACTIVE;
    private static final MonitoringAlertStatus UPDATED_STATUS = MonitoringAlertStatus.ACKNOWLEDGED;

    private static final Instant DEFAULT_TRIGGERED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRIGGERED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACKNOWLEDGED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACKNOWLEDGED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACKNOWLEDGED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACKNOWLEDGED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_RESOLVED_BY = "AAAAAAAAAA";
    private static final String UPDATED_RESOLVED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_RESOLVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RESOLVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/monitoring-alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringAlertRepository monitoringAlertRepository;

    @Autowired
    private MonitoringAlertMapper monitoringAlertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringAlertMockMvc;

    private MonitoringAlert monitoringAlert;

    private MonitoringAlert insertedMonitoringAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringAlert createEntity() {
        return new MonitoringAlert()
            .severity(DEFAULT_SEVERITY)
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityName(DEFAULT_ENTITY_NAME)
            .status(DEFAULT_STATUS)
            .triggeredDate(DEFAULT_TRIGGERED_DATE)
            .acknowledgedBy(DEFAULT_ACKNOWLEDGED_BY)
            .acknowledgedDate(DEFAULT_ACKNOWLEDGED_DATE)
            .resolvedBy(DEFAULT_RESOLVED_BY)
            .resolvedDate(DEFAULT_RESOLVED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringAlert createUpdatedEntity() {
        return new MonitoringAlert()
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);
    }

    @BeforeEach
    void initTest() {
        monitoringAlert = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringAlert != null) {
            monitoringAlertRepository.delete(insertedMonitoringAlert);
            insertedMonitoringAlert = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);
        var returnedMonitoringAlertDTO = om.readValue(
            restMonitoringAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringAlertDTO.class
        );

        // Validate the MonitoringAlert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringAlert = monitoringAlertMapper.toEntity(returnedMonitoringAlertDTO);
        assertMonitoringAlertUpdatableFieldsEquals(returnedMonitoringAlert, getPersistedMonitoringAlert(returnedMonitoringAlert));

        insertedMonitoringAlert = returnedMonitoringAlert;
    }

    @Test
    @Transactional
    void createMonitoringAlertWithExistingId() throws Exception {
        // Create the MonitoringAlert with an existing ID
        monitoringAlert.setId(1L);
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlert.setSeverity(null);

        // Create the MonitoringAlert, which fails.
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        restMonitoringAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlert.setTitle(null);

        // Create the MonitoringAlert, which fails.
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        restMonitoringAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlert.setStatus(null);

        // Create the MonitoringAlert, which fails.
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        restMonitoringAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTriggeredDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlert.setTriggeredDate(null);

        // Create the MonitoringAlert, which fails.
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        restMonitoringAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringAlerts() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].triggeredDate").value(hasItem(DEFAULT_TRIGGERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].acknowledgedBy").value(hasItem(DEFAULT_ACKNOWLEDGED_BY)))
            .andExpect(jsonPath("$.[*].acknowledgedDate").value(hasItem(DEFAULT_ACKNOWLEDGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].resolvedBy").value(hasItem(DEFAULT_RESOLVED_BY)))
            .andExpect(jsonPath("$.[*].resolvedDate").value(hasItem(DEFAULT_RESOLVED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMonitoringAlert() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get the monitoringAlert
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringAlert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringAlert.getId().intValue()))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.triggeredDate").value(DEFAULT_TRIGGERED_DATE.toString()))
            .andExpect(jsonPath("$.acknowledgedBy").value(DEFAULT_ACKNOWLEDGED_BY))
            .andExpect(jsonPath("$.acknowledgedDate").value(DEFAULT_ACKNOWLEDGED_DATE.toString()))
            .andExpect(jsonPath("$.resolvedBy").value(DEFAULT_RESOLVED_BY))
            .andExpect(jsonPath("$.resolvedDate").value(DEFAULT_RESOLVED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMonitoringAlertsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        Long id = monitoringAlert.getId();

        defaultMonitoringAlertFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringAlertFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringAlertFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where severity equals to
        defaultMonitoringAlertFiltering("severity.equals=" + DEFAULT_SEVERITY, "severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where severity in
        defaultMonitoringAlertFiltering("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY, "severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where severity is not null
        defaultMonitoringAlertFiltering("severity.specified=true", "severity.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where title equals to
        defaultMonitoringAlertFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where title in
        defaultMonitoringAlertFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where title is not null
        defaultMonitoringAlertFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where title contains
        defaultMonitoringAlertFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where title does not contain
        defaultMonitoringAlertFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityType equals to
        defaultMonitoringAlertFiltering("entityType.equals=" + DEFAULT_ENTITY_TYPE, "entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityType in
        defaultMonitoringAlertFiltering(
            "entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE,
            "entityType.in=" + UPDATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityType is not null
        defaultMonitoringAlertFiltering("entityType.specified=true", "entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityType contains
        defaultMonitoringAlertFiltering("entityType.contains=" + DEFAULT_ENTITY_TYPE, "entityType.contains=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityType does not contain
        defaultMonitoringAlertFiltering(
            "entityType.doesNotContain=" + UPDATED_ENTITY_TYPE,
            "entityType.doesNotContain=" + DEFAULT_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityName equals to
        defaultMonitoringAlertFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityName in
        defaultMonitoringAlertFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityName is not null
        defaultMonitoringAlertFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityName contains
        defaultMonitoringAlertFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where entityName does not contain
        defaultMonitoringAlertFiltering(
            "entityName.doesNotContain=" + UPDATED_ENTITY_NAME,
            "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where status equals to
        defaultMonitoringAlertFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where status in
        defaultMonitoringAlertFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where status is not null
        defaultMonitoringAlertFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTriggeredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where triggeredDate equals to
        defaultMonitoringAlertFiltering("triggeredDate.equals=" + DEFAULT_TRIGGERED_DATE, "triggeredDate.equals=" + UPDATED_TRIGGERED_DATE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTriggeredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where triggeredDate in
        defaultMonitoringAlertFiltering(
            "triggeredDate.in=" + DEFAULT_TRIGGERED_DATE + "," + UPDATED_TRIGGERED_DATE,
            "triggeredDate.in=" + UPDATED_TRIGGERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByTriggeredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where triggeredDate is not null
        defaultMonitoringAlertFiltering("triggeredDate.specified=true", "triggeredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedBy equals to
        defaultMonitoringAlertFiltering(
            "acknowledgedBy.equals=" + DEFAULT_ACKNOWLEDGED_BY,
            "acknowledgedBy.equals=" + UPDATED_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedBy in
        defaultMonitoringAlertFiltering(
            "acknowledgedBy.in=" + DEFAULT_ACKNOWLEDGED_BY + "," + UPDATED_ACKNOWLEDGED_BY,
            "acknowledgedBy.in=" + UPDATED_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedBy is not null
        defaultMonitoringAlertFiltering("acknowledgedBy.specified=true", "acknowledgedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedBy contains
        defaultMonitoringAlertFiltering(
            "acknowledgedBy.contains=" + DEFAULT_ACKNOWLEDGED_BY,
            "acknowledgedBy.contains=" + UPDATED_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedBy does not contain
        defaultMonitoringAlertFiltering(
            "acknowledgedBy.doesNotContain=" + UPDATED_ACKNOWLEDGED_BY,
            "acknowledgedBy.doesNotContain=" + DEFAULT_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedDate equals to
        defaultMonitoringAlertFiltering(
            "acknowledgedDate.equals=" + DEFAULT_ACKNOWLEDGED_DATE,
            "acknowledgedDate.equals=" + UPDATED_ACKNOWLEDGED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedDate in
        defaultMonitoringAlertFiltering(
            "acknowledgedDate.in=" + DEFAULT_ACKNOWLEDGED_DATE + "," + UPDATED_ACKNOWLEDGED_DATE,
            "acknowledgedDate.in=" + UPDATED_ACKNOWLEDGED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAcknowledgedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where acknowledgedDate is not null
        defaultMonitoringAlertFiltering("acknowledgedDate.specified=true", "acknowledgedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedBy equals to
        defaultMonitoringAlertFiltering("resolvedBy.equals=" + DEFAULT_RESOLVED_BY, "resolvedBy.equals=" + UPDATED_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedBy in
        defaultMonitoringAlertFiltering(
            "resolvedBy.in=" + DEFAULT_RESOLVED_BY + "," + UPDATED_RESOLVED_BY,
            "resolvedBy.in=" + UPDATED_RESOLVED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedBy is not null
        defaultMonitoringAlertFiltering("resolvedBy.specified=true", "resolvedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedBy contains
        defaultMonitoringAlertFiltering("resolvedBy.contains=" + DEFAULT_RESOLVED_BY, "resolvedBy.contains=" + UPDATED_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedBy does not contain
        defaultMonitoringAlertFiltering(
            "resolvedBy.doesNotContain=" + UPDATED_RESOLVED_BY,
            "resolvedBy.doesNotContain=" + DEFAULT_RESOLVED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedDate equals to
        defaultMonitoringAlertFiltering("resolvedDate.equals=" + DEFAULT_RESOLVED_DATE, "resolvedDate.equals=" + UPDATED_RESOLVED_DATE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedDate in
        defaultMonitoringAlertFiltering(
            "resolvedDate.in=" + DEFAULT_RESOLVED_DATE + "," + UPDATED_RESOLVED_DATE,
            "resolvedDate.in=" + UPDATED_RESOLVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByResolvedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        // Get all the monitoringAlertList where resolvedDate is not null
        defaultMonitoringAlertFiltering("resolvedDate.specified=true", "resolvedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertsByAlertRuleIsEqualToSomething() throws Exception {
        MonitoringAlertRule alertRule;
        if (TestUtil.findAll(em, MonitoringAlertRule.class).isEmpty()) {
            monitoringAlertRepository.saveAndFlush(monitoringAlert);
            alertRule = MonitoringAlertRuleResourceIT.createEntity();
        } else {
            alertRule = TestUtil.findAll(em, MonitoringAlertRule.class).get(0);
        }
        em.persist(alertRule);
        em.flush();
        monitoringAlert.setAlertRule(alertRule);
        monitoringAlertRepository.saveAndFlush(monitoringAlert);
        Long alertRuleId = alertRule.getId();
        // Get all the monitoringAlertList where alertRule equals to alertRuleId
        defaultMonitoringAlertShouldBeFound("alertRuleId.equals=" + alertRuleId);

        // Get all the monitoringAlertList where alertRule equals to (alertRuleId + 1)
        defaultMonitoringAlertShouldNotBeFound("alertRuleId.equals=" + (alertRuleId + 1));
    }

    private void defaultMonitoringAlertFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringAlertShouldBeFound(shouldBeFound);
        defaultMonitoringAlertShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringAlertShouldBeFound(String filter) throws Exception {
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringAlert.getId().intValue())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].triggeredDate").value(hasItem(DEFAULT_TRIGGERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].acknowledgedBy").value(hasItem(DEFAULT_ACKNOWLEDGED_BY)))
            .andExpect(jsonPath("$.[*].acknowledgedDate").value(hasItem(DEFAULT_ACKNOWLEDGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].resolvedBy").value(hasItem(DEFAULT_RESOLVED_BY)))
            .andExpect(jsonPath("$.[*].resolvedDate").value(hasItem(DEFAULT_RESOLVED_DATE.toString())));

        // Check, that the count call also returns 1
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringAlertShouldNotBeFound(String filter) throws Exception {
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringAlert() throws Exception {
        // Get the monitoringAlert
        restMonitoringAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringAlert() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlert
        MonitoringAlert updatedMonitoringAlert = monitoringAlertRepository.findById(monitoringAlert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringAlert are not directly saved in db
        em.detach(updatedMonitoringAlert);
        updatedMonitoringAlert
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(updatedMonitoringAlert);

        restMonitoringAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringAlertToMatchAllProperties(updatedMonitoringAlert);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringAlertDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringAlertWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlert using partial update
        MonitoringAlert partialUpdatedMonitoringAlert = new MonitoringAlert();
        partialUpdatedMonitoringAlert.setId(monitoringAlert.getId());

        partialUpdatedMonitoringAlert
            .message(UPDATED_MESSAGE)
            .entityName(UPDATED_ENTITY_NAME)
            .status(UPDATED_STATUS)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY);

        restMonitoringAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringAlert))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringAlertUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringAlert, monitoringAlert),
            getPersistedMonitoringAlert(monitoringAlert)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringAlertWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlert using partial update
        MonitoringAlert partialUpdatedMonitoringAlert = new MonitoringAlert();
        partialUpdatedMonitoringAlert.setId(monitoringAlert.getId());

        partialUpdatedMonitoringAlert
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);

        restMonitoringAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringAlert))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringAlertUpdatableFieldsEquals(
            partialUpdatedMonitoringAlert,
            getPersistedMonitoringAlert(partialUpdatedMonitoringAlert)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringAlertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringAlertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlert.setId(longCount.incrementAndGet());

        // Create the MonitoringAlert
        MonitoringAlertDTO monitoringAlertDTO = monitoringAlertMapper.toDto(monitoringAlert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitoringAlertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringAlert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringAlert() throws Exception {
        // Initialize the database
        insertedMonitoringAlert = monitoringAlertRepository.saveAndFlush(monitoringAlert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringAlert
        restMonitoringAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringAlert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringAlertRepository.count();
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

    protected MonitoringAlert getPersistedMonitoringAlert(MonitoringAlert monitoringAlert) {
        return monitoringAlertRepository.findById(monitoringAlert.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringAlertToMatchAllProperties(MonitoringAlert expectedMonitoringAlert) {
        assertMonitoringAlertAllPropertiesEquals(expectedMonitoringAlert, getPersistedMonitoringAlert(expectedMonitoringAlert));
    }

    protected void assertPersistedMonitoringAlertToMatchUpdatableProperties(MonitoringAlert expectedMonitoringAlert) {
        assertMonitoringAlertAllUpdatablePropertiesEquals(expectedMonitoringAlert, getPersistedMonitoringAlert(expectedMonitoringAlert));
    }
}
