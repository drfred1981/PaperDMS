package fr.smartprod.paperdms.pdftoimage.web.rest;

import static fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfigAsserts.*;
import static fr.smartprod.paperdms.pdftoimage.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.pdftoimage.IntegrationTest;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ConversionType;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageFormat;
import fr.smartprod.paperdms.pdftoimage.domain.enumeration.ImageQuality;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionConfigRepository;
import fr.smartprod.paperdms.pdftoimage.repository.search.ImageConversionConfigSearchRepository;
import fr.smartprod.paperdms.pdftoimage.service.dto.ImageConversionConfigDTO;
import fr.smartprod.paperdms.pdftoimage.service.mapper.ImageConversionConfigMapper;
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
 * Integration tests for the {@link ImageConversionConfigResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImageConversionConfigResourceIT {

    private static final String DEFAULT_CONFIG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_CONFIG_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ImageQuality DEFAULT_DEFAULT_QUALITY = ImageQuality.LOW;
    private static final ImageQuality UPDATED_DEFAULT_QUALITY = ImageQuality.MEDIUM;

    private static final ImageFormat DEFAULT_DEFAULT_FORMAT = ImageFormat.PNG;
    private static final ImageFormat UPDATED_DEFAULT_FORMAT = ImageFormat.JPEG;

    private static final Integer DEFAULT_DEFAULT_DPI = 72;
    private static final Integer UPDATED_DEFAULT_DPI = 73;
    private static final Integer SMALLER_DEFAULT_DPI = 72 - 1;

    private static final ConversionType DEFAULT_DEFAULT_CONVERSION_TYPE = ConversionType.SINGLE_PAGE;
    private static final ConversionType UPDATED_DEFAULT_CONVERSION_TYPE = ConversionType.ALL_PAGES;

    private static final Integer DEFAULT_DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_DEFAULT_PRIORITY = 2;
    private static final Integer SMALLER_DEFAULT_PRIORITY = 1 - 1;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_UPDATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/image-conversion-configs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/image-conversion-configs/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImageConversionConfigRepository imageConversionConfigRepository;

    @Autowired
    private ImageConversionConfigMapper imageConversionConfigMapper;

    @Autowired
    private ImageConversionConfigSearchRepository imageConversionConfigSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImageConversionConfigMockMvc;

    private ImageConversionConfig imageConversionConfig;

    private ImageConversionConfig insertedImageConversionConfig;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionConfig createEntity() {
        return new ImageConversionConfig()
            .configName(DEFAULT_CONFIG_NAME)
            .description(DEFAULT_DESCRIPTION)
            .defaultQuality(DEFAULT_DEFAULT_QUALITY)
            .defaultFormat(DEFAULT_DEFAULT_FORMAT)
            .defaultDpi(DEFAULT_DEFAULT_DPI)
            .defaultConversionType(DEFAULT_DEFAULT_CONVERSION_TYPE)
            .defaultPriority(DEFAULT_DEFAULT_PRIORITY)
            .isActive(DEFAULT_IS_ACTIVE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .createdAt(DEFAULT_CREATED_AT)
            .updatedAt(DEFAULT_UPDATED_AT);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImageConversionConfig createUpdatedEntity() {
        return new ImageConversionConfig()
            .configName(UPDATED_CONFIG_NAME)
            .description(UPDATED_DESCRIPTION)
            .defaultQuality(UPDATED_DEFAULT_QUALITY)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .defaultDpi(UPDATED_DEFAULT_DPI)
            .defaultConversionType(UPDATED_DEFAULT_CONVERSION_TYPE)
            .defaultPriority(UPDATED_DEFAULT_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
    }

    @BeforeEach
    void initTest() {
        imageConversionConfig = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedImageConversionConfig != null) {
            imageConversionConfigRepository.delete(insertedImageConversionConfig);
            imageConversionConfigSearchRepository.delete(insertedImageConversionConfig);
            insertedImageConversionConfig = null;
        }
    }

    @Test
    @Transactional
    void createImageConversionConfig() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);
        var returnedImageConversionConfigDTO = om.readValue(
            restImageConversionConfigMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImageConversionConfigDTO.class
        );

        // Validate the ImageConversionConfig in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImageConversionConfig = imageConversionConfigMapper.toEntity(returnedImageConversionConfigDTO);
        assertImageConversionConfigUpdatableFieldsEquals(
            returnedImageConversionConfig,
            getPersistedImageConversionConfig(returnedImageConversionConfig)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedImageConversionConfig = returnedImageConversionConfig;
    }

    @Test
    @Transactional
    void createImageConversionConfigWithExistingId() throws Exception {
        // Create the ImageConversionConfig with an existing ID
        imageConversionConfig.setId(1L);
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkConfigNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setConfigName(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDefaultQualityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setDefaultQuality(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDefaultFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setDefaultFormat(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDefaultDpiIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setDefaultDpi(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDefaultConversionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setDefaultConversionType(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setIsActive(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsDefaultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setIsDefault(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        // set the field null
        imageConversionConfig.setCreatedAt(null);

        // Create the ImageConversionConfig, which fails.
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigs() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].defaultQuality").value(hasItem(DEFAULT_DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].defaultFormat").value(hasItem(DEFAULT_DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].defaultDpi").value(hasItem(DEFAULT_DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].defaultConversionType").value(hasItem(DEFAULT_DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].defaultPriority").value(hasItem(DEFAULT_DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    @Test
    @Transactional
    void getImageConversionConfig() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get the imageConversionConfig
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL_ID, imageConversionConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(imageConversionConfig.getId().intValue()))
            .andExpect(jsonPath("$.configName").value(DEFAULT_CONFIG_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.defaultQuality").value(DEFAULT_DEFAULT_QUALITY.toString()))
            .andExpect(jsonPath("$.defaultFormat").value(DEFAULT_DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.defaultDpi").value(DEFAULT_DEFAULT_DPI))
            .andExpect(jsonPath("$.defaultConversionType").value(DEFAULT_DEFAULT_CONVERSION_TYPE.toString()))
            .andExpect(jsonPath("$.defaultPriority").value(DEFAULT_DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.updatedAt").value(DEFAULT_UPDATED_AT.toString()));
    }

    @Test
    @Transactional
    void getImageConversionConfigsByIdFiltering() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        Long id = imageConversionConfig.getId();

        defaultImageConversionConfigFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultImageConversionConfigFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultImageConversionConfigFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByConfigNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where configName equals to
        defaultImageConversionConfigFiltering("configName.equals=" + DEFAULT_CONFIG_NAME, "configName.equals=" + UPDATED_CONFIG_NAME);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByConfigNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where configName in
        defaultImageConversionConfigFiltering(
            "configName.in=" + DEFAULT_CONFIG_NAME + "," + UPDATED_CONFIG_NAME,
            "configName.in=" + UPDATED_CONFIG_NAME
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByConfigNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where configName is not null
        defaultImageConversionConfigFiltering("configName.specified=true", "configName.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByConfigNameContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where configName contains
        defaultImageConversionConfigFiltering("configName.contains=" + DEFAULT_CONFIG_NAME, "configName.contains=" + UPDATED_CONFIG_NAME);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByConfigNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where configName does not contain
        defaultImageConversionConfigFiltering(
            "configName.doesNotContain=" + UPDATED_CONFIG_NAME,
            "configName.doesNotContain=" + DEFAULT_CONFIG_NAME
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where description equals to
        defaultImageConversionConfigFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where description in
        defaultImageConversionConfigFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where description is not null
        defaultImageConversionConfigFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where description contains
        defaultImageConversionConfigFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where description does not contain
        defaultImageConversionConfigFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultQualityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultQuality equals to
        defaultImageConversionConfigFiltering(
            "defaultQuality.equals=" + DEFAULT_DEFAULT_QUALITY,
            "defaultQuality.equals=" + UPDATED_DEFAULT_QUALITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultQualityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultQuality in
        defaultImageConversionConfigFiltering(
            "defaultQuality.in=" + DEFAULT_DEFAULT_QUALITY + "," + UPDATED_DEFAULT_QUALITY,
            "defaultQuality.in=" + UPDATED_DEFAULT_QUALITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultQualityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultQuality is not null
        defaultImageConversionConfigFiltering("defaultQuality.specified=true", "defaultQuality.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultFormat equals to
        defaultImageConversionConfigFiltering(
            "defaultFormat.equals=" + DEFAULT_DEFAULT_FORMAT,
            "defaultFormat.equals=" + UPDATED_DEFAULT_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultFormat in
        defaultImageConversionConfigFiltering(
            "defaultFormat.in=" + DEFAULT_DEFAULT_FORMAT + "," + UPDATED_DEFAULT_FORMAT,
            "defaultFormat.in=" + UPDATED_DEFAULT_FORMAT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultFormat is not null
        defaultImageConversionConfigFiltering("defaultFormat.specified=true", "defaultFormat.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi equals to
        defaultImageConversionConfigFiltering("defaultDpi.equals=" + DEFAULT_DEFAULT_DPI, "defaultDpi.equals=" + UPDATED_DEFAULT_DPI);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi in
        defaultImageConversionConfigFiltering(
            "defaultDpi.in=" + DEFAULT_DEFAULT_DPI + "," + UPDATED_DEFAULT_DPI,
            "defaultDpi.in=" + UPDATED_DEFAULT_DPI
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi is not null
        defaultImageConversionConfigFiltering("defaultDpi.specified=true", "defaultDpi.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi is greater than or equal to
        defaultImageConversionConfigFiltering(
            "defaultDpi.greaterThanOrEqual=" + DEFAULT_DEFAULT_DPI,
            "defaultDpi.greaterThanOrEqual=" + (DEFAULT_DEFAULT_DPI + 1)
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi is less than or equal to
        defaultImageConversionConfigFiltering(
            "defaultDpi.lessThanOrEqual=" + DEFAULT_DEFAULT_DPI,
            "defaultDpi.lessThanOrEqual=" + SMALLER_DEFAULT_DPI
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi is less than
        defaultImageConversionConfigFiltering(
            "defaultDpi.lessThan=" + (DEFAULT_DEFAULT_DPI + 1),
            "defaultDpi.lessThan=" + DEFAULT_DEFAULT_DPI
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultDpiIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultDpi is greater than
        defaultImageConversionConfigFiltering(
            "defaultDpi.greaterThan=" + SMALLER_DEFAULT_DPI,
            "defaultDpi.greaterThan=" + DEFAULT_DEFAULT_DPI
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultConversionTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultConversionType equals to
        defaultImageConversionConfigFiltering(
            "defaultConversionType.equals=" + DEFAULT_DEFAULT_CONVERSION_TYPE,
            "defaultConversionType.equals=" + UPDATED_DEFAULT_CONVERSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultConversionTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultConversionType in
        defaultImageConversionConfigFiltering(
            "defaultConversionType.in=" + DEFAULT_DEFAULT_CONVERSION_TYPE + "," + UPDATED_DEFAULT_CONVERSION_TYPE,
            "defaultConversionType.in=" + UPDATED_DEFAULT_CONVERSION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultConversionTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultConversionType is not null
        defaultImageConversionConfigFiltering("defaultConversionType.specified=true", "defaultConversionType.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority equals to
        defaultImageConversionConfigFiltering(
            "defaultPriority.equals=" + DEFAULT_DEFAULT_PRIORITY,
            "defaultPriority.equals=" + UPDATED_DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority in
        defaultImageConversionConfigFiltering(
            "defaultPriority.in=" + DEFAULT_DEFAULT_PRIORITY + "," + UPDATED_DEFAULT_PRIORITY,
            "defaultPriority.in=" + UPDATED_DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority is not null
        defaultImageConversionConfigFiltering("defaultPriority.specified=true", "defaultPriority.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority is greater than or equal to
        defaultImageConversionConfigFiltering(
            "defaultPriority.greaterThanOrEqual=" + DEFAULT_DEFAULT_PRIORITY,
            "defaultPriority.greaterThanOrEqual=" + (DEFAULT_DEFAULT_PRIORITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority is less than or equal to
        defaultImageConversionConfigFiltering(
            "defaultPriority.lessThanOrEqual=" + DEFAULT_DEFAULT_PRIORITY,
            "defaultPriority.lessThanOrEqual=" + SMALLER_DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority is less than
        defaultImageConversionConfigFiltering(
            "defaultPriority.lessThan=" + (DEFAULT_DEFAULT_PRIORITY + 1),
            "defaultPriority.lessThan=" + DEFAULT_DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByDefaultPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where defaultPriority is greater than
        defaultImageConversionConfigFiltering(
            "defaultPriority.greaterThan=" + SMALLER_DEFAULT_PRIORITY,
            "defaultPriority.greaterThan=" + DEFAULT_DEFAULT_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isActive equals to
        defaultImageConversionConfigFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isActive in
        defaultImageConversionConfigFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isActive is not null
        defaultImageConversionConfigFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isDefault equals to
        defaultImageConversionConfigFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isDefault in
        defaultImageConversionConfigFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where isDefault is not null
        defaultImageConversionConfigFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByCreatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where createdAt equals to
        defaultImageConversionConfigFiltering("createdAt.equals=" + DEFAULT_CREATED_AT, "createdAt.equals=" + UPDATED_CREATED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByCreatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where createdAt in
        defaultImageConversionConfigFiltering(
            "createdAt.in=" + DEFAULT_CREATED_AT + "," + UPDATED_CREATED_AT,
            "createdAt.in=" + UPDATED_CREATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByCreatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where createdAt is not null
        defaultImageConversionConfigFiltering("createdAt.specified=true", "createdAt.specified=false");
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByUpdatedAtIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where updatedAt equals to
        defaultImageConversionConfigFiltering("updatedAt.equals=" + DEFAULT_UPDATED_AT, "updatedAt.equals=" + UPDATED_UPDATED_AT);
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByUpdatedAtIsInShouldWork() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where updatedAt in
        defaultImageConversionConfigFiltering(
            "updatedAt.in=" + DEFAULT_UPDATED_AT + "," + UPDATED_UPDATED_AT,
            "updatedAt.in=" + UPDATED_UPDATED_AT
        );
    }

    @Test
    @Transactional
    void getAllImageConversionConfigsByUpdatedAtIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        // Get all the imageConversionConfigList where updatedAt is not null
        defaultImageConversionConfigFiltering("updatedAt.specified=true", "updatedAt.specified=false");
    }

    private void defaultImageConversionConfigFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultImageConversionConfigShouldBeFound(shouldBeFound);
        defaultImageConversionConfigShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultImageConversionConfigShouldBeFound(String filter) throws Exception {
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].defaultQuality").value(hasItem(DEFAULT_DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].defaultFormat").value(hasItem(DEFAULT_DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].defaultDpi").value(hasItem(DEFAULT_DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].defaultConversionType").value(hasItem(DEFAULT_DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].defaultPriority").value(hasItem(DEFAULT_DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));

        // Check, that the count call also returns 1
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultImageConversionConfigShouldNotBeFound(String filter) throws Exception {
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingImageConversionConfig() throws Exception {
        // Get the imageConversionConfig
        restImageConversionConfigMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImageConversionConfig() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        imageConversionConfigSearchRepository.save(imageConversionConfig);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());

        // Update the imageConversionConfig
        ImageConversionConfig updatedImageConversionConfig = imageConversionConfigRepository
            .findById(imageConversionConfig.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedImageConversionConfig are not directly saved in db
        em.detach(updatedImageConversionConfig);
        updatedImageConversionConfig
            .configName(UPDATED_CONFIG_NAME)
            .description(UPDATED_DESCRIPTION)
            .defaultQuality(UPDATED_DEFAULT_QUALITY)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .defaultDpi(UPDATED_DEFAULT_DPI)
            .defaultConversionType(UPDATED_DEFAULT_CONVERSION_TYPE)
            .defaultPriority(UPDATED_DEFAULT_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(updatedImageConversionConfig);

        restImageConversionConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImageConversionConfigToMatchAllProperties(updatedImageConversionConfig);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ImageConversionConfig> imageConversionConfigSearchList = Streamable.of(
                    imageConversionConfigSearchRepository.findAll()
                ).toList();
                ImageConversionConfig testImageConversionConfigSearch = imageConversionConfigSearchList.get(searchDatabaseSizeAfter - 1);

                assertImageConversionConfigAllPropertiesEquals(testImageConversionConfigSearch, updatedImageConversionConfig);
            });
    }

    @Test
    @Transactional
    void putNonExistingImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, imageConversionConfigDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(imageConversionConfigDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateImageConversionConfigWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionConfig using partial update
        ImageConversionConfig partialUpdatedImageConversionConfig = new ImageConversionConfig();
        partialUpdatedImageConversionConfig.setId(imageConversionConfig.getId());

        partialUpdatedImageConversionConfig
            .description(UPDATED_DESCRIPTION)
            .defaultQuality(UPDATED_DEFAULT_QUALITY)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .defaultDpi(UPDATED_DEFAULT_DPI)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restImageConversionConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionConfig))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionConfigUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImageConversionConfig, imageConversionConfig),
            getPersistedImageConversionConfig(imageConversionConfig)
        );
    }

    @Test
    @Transactional
    void fullUpdateImageConversionConfigWithPatch() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the imageConversionConfig using partial update
        ImageConversionConfig partialUpdatedImageConversionConfig = new ImageConversionConfig();
        partialUpdatedImageConversionConfig.setId(imageConversionConfig.getId());

        partialUpdatedImageConversionConfig
            .configName(UPDATED_CONFIG_NAME)
            .description(UPDATED_DESCRIPTION)
            .defaultQuality(UPDATED_DEFAULT_QUALITY)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .defaultDpi(UPDATED_DEFAULT_DPI)
            .defaultConversionType(UPDATED_DEFAULT_CONVERSION_TYPE)
            .defaultPriority(UPDATED_DEFAULT_PRIORITY)
            .isActive(UPDATED_IS_ACTIVE)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdAt(UPDATED_CREATED_AT)
            .updatedAt(UPDATED_UPDATED_AT);

        restImageConversionConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImageConversionConfig.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImageConversionConfig))
            )
            .andExpect(status().isOk());

        // Validate the ImageConversionConfig in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImageConversionConfigUpdatableFieldsEquals(
            partialUpdatedImageConversionConfig,
            getPersistedImageConversionConfig(partialUpdatedImageConversionConfig)
        );
    }

    @Test
    @Transactional
    void patchNonExistingImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, imageConversionConfigDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImageConversionConfig() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        imageConversionConfig.setId(longCount.incrementAndGet());

        // Create the ImageConversionConfig
        ImageConversionConfigDTO imageConversionConfigDTO = imageConversionConfigMapper.toDto(imageConversionConfig);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImageConversionConfigMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(imageConversionConfigDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImageConversionConfig in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteImageConversionConfig() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);
        imageConversionConfigRepository.save(imageConversionConfig);
        imageConversionConfigSearchRepository.save(imageConversionConfig);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the imageConversionConfig
        restImageConversionConfigMockMvc
            .perform(delete(ENTITY_API_URL_ID, imageConversionConfig.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(imageConversionConfigSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchImageConversionConfig() throws Exception {
        // Initialize the database
        insertedImageConversionConfig = imageConversionConfigRepository.saveAndFlush(imageConversionConfig);
        imageConversionConfigSearchRepository.save(imageConversionConfig);

        // Search the imageConversionConfig
        restImageConversionConfigMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + imageConversionConfig.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(imageConversionConfig.getId().intValue())))
            .andExpect(jsonPath("$.[*].configName").value(hasItem(DEFAULT_CONFIG_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].defaultQuality").value(hasItem(DEFAULT_DEFAULT_QUALITY.toString())))
            .andExpect(jsonPath("$.[*].defaultFormat").value(hasItem(DEFAULT_DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].defaultDpi").value(hasItem(DEFAULT_DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].defaultConversionType").value(hasItem(DEFAULT_DEFAULT_CONVERSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].defaultPriority").value(hasItem(DEFAULT_DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].updatedAt").value(hasItem(DEFAULT_UPDATED_AT.toString())));
    }

    protected long getRepositoryCount() {
        return imageConversionConfigRepository.count();
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

    protected ImageConversionConfig getPersistedImageConversionConfig(ImageConversionConfig imageConversionConfig) {
        return imageConversionConfigRepository.findById(imageConversionConfig.getId()).orElseThrow();
    }

    protected void assertPersistedImageConversionConfigToMatchAllProperties(ImageConversionConfig expectedImageConversionConfig) {
        assertImageConversionConfigAllPropertiesEquals(
            expectedImageConversionConfig,
            getPersistedImageConversionConfig(expectedImageConversionConfig)
        );
    }

    protected void assertPersistedImageConversionConfigToMatchUpdatableProperties(ImageConversionConfig expectedImageConversionConfig) {
        assertImageConversionConfigAllUpdatablePropertiesEquals(
            expectedImageConversionConfig,
            getPersistedImageConversionConfig(expectedImageConversionConfig)
        );
    }
}
