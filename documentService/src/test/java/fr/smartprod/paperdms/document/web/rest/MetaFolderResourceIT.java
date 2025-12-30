package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaFolderAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.repository.MetaFolderRepository;
import fr.smartprod.paperdms.document.repository.search.MetaFolderSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaFolderMapper;
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
 * Integration tests for the {@link MetaFolderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaFolderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SHARED = false;
    private static final Boolean UPDATED_IS_SHARED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-folders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-folders/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaFolderRepository metaFolderRepository;

    @Autowired
    private MetaFolderMapper metaFolderMapper;

    @Autowired
    private MetaFolderSearchRepository metaFolderSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaFolderMockMvc;

    private MetaFolder metaFolder;

    private MetaFolder insertedMetaFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaFolder createEntity() {
        return new MetaFolder()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .path(DEFAULT_PATH)
            .isShared(DEFAULT_IS_SHARED)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaFolder createUpdatedEntity() {
        return new MetaFolder()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        metaFolder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaFolder != null) {
            metaFolderRepository.delete(insertedMetaFolder);
            metaFolderSearchRepository.delete(insertedMetaFolder);
            insertedMetaFolder = null;
        }
    }

    @Test
    @Transactional
    void createMetaFolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);
        var returnedMetaFolderDTO = om.readValue(
            restMetaFolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaFolderDTO.class
        );

        // Validate the MetaFolder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaFolder = metaFolderMapper.toEntity(returnedMetaFolderDTO);
        assertMetaFolderUpdatableFieldsEquals(returnedMetaFolder, getPersistedMetaFolder(returnedMetaFolder));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaFolder = returnedMetaFolder;
    }

    @Test
    @Transactional
    void createMetaFolderWithExistingId() throws Exception {
        // Create the MetaFolder with an existing ID
        metaFolder.setId(1L);
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        // set the field null
        metaFolder.setName(null);

        // Create the MetaFolder, which fails.
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        restMetaFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSharedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        // set the field null
        metaFolder.setIsShared(null);

        // Create the MetaFolder, which fails.
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        restMetaFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        // set the field null
        metaFolder.setCreatedDate(null);

        // Create the MetaFolder, which fails.
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        restMetaFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        // set the field null
        metaFolder.setCreatedBy(null);

        // Create the MetaFolder, which fails.
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        restMetaFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaFolders() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getMetaFolder() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get the metaFolder
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL_ID, metaFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaFolder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.isShared").value(DEFAULT_IS_SHARED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getMetaFoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        Long id = metaFolder.getId();

        defaultMetaFolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaFolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaFolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where name equals to
        defaultMetaFolderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where name in
        defaultMetaFolderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where name is not null
        defaultMetaFolderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaFoldersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where name contains
        defaultMetaFolderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where name does not contain
        defaultMetaFolderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where path equals to
        defaultMetaFolderFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where path in
        defaultMetaFolderFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where path is not null
        defaultMetaFolderFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaFoldersByPathContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where path contains
        defaultMetaFolderFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where path does not contain
        defaultMetaFolderFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByIsSharedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where isShared equals to
        defaultMetaFolderFiltering("isShared.equals=" + DEFAULT_IS_SHARED, "isShared.equals=" + UPDATED_IS_SHARED);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByIsSharedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where isShared in
        defaultMetaFolderFiltering("isShared.in=" + DEFAULT_IS_SHARED + "," + UPDATED_IS_SHARED, "isShared.in=" + UPDATED_IS_SHARED);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByIsSharedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where isShared is not null
        defaultMetaFolderFiltering("isShared.specified=true", "isShared.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdDate equals to
        defaultMetaFolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdDate in
        defaultMetaFolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdDate is not null
        defaultMetaFolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdBy equals to
        defaultMetaFolderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdBy in
        defaultMetaFolderFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdBy is not null
        defaultMetaFolderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdBy contains
        defaultMetaFolderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        // Get all the metaFolderList where createdBy does not contain
        defaultMetaFolderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaFoldersByParentIsEqualToSomething() throws Exception {
        MetaFolder parent;
        if (TestUtil.findAll(em, MetaFolder.class).isEmpty()) {
            metaFolderRepository.saveAndFlush(metaFolder);
            parent = MetaFolderResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, MetaFolder.class).get(0);
        }
        em.persist(parent);
        em.flush();
        metaFolder.setParent(parent);
        metaFolderRepository.saveAndFlush(metaFolder);
        Long parentId = parent.getId();
        // Get all the metaFolderList where parent equals to parentId
        defaultMetaFolderShouldBeFound("parentId.equals=" + parentId);

        // Get all the metaFolderList where parent equals to (parentId + 1)
        defaultMetaFolderShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultMetaFolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaFolderShouldBeFound(shouldBeFound);
        defaultMetaFolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaFolderShouldBeFound(String filter) throws Exception {
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaFolderShouldNotBeFound(String filter) throws Exception {
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaFolder() throws Exception {
        // Get the metaFolder
        restMetaFolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaFolder() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaFolderSearchRepository.save(metaFolder);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());

        // Update the metaFolder
        MetaFolder updatedMetaFolder = metaFolderRepository.findById(metaFolder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaFolder are not directly saved in db
        em.detach(updatedMetaFolder);
        updatedMetaFolder
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(updatedMetaFolder);

        restMetaFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaFolderDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaFolderToMatchAllProperties(updatedMetaFolder);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaFolder> metaFolderSearchList = Streamable.of(metaFolderSearchRepository.findAll()).toList();
                MetaFolder testMetaFolderSearch = metaFolderSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaFolderAllPropertiesEquals(testMetaFolderSearch, updatedMetaFolder);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaFolderDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaFolderWithPatch() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaFolder using partial update
        MetaFolder partialUpdatedMetaFolder = new MetaFolder();
        partialUpdatedMetaFolder.setId(metaFolder.getId());

        partialUpdatedMetaFolder.description(UPDATED_DESCRIPTION).isShared(UPDATED_IS_SHARED).createdDate(UPDATED_CREATED_DATE);

        restMetaFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaFolder))
            )
            .andExpect(status().isOk());

        // Validate the MetaFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaFolderUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaFolder, metaFolder),
            getPersistedMetaFolder(metaFolder)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaFolderWithPatch() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaFolder using partial update
        MetaFolder partialUpdatedMetaFolder = new MetaFolder();
        partialUpdatedMetaFolder.setId(metaFolder.getId());

        partialUpdatedMetaFolder
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaFolder))
            )
            .andExpect(status().isOk());

        // Validate the MetaFolder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaFolderUpdatableFieldsEquals(partialUpdatedMetaFolder, getPersistedMetaFolder(partialUpdatedMetaFolder));
    }

    @Test
    @Transactional
    void patchNonExistingMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaFolderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaFolderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        metaFolder.setId(longCount.incrementAndGet());

        // Create the MetaFolder
        MetaFolderDTO metaFolderDTO = metaFolderMapper.toDto(metaFolder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaFolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaFolderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaFolder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaFolder() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);
        metaFolderRepository.save(metaFolder);
        metaFolderSearchRepository.save(metaFolder);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaFolder
        restMetaFolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaFolder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaFolderSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaFolder() throws Exception {
        // Initialize the database
        insertedMetaFolder = metaFolderRepository.saveAndFlush(metaFolder);
        metaFolderSearchRepository.save(metaFolder);

        // Search the metaFolder
        restMetaFolderMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaFolder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaFolder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return metaFolderRepository.count();
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

    protected MetaFolder getPersistedMetaFolder(MetaFolder metaFolder) {
        return metaFolderRepository.findById(metaFolder.getId()).orElseThrow();
    }

    protected void assertPersistedMetaFolderToMatchAllProperties(MetaFolder expectedMetaFolder) {
        assertMetaFolderAllPropertiesEquals(expectedMetaFolder, getPersistedMetaFolder(expectedMetaFolder));
    }

    protected void assertPersistedMetaFolderToMatchUpdatableProperties(MetaFolder expectedMetaFolder) {
        assertMetaFolderAllUpdatablePropertiesEquals(expectedMetaFolder, getPersistedMetaFolder(expectedMetaFolder));
    }
}
