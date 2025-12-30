package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OrcExtractedTextAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import fr.smartprod.paperdms.ocr.repository.OrcExtractedTextRepository;
import fr.smartprod.paperdms.ocr.repository.search.OrcExtractedTextSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OrcExtractedTextDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OrcExtractedTextMapper;
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
 * Integration tests for the {@link OrcExtractedTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OrcExtractedTextResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_CONTENT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_CONTENT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_WORD_COUNT = 1;
    private static final Integer UPDATED_WORD_COUNT = 2;
    private static final Integer SMALLER_WORD_COUNT = 1 - 1;

    private static final Boolean DEFAULT_HAS_STRUCTURED_DATA = false;
    private static final Boolean UPDATED_HAS_STRUCTURED_DATA = true;

    private static final String DEFAULT_STRUCTURED_DATA = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_DATA = "BBBBBBBBBB";

    private static final String DEFAULT_STRUCTURED_DATA_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_DATA_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/orc-extracted-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/orc-extracted-texts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OrcExtractedTextRepository orcExtractedTextRepository;

    @Autowired
    private OrcExtractedTextMapper orcExtractedTextMapper;

    @Autowired
    private OrcExtractedTextSearchRepository orcExtractedTextSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOrcExtractedTextMockMvc;

    private OrcExtractedText orcExtractedText;

    private OrcExtractedText insertedOrcExtractedText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrcExtractedText createEntity() {
        return new OrcExtractedText()
            .content(DEFAULT_CONTENT)
            .contentSha256(DEFAULT_CONTENT_SHA_256)
            .s3ContentKey(DEFAULT_S_3_CONTENT_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .language(DEFAULT_LANGUAGE)
            .wordCount(DEFAULT_WORD_COUNT)
            .hasStructuredData(DEFAULT_HAS_STRUCTURED_DATA)
            .structuredData(DEFAULT_STRUCTURED_DATA)
            .structuredDataS3Key(DEFAULT_STRUCTURED_DATA_S_3_KEY)
            .extractedDate(DEFAULT_EXTRACTED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OrcExtractedText createUpdatedEntity() {
        return new OrcExtractedText()
            .content(UPDATED_CONTENT)
            .contentSha256(UPDATED_CONTENT_SHA_256)
            .s3ContentKey(UPDATED_S_3_CONTENT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .structuredDataS3Key(UPDATED_STRUCTURED_DATA_S_3_KEY)
            .extractedDate(UPDATED_EXTRACTED_DATE);
    }

    @BeforeEach
    void initTest() {
        orcExtractedText = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOrcExtractedText != null) {
            orcExtractedTextRepository.delete(insertedOrcExtractedText);
            orcExtractedTextSearchRepository.delete(insertedOrcExtractedText);
            insertedOrcExtractedText = null;
        }
    }

    @Test
    @Transactional
    void createOrcExtractedText() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);
        var returnedOrcExtractedTextDTO = om.readValue(
            restOrcExtractedTextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OrcExtractedTextDTO.class
        );

        // Validate the OrcExtractedText in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOrcExtractedText = orcExtractedTextMapper.toEntity(returnedOrcExtractedTextDTO);
        assertOrcExtractedTextUpdatableFieldsEquals(returnedOrcExtractedText, getPersistedOrcExtractedText(returnedOrcExtractedText));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOrcExtractedText = returnedOrcExtractedText;
    }

    @Test
    @Transactional
    void createOrcExtractedTextWithExistingId() throws Exception {
        // Create the OrcExtractedText with an existing ID
        orcExtractedText.setId(1L);
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOrcExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        // set the field null
        orcExtractedText.sets3Bucket(null);

        // Create the OrcExtractedText, which fails.
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        restOrcExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        // set the field null
        orcExtractedText.setPageNumber(null);

        // Create the OrcExtractedText, which fails.
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        restOrcExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExtractedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        // set the field null
        orcExtractedText.setExtractedDate(null);

        // Create the OrcExtractedText, which fails.
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        restOrcExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTexts() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcExtractedText.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].contentSha256").value(hasItem(DEFAULT_CONTENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3ContentKey").value(hasItem(DEFAULT_S_3_CONTENT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].hasStructuredData").value(hasItem(DEFAULT_HAS_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredData").value(hasItem(DEFAULT_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredDataS3Key").value(hasItem(DEFAULT_STRUCTURED_DATA_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOrcExtractedText() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get the orcExtractedText
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL_ID, orcExtractedText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(orcExtractedText.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.contentSha256").value(DEFAULT_CONTENT_SHA_256))
            .andExpect(jsonPath("$.s3ContentKey").value(DEFAULT_S_3_CONTENT_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.wordCount").value(DEFAULT_WORD_COUNT))
            .andExpect(jsonPath("$.hasStructuredData").value(DEFAULT_HAS_STRUCTURED_DATA))
            .andExpect(jsonPath("$.structuredData").value(DEFAULT_STRUCTURED_DATA))
            .andExpect(jsonPath("$.structuredDataS3Key").value(DEFAULT_STRUCTURED_DATA_S_3_KEY))
            .andExpect(jsonPath("$.extractedDate").value(DEFAULT_EXTRACTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getOrcExtractedTextsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        Long id = orcExtractedText.getId();

        defaultOrcExtractedTextFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOrcExtractedTextFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOrcExtractedTextFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByContentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where contentSha256 equals to
        defaultOrcExtractedTextFiltering(
            "contentSha256.equals=" + DEFAULT_CONTENT_SHA_256,
            "contentSha256.equals=" + UPDATED_CONTENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByContentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where contentSha256 in
        defaultOrcExtractedTextFiltering(
            "contentSha256.in=" + DEFAULT_CONTENT_SHA_256 + "," + UPDATED_CONTENT_SHA_256,
            "contentSha256.in=" + UPDATED_CONTENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByContentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where contentSha256 is not null
        defaultOrcExtractedTextFiltering("contentSha256.specified=true", "contentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByContentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where contentSha256 contains
        defaultOrcExtractedTextFiltering(
            "contentSha256.contains=" + DEFAULT_CONTENT_SHA_256,
            "contentSha256.contains=" + UPDATED_CONTENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByContentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where contentSha256 does not contain
        defaultOrcExtractedTextFiltering(
            "contentSha256.doesNotContain=" + UPDATED_CONTENT_SHA_256,
            "contentSha256.doesNotContain=" + DEFAULT_CONTENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3ContentKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3ContentKey equals to
        defaultOrcExtractedTextFiltering(
            "s3ContentKey.equals=" + DEFAULT_S_3_CONTENT_KEY,
            "s3ContentKey.equals=" + UPDATED_S_3_CONTENT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3ContentKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3ContentKey in
        defaultOrcExtractedTextFiltering(
            "s3ContentKey.in=" + DEFAULT_S_3_CONTENT_KEY + "," + UPDATED_S_3_CONTENT_KEY,
            "s3ContentKey.in=" + UPDATED_S_3_CONTENT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3ContentKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3ContentKey is not null
        defaultOrcExtractedTextFiltering("s3ContentKey.specified=true", "s3ContentKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3ContentKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3ContentKey contains
        defaultOrcExtractedTextFiltering(
            "s3ContentKey.contains=" + DEFAULT_S_3_CONTENT_KEY,
            "s3ContentKey.contains=" + UPDATED_S_3_CONTENT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3ContentKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3ContentKey does not contain
        defaultOrcExtractedTextFiltering(
            "s3ContentKey.doesNotContain=" + UPDATED_S_3_CONTENT_KEY,
            "s3ContentKey.doesNotContain=" + DEFAULT_S_3_CONTENT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3BucketIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3Bucket equals to
        defaultOrcExtractedTextFiltering("s3Bucket.equals=" + DEFAULT_S_3_BUCKET, "s3Bucket.equals=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3BucketIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3Bucket in
        defaultOrcExtractedTextFiltering(
            "s3Bucket.in=" + DEFAULT_S_3_BUCKET + "," + UPDATED_S_3_BUCKET,
            "s3Bucket.in=" + UPDATED_S_3_BUCKET
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3BucketIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3Bucket is not null
        defaultOrcExtractedTextFiltering("s3Bucket.specified=true", "s3Bucket.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3BucketContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3Bucket contains
        defaultOrcExtractedTextFiltering("s3Bucket.contains=" + DEFAULT_S_3_BUCKET, "s3Bucket.contains=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsBys3BucketNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where s3Bucket does not contain
        defaultOrcExtractedTextFiltering("s3Bucket.doesNotContain=" + UPDATED_S_3_BUCKET, "s3Bucket.doesNotContain=" + DEFAULT_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber equals to
        defaultOrcExtractedTextFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber in
        defaultOrcExtractedTextFiltering(
            "pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER,
            "pageNumber.in=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber is not null
        defaultOrcExtractedTextFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber is greater than or equal to
        defaultOrcExtractedTextFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber is less than or equal to
        defaultOrcExtractedTextFiltering(
            "pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber is less than
        defaultOrcExtractedTextFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where pageNumber is greater than
        defaultOrcExtractedTextFiltering("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER, "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where language equals to
        defaultOrcExtractedTextFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where language in
        defaultOrcExtractedTextFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where language is not null
        defaultOrcExtractedTextFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where language contains
        defaultOrcExtractedTextFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where language does not contain
        defaultOrcExtractedTextFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount equals to
        defaultOrcExtractedTextFiltering("wordCount.equals=" + DEFAULT_WORD_COUNT, "wordCount.equals=" + UPDATED_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount in
        defaultOrcExtractedTextFiltering(
            "wordCount.in=" + DEFAULT_WORD_COUNT + "," + UPDATED_WORD_COUNT,
            "wordCount.in=" + UPDATED_WORD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount is not null
        defaultOrcExtractedTextFiltering("wordCount.specified=true", "wordCount.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount is greater than or equal to
        defaultOrcExtractedTextFiltering(
            "wordCount.greaterThanOrEqual=" + DEFAULT_WORD_COUNT,
            "wordCount.greaterThanOrEqual=" + UPDATED_WORD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount is less than or equal to
        defaultOrcExtractedTextFiltering(
            "wordCount.lessThanOrEqual=" + DEFAULT_WORD_COUNT,
            "wordCount.lessThanOrEqual=" + SMALLER_WORD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount is less than
        defaultOrcExtractedTextFiltering("wordCount.lessThan=" + UPDATED_WORD_COUNT, "wordCount.lessThan=" + DEFAULT_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByWordCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where wordCount is greater than
        defaultOrcExtractedTextFiltering("wordCount.greaterThan=" + SMALLER_WORD_COUNT, "wordCount.greaterThan=" + DEFAULT_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByHasStructuredDataIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where hasStructuredData equals to
        defaultOrcExtractedTextFiltering(
            "hasStructuredData.equals=" + DEFAULT_HAS_STRUCTURED_DATA,
            "hasStructuredData.equals=" + UPDATED_HAS_STRUCTURED_DATA
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByHasStructuredDataIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where hasStructuredData in
        defaultOrcExtractedTextFiltering(
            "hasStructuredData.in=" + DEFAULT_HAS_STRUCTURED_DATA + "," + UPDATED_HAS_STRUCTURED_DATA,
            "hasStructuredData.in=" + UPDATED_HAS_STRUCTURED_DATA
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByHasStructuredDataIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where hasStructuredData is not null
        defaultOrcExtractedTextFiltering("hasStructuredData.specified=true", "hasStructuredData.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByStructuredDataS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where structuredDataS3Key equals to
        defaultOrcExtractedTextFiltering(
            "structuredDataS3Key.equals=" + DEFAULT_STRUCTURED_DATA_S_3_KEY,
            "structuredDataS3Key.equals=" + UPDATED_STRUCTURED_DATA_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByStructuredDataS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where structuredDataS3Key in
        defaultOrcExtractedTextFiltering(
            "structuredDataS3Key.in=" + DEFAULT_STRUCTURED_DATA_S_3_KEY + "," + UPDATED_STRUCTURED_DATA_S_3_KEY,
            "structuredDataS3Key.in=" + UPDATED_STRUCTURED_DATA_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByStructuredDataS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where structuredDataS3Key is not null
        defaultOrcExtractedTextFiltering("structuredDataS3Key.specified=true", "structuredDataS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByStructuredDataS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where structuredDataS3Key contains
        defaultOrcExtractedTextFiltering(
            "structuredDataS3Key.contains=" + DEFAULT_STRUCTURED_DATA_S_3_KEY,
            "structuredDataS3Key.contains=" + UPDATED_STRUCTURED_DATA_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByStructuredDataS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where structuredDataS3Key does not contain
        defaultOrcExtractedTextFiltering(
            "structuredDataS3Key.doesNotContain=" + UPDATED_STRUCTURED_DATA_S_3_KEY,
            "structuredDataS3Key.doesNotContain=" + DEFAULT_STRUCTURED_DATA_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByExtractedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where extractedDate equals to
        defaultOrcExtractedTextFiltering(
            "extractedDate.equals=" + DEFAULT_EXTRACTED_DATE,
            "extractedDate.equals=" + UPDATED_EXTRACTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByExtractedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where extractedDate in
        defaultOrcExtractedTextFiltering(
            "extractedDate.in=" + DEFAULT_EXTRACTED_DATE + "," + UPDATED_EXTRACTED_DATE,
            "extractedDate.in=" + UPDATED_EXTRACTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByExtractedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        // Get all the orcExtractedTextList where extractedDate is not null
        defaultOrcExtractedTextFiltering("extractedDate.specified=true", "extractedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOrcExtractedTextsByJobIsEqualToSomething() throws Exception {
        OcrJob job;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            orcExtractedTextRepository.saveAndFlush(orcExtractedText);
            job = OcrJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        orcExtractedText.setJob(job);
        orcExtractedTextRepository.saveAndFlush(orcExtractedText);
        Long jobId = job.getId();
        // Get all the orcExtractedTextList where job equals to jobId
        defaultOrcExtractedTextShouldBeFound("jobId.equals=" + jobId);

        // Get all the orcExtractedTextList where job equals to (jobId + 1)
        defaultOrcExtractedTextShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultOrcExtractedTextFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOrcExtractedTextShouldBeFound(shouldBeFound);
        defaultOrcExtractedTextShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOrcExtractedTextShouldBeFound(String filter) throws Exception {
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcExtractedText.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].contentSha256").value(hasItem(DEFAULT_CONTENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3ContentKey").value(hasItem(DEFAULT_S_3_CONTENT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].hasStructuredData").value(hasItem(DEFAULT_HAS_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredData").value(hasItem(DEFAULT_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredDataS3Key").value(hasItem(DEFAULT_STRUCTURED_DATA_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));

        // Check, that the count call also returns 1
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOrcExtractedTextShouldNotBeFound(String filter) throws Exception {
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOrcExtractedText() throws Exception {
        // Get the orcExtractedText
        restOrcExtractedTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOrcExtractedText() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        orcExtractedTextSearchRepository.save(orcExtractedText);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());

        // Update the orcExtractedText
        OrcExtractedText updatedOrcExtractedText = orcExtractedTextRepository.findById(orcExtractedText.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOrcExtractedText are not directly saved in db
        em.detach(updatedOrcExtractedText);
        updatedOrcExtractedText
            .content(UPDATED_CONTENT)
            .contentSha256(UPDATED_CONTENT_SHA_256)
            .s3ContentKey(UPDATED_S_3_CONTENT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .structuredDataS3Key(UPDATED_STRUCTURED_DATA_S_3_KEY)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(updatedOrcExtractedText);

        restOrcExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcExtractedTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orcExtractedTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOrcExtractedTextToMatchAllProperties(updatedOrcExtractedText);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OrcExtractedText> orcExtractedTextSearchList = Streamable.of(orcExtractedTextSearchRepository.findAll()).toList();
                OrcExtractedText testOrcExtractedTextSearch = orcExtractedTextSearchList.get(searchDatabaseSizeAfter - 1);

                assertOrcExtractedTextAllPropertiesEquals(testOrcExtractedTextSearch, updatedOrcExtractedText);
            });
    }

    @Test
    @Transactional
    void putNonExistingOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, orcExtractedTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orcExtractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(orcExtractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOrcExtractedTextWithPatch() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orcExtractedText using partial update
        OrcExtractedText partialUpdatedOrcExtractedText = new OrcExtractedText();
        partialUpdatedOrcExtractedText.setId(orcExtractedText.getId());

        partialUpdatedOrcExtractedText
            .contentSha256(UPDATED_CONTENT_SHA_256)
            .s3ContentKey(UPDATED_S_3_CONTENT_KEY)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restOrcExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrcExtractedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrcExtractedText))
            )
            .andExpect(status().isOk());

        // Validate the OrcExtractedText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrcExtractedTextUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOrcExtractedText, orcExtractedText),
            getPersistedOrcExtractedText(orcExtractedText)
        );
    }

    @Test
    @Transactional
    void fullUpdateOrcExtractedTextWithPatch() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the orcExtractedText using partial update
        OrcExtractedText partialUpdatedOrcExtractedText = new OrcExtractedText();
        partialUpdatedOrcExtractedText.setId(orcExtractedText.getId());

        partialUpdatedOrcExtractedText
            .content(UPDATED_CONTENT)
            .contentSha256(UPDATED_CONTENT_SHA_256)
            .s3ContentKey(UPDATED_S_3_CONTENT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .structuredDataS3Key(UPDATED_STRUCTURED_DATA_S_3_KEY)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restOrcExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOrcExtractedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOrcExtractedText))
            )
            .andExpect(status().isOk());

        // Validate the OrcExtractedText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOrcExtractedTextUpdatableFieldsEquals(
            partialUpdatedOrcExtractedText,
            getPersistedOrcExtractedText(partialUpdatedOrcExtractedText)
        );
    }

    @Test
    @Transactional
    void patchNonExistingOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, orcExtractedTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orcExtractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(orcExtractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOrcExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        orcExtractedText.setId(longCount.incrementAndGet());

        // Create the OrcExtractedText
        OrcExtractedTextDTO orcExtractedTextDTO = orcExtractedTextMapper.toDto(orcExtractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOrcExtractedTextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(orcExtractedTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OrcExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOrcExtractedText() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);
        orcExtractedTextRepository.save(orcExtractedText);
        orcExtractedTextSearchRepository.save(orcExtractedText);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the orcExtractedText
        restOrcExtractedTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, orcExtractedText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(orcExtractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOrcExtractedText() throws Exception {
        // Initialize the database
        insertedOrcExtractedText = orcExtractedTextRepository.saveAndFlush(orcExtractedText);
        orcExtractedTextSearchRepository.save(orcExtractedText);

        // Search the orcExtractedText
        restOrcExtractedTextMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + orcExtractedText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(orcExtractedText.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].contentSha256").value(hasItem(DEFAULT_CONTENT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3ContentKey").value(hasItem(DEFAULT_S_3_CONTENT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].hasStructuredData").value(hasItem(DEFAULT_HAS_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredData").value(hasItem(DEFAULT_STRUCTURED_DATA.toString())))
            .andExpect(jsonPath("$.[*].structuredDataS3Key").value(hasItem(DEFAULT_STRUCTURED_DATA_S_3_KEY)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return orcExtractedTextRepository.count();
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

    protected OrcExtractedText getPersistedOrcExtractedText(OrcExtractedText orcExtractedText) {
        return orcExtractedTextRepository.findById(orcExtractedText.getId()).orElseThrow();
    }

    protected void assertPersistedOrcExtractedTextToMatchAllProperties(OrcExtractedText expectedOrcExtractedText) {
        assertOrcExtractedTextAllPropertiesEquals(expectedOrcExtractedText, getPersistedOrcExtractedText(expectedOrcExtractedText));
    }

    protected void assertPersistedOrcExtractedTextToMatchUpdatableProperties(OrcExtractedText expectedOrcExtractedText) {
        assertOrcExtractedTextAllUpdatablePropertiesEquals(
            expectedOrcExtractedText,
            getPersistedOrcExtractedText(expectedOrcExtractedText)
        );
    }
}
