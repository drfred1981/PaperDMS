package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImageAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.pdftoimage.IntegrationTest;
import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import fr.smartprod.paperdms.pdftoimage.domain.ImagePdfConversionRequest;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import fr.smartprod.paperdms.pdftoimage.repository.ImageGeneratedImageRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageGeneratedImageSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageGeneratedImageDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageGeneratedImageMapper;
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
 * Integration tests for the {@link ImageGeneratedImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageGeneratedImageResourceIT {

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_PRE_SIGNED_URL = "AAAAAAAAAA";
    private static final String UPDATED_PRE_SIGNED_URL = "BBBBBBBBBB";

    private static final Instant DEFAULT_URL_EXPIRES_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_URL_EXPIRES_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ImageFormat DEFAULT_FORMAT = ImageFormat.PNG;
    private static final ImageFormat UPDATED_FORMAT = ImageFormat.JPEG;

    private static final ImageQuality DEFAULT_QUALITY = ImageQuality.LOW;
    private static final ImageQuality UPDATED_QUALITY = ImageQuality.MEDIUM;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;
    private static final Integer SMALLER_WIDTH = 1 - 1;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;
    private static final Integer SMALLER_HEIGHT = 1 - 1;

    private static final Long DEFAULT_FILE_SIZE = 0L;
    private static final Long UPDATED_FILE_SIZE = 1L;
    private static final Long SMALLER_FILE_SIZE = 0L - 1L;

    private static final Integer DEFAULT_DPI = 72;
    private static final Integer UPDATED_DPI = 73;
    private static final Integer SMALLER_DPI = 72 - 1;

    private static final String DEFAULT_SHA_256_HASH = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256_HASH = "BBBBBBBBBB";

    private static final Instant DEFAULT_GENERATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GENERATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/image-generated-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-generated-images/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageGeneratedImageRepository imageGeneratedImageRepository;

    @Autowired
    private ImageGeneratedImageMapper imageGeneratedImageMapper;

    @Autowired
    private ImageGeneratedImageSearchRepository imageGeneratedImageSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageGeneratedImageMockMvc;

    private ImageGeneratedImage imageGeneratedImage;

    private ImageGeneratedImage insertedImageGeneratedImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageGeneratedImage createEntity(EntityManager em) {
        ImageGeneratedImage imageGeneratedImage = new ImageGeneratedImage()
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .fileName(DEFAULT_FILE_NAME)
            .s3Key(DEFAULT_S_3_KEY)
            .preSignedUrl(DEFAULT_PRE_SIGNED_URL)
            .urlExpiresAt(DEFAULT_URL_EXPIRES_AT)
            .format(DEFAULT_FORMAT)
            .quality(DEFAULT_QUALITY)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .fileSize(DEFAULT_FILE_SIZE)
            .dpi(DEFAULT_DPI)
            .sha256Hash(DEFAULT_SHA_256_HASH)
            .generatedAt(DEFAULT_GENERATED_AT)
            .metadata(DEFAULT_METADATA);
        // Add required entity
        ImagePdfConversionRequest imagePdfConversionRequest;
        if (TestUtil.findAll(em, ImagePdfConversionRequest.class).isEmpty()) {
            imagePdfConversionRequest = ImagePdfConversionRequestResourceIT.createEntity();
            em.persist(imagePdfConversionRequest);
            em.flush();
        } else {
            imagePdfConversionRequest = TestUtil.findAll(em, ImagePdfConversionRequest.class).get(0);
        }
        imageGeneratedImage.setConversionRequest(imagePdfConversionRequest);
        return imageGeneratedImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageGeneratedImage createUpdatedEntity(EntityManager em) {
        ImageGeneratedImage updatedImageGeneratedImage = new ImageGeneratedImage()
            .pageNumber(UPDATED_PAGE_NUMBER)
            .fileName(UPDATED_FILE_NAME)
            .s3Key(UPDATED_S_3_KEY)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL)
            .urlExpiresAt(UPDATED_URL_EXPIRES_AT)
            .format(UPDATED_FORMAT)
            .quality(UPDATED_QUALITY)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .fileSize(UPDATED_FILE_SIZE)
            .dpi(UPDATED_DPI)
            .sha256Hash(UPDATED_SHA_256_HASH)
            .generatedAt(UPDATED_GENERATED_AT)
            .metadata(UPDATED_METADATA);
        // Add required entity
        ImagePdfConversionRequest imagePdfConversionRequest;
        if (TestUtil.findAll(em, ImagePdfConversionRequest.class).isEmpty()) {
            imagePdfConversionRequest = ImagePdfConversionRequestResourceIT.createUpdatedEntity();
            em.persist(imagePdfConversionRequest);
            em.flush();
        } else {
            imagePdfConversionRequest = TestUtil.findAll(em, ImagePdfConversionRequest.class).get(0);
        }
        updatedImageGeneratedImage.setConversionRequest(imagePdfConversionRequest);
        return updatedImageGeneratedImage;
    }

    @BeforeEach
    void initTest() {
        imageGeneratedImage = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedImageGeneratedImage != null) {
            imageGeneratedImageRepository.delete(insertedImageGeneratedImage);
            imageGeneratedImageSearchRepository.delete(insertedImageGeneratedImage);
            insertedImageGeneratedImage = null;
        }
    }

    @Test
    @Transactional
    void createImageGeneratedImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);
        var returnedImageGeneratedImageDTO = om.readValue(
            restImageGeneratedImageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageGeneratedImageDTO.class
        );

        // Validate the ImageGeneratedImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageGeneratedImage = imageGeneratedImageMapper.toEntity(returnedImageGeneratedImageDTO);
        assertImageGeneratedImageUpdatableFieldsEquals(
            returnedImageGeneratedImage,
            getPersistedImageGeneratedImage(returnedImageGeneratedImage)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImageGeneratedImage = returnedImageGeneratedImage;
    }

    @Test
    @Transactional
    void createImageGeneratedImageWithExistingId() throws Exception {
        // Create the ImageGeneratedImage with an existing ID
        imageGeneratedImage.setId(1L);
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setPageNumber(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setFileName(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.sets3Key(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setFormat(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkQualityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setQuality(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkWidthIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setWidth(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkHeightIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setHeight(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setFileSize(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDpiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setDpi(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGeneratedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        // set the field null
        imageGeneratedImage.setGeneratedAt(null);

        // Create the ImageGeneratedImage, which fails.
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImages() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageGeneratedImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].preSignedUrl").value(hasItem(DEFAULT_PRE_SIGNED_URL)))
            .andExpect(jsonPath("$.[*].urlExpiresAt").value(hasItem(DEFAULT_URL_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].sha256Hash").value(hasItem(DEFAULT_SHA_256_HASH)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @Test
    @Transactional
    void getImageGeneratedImage() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get the imageGeneratedImage
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL_ID, imageGeneratedImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageGeneratedImage.getId().intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.preSignedUrl").value(DEFAULT_PRE_SIGNED_URL))
            .andExpect(jsonPath("$.urlExpiresAt").value(DEFAULT_URL_EXPIRES_AT.toString()))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY.toString()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.dpi").value(DEFAULT_DPI))
            .andExpect(jsonPath("$.sha256Hash").value(DEFAULT_SHA_256_HASH))
            .andExpect(jsonPath("$.generatedAt").value(DEFAULT_GENERATED_AT.toString()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getImageGeneratedImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        Long id = imageGeneratedImage.getId();

        defaultImageGeneratedImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageGeneratedImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageGeneratedImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber equals to
        defaultImageGeneratedImageFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber in
        defaultImageGeneratedImageFiltering(
            "pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER,
            "pageNumber.in=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber is not null
        defaultImageGeneratedImageFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber is greater than or equal to
        defaultImageGeneratedImageFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber is less than or equal to
        defaultImageGeneratedImageFiltering(
            "pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber is less than
        defaultImageGeneratedImageFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where pageNumber is greater than
        defaultImageGeneratedImageFiltering(
            "pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER,
            "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileName equals to
        defaultImageGeneratedImageFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileName in
        defaultImageGeneratedImageFiltering(
            "fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME,
            "fileName.in=" + UPDATED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileName is not null
        defaultImageGeneratedImageFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileName contains
        defaultImageGeneratedImageFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileName does not contain
        defaultImageGeneratedImageFiltering("fileName.doesNotContain=" + UPDATED_FILE_NAME, "fileName.doesNotContain=" + DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where s3Key equals to
        defaultImageGeneratedImageFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where s3Key in
        defaultImageGeneratedImageFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where s3Key is not null
        defaultImageGeneratedImageFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where s3Key contains
        defaultImageGeneratedImageFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where s3Key does not contain
        defaultImageGeneratedImageFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPreSignedUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where preSignedUrl equals to
        defaultImageGeneratedImageFiltering(
            "preSignedUrl.equals=" + DEFAULT_PRE_SIGNED_URL,
            "preSignedUrl.equals=" + UPDATED_PRE_SIGNED_URL
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPreSignedUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where preSignedUrl in
        defaultImageGeneratedImageFiltering(
            "preSignedUrl.in=" + DEFAULT_PRE_SIGNED_URL + "," + UPDATED_PRE_SIGNED_URL,
            "preSignedUrl.in=" + UPDATED_PRE_SIGNED_URL
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPreSignedUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where preSignedUrl is not null
        defaultImageGeneratedImageFiltering("preSignedUrl.specified=true", "preSignedUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPreSignedUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where preSignedUrl contains
        defaultImageGeneratedImageFiltering(
            "preSignedUrl.contains=" + DEFAULT_PRE_SIGNED_URL,
            "preSignedUrl.contains=" + UPDATED_PRE_SIGNED_URL
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByPreSignedUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where preSignedUrl does not contain
        defaultImageGeneratedImageFiltering(
            "preSignedUrl.doesNotContain=" + UPDATED_PRE_SIGNED_URL,
            "preSignedUrl.doesNotContain=" + DEFAULT_PRE_SIGNED_URL
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByUrlExpiresAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where urlExpiresAt equals to
        defaultImageGeneratedImageFiltering(
            "urlExpiresAt.equals=" + DEFAULT_URL_EXPIRES_AT,
            "urlExpiresAt.equals=" + UPDATED_URL_EXPIRES_AT
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByUrlExpiresAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where urlExpiresAt in
        defaultImageGeneratedImageFiltering(
            "urlExpiresAt.in=" + DEFAULT_URL_EXPIRES_AT + "," + UPDATED_URL_EXPIRES_AT,
            "urlExpiresAt.in=" + UPDATED_URL_EXPIRES_AT
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByUrlExpiresAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where urlExpiresAt is not null
        defaultImageGeneratedImageFiltering("urlExpiresAt.specified=true", "urlExpiresAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where format equals to
        defaultImageGeneratedImageFiltering("format.equals=" + DEFAULT_FORMAT, "format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where format in
        defaultImageGeneratedImageFiltering("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT, "format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where format is not null
        defaultImageGeneratedImageFiltering("format.specified=true", "format.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByQualityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where quality equals to
        defaultImageGeneratedImageFiltering("quality.equals=" + DEFAULT_QUALITY, "quality.equals=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByQualityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where quality in
        defaultImageGeneratedImageFiltering("quality.in=" + DEFAULT_QUALITY + "," + UPDATED_QUALITY, "quality.in=" + UPDATED_QUALITY);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByQualityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where quality is not null
        defaultImageGeneratedImageFiltering("quality.specified=true", "quality.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width equals to
        defaultImageGeneratedImageFiltering("width.equals=" + DEFAULT_WIDTH, "width.equals=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width in
        defaultImageGeneratedImageFiltering("width.in=" + DEFAULT_WIDTH + "," + UPDATED_WIDTH, "width.in=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width is not null
        defaultImageGeneratedImageFiltering("width.specified=true", "width.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width is greater than or equal to
        defaultImageGeneratedImageFiltering("width.greaterThanOrEqual=" + DEFAULT_WIDTH, "width.greaterThanOrEqual=" + UPDATED_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width is less than or equal to
        defaultImageGeneratedImageFiltering("width.lessThanOrEqual=" + DEFAULT_WIDTH, "width.lessThanOrEqual=" + SMALLER_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width is less than
        defaultImageGeneratedImageFiltering("width.lessThan=" + UPDATED_WIDTH, "width.lessThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByWidthIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where width is greater than
        defaultImageGeneratedImageFiltering("width.greaterThan=" + SMALLER_WIDTH, "width.greaterThan=" + DEFAULT_WIDTH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height equals to
        defaultImageGeneratedImageFiltering("height.equals=" + DEFAULT_HEIGHT, "height.equals=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height in
        defaultImageGeneratedImageFiltering("height.in=" + DEFAULT_HEIGHT + "," + UPDATED_HEIGHT, "height.in=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height is not null
        defaultImageGeneratedImageFiltering("height.specified=true", "height.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height is greater than or equal to
        defaultImageGeneratedImageFiltering("height.greaterThanOrEqual=" + DEFAULT_HEIGHT, "height.greaterThanOrEqual=" + UPDATED_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height is less than or equal to
        defaultImageGeneratedImageFiltering("height.lessThanOrEqual=" + DEFAULT_HEIGHT, "height.lessThanOrEqual=" + SMALLER_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height is less than
        defaultImageGeneratedImageFiltering("height.lessThan=" + UPDATED_HEIGHT, "height.lessThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByHeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where height is greater than
        defaultImageGeneratedImageFiltering("height.greaterThan=" + SMALLER_HEIGHT, "height.greaterThan=" + DEFAULT_HEIGHT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize equals to
        defaultImageGeneratedImageFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize in
        defaultImageGeneratedImageFiltering(
            "fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE,
            "fileSize.in=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize is not null
        defaultImageGeneratedImageFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize is greater than or equal to
        defaultImageGeneratedImageFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize is less than or equal to
        defaultImageGeneratedImageFiltering(
            "fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize is less than
        defaultImageGeneratedImageFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where fileSize is greater than
        defaultImageGeneratedImageFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi equals to
        defaultImageGeneratedImageFiltering("dpi.equals=" + DEFAULT_DPI, "dpi.equals=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi in
        defaultImageGeneratedImageFiltering("dpi.in=" + DEFAULT_DPI + "," + UPDATED_DPI, "dpi.in=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi is not null
        defaultImageGeneratedImageFiltering("dpi.specified=true", "dpi.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi is greater than or equal to
        defaultImageGeneratedImageFiltering("dpi.greaterThanOrEqual=" + DEFAULT_DPI, "dpi.greaterThanOrEqual=" + UPDATED_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi is less than or equal to
        defaultImageGeneratedImageFiltering("dpi.lessThanOrEqual=" + DEFAULT_DPI, "dpi.lessThanOrEqual=" + SMALLER_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi is less than
        defaultImageGeneratedImageFiltering("dpi.lessThan=" + UPDATED_DPI, "dpi.lessThan=" + DEFAULT_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByDpiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where dpi is greater than
        defaultImageGeneratedImageFiltering("dpi.greaterThan=" + SMALLER_DPI, "dpi.greaterThan=" + DEFAULT_DPI);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBySha256HashIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where sha256Hash equals to
        defaultImageGeneratedImageFiltering("sha256Hash.equals=" + DEFAULT_SHA_256_HASH, "sha256Hash.equals=" + UPDATED_SHA_256_HASH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBySha256HashIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where sha256Hash in
        defaultImageGeneratedImageFiltering(
            "sha256Hash.in=" + DEFAULT_SHA_256_HASH + "," + UPDATED_SHA_256_HASH,
            "sha256Hash.in=" + UPDATED_SHA_256_HASH
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBySha256HashIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where sha256Hash is not null
        defaultImageGeneratedImageFiltering("sha256Hash.specified=true", "sha256Hash.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBySha256HashContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where sha256Hash contains
        defaultImageGeneratedImageFiltering("sha256Hash.contains=" + DEFAULT_SHA_256_HASH, "sha256Hash.contains=" + UPDATED_SHA_256_HASH);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesBySha256HashNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where sha256Hash does not contain
        defaultImageGeneratedImageFiltering(
            "sha256Hash.doesNotContain=" + UPDATED_SHA_256_HASH,
            "sha256Hash.doesNotContain=" + DEFAULT_SHA_256_HASH
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByGeneratedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where generatedAt equals to
        defaultImageGeneratedImageFiltering("generatedAt.equals=" + DEFAULT_GENERATED_AT, "generatedAt.equals=" + UPDATED_GENERATED_AT);
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByGeneratedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where generatedAt in
        defaultImageGeneratedImageFiltering(
            "generatedAt.in=" + DEFAULT_GENERATED_AT + "," + UPDATED_GENERATED_AT,
            "generatedAt.in=" + UPDATED_GENERATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByGeneratedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        // Get all the imageGeneratedImageList where generatedAt is not null
        defaultImageGeneratedImageFiltering("generatedAt.specified=true", "generatedAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageGeneratedImagesByConversionRequestIsEqualToSomething() throws Exception {
        ImagePdfConversionRequest conversionRequest;
        if (TestUtil.findAll(em, ImagePdfConversionRequest.class).isEmpty()) {
            imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);
            conversionRequest = ImagePdfConversionRequestResourceIT.createEntity();
        } else {
            conversionRequest = TestUtil.findAll(em, ImagePdfConversionRequest.class).get(0);
        }
        em.persist(conversionRequest);
        em.flush();
        imageGeneratedImage.setConversionRequest(conversionRequest);
        imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);
        Long conversionRequestId = conversionRequest.getId();
        // Get all the imageGeneratedImageList where conversionRequest equals to conversionRequestId
        defaultImageGeneratedImageShouldBeFound("conversionRequestId.equals=" + conversionRequestId);

        // Get all the imageGeneratedImageList where conversionRequest equals to (conversionRequestId + 1)
        defaultImageGeneratedImageShouldNotBeFound("conversionRequestId.equals=" + (conversionRequestId + 1));
    }

    private void defaultImageGeneratedImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageGeneratedImageShouldBeFound(shouldBeFound);
        defaultImageGeneratedImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageGeneratedImageShouldBeFound(String filter) throws Exception {
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageGeneratedImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].preSignedUrl").value(hasItem(DEFAULT_PRE_SIGNED_URL)))
            .andExpect(jsonPath("$.[*].urlExpiresAt").value(hasItem(DEFAULT_URL_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].sha256Hash").value(hasItem(DEFAULT_SHA_256_HASH)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageGeneratedImageShouldNotBeFound(String filter) throws Exception {
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageGeneratedImage() throws Exception {
        // Get the imageGeneratedImage
        restImageGeneratedImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageGeneratedImage() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageGeneratedImageSearchRepository.save(imageGeneratedImage);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());

        // Update the imageGeneratedImage
        ImageGeneratedImage updatedImageGeneratedImage = imageGeneratedImageRepository.findById(imageGeneratedImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImageGeneratedImage are not directly saved in db
        em.detach(updatedImageGeneratedImage);
        updatedImageGeneratedImage
            .pageNumber(UPDATED_PAGE_NUMBER)
            .fileName(UPDATED_FILE_NAME)
            .s3Key(UPDATED_S_3_KEY)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL)
            .urlExpiresAt(UPDATED_URL_EXPIRES_AT)
            .format(UPDATED_FORMAT)
            .quality(UPDATED_QUALITY)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .fileSize(UPDATED_FILE_SIZE)
            .dpi(UPDATED_DPI)
            .sha256Hash(UPDATED_SHA_256_HASH)
            .generatedAt(UPDATED_GENERATED_AT)
            .metadata(UPDATED_METADATA);
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(updatedImageGeneratedImage);

        restImageGeneratedImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageGeneratedImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageGeneratedImageToMatchAllProperties(updatedImageGeneratedImage);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImageGeneratedImage> imageGeneratedImageSearchList = Streamable.of(
                    imageGeneratedImageSearchRepository.findAll()
                ).toList();
                ImageGeneratedImage testImageGeneratedImageSearch = imageGeneratedImageSearchList.get(searchDatabaseSizeAfter - 1);

                assertImageGeneratedImageAllPropertiesEquals(testImageGeneratedImageSearch, updatedImageGeneratedImage);
            });
    }

    @Test
    @Transactional
    void putNonExistingImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageGeneratedImageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageGeneratedImageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImageGeneratedImageWithPatch() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageGeneratedImage using partial update
        ImageGeneratedImage partialUpdatedImageGeneratedImage = new ImageGeneratedImage();
        partialUpdatedImageGeneratedImage.setId(imageGeneratedImage.getId());

        partialUpdatedImageGeneratedImage
            .pageNumber(UPDATED_PAGE_NUMBER)
            .fileName(UPDATED_FILE_NAME)
            .width(UPDATED_WIDTH)
            .dpi(UPDATED_DPI)
            .sha256Hash(UPDATED_SHA_256_HASH)
            .metadata(UPDATED_METADATA);

        restImageGeneratedImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageGeneratedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageGeneratedImage))
            )
            .andExpect(status().isOk());

        // Validate the ImageGeneratedImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageGeneratedImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageGeneratedImage, imageGeneratedImage),
            getPersistedImageGeneratedImage(imageGeneratedImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageGeneratedImageWithPatch() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageGeneratedImage using partial update
        ImageGeneratedImage partialUpdatedImageGeneratedImage = new ImageGeneratedImage();
        partialUpdatedImageGeneratedImage.setId(imageGeneratedImage.getId());

        partialUpdatedImageGeneratedImage
            .pageNumber(UPDATED_PAGE_NUMBER)
            .fileName(UPDATED_FILE_NAME)
            .s3Key(UPDATED_S_3_KEY)
            .preSignedUrl(UPDATED_PRE_SIGNED_URL)
            .urlExpiresAt(UPDATED_URL_EXPIRES_AT)
            .format(UPDATED_FORMAT)
            .quality(UPDATED_QUALITY)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .fileSize(UPDATED_FILE_SIZE)
            .dpi(UPDATED_DPI)
            .sha256Hash(UPDATED_SHA_256_HASH)
            .generatedAt(UPDATED_GENERATED_AT)
            .metadata(UPDATED_METADATA);

        restImageGeneratedImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageGeneratedImage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageGeneratedImage))
            )
            .andExpect(status().isOk());

        // Validate the ImageGeneratedImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageGeneratedImageUpdatableFieldsEquals(
            partialUpdatedImageGeneratedImage,
            getPersistedImageGeneratedImage(partialUpdatedImageGeneratedImage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageGeneratedImageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageGeneratedImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        imageGeneratedImage.setId(longCount.incrementAndGet());

        // Create the ImageGeneratedImage
        ImageGeneratedImageDTO imageGeneratedImageDTO = imageGeneratedImageMapper.toDto(imageGeneratedImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageGeneratedImageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageGeneratedImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageGeneratedImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImageGeneratedImage() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);
        imageGeneratedImageRepository.save(imageGeneratedImage);
        imageGeneratedImageSearchRepository.save(imageGeneratedImage);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imageGeneratedImage
        restImageGeneratedImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageGeneratedImage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageGeneratedImageSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImageGeneratedImage() throws Exception {
        // Initialize the database
        insertedImageGeneratedImage = imageGeneratedImageRepository.saveAndFlush(imageGeneratedImage);
        imageGeneratedImageSearchRepository.save(imageGeneratedImage);

        // Search the imageGeneratedImage
        restImageGeneratedImageMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageGeneratedImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageGeneratedImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].preSignedUrl").value(hasItem(DEFAULT_PRE_SIGNED_URL)))
            .andExpect(jsonPath("$.[*].urlExpiresAt").value(hasItem(DEFAULT_URL_EXPIRES_AT.toString())))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].sha256Hash").value(hasItem(DEFAULT_SHA_256_HASH)))
            .andExpect(jsonPath("$.[*].generatedAt").value(hasItem(DEFAULT_GENERATED_AT.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())));
    }

    protected long getRepositoryCount() {
        return imageGeneratedImageRepository.count();
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

    protected ImageGeneratedImage getPersistedImageGeneratedImage(ImageGeneratedImage imageGeneratedImage) {
        return imageGeneratedImageRepository.findById(imageGeneratedImage.getId()).orElseThrow();
    }

    protected void assertPersistedImageGeneratedImageToMatchAllProperties(ImageGeneratedImage expectedImageGeneratedImage) {
        assertImageGeneratedImageAllPropertiesEquals(
            expectedImageGeneratedImage,
            getPersistedImageGeneratedImage(expectedImageGeneratedImage)
        );
    }

    protected void assertPersistedImageGeneratedImageToMatchUpdatableProperties(ImageGeneratedImage expectedImageGeneratedImage) {
        assertImageGeneratedImageAllUpdatablePropertiesEquals(
            expectedImageGeneratedImage,
            getPersistedImageGeneratedImage(expectedImageGeneratedImage)
        );
    }
}
