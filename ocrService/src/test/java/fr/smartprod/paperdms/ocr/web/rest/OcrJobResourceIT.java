package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrJobAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrStatus;
import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrJobDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrJobMapper;
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
 * Integration tests for the {@link OcrJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrJobResourceIT {

    private static final OcrStatus DEFAULT_STATUS = OcrStatus.PENDING;
    private static final OcrStatus UPDATED_STATUS = OcrStatus.IN_PROGRESS;

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_REQUESTED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_REQUESTED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DETECTED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_DETECTED_LANGUAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_LANGUAGE_CONFIDENCE = 0D;
    private static final Double UPDATED_LANGUAGE_CONFIDENCE = 1D;
    private static final Double SMALLER_LANGUAGE_CONFIDENCE = 0D - 1D;

    private static final OcrEngine DEFAULT_OCR_ENGINE = OcrEngine.TIKA_TESSERACT;
    private static final OcrEngine UPDATED_OCR_ENGINE = OcrEngine.TIKA_NATIVE;

    private static final String DEFAULT_TIKA_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_TIKA_ENDPOINT = "BBBBBBBBBB";

    private static final String DEFAULT_AI_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_AI_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_AI_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_AI_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_CACHE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_CACHE_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CACHED = false;
    private static final Boolean UPDATED_IS_CACHED = true;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_COUNT = 1;
    private static final Integer UPDATED_PAGE_COUNT = 2;
    private static final Integer SMALLER_PAGE_COUNT = 1 - 1;

    private static final Integer DEFAULT_PROGRESS = 0;
    private static final Integer UPDATED_PROGRESS = 1;
    private static final Integer SMALLER_PROGRESS = 0 - 1;

    private static final Integer DEFAULT_RETRY_COUNT = 1;
    private static final Integer UPDATED_RETRY_COUNT = 2;
    private static final Integer SMALLER_RETRY_COUNT = 1 - 1;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final Long DEFAULT_PROCESSING_TIME = 1L;
    private static final Long UPDATED_PROCESSING_TIME = 2L;
    private static final Long SMALLER_PROCESSING_TIME = 1L - 1L;

    private static final Double DEFAULT_COST_ESTIMATE = 1D;
    private static final Double UPDATED_COST_ESTIMATE = 2D;
    private static final Double SMALLER_COST_ESTIMATE = 1D - 1D;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ocr-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrJobRepository ocrJobRepository;

    @Autowired
    private OcrJobMapper ocrJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOcrJobMockMvc;

    private OcrJob ocrJob;

    private OcrJob insertedOcrJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrJob createEntity() {
        return new OcrJob()
            .status(DEFAULT_STATUS)
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .requestedLanguage(DEFAULT_REQUESTED_LANGUAGE)
            .detectedLanguage(DEFAULT_DETECTED_LANGUAGE)
            .languageConfidence(DEFAULT_LANGUAGE_CONFIDENCE)
            .ocrEngine(DEFAULT_OCR_ENGINE)
            .tikaEndpoint(DEFAULT_TIKA_ENDPOINT)
            .aiProvider(DEFAULT_AI_PROVIDER)
            .aiModel(DEFAULT_AI_MODEL)
            .resultCacheKey(DEFAULT_RESULT_CACHE_KEY)
            .isCached(DEFAULT_IS_CACHED)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .pageCount(DEFAULT_PAGE_COUNT)
            .progress(DEFAULT_PROGRESS)
            .retryCount(DEFAULT_RETRY_COUNT)
            .priority(DEFAULT_PRIORITY)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .costEstimate(DEFAULT_COST_ESTIMATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrJob createUpdatedEntity() {
        return new OcrJob()
            .status(UPDATED_STATUS)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .requestedLanguage(UPDATED_REQUESTED_LANGUAGE)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
            .processingTime(UPDATED_PROCESSING_TIME)
            .costEstimate(UPDATED_COST_ESTIMATE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        ocrJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOcrJob != null) {
            ocrJobRepository.delete(insertedOcrJob);
            insertedOcrJob = null;
        }
    }

    @Test
    @Transactional
    void createOcrJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);
        var returnedOcrJobDTO = om.readValue(
            restOcrJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OcrJobDTO.class
        );

        // Validate the OcrJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOcrJob = ocrJobMapper.toEntity(returnedOcrJobDTO);
        assertOcrJobUpdatableFieldsEquals(returnedOcrJob, getPersistedOcrJob(returnedOcrJob));

        insertedOcrJob = returnedOcrJob;
    }

    @Test
    @Transactional
    void createOcrJobWithExistingId() throws Exception {
        // Create the OcrJob with an existing ID
        ocrJob.setId(1L);
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setStatus(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setDocumentId(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setDocumentSha256(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.sets3Key(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.sets3Bucket(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsCachedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setIsCached(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setCreatedDate(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrJob.setCreatedBy(null);

        // Create the OcrJob, which fails.
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        restOcrJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllOcrJobs() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].requestedLanguage").value(hasItem(DEFAULT_REQUESTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].languageConfidence").value(hasItem(DEFAULT_LANGUAGE_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].tikaEndpoint").value(hasItem(DEFAULT_TIKA_ENDPOINT)))
            .andExpect(jsonPath("$.[*].aiProvider").value(hasItem(DEFAULT_AI_PROVIDER)))
            .andExpect(jsonPath("$.[*].aiModel").value(hasItem(DEFAULT_AI_MODEL)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].costEstimate").value(hasItem(DEFAULT_COST_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getOcrJob() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get the ocrJob
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL_ID, ocrJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ocrJob.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.requestedLanguage").value(DEFAULT_REQUESTED_LANGUAGE))
            .andExpect(jsonPath("$.detectedLanguage").value(DEFAULT_DETECTED_LANGUAGE))
            .andExpect(jsonPath("$.languageConfidence").value(DEFAULT_LANGUAGE_CONFIDENCE))
            .andExpect(jsonPath("$.ocrEngine").value(DEFAULT_OCR_ENGINE.toString()))
            .andExpect(jsonPath("$.tikaEndpoint").value(DEFAULT_TIKA_ENDPOINT))
            .andExpect(jsonPath("$.aiProvider").value(DEFAULT_AI_PROVIDER))
            .andExpect(jsonPath("$.aiModel").value(DEFAULT_AI_MODEL))
            .andExpect(jsonPath("$.resultCacheKey").value(DEFAULT_RESULT_CACHE_KEY))
            .andExpect(jsonPath("$.isCached").value(DEFAULT_IS_CACHED))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.retryCount").value(DEFAULT_RETRY_COUNT))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.intValue()))
            .andExpect(jsonPath("$.costEstimate").value(DEFAULT_COST_ESTIMATE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getOcrJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        Long id = ocrJob.getId();

        defaultOcrJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOcrJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOcrJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOcrJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where status equals to
        defaultOcrJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where status in
        defaultOcrJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where status is not null
        defaultOcrJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId equals to
        defaultOcrJobFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId in
        defaultOcrJobFiltering("documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID, "documentId.in=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId is not null
        defaultOcrJobFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId is greater than or equal to
        defaultOcrJobFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId is less than or equal to
        defaultOcrJobFiltering("documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID, "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId is less than
        defaultOcrJobFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentId is greater than
        defaultOcrJobFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentSha256 equals to
        defaultOcrJobFiltering("documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256, "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256);
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentSha256 in
        defaultOcrJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentSha256 is not null
        defaultOcrJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentSha256 contains
        defaultOcrJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where documentSha256 does not contain
        defaultOcrJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Key equals to
        defaultOcrJobFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Key in
        defaultOcrJobFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Key is not null
        defaultOcrJobFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Key contains
        defaultOcrJobFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Key does not contain
        defaultOcrJobFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3BucketIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Bucket equals to
        defaultOcrJobFiltering("s3Bucket.equals=" + DEFAULT_S_3_BUCKET, "s3Bucket.equals=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3BucketIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Bucket in
        defaultOcrJobFiltering("s3Bucket.in=" + DEFAULT_S_3_BUCKET + "," + UPDATED_S_3_BUCKET, "s3Bucket.in=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3BucketIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Bucket is not null
        defaultOcrJobFiltering("s3Bucket.specified=true", "s3Bucket.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3BucketContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Bucket contains
        defaultOcrJobFiltering("s3Bucket.contains=" + DEFAULT_S_3_BUCKET, "s3Bucket.contains=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrJobsBys3BucketNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where s3Bucket does not contain
        defaultOcrJobFiltering("s3Bucket.doesNotContain=" + UPDATED_S_3_BUCKET, "s3Bucket.doesNotContain=" + DEFAULT_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRequestedLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where requestedLanguage equals to
        defaultOcrJobFiltering(
            "requestedLanguage.equals=" + DEFAULT_REQUESTED_LANGUAGE,
            "requestedLanguage.equals=" + UPDATED_REQUESTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByRequestedLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where requestedLanguage in
        defaultOcrJobFiltering(
            "requestedLanguage.in=" + DEFAULT_REQUESTED_LANGUAGE + "," + UPDATED_REQUESTED_LANGUAGE,
            "requestedLanguage.in=" + UPDATED_REQUESTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByRequestedLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where requestedLanguage is not null
        defaultOcrJobFiltering("requestedLanguage.specified=true", "requestedLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByRequestedLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where requestedLanguage contains
        defaultOcrJobFiltering(
            "requestedLanguage.contains=" + DEFAULT_REQUESTED_LANGUAGE,
            "requestedLanguage.contains=" + UPDATED_REQUESTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByRequestedLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where requestedLanguage does not contain
        defaultOcrJobFiltering(
            "requestedLanguage.doesNotContain=" + UPDATED_REQUESTED_LANGUAGE,
            "requestedLanguage.doesNotContain=" + DEFAULT_REQUESTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDetectedLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where detectedLanguage equals to
        defaultOcrJobFiltering(
            "detectedLanguage.equals=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.equals=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDetectedLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where detectedLanguage in
        defaultOcrJobFiltering(
            "detectedLanguage.in=" + DEFAULT_DETECTED_LANGUAGE + "," + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.in=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDetectedLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where detectedLanguage is not null
        defaultOcrJobFiltering("detectedLanguage.specified=true", "detectedLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByDetectedLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where detectedLanguage contains
        defaultOcrJobFiltering(
            "detectedLanguage.contains=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.contains=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByDetectedLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where detectedLanguage does not contain
        defaultOcrJobFiltering(
            "detectedLanguage.doesNotContain=" + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.doesNotContain=" + DEFAULT_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence equals to
        defaultOcrJobFiltering(
            "languageConfidence.equals=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.equals=" + UPDATED_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence in
        defaultOcrJobFiltering(
            "languageConfidence.in=" + DEFAULT_LANGUAGE_CONFIDENCE + "," + UPDATED_LANGUAGE_CONFIDENCE,
            "languageConfidence.in=" + UPDATED_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence is not null
        defaultOcrJobFiltering("languageConfidence.specified=true", "languageConfidence.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence is greater than or equal to
        defaultOcrJobFiltering(
            "languageConfidence.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.greaterThanOrEqual=" + (DEFAULT_LANGUAGE_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence is less than or equal to
        defaultOcrJobFiltering(
            "languageConfidence.lessThanOrEqual=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.lessThanOrEqual=" + SMALLER_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence is less than
        defaultOcrJobFiltering(
            "languageConfidence.lessThan=" + (DEFAULT_LANGUAGE_CONFIDENCE + 1),
            "languageConfidence.lessThan=" + DEFAULT_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where languageConfidence is greater than
        defaultOcrJobFiltering(
            "languageConfidence.greaterThan=" + SMALLER_LANGUAGE_CONFIDENCE,
            "languageConfidence.greaterThan=" + DEFAULT_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByOcrEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where ocrEngine equals to
        defaultOcrJobFiltering("ocrEngine.equals=" + DEFAULT_OCR_ENGINE, "ocrEngine.equals=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByOcrEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where ocrEngine in
        defaultOcrJobFiltering("ocrEngine.in=" + DEFAULT_OCR_ENGINE + "," + UPDATED_OCR_ENGINE, "ocrEngine.in=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByOcrEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where ocrEngine is not null
        defaultOcrJobFiltering("ocrEngine.specified=true", "ocrEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByTikaEndpointIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where tikaEndpoint equals to
        defaultOcrJobFiltering("tikaEndpoint.equals=" + DEFAULT_TIKA_ENDPOINT, "tikaEndpoint.equals=" + UPDATED_TIKA_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByTikaEndpointIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where tikaEndpoint in
        defaultOcrJobFiltering(
            "tikaEndpoint.in=" + DEFAULT_TIKA_ENDPOINT + "," + UPDATED_TIKA_ENDPOINT,
            "tikaEndpoint.in=" + UPDATED_TIKA_ENDPOINT
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByTikaEndpointIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where tikaEndpoint is not null
        defaultOcrJobFiltering("tikaEndpoint.specified=true", "tikaEndpoint.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByTikaEndpointContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where tikaEndpoint contains
        defaultOcrJobFiltering("tikaEndpoint.contains=" + DEFAULT_TIKA_ENDPOINT, "tikaEndpoint.contains=" + UPDATED_TIKA_ENDPOINT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByTikaEndpointNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where tikaEndpoint does not contain
        defaultOcrJobFiltering(
            "tikaEndpoint.doesNotContain=" + UPDATED_TIKA_ENDPOINT,
            "tikaEndpoint.doesNotContain=" + DEFAULT_TIKA_ENDPOINT
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiProvider equals to
        defaultOcrJobFiltering("aiProvider.equals=" + DEFAULT_AI_PROVIDER, "aiProvider.equals=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiProvider in
        defaultOcrJobFiltering("aiProvider.in=" + DEFAULT_AI_PROVIDER + "," + UPDATED_AI_PROVIDER, "aiProvider.in=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiProvider is not null
        defaultOcrJobFiltering("aiProvider.specified=true", "aiProvider.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiProviderContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiProvider contains
        defaultOcrJobFiltering("aiProvider.contains=" + DEFAULT_AI_PROVIDER, "aiProvider.contains=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiProviderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiProvider does not contain
        defaultOcrJobFiltering("aiProvider.doesNotContain=" + UPDATED_AI_PROVIDER, "aiProvider.doesNotContain=" + DEFAULT_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiModel equals to
        defaultOcrJobFiltering("aiModel.equals=" + DEFAULT_AI_MODEL, "aiModel.equals=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiModel in
        defaultOcrJobFiltering("aiModel.in=" + DEFAULT_AI_MODEL + "," + UPDATED_AI_MODEL, "aiModel.in=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiModel is not null
        defaultOcrJobFiltering("aiModel.specified=true", "aiModel.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiModelContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiModel contains
        defaultOcrJobFiltering("aiModel.contains=" + DEFAULT_AI_MODEL, "aiModel.contains=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllOcrJobsByAiModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where aiModel does not contain
        defaultOcrJobFiltering("aiModel.doesNotContain=" + UPDATED_AI_MODEL, "aiModel.doesNotContain=" + DEFAULT_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllOcrJobsByResultCacheKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where resultCacheKey equals to
        defaultOcrJobFiltering("resultCacheKey.equals=" + DEFAULT_RESULT_CACHE_KEY, "resultCacheKey.equals=" + UPDATED_RESULT_CACHE_KEY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByResultCacheKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where resultCacheKey in
        defaultOcrJobFiltering(
            "resultCacheKey.in=" + DEFAULT_RESULT_CACHE_KEY + "," + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.in=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByResultCacheKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where resultCacheKey is not null
        defaultOcrJobFiltering("resultCacheKey.specified=true", "resultCacheKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByResultCacheKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where resultCacheKey contains
        defaultOcrJobFiltering(
            "resultCacheKey.contains=" + DEFAULT_RESULT_CACHE_KEY,
            "resultCacheKey.contains=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByResultCacheKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where resultCacheKey does not contain
        defaultOcrJobFiltering(
            "resultCacheKey.doesNotContain=" + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.doesNotContain=" + DEFAULT_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByIsCachedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where isCached equals to
        defaultOcrJobFiltering("isCached.equals=" + DEFAULT_IS_CACHED, "isCached.equals=" + UPDATED_IS_CACHED);
    }

    @Test
    @Transactional
    void getAllOcrJobsByIsCachedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where isCached in
        defaultOcrJobFiltering("isCached.in=" + DEFAULT_IS_CACHED + "," + UPDATED_IS_CACHED, "isCached.in=" + UPDATED_IS_CACHED);
    }

    @Test
    @Transactional
    void getAllOcrJobsByIsCachedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where isCached is not null
        defaultOcrJobFiltering("isCached.specified=true", "isCached.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where startDate equals to
        defaultOcrJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where startDate in
        defaultOcrJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where startDate is not null
        defaultOcrJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where endDate equals to
        defaultOcrJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where endDate in
        defaultOcrJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where endDate is not null
        defaultOcrJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount equals to
        defaultOcrJobFiltering("pageCount.equals=" + DEFAULT_PAGE_COUNT, "pageCount.equals=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount in
        defaultOcrJobFiltering("pageCount.in=" + DEFAULT_PAGE_COUNT + "," + UPDATED_PAGE_COUNT, "pageCount.in=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount is not null
        defaultOcrJobFiltering("pageCount.specified=true", "pageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount is greater than or equal to
        defaultOcrJobFiltering("pageCount.greaterThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.greaterThanOrEqual=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount is less than or equal to
        defaultOcrJobFiltering("pageCount.lessThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.lessThanOrEqual=" + SMALLER_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount is less than
        defaultOcrJobFiltering("pageCount.lessThan=" + UPDATED_PAGE_COUNT, "pageCount.lessThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where pageCount is greater than
        defaultOcrJobFiltering("pageCount.greaterThan=" + SMALLER_PAGE_COUNT, "pageCount.greaterThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress equals to
        defaultOcrJobFiltering("progress.equals=" + DEFAULT_PROGRESS, "progress.equals=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress in
        defaultOcrJobFiltering("progress.in=" + DEFAULT_PROGRESS + "," + UPDATED_PROGRESS, "progress.in=" + UPDATED_PROGRESS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress is not null
        defaultOcrJobFiltering("progress.specified=true", "progress.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress is greater than or equal to
        defaultOcrJobFiltering("progress.greaterThanOrEqual=" + DEFAULT_PROGRESS, "progress.greaterThanOrEqual=" + (DEFAULT_PROGRESS + 1));
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress is less than or equal to
        defaultOcrJobFiltering("progress.lessThanOrEqual=" + DEFAULT_PROGRESS, "progress.lessThanOrEqual=" + SMALLER_PROGRESS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress is less than
        defaultOcrJobFiltering("progress.lessThan=" + (DEFAULT_PROGRESS + 1), "progress.lessThan=" + DEFAULT_PROGRESS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProgressIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where progress is greater than
        defaultOcrJobFiltering("progress.greaterThan=" + SMALLER_PROGRESS, "progress.greaterThan=" + DEFAULT_PROGRESS);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount equals to
        defaultOcrJobFiltering("retryCount.equals=" + DEFAULT_RETRY_COUNT, "retryCount.equals=" + UPDATED_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount in
        defaultOcrJobFiltering("retryCount.in=" + DEFAULT_RETRY_COUNT + "," + UPDATED_RETRY_COUNT, "retryCount.in=" + UPDATED_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount is not null
        defaultOcrJobFiltering("retryCount.specified=true", "retryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount is greater than or equal to
        defaultOcrJobFiltering(
            "retryCount.greaterThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.greaterThanOrEqual=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount is less than or equal to
        defaultOcrJobFiltering("retryCount.lessThanOrEqual=" + DEFAULT_RETRY_COUNT, "retryCount.lessThanOrEqual=" + SMALLER_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount is less than
        defaultOcrJobFiltering("retryCount.lessThan=" + UPDATED_RETRY_COUNT, "retryCount.lessThan=" + DEFAULT_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByRetryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where retryCount is greater than
        defaultOcrJobFiltering("retryCount.greaterThan=" + SMALLER_RETRY_COUNT, "retryCount.greaterThan=" + DEFAULT_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority equals to
        defaultOcrJobFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority in
        defaultOcrJobFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority is not null
        defaultOcrJobFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority is greater than or equal to
        defaultOcrJobFiltering("priority.greaterThanOrEqual=" + DEFAULT_PRIORITY, "priority.greaterThanOrEqual=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority is less than or equal to
        defaultOcrJobFiltering("priority.lessThanOrEqual=" + DEFAULT_PRIORITY, "priority.lessThanOrEqual=" + SMALLER_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority is less than
        defaultOcrJobFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where priority is greater than
        defaultOcrJobFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime equals to
        defaultOcrJobFiltering("processingTime.equals=" + DEFAULT_PROCESSING_TIME, "processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime in
        defaultOcrJobFiltering(
            "processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME,
            "processingTime.in=" + UPDATED_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime is not null
        defaultOcrJobFiltering("processingTime.specified=true", "processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime is greater than or equal to
        defaultOcrJobFiltering(
            "processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME,
            "processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime is less than or equal to
        defaultOcrJobFiltering(
            "processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME,
            "processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime is less than
        defaultOcrJobFiltering("processingTime.lessThan=" + UPDATED_PROCESSING_TIME, "processingTime.lessThan=" + DEFAULT_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllOcrJobsByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where processingTime is greater than
        defaultOcrJobFiltering(
            "processingTime.greaterThan=" + SMALLER_PROCESSING_TIME,
            "processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate equals to
        defaultOcrJobFiltering("costEstimate.equals=" + DEFAULT_COST_ESTIMATE, "costEstimate.equals=" + UPDATED_COST_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate in
        defaultOcrJobFiltering(
            "costEstimate.in=" + DEFAULT_COST_ESTIMATE + "," + UPDATED_COST_ESTIMATE,
            "costEstimate.in=" + UPDATED_COST_ESTIMATE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate is not null
        defaultOcrJobFiltering("costEstimate.specified=true", "costEstimate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate is greater than or equal to
        defaultOcrJobFiltering(
            "costEstimate.greaterThanOrEqual=" + DEFAULT_COST_ESTIMATE,
            "costEstimate.greaterThanOrEqual=" + UPDATED_COST_ESTIMATE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate is less than or equal to
        defaultOcrJobFiltering(
            "costEstimate.lessThanOrEqual=" + DEFAULT_COST_ESTIMATE,
            "costEstimate.lessThanOrEqual=" + SMALLER_COST_ESTIMATE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate is less than
        defaultOcrJobFiltering("costEstimate.lessThan=" + UPDATED_COST_ESTIMATE, "costEstimate.lessThan=" + DEFAULT_COST_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCostEstimateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where costEstimate is greater than
        defaultOcrJobFiltering("costEstimate.greaterThan=" + SMALLER_COST_ESTIMATE, "costEstimate.greaterThan=" + DEFAULT_COST_ESTIMATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdDate equals to
        defaultOcrJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdDate in
        defaultOcrJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdDate is not null
        defaultOcrJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdBy equals to
        defaultOcrJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdBy in
        defaultOcrJobFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdBy is not null
        defaultOcrJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdBy contains
        defaultOcrJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllOcrJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where createdBy does not contain
        defaultOcrJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    private void defaultOcrJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOcrJobShouldBeFound(shouldBeFound);
        defaultOcrJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOcrJobShouldBeFound(String filter) throws Exception {
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].requestedLanguage").value(hasItem(DEFAULT_REQUESTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].languageConfidence").value(hasItem(DEFAULT_LANGUAGE_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].tikaEndpoint").value(hasItem(DEFAULT_TIKA_ENDPOINT)))
            .andExpect(jsonPath("$.[*].aiProvider").value(hasItem(DEFAULT_AI_PROVIDER)))
            .andExpect(jsonPath("$.[*].aiModel").value(hasItem(DEFAULT_AI_MODEL)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].costEstimate").value(hasItem(DEFAULT_COST_ESTIMATE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOcrJobShouldNotBeFound(String filter) throws Exception {
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOcrJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOcrJob() throws Exception {
        // Get the ocrJob
        restOcrJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOcrJob() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrJob
        OcrJob updatedOcrJob = ocrJobRepository.findById(ocrJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrJob are not directly saved in db
        em.detach(updatedOcrJob);
        updatedOcrJob
            .status(UPDATED_STATUS)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .requestedLanguage(UPDATED_REQUESTED_LANGUAGE)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
            .processingTime(UPDATED_PROCESSING_TIME)
            .costEstimate(UPDATED_COST_ESTIMATE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(updatedOcrJob);

        restOcrJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrJobDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOcrJobToMatchAllProperties(updatedOcrJob);
    }

    @Test
    @Transactional
    void putNonExistingOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrJobDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateOcrJobWithPatch() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrJob using partial update
        OcrJob partialUpdatedOcrJob = new OcrJob();
        partialUpdatedOcrJob.setId(ocrJob.getId());

        partialUpdatedOcrJob
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .requestedLanguage(UPDATED_REQUESTED_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .endDate(UPDATED_END_DATE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .priority(UPDATED_PRIORITY)
            .costEstimate(UPDATED_COST_ESTIMATE)
            .createdBy(UPDATED_CREATED_BY);

        restOcrJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrJob))
            )
            .andExpect(status().isOk());

        // Validate the OcrJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrJobUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOcrJob, ocrJob), getPersistedOcrJob(ocrJob));
    }

    @Test
    @Transactional
    void fullUpdateOcrJobWithPatch() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrJob using partial update
        OcrJob partialUpdatedOcrJob = new OcrJob();
        partialUpdatedOcrJob.setId(ocrJob.getId());

        partialUpdatedOcrJob
            .status(UPDATED_STATUS)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .requestedLanguage(UPDATED_REQUESTED_LANGUAGE)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
            .processingTime(UPDATED_PROCESSING_TIME)
            .costEstimate(UPDATED_COST_ESTIMATE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restOcrJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrJob))
            )
            .andExpect(status().isOk());

        // Validate the OcrJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrJobUpdatableFieldsEquals(partialUpdatedOcrJob, getPersistedOcrJob(partialUpdatedOcrJob));
    }

    @Test
    @Transactional
    void patchNonExistingOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ocrJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrJob.setId(longCount.incrementAndGet());

        // Create the OcrJob
        OcrJobDTO ocrJobDTO = ocrJobMapper.toDto(ocrJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOcrJob() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ocrJob
        restOcrJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return ocrJobRepository.count();
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

    protected OcrJob getPersistedOcrJob(OcrJob ocrJob) {
        return ocrJobRepository.findById(ocrJob.getId()).orElseThrow();
    }

    protected void assertPersistedOcrJobToMatchAllProperties(OcrJob expectedOcrJob) {
        assertOcrJobAllPropertiesEquals(expectedOcrJob, getPersistedOcrJob(expectedOcrJob));
    }

    protected void assertPersistedOcrJobToMatchUpdatableProperties(OcrJob expectedOcrJob) {
        assertOcrJobAllUpdatablePropertiesEquals(expectedOcrJob, getPersistedOcrJob(expectedOcrJob));
    }
}
