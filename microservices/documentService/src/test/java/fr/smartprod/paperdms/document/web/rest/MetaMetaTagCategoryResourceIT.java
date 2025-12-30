package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaMetaTagCategoryAsserts.*;
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
import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.repository.MetaMetaTagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.MetaMetaTagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaMetaTagCategoryMapper;
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
 * Integration tests for the {@link MetaMetaTagCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaMetaTagCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;
    private static final Integer SMALLER_DISPLAY_ORDER = 1 - 1;

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-meta-tag-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-meta-tag-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaMetaTagCategoryRepository metaMetaTagCategoryRepository;

    @Autowired
    private MetaMetaTagCategoryMapper metaMetaTagCategoryMapper;

    @Autowired
    private MetaMetaTagCategorySearchRepository metaMetaTagCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaMetaTagCategoryMockMvc;

    private MetaMetaTagCategory metaMetaTagCategory;

    private MetaMetaTagCategory insertedMetaMetaTagCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaMetaTagCategory createEntity() {
        return new MetaMetaTagCategory()
            .name(DEFAULT_NAME)
            .color(DEFAULT_COLOR)
            .displayOrder(DEFAULT_DISPLAY_ORDER)
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
    public static MetaMetaTagCategory createUpdatedEntity() {
        return new MetaMetaTagCategory()
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        metaMetaTagCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaMetaTagCategory != null) {
            metaMetaTagCategoryRepository.delete(insertedMetaMetaTagCategory);
            metaMetaTagCategorySearchRepository.delete(insertedMetaMetaTagCategory);
            insertedMetaMetaTagCategory = null;
        }
    }

    @Test
    @Transactional
    void createMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);
        var returnedMetaMetaTagCategoryDTO = om.readValue(
            restMetaMetaTagCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaMetaTagCategoryDTO.class
        );

        // Validate the MetaMetaTagCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaMetaTagCategory = metaMetaTagCategoryMapper.toEntity(returnedMetaMetaTagCategoryDTO);
        assertMetaMetaTagCategoryUpdatableFieldsEquals(
            returnedMetaMetaTagCategory,
            getPersistedMetaMetaTagCategory(returnedMetaMetaTagCategory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaMetaTagCategory = returnedMetaMetaTagCategory;
    }

    @Test
    @Transactional
    void createMetaMetaTagCategoryWithExistingId() throws Exception {
        // Create the MetaMetaTagCategory with an existing ID
        metaMetaTagCategory.setId(1L);
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaMetaTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        // set the field null
        metaMetaTagCategory.setName(null);

        // Create the MetaMetaTagCategory, which fails.
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        restMetaMetaTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        // set the field null
        metaMetaTagCategory.setIsSystem(null);

        // Create the MetaMetaTagCategory, which fails.
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        restMetaMetaTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        // set the field null
        metaMetaTagCategory.setCreatedDate(null);

        // Create the MetaMetaTagCategory, which fails.
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        restMetaMetaTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        // set the field null
        metaMetaTagCategory.setCreatedBy(null);

        // Create the MetaMetaTagCategory, which fails.
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        restMetaMetaTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategories() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaMetaTagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getMetaMetaTagCategory() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get the metaMetaTagCategory
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, metaMetaTagCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaMetaTagCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getMetaMetaTagCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        Long id = metaMetaTagCategory.getId();

        defaultMetaMetaTagCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaMetaTagCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaMetaTagCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where name equals to
        defaultMetaMetaTagCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where name in
        defaultMetaMetaTagCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where name is not null
        defaultMetaMetaTagCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where name contains
        defaultMetaMetaTagCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where name does not contain
        defaultMetaMetaTagCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where color equals to
        defaultMetaMetaTagCategoryFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where color in
        defaultMetaMetaTagCategoryFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where color is not null
        defaultMetaMetaTagCategoryFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where color contains
        defaultMetaMetaTagCategoryFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where color does not contain
        defaultMetaMetaTagCategoryFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder equals to
        defaultMetaMetaTagCategoryFiltering("displayOrder.equals=" + DEFAULT_DISPLAY_ORDER, "displayOrder.equals=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder in
        defaultMetaMetaTagCategoryFiltering(
            "displayOrder.in=" + DEFAULT_DISPLAY_ORDER + "," + UPDATED_DISPLAY_ORDER,
            "displayOrder.in=" + UPDATED_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder is not null
        defaultMetaMetaTagCategoryFiltering("displayOrder.specified=true", "displayOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder is greater than or equal to
        defaultMetaMetaTagCategoryFiltering(
            "displayOrder.greaterThanOrEqual=" + DEFAULT_DISPLAY_ORDER,
            "displayOrder.greaterThanOrEqual=" + UPDATED_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder is less than or equal to
        defaultMetaMetaTagCategoryFiltering(
            "displayOrder.lessThanOrEqual=" + DEFAULT_DISPLAY_ORDER,
            "displayOrder.lessThanOrEqual=" + SMALLER_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder is less than
        defaultMetaMetaTagCategoryFiltering(
            "displayOrder.lessThan=" + UPDATED_DISPLAY_ORDER,
            "displayOrder.lessThan=" + DEFAULT_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByDisplayOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where displayOrder is greater than
        defaultMetaMetaTagCategoryFiltering(
            "displayOrder.greaterThan=" + SMALLER_DISPLAY_ORDER,
            "displayOrder.greaterThan=" + DEFAULT_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where isSystem equals to
        defaultMetaMetaTagCategoryFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where isSystem in
        defaultMetaMetaTagCategoryFiltering(
            "isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM,
            "isSystem.in=" + UPDATED_IS_SYSTEM
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where isSystem is not null
        defaultMetaMetaTagCategoryFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdDate equals to
        defaultMetaMetaTagCategoryFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdDate in
        defaultMetaMetaTagCategoryFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdDate is not null
        defaultMetaMetaTagCategoryFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdBy equals to
        defaultMetaMetaTagCategoryFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdBy in
        defaultMetaMetaTagCategoryFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdBy is not null
        defaultMetaMetaTagCategoryFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdBy contains
        defaultMetaMetaTagCategoryFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        // Get all the metaMetaTagCategoryList where createdBy does not contain
        defaultMetaMetaTagCategoryFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMetaMetaTagCategoriesByParentIsEqualToSomething() throws Exception {
        MetaMetaTagCategory parent;
        if (TestUtil.findAll(em, MetaMetaTagCategory.class).isEmpty()) {
            metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);
            parent = MetaMetaTagCategoryResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, MetaMetaTagCategory.class).get(0);
        }
        em.persist(parent);
        em.flush();
        metaMetaTagCategory.setParent(parent);
        metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);
        Long parentId = parent.getId();
        // Get all the metaMetaTagCategoryList where parent equals to parentId
        defaultMetaMetaTagCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the metaMetaTagCategoryList where parent equals to (parentId + 1)
        defaultMetaMetaTagCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultMetaMetaTagCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaMetaTagCategoryShouldBeFound(shouldBeFound);
        defaultMetaMetaTagCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaMetaTagCategoryShouldBeFound(String filter) throws Exception {
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaMetaTagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaMetaTagCategoryShouldNotBeFound(String filter) throws Exception {
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaMetaTagCategory() throws Exception {
        // Get the metaMetaTagCategory
        restMetaMetaTagCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaMetaTagCategory() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaMetaTagCategorySearchRepository.save(metaMetaTagCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());

        // Update the metaMetaTagCategory
        MetaMetaTagCategory updatedMetaMetaTagCategory = metaMetaTagCategoryRepository.findById(metaMetaTagCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaMetaTagCategory are not directly saved in db
        em.detach(updatedMetaMetaTagCategory);
        updatedMetaMetaTagCategory
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(updatedMetaMetaTagCategory);

        restMetaMetaTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaMetaTagCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaMetaTagCategoryToMatchAllProperties(updatedMetaMetaTagCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaMetaTagCategory> metaMetaTagCategorySearchList = Streamable.of(
                    metaMetaTagCategorySearchRepository.findAll()
                ).toList();
                MetaMetaTagCategory testMetaMetaTagCategorySearch = metaMetaTagCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaMetaTagCategoryAllPropertiesEquals(testMetaMetaTagCategorySearch, updatedMetaMetaTagCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaMetaTagCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaMetaTagCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaMetaTagCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaMetaTagCategory using partial update
        MetaMetaTagCategory partialUpdatedMetaMetaTagCategory = new MetaMetaTagCategory();
        partialUpdatedMetaMetaTagCategory.setId(metaMetaTagCategory.getId());

        partialUpdatedMetaMetaTagCategory
            .name(UPDATED_NAME)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaMetaTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaMetaTagCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaMetaTagCategory))
            )
            .andExpect(status().isOk());

        // Validate the MetaMetaTagCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaMetaTagCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaMetaTagCategory, metaMetaTagCategory),
            getPersistedMetaMetaTagCategory(metaMetaTagCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaMetaTagCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaMetaTagCategory using partial update
        MetaMetaTagCategory partialUpdatedMetaMetaTagCategory = new MetaMetaTagCategory();
        partialUpdatedMetaMetaTagCategory.setId(metaMetaTagCategory.getId());

        partialUpdatedMetaMetaTagCategory
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaMetaTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaMetaTagCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaMetaTagCategory))
            )
            .andExpect(status().isOk());

        // Validate the MetaMetaTagCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaMetaTagCategoryUpdatableFieldsEquals(
            partialUpdatedMetaMetaTagCategory,
            getPersistedMetaMetaTagCategory(partialUpdatedMetaMetaTagCategory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaMetaTagCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaMetaTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        metaMetaTagCategory.setId(longCount.incrementAndGet());

        // Create the MetaMetaTagCategory
        MetaMetaTagCategoryDTO metaMetaTagCategoryDTO = metaMetaTagCategoryMapper.toDto(metaMetaTagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaMetaTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaMetaTagCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaMetaTagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaMetaTagCategory() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);
        metaMetaTagCategoryRepository.save(metaMetaTagCategory);
        metaMetaTagCategorySearchRepository.save(metaMetaTagCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaMetaTagCategory
        restMetaMetaTagCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaMetaTagCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaMetaTagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaMetaTagCategory() throws Exception {
        // Initialize the database
        insertedMetaMetaTagCategory = metaMetaTagCategoryRepository.saveAndFlush(metaMetaTagCategory);
        metaMetaTagCategorySearchRepository.save(metaMetaTagCategory);

        // Search the metaMetaTagCategory
        restMetaMetaTagCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaMetaTagCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaMetaTagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return metaMetaTagCategoryRepository.count();
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

    protected MetaMetaTagCategory getPersistedMetaMetaTagCategory(MetaMetaTagCategory metaMetaTagCategory) {
        return metaMetaTagCategoryRepository.findById(metaMetaTagCategory.getId()).orElseThrow();
    }

    protected void assertPersistedMetaMetaTagCategoryToMatchAllProperties(MetaMetaTagCategory expectedMetaMetaTagCategory) {
        assertMetaMetaTagCategoryAllPropertiesEquals(
            expectedMetaMetaTagCategory,
            getPersistedMetaMetaTagCategory(expectedMetaMetaTagCategory)
        );
    }

    protected void assertPersistedMetaMetaTagCategoryToMatchUpdatableProperties(MetaMetaTagCategory expectedMetaMetaTagCategory) {
        assertMetaMetaTagCategoryAllUpdatablePropertiesEquals(
            expectedMetaMetaTagCategory,
            getPersistedMetaMetaTagCategory(expectedMetaMetaTagCategory)
        );
    }
}
