package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.TransformRedactionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.TransformRedactionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.RedactionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.TransformRedactionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformRedactionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformRedactionJobMapper;
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
 * Integration tests for the {@link TransformRedactionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformRedactionJobResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_REDACTION_AREAS = "AAAAAAAAAA";
    private static final String UPDATED_REDACTION_AREAS = "BBBBBBBBBB";

    private static final RedactionType DEFAULT_REDACTION_TYPE = RedactionType.BLACK_BOX;
    private static final RedactionType UPDATED_REDACTION_TYPE = RedactionType.WHITE_BOX;

    private static final String DEFAULT_REDACTION_COLOR = "AAAAAAA";
    private static final String UPDATED_REDACTION_COLOR = "BBBBBBB";

    private static final String DEFAULT_REPLACE_WITH = "AAAAAAAAAA";
    private static final String UPDATED_REPLACE_WITH = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/transform-redaction-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransformRedactionJobRepository transformRedactionJobRepository;

    @Autowired
    private TransformRedactionJobMapper transformRedactionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformRedactionJobMockMvc;

    private TransformRedactionJob transformRedactionJob;

    private TransformRedactionJob insertedTransformRedactionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformRedactionJob createEntity() {
        return new TransformRedactionJob()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .redactionAreas(DEFAULT_REDACTION_AREAS)
            .redactionType(DEFAULT_REDACTION_TYPE)
            .redactionColor(DEFAULT_REDACTION_COLOR)
            .replaceWith(DEFAULT_REPLACE_WITH)
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
    public static TransformRedactionJob createUpdatedEntity() {
        return new TransformRedactionJob()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
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
        transformRedactionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransformRedactionJob != null) {
            transformRedactionJobRepository.delete(insertedTransformRedactionJob);
            insertedTransformRedactionJob = null;
        }
    }

    @Test
    @Transactional
    void createTransformRedactionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);
        var returnedTransformRedactionJobDTO = om.readValue(
            restTransformRedactionJobMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransformRedactionJobDTO.class
        );

        // Validate the TransformRedactionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransformRedactionJob = transformRedactionJobMapper.toEntity(returnedTransformRedactionJobDTO);
        assertTransformRedactionJobUpdatableFieldsEquals(
            returnedTransformRedactionJob,
            getPersistedTransformRedactionJob(returnedTransformRedactionJob)
        );

        insertedTransformRedactionJob = returnedTransformRedactionJob;
    }

    @Test
    @Transactional
    void createTransformRedactionJobWithExistingId() throws Exception {
        // Create the TransformRedactionJob with an existing ID
        transformRedactionJob.setId(1L);
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformRedactionJob.setDocumentSha256(null);

        // Create the TransformRedactionJob, which fails.
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRedactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformRedactionJob.setRedactionType(null);

        // Create the TransformRedactionJob, which fails.
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformRedactionJob.setStatus(null);

        // Create the TransformRedactionJob, which fails.
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformRedactionJob.setCreatedBy(null);

        // Create the TransformRedactionJob, which fails.
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformRedactionJob.setCreatedDate(null);

        // Create the TransformRedactionJob, which fails.
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobs() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformRedactionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].redactionAreas").value(hasItem(DEFAULT_REDACTION_AREAS)))
            .andExpect(jsonPath("$.[*].redactionType").value(hasItem(DEFAULT_REDACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].redactionColor").value(hasItem(DEFAULT_REDACTION_COLOR)))
            .andExpect(jsonPath("$.[*].replaceWith").value(hasItem(DEFAULT_REPLACE_WITH)))
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
    void getTransformRedactionJob() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get the transformRedactionJob
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, transformRedactionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformRedactionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.redactionAreas").value(DEFAULT_REDACTION_AREAS))
            .andExpect(jsonPath("$.redactionType").value(DEFAULT_REDACTION_TYPE.toString()))
            .andExpect(jsonPath("$.redactionColor").value(DEFAULT_REDACTION_COLOR))
            .andExpect(jsonPath("$.replaceWith").value(DEFAULT_REPLACE_WITH))
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
    void getTransformRedactionJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        Long id = transformRedactionJob.getId();

        defaultTransformRedactionJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransformRedactionJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransformRedactionJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where documentSha256 equals to
        defaultTransformRedactionJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where documentSha256 in
        defaultTransformRedactionJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where documentSha256 is not null
        defaultTransformRedactionJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where documentSha256 contains
        defaultTransformRedactionJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where documentSha256 does not contain
        defaultTransformRedactionJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionType equals to
        defaultTransformRedactionJobFiltering(
            "redactionType.equals=" + DEFAULT_REDACTION_TYPE,
            "redactionType.equals=" + UPDATED_REDACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionType in
        defaultTransformRedactionJobFiltering(
            "redactionType.in=" + DEFAULT_REDACTION_TYPE + "," + UPDATED_REDACTION_TYPE,
            "redactionType.in=" + UPDATED_REDACTION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionType is not null
        defaultTransformRedactionJobFiltering("redactionType.specified=true", "redactionType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionColor equals to
        defaultTransformRedactionJobFiltering(
            "redactionColor.equals=" + DEFAULT_REDACTION_COLOR,
            "redactionColor.equals=" + UPDATED_REDACTION_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionColor in
        defaultTransformRedactionJobFiltering(
            "redactionColor.in=" + DEFAULT_REDACTION_COLOR + "," + UPDATED_REDACTION_COLOR,
            "redactionColor.in=" + UPDATED_REDACTION_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionColor is not null
        defaultTransformRedactionJobFiltering("redactionColor.specified=true", "redactionColor.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionColor contains
        defaultTransformRedactionJobFiltering(
            "redactionColor.contains=" + DEFAULT_REDACTION_COLOR,
            "redactionColor.contains=" + UPDATED_REDACTION_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByRedactionColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where redactionColor does not contain
        defaultTransformRedactionJobFiltering(
            "redactionColor.doesNotContain=" + UPDATED_REDACTION_COLOR,
            "redactionColor.doesNotContain=" + DEFAULT_REDACTION_COLOR
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByReplaceWithIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where replaceWith equals to
        defaultTransformRedactionJobFiltering("replaceWith.equals=" + DEFAULT_REPLACE_WITH, "replaceWith.equals=" + UPDATED_REPLACE_WITH);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByReplaceWithIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where replaceWith in
        defaultTransformRedactionJobFiltering(
            "replaceWith.in=" + DEFAULT_REPLACE_WITH + "," + UPDATED_REPLACE_WITH,
            "replaceWith.in=" + UPDATED_REPLACE_WITH
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByReplaceWithIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where replaceWith is not null
        defaultTransformRedactionJobFiltering("replaceWith.specified=true", "replaceWith.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByReplaceWithContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where replaceWith contains
        defaultTransformRedactionJobFiltering(
            "replaceWith.contains=" + DEFAULT_REPLACE_WITH,
            "replaceWith.contains=" + UPDATED_REPLACE_WITH
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByReplaceWithNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where replaceWith does not contain
        defaultTransformRedactionJobFiltering(
            "replaceWith.doesNotContain=" + UPDATED_REPLACE_WITH,
            "replaceWith.doesNotContain=" + DEFAULT_REPLACE_WITH
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputS3Key equals to
        defaultTransformRedactionJobFiltering(
            "outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputS3Key in
        defaultTransformRedactionJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputS3Key is not null
        defaultTransformRedactionJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputS3Key contains
        defaultTransformRedactionJobFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputS3Key does not contain
        defaultTransformRedactionJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputDocumentSha256 equals to
        defaultTransformRedactionJobFiltering(
            "outputDocumentSha256.equals=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.equals=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputDocumentSha256 in
        defaultTransformRedactionJobFiltering(
            "outputDocumentSha256.in=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256 + "," + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.in=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputDocumentSha256 is not null
        defaultTransformRedactionJobFiltering("outputDocumentSha256.specified=true", "outputDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputDocumentSha256 contains
        defaultTransformRedactionJobFiltering(
            "outputDocumentSha256.contains=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.contains=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByOutputDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where outputDocumentSha256 does not contain
        defaultTransformRedactionJobFiltering(
            "outputDocumentSha256.doesNotContain=" + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.doesNotContain=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where status equals to
        defaultTransformRedactionJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where status in
        defaultTransformRedactionJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where status is not null
        defaultTransformRedactionJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where startDate equals to
        defaultTransformRedactionJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where startDate in
        defaultTransformRedactionJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where startDate is not null
        defaultTransformRedactionJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where endDate equals to
        defaultTransformRedactionJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where endDate in
        defaultTransformRedactionJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where endDate is not null
        defaultTransformRedactionJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdBy equals to
        defaultTransformRedactionJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdBy in
        defaultTransformRedactionJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdBy is not null
        defaultTransformRedactionJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdBy contains
        defaultTransformRedactionJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdBy does not contain
        defaultTransformRedactionJobFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdDate equals to
        defaultTransformRedactionJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdDate in
        defaultTransformRedactionJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformRedactionJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        // Get all the transformRedactionJobList where createdDate is not null
        defaultTransformRedactionJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultTransformRedactionJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransformRedactionJobShouldBeFound(shouldBeFound);
        defaultTransformRedactionJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformRedactionJobShouldBeFound(String filter) throws Exception {
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformRedactionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].redactionAreas").value(hasItem(DEFAULT_REDACTION_AREAS)))
            .andExpect(jsonPath("$.[*].redactionType").value(hasItem(DEFAULT_REDACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].redactionColor").value(hasItem(DEFAULT_REDACTION_COLOR)))
            .andExpect(jsonPath("$.[*].replaceWith").value(hasItem(DEFAULT_REPLACE_WITH)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformRedactionJobShouldNotBeFound(String filter) throws Exception {
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformRedactionJob() throws Exception {
        // Get the transformRedactionJob
        restTransformRedactionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransformRedactionJob() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformRedactionJob
        TransformRedactionJob updatedTransformRedactionJob = transformRedactionJobRepository
            .findById(transformRedactionJob.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTransformRedactionJob are not directly saved in db
        em.detach(updatedTransformRedactionJob);
        updatedTransformRedactionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(updatedTransformRedactionJob);

        restTransformRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformRedactionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransformRedactionJobToMatchAllProperties(updatedTransformRedactionJob);
    }

    @Test
    @Transactional
    void putNonExistingTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformRedactionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformRedactionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformRedactionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformRedactionJob using partial update
        TransformRedactionJob partialUpdatedTransformRedactionJob = new TransformRedactionJob();
        partialUpdatedTransformRedactionJob.setId(transformRedactionJob.getId());

        partialUpdatedTransformRedactionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .startDate(UPDATED_START_DATE);

        restTransformRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformRedactionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformRedactionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformRedactionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformRedactionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransformRedactionJob, transformRedactionJob),
            getPersistedTransformRedactionJob(transformRedactionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransformRedactionJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformRedactionJob using partial update
        TransformRedactionJob partialUpdatedTransformRedactionJob = new TransformRedactionJob();
        partialUpdatedTransformRedactionJob.setId(transformRedactionJob.getId());

        partialUpdatedTransformRedactionJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformRedactionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformRedactionJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformRedactionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformRedactionJobUpdatableFieldsEquals(
            partialUpdatedTransformRedactionJob,
            getPersistedTransformRedactionJob(partialUpdatedTransformRedactionJob)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformRedactionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformRedactionJob.setId(longCount.incrementAndGet());

        // Create the TransformRedactionJob
        TransformRedactionJobDTO transformRedactionJobDTO = transformRedactionJobMapper.toDto(transformRedactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transformRedactionJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformRedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformRedactionJob() throws Exception {
        // Initialize the database
        insertedTransformRedactionJob = transformRedactionJobRepository.saveAndFlush(transformRedactionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transformRedactionJob
        restTransformRedactionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformRedactionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transformRedactionJobRepository.count();
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

    protected TransformRedactionJob getPersistedTransformRedactionJob(TransformRedactionJob transformRedactionJob) {
        return transformRedactionJobRepository.findById(transformRedactionJob.getId()).orElseThrow();
    }

    protected void assertPersistedTransformRedactionJobToMatchAllProperties(TransformRedactionJob expectedTransformRedactionJob) {
        assertTransformRedactionJobAllPropertiesEquals(
            expectedTransformRedactionJob,
            getPersistedTransformRedactionJob(expectedTransformRedactionJob)
        );
    }

    protected void assertPersistedTransformRedactionJobToMatchUpdatableProperties(TransformRedactionJob expectedTransformRedactionJob) {
        assertTransformRedactionJobAllUpdatablePropertiesEquals(
            expectedTransformRedactionJob,
            getPersistedTransformRedactionJob(expectedTransformRedactionJob)
        );
    }
}
