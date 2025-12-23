package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.AlertAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.Alert;
import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.domain.enumeration.AlertStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import fr.smartprod.paperdms.monitoring.repository.AlertRepository;
import fr.smartprod.paperdms.monitoring.service.dto.AlertDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertMapper;
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
 * Integration tests for the {@link AlertResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlertResourceIT {

    private static final Long DEFAULT_ALERT_RULE_ID = 1L;
    private static final Long UPDATED_ALERT_RULE_ID = 2L;
    private static final Long SMALLER_ALERT_RULE_ID = 1L - 1L;

    private static final Severity DEFAULT_SEVERITY = Severity.LOW;
    private static final Severity UPDATED_SEVERITY = Severity.MEDIUM;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;
    private static final Long SMALLER_ENTITY_ID = 1L - 1L;

    private static final AlertStatus DEFAULT_STATUS = AlertStatus.ACTIVE;
    private static final AlertStatus UPDATED_STATUS = AlertStatus.ACKNOWLEDGED;

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

    private static final String ENTITY_API_URL = "/api/alerts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlertRepository alertRepository;

    @Autowired
    private AlertMapper alertMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertMockMvc;

    private Alert alert;

    private Alert insertedAlert;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createEntity(EntityManager em) {
        Alert alert = new Alert()
            .alertRuleId(DEFAULT_ALERT_RULE_ID)
            .severity(DEFAULT_SEVERITY)
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .status(DEFAULT_STATUS)
            .triggeredDate(DEFAULT_TRIGGERED_DATE)
            .acknowledgedBy(DEFAULT_ACKNOWLEDGED_BY)
            .acknowledgedDate(DEFAULT_ACKNOWLEDGED_DATE)
            .resolvedBy(DEFAULT_RESOLVED_BY)
            .resolvedDate(DEFAULT_RESOLVED_DATE);
        // Add required entity
        AlertRule alertRule;
        if (TestUtil.findAll(em, AlertRule.class).isEmpty()) {
            alertRule = AlertRuleResourceIT.createEntity();
            em.persist(alertRule);
            em.flush();
        } else {
            alertRule = TestUtil.findAll(em, AlertRule.class).get(0);
        }
        alert.setAlertRule(alertRule);
        return alert;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Alert createUpdatedEntity(EntityManager em) {
        Alert updatedAlert = new Alert()
            .alertRuleId(UPDATED_ALERT_RULE_ID)
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);
        // Add required entity
        AlertRule alertRule;
        if (TestUtil.findAll(em, AlertRule.class).isEmpty()) {
            alertRule = AlertRuleResourceIT.createUpdatedEntity();
            em.persist(alertRule);
            em.flush();
        } else {
            alertRule = TestUtil.findAll(em, AlertRule.class).get(0);
        }
        updatedAlert.setAlertRule(alertRule);
        return updatedAlert;
    }

    @BeforeEach
    void initTest() {
        alert = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedAlert != null) {
            alertRepository.delete(insertedAlert);
            insertedAlert = null;
        }
    }

    @Test
    @Transactional
    void createAlert() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);
        var returnedAlertDTO = om.readValue(
            restAlertMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlertDTO.class
        );

        // Validate the Alert in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlert = alertMapper.toEntity(returnedAlertDTO);
        assertAlertUpdatableFieldsEquals(returnedAlert, getPersistedAlert(returnedAlert));

        insertedAlert = returnedAlert;
    }

    @Test
    @Transactional
    void createAlertWithExistingId() throws Exception {
        // Create the Alert with an existing ID
        alert.setId(1L);
        AlertDTO alertDTO = alertMapper.toDto(alert);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAlertRuleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alert.setAlertRuleId(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alert.setSeverity(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alert.setTitle(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alert.setStatus(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTriggeredDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alert.setTriggeredDate(null);

        // Create the Alert, which fails.
        AlertDTO alertDTO = alertMapper.toDto(alert);

        restAlertMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlerts() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].alertRuleId").value(hasItem(DEFAULT_ALERT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].triggeredDate").value(hasItem(DEFAULT_TRIGGERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].acknowledgedBy").value(hasItem(DEFAULT_ACKNOWLEDGED_BY)))
            .andExpect(jsonPath("$.[*].acknowledgedDate").value(hasItem(DEFAULT_ACKNOWLEDGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].resolvedBy").value(hasItem(DEFAULT_RESOLVED_BY)))
            .andExpect(jsonPath("$.[*].resolvedDate").value(hasItem(DEFAULT_RESOLVED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlert() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get the alert
        restAlertMockMvc
            .perform(get(ENTITY_API_URL_ID, alert.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alert.getId().intValue()))
            .andExpect(jsonPath("$.alertRuleId").value(DEFAULT_ALERT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.triggeredDate").value(DEFAULT_TRIGGERED_DATE.toString()))
            .andExpect(jsonPath("$.acknowledgedBy").value(DEFAULT_ACKNOWLEDGED_BY))
            .andExpect(jsonPath("$.acknowledgedDate").value(DEFAULT_ACKNOWLEDGED_DATE.toString()))
            .andExpect(jsonPath("$.resolvedBy").value(DEFAULT_RESOLVED_BY))
            .andExpect(jsonPath("$.resolvedDate").value(DEFAULT_RESOLVED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlertsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        Long id = alert.getId();

        defaultAlertFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlertFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlertFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId equals to
        defaultAlertFiltering("alertRuleId.equals=" + DEFAULT_ALERT_RULE_ID, "alertRuleId.equals=" + UPDATED_ALERT_RULE_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId in
        defaultAlertFiltering(
            "alertRuleId.in=" + DEFAULT_ALERT_RULE_ID + "," + UPDATED_ALERT_RULE_ID,
            "alertRuleId.in=" + UPDATED_ALERT_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId is not null
        defaultAlertFiltering("alertRuleId.specified=true", "alertRuleId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId is greater than or equal to
        defaultAlertFiltering(
            "alertRuleId.greaterThanOrEqual=" + DEFAULT_ALERT_RULE_ID,
            "alertRuleId.greaterThanOrEqual=" + UPDATED_ALERT_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId is less than or equal to
        defaultAlertFiltering(
            "alertRuleId.lessThanOrEqual=" + DEFAULT_ALERT_RULE_ID,
            "alertRuleId.lessThanOrEqual=" + SMALLER_ALERT_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId is less than
        defaultAlertFiltering("alertRuleId.lessThan=" + UPDATED_ALERT_RULE_ID, "alertRuleId.lessThan=" + DEFAULT_ALERT_RULE_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where alertRuleId is greater than
        defaultAlertFiltering("alertRuleId.greaterThan=" + SMALLER_ALERT_RULE_ID, "alertRuleId.greaterThan=" + DEFAULT_ALERT_RULE_ID);
    }

    @Test
    @Transactional
    void getAllAlertsBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where severity equals to
        defaultAlertFiltering("severity.equals=" + DEFAULT_SEVERITY, "severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllAlertsBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where severity in
        defaultAlertFiltering("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY, "severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllAlertsBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where severity is not null
        defaultAlertFiltering("severity.specified=true", "severity.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where title equals to
        defaultAlertFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlertsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where title in
        defaultAlertFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlertsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where title is not null
        defaultAlertFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where title contains
        defaultAlertFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllAlertsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where title does not contain
        defaultAlertFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityType equals to
        defaultAlertFiltering("entityType.equals=" + DEFAULT_ENTITY_TYPE, "entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityType in
        defaultAlertFiltering("entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE, "entityType.in=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityType is not null
        defaultAlertFiltering("entityType.specified=true", "entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByEntityTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityType contains
        defaultAlertFiltering("entityType.contains=" + DEFAULT_ENTITY_TYPE, "entityType.contains=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityType does not contain
        defaultAlertFiltering("entityType.doesNotContain=" + UPDATED_ENTITY_TYPE, "entityType.doesNotContain=" + DEFAULT_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId equals to
        defaultAlertFiltering("entityId.equals=" + DEFAULT_ENTITY_ID, "entityId.equals=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId in
        defaultAlertFiltering("entityId.in=" + DEFAULT_ENTITY_ID + "," + UPDATED_ENTITY_ID, "entityId.in=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId is not null
        defaultAlertFiltering("entityId.specified=true", "entityId.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId is greater than or equal to
        defaultAlertFiltering("entityId.greaterThanOrEqual=" + DEFAULT_ENTITY_ID, "entityId.greaterThanOrEqual=" + UPDATED_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId is less than or equal to
        defaultAlertFiltering("entityId.lessThanOrEqual=" + DEFAULT_ENTITY_ID, "entityId.lessThanOrEqual=" + SMALLER_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId is less than
        defaultAlertFiltering("entityId.lessThan=" + UPDATED_ENTITY_ID, "entityId.lessThan=" + DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where entityId is greater than
        defaultAlertFiltering("entityId.greaterThan=" + SMALLER_ENTITY_ID, "entityId.greaterThan=" + DEFAULT_ENTITY_ID);
    }

    @Test
    @Transactional
    void getAllAlertsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where status equals to
        defaultAlertFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlertsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where status in
        defaultAlertFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAlertsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where status is not null
        defaultAlertFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByTriggeredDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where triggeredDate equals to
        defaultAlertFiltering("triggeredDate.equals=" + DEFAULT_TRIGGERED_DATE, "triggeredDate.equals=" + UPDATED_TRIGGERED_DATE);
    }

    @Test
    @Transactional
    void getAllAlertsByTriggeredDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where triggeredDate in
        defaultAlertFiltering(
            "triggeredDate.in=" + DEFAULT_TRIGGERED_DATE + "," + UPDATED_TRIGGERED_DATE,
            "triggeredDate.in=" + UPDATED_TRIGGERED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlertsByTriggeredDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where triggeredDate is not null
        defaultAlertFiltering("triggeredDate.specified=true", "triggeredDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedBy equals to
        defaultAlertFiltering("acknowledgedBy.equals=" + DEFAULT_ACKNOWLEDGED_BY, "acknowledgedBy.equals=" + UPDATED_ACKNOWLEDGED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedBy in
        defaultAlertFiltering(
            "acknowledgedBy.in=" + DEFAULT_ACKNOWLEDGED_BY + "," + UPDATED_ACKNOWLEDGED_BY,
            "acknowledgedBy.in=" + UPDATED_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedBy is not null
        defaultAlertFiltering("acknowledgedBy.specified=true", "acknowledgedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedBy contains
        defaultAlertFiltering("acknowledgedBy.contains=" + DEFAULT_ACKNOWLEDGED_BY, "acknowledgedBy.contains=" + UPDATED_ACKNOWLEDGED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedBy does not contain
        defaultAlertFiltering(
            "acknowledgedBy.doesNotContain=" + UPDATED_ACKNOWLEDGED_BY,
            "acknowledgedBy.doesNotContain=" + DEFAULT_ACKNOWLEDGED_BY
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedDate equals to
        defaultAlertFiltering(
            "acknowledgedDate.equals=" + DEFAULT_ACKNOWLEDGED_DATE,
            "acknowledgedDate.equals=" + UPDATED_ACKNOWLEDGED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedDate in
        defaultAlertFiltering(
            "acknowledgedDate.in=" + DEFAULT_ACKNOWLEDGED_DATE + "," + UPDATED_ACKNOWLEDGED_DATE,
            "acknowledgedDate.in=" + UPDATED_ACKNOWLEDGED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlertsByAcknowledgedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where acknowledgedDate is not null
        defaultAlertFiltering("acknowledgedDate.specified=true", "acknowledgedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedBy equals to
        defaultAlertFiltering("resolvedBy.equals=" + DEFAULT_RESOLVED_BY, "resolvedBy.equals=" + UPDATED_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedBy in
        defaultAlertFiltering("resolvedBy.in=" + DEFAULT_RESOLVED_BY + "," + UPDATED_RESOLVED_BY, "resolvedBy.in=" + UPDATED_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedBy is not null
        defaultAlertFiltering("resolvedBy.specified=true", "resolvedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedBy contains
        defaultAlertFiltering("resolvedBy.contains=" + DEFAULT_RESOLVED_BY, "resolvedBy.contains=" + UPDATED_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedBy does not contain
        defaultAlertFiltering("resolvedBy.doesNotContain=" + UPDATED_RESOLVED_BY, "resolvedBy.doesNotContain=" + DEFAULT_RESOLVED_BY);
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedDate equals to
        defaultAlertFiltering("resolvedDate.equals=" + DEFAULT_RESOLVED_DATE, "resolvedDate.equals=" + UPDATED_RESOLVED_DATE);
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedDate in
        defaultAlertFiltering(
            "resolvedDate.in=" + DEFAULT_RESOLVED_DATE + "," + UPDATED_RESOLVED_DATE,
            "resolvedDate.in=" + UPDATED_RESOLVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlertsByResolvedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        // Get all the alertList where resolvedDate is not null
        defaultAlertFiltering("resolvedDate.specified=true", "resolvedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertsByAlertRuleIsEqualToSomething() throws Exception {
        AlertRule alertRule;
        if (TestUtil.findAll(em, AlertRule.class).isEmpty()) {
            alertRepository.saveAndFlush(alert);
            alertRule = AlertRuleResourceIT.createEntity();
        } else {
            alertRule = TestUtil.findAll(em, AlertRule.class).get(0);
        }
        em.persist(alertRule);
        em.flush();
        alert.setAlertRule(alertRule);
        alertRepository.saveAndFlush(alert);
        Long alertRuleId = alertRule.getId();
        // Get all the alertList where alertRule equals to alertRuleId
        defaultAlertShouldBeFound("alertRuleId.equals=" + alertRuleId);

        // Get all the alertList where alertRule equals to (alertRuleId + 1)
        defaultAlertShouldNotBeFound("alertRuleId.equals=" + (alertRuleId + 1));
    }

    private void defaultAlertFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlertShouldBeFound(shouldBeFound);
        defaultAlertShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlertShouldBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alert.getId().intValue())))
            .andExpect(jsonPath("$.[*].alertRuleId").value(hasItem(DEFAULT_ALERT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].triggeredDate").value(hasItem(DEFAULT_TRIGGERED_DATE.toString())))
            .andExpect(jsonPath("$.[*].acknowledgedBy").value(hasItem(DEFAULT_ACKNOWLEDGED_BY)))
            .andExpect(jsonPath("$.[*].acknowledgedDate").value(hasItem(DEFAULT_ACKNOWLEDGED_DATE.toString())))
            .andExpect(jsonPath("$.[*].resolvedBy").value(hasItem(DEFAULT_RESOLVED_BY)))
            .andExpect(jsonPath("$.[*].resolvedDate").value(hasItem(DEFAULT_RESOLVED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlertShouldNotBeFound(String filter) throws Exception {
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlertMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlert() throws Exception {
        // Get the alert
        restAlertMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlert() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alert
        Alert updatedAlert = alertRepository.findById(alert.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlert are not directly saved in db
        em.detach(updatedAlert);
        updatedAlert
            .alertRuleId(UPDATED_ALERT_RULE_ID)
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);
        AlertDTO alertDTO = alertMapper.toDto(updatedAlert);

        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlertToMatchAllProperties(updatedAlert);
    }

    @Test
    @Transactional
    void putNonExistingAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert
            .severity(UPDATED_SEVERITY)
            .message(UPDATED_MESSAGE)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAlert, alert), getPersistedAlert(alert));
    }

    @Test
    @Transactional
    void fullUpdateAlertWithPatch() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alert using partial update
        Alert partialUpdatedAlert = new Alert();
        partialUpdatedAlert.setId(alert.getId());

        partialUpdatedAlert
            .alertRuleId(UPDATED_ALERT_RULE_ID)
            .severity(UPDATED_SEVERITY)
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .status(UPDATED_STATUS)
            .triggeredDate(UPDATED_TRIGGERED_DATE)
            .acknowledgedBy(UPDATED_ACKNOWLEDGED_BY)
            .acknowledgedDate(UPDATED_ACKNOWLEDGED_DATE)
            .resolvedBy(UPDATED_RESOLVED_BY)
            .resolvedDate(UPDATED_RESOLVED_DATE);

        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlert.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlert))
            )
            .andExpect(status().isOk());

        // Validate the Alert in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertUpdatableFieldsEquals(partialUpdatedAlert, getPersistedAlert(partialUpdatedAlert));
    }

    @Test
    @Transactional
    void patchNonExistingAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlert() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alert.setId(longCount.incrementAndGet());

        // Create the Alert
        AlertDTO alertDTO = alertMapper.toDto(alert);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alertDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Alert in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlert() throws Exception {
        // Initialize the database
        insertedAlert = alertRepository.saveAndFlush(alert);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alert
        restAlertMockMvc
            .perform(delete(ENTITY_API_URL_ID, alert.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alertRepository.count();
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

    protected Alert getPersistedAlert(Alert alert) {
        return alertRepository.findById(alert.getId()).orElseThrow();
    }

    protected void assertPersistedAlertToMatchAllProperties(Alert expectedAlert) {
        assertAlertAllPropertiesEquals(expectedAlert, getPersistedAlert(expectedAlert));
    }

    protected void assertPersistedAlertToMatchUpdatableProperties(Alert expectedAlert) {
        assertAlertAllUpdatablePropertiesEquals(expectedAlert, getPersistedAlert(expectedAlert));
    }
}
