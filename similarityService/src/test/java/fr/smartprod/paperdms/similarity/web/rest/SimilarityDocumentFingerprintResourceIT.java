package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprintAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentFingerprintSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentFingerprintDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentFingerprintMapper;
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
 * Integration tests for the {@link SimilarityDocumentFingerprintResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimilarityDocumentFingerprintResourceIT {

    private static final FingerprintType DEFAULT_FINGERPRINT_TYPE = FingerprintType.HASH;
    private static final FingerprintType UPDATED_FINGERPRINT_TYPE = FingerprintType.PERCEPTUAL;

    private static final String DEFAULT_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT = "BBBBBBBBBB";

    private static final String DEFAULT_VECTOR_EMBEDDING = "AAAAAAAAAA";
    private static final String UPDATED_VECTOR_EMBEDDING = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPUTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPUTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/similarity-document-fingerprints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/similarity-document-fingerprints/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SimilarityDocumentFingerprintRepository similarityDocumentFingerprintRepository;

    @Autowired
    private SimilarityDocumentFingerprintMapper similarityDocumentFingerprintMapper;

    @Autowired
    private SimilarityDocumentFingerprintSearchRepository similarityDocumentFingerprintSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimilarityDocumentFingerprintMockMvc;

    private SimilarityDocumentFingerprint similarityDocumentFingerprint;

    private SimilarityDocumentFingerprint insertedSimilarityDocumentFingerprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityDocumentFingerprint createEntity() {
        return new SimilarityDocumentFingerprint()
            .fingerprintType(DEFAULT_FINGERPRINT_TYPE)
            .fingerprint(DEFAULT_FINGERPRINT)
            .vectorEmbedding(DEFAULT_VECTOR_EMBEDDING)
            .metadata(DEFAULT_METADATA)
            .computedDate(DEFAULT_COMPUTED_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityDocumentFingerprint createUpdatedEntity() {
        return new SimilarityDocumentFingerprint()
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        similarityDocumentFingerprint = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSimilarityDocumentFingerprint != null) {
            similarityDocumentFingerprintRepository.delete(insertedSimilarityDocumentFingerprint);
            similarityDocumentFingerprintSearchRepository.delete(insertedSimilarityDocumentFingerprint);
            insertedSimilarityDocumentFingerprint = null;
        }
    }

    @Test
    @Transactional
    void createSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );
        var returnedSimilarityDocumentFingerprintDTO = om.readValue(
            restSimilarityDocumentFingerprintMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SimilarityDocumentFingerprintDTO.class
        );

        // Validate the SimilarityDocumentFingerprint in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSimilarityDocumentFingerprint = similarityDocumentFingerprintMapper.toEntity(returnedSimilarityDocumentFingerprintDTO);
        assertSimilarityDocumentFingerprintUpdatableFieldsEquals(
            returnedSimilarityDocumentFingerprint,
            getPersistedSimilarityDocumentFingerprint(returnedSimilarityDocumentFingerprint)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSimilarityDocumentFingerprint = returnedSimilarityDocumentFingerprint;
    }

    @Test
    @Transactional
    void createSimilarityDocumentFingerprintWithExistingId() throws Exception {
        // Create the SimilarityDocumentFingerprint with an existing ID
        similarityDocumentFingerprint.setId(1L);
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkComputedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        // set the field null
        similarityDocumentFingerprint.setComputedDate(null);

        // Create the SimilarityDocumentFingerprint, which fails.
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        restSimilarityDocumentFingerprintMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprints() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentFingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].fingerprintType").value(hasItem(DEFAULT_FINGERPRINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT)))
            .andExpect(jsonPath("$.[*].vectorEmbedding").value(hasItem(DEFAULT_VECTOR_EMBEDDING)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSimilarityDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get the similarityDocumentFingerprint
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL_ID, similarityDocumentFingerprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(similarityDocumentFingerprint.getId().intValue()))
            .andExpect(jsonPath("$.fingerprintType").value(DEFAULT_FINGERPRINT_TYPE.toString()))
            .andExpect(jsonPath("$.fingerprint").value(DEFAULT_FINGERPRINT))
            .andExpect(jsonPath("$.vectorEmbedding").value(DEFAULT_VECTOR_EMBEDDING))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.computedDate").value(DEFAULT_COMPUTED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getSimilarityDocumentFingerprintsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        Long id = similarityDocumentFingerprint.getId();

        defaultSimilarityDocumentFingerprintFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSimilarityDocumentFingerprintFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSimilarityDocumentFingerprintFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByFingerprintTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where fingerprintType equals to
        defaultSimilarityDocumentFingerprintFiltering(
            "fingerprintType.equals=" + DEFAULT_FINGERPRINT_TYPE,
            "fingerprintType.equals=" + UPDATED_FINGERPRINT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByFingerprintTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where fingerprintType in
        defaultSimilarityDocumentFingerprintFiltering(
            "fingerprintType.in=" + DEFAULT_FINGERPRINT_TYPE + "," + UPDATED_FINGERPRINT_TYPE,
            "fingerprintType.in=" + UPDATED_FINGERPRINT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByFingerprintTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where fingerprintType is not null
        defaultSimilarityDocumentFingerprintFiltering("fingerprintType.specified=true", "fingerprintType.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByComputedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where computedDate equals to
        defaultSimilarityDocumentFingerprintFiltering(
            "computedDate.equals=" + DEFAULT_COMPUTED_DATE,
            "computedDate.equals=" + UPDATED_COMPUTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByComputedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where computedDate in
        defaultSimilarityDocumentFingerprintFiltering(
            "computedDate.in=" + DEFAULT_COMPUTED_DATE + "," + UPDATED_COMPUTED_DATE,
            "computedDate.in=" + UPDATED_COMPUTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByComputedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where computedDate is not null
        defaultSimilarityDocumentFingerprintFiltering("computedDate.specified=true", "computedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where lastUpdated equals to
        defaultSimilarityDocumentFingerprintFiltering(
            "lastUpdated.equals=" + DEFAULT_LAST_UPDATED,
            "lastUpdated.equals=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where lastUpdated in
        defaultSimilarityDocumentFingerprintFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentFingerprintsByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        // Get all the similarityDocumentFingerprintList where lastUpdated is not null
        defaultSimilarityDocumentFingerprintFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    private void defaultSimilarityDocumentFingerprintFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSimilarityDocumentFingerprintShouldBeFound(shouldBeFound);
        defaultSimilarityDocumentFingerprintShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimilarityDocumentFingerprintShouldBeFound(String filter) throws Exception {
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentFingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].fingerprintType").value(hasItem(DEFAULT_FINGERPRINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT)))
            .andExpect(jsonPath("$.[*].vectorEmbedding").value(hasItem(DEFAULT_VECTOR_EMBEDDING)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimilarityDocumentFingerprintShouldNotBeFound(String filter) throws Exception {
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimilarityDocumentFingerprint() throws Exception {
        // Get the similarityDocumentFingerprint
        restSimilarityDocumentFingerprintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimilarityDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityDocumentFingerprintSearchRepository.save(similarityDocumentFingerprint);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());

        // Update the similarityDocumentFingerprint
        SimilarityDocumentFingerprint updatedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository
            .findById(similarityDocumentFingerprint.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSimilarityDocumentFingerprint are not directly saved in db
        em.detach(updatedSimilarityDocumentFingerprint);
        updatedSimilarityDocumentFingerprint
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            updatedSimilarityDocumentFingerprint
        );

        restSimilarityDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityDocumentFingerprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSimilarityDocumentFingerprintToMatchAllProperties(updatedSimilarityDocumentFingerprint);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SimilarityDocumentFingerprint> similarityDocumentFingerprintSearchList = Streamable.of(
                    similarityDocumentFingerprintSearchRepository.findAll()
                ).toList();
                SimilarityDocumentFingerprint testSimilarityDocumentFingerprintSearch = similarityDocumentFingerprintSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertSimilarityDocumentFingerprintAllPropertiesEquals(
                    testSimilarityDocumentFingerprintSearch,
                    updatedSimilarityDocumentFingerprint
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityDocumentFingerprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSimilarityDocumentFingerprintWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityDocumentFingerprint using partial update
        SimilarityDocumentFingerprint partialUpdatedSimilarityDocumentFingerprint = new SimilarityDocumentFingerprint();
        partialUpdatedSimilarityDocumentFingerprint.setId(similarityDocumentFingerprint.getId());

        partialUpdatedSimilarityDocumentFingerprint
            .fingerprint(UPDATED_FINGERPRINT)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSimilarityDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityDocumentFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityDocumentFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentFingerprint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityDocumentFingerprintUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSimilarityDocumentFingerprint, similarityDocumentFingerprint),
            getPersistedSimilarityDocumentFingerprint(similarityDocumentFingerprint)
        );
    }

    @Test
    @Transactional
    void fullUpdateSimilarityDocumentFingerprintWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityDocumentFingerprint using partial update
        SimilarityDocumentFingerprint partialUpdatedSimilarityDocumentFingerprint = new SimilarityDocumentFingerprint();
        partialUpdatedSimilarityDocumentFingerprint.setId(similarityDocumentFingerprint.getId());

        partialUpdatedSimilarityDocumentFingerprint
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSimilarityDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityDocumentFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityDocumentFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentFingerprint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityDocumentFingerprintUpdatableFieldsEquals(
            partialUpdatedSimilarityDocumentFingerprint,
            getPersistedSimilarityDocumentFingerprint(partialUpdatedSimilarityDocumentFingerprint)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, similarityDocumentFingerprintDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimilarityDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        similarityDocumentFingerprint.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentFingerprint
        SimilarityDocumentFingerprintDTO similarityDocumentFingerprintDTO = similarityDocumentFingerprintMapper.toDto(
            similarityDocumentFingerprint
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentFingerprintDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityDocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSimilarityDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);
        similarityDocumentFingerprintRepository.save(similarityDocumentFingerprint);
        similarityDocumentFingerprintSearchRepository.save(similarityDocumentFingerprint);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the similarityDocumentFingerprint
        restSimilarityDocumentFingerprintMockMvc
            .perform(delete(ENTITY_API_URL_ID, similarityDocumentFingerprint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSimilarityDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentFingerprint = similarityDocumentFingerprintRepository.saveAndFlush(similarityDocumentFingerprint);
        similarityDocumentFingerprintSearchRepository.save(similarityDocumentFingerprint);

        // Search the similarityDocumentFingerprint
        restSimilarityDocumentFingerprintMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + similarityDocumentFingerprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentFingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].fingerprintType").value(hasItem(DEFAULT_FINGERPRINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())))
            .andExpect(jsonPath("$.[*].vectorEmbedding").value(hasItem(DEFAULT_VECTOR_EMBEDDING.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    protected long getRepositoryCount() {
        return similarityDocumentFingerprintRepository.count();
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

    protected SimilarityDocumentFingerprint getPersistedSimilarityDocumentFingerprint(
        SimilarityDocumentFingerprint similarityDocumentFingerprint
    ) {
        return similarityDocumentFingerprintRepository.findById(similarityDocumentFingerprint.getId()).orElseThrow();
    }

    protected void assertPersistedSimilarityDocumentFingerprintToMatchAllProperties(
        SimilarityDocumentFingerprint expectedSimilarityDocumentFingerprint
    ) {
        assertSimilarityDocumentFingerprintAllPropertiesEquals(
            expectedSimilarityDocumentFingerprint,
            getPersistedSimilarityDocumentFingerprint(expectedSimilarityDocumentFingerprint)
        );
    }

    protected void assertPersistedSimilarityDocumentFingerprintToMatchUpdatableProperties(
        SimilarityDocumentFingerprint expectedSimilarityDocumentFingerprint
    ) {
        assertSimilarityDocumentFingerprintAllUpdatablePropertiesEquals(
            expectedSimilarityDocumentFingerprint,
            getPersistedSimilarityDocumentFingerprint(expectedSimilarityDocumentFingerprint)
        );
    }
}
