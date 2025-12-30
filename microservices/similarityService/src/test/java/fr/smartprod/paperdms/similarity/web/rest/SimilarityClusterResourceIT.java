package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.SimilarityClusterAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
import fr.smartprod.paperdms.similarity.repository.search.SimilarityClusterSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityClusterMapper;
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
 * Integration tests for the {@link SimilarityClusterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimilarityClusterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final SimilarityAlgorithm DEFAULT_ALGORITHM = SimilarityAlgorithm.COSINE;
    private static final SimilarityAlgorithm UPDATED_ALGORITHM = SimilarityAlgorithm.JACCARD;

    private static final String DEFAULT_CENTROID = "AAAAAAAAAA";
    private static final String UPDATED_CENTROID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOCUMENT_COUNT = 1;
    private static final Integer UPDATED_DOCUMENT_COUNT = 2;
    private static final Integer SMALLER_DOCUMENT_COUNT = 1 - 1;

    private static final Double DEFAULT_AVG_SIMILARITY = 0D;
    private static final Double UPDATED_AVG_SIMILARITY = 1D;
    private static final Double SMALLER_AVG_SIMILARITY = 0D - 1D;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/similarity-clusters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/similarity-clusters/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SimilarityClusterRepository similarityClusterRepository;

    @Autowired
    private SimilarityClusterMapper similarityClusterMapper;

    @Autowired
    private SimilarityClusterSearchRepository similarityClusterSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimilarityClusterMockMvc;

    private SimilarityCluster similarityCluster;

    private SimilarityCluster insertedSimilarityCluster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityCluster createEntity() {
        return new SimilarityCluster()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .algorithm(DEFAULT_ALGORITHM)
            .centroid(DEFAULT_CENTROID)
            .documentCount(DEFAULT_DOCUMENT_COUNT)
            .avgSimilarity(DEFAULT_AVG_SIMILARITY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityCluster createUpdatedEntity() {
        return new SimilarityCluster()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        similarityCluster = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSimilarityCluster != null) {
            similarityClusterRepository.delete(insertedSimilarityCluster);
            similarityClusterSearchRepository.delete(insertedSimilarityCluster);
            insertedSimilarityCluster = null;
        }
    }

    @Test
    @Transactional
    void createSimilarityCluster() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);
        var returnedSimilarityClusterDTO = om.readValue(
            restSimilarityClusterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SimilarityClusterDTO.class
        );

        // Validate the SimilarityCluster in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSimilarityCluster = similarityClusterMapper.toEntity(returnedSimilarityClusterDTO);
        assertSimilarityClusterUpdatableFieldsEquals(returnedSimilarityCluster, getPersistedSimilarityCluster(returnedSimilarityCluster));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSimilarityCluster = returnedSimilarityCluster;
    }

    @Test
    @Transactional
    void createSimilarityClusterWithExistingId() throws Exception {
        // Create the SimilarityCluster with an existing ID
        similarityCluster.setId(1L);
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimilarityClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        // set the field null
        similarityCluster.setCreatedDate(null);

        // Create the SimilarityCluster, which fails.
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        restSimilarityClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSimilarityClusters() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityCluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].centroid").value(hasItem(DEFAULT_CENTROID)))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].avgSimilarity").value(hasItem(DEFAULT_AVG_SIMILARITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get the similarityCluster
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL_ID, similarityCluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(similarityCluster.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
            .andExpect(jsonPath("$.centroid").value(DEFAULT_CENTROID))
            .andExpect(jsonPath("$.documentCount").value(DEFAULT_DOCUMENT_COUNT))
            .andExpect(jsonPath("$.avgSimilarity").value(DEFAULT_AVG_SIMILARITY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getSimilarityClustersByIdFiltering() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        Long id = similarityCluster.getId();

        defaultSimilarityClusterFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSimilarityClusterFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSimilarityClusterFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where name equals to
        defaultSimilarityClusterFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where name in
        defaultSimilarityClusterFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where name is not null
        defaultSimilarityClusterFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where name contains
        defaultSimilarityClusterFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where name does not contain
        defaultSimilarityClusterFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where algorithm equals to
        defaultSimilarityClusterFiltering("algorithm.equals=" + DEFAULT_ALGORITHM, "algorithm.equals=" + UPDATED_ALGORITHM);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAlgorithmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where algorithm in
        defaultSimilarityClusterFiltering(
            "algorithm.in=" + DEFAULT_ALGORITHM + "," + UPDATED_ALGORITHM,
            "algorithm.in=" + UPDATED_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAlgorithmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where algorithm is not null
        defaultSimilarityClusterFiltering("algorithm.specified=true", "algorithm.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount equals to
        defaultSimilarityClusterFiltering(
            "documentCount.equals=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.equals=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount in
        defaultSimilarityClusterFiltering(
            "documentCount.in=" + DEFAULT_DOCUMENT_COUNT + "," + UPDATED_DOCUMENT_COUNT,
            "documentCount.in=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount is not null
        defaultSimilarityClusterFiltering("documentCount.specified=true", "documentCount.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount is greater than or equal to
        defaultSimilarityClusterFiltering(
            "documentCount.greaterThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.greaterThanOrEqual=" + UPDATED_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount is less than or equal to
        defaultSimilarityClusterFiltering(
            "documentCount.lessThanOrEqual=" + DEFAULT_DOCUMENT_COUNT,
            "documentCount.lessThanOrEqual=" + SMALLER_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount is less than
        defaultSimilarityClusterFiltering(
            "documentCount.lessThan=" + UPDATED_DOCUMENT_COUNT,
            "documentCount.lessThan=" + DEFAULT_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByDocumentCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where documentCount is greater than
        defaultSimilarityClusterFiltering(
            "documentCount.greaterThan=" + SMALLER_DOCUMENT_COUNT,
            "documentCount.greaterThan=" + DEFAULT_DOCUMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity equals to
        defaultSimilarityClusterFiltering(
            "avgSimilarity.equals=" + DEFAULT_AVG_SIMILARITY,
            "avgSimilarity.equals=" + UPDATED_AVG_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity in
        defaultSimilarityClusterFiltering(
            "avgSimilarity.in=" + DEFAULT_AVG_SIMILARITY + "," + UPDATED_AVG_SIMILARITY,
            "avgSimilarity.in=" + UPDATED_AVG_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity is not null
        defaultSimilarityClusterFiltering("avgSimilarity.specified=true", "avgSimilarity.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity is greater than or equal to
        defaultSimilarityClusterFiltering(
            "avgSimilarity.greaterThanOrEqual=" + DEFAULT_AVG_SIMILARITY,
            "avgSimilarity.greaterThanOrEqual=" + (DEFAULT_AVG_SIMILARITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity is less than or equal to
        defaultSimilarityClusterFiltering(
            "avgSimilarity.lessThanOrEqual=" + DEFAULT_AVG_SIMILARITY,
            "avgSimilarity.lessThanOrEqual=" + SMALLER_AVG_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity is less than
        defaultSimilarityClusterFiltering(
            "avgSimilarity.lessThan=" + (DEFAULT_AVG_SIMILARITY + 1),
            "avgSimilarity.lessThan=" + DEFAULT_AVG_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByAvgSimilarityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where avgSimilarity is greater than
        defaultSimilarityClusterFiltering(
            "avgSimilarity.greaterThan=" + SMALLER_AVG_SIMILARITY,
            "avgSimilarity.greaterThan=" + DEFAULT_AVG_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where createdDate equals to
        defaultSimilarityClusterFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where createdDate in
        defaultSimilarityClusterFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where createdDate is not null
        defaultSimilarityClusterFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where lastUpdated equals to
        defaultSimilarityClusterFiltering("lastUpdated.equals=" + DEFAULT_LAST_UPDATED, "lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where lastUpdated in
        defaultSimilarityClusterFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllSimilarityClustersByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList where lastUpdated is not null
        defaultSimilarityClusterFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    private void defaultSimilarityClusterFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSimilarityClusterShouldBeFound(shouldBeFound);
        defaultSimilarityClusterShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSimilarityClusterShouldBeFound(String filter) throws Exception {
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityCluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].centroid").value(hasItem(DEFAULT_CENTROID)))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].avgSimilarity").value(hasItem(DEFAULT_AVG_SIMILARITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSimilarityClusterShouldNotBeFound(String filter) throws Exception {
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSimilarityCluster() throws Exception {
        // Get the similarityCluster
        restSimilarityClusterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityClusterSearchRepository.save(similarityCluster);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());

        // Update the similarityCluster
        SimilarityCluster updatedSimilarityCluster = similarityClusterRepository.findById(similarityCluster.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSimilarityCluster are not directly saved in db
        em.detach(updatedSimilarityCluster);
        updatedSimilarityCluster
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(updatedSimilarityCluster);

        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSimilarityClusterToMatchAllProperties(updatedSimilarityCluster);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SimilarityCluster> similarityClusterSearchList = Streamable.of(similarityClusterSearchRepository.findAll()).toList();
                SimilarityCluster testSimilarityClusterSearch = similarityClusterSearchList.get(searchDatabaseSizeAfter - 1);

                assertSimilarityClusterAllPropertiesEquals(testSimilarityClusterSearch, updatedSimilarityCluster);
            });
    }

    @Test
    @Transactional
    void putNonExistingSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSimilarityClusterWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityCluster using partial update
        SimilarityCluster partialUpdatedSimilarityCluster = new SimilarityCluster();
        partialUpdatedSimilarityCluster.setId(similarityCluster.getId());

        partialUpdatedSimilarityCluster
            .description(UPDATED_DESCRIPTION)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityCluster))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityClusterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSimilarityCluster, similarityCluster),
            getPersistedSimilarityCluster(similarityCluster)
        );
    }

    @Test
    @Transactional
    void fullUpdateSimilarityClusterWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityCluster using partial update
        SimilarityCluster partialUpdatedSimilarityCluster = new SimilarityCluster();
        partialUpdatedSimilarityCluster.setId(similarityCluster.getId());

        partialUpdatedSimilarityCluster
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityCluster))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityClusterUpdatableFieldsEquals(
            partialUpdatedSimilarityCluster,
            getPersistedSimilarityCluster(partialUpdatedSimilarityCluster)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);
        similarityClusterRepository.save(similarityCluster);
        similarityClusterSearchRepository.save(similarityCluster);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the similarityCluster
        restSimilarityClusterMockMvc
            .perform(delete(ENTITY_API_URL_ID, similarityCluster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(similarityClusterSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);
        similarityClusterSearchRepository.save(similarityCluster);

        // Search the similarityCluster
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + similarityCluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityCluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].centroid").value(hasItem(DEFAULT_CENTROID.toString())))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].avgSimilarity").value(hasItem(DEFAULT_AVG_SIMILARITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    protected long getRepositoryCount() {
        return similarityClusterRepository.count();
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

    protected SimilarityCluster getPersistedSimilarityCluster(SimilarityCluster similarityCluster) {
        return similarityClusterRepository.findById(similarityCluster.getId()).orElseThrow();
    }

    protected void assertPersistedSimilarityClusterToMatchAllProperties(SimilarityCluster expectedSimilarityCluster) {
        assertSimilarityClusterAllPropertiesEquals(expectedSimilarityCluster, getPersistedSimilarityCluster(expectedSimilarityCluster));
    }

    protected void assertPersistedSimilarityClusterToMatchUpdatableProperties(SimilarityCluster expectedSimilarityCluster) {
        assertSimilarityClusterAllUpdatablePropertiesEquals(
            expectedSimilarityCluster,
            getPersistedSimilarityCluster(expectedSimilarityCluster)
        );
    }
}
