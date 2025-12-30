package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SearchSemanticAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SearchSemantic;
import fr.smartprod.paperdms.search.repository.SearchSemanticRepository;
import fr.smartprod.paperdms.search.repository.search.SearchSemanticSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchSemanticDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchSemanticMapper;
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
 * Integration tests for the {@link SearchSemanticResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchSemanticResourceIT {

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

    private static final String ENTITY_API_URL = "/api/search-semantics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/search-semantics/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchSemanticRepository searchSemanticRepository;

    @Autowired
    private SearchSemanticMapper searchSemanticMapper;

    @Autowired
    private SearchSemanticSearchRepository searchSemanticSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchSemanticMockMvc;

    private SearchSemantic searchSemantic;

    private SearchSemantic insertedSearchSemantic;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchSemantic createEntity() {
        return new SearchSemantic()
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
    public static SearchSemantic createUpdatedEntity() {
        return new SearchSemantic()
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
        searchSemantic = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchSemantic != null) {
            searchSemanticRepository.delete(insertedSearchSemantic);
            searchSemanticSearchRepository.delete(insertedSearchSemantic);
            insertedSearchSemantic = null;
        }
    }

    @Test
    @Transactional
    void createSearchSemantic() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);
        var returnedSearchSemanticDTO = om.readValue(
            restSearchSemanticMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchSemanticDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchSemanticDTO.class
        );

        // Validate the SearchSemantic in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSearchSemantic = searchSemanticMapper.toEntity(returnedSearchSemanticDTO);
        assertSearchSemanticUpdatableFieldsEquals(returnedSearchSemantic, getPersistedSearchSemantic(returnedSearchSemantic));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSearchSemantic = returnedSearchSemantic;
    }

    @Test
    @Transactional
    void createSearchSemanticWithExistingId() throws Exception {
        // Create the SearchSemantic with an existing ID
        searchSemantic.setId(1L);
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchSemanticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchSemanticDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        // set the field null
        searchSemantic.setQuery(null);

        // Create the SearchSemantic, which fails.
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        restSearchSemanticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchSemanticDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSearchDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        // set the field null
        searchSemantic.setSearchDate(null);

        // Create the SearchSemantic, which fails.
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        restSearchSemanticMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchSemanticDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSearchSemantics() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchSemantic.getId().intValue())))
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
    void getSearchSemantic() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get the searchSemantic
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL_ID, searchSemantic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchSemantic.getId().intValue()))
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
    void getSearchSemanticsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        Long id = searchSemantic.getId();

        defaultSearchSemanticFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSearchSemanticFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSearchSemanticFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByQueryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where query equals to
        defaultSearchSemanticFiltering("query.equals=" + DEFAULT_QUERY, "query.equals=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByQueryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where query in
        defaultSearchSemanticFiltering("query.in=" + DEFAULT_QUERY + "," + UPDATED_QUERY, "query.in=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByQueryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where query is not null
        defaultSearchSemanticFiltering("query.specified=true", "query.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByQueryContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where query contains
        defaultSearchSemanticFiltering("query.contains=" + DEFAULT_QUERY, "query.contains=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByQueryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where query does not contain
        defaultSearchSemanticFiltering("query.doesNotContain=" + UPDATED_QUERY, "query.doesNotContain=" + DEFAULT_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByModelUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where modelUsed equals to
        defaultSearchSemanticFiltering("modelUsed.equals=" + DEFAULT_MODEL_USED, "modelUsed.equals=" + UPDATED_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByModelUsedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where modelUsed in
        defaultSearchSemanticFiltering(
            "modelUsed.in=" + DEFAULT_MODEL_USED + "," + UPDATED_MODEL_USED,
            "modelUsed.in=" + UPDATED_MODEL_USED
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByModelUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where modelUsed is not null
        defaultSearchSemanticFiltering("modelUsed.specified=true", "modelUsed.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByModelUsedContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where modelUsed contains
        defaultSearchSemanticFiltering("modelUsed.contains=" + DEFAULT_MODEL_USED, "modelUsed.contains=" + UPDATED_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByModelUsedNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where modelUsed does not contain
        defaultSearchSemanticFiltering("modelUsed.doesNotContain=" + UPDATED_MODEL_USED, "modelUsed.doesNotContain=" + DEFAULT_MODEL_USED);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime equals to
        defaultSearchSemanticFiltering("executionTime.equals=" + DEFAULT_EXECUTION_TIME, "executionTime.equals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime in
        defaultSearchSemanticFiltering(
            "executionTime.in=" + DEFAULT_EXECUTION_TIME + "," + UPDATED_EXECUTION_TIME,
            "executionTime.in=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime is not null
        defaultSearchSemanticFiltering("executionTime.specified=true", "executionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime is greater than or equal to
        defaultSearchSemanticFiltering(
            "executionTime.greaterThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.greaterThanOrEqual=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime is less than or equal to
        defaultSearchSemanticFiltering(
            "executionTime.lessThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.lessThanOrEqual=" + SMALLER_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime is less than
        defaultSearchSemanticFiltering(
            "executionTime.lessThan=" + UPDATED_EXECUTION_TIME,
            "executionTime.lessThan=" + DEFAULT_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByExecutionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where executionTime is greater than
        defaultSearchSemanticFiltering(
            "executionTime.greaterThan=" + SMALLER_EXECUTION_TIME,
            "executionTime.greaterThan=" + DEFAULT_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where userId equals to
        defaultSearchSemanticFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where userId in
        defaultSearchSemanticFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where userId is not null
        defaultSearchSemanticFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where userId contains
        defaultSearchSemanticFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where userId does not contain
        defaultSearchSemanticFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsBySearchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where searchDate equals to
        defaultSearchSemanticFiltering("searchDate.equals=" + DEFAULT_SEARCH_DATE, "searchDate.equals=" + UPDATED_SEARCH_DATE);
    }

    @Test
    @Transactional
    void getAllSearchSemanticsBySearchDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where searchDate in
        defaultSearchSemanticFiltering(
            "searchDate.in=" + DEFAULT_SEARCH_DATE + "," + UPDATED_SEARCH_DATE,
            "searchDate.in=" + UPDATED_SEARCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllSearchSemanticsBySearchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        // Get all the searchSemanticList where searchDate is not null
        defaultSearchSemanticFiltering("searchDate.specified=true", "searchDate.specified=false");
    }

    private void defaultSearchSemanticFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSearchSemanticShouldBeFound(shouldBeFound);
        defaultSearchSemanticShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSearchSemanticShouldBeFound(String filter) throws Exception {
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchSemantic.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].queryEmbedding").value(hasItem(DEFAULT_QUERY_EMBEDDING)))
            .andExpect(jsonPath("$.[*].results").value(hasItem(DEFAULT_RESULTS)))
            .andExpect(jsonPath("$.[*].relevanceScores").value(hasItem(DEFAULT_RELEVANCE_SCORES)))
            .andExpect(jsonPath("$.[*].modelUsed").value(hasItem(DEFAULT_MODEL_USED)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())));

        // Check, that the count call also returns 1
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSearchSemanticShouldNotBeFound(String filter) throws Exception {
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSearchSemanticMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSearchSemantic() throws Exception {
        // Get the searchSemantic
        restSearchSemanticMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchSemantic() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchSemanticSearchRepository.save(searchSemantic);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());

        // Update the searchSemantic
        SearchSemantic updatedSearchSemantic = searchSemanticRepository.findById(searchSemantic.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchSemantic are not directly saved in db
        em.detach(updatedSearchSemantic);
        updatedSearchSemantic
            .query(UPDATED_QUERY)
            .queryEmbedding(UPDATED_QUERY_EMBEDDING)
            .results(UPDATED_RESULTS)
            .relevanceScores(UPDATED_RELEVANCE_SCORES)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(updatedSearchSemantic);

        restSearchSemanticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchSemanticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchSemanticDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchSemanticToMatchAllProperties(updatedSearchSemantic);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SearchSemantic> searchSemanticSearchList = Streamable.of(searchSemanticSearchRepository.findAll()).toList();
                SearchSemantic testSearchSemanticSearch = searchSemanticSearchList.get(searchDatabaseSizeAfter - 1);

                assertSearchSemanticAllPropertiesEquals(testSearchSemanticSearch, updatedSearchSemantic);
            });
    }

    @Test
    @Transactional
    void putNonExistingSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchSemanticDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchSemanticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchSemanticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchSemanticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSearchSemanticWithPatch() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchSemantic using partial update
        SearchSemantic partialUpdatedSearchSemantic = new SearchSemantic();
        partialUpdatedSearchSemantic.setId(searchSemantic.getId());

        partialUpdatedSearchSemantic.results(UPDATED_RESULTS).relevanceScores(UPDATED_RELEVANCE_SCORES).userId(UPDATED_USER_ID);

        restSearchSemanticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchSemantic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchSemantic))
            )
            .andExpect(status().isOk());

        // Validate the SearchSemantic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchSemanticUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchSemantic, searchSemantic),
            getPersistedSearchSemantic(searchSemantic)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchSemanticWithPatch() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchSemantic using partial update
        SearchSemantic partialUpdatedSearchSemantic = new SearchSemantic();
        partialUpdatedSearchSemantic.setId(searchSemantic.getId());

        partialUpdatedSearchSemantic
            .query(UPDATED_QUERY)
            .queryEmbedding(UPDATED_QUERY_EMBEDDING)
            .results(UPDATED_RESULTS)
            .relevanceScores(UPDATED_RELEVANCE_SCORES)
            .modelUsed(UPDATED_MODEL_USED)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE);

        restSearchSemanticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchSemantic.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchSemantic))
            )
            .andExpect(status().isOk());

        // Validate the SearchSemantic in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchSemanticUpdatableFieldsEquals(partialUpdatedSearchSemantic, getPersistedSearchSemantic(partialUpdatedSearchSemantic));
    }

    @Test
    @Transactional
    void patchNonExistingSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchSemanticDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchSemanticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchSemanticDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchSemantic() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        searchSemantic.setId(longCount.incrementAndGet());

        // Create the SearchSemantic
        SearchSemanticDTO searchSemanticDTO = searchSemanticMapper.toDto(searchSemantic);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchSemanticMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchSemanticDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchSemantic in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSearchSemantic() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);
        searchSemanticRepository.save(searchSemantic);
        searchSemanticSearchRepository.save(searchSemantic);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the searchSemantic
        restSearchSemanticMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchSemantic.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchSemanticSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSearchSemantic() throws Exception {
        // Initialize the database
        insertedSearchSemantic = searchSemanticRepository.saveAndFlush(searchSemantic);
        searchSemanticSearchRepository.save(searchSemantic);

        // Search the searchSemantic
        restSearchSemanticMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + searchSemantic.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchSemantic.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].queryEmbedding").value(hasItem(DEFAULT_QUERY_EMBEDDING.toString())))
            .andExpect(jsonPath("$.[*].results").value(hasItem(DEFAULT_RESULTS.toString())))
            .andExpect(jsonPath("$.[*].relevanceScores").value(hasItem(DEFAULT_RELEVANCE_SCORES.toString())))
            .andExpect(jsonPath("$.[*].modelUsed").value(hasItem(DEFAULT_MODEL_USED)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return searchSemanticRepository.count();
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

    protected SearchSemantic getPersistedSearchSemantic(SearchSemantic searchSemantic) {
        return searchSemanticRepository.findById(searchSemantic.getId()).orElseThrow();
    }

    protected void assertPersistedSearchSemanticToMatchAllProperties(SearchSemantic expectedSearchSemantic) {
        assertSearchSemanticAllPropertiesEquals(expectedSearchSemantic, getPersistedSearchSemantic(expectedSearchSemantic));
    }

    protected void assertPersistedSearchSemanticToMatchUpdatableProperties(SearchSemantic expectedSearchSemantic) {
        assertSearchSemanticAllUpdatablePropertiesEquals(expectedSearchSemantic, getPersistedSearchSemantic(expectedSearchSemantic));
    }
}
