package fr.smartprod.paperdms.search.web.rest;

import static fr.smartprod.paperdms.search.domain.SearchIndexAsserts.*;
import static fr.smartprod.paperdms.search.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.search.IntegrationTest;
import fr.smartprod.paperdms.search.domain.SearchIndex;
import fr.smartprod.paperdms.search.repository.SearchIndexRepository;
import fr.smartprod.paperdms.search.repository.search.SearchIndexSearchRepository;
import fr.smartprod.paperdms.search.service.dto.SearchIndexDTO;
import fr.smartprod.paperdms.search.service.mapper.SearchIndexMapper;
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
 * Integration tests for the {@link SearchIndexResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SearchIndexResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_INDEXED_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_INDEXED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final String DEFAULT_CORRESPONDENTS = "AAAAAAAAAA";
    private static final String UPDATED_CORRESPONDENTS = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_ENTITIES = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_ENTITIES = "BBBBBBBBBB";

    private static final Instant DEFAULT_INDEXED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_INDEXED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/search-indices";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/search-indices/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SearchIndexRepository searchIndexRepository;

    @Autowired
    private SearchIndexMapper searchIndexMapper;

    @Autowired
    private SearchIndexSearchRepository searchIndexSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSearchIndexMockMvc;

    private SearchIndex searchIndex;

    private SearchIndex insertedSearchIndex;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchIndex createEntity() {
        return new SearchIndex()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .indexedContent(DEFAULT_INDEXED_CONTENT)
            .metadata(DEFAULT_METADATA)
            .tags(DEFAULT_TAGS)
            .correspondents(DEFAULT_CORRESPONDENTS)
            .extractedEntities(DEFAULT_EXTRACTED_ENTITIES)
            .indexedDate(DEFAULT_INDEXED_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SearchIndex createUpdatedEntity() {
        return new SearchIndex()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .indexedContent(UPDATED_INDEXED_CONTENT)
            .metadata(UPDATED_METADATA)
            .tags(UPDATED_TAGS)
            .correspondents(UPDATED_CORRESPONDENTS)
            .extractedEntities(UPDATED_EXTRACTED_ENTITIES)
            .indexedDate(UPDATED_INDEXED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        searchIndex = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSearchIndex != null) {
            searchIndexRepository.delete(insertedSearchIndex);
            searchIndexSearchRepository.delete(insertedSearchIndex);
            insertedSearchIndex = null;
        }
    }

    @Test
    @Transactional
    void createSearchIndex() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);
        var returnedSearchIndexDTO = om.readValue(
            restSearchIndexMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchIndexDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SearchIndexDTO.class
        );

        // Validate the SearchIndex in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSearchIndex = searchIndexMapper.toEntity(returnedSearchIndexDTO);
        assertSearchIndexUpdatableFieldsEquals(returnedSearchIndex, getPersistedSearchIndex(returnedSearchIndex));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedSearchIndex = returnedSearchIndex;
    }

    @Test
    @Transactional
    void createSearchIndexWithExistingId() throws Exception {
        // Create the SearchIndex with an existing ID
        searchIndex.setId(1L);
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restSearchIndexMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchIndexDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        // set the field null
        searchIndex.setDocumentSha256(null);

        // Create the SearchIndex, which fails.
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        restSearchIndexMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchIndexDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIndexedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        // set the field null
        searchIndex.setIndexedDate(null);

        // Create the SearchIndex, which fails.
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        restSearchIndexMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchIndexDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllSearchIndices() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].indexedContent").value(hasItem(DEFAULT_INDEXED_CONTENT)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].correspondents").value(hasItem(DEFAULT_CORRESPONDENTS)))
            .andExpect(jsonPath("$.[*].extractedEntities").value(hasItem(DEFAULT_EXTRACTED_ENTITIES)))
            .andExpect(jsonPath("$.[*].indexedDate").value(hasItem(DEFAULT_INDEXED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSearchIndex() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get the searchIndex
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL_ID, searchIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(searchIndex.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.indexedContent").value(DEFAULT_INDEXED_CONTENT))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.correspondents").value(DEFAULT_CORRESPONDENTS))
            .andExpect(jsonPath("$.extractedEntities").value(DEFAULT_EXTRACTED_ENTITIES))
            .andExpect(jsonPath("$.indexedDate").value(DEFAULT_INDEXED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getSearchIndicesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        Long id = searchIndex.getId();

        defaultSearchIndexFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSearchIndexFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSearchIndexFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where documentSha256 equals to
        defaultSearchIndexFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where documentSha256 in
        defaultSearchIndexFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where documentSha256 is not null
        defaultSearchIndexFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchIndicesByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where documentSha256 contains
        defaultSearchIndexFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where documentSha256 does not contain
        defaultSearchIndexFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where tags equals to
        defaultSearchIndexFiltering("tags.equals=" + DEFAULT_TAGS, "tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where tags in
        defaultSearchIndexFiltering("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS, "tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where tags is not null
        defaultSearchIndexFiltering("tags.specified=true", "tags.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchIndicesByTagsContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where tags contains
        defaultSearchIndexFiltering("tags.contains=" + DEFAULT_TAGS, "tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where tags does not contain
        defaultSearchIndexFiltering("tags.doesNotContain=" + UPDATED_TAGS, "tags.doesNotContain=" + DEFAULT_TAGS);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByCorrespondentsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where correspondents equals to
        defaultSearchIndexFiltering("correspondents.equals=" + DEFAULT_CORRESPONDENTS, "correspondents.equals=" + UPDATED_CORRESPONDENTS);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByCorrespondentsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where correspondents in
        defaultSearchIndexFiltering(
            "correspondents.in=" + DEFAULT_CORRESPONDENTS + "," + UPDATED_CORRESPONDENTS,
            "correspondents.in=" + UPDATED_CORRESPONDENTS
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByCorrespondentsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where correspondents is not null
        defaultSearchIndexFiltering("correspondents.specified=true", "correspondents.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchIndicesByCorrespondentsContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where correspondents contains
        defaultSearchIndexFiltering(
            "correspondents.contains=" + DEFAULT_CORRESPONDENTS,
            "correspondents.contains=" + UPDATED_CORRESPONDENTS
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByCorrespondentsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where correspondents does not contain
        defaultSearchIndexFiltering(
            "correspondents.doesNotContain=" + UPDATED_CORRESPONDENTS,
            "correspondents.doesNotContain=" + DEFAULT_CORRESPONDENTS
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByIndexedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where indexedDate equals to
        defaultSearchIndexFiltering("indexedDate.equals=" + DEFAULT_INDEXED_DATE, "indexedDate.equals=" + UPDATED_INDEXED_DATE);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByIndexedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where indexedDate in
        defaultSearchIndexFiltering(
            "indexedDate.in=" + DEFAULT_INDEXED_DATE + "," + UPDATED_INDEXED_DATE,
            "indexedDate.in=" + UPDATED_INDEXED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByIndexedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where indexedDate is not null
        defaultSearchIndexFiltering("indexedDate.specified=true", "indexedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllSearchIndicesByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where lastUpdated equals to
        defaultSearchIndexFiltering("lastUpdated.equals=" + DEFAULT_LAST_UPDATED, "lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void getAllSearchIndicesByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where lastUpdated in
        defaultSearchIndexFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllSearchIndicesByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        // Get all the searchIndexList where lastUpdated is not null
        defaultSearchIndexFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    private void defaultSearchIndexFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSearchIndexShouldBeFound(shouldBeFound);
        defaultSearchIndexShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSearchIndexShouldBeFound(String filter) throws Exception {
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].indexedContent").value(hasItem(DEFAULT_INDEXED_CONTENT)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].correspondents").value(hasItem(DEFAULT_CORRESPONDENTS)))
            .andExpect(jsonPath("$.[*].extractedEntities").value(hasItem(DEFAULT_EXTRACTED_ENTITIES)))
            .andExpect(jsonPath("$.[*].indexedDate").value(hasItem(DEFAULT_INDEXED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSearchIndexShouldNotBeFound(String filter) throws Exception {
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSearchIndexMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSearchIndex() throws Exception {
        // Get the searchIndex
        restSearchIndexMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSearchIndex() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        searchIndexSearchRepository.save(searchIndex);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());

        // Update the searchIndex
        SearchIndex updatedSearchIndex = searchIndexRepository.findById(searchIndex.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSearchIndex are not directly saved in db
        em.detach(updatedSearchIndex);
        updatedSearchIndex
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .indexedContent(UPDATED_INDEXED_CONTENT)
            .metadata(UPDATED_METADATA)
            .tags(UPDATED_TAGS)
            .correspondents(UPDATED_CORRESPONDENTS)
            .extractedEntities(UPDATED_EXTRACTED_ENTITIES)
            .indexedDate(UPDATED_INDEXED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(updatedSearchIndex);

        restSearchIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchIndexDTO))
            )
            .andExpect(status().isOk());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSearchIndexToMatchAllProperties(updatedSearchIndex);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<SearchIndex> searchIndexSearchList = Streamable.of(searchIndexSearchRepository.findAll()).toList();
                SearchIndex testSearchIndexSearch = searchIndexSearchList.get(searchDatabaseSizeAfter - 1);

                assertSearchIndexAllPropertiesEquals(testSearchIndexSearch, updatedSearchIndex);
            });
    }

    @Test
    @Transactional
    void putNonExistingSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, searchIndexDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(searchIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(searchIndexDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateSearchIndexWithPatch() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchIndex using partial update
        SearchIndex partialUpdatedSearchIndex = new SearchIndex();
        partialUpdatedSearchIndex.setId(searchIndex.getId());

        partialUpdatedSearchIndex
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .indexedContent(UPDATED_INDEXED_CONTENT)
            .metadata(UPDATED_METADATA)
            .correspondents(UPDATED_CORRESPONDENTS)
            .indexedDate(UPDATED_INDEXED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchIndex))
            )
            .andExpect(status().isOk());

        // Validate the SearchIndex in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchIndexUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSearchIndex, searchIndex),
            getPersistedSearchIndex(searchIndex)
        );
    }

    @Test
    @Transactional
    void fullUpdateSearchIndexWithPatch() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the searchIndex using partial update
        SearchIndex partialUpdatedSearchIndex = new SearchIndex();
        partialUpdatedSearchIndex.setId(searchIndex.getId());

        partialUpdatedSearchIndex
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .indexedContent(UPDATED_INDEXED_CONTENT)
            .metadata(UPDATED_METADATA)
            .tags(UPDATED_TAGS)
            .correspondents(UPDATED_CORRESPONDENTS)
            .extractedEntities(UPDATED_EXTRACTED_ENTITIES)
            .indexedDate(UPDATED_INDEXED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSearchIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSearchIndex.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSearchIndex))
            )
            .andExpect(status().isOk());

        // Validate the SearchIndex in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSearchIndexUpdatableFieldsEquals(partialUpdatedSearchIndex, getPersistedSearchIndex(partialUpdatedSearchIndex));
    }

    @Test
    @Transactional
    void patchNonExistingSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, searchIndexDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(searchIndexDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSearchIndex() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        searchIndex.setId(longCount.incrementAndGet());

        // Create the SearchIndex
        SearchIndexDTO searchIndexDTO = searchIndexMapper.toDto(searchIndex);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSearchIndexMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(searchIndexDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SearchIndex in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteSearchIndex() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);
        searchIndexRepository.save(searchIndex);
        searchIndexSearchRepository.save(searchIndex);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the searchIndex
        restSearchIndexMockMvc
            .perform(delete(ENTITY_API_URL_ID, searchIndex.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(searchIndexSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchSearchIndex() throws Exception {
        // Initialize the database
        insertedSearchIndex = searchIndexRepository.saveAndFlush(searchIndex);
        searchIndexSearchRepository.save(searchIndex);

        // Search the searchIndex
        restSearchIndexMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + searchIndex.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(searchIndex.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].indexedContent").value(hasItem(DEFAULT_INDEXED_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].correspondents").value(hasItem(DEFAULT_CORRESPONDENTS)))
            .andExpect(jsonPath("$.[*].extractedEntities").value(hasItem(DEFAULT_EXTRACTED_ENTITIES.toString())))
            .andExpect(jsonPath("$.[*].indexedDate").value(hasItem(DEFAULT_INDEXED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    protected long getRepositoryCount() {
        return searchIndexRepository.count();
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

    protected SearchIndex getPersistedSearchIndex(SearchIndex searchIndex) {
        return searchIndexRepository.findById(searchIndex.getId()).orElseThrow();
    }

    protected void assertPersistedSearchIndexToMatchAllProperties(SearchIndex expectedSearchIndex) {
        assertSearchIndexAllPropertiesEquals(expectedSearchIndex, getPersistedSearchIndex(expectedSearchIndex));
    }

    protected void assertPersistedSearchIndexToMatchUpdatableProperties(SearchIndex expectedSearchIndex) {
        assertSearchIndexAllUpdatablePropertiesEquals(expectedSearchIndex, getPersistedSearchIndex(expectedSearchIndex));
    }
}
