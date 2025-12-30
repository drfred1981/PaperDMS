package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AICacheAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AICache;
import fr.smartprod.paperdms.ai.repository.AICacheRepository;
import fr.smartprod.paperdms.ai.repository.search.AICacheSearchRepository;
import fr.smartprod.paperdms.ai.service.dto.AICacheDTO;
import fr.smartprod.paperdms.ai.service.mapper.AICacheMapper;
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
 * Integration tests for the {@link AICacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AICacheResourceIT {

    private static final String DEFAULT_CACHE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_CACHE_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_INPUT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_AI_PROVIDER = "AAAAAAAAAA";
    private static final String UPDATED_AI_PROVIDER = "BBBBBBBBBB";

    private static final String DEFAULT_AI_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_AI_MODEL = "BBBBBBBBBB";

    private static final String DEFAULT_OPERATION = "AAAAAAAAAA";
    private static final String UPDATED_OPERATION = "BBBBBBBBBB";

    private static final String DEFAULT_INPUT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_INPUT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_RESULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_RESULT_KEY = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;
    private static final Integer SMALLER_HITS = 1 - 1;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;
    private static final Double SMALLER_COST = 1D - 1D;

    private static final Instant DEFAULT_LAST_ACCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ai-caches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ai-caches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AICacheRepository aICacheRepository;

    @Autowired
    private AICacheMapper aICacheMapper;

    @Autowired
    private AICacheSearchRepository aICacheSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAICacheMockMvc;

    private AICache aICache;

    private AICache insertedAICache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AICache createEntity() {
        return new AICache()
            .cacheKey(DEFAULT_CACHE_KEY)
            .inputSha256(DEFAULT_INPUT_SHA_256)
            .aiProvider(DEFAULT_AI_PROVIDER)
            .aiModel(DEFAULT_AI_MODEL)
            .operation(DEFAULT_OPERATION)
            .inputData(DEFAULT_INPUT_DATA)
            .resultData(DEFAULT_RESULT_DATA)
            .s3ResultKey(DEFAULT_S_3_RESULT_KEY)
            .confidence(DEFAULT_CONFIDENCE)
            .metadata(DEFAULT_METADATA)
            .hits(DEFAULT_HITS)
            .cost(DEFAULT_COST)
            .lastAccessDate(DEFAULT_LAST_ACCESS_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AICache createUpdatedEntity() {
        return new AICache()
            .cacheKey(UPDATED_CACHE_KEY)
            .inputSha256(UPDATED_INPUT_SHA_256)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .operation(UPDATED_OPERATION)
            .inputData(UPDATED_INPUT_DATA)
            .resultData(UPDATED_RESULT_DATA)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .confidence(UPDATED_CONFIDENCE)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .cost(UPDATED_COST)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
    }

    @BeforeEach
    void initTest() {
        aICache = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAICache != null) {
            aICacheRepository.delete(insertedAICache);
            aICacheSearchRepository.delete(insertedAICache);
            insertedAICache = null;
        }
    }

    @Test
    @Transactional
    void createAICache() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);
        var returnedAICacheDTO = om.readValue(
            restAICacheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AICacheDTO.class
        );

        // Validate the AICache in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAICache = aICacheMapper.toEntity(returnedAICacheDTO);
        assertAICacheUpdatableFieldsEquals(returnedAICache, getPersistedAICache(returnedAICache));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedAICache = returnedAICache;
    }

    @Test
    @Transactional
    void createAICacheWithExistingId() throws Exception {
        // Create the AICache with an existing ID
        aICache.setId(1L);
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCacheKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // set the field null
        aICache.setCacheKey(null);

        // Create the AICache, which fails.
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkInputSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // set the field null
        aICache.setInputSha256(null);

        // Create the AICache, which fails.
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAiProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // set the field null
        aICache.setAiProvider(null);

        // Create the AICache, which fails.
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkOperationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // set the field null
        aICache.setOperation(null);

        // Create the AICache, which fails.
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        // set the field null
        aICache.setCreatedDate(null);

        // Create the AICache, which fails.
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        restAICacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllAICaches() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICache.getId().intValue())))
            .andExpect(jsonPath("$.[*].cacheKey").value(hasItem(DEFAULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].inputSha256").value(hasItem(DEFAULT_INPUT_SHA_256)))
            .andExpect(jsonPath("$.[*].aiProvider").value(hasItem(DEFAULT_AI_PROVIDER)))
            .andExpect(jsonPath("$.[*].aiModel").value(hasItem(DEFAULT_AI_MODEL)))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION)))
            .andExpect(jsonPath("$.[*].inputData").value(hasItem(DEFAULT_INPUT_DATA)))
            .andExpect(jsonPath("$.[*].resultData").value(hasItem(DEFAULT_RESULT_DATA)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getAICache() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get the aICache
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL_ID, aICache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aICache.getId().intValue()))
            .andExpect(jsonPath("$.cacheKey").value(DEFAULT_CACHE_KEY))
            .andExpect(jsonPath("$.inputSha256").value(DEFAULT_INPUT_SHA_256))
            .andExpect(jsonPath("$.aiProvider").value(DEFAULT_AI_PROVIDER))
            .andExpect(jsonPath("$.aiModel").value(DEFAULT_AI_MODEL))
            .andExpect(jsonPath("$.operation").value(DEFAULT_OPERATION))
            .andExpect(jsonPath("$.inputData").value(DEFAULT_INPUT_DATA))
            .andExpect(jsonPath("$.resultData").value(DEFAULT_RESULT_DATA))
            .andExpect(jsonPath("$.s3ResultKey").value(DEFAULT_S_3_RESULT_KEY))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS))
            .andExpect(jsonPath("$.cost").value(DEFAULT_COST))
            .andExpect(jsonPath("$.lastAccessDate").value(DEFAULT_LAST_ACCESS_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getAICachesByIdFiltering() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        Long id = aICache.getId();

        defaultAICacheFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultAICacheFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultAICacheFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllAICachesByCacheKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cacheKey equals to
        defaultAICacheFiltering("cacheKey.equals=" + DEFAULT_CACHE_KEY, "cacheKey.equals=" + UPDATED_CACHE_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesByCacheKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cacheKey in
        defaultAICacheFiltering("cacheKey.in=" + DEFAULT_CACHE_KEY + "," + UPDATED_CACHE_KEY, "cacheKey.in=" + UPDATED_CACHE_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesByCacheKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cacheKey is not null
        defaultAICacheFiltering("cacheKey.specified=true", "cacheKey.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByCacheKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cacheKey contains
        defaultAICacheFiltering("cacheKey.contains=" + DEFAULT_CACHE_KEY, "cacheKey.contains=" + UPDATED_CACHE_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesByCacheKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cacheKey does not contain
        defaultAICacheFiltering("cacheKey.doesNotContain=" + UPDATED_CACHE_KEY, "cacheKey.doesNotContain=" + DEFAULT_CACHE_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesByInputSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where inputSha256 equals to
        defaultAICacheFiltering("inputSha256.equals=" + DEFAULT_INPUT_SHA_256, "inputSha256.equals=" + UPDATED_INPUT_SHA_256);
    }

    @Test
    @Transactional
    void getAllAICachesByInputSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where inputSha256 in
        defaultAICacheFiltering(
            "inputSha256.in=" + DEFAULT_INPUT_SHA_256 + "," + UPDATED_INPUT_SHA_256,
            "inputSha256.in=" + UPDATED_INPUT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAICachesByInputSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where inputSha256 is not null
        defaultAICacheFiltering("inputSha256.specified=true", "inputSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByInputSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where inputSha256 contains
        defaultAICacheFiltering("inputSha256.contains=" + DEFAULT_INPUT_SHA_256, "inputSha256.contains=" + UPDATED_INPUT_SHA_256);
    }

    @Test
    @Transactional
    void getAllAICachesByInputSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where inputSha256 does not contain
        defaultAICacheFiltering(
            "inputSha256.doesNotContain=" + UPDATED_INPUT_SHA_256,
            "inputSha256.doesNotContain=" + DEFAULT_INPUT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllAICachesByAiProviderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiProvider equals to
        defaultAICacheFiltering("aiProvider.equals=" + DEFAULT_AI_PROVIDER, "aiProvider.equals=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllAICachesByAiProviderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiProvider in
        defaultAICacheFiltering("aiProvider.in=" + DEFAULT_AI_PROVIDER + "," + UPDATED_AI_PROVIDER, "aiProvider.in=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllAICachesByAiProviderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiProvider is not null
        defaultAICacheFiltering("aiProvider.specified=true", "aiProvider.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByAiProviderContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiProvider contains
        defaultAICacheFiltering("aiProvider.contains=" + DEFAULT_AI_PROVIDER, "aiProvider.contains=" + UPDATED_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllAICachesByAiProviderNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiProvider does not contain
        defaultAICacheFiltering("aiProvider.doesNotContain=" + UPDATED_AI_PROVIDER, "aiProvider.doesNotContain=" + DEFAULT_AI_PROVIDER);
    }

    @Test
    @Transactional
    void getAllAICachesByAiModelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiModel equals to
        defaultAICacheFiltering("aiModel.equals=" + DEFAULT_AI_MODEL, "aiModel.equals=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllAICachesByAiModelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiModel in
        defaultAICacheFiltering("aiModel.in=" + DEFAULT_AI_MODEL + "," + UPDATED_AI_MODEL, "aiModel.in=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllAICachesByAiModelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiModel is not null
        defaultAICacheFiltering("aiModel.specified=true", "aiModel.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByAiModelContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiModel contains
        defaultAICacheFiltering("aiModel.contains=" + DEFAULT_AI_MODEL, "aiModel.contains=" + UPDATED_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllAICachesByAiModelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where aiModel does not contain
        defaultAICacheFiltering("aiModel.doesNotContain=" + UPDATED_AI_MODEL, "aiModel.doesNotContain=" + DEFAULT_AI_MODEL);
    }

    @Test
    @Transactional
    void getAllAICachesByOperationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where operation equals to
        defaultAICacheFiltering("operation.equals=" + DEFAULT_OPERATION, "operation.equals=" + UPDATED_OPERATION);
    }

    @Test
    @Transactional
    void getAllAICachesByOperationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where operation in
        defaultAICacheFiltering("operation.in=" + DEFAULT_OPERATION + "," + UPDATED_OPERATION, "operation.in=" + UPDATED_OPERATION);
    }

    @Test
    @Transactional
    void getAllAICachesByOperationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where operation is not null
        defaultAICacheFiltering("operation.specified=true", "operation.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByOperationContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where operation contains
        defaultAICacheFiltering("operation.contains=" + DEFAULT_OPERATION, "operation.contains=" + UPDATED_OPERATION);
    }

    @Test
    @Transactional
    void getAllAICachesByOperationNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where operation does not contain
        defaultAICacheFiltering("operation.doesNotContain=" + UPDATED_OPERATION, "operation.doesNotContain=" + DEFAULT_OPERATION);
    }

    @Test
    @Transactional
    void getAllAICachesBys3ResultKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where s3ResultKey equals to
        defaultAICacheFiltering("s3ResultKey.equals=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.equals=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesBys3ResultKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where s3ResultKey in
        defaultAICacheFiltering(
            "s3ResultKey.in=" + DEFAULT_S_3_RESULT_KEY + "," + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.in=" + UPDATED_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICachesBys3ResultKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where s3ResultKey is not null
        defaultAICacheFiltering("s3ResultKey.specified=true", "s3ResultKey.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesBys3ResultKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where s3ResultKey contains
        defaultAICacheFiltering("s3ResultKey.contains=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.contains=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllAICachesBys3ResultKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where s3ResultKey does not contain
        defaultAICacheFiltering(
            "s3ResultKey.doesNotContain=" + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.doesNotContain=" + DEFAULT_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence equals to
        defaultAICacheFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence in
        defaultAICacheFiltering("confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE, "confidence.in=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence is not null
        defaultAICacheFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence is greater than or equal to
        defaultAICacheFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence is less than or equal to
        defaultAICacheFiltering("confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE, "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence is less than
        defaultAICacheFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICachesByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where confidence is greater than
        defaultAICacheFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits equals to
        defaultAICacheFiltering("hits.equals=" + DEFAULT_HITS, "hits.equals=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits in
        defaultAICacheFiltering("hits.in=" + DEFAULT_HITS + "," + UPDATED_HITS, "hits.in=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits is not null
        defaultAICacheFiltering("hits.specified=true", "hits.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits is greater than or equal to
        defaultAICacheFiltering("hits.greaterThanOrEqual=" + DEFAULT_HITS, "hits.greaterThanOrEqual=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits is less than or equal to
        defaultAICacheFiltering("hits.lessThanOrEqual=" + DEFAULT_HITS, "hits.lessThanOrEqual=" + SMALLER_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits is less than
        defaultAICacheFiltering("hits.lessThan=" + UPDATED_HITS, "hits.lessThan=" + DEFAULT_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByHitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where hits is greater than
        defaultAICacheFiltering("hits.greaterThan=" + SMALLER_HITS, "hits.greaterThan=" + DEFAULT_HITS);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost equals to
        defaultAICacheFiltering("cost.equals=" + DEFAULT_COST, "cost.equals=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost in
        defaultAICacheFiltering("cost.in=" + DEFAULT_COST + "," + UPDATED_COST, "cost.in=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost is not null
        defaultAICacheFiltering("cost.specified=true", "cost.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost is greater than or equal to
        defaultAICacheFiltering("cost.greaterThanOrEqual=" + DEFAULT_COST, "cost.greaterThanOrEqual=" + UPDATED_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost is less than or equal to
        defaultAICacheFiltering("cost.lessThanOrEqual=" + DEFAULT_COST, "cost.lessThanOrEqual=" + SMALLER_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost is less than
        defaultAICacheFiltering("cost.lessThan=" + UPDATED_COST, "cost.lessThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where cost is greater than
        defaultAICacheFiltering("cost.greaterThan=" + SMALLER_COST, "cost.greaterThan=" + DEFAULT_COST);
    }

    @Test
    @Transactional
    void getAllAICachesByLastAccessDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where lastAccessDate equals to
        defaultAICacheFiltering("lastAccessDate.equals=" + DEFAULT_LAST_ACCESS_DATE, "lastAccessDate.equals=" + UPDATED_LAST_ACCESS_DATE);
    }

    @Test
    @Transactional
    void getAllAICachesByLastAccessDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where lastAccessDate in
        defaultAICacheFiltering(
            "lastAccessDate.in=" + DEFAULT_LAST_ACCESS_DATE + "," + UPDATED_LAST_ACCESS_DATE,
            "lastAccessDate.in=" + UPDATED_LAST_ACCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICachesByLastAccessDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where lastAccessDate is not null
        defaultAICacheFiltering("lastAccessDate.specified=true", "lastAccessDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where createdDate equals to
        defaultAICacheFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllAICachesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where createdDate in
        defaultAICacheFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICachesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where createdDate is not null
        defaultAICacheFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllAICachesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where expirationDate equals to
        defaultAICacheFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllAICachesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where expirationDate in
        defaultAICacheFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllAICachesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        // Get all the aICacheList where expirationDate is not null
        defaultAICacheFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    private void defaultAICacheFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultAICacheShouldBeFound(shouldBeFound);
        defaultAICacheShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultAICacheShouldBeFound(String filter) throws Exception {
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICache.getId().intValue())))
            .andExpect(jsonPath("$.[*].cacheKey").value(hasItem(DEFAULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].inputSha256").value(hasItem(DEFAULT_INPUT_SHA_256)))
            .andExpect(jsonPath("$.[*].aiProvider").value(hasItem(DEFAULT_AI_PROVIDER)))
            .andExpect(jsonPath("$.[*].aiModel").value(hasItem(DEFAULT_AI_MODEL)))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION)))
            .andExpect(jsonPath("$.[*].inputData").value(hasItem(DEFAULT_INPUT_DATA)))
            .andExpect(jsonPath("$.[*].resultData").value(hasItem(DEFAULT_RESULT_DATA)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultAICacheShouldNotBeFound(String filter) throws Exception {
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restAICacheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingAICache() throws Exception {
        // Get the aICache
        restAICacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAICache() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        aICacheSearchRepository.save(aICache);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());

        // Update the aICache
        AICache updatedAICache = aICacheRepository.findById(aICache.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAICache are not directly saved in db
        em.detach(updatedAICache);
        updatedAICache
            .cacheKey(UPDATED_CACHE_KEY)
            .inputSha256(UPDATED_INPUT_SHA_256)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .operation(UPDATED_OPERATION)
            .inputData(UPDATED_INPUT_DATA)
            .resultData(UPDATED_RESULT_DATA)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .confidence(UPDATED_CONFIDENCE)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .cost(UPDATED_COST)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        AICacheDTO aICacheDTO = aICacheMapper.toDto(updatedAICache);

        restAICacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aICacheDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAICacheToMatchAllProperties(updatedAICache);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<AICache> aICacheSearchList = Streamable.of(aICacheSearchRepository.findAll()).toList();
                AICache testAICacheSearch = aICacheSearchList.get(searchDatabaseSizeAfter - 1);

                assertAICacheAllPropertiesEquals(testAICacheSearch, updatedAICache);
            });
    }

    @Test
    @Transactional
    void putNonExistingAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aICacheDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aICacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateAICacheWithPatch() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aICache using partial update
        AICache partialUpdatedAICache = new AICache();
        partialUpdatedAICache.setId(aICache.getId());

        partialUpdatedAICache
            .inputSha256(UPDATED_INPUT_SHA_256)
            .aiProvider(UPDATED_AI_PROVIDER)
            .operation(UPDATED_OPERATION)
            .inputData(UPDATED_INPUT_DATA)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .confidence(UPDATED_CONFIDENCE)
            .metadata(UPDATED_METADATA)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE);

        restAICacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAICache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAICache))
            )
            .andExpect(status().isOk());

        // Validate the AICache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAICacheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAICache, aICache), getPersistedAICache(aICache));
    }

    @Test
    @Transactional
    void fullUpdateAICacheWithPatch() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aICache using partial update
        AICache partialUpdatedAICache = new AICache();
        partialUpdatedAICache.setId(aICache.getId());

        partialUpdatedAICache
            .cacheKey(UPDATED_CACHE_KEY)
            .inputSha256(UPDATED_INPUT_SHA_256)
            .aiProvider(UPDATED_AI_PROVIDER)
            .aiModel(UPDATED_AI_MODEL)
            .operation(UPDATED_OPERATION)
            .inputData(UPDATED_INPUT_DATA)
            .resultData(UPDATED_RESULT_DATA)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .confidence(UPDATED_CONFIDENCE)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .cost(UPDATED_COST)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restAICacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAICache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAICache))
            )
            .andExpect(status().isOk());

        // Validate the AICache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAICacheUpdatableFieldsEquals(partialUpdatedAICache, getPersistedAICache(partialUpdatedAICache));
    }

    @Test
    @Transactional
    void patchNonExistingAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aICacheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aICacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aICacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAICache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        aICache.setId(longCount.incrementAndGet());

        // Create the AICache
        AICacheDTO aICacheDTO = aICacheMapper.toDto(aICache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAICacheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aICacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AICache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteAICache() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);
        aICacheRepository.save(aICache);
        aICacheSearchRepository.save(aICache);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the aICache
        restAICacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, aICache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(aICacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchAICache() throws Exception {
        // Initialize the database
        insertedAICache = aICacheRepository.saveAndFlush(aICache);
        aICacheSearchRepository.save(aICache);

        // Search the aICache
        restAICacheMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + aICache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aICache.getId().intValue())))
            .andExpect(jsonPath("$.[*].cacheKey").value(hasItem(DEFAULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].inputSha256").value(hasItem(DEFAULT_INPUT_SHA_256)))
            .andExpect(jsonPath("$.[*].aiProvider").value(hasItem(DEFAULT_AI_PROVIDER)))
            .andExpect(jsonPath("$.[*].aiModel").value(hasItem(DEFAULT_AI_MODEL)))
            .andExpect(jsonPath("$.[*].operation").value(hasItem(DEFAULT_OPERATION)))
            .andExpect(jsonPath("$.[*].inputData").value(hasItem(DEFAULT_INPUT_DATA.toString())))
            .andExpect(jsonPath("$.[*].resultData").value(hasItem(DEFAULT_RESULT_DATA.toString())))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].cost").value(hasItem(DEFAULT_COST)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return aICacheRepository.count();
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

    protected AICache getPersistedAICache(AICache aICache) {
        return aICacheRepository.findById(aICache.getId()).orElseThrow();
    }

    protected void assertPersistedAICacheToMatchAllProperties(AICache expectedAICache) {
        assertAICacheAllPropertiesEquals(expectedAICache, getPersistedAICache(expectedAICache));
    }

    protected void assertPersistedAICacheToMatchUpdatableProperties(AICache expectedAICache) {
        assertAICacheAllUpdatablePropertiesEquals(expectedAICache, getPersistedAICache(expectedAICache));
    }
}
