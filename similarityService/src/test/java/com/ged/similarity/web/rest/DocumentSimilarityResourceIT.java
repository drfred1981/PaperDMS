package com.ged.similarity.web.rest;

import static com.ged.similarity.domain.DocumentSimilarityAsserts.*;
import static com.ged.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.similarity.IntegrationTest;
import com.ged.similarity.domain.DocumentSimilarity;
import com.ged.similarity.domain.SimilarityJob;
import com.ged.similarity.domain.enumeration.SimilarityAlgorithm;
import com.ged.similarity.repository.DocumentSimilarityRepository;
import com.ged.similarity.service.dto.DocumentSimilarityDTO;
import com.ged.similarity.service.mapper.DocumentSimilarityMapper;
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
 * Integration tests for the {@link DocumentSimilarityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentSimilarityResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID_1 = 1L;
    private static final Long UPDATED_DOCUMENT_ID_1 = 2L;
    private static final Long SMALLER_DOCUMENT_ID_1 = 1L - 1L;

    private static final Long DEFAULT_DOCUMENT_ID_2 = 1L;
    private static final Long UPDATED_DOCUMENT_ID_2 = 2L;
    private static final Long SMALLER_DOCUMENT_ID_2 = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/document-similarities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentSimilarityRepository documentSimilarityRepository;

    @Autowired
    private DocumentSimilarityMapper documentSimilarityMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentSimilarityMockMvc;

    private DocumentSimilarity documentSimilarity;

    private DocumentSimilarity insertedDocumentSimilarity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentSimilarity createEntity() {
        return new DocumentSimilarity()
            .documentId1(DEFAULT_DOCUMENT_ID_1)
            .documentId2(DEFAULT_DOCUMENT_ID_2)
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
    public static DocumentSimilarity createUpdatedEntity() {
        return new DocumentSimilarity()
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
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
        documentSimilarity = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentSimilarity != null) {
            documentSimilarityRepository.delete(insertedDocumentSimilarity);
            insertedDocumentSimilarity = null;
        }
    }

    @Test
    @Transactional
    void createDocumentSimilarity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);
        var returnedDocumentSimilarityDTO = om.readValue(
            restDocumentSimilarityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentSimilarityDTO.class
        );

        // Validate the DocumentSimilarity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentSimilarity = documentSimilarityMapper.toEntity(returnedDocumentSimilarityDTO);
        assertDocumentSimilarityUpdatableFieldsEquals(
            returnedDocumentSimilarity,
            getPersistedDocumentSimilarity(returnedDocumentSimilarity)
        );

        insertedDocumentSimilarity = returnedDocumentSimilarity;
    }

    @Test
    @Transactional
    void createDocumentSimilarityWithExistingId() throws Exception {
        // Create the DocumentSimilarity with an existing ID
        documentSimilarity.setId(1L);
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentId1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentSimilarity.setDocumentId1(null);

        // Create the DocumentSimilarity, which fails.
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentId2IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentSimilarity.setDocumentId2(null);

        // Create the DocumentSimilarity, which fails.
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSimilarityScoreIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentSimilarity.setSimilarityScore(null);

        // Create the DocumentSimilarity, which fails.
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAlgorithmIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentSimilarity.setAlgorithm(null);

        // Create the DocumentSimilarity, which fails.
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComputedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentSimilarity.setComputedDate(null);

        // Create the DocumentSimilarity, which fails.
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentSimilarities() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentSimilarity.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId1").value(hasItem(DEFAULT_DOCUMENT_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].documentId2").value(hasItem(DEFAULT_DOCUMENT_ID_2.intValue())))
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
    void getDocumentSimilarity() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get the documentSimilarity
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL_ID, documentSimilarity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentSimilarity.getId().intValue()))
            .andExpect(jsonPath("$.documentId1").value(DEFAULT_DOCUMENT_ID_1.intValue()))
            .andExpect(jsonPath("$.documentId2").value(DEFAULT_DOCUMENT_ID_2.intValue()))
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
    void getDocumentSimilaritiesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        Long id = documentSimilarity.getId();

        defaultDocumentSimilarityFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentSimilarityFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentSimilarityFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 equals to
        defaultDocumentSimilarityFiltering("documentId1.equals=" + DEFAULT_DOCUMENT_ID_1, "documentId1.equals=" + UPDATED_DOCUMENT_ID_1);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 in
        defaultDocumentSimilarityFiltering(
            "documentId1.in=" + DEFAULT_DOCUMENT_ID_1 + "," + UPDATED_DOCUMENT_ID_1,
            "documentId1.in=" + UPDATED_DOCUMENT_ID_1
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 is not null
        defaultDocumentSimilarityFiltering("documentId1.specified=true", "documentId1.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 is greater than or equal to
        defaultDocumentSimilarityFiltering(
            "documentId1.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID_1,
            "documentId1.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID_1
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 is less than or equal to
        defaultDocumentSimilarityFiltering(
            "documentId1.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID_1,
            "documentId1.lessThanOrEqual=" + SMALLER_DOCUMENT_ID_1
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 is less than
        defaultDocumentSimilarityFiltering(
            "documentId1.lessThan=" + UPDATED_DOCUMENT_ID_1,
            "documentId1.lessThan=" + DEFAULT_DOCUMENT_ID_1
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId1IsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId1 is greater than
        defaultDocumentSimilarityFiltering(
            "documentId1.greaterThan=" + SMALLER_DOCUMENT_ID_1,
            "documentId1.greaterThan=" + DEFAULT_DOCUMENT_ID_1
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 equals to
        defaultDocumentSimilarityFiltering("documentId2.equals=" + DEFAULT_DOCUMENT_ID_2, "documentId2.equals=" + UPDATED_DOCUMENT_ID_2);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 in
        defaultDocumentSimilarityFiltering(
            "documentId2.in=" + DEFAULT_DOCUMENT_ID_2 + "," + UPDATED_DOCUMENT_ID_2,
            "documentId2.in=" + UPDATED_DOCUMENT_ID_2
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 is not null
        defaultDocumentSimilarityFiltering("documentId2.specified=true", "documentId2.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 is greater than or equal to
        defaultDocumentSimilarityFiltering(
            "documentId2.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID_2,
            "documentId2.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID_2
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 is less than or equal to
        defaultDocumentSimilarityFiltering(
            "documentId2.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID_2,
            "documentId2.lessThanOrEqual=" + SMALLER_DOCUMENT_ID_2
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 is less than
        defaultDocumentSimilarityFiltering(
            "documentId2.lessThan=" + UPDATED_DOCUMENT_ID_2,
            "documentId2.lessThan=" + DEFAULT_DOCUMENT_ID_2
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByDocumentId2IsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where documentId2 is greater than
        defaultDocumentSimilarityFiltering(
            "documentId2.greaterThan=" + SMALLER_DOCUMENT_ID_2,
            "documentId2.greaterThan=" + DEFAULT_DOCUMENT_ID_2
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore equals to
        defaultDocumentSimilarityFiltering(
            "similarityScore.equals=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.equals=" + UPDATED_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore in
        defaultDocumentSimilarityFiltering(
            "similarityScore.in=" + DEFAULT_SIMILARITY_SCORE + "," + UPDATED_SIMILARITY_SCORE,
            "similarityScore.in=" + UPDATED_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore is not null
        defaultDocumentSimilarityFiltering("similarityScore.specified=true", "similarityScore.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore is greater than or equal to
        defaultDocumentSimilarityFiltering(
            "similarityScore.greaterThanOrEqual=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.greaterThanOrEqual=" + (DEFAULT_SIMILARITY_SCORE + 1)
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore is less than or equal to
        defaultDocumentSimilarityFiltering(
            "similarityScore.lessThanOrEqual=" + DEFAULT_SIMILARITY_SCORE,
            "similarityScore.lessThanOrEqual=" + SMALLER_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore is less than
        defaultDocumentSimilarityFiltering(
            "similarityScore.lessThan=" + (DEFAULT_SIMILARITY_SCORE + 1),
            "similarityScore.lessThan=" + DEFAULT_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesBySimilarityScoreIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where similarityScore is greater than
        defaultDocumentSimilarityFiltering(
            "similarityScore.greaterThan=" + SMALLER_SIMILARITY_SCORE,
            "similarityScore.greaterThan=" + DEFAULT_SIMILARITY_SCORE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByAlgorithmIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where algorithm equals to
        defaultDocumentSimilarityFiltering("algorithm.equals=" + DEFAULT_ALGORITHM, "algorithm.equals=" + UPDATED_ALGORITHM);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByAlgorithmIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where algorithm in
        defaultDocumentSimilarityFiltering(
            "algorithm.in=" + DEFAULT_ALGORITHM + "," + UPDATED_ALGORITHM,
            "algorithm.in=" + UPDATED_ALGORITHM
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByAlgorithmIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where algorithm is not null
        defaultDocumentSimilarityFiltering("algorithm.specified=true", "algorithm.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByComputedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where computedDate equals to
        defaultDocumentSimilarityFiltering("computedDate.equals=" + DEFAULT_COMPUTED_DATE, "computedDate.equals=" + UPDATED_COMPUTED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByComputedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where computedDate in
        defaultDocumentSimilarityFiltering(
            "computedDate.in=" + DEFAULT_COMPUTED_DATE + "," + UPDATED_COMPUTED_DATE,
            "computedDate.in=" + UPDATED_COMPUTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByComputedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where computedDate is not null
        defaultDocumentSimilarityFiltering("computedDate.specified=true", "computedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByIsRelevantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where isRelevant equals to
        defaultDocumentSimilarityFiltering("isRelevant.equals=" + DEFAULT_IS_RELEVANT, "isRelevant.equals=" + UPDATED_IS_RELEVANT);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByIsRelevantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where isRelevant in
        defaultDocumentSimilarityFiltering(
            "isRelevant.in=" + DEFAULT_IS_RELEVANT + "," + UPDATED_IS_RELEVANT,
            "isRelevant.in=" + UPDATED_IS_RELEVANT
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByIsRelevantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where isRelevant is not null
        defaultDocumentSimilarityFiltering("isRelevant.specified=true", "isRelevant.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedBy equals to
        defaultDocumentSimilarityFiltering("reviewedBy.equals=" + DEFAULT_REVIEWED_BY, "reviewedBy.equals=" + UPDATED_REVIEWED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedBy in
        defaultDocumentSimilarityFiltering(
            "reviewedBy.in=" + DEFAULT_REVIEWED_BY + "," + UPDATED_REVIEWED_BY,
            "reviewedBy.in=" + UPDATED_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedBy is not null
        defaultDocumentSimilarityFiltering("reviewedBy.specified=true", "reviewedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedBy contains
        defaultDocumentSimilarityFiltering("reviewedBy.contains=" + DEFAULT_REVIEWED_BY, "reviewedBy.contains=" + UPDATED_REVIEWED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedBy does not contain
        defaultDocumentSimilarityFiltering(
            "reviewedBy.doesNotContain=" + UPDATED_REVIEWED_BY,
            "reviewedBy.doesNotContain=" + DEFAULT_REVIEWED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedDate equals to
        defaultDocumentSimilarityFiltering("reviewedDate.equals=" + DEFAULT_REVIEWED_DATE, "reviewedDate.equals=" + UPDATED_REVIEWED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedDate in
        defaultDocumentSimilarityFiltering(
            "reviewedDate.in=" + DEFAULT_REVIEWED_DATE + "," + UPDATED_REVIEWED_DATE,
            "reviewedDate.in=" + UPDATED_REVIEWED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByReviewedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        // Get all the documentSimilarityList where reviewedDate is not null
        defaultDocumentSimilarityFiltering("reviewedDate.specified=true", "reviewedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentSimilaritiesByJobIsEqualToSomething() throws Exception {
        SimilarityJob job;
        if (TestUtil.findAll(em, SimilarityJob.class).isEmpty()) {
            documentSimilarityRepository.saveAndFlush(documentSimilarity);
            job = SimilarityJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, SimilarityJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        documentSimilarity.setJob(job);
        documentSimilarityRepository.saveAndFlush(documentSimilarity);
        Long jobId = job.getId();
        // Get all the documentSimilarityList where job equals to jobId
        defaultDocumentSimilarityShouldBeFound("jobId.equals=" + jobId);

        // Get all the documentSimilarityList where job equals to (jobId + 1)
        defaultDocumentSimilarityShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultDocumentSimilarityFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentSimilarityShouldBeFound(shouldBeFound);
        defaultDocumentSimilarityShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentSimilarityShouldBeFound(String filter) throws Exception {
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentSimilarity.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId1").value(hasItem(DEFAULT_DOCUMENT_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].documentId2").value(hasItem(DEFAULT_DOCUMENT_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].similarityScore").value(hasItem(DEFAULT_SIMILARITY_SCORE)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].features").value(hasItem(DEFAULT_FEATURES)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)))
            .andExpect(jsonPath("$.[*].reviewedBy").value(hasItem(DEFAULT_REVIEWED_BY)))
            .andExpect(jsonPath("$.[*].reviewedDate").value(hasItem(DEFAULT_REVIEWED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentSimilarityShouldNotBeFound(String filter) throws Exception {
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentSimilarityMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentSimilarity() throws Exception {
        // Get the documentSimilarity
        restDocumentSimilarityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentSimilarity() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSimilarity
        DocumentSimilarity updatedDocumentSimilarity = documentSimilarityRepository.findById(documentSimilarity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentSimilarity are not directly saved in db
        em.detach(updatedDocumentSimilarity);
        updatedDocumentSimilarity
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
            .similarityScore(UPDATED_SIMILARITY_SCORE)
            .algorithm(UPDATED_ALGORITHM)
            .features(UPDATED_FEATURES)
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(updatedDocumentSimilarity);

        restDocumentSimilarityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentSimilarityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentSimilarityDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentSimilarityToMatchAllProperties(updatedDocumentSimilarity);
    }

    @Test
    @Transactional
    void putNonExistingDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentSimilarityDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentSimilarityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentSimilarityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentSimilarityWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSimilarity using partial update
        DocumentSimilarity partialUpdatedDocumentSimilarity = new DocumentSimilarity();
        partialUpdatedDocumentSimilarity.setId(documentSimilarity.getId());

        partialUpdatedDocumentSimilarity
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);

        restDocumentSimilarityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSimilarity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentSimilarity))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSimilarity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentSimilarityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentSimilarity, documentSimilarity),
            getPersistedDocumentSimilarity(documentSimilarity)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentSimilarityWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentSimilarity using partial update
        DocumentSimilarity partialUpdatedDocumentSimilarity = new DocumentSimilarity();
        partialUpdatedDocumentSimilarity.setId(documentSimilarity.getId());

        partialUpdatedDocumentSimilarity
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
            .similarityScore(UPDATED_SIMILARITY_SCORE)
            .algorithm(UPDATED_ALGORITHM)
            .features(UPDATED_FEATURES)
            .computedDate(UPDATED_COMPUTED_DATE)
            .isRelevant(UPDATED_IS_RELEVANT)
            .reviewedBy(UPDATED_REVIEWED_BY)
            .reviewedDate(UPDATED_REVIEWED_DATE);

        restDocumentSimilarityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentSimilarity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentSimilarity))
            )
            .andExpect(status().isOk());

        // Validate the DocumentSimilarity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentSimilarityUpdatableFieldsEquals(
            partialUpdatedDocumentSimilarity,
            getPersistedDocumentSimilarity(partialUpdatedDocumentSimilarity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentSimilarityDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentSimilarityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentSimilarityDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentSimilarity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSimilarity.setId(longCount.incrementAndGet());

        // Create the DocumentSimilarity
        DocumentSimilarityDTO documentSimilarityDTO = documentSimilarityMapper.toDto(documentSimilarity);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentSimilarityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentSimilarityDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentSimilarity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentSimilarity() throws Exception {
        // Initialize the database
        insertedDocumentSimilarity = documentSimilarityRepository.saveAndFlush(documentSimilarity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentSimilarity
        restDocumentSimilarityMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentSimilarity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentSimilarityRepository.count();
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

    protected DocumentSimilarity getPersistedDocumentSimilarity(DocumentSimilarity documentSimilarity) {
        return documentSimilarityRepository.findById(documentSimilarity.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentSimilarityToMatchAllProperties(DocumentSimilarity expectedDocumentSimilarity) {
        assertDocumentSimilarityAllPropertiesEquals(expectedDocumentSimilarity, getPersistedDocumentSimilarity(expectedDocumentSimilarity));
    }

    protected void assertPersistedDocumentSimilarityToMatchUpdatableProperties(DocumentSimilarity expectedDocumentSimilarity) {
        assertDocumentSimilarityAllUpdatablePropertiesEquals(
            expectedDocumentSimilarity,
            getPersistedDocumentSimilarity(expectedDocumentSimilarity)
        );
    }
}
