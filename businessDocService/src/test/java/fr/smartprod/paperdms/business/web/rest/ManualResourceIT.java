package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.ManualAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.domain.enumeration.ManualStatus;
import fr.smartprod.paperdms.business.domain.enumeration.ManualType;
import fr.smartprod.paperdms.business.repository.ManualRepository;
import fr.smartprod.paperdms.business.repository.search.ManualSearchRepository;
import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import fr.smartprod.paperdms.business.service.mapper.ManualMapper;
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
 * Integration tests for the {@link ManualResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManualResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final ManualType DEFAULT_MANUAL_TYPE = ManualType.USER_MANUAL;
    private static final ManualType UPDATED_MANUAL_TYPE = ManualType.TECHNICAL_MANUAL;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_PUBLICATION_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLICATION_DATE = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_PUBLICATION_DATE = LocalDate.ofEpochDay(-1L);

    private static final Integer DEFAULT_PAGE_COUNT = 1;
    private static final Integer UPDATED_PAGE_COUNT = 2;
    private static final Integer SMALLER_PAGE_COUNT = 1 - 1;

    private static final ManualStatus DEFAULT_STATUS = ManualStatus.DRAFT;
    private static final ManualStatus UPDATED_STATUS = ManualStatus.REVIEW;

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/manuals";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/manuals/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ManualRepository manualRepository;

    @Autowired
    private ManualMapper manualMapper;

    @Autowired
    private ManualSearchRepository manualSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManualMockMvc;

    private Manual manual;

    private Manual insertedManual;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manual createEntity() {
        return new Manual()
            .documentId(DEFAULT_DOCUMENT_ID)
            .title(DEFAULT_TITLE)
            .manualType(DEFAULT_MANUAL_TYPE)
            .version(DEFAULT_VERSION)
            .language(DEFAULT_LANGUAGE)
            .publicationDate(DEFAULT_PUBLICATION_DATE)
            .pageCount(DEFAULT_PAGE_COUNT)
            .status(DEFAULT_STATUS)
            .isPublic(DEFAULT_IS_PUBLIC)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Manual createUpdatedEntity() {
        return new Manual()
            .documentId(UPDATED_DOCUMENT_ID)
            .title(UPDATED_TITLE)
            .manualType(UPDATED_MANUAL_TYPE)
            .version(UPDATED_VERSION)
            .language(UPDATED_LANGUAGE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        manual = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedManual != null) {
            manualRepository.delete(insertedManual);
            manualSearchRepository.delete(insertedManual);
            insertedManual = null;
        }
    }

    @Test
    @Transactional
    void createManual() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);
        var returnedManualDTO = om.readValue(
            restManualMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ManualDTO.class
        );

        // Validate the Manual in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedManual = manualMapper.toEntity(returnedManualDTO);
        assertManualUpdatableFieldsEquals(returnedManual, getPersistedManual(returnedManual));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedManual = returnedManual;
    }

    @Test
    @Transactional
    void createManualWithExistingId() throws Exception {
        // Create the Manual with an existing ID
        manual.setId(1L);
        ManualDTO manualDTO = manualMapper.toDto(manual);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setDocumentId(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setTitle(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkManualTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setManualType(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setVersion(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setLanguage(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setStatus(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setIsPublic(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        // set the field null
        manual.setCreatedDate(null);

        // Create the Manual, which fails.
        ManualDTO manualDTO = manualMapper.toDto(manual);

        restManualMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllManuals() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList
        restManualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manual.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].manualType").value(hasItem(DEFAULT_MANUAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getManual() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get the manual
        restManualMockMvc
            .perform(get(ENTITY_API_URL_ID, manual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manual.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.manualType").value(DEFAULT_MANUAL_TYPE.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.publicationDate").value(DEFAULT_PUBLICATION_DATE.toString()))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getManualsByIdFiltering() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        Long id = manual.getId();

        defaultManualFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultManualFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultManualFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId equals to
        defaultManualFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId in
        defaultManualFiltering("documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID, "documentId.in=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId is not null
        defaultManualFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId is greater than or equal to
        defaultManualFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId is less than or equal to
        defaultManualFiltering("documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID, "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId is less than
        defaultManualFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllManualsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where documentId is greater than
        defaultManualFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllManualsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where title equals to
        defaultManualFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllManualsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where title in
        defaultManualFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllManualsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where title is not null
        defaultManualFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where title contains
        defaultManualFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllManualsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where title does not contain
        defaultManualFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllManualsByManualTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where manualType equals to
        defaultManualFiltering("manualType.equals=" + DEFAULT_MANUAL_TYPE, "manualType.equals=" + UPDATED_MANUAL_TYPE);
    }

    @Test
    @Transactional
    void getAllManualsByManualTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where manualType in
        defaultManualFiltering("manualType.in=" + DEFAULT_MANUAL_TYPE + "," + UPDATED_MANUAL_TYPE, "manualType.in=" + UPDATED_MANUAL_TYPE);
    }

    @Test
    @Transactional
    void getAllManualsByManualTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where manualType is not null
        defaultManualFiltering("manualType.specified=true", "manualType.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByVersionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where version equals to
        defaultManualFiltering("version.equals=" + DEFAULT_VERSION, "version.equals=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllManualsByVersionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where version in
        defaultManualFiltering("version.in=" + DEFAULT_VERSION + "," + UPDATED_VERSION, "version.in=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllManualsByVersionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where version is not null
        defaultManualFiltering("version.specified=true", "version.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByVersionContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where version contains
        defaultManualFiltering("version.contains=" + DEFAULT_VERSION, "version.contains=" + UPDATED_VERSION);
    }

    @Test
    @Transactional
    void getAllManualsByVersionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where version does not contain
        defaultManualFiltering("version.doesNotContain=" + UPDATED_VERSION, "version.doesNotContain=" + DEFAULT_VERSION);
    }

    @Test
    @Transactional
    void getAllManualsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where language equals to
        defaultManualFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllManualsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where language in
        defaultManualFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllManualsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where language is not null
        defaultManualFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where language contains
        defaultManualFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllManualsByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where language does not contain
        defaultManualFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate equals to
        defaultManualFiltering("publicationDate.equals=" + DEFAULT_PUBLICATION_DATE, "publicationDate.equals=" + UPDATED_PUBLICATION_DATE);
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate in
        defaultManualFiltering(
            "publicationDate.in=" + DEFAULT_PUBLICATION_DATE + "," + UPDATED_PUBLICATION_DATE,
            "publicationDate.in=" + UPDATED_PUBLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate is not null
        defaultManualFiltering("publicationDate.specified=true", "publicationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate is greater than or equal to
        defaultManualFiltering(
            "publicationDate.greaterThanOrEqual=" + DEFAULT_PUBLICATION_DATE,
            "publicationDate.greaterThanOrEqual=" + UPDATED_PUBLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate is less than or equal to
        defaultManualFiltering(
            "publicationDate.lessThanOrEqual=" + DEFAULT_PUBLICATION_DATE,
            "publicationDate.lessThanOrEqual=" + SMALLER_PUBLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate is less than
        defaultManualFiltering(
            "publicationDate.lessThan=" + UPDATED_PUBLICATION_DATE,
            "publicationDate.lessThan=" + DEFAULT_PUBLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByPublicationDateIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where publicationDate is greater than
        defaultManualFiltering(
            "publicationDate.greaterThan=" + SMALLER_PUBLICATION_DATE,
            "publicationDate.greaterThan=" + DEFAULT_PUBLICATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount equals to
        defaultManualFiltering("pageCount.equals=" + DEFAULT_PAGE_COUNT, "pageCount.equals=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount in
        defaultManualFiltering("pageCount.in=" + DEFAULT_PAGE_COUNT + "," + UPDATED_PAGE_COUNT, "pageCount.in=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount is not null
        defaultManualFiltering("pageCount.specified=true", "pageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount is greater than or equal to
        defaultManualFiltering("pageCount.greaterThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.greaterThanOrEqual=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount is less than or equal to
        defaultManualFiltering("pageCount.lessThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.lessThanOrEqual=" + SMALLER_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount is less than
        defaultManualFiltering("pageCount.lessThan=" + UPDATED_PAGE_COUNT, "pageCount.lessThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByPageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where pageCount is greater than
        defaultManualFiltering("pageCount.greaterThan=" + SMALLER_PAGE_COUNT, "pageCount.greaterThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllManualsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where status equals to
        defaultManualFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllManualsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where status in
        defaultManualFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllManualsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where status is not null
        defaultManualFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where isPublic equals to
        defaultManualFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllManualsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where isPublic in
        defaultManualFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllManualsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where isPublic is not null
        defaultManualFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllManualsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where createdDate equals to
        defaultManualFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllManualsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where createdDate in
        defaultManualFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllManualsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        // Get all the manualList where createdDate is not null
        defaultManualFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultManualFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultManualShouldBeFound(shouldBeFound);
        defaultManualShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultManualShouldBeFound(String filter) throws Exception {
        restManualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manual.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].manualType").value(hasItem(DEFAULT_MANUAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restManualMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultManualShouldNotBeFound(String filter) throws Exception {
        restManualMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restManualMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingManual() throws Exception {
        // Get the manual
        restManualMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManual() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualSearchRepository.save(manual);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());

        // Update the manual
        Manual updatedManual = manualRepository.findById(manual.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedManual are not directly saved in db
        em.detach(updatedManual);
        updatedManual
            .documentId(UPDATED_DOCUMENT_ID)
            .title(UPDATED_TITLE)
            .manualType(UPDATED_MANUAL_TYPE)
            .version(UPDATED_VERSION)
            .language(UPDATED_LANGUAGE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdDate(UPDATED_CREATED_DATE);
        ManualDTO manualDTO = manualMapper.toDto(updatedManual);

        restManualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO))
            )
            .andExpect(status().isOk());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedManualToMatchAllProperties(updatedManual);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Manual> manualSearchList = Streamable.of(manualSearchRepository.findAll()).toList();
                Manual testManualSearch = manualSearchList.get(searchDatabaseSizeAfter - 1);

                assertManualAllPropertiesEquals(testManualSearch, updatedManual);
            });
    }

    @Test
    @Transactional
    void putNonExistingManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateManualWithPatch() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manual using partial update
        Manual partialUpdatedManual = new Manual();
        partialUpdatedManual.setId(manual.getId());

        partialUpdatedManual
            .documentId(UPDATED_DOCUMENT_ID)
            .title(UPDATED_TITLE)
            .manualType(UPDATED_MANUAL_TYPE)
            .pageCount(UPDATED_PAGE_COUNT);

        restManualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManual))
            )
            .andExpect(status().isOk());

        // Validate the Manual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedManual, manual), getPersistedManual(manual));
    }

    @Test
    @Transactional
    void fullUpdateManualWithPatch() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manual using partial update
        Manual partialUpdatedManual = new Manual();
        partialUpdatedManual.setId(manual.getId());

        partialUpdatedManual
            .documentId(UPDATED_DOCUMENT_ID)
            .title(UPDATED_TITLE)
            .manualType(UPDATED_MANUAL_TYPE)
            .version(UPDATED_VERSION)
            .language(UPDATED_LANGUAGE)
            .publicationDate(UPDATED_PUBLICATION_DATE)
            .pageCount(UPDATED_PAGE_COUNT)
            .status(UPDATED_STATUS)
            .isPublic(UPDATED_IS_PUBLIC)
            .createdDate(UPDATED_CREATED_DATE);

        restManualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManual.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManual))
            )
            .andExpect(status().isOk());

        // Validate the Manual in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualUpdatableFieldsEquals(partialUpdatedManual, getPersistedManual(partialUpdatedManual));
    }

    @Test
    @Transactional
    void patchNonExistingManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManual() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        manual.setId(longCount.incrementAndGet());

        // Create the Manual
        ManualDTO manualDTO = manualMapper.toDto(manual);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(manualDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Manual in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteManual() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);
        manualRepository.save(manual);
        manualSearchRepository.save(manual);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the manual
        restManualMockMvc
            .perform(delete(ENTITY_API_URL_ID, manual.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(manualSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchManual() throws Exception {
        // Initialize the database
        insertedManual = manualRepository.saveAndFlush(manual);
        manualSearchRepository.save(manual);

        // Search the manual
        restManualMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + manual.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manual.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].manualType").value(hasItem(DEFAULT_MANUAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].publicationDate").value(hasItem(DEFAULT_PUBLICATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return manualRepository.count();
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

    protected Manual getPersistedManual(Manual manual) {
        return manualRepository.findById(manual.getId()).orElseThrow();
    }

    protected void assertPersistedManualToMatchAllProperties(Manual expectedManual) {
        assertManualAllPropertiesEquals(expectedManual, getPersistedManual(expectedManual));
    }

    protected void assertPersistedManualToMatchUpdatableProperties(Manual expectedManual) {
        assertManualAllUpdatablePropertiesEquals(expectedManual, getPersistedManual(expectedManual));
    }
}
