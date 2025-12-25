package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.TagAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Tag;
import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.repository.TagRepository;
import fr.smartprod.paperdms.document.repository.search.TagSearchRepository;
import fr.smartprod.paperdms.document.service.dto.TagDTO;
import fr.smartprod.paperdms.document.service.mapper.TagMapper;
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
 * Integration tests for the {@link TagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagResourceIT {

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

    private static final String ENTITY_API_URL = "/api/tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/tags/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagRepository tagRepository;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private TagSearchRepository tagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagMockMvc;

    private Tag tag;

    private Tag insertedTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Tag createEntity() {
        return new Tag()
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
    public static Tag createUpdatedEntity() {
        return new Tag()
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
        tag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTag != null) {
            tagRepository.delete(insertedTag);
            tagSearchRepository.delete(insertedTag);
            insertedTag = null;
        }
    }

    @Test
    @Transactional
    void createTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);
        var returnedTagDTO = om.readValue(
            restTagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TagDTO.class
        );

        // Validate the Tag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTag = tagMapper.toEntity(returnedTagDTO);
        assertTagUpdatableFieldsEquals(returnedTag, getPersistedTag(returnedTag));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedTag = returnedTag;
    }

    @Test
    @Transactional
    void createTagWithExistingId() throws Exception {
        // Create the Tag with an existing ID
        tag.setId(1L);
        TagDTO tagDTO = tagMapper.toDto(tag);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        // set the field null
        tag.setName(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        // set the field null
        tag.setIsSystem(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        // set the field null
        tag.setCreatedDate(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        // set the field null
        tag.setCreatedBy(null);

        // Create the Tag, which fails.
        TagDTO tagDTO = tagMapper.toDto(tag);

        restTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllTags() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
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
    void getTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get the tag
        restTagMockMvc
            .perform(get(ENTITY_API_URL_ID, tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tag.getId().intValue()))
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
    void getTagsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        Long id = tag.getId();

        defaultTagFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTagFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTagFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTagsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where name equals to
        defaultTagFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where name in
        defaultTagFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where name is not null
        defaultTagFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where name contains
        defaultTagFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTagsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where name does not contain
        defaultTagFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTagsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where color equals to
        defaultTagFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where color in
        defaultTagFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where color is not null
        defaultTagFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where color contains
        defaultTagFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTagsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where color does not contain
        defaultTagFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllTagsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where description equals to
        defaultTagFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTagsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where description in
        defaultTagFiltering("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION, "description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTagsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where description is not null
        defaultTagFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where description contains
        defaultTagFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTagsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where description does not contain
        defaultTagFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount equals to
        defaultTagFiltering("usageCount.equals=" + DEFAULT_USAGE_COUNT, "usageCount.equals=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount in
        defaultTagFiltering("usageCount.in=" + DEFAULT_USAGE_COUNT + "," + UPDATED_USAGE_COUNT, "usageCount.in=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount is not null
        defaultTagFiltering("usageCount.specified=true", "usageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount is greater than or equal to
        defaultTagFiltering("usageCount.greaterThanOrEqual=" + DEFAULT_USAGE_COUNT, "usageCount.greaterThanOrEqual=" + UPDATED_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount is less than or equal to
        defaultTagFiltering("usageCount.lessThanOrEqual=" + DEFAULT_USAGE_COUNT, "usageCount.lessThanOrEqual=" + SMALLER_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount is less than
        defaultTagFiltering("usageCount.lessThan=" + UPDATED_USAGE_COUNT, "usageCount.lessThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByUsageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where usageCount is greater than
        defaultTagFiltering("usageCount.greaterThan=" + SMALLER_USAGE_COUNT, "usageCount.greaterThan=" + DEFAULT_USAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllTagsByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where isSystem equals to
        defaultTagFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllTagsByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where isSystem in
        defaultTagFiltering("isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM, "isSystem.in=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllTagsByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where isSystem is not null
        defaultTagFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdDate equals to
        defaultTagFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdDate in
        defaultTagFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTagsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdDate is not null
        defaultTagFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdBy equals to
        defaultTagFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdBy in
        defaultTagFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdBy is not null
        defaultTagFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTagsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdBy contains
        defaultTagFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        // Get all the tagList where createdBy does not contain
        defaultTagFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTagsByTagCategoryIsEqualToSomething() throws Exception {
        TagCategory tagCategory;
        if (TestUtil.findAll(em, TagCategory.class).isEmpty()) {
            tagRepository.saveAndFlush(tag);
            tagCategory = TagCategoryResourceIT.createEntity();
        } else {
            tagCategory = TestUtil.findAll(em, TagCategory.class).get(0);
        }
        em.persist(tagCategory);
        em.flush();
        tag.setTagCategory(tagCategory);
        tagRepository.saveAndFlush(tag);
        Long tagCategoryId = tagCategory.getId();
        // Get all the tagList where tagCategory equals to tagCategoryId
        defaultTagShouldBeFound("tagCategoryId.equals=" + tagCategoryId);

        // Get all the tagList where tagCategory equals to (tagCategoryId + 1)
        defaultTagShouldNotBeFound("tagCategoryId.equals=" + (tagCategoryId + 1));
    }

    private void defaultTagFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTagShouldBeFound(shouldBeFound);
        defaultTagShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTagShouldBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTagShouldNotBeFound(String filter) throws Exception {
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTag() throws Exception {
        // Get the tag
        restTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagSearchRepository.save(tag);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());

        // Update the tag
        Tag updatedTag = tagRepository.findById(tag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTag are not directly saved in db
        em.detach(updatedTag);
        updatedTag
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .description(UPDATED_DESCRIPTION)
            .usageCount(UPDATED_USAGE_COUNT)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        TagDTO tagDTO = tagMapper.toDto(updatedTag);

        restTagMockMvc
            .perform(put(ENTITY_API_URL_ID, tagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isOk());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTagToMatchAllProperties(updatedTag);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Tag> tagSearchList = Streamable.of(tagSearchRepository.findAll()).toList();
                Tag testTagSearch = tagSearchList.get(searchDatabaseSizeAfter - 1);

                assertTagAllPropertiesEquals(testTagSearch, updatedTag);
            });
    }

    @Test
    @Transactional
    void putNonExistingTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL_ID, tagDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateTagWithPatch() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag.name(UPDATED_NAME).color(UPDATED_COLOR).createdDate(UPDATED_CREATED_DATE);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedTag, tag), getPersistedTag(tag));
    }

    @Test
    @Transactional
    void fullUpdateTagWithPatch() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tag using partial update
        Tag partialUpdatedTag = new Tag();
        partialUpdatedTag.setId(tag.getId());

        partialUpdatedTag
            .name(UPDATED_NAME)
            .color(UPDATED_COLOR)
            .description(UPDATED_DESCRIPTION)
            .usageCount(UPDATED_USAGE_COUNT)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTag))
            )
            .andExpect(status().isOk());

        // Validate the Tag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagUpdatableFieldsEquals(partialUpdatedTag, getPersistedTag(partialUpdatedTag));
    }

    @Test
    @Transactional
    void patchNonExistingTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagDTO.getId()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        tag.setId(longCount.incrementAndGet());

        // Create the Tag
        TagDTO tagDTO = tagMapper.toDto(tag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Tag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);
        tagRepository.save(tag);
        tagSearchRepository.save(tag);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the tag
        restTagMockMvc.perform(delete(ENTITY_API_URL_ID, tag.getId()).accept(MediaType.APPLICATION_JSON)).andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(tagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchTag() throws Exception {
        // Initialize the database
        insertedTag = tagRepository.saveAndFlush(tag);
        tagSearchRepository.save(tag);

        // Search the tag
        restTagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + tag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tag.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].usageCount").value(hasItem(DEFAULT_USAGE_COUNT)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return tagRepository.count();
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

    protected Tag getPersistedTag(Tag tag) {
        return tagRepository.findById(tag.getId()).orElseThrow();
    }

    protected void assertPersistedTagToMatchAllProperties(Tag expectedTag) {
        assertTagAllPropertiesEquals(expectedTag, getPersistedTag(expectedTag));
    }

    protected void assertPersistedTagToMatchUpdatableProperties(Tag expectedTag) {
        assertTagAllUpdatablePropertiesEquals(expectedTag, getPersistedTag(expectedTag));
    }
}
