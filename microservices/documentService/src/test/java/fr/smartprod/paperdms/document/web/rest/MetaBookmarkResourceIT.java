package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaBookmarkAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaBookmark;
import fr.smartprod.paperdms.document.domain.enumeration.MetaBookmarkType;
import fr.smartprod.paperdms.document.repository.MetaBookmarkRepository;
import fr.smartprod.paperdms.document.repository.search.MetaBookmarkSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaBookmarkDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaBookmarkMapper;
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
 * Integration tests for the {@link MetaBookmarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaBookmarkResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final MetaBookmarkType DEFAULT_ENTITY_TYPE = MetaBookmarkType.DOCUMENT;
    private static final MetaBookmarkType UPDATED_ENTITY_TYPE = MetaBookmarkType.FOLDER;

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/meta-bookmarks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-bookmarks/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaBookmarkRepository metaBookmarkRepository;

    @Autowired
    private MetaBookmarkMapper metaBookmarkMapper;

    @Autowired
    private MetaBookmarkSearchRepository metaBookmarkSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaBookmarkMockMvc;

    private MetaBookmark metaBookmark;

    private MetaBookmark insertedMetaBookmark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaBookmark createEntity() {
        return new MetaBookmark()
            .userId(DEFAULT_USER_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityName(DEFAULT_ENTITY_NAME)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaBookmark createUpdatedEntity() {
        return new MetaBookmark()
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        metaBookmark = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaBookmark != null) {
            metaBookmarkRepository.delete(insertedMetaBookmark);
            metaBookmarkSearchRepository.delete(insertedMetaBookmark);
            insertedMetaBookmark = null;
        }
    }

    @Test
    @Transactional
    void createMetaBookmark() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);
        var returnedMetaBookmarkDTO = om.readValue(
            restMetaBookmarkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaBookmarkDTO.class
        );

        // Validate the MetaBookmark in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaBookmark = metaBookmarkMapper.toEntity(returnedMetaBookmarkDTO);
        assertMetaBookmarkUpdatableFieldsEquals(returnedMetaBookmark, getPersistedMetaBookmark(returnedMetaBookmark));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaBookmark = returnedMetaBookmark;
    }

    @Test
    @Transactional
    void createMetaBookmarkWithExistingId() throws Exception {
        // Create the MetaBookmark with an existing ID
        metaBookmark.setId(1L);
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        // set the field null
        metaBookmark.setUserId(null);

        // Create the MetaBookmark, which fails.
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        restMetaBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        // set the field null
        metaBookmark.setEntityType(null);

        // Create the MetaBookmark, which fails.
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        restMetaBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkEntityNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        // set the field null
        metaBookmark.setEntityName(null);

        // Create the MetaBookmark, which fails.
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        restMetaBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        // set the field null
        metaBookmark.setCreatedDate(null);

        // Create the MetaBookmark, which fails.
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        restMetaBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaBookmarks() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaBookmark.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getMetaBookmark() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get the metaBookmark
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL_ID, metaBookmark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaBookmark.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getMetaBookmarksByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        Long id = metaBookmark.getId();

        defaultMetaBookmarkFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaBookmarkFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaBookmarkFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where userId equals to
        defaultMetaBookmarkFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where userId in
        defaultMetaBookmarkFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where userId is not null
        defaultMetaBookmarkFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where userId contains
        defaultMetaBookmarkFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where userId does not contain
        defaultMetaBookmarkFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityType equals to
        defaultMetaBookmarkFiltering("entityType.equals=" + DEFAULT_ENTITY_TYPE, "entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityType in
        defaultMetaBookmarkFiltering(
            "entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE,
            "entityType.in=" + UPDATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityType is not null
        defaultMetaBookmarkFiltering("entityType.specified=true", "entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityName equals to
        defaultMetaBookmarkFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityName in
        defaultMetaBookmarkFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityName is not null
        defaultMetaBookmarkFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityName contains
        defaultMetaBookmarkFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where entityName does not contain
        defaultMetaBookmarkFiltering(
            "entityName.doesNotContain=" + UPDATED_ENTITY_NAME,
            "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where createdDate equals to
        defaultMetaBookmarkFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where createdDate in
        defaultMetaBookmarkFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaBookmarksByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        // Get all the metaBookmarkList where createdDate is not null
        defaultMetaBookmarkFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMetaBookmarkFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaBookmarkShouldBeFound(shouldBeFound);
        defaultMetaBookmarkShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaBookmarkShouldBeFound(String filter) throws Exception {
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaBookmark.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaBookmarkShouldNotBeFound(String filter) throws Exception {
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaBookmark() throws Exception {
        // Get the metaBookmark
        restMetaBookmarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaBookmark() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaBookmarkSearchRepository.save(metaBookmark);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());

        // Update the metaBookmark
        MetaBookmark updatedMetaBookmark = metaBookmarkRepository.findById(metaBookmark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaBookmark are not directly saved in db
        em.detach(updatedMetaBookmark);
        updatedMetaBookmark
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .createdDate(UPDATED_CREATED_DATE);
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(updatedMetaBookmark);

        restMetaBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaBookmarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaBookmarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaBookmarkToMatchAllProperties(updatedMetaBookmark);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaBookmark> metaBookmarkSearchList = Streamable.of(metaBookmarkSearchRepository.findAll()).toList();
                MetaBookmark testMetaBookmarkSearch = metaBookmarkSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaBookmarkAllPropertiesEquals(testMetaBookmarkSearch, updatedMetaBookmark);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaBookmarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaBookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaBookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaBookmarkWithPatch() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaBookmark using partial update
        MetaBookmark partialUpdatedMetaBookmark = new MetaBookmark();
        partialUpdatedMetaBookmark.setId(metaBookmark.getId());

        partialUpdatedMetaBookmark.entityType(UPDATED_ENTITY_TYPE).entityName(UPDATED_ENTITY_NAME).createdDate(UPDATED_CREATED_DATE);

        restMetaBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaBookmark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaBookmark))
            )
            .andExpect(status().isOk());

        // Validate the MetaBookmark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaBookmarkUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaBookmark, metaBookmark),
            getPersistedMetaBookmark(metaBookmark)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaBookmarkWithPatch() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaBookmark using partial update
        MetaBookmark partialUpdatedMetaBookmark = new MetaBookmark();
        partialUpdatedMetaBookmark.setId(metaBookmark.getId());

        partialUpdatedMetaBookmark
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .createdDate(UPDATED_CREATED_DATE);

        restMetaBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaBookmark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaBookmark))
            )
            .andExpect(status().isOk());

        // Validate the MetaBookmark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaBookmarkUpdatableFieldsEquals(partialUpdatedMetaBookmark, getPersistedMetaBookmark(partialUpdatedMetaBookmark));
    }

    @Test
    @Transactional
    void patchNonExistingMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaBookmarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaBookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaBookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        metaBookmark.setId(longCount.incrementAndGet());

        // Create the MetaBookmark
        MetaBookmarkDTO metaBookmarkDTO = metaBookmarkMapper.toDto(metaBookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaBookmarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaBookmarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaBookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaBookmark() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);
        metaBookmarkRepository.save(metaBookmark);
        metaBookmarkSearchRepository.save(metaBookmark);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaBookmark
        restMetaBookmarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaBookmark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaBookmarkSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaBookmark() throws Exception {
        // Initialize the database
        insertedMetaBookmark = metaBookmarkRepository.saveAndFlush(metaBookmark);
        metaBookmarkSearchRepository.save(metaBookmark);

        // Search the metaBookmark
        restMetaBookmarkMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaBookmark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaBookmark.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return metaBookmarkRepository.count();
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

    protected MetaBookmark getPersistedMetaBookmark(MetaBookmark metaBookmark) {
        return metaBookmarkRepository.findById(metaBookmark.getId()).orElseThrow();
    }

    protected void assertPersistedMetaBookmarkToMatchAllProperties(MetaBookmark expectedMetaBookmark) {
        assertMetaBookmarkAllPropertiesEquals(expectedMetaBookmark, getPersistedMetaBookmark(expectedMetaBookmark));
    }

    protected void assertPersistedMetaBookmarkToMatchUpdatableProperties(MetaBookmark expectedMetaBookmark) {
        assertMetaBookmarkAllUpdatablePropertiesEquals(expectedMetaBookmark, getPersistedMetaBookmark(expectedMetaBookmark));
    }
}
