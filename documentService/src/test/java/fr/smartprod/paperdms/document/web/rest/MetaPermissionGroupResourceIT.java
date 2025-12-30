package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.MetaPermissionGroupAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.repository.MetaPermissionGroupRepository;
import fr.smartprod.paperdms.document.repository.search.MetaPermissionGroupSearchRepository;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
import fr.smartprod.paperdms.document.service.mapper.MetaPermissionGroupMapper;
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
 * Integration tests for the {@link MetaPermissionGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MetaPermissionGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/meta-permission-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/meta-permission-groups/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MetaPermissionGroupRepository metaPermissionGroupRepository;

    @Autowired
    private MetaPermissionGroupMapper metaPermissionGroupMapper;

    @Autowired
    private MetaPermissionGroupSearchRepository metaPermissionGroupSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMetaPermissionGroupMockMvc;

    private MetaPermissionGroup metaPermissionGroup;

    private MetaPermissionGroup insertedMetaPermissionGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MetaPermissionGroup createEntity() {
        return new MetaPermissionGroup()
            .name(DEFAULT_NAME)
            .permissions(DEFAULT_PERMISSIONS)
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
    public static MetaPermissionGroup createUpdatedEntity() {
        return new MetaPermissionGroup()
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        metaPermissionGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMetaPermissionGroup != null) {
            metaPermissionGroupRepository.delete(insertedMetaPermissionGroup);
            metaPermissionGroupSearchRepository.delete(insertedMetaPermissionGroup);
            insertedMetaPermissionGroup = null;
        }
    }

    @Test
    @Transactional
    void createMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);
        var returnedMetaPermissionGroupDTO = om.readValue(
            restMetaPermissionGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MetaPermissionGroupDTO.class
        );

        // Validate the MetaPermissionGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMetaPermissionGroup = metaPermissionGroupMapper.toEntity(returnedMetaPermissionGroupDTO);
        assertMetaPermissionGroupUpdatableFieldsEquals(
            returnedMetaPermissionGroup,
            getPersistedMetaPermissionGroup(returnedMetaPermissionGroup)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedMetaPermissionGroup = returnedMetaPermissionGroup;
    }

    @Test
    @Transactional
    void createMetaPermissionGroupWithExistingId() throws Exception {
        // Create the MetaPermissionGroup with an existing ID
        metaPermissionGroup.setId(1L);
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restMetaPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        // set the field null
        metaPermissionGroup.setName(null);

        // Create the MetaPermissionGroup, which fails.
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        restMetaPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        // set the field null
        metaPermissionGroup.setIsSystem(null);

        // Create the MetaPermissionGroup, which fails.
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        restMetaPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        // set the field null
        metaPermissionGroup.setCreatedDate(null);

        // Create the MetaPermissionGroup, which fails.
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        restMetaPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        // set the field null
        metaPermissionGroup.setCreatedBy(null);

        // Create the MetaPermissionGroup, which fails.
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        restMetaPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroups() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaPermissionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getMetaPermissionGroup() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get the metaPermissionGroup
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, metaPermissionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(metaPermissionGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.permissions").value(DEFAULT_PERMISSIONS))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getMetaPermissionGroupsByIdFiltering() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        Long id = metaPermissionGroup.getId();

        defaultMetaPermissionGroupFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMetaPermissionGroupFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMetaPermissionGroupFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where name equals to
        defaultMetaPermissionGroupFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where name in
        defaultMetaPermissionGroupFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where name is not null
        defaultMetaPermissionGroupFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where name contains
        defaultMetaPermissionGroupFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where name does not contain
        defaultMetaPermissionGroupFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByIsSystemIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where isSystem equals to
        defaultMetaPermissionGroupFiltering("isSystem.equals=" + DEFAULT_IS_SYSTEM, "isSystem.equals=" + UPDATED_IS_SYSTEM);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByIsSystemIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where isSystem in
        defaultMetaPermissionGroupFiltering(
            "isSystem.in=" + DEFAULT_IS_SYSTEM + "," + UPDATED_IS_SYSTEM,
            "isSystem.in=" + UPDATED_IS_SYSTEM
        );
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByIsSystemIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where isSystem is not null
        defaultMetaPermissionGroupFiltering("isSystem.specified=true", "isSystem.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdDate equals to
        defaultMetaPermissionGroupFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdDate in
        defaultMetaPermissionGroupFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdDate is not null
        defaultMetaPermissionGroupFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdBy equals to
        defaultMetaPermissionGroupFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdBy in
        defaultMetaPermissionGroupFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdBy is not null
        defaultMetaPermissionGroupFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdBy contains
        defaultMetaPermissionGroupFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllMetaPermissionGroupsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        // Get all the metaPermissionGroupList where createdBy does not contain
        defaultMetaPermissionGroupFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    private void defaultMetaPermissionGroupFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMetaPermissionGroupShouldBeFound(shouldBeFound);
        defaultMetaPermissionGroupShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMetaPermissionGroupShouldBeFound(String filter) throws Exception {
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaPermissionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMetaPermissionGroupShouldNotBeFound(String filter) throws Exception {
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMetaPermissionGroup() throws Exception {
        // Get the metaPermissionGroup
        restMetaPermissionGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMetaPermissionGroup() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        metaPermissionGroupSearchRepository.save(metaPermissionGroup);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());

        // Update the metaPermissionGroup
        MetaPermissionGroup updatedMetaPermissionGroup = metaPermissionGroupRepository.findById(metaPermissionGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMetaPermissionGroup are not directly saved in db
        em.detach(updatedMetaPermissionGroup);
        updatedMetaPermissionGroup
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(updatedMetaPermissionGroup);

        restMetaPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaPermissionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMetaPermissionGroupToMatchAllProperties(updatedMetaPermissionGroup);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<MetaPermissionGroup> metaPermissionGroupSearchList = Streamable.of(
                    metaPermissionGroupSearchRepository.findAll()
                ).toList();
                MetaPermissionGroup testMetaPermissionGroupSearch = metaPermissionGroupSearchList.get(searchDatabaseSizeAfter - 1);

                assertMetaPermissionGroupAllPropertiesEquals(testMetaPermissionGroupSearch, updatedMetaPermissionGroup);
            });
    }

    @Test
    @Transactional
    void putNonExistingMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, metaPermissionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(metaPermissionGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateMetaPermissionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaPermissionGroup using partial update
        MetaPermissionGroup partialUpdatedMetaPermissionGroup = new MetaPermissionGroup();
        partialUpdatedMetaPermissionGroup.setId(metaPermissionGroup.getId());

        partialUpdatedMetaPermissionGroup
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaPermissionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaPermissionGroup))
            )
            .andExpect(status().isOk());

        // Validate the MetaPermissionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaPermissionGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMetaPermissionGroup, metaPermissionGroup),
            getPersistedMetaPermissionGroup(metaPermissionGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdateMetaPermissionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the metaPermissionGroup using partial update
        MetaPermissionGroup partialUpdatedMetaPermissionGroup = new MetaPermissionGroup();
        partialUpdatedMetaPermissionGroup.setId(metaPermissionGroup.getId());

        partialUpdatedMetaPermissionGroup
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restMetaPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMetaPermissionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMetaPermissionGroup))
            )
            .andExpect(status().isOk());

        // Validate the MetaPermissionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMetaPermissionGroupUpdatableFieldsEquals(
            partialUpdatedMetaPermissionGroup,
            getPersistedMetaPermissionGroup(partialUpdatedMetaPermissionGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, metaPermissionGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMetaPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        metaPermissionGroup.setId(longCount.incrementAndGet());

        // Create the MetaPermissionGroup
        MetaPermissionGroupDTO metaPermissionGroupDTO = metaPermissionGroupMapper.toDto(metaPermissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMetaPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(metaPermissionGroupDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MetaPermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteMetaPermissionGroup() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);
        metaPermissionGroupRepository.save(metaPermissionGroup);
        metaPermissionGroupSearchRepository.save(metaPermissionGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the metaPermissionGroup
        restMetaPermissionGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, metaPermissionGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(metaPermissionGroupSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchMetaPermissionGroup() throws Exception {
        // Initialize the database
        insertedMetaPermissionGroup = metaPermissionGroupRepository.saveAndFlush(metaPermissionGroup);
        metaPermissionGroupSearchRepository.save(metaPermissionGroup);

        // Search the metaPermissionGroup
        restMetaPermissionGroupMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + metaPermissionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(metaPermissionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS.toString())))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return metaPermissionGroupRepository.count();
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

    protected MetaPermissionGroup getPersistedMetaPermissionGroup(MetaPermissionGroup metaPermissionGroup) {
        return metaPermissionGroupRepository.findById(metaPermissionGroup.getId()).orElseThrow();
    }

    protected void assertPersistedMetaPermissionGroupToMatchAllProperties(MetaPermissionGroup expectedMetaPermissionGroup) {
        assertMetaPermissionGroupAllPropertiesEquals(
            expectedMetaPermissionGroup,
            getPersistedMetaPermissionGroup(expectedMetaPermissionGroup)
        );
    }

    protected void assertPersistedMetaPermissionGroupToMatchUpdatableProperties(MetaPermissionGroup expectedMetaPermissionGroup) {
        assertMetaPermissionGroupAllUpdatablePropertiesEquals(
            expectedMetaPermissionGroup,
            getPersistedMetaPermissionGroup(expectedMetaPermissionGroup)
        );
    }
}
