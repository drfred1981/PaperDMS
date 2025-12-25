package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.ConversionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.ConversionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.ConversionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.ConversionJobMapper;
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
 * Integration tests for the {@link ConversionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConversionJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

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

    private static final Long DEFAULT_OUTPUT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_OUTPUT_DOCUMENT_ID = 2L;
    private static final Long SMALLER_OUTPUT_DOCUMENT_ID = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/conversion-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ConversionJobRepository conversionJobRepository;

    @Autowired
    private ConversionJobMapper conversionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConversionJobMockMvc;

    private ConversionJob conversionJob;

    private ConversionJob insertedConversionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConversionJob createEntity() {
        return new ConversionJob()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .sourceFormat(DEFAULT_SOURCE_FORMAT)
            .targetFormat(DEFAULT_TARGET_FORMAT)
            .conversionEngine(DEFAULT_CONVERSION_ENGINE)
            .options(DEFAULT_OPTIONS)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputDocumentId(DEFAULT_OUTPUT_DOCUMENT_ID)
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
    public static ConversionJob createUpdatedEntity() {
        return new ConversionJob()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        conversionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedConversionJob != null) {
            conversionJobRepository.delete(insertedConversionJob);
            insertedConversionJob = null;
        }
    }

    @Test
    @Transactional
    void createConversionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);
        var returnedConversionJobDTO = om.readValue(
            restConversionJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ConversionJobDTO.class
        );

        // Validate the ConversionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedConversionJob = conversionJobMapper.toEntity(returnedConversionJobDTO);
        assertConversionJobUpdatableFieldsEquals(returnedConversionJob, getPersistedConversionJob(returnedConversionJob));

        insertedConversionJob = returnedConversionJob;
    }

    @Test
    @Transactional
    void createConversionJobWithExistingId() throws Exception {
        // Create the ConversionJob with an existing ID
        conversionJob.setId(1L);
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setDocumentId(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setDocumentSha256(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setSourceFormat(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTargetFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setTargetFormat(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setStatus(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setCreatedBy(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        conversionJob.setCreatedDate(null);

        // Create the ConversionJob, which fails.
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        restConversionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConversionJobs() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)))
            .andExpect(jsonPath("$.[*].targetFormat").value(hasItem(DEFAULT_TARGET_FORMAT)))
            .andExpect(jsonPath("$.[*].conversionEngine").value(hasItem(DEFAULT_CONVERSION_ENGINE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentId").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getConversionJob() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get the conversionJob
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, conversionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(conversionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.sourceFormat").value(DEFAULT_SOURCE_FORMAT))
            .andExpect(jsonPath("$.targetFormat").value(DEFAULT_TARGET_FORMAT))
            .andExpect(jsonPath("$.conversionEngine").value(DEFAULT_CONVERSION_ENGINE))
            .andExpect(jsonPath("$.options").value(DEFAULT_OPTIONS))
            .andExpect(jsonPath("$.outputS3Key").value(DEFAULT_OUTPUT_S_3_KEY))
            .andExpect(jsonPath("$.outputDocumentId").value(DEFAULT_OUTPUT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getConversionJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        Long id = conversionJob.getId();

        defaultConversionJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultConversionJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultConversionJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId equals to
        defaultConversionJobFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId in
        defaultConversionJobFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId is not null
        defaultConversionJobFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId is greater than or equal to
        defaultConversionJobFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId is less than or equal to
        defaultConversionJobFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId is less than
        defaultConversionJobFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentId is greater than
        defaultConversionJobFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentSha256 equals to
        defaultConversionJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentSha256 in
        defaultConversionJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentSha256 is not null
        defaultConversionJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentSha256 contains
        defaultConversionJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where documentSha256 does not contain
        defaultConversionJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsBySourceFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where sourceFormat equals to
        defaultConversionJobFiltering("sourceFormat.equals=" + DEFAULT_SOURCE_FORMAT, "sourceFormat.equals=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    void getAllConversionJobsBySourceFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where sourceFormat in
        defaultConversionJobFiltering(
            "sourceFormat.in=" + DEFAULT_SOURCE_FORMAT + "," + UPDATED_SOURCE_FORMAT,
            "sourceFormat.in=" + UPDATED_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsBySourceFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where sourceFormat is not null
        defaultConversionJobFiltering("sourceFormat.specified=true", "sourceFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsBySourceFormatContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where sourceFormat contains
        defaultConversionJobFiltering("sourceFormat.contains=" + DEFAULT_SOURCE_FORMAT, "sourceFormat.contains=" + UPDATED_SOURCE_FORMAT);
    }

    @Test
    @Transactional
    void getAllConversionJobsBySourceFormatNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where sourceFormat does not contain
        defaultConversionJobFiltering(
            "sourceFormat.doesNotContain=" + UPDATED_SOURCE_FORMAT,
            "sourceFormat.doesNotContain=" + DEFAULT_SOURCE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByTargetFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where targetFormat equals to
        defaultConversionJobFiltering("targetFormat.equals=" + DEFAULT_TARGET_FORMAT, "targetFormat.equals=" + UPDATED_TARGET_FORMAT);
    }

    @Test
    @Transactional
    void getAllConversionJobsByTargetFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where targetFormat in
        defaultConversionJobFiltering(
            "targetFormat.in=" + DEFAULT_TARGET_FORMAT + "," + UPDATED_TARGET_FORMAT,
            "targetFormat.in=" + UPDATED_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByTargetFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where targetFormat is not null
        defaultConversionJobFiltering("targetFormat.specified=true", "targetFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByTargetFormatContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where targetFormat contains
        defaultConversionJobFiltering("targetFormat.contains=" + DEFAULT_TARGET_FORMAT, "targetFormat.contains=" + UPDATED_TARGET_FORMAT);
    }

    @Test
    @Transactional
    void getAllConversionJobsByTargetFormatNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where targetFormat does not contain
        defaultConversionJobFiltering(
            "targetFormat.doesNotContain=" + UPDATED_TARGET_FORMAT,
            "targetFormat.doesNotContain=" + DEFAULT_TARGET_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByConversionEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where conversionEngine equals to
        defaultConversionJobFiltering(
            "conversionEngine.equals=" + DEFAULT_CONVERSION_ENGINE,
            "conversionEngine.equals=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByConversionEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where conversionEngine in
        defaultConversionJobFiltering(
            "conversionEngine.in=" + DEFAULT_CONVERSION_ENGINE + "," + UPDATED_CONVERSION_ENGINE,
            "conversionEngine.in=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByConversionEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where conversionEngine is not null
        defaultConversionJobFiltering("conversionEngine.specified=true", "conversionEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByConversionEngineContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where conversionEngine contains
        defaultConversionJobFiltering(
            "conversionEngine.contains=" + DEFAULT_CONVERSION_ENGINE,
            "conversionEngine.contains=" + UPDATED_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByConversionEngineNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where conversionEngine does not contain
        defaultConversionJobFiltering(
            "conversionEngine.doesNotContain=" + UPDATED_CONVERSION_ENGINE,
            "conversionEngine.doesNotContain=" + DEFAULT_CONVERSION_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputS3Key equals to
        defaultConversionJobFiltering("outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputS3Key in
        defaultConversionJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputS3Key is not null
        defaultConversionJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputS3Key contains
        defaultConversionJobFiltering("outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputS3Key does not contain
        defaultConversionJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId equals to
        defaultConversionJobFiltering(
            "outputDocumentId.equals=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.equals=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId in
        defaultConversionJobFiltering(
            "outputDocumentId.in=" + DEFAULT_OUTPUT_DOCUMENT_ID + "," + UPDATED_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.in=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId is not null
        defaultConversionJobFiltering("outputDocumentId.specified=true", "outputDocumentId.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId is greater than or equal to
        defaultConversionJobFiltering(
            "outputDocumentId.greaterThanOrEqual=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.greaterThanOrEqual=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId is less than or equal to
        defaultConversionJobFiltering(
            "outputDocumentId.lessThanOrEqual=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.lessThanOrEqual=" + SMALLER_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId is less than
        defaultConversionJobFiltering(
            "outputDocumentId.lessThan=" + UPDATED_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.lessThan=" + DEFAULT_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByOutputDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where outputDocumentId is greater than
        defaultConversionJobFiltering(
            "outputDocumentId.greaterThan=" + SMALLER_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.greaterThan=" + DEFAULT_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where status equals to
        defaultConversionJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllConversionJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where status in
        defaultConversionJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllConversionJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where status is not null
        defaultConversionJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where startDate equals to
        defaultConversionJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllConversionJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where startDate in
        defaultConversionJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where startDate is not null
        defaultConversionJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where endDate equals to
        defaultConversionJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllConversionJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where endDate in
        defaultConversionJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllConversionJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where endDate is not null
        defaultConversionJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdBy equals to
        defaultConversionJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdBy in
        defaultConversionJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdBy is not null
        defaultConversionJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdBy contains
        defaultConversionJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdBy does not contain
        defaultConversionJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdDate equals to
        defaultConversionJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdDate in
        defaultConversionJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllConversionJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        // Get all the conversionJobList where createdDate is not null
        defaultConversionJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultConversionJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultConversionJobShouldBeFound(shouldBeFound);
        defaultConversionJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultConversionJobShouldBeFound(String filter) throws Exception {
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(conversionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].sourceFormat").value(hasItem(DEFAULT_SOURCE_FORMAT)))
            .andExpect(jsonPath("$.[*].targetFormat").value(hasItem(DEFAULT_TARGET_FORMAT)))
            .andExpect(jsonPath("$.[*].conversionEngine").value(hasItem(DEFAULT_CONVERSION_ENGINE)))
            .andExpect(jsonPath("$.[*].options").value(hasItem(DEFAULT_OPTIONS)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentId").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultConversionJobShouldNotBeFound(String filter) throws Exception {
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restConversionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingConversionJob() throws Exception {
        // Get the conversionJob
        restConversionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingConversionJob() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conversionJob
        ConversionJob updatedConversionJob = conversionJobRepository.findById(conversionJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedConversionJob are not directly saved in db
        em.detach(updatedConversionJob);
        updatedConversionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(updatedConversionJob);

        restConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conversionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedConversionJobToMatchAllProperties(updatedConversionJob);
    }

    @Test
    @Transactional
    void putNonExistingConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, conversionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(conversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConversionJobWithPatch() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conversionJob using partial update
        ConversionJob partialUpdatedConversionJob = new ConversionJob();
        partialUpdatedConversionJob.setId(conversionJob.getId());

        partialUpdatedConversionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConversionJob))
            )
            .andExpect(status().isOk());

        // Validate the ConversionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConversionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedConversionJob, conversionJob),
            getPersistedConversionJob(conversionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateConversionJobWithPatch() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the conversionJob using partial update
        ConversionJob partialUpdatedConversionJob = new ConversionJob();
        partialUpdatedConversionJob.setId(conversionJob.getId());

        partialUpdatedConversionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .sourceFormat(UPDATED_SOURCE_FORMAT)
            .targetFormat(UPDATED_TARGET_FORMAT)
            .conversionEngine(UPDATED_CONVERSION_ENGINE)
            .options(UPDATED_OPTIONS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConversionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedConversionJob))
            )
            .andExpect(status().isOk());

        // Validate the ConversionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertConversionJobUpdatableFieldsEquals(partialUpdatedConversionJob, getPersistedConversionJob(partialUpdatedConversionJob));
    }

    @Test
    @Transactional
    void patchNonExistingConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, conversionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(conversionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConversionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        conversionJob.setId(longCount.incrementAndGet());

        // Create the ConversionJob
        ConversionJobDTO conversionJobDTO = conversionJobMapper.toDto(conversionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConversionJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(conversionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConversionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConversionJob() throws Exception {
        // Initialize the database
        insertedConversionJob = conversionJobRepository.saveAndFlush(conversionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the conversionJob
        restConversionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, conversionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return conversionJobRepository.count();
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

    protected ConversionJob getPersistedConversionJob(ConversionJob conversionJob) {
        return conversionJobRepository.findById(conversionJob.getId()).orElseThrow();
    }

    protected void assertPersistedConversionJobToMatchAllProperties(ConversionJob expectedConversionJob) {
        assertConversionJobAllPropertiesEquals(expectedConversionJob, getPersistedConversionJob(expectedConversionJob));
    }

    protected void assertPersistedConversionJobToMatchUpdatableProperties(ConversionJob expectedConversionJob) {
        assertConversionJobAllUpdatablePropertiesEquals(expectedConversionJob, getPersistedConversionJob(expectedConversionJob));
    }
}
