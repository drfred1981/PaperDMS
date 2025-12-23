package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SearchFacetAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.domain.enumeration.FacetType;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
import fr.smartprod.paperdms.search.service.dto.SearchFacetDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchFacetMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link SearchFacetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchFacetResourceIT {

    private static final Long DEFAULT_SEARCH_QUERY_ID = 1L;
    private static final Long UPDATED_SEARCH_QUERY_ID = 2L;

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchFacetRepository searchFacetRepository;

    @Autowired
    private SearchFacetMapper searchFacetMapper;

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
        return new SearchFacet()
            .searchQueryId(DEFAULT_SEARCH_QUERY_ID)
            .facetName(DEFAULT_FACET_NAME)
            .facetType(DEFAULT_FACET_TYPE)
            .values(DEFAULT_VALUES)
            .counts(DEFAULT_COUNTS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchFacet createUpdatedEntity() {
        return new SearchFacet()
            .searchQueryId(UPDATED_SEARCH_QUERY_ID)
            .facetName(UPDATED_FACET_NAME)
            .facetType(UPDATED_FACET_TYPE)
            .values(UPDATED_VALUES)
            .counts(UPDATED_COUNTS);
    }

    @BeforeEach
    void initTest() {
        searchFacet = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchFacet != null) {
            searchFacetRepository.delete(insertedSearchFacet);
            insertedSearchFacet = null;
        }
    }

    @Test
    @Transactional
    void createSearchFacet() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedSearchFacet = returnedSearchFacet;
    }

    @Test
    @Transactional
    void createSearchFacetWithExistingId() throws Exception {
        // Create the SearchFacet with an existing ID
        searchFacet.setId(1L);
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFacetNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchFacet.setFacetName(null);

        // Create the SearchFacet, which fails.
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFacetTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        searchFacet.setFacetType(null);

        // Create the SearchFacet, which fails.
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        restSearchFacetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].searchQueryId").value(hasItem(DEFAULT_SEARCH_QUERY_ID.intValue())))
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
            .andExpect(jsonPath("$.searchQueryId").value(DEFAULT_SEARCH_QUERY_ID.intValue()))
            .andExpect(jsonPath("$.facetName").value(DEFAULT_FACET_NAME))
            .andExpect(jsonPath("$.facetType").value(DEFAULT_FACET_TYPE.toString()))
            .andExpect(jsonPath("$.values").value(DEFAULT_VALUES))
            .andExpect(jsonPath("$.counts").value(DEFAULT_COUNTS));
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

        // Update the searchFacet
        SearchFacet updatedSearchFacet = searchFacetRepository.findById(searchFacet.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchFacet are not directly saved in db
        em.detach(updatedSearchFacet);
        updatedSearchFacet
            .searchQueryId(UPDATED_SEARCH_QUERY_ID)
            .facetName(UPDATED_FACET_NAME)
            .facetType(UPDATED_FACET_TYPE)
            .values(UPDATED_VALUES)
            .counts(UPDATED_COUNTS);
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
    }

    @Test
    @Transactional
    void putNonExistingSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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

        partialUpdatedSearchFacet
            .searchQueryId(UPDATED_SEARCH_QUERY_ID)
            .facetName(UPDATED_FACET_NAME)
            .facetType(UPDATED_FACET_TYPE)
            .values(UPDATED_VALUES);

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

        partialUpdatedSearchFacet
            .searchQueryId(UPDATED_SEARCH_QUERY_ID)
            .facetName(UPDATED_FACET_NAME)
            .facetType(UPDATED_FACET_TYPE)
            .values(UPDATED_VALUES)
            .counts(UPDATED_COUNTS);

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
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchFacet() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchFacet.setId(longCount.incrementAndGet());

        // Create the SearchFacet
        SearchFacetDTO searchFacetDTO = searchFacetMapper.toDto(searchFacet);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchFacetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchFacetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchFacet in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSearchFacet() throws Exception {
        // Initialize the database
        insertedSearchFacet = searchFacetRepository.saveAndFlush(searchFacet);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the searchFacet
        restSearchFacetMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchFacet.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
