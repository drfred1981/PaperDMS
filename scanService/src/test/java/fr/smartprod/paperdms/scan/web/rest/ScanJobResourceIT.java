package fr.smartprod.paperdms.scan.web.rest;

import static fr.smartprod.paperdms.scan.domain.ScanJobAsserts.*;
import static fr.smartprod.paperdms.scan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.scan.IntegrationTest;
import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanStatus;
import fr.smartprod.paperdms.scan.repository.ScanJobRepository;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanJobMapper;
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
 * Integration tests for the {@link ScanJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScanJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Long DEFAULT_SCANNER_CONFIG_ID = 1L;
    private static final Long UPDATED_SCANNER_CONFIG_ID = 2L;
    private static final Long SMALLER_SCANNER_CONFIG_ID = 1L - 1L;

    private static final Long DEFAULT_BATCH_ID = 1L;
    private static final Long UPDATED_BATCH_ID = 2L;
    private static final Long SMALLER_BATCH_ID = 1L - 1L;

    private static final Long DEFAULT_DOCUMENT_TYPE_ID = 1L;
    private static final Long UPDATED_DOCUMENT_TYPE_ID = 2L;
    private static final Long SMALLER_DOCUMENT_TYPE_ID = 1L - 1L;

    private static final Long DEFAULT_FOLDER_ID = 1L;
    private static final Long UPDATED_FOLDER_ID = 2L;
    private static final Long SMALLER_FOLDER_ID = 1L - 1L;

    private static final Integer DEFAULT_PAGE_COUNT = 1;
    private static final Integer UPDATED_PAGE_COUNT = 2;
    private static final Integer SMALLER_PAGE_COUNT = 1 - 1;

    private static final ScanStatus DEFAULT_STATUS = ScanStatus.PENDING;
    private static final ScanStatus UPDATED_STATUS = ScanStatus.SCANNING;

    private static final ColorMode DEFAULT_COLOR_MODE = ColorMode.BLACK_WHITE;
    private static final ColorMode UPDATED_COLOR_MODE = ColorMode.GRAYSCALE;

    private static final Integer DEFAULT_RESOLUTION = 1;
    private static final Integer UPDATED_RESOLUTION = 2;
    private static final Integer SMALLER_RESOLUTION = 1 - 1;

    private static final ScanFormat DEFAULT_FILE_FORMAT = ScanFormat.PDF;
    private static final ScanFormat UPDATED_FILE_FORMAT = ScanFormat.TIFF;

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

    private static final String ENTITY_API_URL = "/api/scan-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScanJobRepository scanJobRepository;

    @Autowired
    private ScanJobMapper scanJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScanJobMockMvc;

    private ScanJob scanJob;

    private ScanJob insertedScanJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScanJob createEntity(EntityManager em) {
        ScanJob scanJob = new ScanJob()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .scannerConfigId(DEFAULT_SCANNER_CONFIG_ID)
            .batchId(DEFAULT_BATCH_ID)
            .documentTypeId(DEFAULT_DOCUMENT_TYPE_ID)
            .folderId(DEFAULT_FOLDER_ID)
            .pageCount(DEFAULT_PAGE_COUNT)
            .status(DEFAULT_STATUS)
            .colorMode(DEFAULT_COLOR_MODE)
            .resolution(DEFAULT_RESOLUTION)
            .fileFormat(DEFAULT_FILE_FORMAT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        ScannerConfiguration scannerConfiguration;
        if (TestUtil.findAll(em, ScannerConfiguration.class).isEmpty()) {
            scannerConfiguration = ScannerConfigurationResourceIT.createEntity();
            em.persist(scannerConfiguration);
            em.flush();
        } else {
            scannerConfiguration = TestUtil.findAll(em, ScannerConfiguration.class).get(0);
        }
        scanJob.setScannerConfig(scannerConfiguration);
        return scanJob;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScanJob createUpdatedEntity(EntityManager em) {
        ScanJob updatedScanJob = new ScanJob()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .scannerConfigId(UPDATED_SCANNER_CONFIG_ID)
            .batchId(UPDATED_BATCH_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .folderId(UPDATED_FOLDER_ID)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .colorMode(UPDATED_COLOR_MODE)
            .resolution(UPDATED_RESOLUTION)
            .fileFormat(UPDATED_FILE_FORMAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        ScannerConfiguration scannerConfiguration;
        if (TestUtil.findAll(em, ScannerConfiguration.class).isEmpty()) {
            scannerConfiguration = ScannerConfigurationResourceIT.createUpdatedEntity();
            em.persist(scannerConfiguration);
            em.flush();
        } else {
            scannerConfiguration = TestUtil.findAll(em, ScannerConfiguration.class).get(0);
        }
        updatedScanJob.setScannerConfig(scannerConfiguration);
        return updatedScanJob;
    }

    @BeforeEach
    void initTest() {
        scanJob = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedScanJob != null) {
            scanJobRepository.delete(insertedScanJob);
            insertedScanJob = null;
        }
    }

    @Test
    @Transactional
    void createScanJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);
        var returnedScanJobDTO = om.readValue(
            restScanJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScanJobDTO.class
        );

        // Validate the ScanJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScanJob = scanJobMapper.toEntity(returnedScanJobDTO);
        assertScanJobUpdatableFieldsEquals(returnedScanJob, getPersistedScanJob(returnedScanJob));

        insertedScanJob = returnedScanJob;
    }

    @Test
    @Transactional
    void createScanJobWithExistingId() throws Exception {
        // Create the ScanJob with an existing ID
        scanJob.setId(1L);
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanJob.setName(null);

        // Create the ScanJob, which fails.
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScannerConfigIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanJob.setScannerConfigId(null);

        // Create the ScanJob, which fails.
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanJob.setStatus(null);

        // Create the ScanJob, which fails.
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanJob.setCreatedBy(null);

        // Create the ScanJob, which fails.
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanJob.setCreatedDate(null);

        // Create the ScanJob, which fails.
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        restScanJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScanJobs() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scanJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].scannerConfigId").value(hasItem(DEFAULT_SCANNER_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].batchId").value(hasItem(DEFAULT_BATCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].colorMode").value(hasItem(DEFAULT_COLOR_MODE.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)))
            .andExpect(jsonPath("$.[*].fileFormat").value(hasItem(DEFAULT_FILE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getScanJob() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get the scanJob
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL_ID, scanJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scanJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.scannerConfigId").value(DEFAULT_SCANNER_CONFIG_ID.intValue()))
            .andExpect(jsonPath("$.batchId").value(DEFAULT_BATCH_ID.intValue()))
            .andExpect(jsonPath("$.documentTypeId").value(DEFAULT_DOCUMENT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.folderId").value(DEFAULT_FOLDER_ID.intValue()))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.colorMode").value(DEFAULT_COLOR_MODE.toString()))
            .andExpect(jsonPath("$.resolution").value(DEFAULT_RESOLUTION))
            .andExpect(jsonPath("$.fileFormat").value(DEFAULT_FILE_FORMAT.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getScanJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        Long id = scanJob.getId();

        defaultScanJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultScanJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultScanJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScanJobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where name equals to
        defaultScanJobFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanJobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where name in
        defaultScanJobFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanJobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where name is not null
        defaultScanJobFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where name contains
        defaultScanJobFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanJobsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where name does not contain
        defaultScanJobFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId equals to
        defaultScanJobFiltering(
            "scannerConfigId.equals=" + DEFAULT_SCANNER_CONFIG_ID,
            "scannerConfigId.equals=" + UPDATED_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId in
        defaultScanJobFiltering(
            "scannerConfigId.in=" + DEFAULT_SCANNER_CONFIG_ID + "," + UPDATED_SCANNER_CONFIG_ID,
            "scannerConfigId.in=" + UPDATED_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId is not null
        defaultScanJobFiltering("scannerConfigId.specified=true", "scannerConfigId.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId is greater than or equal to
        defaultScanJobFiltering(
            "scannerConfigId.greaterThanOrEqual=" + DEFAULT_SCANNER_CONFIG_ID,
            "scannerConfigId.greaterThanOrEqual=" + UPDATED_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId is less than or equal to
        defaultScanJobFiltering(
            "scannerConfigId.lessThanOrEqual=" + DEFAULT_SCANNER_CONFIG_ID,
            "scannerConfigId.lessThanOrEqual=" + SMALLER_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId is less than
        defaultScanJobFiltering(
            "scannerConfigId.lessThan=" + UPDATED_SCANNER_CONFIG_ID,
            "scannerConfigId.lessThan=" + DEFAULT_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where scannerConfigId is greater than
        defaultScanJobFiltering(
            "scannerConfigId.greaterThan=" + SMALLER_SCANNER_CONFIG_ID,
            "scannerConfigId.greaterThan=" + DEFAULT_SCANNER_CONFIG_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId equals to
        defaultScanJobFiltering("batchId.equals=" + DEFAULT_BATCH_ID, "batchId.equals=" + UPDATED_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId in
        defaultScanJobFiltering("batchId.in=" + DEFAULT_BATCH_ID + "," + UPDATED_BATCH_ID, "batchId.in=" + UPDATED_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId is not null
        defaultScanJobFiltering("batchId.specified=true", "batchId.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId is greater than or equal to
        defaultScanJobFiltering("batchId.greaterThanOrEqual=" + DEFAULT_BATCH_ID, "batchId.greaterThanOrEqual=" + UPDATED_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId is less than or equal to
        defaultScanJobFiltering("batchId.lessThanOrEqual=" + DEFAULT_BATCH_ID, "batchId.lessThanOrEqual=" + SMALLER_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId is less than
        defaultScanJobFiltering("batchId.lessThan=" + UPDATED_BATCH_ID, "batchId.lessThan=" + DEFAULT_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where batchId is greater than
        defaultScanJobFiltering("batchId.greaterThan=" + SMALLER_BATCH_ID, "batchId.greaterThan=" + DEFAULT_BATCH_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId equals to
        defaultScanJobFiltering("documentTypeId.equals=" + DEFAULT_DOCUMENT_TYPE_ID, "documentTypeId.equals=" + UPDATED_DOCUMENT_TYPE_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId in
        defaultScanJobFiltering(
            "documentTypeId.in=" + DEFAULT_DOCUMENT_TYPE_ID + "," + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.in=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId is not null
        defaultScanJobFiltering("documentTypeId.specified=true", "documentTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId is greater than or equal to
        defaultScanJobFiltering(
            "documentTypeId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThanOrEqual=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId is less than or equal to
        defaultScanJobFiltering(
            "documentTypeId.lessThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThanOrEqual=" + SMALLER_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId is less than
        defaultScanJobFiltering(
            "documentTypeId.lessThan=" + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByDocumentTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where documentTypeId is greater than
        defaultScanJobFiltering(
            "documentTypeId.greaterThan=" + SMALLER_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId equals to
        defaultScanJobFiltering("folderId.equals=" + DEFAULT_FOLDER_ID, "folderId.equals=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId in
        defaultScanJobFiltering("folderId.in=" + DEFAULT_FOLDER_ID + "," + UPDATED_FOLDER_ID, "folderId.in=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId is not null
        defaultScanJobFiltering("folderId.specified=true", "folderId.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId is greater than or equal to
        defaultScanJobFiltering("folderId.greaterThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.greaterThanOrEqual=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId is less than or equal to
        defaultScanJobFiltering("folderId.lessThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.lessThanOrEqual=" + SMALLER_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId is less than
        defaultScanJobFiltering("folderId.lessThan=" + UPDATED_FOLDER_ID, "folderId.lessThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByFolderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where folderId is greater than
        defaultScanJobFiltering("folderId.greaterThan=" + SMALLER_FOLDER_ID, "folderId.greaterThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount equals to
        defaultScanJobFiltering("pageCount.equals=" + DEFAULT_PAGE_COUNT, "pageCount.equals=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount in
        defaultScanJobFiltering("pageCount.in=" + DEFAULT_PAGE_COUNT + "," + UPDATED_PAGE_COUNT, "pageCount.in=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount is not null
        defaultScanJobFiltering("pageCount.specified=true", "pageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount is greater than or equal to
        defaultScanJobFiltering("pageCount.greaterThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.greaterThanOrEqual=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount is less than or equal to
        defaultScanJobFiltering("pageCount.lessThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.lessThanOrEqual=" + SMALLER_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount is less than
        defaultScanJobFiltering("pageCount.lessThan=" + UPDATED_PAGE_COUNT, "pageCount.lessThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByPageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where pageCount is greater than
        defaultScanJobFiltering("pageCount.greaterThan=" + SMALLER_PAGE_COUNT, "pageCount.greaterThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllScanJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where status equals to
        defaultScanJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllScanJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where status in
        defaultScanJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllScanJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where status is not null
        defaultScanJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByColorModeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where colorMode equals to
        defaultScanJobFiltering("colorMode.equals=" + DEFAULT_COLOR_MODE, "colorMode.equals=" + UPDATED_COLOR_MODE);
    }

    @Test
    @Transactional
    void getAllScanJobsByColorModeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where colorMode in
        defaultScanJobFiltering("colorMode.in=" + DEFAULT_COLOR_MODE + "," + UPDATED_COLOR_MODE, "colorMode.in=" + UPDATED_COLOR_MODE);
    }

    @Test
    @Transactional
    void getAllScanJobsByColorModeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where colorMode is not null
        defaultScanJobFiltering("colorMode.specified=true", "colorMode.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution equals to
        defaultScanJobFiltering("resolution.equals=" + DEFAULT_RESOLUTION, "resolution.equals=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution in
        defaultScanJobFiltering("resolution.in=" + DEFAULT_RESOLUTION + "," + UPDATED_RESOLUTION, "resolution.in=" + UPDATED_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution is not null
        defaultScanJobFiltering("resolution.specified=true", "resolution.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution is greater than or equal to
        defaultScanJobFiltering(
            "resolution.greaterThanOrEqual=" + DEFAULT_RESOLUTION,
            "resolution.greaterThanOrEqual=" + UPDATED_RESOLUTION
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution is less than or equal to
        defaultScanJobFiltering("resolution.lessThanOrEqual=" + DEFAULT_RESOLUTION, "resolution.lessThanOrEqual=" + SMALLER_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution is less than
        defaultScanJobFiltering("resolution.lessThan=" + UPDATED_RESOLUTION, "resolution.lessThan=" + DEFAULT_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllScanJobsByResolutionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where resolution is greater than
        defaultScanJobFiltering("resolution.greaterThan=" + SMALLER_RESOLUTION, "resolution.greaterThan=" + DEFAULT_RESOLUTION);
    }

    @Test
    @Transactional
    void getAllScanJobsByFileFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where fileFormat equals to
        defaultScanJobFiltering("fileFormat.equals=" + DEFAULT_FILE_FORMAT, "fileFormat.equals=" + UPDATED_FILE_FORMAT);
    }

    @Test
    @Transactional
    void getAllScanJobsByFileFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where fileFormat in
        defaultScanJobFiltering("fileFormat.in=" + DEFAULT_FILE_FORMAT + "," + UPDATED_FILE_FORMAT, "fileFormat.in=" + UPDATED_FILE_FORMAT);
    }

    @Test
    @Transactional
    void getAllScanJobsByFileFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where fileFormat is not null
        defaultScanJobFiltering("fileFormat.specified=true", "fileFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where startDate equals to
        defaultScanJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScanJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where startDate in
        defaultScanJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllScanJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where startDate is not null
        defaultScanJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where endDate equals to
        defaultScanJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScanJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where endDate in
        defaultScanJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllScanJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where endDate is not null
        defaultScanJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdBy equals to
        defaultScanJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdBy in
        defaultScanJobFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdBy is not null
        defaultScanJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdBy contains
        defaultScanJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdBy does not contain
        defaultScanJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdDate equals to
        defaultScanJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdDate in
        defaultScanJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllScanJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        // Get all the scanJobList where createdDate is not null
        defaultScanJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllScanJobsByScannerConfigIsEqualToSomething() throws Exception {
        ScannerConfiguration scannerConfig;
        if (TestUtil.findAll(em, ScannerConfiguration.class).isEmpty()) {
            scanJobRepository.saveAndFlush(scanJob);
            scannerConfig = ScannerConfigurationResourceIT.createEntity();
        } else {
            scannerConfig = TestUtil.findAll(em, ScannerConfiguration.class).get(0);
        }
        em.persist(scannerConfig);
        em.flush();
        scanJob.setScannerConfig(scannerConfig);
        scanJobRepository.saveAndFlush(scanJob);
        Long scannerConfigId = scannerConfig.getId();
        // Get all the scanJobList where scannerConfig equals to scannerConfigId
        defaultScanJobShouldBeFound("scannerConfigId.equals=" + scannerConfigId);

        // Get all the scanJobList where scannerConfig equals to (scannerConfigId + 1)
        defaultScanJobShouldNotBeFound("scannerConfigId.equals=" + (scannerConfigId + 1));
    }

    @Test
    @Transactional
    void getAllScanJobsByBatchIsEqualToSomething() throws Exception {
        ScanBatch batch;
        if (TestUtil.findAll(em, ScanBatch.class).isEmpty()) {
            scanJobRepository.saveAndFlush(scanJob);
            batch = ScanBatchResourceIT.createEntity();
        } else {
            batch = TestUtil.findAll(em, ScanBatch.class).get(0);
        }
        em.persist(batch);
        em.flush();
        scanJob.setBatch(batch);
        scanJobRepository.saveAndFlush(scanJob);
        Long batchId = batch.getId();
        // Get all the scanJobList where batch equals to batchId
        defaultScanJobShouldBeFound("batchId.equals=" + batchId);

        // Get all the scanJobList where batch equals to (batchId + 1)
        defaultScanJobShouldNotBeFound("batchId.equals=" + (batchId + 1));
    }

    private void defaultScanJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultScanJobShouldBeFound(shouldBeFound);
        defaultScanJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScanJobShouldBeFound(String filter) throws Exception {
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scanJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].scannerConfigId").value(hasItem(DEFAULT_SCANNER_CONFIG_ID.intValue())))
            .andExpect(jsonPath("$.[*].batchId").value(hasItem(DEFAULT_BATCH_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].colorMode").value(hasItem(DEFAULT_COLOR_MODE.toString())))
            .andExpect(jsonPath("$.[*].resolution").value(hasItem(DEFAULT_RESOLUTION)))
            .andExpect(jsonPath("$.[*].fileFormat").value(hasItem(DEFAULT_FILE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScanJobShouldNotBeFound(String filter) throws Exception {
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScanJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScanJob() throws Exception {
        // Get the scanJob
        restScanJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScanJob() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanJob
        ScanJob updatedScanJob = scanJobRepository.findById(scanJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScanJob are not directly saved in db
        em.detach(updatedScanJob);
        updatedScanJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .scannerConfigId(UPDATED_SCANNER_CONFIG_ID)
            .batchId(UPDATED_BATCH_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .folderId(UPDATED_FOLDER_ID)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .colorMode(UPDATED_COLOR_MODE)
            .resolution(UPDATED_RESOLUTION)
            .fileFormat(UPDATED_FILE_FORMAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(updatedScanJob);

        restScanJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scanJobDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScanJobToMatchAllProperties(updatedScanJob);
    }

    @Test
    @Transactional
    void putNonExistingScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scanJobDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scanJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScanJobWithPatch() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanJob using partial update
        ScanJob partialUpdatedScanJob = new ScanJob();
        partialUpdatedScanJob.setId(scanJob.getId());

        partialUpdatedScanJob
            .scannerConfigId(UPDATED_SCANNER_CONFIG_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restScanJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScanJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScanJob))
            )
            .andExpect(status().isOk());

        // Validate the ScanJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScanJobUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedScanJob, scanJob), getPersistedScanJob(scanJob));
    }

    @Test
    @Transactional
    void fullUpdateScanJobWithPatch() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanJob using partial update
        ScanJob partialUpdatedScanJob = new ScanJob();
        partialUpdatedScanJob.setId(scanJob.getId());

        partialUpdatedScanJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .scannerConfigId(UPDATED_SCANNER_CONFIG_ID)
            .batchId(UPDATED_BATCH_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .folderId(UPDATED_FOLDER_ID)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .colorMode(UPDATED_COLOR_MODE)
            .resolution(UPDATED_RESOLUTION)
            .fileFormat(UPDATED_FILE_FORMAT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restScanJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScanJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScanJob))
            )
            .andExpect(status().isOk());

        // Validate the ScanJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScanJobUpdatableFieldsEquals(partialUpdatedScanJob, getPersistedScanJob(partialUpdatedScanJob));
    }

    @Test
    @Transactional
    void patchNonExistingScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scanJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scanJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scanJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScanJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanJob.setId(longCount.incrementAndGet());

        // Create the ScanJob
        ScanJobDTO scanJobDTO = scanJobMapper.toDto(scanJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scanJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScanJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScanJob() throws Exception {
        // Initialize the database
        insertedScanJob = scanJobRepository.saveAndFlush(scanJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scanJob
        restScanJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, scanJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scanJobRepository.count();
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

    protected ScanJob getPersistedScanJob(ScanJob scanJob) {
        return scanJobRepository.findById(scanJob.getId()).orElseThrow();
    }

    protected void assertPersistedScanJobToMatchAllProperties(ScanJob expectedScanJob) {
        assertScanJobAllPropertiesEquals(expectedScanJob, getPersistedScanJob(expectedScanJob));
    }

    protected void assertPersistedScanJobToMatchUpdatableProperties(ScanJob expectedScanJob) {
        assertScanJobAllUpdatablePropertiesEquals(expectedScanJob, getPersistedScanJob(expectedScanJob));
    }
}
