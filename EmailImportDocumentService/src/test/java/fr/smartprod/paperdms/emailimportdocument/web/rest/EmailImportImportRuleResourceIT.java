package fr.smartprod.paperdms.emailimportdocument.web.rest;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRuleAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimportdocument.IntegrationTest;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportImportRuleRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportImportRuleMapper;
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
 * Integration tests for the {@link EmailImportImportRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailImportImportRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_CONDITIONS = "AAAAAAAAAA";
    private static final String UPDATED_CONDITIONS = "BBBBBBBBBB";

    private static final String DEFAULT_ACTIONS = "AAAAAAAAAA";
    private static final String UPDATED_ACTIONS = "BBBBBBBBBB";

    private static final String DEFAULT_NOTIFY_USERS = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFY_USERS = "BBBBBBBBBB";

    private static final Integer DEFAULT_MATCH_COUNT = 1;
    private static final Integer UPDATED_MATCH_COUNT = 2;
    private static final Integer SMALLER_MATCH_COUNT = 1 - 1;

    private static final Instant DEFAULT_LAST_MATCH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MATCH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/email-import-import-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailImportImportRuleRepository emailImportImportRuleRepository;

    @Autowired
    private EmailImportImportRuleMapper emailImportImportRuleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailImportImportRuleMockMvc;

    private EmailImportImportRule emailImportImportRule;

    private EmailImportImportRule insertedEmailImportImportRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportImportRule createEntity() {
        return new EmailImportImportRule()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .conditions(DEFAULT_CONDITIONS)
            .actions(DEFAULT_ACTIONS)
            .notifyUsers(DEFAULT_NOTIFY_USERS)
            .matchCount(DEFAULT_MATCH_COUNT)
            .lastMatchDate(DEFAULT_LAST_MATCH_DATE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportImportRule createUpdatedEntity() {
        return new EmailImportImportRule()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        emailImportImportRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailImportImportRule != null) {
            emailImportImportRuleRepository.delete(insertedEmailImportImportRule);
            insertedEmailImportImportRule = null;
        }
    }

    @Test
    @Transactional
    void createEmailImportImportRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);
        var returnedEmailImportImportRuleDTO = om.readValue(
            restEmailImportImportRuleMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailImportImportRuleDTO.class
        );

        // Validate the EmailImportImportRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailImportImportRule = emailImportImportRuleMapper.toEntity(returnedEmailImportImportRuleDTO);
        assertEmailImportImportRuleUpdatableFieldsEquals(
            returnedEmailImportImportRule,
            getPersistedEmailImportImportRule(returnedEmailImportImportRule)
        );

        insertedEmailImportImportRule = returnedEmailImportImportRule;
    }

    @Test
    @Transactional
    void createEmailImportImportRuleWithExistingId() throws Exception {
        // Create the EmailImportImportRule with an existing ID
        emailImportImportRule.setId(1L);
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportRule.setName(null);

        // Create the EmailImportImportRule, which fails.
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportRule.setPriority(null);

        // Create the EmailImportImportRule, which fails.
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportRule.setIsActive(null);

        // Create the EmailImportImportRule, which fails.
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportRule.setCreatedBy(null);

        // Create the EmailImportImportRule, which fails.
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportImportRule.setCreatedDate(null);

        // Create the EmailImportImportRule, which fails.
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRules() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportImportRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].notifyUsers").value(hasItem(DEFAULT_NOTIFY_USERS)))
            .andExpect(jsonPath("$.[*].matchCount").value(hasItem(DEFAULT_MATCH_COUNT)))
            .andExpect(jsonPath("$.[*].lastMatchDate").value(hasItem(DEFAULT_LAST_MATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getEmailImportImportRule() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get the emailImportImportRule
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, emailImportImportRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailImportImportRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.actions").value(DEFAULT_ACTIONS))
            .andExpect(jsonPath("$.notifyUsers").value(DEFAULT_NOTIFY_USERS))
            .andExpect(jsonPath("$.matchCount").value(DEFAULT_MATCH_COUNT))
            .andExpect(jsonPath("$.lastMatchDate").value(DEFAULT_LAST_MATCH_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getEmailImportImportRulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        Long id = emailImportImportRule.getId();

        defaultEmailImportImportRuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmailImportImportRuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmailImportImportRuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where name equals to
        defaultEmailImportImportRuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where name in
        defaultEmailImportImportRuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where name is not null
        defaultEmailImportImportRuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where name contains
        defaultEmailImportImportRuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where name does not contain
        defaultEmailImportImportRuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority equals to
        defaultEmailImportImportRuleFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority in
        defaultEmailImportImportRuleFiltering(
            "priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY,
            "priority.in=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority is not null
        defaultEmailImportImportRuleFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority is greater than or equal to
        defaultEmailImportImportRuleFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority is less than or equal to
        defaultEmailImportImportRuleFiltering(
            "priority.lessThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.lessThanOrEqual=" + SMALLER_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority is less than
        defaultEmailImportImportRuleFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where priority is greater than
        defaultEmailImportImportRuleFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where isActive equals to
        defaultEmailImportImportRuleFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where isActive in
        defaultEmailImportImportRuleFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where isActive is not null
        defaultEmailImportImportRuleFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount equals to
        defaultEmailImportImportRuleFiltering("matchCount.equals=" + DEFAULT_MATCH_COUNT, "matchCount.equals=" + UPDATED_MATCH_COUNT);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount in
        defaultEmailImportImportRuleFiltering(
            "matchCount.in=" + DEFAULT_MATCH_COUNT + "," + UPDATED_MATCH_COUNT,
            "matchCount.in=" + UPDATED_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount is not null
        defaultEmailImportImportRuleFiltering("matchCount.specified=true", "matchCount.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount is greater than or equal to
        defaultEmailImportImportRuleFiltering(
            "matchCount.greaterThanOrEqual=" + DEFAULT_MATCH_COUNT,
            "matchCount.greaterThanOrEqual=" + UPDATED_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount is less than or equal to
        defaultEmailImportImportRuleFiltering(
            "matchCount.lessThanOrEqual=" + DEFAULT_MATCH_COUNT,
            "matchCount.lessThanOrEqual=" + SMALLER_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount is less than
        defaultEmailImportImportRuleFiltering("matchCount.lessThan=" + UPDATED_MATCH_COUNT, "matchCount.lessThan=" + DEFAULT_MATCH_COUNT);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByMatchCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where matchCount is greater than
        defaultEmailImportImportRuleFiltering(
            "matchCount.greaterThan=" + SMALLER_MATCH_COUNT,
            "matchCount.greaterThan=" + DEFAULT_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastMatchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastMatchDate equals to
        defaultEmailImportImportRuleFiltering(
            "lastMatchDate.equals=" + DEFAULT_LAST_MATCH_DATE,
            "lastMatchDate.equals=" + UPDATED_LAST_MATCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastMatchDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastMatchDate in
        defaultEmailImportImportRuleFiltering(
            "lastMatchDate.in=" + DEFAULT_LAST_MATCH_DATE + "," + UPDATED_LAST_MATCH_DATE,
            "lastMatchDate.in=" + UPDATED_LAST_MATCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastMatchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastMatchDate is not null
        defaultEmailImportImportRuleFiltering("lastMatchDate.specified=true", "lastMatchDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdBy equals to
        defaultEmailImportImportRuleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdBy in
        defaultEmailImportImportRuleFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdBy is not null
        defaultEmailImportImportRuleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdBy contains
        defaultEmailImportImportRuleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdBy does not contain
        defaultEmailImportImportRuleFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdDate equals to
        defaultEmailImportImportRuleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdDate in
        defaultEmailImportImportRuleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where createdDate is not null
        defaultEmailImportImportRuleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastModifiedDate equals to
        defaultEmailImportImportRuleFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastModifiedDate in
        defaultEmailImportImportRuleFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportImportRulesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        // Get all the emailImportImportRuleList where lastModifiedDate is not null
        defaultEmailImportImportRuleFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultEmailImportImportRuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmailImportImportRuleShouldBeFound(shouldBeFound);
        defaultEmailImportImportRuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailImportImportRuleShouldBeFound(String filter) throws Exception {
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportImportRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].notifyUsers").value(hasItem(DEFAULT_NOTIFY_USERS)))
            .andExpect(jsonPath("$.[*].matchCount").value(hasItem(DEFAULT_MATCH_COUNT)))
            .andExpect(jsonPath("$.[*].lastMatchDate").value(hasItem(DEFAULT_LAST_MATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailImportImportRuleShouldNotBeFound(String filter) throws Exception {
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailImportImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailImportImportRule() throws Exception {
        // Get the emailImportImportRule
        restEmailImportImportRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailImportImportRule() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportRule
        EmailImportImportRule updatedEmailImportImportRule = emailImportImportRuleRepository
            .findById(emailImportImportRule.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEmailImportImportRule are not directly saved in db
        em.detach(updatedEmailImportImportRule);
        updatedEmailImportImportRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(updatedEmailImportImportRule);

        restEmailImportImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportImportRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailImportImportRuleToMatchAllProperties(updatedEmailImportImportRule);
    }

    @Test
    @Transactional
    void putNonExistingEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportImportRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportImportRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailImportImportRuleWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportRule using partial update
        EmailImportImportRule partialUpdatedEmailImportImportRule = new EmailImportImportRule();
        partialUpdatedEmailImportImportRule.setId(emailImportImportRule.getId());

        partialUpdatedEmailImportImportRule
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restEmailImportImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportImportRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportImportRule))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportImportRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailImportImportRule, emailImportImportRule),
            getPersistedEmailImportImportRule(emailImportImportRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailImportImportRuleWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportImportRule using partial update
        EmailImportImportRule partialUpdatedEmailImportImportRule = new EmailImportImportRule();
        partialUpdatedEmailImportImportRule.setId(emailImportImportRule.getId());

        partialUpdatedEmailImportImportRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restEmailImportImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportImportRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportImportRule))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportImportRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportImportRuleUpdatableFieldsEquals(
            partialUpdatedEmailImportImportRule,
            getPersistedEmailImportImportRule(partialUpdatedEmailImportImportRule)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailImportImportRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailImportImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportImportRule.setId(longCount.incrementAndGet());

        // Create the EmailImportImportRule
        EmailImportImportRuleDTO emailImportImportRuleDTO = emailImportImportRuleMapper.toDto(emailImportImportRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailImportImportRuleDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailImportImportRule() throws Exception {
        // Initialize the database
        insertedEmailImportImportRule = emailImportImportRuleRepository.saveAndFlush(emailImportImportRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailImportImportRule
        restEmailImportImportRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailImportImportRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailImportImportRuleRepository.count();
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

    protected EmailImportImportRule getPersistedEmailImportImportRule(EmailImportImportRule emailImportImportRule) {
        return emailImportImportRuleRepository.findById(emailImportImportRule.getId()).orElseThrow();
    }

    protected void assertPersistedEmailImportImportRuleToMatchAllProperties(EmailImportImportRule expectedEmailImportImportRule) {
        assertEmailImportImportRuleAllPropertiesEquals(
            expectedEmailImportImportRule,
            getPersistedEmailImportImportRule(expectedEmailImportImportRule)
        );
    }

    protected void assertPersistedEmailImportImportRuleToMatchUpdatableProperties(EmailImportImportRule expectedEmailImportImportRule) {
        assertEmailImportImportRuleAllUpdatablePropertiesEquals(
            expectedEmailImportImportRule,
            getPersistedEmailImportImportRule(expectedEmailImportImportRule)
        );
    }
}
