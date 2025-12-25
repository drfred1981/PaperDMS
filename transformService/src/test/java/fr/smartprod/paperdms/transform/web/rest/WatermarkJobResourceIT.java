package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.WatermarkJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.WatermarkJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkPosition;
import fr.smartprod.paperdms.transform.domain.enumeration.WatermarkType;
import fr.smartprod.paperdms.transform.repository.WatermarkJobRepository;
import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.WatermarkJobMapper;
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
 * Integration tests for the {@link WatermarkJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WatermarkJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

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

    private static final Long DEFAULT_OUTPUT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_OUTPUT_DOCUMENT_ID = 2L;
    private static final Long SMALLER_OUTPUT_DOCUMENT_ID = 1L - 1L;

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

    private static final String ENTITY_API_URL = "/api/watermark-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WatermarkJobRepository watermarkJobRepository;

    @Autowired
    private WatermarkJobMapper watermarkJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWatermarkJobMockMvc;

    private WatermarkJob watermarkJob;

    private WatermarkJob insertedWatermarkJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WatermarkJob createEntity() {
        return new WatermarkJob()
            .documentId(DEFAULT_DOCUMENT_ID)
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
            .outputDocumentId(DEFAULT_OUTPUT_DOCUMENT_ID)
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
    public static WatermarkJob createUpdatedEntity() {
        return new WatermarkJob()
            .documentId(UPDATED_DOCUMENT_ID)
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
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        watermarkJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWatermarkJob != null) {
            watermarkJobRepository.delete(insertedWatermarkJob);
            insertedWatermarkJob = null;
        }
    }

    @Test
    @Transactional
    void createWatermarkJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);
        var returnedWatermarkJobDTO = om.readValue(
            restWatermarkJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WatermarkJobDTO.class
        );

        // Validate the WatermarkJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWatermarkJob = watermarkJobMapper.toEntity(returnedWatermarkJobDTO);
        assertWatermarkJobUpdatableFieldsEquals(returnedWatermarkJob, getPersistedWatermarkJob(returnedWatermarkJob));

        insertedWatermarkJob = returnedWatermarkJob;
    }

    @Test
    @Transactional
    void createWatermarkJobWithExistingId() throws Exception {
        // Create the WatermarkJob with an existing ID
        watermarkJob.setId(1L);
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setDocumentId(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWatermarkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setWatermarkType(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setPosition(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setStatus(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setCreatedBy(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        watermarkJob.setCreatedDate(null);

        // Create the WatermarkJob, which fails.
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        restWatermarkJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWatermarkJobs() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watermarkJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
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
            .andExpect(jsonPath("$.[*].outputDocumentId").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getWatermarkJob() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get the watermarkJob
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL_ID, watermarkJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(watermarkJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
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
            .andExpect(jsonPath("$.outputDocumentId").value(DEFAULT_OUTPUT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getWatermarkJobsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        Long id = watermarkJob.getId();

        defaultWatermarkJobFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWatermarkJobFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWatermarkJobFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId equals to
        defaultWatermarkJobFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId in
        defaultWatermarkJobFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId is not null
        defaultWatermarkJobFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId is greater than or equal to
        defaultWatermarkJobFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId is less than or equal to
        defaultWatermarkJobFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId is less than
        defaultWatermarkJobFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where documentId is greater than
        defaultWatermarkJobFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkType equals to
        defaultWatermarkJobFiltering("watermarkType.equals=" + DEFAULT_WATERMARK_TYPE, "watermarkType.equals=" + UPDATED_WATERMARK_TYPE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkType in
        defaultWatermarkJobFiltering(
            "watermarkType.in=" + DEFAULT_WATERMARK_TYPE + "," + UPDATED_WATERMARK_TYPE,
            "watermarkType.in=" + UPDATED_WATERMARK_TYPE
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkType is not null
        defaultWatermarkJobFiltering("watermarkType.specified=true", "watermarkType.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTextIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkText equals to
        defaultWatermarkJobFiltering("watermarkText.equals=" + DEFAULT_WATERMARK_TEXT, "watermarkText.equals=" + UPDATED_WATERMARK_TEXT);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTextIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkText in
        defaultWatermarkJobFiltering(
            "watermarkText.in=" + DEFAULT_WATERMARK_TEXT + "," + UPDATED_WATERMARK_TEXT,
            "watermarkText.in=" + UPDATED_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTextIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkText is not null
        defaultWatermarkJobFiltering("watermarkText.specified=true", "watermarkText.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTextContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkText contains
        defaultWatermarkJobFiltering(
            "watermarkText.contains=" + DEFAULT_WATERMARK_TEXT,
            "watermarkText.contains=" + UPDATED_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkTextNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkText does not contain
        defaultWatermarkJobFiltering(
            "watermarkText.doesNotContain=" + UPDATED_WATERMARK_TEXT,
            "watermarkText.doesNotContain=" + DEFAULT_WATERMARK_TEXT
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkImageS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkImageS3Key equals to
        defaultWatermarkJobFiltering(
            "watermarkImageS3Key.equals=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.equals=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkImageS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkImageS3Key in
        defaultWatermarkJobFiltering(
            "watermarkImageS3Key.in=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY + "," + UPDATED_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.in=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkImageS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkImageS3Key is not null
        defaultWatermarkJobFiltering("watermarkImageS3Key.specified=true", "watermarkImageS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkImageS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkImageS3Key contains
        defaultWatermarkJobFiltering(
            "watermarkImageS3Key.contains=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.contains=" + UPDATED_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByWatermarkImageS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where watermarkImageS3Key does not contain
        defaultWatermarkJobFiltering(
            "watermarkImageS3Key.doesNotContain=" + UPDATED_WATERMARK_IMAGE_S_3_KEY,
            "watermarkImageS3Key.doesNotContain=" + DEFAULT_WATERMARK_IMAGE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where position equals to
        defaultWatermarkJobFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where position in
        defaultWatermarkJobFiltering("position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION, "position.in=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where position is not null
        defaultWatermarkJobFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity equals to
        defaultWatermarkJobFiltering("opacity.equals=" + DEFAULT_OPACITY, "opacity.equals=" + UPDATED_OPACITY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity in
        defaultWatermarkJobFiltering("opacity.in=" + DEFAULT_OPACITY + "," + UPDATED_OPACITY, "opacity.in=" + UPDATED_OPACITY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity is not null
        defaultWatermarkJobFiltering("opacity.specified=true", "opacity.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity is greater than or equal to
        defaultWatermarkJobFiltering(
            "opacity.greaterThanOrEqual=" + DEFAULT_OPACITY,
            "opacity.greaterThanOrEqual=" + (DEFAULT_OPACITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity is less than or equal to
        defaultWatermarkJobFiltering("opacity.lessThanOrEqual=" + DEFAULT_OPACITY, "opacity.lessThanOrEqual=" + SMALLER_OPACITY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity is less than
        defaultWatermarkJobFiltering("opacity.lessThan=" + (DEFAULT_OPACITY + 1), "opacity.lessThan=" + DEFAULT_OPACITY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOpacityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where opacity is greater than
        defaultWatermarkJobFiltering("opacity.greaterThan=" + SMALLER_OPACITY, "opacity.greaterThan=" + DEFAULT_OPACITY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize equals to
        defaultWatermarkJobFiltering("fontSize.equals=" + DEFAULT_FONT_SIZE, "fontSize.equals=" + UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize in
        defaultWatermarkJobFiltering("fontSize.in=" + DEFAULT_FONT_SIZE + "," + UPDATED_FONT_SIZE, "fontSize.in=" + UPDATED_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize is not null
        defaultWatermarkJobFiltering("fontSize.specified=true", "fontSize.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize is greater than or equal to
        defaultWatermarkJobFiltering(
            "fontSize.greaterThanOrEqual=" + DEFAULT_FONT_SIZE,
            "fontSize.greaterThanOrEqual=" + UPDATED_FONT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize is less than or equal to
        defaultWatermarkJobFiltering("fontSize.lessThanOrEqual=" + DEFAULT_FONT_SIZE, "fontSize.lessThanOrEqual=" + SMALLER_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize is less than
        defaultWatermarkJobFiltering("fontSize.lessThan=" + UPDATED_FONT_SIZE, "fontSize.lessThan=" + DEFAULT_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByFontSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where fontSize is greater than
        defaultWatermarkJobFiltering("fontSize.greaterThan=" + SMALLER_FONT_SIZE, "fontSize.greaterThan=" + DEFAULT_FONT_SIZE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where color equals to
        defaultWatermarkJobFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where color in
        defaultWatermarkJobFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where color is not null
        defaultWatermarkJobFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where color contains
        defaultWatermarkJobFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where color does not contain
        defaultWatermarkJobFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation equals to
        defaultWatermarkJobFiltering("rotation.equals=" + DEFAULT_ROTATION, "rotation.equals=" + UPDATED_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation in
        defaultWatermarkJobFiltering("rotation.in=" + DEFAULT_ROTATION + "," + UPDATED_ROTATION, "rotation.in=" + UPDATED_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation is not null
        defaultWatermarkJobFiltering("rotation.specified=true", "rotation.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation is greater than or equal to
        defaultWatermarkJobFiltering("rotation.greaterThanOrEqual=" + DEFAULT_ROTATION, "rotation.greaterThanOrEqual=" + UPDATED_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation is less than or equal to
        defaultWatermarkJobFiltering("rotation.lessThanOrEqual=" + DEFAULT_ROTATION, "rotation.lessThanOrEqual=" + SMALLER_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation is less than
        defaultWatermarkJobFiltering("rotation.lessThan=" + UPDATED_ROTATION, "rotation.lessThan=" + DEFAULT_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByRotationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where rotation is greater than
        defaultWatermarkJobFiltering("rotation.greaterThan=" + SMALLER_ROTATION, "rotation.greaterThan=" + DEFAULT_ROTATION);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByTiledIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where tiled equals to
        defaultWatermarkJobFiltering("tiled.equals=" + DEFAULT_TILED, "tiled.equals=" + UPDATED_TILED);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByTiledIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where tiled in
        defaultWatermarkJobFiltering("tiled.in=" + DEFAULT_TILED + "," + UPDATED_TILED, "tiled.in=" + UPDATED_TILED);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByTiledIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where tiled is not null
        defaultWatermarkJobFiltering("tiled.specified=true", "tiled.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputS3Key equals to
        defaultWatermarkJobFiltering("outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputS3Key in
        defaultWatermarkJobFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputS3Key is not null
        defaultWatermarkJobFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputS3Key contains
        defaultWatermarkJobFiltering("outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputS3Key does not contain
        defaultWatermarkJobFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId equals to
        defaultWatermarkJobFiltering(
            "outputDocumentId.equals=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.equals=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId in
        defaultWatermarkJobFiltering(
            "outputDocumentId.in=" + DEFAULT_OUTPUT_DOCUMENT_ID + "," + UPDATED_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.in=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId is not null
        defaultWatermarkJobFiltering("outputDocumentId.specified=true", "outputDocumentId.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId is greater than or equal to
        defaultWatermarkJobFiltering(
            "outputDocumentId.greaterThanOrEqual=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.greaterThanOrEqual=" + UPDATED_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId is less than or equal to
        defaultWatermarkJobFiltering(
            "outputDocumentId.lessThanOrEqual=" + DEFAULT_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.lessThanOrEqual=" + SMALLER_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId is less than
        defaultWatermarkJobFiltering(
            "outputDocumentId.lessThan=" + UPDATED_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.lessThan=" + DEFAULT_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByOutputDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where outputDocumentId is greater than
        defaultWatermarkJobFiltering(
            "outputDocumentId.greaterThan=" + SMALLER_OUTPUT_DOCUMENT_ID,
            "outputDocumentId.greaterThan=" + DEFAULT_OUTPUT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where status equals to
        defaultWatermarkJobFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where status in
        defaultWatermarkJobFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where status is not null
        defaultWatermarkJobFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where startDate equals to
        defaultWatermarkJobFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where startDate in
        defaultWatermarkJobFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where startDate is not null
        defaultWatermarkJobFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where endDate equals to
        defaultWatermarkJobFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where endDate in
        defaultWatermarkJobFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where endDate is not null
        defaultWatermarkJobFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdBy equals to
        defaultWatermarkJobFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdBy in
        defaultWatermarkJobFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdBy is not null
        defaultWatermarkJobFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdBy contains
        defaultWatermarkJobFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdBy does not contain
        defaultWatermarkJobFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdDate equals to
        defaultWatermarkJobFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdDate in
        defaultWatermarkJobFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWatermarkJobsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        // Get all the watermarkJobList where createdDate is not null
        defaultWatermarkJobFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultWatermarkJobFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWatermarkJobShouldBeFound(shouldBeFound);
        defaultWatermarkJobShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWatermarkJobShouldBeFound(String filter) throws Exception {
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(watermarkJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
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
            .andExpect(jsonPath("$.[*].outputDocumentId").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWatermarkJobShouldNotBeFound(String filter) throws Exception {
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWatermarkJobMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWatermarkJob() throws Exception {
        // Get the watermarkJob
        restWatermarkJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWatermarkJob() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watermarkJob
        WatermarkJob updatedWatermarkJob = watermarkJobRepository.findById(watermarkJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWatermarkJob are not directly saved in db
        em.detach(updatedWatermarkJob);
        updatedWatermarkJob
            .documentId(UPDATED_DOCUMENT_ID)
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
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(updatedWatermarkJob);

        restWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watermarkJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watermarkJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWatermarkJobToMatchAllProperties(updatedWatermarkJob);
    }

    @Test
    @Transactional
    void putNonExistingWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, watermarkJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(watermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWatermarkJobWithPatch() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watermarkJob using partial update
        WatermarkJob partialUpdatedWatermarkJob = new WatermarkJob();
        partialUpdatedWatermarkJob.setId(watermarkJob.getId());

        partialUpdatedWatermarkJob
            .watermarkText(UPDATED_WATERMARK_TEXT)
            .opacity(UPDATED_OPACITY)
            .fontSize(UPDATED_FONT_SIZE)
            .rotation(UPDATED_ROTATION)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatermarkJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWatermarkJob))
            )
            .andExpect(status().isOk());

        // Validate the WatermarkJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatermarkJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWatermarkJob, watermarkJob),
            getPersistedWatermarkJob(watermarkJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateWatermarkJobWithPatch() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the watermarkJob using partial update
        WatermarkJob partialUpdatedWatermarkJob = new WatermarkJob();
        partialUpdatedWatermarkJob.setId(watermarkJob.getId());

        partialUpdatedWatermarkJob
            .documentId(UPDATED_DOCUMENT_ID)
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
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWatermarkJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWatermarkJob))
            )
            .andExpect(status().isOk());

        // Validate the WatermarkJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWatermarkJobUpdatableFieldsEquals(partialUpdatedWatermarkJob, getPersistedWatermarkJob(partialUpdatedWatermarkJob));
    }

    @Test
    @Transactional
    void patchNonExistingWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, watermarkJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(watermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(watermarkJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWatermarkJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        watermarkJob.setId(longCount.incrementAndGet());

        // Create the WatermarkJob
        WatermarkJobDTO watermarkJobDTO = watermarkJobMapper.toDto(watermarkJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWatermarkJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(watermarkJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WatermarkJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWatermarkJob() throws Exception {
        // Initialize the database
        insertedWatermarkJob = watermarkJobRepository.saveAndFlush(watermarkJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the watermarkJob
        restWatermarkJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, watermarkJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return watermarkJobRepository.count();
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

    protected WatermarkJob getPersistedWatermarkJob(WatermarkJob watermarkJob) {
        return watermarkJobRepository.findById(watermarkJob.getId()).orElseThrow();
    }

    protected void assertPersistedWatermarkJobToMatchAllProperties(WatermarkJob expectedWatermarkJob) {
        assertWatermarkJobAllPropertiesEquals(expectedWatermarkJob, getPersistedWatermarkJob(expectedWatermarkJob));
    }

    protected void assertPersistedWatermarkJobToMatchUpdatableProperties(WatermarkJob expectedWatermarkJob) {
        assertWatermarkJobAllUpdatablePropertiesEquals(expectedWatermarkJob, getPersistedWatermarkJob(expectedWatermarkJob));
    }
}
