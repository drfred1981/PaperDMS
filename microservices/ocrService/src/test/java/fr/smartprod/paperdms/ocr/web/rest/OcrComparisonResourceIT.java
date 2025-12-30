package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrComparisonAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrComparisonSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrComparisonMapper;
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
 * Integration tests for the {@link OcrComparisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrComparisonResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_TIKA_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TIKA_TEXT = "BBBBBBBBBB";

    private static final Double DEFAULT_TIKA_CONFIDENCE = 0D;
    private static final Double UPDATED_TIKA_CONFIDENCE = 1D;
    private static final Double SMALLER_TIKA_CONFIDENCE = 0D - 1D;

    private static final String DEFAULT_AI_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_AI_TEXT = "BBBBBBBBBB";

    private static final Double DEFAULT_AI_CONFIDENCE = 0D;
    private static final Double UPDATED_AI_CONFIDENCE = 1D;
    private static final Double SMALLER_AI_CONFIDENCE = 0D - 1D;

    private static final Double DEFAULT_SIMILARITY = 0D;
    private static final Double UPDATED_SIMILARITY = 1D;
    private static final Double SMALLER_SIMILARITY = 0D - 1D;

    private static final String DEFAULT_DIFFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_DIFFERENCES = "BBBBBBBBBB";

    private static final String DEFAULT_DIFFERENCES_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DIFFERENCES_S_3_KEY = "BBBBBBBBBB";

    private static final OcrEngine DEFAULT_SELECTED_ENGINE = OcrEngine.TIKA_TESSERACT;
    private static final OcrEngine UPDATED_SELECTED_ENGINE = OcrEngine.TIKA_NATIVE;

    private static final String DEFAULT_SELECTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_SELECTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_SELECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SELECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPARISON_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPARISON_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/ocr-comparisons";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ocr-comparisons/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrComparisonRepository ocrComparisonRepository;

    @Autowired
    private OcrComparisonMapper ocrComparisonMapper;

    @Autowired
    private OcrComparisonSearchRepository ocrComparisonSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOcrComparisonMockMvc;

    private OcrComparison ocrComparison;

    private OcrComparison insertedOcrComparison;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrComparison createEntity() {
        return new OcrComparison()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .tikaText(DEFAULT_TIKA_TEXT)
            .tikaConfidence(DEFAULT_TIKA_CONFIDENCE)
            .aiText(DEFAULT_AI_TEXT)
            .aiConfidence(DEFAULT_AI_CONFIDENCE)
            .similarity(DEFAULT_SIMILARITY)
            .differences(DEFAULT_DIFFERENCES)
            .differencesS3Key(DEFAULT_DIFFERENCES_S_3_KEY)
            .selectedEngine(DEFAULT_SELECTED_ENGINE)
            .selectedBy(DEFAULT_SELECTED_BY)
            .selectedDate(DEFAULT_SELECTED_DATE)
            .comparisonDate(DEFAULT_COMPARISON_DATE)
            .metadata(DEFAULT_METADATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrComparison createUpdatedEntity() {
        return new OcrComparison()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .tikaText(UPDATED_TIKA_TEXT)
            .tikaConfidence(UPDATED_TIKA_CONFIDENCE)
            .aiText(UPDATED_AI_TEXT)
            .aiConfidence(UPDATED_AI_CONFIDENCE)
            .similarity(UPDATED_SIMILARITY)
            .differences(UPDATED_DIFFERENCES)
            .differencesS3Key(UPDATED_DIFFERENCES_S_3_KEY)
            .selectedEngine(UPDATED_SELECTED_ENGINE)
            .selectedBy(UPDATED_SELECTED_BY)
            .selectedDate(UPDATED_SELECTED_DATE)
            .comparisonDate(UPDATED_COMPARISON_DATE)
            .metadata(UPDATED_METADATA);
    }

    @BeforeEach
    void initTest() {
        ocrComparison = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOcrComparison != null) {
            ocrComparisonRepository.delete(insertedOcrComparison);
            ocrComparisonSearchRepository.delete(insertedOcrComparison);
            insertedOcrComparison = null;
        }
    }

    @Test
    @Transactional
    void createOcrComparison() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);
        var returnedOcrComparisonDTO = om.readValue(
            restOcrComparisonMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OcrComparisonDTO.class
        );

        // Validate the OcrComparison in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOcrComparison = ocrComparisonMapper.toEntity(returnedOcrComparisonDTO);
        assertOcrComparisonUpdatableFieldsEquals(returnedOcrComparison, getPersistedOcrComparison(returnedOcrComparison));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOcrComparison = returnedOcrComparison;
    }

    @Test
    @Transactional
    void createOcrComparisonWithExistingId() throws Exception {
        // Create the OcrComparison with an existing ID
        ocrComparison.setId(1L);
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        // set the field null
        ocrComparison.setDocumentSha256(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        // set the field null
        ocrComparison.setPageNumber(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkComparisonDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        // set the field null
        ocrComparison.setComparisonDate(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOcrComparisons() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].tikaText").value(hasItem(DEFAULT_TIKA_TEXT)))
            .andExpect(jsonPath("$.[*].tikaConfidence").value(hasItem(DEFAULT_TIKA_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].aiText").value(hasItem(DEFAULT_AI_TEXT)))
            .andExpect(jsonPath("$.[*].aiConfidence").value(hasItem(DEFAULT_AI_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY)))
            .andExpect(jsonPath("$.[*].differences").value(hasItem(DEFAULT_DIFFERENCES)))
            .andExpect(jsonPath("$.[*].differencesS3Key").value(hasItem(DEFAULT_DIFFERENCES_S_3_KEY)))
            .andExpect(jsonPath("$.[*].selectedEngine").value(hasItem(DEFAULT_SELECTED_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].selectedBy").value(hasItem(DEFAULT_SELECTED_BY)))
            .andExpect(jsonPath("$.[*].selectedDate").value(hasItem(DEFAULT_SELECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comparisonDate").value(hasItem(DEFAULT_COMPARISON_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @Test
    @Transactional
    void getOcrComparison() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get the ocrComparison
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL_ID, ocrComparison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ocrComparison.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.tikaText").value(DEFAULT_TIKA_TEXT))
            .andExpect(jsonPath("$.tikaConfidence").value(DEFAULT_TIKA_CONFIDENCE))
            .andExpect(jsonPath("$.aiText").value(DEFAULT_AI_TEXT))
            .andExpect(jsonPath("$.aiConfidence").value(DEFAULT_AI_CONFIDENCE))
            .andExpect(jsonPath("$.similarity").value(DEFAULT_SIMILARITY))
            .andExpect(jsonPath("$.differences").value(DEFAULT_DIFFERENCES))
            .andExpect(jsonPath("$.differencesS3Key").value(DEFAULT_DIFFERENCES_S_3_KEY))
            .andExpect(jsonPath("$.selectedEngine").value(DEFAULT_SELECTED_ENGINE.toString()))
            .andExpect(jsonPath("$.selectedBy").value(DEFAULT_SELECTED_BY))
            .andExpect(jsonPath("$.selectedDate").value(DEFAULT_SELECTED_DATE.toString()))
            .andExpect(jsonPath("$.comparisonDate").value(DEFAULT_COMPARISON_DATE.toString()))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getOcrComparisonsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        Long id = ocrComparison.getId();

        defaultOcrComparisonFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOcrComparisonFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOcrComparisonFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where documentSha256 equals to
        defaultOcrComparisonFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where documentSha256 in
        defaultOcrComparisonFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where documentSha256 is not null
        defaultOcrComparisonFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where documentSha256 contains
        defaultOcrComparisonFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where documentSha256 does not contain
        defaultOcrComparisonFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber equals to
        defaultOcrComparisonFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber in
        defaultOcrComparisonFiltering(
            "pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER,
            "pageNumber.in=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber is not null
        defaultOcrComparisonFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber is greater than or equal to
        defaultOcrComparisonFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber is less than or equal to
        defaultOcrComparisonFiltering(
            "pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber is less than
        defaultOcrComparisonFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where pageNumber is greater than
        defaultOcrComparisonFiltering("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER, "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence equals to
        defaultOcrComparisonFiltering(
            "tikaConfidence.equals=" + DEFAULT_TIKA_CONFIDENCE,
            "tikaConfidence.equals=" + UPDATED_TIKA_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence in
        defaultOcrComparisonFiltering(
            "tikaConfidence.in=" + DEFAULT_TIKA_CONFIDENCE + "," + UPDATED_TIKA_CONFIDENCE,
            "tikaConfidence.in=" + UPDATED_TIKA_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence is not null
        defaultOcrComparisonFiltering("tikaConfidence.specified=true", "tikaConfidence.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence is greater than or equal to
        defaultOcrComparisonFiltering(
            "tikaConfidence.greaterThanOrEqual=" + DEFAULT_TIKA_CONFIDENCE,
            "tikaConfidence.greaterThanOrEqual=" + (DEFAULT_TIKA_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence is less than or equal to
        defaultOcrComparisonFiltering(
            "tikaConfidence.lessThanOrEqual=" + DEFAULT_TIKA_CONFIDENCE,
            "tikaConfidence.lessThanOrEqual=" + SMALLER_TIKA_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence is less than
        defaultOcrComparisonFiltering(
            "tikaConfidence.lessThan=" + (DEFAULT_TIKA_CONFIDENCE + 1),
            "tikaConfidence.lessThan=" + DEFAULT_TIKA_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByTikaConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where tikaConfidence is greater than
        defaultOcrComparisonFiltering(
            "tikaConfidence.greaterThan=" + SMALLER_TIKA_CONFIDENCE,
            "tikaConfidence.greaterThan=" + DEFAULT_TIKA_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence equals to
        defaultOcrComparisonFiltering("aiConfidence.equals=" + DEFAULT_AI_CONFIDENCE, "aiConfidence.equals=" + UPDATED_AI_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence in
        defaultOcrComparisonFiltering(
            "aiConfidence.in=" + DEFAULT_AI_CONFIDENCE + "," + UPDATED_AI_CONFIDENCE,
            "aiConfidence.in=" + UPDATED_AI_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence is not null
        defaultOcrComparisonFiltering("aiConfidence.specified=true", "aiConfidence.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence is greater than or equal to
        defaultOcrComparisonFiltering(
            "aiConfidence.greaterThanOrEqual=" + DEFAULT_AI_CONFIDENCE,
            "aiConfidence.greaterThanOrEqual=" + (DEFAULT_AI_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence is less than or equal to
        defaultOcrComparisonFiltering(
            "aiConfidence.lessThanOrEqual=" + DEFAULT_AI_CONFIDENCE,
            "aiConfidence.lessThanOrEqual=" + SMALLER_AI_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence is less than
        defaultOcrComparisonFiltering(
            "aiConfidence.lessThan=" + (DEFAULT_AI_CONFIDENCE + 1),
            "aiConfidence.lessThan=" + DEFAULT_AI_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByAiConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where aiConfidence is greater than
        defaultOcrComparisonFiltering(
            "aiConfidence.greaterThan=" + SMALLER_AI_CONFIDENCE,
            "aiConfidence.greaterThan=" + DEFAULT_AI_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity equals to
        defaultOcrComparisonFiltering("similarity.equals=" + DEFAULT_SIMILARITY, "similarity.equals=" + UPDATED_SIMILARITY);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity in
        defaultOcrComparisonFiltering(
            "similarity.in=" + DEFAULT_SIMILARITY + "," + UPDATED_SIMILARITY,
            "similarity.in=" + UPDATED_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity is not null
        defaultOcrComparisonFiltering("similarity.specified=true", "similarity.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity is greater than or equal to
        defaultOcrComparisonFiltering(
            "similarity.greaterThanOrEqual=" + DEFAULT_SIMILARITY,
            "similarity.greaterThanOrEqual=" + (DEFAULT_SIMILARITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity is less than or equal to
        defaultOcrComparisonFiltering(
            "similarity.lessThanOrEqual=" + DEFAULT_SIMILARITY,
            "similarity.lessThanOrEqual=" + SMALLER_SIMILARITY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity is less than
        defaultOcrComparisonFiltering("similarity.lessThan=" + (DEFAULT_SIMILARITY + 1), "similarity.lessThan=" + DEFAULT_SIMILARITY);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySimilarityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where similarity is greater than
        defaultOcrComparisonFiltering("similarity.greaterThan=" + SMALLER_SIMILARITY, "similarity.greaterThan=" + DEFAULT_SIMILARITY);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDifferencesS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where differencesS3Key equals to
        defaultOcrComparisonFiltering(
            "differencesS3Key.equals=" + DEFAULT_DIFFERENCES_S_3_KEY,
            "differencesS3Key.equals=" + UPDATED_DIFFERENCES_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDifferencesS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where differencesS3Key in
        defaultOcrComparisonFiltering(
            "differencesS3Key.in=" + DEFAULT_DIFFERENCES_S_3_KEY + "," + UPDATED_DIFFERENCES_S_3_KEY,
            "differencesS3Key.in=" + UPDATED_DIFFERENCES_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDifferencesS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where differencesS3Key is not null
        defaultOcrComparisonFiltering("differencesS3Key.specified=true", "differencesS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDifferencesS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where differencesS3Key contains
        defaultOcrComparisonFiltering(
            "differencesS3Key.contains=" + DEFAULT_DIFFERENCES_S_3_KEY,
            "differencesS3Key.contains=" + UPDATED_DIFFERENCES_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByDifferencesS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where differencesS3Key does not contain
        defaultOcrComparisonFiltering(
            "differencesS3Key.doesNotContain=" + UPDATED_DIFFERENCES_S_3_KEY,
            "differencesS3Key.doesNotContain=" + DEFAULT_DIFFERENCES_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedEngine equals to
        defaultOcrComparisonFiltering(
            "selectedEngine.equals=" + DEFAULT_SELECTED_ENGINE,
            "selectedEngine.equals=" + UPDATED_SELECTED_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedEngine in
        defaultOcrComparisonFiltering(
            "selectedEngine.in=" + DEFAULT_SELECTED_ENGINE + "," + UPDATED_SELECTED_ENGINE,
            "selectedEngine.in=" + UPDATED_SELECTED_ENGINE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedEngine is not null
        defaultOcrComparisonFiltering("selectedEngine.specified=true", "selectedEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedBy equals to
        defaultOcrComparisonFiltering("selectedBy.equals=" + DEFAULT_SELECTED_BY, "selectedBy.equals=" + UPDATED_SELECTED_BY);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedBy in
        defaultOcrComparisonFiltering(
            "selectedBy.in=" + DEFAULT_SELECTED_BY + "," + UPDATED_SELECTED_BY,
            "selectedBy.in=" + UPDATED_SELECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedBy is not null
        defaultOcrComparisonFiltering("selectedBy.specified=true", "selectedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedByContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedBy contains
        defaultOcrComparisonFiltering("selectedBy.contains=" + DEFAULT_SELECTED_BY, "selectedBy.contains=" + UPDATED_SELECTED_BY);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedBy does not contain
        defaultOcrComparisonFiltering(
            "selectedBy.doesNotContain=" + UPDATED_SELECTED_BY,
            "selectedBy.doesNotContain=" + DEFAULT_SELECTED_BY
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedDate equals to
        defaultOcrComparisonFiltering("selectedDate.equals=" + DEFAULT_SELECTED_DATE, "selectedDate.equals=" + UPDATED_SELECTED_DATE);
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedDate in
        defaultOcrComparisonFiltering(
            "selectedDate.in=" + DEFAULT_SELECTED_DATE + "," + UPDATED_SELECTED_DATE,
            "selectedDate.in=" + UPDATED_SELECTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsBySelectedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where selectedDate is not null
        defaultOcrComparisonFiltering("selectedDate.specified=true", "selectedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByComparisonDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where comparisonDate equals to
        defaultOcrComparisonFiltering(
            "comparisonDate.equals=" + DEFAULT_COMPARISON_DATE,
            "comparisonDate.equals=" + UPDATED_COMPARISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByComparisonDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where comparisonDate in
        defaultOcrComparisonFiltering(
            "comparisonDate.in=" + DEFAULT_COMPARISON_DATE + "," + UPDATED_COMPARISON_DATE,
            "comparisonDate.in=" + UPDATED_COMPARISON_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrComparisonsByComparisonDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        // Get all the ocrComparisonList where comparisonDate is not null
        defaultOcrComparisonFiltering("comparisonDate.specified=true", "comparisonDate.specified=false");
    }

    private void defaultOcrComparisonFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOcrComparisonShouldBeFound(shouldBeFound);
        defaultOcrComparisonShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOcrComparisonShouldBeFound(String filter) throws Exception {
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].tikaText").value(hasItem(DEFAULT_TIKA_TEXT)))
            .andExpect(jsonPath("$.[*].tikaConfidence").value(hasItem(DEFAULT_TIKA_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].aiText").value(hasItem(DEFAULT_AI_TEXT)))
            .andExpect(jsonPath("$.[*].aiConfidence").value(hasItem(DEFAULT_AI_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY)))
            .andExpect(jsonPath("$.[*].differences").value(hasItem(DEFAULT_DIFFERENCES)))
            .andExpect(jsonPath("$.[*].differencesS3Key").value(hasItem(DEFAULT_DIFFERENCES_S_3_KEY)))
            .andExpect(jsonPath("$.[*].selectedEngine").value(hasItem(DEFAULT_SELECTED_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].selectedBy").value(hasItem(DEFAULT_SELECTED_BY)))
            .andExpect(jsonPath("$.[*].selectedDate").value(hasItem(DEFAULT_SELECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comparisonDate").value(hasItem(DEFAULT_COMPARISON_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOcrComparisonShouldNotBeFound(String filter) throws Exception {
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOcrComparisonMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOcrComparison() throws Exception {
        // Get the ocrComparison
        restOcrComparisonMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOcrComparison() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrComparisonSearchRepository.save(ocrComparison);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());

        // Update the ocrComparison
        OcrComparison updatedOcrComparison = ocrComparisonRepository.findById(ocrComparison.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrComparison are not directly saved in db
        em.detach(updatedOcrComparison);
        updatedOcrComparison
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .tikaText(UPDATED_TIKA_TEXT)
            .tikaConfidence(UPDATED_TIKA_CONFIDENCE)
            .aiText(UPDATED_AI_TEXT)
            .aiConfidence(UPDATED_AI_CONFIDENCE)
            .similarity(UPDATED_SIMILARITY)
            .differences(UPDATED_DIFFERENCES)
            .differencesS3Key(UPDATED_DIFFERENCES_S_3_KEY)
            .selectedEngine(UPDATED_SELECTED_ENGINE)
            .selectedBy(UPDATED_SELECTED_BY)
            .selectedDate(UPDATED_SELECTED_DATE)
            .comparisonDate(UPDATED_COMPARISON_DATE)
            .metadata(UPDATED_METADATA);
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(updatedOcrComparison);

        restOcrComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrComparisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrComparisonDTO))
            )
            .andExpect(status().isOk());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOcrComparisonToMatchAllProperties(updatedOcrComparison);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OcrComparison> ocrComparisonSearchList = Streamable.of(ocrComparisonSearchRepository.findAll()).toList();
                OcrComparison testOcrComparisonSearch = ocrComparisonSearchList.get(searchDatabaseSizeAfter - 1);

                assertOcrComparisonAllPropertiesEquals(testOcrComparisonSearch, updatedOcrComparison);
            });
    }

    @Test
    @Transactional
    void putNonExistingOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrComparisonDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOcrComparisonWithPatch() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrComparison using partial update
        OcrComparison partialUpdatedOcrComparison = new OcrComparison();
        partialUpdatedOcrComparison.setId(ocrComparison.getId());

        partialUpdatedOcrComparison
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .tikaConfidence(UPDATED_TIKA_CONFIDENCE)
            .aiText(UPDATED_AI_TEXT)
            .comparisonDate(UPDATED_COMPARISON_DATE)
            .metadata(UPDATED_METADATA);

        restOcrComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrComparison.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrComparison))
            )
            .andExpect(status().isOk());

        // Validate the OcrComparison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrComparisonUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOcrComparison, ocrComparison),
            getPersistedOcrComparison(ocrComparison)
        );
    }

    @Test
    @Transactional
    void fullUpdateOcrComparisonWithPatch() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrComparison using partial update
        OcrComparison partialUpdatedOcrComparison = new OcrComparison();
        partialUpdatedOcrComparison.setId(ocrComparison.getId());

        partialUpdatedOcrComparison
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .tikaText(UPDATED_TIKA_TEXT)
            .tikaConfidence(UPDATED_TIKA_CONFIDENCE)
            .aiText(UPDATED_AI_TEXT)
            .aiConfidence(UPDATED_AI_CONFIDENCE)
            .similarity(UPDATED_SIMILARITY)
            .differences(UPDATED_DIFFERENCES)
            .differencesS3Key(UPDATED_DIFFERENCES_S_3_KEY)
            .selectedEngine(UPDATED_SELECTED_ENGINE)
            .selectedBy(UPDATED_SELECTED_BY)
            .selectedDate(UPDATED_SELECTED_DATE)
            .comparisonDate(UPDATED_COMPARISON_DATE)
            .metadata(UPDATED_METADATA);

        restOcrComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrComparison.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrComparison))
            )
            .andExpect(status().isOk());

        // Validate the OcrComparison in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrComparisonUpdatableFieldsEquals(partialUpdatedOcrComparison, getPersistedOcrComparison(partialUpdatedOcrComparison));
    }

    @Test
    @Transactional
    void patchNonExistingOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ocrComparisonDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrComparisonDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOcrComparison() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);
        ocrComparisonRepository.save(ocrComparison);
        ocrComparisonSearchRepository.save(ocrComparison);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ocrComparison
        restOcrComparisonMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrComparison.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrComparisonSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOcrComparison() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);
        ocrComparisonSearchRepository.save(ocrComparison);

        // Search the ocrComparison
        restOcrComparisonMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ocrComparison.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrComparison.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].tikaText").value(hasItem(DEFAULT_TIKA_TEXT.toString())))
            .andExpect(jsonPath("$.[*].tikaConfidence").value(hasItem(DEFAULT_TIKA_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].aiText").value(hasItem(DEFAULT_AI_TEXT.toString())))
            .andExpect(jsonPath("$.[*].aiConfidence").value(hasItem(DEFAULT_AI_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].similarity").value(hasItem(DEFAULT_SIMILARITY)))
            .andExpect(jsonPath("$.[*].differences").value(hasItem(DEFAULT_DIFFERENCES.toString())))
            .andExpect(jsonPath("$.[*].differencesS3Key").value(hasItem(DEFAULT_DIFFERENCES_S_3_KEY)))
            .andExpect(jsonPath("$.[*].selectedEngine").value(hasItem(DEFAULT_SELECTED_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].selectedBy").value(hasItem(DEFAULT_SELECTED_BY)))
            .andExpect(jsonPath("$.[*].selectedDate").value(hasItem(DEFAULT_SELECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comparisonDate").value(hasItem(DEFAULT_COMPARISON_DATE.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())));
    }

    protected long getRepositoryCount() {
        return ocrComparisonRepository.count();
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

    protected OcrComparison getPersistedOcrComparison(OcrComparison ocrComparison) {
        return ocrComparisonRepository.findById(ocrComparison.getId()).orElseThrow();
    }

    protected void assertPersistedOcrComparisonToMatchAllProperties(OcrComparison expectedOcrComparison) {
        assertOcrComparisonAllPropertiesEquals(expectedOcrComparison, getPersistedOcrComparison(expectedOcrComparison));
    }

    protected void assertPersistedOcrComparisonToMatchUpdatableProperties(OcrComparison expectedOcrComparison) {
        assertOcrComparisonAllUpdatablePropertiesEquals(expectedOcrComparison, getPersistedOcrComparison(expectedOcrComparison));
    }
}
