package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.TransformMergeJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.TransformMergeJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.TransformMergeJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformMergeJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformMergeJobMapper;
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
 * Integration tests for the {@link TransformMergeJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformMergeJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_MERGE_ORDER = "AAAAAAAAAA";
    private static final String UPDATED_MERGE_ORDER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCLUDE_BOOKMARKS = false;
    private static final Boolean UPDATED_INCLUDE_BOOKMARKS = true;

    private static final Boolean DEFAULT_INCLUDE_TOC = false;
    private static final Boolean UPDATED_INCLUDE_TOC = true;

    private static final Boolean DEFAULT_ADD_PAGE_NUMBERS = false;
    private static final Boolean UPDATED_ADD_PAGE_NUMBERS = true;

    private static final String DEFAULT_OUTPUT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_OUTPUT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final TransformStatus DEFAULT_STATUS = TransformStatus.PENDING;
    private static final TransformStatus UPDATED_STATUS = TransformStatus.PROCESSING;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/transform-merge-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransformMergeJobRepository transformMergeJobRepository;

    @Autowired
    private TransformMergeJobMapper transformMergeJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformMergeJobMockMvc;

    private TransformMergeJob transformMergeJob;

    private TransformMergeJob insertedTransformMergeJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformMergeJob createEntity() {
        return new TransformMergeJob()
            .name(DEFAULT_NAME)
            .sourceDocumentSha256(DEFAULT_SOURCE_DOCUMENT_SHA_256)
            .mergeOrder(DEFAULT_MERGE_ORDER)
            .includeBookmarks(DEFAULT_INCLUDE_BOOKMARKS)
            .includeToc(DEFAULT_INCLUDE_TOC)
            .addPageNumbers(DEFAULT_ADD_PAGE_NUMBERS)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputDocumentSha256(DEFAULT_OUTPUT_DOCUMENT_SHA_256)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformMergeJob createUpdatedEntity() {
        return new TransformMergeJob()
            .name(UPDATED_NAME)
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        transformMergeJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransformMergeJob != null) {
            transformMergeJobRepository.delete(insertedTransformMergeJob);
            insertedTransformMergeJob = null;
        }
    }

    @Test
    @Transactional
    void createTransformMergeJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);
        var returnedTransformMergeJobDTO = om.readValue(
            restTransformMergeJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransformMergeJobDTO.class
        );

        // Validate the TransformMergeJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransformMergeJob = transformMergeJobMapper.toEntity(returnedTransformMergeJobDTO);
        assertTransformMergeJobUpdatableFieldsEquals(returnedTransformMergeJob, getPersistedTransformMergeJob(returnedTransformMergeJob));

        insertedTransformMergeJob = returnedTransformMergeJob;
    }

    @Test
    @Transactional
    void createTransformMergeJobWithExistingId() throws Exception {
        // Create the TransformMergeJob with an existing ID
        transformMergeJob.setId(1L);
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformMergeJob.setName(null);

        // Create the TransformMergeJob, which fails.
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        restTransformMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformMergeJob.setStatus(null);

        // Create the TransformMergeJob, which fails.
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        restTransformMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformMergeJob.setCreatedBy(null);

        // Create the TransformMergeJob, which fails.
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        restTransformMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformMergeJob.setCreatedDate(null);

        // Create the TransformMergeJob, which fails.
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        restTransformMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobs() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformMergeJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceDocumentSha256").value(hasItem(DEFAULT_SOURCE_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].mergeOrder").value(hasItem(DEFAULT_MERGE_ORDER)))
            .andExpect(jsonPath("$.[*].includeBookmarks").value(hasItem(DEFAULT_INCLUDE_BOOKMARKS)))
            .andExpect(jsonPath("$.[*].includeToc").value(hasItem(DEFAULT_INCLUDE_TOC)))
            .andExpect(jsonPath("$.[*].addPageNumbers").value(hasItem(DEFAULT_ADD_PAGE_NUMBERS)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTransformMergeJob() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get the transformMergeJob
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL_ID, transformMergeJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformMergeJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sourceDocumentSha256").value(DEFAULT_SOURCE_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.mergeOrder").value(DEFAULT_MERGE_ORDER))
            .andExpect(jsonPath("$.includeBookmarks").value(DEFAULT_INCLUDE_BOOKMARKS))
            .andExpect(jsonPath("$.includeToc").value(DEFAULT_INCLUDE_TOC))
            .andExpect(jsonPath("$.addPageNumbers").value(DEFAULT_ADD_PAGE_NUMBERS))
            .andExpect(jsonPath("$.outputS3Key").value(DEFAULT_OUTPUT_S_3_KEY))
            .andExpect(jsonPath("$.outputDocumentSha256").value(DEFAULT_OUTPUT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getTransformMergeJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        Long id = transformMergeJob.getId();

        defaultTransformMergeJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransformMergeJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransformMergeJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where name equals to
        defaultTransformMergeJobFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where name in
        defaultTransformMergeJobFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where name is not null
        defaultTransformMergeJobFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where name contains
        defaultTransformMergeJobFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where name does not contain
        defaultTransformMergeJobFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeBookmarksIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeBookmarks equals to
        defaultTransformMergeJobFiltering(
            "includeBookmarks.equals=" + DEFAULT_INCLUDE_BOOKMARKS,
            "includeBookmarks.equals=" + UPDATED_INCLUDE_BOOKMARKS
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeBookmarksIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeBookmarks in
        defaultTransformMergeJobFiltering(
            "includeBookmarks.in=" + DEFAULT_INCLUDE_BOOKMARKS + "," + UPDATED_INCLUDE_BOOKMARKS,
            "includeBookmarks.in=" + UPDATED_INCLUDE_BOOKMARKS
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeBookmarksIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeBookmarks is not null
        defaultTransformMergeJobFiltering("includeBookmarks.specified=true", "includeBookmarks.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeTocIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeToc equals to
        defaultTransformMergeJobFiltering("includeToc.equals=" + DEFAULT_INCLUDE_TOC, "includeToc.equals=" + UPDATED_INCLUDE_TOC);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeTocIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeToc in
        defaultTransformMergeJobFiltering(
            "includeToc.in=" + DEFAULT_INCLUDE_TOC + "," + UPDATED_INCLUDE_TOC,
            "includeToc.in=" + UPDATED_INCLUDE_TOC
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByIncludeTocIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where includeToc is not null
        defaultTransformMergeJobFiltering("includeToc.specified=true", "includeToc.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByAddPageNumbersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where addPageNumbers equals to
        defaultTransformMergeJobFiltering(
            "addPageNumbers.equals=" + DEFAULT_ADD_PAGE_NUMBERS,
            "addPageNumbers.equals=" + UPDATED_ADD_PAGE_NUMBERS
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByAddPageNumbersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where addPageNumbers in
        defaultTransformMergeJobFiltering(
            "addPageNumbers.in=" + DEFAULT_ADD_PAGE_NUMBERS + "," + UPDATED_ADD_PAGE_NUMBERS,
            "addPageNumbers.in=" + UPDATED_ADD_PAGE_NUMBERS
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByAddPageNumbersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where addPageNumbers is not null
        defaultTransformMergeJobFiltering("addPageNumbers.specified=true", "addPageNumbers.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputS3Key equals to
        defaultTransformMergeJobFiltering("outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputS3Key in
        defaultTransformMergeJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputS3Key is not null
        defaultTransformMergeJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputS3Key contains
        defaultTransformMergeJobFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputS3Key does not contain
        defaultTransformMergeJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputDocumentSha256 equals to
        defaultTransformMergeJobFiltering(
            "outputDocumentSha256.equals=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.equals=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputDocumentSha256 in
        defaultTransformMergeJobFiltering(
            "outputDocumentSha256.in=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256 + "," + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.in=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputDocumentSha256 is not null
        defaultTransformMergeJobFiltering("outputDocumentSha256.specified=true", "outputDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputDocumentSha256 contains
        defaultTransformMergeJobFiltering(
            "outputDocumentSha256.contains=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.contains=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByOutputDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where outputDocumentSha256 does not contain
        defaultTransformMergeJobFiltering(
            "outputDocumentSha256.doesNotContain=" + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.doesNotContain=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where status equals to
        defaultTransformMergeJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where status in
        defaultTransformMergeJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where status is not null
        defaultTransformMergeJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where startDate equals to
        defaultTransformMergeJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where startDate in
        defaultTransformMergeJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where startDate is not null
        defaultTransformMergeJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where endDate equals to
        defaultTransformMergeJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where endDate in
        defaultTransformMergeJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where endDate is not null
        defaultTransformMergeJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdBy equals to
        defaultTransformMergeJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdBy in
        defaultTransformMergeJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdBy is not null
        defaultTransformMergeJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdBy contains
        defaultTransformMergeJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdBy does not contain
        defaultTransformMergeJobFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdDate equals to
        defaultTransformMergeJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdDate in
        defaultTransformMergeJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformMergeJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        // Get all the transformMergeJobList where createdDate is not null
        defaultTransformMergeJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultTransformMergeJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransformMergeJobShouldBeFound(shouldBeFound);
        defaultTransformMergeJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformMergeJobShouldBeFound(String filter) throws Exception {
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformMergeJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceDocumentSha256").value(hasItem(DEFAULT_SOURCE_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].mergeOrder").value(hasItem(DEFAULT_MERGE_ORDER)))
            .andExpect(jsonPath("$.[*].includeBookmarks").value(hasItem(DEFAULT_INCLUDE_BOOKMARKS)))
            .andExpect(jsonPath("$.[*].includeToc").value(hasItem(DEFAULT_INCLUDE_TOC)))
            .andExpect(jsonPath("$.[*].addPageNumbers").value(hasItem(DEFAULT_ADD_PAGE_NUMBERS)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformMergeJobShouldNotBeFound(String filter) throws Exception {
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformMergeJob() throws Exception {
        // Get the transformMergeJob
        restTransformMergeJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransformMergeJob() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformMergeJob
        TransformMergeJob updatedTransformMergeJob = transformMergeJobRepository.findById(transformMergeJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTransformMergeJob are not directly saved in db
        em.detach(updatedTransformMergeJob);
        updatedTransformMergeJob
            .name(UPDATED_NAME)
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(updatedTransformMergeJob);

        restTransformMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformMergeJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformMergeJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransformMergeJobToMatchAllProperties(updatedTransformMergeJob);
    }

    @Test
    @Transactional
    void putNonExistingTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformMergeJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformMergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformMergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformMergeJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformMergeJob using partial update
        TransformMergeJob partialUpdatedTransformMergeJob = new TransformMergeJob();
        partialUpdatedTransformMergeJob.setId(transformMergeJob.getId());

        partialUpdatedTransformMergeJob
            .mergeOrder(UPDATED_MERGE_ORDER)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY);

        restTransformMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformMergeJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformMergeJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformMergeJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformMergeJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransformMergeJob, transformMergeJob),
            getPersistedTransformMergeJob(transformMergeJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransformMergeJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformMergeJob using partial update
        TransformMergeJob partialUpdatedTransformMergeJob = new TransformMergeJob();
        partialUpdatedTransformMergeJob.setId(transformMergeJob.getId());

        partialUpdatedTransformMergeJob
            .name(UPDATED_NAME)
            .sourceDocumentSha256(UPDATED_SOURCE_DOCUMENT_SHA_256)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformMergeJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformMergeJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformMergeJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformMergeJobUpdatableFieldsEquals(
            partialUpdatedTransformMergeJob,
            getPersistedTransformMergeJob(partialUpdatedTransformMergeJob)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformMergeJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformMergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformMergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformMergeJob.setId(longCount.incrementAndGet());

        // Create the TransformMergeJob
        TransformMergeJobDTO transformMergeJobDTO = transformMergeJobMapper.toDto(transformMergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformMergeJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transformMergeJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformMergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformMergeJob() throws Exception {
        // Initialize the database
        insertedTransformMergeJob = transformMergeJobRepository.saveAndFlush(transformMergeJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transformMergeJob
        restTransformMergeJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformMergeJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transformMergeJobRepository.count();
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

    protected TransformMergeJob getPersistedTransformMergeJob(TransformMergeJob transformMergeJob) {
        return transformMergeJobRepository.findById(transformMergeJob.getId()).orElseThrow();
    }

    protected void assertPersistedTransformMergeJobToMatchAllProperties(TransformMergeJob expectedTransformMergeJob) {
        assertTransformMergeJobAllPropertiesEquals(expectedTransformMergeJob, getPersistedTransformMergeJob(expectedTransformMergeJob));
    }

    protected void assertPersistedTransformMergeJobToMatchUpdatableProperties(TransformMergeJob expectedTransformMergeJob) {
        assertTransformMergeJobAllUpdatablePropertiesEquals(
            expectedTransformMergeJob,
            getPersistedTransformMergeJob(expectedTransformMergeJob)
        );
    }
}
