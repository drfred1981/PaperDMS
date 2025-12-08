package com.ged.ai.web.rest;

import static com.ged.ai.domain.AutoTagJobAsserts.*;
import static com.ged.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ai.IntegrationTest;
import com.ged.ai.domain.AutoTagJob;
import com.ged.ai.domain.enumeration.AiJobStatus;
import com.ged.ai.repository.AutoTagJobRepository;
import com.ged.ai.service.dto.AutoTagJobDTO;
import com.ged.ai.service.mapper.AutoTagJobMapper;
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
 * Integration tests for the {@link AutoTagJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AutoTagJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT = "BBBBBBBBBB";

    private static final AiJobStatus DEFAULT_STATUS = AiJobStatus.PENDING;
    private static final AiJobStatus UPDATED_STATUS = AiJobStatus.IN_PROGRESS;

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/auto-tag-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AutoTagJobRepository autoTagJobRepository;

    @Autowired
    private AutoTagJobMapper autoTagJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAutoTagJobMockMvc;

    private AutoTagJob autoTagJob;

    private AutoTagJob insertedAutoTagJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoTagJob createEntity() {
        return new AutoTagJob()
            .documentId(DEFAULT_DOCUMENT_ID)
            .s3Key(DEFAULT_S_3_KEY)
            .extractedText(DEFAULT_EXTRACTED_TEXT)
            .status(DEFAULT_STATUS)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .confidence(DEFAULT_CONFIDENCE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AutoTagJob createUpdatedEntity() {
        return new AutoTagJob()
            .documentId(UPDATED_DOCUMENT_ID)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .confidence(UPDATED_CONFIDENCE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        autoTagJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAutoTagJob != null) {
            autoTagJobRepository.delete(insertedAutoTagJob);
            insertedAutoTagJob = null;
        }
    }

    @Test
    @Transactional
    void createAutoTagJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);
        var returnedAutoTagJobDTO = om.readValue(
            restAutoTagJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AutoTagJobDTO.class
        );

        // Validate the AutoTagJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAutoTagJob = autoTagJobMapper.toEntity(returnedAutoTagJobDTO);
        assertAutoTagJobUpdatableFieldsEquals(returnedAutoTagJob, getPersistedAutoTagJob(returnedAutoTagJob));

        insertedAutoTagJob = returnedAutoTagJob;
    }

    @Test
    @Transactional
    void createAutoTagJobWithExistingId() throws Exception {
        // Create the AutoTagJob with an existing ID
        autoTagJob.setId(1L);
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autoTagJob.setDocumentId(null);

        // Create the AutoTagJob, which fails.
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        restAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autoTagJob.sets3Key(null);

        // Create the AutoTagJob, which fails.
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        restAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autoTagJob.setStatus(null);

        // Create the AutoTagJob, which fails.
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        restAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        autoTagJob.setCreatedDate(null);

        // Create the AutoTagJob, which fails.
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        restAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAutoTagJobs() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoTagJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAutoTagJob() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get the autoTagJob
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL_ID, autoTagJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(autoTagJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.extractedText").value(DEFAULT_EXTRACTED_TEXT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAutoTagJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        Long id = autoTagJob.getId();

        defaultAutoTagJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAutoTagJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAutoTagJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId equals to
        defaultAutoTagJobFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId in
        defaultAutoTagJobFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId is not null
        defaultAutoTagJobFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId is greater than or equal to
        defaultAutoTagJobFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId is less than or equal to
        defaultAutoTagJobFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId is less than
        defaultAutoTagJobFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where documentId is greater than
        defaultAutoTagJobFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where s3Key equals to
        defaultAutoTagJobFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where s3Key in
        defaultAutoTagJobFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where s3Key is not null
        defaultAutoTagJobFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where s3Key contains
        defaultAutoTagJobFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where s3Key does not contain
        defaultAutoTagJobFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where status equals to
        defaultAutoTagJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where status in
        defaultAutoTagJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where status is not null
        defaultAutoTagJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where modelVersion equals to
        defaultAutoTagJobFiltering("modelVersion.equals=" + DEFAULT_MODEL_VERSION, "modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where modelVersion in
        defaultAutoTagJobFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where modelVersion is not null
        defaultAutoTagJobFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where modelVersion contains
        defaultAutoTagJobFiltering("modelVersion.contains=" + DEFAULT_MODEL_VERSION, "modelVersion.contains=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where modelVersion does not contain
        defaultAutoTagJobFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where startDate equals to
        defaultAutoTagJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where startDate in
        defaultAutoTagJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where startDate is not null
        defaultAutoTagJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where endDate equals to
        defaultAutoTagJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where endDate in
        defaultAutoTagJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where endDate is not null
        defaultAutoTagJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence equals to
        defaultAutoTagJobFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence in
        defaultAutoTagJobFiltering("confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE, "confidence.in=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence is not null
        defaultAutoTagJobFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence is greater than or equal to
        defaultAutoTagJobFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence is less than or equal to
        defaultAutoTagJobFiltering("confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE, "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence is less than
        defaultAutoTagJobFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where confidence is greater than
        defaultAutoTagJobFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where createdDate equals to
        defaultAutoTagJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where createdDate in
        defaultAutoTagJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAutoTagJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        // Get all the autoTagJobList where createdDate is not null
        defaultAutoTagJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultAutoTagJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAutoTagJobShouldBeFound(shouldBeFound);
        defaultAutoTagJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAutoTagJobShouldBeFound(String filter) throws Exception {
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(autoTagJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAutoTagJobShouldNotBeFound(String filter) throws Exception {
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAutoTagJob() throws Exception {
        // Get the autoTagJob
        restAutoTagJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAutoTagJob() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autoTagJob
        AutoTagJob updatedAutoTagJob = autoTagJobRepository.findById(autoTagJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAutoTagJob are not directly saved in db
        em.detach(updatedAutoTagJob);
        updatedAutoTagJob
            .documentId(UPDATED_DOCUMENT_ID)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .confidence(UPDATED_CONFIDENCE)
            .createdDate(UPDATED_CREATED_DATE);
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(updatedAutoTagJob);

        restAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoTagJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoTagJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAutoTagJobToMatchAllProperties(updatedAutoTagJob);
    }

    @Test
    @Transactional
    void putNonExistingAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, autoTagJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(autoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAutoTagJobWithPatch() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autoTagJob using partial update
        AutoTagJob partialUpdatedAutoTagJob = new AutoTagJob();
        partialUpdatedAutoTagJob.setId(autoTagJob.getId());

        partialUpdatedAutoTagJob.status(UPDATED_STATUS).modelVersion(UPDATED_MODEL_VERSION).confidence(UPDATED_CONFIDENCE);

        restAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutoTagJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutoTagJob))
            )
            .andExpect(status().isOk());

        // Validate the AutoTagJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoTagJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAutoTagJob, autoTagJob),
            getPersistedAutoTagJob(autoTagJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateAutoTagJobWithPatch() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the autoTagJob using partial update
        AutoTagJob partialUpdatedAutoTagJob = new AutoTagJob();
        partialUpdatedAutoTagJob.setId(autoTagJob.getId());

        partialUpdatedAutoTagJob
            .documentId(UPDATED_DOCUMENT_ID)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .confidence(UPDATED_CONFIDENCE)
            .createdDate(UPDATED_CREATED_DATE);

        restAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAutoTagJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAutoTagJob))
            )
            .andExpect(status().isOk());

        // Validate the AutoTagJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAutoTagJobUpdatableFieldsEquals(partialUpdatedAutoTagJob, getPersistedAutoTagJob(partialUpdatedAutoTagJob));
    }

    @Test
    @Transactional
    void patchNonExistingAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, autoTagJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(autoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        autoTagJob.setId(longCount.incrementAndGet());

        // Create the AutoTagJob
        AutoTagJobDTO autoTagJobDTO = autoTagJobMapper.toDto(autoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAutoTagJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(autoTagJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAutoTagJob() throws Exception {
        // Initialize the database
        insertedAutoTagJob = autoTagJobRepository.saveAndFlush(autoTagJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the autoTagJob
        restAutoTagJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, autoTagJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return autoTagJobRepository.count();
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

    protected AutoTagJob getPersistedAutoTagJob(AutoTagJob autoTagJob) {
        return autoTagJobRepository.findById(autoTagJob.getId()).orElseThrow();
    }

    protected void assertPersistedAutoTagJobToMatchAllProperties(AutoTagJob expectedAutoTagJob) {
        assertAutoTagJobAllPropertiesEquals(expectedAutoTagJob, getPersistedAutoTagJob(expectedAutoTagJob));
    }

    protected void assertPersistedAutoTagJobToMatchUpdatableProperties(AutoTagJob expectedAutoTagJob) {
        assertAutoTagJobAllUpdatablePropertiesEquals(expectedAutoTagJob, getPersistedAutoTagJob(expectedAutoTagJob));
    }
}
