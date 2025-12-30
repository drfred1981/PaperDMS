package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.TransformConversionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.TransformConversionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.TransformConversionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformConversionJobMapper;
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
 * Integration tests for the {@link TransformConversionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformConversionJobResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_FORMAT = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_FORMAT = "BBBBBBBBBB";

    private static final String DEFAULT_CONVERSION_ENGINE = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_ENGINE = "BBBBBBBBBB";

    private static final String DEFAULT_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_OPTIONS = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/transform-conversion-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransformConversionJobRepository transformConversionJobRepository;

    @Autowired
    private TransformConversionJobMapper transformConversionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformConversionJobMockMvc;

    private TransformConversionJob transformConversionJob;

    private TransformConversionJob insertedTransformConversionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformConversionJob createEntity() {
        return new TransformConversionJob()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .sourceFormat(DEFAULT_SOURCE_FORMAT)
            .targetFormat(DEFAULT_TARGET_FORMAT)
            .conversionEngine(DEFAULT_CONVERSION_ENGINE)
            .options(DEFAULT_OPTIONS)
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
    public static TransformConversionJob createUpdatedEntity() {
        return new TransformConversionJob()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
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
        transformConversionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransformConversionJob != null) {
            transformConversionJobRepository.delete(insertedTransformConversionJob);
            insertedTransformConversionJob = null;
        }
    }

    @Test
    @Transactional
    void createTransformConversionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);
        var returnedTransformConversionJobDTO = om.readValue(
            restTransformConversionJobMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransformConversionJobDTO.class
        );

        // Validate the TransformConversionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransformConversionJob = transformConversionJobMapper.toEntity(returnedTransformConversionJobDTO);
        assertTransformConversionJobUpdatableFieldsEquals(
            returnedTransformConversionJob,
            getPersistedTransformConversionJob(returnedTransformConversionJob)
        );

        insertedTransformConversionJob = returnedTransformConversionJob;
    }

    @Test
    @Transactional
    void createTransformConversionJobWithExistingId() throws Exception {
        // Create the TransformConversionJob with an existing ID
        transformConversionJob.setId(1L);
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setDocumentSha256(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setSourceFormat(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTargetFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setTargetFormat(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setStatus(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setCreatedBy(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformConversionJob.setCreatedDate(null);

        // Create the TransformConversionJob, which fails.
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        restTransformConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobs() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformConversionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)))
            .andExpect(jsonPath("$.[*].targetFormat").value(hasItem(DEFAULT_TARGET_FORMAT)))
            .andExpect(jsonPath("$.[*].conversionEngine").value(hasItem(DEFAULT_CONVERSION_ENGINE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
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
    void getTransformConversionJob() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get the transformConversionJob
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, transformConversionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformConversionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.sourceFormat").value(DEFAULT_SOURCE_FORMAT))
            .andExpect(jsonPath("$.targetFormat").value(DEFAULT_TARGET_FORMAT))
            .andExpect(jsonPath("$.conversionEngine").value(DEFAULT_CONVERSION_ENGINE))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS))
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
    void getTransformConversionJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        Long id = transformConversionJob.getId();

        defaultTransformConversionJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransformConversionJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransformConversionJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where documentSha256 equals to
        defaultTransformConversionJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where documentSha256 in
        defaultTransformConversionJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where documentSha256 is not null
        defaultTransformConversionJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where documentSha256 contains
        defaultTransformConversionJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where documentSha256 does not contain
        defaultTransformConversionJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsBySourceFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where sourceFormat equals to
        defaultTransformConversionJobFiltering(
            "sourceFormat.equals=" + DEFAULT_SOURCE_FORMAT,
            "sourceFormat.equals=" + UPDATED_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsBySourceFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where sourceFormat in
        defaultTransformConversionJobFiltering(
            "sourceFormat.in=" + DEFAULT_SOURCE_FORMAT + "," + UPDATED_SOURCE_FORMAT,
            "sourceFormat.in=" + UPDATED_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsBySourceFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where sourceFormat is not null
        defaultTransformConversionJobFiltering("sourceFormat.specified=true", "sourceFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsBySourceFormatContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where sourceFormat contains
        defaultTransformConversionJobFiltering(
            "sourceFormat.contains=" + DEFAULT_SOURCE_FORMAT,
            "sourceFormat.contains=" + UPDATED_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsBySourceFormatNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where sourceFormat does not contain
        defaultTransformConversionJobFiltering(
            "sourceFormat.doesNotContain=" + UPDATED_SOURCE_FORMAT,
            "sourceFormat.doesNotContain=" + DEFAULT_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByTargetFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where targetFormat equals to
        defaultTransformConversionJobFiltering(
            "targetFormat.equals=" + DEFAULT_TARGET_FORMAT,
            "targetFormat.equals=" + UPDATED_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByTargetFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where targetFormat in
        defaultTransformConversionJobFiltering(
            "targetFormat.in=" + DEFAULT_TARGET_FORMAT + "," + UPDATED_TARGET_FORMAT,
            "targetFormat.in=" + UPDATED_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByTargetFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where targetFormat is not null
        defaultTransformConversionJobFiltering("targetFormat.specified=true", "targetFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByTargetFormatContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where targetFormat contains
        defaultTransformConversionJobFiltering(
            "targetFormat.contains=" + DEFAULT_TARGET_FORMAT,
            "targetFormat.contains=" + UPDATED_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByTargetFormatNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where targetFormat does not contain
        defaultTransformConversionJobFiltering(
            "targetFormat.doesNotContain=" + UPDATED_TARGET_FORMAT,
            "targetFormat.doesNotContain=" + DEFAULT_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByConversionEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where conversionEngine equals to
        defaultTransformConversionJobFiltering(
            "conversionEngine.equals=" + DEFAULT_CONVERSION_ENGINE,
            "conversionEngine.equals=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByConversionEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where conversionEngine in
        defaultTransformConversionJobFiltering(
            "conversionEngine.in=" + DEFAULT_CONVERSION_ENGINE + "," + UPDATED_CONVERSION_ENGINE,
            "conversionEngine.in=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByConversionEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where conversionEngine is not null
        defaultTransformConversionJobFiltering("conversionEngine.specified=true", "conversionEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByConversionEngineContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where conversionEngine contains
        defaultTransformConversionJobFiltering(
            "conversionEngine.contains=" + DEFAULT_CONVERSION_ENGINE,
            "conversionEngine.contains=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByConversionEngineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where conversionEngine does not contain
        defaultTransformConversionJobFiltering(
            "conversionEngine.doesNotContain=" + UPDATED_CONVERSION_ENGINE,
            "conversionEngine.doesNotContain=" + DEFAULT_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputS3Key equals to
        defaultTransformConversionJobFiltering(
            "outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputS3Key in
        defaultTransformConversionJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputS3Key is not null
        defaultTransformConversionJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputS3Key contains
        defaultTransformConversionJobFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputS3Key does not contain
        defaultTransformConversionJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputDocumentSha256 equals to
        defaultTransformConversionJobFiltering(
            "outputDocumentSha256.equals=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.equals=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputDocumentSha256 in
        defaultTransformConversionJobFiltering(
            "outputDocumentSha256.in=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256 + "," + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.in=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputDocumentSha256 is not null
        defaultTransformConversionJobFiltering("outputDocumentSha256.specified=true", "outputDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputDocumentSha256 contains
        defaultTransformConversionJobFiltering(
            "outputDocumentSha256.contains=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.contains=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByOutputDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where outputDocumentSha256 does not contain
        defaultTransformConversionJobFiltering(
            "outputDocumentSha256.doesNotContain=" + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.doesNotContain=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where status equals to
        defaultTransformConversionJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where status in
        defaultTransformConversionJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where status is not null
        defaultTransformConversionJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where startDate equals to
        defaultTransformConversionJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where startDate in
        defaultTransformConversionJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where startDate is not null
        defaultTransformConversionJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where endDate equals to
        defaultTransformConversionJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where endDate in
        defaultTransformConversionJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where endDate is not null
        defaultTransformConversionJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdBy equals to
        defaultTransformConversionJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdBy in
        defaultTransformConversionJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdBy is not null
        defaultTransformConversionJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdBy contains
        defaultTransformConversionJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdBy does not contain
        defaultTransformConversionJobFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdDate equals to
        defaultTransformConversionJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdDate in
        defaultTransformConversionJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformConversionJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        // Get all the transformConversionJobList where createdDate is not null
        defaultTransformConversionJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultTransformConversionJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransformConversionJobShouldBeFound(shouldBeFound);
        defaultTransformConversionJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformConversionJobShouldBeFound(String filter) throws Exception {
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformConversionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)))
            .andExpect(jsonPath("$.[*].targetFormat").value(hasItem(DEFAULT_TARGET_FORMAT)))
            .andExpect(jsonPath("$.[*].conversionEngine").value(hasItem(DEFAULT_CONVERSION_ENGINE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformConversionJobShouldNotBeFound(String filter) throws Exception {
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformConversionJob() throws Exception {
        // Get the transformConversionJob
        restTransformConversionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransformConversionJob() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformConversionJob
        TransformConversionJob updatedTransformConversionJob = transformConversionJobRepository
            .findById(transformConversionJob.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTransformConversionJob are not directly saved in db
        em.detach(updatedTransformConversionJob);
        updatedTransformConversionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(updatedTransformConversionJob);

        restTransformConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformConversionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransformConversionJobToMatchAllProperties(updatedTransformConversionJob);
    }

    @Test
    @Transactional
    void putNonExistingTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformConversionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformConversionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformConversionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformConversionJob using partial update
        TransformConversionJob partialUpdatedTransformConversionJob = new TransformConversionJob();
        partialUpdatedTransformConversionJob.setId(transformConversionJob.getId());

        partialUpdatedTransformConversionJob
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .status(UPDATED_STATUS)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformConversionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformConversionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformConversionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformConversionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransformConversionJob, transformConversionJob),
            getPersistedTransformConversionJob(transformConversionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransformConversionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformConversionJob using partial update
        TransformConversionJob partialUpdatedTransformConversionJob = new TransformConversionJob();
        partialUpdatedTransformConversionJob.setId(transformConversionJob.getId());

        partialUpdatedTransformConversionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformConversionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformConversionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformConversionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformConversionJobUpdatableFieldsEquals(
            partialUpdatedTransformConversionJob,
            getPersistedTransformConversionJob(partialUpdatedTransformConversionJob)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformConversionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformConversionJob.setId(longCount.incrementAndGet());

        // Create the TransformConversionJob
        TransformConversionJobDTO transformConversionJobDTO = transformConversionJobMapper.toDto(transformConversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transformConversionJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformConversionJob() throws Exception {
        // Initialize the database
        insertedTransformConversionJob = transformConversionJobRepository.saveAndFlush(transformConversionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transformConversionJob
        restTransformConversionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformConversionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transformConversionJobRepository.count();
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

    protected TransformConversionJob getPersistedTransformConversionJob(TransformConversionJob transformConversionJob) {
        return transformConversionJobRepository.findById(transformConversionJob.getId()).orElseThrow();
    }

    protected void assertPersistedTransformConversionJobToMatchAllProperties(TransformConversionJob expectedTransformConversionJob) {
        assertTransformConversionJobAllPropertiesEquals(
            expectedTransformConversionJob,
            getPersistedTransformConversionJob(expectedTransformConversionJob)
        );
    }

    protected void assertPersistedTransformConversionJobToMatchUpdatableProperties(TransformConversionJob expectedTransformConversionJob) {
        assertTransformConversionJobAllUpdatablePropertiesEquals(
            expectedTransformConversionJob,
            getPersistedTransformConversionJob(expectedTransformConversionJob)
        );
    }
}
