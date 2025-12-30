package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SearchFacetAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.domain.enumeration.FacetType;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.repository.search.SearchFacetSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchFacetMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SearchFacetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchFacetResourceIT {

    private static final String DEFAULT_FACET_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FACET_NAME = "BBBBBBBBBB";

    private static final FacetType DEFAULT_FACET_TYPE = FacetType.TERMS;
    private static final FacetType UPDATED_FACET_TYPE = FacetType.DATE_RANGE;

    private static final String DEFAULT_VALUES = "AAAAAAAAAA";
    private static final String UPDATED_VALUES = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTS = "AAAAAAAAAA";
    private static final String UPDATED_COUNTS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/search-facets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/search-facets/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchFacetRepository searchFacetRepository;

    @Autowired
    private SearchFacetMapper searchFacetMapper;

    @Autowired
    private SearchFacetSearchRepository searchFacetSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchFacetMockMvc;

    private SearchFacet searchFacet;

    private SearchFacet insertedSearchFacet;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchFacet createEntity() {
        return new SearchFacet().facetName(DEFAULT_FACET_NAME).facetType(DEFAULT_FACET_TYPE).values(DEFAULT_VALUES).counts(DEFAULT_COUNTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchFacet createUpdatedEntity() {
        return new SearchFacet().facetName(UPDATED_FACET_NAME).facetType(UPDATED_FACET_TYPE).values(UPDATED_VALUES).counts(UPDATED_COUNTS);
    }

    @BeforeEach
    void initTest() {
        searchFacet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchFacet != null) {
            searchFacetRepository.delete(insertedSearchFacet);
            searchFacetSearchRepository.delete(insertedSearchFacet);
            insertedSearchFacet = null;
        }
    }

    @Test
    @Transactional
    void createSearchFacet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);
        var returnedSearchFacetDTO = om.readValue(
            restSearchFacetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchFacetDTO.class
        );

        // Validate the SearchFacet in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSearchFacet = searchFacetMapper.toEntity(returnedSearchFacetDTO);
        assertSearchFacetUpdatableFieldsEquals(returnedSearchFacet, getPersistedSearchFacet(returnedSearchFacet));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSearchFacet = returnedSearchFacet;
    }

    @Test
    @Transactional
    void createSearchFacetWithExistingId() throws Exception {
        // Create the SearchFacet with an existing ID
        searchFacet.setId(1L);
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFacetNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        // set the field null
        searchFacet.setFacetName(null);

        // Create the SearchFacet, which fails.
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFacetTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        // set the field null
        searchFacet.setFacetType(null);

        // Create the SearchFacet, which fails.
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSearchFacets() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchFacet.getId().intValue())))
            .andExpect(jsonPath("$.[*].facetName").value(hasItem(DEFAULT_FACET_NAME)))
            .andExpect(jsonPath("$.[*].facetType").value(hasItem(DEFAULT_FACET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES)))
            .andExpect(jsonPath("$.[*].counts").value(hasItem(DEFAULT_COUNTS)));
    }

    @Test
    @Transactional
    void getSearchFacet() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get the searchFacet
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL_ID, searchFacet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchFacet.getId().intValue()))
            .andExpect(jsonPath("$.facetName").value(DEFAULT_FACET_NAME))
            .andExpect(jsonPath("$.facetType").value(DEFAULT_FACET_TYPE.toString()))
            .andExpect(jsonPath("$.values").value(DEFAULT_VALUES))
            .andExpect(jsonPath("$.counts").value(DEFAULT_COUNTS));
    }

    @Test
    @Transactional
    void getSearchFacetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        Long id = searchFacet.getId();

        defaultSearchFacetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSearchFacetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSearchFacetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetName equals to
        defaultSearchFacetFiltering("facetName.equals=" + DEFAULT_FACET_NAME, "facetName.equals=" + UPDATED_FACET_NAME);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetName in
        defaultSearchFacetFiltering("facetName.in=" + DEFAULT_FACET_NAME + "," + UPDATED_FACET_NAME, "facetName.in=" + UPDATED_FACET_NAME);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetName is not null
        defaultSearchFacetFiltering("facetName.specified=true", "facetName.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetName contains
        defaultSearchFacetFiltering("facetName.contains=" + DEFAULT_FACET_NAME, "facetName.contains=" + UPDATED_FACET_NAME);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetName does not contain
        defaultSearchFacetFiltering("facetName.doesNotContain=" + UPDATED_FACET_NAME, "facetName.doesNotContain=" + DEFAULT_FACET_NAME);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetType equals to
        defaultSearchFacetFiltering("facetType.equals=" + DEFAULT_FACET_TYPE, "facetType.equals=" + UPDATED_FACET_TYPE);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetType in
        defaultSearchFacetFiltering("facetType.in=" + DEFAULT_FACET_TYPE + "," + UPDATED_FACET_TYPE, "facetType.in=" + UPDATED_FACET_TYPE);
    }

    @Test
    @Transactional
    void getAllSearchFacetsByFacetTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        // Get all the searchFacetList where facetType is not null
        defaultSearchFacetFiltering("facetType.specified=true", "facetType.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchFacetsBySearchQueryIsEqualToSomething() throws Exception {
        SearchQuery searchQuery;
        if (TestUtil.findAll(em, SearchQuery.class).isEmpty()) {
            searchFacetRepository.saveAndFlush(searchFacet);
            searchQuery = SearchQueryResourceIT.createEntity();
        } else {
            searchQuery = TestUtil.findAll(em, SearchQuery.class).get(0);
        }
        em.persist(searchQuery);
        em.flush();
        searchFacet.setSearchQuery(searchQuery);
        searchFacetRepository.saveAndFlush(searchFacet);
        Long searchQueryId = searchQuery.getId();
        // Get all the searchFacetList where searchQuery equals to searchQueryId
        defaultSearchFacetShouldBeFound("searchQueryId.equals=" + searchQueryId);

        // Get all the searchFacetList where searchQuery equals to (searchQueryId + 1)
        defaultSearchFacetShouldNotBeFound("searchQueryId.equals=" + (searchQueryId + 1));
    }

    private void defaultSearchFacetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSearchFacetShouldBeFound(shouldBeFound);
        defaultSearchFacetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSearchFacetShouldBeFound(String filter) throws Exception {
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchFacet.getId().intValue())))
            .andExpect(jsonPath("$.[*].facetName").value(hasItem(DEFAULT_FACET_NAME)))
            .andExpect(jsonPath("$.[*].facetType").value(hasItem(DEFAULT_FACET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES)))
            .andExpect(jsonPath("$.[*].counts").value(hasItem(DEFAULT_COUNTS)));

        // Check, that the count call also returns 1
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSearchFacetShouldNotBeFound(String filter) throws Exception {
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSearchFacetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSearchFacet() throws Exception {
        // Get the searchFacet
        restSearchFacetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchFacet() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchFacetSearchRepository.save(searchFacet);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());

        // Update the searchFacet
        SearchFacet updatedSearchFacet = searchFacetRepository.findById(searchFacet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchFacet are not directly saved in db
        em.detach(updatedSearchFacet);
        updatedSearchFacet.facetName(UPDATED_FACET_NAME).facetType(UPDATED_FACET_TYPE).values(UPDATED_VALUES).counts(UPDATED_COUNTS);
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(updatedSearchFacet);

        restSearchFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchFacetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchFacetDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchFacetToMatchAllProperties(updatedSearchFacet);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SearchFacet> searchFacetSearchList = Streamable.of(searchFacetSearchRepository.findAll()).toList();
                SearchFacet testSearchFacetSearch = searchFacetSearchList.get(searchDatabaseSizeAfter - 1);

                assertSearchFacetAllPropertiesEquals(testSearchFacetSearch, updatedSearchFacet);
            });
    }

    @Test
    @Transactional
    void putNonExistingSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchFacetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSearchFacetWithPatch() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchFacet using partial update
        SearchFacet partialUpdatedSearchFacet = new SearchFacet();
        partialUpdatedSearchFacet.setId(searchFacet.getId());

        partialUpdatedSearchFacet.facetType(UPDATED_FACET_TYPE).values(UPDATED_VALUES);

        restSearchFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchFacet))
            )
            .andExpect(status().isOk());

        // Validate the SearchFacet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchFacetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchFacet, searchFacet),
            getPersistedSearchFacet(searchFacet)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchFacetWithPatch() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchFacet using partial update
        SearchFacet partialUpdatedSearchFacet = new SearchFacet();
        partialUpdatedSearchFacet.setId(searchFacet.getId());

        partialUpdatedSearchFacet.facetName(UPDATED_FACET_NAME).facetType(UPDATED_FACET_TYPE).values(UPDATED_VALUES).counts(UPDATED_COUNTS);

        restSearchFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchFacet.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchFacet))
            )
            .andExpect(status().isOk());

        // Validate the SearchFacet in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchFacetUpdatableFieldsEquals(partialUpdatedSearchFacet, getPersistedSearchFacet(partialUpdatedSearchFacet));
    }

    @Test
    @Transactional
    void patchNonExistingSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchFacetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchFacetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSearchFacet() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);
        searchFacetRepository.save(searchFacet);
        searchFacetSearchRepository.save(searchFacet);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the searchFacet
        restSearchFacetMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchFacet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchFacetSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSearchFacet() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);
        searchFacetSearchRepository.save(searchFacet);

        // Search the searchFacet
        restSearchFacetMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + searchFacet.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchFacet.getId().intValue())))
            .andExpect(jsonPath("$.[*].facetName").value(hasItem(DEFAULT_FACET_NAME)))
            .andExpect(jsonPath("$.[*].facetType").value(hasItem(DEFAULT_FACET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].values").value(hasItem(DEFAULT_VALUES.toString())))
            .andExpect(jsonPath("$.[*].counts").value(hasItem(DEFAULT_COUNTS.toString())));
    }

    protected long getRepositoryCount() {
        return searchFacetRepository.count();
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

    protected SearchFacet getPersistedSearchFacet(SearchFacet searchFacet) {
        return searchFacetRepository.findById(searchFacet.getId()).orElseThrow();
    }

    protected void assertPersistedSearchFacetToMatchAllProperties(SearchFacet expectedSearchFacet) {
        assertSearchFacetAllPropertiesEquals(expectedSearchFacet, getPersistedSearchFacet(expectedSearchFacet));
    }

    protected void assertPersistedSearchFacetToMatchUpdatableProperties(SearchFacet expectedSearchFacet) {
        assertSearchFacetAllUpdatablePropertiesEquals(expectedSearchFacet, getPersistedSearchFacet(expectedSearchFacet));
    }
}
