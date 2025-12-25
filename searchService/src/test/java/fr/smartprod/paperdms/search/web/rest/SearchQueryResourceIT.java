package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SearchQueryAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.repository.SearchQueryRepository;
import fr.smartprod.paperdms.search.service.dto.SearchQueryDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchQueryMapper;
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
 * Integration tests for the {@link SearchQueryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchQueryResourceIT {

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_FILTERS = "AAAAAAAAAA";
    private static final String UPDATED_FILTERS = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESULT_COUNT = 1;
    private static final Integer UPDATED_RESULT_COUNT = 2;
    private static final Integer SMALLER_RESULT_COUNT = 1 - 1;

    private static final Long DEFAULT_EXECUTION_TIME = 1L;
    private static final Long UPDATED_EXECUTION_TIME = 2L;
    private static final Long SMALLER_EXECUTION_TIME = 1L - 1L;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_SEARCH_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SEARCH_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_RELEVANT = false;
    private static final Boolean UPDATED_IS_RELEVANT = true;

    private static final String ENTITY_API_URL = "/api/search-queries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchQueryRepository searchQueryRepository;

    @Autowired
    private SearchQueryMapper searchQueryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchQueryMockMvc;

    private SearchQuery searchQuery;

    private SearchQuery insertedSearchQuery;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchQuery createEntity() {
        return new SearchQuery()
            .query(DEFAULT_QUERY)
            .filters(DEFAULT_FILTERS)
            .resultCount(DEFAULT_RESULT_COUNT)
            .executionTime(DEFAULT_EXECUTION_TIME)
            .userId(DEFAULT_USER_ID)
            .searchDate(DEFAULT_SEARCH_DATE)
            .isRelevant(DEFAULT_IS_RELEVANT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchQuery createUpdatedEntity() {
        return new SearchQuery()
            .query(UPDATED_QUERY)
            .filters(UPDATED_FILTERS)
            .resultCount(UPDATED_RESULT_COUNT)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE)
            .isRelevant(UPDATED_IS_RELEVANT);
    }

    @BeforeEach
    void initTest() {
        searchQuery = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchQuery != null) {
            searchQueryRepository.delete(insertedSearchQuery);
            insertedSearchQuery = null;
        }
    }

    @Test
    @Transactional
    void createSearchQuery() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);
        var returnedSearchQueryDTO = om.readValue(
            restSearchQueryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchQueryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchQueryDTO.class
        );

        // Validate the SearchQuery in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSearchQuery = searchQueryMapper.toEntity(returnedSearchQueryDTO);
        assertSearchQueryUpdatableFieldsEquals(returnedSearchQuery, getPersistedSearchQuery(returnedSearchQuery));

        insertedSearchQuery = returnedSearchQuery;
    }

    @Test
    @Transactional
    void createSearchQueryWithExistingId() throws Exception {
        // Create the SearchQuery with an existing ID
        searchQuery.setId(1L);
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchQueryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchQueryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQueryIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchQuery.setQuery(null);

        // Create the SearchQuery, which fails.
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        restSearchQueryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchQueryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSearchDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchQuery.setSearchDate(null);

        // Create the SearchQuery, which fails.
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        restSearchQueryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchQueryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSearchQueries() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].filters").value(hasItem(DEFAULT_FILTERS)))
            .andExpect(jsonPath("$.[*].resultCount").value(hasItem(DEFAULT_RESULT_COUNT)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)));
    }

    @Test
    @Transactional
    void getSearchQuery() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get the searchQuery
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL_ID, searchQuery.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchQuery.getId().intValue()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.filters").value(DEFAULT_FILTERS))
            .andExpect(jsonPath("$.resultCount").value(DEFAULT_RESULT_COUNT))
            .andExpect(jsonPath("$.executionTime").value(DEFAULT_EXECUTION_TIME.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.searchDate").value(DEFAULT_SEARCH_DATE.toString()))
            .andExpect(jsonPath("$.isRelevant").value(DEFAULT_IS_RELEVANT));
    }

    @Test
    @Transactional
    void getSearchQueriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        Long id = searchQuery.getId();

        defaultSearchQueryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSearchQueryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSearchQueryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByQueryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where query equals to
        defaultSearchQueryFiltering("query.equals=" + DEFAULT_QUERY, "query.equals=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByQueryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where query in
        defaultSearchQueryFiltering("query.in=" + DEFAULT_QUERY + "," + UPDATED_QUERY, "query.in=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByQueryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where query is not null
        defaultSearchQueryFiltering("query.specified=true", "query.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchQueriesByQueryContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where query contains
        defaultSearchQueryFiltering("query.contains=" + DEFAULT_QUERY, "query.contains=" + UPDATED_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByQueryNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where query does not contain
        defaultSearchQueryFiltering("query.doesNotContain=" + UPDATED_QUERY, "query.doesNotContain=" + DEFAULT_QUERY);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount equals to
        defaultSearchQueryFiltering("resultCount.equals=" + DEFAULT_RESULT_COUNT, "resultCount.equals=" + UPDATED_RESULT_COUNT);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount in
        defaultSearchQueryFiltering(
            "resultCount.in=" + DEFAULT_RESULT_COUNT + "," + UPDATED_RESULT_COUNT,
            "resultCount.in=" + UPDATED_RESULT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount is not null
        defaultSearchQueryFiltering("resultCount.specified=true", "resultCount.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount is greater than or equal to
        defaultSearchQueryFiltering(
            "resultCount.greaterThanOrEqual=" + DEFAULT_RESULT_COUNT,
            "resultCount.greaterThanOrEqual=" + UPDATED_RESULT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount is less than or equal to
        defaultSearchQueryFiltering(
            "resultCount.lessThanOrEqual=" + DEFAULT_RESULT_COUNT,
            "resultCount.lessThanOrEqual=" + SMALLER_RESULT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount is less than
        defaultSearchQueryFiltering("resultCount.lessThan=" + UPDATED_RESULT_COUNT, "resultCount.lessThan=" + DEFAULT_RESULT_COUNT);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByResultCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where resultCount is greater than
        defaultSearchQueryFiltering("resultCount.greaterThan=" + SMALLER_RESULT_COUNT, "resultCount.greaterThan=" + DEFAULT_RESULT_COUNT);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime equals to
        defaultSearchQueryFiltering("executionTime.equals=" + DEFAULT_EXECUTION_TIME, "executionTime.equals=" + UPDATED_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime in
        defaultSearchQueryFiltering(
            "executionTime.in=" + DEFAULT_EXECUTION_TIME + "," + UPDATED_EXECUTION_TIME,
            "executionTime.in=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime is not null
        defaultSearchQueryFiltering("executionTime.specified=true", "executionTime.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime is greater than or equal to
        defaultSearchQueryFiltering(
            "executionTime.greaterThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.greaterThanOrEqual=" + UPDATED_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime is less than or equal to
        defaultSearchQueryFiltering(
            "executionTime.lessThanOrEqual=" + DEFAULT_EXECUTION_TIME,
            "executionTime.lessThanOrEqual=" + SMALLER_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime is less than
        defaultSearchQueryFiltering("executionTime.lessThan=" + UPDATED_EXECUTION_TIME, "executionTime.lessThan=" + DEFAULT_EXECUTION_TIME);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByExecutionTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where executionTime is greater than
        defaultSearchQueryFiltering(
            "executionTime.greaterThan=" + SMALLER_EXECUTION_TIME,
            "executionTime.greaterThan=" + DEFAULT_EXECUTION_TIME
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where userId equals to
        defaultSearchQueryFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where userId in
        defaultSearchQueryFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where userId is not null
        defaultSearchQueryFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchQueriesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where userId contains
        defaultSearchQueryFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where userId does not contain
        defaultSearchQueryFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllSearchQueriesBySearchDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where searchDate equals to
        defaultSearchQueryFiltering("searchDate.equals=" + DEFAULT_SEARCH_DATE, "searchDate.equals=" + UPDATED_SEARCH_DATE);
    }

    @Test
    @Transactional
    void getAllSearchQueriesBySearchDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where searchDate in
        defaultSearchQueryFiltering(
            "searchDate.in=" + DEFAULT_SEARCH_DATE + "," + UPDATED_SEARCH_DATE,
            "searchDate.in=" + UPDATED_SEARCH_DATE
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesBySearchDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where searchDate is not null
        defaultSearchQueryFiltering("searchDate.specified=true", "searchDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchQueriesByIsRelevantIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where isRelevant equals to
        defaultSearchQueryFiltering("isRelevant.equals=" + DEFAULT_IS_RELEVANT, "isRelevant.equals=" + UPDATED_IS_RELEVANT);
    }

    @Test
    @Transactional
    void getAllSearchQueriesByIsRelevantIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where isRelevant in
        defaultSearchQueryFiltering(
            "isRelevant.in=" + DEFAULT_IS_RELEVANT + "," + UPDATED_IS_RELEVANT,
            "isRelevant.in=" + UPDATED_IS_RELEVANT
        );
    }

    @Test
    @Transactional
    void getAllSearchQueriesByIsRelevantIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        // Get all the searchQueryList where isRelevant is not null
        defaultSearchQueryFiltering("isRelevant.specified=true", "isRelevant.specified=false");
    }

    private void defaultSearchQueryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSearchQueryShouldBeFound(shouldBeFound);
        defaultSearchQueryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSearchQueryShouldBeFound(String filter) throws Exception {
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchQuery.getId().intValue())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].filters").value(hasItem(DEFAULT_FILTERS)))
            .andExpect(jsonPath("$.[*].resultCount").value(hasItem(DEFAULT_RESULT_COUNT)))
            .andExpect(jsonPath("$.[*].executionTime").value(hasItem(DEFAULT_EXECUTION_TIME.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].searchDate").value(hasItem(DEFAULT_SEARCH_DATE.toString())))
            .andExpect(jsonPath("$.[*].isRelevant").value(hasItem(DEFAULT_IS_RELEVANT)));

        // Check, that the count call also returns 1
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSearchQueryShouldNotBeFound(String filter) throws Exception {
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSearchQueryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSearchQuery() throws Exception {
        // Get the searchQuery
        restSearchQueryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchQuery() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchQuery
        SearchQuery updatedSearchQuery = searchQueryRepository.findById(searchQuery.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchQuery are not directly saved in db
        em.detach(updatedSearchQuery);
        updatedSearchQuery
            .query(UPDATED_QUERY)
            .filters(UPDATED_FILTERS)
            .resultCount(UPDATED_RESULT_COUNT)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE)
            .isRelevant(UPDATED_IS_RELEVANT);
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(updatedSearchQuery);

        restSearchQueryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchQueryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchQueryDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchQueryToMatchAllProperties(updatedSearchQuery);
    }

    @Test
    @Transactional
    void putNonExistingSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchQueryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchQueryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchQueryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchQueryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSearchQueryWithPatch() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchQuery using partial update
        SearchQuery partialUpdatedSearchQuery = new SearchQuery();
        partialUpdatedSearchQuery.setId(searchQuery.getId());

        partialUpdatedSearchQuery.filters(UPDATED_FILTERS).userId(UPDATED_USER_ID).isRelevant(UPDATED_IS_RELEVANT);

        restSearchQueryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchQuery.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchQuery))
            )
            .andExpect(status().isOk());

        // Validate the SearchQuery in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchQueryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchQuery, searchQuery),
            getPersistedSearchQuery(searchQuery)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchQueryWithPatch() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchQuery using partial update
        SearchQuery partialUpdatedSearchQuery = new SearchQuery();
        partialUpdatedSearchQuery.setId(searchQuery.getId());

        partialUpdatedSearchQuery
            .query(UPDATED_QUERY)
            .filters(UPDATED_FILTERS)
            .resultCount(UPDATED_RESULT_COUNT)
            .executionTime(UPDATED_EXECUTION_TIME)
            .userId(UPDATED_USER_ID)
            .searchDate(UPDATED_SEARCH_DATE)
            .isRelevant(UPDATED_IS_RELEVANT);

        restSearchQueryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchQuery.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchQuery))
            )
            .andExpect(status().isOk());

        // Validate the SearchQuery in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchQueryUpdatableFieldsEquals(partialUpdatedSearchQuery, getPersistedSearchQuery(partialUpdatedSearchQuery));
    }

    @Test
    @Transactional
    void patchNonExistingSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchQueryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchQueryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchQueryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchQuery() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchQuery.setId(longCount.incrementAndGet());

        // Create the SearchQuery
        SearchQueryDTO searchQueryDTO = searchQueryMapper.toDto(searchQuery);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchQueryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchQueryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchQuery in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchQuery() throws Exception {
        // Initialize the database
        insertedSearchQuery = searchQueryRepository.saveAndFlush(searchQuery);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the searchQuery
        restSearchQueryMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchQuery.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return searchQueryRepository.count();
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

    protected SearchQuery getPersistedSearchQuery(SearchQuery searchQuery) {
        return searchQueryRepository.findById(searchQuery.getId()).orElseThrow();
    }

    protected void assertPersistedSearchQueryToMatchAllProperties(SearchQuery expectedSearchQuery) {
        assertSearchQueryAllPropertiesEquals(expectedSearchQuery, getPersistedSearchQuery(expectedSearchQuery));
    }

    protected void assertPersistedSearchQueryToMatchUpdatableProperties(SearchQuery expectedSearchQuery) {
        assertSearchQueryAllUpdatablePropertiesEquals(expectedSearchQuery, getPersistedSearchQuery(expectedSearchQuery));
    }
}
