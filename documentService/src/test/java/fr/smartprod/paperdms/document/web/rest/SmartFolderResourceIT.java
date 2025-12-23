package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.SmartFolderAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.SmartFolder;
import fr.smartprod.paperdms.document.repository.SmartFolderRepository;
import fr.smartprod.paperdms.document.service.dto.SmartFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.SmartFolderMapper;
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
 * Integration tests for the {@link SmartFolderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SmartFolderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_QUERY_JSON = "AAAAAAAAAA";
    private static final String UPDATED_QUERY_JSON = "BBBBBBBBBB";

    private static final Boolean DEFAULT_AUTO_REFRESH = false;
    private static final Boolean UPDATED_AUTO_REFRESH = true;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/smart-folders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SmartFolderRepository smartFolderRepository;

    @Autowired
    private SmartFolderMapper smartFolderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSmartFolderMockMvc;

    private SmartFolder smartFolder;

    private SmartFolder insertedSmartFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmartFolder createEntity() {
        return new SmartFolder()
            .name(DEFAULT_NAME)
            .queryJson(DEFAULT_QUERY_JSON)
            .autoRefresh(DEFAULT_AUTO_REFRESH)
            .isPublic(DEFAULT_IS_PUBLIC)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SmartFolder createUpdatedEntity() {
        return new SmartFolder()
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        smartFolder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSmartFolder != null) {
            smartFolderRepository.delete(insertedSmartFolder);
            insertedSmartFolder = null;
        }
    }

    @Test
    @Transactional
    void createSmartFolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);
        var returnedSmartFolderDTO = om.readValue(
            restSmartFolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SmartFolderDTO.class
        );

        // Validate the SmartFolder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSmartFolder = smartFolderMapper.toEntity(returnedSmartFolderDTO);
        assertSmartFolderUpdatableFieldsEquals(returnedSmartFolder, getPersistedSmartFolder(returnedSmartFolder));

        insertedSmartFolder = returnedSmartFolder;
    }

    @Test
    @Transactional
    void createSmartFolderWithExistingId() throws Exception {
        // Create the SmartFolder with an existing ID
        smartFolder.setId(1L);
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        smartFolder.setName(null);

        // Create the SmartFolder, which fails.
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAutoRefreshIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        smartFolder.setAutoRefresh(null);

        // Create the SmartFolder, which fails.
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        smartFolder.setIsPublic(null);

        // Create the SmartFolder, which fails.
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        smartFolder.setCreatedBy(null);

        // Create the SmartFolder, which fails.
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        smartFolder.setCreatedDate(null);

        // Create the SmartFolder, which fails.
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        restSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSmartFolders() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].queryJson").value(hasItem(DEFAULT_QUERY_JSON)))
            .andExpect(jsonPath("$.[*].autoRefresh").value(hasItem(DEFAULT_AUTO_REFRESH)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getSmartFolder() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get the smartFolder
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL_ID, smartFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(smartFolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.queryJson").value(DEFAULT_QUERY_JSON))
            .andExpect(jsonPath("$.autoRefresh").value(DEFAULT_AUTO_REFRESH))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getSmartFoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        Long id = smartFolder.getId();

        defaultSmartFolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultSmartFolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultSmartFolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where name equals to
        defaultSmartFolderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where name in
        defaultSmartFolderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where name is not null
        defaultSmartFolderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllSmartFoldersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where name contains
        defaultSmartFolderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where name does not contain
        defaultSmartFolderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByAutoRefreshIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where autoRefresh equals to
        defaultSmartFolderFiltering("autoRefresh.equals=" + DEFAULT_AUTO_REFRESH, "autoRefresh.equals=" + UPDATED_AUTO_REFRESH);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByAutoRefreshIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where autoRefresh in
        defaultSmartFolderFiltering(
            "autoRefresh.in=" + DEFAULT_AUTO_REFRESH + "," + UPDATED_AUTO_REFRESH,
            "autoRefresh.in=" + UPDATED_AUTO_REFRESH
        );
    }

    @Test
    @Transactional
    void getAllSmartFoldersByAutoRefreshIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where autoRefresh is not null
        defaultSmartFolderFiltering("autoRefresh.specified=true", "autoRefresh.specified=false");
    }

    @Test
    @Transactional
    void getAllSmartFoldersByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where isPublic equals to
        defaultSmartFolderFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where isPublic in
        defaultSmartFolderFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where isPublic is not null
        defaultSmartFolderFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdBy equals to
        defaultSmartFolderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdBy in
        defaultSmartFolderFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdBy is not null
        defaultSmartFolderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdBy contains
        defaultSmartFolderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdBy does not contain
        defaultSmartFolderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdDate equals to
        defaultSmartFolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdDate in
        defaultSmartFolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllSmartFoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        // Get all the smartFolderList where createdDate is not null
        defaultSmartFolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultSmartFolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultSmartFolderShouldBeFound(shouldBeFound);
        defaultSmartFolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultSmartFolderShouldBeFound(String filter) throws Exception {
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(smartFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].queryJson").value(hasItem(DEFAULT_QUERY_JSON)))
            .andExpect(jsonPath("$.[*].autoRefresh").value(hasItem(DEFAULT_AUTO_REFRESH)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultSmartFolderShouldNotBeFound(String filter) throws Exception {
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingSmartFolder() throws Exception {
        // Get the smartFolder
        restSmartFolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSmartFolder() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smartFolder
        SmartFolder updatedSmartFolder = smartFolderRepository.findById(smartFolder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSmartFolder are not directly saved in db
        em.detach(updatedSmartFolder);
        updatedSmartFolder
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(updatedSmartFolder);

        restSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smartFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smartFolderDTO))
            )
            .andExpect(status().isOk());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSmartFolderToMatchAllProperties(updatedSmartFolder);
    }

    @Test
    @Transactional
    void putNonExistingSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, smartFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(smartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSmartFolderWithPatch() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smartFolder using partial update
        SmartFolder partialUpdatedSmartFolder = new SmartFolder();
        partialUpdatedSmartFolder.setId(smartFolder.getId());

        partialUpdatedSmartFolder
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmartFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmartFolder))
            )
            .andExpect(status().isOk());

        // Validate the SmartFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmartFolderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSmartFolder, smartFolder),
            getPersistedSmartFolder(smartFolder)
        );
    }

    @Test
    @Transactional
    void fullUpdateSmartFolderWithPatch() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the smartFolder using partial update
        SmartFolder partialUpdatedSmartFolder = new SmartFolder();
        partialUpdatedSmartFolder.setId(smartFolder.getId());

        partialUpdatedSmartFolder
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSmartFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSmartFolder))
            )
            .andExpect(status().isOk());

        // Validate the SmartFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSmartFolderUpdatableFieldsEquals(partialUpdatedSmartFolder, getPersistedSmartFolder(partialUpdatedSmartFolder));
    }

    @Test
    @Transactional
    void patchNonExistingSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, smartFolderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(smartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        smartFolder.setId(longCount.incrementAndGet());

        // Create the SmartFolder
        SmartFolderDTO smartFolderDTO = smartFolderMapper.toDto(smartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSmartFolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(smartFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSmartFolder() throws Exception {
        // Initialize the database
        insertedSmartFolder = smartFolderRepository.saveAndFlush(smartFolder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the smartFolder
        restSmartFolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, smartFolder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return smartFolderRepository.count();
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

    protected SmartFolder getPersistedSmartFolder(SmartFolder smartFolder) {
        return smartFolderRepository.findById(smartFolder.getId()).orElseThrow();
    }

    protected void assertPersistedSmartFolderToMatchAllProperties(SmartFolder expectedSmartFolder) {
        assertSmartFolderAllPropertiesEquals(expectedSmartFolder, getPersistedSmartFolder(expectedSmartFolder));
    }

    protected void assertPersistedSmartFolderToMatchUpdatableProperties(SmartFolder expectedSmartFolder) {
        assertSmartFolderAllUpdatablePropertiesEquals(expectedSmartFolder, getPersistedSmartFolder(expectedSmartFolder));
    }
}
