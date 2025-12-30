package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AILanguageDetectionAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.domain.enumeration.AILanguageDetectionMethod;
import fr.smartprod.paperdms.ai.repository.AILanguageDetectionRepository;
import fr.smartprod.paperdms.ai.repository.search.AILanguageDetectionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AILanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AILanguageDetectionMapper;
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
 * Integration tests for the {@link AILanguageDetectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AILanguageDetectionResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_DETECTED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_DETECTED_LANGUAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final AILanguageDetectionMethod DEFAULT_DETECTION_METHOD = AILanguageDetectionMethod.TIKA;
    private static final AILanguageDetectionMethod UPDATED_DETECTION_METHOD = AILanguageDetectionMethod.LANGDETECT;

    private static final String DEFAULT_ALTERNATIVE_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVE_LANGUAGES = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_SAMPLE = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_SAMPLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_CACHE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_CACHE_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CACHED = false;
    private static final Boolean UPDATED_IS_CACHED = true;

    private static final Instant DEFAULT_DETECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DETECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ai-language-detections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-language-detections/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AILanguageDetectionRepository aILanguageDetectionRepository;

    @Autowired
    private AILanguageDetectionMapper aILanguageDetectionMapper;

    @Autowired
    private AILanguageDetectionSearchRepository aILanguageDetectionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAILanguageDetectionMockMvc;

    private AILanguageDetection aILanguageDetection;

    private AILanguageDetection insertedAILanguageDetection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AILanguageDetection createEntity() {
        return new AILanguageDetection()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .detectedLanguage(DEFAULT_DETECTED_LANGUAGE)
            .confidence(DEFAULT_CONFIDENCE)
            .detectionMethod(DEFAULT_DETECTION_METHOD)
            .alternativeLanguages(DEFAULT_ALTERNATIVE_LANGUAGES)
            .textSample(DEFAULT_TEXT_SAMPLE)
            .resultCacheKey(DEFAULT_RESULT_CACHE_KEY)
            .isCached(DEFAULT_IS_CACHED)
            .detectedDate(DEFAULT_DETECTED_DATE)
            .modelVersion(DEFAULT_MODEL_VERSION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AILanguageDetection createUpdatedEntity() {
        return new AILanguageDetection()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);
    }

    @BeforeEach
    void initTest() {
        aILanguageDetection = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAILanguageDetection != null) {
            aILanguageDetectionRepository.delete(insertedAILanguageDetection);
            aILanguageDetectionSearchRepository.delete(insertedAILanguageDetection);
            insertedAILanguageDetection = null;
        }
    }

    @Test
    @Transactional
    void createAILanguageDetection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);
        var returnedAILanguageDetectionDTO = om.readValue(
            restAILanguageDetectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AILanguageDetectionDTO.class
        );

        // Validate the AILanguageDetection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAILanguageDetection = aILanguageDetectionMapper.toEntity(returnedAILanguageDetectionDTO);
        assertAILanguageDetectionUpdatableFieldsEquals(
            returnedAILanguageDetection,
            getPersistedAILanguageDetection(returnedAILanguageDetection)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAILanguageDetection = returnedAILanguageDetection;
    }

    @Test
    @Transactional
    void createAILanguageDetectionWithExistingId() throws Exception {
        // Create the AILanguageDetection with an existing ID
        aILanguageDetection.setId(1L);
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // set the field null
        aILanguageDetection.setDocumentSha256(null);

        // Create the AILanguageDetection, which fails.
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDetectedLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // set the field null
        aILanguageDetection.setDetectedLanguage(null);

        // Create the AILanguageDetection, which fails.
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // set the field null
        aILanguageDetection.setConfidence(null);

        // Create the AILanguageDetection, which fails.
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsCachedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // set the field null
        aILanguageDetection.setIsCached(null);

        // Create the AILanguageDetection, which fails.
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDetectedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        // set the field null
        aILanguageDetection.setDetectedDate(null);

        // Create the AILanguageDetection, which fails.
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAILanguageDetections() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aILanguageDetection.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].detectionMethod").value(hasItem(DEFAULT_DETECTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].alternativeLanguages").value(hasItem(DEFAULT_ALTERNATIVE_LANGUAGES)))
            .andExpect(jsonPath("$.[*].textSample").value(hasItem(DEFAULT_TEXT_SAMPLE)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].detectedDate").value(hasItem(DEFAULT_DETECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)));
    }

    @Test
    @Transactional
    void getAILanguageDetection() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get the aILanguageDetection
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL_ID, aILanguageDetection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aILanguageDetection.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.detectedLanguage").value(DEFAULT_DETECTED_LANGUAGE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.detectionMethod").value(DEFAULT_DETECTION_METHOD.toString()))
            .andExpect(jsonPath("$.alternativeLanguages").value(DEFAULT_ALTERNATIVE_LANGUAGES))
            .andExpect(jsonPath("$.textSample").value(DEFAULT_TEXT_SAMPLE))
            .andExpect(jsonPath("$.resultCacheKey").value(DEFAULT_RESULT_CACHE_KEY))
            .andExpect(jsonPath("$.isCached").value(DEFAULT_IS_CACHED))
            .andExpect(jsonPath("$.detectedDate").value(DEFAULT_DETECTED_DATE.toString()))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION));
    }

    @Test
    @Transactional
    void getAILanguageDetectionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        Long id = aILanguageDetection.getId();

        defaultAILanguageDetectionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAILanguageDetectionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAILanguageDetectionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where documentSha256 equals to
        defaultAILanguageDetectionFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where documentSha256 in
        defaultAILanguageDetectionFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where documentSha256 is not null
        defaultAILanguageDetectionFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where documentSha256 contains
        defaultAILanguageDetectionFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where documentSha256 does not contain
        defaultAILanguageDetectionFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedLanguage equals to
        defaultAILanguageDetectionFiltering(
            "detectedLanguage.equals=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.equals=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedLanguage in
        defaultAILanguageDetectionFiltering(
            "detectedLanguage.in=" + DEFAULT_DETECTED_LANGUAGE + "," + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.in=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedLanguage is not null
        defaultAILanguageDetectionFiltering("detectedLanguage.specified=true", "detectedLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedLanguage contains
        defaultAILanguageDetectionFiltering(
            "detectedLanguage.contains=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.contains=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedLanguage does not contain
        defaultAILanguageDetectionFiltering(
            "detectedLanguage.doesNotContain=" + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.doesNotContain=" + DEFAULT_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence equals to
        defaultAILanguageDetectionFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence in
        defaultAILanguageDetectionFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence is not null
        defaultAILanguageDetectionFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence is greater than or equal to
        defaultAILanguageDetectionFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence is less than or equal to
        defaultAILanguageDetectionFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence is less than
        defaultAILanguageDetectionFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where confidence is greater than
        defaultAILanguageDetectionFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectionMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectionMethod equals to
        defaultAILanguageDetectionFiltering(
            "detectionMethod.equals=" + DEFAULT_DETECTION_METHOD,
            "detectionMethod.equals=" + UPDATED_DETECTION_METHOD
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectionMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectionMethod in
        defaultAILanguageDetectionFiltering(
            "detectionMethod.in=" + DEFAULT_DETECTION_METHOD + "," + UPDATED_DETECTION_METHOD,
            "detectionMethod.in=" + UPDATED_DETECTION_METHOD
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectionMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectionMethod is not null
        defaultAILanguageDetectionFiltering("detectionMethod.specified=true", "detectionMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByResultCacheKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where resultCacheKey equals to
        defaultAILanguageDetectionFiltering(
            "resultCacheKey.equals=" + DEFAULT_RESULT_CACHE_KEY,
            "resultCacheKey.equals=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByResultCacheKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where resultCacheKey in
        defaultAILanguageDetectionFiltering(
            "resultCacheKey.in=" + DEFAULT_RESULT_CACHE_KEY + "," + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.in=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByResultCacheKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where resultCacheKey is not null
        defaultAILanguageDetectionFiltering("resultCacheKey.specified=true", "resultCacheKey.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByResultCacheKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where resultCacheKey contains
        defaultAILanguageDetectionFiltering(
            "resultCacheKey.contains=" + DEFAULT_RESULT_CACHE_KEY,
            "resultCacheKey.contains=" + UPDATED_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByResultCacheKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where resultCacheKey does not contain
        defaultAILanguageDetectionFiltering(
            "resultCacheKey.doesNotContain=" + UPDATED_RESULT_CACHE_KEY,
            "resultCacheKey.doesNotContain=" + DEFAULT_RESULT_CACHE_KEY
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByIsCachedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where isCached equals to
        defaultAILanguageDetectionFiltering("isCached.equals=" + DEFAULT_IS_CACHED, "isCached.equals=" + UPDATED_IS_CACHED);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByIsCachedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where isCached in
        defaultAILanguageDetectionFiltering(
            "isCached.in=" + DEFAULT_IS_CACHED + "," + UPDATED_IS_CACHED,
            "isCached.in=" + UPDATED_IS_CACHED
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByIsCachedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where isCached is not null
        defaultAILanguageDetectionFiltering("isCached.specified=true", "isCached.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedDate equals to
        defaultAILanguageDetectionFiltering("detectedDate.equals=" + DEFAULT_DETECTED_DATE, "detectedDate.equals=" + UPDATED_DETECTED_DATE);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedDate in
        defaultAILanguageDetectionFiltering(
            "detectedDate.in=" + DEFAULT_DETECTED_DATE + "," + UPDATED_DETECTED_DATE,
            "detectedDate.in=" + UPDATED_DETECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByDetectedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where detectedDate is not null
        defaultAILanguageDetectionFiltering("detectedDate.specified=true", "detectedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where modelVersion equals to
        defaultAILanguageDetectionFiltering("modelVersion.equals=" + DEFAULT_MODEL_VERSION, "modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where modelVersion in
        defaultAILanguageDetectionFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where modelVersion is not null
        defaultAILanguageDetectionFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where modelVersion contains
        defaultAILanguageDetectionFiltering(
            "modelVersion.contains=" + DEFAULT_MODEL_VERSION,
            "modelVersion.contains=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAILanguageDetectionsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        // Get all the aILanguageDetectionList where modelVersion does not contain
        defaultAILanguageDetectionFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    private void defaultAILanguageDetectionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAILanguageDetectionShouldBeFound(shouldBeFound);
        defaultAILanguageDetectionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAILanguageDetectionShouldBeFound(String filter) throws Exception {
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aILanguageDetection.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].detectionMethod").value(hasItem(DEFAULT_DETECTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].alternativeLanguages").value(hasItem(DEFAULT_ALTERNATIVE_LANGUAGES)))
            .andExpect(jsonPath("$.[*].textSample").value(hasItem(DEFAULT_TEXT_SAMPLE)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].detectedDate").value(hasItem(DEFAULT_DETECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)));

        // Check, that the count call also returns 1
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAILanguageDetectionShouldNotBeFound(String filter) throws Exception {
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAILanguageDetection() throws Exception {
        // Get the aILanguageDetection
        restAILanguageDetectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAILanguageDetection() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aILanguageDetectionSearchRepository.save(aILanguageDetection);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());

        // Update the aILanguageDetection
        AILanguageDetection updatedAILanguageDetection = aILanguageDetectionRepository.findById(aILanguageDetection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAILanguageDetection are not directly saved in db
        em.detach(updatedAILanguageDetection);
        updatedAILanguageDetection
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(updatedAILanguageDetection);

        restAILanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aILanguageDetectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAILanguageDetectionToMatchAllProperties(updatedAILanguageDetection);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AILanguageDetection> aILanguageDetectionSearchList = Streamable.of(
                    aILanguageDetectionSearchRepository.findAll()
                ).toList();
                AILanguageDetection testAILanguageDetectionSearch = aILanguageDetectionSearchList.get(searchDatabaseSizeAfter - 1);

                assertAILanguageDetectionAllPropertiesEquals(testAILanguageDetectionSearch, updatedAILanguageDetection);
            });
    }

    @Test
    @Transactional
    void putNonExistingAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aILanguageDetectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aILanguageDetectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAILanguageDetectionWithPatch() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aILanguageDetection using partial update
        AILanguageDetection partialUpdatedAILanguageDetection = new AILanguageDetection();
        partialUpdatedAILanguageDetection.setId(aILanguageDetection.getId());

        partialUpdatedAILanguageDetection
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .isCached(UPDATED_IS_CACHED);

        restAILanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAILanguageDetection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAILanguageDetection))
            )
            .andExpect(status().isOk());

        // Validate the AILanguageDetection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAILanguageDetectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAILanguageDetection, aILanguageDetection),
            getPersistedAILanguageDetection(aILanguageDetection)
        );
    }

    @Test
    @Transactional
    void fullUpdateAILanguageDetectionWithPatch() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aILanguageDetection using partial update
        AILanguageDetection partialUpdatedAILanguageDetection = new AILanguageDetection();
        partialUpdatedAILanguageDetection.setId(aILanguageDetection.getId());

        partialUpdatedAILanguageDetection
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);

        restAILanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAILanguageDetection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAILanguageDetection))
            )
            .andExpect(status().isOk());

        // Validate the AILanguageDetection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAILanguageDetectionUpdatableFieldsEquals(
            partialUpdatedAILanguageDetection,
            getPersistedAILanguageDetection(partialUpdatedAILanguageDetection)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aILanguageDetectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAILanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        aILanguageDetection.setId(longCount.incrementAndGet());

        // Create the AILanguageDetection
        AILanguageDetectionDTO aILanguageDetectionDTO = aILanguageDetectionMapper.toDto(aILanguageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAILanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aILanguageDetectionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AILanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAILanguageDetection() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);
        aILanguageDetectionRepository.save(aILanguageDetection);
        aILanguageDetectionSearchRepository.save(aILanguageDetection);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aILanguageDetection
        restAILanguageDetectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, aILanguageDetection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aILanguageDetectionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAILanguageDetection() throws Exception {
        // Initialize the database
        insertedAILanguageDetection = aILanguageDetectionRepository.saveAndFlush(aILanguageDetection);
        aILanguageDetectionSearchRepository.save(aILanguageDetection);

        // Search the aILanguageDetection
        restAILanguageDetectionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aILanguageDetection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aILanguageDetection.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].detectionMethod").value(hasItem(DEFAULT_DETECTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].alternativeLanguages").value(hasItem(DEFAULT_ALTERNATIVE_LANGUAGES.toString())))
            .andExpect(jsonPath("$.[*].textSample").value(hasItem(DEFAULT_TEXT_SAMPLE.toString())))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].detectedDate").value(hasItem(DEFAULT_DETECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)));
    }

    protected long getRepositoryCount() {
        return aILanguageDetectionRepository.count();
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

    protected AILanguageDetection getPersistedAILanguageDetection(AILanguageDetection aILanguageDetection) {
        return aILanguageDetectionRepository.findById(aILanguageDetection.getId()).orElseThrow();
    }

    protected void assertPersistedAILanguageDetectionToMatchAllProperties(AILanguageDetection expectedAILanguageDetection) {
        assertAILanguageDetectionAllPropertiesEquals(
            expectedAILanguageDetection,
            getPersistedAILanguageDetection(expectedAILanguageDetection)
        );
    }

    protected void assertPersistedAILanguageDetectionToMatchUpdatableProperties(AILanguageDetection expectedAILanguageDetection) {
        assertAILanguageDetectionAllUpdatablePropertiesEquals(
            expectedAILanguageDetection,
            getPersistedAILanguageDetection(expectedAILanguageDetection)
        );
    }
}
