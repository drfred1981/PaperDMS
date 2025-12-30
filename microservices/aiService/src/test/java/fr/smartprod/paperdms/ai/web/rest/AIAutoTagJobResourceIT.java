package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AIAutoTagJobAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.domain.enumeration.AiJobStatus;
import fr.smartprod.paperdms.ai.repository.AIAutoTagJobRepository;
import fr.smartprod.paperdms.ai.repository.search.AIAutoTagJobSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AIAutoTagJobDTO;
import fr.smartprod.paperdms.ai.service.mapper.AIAutoTagJobMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AIAutoTagJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AIAutoTagJobResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_TEXT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT_SHA_256 = "BBBBBBBBBB";

    private static final AiJobStatus DEFAULT_STATUS = AiJobStatus.PENDING;
    private static final AiJobStatus UPDATED_STATUS = AiJobStatus.IN_PROGRESS;

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

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

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ai-auto-tag-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-auto-tag-jobs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AIAutoTagJobRepository aIAutoTagJobRepository;

    @Autowired
    private AIAutoTagJobMapper aIAutoTagJobMapper;

    @Autowired
    private AIAutoTagJobSearchRepository aIAutoTagJobSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAIAutoTagJobMockMvc;

    private AIAutoTagJob aIAutoTagJob;

    private AIAutoTagJob insertedAIAutoTagJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIAutoTagJob createEntity() {
        return new AIAutoTagJob()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .extractedText(DEFAULT_EXTRACTED_TEXT)
            .extractedTextSha256(DEFAULT_EXTRACTED_TEXT_SHA_256)
            .status(DEFAULT_STATUS)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .resultCacheKey(DEFAULT_RESULT_CACHE_KEY)
            .isCached(DEFAULT_IS_CACHED)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AIAutoTagJob createUpdatedEntity() {
        return new AIAutoTagJob()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .extractedTextSha256(UPDATED_EXTRACTED_TEXT_SHA_256)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        aIAutoTagJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAIAutoTagJob != null) {
            aIAutoTagJobRepository.delete(insertedAIAutoTagJob);
            aIAutoTagJobSearchRepository.delete(insertedAIAutoTagJob);
            insertedAIAutoTagJob = null;
        }
    }

    @Test
    @Transactional
    void createAIAutoTagJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);
        var returnedAIAutoTagJobDTO = om.readValue(
            restAIAutoTagJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AIAutoTagJobDTO.class
        );

        // Validate the AIAutoTagJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAIAutoTagJob = aIAutoTagJobMapper.toEntity(returnedAIAutoTagJobDTO);
        assertAIAutoTagJobUpdatableFieldsEquals(returnedAIAutoTagJob, getPersistedAIAutoTagJob(returnedAIAutoTagJob));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAIAutoTagJob = returnedAIAutoTagJob;
    }

    @Test
    @Transactional
    void createAIAutoTagJobWithExistingId() throws Exception {
        // Create the AIAutoTagJob with an existing ID
        aIAutoTagJob.setId(1L);
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAIAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        // set the field null
        aIAutoTagJob.setDocumentSha256(null);

        // Create the AIAutoTagJob, which fails.
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        restAIAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        // set the field null
        aIAutoTagJob.sets3Key(null);

        // Create the AIAutoTagJob, which fails.
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        restAIAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsCachedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        // set the field null
        aIAutoTagJob.setIsCached(null);

        // Create the AIAutoTagJob, which fails.
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        restAIAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        // set the field null
        aIAutoTagJob.setCreatedDate(null);

        // Create the AIAutoTagJob, which fails.
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        restAIAutoTagJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobs() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIAutoTagJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].extractedTextSha256").value(hasItem(DEFAULT_EXTRACTED_TEXT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getAIAutoTagJob() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get the aIAutoTagJob
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL_ID, aIAutoTagJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aIAutoTagJob.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.extractedText").value(DEFAULT_EXTRACTED_TEXT))
            .andExpect(jsonPath("$.extractedTextSha256").value(DEFAULT_EXTRACTED_TEXT_SHA_256))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION))
            .andExpect(jsonPath("$.resultCacheKey").value(DEFAULT_RESULT_CACHE_KEY))
            .andExpect(jsonPath("$.isCached").value(DEFAULT_IS_CACHED))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getAIAutoTagJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        Long id = aIAutoTagJob.getId();

        defaultAIAutoTagJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAIAutoTagJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAIAutoTagJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where documentSha256 equals to
        defaultAIAutoTagJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where documentSha256 in
        defaultAIAutoTagJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where documentSha256 is not null
        defaultAIAutoTagJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where documentSha256 contains
        defaultAIAutoTagJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where documentSha256 does not contain
        defaultAIAutoTagJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where s3Key equals to
        defaultAIAutoTagJobFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where s3Key in
        defaultAIAutoTagJobFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where s3Key is not null
        defaultAIAutoTagJobFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where s3Key contains
        defaultAIAutoTagJobFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where s3Key does not contain
        defaultAIAutoTagJobFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByExtractedTextSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where extractedTextSha256 equals to
        defaultAIAutoTagJobFiltering(
            "extractedTextSha256.equals=" + DEFAULT_EXTRACTED_TEXT_SHA_256,
            "extractedTextSha256.equals=" + UPDATED_EXTRACTED_TEXT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByExtractedTextSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where extractedTextSha256 in
        defaultAIAutoTagJobFiltering(
            "extractedTextSha256.in=" + DEFAULT_EXTRACTED_TEXT_SHA_256 + "," + UPDATED_EXTRACTED_TEXT_SHA_256,
            "extractedTextSha256.in=" + UPDATED_EXTRACTED_TEXT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByExtractedTextSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where extractedTextSha256 is not null
        defaultAIAutoTagJobFiltering("extractedTextSha256.specified=true", "extractedTextSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByExtractedTextSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where extractedTextSha256 contains
        defaultAIAutoTagJobFiltering(
            "extractedTextSha256.contains=" + DEFAULT_EXTRACTED_TEXT_SHA_256,
            "extractedTextSha256.contains=" + UPDATED_EXTRACTED_TEXT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByExtractedTextSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where extractedTextSha256 does not contain
        defaultAIAutoTagJobFiltering(
            "extractedTextSha256.doesNotContain=" + UPDATED_EXTRACTED_TEXT_SHA_256,
            "extractedTextSha256.doesNotContain=" + DEFAULT_EXTRACTED_TEXT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where status equals to
        defaultAIAutoTagJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where status in
        defaultAIAutoTagJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where status is not null
        defaultAIAutoTagJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where modelVersion equals to
        defaultAIAutoTagJobFiltering("modelVersion.equals=" + DEFAULT_MODEL_VERSION, "modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where modelVersion in
        defaultAIAutoTagJobFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where modelVersion is not null
        defaultAIAutoTagJobFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where modelVersion contains
        defaultAIAutoTagJobFiltering("modelVersion.contains=" + DEFAULT_MODEL_VERSION, "modelVersion.contains=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where modelVersion does not contain
        defaultAIAutoTagJobFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByResultCacheKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where resultCacheKey equals to
        defaultAIAutoTagJobFiltering(
            "resultCacheKey.equals=" + DEFAULT_RESULT_CACHE_KEY,
            "resultCacheKey.equals=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByResultCacheKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where resultCacheKey in
        defaultAIAutoTagJobFiltering(
            "resultCacheKey.in=" + DEFAULT_RESULT_CACHE_KEY + "," + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.in=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByResultCacheKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where resultCacheKey is not null
        defaultAIAutoTagJobFiltering("resultCacheKey.specified=true", "resultCacheKey.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByResultCacheKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where resultCacheKey contains
        defaultAIAutoTagJobFiltering(
            "resultCacheKey.contains=" + DEFAULT_RESULT_CACHE_KEY,
            "resultCacheKey.contains=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByResultCacheKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where resultCacheKey does not contain
        defaultAIAutoTagJobFiltering(
            "resultCacheKey.doesNotContain=" + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.doesNotContain=" + DEFAULT_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByIsCachedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where isCached equals to
        defaultAIAutoTagJobFiltering("isCached.equals=" + DEFAULT_IS_CACHED, "isCached.equals=" + UPDATED_IS_CACHED);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByIsCachedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where isCached in
        defaultAIAutoTagJobFiltering("isCached.in=" + DEFAULT_IS_CACHED + "," + UPDATED_IS_CACHED, "isCached.in=" + UPDATED_IS_CACHED);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByIsCachedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where isCached is not null
        defaultAIAutoTagJobFiltering("isCached.specified=true", "isCached.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where startDate equals to
        defaultAIAutoTagJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where startDate in
        defaultAIAutoTagJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where startDate is not null
        defaultAIAutoTagJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where endDate equals to
        defaultAIAutoTagJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where endDate in
        defaultAIAutoTagJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where endDate is not null
        defaultAIAutoTagJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where createdDate equals to
        defaultAIAutoTagJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where createdDate in
        defaultAIAutoTagJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        // Get all the aIAutoTagJobList where createdDate is not null
        defaultAIAutoTagJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByAITypePredictionIsEqualToSomething() throws Exception {
        AITypePrediction aITypePrediction;
        if (TestUtil.findAll(em, AITypePrediction.class).isEmpty()) {
            aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
            aITypePrediction = AITypePredictionResourceIT.createEntity();
        } else {
            aITypePrediction = TestUtil.findAll(em, AITypePrediction.class).get(0);
        }
        em.persist(aITypePrediction);
        em.flush();
        aIAutoTagJob.setAITypePrediction(aITypePrediction);
        aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
        Long aITypePredictionId = aITypePrediction.getId();
        // Get all the aIAutoTagJobList where aITypePrediction equals to aITypePredictionId
        defaultAIAutoTagJobShouldBeFound("aITypePredictionId.equals=" + aITypePredictionId);

        // Get all the aIAutoTagJobList where aITypePrediction equals to (aITypePredictionId + 1)
        defaultAIAutoTagJobShouldNotBeFound("aITypePredictionId.equals=" + (aITypePredictionId + 1));
    }

    @Test
    @Transactional
    void getAllAIAutoTagJobsByLanguagePredictionIsEqualToSomething() throws Exception {
        AILanguageDetection languagePrediction;
        if (TestUtil.findAll(em, AILanguageDetection.class).isEmpty()) {
            aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
            languagePrediction = AILanguageDetectionResourceIT.createEntity();
        } else {
            languagePrediction = TestUtil.findAll(em, AILanguageDetection.class).get(0);
        }
        em.persist(languagePrediction);
        em.flush();
        aIAutoTagJob.setLanguagePrediction(languagePrediction);
        aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
        Long languagePredictionId = languagePrediction.getId();
        // Get all the aIAutoTagJobList where languagePrediction equals to languagePredictionId
        defaultAIAutoTagJobShouldBeFound("languagePredictionId.equals=" + languagePredictionId);

        // Get all the aIAutoTagJobList where languagePrediction equals to (languagePredictionId + 1)
        defaultAIAutoTagJobShouldNotBeFound("languagePredictionId.equals=" + (languagePredictionId + 1));
    }

    private void defaultAIAutoTagJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAIAutoTagJobShouldBeFound(shouldBeFound);
        defaultAIAutoTagJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAIAutoTagJobShouldBeFound(String filter) throws Exception {
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIAutoTagJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].extractedTextSha256").value(hasItem(DEFAULT_EXTRACTED_TEXT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAIAutoTagJobShouldNotBeFound(String filter) throws Exception {
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAIAutoTagJob() throws Exception {
        // Get the aIAutoTagJob
        restAIAutoTagJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAIAutoTagJob() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aIAutoTagJobSearchRepository.save(aIAutoTagJob);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());

        // Update the aIAutoTagJob
        AIAutoTagJob updatedAIAutoTagJob = aIAutoTagJobRepository.findById(aIAutoTagJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAIAutoTagJob are not directly saved in db
        em.detach(updatedAIAutoTagJob);
        updatedAIAutoTagJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .extractedTextSha256(UPDATED_EXTRACTED_TEXT_SHA_256)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(updatedAIAutoTagJob);

        restAIAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIAutoTagJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAutoTagJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAIAutoTagJobToMatchAllProperties(updatedAIAutoTagJob);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AIAutoTagJob> aIAutoTagJobSearchList = Streamable.of(aIAutoTagJobSearchRepository.findAll()).toList();
                AIAutoTagJob testAIAutoTagJobSearch = aIAutoTagJobSearchList.get(searchDatabaseSizeAfter - 1);

                assertAIAutoTagJobAllPropertiesEquals(testAIAutoTagJobSearch, updatedAIAutoTagJob);
            });
    }

    @Test
    @Transactional
    void putNonExistingAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aIAutoTagJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAutoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aIAutoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAIAutoTagJobWithPatch() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIAutoTagJob using partial update
        AIAutoTagJob partialUpdatedAIAutoTagJob = new AIAutoTagJob();
        partialUpdatedAIAutoTagJob.setId(aIAutoTagJob.getId());

        partialUpdatedAIAutoTagJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restAIAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIAutoTagJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIAutoTagJob))
            )
            .andExpect(status().isOk());

        // Validate the AIAutoTagJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIAutoTagJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAIAutoTagJob, aIAutoTagJob),
            getPersistedAIAutoTagJob(aIAutoTagJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateAIAutoTagJobWithPatch() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aIAutoTagJob using partial update
        AIAutoTagJob partialUpdatedAIAutoTagJob = new AIAutoTagJob();
        partialUpdatedAIAutoTagJob.setId(aIAutoTagJob.getId());

        partialUpdatedAIAutoTagJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .extractedTextSha256(UPDATED_EXTRACTED_TEXT_SHA_256)
            .status(UPDATED_STATUS)
            .modelVersion(UPDATED_MODEL_VERSION)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restAIAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAIAutoTagJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAIAutoTagJob))
            )
            .andExpect(status().isOk());

        // Validate the AIAutoTagJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAIAutoTagJobUpdatableFieldsEquals(partialUpdatedAIAutoTagJob, getPersistedAIAutoTagJob(partialUpdatedAIAutoTagJob));
    }

    @Test
    @Transactional
    void patchNonExistingAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aIAutoTagJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIAutoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aIAutoTagJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAIAutoTagJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        aIAutoTagJob.setId(longCount.incrementAndGet());

        // Create the AIAutoTagJob
        AIAutoTagJobDTO aIAutoTagJobDTO = aIAutoTagJobMapper.toDto(aIAutoTagJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAIAutoTagJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aIAutoTagJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AIAutoTagJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAIAutoTagJob() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
        aIAutoTagJobRepository.save(aIAutoTagJob);
        aIAutoTagJobSearchRepository.save(aIAutoTagJob);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aIAutoTagJob
        restAIAutoTagJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, aIAutoTagJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aIAutoTagJobSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAIAutoTagJob() throws Exception {
        // Initialize the database
        insertedAIAutoTagJob = aIAutoTagJobRepository.saveAndFlush(aIAutoTagJob);
        aIAutoTagJobSearchRepository.save(aIAutoTagJob);

        // Search the aIAutoTagJob
        restAIAutoTagJobMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aIAutoTagJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aIAutoTagJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT.toString())))
            .andExpect(jsonPath("$.[*].extractedTextSha256").value(hasItem(DEFAULT_EXTRACTED_TEXT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return aIAutoTagJobRepository.count();
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

    protected AIAutoTagJob getPersistedAIAutoTagJob(AIAutoTagJob aIAutoTagJob) {
        return aIAutoTagJobRepository.findById(aIAutoTagJob.getId()).orElseThrow();
    }

    protected void assertPersistedAIAutoTagJobToMatchAllProperties(AIAutoTagJob expectedAIAutoTagJob) {
        assertAIAutoTagJobAllPropertiesEquals(expectedAIAutoTagJob, getPersistedAIAutoTagJob(expectedAIAutoTagJob));
    }

    protected void assertPersistedAIAutoTagJobToMatchUpdatableProperties(AIAutoTagJob expectedAIAutoTagJob) {
        assertAIAutoTagJobAllUpdatablePropertiesEquals(expectedAIAutoTagJob, getPersistedAIAutoTagJob(expectedAIAutoTagJob));
    }
}
