package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AITagPredictionAsserts.*;
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
import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import fr.smartprod.paperdms.ai.repository.AITagPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AITagPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AITagPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AITagPredictionMapper;
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
 * Integration tests for the {@link AITagPredictionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AITagPredictionResourceIT {

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/ai-tag-predictions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-tag-predictions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AITagPredictionRepository aITagPredictionRepository;

    @Autowired
    private AITagPredictionMapper aITagPredictionMapper;

    @Autowired
    private AITagPredictionSearchRepository aITagPredictionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAITagPredictionMockMvc;

    private AITagPrediction aITagPrediction;

    private AITagPrediction insertedAITagPrediction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AITagPrediction createEntity() {
        return new AITagPrediction()
            .tagName(DEFAULT_TAG_NAME)
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
    public static AITagPrediction createUpdatedEntity() {
        return new AITagPrediction()
            .tagName(UPDATED_TAG_NAME)
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
        aITagPrediction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAITagPrediction != null) {
            aITagPredictionRepository.delete(insertedAITagPrediction);
            aITagPredictionSearchRepository.delete(insertedAITagPrediction);
            insertedAITagPrediction = null;
        }
    }

    @Test
    @Transactional
    void createAITagPrediction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);
        var returnedAITagPredictionDTO = om.readValue(
            restAITagPredictionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AITagPredictionDTO.class
        );

        // Validate the AITagPrediction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAITagPrediction = aITagPredictionMapper.toEntity(returnedAITagPredictionDTO);
        assertAITagPredictionUpdatableFieldsEquals(returnedAITagPrediction, getPersistedAITagPrediction(returnedAITagPrediction));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAITagPrediction = returnedAITagPrediction;
    }

    @Test
    @Transactional
    void createAITagPredictionWithExistingId() throws Exception {
        // Create the AITagPrediction with an existing ID
        aITagPrediction.setId(1L);
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAITagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTagNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        // set the field null
        aITagPrediction.setTagName(null);

        // Create the AITagPrediction, which fails.
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        restAITagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        // set the field null
        aITagPrediction.setConfidence(null);

        // Create the AITagPrediction, which fails.
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        restAITagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPredictionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        // set the field null
        aITagPrediction.setPredictionDate(null);

        // Create the AITagPrediction, which fails.
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        restAITagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAITagPredictions() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITagPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
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
    void getAITagPrediction() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get the aITagPrediction
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL_ID, aITagPrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aITagPrediction.getId().intValue()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME))
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
    void getAITagPredictionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        Long id = aITagPrediction.getId();

        defaultAITagPredictionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAITagPredictionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAITagPredictionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByTagNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where tagName equals to
        defaultAITagPredictionFiltering("tagName.equals=" + DEFAULT_TAG_NAME, "tagName.equals=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByTagNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where tagName in
        defaultAITagPredictionFiltering("tagName.in=" + DEFAULT_TAG_NAME + "," + UPDATED_TAG_NAME, "tagName.in=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByTagNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where tagName is not null
        defaultAITagPredictionFiltering("tagName.specified=true", "tagName.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByTagNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where tagName contains
        defaultAITagPredictionFiltering("tagName.contains=" + DEFAULT_TAG_NAME, "tagName.contains=" + UPDATED_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByTagNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where tagName does not contain
        defaultAITagPredictionFiltering("tagName.doesNotContain=" + UPDATED_TAG_NAME, "tagName.doesNotContain=" + DEFAULT_TAG_NAME);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence equals to
        defaultAITagPredictionFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence in
        defaultAITagPredictionFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence is not null
        defaultAITagPredictionFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence is greater than or equal to
        defaultAITagPredictionFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence is less than or equal to
        defaultAITagPredictionFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence is less than
        defaultAITagPredictionFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where confidence is greater than
        defaultAITagPredictionFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where reason equals to
        defaultAITagPredictionFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where reason in
        defaultAITagPredictionFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where reason is not null
        defaultAITagPredictionFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where reason contains
        defaultAITagPredictionFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where reason does not contain
        defaultAITagPredictionFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where modelVersion equals to
        defaultAITagPredictionFiltering("modelVersion.equals=" + DEFAULT_MODEL_VERSION, "modelVersion.equals=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where modelVersion in
        defaultAITagPredictionFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where modelVersion is not null
        defaultAITagPredictionFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where modelVersion contains
        defaultAITagPredictionFiltering("modelVersion.contains=" + DEFAULT_MODEL_VERSION, "modelVersion.contains=" + UPDATED_MODEL_VERSION);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where modelVersion does not contain
        defaultAITagPredictionFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionS3Key equals to
        defaultAITagPredictionFiltering(
            "predictionS3Key.equals=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.equals=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionS3Key in
        defaultAITagPredictionFiltering(
            "predictionS3Key.in=" + DEFAULT_PREDICTION_S_3_KEY + "," + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.in=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionS3Key is not null
        defaultAITagPredictionFiltering("predictionS3Key.specified=true", "predictionS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionS3Key contains
        defaultAITagPredictionFiltering(
            "predictionS3Key.contains=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.contains=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionS3Key does not contain
        defaultAITagPredictionFiltering(
            "predictionS3Key.doesNotContain=" + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.doesNotContain=" + DEFAULT_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByIsAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where isAccepted equals to
        defaultAITagPredictionFiltering("isAccepted.equals=" + DEFAULT_IS_ACCEPTED, "isAccepted.equals=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByIsAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where isAccepted in
        defaultAITagPredictionFiltering(
            "isAccepted.in=" + DEFAULT_IS_ACCEPTED + "," + UPDATED_IS_ACCEPTED,
            "isAccepted.in=" + UPDATED_IS_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByIsAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where isAccepted is not null
        defaultAITagPredictionFiltering("isAccepted.specified=true", "isAccepted.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedBy equals to
        defaultAITagPredictionFiltering("acceptedBy.equals=" + DEFAULT_ACCEPTED_BY, "acceptedBy.equals=" + UPDATED_ACCEPTED_BY);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedBy in
        defaultAITagPredictionFiltering(
            "acceptedBy.in=" + DEFAULT_ACCEPTED_BY + "," + UPDATED_ACCEPTED_BY,
            "acceptedBy.in=" + UPDATED_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedBy is not null
        defaultAITagPredictionFiltering("acceptedBy.specified=true", "acceptedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedBy contains
        defaultAITagPredictionFiltering("acceptedBy.contains=" + DEFAULT_ACCEPTED_BY, "acceptedBy.contains=" + UPDATED_ACCEPTED_BY);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedBy does not contain
        defaultAITagPredictionFiltering(
            "acceptedBy.doesNotContain=" + UPDATED_ACCEPTED_BY,
            "acceptedBy.doesNotContain=" + DEFAULT_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedDate equals to
        defaultAITagPredictionFiltering("acceptedDate.equals=" + DEFAULT_ACCEPTED_DATE, "acceptedDate.equals=" + UPDATED_ACCEPTED_DATE);
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedDate in
        defaultAITagPredictionFiltering(
            "acceptedDate.in=" + DEFAULT_ACCEPTED_DATE + "," + UPDATED_ACCEPTED_DATE,
            "acceptedDate.in=" + UPDATED_ACCEPTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByAcceptedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where acceptedDate is not null
        defaultAITagPredictionFiltering("acceptedDate.specified=true", "acceptedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionDate equals to
        defaultAITagPredictionFiltering(
            "predictionDate.equals=" + DEFAULT_PREDICTION_DATE,
            "predictionDate.equals=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionDate in
        defaultAITagPredictionFiltering(
            "predictionDate.in=" + DEFAULT_PREDICTION_DATE + "," + UPDATED_PREDICTION_DATE,
            "predictionDate.in=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByPredictionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        // Get all the aITagPredictionList where predictionDate is not null
        defaultAITagPredictionFiltering("predictionDate.specified=true", "predictionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAITagPredictionsByJobIsEqualToSomething() throws Exception {
        AIAutoTagJob job;
        if (TestUtil.findAll(em, AIAutoTagJob.class).isEmpty()) {
            aITagPredictionRepository.saveAndFlush(aITagPrediction);
            job = AIAutoTagJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, AIAutoTagJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        aITagPrediction.setJob(job);
        aITagPredictionRepository.saveAndFlush(aITagPrediction);
        Long jobId = job.getId();
        // Get all the aITagPredictionList where job equals to jobId
        defaultAITagPredictionShouldBeFound("jobId.equals=" + jobId);

        // Get all the aITagPredictionList where job equals to (jobId + 1)
        defaultAITagPredictionShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultAITagPredictionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAITagPredictionShouldBeFound(shouldBeFound);
        defaultAITagPredictionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAITagPredictionShouldBeFound(String filter) throws Exception {
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITagPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));

        // Check, that the count call also returns 1
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAITagPredictionShouldNotBeFound(String filter) throws Exception {
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAITagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAITagPrediction() throws Exception {
        // Get the aITagPrediction
        restAITagPredictionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAITagPrediction() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aITagPredictionSearchRepository.save(aITagPrediction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());

        // Update the aITagPrediction
        AITagPrediction updatedAITagPrediction = aITagPredictionRepository.findById(aITagPrediction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAITagPrediction are not directly saved in db
        em.detach(updatedAITagPrediction);
        updatedAITagPrediction
            .tagName(UPDATED_TAG_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(updatedAITagPrediction);

        restAITagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aITagPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITagPredictionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAITagPredictionToMatchAllProperties(updatedAITagPrediction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AITagPrediction> aITagPredictionSearchList = Streamable.of(aITagPredictionSearchRepository.findAll()).toList();
                AITagPrediction testAITagPredictionSearch = aITagPredictionSearchList.get(searchDatabaseSizeAfter - 1);

                assertAITagPredictionAllPropertiesEquals(testAITagPredictionSearch, updatedAITagPrediction);
            });
    }

    @Test
    @Transactional
    void putNonExistingAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aITagPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aITagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAITagPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aITagPrediction using partial update
        AITagPrediction partialUpdatedAITagPrediction = new AITagPrediction();
        partialUpdatedAITagPrediction.setId(aITagPrediction.getId());

        partialUpdatedAITagPrediction
            .tagName(UPDATED_TAG_NAME)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restAITagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAITagPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAITagPrediction))
            )
            .andExpect(status().isOk());

        // Validate the AITagPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAITagPredictionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAITagPrediction, aITagPrediction),
            getPersistedAITagPrediction(aITagPrediction)
        );
    }

    @Test
    @Transactional
    void fullUpdateAITagPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aITagPrediction using partial update
        AITagPrediction partialUpdatedAITagPrediction = new AITagPrediction();
        partialUpdatedAITagPrediction.setId(aITagPrediction.getId());

        partialUpdatedAITagPrediction
            .tagName(UPDATED_TAG_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restAITagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAITagPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAITagPrediction))
            )
            .andExpect(status().isOk());

        // Validate the AITagPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAITagPredictionUpdatableFieldsEquals(
            partialUpdatedAITagPrediction,
            getPersistedAITagPrediction(partialUpdatedAITagPrediction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aITagPredictionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aITagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aITagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAITagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        aITagPrediction.setId(longCount.incrementAndGet());

        // Create the AITagPrediction
        AITagPredictionDTO aITagPredictionDTO = aITagPredictionMapper.toDto(aITagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAITagPredictionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aITagPredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AITagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAITagPrediction() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);
        aITagPredictionRepository.save(aITagPrediction);
        aITagPredictionSearchRepository.save(aITagPrediction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aITagPrediction
        restAITagPredictionMockMvc
            .perform(delete(ENTITY_API_URL_ID, aITagPrediction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aITagPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAITagPrediction() throws Exception {
        // Initialize the database
        insertedAITagPrediction = aITagPredictionRepository.saveAndFlush(aITagPrediction);
        aITagPredictionSearchRepository.save(aITagPrediction);

        // Search the aITagPrediction
        restAITagPredictionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aITagPrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aITagPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
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
        return aITagPredictionRepository.count();
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

    protected AITagPrediction getPersistedAITagPrediction(AITagPrediction aITagPrediction) {
        return aITagPredictionRepository.findById(aITagPrediction.getId()).orElseThrow();
    }

    protected void assertPersistedAITagPredictionToMatchAllProperties(AITagPrediction expectedAITagPrediction) {
        assertAITagPredictionAllPropertiesEquals(expectedAITagPrediction, getPersistedAITagPrediction(expectedAITagPrediction));
    }

    protected void assertPersistedAITagPredictionToMatchUpdatableProperties(AITagPrediction expectedAITagPrediction) {
        assertAITagPredictionAllUpdatablePropertiesEquals(expectedAITagPrediction, getPersistedAITagPrediction(expectedAITagPrediction));
    }
}
