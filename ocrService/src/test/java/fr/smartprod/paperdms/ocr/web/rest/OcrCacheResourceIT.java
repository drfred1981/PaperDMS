package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrCacheAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrCacheSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrCacheMapper;
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
 * Integration tests for the {@link OcrCacheResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrCacheResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final OcrEngine DEFAULT_OCR_ENGINE = OcrEngine.TIKA_TESSERACT;
    private static final OcrEngine UPDATED_OCR_ENGINE = OcrEngine.TIKA_NATIVE;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_COUNT = 1;
    private static final Integer UPDATED_PAGE_COUNT = 2;
    private static final Integer SMALLER_PAGE_COUNT = 1 - 1;

    private static final Double DEFAULT_TOTAL_CONFIDENCE = 0D;
    private static final Double UPDATED_TOTAL_CONFIDENCE = 1D;
    private static final Double SMALLER_TOTAL_CONFIDENCE = 0D - 1D;

    private static final String DEFAULT_S_3_RESULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_RESULT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;
    private static final Integer SMALLER_HITS = 1 - 1;

    private static final Instant DEFAULT_LAST_ACCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ocr-caches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ocr-caches/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrCacheRepository ocrCacheRepository;

    @Autowired
    private OcrCacheMapper ocrCacheMapper;

    @Autowired
    private OcrCacheSearchRepository ocrCacheSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOcrCacheMockMvc;

    private OcrCache ocrCache;

    private OcrCache insertedOcrCache;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrCache createEntity() {
        return new OcrCache()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .ocrEngine(DEFAULT_OCR_ENGINE)
            .language(DEFAULT_LANGUAGE)
            .pageCount(DEFAULT_PAGE_COUNT)
            .totalConfidence(DEFAULT_TOTAL_CONFIDENCE)
            .s3ResultKey(DEFAULT_S_3_RESULT_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .orcExtractedTextS3Key(DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY)
            .metadata(DEFAULT_METADATA)
            .hits(DEFAULT_HITS)
            .lastAccessDate(DEFAULT_LAST_ACCESS_DATE)
            .createdDate(DEFAULT_CREATED_DATE)
            .expirationDate(DEFAULT_EXPIRATION_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrCache createUpdatedEntity() {
        return new OcrCache()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .language(UPDATED_LANGUAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .totalConfidence(UPDATED_TOTAL_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .orcExtractedTextS3Key(UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
    }

    @BeforeEach
    void initTest() {
        ocrCache = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOcrCache != null) {
            ocrCacheRepository.delete(insertedOcrCache);
            ocrCacheSearchRepository.delete(insertedOcrCache);
            insertedOcrCache = null;
        }
    }

    @Test
    @Transactional
    void createOcrCache() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);
        var returnedOcrCacheDTO = om.readValue(
            restOcrCacheMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OcrCacheDTO.class
        );

        // Validate the OcrCache in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOcrCache = ocrCacheMapper.toEntity(returnedOcrCacheDTO);
        assertOcrCacheUpdatableFieldsEquals(returnedOcrCache, getPersistedOcrCache(returnedOcrCache));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOcrCache = returnedOcrCache;
    }

    @Test
    @Transactional
    void createOcrCacheWithExistingId() throws Exception {
        // Create the OcrCache with an existing ID
        ocrCache.setId(1L);
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // set the field null
        ocrCache.setDocumentSha256(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // set the field null
        ocrCache.setPageCount(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3ResultKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // set the field null
        ocrCache.sets3ResultKey(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // set the field null
        ocrCache.sets3Bucket(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        // set the field null
        ocrCache.setCreatedDate(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOcrCaches() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].totalConfidence").value(hasItem(DEFAULT_TOTAL_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].orcExtractedTextS3Key").value(hasItem(DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    @Test
    @Transactional
    void getOcrCache() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get the ocrCache
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL_ID, ocrCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ocrCache.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.ocrEngine").value(DEFAULT_OCR_ENGINE.toString()))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.totalConfidence").value(DEFAULT_TOTAL_CONFIDENCE))
            .andExpect(jsonPath("$.s3ResultKey").value(DEFAULT_S_3_RESULT_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.orcExtractedTextS3Key").value(DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS))
            .andExpect(jsonPath("$.lastAccessDate").value(DEFAULT_LAST_ACCESS_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
    }

    @Test
    @Transactional
    void getOcrCachesByIdFiltering() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        Long id = ocrCache.getId();

        defaultOcrCacheFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOcrCacheFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOcrCacheFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOcrCachesByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where documentSha256 equals to
        defaultOcrCacheFiltering("documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256, "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256);
    }

    @Test
    @Transactional
    void getAllOcrCachesByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where documentSha256 in
        defaultOcrCacheFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where documentSha256 is not null
        defaultOcrCacheFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where documentSha256 contains
        defaultOcrCacheFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where documentSha256 does not contain
        defaultOcrCacheFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByOcrEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where ocrEngine equals to
        defaultOcrCacheFiltering("ocrEngine.equals=" + DEFAULT_OCR_ENGINE, "ocrEngine.equals=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByOcrEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where ocrEngine in
        defaultOcrCacheFiltering("ocrEngine.in=" + DEFAULT_OCR_ENGINE + "," + UPDATED_OCR_ENGINE, "ocrEngine.in=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByOcrEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where ocrEngine is not null
        defaultOcrCacheFiltering("ocrEngine.specified=true", "ocrEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where language equals to
        defaultOcrCacheFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where language in
        defaultOcrCacheFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where language is not null
        defaultOcrCacheFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where language contains
        defaultOcrCacheFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where language does not contain
        defaultOcrCacheFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount equals to
        defaultOcrCacheFiltering("pageCount.equals=" + DEFAULT_PAGE_COUNT, "pageCount.equals=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount in
        defaultOcrCacheFiltering("pageCount.in=" + DEFAULT_PAGE_COUNT + "," + UPDATED_PAGE_COUNT, "pageCount.in=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount is not null
        defaultOcrCacheFiltering("pageCount.specified=true", "pageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount is greater than or equal to
        defaultOcrCacheFiltering(
            "pageCount.greaterThanOrEqual=" + DEFAULT_PAGE_COUNT,
            "pageCount.greaterThanOrEqual=" + UPDATED_PAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount is less than or equal to
        defaultOcrCacheFiltering("pageCount.lessThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.lessThanOrEqual=" + SMALLER_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount is less than
        defaultOcrCacheFiltering("pageCount.lessThan=" + UPDATED_PAGE_COUNT, "pageCount.lessThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrCachesByPageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where pageCount is greater than
        defaultOcrCacheFiltering("pageCount.greaterThan=" + SMALLER_PAGE_COUNT, "pageCount.greaterThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence equals to
        defaultOcrCacheFiltering(
            "totalConfidence.equals=" + DEFAULT_TOTAL_CONFIDENCE,
            "totalConfidence.equals=" + UPDATED_TOTAL_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence in
        defaultOcrCacheFiltering(
            "totalConfidence.in=" + DEFAULT_TOTAL_CONFIDENCE + "," + UPDATED_TOTAL_CONFIDENCE,
            "totalConfidence.in=" + UPDATED_TOTAL_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence is not null
        defaultOcrCacheFiltering("totalConfidence.specified=true", "totalConfidence.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence is greater than or equal to
        defaultOcrCacheFiltering(
            "totalConfidence.greaterThanOrEqual=" + DEFAULT_TOTAL_CONFIDENCE,
            "totalConfidence.greaterThanOrEqual=" + (DEFAULT_TOTAL_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence is less than or equal to
        defaultOcrCacheFiltering(
            "totalConfidence.lessThanOrEqual=" + DEFAULT_TOTAL_CONFIDENCE,
            "totalConfidence.lessThanOrEqual=" + SMALLER_TOTAL_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence is less than
        defaultOcrCacheFiltering(
            "totalConfidence.lessThan=" + (DEFAULT_TOTAL_CONFIDENCE + 1),
            "totalConfidence.lessThan=" + DEFAULT_TOTAL_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByTotalConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where totalConfidence is greater than
        defaultOcrCacheFiltering(
            "totalConfidence.greaterThan=" + SMALLER_TOTAL_CONFIDENCE,
            "totalConfidence.greaterThan=" + DEFAULT_TOTAL_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3ResultKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3ResultKey equals to
        defaultOcrCacheFiltering("s3ResultKey.equals=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.equals=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3ResultKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3ResultKey in
        defaultOcrCacheFiltering(
            "s3ResultKey.in=" + DEFAULT_S_3_RESULT_KEY + "," + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.in=" + UPDATED_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3ResultKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3ResultKey is not null
        defaultOcrCacheFiltering("s3ResultKey.specified=true", "s3ResultKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3ResultKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3ResultKey contains
        defaultOcrCacheFiltering("s3ResultKey.contains=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.contains=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3ResultKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3ResultKey does not contain
        defaultOcrCacheFiltering(
            "s3ResultKey.doesNotContain=" + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.doesNotContain=" + DEFAULT_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3BucketIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3Bucket equals to
        defaultOcrCacheFiltering("s3Bucket.equals=" + DEFAULT_S_3_BUCKET, "s3Bucket.equals=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3BucketIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3Bucket in
        defaultOcrCacheFiltering("s3Bucket.in=" + DEFAULT_S_3_BUCKET + "," + UPDATED_S_3_BUCKET, "s3Bucket.in=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3BucketIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3Bucket is not null
        defaultOcrCacheFiltering("s3Bucket.specified=true", "s3Bucket.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3BucketContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3Bucket contains
        defaultOcrCacheFiltering("s3Bucket.contains=" + DEFAULT_S_3_BUCKET, "s3Bucket.contains=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrCachesBys3BucketNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where s3Bucket does not contain
        defaultOcrCacheFiltering("s3Bucket.doesNotContain=" + UPDATED_S_3_BUCKET, "s3Bucket.doesNotContain=" + DEFAULT_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrCachesByOrcExtractedTextS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where orcExtractedTextS3Key equals to
        defaultOcrCacheFiltering(
            "orcExtractedTextS3Key.equals=" + DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY,
            "orcExtractedTextS3Key.equals=" + UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByOrcExtractedTextS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where orcExtractedTextS3Key in
        defaultOcrCacheFiltering(
            "orcExtractedTextS3Key.in=" + DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY + "," + UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY,
            "orcExtractedTextS3Key.in=" + UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByOrcExtractedTextS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where orcExtractedTextS3Key is not null
        defaultOcrCacheFiltering("orcExtractedTextS3Key.specified=true", "orcExtractedTextS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByOrcExtractedTextS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where orcExtractedTextS3Key contains
        defaultOcrCacheFiltering(
            "orcExtractedTextS3Key.contains=" + DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY,
            "orcExtractedTextS3Key.contains=" + UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByOrcExtractedTextS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where orcExtractedTextS3Key does not contain
        defaultOcrCacheFiltering(
            "orcExtractedTextS3Key.doesNotContain=" + UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY,
            "orcExtractedTextS3Key.doesNotContain=" + DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits equals to
        defaultOcrCacheFiltering("hits.equals=" + DEFAULT_HITS, "hits.equals=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits in
        defaultOcrCacheFiltering("hits.in=" + DEFAULT_HITS + "," + UPDATED_HITS, "hits.in=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits is not null
        defaultOcrCacheFiltering("hits.specified=true", "hits.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits is greater than or equal to
        defaultOcrCacheFiltering("hits.greaterThanOrEqual=" + DEFAULT_HITS, "hits.greaterThanOrEqual=" + UPDATED_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits is less than or equal to
        defaultOcrCacheFiltering("hits.lessThanOrEqual=" + DEFAULT_HITS, "hits.lessThanOrEqual=" + SMALLER_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits is less than
        defaultOcrCacheFiltering("hits.lessThan=" + UPDATED_HITS, "hits.lessThan=" + DEFAULT_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByHitsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where hits is greater than
        defaultOcrCacheFiltering("hits.greaterThan=" + SMALLER_HITS, "hits.greaterThan=" + DEFAULT_HITS);
    }

    @Test
    @Transactional
    void getAllOcrCachesByLastAccessDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where lastAccessDate equals to
        defaultOcrCacheFiltering("lastAccessDate.equals=" + DEFAULT_LAST_ACCESS_DATE, "lastAccessDate.equals=" + UPDATED_LAST_ACCESS_DATE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByLastAccessDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where lastAccessDate in
        defaultOcrCacheFiltering(
            "lastAccessDate.in=" + DEFAULT_LAST_ACCESS_DATE + "," + UPDATED_LAST_ACCESS_DATE,
            "lastAccessDate.in=" + UPDATED_LAST_ACCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByLastAccessDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where lastAccessDate is not null
        defaultOcrCacheFiltering("lastAccessDate.specified=true", "lastAccessDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where createdDate equals to
        defaultOcrCacheFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where createdDate in
        defaultOcrCacheFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where createdDate is not null
        defaultOcrCacheFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrCachesByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where expirationDate equals to
        defaultOcrCacheFiltering("expirationDate.equals=" + DEFAULT_EXPIRATION_DATE, "expirationDate.equals=" + UPDATED_EXPIRATION_DATE);
    }

    @Test
    @Transactional
    void getAllOcrCachesByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where expirationDate in
        defaultOcrCacheFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrCachesByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        // Get all the ocrCacheList where expirationDate is not null
        defaultOcrCacheFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    private void defaultOcrCacheFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOcrCacheShouldBeFound(shouldBeFound);
        defaultOcrCacheShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOcrCacheShouldBeFound(String filter) throws Exception {
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].totalConfidence").value(hasItem(DEFAULT_TOTAL_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].orcExtractedTextS3Key").value(hasItem(DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));

        // Check, that the count call also returns 1
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOcrCacheShouldNotBeFound(String filter) throws Exception {
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOcrCacheMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOcrCache() throws Exception {
        // Get the ocrCache
        restOcrCacheMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOcrCache() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrCacheSearchRepository.save(ocrCache);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());

        // Update the ocrCache
        OcrCache updatedOcrCache = ocrCacheRepository.findById(ocrCache.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrCache are not directly saved in db
        em.detach(updatedOcrCache);
        updatedOcrCache
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .language(UPDATED_LANGUAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .totalConfidence(UPDATED_TOTAL_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .orcExtractedTextS3Key(UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(updatedOcrCache);

        restOcrCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrCacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrCacheDTO))
            )
            .andExpect(status().isOk());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOcrCacheToMatchAllProperties(updatedOcrCache);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OcrCache> ocrCacheSearchList = Streamable.of(ocrCacheSearchRepository.findAll()).toList();
                OcrCache testOcrCacheSearch = ocrCacheSearchList.get(searchDatabaseSizeAfter - 1);

                assertOcrCacheAllPropertiesEquals(testOcrCacheSearch, updatedOcrCache);
            });
    }

    @Test
    @Transactional
    void putNonExistingOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrCacheDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOcrCacheWithPatch() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrCache using partial update
        OcrCache partialUpdatedOcrCache = new OcrCache();
        partialUpdatedOcrCache.setId(ocrCache.getId());

        partialUpdatedOcrCache
            .pageCount(UPDATED_PAGE_COUNT)
            .totalConfidence(UPDATED_TOTAL_CONFIDENCE)
            .orcExtractedTextS3Key(UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY);

        restOcrCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrCache))
            )
            .andExpect(status().isOk());

        // Validate the OcrCache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrCacheUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedOcrCache, ocrCache), getPersistedOcrCache(ocrCache));
    }

    @Test
    @Transactional
    void fullUpdateOcrCacheWithPatch() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrCache using partial update
        OcrCache partialUpdatedOcrCache = new OcrCache();
        partialUpdatedOcrCache.setId(ocrCache.getId());

        partialUpdatedOcrCache
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .language(UPDATED_LANGUAGE)
            .pageCount(UPDATED_PAGE_COUNT)
            .totalConfidence(UPDATED_TOTAL_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .orcExtractedTextS3Key(UPDATED_ORC_EXTRACTED_TEXT_S_3_KEY)
            .metadata(UPDATED_METADATA)
            .hits(UPDATED_HITS)
            .lastAccessDate(UPDATED_LAST_ACCESS_DATE)
            .createdDate(UPDATED_CREATED_DATE)
            .expirationDate(UPDATED_EXPIRATION_DATE);

        restOcrCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrCache.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrCache))
            )
            .andExpect(status().isOk());

        // Validate the OcrCache in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrCacheUpdatableFieldsEquals(partialUpdatedOcrCache, getPersistedOcrCache(partialUpdatedOcrCache));
    }

    @Test
    @Transactional
    void patchNonExistingOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ocrCacheDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrCacheDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOcrCache() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);
        ocrCacheRepository.save(ocrCache);
        ocrCacheSearchRepository.save(ocrCache);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ocrCache
        restOcrCacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrCache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrCacheSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOcrCache() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);
        ocrCacheSearchRepository.save(ocrCache);

        // Search the ocrCache
        restOcrCacheMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ocrCache.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrCache.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].totalConfidence").value(hasItem(DEFAULT_TOTAL_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].orcExtractedTextS3Key").value(hasItem(DEFAULT_ORC_EXTRACTED_TEXT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].hits").value(hasItem(DEFAULT_HITS)))
            .andExpect(jsonPath("$.[*].lastAccessDate").value(hasItem(DEFAULT_LAST_ACCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return ocrCacheRepository.count();
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

    protected OcrCache getPersistedOcrCache(OcrCache ocrCache) {
        return ocrCacheRepository.findById(ocrCache.getId()).orElseThrow();
    }

    protected void assertPersistedOcrCacheToMatchAllProperties(OcrCache expectedOcrCache) {
        assertOcrCacheAllPropertiesEquals(expectedOcrCache, getPersistedOcrCache(expectedOcrCache));
    }

    protected void assertPersistedOcrCacheToMatchUpdatableProperties(OcrCache expectedOcrCache) {
        assertOcrCacheAllUpdatablePropertiesEquals(expectedOcrCache, getPersistedOcrCache(expectedOcrCache));
    }
}
