package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaSmartFolderAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import fr.smartprod.paperdms.document.repository.MetaSmartFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaSmartFolderSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaSmartFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaSmartFolderMapper;
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
 * Integration tests for the {@link MetaSmartFolderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaSmartFolderResourceIT {

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

    private static final String ENTITY_API_URL = "/api/meta-smart-folders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-smart-folders/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaSmartFolderRepository metaSmartFolderRepository;

    @Autowired
    private MetaSmartFolderMapper metaSmartFolderMapper;

    @Autowired
    private MetaSmartFolderSearchRepository metaSmartFolderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaSmartFolderMockMvc;

    private MetaSmartFolder metaSmartFolder;

    private MetaSmartFolder insertedMetaSmartFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaSmartFolder createEntity() {
        return new MetaSmartFolder()
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
    public static MetaSmartFolder createUpdatedEntity() {
        return new MetaSmartFolder()
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        metaSmartFolder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaSmartFolder != null) {
            metaSmartFolderRepository.delete(insertedMetaSmartFolder);
            metaSmartFolderSearchRepository.delete(insertedMetaSmartFolder);
            insertedMetaSmartFolder = null;
        }
    }

    @Test
    @Transactional
    void createMetaSmartFolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);
        var returnedMetaSmartFolderDTO = om.readValue(
            restMetaSmartFolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaSmartFolderDTO.class
        );

        // Validate the MetaSmartFolder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaSmartFolder = metaSmartFolderMapper.toEntity(returnedMetaSmartFolderDTO);
        assertMetaSmartFolderUpdatableFieldsEquals(returnedMetaSmartFolder, getPersistedMetaSmartFolder(returnedMetaSmartFolder));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaSmartFolder = returnedMetaSmartFolder;
    }

    @Test
    @Transactional
    void createMetaSmartFolderWithExistingId() throws Exception {
        // Create the MetaSmartFolder with an existing ID
        metaSmartFolder.setId(1L);
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // set the field null
        metaSmartFolder.setName(null);

        // Create the MetaSmartFolder, which fails.
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAutoRefreshIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // set the field null
        metaSmartFolder.setAutoRefresh(null);

        // Create the MetaSmartFolder, which fails.
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // set the field null
        metaSmartFolder.setIsPublic(null);

        // Create the MetaSmartFolder, which fails.
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // set the field null
        metaSmartFolder.setCreatedBy(null);

        // Create the MetaSmartFolder, which fails.
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        // set the field null
        metaSmartFolder.setCreatedDate(null);

        // Create the MetaSmartFolder, which fails.
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaSmartFolders() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSmartFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].queryJson").value(hasItem(DEFAULT_QUERY_JSON)))
            .andExpect(jsonPath("$.[*].autoRefresh").value(hasItem(DEFAULT_AUTO_REFRESH)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMetaSmartFolder() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get the metaSmartFolder
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL_ID, metaSmartFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaSmartFolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.queryJson").value(DEFAULT_QUERY_JSON))
            .andExpect(jsonPath("$.autoRefresh").value(DEFAULT_AUTO_REFRESH))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMetaSmartFoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        Long id = metaSmartFolder.getId();

        defaultMetaSmartFolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaSmartFolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaSmartFolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where name equals to
        defaultMetaSmartFolderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where name in
        defaultMetaSmartFolderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where name is not null
        defaultMetaSmartFolderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where name contains
        defaultMetaSmartFolderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where name does not contain
        defaultMetaSmartFolderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByAutoRefreshIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where autoRefresh equals to
        defaultMetaSmartFolderFiltering("autoRefresh.equals=" + DEFAULT_AUTO_REFRESH, "autoRefresh.equals=" + UPDATED_AUTO_REFRESH);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByAutoRefreshIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where autoRefresh in
        defaultMetaSmartFolderFiltering(
            "autoRefresh.in=" + DEFAULT_AUTO_REFRESH + "," + UPDATED_AUTO_REFRESH,
            "autoRefresh.in=" + UPDATED_AUTO_REFRESH
        );
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByAutoRefreshIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where autoRefresh is not null
        defaultMetaSmartFolderFiltering("autoRefresh.specified=true", "autoRefresh.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where isPublic equals to
        defaultMetaSmartFolderFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where isPublic in
        defaultMetaSmartFolderFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where isPublic is not null
        defaultMetaSmartFolderFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdBy equals to
        defaultMetaSmartFolderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdBy in
        defaultMetaSmartFolderFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdBy is not null
        defaultMetaSmartFolderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdBy contains
        defaultMetaSmartFolderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdBy does not contain
        defaultMetaSmartFolderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdDate equals to
        defaultMetaSmartFolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdDate in
        defaultMetaSmartFolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaSmartFoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        // Get all the metaSmartFolderList where createdDate is not null
        defaultMetaSmartFolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMetaSmartFolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaSmartFolderShouldBeFound(shouldBeFound);
        defaultMetaSmartFolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaSmartFolderShouldBeFound(String filter) throws Exception {
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSmartFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].queryJson").value(hasItem(DEFAULT_QUERY_JSON)))
            .andExpect(jsonPath("$.[*].autoRefresh").value(hasItem(DEFAULT_AUTO_REFRESH)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaSmartFolderShouldNotBeFound(String filter) throws Exception {
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaSmartFolder() throws Exception {
        // Get the metaSmartFolder
        restMetaSmartFolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaSmartFolder() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaSmartFolderSearchRepository.save(metaSmartFolder);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());

        // Update the metaSmartFolder
        MetaSmartFolder updatedMetaSmartFolder = metaSmartFolderRepository.findById(metaSmartFolder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaSmartFolder are not directly saved in db
        em.detach(updatedMetaSmartFolder);
        updatedMetaSmartFolder
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(updatedMetaSmartFolder);

        restMetaSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaSmartFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSmartFolderDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaSmartFolderToMatchAllProperties(updatedMetaSmartFolder);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaSmartFolder> metaSmartFolderSearchList = Streamable.of(metaSmartFolderSearchRepository.findAll()).toList();
                MetaSmartFolder testMetaSmartFolderSearch = metaSmartFolderSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaSmartFolderAllPropertiesEquals(testMetaSmartFolderSearch, updatedMetaSmartFolder);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaSmartFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSmartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaSmartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaSmartFolderWithPatch() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaSmartFolder using partial update
        MetaSmartFolder partialUpdatedMetaSmartFolder = new MetaSmartFolder();
        partialUpdatedMetaSmartFolder.setId(metaSmartFolder.getId());

        partialUpdatedMetaSmartFolder.name(UPDATED_NAME).autoRefresh(UPDATED_AUTO_REFRESH);

        restMetaSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaSmartFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaSmartFolder))
            )
            .andExpect(status().isOk());

        // Validate the MetaSmartFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaSmartFolderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaSmartFolder, metaSmartFolder),
            getPersistedMetaSmartFolder(metaSmartFolder)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaSmartFolderWithPatch() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaSmartFolder using partial update
        MetaSmartFolder partialUpdatedMetaSmartFolder = new MetaSmartFolder();
        partialUpdatedMetaSmartFolder.setId(metaSmartFolder.getId());

        partialUpdatedMetaSmartFolder
            .name(UPDATED_NAME)
            .queryJson(UPDATED_QUERY_JSON)
            .autoRefresh(UPDATED_AUTO_REFRESH)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restMetaSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaSmartFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaSmartFolder))
            )
            .andExpect(status().isOk());

        // Validate the MetaSmartFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaSmartFolderUpdatableFieldsEquals(
            partialUpdatedMetaSmartFolder,
            getPersistedMetaSmartFolder(partialUpdatedMetaSmartFolder)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaSmartFolderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaSmartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaSmartFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaSmartFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        metaSmartFolder.setId(longCount.incrementAndGet());

        // Create the MetaSmartFolder
        MetaSmartFolderDTO metaSmartFolderDTO = metaSmartFolderMapper.toDto(metaSmartFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaSmartFolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaSmartFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaSmartFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaSmartFolder() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);
        metaSmartFolderRepository.save(metaSmartFolder);
        metaSmartFolderSearchRepository.save(metaSmartFolder);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaSmartFolder
        restMetaSmartFolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaSmartFolder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaSmartFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaSmartFolder() throws Exception {
        // Initialize the database
        insertedMetaSmartFolder = metaSmartFolderRepository.saveAndFlush(metaSmartFolder);
        metaSmartFolderSearchRepository.save(metaSmartFolder);

        // Search the metaSmartFolder
        restMetaSmartFolderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaSmartFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaSmartFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].queryJson").value(hasItem(DEFAULT_QUERY_JSON.toString())))
            .andExpect(jsonPath("$.[*].autoRefresh").value(hasItem(DEFAULT_AUTO_REFRESH)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return metaSmartFolderRepository.count();
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

    protected MetaSmartFolder getPersistedMetaSmartFolder(MetaSmartFolder metaSmartFolder) {
        return metaSmartFolderRepository.findById(metaSmartFolder.getId()).orElseThrow();
    }

    protected void assertPersistedMetaSmartFolderToMatchAllProperties(MetaSmartFolder expectedMetaSmartFolder) {
        assertMetaSmartFolderAllPropertiesEquals(expectedMetaSmartFolder, getPersistedMetaSmartFolder(expectedMetaSmartFolder));
    }

    protected void assertPersistedMetaSmartFolderToMatchUpdatableProperties(MetaSmartFolder expectedMetaSmartFolder) {
        assertMetaSmartFolderAllUpdatablePropertiesEquals(expectedMetaSmartFolder, getPersistedMetaSmartFolder(expectedMetaSmartFolder));
    }
}
