package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequestAsserts.*;
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
import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionStatus;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import fr.smartprod.paperdms.pdftoimage.repository.ImagePdfConversionRequestRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImagePdfConversionRequestSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImagePdfConversionRequestDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImagePdfConversionRequestMapper;
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
 * Integration tests for the {@link ImagePdfConversionRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImagePdfConversionRequestResourceIT {

    private static final Long DEFAULT_SOURCE_DOCUMENT_ID = 1L;
    private static final Long UPDATED_SOURCE_DOCUMENT_ID = 2L;
    private static final Long SMALLER_SOURCE_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_SOURCE_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_PDF_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_PDF_S_3_KEY = "BBBBBBBBBB";

    private static final ImageQuality DEFAULT_IMAGE_QUALITY = ImageQuality.LOW;
    private static final ImageQuality UPDATED_IMAGE_QUALITY = ImageQuality.MEDIUM;

    private static final ImageFormat DEFAULT_IMAGE_FORMAT = ImageFormat.PNG;
    private static final ImageFormat UPDATED_IMAGE_FORMAT = ImageFormat.JPEG;

    private static final ConversionType DEFAULT_CONVERSION_TYPE = ConversionType.SINGLE_PAGE;
    private static final ConversionType UPDATED_CONVERSION_TYPE = ConversionType.ALL_PAGES;

    private static final Integer DEFAULT_START_PAGE = 1;
    private static final Integer UPDATED_START_PAGE = 2;
    private static final Integer SMALLER_START_PAGE = 1 - 1;

    private static final Integer DEFAULT_END_PAGE = 1;
    private static final Integer UPDATED_END_PAGE = 2;
    private static final Integer SMALLER_END_PAGE = 1 - 1;

    private static final Integer DEFAULT_TOTAL_PAGES = 1;
    private static final Integer UPDATED_TOTAL_PAGES = 2;
    private static final Integer SMALLER_TOTAL_PAGES = 1 - 1;

    private static final ConversionStatus DEFAULT_STATUS = ConversionStatus.PENDING;
    private static final ConversionStatus UPDATED_STATUS = ConversionStatus.PROCESSING;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_REQUESTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_REQUESTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_STARTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_STARTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_PROCESSING_DURATION = 1L - 1L;

    private static final Long DEFAULT_TOTAL_IMAGES_SIZE = 1L;
    private static final Long UPDATED_TOTAL_IMAGES_SIZE = 2L;
    private static final Long SMALLER_TOTAL_IMAGES_SIZE = 1L - 1L;

    private static final Integer DEFAULT_IMAGES_GENERATED = 0;
    private static final Integer UPDATED_IMAGES_GENERATED = 1;
    private static final Integer SMALLER_IMAGES_GENERATED = 0 - 1;

    private static final Integer DEFAULT_DPI = 72;
    private static final Integer UPDATED_DPI = 73;
    private static final Integer SMALLER_DPI = 72 - 1;

    private static final Long DEFAULT_REQUESTED_BY_USER_ID = 1L;
    private static final Long UPDATED_REQUESTED_BY_USER_ID = 2L;
    private static final Long SMALLER_REQUESTED_BY_USER_ID = 1L - 1L;

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final String DEFAULT_ADDITIONAL_OPTIONS = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_OPTIONS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-pdf-conversion-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-pdf-conversion-requests/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImagePdfConversionRequestRepository imagePdfConversionRequestRepository;

    @Autowired
    private ImagePdfConversionRequestMapper imagePdfConversionRequestMapper;

    @Autowired
    private ImagePdfConversionRequestSearchRepository imagePdfConversionRequestSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImagePdfConversionRequestMockMvc;

    private ImagePdfConversionRequest imagePdfConversionRequest;

    private ImagePdfConversionRequest insertedImagePdfConversionRequest;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePdfConversionRequest createEntity() {
        return new ImagePdfConversionRequest()
            .sourceDocumentId(DEFAULT_SOURCE_DOCUMENT_ID)
            .sourceFileName(DEFAULT_SOURCE_FILE_NAME)
            .sourcePdfS3Key(DEFAULT_SOURCE_PDF_S_3_KEY)
            .imageQuality(DEFAULT_IMAGE_QUALITY)
            .imageFormat(DEFAULT_IMAGE_FORMAT)
            .conversionType(DEFAULT_CONVERSION_TYPE)
            .startPage(DEFAULT_START_PAGE)
            .endPage(DEFAULT_END_PAGE)
            .totalPages(DEFAULT_TOTAL_PAGES)
            .status(DEFAULT_STATUS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .requestedAt(DEFAULT_REQUESTED_AT)
            .startedAt(DEFAULT_STARTED_AT)
            .completedAt(DEFAULT_COMPLETED_AT)
            .processingDuration(DEFAULT_PROCESSING_DURATION)
            .totalImagesSize(DEFAULT_TOTAL_IMAGES_SIZE)
            .imagesGenerated(DEFAULT_IMAGES_GENERATED)
            .dpi(DEFAULT_DPI)
            .requestedByUserId(DEFAULT_REQUESTED_BY_USER_ID)
            .priority(DEFAULT_PRIORITY)
            .additionalOptions(DEFAULT_ADDITIONAL_OPTIONS);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImagePdfConversionRequest createUpdatedEntity() {
        return new ImagePdfConversionRequest()
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .sourceFileName(UPDATED_SOURCE_FILE_NAME)
            .sourcePdfS3Key(UPDATED_SOURCE_PDF_S_3_KEY)
            .imageQuality(UPDATED_IMAGE_QUALITY)
            .imageFormat(UPDATED_IMAGE_FORMAT)
            .conversionType(UPDATED_CONVERSION_TYPE)
            .startPage(UPDATED_START_PAGE)
            .endPage(UPDATED_END_PAGE)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .requestedAt(UPDATED_REQUESTED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .imagesGenerated(UPDATED_IMAGES_GENERATED)
            .dpi(UPDATED_DPI)
            .requestedByUserId(UPDATED_REQUESTED_BY_USER_ID)
            .priority(UPDATED_PRIORITY)
            .additionalOptions(UPDATED_ADDITIONAL_OPTIONS);
    }

    @BeforeEach
    void initTest() {
        imagePdfConversionRequest = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImagePdfConversionRequest != null) {
            imagePdfConversionRequestRepository.delete(insertedImagePdfConversionRequest);
            imagePdfConversionRequestSearchRepository.delete(insertedImagePdfConversionRequest);
            insertedImagePdfConversionRequest = null;
        }
    }

    @Test
    @Transactional
    void createImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);
        var returnedImagePdfConversionRequestDTO = om.readValue(
            restImagePdfConversionRequestMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImagePdfConversionRequestDTO.class
        );

        // Validate the ImagePdfConversionRequest in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImagePdfConversionRequest = imagePdfConversionRequestMapper.toEntity(returnedImagePdfConversionRequestDTO);
        assertImagePdfConversionRequestUpdatableFieldsEquals(
            returnedImagePdfConversionRequest,
            getPersistedImagePdfConversionRequest(returnedImagePdfConversionRequest)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImagePdfConversionRequest = returnedImagePdfConversionRequest;
    }

    @Test
    @Transactional
    void createImagePdfConversionRequestWithExistingId() throws Exception {
        // Create the ImagePdfConversionRequest with an existing ID
        imagePdfConversionRequest.setId(1L);
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSourceDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setSourceDocumentId(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSourceFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setSourceFileName(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSourcePdfS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setSourcePdfS3Key(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkImageQualityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setImageQuality(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkImageFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setImageFormat(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConversionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setConversionType(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setStatus(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkRequestedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        // set the field null
        imagePdfConversionRequest.setRequestedAt(null);

        // Create the ImagePdfConversionRequest, which fails.
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequests() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagePdfConversionRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentId").value(hasItem(DEFAULT_SOURCE_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceFileName").value(hasItem(DEFAULT_SOURCE_FILE_NAME)))
            .andExpect(jsonPath("$.[*].sourcePdfS3Key").value(hasItem(DEFAULT_SOURCE_PDF_S_3_KEY)))
            .andExpect(jsonPath("$.[*].imageQuality").value(hasItem(DEFAULT_IMAGE_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].imageFormat").value(hasItem(DEFAULT_IMAGE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].conversionType").value(hasItem(DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startPage").value(hasItem(DEFAULT_START_PAGE)))
            .andExpect(jsonPath("$.[*].endPage").value(hasItem(DEFAULT_END_PAGE)))
            .andExpect(jsonPath("$.[*].totalPages").value(hasItem(DEFAULT_TOTAL_PAGES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].requestedAt").value(hasItem(DEFAULT_REQUESTED_AT.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].imagesGenerated").value(hasItem(DEFAULT_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].requestedByUserId").value(hasItem(DEFAULT_REQUESTED_BY_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].additionalOptions").value(hasItem(DEFAULT_ADDITIONAL_OPTIONS)));
    }

    @Test
    @Transactional
    void getImagePdfConversionRequest() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get the imagePdfConversionRequest
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, imagePdfConversionRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imagePdfConversionRequest.getId().intValue()))
            .andExpect(jsonPath("$.sourceDocumentId").value(DEFAULT_SOURCE_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.sourceFileName").value(DEFAULT_SOURCE_FILE_NAME))
            .andExpect(jsonPath("$.sourcePdfS3Key").value(DEFAULT_SOURCE_PDF_S_3_KEY))
            .andExpect(jsonPath("$.imageQuality").value(DEFAULT_IMAGE_QUALITY.toString()))
            .andExpect(jsonPath("$.imageFormat").value(DEFAULT_IMAGE_FORMAT.toString()))
            .andExpect(jsonPath("$.conversionType").value(DEFAULT_CONVERSION_TYPE.toString()))
            .andExpect(jsonPath("$.startPage").value(DEFAULT_START_PAGE))
            .andExpect(jsonPath("$.endPage").value(DEFAULT_END_PAGE))
            .andExpect(jsonPath("$.totalPages").value(DEFAULT_TOTAL_PAGES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.requestedAt").value(DEFAULT_REQUESTED_AT.toString()))
            .andExpect(jsonPath("$.startedAt").value(DEFAULT_STARTED_AT.toString()))
            .andExpect(jsonPath("$.completedAt").value(DEFAULT_COMPLETED_AT.toString()))
            .andExpect(jsonPath("$.processingDuration").value(DEFAULT_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.totalImagesSize").value(DEFAULT_TOTAL_IMAGES_SIZE.intValue()))
            .andExpect(jsonPath("$.imagesGenerated").value(DEFAULT_IMAGES_GENERATED))
            .andExpect(jsonPath("$.dpi").value(DEFAULT_DPI))
            .andExpect(jsonPath("$.requestedByUserId").value(DEFAULT_REQUESTED_BY_USER_ID.intValue()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.additionalOptions").value(DEFAULT_ADDITIONAL_OPTIONS));
    }

    @Test
    @Transactional
    void getImagePdfConversionRequestsByIdFiltering() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        Long id = imagePdfConversionRequest.getId();

        defaultImagePdfConversionRequestFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImagePdfConversionRequestFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImagePdfConversionRequestFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId equals to
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.equals=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.equals=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId in
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.in=" + DEFAULT_SOURCE_DOCUMENT_ID + "," + UPDATED_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.in=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId is not null
        defaultImagePdfConversionRequestFiltering("sourceDocumentId.specified=true", "sourceDocumentId.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.greaterThanOrEqual=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.greaterThanOrEqual=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.lessThanOrEqual=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.lessThanOrEqual=" + SMALLER_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId is less than
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.lessThan=" + UPDATED_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.lessThan=" + DEFAULT_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceDocumentId is greater than
        defaultImagePdfConversionRequestFiltering(
            "sourceDocumentId.greaterThan=" + SMALLER_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.greaterThan=" + DEFAULT_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceFileName equals to
        defaultImagePdfConversionRequestFiltering(
            "sourceFileName.equals=" + DEFAULT_SOURCE_FILE_NAME,
            "sourceFileName.equals=" + UPDATED_SOURCE_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceFileName in
        defaultImagePdfConversionRequestFiltering(
            "sourceFileName.in=" + DEFAULT_SOURCE_FILE_NAME + "," + UPDATED_SOURCE_FILE_NAME,
            "sourceFileName.in=" + UPDATED_SOURCE_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceFileName is not null
        defaultImagePdfConversionRequestFiltering("sourceFileName.specified=true", "sourceFileName.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceFileName contains
        defaultImagePdfConversionRequestFiltering(
            "sourceFileName.contains=" + DEFAULT_SOURCE_FILE_NAME,
            "sourceFileName.contains=" + UPDATED_SOURCE_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourceFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourceFileName does not contain
        defaultImagePdfConversionRequestFiltering(
            "sourceFileName.doesNotContain=" + UPDATED_SOURCE_FILE_NAME,
            "sourceFileName.doesNotContain=" + DEFAULT_SOURCE_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourcePdfS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourcePdfS3Key equals to
        defaultImagePdfConversionRequestFiltering(
            "sourcePdfS3Key.equals=" + DEFAULT_SOURCE_PDF_S_3_KEY,
            "sourcePdfS3Key.equals=" + UPDATED_SOURCE_PDF_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourcePdfS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourcePdfS3Key in
        defaultImagePdfConversionRequestFiltering(
            "sourcePdfS3Key.in=" + DEFAULT_SOURCE_PDF_S_3_KEY + "," + UPDATED_SOURCE_PDF_S_3_KEY,
            "sourcePdfS3Key.in=" + UPDATED_SOURCE_PDF_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourcePdfS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourcePdfS3Key is not null
        defaultImagePdfConversionRequestFiltering("sourcePdfS3Key.specified=true", "sourcePdfS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourcePdfS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourcePdfS3Key contains
        defaultImagePdfConversionRequestFiltering(
            "sourcePdfS3Key.contains=" + DEFAULT_SOURCE_PDF_S_3_KEY,
            "sourcePdfS3Key.contains=" + UPDATED_SOURCE_PDF_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsBySourcePdfS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where sourcePdfS3Key does not contain
        defaultImagePdfConversionRequestFiltering(
            "sourcePdfS3Key.doesNotContain=" + UPDATED_SOURCE_PDF_S_3_KEY,
            "sourcePdfS3Key.doesNotContain=" + DEFAULT_SOURCE_PDF_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageQualityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageQuality equals to
        defaultImagePdfConversionRequestFiltering(
            "imageQuality.equals=" + DEFAULT_IMAGE_QUALITY,
            "imageQuality.equals=" + UPDATED_IMAGE_QUALITY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageQualityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageQuality in
        defaultImagePdfConversionRequestFiltering(
            "imageQuality.in=" + DEFAULT_IMAGE_QUALITY + "," + UPDATED_IMAGE_QUALITY,
            "imageQuality.in=" + UPDATED_IMAGE_QUALITY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageQualityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageQuality is not null
        defaultImagePdfConversionRequestFiltering("imageQuality.specified=true", "imageQuality.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageFormat equals to
        defaultImagePdfConversionRequestFiltering(
            "imageFormat.equals=" + DEFAULT_IMAGE_FORMAT,
            "imageFormat.equals=" + UPDATED_IMAGE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageFormat in
        defaultImagePdfConversionRequestFiltering(
            "imageFormat.in=" + DEFAULT_IMAGE_FORMAT + "," + UPDATED_IMAGE_FORMAT,
            "imageFormat.in=" + UPDATED_IMAGE_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImageFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imageFormat is not null
        defaultImagePdfConversionRequestFiltering("imageFormat.specified=true", "imageFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByConversionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where conversionType equals to
        defaultImagePdfConversionRequestFiltering(
            "conversionType.equals=" + DEFAULT_CONVERSION_TYPE,
            "conversionType.equals=" + UPDATED_CONVERSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByConversionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where conversionType in
        defaultImagePdfConversionRequestFiltering(
            "conversionType.in=" + DEFAULT_CONVERSION_TYPE + "," + UPDATED_CONVERSION_TYPE,
            "conversionType.in=" + UPDATED_CONVERSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByConversionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where conversionType is not null
        defaultImagePdfConversionRequestFiltering("conversionType.specified=true", "conversionType.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage equals to
        defaultImagePdfConversionRequestFiltering("startPage.equals=" + DEFAULT_START_PAGE, "startPage.equals=" + UPDATED_START_PAGE);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage in
        defaultImagePdfConversionRequestFiltering(
            "startPage.in=" + DEFAULT_START_PAGE + "," + UPDATED_START_PAGE,
            "startPage.in=" + UPDATED_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage is not null
        defaultImagePdfConversionRequestFiltering("startPage.specified=true", "startPage.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "startPage.greaterThanOrEqual=" + DEFAULT_START_PAGE,
            "startPage.greaterThanOrEqual=" + UPDATED_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "startPage.lessThanOrEqual=" + DEFAULT_START_PAGE,
            "startPage.lessThanOrEqual=" + SMALLER_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage is less than
        defaultImagePdfConversionRequestFiltering("startPage.lessThan=" + UPDATED_START_PAGE, "startPage.lessThan=" + DEFAULT_START_PAGE);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startPage is greater than
        defaultImagePdfConversionRequestFiltering(
            "startPage.greaterThan=" + SMALLER_START_PAGE,
            "startPage.greaterThan=" + DEFAULT_START_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage equals to
        defaultImagePdfConversionRequestFiltering("endPage.equals=" + DEFAULT_END_PAGE, "endPage.equals=" + UPDATED_END_PAGE);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage in
        defaultImagePdfConversionRequestFiltering(
            "endPage.in=" + DEFAULT_END_PAGE + "," + UPDATED_END_PAGE,
            "endPage.in=" + UPDATED_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage is not null
        defaultImagePdfConversionRequestFiltering("endPage.specified=true", "endPage.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "endPage.greaterThanOrEqual=" + DEFAULT_END_PAGE,
            "endPage.greaterThanOrEqual=" + UPDATED_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "endPage.lessThanOrEqual=" + DEFAULT_END_PAGE,
            "endPage.lessThanOrEqual=" + SMALLER_END_PAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage is less than
        defaultImagePdfConversionRequestFiltering("endPage.lessThan=" + UPDATED_END_PAGE, "endPage.lessThan=" + DEFAULT_END_PAGE);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByEndPageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where endPage is greater than
        defaultImagePdfConversionRequestFiltering("endPage.greaterThan=" + SMALLER_END_PAGE, "endPage.greaterThan=" + DEFAULT_END_PAGE);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages equals to
        defaultImagePdfConversionRequestFiltering("totalPages.equals=" + DEFAULT_TOTAL_PAGES, "totalPages.equals=" + UPDATED_TOTAL_PAGES);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages in
        defaultImagePdfConversionRequestFiltering(
            "totalPages.in=" + DEFAULT_TOTAL_PAGES + "," + UPDATED_TOTAL_PAGES,
            "totalPages.in=" + UPDATED_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages is not null
        defaultImagePdfConversionRequestFiltering("totalPages.specified=true", "totalPages.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "totalPages.greaterThanOrEqual=" + DEFAULT_TOTAL_PAGES,
            "totalPages.greaterThanOrEqual=" + UPDATED_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "totalPages.lessThanOrEqual=" + DEFAULT_TOTAL_PAGES,
            "totalPages.lessThanOrEqual=" + SMALLER_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages is less than
        defaultImagePdfConversionRequestFiltering(
            "totalPages.lessThan=" + UPDATED_TOTAL_PAGES,
            "totalPages.lessThan=" + DEFAULT_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalPagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalPages is greater than
        defaultImagePdfConversionRequestFiltering(
            "totalPages.greaterThan=" + SMALLER_TOTAL_PAGES,
            "totalPages.greaterThan=" + DEFAULT_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where status equals to
        defaultImagePdfConversionRequestFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where status in
        defaultImagePdfConversionRequestFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where status is not null
        defaultImagePdfConversionRequestFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByErrorMessageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where errorMessage equals to
        defaultImagePdfConversionRequestFiltering(
            "errorMessage.equals=" + DEFAULT_ERROR_MESSAGE,
            "errorMessage.equals=" + UPDATED_ERROR_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByErrorMessageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where errorMessage in
        defaultImagePdfConversionRequestFiltering(
            "errorMessage.in=" + DEFAULT_ERROR_MESSAGE + "," + UPDATED_ERROR_MESSAGE,
            "errorMessage.in=" + UPDATED_ERROR_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByErrorMessageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where errorMessage is not null
        defaultImagePdfConversionRequestFiltering("errorMessage.specified=true", "errorMessage.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByErrorMessageContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where errorMessage contains
        defaultImagePdfConversionRequestFiltering(
            "errorMessage.contains=" + DEFAULT_ERROR_MESSAGE,
            "errorMessage.contains=" + UPDATED_ERROR_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByErrorMessageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where errorMessage does not contain
        defaultImagePdfConversionRequestFiltering(
            "errorMessage.doesNotContain=" + UPDATED_ERROR_MESSAGE,
            "errorMessage.doesNotContain=" + DEFAULT_ERROR_MESSAGE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedAt equals to
        defaultImagePdfConversionRequestFiltering(
            "requestedAt.equals=" + DEFAULT_REQUESTED_AT,
            "requestedAt.equals=" + UPDATED_REQUESTED_AT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedAt in
        defaultImagePdfConversionRequestFiltering(
            "requestedAt.in=" + DEFAULT_REQUESTED_AT + "," + UPDATED_REQUESTED_AT,
            "requestedAt.in=" + UPDATED_REQUESTED_AT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedAt is not null
        defaultImagePdfConversionRequestFiltering("requestedAt.specified=true", "requestedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startedAt equals to
        defaultImagePdfConversionRequestFiltering("startedAt.equals=" + DEFAULT_STARTED_AT, "startedAt.equals=" + UPDATED_STARTED_AT);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startedAt in
        defaultImagePdfConversionRequestFiltering(
            "startedAt.in=" + DEFAULT_STARTED_AT + "," + UPDATED_STARTED_AT,
            "startedAt.in=" + UPDATED_STARTED_AT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByStartedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where startedAt is not null
        defaultImagePdfConversionRequestFiltering("startedAt.specified=true", "startedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByCompletedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where completedAt equals to
        defaultImagePdfConversionRequestFiltering(
            "completedAt.equals=" + DEFAULT_COMPLETED_AT,
            "completedAt.equals=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByCompletedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where completedAt in
        defaultImagePdfConversionRequestFiltering(
            "completedAt.in=" + DEFAULT_COMPLETED_AT + "," + UPDATED_COMPLETED_AT,
            "completedAt.in=" + UPDATED_COMPLETED_AT
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByCompletedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where completedAt is not null
        defaultImagePdfConversionRequestFiltering("completedAt.specified=true", "completedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration equals to
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.equals=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.equals=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration in
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.in=" + DEFAULT_PROCESSING_DURATION + "," + UPDATED_PROCESSING_DURATION,
            "processingDuration.in=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration is not null
        defaultImagePdfConversionRequestFiltering("processingDuration.specified=true", "processingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.greaterThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.greaterThanOrEqual=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.lessThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.lessThanOrEqual=" + SMALLER_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration is less than
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.lessThan=" + UPDATED_PROCESSING_DURATION,
            "processingDuration.lessThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where processingDuration is greater than
        defaultImagePdfConversionRequestFiltering(
            "processingDuration.greaterThan=" + SMALLER_PROCESSING_DURATION,
            "processingDuration.greaterThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize equals to
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.equals=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.equals=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize in
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.in=" + DEFAULT_TOTAL_IMAGES_SIZE + "," + UPDATED_TOTAL_IMAGES_SIZE,
            "totalImagesSize.in=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize is not null
        defaultImagePdfConversionRequestFiltering("totalImagesSize.specified=true", "totalImagesSize.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.greaterThanOrEqual=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.greaterThanOrEqual=" + UPDATED_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.lessThanOrEqual=" + DEFAULT_TOTAL_IMAGES_SIZE,
            "totalImagesSize.lessThanOrEqual=" + SMALLER_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize is less than
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.lessThan=" + UPDATED_TOTAL_IMAGES_SIZE,
            "totalImagesSize.lessThan=" + DEFAULT_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByTotalImagesSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where totalImagesSize is greater than
        defaultImagePdfConversionRequestFiltering(
            "totalImagesSize.greaterThan=" + SMALLER_TOTAL_IMAGES_SIZE,
            "totalImagesSize.greaterThan=" + DEFAULT_TOTAL_IMAGES_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated equals to
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.equals=" + DEFAULT_IMAGES_GENERATED,
            "imagesGenerated.equals=" + UPDATED_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated in
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.in=" + DEFAULT_IMAGES_GENERATED + "," + UPDATED_IMAGES_GENERATED,
            "imagesGenerated.in=" + UPDATED_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated is not null
        defaultImagePdfConversionRequestFiltering("imagesGenerated.specified=true", "imagesGenerated.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.greaterThanOrEqual=" + DEFAULT_IMAGES_GENERATED,
            "imagesGenerated.greaterThanOrEqual=" + UPDATED_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.lessThanOrEqual=" + DEFAULT_IMAGES_GENERATED,
            "imagesGenerated.lessThanOrEqual=" + SMALLER_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated is less than
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.lessThan=" + UPDATED_IMAGES_GENERATED,
            "imagesGenerated.lessThan=" + DEFAULT_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByImagesGeneratedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where imagesGenerated is greater than
        defaultImagePdfConversionRequestFiltering(
            "imagesGenerated.greaterThan=" + SMALLER_IMAGES_GENERATED,
            "imagesGenerated.greaterThan=" + DEFAULT_IMAGES_GENERATED
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi equals to
        defaultImagePdfConversionRequestFiltering("dpi.equals=" + DEFAULT_DPI, "dpi.equals=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi in
        defaultImagePdfConversionRequestFiltering("dpi.in=" + DEFAULT_DPI + "," + UPDATED_DPI, "dpi.in=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi is not null
        defaultImagePdfConversionRequestFiltering("dpi.specified=true", "dpi.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi is greater than or equal to
        defaultImagePdfConversionRequestFiltering("dpi.greaterThanOrEqual=" + DEFAULT_DPI, "dpi.greaterThanOrEqual=" + (DEFAULT_DPI + 1));
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi is less than or equal to
        defaultImagePdfConversionRequestFiltering("dpi.lessThanOrEqual=" + DEFAULT_DPI, "dpi.lessThanOrEqual=" + SMALLER_DPI);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi is less than
        defaultImagePdfConversionRequestFiltering("dpi.lessThan=" + (DEFAULT_DPI + 1), "dpi.lessThan=" + DEFAULT_DPI);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByDpiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where dpi is greater than
        defaultImagePdfConversionRequestFiltering("dpi.greaterThan=" + SMALLER_DPI, "dpi.greaterThan=" + DEFAULT_DPI);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId equals to
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.equals=" + DEFAULT_REQUESTED_BY_USER_ID,
            "requestedByUserId.equals=" + UPDATED_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId in
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.in=" + DEFAULT_REQUESTED_BY_USER_ID + "," + UPDATED_REQUESTED_BY_USER_ID,
            "requestedByUserId.in=" + UPDATED_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId is not null
        defaultImagePdfConversionRequestFiltering("requestedByUserId.specified=true", "requestedByUserId.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.greaterThanOrEqual=" + DEFAULT_REQUESTED_BY_USER_ID,
            "requestedByUserId.greaterThanOrEqual=" + UPDATED_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.lessThanOrEqual=" + DEFAULT_REQUESTED_BY_USER_ID,
            "requestedByUserId.lessThanOrEqual=" + SMALLER_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId is less than
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.lessThan=" + UPDATED_REQUESTED_BY_USER_ID,
            "requestedByUserId.lessThan=" + DEFAULT_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByRequestedByUserIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where requestedByUserId is greater than
        defaultImagePdfConversionRequestFiltering(
            "requestedByUserId.greaterThan=" + SMALLER_REQUESTED_BY_USER_ID,
            "requestedByUserId.greaterThan=" + DEFAULT_REQUESTED_BY_USER_ID
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority equals to
        defaultImagePdfConversionRequestFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority in
        defaultImagePdfConversionRequestFiltering(
            "priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY,
            "priority.in=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority is not null
        defaultImagePdfConversionRequestFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority is greater than or equal to
        defaultImagePdfConversionRequestFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + (DEFAULT_PRIORITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority is less than or equal to
        defaultImagePdfConversionRequestFiltering(
            "priority.lessThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.lessThanOrEqual=" + SMALLER_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority is less than
        defaultImagePdfConversionRequestFiltering("priority.lessThan=" + (DEFAULT_PRIORITY + 1), "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        // Get all the imagePdfConversionRequestList where priority is greater than
        defaultImagePdfConversionRequestFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllImagePdfConversionRequestsByBatchIsEqualToSomething() throws Exception {
        ImageConversionBatch batch;
        if (TestUtil.findAll(em, ImageConversionBatch.class).isEmpty()) {
            imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);
            batch = ImageConversionBatchResourceIT.createEntity();
        } else {
            batch = TestUtil.findAll(em, ImageConversionBatch.class).get(0);
        }
        em.persist(batch);
        em.flush();
        imagePdfConversionRequest.setBatch(batch);
        imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);
        Long batchId = batch.getId();
        // Get all the imagePdfConversionRequestList where batch equals to batchId
        defaultImagePdfConversionRequestShouldBeFound("batchId.equals=" + batchId);

        // Get all the imagePdfConversionRequestList where batch equals to (batchId + 1)
        defaultImagePdfConversionRequestShouldNotBeFound("batchId.equals=" + (batchId + 1));
    }

    private void defaultImagePdfConversionRequestFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImagePdfConversionRequestShouldBeFound(shouldBeFound);
        defaultImagePdfConversionRequestShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImagePdfConversionRequestShouldBeFound(String filter) throws Exception {
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagePdfConversionRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentId").value(hasItem(DEFAULT_SOURCE_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceFileName").value(hasItem(DEFAULT_SOURCE_FILE_NAME)))
            .andExpect(jsonPath("$.[*].sourcePdfS3Key").value(hasItem(DEFAULT_SOURCE_PDF_S_3_KEY)))
            .andExpect(jsonPath("$.[*].imageQuality").value(hasItem(DEFAULT_IMAGE_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].imageFormat").value(hasItem(DEFAULT_IMAGE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].conversionType").value(hasItem(DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startPage").value(hasItem(DEFAULT_START_PAGE)))
            .andExpect(jsonPath("$.[*].endPage").value(hasItem(DEFAULT_END_PAGE)))
            .andExpect(jsonPath("$.[*].totalPages").value(hasItem(DEFAULT_TOTAL_PAGES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].requestedAt").value(hasItem(DEFAULT_REQUESTED_AT.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].imagesGenerated").value(hasItem(DEFAULT_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].requestedByUserId").value(hasItem(DEFAULT_REQUESTED_BY_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].additionalOptions").value(hasItem(DEFAULT_ADDITIONAL_OPTIONS)));

        // Check, that the count call also returns 1
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImagePdfConversionRequestShouldNotBeFound(String filter) throws Exception {
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImagePdfConversionRequest() throws Exception {
        // Get the imagePdfConversionRequest
        restImagePdfConversionRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImagePdfConversionRequest() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imagePdfConversionRequestSearchRepository.save(imagePdfConversionRequest);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());

        // Update the imagePdfConversionRequest
        ImagePdfConversionRequest updatedImagePdfConversionRequest = imagePdfConversionRequestRepository
            .findById(imagePdfConversionRequest.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedImagePdfConversionRequest are not directly saved in db
        em.detach(updatedImagePdfConversionRequest);
        updatedImagePdfConversionRequest
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .sourceFileName(UPDATED_SOURCE_FILE_NAME)
            .sourcePdfS3Key(UPDATED_SOURCE_PDF_S_3_KEY)
            .imageQuality(UPDATED_IMAGE_QUALITY)
            .imageFormat(UPDATED_IMAGE_FORMAT)
            .conversionType(UPDATED_CONVERSION_TYPE)
            .startPage(UPDATED_START_PAGE)
            .endPage(UPDATED_END_PAGE)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .requestedAt(UPDATED_REQUESTED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .imagesGenerated(UPDATED_IMAGES_GENERATED)
            .dpi(UPDATED_DPI)
            .requestedByUserId(UPDATED_REQUESTED_BY_USER_ID)
            .priority(UPDATED_PRIORITY)
            .additionalOptions(UPDATED_ADDITIONAL_OPTIONS);
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(updatedImagePdfConversionRequest);

        restImagePdfConversionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagePdfConversionRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImagePdfConversionRequestToMatchAllProperties(updatedImagePdfConversionRequest);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImagePdfConversionRequest> imagePdfConversionRequestSearchList = Streamable.of(
                    imagePdfConversionRequestSearchRepository.findAll()
                ).toList();
                ImagePdfConversionRequest testImagePdfConversionRequestSearch = imagePdfConversionRequestSearchList.get(
                    searchDatabaseSizeAfter - 1
                );

                assertImagePdfConversionRequestAllPropertiesEquals(testImagePdfConversionRequestSearch, updatedImagePdfConversionRequest);
            });
    }

    @Test
    @Transactional
    void putNonExistingImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imagePdfConversionRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImagePdfConversionRequestWithPatch() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imagePdfConversionRequest using partial update
        ImagePdfConversionRequest partialUpdatedImagePdfConversionRequest = new ImagePdfConversionRequest();
        partialUpdatedImagePdfConversionRequest.setId(imagePdfConversionRequest.getId());

        partialUpdatedImagePdfConversionRequest
            .sourceFileName(UPDATED_SOURCE_FILE_NAME)
            .imageFormat(UPDATED_IMAGE_FORMAT)
            .endPage(UPDATED_END_PAGE)
            .status(UPDATED_STATUS)
            .completedAt(UPDATED_COMPLETED_AT)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .imagesGenerated(UPDATED_IMAGES_GENERATED)
            .priority(UPDATED_PRIORITY);

        restImagePdfConversionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagePdfConversionRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImagePdfConversionRequest))
            )
            .andExpect(status().isOk());

        // Validate the ImagePdfConversionRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImagePdfConversionRequestUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImagePdfConversionRequest, imagePdfConversionRequest),
            getPersistedImagePdfConversionRequest(imagePdfConversionRequest)
        );
    }

    @Test
    @Transactional
    void fullUpdateImagePdfConversionRequestWithPatch() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imagePdfConversionRequest using partial update
        ImagePdfConversionRequest partialUpdatedImagePdfConversionRequest = new ImagePdfConversionRequest();
        partialUpdatedImagePdfConversionRequest.setId(imagePdfConversionRequest.getId());

        partialUpdatedImagePdfConversionRequest
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .sourceFileName(UPDATED_SOURCE_FILE_NAME)
            .sourcePdfS3Key(UPDATED_SOURCE_PDF_S_3_KEY)
            .imageQuality(UPDATED_IMAGE_QUALITY)
            .imageFormat(UPDATED_IMAGE_FORMAT)
            .conversionType(UPDATED_CONVERSION_TYPE)
            .startPage(UPDATED_START_PAGE)
            .endPage(UPDATED_END_PAGE)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .requestedAt(UPDATED_REQUESTED_AT)
            .startedAt(UPDATED_STARTED_AT)
            .completedAt(UPDATED_COMPLETED_AT)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .totalImagesSize(UPDATED_TOTAL_IMAGES_SIZE)
            .imagesGenerated(UPDATED_IMAGES_GENERATED)
            .dpi(UPDATED_DPI)
            .requestedByUserId(UPDATED_REQUESTED_BY_USER_ID)
            .priority(UPDATED_PRIORITY)
            .additionalOptions(UPDATED_ADDITIONAL_OPTIONS);

        restImagePdfConversionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImagePdfConversionRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImagePdfConversionRequest))
            )
            .andExpect(status().isOk());

        // Validate the ImagePdfConversionRequest in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImagePdfConversionRequestUpdatableFieldsEquals(
            partialUpdatedImagePdfConversionRequest,
            getPersistedImagePdfConversionRequest(partialUpdatedImagePdfConversionRequest)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imagePdfConversionRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImagePdfConversionRequest() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        imagePdfConversionRequest.setId(longCount.incrementAndGet());

        // Create the ImagePdfConversionRequest
        ImagePdfConversionRequestDTO imagePdfConversionRequestDTO = imagePdfConversionRequestMapper.toDto(imagePdfConversionRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImagePdfConversionRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imagePdfConversionRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImagePdfConversionRequest in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImagePdfConversionRequest() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);
        imagePdfConversionRequestRepository.save(imagePdfConversionRequest);
        imagePdfConversionRequestSearchRepository.save(imagePdfConversionRequest);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imagePdfConversionRequest
        restImagePdfConversionRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, imagePdfConversionRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imagePdfConversionRequestSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImagePdfConversionRequest() throws Exception {
        // Initialize the database
        insertedImagePdfConversionRequest = imagePdfConversionRequestRepository.saveAndFlush(imagePdfConversionRequest);
        imagePdfConversionRequestSearchRepository.save(imagePdfConversionRequest);

        // Search the imagePdfConversionRequest
        restImagePdfConversionRequestMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imagePdfConversionRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imagePdfConversionRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentId").value(hasItem(DEFAULT_SOURCE_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].sourceFileName").value(hasItem(DEFAULT_SOURCE_FILE_NAME)))
            .andExpect(jsonPath("$.[*].sourcePdfS3Key").value(hasItem(DEFAULT_SOURCE_PDF_S_3_KEY)))
            .andExpect(jsonPath("$.[*].imageQuality").value(hasItem(DEFAULT_IMAGE_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].imageFormat").value(hasItem(DEFAULT_IMAGE_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].conversionType").value(hasItem(DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startPage").value(hasItem(DEFAULT_START_PAGE)))
            .andExpect(jsonPath("$.[*].endPage").value(hasItem(DEFAULT_END_PAGE)))
            .andExpect(jsonPath("$.[*].totalPages").value(hasItem(DEFAULT_TOTAL_PAGES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].requestedAt").value(hasItem(DEFAULT_REQUESTED_AT.toString())))
            .andExpect(jsonPath("$.[*].startedAt").value(hasItem(DEFAULT_STARTED_AT.toString())))
            .andExpect(jsonPath("$.[*].completedAt").value(hasItem(DEFAULT_COMPLETED_AT.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].totalImagesSize").value(hasItem(DEFAULT_TOTAL_IMAGES_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].imagesGenerated").value(hasItem(DEFAULT_IMAGES_GENERATED)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].requestedByUserId").value(hasItem(DEFAULT_REQUESTED_BY_USER_ID.intValue())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].additionalOptions").value(hasItem(DEFAULT_ADDITIONAL_OPTIONS.toString())));
    }

    protected long getRepositoryCount() {
        return imagePdfConversionRequestRepository.count();
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

    protected ImagePdfConversionRequest getPersistedImagePdfConversionRequest(ImagePdfConversionRequest imagePdfConversionRequest) {
        return imagePdfConversionRequestRepository.findById(imagePdfConversionRequest.getId()).orElseThrow();
    }

    protected void assertPersistedImagePdfConversionRequestToMatchAllProperties(
        ImagePdfConversionRequest expectedImagePdfConversionRequest
    ) {
        assertImagePdfConversionRequestAllPropertiesEquals(
            expectedImagePdfConversionRequest,
            getPersistedImagePdfConversionRequest(expectedImagePdfConversionRequest)
        );
    }

    protected void assertPersistedImagePdfConversionRequestToMatchUpdatableProperties(
        ImagePdfConversionRequest expectedImagePdfConversionRequest
    ) {
        assertImagePdfConversionRequestAllUpdatablePropertiesEquals(
            expectedImagePdfConversionRequest,
            getPersistedImagePdfConversionRequest(expectedImagePdfConversionRequest)
        );
    }
}
