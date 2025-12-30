package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AICorrespondentPredictionAsserts.*;
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
import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentRole;
import fr.smartprod.paperdms.ai.domain.enumeration.CorrespondentType;
import fr.smartprod.paperdms.ai.repository.AICorrespondentPredictionRepository;
import fr.smartprod.paperdms.ai.repository.search.AICorrespondentPredictionSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AICorrespondentPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICorrespondentPredictionMapper;
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
 * Integration tests for the {@link AICorrespondentPredictionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AICorrespondentPredictionResourceIT {

    private static final String DEFAULT_CORRESPONDENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CORRESPONDENT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_COMPANY = "AAAAAAAAAA";
    private static final String UPDATED_COMPANY = "BBBBBBBBBB";

    private static final CorrespondentType DEFAULT_TYPE = CorrespondentType.PERSON;
    private static final CorrespondentType UPDATED_TYPE = CorrespondentType.ORGANIZATION;

    private static final CorrespondentRole DEFAULT_ROLE = CorrespondentRole.SENDER;
    private static final CorrespondentRole UPDATED_ROLE = CorrespondentRole.RECIPIENT;

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

    private static final String ENTITY_API_URL = "/api/ai-correspondent-predictions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-correspondent-predictions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AICorrespondentPredictionRepository aICorrespondentPredictionRepository;

    @Autowired
    private AICorrespondentPredictionMapper aICorrespondentPredictionMapper;

    @Autowired
    private AICorrespondentPredictionSearchRepository aICorrespondentPredictionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAICorrespondentPredictionMockMvc;

    private AICorrespondentPrediction aICorrespondentPrediction;

    private AICorrespondentPrediction insertedAICorrespondentPrediction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AICorrespondentPrediction createEntity() {
        return new AICorrespondentPrediction()
            .correspondentName(DEFAULT_CORRESPONDENT_NAME)
            .name(DEFAULT_NAME)
            .email(DEFAULT_EMAIL)
            .phone(DEFAULT_PHONE)
            .address(DEFAULT_ADDRESS)
            .company(DEFAULT_COMPANY)
            .type(DEFAULT_TYPE)
            .role(DEFAULT_ROLE)
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
    public static AICorrespondentPrediction createUpdatedEntity() {
        return new AICorrespondentPrediction()
            .correspondentName(UPDATED_CORRESPONDENT_NAME)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
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
        aICorrespondentPrediction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAICorrespondentPrediction != null) {
            aICorrespondentPredictionRepository.delete(insertedAICorrespondentPrediction);
            aICorrespondentPredictionSearchRepository.delete(insertedAICorrespondentPrediction);
            insertedAICorrespondentPrediction = null;
        }
    }

    @Test
    @Transactional
    void createAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);
        var returnedAICorrespondentPredictionDTO = om.readValue(
            restAICorrespondentPredictionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AICorrespondentPredictionDTO.class
        );

        // Validate the AICorrespondentPrediction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAICorrespondentPrediction = aICorrespondentPredictionMapper.toEntity(returnedAICorrespondentPredictionDTO);
        assertAICorrespondentPredictionUpdatableFieldsEquals(
            returnedAICorrespondentPrediction,
            getPersistedAICorrespondentPrediction(returnedAICorrespondentPrediction)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAICorrespondentPrediction = returnedAICorrespondentPrediction;
    }

    @Test
    @Transactional
    void createAICorrespondentPredictionWithExistingId() throws Exception {
        // Create the AICorrespondentPrediction with an existing ID
        aICorrespondentPrediction.setId(1L);
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAICorrespondentPredictionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCorrespondentNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        // set the field null
        aICorrespondentPrediction.setCorrespondentName(null);

        // Create the AICorrespondentPrediction, which fails.
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        restAICorrespondentPredictionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        // set the field null
        aICorrespondentPrediction.setName(null);

        // Create the AICorrespondentPrediction, which fails.
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        restAICorrespondentPredictionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        // set the field null
        aICorrespondentPrediction.setConfidence(null);

        // Create the AICorrespondentPrediction, which fails.
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        restAICorrespondentPredictionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPredictionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        // set the field null
        aICorrespondentPrediction.setPredictionDate(null);

        // Create the AICorrespondentPrediction, which fails.
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        restAICorrespondentPredictionMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictions() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICorrespondentPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondentName").value(hasItem(DEFAULT_CORRESPONDENT_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
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
    void getAICorrespondentPrediction() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get the aICorrespondentPrediction
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL_ID, aICorrespondentPrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aICorrespondentPrediction.getId().intValue()))
            .andExpect(jsonPath("$.correspondentName").value(DEFAULT_CORRESPONDENT_NAME))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.company").value(DEFAULT_COMPANY))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.role").value(DEFAULT_ROLE.toString()))
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
    void getAICorrespondentPredictionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        Long id = aICorrespondentPrediction.getId();

        defaultAICorrespondentPredictionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAICorrespondentPredictionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAICorrespondentPredictionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCorrespondentNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where correspondentName equals to
        defaultAICorrespondentPredictionFiltering(
            "correspondentName.equals=" + DEFAULT_CORRESPONDENT_NAME,
            "correspondentName.equals=" + UPDATED_CORRESPONDENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCorrespondentNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where correspondentName in
        defaultAICorrespondentPredictionFiltering(
            "correspondentName.in=" + DEFAULT_CORRESPONDENT_NAME + "," + UPDATED_CORRESPONDENT_NAME,
            "correspondentName.in=" + UPDATED_CORRESPONDENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCorrespondentNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where correspondentName is not null
        defaultAICorrespondentPredictionFiltering("correspondentName.specified=true", "correspondentName.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCorrespondentNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where correspondentName contains
        defaultAICorrespondentPredictionFiltering(
            "correspondentName.contains=" + DEFAULT_CORRESPONDENT_NAME,
            "correspondentName.contains=" + UPDATED_CORRESPONDENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCorrespondentNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where correspondentName does not contain
        defaultAICorrespondentPredictionFiltering(
            "correspondentName.doesNotContain=" + UPDATED_CORRESPONDENT_NAME,
            "correspondentName.doesNotContain=" + DEFAULT_CORRESPONDENT_NAME
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where name equals to
        defaultAICorrespondentPredictionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where name in
        defaultAICorrespondentPredictionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where name is not null
        defaultAICorrespondentPredictionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where name contains
        defaultAICorrespondentPredictionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where name does not contain
        defaultAICorrespondentPredictionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where email equals to
        defaultAICorrespondentPredictionFiltering("email.equals=" + DEFAULT_EMAIL, "email.equals=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where email in
        defaultAICorrespondentPredictionFiltering("email.in=" + DEFAULT_EMAIL + "," + UPDATED_EMAIL, "email.in=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where email is not null
        defaultAICorrespondentPredictionFiltering("email.specified=true", "email.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where email contains
        defaultAICorrespondentPredictionFiltering("email.contains=" + DEFAULT_EMAIL, "email.contains=" + UPDATED_EMAIL);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where email does not contain
        defaultAICorrespondentPredictionFiltering("email.doesNotContain=" + UPDATED_EMAIL, "email.doesNotContain=" + DEFAULT_EMAIL);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where phone equals to
        defaultAICorrespondentPredictionFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where phone in
        defaultAICorrespondentPredictionFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where phone is not null
        defaultAICorrespondentPredictionFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where phone contains
        defaultAICorrespondentPredictionFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where phone does not contain
        defaultAICorrespondentPredictionFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCompanyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where company equals to
        defaultAICorrespondentPredictionFiltering("company.equals=" + DEFAULT_COMPANY, "company.equals=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCompanyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where company in
        defaultAICorrespondentPredictionFiltering("company.in=" + DEFAULT_COMPANY + "," + UPDATED_COMPANY, "company.in=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCompanyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where company is not null
        defaultAICorrespondentPredictionFiltering("company.specified=true", "company.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCompanyContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where company contains
        defaultAICorrespondentPredictionFiltering("company.contains=" + DEFAULT_COMPANY, "company.contains=" + UPDATED_COMPANY);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByCompanyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where company does not contain
        defaultAICorrespondentPredictionFiltering("company.doesNotContain=" + UPDATED_COMPANY, "company.doesNotContain=" + DEFAULT_COMPANY);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where type equals to
        defaultAICorrespondentPredictionFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where type in
        defaultAICorrespondentPredictionFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where type is not null
        defaultAICorrespondentPredictionFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByRoleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where role equals to
        defaultAICorrespondentPredictionFiltering("role.equals=" + DEFAULT_ROLE, "role.equals=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByRoleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where role in
        defaultAICorrespondentPredictionFiltering("role.in=" + DEFAULT_ROLE + "," + UPDATED_ROLE, "role.in=" + UPDATED_ROLE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByRoleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where role is not null
        defaultAICorrespondentPredictionFiltering("role.specified=true", "role.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence equals to
        defaultAICorrespondentPredictionFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence in
        defaultAICorrespondentPredictionFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence is not null
        defaultAICorrespondentPredictionFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence is greater than or equal to
        defaultAICorrespondentPredictionFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence is less than or equal to
        defaultAICorrespondentPredictionFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence is less than
        defaultAICorrespondentPredictionFiltering(
            "confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1),
            "confidence.lessThan=" + DEFAULT_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where confidence is greater than
        defaultAICorrespondentPredictionFiltering(
            "confidence.greaterThan=" + SMALLER_CONFIDENCE,
            "confidence.greaterThan=" + DEFAULT_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByReasonIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where reason equals to
        defaultAICorrespondentPredictionFiltering("reason.equals=" + DEFAULT_REASON, "reason.equals=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByReasonIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where reason in
        defaultAICorrespondentPredictionFiltering("reason.in=" + DEFAULT_REASON + "," + UPDATED_REASON, "reason.in=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByReasonIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where reason is not null
        defaultAICorrespondentPredictionFiltering("reason.specified=true", "reason.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByReasonContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where reason contains
        defaultAICorrespondentPredictionFiltering("reason.contains=" + DEFAULT_REASON, "reason.contains=" + UPDATED_REASON);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByReasonNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where reason does not contain
        defaultAICorrespondentPredictionFiltering("reason.doesNotContain=" + UPDATED_REASON, "reason.doesNotContain=" + DEFAULT_REASON);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByModelVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where modelVersion equals to
        defaultAICorrespondentPredictionFiltering(
            "modelVersion.equals=" + DEFAULT_MODEL_VERSION,
            "modelVersion.equals=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByModelVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where modelVersion in
        defaultAICorrespondentPredictionFiltering(
            "modelVersion.in=" + DEFAULT_MODEL_VERSION + "," + UPDATED_MODEL_VERSION,
            "modelVersion.in=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByModelVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where modelVersion is not null
        defaultAICorrespondentPredictionFiltering("modelVersion.specified=true", "modelVersion.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByModelVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where modelVersion contains
        defaultAICorrespondentPredictionFiltering(
            "modelVersion.contains=" + DEFAULT_MODEL_VERSION,
            "modelVersion.contains=" + UPDATED_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByModelVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where modelVersion does not contain
        defaultAICorrespondentPredictionFiltering(
            "modelVersion.doesNotContain=" + UPDATED_MODEL_VERSION,
            "modelVersion.doesNotContain=" + DEFAULT_MODEL_VERSION
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionS3Key equals to
        defaultAICorrespondentPredictionFiltering(
            "predictionS3Key.equals=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.equals=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionS3Key in
        defaultAICorrespondentPredictionFiltering(
            "predictionS3Key.in=" + DEFAULT_PREDICTION_S_3_KEY + "," + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.in=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionS3Key is not null
        defaultAICorrespondentPredictionFiltering("predictionS3Key.specified=true", "predictionS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionS3Key contains
        defaultAICorrespondentPredictionFiltering(
            "predictionS3Key.contains=" + DEFAULT_PREDICTION_S_3_KEY,
            "predictionS3Key.contains=" + UPDATED_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionS3Key does not contain
        defaultAICorrespondentPredictionFiltering(
            "predictionS3Key.doesNotContain=" + UPDATED_PREDICTION_S_3_KEY,
            "predictionS3Key.doesNotContain=" + DEFAULT_PREDICTION_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByIsAcceptedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where isAccepted equals to
        defaultAICorrespondentPredictionFiltering("isAccepted.equals=" + DEFAULT_IS_ACCEPTED, "isAccepted.equals=" + UPDATED_IS_ACCEPTED);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByIsAcceptedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where isAccepted in
        defaultAICorrespondentPredictionFiltering(
            "isAccepted.in=" + DEFAULT_IS_ACCEPTED + "," + UPDATED_IS_ACCEPTED,
            "isAccepted.in=" + UPDATED_IS_ACCEPTED
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByIsAcceptedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where isAccepted is not null
        defaultAICorrespondentPredictionFiltering("isAccepted.specified=true", "isAccepted.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedBy equals to
        defaultAICorrespondentPredictionFiltering("acceptedBy.equals=" + DEFAULT_ACCEPTED_BY, "acceptedBy.equals=" + UPDATED_ACCEPTED_BY);
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedBy in
        defaultAICorrespondentPredictionFiltering(
            "acceptedBy.in=" + DEFAULT_ACCEPTED_BY + "," + UPDATED_ACCEPTED_BY,
            "acceptedBy.in=" + UPDATED_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedBy is not null
        defaultAICorrespondentPredictionFiltering("acceptedBy.specified=true", "acceptedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedByContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedBy contains
        defaultAICorrespondentPredictionFiltering(
            "acceptedBy.contains=" + DEFAULT_ACCEPTED_BY,
            "acceptedBy.contains=" + UPDATED_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedBy does not contain
        defaultAICorrespondentPredictionFiltering(
            "acceptedBy.doesNotContain=" + UPDATED_ACCEPTED_BY,
            "acceptedBy.doesNotContain=" + DEFAULT_ACCEPTED_BY
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedDate equals to
        defaultAICorrespondentPredictionFiltering(
            "acceptedDate.equals=" + DEFAULT_ACCEPTED_DATE,
            "acceptedDate.equals=" + UPDATED_ACCEPTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedDate in
        defaultAICorrespondentPredictionFiltering(
            "acceptedDate.in=" + DEFAULT_ACCEPTED_DATE + "," + UPDATED_ACCEPTED_DATE,
            "acceptedDate.in=" + UPDATED_ACCEPTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByAcceptedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where acceptedDate is not null
        defaultAICorrespondentPredictionFiltering("acceptedDate.specified=true", "acceptedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionDate equals to
        defaultAICorrespondentPredictionFiltering(
            "predictionDate.equals=" + DEFAULT_PREDICTION_DATE,
            "predictionDate.equals=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionDate in
        defaultAICorrespondentPredictionFiltering(
            "predictionDate.in=" + DEFAULT_PREDICTION_DATE + "," + UPDATED_PREDICTION_DATE,
            "predictionDate.in=" + UPDATED_PREDICTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByPredictionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        // Get all the aICorrespondentPredictionList where predictionDate is not null
        defaultAICorrespondentPredictionFiltering("predictionDate.specified=true", "predictionDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAICorrespondentPredictionsByJobIsEqualToSomething() throws Exception {
        AIAutoTagJob job;
        if (TestUtil.findAll(em, AIAutoTagJob.class).isEmpty()) {
            aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);
            job = AIAutoTagJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, AIAutoTagJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        aICorrespondentPrediction.setJob(job);
        aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);
        Long jobId = job.getId();
        // Get all the aICorrespondentPredictionList where job equals to jobId
        defaultAICorrespondentPredictionShouldBeFound("jobId.equals=" + jobId);

        // Get all the aICorrespondentPredictionList where job equals to (jobId + 1)
        defaultAICorrespondentPredictionShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultAICorrespondentPredictionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAICorrespondentPredictionShouldBeFound(shouldBeFound);
        defaultAICorrespondentPredictionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAICorrespondentPredictionShouldBeFound(String filter) throws Exception {
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICorrespondentPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondentName").value(hasItem(DEFAULT_CORRESPONDENT_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));

        // Check, that the count call also returns 1
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAICorrespondentPredictionShouldNotBeFound(String filter) throws Exception {
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAICorrespondentPrediction() throws Exception {
        // Get the aICorrespondentPrediction
        restAICorrespondentPredictionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAICorrespondentPrediction() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aICorrespondentPredictionSearchRepository.save(aICorrespondentPrediction);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());

        // Update the aICorrespondentPrediction
        AICorrespondentPrediction updatedAICorrespondentPrediction = aICorrespondentPredictionRepository
            .findById(aICorrespondentPrediction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAICorrespondentPrediction are not directly saved in db
        em.detach(updatedAICorrespondentPrediction);
        updatedAICorrespondentPrediction
            .correspondentName(UPDATED_CORRESPONDENT_NAME)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(updatedAICorrespondentPrediction);

        restAICorrespondentPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aICorrespondentPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isOk());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAICorrespondentPredictionToMatchAllProperties(updatedAICorrespondentPrediction);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AICorrespondentPrediction> aICorrespondentPredictionSearchList = Streamable.of(
                    aICorrespondentPredictionSearchRepository.findAll()
                ).toList();
                AICorrespondentPrediction testAICorrespondentPredictionSearch = aICorrespondentPredictionSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertAICorrespondentPredictionAllPropertiesEquals(testAICorrespondentPredictionSearch, updatedAICorrespondentPrediction);
            });
    }

    @Test
    @Transactional
    void putNonExistingAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aICorrespondentPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAICorrespondentPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aICorrespondentPrediction using partial update
        AICorrespondentPrediction partialUpdatedAICorrespondentPrediction = new AICorrespondentPrediction();
        partialUpdatedAICorrespondentPrediction.setId(aICorrespondentPrediction.getId());

        partialUpdatedAICorrespondentPrediction
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .reason(UPDATED_REASON)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE);

        restAICorrespondentPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAICorrespondentPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAICorrespondentPrediction))
            )
            .andExpect(status().isOk());

        // Validate the AICorrespondentPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAICorrespondentPredictionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAICorrespondentPrediction, aICorrespondentPrediction),
            getPersistedAICorrespondentPrediction(aICorrespondentPrediction)
        );
    }

    @Test
    @Transactional
    void fullUpdateAICorrespondentPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aICorrespondentPrediction using partial update
        AICorrespondentPrediction partialUpdatedAICorrespondentPrediction = new AICorrespondentPrediction();
        partialUpdatedAICorrespondentPrediction.setId(aICorrespondentPrediction.getId());

        partialUpdatedAICorrespondentPrediction
            .correspondentName(UPDATED_CORRESPONDENT_NAME)
            .name(UPDATED_NAME)
            .email(UPDATED_EMAIL)
            .phone(UPDATED_PHONE)
            .address(UPDATED_ADDRESS)
            .company(UPDATED_COMPANY)
            .type(UPDATED_TYPE)
            .role(UPDATED_ROLE)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restAICorrespondentPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAICorrespondentPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAICorrespondentPrediction))
            )
            .andExpect(status().isOk());

        // Validate the AICorrespondentPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAICorrespondentPredictionUpdatableFieldsEquals(
            partialUpdatedAICorrespondentPrediction,
            getPersistedAICorrespondentPrediction(partialUpdatedAICorrespondentPrediction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aICorrespondentPredictionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAICorrespondentPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        aICorrespondentPrediction.setId(longCount.incrementAndGet());

        // Create the AICorrespondentPrediction
        AICorrespondentPredictionDTO aICorrespondentPredictionDTO = aICorrespondentPredictionMapper.toDto(aICorrespondentPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICorrespondentPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aICorrespondentPredictionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AICorrespondentPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAICorrespondentPrediction() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);
        aICorrespondentPredictionRepository.save(aICorrespondentPrediction);
        aICorrespondentPredictionSearchRepository.save(aICorrespondentPrediction);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aICorrespondentPrediction
        restAICorrespondentPredictionMockMvc
            .perform(delete(ENTITY_API_URL_ID, aICorrespondentPrediction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICorrespondentPredictionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAICorrespondentPrediction() throws Exception {
        // Initialize the database
        insertedAICorrespondentPrediction = aICorrespondentPredictionRepository.saveAndFlush(aICorrespondentPrediction);
        aICorrespondentPredictionSearchRepository.save(aICorrespondentPrediction);

        // Search the aICorrespondentPrediction
        restAICorrespondentPredictionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aICorrespondentPrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICorrespondentPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].correspondentName").value(hasItem(DEFAULT_CORRESPONDENT_NAME)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
            .andExpect(jsonPath("$.[*].company").value(hasItem(DEFAULT_COMPANY)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].role").value(hasItem(DEFAULT_ROLE.toString())))
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
        return aICorrespondentPredictionRepository.count();
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

    protected AICorrespondentPrediction getPersistedAICorrespondentPrediction(AICorrespondentPrediction aICorrespondentPrediction) {
        return aICorrespondentPredictionRepository.findById(aICorrespondentPrediction.getId()).orElseThrow();
    }

    protected void assertPersistedAICorrespondentPredictionToMatchAllProperties(
        AICorrespondentPrediction expectedAICorrespondentPrediction
    ) {
        assertAICorrespondentPredictionAllPropertiesEquals(
            expectedAICorrespondentPrediction,
            getPersistedAICorrespondentPrediction(expectedAICorrespondentPrediction)
        );
    }

    protected void assertPersistedAICorrespondentPredictionToMatchUpdatableProperties(
        AICorrespondentPrediction expectedAICorrespondentPrediction
    ) {
        assertAICorrespondentPredictionAllUpdatablePropertiesEquals(
            expectedAICorrespondentPrediction,
            getPersistedAICorrespondentPrediction(expectedAICorrespondentPrediction)
        );
    }
}
