package com.ged.ocr.web.rest;

import static com.ged.ocr.domain.OcrJobAsserts.*;
import static com.ged.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ocr.IntegrationTest;
import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.domain.TikaConfiguration;
import com.ged.ocr.domain.enumeration.OcrStatus;
import com.ged.ocr.repository.OcrJobRepository;
import com.ged.ocr.service.dto.OcrJobDTO;
import com.ged.ocr.service.mapper.OcrJobMapper;
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

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_TIKA_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_TIKA_ENDPOINT = "BBBBBBBBBB";

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
            .s3Key(DEFAULT_S_3_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .language(DEFAULT_LANGUAGE)
            .tikaEndpoint(DEFAULT_TIKA_ENDPOINT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .pageCount(DEFAULT_PAGE_COUNT)
            .progress(DEFAULT_PROGRESS)
            .retryCount(DEFAULT_RETRY_COUNT)
            .priority(DEFAULT_PRIORITY)
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
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .language(UPDATED_LANGUAGE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
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
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].tikaEndpoint").value(hasItem(DEFAULT_TIKA_ENDPOINT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
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
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.tikaEndpoint").value(DEFAULT_TIKA_ENDPOINT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.progress").value(DEFAULT_PROGRESS))
            .andExpect(jsonPath("$.retryCount").value(DEFAULT_RETRY_COUNT))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
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
    void getAllOcrJobsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where language equals to
        defaultOcrJobFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where language in
        defaultOcrJobFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where language is not null
        defaultOcrJobFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where language contains
        defaultOcrJobFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrJobsByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrJob = ocrJobRepository.saveAndFlush(ocrJob);

        // Get all the ocrJobList where language does not contain
        defaultOcrJobFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
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

    @Test
    @Transactional
    void getAllOcrJobsByTikaConfigIsEqualToSomething() throws Exception {
        TikaConfiguration tikaConfig;
        if (TestUtil.findAll(em, TikaConfiguration.class).isEmpty()) {
            ocrJobRepository.saveAndFlush(ocrJob);
            tikaConfig = TikaConfigurationResourceIT.createEntity();
        } else {
            tikaConfig = TestUtil.findAll(em, TikaConfiguration.class).get(0);
        }
        em.persist(tikaConfig);
        em.flush();
        ocrJob.setTikaConfig(tikaConfig);
        ocrJobRepository.saveAndFlush(ocrJob);
        Long tikaConfigId = tikaConfig.getId();
        // Get all the ocrJobList where tikaConfig equals to tikaConfigId
        defaultOcrJobShouldBeFound("tikaConfigId.equals=" + tikaConfigId);

        // Get all the ocrJobList where tikaConfig equals to (tikaConfigId + 1)
        defaultOcrJobShouldNotBeFound("tikaConfigId.equals=" + (tikaConfigId + 1));
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
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].tikaEndpoint").value(hasItem(DEFAULT_TIKA_ENDPOINT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].progress").value(hasItem(DEFAULT_PROGRESS)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
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
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .language(UPDATED_LANGUAGE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
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
            .s3Key(UPDATED_S_3_KEY)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .priority(UPDATED_PRIORITY);

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
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .language(UPDATED_LANGUAGE)
            .tikaEndpoint(UPDATED_TIKA_ENDPOINT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .progress(UPDATED_PROGRESS)
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
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
