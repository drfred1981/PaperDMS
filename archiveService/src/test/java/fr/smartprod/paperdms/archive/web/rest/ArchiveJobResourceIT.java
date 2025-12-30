package fr.smartprod.paperdms.archive.web.rest;

import static fr.smartprod.paperdms.archive.domain.ArchiveJobAsserts.*;
import static fr.smartprod.paperdms.archive.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.archive.IntegrationTest;
import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveFormat;
import fr.smartprod.paperdms.archive.domain.enumeration.ArchiveStatus;
import fr.smartprod.paperdms.archive.repository.ArchiveJobRepository;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveJobMapper;
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
 * Integration tests for the {@link ArchiveJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArchiveJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_QUERY = "BBBBBBBBBB";

    private static final ArchiveFormat DEFAULT_ARCHIVE_FORMAT = ArchiveFormat.ZIP;
    private static final ArchiveFormat UPDATED_ARCHIVE_FORMAT = ArchiveFormat.TAR;

    private static final Integer DEFAULT_COMPRESSION_LEVEL = 0;
    private static final Integer UPDATED_COMPRESSION_LEVEL = 1;
    private static final Integer SMALLER_COMPRESSION_LEVEL = 0 - 1;

    private static final Boolean DEFAULT_ENCRYPTION_ENABLED = false;
    private static final Boolean UPDATED_ENCRYPTION_ENABLED = true;

    private static final String DEFAULT_ENCRYPTION_ALGORITHM = "AAAAAAAAAA";
    private static final String UPDATED_ENCRYPTION_ALGORITHM = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_ARCHIVE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_ARCHIVE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_ARCHIVE_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVE_SHA_256 = "BBBBBBBBBB";

    private static final Long DEFAULT_ARCHIVE_SIZE = 1L;
    private static final Long UPDATED_ARCHIVE_SIZE = 2L;
    private static final Long SMALLER_ARCHIVE_SIZE = 1L - 1L;

    private static final Integer DEFAULT_DOCUMENT_COUNT = 1;
    private static final Integer UPDATED_DOCUMENT_COUNT = 2;
    private static final Integer SMALLER_DOCUMENT_COUNT = 1 - 1;

    private static final ArchiveStatus DEFAULT_STATUS = ArchiveStatus.PENDING;
    private static final ArchiveStatus UPDATED_STATUS = ArchiveStatus.ARCHIVING;

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

    private static final String ENTITY_API_URL = "/api/archive-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArchiveJobRepository archiveJobRepository;

    @Autowired
    private ArchiveJobMapper archiveJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchiveJobMockMvc;

    private ArchiveJob archiveJob;

    private ArchiveJob insertedArchiveJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveJob createEntity() {
        return new ArchiveJob()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .documentQuery(DEFAULT_DOCUMENT_QUERY)
            .archiveFormat(DEFAULT_ARCHIVE_FORMAT)
            .compressionLevel(DEFAULT_COMPRESSION_LEVEL)
            .encryptionEnabled(DEFAULT_ENCRYPTION_ENABLED)
            .encryptionAlgorithm(DEFAULT_ENCRYPTION_ALGORITHM)
            .password(DEFAULT_PASSWORD)
            .s3ArchiveKey(DEFAULT_S_3_ARCHIVE_KEY)
            .archiveSha256(DEFAULT_ARCHIVE_SHA_256)
            .archiveSize(DEFAULT_ARCHIVE_SIZE)
            .documentCount(DEFAULT_DOCUMENT_COUNT)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveJob createUpdatedEntity() {
        return new ArchiveJob()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .archiveFormat(UPDATED_ARCHIVE_FORMAT)
            .compressionLevel(UPDATED_COMPRESSION_LEVEL)
            .encryptionEnabled(UPDATED_ENCRYPTION_ENABLED)
            .encryptionAlgorithm(UPDATED_ENCRYPTION_ALGORITHM)
            .password(UPDATED_PASSWORD)
            .s3ArchiveKey(UPDATED_S_3_ARCHIVE_KEY)
            .archiveSha256(UPDATED_ARCHIVE_SHA_256)
            .archiveSize(UPDATED_ARCHIVE_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        archiveJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedArchiveJob != null) {
            archiveJobRepository.delete(insertedArchiveJob);
            insertedArchiveJob = null;
        }
    }

    @Test
    @Transactional
    void createArchiveJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);
        var returnedArchiveJobDTO = om.readValue(
            restArchiveJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArchiveJobDTO.class
        );

        // Validate the ArchiveJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArchiveJob = archiveJobMapper.toEntity(returnedArchiveJobDTO);
        assertArchiveJobUpdatableFieldsEquals(returnedArchiveJob, getPersistedArchiveJob(returnedArchiveJob));

        insertedArchiveJob = returnedArchiveJob;
    }

    @Test
    @Transactional
    void createArchiveJobWithExistingId() throws Exception {
        // Create the ArchiveJob with an existing ID
        archiveJob.setId(1L);
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setName(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkArchiveFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setArchiveFormat(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEncryptionEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setEncryptionEnabled(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setStatus(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setCreatedBy(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveJob.setCreatedDate(null);

        // Create the ArchiveJob, which fails.
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        restArchiveJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArchiveJobs() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentQuery").value(hasItem(DEFAULT_DOCUMENT_QUERY)))
            .andExpect(jsonPath("$.[*].archiveFormat").value(hasItem(DEFAULT_ARCHIVE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].compressionLevel").value(hasItem(DEFAULT_COMPRESSION_LEVEL)))
            .andExpect(jsonPath("$.[*].encryptionEnabled").value(hasItem(DEFAULT_ENCRYPTION_ENABLED)))
            .andExpect(jsonPath("$.[*].encryptionAlgorithm").value(hasItem(DEFAULT_ENCRYPTION_ALGORITHM)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].s3ArchiveKey").value(hasItem(DEFAULT_S_3_ARCHIVE_KEY)))
            .andExpect(jsonPath("$.[*].archiveSha256").value(hasItem(DEFAULT_ARCHIVE_SHA_256)))
            .andExpect(jsonPath("$.[*].archiveSize").value(hasItem(DEFAULT_ARCHIVE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getArchiveJob() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get the archiveJob
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL_ID, archiveJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archiveJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.documentQuery").value(DEFAULT_DOCUMENT_QUERY))
            .andExpect(jsonPath("$.archiveFormat").value(DEFAULT_ARCHIVE_FORMAT.toString()))
            .andExpect(jsonPath("$.compressionLevel").value(DEFAULT_COMPRESSION_LEVEL))
            .andExpect(jsonPath("$.encryptionEnabled").value(DEFAULT_ENCRYPTION_ENABLED))
            .andExpect(jsonPath("$.encryptionAlgorithm").value(DEFAULT_ENCRYPTION_ALGORITHM))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD))
            .andExpect(jsonPath("$.s3ArchiveKey").value(DEFAULT_S_3_ARCHIVE_KEY))
            .andExpect(jsonPath("$.archiveSha256").value(DEFAULT_ARCHIVE_SHA_256))
            .andExpect(jsonPath("$.archiveSize").value(DEFAULT_ARCHIVE_SIZE.intValue()))
            .andExpect(jsonPath("$.documentCount").value(DEFAULT_DOCUMENT_COUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getArchiveJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        Long id = archiveJob.getId();

        defaultArchiveJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArchiveJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArchiveJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where name equals to
        defaultArchiveJobFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where name in
        defaultArchiveJobFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where name is not null
        defaultArchiveJobFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where name contains
        defaultArchiveJobFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where name does not contain
        defaultArchiveJobFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveFormat equals to
        defaultArchiveJobFiltering("archiveFormat.equals=" + DEFAULT_ARCHIVE_FORMAT, "archiveFormat.equals=" + UPDATED_ARCHIVE_FORMAT);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveFormat in
        defaultArchiveJobFiltering(
            "archiveFormat.in=" + DEFAULT_ARCHIVE_FORMAT + "," + UPDATED_ARCHIVE_FORMAT,
            "archiveFormat.in=" + UPDATED_ARCHIVE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveFormat is not null
        defaultArchiveJobFiltering("archiveFormat.specified=true", "archiveFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel equals to
        defaultArchiveJobFiltering(
            "compressionLevel.equals=" + DEFAULT_COMPRESSION_LEVEL,
            "compressionLevel.equals=" + UPDATED_COMPRESSION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel in
        defaultArchiveJobFiltering(
            "compressionLevel.in=" + DEFAULT_COMPRESSION_LEVEL + "," + UPDATED_COMPRESSION_LEVEL,
            "compressionLevel.in=" + UPDATED_COMPRESSION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel is not null
        defaultArchiveJobFiltering("compressionLevel.specified=true", "compressionLevel.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel is greater than or equal to
        defaultArchiveJobFiltering(
            "compressionLevel.greaterThanOrEqual=" + DEFAULT_COMPRESSION_LEVEL,
            "compressionLevel.greaterThanOrEqual=" + (DEFAULT_COMPRESSION_LEVEL + 1)
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel is less than or equal to
        defaultArchiveJobFiltering(
            "compressionLevel.lessThanOrEqual=" + DEFAULT_COMPRESSION_LEVEL,
            "compressionLevel.lessThanOrEqual=" + SMALLER_COMPRESSION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel is less than
        defaultArchiveJobFiltering(
            "compressionLevel.lessThan=" + (DEFAULT_COMPRESSION_LEVEL + 1),
            "compressionLevel.lessThan=" + DEFAULT_COMPRESSION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCompressionLevelIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where compressionLevel is greater than
        defaultArchiveJobFiltering(
            "compressionLevel.greaterThan=" + SMALLER_COMPRESSION_LEVEL,
            "compressionLevel.greaterThan=" + DEFAULT_COMPRESSION_LEVEL
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionEnabledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionEnabled equals to
        defaultArchiveJobFiltering(
            "encryptionEnabled.equals=" + DEFAULT_ENCRYPTION_ENABLED,
            "encryptionEnabled.equals=" + UPDATED_ENCRYPTION_ENABLED
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionEnabledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionEnabled in
        defaultArchiveJobFiltering(
            "encryptionEnabled.in=" + DEFAULT_ENCRYPTION_ENABLED + "," + UPDATED_ENCRYPTION_ENABLED,
            "encryptionEnabled.in=" + UPDATED_ENCRYPTION_ENABLED
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionEnabledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionEnabled is not null
        defaultArchiveJobFiltering("encryptionEnabled.specified=true", "encryptionEnabled.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionAlgorithm equals to
        defaultArchiveJobFiltering(
            "encryptionAlgorithm.equals=" + DEFAULT_ENCRYPTION_ALGORITHM,
            "encryptionAlgorithm.equals=" + UPDATED_ENCRYPTION_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionAlgorithmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionAlgorithm in
        defaultArchiveJobFiltering(
            "encryptionAlgorithm.in=" + DEFAULT_ENCRYPTION_ALGORITHM + "," + UPDATED_ENCRYPTION_ALGORITHM,
            "encryptionAlgorithm.in=" + UPDATED_ENCRYPTION_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionAlgorithmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionAlgorithm is not null
        defaultArchiveJobFiltering("encryptionAlgorithm.specified=true", "encryptionAlgorithm.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionAlgorithmContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionAlgorithm contains
        defaultArchiveJobFiltering(
            "encryptionAlgorithm.contains=" + DEFAULT_ENCRYPTION_ALGORITHM,
            "encryptionAlgorithm.contains=" + UPDATED_ENCRYPTION_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEncryptionAlgorithmNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where encryptionAlgorithm does not contain
        defaultArchiveJobFiltering(
            "encryptionAlgorithm.doesNotContain=" + UPDATED_ENCRYPTION_ALGORITHM,
            "encryptionAlgorithm.doesNotContain=" + DEFAULT_ENCRYPTION_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByPasswordIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where password equals to
        defaultArchiveJobFiltering("password.equals=" + DEFAULT_PASSWORD, "password.equals=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByPasswordIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where password in
        defaultArchiveJobFiltering("password.in=" + DEFAULT_PASSWORD + "," + UPDATED_PASSWORD, "password.in=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByPasswordIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where password is not null
        defaultArchiveJobFiltering("password.specified=true", "password.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByPasswordContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where password contains
        defaultArchiveJobFiltering("password.contains=" + DEFAULT_PASSWORD, "password.contains=" + UPDATED_PASSWORD);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByPasswordNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where password does not contain
        defaultArchiveJobFiltering("password.doesNotContain=" + UPDATED_PASSWORD, "password.doesNotContain=" + DEFAULT_PASSWORD);
    }

    @Test
    @Transactional
    void getAllArchiveJobsBys3ArchiveKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where s3ArchiveKey equals to
        defaultArchiveJobFiltering("s3ArchiveKey.equals=" + DEFAULT_S_3_ARCHIVE_KEY, "s3ArchiveKey.equals=" + UPDATED_S_3_ARCHIVE_KEY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsBys3ArchiveKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where s3ArchiveKey in
        defaultArchiveJobFiltering(
            "s3ArchiveKey.in=" + DEFAULT_S_3_ARCHIVE_KEY + "," + UPDATED_S_3_ARCHIVE_KEY,
            "s3ArchiveKey.in=" + UPDATED_S_3_ARCHIVE_KEY
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsBys3ArchiveKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where s3ArchiveKey is not null
        defaultArchiveJobFiltering("s3ArchiveKey.specified=true", "s3ArchiveKey.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsBys3ArchiveKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where s3ArchiveKey contains
        defaultArchiveJobFiltering("s3ArchiveKey.contains=" + DEFAULT_S_3_ARCHIVE_KEY, "s3ArchiveKey.contains=" + UPDATED_S_3_ARCHIVE_KEY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsBys3ArchiveKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where s3ArchiveKey does not contain
        defaultArchiveJobFiltering(
            "s3ArchiveKey.doesNotContain=" + UPDATED_S_3_ARCHIVE_KEY,
            "s3ArchiveKey.doesNotContain=" + DEFAULT_S_3_ARCHIVE_KEY
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSha256 equals to
        defaultArchiveJobFiltering("archiveSha256.equals=" + DEFAULT_ARCHIVE_SHA_256, "archiveSha256.equals=" + UPDATED_ARCHIVE_SHA_256);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSha256 in
        defaultArchiveJobFiltering(
            "archiveSha256.in=" + DEFAULT_ARCHIVE_SHA_256 + "," + UPDATED_ARCHIVE_SHA_256,
            "archiveSha256.in=" + UPDATED_ARCHIVE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSha256 is not null
        defaultArchiveJobFiltering("archiveSha256.specified=true", "archiveSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSha256 contains
        defaultArchiveJobFiltering(
            "archiveSha256.contains=" + DEFAULT_ARCHIVE_SHA_256,
            "archiveSha256.contains=" + UPDATED_ARCHIVE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSha256 does not contain
        defaultArchiveJobFiltering(
            "archiveSha256.doesNotContain=" + UPDATED_ARCHIVE_SHA_256,
            "archiveSha256.doesNotContain=" + DEFAULT_ARCHIVE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize equals to
        defaultArchiveJobFiltering("archiveSize.equals=" + DEFAULT_ARCHIVE_SIZE, "archiveSize.equals=" + UPDATED_ARCHIVE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize in
        defaultArchiveJobFiltering(
            "archiveSize.in=" + DEFAULT_ARCHIVE_SIZE + "," + UPDATED_ARCHIVE_SIZE,
            "archiveSize.in=" + UPDATED_ARCHIVE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize is not null
        defaultArchiveJobFiltering("archiveSize.specified=true", "archiveSize.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize is greater than or equal to
        defaultArchiveJobFiltering(
            "archiveSize.greaterThanOrEqual=" + DEFAULT_ARCHIVE_SIZE,
            "archiveSize.greaterThanOrEqual=" + UPDATED_ARCHIVE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize is less than or equal to
        defaultArchiveJobFiltering(
            "archiveSize.lessThanOrEqual=" + DEFAULT_ARCHIVE_SIZE,
            "archiveSize.lessThanOrEqual=" + SMALLER_ARCHIVE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize is less than
        defaultArchiveJobFiltering("archiveSize.lessThan=" + UPDATED_ARCHIVE_SIZE, "archiveSize.lessThan=" + DEFAULT_ARCHIVE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByArchiveSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where archiveSize is greater than
        defaultArchiveJobFiltering("archiveSize.greaterThan=" + SMALLER_ARCHIVE_SIZE, "archiveSize.greaterThan=" + DEFAULT_ARCHIVE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount equals to
        defaultArchiveJobFiltering("documentCount.equals=" + DEFAULT_DOCUMENT_COUNT, "documentCount.equals=" + UPDATED_DOCUMENT_COUNT);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount in
        defaultArchiveJobFiltering(
            "documentCount.in=" + DEFAULT_DOCUMENT_COUNT + "," + UPDATED_DOCUMENT_COUNT,
            "documentCount.in=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount is not null
        defaultArchiveJobFiltering("documentCount.specified=true", "documentCount.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount is greater than or equal to
        defaultArchiveJobFiltering(
            "documentCount.greaterThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.greaterThanOrEqual=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount is less than or equal to
        defaultArchiveJobFiltering(
            "documentCount.lessThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.lessThanOrEqual=" + SMALLER_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount is less than
        defaultArchiveJobFiltering("documentCount.lessThan=" + UPDATED_DOCUMENT_COUNT, "documentCount.lessThan=" + DEFAULT_DOCUMENT_COUNT);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByDocumentCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where documentCount is greater than
        defaultArchiveJobFiltering(
            "documentCount.greaterThan=" + SMALLER_DOCUMENT_COUNT,
            "documentCount.greaterThan=" + DEFAULT_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where status equals to
        defaultArchiveJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where status in
        defaultArchiveJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where status is not null
        defaultArchiveJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where startDate equals to
        defaultArchiveJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where startDate in
        defaultArchiveJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where startDate is not null
        defaultArchiveJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where endDate equals to
        defaultArchiveJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where endDate in
        defaultArchiveJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where endDate is not null
        defaultArchiveJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdBy equals to
        defaultArchiveJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdBy in
        defaultArchiveJobFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdBy is not null
        defaultArchiveJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdBy contains
        defaultArchiveJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdBy does not contain
        defaultArchiveJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdDate equals to
        defaultArchiveJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdDate in
        defaultArchiveJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllArchiveJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        // Get all the archiveJobList where createdDate is not null
        defaultArchiveJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultArchiveJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArchiveJobShouldBeFound(shouldBeFound);
        defaultArchiveJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArchiveJobShouldBeFound(String filter) throws Exception {
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].documentQuery").value(hasItem(DEFAULT_DOCUMENT_QUERY)))
            .andExpect(jsonPath("$.[*].archiveFormat").value(hasItem(DEFAULT_ARCHIVE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].compressionLevel").value(hasItem(DEFAULT_COMPRESSION_LEVEL)))
            .andExpect(jsonPath("$.[*].encryptionEnabled").value(hasItem(DEFAULT_ENCRYPTION_ENABLED)))
            .andExpect(jsonPath("$.[*].encryptionAlgorithm").value(hasItem(DEFAULT_ENCRYPTION_ALGORITHM)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)))
            .andExpect(jsonPath("$.[*].s3ArchiveKey").value(hasItem(DEFAULT_S_3_ARCHIVE_KEY)))
            .andExpect(jsonPath("$.[*].archiveSha256").value(hasItem(DEFAULT_ARCHIVE_SHA_256)))
            .andExpect(jsonPath("$.[*].archiveSize").value(hasItem(DEFAULT_ARCHIVE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArchiveJobShouldNotBeFound(String filter) throws Exception {
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArchiveJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArchiveJob() throws Exception {
        // Get the archiveJob
        restArchiveJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArchiveJob() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveJob
        ArchiveJob updatedArchiveJob = archiveJobRepository.findById(archiveJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArchiveJob are not directly saved in db
        em.detach(updatedArchiveJob);
        updatedArchiveJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .archiveFormat(UPDATED_ARCHIVE_FORMAT)
            .compressionLevel(UPDATED_COMPRESSION_LEVEL)
            .encryptionEnabled(UPDATED_ENCRYPTION_ENABLED)
            .encryptionAlgorithm(UPDATED_ENCRYPTION_ALGORITHM)
            .password(UPDATED_PASSWORD)
            .s3ArchiveKey(UPDATED_S_3_ARCHIVE_KEY)
            .archiveSha256(UPDATED_ARCHIVE_SHA_256)
            .archiveSize(UPDATED_ARCHIVE_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(updatedArchiveJob);

        restArchiveJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArchiveJobToMatchAllProperties(updatedArchiveJob);
    }

    @Test
    @Transactional
    void putNonExistingArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchiveJobWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveJob using partial update
        ArchiveJob partialUpdatedArchiveJob = new ArchiveJob();
        partialUpdatedArchiveJob.setId(archiveJob.getId());

        partialUpdatedArchiveJob
            .name(UPDATED_NAME)
            .archiveFormat(UPDATED_ARCHIVE_FORMAT)
            .encryptionEnabled(UPDATED_ENCRYPTION_ENABLED)
            .encryptionAlgorithm(UPDATED_ENCRYPTION_ALGORITHM)
            .password(UPDATED_PASSWORD)
            .s3ArchiveKey(UPDATED_S_3_ARCHIVE_KEY)
            .archiveSha256(UPDATED_ARCHIVE_SHA_256)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restArchiveJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveJob))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArchiveJob, archiveJob),
            getPersistedArchiveJob(archiveJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateArchiveJobWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveJob using partial update
        ArchiveJob partialUpdatedArchiveJob = new ArchiveJob();
        partialUpdatedArchiveJob.setId(archiveJob.getId());

        partialUpdatedArchiveJob
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .documentQuery(UPDATED_DOCUMENT_QUERY)
            .archiveFormat(UPDATED_ARCHIVE_FORMAT)
            .compressionLevel(UPDATED_COMPRESSION_LEVEL)
            .encryptionEnabled(UPDATED_ENCRYPTION_ENABLED)
            .encryptionAlgorithm(UPDATED_ENCRYPTION_ALGORITHM)
            .password(UPDATED_PASSWORD)
            .s3ArchiveKey(UPDATED_S_3_ARCHIVE_KEY)
            .archiveSha256(UPDATED_ARCHIVE_SHA_256)
            .archiveSize(UPDATED_ARCHIVE_SIZE)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restArchiveJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveJob))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveJobUpdatableFieldsEquals(partialUpdatedArchiveJob, getPersistedArchiveJob(partialUpdatedArchiveJob));
    }

    @Test
    @Transactional
    void patchNonExistingArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archiveJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchiveJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveJob.setId(longCount.incrementAndGet());

        // Create the ArchiveJob
        ArchiveJobDTO archiveJobDTO = archiveJobMapper.toDto(archiveJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(archiveJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchiveJob() throws Exception {
        // Initialize the database
        insertedArchiveJob = archiveJobRepository.saveAndFlush(archiveJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the archiveJob
        restArchiveJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, archiveJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return archiveJobRepository.count();
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

    protected ArchiveJob getPersistedArchiveJob(ArchiveJob archiveJob) {
        return archiveJobRepository.findById(archiveJob.getId()).orElseThrow();
    }

    protected void assertPersistedArchiveJobToMatchAllProperties(ArchiveJob expectedArchiveJob) {
        assertArchiveJobAllPropertiesEquals(expectedArchiveJob, getPersistedArchiveJob(expectedArchiveJob));
    }

    protected void assertPersistedArchiveJobToMatchUpdatableProperties(ArchiveJob expectedArchiveJob) {
        assertArchiveJobAllUpdatablePropertiesEquals(expectedArchiveJob, getPersistedArchiveJob(expectedArchiveJob));
    }
}
