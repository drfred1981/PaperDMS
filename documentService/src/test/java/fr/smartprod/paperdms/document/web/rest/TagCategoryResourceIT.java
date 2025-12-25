package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.TagCategoryAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.repository.TagCategoryRepository;
import fr.smartprod.paperdms.document.repository.search.TagCategorySearchRepository;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import fr.smartprod.paperdms.document.service.mapper.TagCategoryMapper;
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
 * Integration tests for the {@link TagCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagCategoryResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tag-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tag-categories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagCategoryRepository tagCategoryRepository;

    @Autowired
    private TagCategoryMapper tagCategoryMapper;

    @Autowired
    private TagCategorySearchRepository tagCategorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagCategoryMockMvc;

    private TagCategory tagCategory;

    private TagCategory insertedTagCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagCategory createEntity() {
        return new TagCategory()
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
    public static TagCategory createUpdatedEntity() {
        return new TagCategory()
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        tagCategory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTagCategory != null) {
            tagCategoryRepository.delete(insertedTagCategory);
            tagCategorySearchRepository.delete(insertedTagCategory);
            insertedTagCategory = null;
        }
    }

    @Test
    @Transactional
    void createTagCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);
        var returnedTagCategoryDTO = om.readValue(
            restTagCategoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TagCategoryDTO.class
        );

        // Validate the TagCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTagCategory = tagCategoryMapper.toEntity(returnedTagCategoryDTO);
        assertTagCategoryUpdatableFieldsEquals(returnedTagCategory, getPersistedTagCategory(returnedTagCategory));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTagCategory = returnedTagCategory;
    }

    @Test
    @Transactional
    void createTagCategoryWithExistingId() throws Exception {
        // Create the TagCategory with an existing ID
        tagCategory.setId(1L);
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        // set the field null
        tagCategory.setName(null);

        // Create the TagCategory, which fails.
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        restTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        // set the field null
        tagCategory.setIsSystem(null);

        // Create the TagCategory, which fails.
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        restTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        // set the field null
        tagCategory.setCreatedDate(null);

        // Create the TagCategory, which fails.
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        restTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        // set the field null
        tagCategory.setCreatedBy(null);

        // Create the TagCategory, which fails.
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        restTagCategoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTagCategories() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getTagCategory() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get the tagCategory
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, tagCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getTagCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        Long id = tagCategory.getId();

        defaultTagCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTagCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTagCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where name equals to
        defaultTagCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where name in
        defaultTagCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where name is not null
        defaultTagCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where name contains
        defaultTagCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where name does not contain
        defaultTagCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where color equals to
        defaultTagCategoryFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where color in
        defaultTagCategoryFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where color is not null
        defaultTagCategoryFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where color contains
        defaultTagCategoryFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where color does not contain
        defaultTagCategoryFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder equals to
        defaultTagCategoryFiltering("displayOrder.equals=" + DEFAULT_DISPLAY_ORDER, "displayOrder.equals=" + UPDATED_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder in
        defaultTagCategoryFiltering(
            "displayOrder.in=" + DEFAULT_DISPLAY_ORDER + "," + UPDATED_DISPLAY_ORDER,
            "displayOrder.in=" + UPDATED_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder is not null
        defaultTagCategoryFiltering("displayOrder.specified=true", "displayOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder is greater than or equal to
        defaultTagCategoryFiltering(
            "displayOrder.greaterThanOrEqual=" + DEFAULT_DISPLAY_ORDER,
            "displayOrder.greaterThanOrEqual=" + UPDATED_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder is less than or equal to
        defaultTagCategoryFiltering(
            "displayOrder.lessThanOrEqual=" + DEFAULT_DISPLAY_ORDER,
            "displayOrder.lessThanOrEqual=" + SMALLER_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder is less than
        defaultTagCategoryFiltering("displayOrder.lessThan=" + UPDATED_DISPLAY_ORDER, "displayOrder.lessThan=" + DEFAULT_DISPLAY_ORDER);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByDisplayOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where displayOrder is greater than
        defaultTagCategoryFiltering(
            "displayOrder.greaterThan=" + SMALLER_DISPLAY_ORDER,
            "displayOrder.greaterThan=" + DEFAULT_DISPLAY_ORDER
        );
    }

    @Test
    @Transactional
    void getAllTagCategoriesByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where isSystem equals to
        defaultTagCategoryFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where isSystem in
        defaultTagCategoryFiltering("isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM, "isSystem.in=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where isSystem is not null
        defaultTagCategoryFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdDate equals to
        defaultTagCategoryFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdDate in
        defaultTagCategoryFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdDate is not null
        defaultTagCategoryFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdBy equals to
        defaultTagCategoryFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdBy in
        defaultTagCategoryFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdBy is not null
        defaultTagCategoryFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdBy contains
        defaultTagCategoryFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        // Get all the tagCategoryList where createdBy does not contain
        defaultTagCategoryFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagCategoriesByParentIsEqualToSomething() throws Exception {
        TagCategory parent;
        if (TestUtil.findAll(em, TagCategory.class).isEmpty()) {
            tagCategoryRepository.saveAndFlush(tagCategory);
            parent = TagCategoryResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, TagCategory.class).get(0);
        }
        em.persist(parent);
        em.flush();
        tagCategory.setParent(parent);
        tagCategoryRepository.saveAndFlush(tagCategory);
        Long parentId = parent.getId();
        // Get all the tagCategoryList where parent equals to parentId
        defaultTagCategoryShouldBeFound("parentId.equals=" + parentId);

        // Get all the tagCategoryList where parent equals to (parentId + 1)
        defaultTagCategoryShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultTagCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTagCategoryShouldBeFound(shouldBeFound);
        defaultTagCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagCategoryShouldBeFound(String filter) throws Exception {
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagCategoryShouldNotBeFound(String filter) throws Exception {
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTagCategory() throws Exception {
        // Get the tagCategory
        restTagCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTagCategory() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagCategorySearchRepository.save(tagCategory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());

        // Update the tagCategory
        TagCategory updatedTagCategory = tagCategoryRepository.findById(tagCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTagCategory are not directly saved in db
        em.detach(updatedTagCategory);
        updatedTagCategory
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(updatedTagCategory);

        restTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTagCategoryToMatchAllProperties(updatedTagCategory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<TagCategory> tagCategorySearchList = Streamable.of(tagCategorySearchRepository.findAll()).toList();
                TagCategory testTagCategorySearch = tagCategorySearchList.get(searchDatabaseSizeAfter - 1);

                assertTagCategoryAllPropertiesEquals(testTagCategorySearch, updatedTagCategory);
            });
    }

    @Test
    @Transactional
    void putNonExistingTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagCategoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTagCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tagCategory using partial update
        TagCategory partialUpdatedTagCategory = new TagCategory();
        partialUpdatedTagCategory.setId(tagCategory.getId());

        partialUpdatedTagCategory.displayOrder(UPDATED_DISPLAY_ORDER).isSystem(UPDATED_IS_SYSTEM).createdBy(UPDATED_CREATED_BY);

        restTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagCategory))
            )
            .andExpect(status().isOk());

        // Validate the TagCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTagCategory, tagCategory),
            getPersistedTagCategory(tagCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateTagCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tagCategory using partial update
        TagCategory partialUpdatedTagCategory = new TagCategory();
        partialUpdatedTagCategory.setId(tagCategory.getId());

        partialUpdatedTagCategory
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .displayOrder(UPDATED_DISPLAY_ORDER)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagCategory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagCategory))
            )
            .andExpect(status().isOk());

        // Validate the TagCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagCategoryUpdatableFieldsEquals(partialUpdatedTagCategory, getPersistedTagCategory(partialUpdatedTagCategory));
    }

    @Test
    @Transactional
    void patchNonExistingTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagCategoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTagCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        tagCategory.setId(longCount.incrementAndGet());

        // Create the TagCategory
        TagCategoryDTO tagCategoryDTO = tagCategoryMapper.toDto(tagCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagCategoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTagCategory() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);
        tagCategoryRepository.save(tagCategory);
        tagCategorySearchRepository.save(tagCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tagCategory
        restTagCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagCategory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagCategorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTagCategory() throws Exception {
        // Initialize the database
        insertedTagCategory = tagCategoryRepository.saveAndFlush(tagCategory);
        tagCategorySearchRepository.save(tagCategory);

        // Search the tagCategory
        restTagCategoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tagCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return tagCategoryRepository.count();
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

    protected TagCategory getPersistedTagCategory(TagCategory tagCategory) {
        return tagCategoryRepository.findById(tagCategory.getId()).orElseThrow();
    }

    protected void assertPersistedTagCategoryToMatchAllProperties(TagCategory expectedTagCategory) {
        assertTagCategoryAllPropertiesEquals(expectedTagCategory, getPersistedTagCategory(expectedTagCategory));
    }

    protected void assertPersistedTagCategoryToMatchUpdatableProperties(TagCategory expectedTagCategory) {
        assertTagCategoryAllUpdatablePropertiesEquals(expectedTagCategory, getPersistedTagCategory(expectedTagCategory));
    }
}
