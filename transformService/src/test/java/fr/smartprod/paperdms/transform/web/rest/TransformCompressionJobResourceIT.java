package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.TransformCompressionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.TransformCompressionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.CompressionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.TransformCompressionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformCompressionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformCompressionJobMapper;
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
 * Integration tests for the {@link TransformCompressionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformCompressionJobResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final CompressionType DEFAULT_COMPRESSION_TYPE = CompressionType.LOSSLESS;
    private static final CompressionType UPDATED_COMPRESSION_TYPE = CompressionType.LOSSY;

    private static final Integer DEFAULT_QUALITY = 0;
    private static final Integer UPDATED_QUALITY = 1;
    private static final Integer SMALLER_QUALITY = 0 - 1;

    private static final Long DEFAULT_TARGET_SIZE_KB = 1L;
    private static final Long UPDATED_TARGET_SIZE_KB = 2L;
    private static final Long SMALLER_TARGET_SIZE_KB = 1L - 1L;

    private static final Long DEFAULT_ORIGINAL_SIZE = 1L;
    private static final Long UPDATED_ORIGINAL_SIZE = 2L;
    private static final Long SMALLER_ORIGINAL_SIZE = 1L - 1L;

    private static final Long DEFAULT_COMPRESSED_SIZE = 1L;
    private static final Long UPDATED_COMPRESSED_SIZE = 2L;
    private static final Long SMALLER_COMPRESSED_SIZE = 1L - 1L;

    private static final Double DEFAULT_COMPRESSION_RATIO = 1D;
    private static final Double UPDATED_COMPRESSION_RATIO = 2D;
    private static final Double SMALLER_COMPRESSION_RATIO = 1D - 1D;

    private static final String DEFAULT_OUTPUT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final TransformStatus DEFAULT_STATUS = TransformStatus.PENDING;
    private static final TransformStatus UPDATED_STATUS = TransformStatus.PROCESSING;

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

    private static final String ENTITY_API_URL = "/api/transform-compression-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransformCompressionJobRepository transformCompressionJobRepository;

    @Autowired
    private TransformCompressionJobMapper transformCompressionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformCompressionJobMockMvc;

    private TransformCompressionJob transformCompressionJob;

    private TransformCompressionJob insertedTransformCompressionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformCompressionJob createEntity() {
        return new TransformCompressionJob()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .compressionType(DEFAULT_COMPRESSION_TYPE)
            .quality(DEFAULT_QUALITY)
            .targetSizeKb(DEFAULT_TARGET_SIZE_KB)
            .originalSize(DEFAULT_ORIGINAL_SIZE)
            .compressedSize(DEFAULT_COMPRESSED_SIZE)
            .compressionRatio(DEFAULT_COMPRESSION_RATIO)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputDocumentSha256(DEFAULT_OUTPUT_DOCUMENT_SHA_256)
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
    public static TransformCompressionJob createUpdatedEntity() {
        return new TransformCompressionJob()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        transformCompressionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransformCompressionJob != null) {
            transformCompressionJobRepository.delete(insertedTransformCompressionJob);
            insertedTransformCompressionJob = null;
        }
    }

    @Test
    @Transactional
    void createTransformCompressionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);
        var returnedTransformCompressionJobDTO = om.readValue(
            restTransformCompressionJobMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransformCompressionJobDTO.class
        );

        // Validate the TransformCompressionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransformCompressionJob = transformCompressionJobMapper.toEntity(returnedTransformCompressionJobDTO);
        assertTransformCompressionJobUpdatableFieldsEquals(
            returnedTransformCompressionJob,
            getPersistedTransformCompressionJob(returnedTransformCompressionJob)
        );

        insertedTransformCompressionJob = returnedTransformCompressionJob;
    }

    @Test
    @Transactional
    void createTransformCompressionJobWithExistingId() throws Exception {
        // Create the TransformCompressionJob with an existing ID
        transformCompressionJob.setId(1L);
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformCompressionJob.setDocumentSha256(null);

        // Create the TransformCompressionJob, which fails.
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompressionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformCompressionJob.setCompressionType(null);

        // Create the TransformCompressionJob, which fails.
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformCompressionJob.setStatus(null);

        // Create the TransformCompressionJob, which fails.
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformCompressionJob.setCreatedBy(null);

        // Create the TransformCompressionJob, which fails.
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformCompressionJob.setCreatedDate(null);

        // Create the TransformCompressionJob, which fails.
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobs() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformCompressionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].compressionType").value(hasItem(DEFAULT_COMPRESSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY)))
            .andExpect(jsonPath("$.[*].targetSizeKb").value(hasItem(DEFAULT_TARGET_SIZE_KB.intValue())))
            .andExpect(jsonPath("$.[*].originalSize").value(hasItem(DEFAULT_ORIGINAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressedSize").value(hasItem(DEFAULT_COMPRESSED_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressionRatio").value(hasItem(DEFAULT_COMPRESSION_RATIO)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTransformCompressionJob() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get the transformCompressionJob
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, transformCompressionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformCompressionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.compressionType").value(DEFAULT_COMPRESSION_TYPE.toString()))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY))
            .andExpect(jsonPath("$.targetSizeKb").value(DEFAULT_TARGET_SIZE_KB.intValue()))
            .andExpect(jsonPath("$.originalSize").value(DEFAULT_ORIGINAL_SIZE.intValue()))
            .andExpect(jsonPath("$.compressedSize").value(DEFAULT_COMPRESSED_SIZE.intValue()))
            .andExpect(jsonPath("$.compressionRatio").value(DEFAULT_COMPRESSION_RATIO))
            .andExpect(jsonPath("$.outputS3Key").value(DEFAULT_OUTPUT_S_3_KEY))
            .andExpect(jsonPath("$.outputDocumentSha256").value(DEFAULT_OUTPUT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTransformCompressionJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        Long id = transformCompressionJob.getId();

        defaultTransformCompressionJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransformCompressionJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransformCompressionJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where documentSha256 equals to
        defaultTransformCompressionJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where documentSha256 in
        defaultTransformCompressionJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where documentSha256 is not null
        defaultTransformCompressionJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where documentSha256 contains
        defaultTransformCompressionJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where documentSha256 does not contain
        defaultTransformCompressionJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionType equals to
        defaultTransformCompressionJobFiltering(
            "compressionType.equals=" + DEFAULT_COMPRESSION_TYPE,
            "compressionType.equals=" + UPDATED_COMPRESSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionType in
        defaultTransformCompressionJobFiltering(
            "compressionType.in=" + DEFAULT_COMPRESSION_TYPE + "," + UPDATED_COMPRESSION_TYPE,
            "compressionType.in=" + UPDATED_COMPRESSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionType is not null
        defaultTransformCompressionJobFiltering("compressionType.specified=true", "compressionType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality equals to
        defaultTransformCompressionJobFiltering("quality.equals=" + DEFAULT_QUALITY, "quality.equals=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality in
        defaultTransformCompressionJobFiltering("quality.in=" + DEFAULT_QUALITY + "," + UPDATED_QUALITY, "quality.in=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality is not null
        defaultTransformCompressionJobFiltering("quality.specified=true", "quality.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality is greater than or equal to
        defaultTransformCompressionJobFiltering(
            "quality.greaterThanOrEqual=" + DEFAULT_QUALITY,
            "quality.greaterThanOrEqual=" + (DEFAULT_QUALITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality is less than or equal to
        defaultTransformCompressionJobFiltering("quality.lessThanOrEqual=" + DEFAULT_QUALITY, "quality.lessThanOrEqual=" + SMALLER_QUALITY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality is less than
        defaultTransformCompressionJobFiltering("quality.lessThan=" + (DEFAULT_QUALITY + 1), "quality.lessThan=" + DEFAULT_QUALITY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByQualityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where quality is greater than
        defaultTransformCompressionJobFiltering("quality.greaterThan=" + SMALLER_QUALITY, "quality.greaterThan=" + DEFAULT_QUALITY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb equals to
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.equals=" + DEFAULT_TARGET_SIZE_KB,
            "targetSizeKb.equals=" + UPDATED_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb in
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.in=" + DEFAULT_TARGET_SIZE_KB + "," + UPDATED_TARGET_SIZE_KB,
            "targetSizeKb.in=" + UPDATED_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb is not null
        defaultTransformCompressionJobFiltering("targetSizeKb.specified=true", "targetSizeKb.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb is greater than or equal to
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.greaterThanOrEqual=" + DEFAULT_TARGET_SIZE_KB,
            "targetSizeKb.greaterThanOrEqual=" + UPDATED_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb is less than or equal to
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.lessThanOrEqual=" + DEFAULT_TARGET_SIZE_KB,
            "targetSizeKb.lessThanOrEqual=" + SMALLER_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb is less than
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.lessThan=" + UPDATED_TARGET_SIZE_KB,
            "targetSizeKb.lessThan=" + DEFAULT_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByTargetSizeKbIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where targetSizeKb is greater than
        defaultTransformCompressionJobFiltering(
            "targetSizeKb.greaterThan=" + SMALLER_TARGET_SIZE_KB,
            "targetSizeKb.greaterThan=" + DEFAULT_TARGET_SIZE_KB
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize equals to
        defaultTransformCompressionJobFiltering(
            "originalSize.equals=" + DEFAULT_ORIGINAL_SIZE,
            "originalSize.equals=" + UPDATED_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize in
        defaultTransformCompressionJobFiltering(
            "originalSize.in=" + DEFAULT_ORIGINAL_SIZE + "," + UPDATED_ORIGINAL_SIZE,
            "originalSize.in=" + UPDATED_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize is not null
        defaultTransformCompressionJobFiltering("originalSize.specified=true", "originalSize.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize is greater than or equal to
        defaultTransformCompressionJobFiltering(
            "originalSize.greaterThanOrEqual=" + DEFAULT_ORIGINAL_SIZE,
            "originalSize.greaterThanOrEqual=" + UPDATED_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize is less than or equal to
        defaultTransformCompressionJobFiltering(
            "originalSize.lessThanOrEqual=" + DEFAULT_ORIGINAL_SIZE,
            "originalSize.lessThanOrEqual=" + SMALLER_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize is less than
        defaultTransformCompressionJobFiltering(
            "originalSize.lessThan=" + UPDATED_ORIGINAL_SIZE,
            "originalSize.lessThan=" + DEFAULT_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOriginalSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where originalSize is greater than
        defaultTransformCompressionJobFiltering(
            "originalSize.greaterThan=" + SMALLER_ORIGINAL_SIZE,
            "originalSize.greaterThan=" + DEFAULT_ORIGINAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize equals to
        defaultTransformCompressionJobFiltering(
            "compressedSize.equals=" + DEFAULT_COMPRESSED_SIZE,
            "compressedSize.equals=" + UPDATED_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize in
        defaultTransformCompressionJobFiltering(
            "compressedSize.in=" + DEFAULT_COMPRESSED_SIZE + "," + UPDATED_COMPRESSED_SIZE,
            "compressedSize.in=" + UPDATED_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize is not null
        defaultTransformCompressionJobFiltering("compressedSize.specified=true", "compressedSize.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize is greater than or equal to
        defaultTransformCompressionJobFiltering(
            "compressedSize.greaterThanOrEqual=" + DEFAULT_COMPRESSED_SIZE,
            "compressedSize.greaterThanOrEqual=" + UPDATED_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize is less than or equal to
        defaultTransformCompressionJobFiltering(
            "compressedSize.lessThanOrEqual=" + DEFAULT_COMPRESSED_SIZE,
            "compressedSize.lessThanOrEqual=" + SMALLER_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize is less than
        defaultTransformCompressionJobFiltering(
            "compressedSize.lessThan=" + UPDATED_COMPRESSED_SIZE,
            "compressedSize.lessThan=" + DEFAULT_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressedSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressedSize is greater than
        defaultTransformCompressionJobFiltering(
            "compressedSize.greaterThan=" + SMALLER_COMPRESSED_SIZE,
            "compressedSize.greaterThan=" + DEFAULT_COMPRESSED_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio equals to
        defaultTransformCompressionJobFiltering(
            "compressionRatio.equals=" + DEFAULT_COMPRESSION_RATIO,
            "compressionRatio.equals=" + UPDATED_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio in
        defaultTransformCompressionJobFiltering(
            "compressionRatio.in=" + DEFAULT_COMPRESSION_RATIO + "," + UPDATED_COMPRESSION_RATIO,
            "compressionRatio.in=" + UPDATED_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio is not null
        defaultTransformCompressionJobFiltering("compressionRatio.specified=true", "compressionRatio.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio is greater than or equal to
        defaultTransformCompressionJobFiltering(
            "compressionRatio.greaterThanOrEqual=" + DEFAULT_COMPRESSION_RATIO,
            "compressionRatio.greaterThanOrEqual=" + UPDATED_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio is less than or equal to
        defaultTransformCompressionJobFiltering(
            "compressionRatio.lessThanOrEqual=" + DEFAULT_COMPRESSION_RATIO,
            "compressionRatio.lessThanOrEqual=" + SMALLER_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio is less than
        defaultTransformCompressionJobFiltering(
            "compressionRatio.lessThan=" + UPDATED_COMPRESSION_RATIO,
            "compressionRatio.lessThan=" + DEFAULT_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCompressionRatioIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where compressionRatio is greater than
        defaultTransformCompressionJobFiltering(
            "compressionRatio.greaterThan=" + SMALLER_COMPRESSION_RATIO,
            "compressionRatio.greaterThan=" + DEFAULT_COMPRESSION_RATIO
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputS3Key equals to
        defaultTransformCompressionJobFiltering(
            "outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputS3Key in
        defaultTransformCompressionJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputS3Key is not null
        defaultTransformCompressionJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputS3Key contains
        defaultTransformCompressionJobFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputS3Key does not contain
        defaultTransformCompressionJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputDocumentSha256 equals to
        defaultTransformCompressionJobFiltering(
            "outputDocumentSha256.equals=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.equals=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputDocumentSha256 in
        defaultTransformCompressionJobFiltering(
            "outputDocumentSha256.in=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256 + "," + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.in=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputDocumentSha256 is not null
        defaultTransformCompressionJobFiltering("outputDocumentSha256.specified=true", "outputDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputDocumentSha256 contains
        defaultTransformCompressionJobFiltering(
            "outputDocumentSha256.contains=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.contains=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByOutputDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where outputDocumentSha256 does not contain
        defaultTransformCompressionJobFiltering(
            "outputDocumentSha256.doesNotContain=" + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.doesNotContain=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where status equals to
        defaultTransformCompressionJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where status in
        defaultTransformCompressionJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where status is not null
        defaultTransformCompressionJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where startDate equals to
        defaultTransformCompressionJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where startDate in
        defaultTransformCompressionJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where startDate is not null
        defaultTransformCompressionJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where endDate equals to
        defaultTransformCompressionJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where endDate in
        defaultTransformCompressionJobFiltering(
            "endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE,
            "endDate.in=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where endDate is not null
        defaultTransformCompressionJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdBy equals to
        defaultTransformCompressionJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdBy in
        defaultTransformCompressionJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdBy is not null
        defaultTransformCompressionJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdBy contains
        defaultTransformCompressionJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdBy does not contain
        defaultTransformCompressionJobFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdDate equals to
        defaultTransformCompressionJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdDate in
        defaultTransformCompressionJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformCompressionJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        // Get all the transformCompressionJobList where createdDate is not null
        defaultTransformCompressionJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultTransformCompressionJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransformCompressionJobShouldBeFound(shouldBeFound);
        defaultTransformCompressionJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformCompressionJobShouldBeFound(String filter) throws Exception {
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformCompressionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].compressionType").value(hasItem(DEFAULT_COMPRESSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY)))
            .andExpect(jsonPath("$.[*].targetSizeKb").value(hasItem(DEFAULT_TARGET_SIZE_KB.intValue())))
            .andExpect(jsonPath("$.[*].originalSize").value(hasItem(DEFAULT_ORIGINAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressedSize").value(hasItem(DEFAULT_COMPRESSED_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressionRatio").value(hasItem(DEFAULT_COMPRESSION_RATIO)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformCompressionJobShouldNotBeFound(String filter) throws Exception {
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformCompressionJob() throws Exception {
        // Get the transformCompressionJob
        restTransformCompressionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransformCompressionJob() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformCompressionJob
        TransformCompressionJob updatedTransformCompressionJob = transformCompressionJobRepository
            .findById(transformCompressionJob.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTransformCompressionJob are not directly saved in db
        em.detach(updatedTransformCompressionJob);
        updatedTransformCompressionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(updatedTransformCompressionJob);

        restTransformCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformCompressionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransformCompressionJobToMatchAllProperties(updatedTransformCompressionJob);
    }

    @Test
    @Transactional
    void putNonExistingTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformCompressionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformCompressionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformCompressionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformCompressionJob using partial update
        TransformCompressionJob partialUpdatedTransformCompressionJob = new TransformCompressionJob();
        partialUpdatedTransformCompressionJob.setId(transformCompressionJob.getId());

        partialUpdatedTransformCompressionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .quality(UPDATED_QUALITY)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY);

        restTransformCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformCompressionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformCompressionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformCompressionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformCompressionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransformCompressionJob, transformCompressionJob),
            getPersistedTransformCompressionJob(transformCompressionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransformCompressionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformCompressionJob using partial update
        TransformCompressionJob partialUpdatedTransformCompressionJob = new TransformCompressionJob();
        partialUpdatedTransformCompressionJob.setId(transformCompressionJob.getId());

        partialUpdatedTransformCompressionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformCompressionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformCompressionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformCompressionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformCompressionJobUpdatableFieldsEquals(
            partialUpdatedTransformCompressionJob,
            getPersistedTransformCompressionJob(partialUpdatedTransformCompressionJob)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformCompressionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformCompressionJob.setId(longCount.incrementAndGet());

        // Create the TransformCompressionJob
        TransformCompressionJobDTO transformCompressionJobDTO = transformCompressionJobMapper.toDto(transformCompressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transformCompressionJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformCompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformCompressionJob() throws Exception {
        // Initialize the database
        insertedTransformCompressionJob = transformCompressionJobRepository.saveAndFlush(transformCompressionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transformCompressionJob
        restTransformCompressionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformCompressionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transformCompressionJobRepository.count();
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

    protected TransformCompressionJob getPersistedTransformCompressionJob(TransformCompressionJob transformCompressionJob) {
        return transformCompressionJobRepository.findById(transformCompressionJob.getId()).orElseThrow();
    }

    protected void assertPersistedTransformCompressionJobToMatchAllProperties(TransformCompressionJob expectedTransformCompressionJob) {
        assertTransformCompressionJobAllPropertiesEquals(
            expectedTransformCompressionJob,
            getPersistedTransformCompressionJob(expectedTransformCompressionJob)
        );
    }

    protected void assertPersistedTransformCompressionJobToMatchUpdatableProperties(
        TransformCompressionJob expectedTransformCompressionJob
    ) {
        assertTransformCompressionJobAllUpdatablePropertiesEquals(
            expectedTransformCompressionJob,
            getPersistedTransformCompressionJob(expectedTransformCompressionJob)
        );
    }
}
