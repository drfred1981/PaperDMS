package fr.smartprod.paperdms.emailimport.web.rest;

import static fr.smartprod.paperdms.emailimport.domain.ImportRuleAsserts.*;
import static fr.smartprod.paperdms.emailimport.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimport.IntegrationTest;
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.repository.ImportRuleRepository;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.ImportRuleMapper;
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
 * Integration tests for the {@link ImportRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImportRuleResourceIT {

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

    private static final Long DEFAULT_FOLDER_ID = 1L;
    private static final Long UPDATED_FOLDER_ID = 2L;
    private static final Long SMALLER_FOLDER_ID = 1L - 1L;

    private static final Long DEFAULT_DOCUMENT_TYPE_ID = 1L;
    private static final Long UPDATED_DOCUMENT_TYPE_ID = 2L;
    private static final Long SMALLER_DOCUMENT_TYPE_ID = 1L - 1L;

    private static final String DEFAULT_APPLY_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_APPLY_TAGS = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/import-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImportRuleRepository importRuleRepository;

    @Autowired
    private ImportRuleMapper importRuleMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImportRuleMockMvc;

    private ImportRule importRule;

    private ImportRule insertedImportRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImportRule createEntity() {
        return new ImportRule()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .priority(DEFAULT_PRIORITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .conditions(DEFAULT_CONDITIONS)
            .actions(DEFAULT_ACTIONS)
            .folderId(DEFAULT_FOLDER_ID)
            .documentTypeId(DEFAULT_DOCUMENT_TYPE_ID)
            .applyTags(DEFAULT_APPLY_TAGS)
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
    public static ImportRule createUpdatedEntity() {
        return new ImportRule()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .applyTags(UPDATED_APPLY_TAGS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        importRule = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImportRule != null) {
            importRuleRepository.delete(insertedImportRule);
            insertedImportRule = null;
        }
    }

    @Test
    @Transactional
    void createImportRule() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);
        var returnedImportRuleDTO = om.readValue(
            restImportRuleMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImportRuleDTO.class
        );

        // Validate the ImportRule in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImportRule = importRuleMapper.toEntity(returnedImportRuleDTO);
        assertImportRuleUpdatableFieldsEquals(returnedImportRule, getPersistedImportRule(returnedImportRule));

        insertedImportRule = returnedImportRule;
    }

    @Test
    @Transactional
    void createImportRuleWithExistingId() throws Exception {
        // Create the ImportRule with an existing ID
        importRule.setId(1L);
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importRule.setName(null);

        // Create the ImportRule, which fails.
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importRule.setPriority(null);

        // Create the ImportRule, which fails.
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importRule.setIsActive(null);

        // Create the ImportRule, which fails.
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importRule.setCreatedBy(null);

        // Create the ImportRule, which fails.
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importRule.setCreatedDate(null);

        // Create the ImportRule, which fails.
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        restImportRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImportRules() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].applyTags").value(hasItem(DEFAULT_APPLY_TAGS)))
            .andExpect(jsonPath("$.[*].notifyUsers").value(hasItem(DEFAULT_NOTIFY_USERS)))
            .andExpect(jsonPath("$.[*].matchCount").value(hasItem(DEFAULT_MATCH_COUNT)))
            .andExpect(jsonPath("$.[*].lastMatchDate").value(hasItem(DEFAULT_LAST_MATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getImportRule() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get the importRule
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, importRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(importRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.conditions").value(DEFAULT_CONDITIONS))
            .andExpect(jsonPath("$.actions").value(DEFAULT_ACTIONS))
            .andExpect(jsonPath("$.folderId").value(DEFAULT_FOLDER_ID.intValue()))
            .andExpect(jsonPath("$.documentTypeId").value(DEFAULT_DOCUMENT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.applyTags").value(DEFAULT_APPLY_TAGS))
            .andExpect(jsonPath("$.notifyUsers").value(DEFAULT_NOTIFY_USERS))
            .andExpect(jsonPath("$.matchCount").value(DEFAULT_MATCH_COUNT))
            .andExpect(jsonPath("$.lastMatchDate").value(DEFAULT_LAST_MATCH_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getImportRulesByIdFiltering() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        Long id = importRule.getId();

        defaultImportRuleFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImportRuleFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImportRuleFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImportRulesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where name equals to
        defaultImportRuleFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImportRulesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where name in
        defaultImportRuleFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImportRulesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where name is not null
        defaultImportRuleFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where name contains
        defaultImportRuleFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllImportRulesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where name does not contain
        defaultImportRuleFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority equals to
        defaultImportRuleFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority in
        defaultImportRuleFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority is not null
        defaultImportRuleFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority is greater than or equal to
        defaultImportRuleFiltering("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY, "priority.greaterThanOrEqual=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority is less than or equal to
        defaultImportRuleFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority is less than
        defaultImportRuleFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where priority is greater than
        defaultImportRuleFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImportRulesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where isActive equals to
        defaultImportRuleFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllImportRulesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where isActive in
        defaultImportRuleFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllImportRulesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where isActive is not null
        defaultImportRuleFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId equals to
        defaultImportRuleFiltering("folderId.equals=" + DEFAULT_FOLDER_ID, "folderId.equals=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId in
        defaultImportRuleFiltering("folderId.in=" + DEFAULT_FOLDER_ID + "," + UPDATED_FOLDER_ID, "folderId.in=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId is not null
        defaultImportRuleFiltering("folderId.specified=true", "folderId.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId is greater than or equal to
        defaultImportRuleFiltering("folderId.greaterThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.greaterThanOrEqual=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId is less than or equal to
        defaultImportRuleFiltering("folderId.lessThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.lessThanOrEqual=" + SMALLER_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId is less than
        defaultImportRuleFiltering("folderId.lessThan=" + UPDATED_FOLDER_ID, "folderId.lessThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByFolderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where folderId is greater than
        defaultImportRuleFiltering("folderId.greaterThan=" + SMALLER_FOLDER_ID, "folderId.greaterThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId equals to
        defaultImportRuleFiltering(
            "documentTypeId.equals=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.equals=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId in
        defaultImportRuleFiltering(
            "documentTypeId.in=" + DEFAULT_DOCUMENT_TYPE_ID + "," + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.in=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId is not null
        defaultImportRuleFiltering("documentTypeId.specified=true", "documentTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId is greater than or equal to
        defaultImportRuleFiltering(
            "documentTypeId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThanOrEqual=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId is less than or equal to
        defaultImportRuleFiltering(
            "documentTypeId.lessThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThanOrEqual=" + SMALLER_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId is less than
        defaultImportRuleFiltering(
            "documentTypeId.lessThan=" + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByDocumentTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where documentTypeId is greater than
        defaultImportRuleFiltering(
            "documentTypeId.greaterThan=" + SMALLER_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount equals to
        defaultImportRuleFiltering("matchCount.equals=" + DEFAULT_MATCH_COUNT, "matchCount.equals=" + UPDATED_MATCH_COUNT);
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount in
        defaultImportRuleFiltering(
            "matchCount.in=" + DEFAULT_MATCH_COUNT + "," + UPDATED_MATCH_COUNT,
            "matchCount.in=" + UPDATED_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount is not null
        defaultImportRuleFiltering("matchCount.specified=true", "matchCount.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount is greater than or equal to
        defaultImportRuleFiltering(
            "matchCount.greaterThanOrEqual=" + DEFAULT_MATCH_COUNT,
            "matchCount.greaterThanOrEqual=" + UPDATED_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount is less than or equal to
        defaultImportRuleFiltering(
            "matchCount.lessThanOrEqual=" + DEFAULT_MATCH_COUNT,
            "matchCount.lessThanOrEqual=" + SMALLER_MATCH_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount is less than
        defaultImportRuleFiltering("matchCount.lessThan=" + UPDATED_MATCH_COUNT, "matchCount.lessThan=" + DEFAULT_MATCH_COUNT);
    }

    @Test
    @Transactional
    void getAllImportRulesByMatchCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where matchCount is greater than
        defaultImportRuleFiltering("matchCount.greaterThan=" + SMALLER_MATCH_COUNT, "matchCount.greaterThan=" + DEFAULT_MATCH_COUNT);
    }

    @Test
    @Transactional
    void getAllImportRulesByLastMatchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastMatchDate equals to
        defaultImportRuleFiltering("lastMatchDate.equals=" + DEFAULT_LAST_MATCH_DATE, "lastMatchDate.equals=" + UPDATED_LAST_MATCH_DATE);
    }

    @Test
    @Transactional
    void getAllImportRulesByLastMatchDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastMatchDate in
        defaultImportRuleFiltering(
            "lastMatchDate.in=" + DEFAULT_LAST_MATCH_DATE + "," + UPDATED_LAST_MATCH_DATE,
            "lastMatchDate.in=" + UPDATED_LAST_MATCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByLastMatchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastMatchDate is not null
        defaultImportRuleFiltering("lastMatchDate.specified=true", "lastMatchDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdBy equals to
        defaultImportRuleFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdBy in
        defaultImportRuleFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdBy is not null
        defaultImportRuleFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdBy contains
        defaultImportRuleFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdBy does not contain
        defaultImportRuleFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdDate equals to
        defaultImportRuleFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdDate in
        defaultImportRuleFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where createdDate is not null
        defaultImportRuleFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImportRulesByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastModifiedDate equals to
        defaultImportRuleFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastModifiedDate in
        defaultImportRuleFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllImportRulesByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        // Get all the importRuleList where lastModifiedDate is not null
        defaultImportRuleFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultImportRuleFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImportRuleShouldBeFound(shouldBeFound);
        defaultImportRuleShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImportRuleShouldBeFound(String filter) throws Exception {
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].conditions").value(hasItem(DEFAULT_CONDITIONS)))
            .andExpect(jsonPath("$.[*].actions").value(hasItem(DEFAULT_ACTIONS)))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].applyTags").value(hasItem(DEFAULT_APPLY_TAGS)))
            .andExpect(jsonPath("$.[*].notifyUsers").value(hasItem(DEFAULT_NOTIFY_USERS)))
            .andExpect(jsonPath("$.[*].matchCount").value(hasItem(DEFAULT_MATCH_COUNT)))
            .andExpect(jsonPath("$.[*].lastMatchDate").value(hasItem(DEFAULT_LAST_MATCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImportRuleShouldNotBeFound(String filter) throws Exception {
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImportRuleMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImportRule() throws Exception {
        // Get the importRule
        restImportRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImportRule() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importRule
        ImportRule updatedImportRule = importRuleRepository.findById(importRule.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImportRule are not directly saved in db
        em.detach(updatedImportRule);
        updatedImportRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .applyTags(UPDATED_APPLY_TAGS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(updatedImportRule);

        restImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, importRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importRuleDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImportRuleToMatchAllProperties(updatedImportRule);
    }

    @Test
    @Transactional
    void putNonExistingImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, importRuleDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImportRuleWithPatch() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importRule using partial update
        ImportRule partialUpdatedImportRule = new ImportRule();
        partialUpdatedImportRule.setId(importRule.getId());

        partialUpdatedImportRule
            .isActive(UPDATED_IS_ACTIVE)
            .actions(UPDATED_ACTIONS)
            .applyTags(UPDATED_APPLY_TAGS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .createdBy(UPDATED_CREATED_BY);

        restImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImportRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImportRule))
            )
            .andExpect(status().isOk());

        // Validate the ImportRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImportRuleUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImportRule, importRule),
            getPersistedImportRule(importRule)
        );
    }

    @Test
    @Transactional
    void fullUpdateImportRuleWithPatch() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importRule using partial update
        ImportRule partialUpdatedImportRule = new ImportRule();
        partialUpdatedImportRule.setId(importRule.getId());

        partialUpdatedImportRule
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .priority(UPDATED_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .conditions(UPDATED_CONDITIONS)
            .actions(UPDATED_ACTIONS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .applyTags(UPDATED_APPLY_TAGS)
            .notifyUsers(UPDATED_NOTIFY_USERS)
            .matchCount(UPDATED_MATCH_COUNT)
            .lastMatchDate(UPDATED_LAST_MATCH_DATE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImportRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImportRule))
            )
            .andExpect(status().isOk());

        // Validate the ImportRule in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImportRuleUpdatableFieldsEquals(partialUpdatedImportRule, getPersistedImportRule(partialUpdatedImportRule));
    }

    @Test
    @Transactional
    void patchNonExistingImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, importRuleDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(importRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(importRuleDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImportRule() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importRule.setId(longCount.incrementAndGet());

        // Create the ImportRule
        ImportRuleDTO importRuleDTO = importRuleMapper.toDto(importRule);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportRuleMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(importRuleDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImportRule in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImportRule() throws Exception {
        // Initialize the database
        insertedImportRule = importRuleRepository.saveAndFlush(importRule);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the importRule
        restImportRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, importRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return importRuleRepository.count();
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

    protected ImportRule getPersistedImportRule(ImportRule importRule) {
        return importRuleRepository.findById(importRule.getId()).orElseThrow();
    }

    protected void assertPersistedImportRuleToMatchAllProperties(ImportRule expectedImportRule) {
        assertImportRuleAllPropertiesEquals(expectedImportRule, getPersistedImportRule(expectedImportRule));
    }

    protected void assertPersistedImportRuleToMatchUpdatableProperties(ImportRule expectedImportRule) {
        assertImportRuleAllUpdatablePropertiesEquals(expectedImportRule, getPersistedImportRule(expectedImportRule));
    }
}
