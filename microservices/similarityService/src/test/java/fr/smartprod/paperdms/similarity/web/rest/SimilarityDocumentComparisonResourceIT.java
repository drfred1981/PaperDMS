package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparisonAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentComparisonRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityDocumentComparisonSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentComparisonDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityDocumentComparisonMapper;
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
 * Integration tests for the {@link SimilarityDocumentComparisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimilarityDocumentComparisonResourceIT {

    private static final String DEFAULT_SOURCE_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_TARGET_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_TARGET_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final Double DEFAULT_SIMILARITY_SCORE = 0D;
    private static final Double UPDATED_SIMILARITY_SCORE = 1D;
    private static final Double SMALLER_SIMILARITY_SCORE = 0D - 1D;

    private static final SimilarityAlgorithm DEFAULT_ALGORITHM = SimilarityAlgorithm.COSINE;
    private static final SimilarityAlgorithm UPDATED_ALGORITHM = SimilarityAlgorithm.JACCARD;

    private static final String DEFAULT_FEATURES = "AAAAAAAAAA";
    private static final String UPDATED_FEATURES = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPUTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPUTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_RELEVANT = false;
    private static final Boolean UPDATED_IS_RELEVANT = true;

    private static final String DEFAULT_REVIEWED_BY = "AAAAAAAAAA";
    private static final String UPDATED_REVIEWED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_REVIEWED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REVIEWED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/similarity-document-comparisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/similarity-document-comparisons/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SimilarityDocumentComparisonRepository similarityDocumentComparisonRepository;

    @Autowired
    private SimilarityDocumentComparisonMapper similarityDocumentComparisonMapper;

    @Autowired
    private SimilarityDocumentComparisonSearchRepository similarityDocumentComparisonSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimilarityDocumentComparisonMockMvc;

    private SimilarityDocumentComparison similarityDocumentComparison;

    private SimilarityDocumentComparison insertedSimilarityDocumentComparison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityDocumentComparison createEntity() {
        return new SimilarityDocumentComparison()
            .sourceDocumentSha256(DEFAULT_SOURCE_DOCUMENT_SHA_256)
            .targetDocumentSha256(DEFAULT_TARGET_DOCUMENT_SHA_256)
            .similarityScore(DEFAULT_SIMILARITY_SCORE)
            .algorithm(DEFAULT_ALGORITHM)
            .features(DEFAULT_FEATURES)
            .computedDate(DEFAULT_COMPUTED_DATE)
            .isRelevant(DEFAULT_IS_RELEVANT)
            .reviewedBy(DEFAULT_REVIEWED_BY)
            .reviewedDate(DEFAULT_REVIEWED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityDocumentComparison createUpdatedEntity() {
        return new SimilarityDocumentComparison()
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .targetDocumentSha256(UPDATED_TARGET_DOCUMENT_SHA_256)
            .similarityScore(UPDATED_SIMILARITY_SCORE)
            .algorithm(UPDATED_ALGORITHM)
            .features(UPDATED_FEATURES)
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);
    }

    @BeforeEach
    void initTest() {
        similarityDocumentComparison = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSimilarityDocumentComparison != null) {
            similarityDocumentComparisonRepository.delete(insertedSimilarityDocumentComparison);
            similarityDocumentComparisonSearchRepository.delete(insertedSimilarityDocumentComparison);
            insertedSimilarityDocumentComparison = null;
        }
    }

    @Test
    @Transactional
    void createSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );
        var returnedSimilarityDocumentComparisonDTO = om.readValue(
            restSimilarityDocumentComparisonMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SimilarityDocumentComparisonDTO.class
        );

        // Validate the SimilarityDocumentComparison in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSimilarityDocumentComparison = similarityDocumentComparisonMapper.toEntity(returnedSimilarityDocumentComparisonDTO);
        assertSimilarityDocumentComparisonUpdatableFieldsEquals(
            returnedSimilarityDocumentComparison,
            getPersistedSimilarityDocumentComparison(returnedSimilarityDocumentComparison)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSimilarityDocumentComparison = returnedSimilarityDocumentComparison;
    }

    @Test
    @Transactional
    void createSimilarityDocumentComparisonWithExistingId() throws Exception {
        // Create the SimilarityDocumentComparison with an existing ID
        similarityDocumentComparison.setId(1L);
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimilarityDocumentComparisonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSourceDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        // set the field null
        similarityDocumentComparison.setSourceDocumentSha256(null);

        // Create the SimilarityDocumentComparison, which fails.
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        restSimilarityDocumentComparisonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTargetDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        // set the field null
        similarityDocumentComparison.setTargetDocumentSha256(null);

        // Create the SimilarityDocumentComparison, which fails.
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        restSimilarityDocumentComparisonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSimilarityScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        // set the field null
        similarityDocumentComparison.setSimilarityScore(null);

        // Create the SimilarityDocumentComparison, which fails.
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        restSimilarityDocumentComparisonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkComputedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        // set the field null
        similarityDocumentComparison.setComputedDate(null);

        // Create the SimilarityDocumentComparison, which fails.
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        restSimilarityDocumentComparisonMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisons() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentSha256").value(hasItem(DEFAULT_SOURCE_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].targetDocumentSha256").value(hasItem(DEFAULT_TARGET_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].similarityScore").value(hasItem(DEFAULT_SIMILARITY_SCORE)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)))
            .andExpect(jsonPath("$.[*].reviewedBy").value(hasItem(DEFAULT_REVIEWED_BY)))
            .andExpect(jsonPath("$.[*].reviewedDate").value(hasItem(DEFAULT_REVIEWED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSimilarityDocumentComparison() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get the similarityDocumentComparison
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL_ID, similarityDocumentComparison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(similarityDocumentComparison.getId().intValue()))
            .andExpect(jsonPath("$.sourceDocumentSha256").value(DEFAULT_SOURCE_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.targetDocumentSha256").value(DEFAULT_TARGET_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.similarityScore").value(DEFAULT_SIMILARITY_SCORE))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
            .andExpect(jsonPath("$.features").value(DEFAULT_FEATURES))
            .andExpect(jsonPath("$.computedDate").value(DEFAULT_COMPUTED_DATE.toString()))
            .andExpect(jsonPath("$.isRelevant").value(DEFAULT_IS_RELEVANT))
            .andExpect(jsonPath("$.reviewedBy").value(DEFAULT_REVIEWED_BY))
            .andExpect(jsonPath("$.reviewedDate").value(DEFAULT_REVIEWED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSimilarityDocumentComparisonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        Long id = similarityDocumentComparison.getId();

        defaultSimilarityDocumentComparisonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSimilarityDocumentComparisonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSimilarityDocumentComparisonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySourceDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where sourceDocumentSha256 equals to
        defaultSimilarityDocumentComparisonFiltering(
            "sourceDocumentSha256.equals=" + DEFAULT_SOURCE_DOCUMENT_SHA_256,
            "sourceDocumentSha256.equals=" + UPDATED_SOURCE_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySourceDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where sourceDocumentSha256 in
        defaultSimilarityDocumentComparisonFiltering(
            "sourceDocumentSha256.in=" + DEFAULT_SOURCE_DOCUMENT_SHA_256 + "," + UPDATED_SOURCE_DOCUMENT_SHA_256,
            "sourceDocumentSha256.in=" + UPDATED_SOURCE_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySourceDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where sourceDocumentSha256 is not null
        defaultSimilarityDocumentComparisonFiltering("sourceDocumentSha256.specified=true", "sourceDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySourceDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where sourceDocumentSha256 contains
        defaultSimilarityDocumentComparisonFiltering(
            "sourceDocumentSha256.contains=" + DEFAULT_SOURCE_DOCUMENT_SHA_256,
            "sourceDocumentSha256.contains=" + UPDATED_SOURCE_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySourceDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where sourceDocumentSha256 does not contain
        defaultSimilarityDocumentComparisonFiltering(
            "sourceDocumentSha256.doesNotContain=" + UPDATED_SOURCE_DOCUMENT_SHA_256,
            "sourceDocumentSha256.doesNotContain=" + DEFAULT_SOURCE_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByTargetDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where targetDocumentSha256 equals to
        defaultSimilarityDocumentComparisonFiltering(
            "targetDocumentSha256.equals=" + DEFAULT_TARGET_DOCUMENT_SHA_256,
            "targetDocumentSha256.equals=" + UPDATED_TARGET_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByTargetDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where targetDocumentSha256 in
        defaultSimilarityDocumentComparisonFiltering(
            "targetDocumentSha256.in=" + DEFAULT_TARGET_DOCUMENT_SHA_256 + "," + UPDATED_TARGET_DOCUMENT_SHA_256,
            "targetDocumentSha256.in=" + UPDATED_TARGET_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByTargetDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where targetDocumentSha256 is not null
        defaultSimilarityDocumentComparisonFiltering("targetDocumentSha256.specified=true", "targetDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByTargetDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where targetDocumentSha256 contains
        defaultSimilarityDocumentComparisonFiltering(
            "targetDocumentSha256.contains=" + DEFAULT_TARGET_DOCUMENT_SHA_256,
            "targetDocumentSha256.contains=" + UPDATED_TARGET_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByTargetDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where targetDocumentSha256 does not contain
        defaultSimilarityDocumentComparisonFiltering(
            "targetDocumentSha256.doesNotContain=" + UPDATED_TARGET_DOCUMENT_SHA_256,
            "targetDocumentSha256.doesNotContain=" + DEFAULT_TARGET_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore equals to
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.equals=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.equals=" + UPDATED_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore in
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.in=" + DEFAULT_SIMILARITY_SCORE + "," + UPDATED_SIMILARITY_SCORE,
            "similarityScore.in=" + UPDATED_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore is not null
        defaultSimilarityDocumentComparisonFiltering("similarityScore.specified=true", "similarityScore.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore is greater than or equal to
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.greaterThanOrEqual=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.greaterThanOrEqual=" + (DEFAULT_SIMILARITY_SCORE + 1)
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore is less than or equal to
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.lessThanOrEqual=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.lessThanOrEqual=" + SMALLER_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore is less than
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.lessThan=" + (DEFAULT_SIMILARITY_SCORE + 1),
            "similarityScore.lessThan=" + DEFAULT_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsBySimilarityScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where similarityScore is greater than
        defaultSimilarityDocumentComparisonFiltering(
            "similarityScore.greaterThan=" + SMALLER_SIMILARITY_SCORE,
            "similarityScore.greaterThan=" + DEFAULT_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where algorithm equals to
        defaultSimilarityDocumentComparisonFiltering("algorithm.equals=" + DEFAULT_ALGORITHM, "algorithm.equals=" + UPDATED_ALGORITHM);
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByAlgorithmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where algorithm in
        defaultSimilarityDocumentComparisonFiltering(
            "algorithm.in=" + DEFAULT_ALGORITHM + "," + UPDATED_ALGORITHM,
            "algorithm.in=" + UPDATED_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByAlgorithmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where algorithm is not null
        defaultSimilarityDocumentComparisonFiltering("algorithm.specified=true", "algorithm.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByComputedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where computedDate equals to
        defaultSimilarityDocumentComparisonFiltering(
            "computedDate.equals=" + DEFAULT_COMPUTED_DATE,
            "computedDate.equals=" + UPDATED_COMPUTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByComputedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where computedDate in
        defaultSimilarityDocumentComparisonFiltering(
            "computedDate.in=" + DEFAULT_COMPUTED_DATE + "," + UPDATED_COMPUTED_DATE,
            "computedDate.in=" + UPDATED_COMPUTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByComputedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where computedDate is not null
        defaultSimilarityDocumentComparisonFiltering("computedDate.specified=true", "computedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByIsRelevantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where isRelevant equals to
        defaultSimilarityDocumentComparisonFiltering(
            "isRelevant.equals=" + DEFAULT_IS_RELEVANT,
            "isRelevant.equals=" + UPDATED_IS_RELEVANT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByIsRelevantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where isRelevant in
        defaultSimilarityDocumentComparisonFiltering(
            "isRelevant.in=" + DEFAULT_IS_RELEVANT + "," + UPDATED_IS_RELEVANT,
            "isRelevant.in=" + UPDATED_IS_RELEVANT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByIsRelevantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where isRelevant is not null
        defaultSimilarityDocumentComparisonFiltering("isRelevant.specified=true", "isRelevant.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedBy equals to
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedBy.equals=" + DEFAULT_REVIEWED_BY,
            "reviewedBy.equals=" + UPDATED_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedBy in
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedBy.in=" + DEFAULT_REVIEWED_BY + "," + UPDATED_REVIEWED_BY,
            "reviewedBy.in=" + UPDATED_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedBy is not null
        defaultSimilarityDocumentComparisonFiltering("reviewedBy.specified=true", "reviewedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedByContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedBy contains
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedBy.contains=" + DEFAULT_REVIEWED_BY,
            "reviewedBy.contains=" + UPDATED_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedBy does not contain
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedBy.doesNotContain=" + UPDATED_REVIEWED_BY,
            "reviewedBy.doesNotContain=" + DEFAULT_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedDate equals to
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedDate.equals=" + DEFAULT_REVIEWED_DATE,
            "reviewedDate.equals=" + UPDATED_REVIEWED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedDate in
        defaultSimilarityDocumentComparisonFiltering(
            "reviewedDate.in=" + DEFAULT_REVIEWED_DATE + "," + UPDATED_REVIEWED_DATE,
            "reviewedDate.in=" + UPDATED_REVIEWED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByReviewedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        // Get all the similarityDocumentComparisonList where reviewedDate is not null
        defaultSimilarityDocumentComparisonFiltering("reviewedDate.specified=true", "reviewedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityDocumentComparisonsByJobIsEqualToSomething() throws Exception {
        SimilarityJob job;
        if (TestUtil.findAll(em, SimilarityJob.class).isEmpty()) {
            similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);
            job = SimilarityJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, SimilarityJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        similarityDocumentComparison.setJob(job);
        similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);
        Long jobId = job.getId();
        // Get all the similarityDocumentComparisonList where job equals to jobId
        defaultSimilarityDocumentComparisonShouldBeFound("jobId.equals=" + jobId);

        // Get all the similarityDocumentComparisonList where job equals to (jobId + 1)
        defaultSimilarityDocumentComparisonShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultSimilarityDocumentComparisonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSimilarityDocumentComparisonShouldBeFound(shouldBeFound);
        defaultSimilarityDocumentComparisonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimilarityDocumentComparisonShouldBeFound(String filter) throws Exception {
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentSha256").value(hasItem(DEFAULT_SOURCE_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].targetDocumentSha256").value(hasItem(DEFAULT_TARGET_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].similarityScore").value(hasItem(DEFAULT_SIMILARITY_SCORE)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)))
            .andExpect(jsonPath("$.[*].reviewedBy").value(hasItem(DEFAULT_REVIEWED_BY)))
            .andExpect(jsonPath("$.[*].reviewedDate").value(hasItem(DEFAULT_REVIEWED_DATE.toString())));

        // Check, that the count call also returns 1
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimilarityDocumentComparisonShouldNotBeFound(String filter) throws Exception {
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimilarityDocumentComparison() throws Exception {
        // Get the similarityDocumentComparison
        restSimilarityDocumentComparisonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimilarityDocumentComparison() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityDocumentComparisonSearchRepository.save(similarityDocumentComparison);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());

        // Update the similarityDocumentComparison
        SimilarityDocumentComparison updatedSimilarityDocumentComparison = similarityDocumentComparisonRepository
            .findById(similarityDocumentComparison.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedSimilarityDocumentComparison are not directly saved in db
        em.detach(updatedSimilarityDocumentComparison);
        updatedSimilarityDocumentComparison
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .targetDocumentSha256(UPDATED_TARGET_DOCUMENT_SHA_256)
            .similarityScore(UPDATED_SIMILARITY_SCORE)
            .algorithm(UPDATED_ALGORITHM)
            .features(UPDATED_FEATURES)
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            updatedSimilarityDocumentComparison
        );

        restSimilarityDocumentComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityDocumentComparisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSimilarityDocumentComparisonToMatchAllProperties(updatedSimilarityDocumentComparison);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SimilarityDocumentComparison> similarityDocumentComparisonSearchList = Streamable.of(
                    similarityDocumentComparisonSearchRepository.findAll()
                ).toList();
                SimilarityDocumentComparison testSimilarityDocumentComparisonSearch = similarityDocumentComparisonSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertSimilarityDocumentComparisonAllPropertiesEquals(
                    testSimilarityDocumentComparisonSearch,
                    updatedSimilarityDocumentComparison
                );
            });
    }

    @Test
    @Transactional
    void putNonExistingSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityDocumentComparisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSimilarityDocumentComparisonWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityDocumentComparison using partial update
        SimilarityDocumentComparison partialUpdatedSimilarityDocumentComparison = new SimilarityDocumentComparison();
        partialUpdatedSimilarityDocumentComparison.setId(similarityDocumentComparison.getId());

        partialUpdatedSimilarityDocumentComparison
            .targetDocumentSha256(UPDATED_TARGET_DOCUMENT_SHA_256)
            .algorithm(UPDATED_ALGORITHM)
            .computedDate(UPDATED_COMPUTED_DATE);

        restSimilarityDocumentComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityDocumentComparison.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityDocumentComparison))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentComparison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityDocumentComparisonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSimilarityDocumentComparison, similarityDocumentComparison),
            getPersistedSimilarityDocumentComparison(similarityDocumentComparison)
        );
    }

    @Test
    @Transactional
    void fullUpdateSimilarityDocumentComparisonWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityDocumentComparison using partial update
        SimilarityDocumentComparison partialUpdatedSimilarityDocumentComparison = new SimilarityDocumentComparison();
        partialUpdatedSimilarityDocumentComparison.setId(similarityDocumentComparison.getId());

        partialUpdatedSimilarityDocumentComparison
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .targetDocumentSha256(UPDATED_TARGET_DOCUMENT_SHA_256)
            .similarityScore(UPDATED_SIMILARITY_SCORE)
            .algorithm(UPDATED_ALGORITHM)
            .features(UPDATED_FEATURES)
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);

        restSimilarityDocumentComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityDocumentComparison.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityDocumentComparison))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityDocumentComparison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityDocumentComparisonUpdatableFieldsEquals(
            partialUpdatedSimilarityDocumentComparison,
            getPersistedSimilarityDocumentComparison(partialUpdatedSimilarityDocumentComparison)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, similarityDocumentComparisonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimilarityDocumentComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        similarityDocumentComparison.setId(longCount.incrementAndGet());

        // Create the SimilarityDocumentComparison
        SimilarityDocumentComparisonDTO similarityDocumentComparisonDTO = similarityDocumentComparisonMapper.toDto(
            similarityDocumentComparison
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityDocumentComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityDocumentComparisonDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityDocumentComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSimilarityDocumentComparison() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);
        similarityDocumentComparisonRepository.save(similarityDocumentComparison);
        similarityDocumentComparisonSearchRepository.save(similarityDocumentComparison);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the similarityDocumentComparison
        restSimilarityDocumentComparisonMockMvc
            .perform(delete(ENTITY_API_URL_ID, similarityDocumentComparison.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityDocumentComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSimilarityDocumentComparison() throws Exception {
        // Initialize the database
        insertedSimilarityDocumentComparison = similarityDocumentComparisonRepository.saveAndFlush(similarityDocumentComparison);
        similarityDocumentComparisonSearchRepository.save(similarityDocumentComparison);

        // Search the similarityDocumentComparison
        restSimilarityDocumentComparisonMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + similarityDocumentComparison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityDocumentComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentSha256").value(hasItem(DEFAULT_SOURCE_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].targetDocumentSha256").value(hasItem(DEFAULT_TARGET_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].similarityScore").value(hasItem(DEFAULT_SIMILARITY_SCORE)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES.toString())))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)))
            .andExpect(jsonPath("$.[*].reviewedBy").value(hasItem(DEFAULT_REVIEWED_BY)))
            .andExpect(jsonPath("$.[*].reviewedDate").value(hasItem(DEFAULT_REVIEWED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return similarityDocumentComparisonRepository.count();
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

    protected SimilarityDocumentComparison getPersistedSimilarityDocumentComparison(
        SimilarityDocumentComparison similarityDocumentComparison
    ) {
        return similarityDocumentComparisonRepository.findById(similarityDocumentComparison.getId()).orElseThrow();
    }

    protected void assertPersistedSimilarityDocumentComparisonToMatchAllProperties(
        SimilarityDocumentComparison expectedSimilarityDocumentComparison
    ) {
        assertSimilarityDocumentComparisonAllPropertiesEquals(
            expectedSimilarityDocumentComparison,
            getPersistedSimilarityDocumentComparison(expectedSimilarityDocumentComparison)
        );
    }

    protected void assertPersistedSimilarityDocumentComparisonToMatchUpdatableProperties(
        SimilarityDocumentComparison expectedSimilarityDocumentComparison
    ) {
        assertSimilarityDocumentComparisonAllUpdatablePropertiesEquals(
            expectedSimilarityDocumentComparison,
            getPersistedSimilarityDocumentComparison(expectedSimilarityDocumentComparison)
        );
    }
}
