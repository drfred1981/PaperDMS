package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.TransformWatermarkJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.TransformWatermarkJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkPosition;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkType;
import fr.smartprod.paperdms.transform.repository.TransformWatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.dto.TransformWatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.TransformWatermarkJobMapper;
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
 * Integration tests for the {@link TransformWatermarkJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TransformWatermarkJobResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final WatermarkType DEFAULT_WATERMARK_TYPE = WatermarkType.TEXT;
    private static final WatermarkType UPDATED_WATERMARK_TYPE = WatermarkType.IMAGE;

    private static final String DEFAULT_WATERMARK_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_WATERMARK_TEXT = "BBBBBBBBBB";

    private static final String DEFAULT_WATERMARK_IMAGE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_WATERMARK_IMAGE_S_3_KEY = "BBBBBBBBBB";

    private static final WatermarkPosition DEFAULT_POSITION = WatermarkPosition.TOP_LEFT;
    private static final WatermarkPosition UPDATED_POSITION = WatermarkPosition.TOP_CENTER;

    private static final Integer DEFAULT_OPACITY = 0;
    private static final Integer UPDATED_OPACITY = 1;
    private static final Integer SMALLER_OPACITY = 0 - 1;

    private static final Integer DEFAULT_FONT_SIZE = 1;
    private static final Integer UPDATED_FONT_SIZE = 2;
    private static final Integer SMALLER_FONT_SIZE = 1 - 1;

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final Integer DEFAULT_ROTATION = 1;
    private static final Integer UPDATED_ROTATION = 2;
    private static final Integer SMALLER_ROTATION = 1 - 1;

    private static final Boolean DEFAULT_TILED = false;
    private static final Boolean UPDATED_TILED = true;

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

    private static final String ENTITY_API_URL = "/api/transform-watermark-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TransformWatermarkJobRepository transformWatermarkJobRepository;

    @Autowired
    private TransformWatermarkJobMapper transformWatermarkJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTransformWatermarkJobMockMvc;

    private TransformWatermarkJob transformWatermarkJob;

    private TransformWatermarkJob insertedTransformWatermarkJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TransformWatermarkJob createEntity() {
        return new TransformWatermarkJob()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .watermarkType(DEFAULT_WATERMARK_TYPE)
            .watermarkText(DEFAULT_WATERMARK_TEXT)
            .watermarkImageS3Key(DEFAULT_WATERMARK_IMAGE_S_3_KEY)
            .position(DEFAULT_POSITION)
            .opacity(DEFAULT_OPACITY)
            .fontSize(DEFAULT_FONT_SIZE)
            .color(DEFAULT_COLOR)
            .rotation(DEFAULT_ROTATION)
            .tiled(DEFAULT_TILED)
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
    public static TransformWatermarkJob createUpdatedEntity() {
        return new TransformWatermarkJob()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .watermarkType(UPDATED_WATERMARK_TYPE)
            .watermarkText(UPDATED_WATERMARK_TEXT)
            .watermarkImageS3Key(UPDATED_WATERMARK_IMAGE_S_3_KEY)
            .position(UPDATED_POSITION)
            .opacity(UPDATED_OPACITY)
            .fontSize(UPDATED_FONT_SIZE)
            .color(UPDATED_COLOR)
            .rotation(UPDATED_ROTATION)
            .tiled(UPDATED_TILED)
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
        transformWatermarkJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTransformWatermarkJob != null) {
            transformWatermarkJobRepository.delete(insertedTransformWatermarkJob);
            insertedTransformWatermarkJob = null;
        }
    }

    @Test
    @Transactional
    void createTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);
        var returnedTransformWatermarkJobDTO = om.readValue(
            restTransformWatermarkJobMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TransformWatermarkJobDTO.class
        );

        // Validate the TransformWatermarkJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTransformWatermarkJob = transformWatermarkJobMapper.toEntity(returnedTransformWatermarkJobDTO);
        assertTransformWatermarkJobUpdatableFieldsEquals(
            returnedTransformWatermarkJob,
            getPersistedTransformWatermarkJob(returnedTransformWatermarkJob)
        );

        insertedTransformWatermarkJob = returnedTransformWatermarkJob;
    }

    @Test
    @Transactional
    void createTransformWatermarkJobWithExistingId() throws Exception {
        // Create the TransformWatermarkJob with an existing ID
        transformWatermarkJob.setId(1L);
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setDocumentSha256(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWatermarkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setWatermarkType(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setPosition(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setStatus(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setCreatedBy(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        transformWatermarkJob.setCreatedDate(null);

        // Create the TransformWatermarkJob, which fails.
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobs() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformWatermarkJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].watermarkType").value(hasItem(DEFAULT_WATERMARK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].watermarkText").value(hasItem(DEFAULT_WATERMARK_TEXT)))
            .andExpect(jsonPath("$.[*].watermarkImageS3Key").value(hasItem(DEFAULT_WATERMARK_IMAGE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].opacity").value(hasItem(DEFAULT_OPACITY)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].rotation").value(hasItem(DEFAULT_ROTATION)))
            .andExpect(jsonPath("$.[*].tiled").value(hasItem(DEFAULT_TILED)))
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
    void getTransformWatermarkJob() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get the transformWatermarkJob
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL_ID, transformWatermarkJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(transformWatermarkJob.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.watermarkType").value(DEFAULT_WATERMARK_TYPE.toString()))
            .andExpect(jsonPath("$.watermarkText").value(DEFAULT_WATERMARK_TEXT))
            .andExpect(jsonPath("$.watermarkImageS3Key").value(DEFAULT_WATERMARK_IMAGE_S_3_KEY))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION.toString()))
            .andExpect(jsonPath("$.opacity").value(DEFAULT_OPACITY))
            .andExpect(jsonPath("$.fontSize").value(DEFAULT_FONT_SIZE))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.rotation").value(DEFAULT_ROTATION))
            .andExpect(jsonPath("$.tiled").value(DEFAULT_TILED))
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
    void getTransformWatermarkJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        Long id = transformWatermarkJob.getId();

        defaultTransformWatermarkJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultTransformWatermarkJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultTransformWatermarkJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where documentSha256 equals to
        defaultTransformWatermarkJobFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where documentSha256 in
        defaultTransformWatermarkJobFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where documentSha256 is not null
        defaultTransformWatermarkJobFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where documentSha256 contains
        defaultTransformWatermarkJobFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where documentSha256 does not contain
        defaultTransformWatermarkJobFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkType equals to
        defaultTransformWatermarkJobFiltering(
            "watermarkType.equals=" + DEFAULT_WATERMARK_TYPE,
            "watermarkType.equals=" + UPDATED_WATERMARK_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkType in
        defaultTransformWatermarkJobFiltering(
            "watermarkType.in=" + DEFAULT_WATERMARK_TYPE + "," + UPDATED_WATERMARK_TYPE,
            "watermarkType.in=" + UPDATED_WATERMARK_TYPE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkType is not null
        defaultTransformWatermarkJobFiltering("watermarkType.specified=true", "watermarkType.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkText equals to
        defaultTransformWatermarkJobFiltering(
            "watermarkText.equals=" + DEFAULT_WATERMARK_TEXT,
            "watermarkText.equals=" + UPDATED_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkText in
        defaultTransformWatermarkJobFiltering(
            "watermarkText.in=" + DEFAULT_WATERMARK_TEXT + "," + UPDATED_WATERMARK_TEXT,
            "watermarkText.in=" + UPDATED_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkText is not null
        defaultTransformWatermarkJobFiltering("watermarkText.specified=true", "watermarkText.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTextContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkText contains
        defaultTransformWatermarkJobFiltering(
            "watermarkText.contains=" + DEFAULT_WATERMARK_TEXT,
            "watermarkText.contains=" + UPDATED_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkText does not contain
        defaultTransformWatermarkJobFiltering(
            "watermarkText.doesNotContain=" + UPDATED_WATERMARK_TEXT,
            "watermarkText.doesNotContain=" + DEFAULT_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkImageS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkImageS3Key equals to
        defaultTransformWatermarkJobFiltering(
            "watermarkImageS3Key.equals=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.equals=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkImageS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkImageS3Key in
        defaultTransformWatermarkJobFiltering(
            "watermarkImageS3Key.in=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY + "," + UPDATED_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.in=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkImageS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkImageS3Key is not null
        defaultTransformWatermarkJobFiltering("watermarkImageS3Key.specified=true", "watermarkImageS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkImageS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkImageS3Key contains
        defaultTransformWatermarkJobFiltering(
            "watermarkImageS3Key.contains=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.contains=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByWatermarkImageS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where watermarkImageS3Key does not contain
        defaultTransformWatermarkJobFiltering(
            "watermarkImageS3Key.doesNotContain=" + UPDATED_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.doesNotContain=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where position equals to
        defaultTransformWatermarkJobFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where position in
        defaultTransformWatermarkJobFiltering(
            "position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION,
            "position.in=" + UPDATED_POSITION
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where position is not null
        defaultTransformWatermarkJobFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity equals to
        defaultTransformWatermarkJobFiltering("opacity.equals=" + DEFAULT_OPACITY, "opacity.equals=" + UPDATED_OPACITY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity in
        defaultTransformWatermarkJobFiltering("opacity.in=" + DEFAULT_OPACITY + "," + UPDATED_OPACITY, "opacity.in=" + UPDATED_OPACITY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity is not null
        defaultTransformWatermarkJobFiltering("opacity.specified=true", "opacity.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity is greater than or equal to
        defaultTransformWatermarkJobFiltering(
            "opacity.greaterThanOrEqual=" + DEFAULT_OPACITY,
            "opacity.greaterThanOrEqual=" + (DEFAULT_OPACITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity is less than or equal to
        defaultTransformWatermarkJobFiltering("opacity.lessThanOrEqual=" + DEFAULT_OPACITY, "opacity.lessThanOrEqual=" + SMALLER_OPACITY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity is less than
        defaultTransformWatermarkJobFiltering("opacity.lessThan=" + (DEFAULT_OPACITY + 1), "opacity.lessThan=" + DEFAULT_OPACITY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOpacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where opacity is greater than
        defaultTransformWatermarkJobFiltering("opacity.greaterThan=" + SMALLER_OPACITY, "opacity.greaterThan=" + DEFAULT_OPACITY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize equals to
        defaultTransformWatermarkJobFiltering("fontSize.equals=" + DEFAULT_FONT_SIZE, "fontSize.equals=" + UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize in
        defaultTransformWatermarkJobFiltering(
            "fontSize.in=" + DEFAULT_FONT_SIZE + "," + UPDATED_FONT_SIZE,
            "fontSize.in=" + UPDATED_FONT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize is not null
        defaultTransformWatermarkJobFiltering("fontSize.specified=true", "fontSize.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize is greater than or equal to
        defaultTransformWatermarkJobFiltering(
            "fontSize.greaterThanOrEqual=" + DEFAULT_FONT_SIZE,
            "fontSize.greaterThanOrEqual=" + UPDATED_FONT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize is less than or equal to
        defaultTransformWatermarkJobFiltering(
            "fontSize.lessThanOrEqual=" + DEFAULT_FONT_SIZE,
            "fontSize.lessThanOrEqual=" + SMALLER_FONT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize is less than
        defaultTransformWatermarkJobFiltering("fontSize.lessThan=" + UPDATED_FONT_SIZE, "fontSize.lessThan=" + DEFAULT_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByFontSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where fontSize is greater than
        defaultTransformWatermarkJobFiltering("fontSize.greaterThan=" + SMALLER_FONT_SIZE, "fontSize.greaterThan=" + DEFAULT_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where color equals to
        defaultTransformWatermarkJobFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where color in
        defaultTransformWatermarkJobFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where color is not null
        defaultTransformWatermarkJobFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where color contains
        defaultTransformWatermarkJobFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where color does not contain
        defaultTransformWatermarkJobFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation equals to
        defaultTransformWatermarkJobFiltering("rotation.equals=" + DEFAULT_ROTATION, "rotation.equals=" + UPDATED_ROTATION);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation in
        defaultTransformWatermarkJobFiltering(
            "rotation.in=" + DEFAULT_ROTATION + "," + UPDATED_ROTATION,
            "rotation.in=" + UPDATED_ROTATION
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation is not null
        defaultTransformWatermarkJobFiltering("rotation.specified=true", "rotation.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation is greater than or equal to
        defaultTransformWatermarkJobFiltering(
            "rotation.greaterThanOrEqual=" + DEFAULT_ROTATION,
            "rotation.greaterThanOrEqual=" + UPDATED_ROTATION
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation is less than or equal to
        defaultTransformWatermarkJobFiltering(
            "rotation.lessThanOrEqual=" + DEFAULT_ROTATION,
            "rotation.lessThanOrEqual=" + SMALLER_ROTATION
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation is less than
        defaultTransformWatermarkJobFiltering("rotation.lessThan=" + UPDATED_ROTATION, "rotation.lessThan=" + DEFAULT_ROTATION);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByRotationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where rotation is greater than
        defaultTransformWatermarkJobFiltering("rotation.greaterThan=" + SMALLER_ROTATION, "rotation.greaterThan=" + DEFAULT_ROTATION);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByTiledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where tiled equals to
        defaultTransformWatermarkJobFiltering("tiled.equals=" + DEFAULT_TILED, "tiled.equals=" + UPDATED_TILED);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByTiledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where tiled in
        defaultTransformWatermarkJobFiltering("tiled.in=" + DEFAULT_TILED + "," + UPDATED_TILED, "tiled.in=" + UPDATED_TILED);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByTiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where tiled is not null
        defaultTransformWatermarkJobFiltering("tiled.specified=true", "tiled.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputS3Key equals to
        defaultTransformWatermarkJobFiltering(
            "outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputS3Key in
        defaultTransformWatermarkJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputS3Key is not null
        defaultTransformWatermarkJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputS3Key contains
        defaultTransformWatermarkJobFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputS3Key does not contain
        defaultTransformWatermarkJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputDocumentSha256 equals to
        defaultTransformWatermarkJobFiltering(
            "outputDocumentSha256.equals=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.equals=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputDocumentSha256 in
        defaultTransformWatermarkJobFiltering(
            "outputDocumentSha256.in=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256 + "," + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.in=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputDocumentSha256 is not null
        defaultTransformWatermarkJobFiltering("outputDocumentSha256.specified=true", "outputDocumentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputDocumentSha256 contains
        defaultTransformWatermarkJobFiltering(
            "outputDocumentSha256.contains=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.contains=" + UPDATED_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByOutputDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where outputDocumentSha256 does not contain
        defaultTransformWatermarkJobFiltering(
            "outputDocumentSha256.doesNotContain=" + UPDATED_OUTPUT_DOCUMENT_SHA_256,
            "outputDocumentSha256.doesNotContain=" + DEFAULT_OUTPUT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where status equals to
        defaultTransformWatermarkJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where status in
        defaultTransformWatermarkJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where status is not null
        defaultTransformWatermarkJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where startDate equals to
        defaultTransformWatermarkJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where startDate in
        defaultTransformWatermarkJobFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where startDate is not null
        defaultTransformWatermarkJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where endDate equals to
        defaultTransformWatermarkJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where endDate in
        defaultTransformWatermarkJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where endDate is not null
        defaultTransformWatermarkJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdBy equals to
        defaultTransformWatermarkJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdBy in
        defaultTransformWatermarkJobFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdBy is not null
        defaultTransformWatermarkJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdBy contains
        defaultTransformWatermarkJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdBy does not contain
        defaultTransformWatermarkJobFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdDate equals to
        defaultTransformWatermarkJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdDate in
        defaultTransformWatermarkJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllTransformWatermarkJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        // Get all the transformWatermarkJobList where createdDate is not null
        defaultTransformWatermarkJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultTransformWatermarkJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultTransformWatermarkJobShouldBeFound(shouldBeFound);
        defaultTransformWatermarkJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultTransformWatermarkJobShouldBeFound(String filter) throws Exception {
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(transformWatermarkJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].watermarkType").value(hasItem(DEFAULT_WATERMARK_TYPE.toString())))
            .andExpect(jsonPath("$.[*].watermarkText").value(hasItem(DEFAULT_WATERMARK_TEXT)))
            .andExpect(jsonPath("$.[*].watermarkImageS3Key").value(hasItem(DEFAULT_WATERMARK_IMAGE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION.toString())))
            .andExpect(jsonPath("$.[*].opacity").value(hasItem(DEFAULT_OPACITY)))
            .andExpect(jsonPath("$.[*].fontSize").value(hasItem(DEFAULT_FONT_SIZE)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].rotation").value(hasItem(DEFAULT_ROTATION)))
            .andExpect(jsonPath("$.[*].tiled").value(hasItem(DEFAULT_TILED)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentSha256").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultTransformWatermarkJobShouldNotBeFound(String filter) throws Exception {
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restTransformWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingTransformWatermarkJob() throws Exception {
        // Get the transformWatermarkJob
        restTransformWatermarkJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTransformWatermarkJob() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformWatermarkJob
        TransformWatermarkJob updatedTransformWatermarkJob = transformWatermarkJobRepository
            .findById(transformWatermarkJob.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedTransformWatermarkJob are not directly saved in db
        em.detach(updatedTransformWatermarkJob);
        updatedTransformWatermarkJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .watermarkType(UPDATED_WATERMARK_TYPE)
            .watermarkText(UPDATED_WATERMARK_TEXT)
            .watermarkImageS3Key(UPDATED_WATERMARK_IMAGE_S_3_KEY)
            .position(UPDATED_POSITION)
            .opacity(UPDATED_OPACITY)
            .fontSize(UPDATED_FONT_SIZE)
            .color(UPDATED_COLOR)
            .rotation(UPDATED_ROTATION)
            .tiled(UPDATED_TILED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(updatedTransformWatermarkJob);

        restTransformWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformWatermarkJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTransformWatermarkJobToMatchAllProperties(updatedTransformWatermarkJob);
    }

    @Test
    @Transactional
    void putNonExistingTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, transformWatermarkJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(transformWatermarkJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTransformWatermarkJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformWatermarkJob using partial update
        TransformWatermarkJob partialUpdatedTransformWatermarkJob = new TransformWatermarkJob();
        partialUpdatedTransformWatermarkJob.setId(transformWatermarkJob.getId());

        partialUpdatedTransformWatermarkJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .watermarkType(UPDATED_WATERMARK_TYPE)
            .watermarkText(UPDATED_WATERMARK_TEXT)
            .position(UPDATED_POSITION)
            .opacity(UPDATED_OPACITY)
            .fontSize(UPDATED_FONT_SIZE)
            .color(UPDATED_COLOR)
            .tiled(UPDATED_TILED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformWatermarkJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformWatermarkJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformWatermarkJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformWatermarkJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTransformWatermarkJob, transformWatermarkJob),
            getPersistedTransformWatermarkJob(transformWatermarkJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateTransformWatermarkJobWithPatch() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the transformWatermarkJob using partial update
        TransformWatermarkJob partialUpdatedTransformWatermarkJob = new TransformWatermarkJob();
        partialUpdatedTransformWatermarkJob.setId(transformWatermarkJob.getId());

        partialUpdatedTransformWatermarkJob
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .watermarkType(UPDATED_WATERMARK_TYPE)
            .watermarkText(UPDATED_WATERMARK_TEXT)
            .watermarkImageS3Key(UPDATED_WATERMARK_IMAGE_S_3_KEY)
            .position(UPDATED_POSITION)
            .opacity(UPDATED_OPACITY)
            .fontSize(UPDATED_FONT_SIZE)
            .color(UPDATED_COLOR)
            .rotation(UPDATED_ROTATION)
            .tiled(UPDATED_TILED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentSha256(UPDATED_OUTPUT_DOCUMENT_SHA_256)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restTransformWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTransformWatermarkJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTransformWatermarkJob))
            )
            .andExpect(status().isOk());

        // Validate the TransformWatermarkJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTransformWatermarkJobUpdatableFieldsEquals(
            partialUpdatedTransformWatermarkJob,
            getPersistedTransformWatermarkJob(partialUpdatedTransformWatermarkJob)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, transformWatermarkJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTransformWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        transformWatermarkJob.setId(longCount.incrementAndGet());

        // Create the TransformWatermarkJob
        TransformWatermarkJobDTO transformWatermarkJobDTO = transformWatermarkJobMapper.toDto(transformWatermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTransformWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(transformWatermarkJobDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TransformWatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTransformWatermarkJob() throws Exception {
        // Initialize the database
        insertedTransformWatermarkJob = transformWatermarkJobRepository.saveAndFlush(transformWatermarkJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the transformWatermarkJob
        restTransformWatermarkJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, transformWatermarkJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return transformWatermarkJobRepository.count();
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

    protected TransformWatermarkJob getPersistedTransformWatermarkJob(TransformWatermarkJob transformWatermarkJob) {
        return transformWatermarkJobRepository.findById(transformWatermarkJob.getId()).orElseThrow();
    }

    protected void assertPersistedTransformWatermarkJobToMatchAllProperties(TransformWatermarkJob expectedTransformWatermarkJob) {
        assertTransformWatermarkJobAllPropertiesEquals(
            expectedTransformWatermarkJob,
            getPersistedTransformWatermarkJob(expectedTransformWatermarkJob)
        );
    }

    protected void assertPersistedTransformWatermarkJobToMatchUpdatableProperties(TransformWatermarkJob expectedTransformWatermarkJob) {
        assertTransformWatermarkJobAllUpdatablePropertiesEquals(
            expectedTransformWatermarkJob,
            getPersistedTransformWatermarkJob(expectedTransformWatermarkJob)
        );
    }
}
