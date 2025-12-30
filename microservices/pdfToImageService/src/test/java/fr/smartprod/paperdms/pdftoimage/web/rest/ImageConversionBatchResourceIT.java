package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatchAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.pdftoimage.IntegrationTest;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionBatchRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionBatchSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionBatchDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionBatchMapper;
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
 * Integration tests for the {@link ImageConversionBatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageConversionBatchResourceIT {

    private static final String DEFAULT_BATCH_NAME = "AAAAAAAAAA";
    private static final String UPDATED_BATCH_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ConversionStatus DEFAULT_STATUS = ConversionStatus.PENDING;
    private static final ConversionStatus UPDATED_STATUS = ConversionStatus.PROCESSING;

    private static final Integer DEFAULT_TOTAL_CONVERSIONS = 0;
    private static final Integer UPDATED_TOTAL_CONVERSIONS = 1;
    private static final Integer SMALLER_TOTAL_CONVERSIONS = 0 - 1;

    private static final Integer DEFAULT_COMPLETED_CONVERSIONS = 0;
    private static final Integer UPDATED_COMPLETED_CONVERSIONS = 1;
    private static final Integer SMALLER_COMPLETED_CONVERSIONS = 0 - 1;

    private static final Integer DEFAULT_FAILED_CONVERSIONS = 0;
    private static final Integer UPDATED_FAILED_CONVERSIONS = 1;
    private static final Integer SMALLER_FAILED_CONVERSIONS = 0 - 1;

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_TOTAL_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_TOTAL_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_TOTAL_PROCESSING_DURATION = 1L - 1L;

    private static final Long DEFAULT_CREATED_BY_USER_ID = 1L;
    private static final Long UPDATED_CREATED_BY_USER_ID = 2L;
    private static final Long SMALLER_CREATED_BY_USER_ID = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/image-conversion-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-conversion-batches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageConversionBatchRepository imageConversionBatchRepository;

    @Autowired
    private ImageConversionBatchMapper imageConversionBatchMapper;

    @Autowired
    private ImageConversionBatchSearchRepository imageConversionBatchSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageConversionBatchMockMvc;

    private ImageConversionBatch imageConversionBatch;

    private ImageConversionBatch insertedImageConversionBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionBatch createEntity() {
        return new ImageConversionBatch()
            .batchName(DEFAULT_BATCH_NAME)
            .description(DEFAULT_DESCRIPTION)
            .createdAt(DEFAULT_CREATED_AT)
            .status(DEFAULT_STATUS)
            .totalConversions(DEFAULT_TOTAL_CONVERSIONS)
            .completedConversions(DEFAULT_COMPLETED_CONVERSIONS)
            .failedConversions(DEFAULT_FAILED_CONVERSIONS)
            .startedAt(DEFAULT_STARTED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .totalProcessingDuration(DEFAULT_TOTAL_PROCESSING_DURATION)
            .createdByUserId(DEFAULT_CREATED_BY_USER_ID);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionBatch createUpdatedEntity() {
        return new ImageConversionBatch()
            .batchName(UPDATED_BATCH_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .completedConversions(UPDATED_COMPLETED_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .totalProcessingDuration(UPDATED_TOTAL_PROCESSING_DURATION)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID);
    }

    @BeforeEach
    void initTest() {
        imageConversionBatch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImageConversionBatch != null) {
            imageConversionBatchRepository.delete(insertedImageConversionBatch);
            imageConversionBatchSearchRepository.delete(insertedImageConversionBatch);
            insertedImageConversionBatch = null;
        }
    }

    @Test
    @Transactional
    void createImageConversionBatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);
        var returnedImageConversionBatchDTO = om.readValue(
            restImageConversionBatchMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageConversionBatchDTO.class
        );

        // Validate the ImageConversionBatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageConversionBatch = imageConversionBatchMapper.toEntity(returnedImageConversionBatchDTO);
        assertImageConversionBatchUpdatableFieldsEquals(
            returnedImageConversionBatch,
            getPersistedImageConversionBatch(returnedImageConversionBatch)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImageConversionBatch = returnedImageConversionBatch;
    }

    @Test
    @Transactional
    void createImageConversionBatchWithExistingId() throws Exception {
        // Create the ImageConversionBatch with an existing ID
        imageConversionBatch.setId(1L);
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageConversionBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkBatchNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        // set the field null
        imageConversionBatch.setBatchName(null);

        // Create the ImageConversionBatch, which fails.
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        restImageConversionBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        // set the field null
        imageConversionBatch.setCreatedAt(null);

        // Create the ImageConversionBatch, which fails.
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        restImageConversionBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        // set the field null
        imageConversionBatch.setStatus(null);

        // Create the ImageConversionBatch, which fails.
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        restImageConversionBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImageConversionBatches() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].completedConversions").value(hasItem(DEFAULT_COMPLETED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].totalProcessingDuration").value(hasItem(DEFAULT_TOTAL_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].createdByUserId").value(hasItem(DEFAULT_CREATED_BY_USER_ID.intValue())));
    }

    @Test
    @Transactional
    void getImageConversionBatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get the imageConversionBatch
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, imageConversionBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageConversionBatch.getId().intValue()))
            .andExpect(jsonPath("$.batchName").value(DEFAULT_BATCH_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.totalConversions").value(DEFAULT_TOTAL_CONVERSIONS))
            .andExpect(jsonPath("$.completedConversions").value(DEFAULT_COMPLETED_CONVERSIONS))
            .andExpect(jsonPath("$.failedConversions").value(DEFAULT_FAILED_CONVERSIONS))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.totalProcessingDuration").value(DEFAULT_TOTAL_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.createdByUserId").value(DEFAULT_CREATED_BY_USER_ID.intValue()));
    }

    @Test
    @Transactional
    void getImageConversionBatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        Long id = imageConversionBatch.getId();

        defaultImageConversionBatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageConversionBatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageConversionBatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByBatchNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where batchName equals to
        defaultImageConversionBatchFiltering("batchName.equals=" + DEFAULT_BATCH_NAME, "batchName.equals=" + UPDATED_BATCH_NAME);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByBatchNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where batchName in
        defaultImageConversionBatchFiltering(
            "batchName.in=" + DEFAULT_BATCH_NAME + "," + UPDATED_BATCH_NAME,
            "batchName.in=" + UPDATED_BATCH_NAME
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByBatchNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where batchName is not null
        defaultImageConversionBatchFiltering("batchName.specified=true", "batchName.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByBatchNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where batchName contains
        defaultImageConversionBatchFiltering("batchName.contains=" + DEFAULT_BATCH_NAME, "batchName.contains=" + UPDATED_BATCH_NAME);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByBatchNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where batchName does not contain
        defaultImageConversionBatchFiltering(
            "batchName.doesNotContain=" + UPDATED_BATCH_NAME,
            "batchName.doesNotContain=" + DEFAULT_BATCH_NAME
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where description equals to
        defaultImageConversionBatchFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where description in
        defaultImageConversionBatchFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where description is not null
        defaultImageConversionBatchFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where description contains
        defaultImageConversionBatchFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where description does not contain
        defaultImageConversionBatchFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdAt equals to
        defaultImageConversionBatchFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdAt in
        defaultImageConversionBatchFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdAt is not null
        defaultImageConversionBatchFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where status equals to
        defaultImageConversionBatchFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where status in
        defaultImageConversionBatchFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where status is not null
        defaultImageConversionBatchFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions equals to
        defaultImageConversionBatchFiltering(
            "totalConversions.equals=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.equals=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions in
        defaultImageConversionBatchFiltering(
            "totalConversions.in=" + DEFAULT_TOTAL_CONVERSIONS + "," + UPDATED_TOTAL_CONVERSIONS,
            "totalConversions.in=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions is not null
        defaultImageConversionBatchFiltering("totalConversions.specified=true", "totalConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions is greater than or equal to
        defaultImageConversionBatchFiltering(
            "totalConversions.greaterThanOrEqual=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.greaterThanOrEqual=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions is less than or equal to
        defaultImageConversionBatchFiltering(
            "totalConversions.lessThanOrEqual=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.lessThanOrEqual=" + SMALLER_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions is less than
        defaultImageConversionBatchFiltering(
            "totalConversions.lessThan=" + UPDATED_TOTAL_CONVERSIONS,
            "totalConversions.lessThan=" + DEFAULT_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalConversions is greater than
        defaultImageConversionBatchFiltering(
            "totalConversions.greaterThan=" + SMALLER_TOTAL_CONVERSIONS,
            "totalConversions.greaterThan=" + DEFAULT_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions equals to
        defaultImageConversionBatchFiltering(
            "completedConversions.equals=" + DEFAULT_COMPLETED_CONVERSIONS,
            "completedConversions.equals=" + UPDATED_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions in
        defaultImageConversionBatchFiltering(
            "completedConversions.in=" + DEFAULT_COMPLETED_CONVERSIONS + "," + UPDATED_COMPLETED_CONVERSIONS,
            "completedConversions.in=" + UPDATED_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions is not null
        defaultImageConversionBatchFiltering("completedConversions.specified=true", "completedConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions is greater than or equal to
        defaultImageConversionBatchFiltering(
            "completedConversions.greaterThanOrEqual=" + DEFAULT_COMPLETED_CONVERSIONS,
            "completedConversions.greaterThanOrEqual=" + UPDATED_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions is less than or equal to
        defaultImageConversionBatchFiltering(
            "completedConversions.lessThanOrEqual=" + DEFAULT_COMPLETED_CONVERSIONS,
            "completedConversions.lessThanOrEqual=" + SMALLER_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions is less than
        defaultImageConversionBatchFiltering(
            "completedConversions.lessThan=" + UPDATED_COMPLETED_CONVERSIONS,
            "completedConversions.lessThan=" + DEFAULT_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedConversions is greater than
        defaultImageConversionBatchFiltering(
            "completedConversions.greaterThan=" + SMALLER_COMPLETED_CONVERSIONS,
            "completedConversions.greaterThan=" + DEFAULT_COMPLETED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions equals to
        defaultImageConversionBatchFiltering(
            "failedConversions.equals=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.equals=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions in
        defaultImageConversionBatchFiltering(
            "failedConversions.in=" + DEFAULT_FAILED_CONVERSIONS + "," + UPDATED_FAILED_CONVERSIONS,
            "failedConversions.in=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions is not null
        defaultImageConversionBatchFiltering("failedConversions.specified=true", "failedConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions is greater than or equal to
        defaultImageConversionBatchFiltering(
            "failedConversions.greaterThanOrEqual=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.greaterThanOrEqual=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions is less than or equal to
        defaultImageConversionBatchFiltering(
            "failedConversions.lessThanOrEqual=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.lessThanOrEqual=" + SMALLER_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions is less than
        defaultImageConversionBatchFiltering(
            "failedConversions.lessThan=" + UPDATED_FAILED_CONVERSIONS,
            "failedConversions.lessThan=" + DEFAULT_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByFailedConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where failedConversions is greater than
        defaultImageConversionBatchFiltering(
            "failedConversions.greaterThan=" + SMALLER_FAILED_CONVERSIONS,
            "failedConversions.greaterThan=" + DEFAULT_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where startedAt equals to
        defaultImageConversionBatchFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where startedAt in
        defaultImageConversionBatchFiltering(
            "startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT,
            "startedAt.in=" + UPDATED_STARTED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where startedAt is not null
        defaultImageConversionBatchFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedAt equals to
        defaultImageConversionBatchFiltering("completedAt.equals=" + DEFAULT_COMPLETED_AT, "completedAt.equals=" + UPDATED_COMPLETED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedAt in
        defaultImageConversionBatchFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where completedAt is not null
        defaultImageConversionBatchFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration equals to
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.equals=" + DEFAULT_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.equals=" + UPDATED_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration in
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.in=" + DEFAULT_TOTAL_PROCESSING_DURATION + "," + UPDATED_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.in=" + UPDATED_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration is not null
        defaultImageConversionBatchFiltering("totalProcessingDuration.specified=true", "totalProcessingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration is greater than or equal to
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.greaterThanOrEqual=" + DEFAULT_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.greaterThanOrEqual=" + UPDATED_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration is less than or equal to
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.lessThanOrEqual=" + DEFAULT_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.lessThanOrEqual=" + SMALLER_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration is less than
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.lessThan=" + UPDATED_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.lessThan=" + DEFAULT_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByTotalProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where totalProcessingDuration is greater than
        defaultImageConversionBatchFiltering(
            "totalProcessingDuration.greaterThan=" + SMALLER_TOTAL_PROCESSING_DURATION,
            "totalProcessingDuration.greaterThan=" + DEFAULT_TOTAL_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId equals to
        defaultImageConversionBatchFiltering(
            "createdByUserId.equals=" + DEFAULT_CREATED_BY_USER_ID,
            "createdByUserId.equals=" + UPDATED_CREATED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId in
        defaultImageConversionBatchFiltering(
            "createdByUserId.in=" + DEFAULT_CREATED_BY_USER_ID + "," + UPDATED_CREATED_BY_USER_ID,
            "createdByUserId.in=" + UPDATED_CREATED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId is not null
        defaultImageConversionBatchFiltering("createdByUserId.specified=true", "createdByUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId is greater than or equal to
        defaultImageConversionBatchFiltering(
            "createdByUserId.greaterThanOrEqual=" + DEFAULT_CREATED_BY_USER_ID,
            "createdByUserId.greaterThanOrEqual=" + UPDATED_CREATED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId is less than or equal to
        defaultImageConversionBatchFiltering(
            "createdByUserId.lessThanOrEqual=" + DEFAULT_CREATED_BY_USER_ID,
            "createdByUserId.lessThanOrEqual=" + SMALLER_CREATED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId is less than
        defaultImageConversionBatchFiltering(
            "createdByUserId.lessThan=" + UPDATED_CREATED_BY_USER_ID,
            "createdByUserId.lessThan=" + DEFAULT_CREATED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionBatchesByCreatedByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        // Get all the imageConversionBatchList where createdByUserId is greater than
        defaultImageConversionBatchFiltering(
            "createdByUserId.greaterThan=" + SMALLER_CREATED_BY_USER_ID,
            "createdByUserId.greaterThan=" + DEFAULT_CREATED_BY_USER_ID
        );
    }

    private void defaultImageConversionBatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageConversionBatchShouldBeFound(shouldBeFound);
        defaultImageConversionBatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageConversionBatchShouldBeFound(String filter) throws Exception {
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].completedConversions").value(hasItem(DEFAULT_COMPLETED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].totalProcessingDuration").value(hasItem(DEFAULT_TOTAL_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].createdByUserId").value(hasItem(DEFAULT_CREATED_BY_USER_ID.intValue())));

        // Check, that the count call also returns 1
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageConversionBatchShouldNotBeFound(String filter) throws Exception {
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageConversionBatch() throws Exception {
        // Get the imageConversionBatch
        restImageConversionBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageConversionBatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageConversionBatchSearchRepository.save(imageConversionBatch);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());

        // Update the imageConversionBatch
        ImageConversionBatch updatedImageConversionBatch = imageConversionBatchRepository
            .findById(imageConversionBatch.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedImageConversionBatch are not directly saved in db
        em.detach(updatedImageConversionBatch);
        updatedImageConversionBatch
            .batchName(UPDATED_BATCH_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .completedConversions(UPDATED_COMPLETED_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .totalProcessingDuration(UPDATED_TOTAL_PROCESSING_DURATION)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID);
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(updatedImageConversionBatch);

        restImageConversionBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageConversionBatchToMatchAllProperties(updatedImageConversionBatch);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImageConversionBatch> imageConversionBatchSearchList = Streamable.of(
                    imageConversionBatchSearchRepository.findAll()
                ).toList();
                ImageConversionBatch testImageConversionBatchSearch = imageConversionBatchSearchList.get(searchDatabaseSizeAfter - 1);

                assertImageConversionBatchAllPropertiesEquals(testImageConversionBatchSearch, updatedImageConversionBatch);
            });
    }

    @Test
    @Transactional
    void putNonExistingImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionBatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImageConversionBatchWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionBatch using partial update
        ImageConversionBatch partialUpdatedImageConversionBatch = new ImageConversionBatch();
        partialUpdatedImageConversionBatch.setId(imageConversionBatch.getId());

        partialUpdatedImageConversionBatch
            .batchName(UPDATED_BATCH_NAME)
            .createdAt(UPDATED_CREATED_AT)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .totalProcessingDuration(UPDATED_TOTAL_PROCESSING_DURATION)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID);

        restImageConversionBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionBatch))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionBatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageConversionBatch, imageConversionBatch),
            getPersistedImageConversionBatch(imageConversionBatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageConversionBatchWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionBatch using partial update
        ImageConversionBatch partialUpdatedImageConversionBatch = new ImageConversionBatch();
        partialUpdatedImageConversionBatch.setId(imageConversionBatch.getId());

        partialUpdatedImageConversionBatch
            .batchName(UPDATED_BATCH_NAME)
            .description(UPDATED_DESCRIPTION)
            .createdAt(UPDATED_CREATED_AT)
            .status(UPDATED_STATUS)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .completedConversions(UPDATED_COMPLETED_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .totalProcessingDuration(UPDATED_TOTAL_PROCESSING_DURATION)
            .createdByUserId(UPDATED_CREATED_BY_USER_ID);

        restImageConversionBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionBatch))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionBatchUpdatableFieldsEquals(
            partialUpdatedImageConversionBatch,
            getPersistedImageConversionBatch(partialUpdatedImageConversionBatch)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageConversionBatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageConversionBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        imageConversionBatch.setId(longCount.incrementAndGet());

        // Create the ImageConversionBatch
        ImageConversionBatchDTO imageConversionBatchDTO = imageConversionBatchMapper.toDto(imageConversionBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionBatchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageConversionBatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImageConversionBatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);
        imageConversionBatchRepository.save(imageConversionBatch);
        imageConversionBatchSearchRepository.save(imageConversionBatch);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imageConversionBatch
        restImageConversionBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageConversionBatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionBatchSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImageConversionBatch() throws Exception {
        // Initialize the database
        insertedImageConversionBatch = imageConversionBatchRepository.saveAndFlush(imageConversionBatch);
        imageConversionBatchSearchRepository.save(imageConversionBatch);

        // Search the imageConversionBatch
        restImageConversionBatchMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageConversionBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].batchName").value(hasItem(DEFAULT_BATCH_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].completedConversions").value(hasItem(DEFAULT_COMPLETED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].totalProcessingDuration").value(hasItem(DEFAULT_TOTAL_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].createdByUserId").value(hasItem(DEFAULT_CREATED_BY_USER_ID.intValue())));
    }

    protected long getRepositoryCount() {
        return imageConversionBatchRepository.count();
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

    protected ImageConversionBatch getPersistedImageConversionBatch(ImageConversionBatch imageConversionBatch) {
        return imageConversionBatchRepository.findById(imageConversionBatch.getId()).orElseThrow();
    }

    protected void assertPersistedImageConversionBatchToMatchAllProperties(ImageConversionBatch expectedImageConversionBatch) {
        assertImageConversionBatchAllPropertiesEquals(
            expectedImageConversionBatch,
            getPersistedImageConversionBatch(expectedImageConversionBatch)
        );
    }

    protected void assertPersistedImageConversionBatchToMatchUpdatableProperties(ImageConversionBatch expectedImageConversionBatch) {
        assertImageConversionBatchAllUpdatablePropertiesEquals(
            expectedImageConversionBatch,
            getPersistedImageConversionBatch(expectedImageConversionBatch)
        );
    }
}
