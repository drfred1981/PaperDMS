package fr.smartprod.paperdms.export.web.rest;

import static fr.smartprod.paperdms.export.domain.ExportPatternAsserts.*;
import static fr.smartprod.paperdms.export.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.export.IntegrationTest;
import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.repository.ExportPatternRepository;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportPatternMapper;
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
 * Integration tests for the {@link ExportPatternResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExportPatternResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_PATH_TEMPLATE = "BBBBBBBBBB";

    private static final String DEFAULT_FILE_NAME_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME_TEMPLATE = "BBBBBBBBBB";

    private static final String DEFAULT_VARIABLES = "AAAAAAAAAA";
    private static final String UPDATED_VARIABLES = "BBBBBBBBBB";

    private static final String DEFAULT_EXAMPLES = "AAAAAAAAAA";
    private static final String UPDATED_EXAMPLES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_USAGE_COUNT = 1;
    private static final Integer UPDATED_USAGE_COUNT = 2;
    private static final Integer SMALLER_USAGE_COUNT = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/export-patterns";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExportPatternRepository exportPatternRepository;

    @Autowired
    private ExportPatternMapper exportPatternMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExportPatternMockMvc;

    private ExportPattern exportPattern;

    private ExportPattern insertedExportPattern;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExportPattern createEntity() {
        return new ExportPattern()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .pathTemplate(DEFAULT_PATH_TEMPLATE)
            .fileNameTemplate(DEFAULT_FILE_NAME_TEMPLATE)
            .variables(DEFAULT_VARIABLES)
            .examples(DEFAULT_EXAMPLES)
            .isSystem(DEFAULT_IS_SYSTEM)
            .isActive(DEFAULT_IS_ACTIVE)
            .usageCount(DEFAULT_USAGE_COUNT)
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
    public static ExportPattern createUpdatedEntity() {
        return new ExportPattern()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pathTemplate(UPDATED_PATH_TEMPLATE)
            .fileNameTemplate(UPDATED_FILE_NAME_TEMPLATE)
            .variables(UPDATED_VARIABLES)
            .examples(UPDATED_EXAMPLES)
            .isSystem(UPDATED_IS_SYSTEM)
            .isActive(UPDATED_IS_ACTIVE)
            .usageCount(UPDATED_USAGE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        exportPattern = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExportPattern != null) {
            exportPatternRepository.delete(insertedExportPattern);
            insertedExportPattern = null;
        }
    }

    @Test
    @Transactional
    void createExportPattern() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);
        var returnedExportPatternDTO = om.readValue(
            restExportPatternMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExportPatternDTO.class
        );

        // Validate the ExportPattern in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExportPattern = exportPatternMapper.toEntity(returnedExportPatternDTO);
        assertExportPatternUpdatableFieldsEquals(returnedExportPattern, getPersistedExportPattern(returnedExportPattern));

        insertedExportPattern = returnedExportPattern;
    }

    @Test
    @Transactional
    void createExportPatternWithExistingId() throws Exception {
        // Create the ExportPattern with an existing ID
        exportPattern.setId(1L);
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setName(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPathTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setPathTemplate(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameTemplateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setFileNameTemplate(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setIsSystem(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setIsActive(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setCreatedBy(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportPattern.setCreatedDate(null);

        // Create the ExportPattern, which fails.
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        restExportPatternMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExportPatterns() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exportPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pathTemplate").value(hasItem(DEFAULT_PATH_TEMPLATE)))
            .andExpect(jsonPath("$.[*].fileNameTemplate").value(hasItem(DEFAULT_FILE_NAME_TEMPLATE)))
            .andExpect(jsonPath("$.[*].variables").value(hasItem(DEFAULT_VARIABLES)))
            .andExpect(jsonPath("$.[*].examples").value(hasItem(DEFAULT_EXAMPLES)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExportPattern() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get the exportPattern
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL_ID, exportPattern.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exportPattern.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.pathTemplate").value(DEFAULT_PATH_TEMPLATE))
            .andExpect(jsonPath("$.fileNameTemplate").value(DEFAULT_FILE_NAME_TEMPLATE))
            .andExpect(jsonPath("$.variables").value(DEFAULT_VARIABLES))
            .andExpect(jsonPath("$.examples").value(DEFAULT_EXAMPLES))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.usageCount").value(DEFAULT_USAGE_COUNT))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getExportPatternsByIdFiltering() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        Long id = exportPattern.getId();

        defaultExportPatternFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExportPatternFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExportPatternFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExportPatternsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where name equals to
        defaultExportPatternFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportPatternsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where name in
        defaultExportPatternFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportPatternsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where name is not null
        defaultExportPatternFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where name contains
        defaultExportPatternFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportPatternsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where name does not contain
        defaultExportPatternFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllExportPatternsByPathTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where pathTemplate equals to
        defaultExportPatternFiltering("pathTemplate.equals=" + DEFAULT_PATH_TEMPLATE, "pathTemplate.equals=" + UPDATED_PATH_TEMPLATE);
    }

    @Test
    @Transactional
    void getAllExportPatternsByPathTemplateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where pathTemplate in
        defaultExportPatternFiltering(
            "pathTemplate.in=" + DEFAULT_PATH_TEMPLATE + "," + UPDATED_PATH_TEMPLATE,
            "pathTemplate.in=" + UPDATED_PATH_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByPathTemplateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where pathTemplate is not null
        defaultExportPatternFiltering("pathTemplate.specified=true", "pathTemplate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByPathTemplateContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where pathTemplate contains
        defaultExportPatternFiltering("pathTemplate.contains=" + DEFAULT_PATH_TEMPLATE, "pathTemplate.contains=" + UPDATED_PATH_TEMPLATE);
    }

    @Test
    @Transactional
    void getAllExportPatternsByPathTemplateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where pathTemplate does not contain
        defaultExportPatternFiltering(
            "pathTemplate.doesNotContain=" + UPDATED_PATH_TEMPLATE,
            "pathTemplate.doesNotContain=" + DEFAULT_PATH_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByFileNameTemplateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where fileNameTemplate equals to
        defaultExportPatternFiltering(
            "fileNameTemplate.equals=" + DEFAULT_FILE_NAME_TEMPLATE,
            "fileNameTemplate.equals=" + UPDATED_FILE_NAME_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByFileNameTemplateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where fileNameTemplate in
        defaultExportPatternFiltering(
            "fileNameTemplate.in=" + DEFAULT_FILE_NAME_TEMPLATE + "," + UPDATED_FILE_NAME_TEMPLATE,
            "fileNameTemplate.in=" + UPDATED_FILE_NAME_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByFileNameTemplateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where fileNameTemplate is not null
        defaultExportPatternFiltering("fileNameTemplate.specified=true", "fileNameTemplate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByFileNameTemplateContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where fileNameTemplate contains
        defaultExportPatternFiltering(
            "fileNameTemplate.contains=" + DEFAULT_FILE_NAME_TEMPLATE,
            "fileNameTemplate.contains=" + UPDATED_FILE_NAME_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByFileNameTemplateNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where fileNameTemplate does not contain
        defaultExportPatternFiltering(
            "fileNameTemplate.doesNotContain=" + UPDATED_FILE_NAME_TEMPLATE,
            "fileNameTemplate.doesNotContain=" + DEFAULT_FILE_NAME_TEMPLATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isSystem equals to
        defaultExportPatternFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isSystem in
        defaultExportPatternFiltering("isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM, "isSystem.in=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isSystem is not null
        defaultExportPatternFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isActive equals to
        defaultExportPatternFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isActive in
        defaultExportPatternFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllExportPatternsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where isActive is not null
        defaultExportPatternFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount equals to
        defaultExportPatternFiltering("usageCount.equals=" + DEFAULT_USAGE_COUNT, "usageCount.equals=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount in
        defaultExportPatternFiltering(
            "usageCount.in=" + DEFAULT_USAGE_COUNT + "," + UPDATED_USAGE_COUNT,
            "usageCount.in=" + UPDATED_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount is not null
        defaultExportPatternFiltering("usageCount.specified=true", "usageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount is greater than or equal to
        defaultExportPatternFiltering(
            "usageCount.greaterThanOrEqual=" + DEFAULT_USAGE_COUNT,
            "usageCount.greaterThanOrEqual=" + UPDATED_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount is less than or equal to
        defaultExportPatternFiltering(
            "usageCount.lessThanOrEqual=" + DEFAULT_USAGE_COUNT,
            "usageCount.lessThanOrEqual=" + SMALLER_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount is less than
        defaultExportPatternFiltering("usageCount.lessThan=" + UPDATED_USAGE_COUNT, "usageCount.lessThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllExportPatternsByUsageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where usageCount is greater than
        defaultExportPatternFiltering("usageCount.greaterThan=" + SMALLER_USAGE_COUNT, "usageCount.greaterThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdBy equals to
        defaultExportPatternFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdBy in
        defaultExportPatternFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdBy is not null
        defaultExportPatternFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdBy contains
        defaultExportPatternFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdBy does not contain
        defaultExportPatternFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdDate equals to
        defaultExportPatternFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdDate in
        defaultExportPatternFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where createdDate is not null
        defaultExportPatternFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportPatternsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where lastModifiedDate equals to
        defaultExportPatternFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where lastModifiedDate in
        defaultExportPatternFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllExportPatternsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        // Get all the exportPatternList where lastModifiedDate is not null
        defaultExportPatternFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultExportPatternFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExportPatternShouldBeFound(shouldBeFound);
        defaultExportPatternShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExportPatternShouldBeFound(String filter) throws Exception {
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exportPattern.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].pathTemplate").value(hasItem(DEFAULT_PATH_TEMPLATE)))
            .andExpect(jsonPath("$.[*].fileNameTemplate").value(hasItem(DEFAULT_FILE_NAME_TEMPLATE)))
            .andExpect(jsonPath("$.[*].variables").value(hasItem(DEFAULT_VARIABLES)))
            .andExpect(jsonPath("$.[*].examples").value(hasItem(DEFAULT_EXAMPLES)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExportPatternShouldNotBeFound(String filter) throws Exception {
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExportPatternMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExportPattern() throws Exception {
        // Get the exportPattern
        restExportPatternMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExportPattern() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportPattern
        ExportPattern updatedExportPattern = exportPatternRepository.findById(exportPattern.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExportPattern are not directly saved in db
        em.detach(updatedExportPattern);
        updatedExportPattern
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pathTemplate(UPDATED_PATH_TEMPLATE)
            .fileNameTemplate(UPDATED_FILE_NAME_TEMPLATE)
            .variables(UPDATED_VARIABLES)
            .examples(UPDATED_EXAMPLES)
            .isSystem(UPDATED_IS_SYSTEM)
            .isActive(UPDATED_IS_ACTIVE)
            .usageCount(UPDATED_USAGE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(updatedExportPattern);

        restExportPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportPatternDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportPatternDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExportPatternToMatchAllProperties(updatedExportPattern);
    }

    @Test
    @Transactional
    void putNonExistingExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportPatternDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportPatternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportPatternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExportPatternWithPatch() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportPattern using partial update
        ExportPattern partialUpdatedExportPattern = new ExportPattern();
        partialUpdatedExportPattern.setId(exportPattern.getId());

        partialUpdatedExportPattern
            .variables(UPDATED_VARIABLES)
            .isSystem(UPDATED_IS_SYSTEM)
            .isActive(UPDATED_IS_ACTIVE)
            .usageCount(UPDATED_USAGE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExportPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportPattern))
            )
            .andExpect(status().isOk());

        // Validate the ExportPattern in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportPatternUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExportPattern, exportPattern),
            getPersistedExportPattern(exportPattern)
        );
    }

    @Test
    @Transactional
    void fullUpdateExportPatternWithPatch() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportPattern using partial update
        ExportPattern partialUpdatedExportPattern = new ExportPattern();
        partialUpdatedExportPattern.setId(exportPattern.getId());

        partialUpdatedExportPattern
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .pathTemplate(UPDATED_PATH_TEMPLATE)
            .fileNameTemplate(UPDATED_FILE_NAME_TEMPLATE)
            .variables(UPDATED_VARIABLES)
            .examples(UPDATED_EXAMPLES)
            .isSystem(UPDATED_IS_SYSTEM)
            .isActive(UPDATED_IS_ACTIVE)
            .usageCount(UPDATED_USAGE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restExportPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportPattern.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportPattern))
            )
            .andExpect(status().isOk());

        // Validate the ExportPattern in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportPatternUpdatableFieldsEquals(partialUpdatedExportPattern, getPersistedExportPattern(partialUpdatedExportPattern));
    }

    @Test
    @Transactional
    void patchNonExistingExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exportPatternDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportPatternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportPatternDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExportPattern() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportPattern.setId(longCount.incrementAndGet());

        // Create the ExportPattern
        ExportPatternDTO exportPatternDTO = exportPatternMapper.toDto(exportPattern);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportPatternMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(exportPatternDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportPattern in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExportPattern() throws Exception {
        // Initialize the database
        insertedExportPattern = exportPatternRepository.saveAndFlush(exportPattern);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the exportPattern
        restExportPatternMockMvc
            .perform(delete(ENTITY_API_URL_ID, exportPattern.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return exportPatternRepository.count();
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

    protected ExportPattern getPersistedExportPattern(ExportPattern exportPattern) {
        return exportPatternRepository.findById(exportPattern.getId()).orElseThrow();
    }

    protected void assertPersistedExportPatternToMatchAllProperties(ExportPattern expectedExportPattern) {
        assertExportPatternAllPropertiesEquals(expectedExportPattern, getPersistedExportPattern(expectedExportPattern));
    }

    protected void assertPersistedExportPatternToMatchUpdatableProperties(ExportPattern expectedExportPattern) {
        assertExportPatternAllUpdatablePropertiesEquals(expectedExportPattern, getPersistedExportPattern(expectedExportPattern));
    }
}
