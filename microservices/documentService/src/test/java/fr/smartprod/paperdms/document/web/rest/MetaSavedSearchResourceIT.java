package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaSavedSearchAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import fr.smartprod.paperdms.document.domain.enumeration.AlertFrequency;
import fr.smartprod.paperdms.document.repository.MetaSavedSearchRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSavedSearchSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaSavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSavedSearchMapper;
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
 * Integration tests for the {@link MetaSavedSearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaSavedSearchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Boolean DEFAULT_IS_ALERT = false;
    private static final Boolean UPDATED_IS_ALERT = true;

    private static final AlertFrequency DEFAULT_ALERT_FREQUENCY = AlertFrequency.IMMEDIATELY;
    private static final AlertFrequency UPDATED_ALERT_FREQUENCY = AlertFrequency.HOURLY;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/meta-saved-searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-saved-searches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaSavedSearchRepository metaSavedSearchRepository;

    @Autowired
    private MetaSavedSearchMapper metaSavedSearchMapper;

    @Autowired
    private MetaSavedSearchSearchRepository metaSavedSearchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaSavedSearchMockMvc;

    private MetaSavedSearch metaSavedSearch;

    private MetaSavedSearch insertedMetaSavedSearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaSavedSearch createEntity() {
        return new MetaSavedSearch()
            .name(DEFAULT_NAME)
            .query(DEFAULT_QUERY)
            .isPublic(DEFAULT_IS_PUBLIC)
            .isAlert(DEFAULT_IS_ALERT)
            .alertFrequency(DEFAULT_ALERT_FREQUENCY)
            .userId(DEFAULT_USER_ID)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaSavedSearch createUpdatedEntity() {
        return new MetaSavedSearch()
            .name(UPDATED_NAME)
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY)
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        metaSavedSearch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaSavedSearch != null) {
            metaSavedSearchRepository.delete(insertedMetaSavedSearch);
            metaSavedSearchSearchRepository.delete(insertedMetaSavedSearch);
            insertedMetaSavedSearch = null;
        }
    }

    @Test
    @Transactional
    void createMetaSavedSearch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);
        var returnedMetaSavedSearchDTO = om.readValue(
            restMetaSavedSearchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaSavedSearchDTO.class
        );

        // Validate the MetaSavedSearch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaSavedSearch = metaSavedSearchMapper.toEntity(returnedMetaSavedSearchDTO);
        assertMetaSavedSearchUpdatableFieldsEquals(returnedMetaSavedSearch, getPersistedMetaSavedSearch(returnedMetaSavedSearch));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaSavedSearch = returnedMetaSavedSearch;
    }

    @Test
    @Transactional
    void createMetaSavedSearchWithExistingId() throws Exception {
        // Create the MetaSavedSearch with an existing ID
        metaSavedSearch.setId(1L);
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // set the field null
        metaSavedSearch.setName(null);

        // Create the MetaSavedSearch, which fails.
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // set the field null
        metaSavedSearch.setIsPublic(null);

        // Create the MetaSavedSearch, which fails.
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsAlertIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // set the field null
        metaSavedSearch.setIsAlert(null);

        // Create the MetaSavedSearch, which fails.
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // set the field null
        metaSavedSearch.setUserId(null);

        // Create the MetaSavedSearch, which fails.
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        // set the field null
        metaSavedSearch.setCreatedDate(null);

        // Create the MetaSavedSearch, which fails.
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearches() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSavedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].isAlert").value(hasItem(DEFAULT_IS_ALERT)))
            .andExpect(jsonPath("$.[*].alertFrequency").value(hasItem(DEFAULT_ALERT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMetaSavedSearch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get the metaSavedSearch
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, metaSavedSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaSavedSearch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.isAlert").value(DEFAULT_IS_ALERT))
            .andExpect(jsonPath("$.alertFrequency").value(DEFAULT_ALERT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMetaSavedSearchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        Long id = metaSavedSearch.getId();

        defaultMetaSavedSearchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaSavedSearchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaSavedSearchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where name equals to
        defaultMetaSavedSearchFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where name in
        defaultMetaSavedSearchFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where name is not null
        defaultMetaSavedSearchFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where name contains
        defaultMetaSavedSearchFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where name does not contain
        defaultMetaSavedSearchFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isPublic equals to
        defaultMetaSavedSearchFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isPublic in
        defaultMetaSavedSearchFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isPublic is not null
        defaultMetaSavedSearchFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsAlertIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isAlert equals to
        defaultMetaSavedSearchFiltering("isAlert.equals=" + DEFAULT_IS_ALERT, "isAlert.equals=" + UPDATED_IS_ALERT);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsAlertIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isAlert in
        defaultMetaSavedSearchFiltering("isAlert.in=" + DEFAULT_IS_ALERT + "," + UPDATED_IS_ALERT, "isAlert.in=" + UPDATED_IS_ALERT);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByIsAlertIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where isAlert is not null
        defaultMetaSavedSearchFiltering("isAlert.specified=true", "isAlert.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByAlertFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where alertFrequency equals to
        defaultMetaSavedSearchFiltering(
            "alertFrequency.equals=" + DEFAULT_ALERT_FREQUENCY,
            "alertFrequency.equals=" + UPDATED_ALERT_FREQUENCY
        );
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByAlertFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where alertFrequency in
        defaultMetaSavedSearchFiltering(
            "alertFrequency.in=" + DEFAULT_ALERT_FREQUENCY + "," + UPDATED_ALERT_FREQUENCY,
            "alertFrequency.in=" + UPDATED_ALERT_FREQUENCY
        );
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByAlertFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where alertFrequency is not null
        defaultMetaSavedSearchFiltering("alertFrequency.specified=true", "alertFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where userId equals to
        defaultMetaSavedSearchFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where userId in
        defaultMetaSavedSearchFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where userId is not null
        defaultMetaSavedSearchFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where userId contains
        defaultMetaSavedSearchFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where userId does not contain
        defaultMetaSavedSearchFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where createdDate equals to
        defaultMetaSavedSearchFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where createdDate in
        defaultMetaSavedSearchFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaSavedSearchesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        // Get all the metaSavedSearchList where createdDate is not null
        defaultMetaSavedSearchFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMetaSavedSearchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaSavedSearchShouldBeFound(shouldBeFound);
        defaultMetaSavedSearchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaSavedSearchShouldBeFound(String filter) throws Exception {
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSavedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].isAlert").value(hasItem(DEFAULT_IS_ALERT)))
            .andExpect(jsonPath("$.[*].alertFrequency").value(hasItem(DEFAULT_ALERT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaSavedSearchShouldNotBeFound(String filter) throws Exception {
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaSavedSearch() throws Exception {
        // Get the metaSavedSearch
        restMetaSavedSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaSavedSearch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaSavedSearchSearchRepository.save(metaSavedSearch);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());

        // Update the metaSavedSearch
        MetaSavedSearch updatedMetaSavedSearch = metaSavedSearchRepository.findById(metaSavedSearch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaSavedSearch are not directly saved in db
        em.detach(updatedMetaSavedSearch);
        updatedMetaSavedSearch
            .name(UPDATED_NAME)
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY)
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(updatedMetaSavedSearch);

        restMetaSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaSavedSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSavedSearchDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaSavedSearchToMatchAllProperties(updatedMetaSavedSearch);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaSavedSearch> metaSavedSearchSearchList = Streamable.of(metaSavedSearchSearchRepository.findAll()).toList();
                MetaSavedSearch testMetaSavedSearchSearch = metaSavedSearchSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaSavedSearchAllPropertiesEquals(testMetaSavedSearchSearch, updatedMetaSavedSearch);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaSavedSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSavedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSavedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaSavedSearchWithPatch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaSavedSearch using partial update
        MetaSavedSearch partialUpdatedMetaSavedSearch = new MetaSavedSearch();
        partialUpdatedMetaSavedSearch.setId(metaSavedSearch.getId());

        partialUpdatedMetaSavedSearch
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY);

        restMetaSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaSavedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaSavedSearch))
            )
            .andExpect(status().isOk());

        // Validate the MetaSavedSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaSavedSearchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaSavedSearch, metaSavedSearch),
            getPersistedMetaSavedSearch(metaSavedSearch)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaSavedSearchWithPatch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaSavedSearch using partial update
        MetaSavedSearch partialUpdatedMetaSavedSearch = new MetaSavedSearch();
        partialUpdatedMetaSavedSearch.setId(metaSavedSearch.getId());

        partialUpdatedMetaSavedSearch
            .name(UPDATED_NAME)
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY)
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restMetaSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaSavedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaSavedSearch))
            )
            .andExpect(status().isOk());

        // Validate the MetaSavedSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaSavedSearchUpdatableFieldsEquals(
            partialUpdatedMetaSavedSearch,
            getPersistedMetaSavedSearch(partialUpdatedMetaSavedSearch)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaSavedSearchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaSavedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaSavedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        metaSavedSearch.setId(longCount.incrementAndGet());

        // Create the MetaSavedSearch
        MetaSavedSearchDTO metaSavedSearchDTO = metaSavedSearchMapper.toDto(metaSavedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSavedSearchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaSavedSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaSavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaSavedSearch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);
        metaSavedSearchRepository.save(metaSavedSearch);
        metaSavedSearchSearchRepository.save(metaSavedSearch);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaSavedSearch
        restMetaSavedSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaSavedSearch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSavedSearchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaSavedSearch() throws Exception {
        // Initialize the database
        insertedMetaSavedSearch = metaSavedSearchRepository.saveAndFlush(metaSavedSearch);
        metaSavedSearchSearchRepository.save(metaSavedSearch);

        // Search the metaSavedSearch
        restMetaSavedSearchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaSavedSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSavedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].isAlert").value(hasItem(DEFAULT_IS_ALERT)))
            .andExpect(jsonPath("$.[*].alertFrequency").value(hasItem(DEFAULT_ALERT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return metaSavedSearchRepository.count();
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

    protected MetaSavedSearch getPersistedMetaSavedSearch(MetaSavedSearch metaSavedSearch) {
        return metaSavedSearchRepository.findById(metaSavedSearch.getId()).orElseThrow();
    }

    protected void assertPersistedMetaSavedSearchToMatchAllProperties(MetaSavedSearch expectedMetaSavedSearch) {
        assertMetaSavedSearchAllPropertiesEquals(expectedMetaSavedSearch, getPersistedMetaSavedSearch(expectedMetaSavedSearch));
    }

    protected void assertPersistedMetaSavedSearchToMatchUpdatableProperties(MetaSavedSearch expectedMetaSavedSearch) {
        assertMetaSavedSearchAllUpdatablePropertiesEquals(expectedMetaSavedSearch, getPersistedMetaSavedSearch(expectedMetaSavedSearch));
    }
}
