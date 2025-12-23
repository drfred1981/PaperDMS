package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.AiCacheAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AiCache;
import fr.smartprod.paperdms.ai.repository.AiCacheRepository;
import fr.smartprod.paperdms.ai.service.dto.AiCacheDTO;
import fr.smartprod.paperdms.ai.service.mapper.AiCacheMapper;
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
 * Integration tests for the {@link AiCacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AiCacheResourceIT {

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

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;

    private static final Double DEFAULT_COST = 1D;
    private static final Double UPDATED_COST = 2D;

    private static final Instant DEFAULT_LAST_ACCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ai-caches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AiCacheRepository aiCacheRepository;

    @Autowired
    private AiCacheMapper aiCacheMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAiCacheMockMvc;

    private AiCache aiCache;

    private AiCache insertedAiCache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AiCache createEntity() {
        return new AiCache()
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
    public static AiCache createUpdatedEntity() {
        return new AiCache()
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
        aiCache = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedAiCache != null) {
            aiCacheRepository.delete(insertedAiCache);
            insertedAiCache = null;
        }
    }

    @Test
    @Transactional
    void createAiCache() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);
        var returnedAiCacheDTO = om.readValue(
            restAiCacheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AiCacheDTO.class
        );

        // Validate the AiCache in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAiCache = aiCacheMapper.toEntity(returnedAiCacheDTO);
        assertAiCacheUpdatableFieldsEquals(returnedAiCache, getPersistedAiCache(returnedAiCache));

        insertedAiCache = returnedAiCache;
    }

    @Test
    @Transactional
    void createAiCacheWithExistingId() throws Exception {
        // Create the AiCache with an existing ID
        aiCache.setId(1L);
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCacheKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aiCache.setCacheKey(null);

        // Create the AiCache, which fails.
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInputSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aiCache.setInputSha256(null);

        // Create the AiCache, which fails.
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAiProviderIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aiCache.setAiProvider(null);

        // Create the AiCache, which fails.
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOperationIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aiCache.setOperation(null);

        // Create the AiCache, which fails.
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        aiCache.setCreatedDate(null);

        // Create the AiCache, which fails.
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        restAiCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAiCaches() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        // Get all the aiCacheList
        restAiCacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(aiCache.getId().intValue())))
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
    void getAiCache() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        // Get the aiCache
        restAiCacheMockMvc
            .perform(get(ENTITY_API_URL_ID, aiCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(aiCache.getId().intValue()))
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
    void getNonExistingAiCache() throws Exception {
        // Get the aiCache
        restAiCacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAiCache() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aiCache
        AiCache updatedAiCache = aiCacheRepository.findById(aiCache.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAiCache are not directly saved in db
        em.detach(updatedAiCache);
        updatedAiCache
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
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(updatedAiCache);

        restAiCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aiCacheDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAiCacheToMatchAllProperties(updatedAiCache);
    }

    @Test
    @Transactional
    void putNonExistingAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, aiCacheDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(aiCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAiCacheWithPatch() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aiCache using partial update
        AiCache partialUpdatedAiCache = new AiCache();
        partialUpdatedAiCache.setId(aiCache.getId());

        partialUpdatedAiCache
            .inputSha256(UPDATED_INPUT_SHA_256)
            .inputData(UPDATED_INPUT_DATA)
            .hits(UPDATED_HITS)
            .cost(UPDATED_COST)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restAiCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAiCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAiCache))
            )
            .andExpect(status().isOk());

        // Validate the AiCache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAiCacheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedAiCache, aiCache), getPersistedAiCache(aiCache));
    }

    @Test
    @Transactional
    void fullUpdateAiCacheWithPatch() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the aiCache using partial update
        AiCache partialUpdatedAiCache = new AiCache();
        partialUpdatedAiCache.setId(aiCache.getId());

        partialUpdatedAiCache
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

        restAiCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAiCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAiCache))
            )
            .andExpect(status().isOk());

        // Validate the AiCache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAiCacheUpdatableFieldsEquals(partialUpdatedAiCache, getPersistedAiCache(partialUpdatedAiCache));
    }

    @Test
    @Transactional
    void patchNonExistingAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, aiCacheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aiCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(aiCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAiCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        aiCache.setId(longCount.incrementAndGet());

        // Create the AiCache
        AiCacheDTO aiCacheDTO = aiCacheMapper.toDto(aiCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAiCacheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(aiCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AiCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAiCache() throws Exception {
        // Initialize the database
        insertedAiCache = aiCacheRepository.saveAndFlush(aiCache);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the aiCache
        restAiCacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, aiCache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return aiCacheRepository.count();
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

    protected AiCache getPersistedAiCache(AiCache aiCache) {
        return aiCacheRepository.findById(aiCache.getId()).orElseThrow();
    }

    protected void assertPersistedAiCacheToMatchAllProperties(AiCache expectedAiCache) {
        assertAiCacheAllPropertiesEquals(expectedAiCache, getPersistedAiCache(expectedAiCache));
    }

    protected void assertPersistedAiCacheToMatchUpdatableProperties(AiCache expectedAiCache) {
        assertAiCacheAllUpdatablePropertiesEquals(expectedAiCache, getPersistedAiCache(expectedAiCache));
    }
}
