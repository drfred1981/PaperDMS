package fr.smartprod.paperdms.export.web.rest;

import static fr.smartprod.paperdms.export.domain.ExportJobAsserts.*;
import static fr.smartprod.paperdms.export.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.export.IntegrationTest;
import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.domain.enumeration.ExportFormat;
import fr.smartprod.paperdms.export.domain.enumeration.ExportStatus;
import fr.smartprod.paperdms.export.repository.ExportJobRepository;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportJobMapper;
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
 * Integration tests for the {@link ExportJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExportJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_QUERY = "BBBBBBBBBB";

    private static final Long DEFAULT_EXPORT_PATTERN_ID = 1L;
    private static final Long UPDATED_EXPORT_PATTERN_ID = 2L;
    private static final Long SMALLER_EXPORT_PATTERN_ID = 1L - 1L;

    private static final ExportFormat DEFAULT_EXPORT_FORMAT = ExportFormat.ORIGINAL;
    private static final ExportFormat UPDATED_EXPORT_FORMAT = ExportFormat.PDF;

    private static final Boolean DEFAULT_INCLUDE_METADATA = false;
    private static final Boolean UPDATED_INCLUDE_METADATA = true;

    private static final Boolean DEFAULT_INCLUDE_VERSIONS = false;
    private static final Boolean UPDATED_INCLUDE_VERSIONS = true;

    private static final Boolean DEFAULT_INCLUDE_COMMENTS = false;
    private static final Boolean UPDATED_INCLUDE_COMMENTS = true;

    private static final Boolean DEFAULT_INCLUDE_AUDIT_TRAIL = false;
    private static final Boolean UPDATED_INCLUDE_AUDIT_TRAIL = true;

    private static final String DEFAULT_S_3_EXPORT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_EXPORT_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_EXPORT_SIZE = 1L;
    private static final Long UPDATED_EXPORT_SIZE = 2L;
    private static final Long SMALLER_EXPORT_SIZE = 1L - 1L;

    private static final Integer DEFAULT_DOCUMENT_COUNT = 1;
    private static final Integer UPDATED_DOCUMENT_COUNT = 2;
    private static final Integer SMALLER_DOCUMENT_COUNT = 1 - 1;

    private static final Integer DEFAULT_FILES_GENERATED = 1;
    private static final Integer UPDATED_FILES_GENERATED = 2;
    private static final Integer SMALLER_FILES_GENERATED = 1 - 1;

    private static final ExportStatus DEFAULT_STATUS = ExportStatus.PENDING;
    private static final ExportStatus UPDATED_STATUS = ExportStatus.EXPORTING;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/export-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExportJobRepository exportJobRepository;

    @Autowired
    private ExportJobMapper exportJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExportJobMockMvc;

    private ExportJob exportJob;

    private ExportJob insertedExportJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExportJob createEntity(EntityManager em) {
        ExportJob exportJob = new ExportJob()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .documentQuery(DEFAULT_DOCUMENT_QUERY)
            .exportPatternId(DEFAULT_EXPORT_PATTERN_ID)
            .exportFormat(DEFAULT_EXPORT_FORMAT)
            .includeMetadata(DEFAULT_INCLUDE_METADATA)
            .includeVersions(DEFAULT_INCLUDE_VERSIONS)
            .includeComments(DEFAULT_INCLUDE_COMMENTS)
            .includeAuditTrail(DEFAULT_INCLUDE_AUDIT_TRAIL)
            .s3ExportKey(DEFAULT_S_3_EXPORT_KEY)
            .exportSize(DEFAULT_EXPORT_SIZE)
            .documentCount(DEFAULT_DOCUMENT_COUNT)
            .filesGenerated(DEFAULT_FILES_GENERATED)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        ExportPattern exportPattern;
        if (TestUtil.findAll(em, ExportPattern.class).isEmpty()) {
            exportPattern = ExportPatternResourceIT.createEntity();
            em.persist(exportPattern);
            em.flush();
        } else {
            exportPattern = TestUtil.findAll(em, ExportPattern.class).get(0);
        }
        exportJob.setExportPattern(exportPattern);
        return exportJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExportJob createUpdatedEntity(EntityManager em) {
        ExportJob updatedExportJob = new ExportJob()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .exportPatternId(UPDATED_EXPORT_PATTERN_ID)
            .exportFormat(UPDATED_EXPORT_FORMAT)
            .includeMetadata(UPDATED_INCLUDE_METADATA)
            .includeVersions(UPDATED_INCLUDE_VERSIONS)
            .includeComments(UPDATED_INCLUDE_COMMENTS)
            .includeAuditTrail(UPDATED_INCLUDE_AUDIT_TRAIL)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .exportSize(UPDATED_EXPORT_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .filesGenerated(UPDATED_FILES_GENERATED)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        ExportPattern exportPattern;
        if (TestUtil.findAll(em, ExportPattern.class).isEmpty()) {
            exportPattern = ExportPatternResourceIT.createUpdatedEntity();
            em.persist(exportPattern);
            em.flush();
        } else {
            exportPattern = TestUtil.findAll(em, ExportPattern.class).get(0);
        }
        updatedExportJob.setExportPattern(exportPattern);
        return updatedExportJob;
    }

    @BeforeEach
    void initTest() {
        exportJob = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExportJob != null) {
            exportJobRepository.delete(insertedExportJob);
            insertedExportJob = null;
        }
    }

    @Test
    @Transactional
    void createExportJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);
        var returnedExportJobDTO = om.readValue(
            restExportJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExportJobDTO.class
        );

        // Validate the ExportJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExportJob = exportJobMapper.toEntity(returnedExportJobDTO);
        assertExportJobUpdatableFieldsEquals(returnedExportJob, getPersistedExportJob(returnedExportJob));

        insertedExportJob = returnedExportJob;
    }

    @Test
    @Transactional
    void createExportJobWithExistingId() throws Exception {
        // Create the ExportJob with an existing ID
        exportJob.setId(1L);
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setName(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExportPatternIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setExportPatternId(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExportFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setExportFormat(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncludeMetadataIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setIncludeMetadata(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIncludeVersionsIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setIncludeVersions(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setStatus(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setCreatedBy(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportJob.setCreatedDate(null);

        // Create the ExportJob, which fails.
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        restExportJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExportJobs() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exportJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentQuery").value(hasItem(DEFAULT_DOCUMENT_QUERY)))
            .andExpect(jsonPath("$.[*].exportPatternId").value(hasItem(DEFAULT_EXPORT_PATTERN_ID.intValue())))
            .andExpect(jsonPath("$.[*].exportFormat").value(hasItem(DEFAULT_EXPORT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].includeMetadata").value(hasItem(DEFAULT_INCLUDE_METADATA)))
            .andExpect(jsonPath("$.[*].includeVersions").value(hasItem(DEFAULT_INCLUDE_VERSIONS)))
            .andExpect(jsonPath("$.[*].includeComments").value(hasItem(DEFAULT_INCLUDE_COMMENTS)))
            .andExpect(jsonPath("$.[*].includeAuditTrail").value(hasItem(DEFAULT_INCLUDE_AUDIT_TRAIL)))
            .andExpect(jsonPath("$.[*].s3ExportKey").value(hasItem(DEFAULT_S_3_EXPORT_KEY)))
            .andExpect(jsonPath("$.[*].exportSize").value(hasItem(DEFAULT_EXPORT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].filesGenerated").value(hasItem(DEFAULT_FILES_GENERATED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExportJob() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get the exportJob
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL_ID, exportJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exportJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.documentQuery").value(DEFAULT_DOCUMENT_QUERY))
            .andExpect(jsonPath("$.exportPatternId").value(DEFAULT_EXPORT_PATTERN_ID.intValue()))
            .andExpect(jsonPath("$.exportFormat").value(DEFAULT_EXPORT_FORMAT.toString()))
            .andExpect(jsonPath("$.includeMetadata").value(DEFAULT_INCLUDE_METADATA))
            .andExpect(jsonPath("$.includeVersions").value(DEFAULT_INCLUDE_VERSIONS))
            .andExpect(jsonPath("$.includeComments").value(DEFAULT_INCLUDE_COMMENTS))
            .andExpect(jsonPath("$.includeAuditTrail").value(DEFAULT_INCLUDE_AUDIT_TRAIL))
            .andExpect(jsonPath("$.s3ExportKey").value(DEFAULT_S_3_EXPORT_KEY))
            .andExpect(jsonPath("$.exportSize").value(DEFAULT_EXPORT_SIZE.intValue()))
            .andExpect(jsonPath("$.documentCount").value(DEFAULT_DOCUMENT_COUNT))
            .andExpect(jsonPath("$.filesGenerated").value(DEFAULT_FILES_GENERATED))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getExportJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        Long id = exportJob.getId();

        defaultExportJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultExportJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultExportJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllExportJobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where name equals to
        defaultExportJobFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportJobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where name in
        defaultExportJobFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportJobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where name is not null
        defaultExportJobFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where name contains
        defaultExportJobFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllExportJobsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where name does not contain
        defaultExportJobFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId equals to
        defaultExportJobFiltering(
            "exportPatternId.equals=" + DEFAULT_EXPORT_PATTERN_ID,
            "exportPatternId.equals=" + UPDATED_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId in
        defaultExportJobFiltering(
            "exportPatternId.in=" + DEFAULT_EXPORT_PATTERN_ID + "," + UPDATED_EXPORT_PATTERN_ID,
            "exportPatternId.in=" + UPDATED_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId is not null
        defaultExportJobFiltering("exportPatternId.specified=true", "exportPatternId.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId is greater than or equal to
        defaultExportJobFiltering(
            "exportPatternId.greaterThanOrEqual=" + DEFAULT_EXPORT_PATTERN_ID,
            "exportPatternId.greaterThanOrEqual=" + UPDATED_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId is less than or equal to
        defaultExportJobFiltering(
            "exportPatternId.lessThanOrEqual=" + DEFAULT_EXPORT_PATTERN_ID,
            "exportPatternId.lessThanOrEqual=" + SMALLER_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId is less than
        defaultExportJobFiltering(
            "exportPatternId.lessThan=" + UPDATED_EXPORT_PATTERN_ID,
            "exportPatternId.lessThan=" + DEFAULT_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportPatternId is greater than
        defaultExportJobFiltering(
            "exportPatternId.greaterThan=" + SMALLER_EXPORT_PATTERN_ID,
            "exportPatternId.greaterThan=" + DEFAULT_EXPORT_PATTERN_ID
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportFormat equals to
        defaultExportJobFiltering("exportFormat.equals=" + DEFAULT_EXPORT_FORMAT, "exportFormat.equals=" + UPDATED_EXPORT_FORMAT);
    }

    @Test
    @Transactional
    void getAllExportJobsByExportFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportFormat in
        defaultExportJobFiltering(
            "exportFormat.in=" + DEFAULT_EXPORT_FORMAT + "," + UPDATED_EXPORT_FORMAT,
            "exportFormat.in=" + UPDATED_EXPORT_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportFormat is not null
        defaultExportJobFiltering("exportFormat.specified=true", "exportFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeMetadataIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeMetadata equals to
        defaultExportJobFiltering(
            "includeMetadata.equals=" + DEFAULT_INCLUDE_METADATA,
            "includeMetadata.equals=" + UPDATED_INCLUDE_METADATA
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeMetadataIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeMetadata in
        defaultExportJobFiltering(
            "includeMetadata.in=" + DEFAULT_INCLUDE_METADATA + "," + UPDATED_INCLUDE_METADATA,
            "includeMetadata.in=" + UPDATED_INCLUDE_METADATA
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeMetadataIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeMetadata is not null
        defaultExportJobFiltering("includeMetadata.specified=true", "includeMetadata.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeVersionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeVersions equals to
        defaultExportJobFiltering(
            "includeVersions.equals=" + DEFAULT_INCLUDE_VERSIONS,
            "includeVersions.equals=" + UPDATED_INCLUDE_VERSIONS
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeVersionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeVersions in
        defaultExportJobFiltering(
            "includeVersions.in=" + DEFAULT_INCLUDE_VERSIONS + "," + UPDATED_INCLUDE_VERSIONS,
            "includeVersions.in=" + UPDATED_INCLUDE_VERSIONS
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeVersionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeVersions is not null
        defaultExportJobFiltering("includeVersions.specified=true", "includeVersions.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeCommentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeComments equals to
        defaultExportJobFiltering(
            "includeComments.equals=" + DEFAULT_INCLUDE_COMMENTS,
            "includeComments.equals=" + UPDATED_INCLUDE_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeCommentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeComments in
        defaultExportJobFiltering(
            "includeComments.in=" + DEFAULT_INCLUDE_COMMENTS + "," + UPDATED_INCLUDE_COMMENTS,
            "includeComments.in=" + UPDATED_INCLUDE_COMMENTS
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeCommentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeComments is not null
        defaultExportJobFiltering("includeComments.specified=true", "includeComments.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeAuditTrailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeAuditTrail equals to
        defaultExportJobFiltering(
            "includeAuditTrail.equals=" + DEFAULT_INCLUDE_AUDIT_TRAIL,
            "includeAuditTrail.equals=" + UPDATED_INCLUDE_AUDIT_TRAIL
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeAuditTrailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeAuditTrail in
        defaultExportJobFiltering(
            "includeAuditTrail.in=" + DEFAULT_INCLUDE_AUDIT_TRAIL + "," + UPDATED_INCLUDE_AUDIT_TRAIL,
            "includeAuditTrail.in=" + UPDATED_INCLUDE_AUDIT_TRAIL
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByIncludeAuditTrailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where includeAuditTrail is not null
        defaultExportJobFiltering("includeAuditTrail.specified=true", "includeAuditTrail.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsBys3ExportKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where s3ExportKey equals to
        defaultExportJobFiltering("s3ExportKey.equals=" + DEFAULT_S_3_EXPORT_KEY, "s3ExportKey.equals=" + UPDATED_S_3_EXPORT_KEY);
    }

    @Test
    @Transactional
    void getAllExportJobsBys3ExportKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where s3ExportKey in
        defaultExportJobFiltering(
            "s3ExportKey.in=" + DEFAULT_S_3_EXPORT_KEY + "," + UPDATED_S_3_EXPORT_KEY,
            "s3ExportKey.in=" + UPDATED_S_3_EXPORT_KEY
        );
    }

    @Test
    @Transactional
    void getAllExportJobsBys3ExportKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where s3ExportKey is not null
        defaultExportJobFiltering("s3ExportKey.specified=true", "s3ExportKey.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsBys3ExportKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where s3ExportKey contains
        defaultExportJobFiltering("s3ExportKey.contains=" + DEFAULT_S_3_EXPORT_KEY, "s3ExportKey.contains=" + UPDATED_S_3_EXPORT_KEY);
    }

    @Test
    @Transactional
    void getAllExportJobsBys3ExportKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where s3ExportKey does not contain
        defaultExportJobFiltering(
            "s3ExportKey.doesNotContain=" + UPDATED_S_3_EXPORT_KEY,
            "s3ExportKey.doesNotContain=" + DEFAULT_S_3_EXPORT_KEY
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize equals to
        defaultExportJobFiltering("exportSize.equals=" + DEFAULT_EXPORT_SIZE, "exportSize.equals=" + UPDATED_EXPORT_SIZE);
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize in
        defaultExportJobFiltering(
            "exportSize.in=" + DEFAULT_EXPORT_SIZE + "," + UPDATED_EXPORT_SIZE,
            "exportSize.in=" + UPDATED_EXPORT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize is not null
        defaultExportJobFiltering("exportSize.specified=true", "exportSize.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize is greater than or equal to
        defaultExportJobFiltering(
            "exportSize.greaterThanOrEqual=" + DEFAULT_EXPORT_SIZE,
            "exportSize.greaterThanOrEqual=" + UPDATED_EXPORT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize is less than or equal to
        defaultExportJobFiltering("exportSize.lessThanOrEqual=" + DEFAULT_EXPORT_SIZE, "exportSize.lessThanOrEqual=" + SMALLER_EXPORT_SIZE);
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize is less than
        defaultExportJobFiltering("exportSize.lessThan=" + UPDATED_EXPORT_SIZE, "exportSize.lessThan=" + DEFAULT_EXPORT_SIZE);
    }

    @Test
    @Transactional
    void getAllExportJobsByExportSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where exportSize is greater than
        defaultExportJobFiltering("exportSize.greaterThan=" + SMALLER_EXPORT_SIZE, "exportSize.greaterThan=" + DEFAULT_EXPORT_SIZE);
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount equals to
        defaultExportJobFiltering("documentCount.equals=" + DEFAULT_DOCUMENT_COUNT, "documentCount.equals=" + UPDATED_DOCUMENT_COUNT);
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount in
        defaultExportJobFiltering(
            "documentCount.in=" + DEFAULT_DOCUMENT_COUNT + "," + UPDATED_DOCUMENT_COUNT,
            "documentCount.in=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount is not null
        defaultExportJobFiltering("documentCount.specified=true", "documentCount.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount is greater than or equal to
        defaultExportJobFiltering(
            "documentCount.greaterThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.greaterThanOrEqual=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount is less than or equal to
        defaultExportJobFiltering(
            "documentCount.lessThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.lessThanOrEqual=" + SMALLER_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount is less than
        defaultExportJobFiltering("documentCount.lessThan=" + UPDATED_DOCUMENT_COUNT, "documentCount.lessThan=" + DEFAULT_DOCUMENT_COUNT);
    }

    @Test
    @Transactional
    void getAllExportJobsByDocumentCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where documentCount is greater than
        defaultExportJobFiltering(
            "documentCount.greaterThan=" + SMALLER_DOCUMENT_COUNT,
            "documentCount.greaterThan=" + DEFAULT_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated equals to
        defaultExportJobFiltering("filesGenerated.equals=" + DEFAULT_FILES_GENERATED, "filesGenerated.equals=" + UPDATED_FILES_GENERATED);
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated in
        defaultExportJobFiltering(
            "filesGenerated.in=" + DEFAULT_FILES_GENERATED + "," + UPDATED_FILES_GENERATED,
            "filesGenerated.in=" + UPDATED_FILES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated is not null
        defaultExportJobFiltering("filesGenerated.specified=true", "filesGenerated.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated is greater than or equal to
        defaultExportJobFiltering(
            "filesGenerated.greaterThanOrEqual=" + DEFAULT_FILES_GENERATED,
            "filesGenerated.greaterThanOrEqual=" + UPDATED_FILES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated is less than or equal to
        defaultExportJobFiltering(
            "filesGenerated.lessThanOrEqual=" + DEFAULT_FILES_GENERATED,
            "filesGenerated.lessThanOrEqual=" + SMALLER_FILES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated is less than
        defaultExportJobFiltering(
            "filesGenerated.lessThan=" + UPDATED_FILES_GENERATED,
            "filesGenerated.lessThan=" + DEFAULT_FILES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByFilesGeneratedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where filesGenerated is greater than
        defaultExportJobFiltering(
            "filesGenerated.greaterThan=" + SMALLER_FILES_GENERATED,
            "filesGenerated.greaterThan=" + DEFAULT_FILES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where status equals to
        defaultExportJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllExportJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where status in
        defaultExportJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllExportJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where status is not null
        defaultExportJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where startDate equals to
        defaultExportJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllExportJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where startDate in
        defaultExportJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllExportJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where startDate is not null
        defaultExportJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where endDate equals to
        defaultExportJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllExportJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where endDate in
        defaultExportJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllExportJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where endDate is not null
        defaultExportJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdBy equals to
        defaultExportJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdBy in
        defaultExportJobFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdBy is not null
        defaultExportJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdBy contains
        defaultExportJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdBy does not contain
        defaultExportJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdDate equals to
        defaultExportJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdDate in
        defaultExportJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllExportJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        // Get all the exportJobList where createdDate is not null
        defaultExportJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllExportJobsByExportPatternIsEqualToSomething() throws Exception {
        ExportPattern exportPattern;
        if (TestUtil.findAll(em, ExportPattern.class).isEmpty()) {
            exportJobRepository.saveAndFlush(exportJob);
            exportPattern = ExportPatternResourceIT.createEntity();
        } else {
            exportPattern = TestUtil.findAll(em, ExportPattern.class).get(0);
        }
        em.persist(exportPattern);
        em.flush();
        exportJob.setExportPattern(exportPattern);
        exportJobRepository.saveAndFlush(exportJob);
        Long exportPatternId = exportPattern.getId();
        // Get all the exportJobList where exportPattern equals to exportPatternId
        defaultExportJobShouldBeFound("exportPatternId.equals=" + exportPatternId);

        // Get all the exportJobList where exportPattern equals to (exportPatternId + 1)
        defaultExportJobShouldNotBeFound("exportPatternId.equals=" + (exportPatternId + 1));
    }

    private void defaultExportJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultExportJobShouldBeFound(shouldBeFound);
        defaultExportJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultExportJobShouldBeFound(String filter) throws Exception {
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exportJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentQuery").value(hasItem(DEFAULT_DOCUMENT_QUERY)))
            .andExpect(jsonPath("$.[*].exportPatternId").value(hasItem(DEFAULT_EXPORT_PATTERN_ID.intValue())))
            .andExpect(jsonPath("$.[*].exportFormat").value(hasItem(DEFAULT_EXPORT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].includeMetadata").value(hasItem(DEFAULT_INCLUDE_METADATA)))
            .andExpect(jsonPath("$.[*].includeVersions").value(hasItem(DEFAULT_INCLUDE_VERSIONS)))
            .andExpect(jsonPath("$.[*].includeComments").value(hasItem(DEFAULT_INCLUDE_COMMENTS)))
            .andExpect(jsonPath("$.[*].includeAuditTrail").value(hasItem(DEFAULT_INCLUDE_AUDIT_TRAIL)))
            .andExpect(jsonPath("$.[*].s3ExportKey").value(hasItem(DEFAULT_S_3_EXPORT_KEY)))
            .andExpect(jsonPath("$.[*].exportSize").value(hasItem(DEFAULT_EXPORT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].filesGenerated").value(hasItem(DEFAULT_FILES_GENERATED)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultExportJobShouldNotBeFound(String filter) throws Exception {
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restExportJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingExportJob() throws Exception {
        // Get the exportJob
        restExportJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExportJob() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportJob
        ExportJob updatedExportJob = exportJobRepository.findById(exportJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExportJob are not directly saved in db
        em.detach(updatedExportJob);
        updatedExportJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .exportPatternId(UPDATED_EXPORT_PATTERN_ID)
            .exportFormat(UPDATED_EXPORT_FORMAT)
            .includeMetadata(UPDATED_INCLUDE_METADATA)
            .includeVersions(UPDATED_INCLUDE_VERSIONS)
            .includeComments(UPDATED_INCLUDE_COMMENTS)
            .includeAuditTrail(UPDATED_INCLUDE_AUDIT_TRAIL)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .exportSize(UPDATED_EXPORT_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .filesGenerated(UPDATED_FILES_GENERATED)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(updatedExportJob);

        restExportJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExportJobToMatchAllProperties(updatedExportJob);
    }

    @Test
    @Transactional
    void putNonExistingExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExportJobWithPatch() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportJob using partial update
        ExportJob partialUpdatedExportJob = new ExportJob();
        partialUpdatedExportJob.setId(exportJob.getId());

        partialUpdatedExportJob
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .includeVersions(UPDATED_INCLUDE_VERSIONS)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .exportSize(UPDATED_EXPORT_SIZE)
            .filesGenerated(UPDATED_FILES_GENERATED)
            .startDate(UPDATED_START_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restExportJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportJob))
            )
            .andExpect(status().isOk());

        // Validate the ExportJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExportJob, exportJob),
            getPersistedExportJob(exportJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateExportJobWithPatch() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportJob using partial update
        ExportJob partialUpdatedExportJob = new ExportJob();
        partialUpdatedExportJob.setId(exportJob.getId());

        partialUpdatedExportJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .exportPatternId(UPDATED_EXPORT_PATTERN_ID)
            .exportFormat(UPDATED_EXPORT_FORMAT)
            .includeMetadata(UPDATED_INCLUDE_METADATA)
            .includeVersions(UPDATED_INCLUDE_VERSIONS)
            .includeComments(UPDATED_INCLUDE_COMMENTS)
            .includeAuditTrail(UPDATED_INCLUDE_AUDIT_TRAIL)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .exportSize(UPDATED_EXPORT_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .filesGenerated(UPDATED_FILES_GENERATED)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restExportJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportJob))
            )
            .andExpect(status().isOk());

        // Validate the ExportJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportJobUpdatableFieldsEquals(partialUpdatedExportJob, getPersistedExportJob(partialUpdatedExportJob));
    }

    @Test
    @Transactional
    void patchNonExistingExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exportJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExportJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportJob.setId(longCount.incrementAndGet());

        // Create the ExportJob
        ExportJobDTO exportJobDTO = exportJobMapper.toDto(exportJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(exportJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExportJob() throws Exception {
        // Initialize the database
        insertedExportJob = exportJobRepository.saveAndFlush(exportJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the exportJob
        restExportJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, exportJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return exportJobRepository.count();
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

    protected ExportJob getPersistedExportJob(ExportJob exportJob) {
        return exportJobRepository.findById(exportJob.getId()).orElseThrow();
    }

    protected void assertPersistedExportJobToMatchAllProperties(ExportJob expectedExportJob) {
        assertExportJobAllPropertiesEquals(expectedExportJob, getPersistedExportJob(expectedExportJob));
    }

    protected void assertPersistedExportJobToMatchUpdatableProperties(ExportJob expectedExportJob) {
        assertExportJobAllUpdatablePropertiesEquals(expectedExportJob, getPersistedExportJob(expectedExportJob));
    }
}
