package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AITypePredictionAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.repository.AITypePredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITypePredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AITypePredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITypePredictionMapper;
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
 * Integration tests for the {@link AITypePredictionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AITypePredictionResourceIT {

    private static final String DEFAULT_DOCUMENT_TYPE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_TYPE_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PREDICTION_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_S_3_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;

    private static final String DEFAULT_ACCEPTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACCEPTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACCEPTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PREDICTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PREDICTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ai-type-predictions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-type-predictions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AITypePredictionRepository aITypePredictionRepository;

    @Autowired
    private AITypePredictionMapper aITypePredictionMapper;

    @Autowired
    private AITypePredictionSearchRepository aITypePredictionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAITypePredictionMockMvc;

    private AITypePrediction aITypePrediction;

    private AITypePrediction insertedAITypePrediction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AITypePrediction createEntity() {
        return new AITypePrediction()
            .documentTypeName(DEFAULT_DOCUMENT_TYPE_NAME)
            .confidence(DEFAULT_CONFIDENCE)
            .reason(DEFAULT_REASON)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .predictionS3Key(DEFAULT_PREDICTION_S_3_KEY)
            .isAccepted(DEFAULT_IS_ACCEPTED)
            .acceptedBy(DEFAULT_ACCEPTED_BY)
            .acceptedDate(DEFAULT_ACCEPTED_DATE)
            .predictionDate(DEFAULT_PREDICTION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AITypePrediction createUpdatedEntity() {
        return new AITypePrediction()
            .documentTypeName(UPDATED_DOCUMENT_TYPE_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
    }

    @BeforeEach
    void initTest() {
        aITypePrediction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAITypePrediction != null) {
            aITypePredictionRepository.delete(insertedAITypePrediction);
            aITypePredictionSearchRepository.delete(insertedAITypePrediction);
            insertedAITypePrediction = null;
        }
    }

    @Test
    @Transactional
    void createAITypePrediction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);
        var returnedAITypePredictionDTO = om.readValue(
            restAITypePredictionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AITypePredictionDTO.class
        );

        // Validate the AITypePrediction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAITypePrediction = aITypePredictionMapper.toEntity(returnedAITypePredictionDTO);
        assertAITypePredictionUpdatableFieldsEquals(returnedAITypePrediction, getPersistedAITypePrediction(returnedAITypePrediction));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAITypePrediction = returnedAITypePrediction;
    }

    @Test
    @Transactional
    void createAITypePredictionWithExistingId() throws Exception {
        // Create the AITypePrediction with an existing ID
        aITypePrediction.setId(1L);
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAITypePredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentTypeNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        // set the field null
        aITypePrediction.setDocumentTypeName(null);

        // Create the AITypePrediction, which fails.
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        restAITypePredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        // set the field null
        aITypePrediction.setConfidence(null);

        // Create the AITypePrediction, which fails.
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        restAITypePredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPredictionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        // set the field null
        aITypePrediction.setPredictionDate(null);

        // Create the AITypePrediction, which fails.
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        restAITypePredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAITypePredictions() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITypePrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTypeName").value(hasItem(DEFAULT_DOCUMENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getAITypePrediction() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get the aITypePrediction
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL_ID, aITypePrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aITypePrediction.getId().intValue()))
            .andExpect(jsonPath("$.documentTypeName").value(DEFAULT_DOCUMENT_TYPE_NAME))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION))
            .andExpect(jsonPath("$.predictionS3Key").value(DEFAULT_PREDICTION_S_3_KEY))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED))
            .andExpect(jsonPath("$.acceptedBy").value(DEFAULT_ACCEPTED_BY))
            .andExpect(jsonPath("$.acceptedDate").value(DEFAULT_ACCEPTED_DATE.toString()))
            .andExpect(jsonPath("$.predictionDate").value(DEFAULT_PREDICTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getAITypePredictionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        Long id = aITypePrediction.getId();

        defaultAITypePredictionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAITypePredictionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAITypePredictionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByDocumentTypeNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where documentTypeName equals to
        defaultAITypePredictionFiltering(
            "documentTypeName.equals=" + DEFAULT_DOCUMENT_TYPE_NAME,
            "documentTypeName.equals=" + UPDATED_DOCUMENT_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByDocumentTypeNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where documentTypeName in
        defaultAITypePredictionFiltering(
            "documentTypeName.in=" + DEFAULT_DOCUMENT_TYPE_NAME + "," + UPDATED_DOCUMENT_TYPE_NAME,
            "documentTypeName.in=" + UPDATED_DOCUMENT_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByDocumentTypeNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where documentTypeName is not null
        defaultAITypePredictionFiltering("documentTypeName.specified=true", "documentTypeName.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByDocumentTypeNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where documentTypeName contains
        defaultAITypePredictionFiltering(
            "documentTypeName.contains=" + DEFAULT_DOCUMENT_TYPE_NAME,
            "documentTypeName.contains=" + UPDATED_DOCUMENT_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByDocumentTypeNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where documentTypeName does not contain
        defaultAITypePredictionFiltering(
            "documentTypeName.doesNotContain=" + UPDATED_DOCUMENT_TYPE_NAME,
            "documentTypeName.doesNotContain=" + DEFAULT_DOCUMENT_TYPE_NAME
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence equals to
        defaultAITypePredictionFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence in
        defaultAITypePredictionFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence is not null
        defaultAITypePredictionFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence is greater than or equal to
        defaultAITypePredictionFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence is less than or equal to
        defaultAITypePredictionFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence is less than
        defaultAITypePredictionFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where confidence is greater than
        defaultAITypePredictionFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where reason equals to
        defaultAITypePredictionFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where reason in
        defaultAITypePredictionFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where reason is not null
        defaultAITypePredictionFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where reason contains
        defaultAITypePredictionFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where reason does not contain
        defaultAITypePredictionFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where modelVersion equals to
        defaultAITypePredictionFiltering("modelVersion.equals=" + DEFAULT_MODEL_VERSION, "modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where modelVersion in
        defaultAITypePredictionFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where modelVersion is not null
        defaultAITypePredictionFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where modelVersion contains
        defaultAITypePredictionFiltering(
            "modelVersion.contains=" + DEFAULT_MODEL_VERSION,
            "modelVersion.contains=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where modelVersion does not contain
        defaultAITypePredictionFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionS3Key equals to
        defaultAITypePredictionFiltering(
            "predictionS3Key.equals=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.equals=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionS3Key in
        defaultAITypePredictionFiltering(
            "predictionS3Key.in=" + DEFAULT_PREDICTION_S_3_KEY + "," + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.in=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionS3Key is not null
        defaultAITypePredictionFiltering("predictionS3Key.specified=true", "predictionS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionS3Key contains
        defaultAITypePredictionFiltering(
            "predictionS3Key.contains=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.contains=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionS3Key does not contain
        defaultAITypePredictionFiltering(
            "predictionS3Key.doesNotContain=" + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.doesNotContain=" + DEFAULT_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByIsAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where isAccepted equals to
        defaultAITypePredictionFiltering("isAccepted.equals=" + DEFAULT_IS_ACCEPTED, "isAccepted.equals=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByIsAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where isAccepted in
        defaultAITypePredictionFiltering(
            "isAccepted.in=" + DEFAULT_IS_ACCEPTED + "," + UPDATED_IS_ACCEPTED,
            "isAccepted.in=" + UPDATED_IS_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByIsAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where isAccepted is not null
        defaultAITypePredictionFiltering("isAccepted.specified=true", "isAccepted.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedBy equals to
        defaultAITypePredictionFiltering("acceptedBy.equals=" + DEFAULT_ACCEPTED_BY, "acceptedBy.equals=" + UPDATED_ACCEPTED_BY);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedBy in
        defaultAITypePredictionFiltering(
            "acceptedBy.in=" + DEFAULT_ACCEPTED_BY + "," + UPDATED_ACCEPTED_BY,
            "acceptedBy.in=" + UPDATED_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedBy is not null
        defaultAITypePredictionFiltering("acceptedBy.specified=true", "acceptedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedBy contains
        defaultAITypePredictionFiltering("acceptedBy.contains=" + DEFAULT_ACCEPTED_BY, "acceptedBy.contains=" + UPDATED_ACCEPTED_BY);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedBy does not contain
        defaultAITypePredictionFiltering(
            "acceptedBy.doesNotContain=" + UPDATED_ACCEPTED_BY,
            "acceptedBy.doesNotContain=" + DEFAULT_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedDate equals to
        defaultAITypePredictionFiltering("acceptedDate.equals=" + DEFAULT_ACCEPTED_DATE, "acceptedDate.equals=" + UPDATED_ACCEPTED_DATE);
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedDate in
        defaultAITypePredictionFiltering(
            "acceptedDate.in=" + DEFAULT_ACCEPTED_DATE + "," + UPDATED_ACCEPTED_DATE,
            "acceptedDate.in=" + UPDATED_ACCEPTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByAcceptedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where acceptedDate is not null
        defaultAITypePredictionFiltering("acceptedDate.specified=true", "acceptedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionDate equals to
        defaultAITypePredictionFiltering(
            "predictionDate.equals=" + DEFAULT_PREDICTION_DATE,
            "predictionDate.equals=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionDate in
        defaultAITypePredictionFiltering(
            "predictionDate.in=" + DEFAULT_PREDICTION_DATE + "," + UPDATED_PREDICTION_DATE,
            "predictionDate.in=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITypePredictionsByPredictionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        // Get all the aITypePredictionList where predictionDate is not null
        defaultAITypePredictionFiltering("predictionDate.specified=true", "predictionDate.specified=false");
    }

    private void defaultAITypePredictionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAITypePredictionShouldBeFound(shouldBeFound);
        defaultAITypePredictionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAITypePredictionShouldBeFound(String filter) throws Exception {
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITypePrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTypeName").value(hasItem(DEFAULT_DOCUMENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));

        // Check, that the count call also returns 1
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAITypePredictionShouldNotBeFound(String filter) throws Exception {
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAITypePredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAITypePrediction() throws Exception {
        // Get the aITypePrediction
        restAITypePredictionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAITypePrediction() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aITypePredictionSearchRepository.save(aITypePrediction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());

        // Update the aITypePrediction
        AITypePrediction updatedAITypePrediction = aITypePredictionRepository.findById(aITypePrediction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAITypePrediction are not directly saved in db
        em.detach(updatedAITypePrediction);
        updatedAITypePrediction
            .documentTypeName(UPDATED_DOCUMENT_TYPE_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(updatedAITypePrediction);

        restAITypePredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aITypePredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITypePredictionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAITypePredictionToMatchAllProperties(updatedAITypePrediction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AITypePrediction> aITypePredictionSearchList = Streamable.of(aITypePredictionSearchRepository.findAll()).toList();
                AITypePrediction testAITypePredictionSearch = aITypePredictionSearchList.get(searchDatabaseSizeAfter - 1);

                assertAITypePredictionAllPropertiesEquals(testAITypePredictionSearch, updatedAITypePrediction);
            });
    }

    @Test
    @Transactional
    void putNonExistingAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aITypePredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITypePredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITypePredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAITypePredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aITypePrediction using partial update
        AITypePrediction partialUpdatedAITypePrediction = new AITypePrediction();
        partialUpdatedAITypePrediction.setId(aITypePrediction.getId());

        partialUpdatedAITypePrediction
            .documentTypeName(UPDATED_DOCUMENT_TYPE_NAME)
            .modelVersion(UPDATED_MODEL_VERSION)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY);

        restAITypePredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAITypePrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAITypePrediction))
            )
            .andExpect(status().isOk());

        // Validate the AITypePrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAITypePredictionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAITypePrediction, aITypePrediction),
            getPersistedAITypePrediction(aITypePrediction)
        );
    }

    @Test
    @Transactional
    void fullUpdateAITypePredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aITypePrediction using partial update
        AITypePrediction partialUpdatedAITypePrediction = new AITypePrediction();
        partialUpdatedAITypePrediction.setId(aITypePrediction.getId());

        partialUpdatedAITypePrediction
            .documentTypeName(UPDATED_DOCUMENT_TYPE_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restAITypePredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAITypePrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAITypePrediction))
            )
            .andExpect(status().isOk());

        // Validate the AITypePrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAITypePredictionUpdatableFieldsEquals(
            partialUpdatedAITypePrediction,
            getPersistedAITypePrediction(partialUpdatedAITypePrediction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aITypePredictionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aITypePredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aITypePredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAITypePrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        aITypePrediction.setId(longCount.incrementAndGet());

        // Create the AITypePrediction
        AITypePredictionDTO aITypePredictionDTO = aITypePredictionMapper.toDto(aITypePrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITypePredictionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aITypePredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AITypePrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAITypePrediction() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);
        aITypePredictionRepository.save(aITypePrediction);
        aITypePredictionSearchRepository.save(aITypePrediction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aITypePrediction
        restAITypePredictionMockMvc
            .perform(delete(ENTITY_API_URL_ID, aITypePrediction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITypePredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAITypePrediction() throws Exception {
        // Initialize the database
        insertedAITypePrediction = aITypePredictionRepository.saveAndFlush(aITypePrediction);
        aITypePredictionSearchRepository.save(aITypePrediction);

        // Search the aITypePrediction
        restAITypePredictionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aITypePrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITypePrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentTypeName").value(hasItem(DEFAULT_DOCUMENT_TYPE_NAME)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return aITypePredictionRepository.count();
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

    protected AITypePrediction getPersistedAITypePrediction(AITypePrediction aITypePrediction) {
        return aITypePredictionRepository.findById(aITypePrediction.getId()).orElseThrow();
    }

    protected void assertPersistedAITypePredictionToMatchAllProperties(AITypePrediction expectedAITypePrediction) {
        assertAITypePredictionAllPropertiesEquals(expectedAITypePrediction, getPersistedAITypePrediction(expectedAITypePrediction));
    }

    protected void assertPersistedAITypePredictionToMatchUpdatableProperties(AITypePrediction expectedAITypePrediction) {
        assertAITypePredictionAllUpdatablePropertiesEquals(
            expectedAITypePrediction,
            getPersistedAITypePrediction(expectedAITypePrediction)
        );
    }
}
