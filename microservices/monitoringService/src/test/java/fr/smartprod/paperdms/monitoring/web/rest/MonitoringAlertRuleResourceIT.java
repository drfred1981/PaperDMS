package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRuleAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringAlertRule;
import fr.smartprod.paperdms.monitoring.domain.enumeration.MonitoringAlertType;
import fr.smartprod.paperdms.monitoring.domain.enumeration.Severity;
import fr.smartprod.paperdms.monitoring.repository.MonitoringAlertRuleRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringAlertRuleDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringAlertRuleMapper;
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
 * Integration tests for the {@link MonitoringAlertRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringAlertRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final MonitoringAlertType DEFAULT_ALERT_TYPE = MonitoringAlertType.DOCUMENT_EXPIRATION;
    private static final MonitoringAlertType UPDATED_ALERT_TYPE = MonitoringAlertType.STORAGE_QUOTA;

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

    private static final String ENTITY_API_URL = "/api/monitoring-alert-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringAlertRuleRepository monitoringAlertRuleRepository;

    @Autowired
    private MonitoringAlertRuleMapper monitoringAlertRuleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringAlertRuleMockMvc;

    private MonitoringAlertRule monitoringAlertRule;

    private MonitoringAlertRule insertedMonitoringAlertRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringAlertRule createEntity() {
        return new MonitoringAlertRule()
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
    public static MonitoringAlertRule createUpdatedEntity() {
        return new MonitoringAlertRule()
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
        monitoringAlertRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringAlertRule != null) {
            monitoringAlertRuleRepository.delete(insertedMonitoringAlertRule);
            insertedMonitoringAlertRule = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);
        var returnedMonitoringAlertRuleDTO = om.readValue(
            restMonitoringAlertRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringAlertRuleDTO.class
        );

        // Validate the MonitoringAlertRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringAlertRule = monitoringAlertRuleMapper.toEntity(returnedMonitoringAlertRuleDTO);
        assertMonitoringAlertRuleUpdatableFieldsEquals(
            returnedMonitoringAlertRule,
            getPersistedMonitoringAlertRule(returnedMonitoringAlertRule)
        );

        insertedMonitoringAlertRule = returnedMonitoringAlertRule;
    }

    @Test
    @Transactional
    void createMonitoringAlertRuleWithExistingId() throws Exception {
        // Create the MonitoringAlertRule with an existing ID
        monitoringAlertRule.setId(1L);
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setName(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlertTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setAlertType(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSeverityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setSeverity(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setIsActive(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setCreatedBy(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringAlertRule.setCreatedDate(null);

        // Create the MonitoringAlertRule, which fails.
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRules() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringAlertRule.getId().intValue())))
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
    void getMonitoringAlertRule() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get the monitoringAlertRule
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringAlertRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringAlertRule.getId().intValue()))
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
    void getMonitoringAlertRulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        Long id = monitoringAlertRule.getId();

        defaultMonitoringAlertRuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringAlertRuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringAlertRuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where name equals to
        defaultMonitoringAlertRuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where name in
        defaultMonitoringAlertRuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where name is not null
        defaultMonitoringAlertRuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where name contains
        defaultMonitoringAlertRuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where name does not contain
        defaultMonitoringAlertRuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByAlertTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where alertType equals to
        defaultMonitoringAlertRuleFiltering("alertType.equals=" + DEFAULT_ALERT_TYPE, "alertType.equals=" + UPDATED_ALERT_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByAlertTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where alertType in
        defaultMonitoringAlertRuleFiltering(
            "alertType.in=" + DEFAULT_ALERT_TYPE + "," + UPDATED_ALERT_TYPE,
            "alertType.in=" + UPDATED_ALERT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByAlertTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where alertType is not null
        defaultMonitoringAlertRuleFiltering("alertType.specified=true", "alertType.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesBySeverityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where severity equals to
        defaultMonitoringAlertRuleFiltering("severity.equals=" + DEFAULT_SEVERITY, "severity.equals=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesBySeverityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where severity in
        defaultMonitoringAlertRuleFiltering("severity.in=" + DEFAULT_SEVERITY + "," + UPDATED_SEVERITY, "severity.in=" + UPDATED_SEVERITY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesBySeverityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where severity is not null
        defaultMonitoringAlertRuleFiltering("severity.specified=true", "severity.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where isActive equals to
        defaultMonitoringAlertRuleFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where isActive in
        defaultMonitoringAlertRuleFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where isActive is not null
        defaultMonitoringAlertRuleFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount equals to
        defaultMonitoringAlertRuleFiltering("triggerCount.equals=" + DEFAULT_TRIGGER_COUNT, "triggerCount.equals=" + UPDATED_TRIGGER_COUNT);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount in
        defaultMonitoringAlertRuleFiltering(
            "triggerCount.in=" + DEFAULT_TRIGGER_COUNT + "," + UPDATED_TRIGGER_COUNT,
            "triggerCount.in=" + UPDATED_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount is not null
        defaultMonitoringAlertRuleFiltering("triggerCount.specified=true", "triggerCount.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount is greater than or equal to
        defaultMonitoringAlertRuleFiltering(
            "triggerCount.greaterThanOrEqual=" + DEFAULT_TRIGGER_COUNT,
            "triggerCount.greaterThanOrEqual=" + UPDATED_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount is less than or equal to
        defaultMonitoringAlertRuleFiltering(
            "triggerCount.lessThanOrEqual=" + DEFAULT_TRIGGER_COUNT,
            "triggerCount.lessThanOrEqual=" + SMALLER_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount is less than
        defaultMonitoringAlertRuleFiltering(
            "triggerCount.lessThan=" + UPDATED_TRIGGER_COUNT,
            "triggerCount.lessThan=" + DEFAULT_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByTriggerCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where triggerCount is greater than
        defaultMonitoringAlertRuleFiltering(
            "triggerCount.greaterThan=" + SMALLER_TRIGGER_COUNT,
            "triggerCount.greaterThan=" + DEFAULT_TRIGGER_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByLastTriggeredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where lastTriggered equals to
        defaultMonitoringAlertRuleFiltering(
            "lastTriggered.equals=" + DEFAULT_LAST_TRIGGERED,
            "lastTriggered.equals=" + UPDATED_LAST_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByLastTriggeredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where lastTriggered in
        defaultMonitoringAlertRuleFiltering(
            "lastTriggered.in=" + DEFAULT_LAST_TRIGGERED + "," + UPDATED_LAST_TRIGGERED,
            "lastTriggered.in=" + UPDATED_LAST_TRIGGERED
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByLastTriggeredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where lastTriggered is not null
        defaultMonitoringAlertRuleFiltering("lastTriggered.specified=true", "lastTriggered.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdBy equals to
        defaultMonitoringAlertRuleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdBy in
        defaultMonitoringAlertRuleFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdBy is not null
        defaultMonitoringAlertRuleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdBy contains
        defaultMonitoringAlertRuleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdBy does not contain
        defaultMonitoringAlertRuleFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdDate equals to
        defaultMonitoringAlertRuleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdDate in
        defaultMonitoringAlertRuleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringAlertRulesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        // Get all the monitoringAlertRuleList where createdDate is not null
        defaultMonitoringAlertRuleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMonitoringAlertRuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringAlertRuleShouldBeFound(shouldBeFound);
        defaultMonitoringAlertRuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringAlertRuleShouldBeFound(String filter) throws Exception {
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringAlertRule.getId().intValue())))
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
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringAlertRuleShouldNotBeFound(String filter) throws Exception {
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringAlertRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringAlertRule() throws Exception {
        // Get the monitoringAlertRule
        restMonitoringAlertRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringAlertRule() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlertRule
        MonitoringAlertRule updatedMonitoringAlertRule = monitoringAlertRuleRepository.findById(monitoringAlertRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringAlertRule are not directly saved in db
        em.detach(updatedMonitoringAlertRule);
        updatedMonitoringAlertRule
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
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(updatedMonitoringAlertRule);

        restMonitoringAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringAlertRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringAlertRuleToMatchAllProperties(updatedMonitoringAlertRule);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringAlertRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringAlertRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringAlertRuleWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlertRule using partial update
        MonitoringAlertRule partialUpdatedMonitoringAlertRule = new MonitoringAlertRule();
        partialUpdatedMonitoringAlertRule.setId(monitoringAlertRule.getId());

        partialUpdatedMonitoringAlertRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .alertType(UPDATED_ALERT_TYPE)
            .conditions(UPDATED_CONDITIONS)
            .isActive(UPDATED_IS_ACTIVE)
            .triggerCount(UPDATED_TRIGGER_COUNT)
            .createdDate(UPDATED_CREATED_DATE);

        restMonitoringAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringAlertRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringAlertRule))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlertRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringAlertRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringAlertRule, monitoringAlertRule),
            getPersistedMonitoringAlertRule(monitoringAlertRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringAlertRuleWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringAlertRule using partial update
        MonitoringAlertRule partialUpdatedMonitoringAlertRule = new MonitoringAlertRule();
        partialUpdatedMonitoringAlertRule.setId(monitoringAlertRule.getId());

        partialUpdatedMonitoringAlertRule
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

        restMonitoringAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringAlertRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringAlertRule))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringAlertRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringAlertRuleUpdatableFieldsEquals(
            partialUpdatedMonitoringAlertRule,
            getPersistedMonitoringAlertRule(partialUpdatedMonitoringAlertRule)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringAlertRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringAlertRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringAlertRule.setId(longCount.incrementAndGet());

        // Create the MonitoringAlertRule
        MonitoringAlertRuleDTO monitoringAlertRuleDTO = monitoringAlertRuleMapper.toDto(monitoringAlertRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringAlertRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitoringAlertRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringAlertRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringAlertRule() throws Exception {
        // Initialize the database
        insertedMonitoringAlertRule = monitoringAlertRuleRepository.saveAndFlush(monitoringAlertRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringAlertRule
        restMonitoringAlertRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringAlertRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringAlertRuleRepository.count();
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

    protected MonitoringAlertRule getPersistedMonitoringAlertRule(MonitoringAlertRule monitoringAlertRule) {
        return monitoringAlertRuleRepository.findById(monitoringAlertRule.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringAlertRuleToMatchAllProperties(MonitoringAlertRule expectedMonitoringAlertRule) {
        assertMonitoringAlertRuleAllPropertiesEquals(
            expectedMonitoringAlertRule,
            getPersistedMonitoringAlertRule(expectedMonitoringAlertRule)
        );
    }

    protected void assertPersistedMonitoringAlertRuleToMatchUpdatableProperties(MonitoringAlertRule expectedMonitoringAlertRule) {
        assertMonitoringAlertRuleAllUpdatablePropertiesEquals(
            expectedMonitoringAlertRule,
            getPersistedMonitoringAlertRule(expectedMonitoringAlertRule)
        );
    }
}
