package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatisticsAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.pdftoimage.IntegrationTest;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionStatistics;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionStatisticsRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionStatisticsSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionStatisticsDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionStatisticsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link ImageConversionStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageConversionStatisticsResourceIT {

    private static final LocalDate DEFAULT_STATISTICS_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_STATISTICS_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_STATISTICS_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_TOTAL_CONVERSIONS = 0;
    private static final Integer UPDATED_TOTAL_CONVERSIONS = 1;
    private static final Integer SMALLER_TOTAL_CONVERSIONS = 0 - 1;

    private static final Integer DEFAULT_SUCCESSFUL_CONVERSIONS = 0;
    private static final Integer UPDATED_SUCCESSFUL_CONVERSIONS = 1;
    private static final Integer SMALLER_SUCCESSFUL_CONVERSIONS = 0 - 1;

    private static final Integer DEFAULT_FAILED_CONVERSIONS = 0;
    private static final Integer UPDATED_FAILED_CONVERSIONS = 1;
    private static final Integer SMALLER_FAILED_CONVERSIONS = 0 - 1;

    private static final Integer DEFAULT_TOTAL_PAGES_CONVERTED = 0;
    private static final Integer UPDATED_TOTAL_PAGES_CONVERTED = 1;
    private static final Integer SMALLER_TOTAL_PAGES_CONVERTED = 0 - 1;

    private static final Integer DEFAULT_TOTAL_IMAGES_GENERATED = 0;
    private static final Integer UPDATED_TOTAL_IMAGES_GENERATED = 1;
    private static final Integer SMALLER_TOTAL_IMAGES_GENERATED = 0 - 1;

    private static final Long DEFAULT_TOTAL_IMAGES_SIZE = 0L;
    private static final Long UPDATED_TOTAL_IMAGES_SIZE = 1L;
    private static final Long SMALLER_TOTAL_IMAGES_SIZE = 0L - 1L;

    private static final Long DEFAULT_AVERAGE_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_AVERAGE_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_AVERAGE_PROCESSING_DURATION = 1L - 1L;

    private static final Long DEFAULT_MAX_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_MAX_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_MAX_PROCESSING_DURATION = 1L - 1L;

    private static final Long DEFAULT_MIN_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_MIN_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_MIN_PROCESSING_DURATION = 1L - 1L;

    private static final Instant DEFAULT_CALCULATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CALCULATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/image-conversion-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-conversion-statistics/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageConversionStatisticsRepository imageConversionStatisticsRepository;

    @Autowired
    private ImageConversionStatisticsMapper imageConversionStatisticsMapper;

    @Autowired
    private ImageConversionStatisticsSearchRepository imageConversionStatisticsSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageConversionStatisticsMockMvc;

    private ImageConversionStatistics imageConversionStatistics;

    private ImageConversionStatistics insertedImageConversionStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionStatistics createEntity() {
        return new ImageConversionStatistics()
            .statisticsDate(DEFAULT_STATISTICS_DATE)
            .totalConversions(DEFAULT_TOTAL_CONVERSIONS)
            .successfulConversions(DEFAULT_SUCCESSFUL_CONVERSIONS)
            .failedConversions(DEFAULT_FAILED_CONVERSIONS)
            .totalPagesConverted(DEFAULT_TOTAL_PAGES_CONVERTED)
            .totalImagesGenerated(DEFAULT_TOTAL_IMAGES_GENERATED)
            .totalImagesSize(DEFAULT_TOTAL_IMAGES_SIZE)
            .averageProcessingDuration(DEFAULT_AVERAGE_PROCESSING_DURATION)
            .maxProcessingDuration(DEFAULT_MAX_PROCESSING_DURATION)
            .minProcessingDuration(DEFAULT_MIN_PROCESSING_DURATION)
            .calculatedAt(DEFAULT_CALCULATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionStatistics createUpdatedEntity() {
        return new ImageConversionStatistics()
            .statisticsDate(UPDATED_STATISTICS_DATE)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .successfulConversions(UPDATED_SUCCESSFUL_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .totalPagesConverted(UPDATED_TOTAL_PAGES_CONVERTED)
            .totalImagesGenerated(UPDATED_TOTAL_IMAGES_GENERATED)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .averageProcessingDuration(UPDATED_AVERAGE_PROCESSING_DURATION)
            .maxProcessingDuration(UPDATED_MAX_PROCESSING_DURATION)
            .minProcessingDuration(UPDATED_MIN_PROCESSING_DURATION)
            .calculatedAt(UPDATED_CALCULATED_AT);
    }

    @BeforeEach
    void initTest() {
        imageConversionStatistics = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImageConversionStatistics != null) {
            imageConversionStatisticsRepository.delete(insertedImageConversionStatistics);
            imageConversionStatisticsSearchRepository.delete(insertedImageConversionStatistics);
            insertedImageConversionStatistics = null;
        }
    }

    @Test
    @Transactional
    void createImageConversionStatistics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);
        var returnedImageConversionStatisticsDTO = om.readValue(
            restImageConversionStatisticsMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionStatisticsDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageConversionStatisticsDTO.class
        );

        // Validate the ImageConversionStatistics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageConversionStatistics = imageConversionStatisticsMapper.toEntity(returnedImageConversionStatisticsDTO);
        assertImageConversionStatisticsUpdatableFieldsEquals(
            returnedImageConversionStatistics,
            getPersistedImageConversionStatistics(returnedImageConversionStatistics)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImageConversionStatistics = returnedImageConversionStatistics;
    }

    @Test
    @Transactional
    void createImageConversionStatisticsWithExistingId() throws Exception {
        // Create the ImageConversionStatistics with an existing ID
        imageConversionStatistics.setId(1L);
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageConversionStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatisticsDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        // set the field null
        imageConversionStatistics.setStatisticsDate(null);

        // Create the ImageConversionStatistics, which fails.
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        restImageConversionStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCalculatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        // set the field null
        imageConversionStatistics.setCalculatedAt(null);

        // Create the ImageConversionStatistics, which fails.
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        restImageConversionStatisticsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImageConversionStatistics() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].statisticsDate").value(hasItem(DEFAULT_STATISTICS_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].successfulConversions").value(hasItem(DEFAULT_SUCCESSFUL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].totalPagesConverted").value(hasItem(DEFAULT_TOTAL_PAGES_CONVERTED)))
            .andExpect(jsonPath("$.[*].totalImagesGenerated").value(hasItem(DEFAULT_TOTAL_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].averageProcessingDuration").value(hasItem(DEFAULT_AVERAGE_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].maxProcessingDuration").value(hasItem(DEFAULT_MAX_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].minProcessingDuration").value(hasItem(DEFAULT_MIN_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].calculatedAt").value(hasItem(DEFAULT_CALCULATED_AT.toString())));
    }

    @Test
    @Transactional
    void getImageConversionStatistics() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get the imageConversionStatistics
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, imageConversionStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageConversionStatistics.getId().intValue()))
            .andExpect(jsonPath("$.statisticsDate").value(DEFAULT_STATISTICS_DATE.toString()))
            .andExpect(jsonPath("$.totalConversions").value(DEFAULT_TOTAL_CONVERSIONS))
            .andExpect(jsonPath("$.successfulConversions").value(DEFAULT_SUCCESSFUL_CONVERSIONS))
            .andExpect(jsonPath("$.failedConversions").value(DEFAULT_FAILED_CONVERSIONS))
            .andExpect(jsonPath("$.totalPagesConverted").value(DEFAULT_TOTAL_PAGES_CONVERTED))
            .andExpect(jsonPath("$.totalImagesGenerated").value(DEFAULT_TOTAL_IMAGES_GENERATED))
            .andExpect(jsonPath("$.totalImagesSize").value(DEFAULT_TOTAL_IMAGES_SIZE.intValue()))
            .andExpect(jsonPath("$.averageProcessingDuration").value(DEFAULT_AVERAGE_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.maxProcessingDuration").value(DEFAULT_MAX_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.minProcessingDuration").value(DEFAULT_MIN_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.calculatedAt").value(DEFAULT_CALCULATED_AT.toString()));
    }

    @Test
    @Transactional
    void getImageConversionStatisticsByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        Long id = imageConversionStatistics.getId();

        defaultImageConversionStatisticsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageConversionStatisticsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageConversionStatisticsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate equals to
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.equals=" + DEFAULT_STATISTICS_DATE,
            "statisticsDate.equals=" + UPDATED_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate in
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.in=" + DEFAULT_STATISTICS_DATE + "," + UPDATED_STATISTICS_DATE,
            "statisticsDate.in=" + UPDATED_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate is not null
        defaultImageConversionStatisticsFiltering("statisticsDate.specified=true", "statisticsDate.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.greaterThanOrEqual=" + DEFAULT_STATISTICS_DATE,
            "statisticsDate.greaterThanOrEqual=" + UPDATED_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.lessThanOrEqual=" + DEFAULT_STATISTICS_DATE,
            "statisticsDate.lessThanOrEqual=" + SMALLER_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate is less than
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.lessThan=" + UPDATED_STATISTICS_DATE,
            "statisticsDate.lessThan=" + DEFAULT_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByStatisticsDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where statisticsDate is greater than
        defaultImageConversionStatisticsFiltering(
            "statisticsDate.greaterThan=" + SMALLER_STATISTICS_DATE,
            "statisticsDate.greaterThan=" + DEFAULT_STATISTICS_DATE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions equals to
        defaultImageConversionStatisticsFiltering(
            "totalConversions.equals=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.equals=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions in
        defaultImageConversionStatisticsFiltering(
            "totalConversions.in=" + DEFAULT_TOTAL_CONVERSIONS + "," + UPDATED_TOTAL_CONVERSIONS,
            "totalConversions.in=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions is not null
        defaultImageConversionStatisticsFiltering("totalConversions.specified=true", "totalConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalConversions.greaterThanOrEqual=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.greaterThanOrEqual=" + UPDATED_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalConversions.lessThanOrEqual=" + DEFAULT_TOTAL_CONVERSIONS,
            "totalConversions.lessThanOrEqual=" + SMALLER_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions is less than
        defaultImageConversionStatisticsFiltering(
            "totalConversions.lessThan=" + UPDATED_TOTAL_CONVERSIONS,
            "totalConversions.lessThan=" + DEFAULT_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalConversions is greater than
        defaultImageConversionStatisticsFiltering(
            "totalConversions.greaterThan=" + SMALLER_TOTAL_CONVERSIONS,
            "totalConversions.greaterThan=" + DEFAULT_TOTAL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions equals to
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.equals=" + DEFAULT_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.equals=" + UPDATED_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions in
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.in=" + DEFAULT_SUCCESSFUL_CONVERSIONS + "," + UPDATED_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.in=" + UPDATED_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions is not null
        defaultImageConversionStatisticsFiltering("successfulConversions.specified=true", "successfulConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.greaterThanOrEqual=" + DEFAULT_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.greaterThanOrEqual=" + UPDATED_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.lessThanOrEqual=" + DEFAULT_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.lessThanOrEqual=" + SMALLER_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions is less than
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.lessThan=" + UPDATED_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.lessThan=" + DEFAULT_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsBySuccessfulConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where successfulConversions is greater than
        defaultImageConversionStatisticsFiltering(
            "successfulConversions.greaterThan=" + SMALLER_SUCCESSFUL_CONVERSIONS,
            "successfulConversions.greaterThan=" + DEFAULT_SUCCESSFUL_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions equals to
        defaultImageConversionStatisticsFiltering(
            "failedConversions.equals=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.equals=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions in
        defaultImageConversionStatisticsFiltering(
            "failedConversions.in=" + DEFAULT_FAILED_CONVERSIONS + "," + UPDATED_FAILED_CONVERSIONS,
            "failedConversions.in=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions is not null
        defaultImageConversionStatisticsFiltering("failedConversions.specified=true", "failedConversions.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "failedConversions.greaterThanOrEqual=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.greaterThanOrEqual=" + UPDATED_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "failedConversions.lessThanOrEqual=" + DEFAULT_FAILED_CONVERSIONS,
            "failedConversions.lessThanOrEqual=" + SMALLER_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions is less than
        defaultImageConversionStatisticsFiltering(
            "failedConversions.lessThan=" + UPDATED_FAILED_CONVERSIONS,
            "failedConversions.lessThan=" + DEFAULT_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByFailedConversionsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where failedConversions is greater than
        defaultImageConversionStatisticsFiltering(
            "failedConversions.greaterThan=" + SMALLER_FAILED_CONVERSIONS,
            "failedConversions.greaterThan=" + DEFAULT_FAILED_CONVERSIONS
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted equals to
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.equals=" + DEFAULT_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.equals=" + UPDATED_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted in
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.in=" + DEFAULT_TOTAL_PAGES_CONVERTED + "," + UPDATED_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.in=" + UPDATED_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted is not null
        defaultImageConversionStatisticsFiltering("totalPagesConverted.specified=true", "totalPagesConverted.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.greaterThanOrEqual=" + DEFAULT_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.greaterThanOrEqual=" + UPDATED_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.lessThanOrEqual=" + DEFAULT_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.lessThanOrEqual=" + SMALLER_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted is less than
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.lessThan=" + UPDATED_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.lessThan=" + DEFAULT_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalPagesConvertedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalPagesConverted is greater than
        defaultImageConversionStatisticsFiltering(
            "totalPagesConverted.greaterThan=" + SMALLER_TOTAL_PAGES_CONVERTED,
            "totalPagesConverted.greaterThan=" + DEFAULT_TOTAL_PAGES_CONVERTED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated equals to
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.equals=" + DEFAULT_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.equals=" + UPDATED_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated in
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.in=" + DEFAULT_TOTAL_IMAGES_GENERATED + "," + UPDATED_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.in=" + UPDATED_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated is not null
        defaultImageConversionStatisticsFiltering("totalImagesGenerated.specified=true", "totalImagesGenerated.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.greaterThanOrEqual=" + DEFAULT_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.greaterThanOrEqual=" + UPDATED_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.lessThanOrEqual=" + DEFAULT_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.lessThanOrEqual=" + SMALLER_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated is less than
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.lessThan=" + UPDATED_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.lessThan=" + DEFAULT_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesGeneratedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesGenerated is greater than
        defaultImageConversionStatisticsFiltering(
            "totalImagesGenerated.greaterThan=" + SMALLER_TOTAL_IMAGES_GENERATED,
            "totalImagesGenerated.greaterThan=" + DEFAULT_TOTAL_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize equals to
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.equals=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.equals=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize in
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.in=" + DEFAULT_TOTAL_IMAGES_SIZE + "," + UPDATED_TOTAL_IMAGES_SIZE,
            "totalImagesSize.in=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize is not null
        defaultImageConversionStatisticsFiltering("totalImagesSize.specified=true", "totalImagesSize.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.greaterThanOrEqual=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.greaterThanOrEqual=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.lessThanOrEqual=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.lessThanOrEqual=" + SMALLER_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize is less than
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.lessThan=" + UPDATED_TOTAL_IMAGES_SIZE,
            "totalImagesSize.lessThan=" + DEFAULT_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByTotalImagesSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where totalImagesSize is greater than
        defaultImageConversionStatisticsFiltering(
            "totalImagesSize.greaterThan=" + SMALLER_TOTAL_IMAGES_SIZE,
            "totalImagesSize.greaterThan=" + DEFAULT_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration equals to
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.equals=" + DEFAULT_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.equals=" + UPDATED_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration in
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.in=" + DEFAULT_AVERAGE_PROCESSING_DURATION + "," + UPDATED_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.in=" + UPDATED_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration is not null
        defaultImageConversionStatisticsFiltering("averageProcessingDuration.specified=true", "averageProcessingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.greaterThanOrEqual=" + DEFAULT_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.greaterThanOrEqual=" + UPDATED_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.lessThanOrEqual=" + DEFAULT_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.lessThanOrEqual=" + SMALLER_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration is less than
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.lessThan=" + UPDATED_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.lessThan=" + DEFAULT_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByAverageProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where averageProcessingDuration is greater than
        defaultImageConversionStatisticsFiltering(
            "averageProcessingDuration.greaterThan=" + SMALLER_AVERAGE_PROCESSING_DURATION,
            "averageProcessingDuration.greaterThan=" + DEFAULT_AVERAGE_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration equals to
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.equals=" + DEFAULT_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.equals=" + UPDATED_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration in
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.in=" + DEFAULT_MAX_PROCESSING_DURATION + "," + UPDATED_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.in=" + UPDATED_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration is not null
        defaultImageConversionStatisticsFiltering("maxProcessingDuration.specified=true", "maxProcessingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.greaterThanOrEqual=" + DEFAULT_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.greaterThanOrEqual=" + UPDATED_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.lessThanOrEqual=" + DEFAULT_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.lessThanOrEqual=" + SMALLER_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration is less than
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.lessThan=" + UPDATED_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.lessThan=" + DEFAULT_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMaxProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where maxProcessingDuration is greater than
        defaultImageConversionStatisticsFiltering(
            "maxProcessingDuration.greaterThan=" + SMALLER_MAX_PROCESSING_DURATION,
            "maxProcessingDuration.greaterThan=" + DEFAULT_MAX_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration equals to
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.equals=" + DEFAULT_MIN_PROCESSING_DURATION,
            "minProcessingDuration.equals=" + UPDATED_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration in
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.in=" + DEFAULT_MIN_PROCESSING_DURATION + "," + UPDATED_MIN_PROCESSING_DURATION,
            "minProcessingDuration.in=" + UPDATED_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration is not null
        defaultImageConversionStatisticsFiltering("minProcessingDuration.specified=true", "minProcessingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration is greater than or equal to
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.greaterThanOrEqual=" + DEFAULT_MIN_PROCESSING_DURATION,
            "minProcessingDuration.greaterThanOrEqual=" + UPDATED_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration is less than or equal to
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.lessThanOrEqual=" + DEFAULT_MIN_PROCESSING_DURATION,
            "minProcessingDuration.lessThanOrEqual=" + SMALLER_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration is less than
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.lessThan=" + UPDATED_MIN_PROCESSING_DURATION,
            "minProcessingDuration.lessThan=" + DEFAULT_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByMinProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where minProcessingDuration is greater than
        defaultImageConversionStatisticsFiltering(
            "minProcessingDuration.greaterThan=" + SMALLER_MIN_PROCESSING_DURATION,
            "minProcessingDuration.greaterThan=" + DEFAULT_MIN_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByCalculatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where calculatedAt equals to
        defaultImageConversionStatisticsFiltering(
            "calculatedAt.equals=" + DEFAULT_CALCULATED_AT,
            "calculatedAt.equals=" + UPDATED_CALCULATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByCalculatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where calculatedAt in
        defaultImageConversionStatisticsFiltering(
            "calculatedAt.in=" + DEFAULT_CALCULATED_AT + "," + UPDATED_CALCULATED_AT,
            "calculatedAt.in=" + UPDATED_CALCULATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionStatisticsByCalculatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        // Get all the imageConversionStatisticsList where calculatedAt is not null
        defaultImageConversionStatisticsFiltering("calculatedAt.specified=true", "calculatedAt.specified=false");
    }

    private void defaultImageConversionStatisticsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageConversionStatisticsShouldBeFound(shouldBeFound);
        defaultImageConversionStatisticsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageConversionStatisticsShouldBeFound(String filter) throws Exception {
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].statisticsDate").value(hasItem(DEFAULT_STATISTICS_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].successfulConversions").value(hasItem(DEFAULT_SUCCESSFUL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].totalPagesConverted").value(hasItem(DEFAULT_TOTAL_PAGES_CONVERTED)))
            .andExpect(jsonPath("$.[*].totalImagesGenerated").value(hasItem(DEFAULT_TOTAL_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].averageProcessingDuration").value(hasItem(DEFAULT_AVERAGE_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].maxProcessingDuration").value(hasItem(DEFAULT_MAX_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].minProcessingDuration").value(hasItem(DEFAULT_MIN_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].calculatedAt").value(hasItem(DEFAULT_CALCULATED_AT.toString())));

        // Check, that the count call also returns 1
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageConversionStatisticsShouldNotBeFound(String filter) throws Exception {
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageConversionStatistics() throws Exception {
        // Get the imageConversionStatistics
        restImageConversionStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageConversionStatistics() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageConversionStatisticsSearchRepository.save(imageConversionStatistics);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());

        // Update the imageConversionStatistics
        ImageConversionStatistics updatedImageConversionStatistics = imageConversionStatisticsRepository
            .findById(imageConversionStatistics.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedImageConversionStatistics are not directly saved in db
        em.detach(updatedImageConversionStatistics);
        updatedImageConversionStatistics
            .statisticsDate(UPDATED_STATISTICS_DATE)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .successfulConversions(UPDATED_SUCCESSFUL_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .totalPagesConverted(UPDATED_TOTAL_PAGES_CONVERTED)
            .totalImagesGenerated(UPDATED_TOTAL_IMAGES_GENERATED)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .averageProcessingDuration(UPDATED_AVERAGE_PROCESSING_DURATION)
            .maxProcessingDuration(UPDATED_MAX_PROCESSING_DURATION)
            .minProcessingDuration(UPDATED_MIN_PROCESSING_DURATION)
            .calculatedAt(UPDATED_CALCULATED_AT);
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(updatedImageConversionStatistics);

        restImageConversionStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageConversionStatisticsToMatchAllProperties(updatedImageConversionStatistics);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImageConversionStatistics> imageConversionStatisticsSearchList = Streamable.of(
                    imageConversionStatisticsSearchRepository.findAll()
                ).toList();
                ImageConversionStatistics testImageConversionStatisticsSearch = imageConversionStatisticsSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertImageConversionStatisticsAllPropertiesEquals(testImageConversionStatisticsSearch, updatedImageConversionStatistics);
            });
    }

    @Test
    @Transactional
    void putNonExistingImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImageConversionStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionStatistics using partial update
        ImageConversionStatistics partialUpdatedImageConversionStatistics = new ImageConversionStatistics();
        partialUpdatedImageConversionStatistics.setId(imageConversionStatistics.getId());

        partialUpdatedImageConversionStatistics
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .totalPagesConverted(UPDATED_TOTAL_PAGES_CONVERTED)
            .totalImagesGenerated(UPDATED_TOTAL_IMAGES_GENERATED)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .maxProcessingDuration(UPDATED_MAX_PROCESSING_DURATION)
            .minProcessingDuration(UPDATED_MIN_PROCESSING_DURATION);

        restImageConversionStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionStatistics))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionStatisticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageConversionStatistics, imageConversionStatistics),
            getPersistedImageConversionStatistics(imageConversionStatistics)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageConversionStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionStatistics using partial update
        ImageConversionStatistics partialUpdatedImageConversionStatistics = new ImageConversionStatistics();
        partialUpdatedImageConversionStatistics.setId(imageConversionStatistics.getId());

        partialUpdatedImageConversionStatistics
            .statisticsDate(UPDATED_STATISTICS_DATE)
            .totalConversions(UPDATED_TOTAL_CONVERSIONS)
            .successfulConversions(UPDATED_SUCCESSFUL_CONVERSIONS)
            .failedConversions(UPDATED_FAILED_CONVERSIONS)
            .totalPagesConverted(UPDATED_TOTAL_PAGES_CONVERTED)
            .totalImagesGenerated(UPDATED_TOTAL_IMAGES_GENERATED)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .averageProcessingDuration(UPDATED_AVERAGE_PROCESSING_DURATION)
            .maxProcessingDuration(UPDATED_MAX_PROCESSING_DURATION)
            .minProcessingDuration(UPDATED_MIN_PROCESSING_DURATION)
            .calculatedAt(UPDATED_CALCULATED_AT);

        restImageConversionStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionStatistics))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionStatisticsUpdatableFieldsEquals(
            partialUpdatedImageConversionStatistics,
            getPersistedImageConversionStatistics(partialUpdatedImageConversionStatistics)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageConversionStatisticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageConversionStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        imageConversionStatistics.setId(longCount.incrementAndGet());

        // Create the ImageConversionStatistics
        ImageConversionStatisticsDTO imageConversionStatisticsDTO = imageConversionStatisticsMapper.toDto(imageConversionStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionStatisticsDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImageConversionStatistics() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);
        imageConversionStatisticsRepository.save(imageConversionStatistics);
        imageConversionStatisticsSearchRepository.save(imageConversionStatistics);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imageConversionStatistics
        restImageConversionStatisticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageConversionStatistics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImageConversionStatistics() throws Exception {
        // Initialize the database
        insertedImageConversionStatistics = imageConversionStatisticsRepository.saveAndFlush(imageConversionStatistics);
        imageConversionStatisticsSearchRepository.save(imageConversionStatistics);

        // Search the imageConversionStatistics
        restImageConversionStatisticsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageConversionStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].statisticsDate").value(hasItem(DEFAULT_STATISTICS_DATE.toString())))
            .andExpect(jsonPath("$.[*].totalConversions").value(hasItem(DEFAULT_TOTAL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].successfulConversions").value(hasItem(DEFAULT_SUCCESSFUL_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].failedConversions").value(hasItem(DEFAULT_FAILED_CONVERSIONS)))
            .andExpect(jsonPath("$.[*].totalPagesConverted").value(hasItem(DEFAULT_TOTAL_PAGES_CONVERTED)))
            .andExpect(jsonPath("$.[*].totalImagesGenerated").value(hasItem(DEFAULT_TOTAL_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].averageProcessingDuration").value(hasItem(DEFAULT_AVERAGE_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].maxProcessingDuration").value(hasItem(DEFAULT_MAX_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].minProcessingDuration").value(hasItem(DEFAULT_MIN_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].calculatedAt").value(hasItem(DEFAULT_CALCULATED_AT.toString())));
    }

    protected long getRepositoryCount() {
        return imageConversionStatisticsRepository.count();
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

    protected ImageConversionStatistics getPersistedImageConversionStatistics(ImageConversionStatistics imageConversionStatistics) {
        return imageConversionStatisticsRepository.findById(imageConversionStatistics.getId()).orElseThrow();
    }

    protected void assertPersistedImageConversionStatisticsToMatchAllProperties(
        ImageConversionStatistics expectedImageConversionStatistics
    ) {
        assertImageConversionStatisticsAllPropertiesEquals(
            expectedImageConversionStatistics,
            getPersistedImageConversionStatistics(expectedImageConversionStatistics)
        );
    }

    protected void assertPersistedImageConversionStatisticsToMatchUpdatableProperties(
        ImageConversionStatistics expectedImageConversionStatistics
    ) {
        assertImageConversionStatisticsAllUpdatablePropertiesEquals(
            expectedImageConversionStatistics,
            getPersistedImageConversionStatistics(expectedImageConversionStatistics)
        );
    }
}
