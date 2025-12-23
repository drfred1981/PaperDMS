package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.SavedSearchAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.SavedSearch;
import fr.smartprod.paperdms.document.domain.enumeration.AlertFrequency;
import fr.smartprod.paperdms.document.repository.SavedSearchRepository;
import fr.smartprod.paperdms.document.service.dto.SavedSearchDTO;
import fr.smartprod.paperdms.document.service.mapper.SavedSearchMapper;
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
 * Integration tests for the {@link SavedSearchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SavedSearchResourceIT {

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

    private static final String ENTITY_API_URL = "/api/saved-searches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SavedSearchRepository savedSearchRepository;

    @Autowired
    private SavedSearchMapper savedSearchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSavedSearchMockMvc;

    private SavedSearch savedSearch;

    private SavedSearch insertedSavedSearch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SavedSearch createEntity() {
        return new SavedSearch()
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
    public static SavedSearch createUpdatedEntity() {
        return new SavedSearch()
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
        savedSearch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSavedSearch != null) {
            savedSearchRepository.delete(insertedSavedSearch);
            insertedSavedSearch = null;
        }
    }

    @Test
    @Transactional
    void createSavedSearch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);
        var returnedSavedSearchDTO = om.readValue(
            restSavedSearchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SavedSearchDTO.class
        );

        // Validate the SavedSearch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSavedSearch = savedSearchMapper.toEntity(returnedSavedSearchDTO);
        assertSavedSearchUpdatableFieldsEquals(returnedSavedSearch, getPersistedSavedSearch(returnedSavedSearch));

        insertedSavedSearch = returnedSavedSearch;
    }

    @Test
    @Transactional
    void createSavedSearchWithExistingId() throws Exception {
        // Create the SavedSearch with an existing ID
        savedSearch.setId(1L);
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        savedSearch.setName(null);

        // Create the SavedSearch, which fails.
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        savedSearch.setIsPublic(null);

        // Create the SavedSearch, which fails.
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAlertIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        savedSearch.setIsAlert(null);

        // Create the SavedSearch, which fails.
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        savedSearch.setUserId(null);

        // Create the SavedSearch, which fails.
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        savedSearch.setCreatedDate(null);

        // Create the SavedSearch, which fails.
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        restSavedSearchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSavedSearches() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savedSearch.getId().intValue())))
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
    void getSavedSearch() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get the savedSearch
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL_ID, savedSearch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(savedSearch.getId().intValue()))
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
    void getSavedSearchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        Long id = savedSearch.getId();

        defaultSavedSearchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSavedSearchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSavedSearchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where name equals to
        defaultSavedSearchFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where name in
        defaultSavedSearchFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where name is not null
        defaultSavedSearchFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSavedSearchesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where name contains
        defaultSavedSearchFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where name does not contain
        defaultSavedSearchFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isPublic equals to
        defaultSavedSearchFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isPublic in
        defaultSavedSearchFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isPublic is not null
        defaultSavedSearchFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsAlertIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isAlert equals to
        defaultSavedSearchFiltering("isAlert.equals=" + DEFAULT_IS_ALERT, "isAlert.equals=" + UPDATED_IS_ALERT);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsAlertIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isAlert in
        defaultSavedSearchFiltering("isAlert.in=" + DEFAULT_IS_ALERT + "," + UPDATED_IS_ALERT, "isAlert.in=" + UPDATED_IS_ALERT);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByIsAlertIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where isAlert is not null
        defaultSavedSearchFiltering("isAlert.specified=true", "isAlert.specified=false");
    }

    @Test
    @Transactional
    void getAllSavedSearchesByAlertFrequencyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where alertFrequency equals to
        defaultSavedSearchFiltering("alertFrequency.equals=" + DEFAULT_ALERT_FREQUENCY, "alertFrequency.equals=" + UPDATED_ALERT_FREQUENCY);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByAlertFrequencyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where alertFrequency in
        defaultSavedSearchFiltering(
            "alertFrequency.in=" + DEFAULT_ALERT_FREQUENCY + "," + UPDATED_ALERT_FREQUENCY,
            "alertFrequency.in=" + UPDATED_ALERT_FREQUENCY
        );
    }

    @Test
    @Transactional
    void getAllSavedSearchesByAlertFrequencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where alertFrequency is not null
        defaultSavedSearchFiltering("alertFrequency.specified=true", "alertFrequency.specified=false");
    }

    @Test
    @Transactional
    void getAllSavedSearchesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where userId equals to
        defaultSavedSearchFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where userId in
        defaultSavedSearchFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where userId is not null
        defaultSavedSearchFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllSavedSearchesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where userId contains
        defaultSavedSearchFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where userId does not contain
        defaultSavedSearchFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where createdDate equals to
        defaultSavedSearchFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSavedSearchesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where createdDate in
        defaultSavedSearchFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSavedSearchesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        // Get all the savedSearchList where createdDate is not null
        defaultSavedSearchFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultSavedSearchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSavedSearchShouldBeFound(shouldBeFound);
        defaultSavedSearchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSavedSearchShouldBeFound(String filter) throws Exception {
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(savedSearch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].isAlert").value(hasItem(DEFAULT_IS_ALERT)))
            .andExpect(jsonPath("$.[*].alertFrequency").value(hasItem(DEFAULT_ALERT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSavedSearchShouldNotBeFound(String filter) throws Exception {
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSavedSearchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSavedSearch() throws Exception {
        // Get the savedSearch
        restSavedSearchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSavedSearch() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the savedSearch
        SavedSearch updatedSavedSearch = savedSearchRepository.findById(savedSearch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSavedSearch are not directly saved in db
        em.detach(updatedSavedSearch);
        updatedSavedSearch
            .name(UPDATED_NAME)
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY)
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(updatedSavedSearch);

        restSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, savedSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(savedSearchDTO))
            )
            .andExpect(status().isOk());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSavedSearchToMatchAllProperties(updatedSavedSearch);
    }

    @Test
    @Transactional
    void putNonExistingSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, savedSearchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(savedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(savedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSavedSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the savedSearch using partial update
        SavedSearch partialUpdatedSavedSearch = new SavedSearch();
        partialUpdatedSavedSearch.setId(savedSearch.getId());

        partialUpdatedSavedSearch.name(UPDATED_NAME).query(UPDATED_QUERY).isPublic(UPDATED_IS_PUBLIC);

        restSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSavedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSavedSearch))
            )
            .andExpect(status().isOk());

        // Validate the SavedSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSavedSearchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSavedSearch, savedSearch),
            getPersistedSavedSearch(savedSearch)
        );
    }

    @Test
    @Transactional
    void fullUpdateSavedSearchWithPatch() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the savedSearch using partial update
        SavedSearch partialUpdatedSavedSearch = new SavedSearch();
        partialUpdatedSavedSearch.setId(savedSearch.getId());

        partialUpdatedSavedSearch
            .name(UPDATED_NAME)
            .query(UPDATED_QUERY)
            .isPublic(UPDATED_IS_PUBLIC)
            .isAlert(UPDATED_IS_ALERT)
            .alertFrequency(UPDATED_ALERT_FREQUENCY)
            .userId(UPDATED_USER_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSavedSearch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSavedSearch))
            )
            .andExpect(status().isOk());

        // Validate the SavedSearch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSavedSearchUpdatableFieldsEquals(partialUpdatedSavedSearch, getPersistedSavedSearch(partialUpdatedSavedSearch));
    }

    @Test
    @Transactional
    void patchNonExistingSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, savedSearchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(savedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(savedSearchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSavedSearch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        savedSearch.setId(longCount.incrementAndGet());

        // Create the SavedSearch
        SavedSearchDTO savedSearchDTO = savedSearchMapper.toDto(savedSearch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSavedSearchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(savedSearchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SavedSearch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSavedSearch() throws Exception {
        // Initialize the database
        insertedSavedSearch = savedSearchRepository.saveAndFlush(savedSearch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the savedSearch
        restSavedSearchMockMvc
            .perform(delete(ENTITY_API_URL_ID, savedSearch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return savedSearchRepository.count();
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

    protected SavedSearch getPersistedSavedSearch(SavedSearch savedSearch) {
        return savedSearchRepository.findById(savedSearch.getId()).orElseThrow();
    }

    protected void assertPersistedSavedSearchToMatchAllProperties(SavedSearch expectedSavedSearch) {
        assertSavedSearchAllPropertiesEquals(expectedSavedSearch, getPersistedSavedSearch(expectedSavedSearch));
    }

    protected void assertPersistedSavedSearchToMatchUpdatableProperties(SavedSearch expectedSavedSearch) {
        assertSavedSearchAllUpdatablePropertiesEquals(expectedSavedSearch, getPersistedSavedSearch(expectedSavedSearch));
    }
}
