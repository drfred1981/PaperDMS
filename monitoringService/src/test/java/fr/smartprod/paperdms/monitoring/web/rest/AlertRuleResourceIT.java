package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.AlertRuleAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.AlertRule;
import fr.smartprod.paperdms.monitoring.domain.enumeration.AlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import fr.smartprod.paperdms.monitoring.repository.AlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.AlertRuleMapper;
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
 * Integration tests for the {@link AlertRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AlertRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final AlertType DEFAULT_ALERT_TYPE = AlertType.DOCUMENT_EXPIRATION;
    private static final AlertType UPDATED_ALERT_TYPE = AlertType.STORAGE_QUOTA;

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final Severity DEFAULT_SEVERITY = Severity.LOW;
    private static final Severity UPDATED_SEVERITY = Severity.MEDIUM;

    private static final String DEFAULT_RECIPIENTS = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_TRIGGER_COUNT = 1;
    private static final Integer UPDATED_TRIGGER_COUNT = 2;
    private static final Integer SMALLER_TRIGGER_COUNT = 1 - 1;

    private static final Instant DEFAULT_LAST_TRIGGERED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_TRIGGERED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/alert-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AlertRuleRepository alertRuleRepository;

    @Autowired
    private AlertRuleMapper alertRuleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAlertRuleMockMvc;

    private AlertRule alertRule;

    private AlertRule insertedAlertRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertRule createEntity() {
        return new AlertRule()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .alertType(DEFAULT_ALERT_TYPE)
            .conditions(DEFAULT_CONDITIONS)
            .severity(DEFAULT_SEVERITY)
            .recipients(DEFAULT_RECIPIENTS)
            .isActive(DEFAULT_IS_ACTIVE)
            .triggerCount(DEFAULT_TRIGGER_COUNT)
            .lastTriggered(DEFAULT_LAST_TRIGGERED)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AlertRule createUpdatedEntity() {
        return new AlertRule()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE)
            .conditions(UPDATED_CONDITIONS)
            .severity(UPDATED_SEVERITY)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .triggerCount(UPDATED_TRIGGER_COUNT)
            .lastTriggered(UPDATED_LAST_TRIGGERED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        alertRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAlertRule != null) {
            alertRuleRepository.delete(insertedAlertRule);
            insertedAlertRule = null;
        }
    }

    @Test
    @Transactional
    void createAlertRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);
        var returnedAlertRuleDTO = om.readValue(
            restAlertRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AlertRuleDTO.class
        );

        // Validate the AlertRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAlertRule = alertRuleMapper.toEntity(returnedAlertRuleDTO);
        assertAlertRuleUpdatableFieldsEquals(returnedAlertRule, getPersistedAlertRule(returnedAlertRule));

        insertedAlertRule = returnedAlertRule;
    }

    @Test
    @Transactional
    void createAlertRuleWithExistingId() throws Exception {
        // Create the AlertRule with an existing ID
        alertRule.setId(1L);
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setName(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlertTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setAlertType(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setSeverity(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setIsActive(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setCreatedBy(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        alertRule.setCreatedDate(null);

        // Create the AlertRule, which fails.
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        restAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAlertRules() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].recipients").value(hasItem(DEFAULT_RECIPIENTS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].triggerCount").value(hasItem(DEFAULT_TRIGGER_COUNT)))
            .andExpect(jsonPath("$.[*].lastTriggered").value(hasItem(DEFAULT_LAST_TRIGGERED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAlertRule() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get the alertRule
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, alertRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(alertRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE.toString()))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.severity").value(DEFAULT_SEVERITY.toString()))
            .andExpect(jsonPath("$.recipients").value(DEFAULT_RECIPIENTS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.triggerCount").value(DEFAULT_TRIGGER_COUNT))
            .andExpect(jsonPath("$.lastTriggered").value(DEFAULT_LAST_TRIGGERED.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAlertRulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        Long id = alertRule.getId();

        defaultAlertRuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAlertRuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAlertRuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAlertRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where name equals to
        defaultAlertRuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlertRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where name in
        defaultAlertRuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlertRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where name is not null
        defaultAlertRuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where name contains
        defaultAlertRuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAlertRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where name does not contain
        defaultAlertRuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAlertRulesByAlertTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where alertType equals to
        defaultAlertRuleFiltering("alertType.equals=" + DEFAULT_ALERT_TYPE, "alertType.equals=" + UPDATED_ALERT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertRulesByAlertTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where alertType in
        defaultAlertRuleFiltering("alertType.in=" + DEFAULT_ALERT_TYPE + "," + UPDATED_ALERT_TYPE, "alertType.in=" + UPDATED_ALERT_TYPE);
    }

    @Test
    @Transactional
    void getAllAlertRulesByAlertTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where alertType is not null
        defaultAlertRuleFiltering("alertType.specified=true", "alertType.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where severity equals to
        defaultAlertRuleFiltering("severity.equals=" + DEFAULT_SEVERITY, "severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllAlertRulesBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where severity in
        defaultAlertRuleFiltering("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY, "severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllAlertRulesBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where severity is not null
        defaultAlertRuleFiltering("severity.specified=true", "severity.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where isActive equals to
        defaultAlertRuleFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAlertRulesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where isActive in
        defaultAlertRuleFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllAlertRulesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where isActive is not null
        defaultAlertRuleFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount equals to
        defaultAlertRuleFiltering("triggerCount.equals=" + DEFAULT_TRIGGER_COUNT, "triggerCount.equals=" + UPDATED_TRIGGER_COUNT);
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount in
        defaultAlertRuleFiltering(
            "triggerCount.in=" + DEFAULT_TRIGGER_COUNT + "," + UPDATED_TRIGGER_COUNT,
            "triggerCount.in=" + UPDATED_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount is not null
        defaultAlertRuleFiltering("triggerCount.specified=true", "triggerCount.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount is greater than or equal to
        defaultAlertRuleFiltering(
            "triggerCount.greaterThanOrEqual=" + DEFAULT_TRIGGER_COUNT,
            "triggerCount.greaterThanOrEqual=" + UPDATED_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount is less than or equal to
        defaultAlertRuleFiltering(
            "triggerCount.lessThanOrEqual=" + DEFAULT_TRIGGER_COUNT,
            "triggerCount.lessThanOrEqual=" + SMALLER_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount is less than
        defaultAlertRuleFiltering("triggerCount.lessThan=" + UPDATED_TRIGGER_COUNT, "triggerCount.lessThan=" + DEFAULT_TRIGGER_COUNT);
    }

    @Test
    @Transactional
    void getAllAlertRulesByTriggerCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where triggerCount is greater than
        defaultAlertRuleFiltering("triggerCount.greaterThan=" + SMALLER_TRIGGER_COUNT, "triggerCount.greaterThan=" + DEFAULT_TRIGGER_COUNT);
    }

    @Test
    @Transactional
    void getAllAlertRulesByLastTriggeredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where lastTriggered equals to
        defaultAlertRuleFiltering("lastTriggered.equals=" + DEFAULT_LAST_TRIGGERED, "lastTriggered.equals=" + UPDATED_LAST_TRIGGERED);
    }

    @Test
    @Transactional
    void getAllAlertRulesByLastTriggeredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where lastTriggered in
        defaultAlertRuleFiltering(
            "lastTriggered.in=" + DEFAULT_LAST_TRIGGERED + "," + UPDATED_LAST_TRIGGERED,
            "lastTriggered.in=" + UPDATED_LAST_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllAlertRulesByLastTriggeredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where lastTriggered is not null
        defaultAlertRuleFiltering("lastTriggered.specified=true", "lastTriggered.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdBy equals to
        defaultAlertRuleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdBy in
        defaultAlertRuleFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdBy is not null
        defaultAlertRuleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdBy contains
        defaultAlertRuleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdBy does not contain
        defaultAlertRuleFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdDate equals to
        defaultAlertRuleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdDate in
        defaultAlertRuleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAlertRulesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        // Get all the alertRuleList where createdDate is not null
        defaultAlertRuleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultAlertRuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAlertRuleShouldBeFound(shouldBeFound);
        defaultAlertRuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAlertRuleShouldBeFound(String filter) throws Exception {
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(alertRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].severity").value(hasItem(DEFAULT_SEVERITY.toString())))
            .andExpect(jsonPath("$.[*].recipients").value(hasItem(DEFAULT_RECIPIENTS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].triggerCount").value(hasItem(DEFAULT_TRIGGER_COUNT)))
            .andExpect(jsonPath("$.[*].lastTriggered").value(hasItem(DEFAULT_LAST_TRIGGERED.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAlertRuleShouldNotBeFound(String filter) throws Exception {
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAlertRule() throws Exception {
        // Get the alertRule
        restAlertRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAlertRule() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertRule
        AlertRule updatedAlertRule = alertRuleRepository.findById(alertRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAlertRule are not directly saved in db
        em.detach(updatedAlertRule);
        updatedAlertRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE)
            .conditions(UPDATED_CONDITIONS)
            .severity(UPDATED_SEVERITY)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .triggerCount(UPDATED_TRIGGER_COUNT)
            .lastTriggered(UPDATED_LAST_TRIGGERED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(updatedAlertRule);

        restAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAlertRuleToMatchAllProperties(updatedAlertRule);
    }

    @Test
    @Transactional
    void putNonExistingAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, alertRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(alertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAlertRuleWithPatch() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertRule using partial update
        AlertRule partialUpdatedAlertRule = new AlertRule();
        partialUpdatedAlertRule.setId(alertRule.getId());

        partialUpdatedAlertRule
            .alertType(UPDATED_ALERT_TYPE)
            .severity(UPDATED_SEVERITY)
            .isActive(UPDATED_IS_ACTIVE)
            .triggerCount(UPDATED_TRIGGER_COUNT)
            .createdDate(UPDATED_CREATED_DATE);

        restAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlertRule))
            )
            .andExpect(status().isOk());

        // Validate the AlertRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAlertRule, alertRule),
            getPersistedAlertRule(alertRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateAlertRuleWithPatch() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the alertRule using partial update
        AlertRule partialUpdatedAlertRule = new AlertRule();
        partialUpdatedAlertRule.setId(alertRule.getId());

        partialUpdatedAlertRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE)
            .conditions(UPDATED_CONDITIONS)
            .severity(UPDATED_SEVERITY)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .triggerCount(UPDATED_TRIGGER_COUNT)
            .lastTriggered(UPDATED_LAST_TRIGGERED)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAlertRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAlertRule))
            )
            .andExpect(status().isOk());

        // Validate the AlertRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAlertRuleUpdatableFieldsEquals(partialUpdatedAlertRule, getPersistedAlertRule(partialUpdatedAlertRule));
    }

    @Test
    @Transactional
    void patchNonExistingAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, alertRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(alertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        alertRule.setId(longCount.incrementAndGet());

        // Create the AlertRule
        AlertRuleDTO alertRuleDTO = alertRuleMapper.toDto(alertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAlertRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(alertRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAlertRule() throws Exception {
        // Initialize the database
        insertedAlertRule = alertRuleRepository.saveAndFlush(alertRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the alertRule
        restAlertRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, alertRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return alertRuleRepository.count();
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

    protected AlertRule getPersistedAlertRule(AlertRule alertRule) {
        return alertRuleRepository.findById(alertRule.getId()).orElseThrow();
    }

    protected void assertPersistedAlertRuleToMatchAllProperties(AlertRule expectedAlertRule) {
        assertAlertRuleAllPropertiesEquals(expectedAlertRule, getPersistedAlertRule(expectedAlertRule));
    }

    protected void assertPersistedAlertRuleToMatchUpdatableProperties(AlertRule expectedAlertRule) {
        assertAlertRuleAllUpdatablePropertiesEquals(expectedAlertRule, getPersistedAlertRule(expectedAlertRule));
    }
}
