package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistoryAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.pdftoimage.IntegrationTest;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionHistoryRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionHistorySearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionHistoryDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionHistoryMapper;
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
 * Integration tests for the {@link ImageConversionHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageConversionHistoryResourceIT {

    private static final Long DEFAULT_ORIGINAL_REQUEST_ID = 1L;
    private static final Long UPDATED_ORIGINAL_REQUEST_ID = 2L;
    private static final Long SMALLER_ORIGINAL_REQUEST_ID = 1L - 1L;

    private static final Instant DEFAULT_ARCHIVED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ARCHIVED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CONVERSION_DATA = "AAAAAAAAAA";
    private static final String UPDATED_CONVERSION_DATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_IMAGES_COUNT = 0;
    private static final Integer UPDATED_IMAGES_COUNT = 1;
    private static final Integer SMALLER_IMAGES_COUNT = 0 - 1;

    private static final Long DEFAULT_TOTAL_SIZE = 0L;
    private static final Long UPDATED_TOTAL_SIZE = 1L;
    private static final Long SMALLER_TOTAL_SIZE = 0L - 1L;

    private static final ConversionStatus DEFAULT_FINAL_STATUS = ConversionStatus.PENDING;
    private static final ConversionStatus UPDATED_FINAL_STATUS = ConversionStatus.PROCESSING;

    private static final Long DEFAULT_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_PROCESSING_DURATION = 1L - 1L;

    private static final String ENTITY_API_URL = "/api/image-conversion-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-conversion-histories/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageConversionHistoryRepository imageConversionHistoryRepository;

    @Autowired
    private ImageConversionHistoryMapper imageConversionHistoryMapper;

    @Autowired
    private ImageConversionHistorySearchRepository imageConversionHistorySearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageConversionHistoryMockMvc;

    private ImageConversionHistory imageConversionHistory;

    private ImageConversionHistory insertedImageConversionHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionHistory createEntity() {
        return new ImageConversionHistory()
            .originalRequestId(DEFAULT_ORIGINAL_REQUEST_ID)
            .archivedAt(DEFAULT_ARCHIVED_AT)
            .conversionData(DEFAULT_CONVERSION_DATA)
            .imagesCount(DEFAULT_IMAGES_COUNT)
            .totalSize(DEFAULT_TOTAL_SIZE)
            .finalStatus(DEFAULT_FINAL_STATUS)
            .processingDuration(DEFAULT_PROCESSING_DURATION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionHistory createUpdatedEntity() {
        return new ImageConversionHistory()
            .originalRequestId(UPDATED_ORIGINAL_REQUEST_ID)
            .archivedAt(UPDATED_ARCHIVED_AT)
            .conversionData(UPDATED_CONVERSION_DATA)
            .imagesCount(UPDATED_IMAGES_COUNT)
            .totalSize(UPDATED_TOTAL_SIZE)
            .finalStatus(UPDATED_FINAL_STATUS)
            .processingDuration(UPDATED_PROCESSING_DURATION);
    }

    @BeforeEach
    void initTest() {
        imageConversionHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImageConversionHistory != null) {
            imageConversionHistoryRepository.delete(insertedImageConversionHistory);
            imageConversionHistorySearchRepository.delete(insertedImageConversionHistory);
            insertedImageConversionHistory = null;
        }
    }

    @Test
    @Transactional
    void createImageConversionHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);
        var returnedImageConversionHistoryDTO = om.readValue(
            restImageConversionHistoryMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageConversionHistoryDTO.class
        );

        // Validate the ImageConversionHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageConversionHistory = imageConversionHistoryMapper.toEntity(returnedImageConversionHistoryDTO);
        assertImageConversionHistoryUpdatableFieldsEquals(
            returnedImageConversionHistory,
            getPersistedImageConversionHistory(returnedImageConversionHistory)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImageConversionHistory = returnedImageConversionHistory;
    }

    @Test
    @Transactional
    void createImageConversionHistoryWithExistingId() throws Exception {
        // Create the ImageConversionHistory with an existing ID
        imageConversionHistory.setId(1L);
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageConversionHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkOriginalRequestIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        // set the field null
        imageConversionHistory.setOriginalRequestId(null);

        // Create the ImageConversionHistory, which fails.
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        restImageConversionHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkArchivedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        // set the field null
        imageConversionHistory.setArchivedAt(null);

        // Create the ImageConversionHistory, which fails.
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        restImageConversionHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFinalStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        // set the field null
        imageConversionHistory.setFinalStatus(null);

        // Create the ImageConversionHistory, which fails.
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        restImageConversionHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImageConversionHistories() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRequestId").value(hasItem(DEFAULT_ORIGINAL_REQUEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].archivedAt").value(hasItem(DEFAULT_ARCHIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].conversionData").value(hasItem(DEFAULT_CONVERSION_DATA)))
            .andExpect(jsonPath("$.[*].imagesCount").value(hasItem(DEFAULT_IMAGES_COUNT)))
            .andExpect(jsonPath("$.[*].totalSize").value(hasItem(DEFAULT_TOTAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())));
    }

    @Test
    @Transactional
    void getImageConversionHistory() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get the imageConversionHistory
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, imageConversionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageConversionHistory.getId().intValue()))
            .andExpect(jsonPath("$.originalRequestId").value(DEFAULT_ORIGINAL_REQUEST_ID.intValue()))
            .andExpect(jsonPath("$.archivedAt").value(DEFAULT_ARCHIVED_AT.toString()))
            .andExpect(jsonPath("$.conversionData").value(DEFAULT_CONVERSION_DATA))
            .andExpect(jsonPath("$.imagesCount").value(DEFAULT_IMAGES_COUNT))
            .andExpect(jsonPath("$.totalSize").value(DEFAULT_TOTAL_SIZE.intValue()))
            .andExpect(jsonPath("$.finalStatus").value(DEFAULT_FINAL_STATUS.toString()))
            .andExpect(jsonPath("$.processingDuration").value(DEFAULT_PROCESSING_DURATION.intValue()));
    }

    @Test
    @Transactional
    void getImageConversionHistoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        Long id = imageConversionHistory.getId();

        defaultImageConversionHistoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageConversionHistoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageConversionHistoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId equals to
        defaultImageConversionHistoryFiltering(
            "originalRequestId.equals=" + DEFAULT_ORIGINAL_REQUEST_ID,
            "originalRequestId.equals=" + UPDATED_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId in
        defaultImageConversionHistoryFiltering(
            "originalRequestId.in=" + DEFAULT_ORIGINAL_REQUEST_ID + "," + UPDATED_ORIGINAL_REQUEST_ID,
            "originalRequestId.in=" + UPDATED_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId is not null
        defaultImageConversionHistoryFiltering("originalRequestId.specified=true", "originalRequestId.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId is greater than or equal to
        defaultImageConversionHistoryFiltering(
            "originalRequestId.greaterThanOrEqual=" + DEFAULT_ORIGINAL_REQUEST_ID,
            "originalRequestId.greaterThanOrEqual=" + UPDATED_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId is less than or equal to
        defaultImageConversionHistoryFiltering(
            "originalRequestId.lessThanOrEqual=" + DEFAULT_ORIGINAL_REQUEST_ID,
            "originalRequestId.lessThanOrEqual=" + SMALLER_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId is less than
        defaultImageConversionHistoryFiltering(
            "originalRequestId.lessThan=" + UPDATED_ORIGINAL_REQUEST_ID,
            "originalRequestId.lessThan=" + DEFAULT_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByOriginalRequestIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where originalRequestId is greater than
        defaultImageConversionHistoryFiltering(
            "originalRequestId.greaterThan=" + SMALLER_ORIGINAL_REQUEST_ID,
            "originalRequestId.greaterThan=" + DEFAULT_ORIGINAL_REQUEST_ID
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByArchivedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where archivedAt equals to
        defaultImageConversionHistoryFiltering("archivedAt.equals=" + DEFAULT_ARCHIVED_AT, "archivedAt.equals=" + UPDATED_ARCHIVED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByArchivedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where archivedAt in
        defaultImageConversionHistoryFiltering(
            "archivedAt.in=" + DEFAULT_ARCHIVED_AT + "," + UPDATED_ARCHIVED_AT,
            "archivedAt.in=" + UPDATED_ARCHIVED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByArchivedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where archivedAt is not null
        defaultImageConversionHistoryFiltering("archivedAt.specified=true", "archivedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount equals to
        defaultImageConversionHistoryFiltering("imagesCount.equals=" + DEFAULT_IMAGES_COUNT, "imagesCount.equals=" + UPDATED_IMAGES_COUNT);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount in
        defaultImageConversionHistoryFiltering(
            "imagesCount.in=" + DEFAULT_IMAGES_COUNT + "," + UPDATED_IMAGES_COUNT,
            "imagesCount.in=" + UPDATED_IMAGES_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount is not null
        defaultImageConversionHistoryFiltering("imagesCount.specified=true", "imagesCount.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount is greater than or equal to
        defaultImageConversionHistoryFiltering(
            "imagesCount.greaterThanOrEqual=" + DEFAULT_IMAGES_COUNT,
            "imagesCount.greaterThanOrEqual=" + UPDATED_IMAGES_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount is less than or equal to
        defaultImageConversionHistoryFiltering(
            "imagesCount.lessThanOrEqual=" + DEFAULT_IMAGES_COUNT,
            "imagesCount.lessThanOrEqual=" + SMALLER_IMAGES_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount is less than
        defaultImageConversionHistoryFiltering(
            "imagesCount.lessThan=" + UPDATED_IMAGES_COUNT,
            "imagesCount.lessThan=" + DEFAULT_IMAGES_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByImagesCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where imagesCount is greater than
        defaultImageConversionHistoryFiltering(
            "imagesCount.greaterThan=" + SMALLER_IMAGES_COUNT,
            "imagesCount.greaterThan=" + DEFAULT_IMAGES_COUNT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize equals to
        defaultImageConversionHistoryFiltering("totalSize.equals=" + DEFAULT_TOTAL_SIZE, "totalSize.equals=" + UPDATED_TOTAL_SIZE);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize in
        defaultImageConversionHistoryFiltering(
            "totalSize.in=" + DEFAULT_TOTAL_SIZE + "," + UPDATED_TOTAL_SIZE,
            "totalSize.in=" + UPDATED_TOTAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize is not null
        defaultImageConversionHistoryFiltering("totalSize.specified=true", "totalSize.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize is greater than or equal to
        defaultImageConversionHistoryFiltering(
            "totalSize.greaterThanOrEqual=" + DEFAULT_TOTAL_SIZE,
            "totalSize.greaterThanOrEqual=" + UPDATED_TOTAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize is less than or equal to
        defaultImageConversionHistoryFiltering(
            "totalSize.lessThanOrEqual=" + DEFAULT_TOTAL_SIZE,
            "totalSize.lessThanOrEqual=" + SMALLER_TOTAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize is less than
        defaultImageConversionHistoryFiltering("totalSize.lessThan=" + UPDATED_TOTAL_SIZE, "totalSize.lessThan=" + DEFAULT_TOTAL_SIZE);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByTotalSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where totalSize is greater than
        defaultImageConversionHistoryFiltering(
            "totalSize.greaterThan=" + SMALLER_TOTAL_SIZE,
            "totalSize.greaterThan=" + DEFAULT_TOTAL_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByFinalStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where finalStatus equals to
        defaultImageConversionHistoryFiltering("finalStatus.equals=" + DEFAULT_FINAL_STATUS, "finalStatus.equals=" + UPDATED_FINAL_STATUS);
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByFinalStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where finalStatus in
        defaultImageConversionHistoryFiltering(
            "finalStatus.in=" + DEFAULT_FINAL_STATUS + "," + UPDATED_FINAL_STATUS,
            "finalStatus.in=" + UPDATED_FINAL_STATUS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByFinalStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where finalStatus is not null
        defaultImageConversionHistoryFiltering("finalStatus.specified=true", "finalStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration equals to
        defaultImageConversionHistoryFiltering(
            "processingDuration.equals=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.equals=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration in
        defaultImageConversionHistoryFiltering(
            "processingDuration.in=" + DEFAULT_PROCESSING_DURATION + "," + UPDATED_PROCESSING_DURATION,
            "processingDuration.in=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration is not null
        defaultImageConversionHistoryFiltering("processingDuration.specified=true", "processingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration is greater than or equal to
        defaultImageConversionHistoryFiltering(
            "processingDuration.greaterThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.greaterThanOrEqual=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration is less than or equal to
        defaultImageConversionHistoryFiltering(
            "processingDuration.lessThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.lessThanOrEqual=" + SMALLER_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration is less than
        defaultImageConversionHistoryFiltering(
            "processingDuration.lessThan=" + UPDATED_PROCESSING_DURATION,
            "processingDuration.lessThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionHistoriesByProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        // Get all the imageConversionHistoryList where processingDuration is greater than
        defaultImageConversionHistoryFiltering(
            "processingDuration.greaterThan=" + SMALLER_PROCESSING_DURATION,
            "processingDuration.greaterThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    private void defaultImageConversionHistoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageConversionHistoryShouldBeFound(shouldBeFound);
        defaultImageConversionHistoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageConversionHistoryShouldBeFound(String filter) throws Exception {
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRequestId").value(hasItem(DEFAULT_ORIGINAL_REQUEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].archivedAt").value(hasItem(DEFAULT_ARCHIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].conversionData").value(hasItem(DEFAULT_CONVERSION_DATA)))
            .andExpect(jsonPath("$.[*].imagesCount").value(hasItem(DEFAULT_IMAGES_COUNT)))
            .andExpect(jsonPath("$.[*].totalSize").value(hasItem(DEFAULT_TOTAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())));

        // Check, that the count call also returns 1
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageConversionHistoryShouldNotBeFound(String filter) throws Exception {
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageConversionHistory() throws Exception {
        // Get the imageConversionHistory
        restImageConversionHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageConversionHistory() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageConversionHistorySearchRepository.save(imageConversionHistory);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());

        // Update the imageConversionHistory
        ImageConversionHistory updatedImageConversionHistory = imageConversionHistoryRepository
            .findById(imageConversionHistory.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedImageConversionHistory are not directly saved in db
        em.detach(updatedImageConversionHistory);
        updatedImageConversionHistory
            .originalRequestId(UPDATED_ORIGINAL_REQUEST_ID)
            .archivedAt(UPDATED_ARCHIVED_AT)
            .conversionData(UPDATED_CONVERSION_DATA)
            .imagesCount(UPDATED_IMAGES_COUNT)
            .totalSize(UPDATED_TOTAL_SIZE)
            .finalStatus(UPDATED_FINAL_STATUS)
            .processingDuration(UPDATED_PROCESSING_DURATION);
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(updatedImageConversionHistory);

        restImageConversionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageConversionHistoryToMatchAllProperties(updatedImageConversionHistory);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImageConversionHistory> imageConversionHistorySearchList = Streamable.of(
                    imageConversionHistorySearchRepository.findAll()
                ).toList();
                ImageConversionHistory testImageConversionHistorySearch = imageConversionHistorySearchList.get(searchDatabaseSizeAfter - 1);

                assertImageConversionHistoryAllPropertiesEquals(testImageConversionHistorySearch, updatedImageConversionHistory);
            });
    }

    @Test
    @Transactional
    void putNonExistingImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImageConversionHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionHistory using partial update
        ImageConversionHistory partialUpdatedImageConversionHistory = new ImageConversionHistory();
        partialUpdatedImageConversionHistory.setId(imageConversionHistory.getId());

        partialUpdatedImageConversionHistory
            .originalRequestId(UPDATED_ORIGINAL_REQUEST_ID)
            .archivedAt(UPDATED_ARCHIVED_AT)
            .imagesCount(UPDATED_IMAGES_COUNT)
            .finalStatus(UPDATED_FINAL_STATUS)
            .processingDuration(UPDATED_PROCESSING_DURATION);

        restImageConversionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionHistory))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageConversionHistory, imageConversionHistory),
            getPersistedImageConversionHistory(imageConversionHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageConversionHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionHistory using partial update
        ImageConversionHistory partialUpdatedImageConversionHistory = new ImageConversionHistory();
        partialUpdatedImageConversionHistory.setId(imageConversionHistory.getId());

        partialUpdatedImageConversionHistory
            .originalRequestId(UPDATED_ORIGINAL_REQUEST_ID)
            .archivedAt(UPDATED_ARCHIVED_AT)
            .conversionData(UPDATED_CONVERSION_DATA)
            .imagesCount(UPDATED_IMAGES_COUNT)
            .totalSize(UPDATED_TOTAL_SIZE)
            .finalStatus(UPDATED_FINAL_STATUS)
            .processingDuration(UPDATED_PROCESSING_DURATION);

        restImageConversionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionHistory))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionHistoryUpdatableFieldsEquals(
            partialUpdatedImageConversionHistory,
            getPersistedImageConversionHistory(partialUpdatedImageConversionHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageConversionHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageConversionHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        imageConversionHistory.setId(longCount.incrementAndGet());

        // Create the ImageConversionHistory
        ImageConversionHistoryDTO imageConversionHistoryDTO = imageConversionHistoryMapper.toDto(imageConversionHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageConversionHistoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImageConversionHistory() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);
        imageConversionHistoryRepository.save(imageConversionHistory);
        imageConversionHistorySearchRepository.save(imageConversionHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imageConversionHistory
        restImageConversionHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageConversionHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionHistorySearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImageConversionHistory() throws Exception {
        // Initialize the database
        insertedImageConversionHistory = imageConversionHistoryRepository.saveAndFlush(imageConversionHistory);
        imageConversionHistorySearchRepository.save(imageConversionHistory);

        // Search the imageConversionHistory
        restImageConversionHistoryMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageConversionHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].originalRequestId").value(hasItem(DEFAULT_ORIGINAL_REQUEST_ID.intValue())))
            .andExpect(jsonPath("$.[*].archivedAt").value(hasItem(DEFAULT_ARCHIVED_AT.toString())))
            .andExpect(jsonPath("$.[*].conversionData").value(hasItem(DEFAULT_CONVERSION_DATA.toString())))
            .andExpect(jsonPath("$.[*].imagesCount").value(hasItem(DEFAULT_IMAGES_COUNT)))
            .andExpect(jsonPath("$.[*].totalSize").value(hasItem(DEFAULT_TOTAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].finalStatus").value(hasItem(DEFAULT_FINAL_STATUS.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())));
    }

    protected long getRepositoryCount() {
        return imageConversionHistoryRepository.count();
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

    protected ImageConversionHistory getPersistedImageConversionHistory(ImageConversionHistory imageConversionHistory) {
        return imageConversionHistoryRepository.findById(imageConversionHistory.getId()).orElseThrow();
    }

    protected void assertPersistedImageConversionHistoryToMatchAllProperties(ImageConversionHistory expectedImageConversionHistory) {
        assertImageConversionHistoryAllPropertiesEquals(
            expectedImageConversionHistory,
            getPersistedImageConversionHistory(expectedImageConversionHistory)
        );
    }

    protected void assertPersistedImageConversionHistoryToMatchUpdatableProperties(ImageConversionHistory expectedImageConversionHistory) {
        assertImageConversionHistoryAllUpdatablePropertiesEquals(
            expectedImageConversionHistory,
            getPersistedImageConversionHistory(expectedImageConversionHistory)
        );
    }
}
