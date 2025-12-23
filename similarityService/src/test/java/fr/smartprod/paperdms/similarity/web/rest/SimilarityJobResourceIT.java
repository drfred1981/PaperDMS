package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.SimilarityJobAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.domain.enumeration.AiJobStatus;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityScope;
import fr.smartprod.paperdms.similarity.repository.SimilarityJobRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityJobMapper;
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
 * Integration tests for the {@link SimilarityJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimilarityJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final AiJobStatus DEFAULT_STATUS = AiJobStatus.PENDING;
    private static final AiJobStatus UPDATED_STATUS = AiJobStatus.IN_PROGRESS;

    private static final SimilarityAlgorithm DEFAULT_ALGORITHM = SimilarityAlgorithm.COSINE;
    private static final SimilarityAlgorithm UPDATED_ALGORITHM = SimilarityAlgorithm.JACCARD;

    private static final SimilarityScope DEFAULT_SCOPE = SimilarityScope.ALL_DOCUMENTS;
    private static final SimilarityScope UPDATED_SCOPE = SimilarityScope.SAME_FOLDER;

    private static final Double DEFAULT_MIN_SIMILARITY_THRESHOLD = 0D;
    private static final Double UPDATED_MIN_SIMILARITY_THRESHOLD = 1D;
    private static final Double SMALLER_MIN_SIMILARITY_THRESHOLD = 0D - 1D;

    private static final Integer DEFAULT_MATCHES_FOUND = 1;
    private static final Integer UPDATED_MATCHES_FOUND = 2;
    private static final Integer SMALLER_MATCHES_FOUND = 1 - 1;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/similarity-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SimilarityJobRepository similarityJobRepository;

    @Autowired
    private SimilarityJobMapper similarityJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimilarityJobMockMvc;

    private SimilarityJob similarityJob;

    private SimilarityJob insertedSimilarityJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityJob createEntity() {
        return new SimilarityJob()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .status(DEFAULT_STATUS)
            .algorithm(DEFAULT_ALGORITHM)
            .scope(DEFAULT_SCOPE)
            .minSimilarityThreshold(DEFAULT_MIN_SIMILARITY_THRESHOLD)
            .matchesFound(DEFAULT_MATCHES_FOUND)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityJob createUpdatedEntity() {
        return new SimilarityJob()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .algorithm(UPDATED_ALGORITHM)
            .scope(UPDATED_SCOPE)
            .minSimilarityThreshold(UPDATED_MIN_SIMILARITY_THRESHOLD)
            .matchesFound(UPDATED_MATCHES_FOUND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        similarityJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSimilarityJob != null) {
            similarityJobRepository.delete(insertedSimilarityJob);
            insertedSimilarityJob = null;
        }
    }

    @Test
    @Transactional
    void createSimilarityJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);
        var returnedSimilarityJobDTO = om.readValue(
            restSimilarityJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SimilarityJobDTO.class
        );

        // Validate the SimilarityJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSimilarityJob = similarityJobMapper.toEntity(returnedSimilarityJobDTO);
        assertSimilarityJobUpdatableFieldsEquals(returnedSimilarityJob, getPersistedSimilarityJob(returnedSimilarityJob));

        insertedSimilarityJob = returnedSimilarityJob;
    }

    @Test
    @Transactional
    void createSimilarityJobWithExistingId() throws Exception {
        // Create the SimilarityJob with an existing ID
        similarityJob.setId(1L);
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimilarityJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        similarityJob.setDocumentId(null);

        // Create the SimilarityJob, which fails.
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        restSimilarityJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        similarityJob.setDocumentSha256(null);

        // Create the SimilarityJob, which fails.
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        restSimilarityJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        similarityJob.setCreatedDate(null);

        // Create the SimilarityJob, which fails.
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        restSimilarityJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        similarityJob.setCreatedBy(null);

        // Create the SimilarityJob, which fails.
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        restSimilarityJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSimilarityJobs() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].minSimilarityThreshold").value(hasItem(DEFAULT_MIN_SIMILARITY_THRESHOLD)))
            .andExpect(jsonPath("$.[*].matchesFound").value(hasItem(DEFAULT_MATCHES_FOUND)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getSimilarityJob() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get the similarityJob
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL_ID, similarityJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(similarityJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
            .andExpect(jsonPath("$.scope").value(DEFAULT_SCOPE.toString()))
            .andExpect(jsonPath("$.minSimilarityThreshold").value(DEFAULT_MIN_SIMILARITY_THRESHOLD))
            .andExpect(jsonPath("$.matchesFound").value(DEFAULT_MATCHES_FOUND))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getSimilarityJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        Long id = similarityJob.getId();

        defaultSimilarityJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSimilarityJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSimilarityJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId equals to
        defaultSimilarityJobFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId in
        defaultSimilarityJobFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId is not null
        defaultSimilarityJobFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId is greater than or equal to
        defaultSimilarityJobFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId is less than or equal to
        defaultSimilarityJobFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId is less than
        defaultSimilarityJobFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentId is greater than
        defaultSimilarityJobFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentSha256 equals to
        defaultSimilarityJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentSha256 in
        defaultSimilarityJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentSha256 is not null
        defaultSimilarityJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentSha256 contains
        defaultSimilarityJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where documentSha256 does not contain
        defaultSimilarityJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where status equals to
        defaultSimilarityJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where status in
        defaultSimilarityJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where status is not null
        defaultSimilarityJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where algorithm equals to
        defaultSimilarityJobFiltering("algorithm.equals=" + DEFAULT_ALGORITHM, "algorithm.equals=" + UPDATED_ALGORITHM);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByAlgorithmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where algorithm in
        defaultSimilarityJobFiltering("algorithm.in=" + DEFAULT_ALGORITHM + "," + UPDATED_ALGORITHM, "algorithm.in=" + UPDATED_ALGORITHM);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByAlgorithmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where algorithm is not null
        defaultSimilarityJobFiltering("algorithm.specified=true", "algorithm.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByScopeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where scope equals to
        defaultSimilarityJobFiltering("scope.equals=" + DEFAULT_SCOPE, "scope.equals=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByScopeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where scope in
        defaultSimilarityJobFiltering("scope.in=" + DEFAULT_SCOPE + "," + UPDATED_SCOPE, "scope.in=" + UPDATED_SCOPE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByScopeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where scope is not null
        defaultSimilarityJobFiltering("scope.specified=true", "scope.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold equals to
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.equals=" + DEFAULT_MIN_SIMILARITY_THRESHOLD,
            "minSimilarityThreshold.equals=" + UPDATED_MIN_SIMILARITY_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold in
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.in=" + DEFAULT_MIN_SIMILARITY_THRESHOLD + "," + UPDATED_MIN_SIMILARITY_THRESHOLD,
            "minSimilarityThreshold.in=" + UPDATED_MIN_SIMILARITY_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold is not null
        defaultSimilarityJobFiltering("minSimilarityThreshold.specified=true", "minSimilarityThreshold.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold is greater than or equal to
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.greaterThanOrEqual=" + DEFAULT_MIN_SIMILARITY_THRESHOLD,
            "minSimilarityThreshold.greaterThanOrEqual=" + (DEFAULT_MIN_SIMILARITY_THRESHOLD + 1)
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold is less than or equal to
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.lessThanOrEqual=" + DEFAULT_MIN_SIMILARITY_THRESHOLD,
            "minSimilarityThreshold.lessThanOrEqual=" + SMALLER_MIN_SIMILARITY_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold is less than
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.lessThan=" + (DEFAULT_MIN_SIMILARITY_THRESHOLD + 1),
            "minSimilarityThreshold.lessThan=" + DEFAULT_MIN_SIMILARITY_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMinSimilarityThresholdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where minSimilarityThreshold is greater than
        defaultSimilarityJobFiltering(
            "minSimilarityThreshold.greaterThan=" + SMALLER_MIN_SIMILARITY_THRESHOLD,
            "minSimilarityThreshold.greaterThan=" + DEFAULT_MIN_SIMILARITY_THRESHOLD
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound equals to
        defaultSimilarityJobFiltering("matchesFound.equals=" + DEFAULT_MATCHES_FOUND, "matchesFound.equals=" + UPDATED_MATCHES_FOUND);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound in
        defaultSimilarityJobFiltering(
            "matchesFound.in=" + DEFAULT_MATCHES_FOUND + "," + UPDATED_MATCHES_FOUND,
            "matchesFound.in=" + UPDATED_MATCHES_FOUND
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound is not null
        defaultSimilarityJobFiltering("matchesFound.specified=true", "matchesFound.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound is greater than or equal to
        defaultSimilarityJobFiltering(
            "matchesFound.greaterThanOrEqual=" + DEFAULT_MATCHES_FOUND,
            "matchesFound.greaterThanOrEqual=" + UPDATED_MATCHES_FOUND
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound is less than or equal to
        defaultSimilarityJobFiltering(
            "matchesFound.lessThanOrEqual=" + DEFAULT_MATCHES_FOUND,
            "matchesFound.lessThanOrEqual=" + SMALLER_MATCHES_FOUND
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound is less than
        defaultSimilarityJobFiltering("matchesFound.lessThan=" + UPDATED_MATCHES_FOUND, "matchesFound.lessThan=" + DEFAULT_MATCHES_FOUND);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByMatchesFoundIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where matchesFound is greater than
        defaultSimilarityJobFiltering(
            "matchesFound.greaterThan=" + SMALLER_MATCHES_FOUND,
            "matchesFound.greaterThan=" + DEFAULT_MATCHES_FOUND
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where startDate equals to
        defaultSimilarityJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where startDate in
        defaultSimilarityJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where startDate is not null
        defaultSimilarityJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where endDate equals to
        defaultSimilarityJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where endDate in
        defaultSimilarityJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where endDate is not null
        defaultSimilarityJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdDate equals to
        defaultSimilarityJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdDate in
        defaultSimilarityJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdDate is not null
        defaultSimilarityJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdBy equals to
        defaultSimilarityJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdBy in
        defaultSimilarityJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdBy is not null
        defaultSimilarityJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdBy contains
        defaultSimilarityJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSimilarityJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        // Get all the similarityJobList where createdBy does not contain
        defaultSimilarityJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    private void defaultSimilarityJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSimilarityJobShouldBeFound(shouldBeFound);
        defaultSimilarityJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimilarityJobShouldBeFound(String filter) throws Exception {
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].scope").value(hasItem(DEFAULT_SCOPE.toString())))
            .andExpect(jsonPath("$.[*].minSimilarityThreshold").value(hasItem(DEFAULT_MIN_SIMILARITY_THRESHOLD)))
            .andExpect(jsonPath("$.[*].matchesFound").value(hasItem(DEFAULT_MATCHES_FOUND)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimilarityJobShouldNotBeFound(String filter) throws Exception {
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimilarityJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimilarityJob() throws Exception {
        // Get the similarityJob
        restSimilarityJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimilarityJob() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityJob
        SimilarityJob updatedSimilarityJob = similarityJobRepository.findById(similarityJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSimilarityJob are not directly saved in db
        em.detach(updatedSimilarityJob);
        updatedSimilarityJob
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .algorithm(UPDATED_ALGORITHM)
            .scope(UPDATED_SCOPE)
            .minSimilarityThreshold(UPDATED_MIN_SIMILARITY_THRESHOLD)
            .matchesFound(UPDATED_MATCHES_FOUND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(updatedSimilarityJob);

        restSimilarityJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSimilarityJobToMatchAllProperties(updatedSimilarityJob);
    }

    @Test
    @Transactional
    void putNonExistingSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSimilarityJobWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityJob using partial update
        SimilarityJob partialUpdatedSimilarityJob = new SimilarityJob();
        partialUpdatedSimilarityJob.setId(similarityJob.getId());

        partialUpdatedSimilarityJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .algorithm(UPDATED_ALGORITHM)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restSimilarityJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityJob))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSimilarityJob, similarityJob),
            getPersistedSimilarityJob(similarityJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateSimilarityJobWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityJob using partial update
        SimilarityJob partialUpdatedSimilarityJob = new SimilarityJob();
        partialUpdatedSimilarityJob.setId(similarityJob.getId());

        partialUpdatedSimilarityJob
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .algorithm(UPDATED_ALGORITHM)
            .scope(UPDATED_SCOPE)
            .minSimilarityThreshold(UPDATED_MIN_SIMILARITY_THRESHOLD)
            .matchesFound(UPDATED_MATCHES_FOUND)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restSimilarityJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityJob))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityJobUpdatableFieldsEquals(partialUpdatedSimilarityJob, getPersistedSimilarityJob(partialUpdatedSimilarityJob));
    }

    @Test
    @Transactional
    void patchNonExistingSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, similarityJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimilarityJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityJob.setId(longCount.incrementAndGet());

        // Create the SimilarityJob
        SimilarityJobDTO similarityJobDTO = similarityJobMapper.toDto(similarityJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(similarityJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSimilarityJob() throws Exception {
        // Initialize the database
        insertedSimilarityJob = similarityJobRepository.saveAndFlush(similarityJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the similarityJob
        restSimilarityJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, similarityJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return similarityJobRepository.count();
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

    protected SimilarityJob getPersistedSimilarityJob(SimilarityJob similarityJob) {
        return similarityJobRepository.findById(similarityJob.getId()).orElseThrow();
    }

    protected void assertPersistedSimilarityJobToMatchAllProperties(SimilarityJob expectedSimilarityJob) {
        assertSimilarityJobAllPropertiesEquals(expectedSimilarityJob, getPersistedSimilarityJob(expectedSimilarityJob));
    }

    protected void assertPersistedSimilarityJobToMatchUpdatableProperties(SimilarityJob expectedSimilarityJob) {
        assertSimilarityJobAllUpdatablePropertiesEquals(expectedSimilarityJob, getPersistedSimilarityJob(expectedSimilarityJob));
    }
}
