package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SemanticSearchAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SemanticSearch;
import fr.smartprod.paperdms.search.repository.SemanticSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SemanticSearchDTO;
import fr.smartprod.paperdms.search.service.mapper.SemanticSearchMapper;
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
 * Integration tests for the {@link SemanticSearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SemanticSearchResourceIT {

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY_EMBEDDING = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_EMBEDDING = "BBBBBBBBBB";

    private static final String DEFAULT_RESULTS = "AAAAAAAAAA";
    private static final String UPDATED_RESULTS = "BBBBBBBBBB";

    private static final String DEFAULT_RELEVANCE_SCORES = "AAAAAAAAAA";
    private static final String UPDATED_RELEVANCE_SCORES = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_USED = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_USED = "BBBBBBBBBB";

    private static final Long DEFAULT_EXECUTION_TIME = 1L;
    private static final Long UPDATED_EXECUTION_TIME = 2L;
    private static final Long SMALLER_EXECUTION_TIME = 1L - 1L;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_SEARCH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEARCH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/semantic-searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SemanticSearchRepository semanticSearchRepository;

    @Autowired
    private SemanticSearchMapper semanticSearchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSemanticSearchMockMvc;

    private SemanticSearch semanticSearch;

    private SemanticSearch insertedSemanticSearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SemanticSearch createEntity() {
        return new SemanticSearch()
            .query(DEFAULT_QUERY)
            .queryEmbedding(DEFAULT_QUERY_EMBEDDING)
            .results(DEFAULT_RESULTS)
            .relevanceScores(DEFAULT_RELEVANCE_SCORES)
            .modelUsed(DEFAULT_MODEL_USED)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .userId(DEFAULT_USER_ID)
            .searchDate(DEFAULT_SEARCH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SemanticSearch createUpdatedEntity() {
        return new SemanticSearch()
            .query(UPDATED_QUERY)
            .queryEmbedding(UPDATED_QUERY_EMBEDDING)
            .results(UPDATED_RESULTS)
            .relevanceScores(UPDATED_RELEVANCE_SCORES)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);
    }

    @BeforeEach
    void initTest() {
        semanticSearch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSemanticSearch != null) {
            semanticSearchRepository.delete(insertedSemanticSearch);
            insertedSemanticSearch = null;
        }
    }

    @Test
    @Transactional
    void createSemanticSearch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);
        var returnedSemanticSearchDTO = om.readValue(
            restSemanticSearchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semanticSearchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SemanticSearchDTO.class
        );

        // Validate the SemanticSearch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSemanticSearch = semanticSearchMapper.toEntity(returnedSemanticSearchDTO);
        assertSemanticSearchUpdatableFieldsEquals(returnedSemanticSearch, getPersistedSemanticSearch(returnedSemanticSearch));

        insertedSemanticSearch = returnedSemanticSearch;
    }

    @Test
    @Transactional
    void createSemanticSearchWithExistingId() throws Exception {
        // Create the SemanticSearch with an existing ID
        semanticSearch.setId(1L);
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSemanticSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semanticSearchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        semanticSearch.setQuery(null);

        // Create the SemanticSearch, which fails.
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        restSemanticSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semanticSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSearchDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        semanticSearch.setSearchDate(null);

        // Create the SemanticSearch, which fails.
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        restSemanticSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semanticSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSemanticSearches() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semanticSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].queryEmbedding").value(hasItem(DEFAULT_QUERY_EMBEDDING)))
            .andExpect(jsonPath("$.[*].results").value(hasItem(DEFAULT_RESULTS)))
            .andExpect(jsonPath("$.[*].relevanceScores").value(hasItem(DEFAULT_RELEVANCE_SCORES)))
            .andExpect(jsonPath("$.[*].modelUsed").value(hasItem(DEFAULT_MODEL_USED)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())));
    }

    @Test
    @Transactional
    void getSemanticSearch() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get the semanticSearch
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, semanticSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(semanticSearch.getId().intValue()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.queryEmbedding").value(DEFAULT_QUERY_EMBEDDING))
            .andExpect(jsonPath("$.results").value(DEFAULT_RESULTS))
            .andExpect(jsonPath("$.relevanceScores").value(DEFAULT_RELEVANCE_SCORES))
            .andExpect(jsonPath("$.modelUsed").value(DEFAULT_MODEL_USED))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.searchDate").value(DEFAULT_SEARCH_DATE.toString()));
    }

    @Test
    @Transactional
    void getSemanticSearchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        Long id = semanticSearch.getId();

        defaultSemanticSearchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSemanticSearchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSemanticSearchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByQueryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where query equals to
        defaultSemanticSearchFiltering("query.equals=" + DEFAULT_QUERY, "query.equals=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByQueryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where query in
        defaultSemanticSearchFiltering("query.in=" + DEFAULT_QUERY + "," + UPDATED_QUERY, "query.in=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByQueryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where query is not null
        defaultSemanticSearchFiltering("query.specified=true", "query.specified=false");
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByQueryContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where query contains
        defaultSemanticSearchFiltering("query.contains=" + DEFAULT_QUERY, "query.contains=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByQueryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where query does not contain
        defaultSemanticSearchFiltering("query.doesNotContain=" + UPDATED_QUERY, "query.doesNotContain=" + DEFAULT_QUERY);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByModelUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where modelUsed equals to
        defaultSemanticSearchFiltering("modelUsed.equals=" + DEFAULT_MODEL_USED, "modelUsed.equals=" + UPDATED_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByModelUsedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where modelUsed in
        defaultSemanticSearchFiltering(
            "modelUsed.in=" + DEFAULT_MODEL_USED + "," + UPDATED_MODEL_USED,
            "modelUsed.in=" + UPDATED_MODEL_USED
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByModelUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where modelUsed is not null
        defaultSemanticSearchFiltering("modelUsed.specified=true", "modelUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByModelUsedContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where modelUsed contains
        defaultSemanticSearchFiltering("modelUsed.contains=" + DEFAULT_MODEL_USED, "modelUsed.contains=" + UPDATED_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByModelUsedNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where modelUsed does not contain
        defaultSemanticSearchFiltering("modelUsed.doesNotContain=" + UPDATED_MODEL_USED, "modelUsed.doesNotContain=" + DEFAULT_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime equals to
        defaultSemanticSearchFiltering("executionTime.equals=" + DEFAULT_EXECUTION_TIME, "executionTime.equals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime in
        defaultSemanticSearchFiltering(
            "executionTime.in=" + DEFAULT_EXECUTION_TIME + "," + UPDATED_EXECUTION_TIME,
            "executionTime.in=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime is not null
        defaultSemanticSearchFiltering("executionTime.specified=true", "executionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime is greater than or equal to
        defaultSemanticSearchFiltering(
            "executionTime.greaterThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.greaterThanOrEqual=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime is less than or equal to
        defaultSemanticSearchFiltering(
            "executionTime.lessThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.lessThanOrEqual=" + SMALLER_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime is less than
        defaultSemanticSearchFiltering(
            "executionTime.lessThan=" + UPDATED_EXECUTION_TIME,
            "executionTime.lessThan=" + DEFAULT_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByExecutionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where executionTime is greater than
        defaultSemanticSearchFiltering(
            "executionTime.greaterThan=" + SMALLER_EXECUTION_TIME,
            "executionTime.greaterThan=" + DEFAULT_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where userId equals to
        defaultSemanticSearchFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where userId in
        defaultSemanticSearchFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where userId is not null
        defaultSemanticSearchFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where userId contains
        defaultSemanticSearchFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where userId does not contain
        defaultSemanticSearchFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesBySearchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where searchDate equals to
        defaultSemanticSearchFiltering("searchDate.equals=" + DEFAULT_SEARCH_DATE, "searchDate.equals=" + UPDATED_SEARCH_DATE);
    }

    @Test
    @Transactional
    void getAllSemanticSearchesBySearchDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where searchDate in
        defaultSemanticSearchFiltering(
            "searchDate.in=" + DEFAULT_SEARCH_DATE + "," + UPDATED_SEARCH_DATE,
            "searchDate.in=" + UPDATED_SEARCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllSemanticSearchesBySearchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        // Get all the semanticSearchList where searchDate is not null
        defaultSemanticSearchFiltering("searchDate.specified=true", "searchDate.specified=false");
    }

    private void defaultSemanticSearchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSemanticSearchShouldBeFound(shouldBeFound);
        defaultSemanticSearchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSemanticSearchShouldBeFound(String filter) throws Exception {
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(semanticSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].queryEmbedding").value(hasItem(DEFAULT_QUERY_EMBEDDING)))
            .andExpect(jsonPath("$.[*].results").value(hasItem(DEFAULT_RESULTS)))
            .andExpect(jsonPath("$.[*].relevanceScores").value(hasItem(DEFAULT_RELEVANCE_SCORES)))
            .andExpect(jsonPath("$.[*].modelUsed").value(hasItem(DEFAULT_MODEL_USED)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())));

        // Check, that the count call also returns 1
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSemanticSearchShouldNotBeFound(String filter) throws Exception {
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSemanticSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSemanticSearch() throws Exception {
        // Get the semanticSearch
        restSemanticSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSemanticSearch() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semanticSearch
        SemanticSearch updatedSemanticSearch = semanticSearchRepository.findById(semanticSearch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSemanticSearch are not directly saved in db
        em.detach(updatedSemanticSearch);
        updatedSemanticSearch
            .query(UPDATED_QUERY)
            .queryEmbedding(UPDATED_QUERY_EMBEDDING)
            .results(UPDATED_RESULTS)
            .relevanceScores(UPDATED_RELEVANCE_SCORES)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(updatedSemanticSearch);

        restSemanticSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semanticSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semanticSearchDTO))
            )
            .andExpect(status().isOk());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSemanticSearchToMatchAllProperties(updatedSemanticSearch);
    }

    @Test
    @Transactional
    void putNonExistingSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, semanticSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semanticSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(semanticSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(semanticSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSemanticSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semanticSearch using partial update
        SemanticSearch partialUpdatedSemanticSearch = new SemanticSearch();
        partialUpdatedSemanticSearch.setId(semanticSearch.getId());

        partialUpdatedSemanticSearch
            .query(UPDATED_QUERY)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);

        restSemanticSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemanticSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSemanticSearch))
            )
            .andExpect(status().isOk());

        // Validate the SemanticSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSemanticSearchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSemanticSearch, semanticSearch),
            getPersistedSemanticSearch(semanticSearch)
        );
    }

    @Test
    @Transactional
    void fullUpdateSemanticSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the semanticSearch using partial update
        SemanticSearch partialUpdatedSemanticSearch = new SemanticSearch();
        partialUpdatedSemanticSearch.setId(semanticSearch.getId());

        partialUpdatedSemanticSearch
            .query(UPDATED_QUERY)
            .queryEmbedding(UPDATED_QUERY_EMBEDDING)
            .results(UPDATED_RESULTS)
            .relevanceScores(UPDATED_RELEVANCE_SCORES)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);

        restSemanticSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSemanticSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSemanticSearch))
            )
            .andExpect(status().isOk());

        // Validate the SemanticSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSemanticSearchUpdatableFieldsEquals(partialUpdatedSemanticSearch, getPersistedSemanticSearch(partialUpdatedSemanticSearch));
    }

    @Test
    @Transactional
    void patchNonExistingSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, semanticSearchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(semanticSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(semanticSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSemanticSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        semanticSearch.setId(longCount.incrementAndGet());

        // Create the SemanticSearch
        SemanticSearchDTO semanticSearchDTO = semanticSearchMapper.toDto(semanticSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSemanticSearchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(semanticSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SemanticSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSemanticSearch() throws Exception {
        // Initialize the database
        insertedSemanticSearch = semanticSearchRepository.saveAndFlush(semanticSearch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the semanticSearch
        restSemanticSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, semanticSearch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return semanticSearchRepository.count();
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

    protected SemanticSearch getPersistedSemanticSearch(SemanticSearch semanticSearch) {
        return semanticSearchRepository.findById(semanticSearch.getId()).orElseThrow();
    }

    protected void assertPersistedSemanticSearchToMatchAllProperties(SemanticSearch expectedSemanticSearch) {
        assertSemanticSearchAllPropertiesEquals(expectedSemanticSearch, getPersistedSemanticSearch(expectedSemanticSearch));
    }

    protected void assertPersistedSemanticSearchToMatchUpdatableProperties(SemanticSearch expectedSemanticSearch) {
        assertSemanticSearchAllUpdatablePropertiesEquals(expectedSemanticSearch, getPersistedSemanticSearch(expectedSemanticSearch));
    }
}
