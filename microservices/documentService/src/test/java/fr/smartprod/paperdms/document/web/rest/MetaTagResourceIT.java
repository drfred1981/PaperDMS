package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaTagAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.repository.MetaTagRepository;
import fr.smartprod.paperdms.document.repository.search.MetaTagSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaTagMapper;
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
 * Integration tests for the {@link MetaTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaTagResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_USAGE_COUNT = 1;
    private static final Integer UPDATED_USAGE_COUNT = 2;
    private static final Integer SMALLER_USAGE_COUNT = 1 - 1;

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-tags/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaTagRepository metaTagRepository;

    @Autowired
    private MetaTagMapper metaTagMapper;

    @Autowired
    private MetaTagSearchRepository metaTagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaTagMockMvc;

    private MetaTag metaTag;

    private MetaTag insertedMetaTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaTag createEntity() {
        return new MetaTag()
            .name(DEFAULT_NAME)
            .color(DEFAULT_COLOR)
            .description(DEFAULT_DESCRIPTION)
            .usageCount(DEFAULT_USAGE_COUNT)
            .isSystem(DEFAULT_IS_SYSTEM)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaTag createUpdatedEntity() {
        return new MetaTag()
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .description(UPDATED_DESCRIPTION)
            .usageCount(UPDATED_USAGE_COUNT)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        metaTag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaTag != null) {
            metaTagRepository.delete(insertedMetaTag);
            metaTagSearchRepository.delete(insertedMetaTag);
            insertedMetaTag = null;
        }
    }

    @Test
    @Transactional
    void createMetaTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);
        var returnedMetaTagDTO = om.readValue(
            restMetaTagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaTagDTO.class
        );

        // Validate the MetaTag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaTag = metaTagMapper.toEntity(returnedMetaTagDTO);
        assertMetaTagUpdatableFieldsEquals(returnedMetaTag, getPersistedMetaTag(returnedMetaTag));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaTag = returnedMetaTag;
    }

    @Test
    @Transactional
    void createMetaTagWithExistingId() throws Exception {
        // Create the MetaTag with an existing ID
        metaTag.setId(1L);
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        // set the field null
        metaTag.setName(null);

        // Create the MetaTag, which fails.
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        restMetaTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        // set the field null
        metaTag.setIsSystem(null);

        // Create the MetaTag, which fails.
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        restMetaTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        // set the field null
        metaTag.setCreatedDate(null);

        // Create the MetaTag, which fails.
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        restMetaTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        // set the field null
        metaTag.setCreatedBy(null);

        // Create the MetaTag, which fails.
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        restMetaTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaTags() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getMetaTag() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get the metaTag
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL_ID, metaTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaTag.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.usageCount").value(DEFAULT_USAGE_COUNT))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getMetaTagsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        Long id = metaTag.getId();

        defaultMetaTagFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaTagFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaTagFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaTagsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where name equals to
        defaultMetaTagFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaTagsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where name in
        defaultMetaTagFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaTagsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where name is not null
        defaultMetaTagFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where name contains
        defaultMetaTagFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaTagsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where name does not contain
        defaultMetaTagFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaTagsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where color equals to
        defaultMetaTagFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaTagsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where color in
        defaultMetaTagFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaTagsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where color is not null
        defaultMetaTagFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where color contains
        defaultMetaTagFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaTagsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where color does not contain
        defaultMetaTagFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaTagsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where description equals to
        defaultMetaTagFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMetaTagsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where description in
        defaultMetaTagFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllMetaTagsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where description is not null
        defaultMetaTagFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where description contains
        defaultMetaTagFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMetaTagsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where description does not contain
        defaultMetaTagFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount equals to
        defaultMetaTagFiltering("usageCount.equals=" + DEFAULT_USAGE_COUNT, "usageCount.equals=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount in
        defaultMetaTagFiltering("usageCount.in=" + DEFAULT_USAGE_COUNT + "," + UPDATED_USAGE_COUNT, "usageCount.in=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount is not null
        defaultMetaTagFiltering("usageCount.specified=true", "usageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount is greater than or equal to
        defaultMetaTagFiltering(
            "usageCount.greaterThanOrEqual=" + DEFAULT_USAGE_COUNT,
            "usageCount.greaterThanOrEqual=" + UPDATED_USAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount is less than or equal to
        defaultMetaTagFiltering("usageCount.lessThanOrEqual=" + DEFAULT_USAGE_COUNT, "usageCount.lessThanOrEqual=" + SMALLER_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount is less than
        defaultMetaTagFiltering("usageCount.lessThan=" + UPDATED_USAGE_COUNT, "usageCount.lessThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllMetaTagsByUsageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where usageCount is greater than
        defaultMetaTagFiltering("usageCount.greaterThan=" + SMALLER_USAGE_COUNT, "usageCount.greaterThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllMetaTagsByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where isSystem equals to
        defaultMetaTagFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllMetaTagsByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where isSystem in
        defaultMetaTagFiltering("isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM, "isSystem.in=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllMetaTagsByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where isSystem is not null
        defaultMetaTagFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdDate equals to
        defaultMetaTagFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdDate in
        defaultMetaTagFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdDate is not null
        defaultMetaTagFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdBy equals to
        defaultMetaTagFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdBy in
        defaultMetaTagFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdBy is not null
        defaultMetaTagFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdBy contains
        defaultMetaTagFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaTagsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        // Get all the metaTagList where createdBy does not contain
        defaultMetaTagFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaTagsByMetaMetaTagCategoryIsEqualToSomething() throws Exception {
        MetaMetaTagCategory metaMetaTagCategory;
        if (TestUtil.findAll(em, MetaMetaTagCategory.class).isEmpty()) {
            metaTagRepository.saveAndFlush(metaTag);
            metaMetaTagCategory = MetaMetaTagCategoryResourceIT.createEntity();
        } else {
            metaMetaTagCategory = TestUtil.findAll(em, MetaMetaTagCategory.class).get(0);
        }
        em.persist(metaMetaTagCategory);
        em.flush();
        metaTag.setMetaMetaTagCategory(metaMetaTagCategory);
        metaTagRepository.saveAndFlush(metaTag);
        Long metaMetaTagCategoryId = metaMetaTagCategory.getId();
        // Get all the metaTagList where metaMetaTagCategory equals to metaMetaTagCategoryId
        defaultMetaTagShouldBeFound("metaMetaTagCategoryId.equals=" + metaMetaTagCategoryId);

        // Get all the metaTagList where metaMetaTagCategory equals to (metaMetaTagCategoryId + 1)
        defaultMetaTagShouldNotBeFound("metaMetaTagCategoryId.equals=" + (metaMetaTagCategoryId + 1));
    }

    private void defaultMetaTagFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaTagShouldBeFound(shouldBeFound);
        defaultMetaTagShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaTagShouldBeFound(String filter) throws Exception {
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaTagShouldNotBeFound(String filter) throws Exception {
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaTag() throws Exception {
        // Get the metaTag
        restMetaTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaTag() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaTagSearchRepository.save(metaTag);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());

        // Update the metaTag
        MetaTag updatedMetaTag = metaTagRepository.findById(metaTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaTag are not directly saved in db
        em.detach(updatedMetaTag);
        updatedMetaTag
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .description(UPDATED_DESCRIPTION)
            .usageCount(UPDATED_USAGE_COUNT)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(updatedMetaTag);

        restMetaTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaTagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaTagToMatchAllProperties(updatedMetaTag);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaTag> metaTagSearchList = Streamable.of(metaTagSearchRepository.findAll()).toList();
                MetaTag testMetaTagSearch = metaTagSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaTagAllPropertiesEquals(testMetaTagSearch, updatedMetaTag);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaTagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaTagWithPatch() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaTag using partial update
        MetaTag partialUpdatedMetaTag = new MetaTag();
        partialUpdatedMetaTag.setId(metaTag.getId());

        partialUpdatedMetaTag.description(UPDATED_DESCRIPTION).usageCount(UPDATED_USAGE_COUNT).isSystem(UPDATED_IS_SYSTEM);

        restMetaTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaTag))
            )
            .andExpect(status().isOk());

        // Validate the MetaTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaTagUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMetaTag, metaTag), getPersistedMetaTag(metaTag));
    }

    @Test
    @Transactional
    void fullUpdateMetaTagWithPatch() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaTag using partial update
        MetaTag partialUpdatedMetaTag = new MetaTag();
        partialUpdatedMetaTag.setId(metaTag.getId());

        partialUpdatedMetaTag
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .description(UPDATED_DESCRIPTION)
            .usageCount(UPDATED_USAGE_COUNT)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaTag))
            )
            .andExpect(status().isOk());

        // Validate the MetaTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaTagUpdatableFieldsEquals(partialUpdatedMetaTag, getPersistedMetaTag(partialUpdatedMetaTag));
    }

    @Test
    @Transactional
    void patchNonExistingMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        metaTag.setId(longCount.incrementAndGet());

        // Create the MetaTag
        MetaTagDTO metaTagDTO = metaTagMapper.toDto(metaTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaTag() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);
        metaTagRepository.save(metaTag);
        metaTagSearchRepository.save(metaTag);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaTag
        restMetaTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaTag() throws Exception {
        // Initialize the database
        insertedMetaTag = metaTagRepository.saveAndFlush(metaTag);
        metaTagSearchRepository.save(metaTag);

        // Search the metaTag
        restMetaTagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return metaTagRepository.count();
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

    protected MetaTag getPersistedMetaTag(MetaTag metaTag) {
        return metaTagRepository.findById(metaTag.getId()).orElseThrow();
    }

    protected void assertPersistedMetaTagToMatchAllProperties(MetaTag expectedMetaTag) {
        assertMetaTagAllPropertiesEquals(expectedMetaTag, getPersistedMetaTag(expectedMetaTag));
    }

    protected void assertPersistedMetaTagToMatchUpdatableProperties(MetaTag expectedMetaTag) {
        assertMetaTagAllUpdatablePropertiesEquals(expectedMetaTag, getPersistedMetaTag(expectedMetaTag));
    }
}
