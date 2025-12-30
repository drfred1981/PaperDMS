package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrResultAsserts.*;
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
import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.repository.search.OcrResultSearchRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrResultMapper;
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
 * Integration tests for the {@link OcrResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrResultResourceIT {

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final String DEFAULT_PAGE_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_SHA_256 = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final String DEFAULT_S_3_RESULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_RESULT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BOUNDING_BOX_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BOUNDING_BOX_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_BOUNDING_BOXES = "AAAAAAAAAA";
    private static final String UPDATED_BOUNDING_BOXES = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_WORD_COUNT = 1;
    private static final Integer UPDATED_WORD_COUNT = 2;
    private static final Integer SMALLER_WORD_COUNT = 1 - 1;

    private static final OcrEngine DEFAULT_OCR_ENGINE = OcrEngine.TIKA_TESSERACT;
    private static final OcrEngine UPDATED_OCR_ENGINE = OcrEngine.TIKA_NATIVE;

    private static final Long DEFAULT_PROCESSING_TIME = 1L;
    private static final Long UPDATED_PROCESSING_TIME = 2L;
    private static final Long SMALLER_PROCESSING_TIME = 1L - 1L;

    private static final String DEFAULT_RAW_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_RESPONSE = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_RESPONSE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RAW_RESPONSE_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ocr-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/ocr-results/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Autowired
    private OcrResultMapper ocrResultMapper;

    @Autowired
    private OcrResultSearchRepository ocrResultSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restOcrResultMockMvc;

    private OcrResult ocrResult;

    private OcrResult insertedOcrResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrResult createEntity() {
        return new OcrResult()
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .pageSha256(DEFAULT_PAGE_SHA_256)
            .confidence(DEFAULT_CONFIDENCE)
            .s3ResultKey(DEFAULT_S_3_RESULT_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .s3BoundingBoxKey(DEFAULT_S_3_BOUNDING_BOX_KEY)
            .boundingBoxes(DEFAULT_BOUNDING_BOXES)
            .metadata(DEFAULT_METADATA)
            .language(DEFAULT_LANGUAGE)
            .wordCount(DEFAULT_WORD_COUNT)
            .ocrEngine(DEFAULT_OCR_ENGINE)
            .processingTime(DEFAULT_PROCESSING_TIME)
            .rawResponse(DEFAULT_RAW_RESPONSE)
            .rawResponseS3Key(DEFAULT_RAW_RESPONSE_S_3_KEY)
            .processedDate(DEFAULT_PROCESSED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrResult createUpdatedEntity() {
        return new OcrResult()
            .pageNumber(UPDATED_PAGE_NUMBER)
            .pageSha256(UPDATED_PAGE_SHA_256)
            .confidence(UPDATED_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3BoundingBoxKey(UPDATED_S_3_BOUNDING_BOX_KEY)
            .boundingBoxes(UPDATED_BOUNDING_BOXES)
            .metadata(UPDATED_METADATA)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .rawResponseS3Key(UPDATED_RAW_RESPONSE_S_3_KEY)
            .processedDate(UPDATED_PROCESSED_DATE);
    }

    @BeforeEach
    void initTest() {
        ocrResult = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedOcrResult != null) {
            ocrResultRepository.delete(insertedOcrResult);
            ocrResultSearchRepository.delete(insertedOcrResult);
            insertedOcrResult = null;
        }
    }

    @Test
    @Transactional
    void createOcrResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);
        var returnedOcrResultDTO = om.readValue(
            restOcrResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            OcrResultDTO.class
        );

        // Validate the OcrResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedOcrResult = ocrResultMapper.toEntity(returnedOcrResultDTO);
        assertOcrResultUpdatableFieldsEquals(returnedOcrResult, getPersistedOcrResult(returnedOcrResult));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedOcrResult = returnedOcrResult;
    }

    @Test
    @Transactional
    void createOcrResultWithExistingId() throws Exception {
        // Create the OcrResult with an existing ID
        ocrResult.setId(1L);
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        // set the field null
        ocrResult.setPageNumber(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        // set the field null
        ocrResult.sets3Bucket(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkProcessedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        // set the field null
        ocrResult.setProcessedDate(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllOcrResults() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].pageSha256").value(hasItem(DEFAULT_PAGE_SHA_256)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3BoundingBoxKey").value(hasItem(DEFAULT_S_3_BOUNDING_BOX_KEY)))
            .andExpect(jsonPath("$.[*].boundingBoxes").value(hasItem(DEFAULT_BOUNDING_BOXES)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].rawResponse").value(hasItem(DEFAULT_RAW_RESPONSE)))
            .andExpect(jsonPath("$.[*].rawResponseS3Key").value(hasItem(DEFAULT_RAW_RESPONSE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));
    }

    @Test
    @Transactional
    void getOcrResult() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get the ocrResult
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL_ID, ocrResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(ocrResult.getId().intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.pageSha256").value(DEFAULT_PAGE_SHA_256))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.s3ResultKey").value(DEFAULT_S_3_RESULT_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.s3BoundingBoxKey").value(DEFAULT_S_3_BOUNDING_BOX_KEY))
            .andExpect(jsonPath("$.boundingBoxes").value(DEFAULT_BOUNDING_BOXES))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.wordCount").value(DEFAULT_WORD_COUNT))
            .andExpect(jsonPath("$.ocrEngine").value(DEFAULT_OCR_ENGINE.toString()))
            .andExpect(jsonPath("$.processingTime").value(DEFAULT_PROCESSING_TIME.intValue()))
            .andExpect(jsonPath("$.rawResponse").value(DEFAULT_RAW_RESPONSE))
            .andExpect(jsonPath("$.rawResponseS3Key").value(DEFAULT_RAW_RESPONSE_S_3_KEY))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getOcrResultsByIdFiltering() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        Long id = ocrResult.getId();

        defaultOcrResultFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultOcrResultFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultOcrResultFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber equals to
        defaultOcrResultFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber in
        defaultOcrResultFiltering(
            "pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER,
            "pageNumber.in=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber is not null
        defaultOcrResultFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber is greater than or equal to
        defaultOcrResultFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber is less than or equal to
        defaultOcrResultFiltering("pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER, "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber is less than
        defaultOcrResultFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageNumber is greater than
        defaultOcrResultFiltering("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER, "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageSha256 equals to
        defaultOcrResultFiltering("pageSha256.equals=" + DEFAULT_PAGE_SHA_256, "pageSha256.equals=" + UPDATED_PAGE_SHA_256);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageSha256 in
        defaultOcrResultFiltering(
            "pageSha256.in=" + DEFAULT_PAGE_SHA_256 + "," + UPDATED_PAGE_SHA_256,
            "pageSha256.in=" + UPDATED_PAGE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageSha256 is not null
        defaultOcrResultFiltering("pageSha256.specified=true", "pageSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageSha256 contains
        defaultOcrResultFiltering("pageSha256.contains=" + DEFAULT_PAGE_SHA_256, "pageSha256.contains=" + UPDATED_PAGE_SHA_256);
    }

    @Test
    @Transactional
    void getAllOcrResultsByPageSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where pageSha256 does not contain
        defaultOcrResultFiltering("pageSha256.doesNotContain=" + UPDATED_PAGE_SHA_256, "pageSha256.doesNotContain=" + DEFAULT_PAGE_SHA_256);
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence equals to
        defaultOcrResultFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence in
        defaultOcrResultFiltering("confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE, "confidence.in=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence is not null
        defaultOcrResultFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence is greater than or equal to
        defaultOcrResultFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence is less than or equal to
        defaultOcrResultFiltering("confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE, "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence is less than
        defaultOcrResultFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where confidence is greater than
        defaultOcrResultFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3ResultKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3ResultKey equals to
        defaultOcrResultFiltering("s3ResultKey.equals=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.equals=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3ResultKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3ResultKey in
        defaultOcrResultFiltering(
            "s3ResultKey.in=" + DEFAULT_S_3_RESULT_KEY + "," + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.in=" + UPDATED_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3ResultKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3ResultKey is not null
        defaultOcrResultFiltering("s3ResultKey.specified=true", "s3ResultKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3ResultKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3ResultKey contains
        defaultOcrResultFiltering("s3ResultKey.contains=" + DEFAULT_S_3_RESULT_KEY, "s3ResultKey.contains=" + UPDATED_S_3_RESULT_KEY);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3ResultKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3ResultKey does not contain
        defaultOcrResultFiltering(
            "s3ResultKey.doesNotContain=" + UPDATED_S_3_RESULT_KEY,
            "s3ResultKey.doesNotContain=" + DEFAULT_S_3_RESULT_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BucketIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3Bucket equals to
        defaultOcrResultFiltering("s3Bucket.equals=" + DEFAULT_S_3_BUCKET, "s3Bucket.equals=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BucketIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3Bucket in
        defaultOcrResultFiltering("s3Bucket.in=" + DEFAULT_S_3_BUCKET + "," + UPDATED_S_3_BUCKET, "s3Bucket.in=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BucketIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3Bucket is not null
        defaultOcrResultFiltering("s3Bucket.specified=true", "s3Bucket.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BucketContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3Bucket contains
        defaultOcrResultFiltering("s3Bucket.contains=" + DEFAULT_S_3_BUCKET, "s3Bucket.contains=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BucketNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3Bucket does not contain
        defaultOcrResultFiltering("s3Bucket.doesNotContain=" + UPDATED_S_3_BUCKET, "s3Bucket.doesNotContain=" + DEFAULT_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BoundingBoxKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3BoundingBoxKey equals to
        defaultOcrResultFiltering(
            "s3BoundingBoxKey.equals=" + DEFAULT_S_3_BOUNDING_BOX_KEY,
            "s3BoundingBoxKey.equals=" + UPDATED_S_3_BOUNDING_BOX_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BoundingBoxKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3BoundingBoxKey in
        defaultOcrResultFiltering(
            "s3BoundingBoxKey.in=" + DEFAULT_S_3_BOUNDING_BOX_KEY + "," + UPDATED_S_3_BOUNDING_BOX_KEY,
            "s3BoundingBoxKey.in=" + UPDATED_S_3_BOUNDING_BOX_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BoundingBoxKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3BoundingBoxKey is not null
        defaultOcrResultFiltering("s3BoundingBoxKey.specified=true", "s3BoundingBoxKey.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BoundingBoxKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3BoundingBoxKey contains
        defaultOcrResultFiltering(
            "s3BoundingBoxKey.contains=" + DEFAULT_S_3_BOUNDING_BOX_KEY,
            "s3BoundingBoxKey.contains=" + UPDATED_S_3_BOUNDING_BOX_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsBys3BoundingBoxKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where s3BoundingBoxKey does not contain
        defaultOcrResultFiltering(
            "s3BoundingBoxKey.doesNotContain=" + UPDATED_S_3_BOUNDING_BOX_KEY,
            "s3BoundingBoxKey.doesNotContain=" + DEFAULT_S_3_BOUNDING_BOX_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where language equals to
        defaultOcrResultFiltering("language.equals=" + DEFAULT_LANGUAGE, "language.equals=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where language in
        defaultOcrResultFiltering("language.in=" + DEFAULT_LANGUAGE + "," + UPDATED_LANGUAGE, "language.in=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where language is not null
        defaultOcrResultFiltering("language.specified=true", "language.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where language contains
        defaultOcrResultFiltering("language.contains=" + DEFAULT_LANGUAGE, "language.contains=" + UPDATED_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where language does not contain
        defaultOcrResultFiltering("language.doesNotContain=" + UPDATED_LANGUAGE, "language.doesNotContain=" + DEFAULT_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount equals to
        defaultOcrResultFiltering("wordCount.equals=" + DEFAULT_WORD_COUNT, "wordCount.equals=" + UPDATED_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount in
        defaultOcrResultFiltering("wordCount.in=" + DEFAULT_WORD_COUNT + "," + UPDATED_WORD_COUNT, "wordCount.in=" + UPDATED_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount is not null
        defaultOcrResultFiltering("wordCount.specified=true", "wordCount.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount is greater than or equal to
        defaultOcrResultFiltering(
            "wordCount.greaterThanOrEqual=" + DEFAULT_WORD_COUNT,
            "wordCount.greaterThanOrEqual=" + UPDATED_WORD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount is less than or equal to
        defaultOcrResultFiltering("wordCount.lessThanOrEqual=" + DEFAULT_WORD_COUNT, "wordCount.lessThanOrEqual=" + SMALLER_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount is less than
        defaultOcrResultFiltering("wordCount.lessThan=" + UPDATED_WORD_COUNT, "wordCount.lessThan=" + DEFAULT_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrResultsByWordCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where wordCount is greater than
        defaultOcrResultFiltering("wordCount.greaterThan=" + SMALLER_WORD_COUNT, "wordCount.greaterThan=" + DEFAULT_WORD_COUNT);
    }

    @Test
    @Transactional
    void getAllOcrResultsByOcrEngineIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where ocrEngine equals to
        defaultOcrResultFiltering("ocrEngine.equals=" + DEFAULT_OCR_ENGINE, "ocrEngine.equals=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByOcrEngineIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where ocrEngine in
        defaultOcrResultFiltering("ocrEngine.in=" + DEFAULT_OCR_ENGINE + "," + UPDATED_OCR_ENGINE, "ocrEngine.in=" + UPDATED_OCR_ENGINE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByOcrEngineIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where ocrEngine is not null
        defaultOcrResultFiltering("ocrEngine.specified=true", "ocrEngine.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime equals to
        defaultOcrResultFiltering("processingTime.equals=" + DEFAULT_PROCESSING_TIME, "processingTime.equals=" + UPDATED_PROCESSING_TIME);
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime in
        defaultOcrResultFiltering(
            "processingTime.in=" + DEFAULT_PROCESSING_TIME + "," + UPDATED_PROCESSING_TIME,
            "processingTime.in=" + UPDATED_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime is not null
        defaultOcrResultFiltering("processingTime.specified=true", "processingTime.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime is greater than or equal to
        defaultOcrResultFiltering(
            "processingTime.greaterThanOrEqual=" + DEFAULT_PROCESSING_TIME,
            "processingTime.greaterThanOrEqual=" + UPDATED_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime is less than or equal to
        defaultOcrResultFiltering(
            "processingTime.lessThanOrEqual=" + DEFAULT_PROCESSING_TIME,
            "processingTime.lessThanOrEqual=" + SMALLER_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime is less than
        defaultOcrResultFiltering(
            "processingTime.lessThan=" + UPDATED_PROCESSING_TIME,
            "processingTime.lessThan=" + DEFAULT_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessingTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processingTime is greater than
        defaultOcrResultFiltering(
            "processingTime.greaterThan=" + SMALLER_PROCESSING_TIME,
            "processingTime.greaterThan=" + DEFAULT_PROCESSING_TIME
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByRawResponseS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where rawResponseS3Key equals to
        defaultOcrResultFiltering(
            "rawResponseS3Key.equals=" + DEFAULT_RAW_RESPONSE_S_3_KEY,
            "rawResponseS3Key.equals=" + UPDATED_RAW_RESPONSE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByRawResponseS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where rawResponseS3Key in
        defaultOcrResultFiltering(
            "rawResponseS3Key.in=" + DEFAULT_RAW_RESPONSE_S_3_KEY + "," + UPDATED_RAW_RESPONSE_S_3_KEY,
            "rawResponseS3Key.in=" + UPDATED_RAW_RESPONSE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByRawResponseS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where rawResponseS3Key is not null
        defaultOcrResultFiltering("rawResponseS3Key.specified=true", "rawResponseS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByRawResponseS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where rawResponseS3Key contains
        defaultOcrResultFiltering(
            "rawResponseS3Key.contains=" + DEFAULT_RAW_RESPONSE_S_3_KEY,
            "rawResponseS3Key.contains=" + UPDATED_RAW_RESPONSE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByRawResponseS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where rawResponseS3Key does not contain
        defaultOcrResultFiltering(
            "rawResponseS3Key.doesNotContain=" + UPDATED_RAW_RESPONSE_S_3_KEY,
            "rawResponseS3Key.doesNotContain=" + DEFAULT_RAW_RESPONSE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processedDate equals to
        defaultOcrResultFiltering("processedDate.equals=" + DEFAULT_PROCESSED_DATE, "processedDate.equals=" + UPDATED_PROCESSED_DATE);
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processedDate in
        defaultOcrResultFiltering(
            "processedDate.in=" + DEFAULT_PROCESSED_DATE + "," + UPDATED_PROCESSED_DATE,
            "processedDate.in=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllOcrResultsByProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        // Get all the ocrResultList where processedDate is not null
        defaultOcrResultFiltering("processedDate.specified=true", "processedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllOcrResultsByJobIsEqualToSomething() throws Exception {
        OcrJob job;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            ocrResultRepository.saveAndFlush(ocrResult);
            job = OcrJobResourceIT.createEntity();
        } else {
            job = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        em.persist(job);
        em.flush();
        ocrResult.setJob(job);
        ocrResultRepository.saveAndFlush(ocrResult);
        Long jobId = job.getId();
        // Get all the ocrResultList where job equals to jobId
        defaultOcrResultShouldBeFound("jobId.equals=" + jobId);

        // Get all the ocrResultList where job equals to (jobId + 1)
        defaultOcrResultShouldNotBeFound("jobId.equals=" + (jobId + 1));
    }

    private void defaultOcrResultFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultOcrResultShouldBeFound(shouldBeFound);
        defaultOcrResultShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultOcrResultShouldBeFound(String filter) throws Exception {
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].pageSha256").value(hasItem(DEFAULT_PAGE_SHA_256)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3BoundingBoxKey").value(hasItem(DEFAULT_S_3_BOUNDING_BOX_KEY)))
            .andExpect(jsonPath("$.[*].boundingBoxes").value(hasItem(DEFAULT_BOUNDING_BOXES)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].rawResponse").value(hasItem(DEFAULT_RAW_RESPONSE)))
            .andExpect(jsonPath("$.[*].rawResponseS3Key").value(hasItem(DEFAULT_RAW_RESPONSE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));

        // Check, that the count call also returns 1
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultOcrResultShouldNotBeFound(String filter) throws Exception {
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restOcrResultMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingOcrResult() throws Exception {
        // Get the ocrResult
        restOcrResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingOcrResult() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrResultSearchRepository.save(ocrResult);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());

        // Update the ocrResult
        OcrResult updatedOcrResult = ocrResultRepository.findById(ocrResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrResult are not directly saved in db
        em.detach(updatedOcrResult);
        updatedOcrResult
            .pageNumber(UPDATED_PAGE_NUMBER)
            .pageSha256(UPDATED_PAGE_SHA_256)
            .confidence(UPDATED_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3BoundingBoxKey(UPDATED_S_3_BOUNDING_BOX_KEY)
            .boundingBoxes(UPDATED_BOUNDING_BOXES)
            .metadata(UPDATED_METADATA)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .rawResponseS3Key(UPDATED_RAW_RESPONSE_S_3_KEY)
            .processedDate(UPDATED_PROCESSED_DATE);
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(updatedOcrResult);

        restOcrResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedOcrResultToMatchAllProperties(updatedOcrResult);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<OcrResult> ocrResultSearchList = Streamable.of(ocrResultSearchRepository.findAll()).toList();
                OcrResult testOcrResultSearch = ocrResultSearchList.get(searchDatabaseSizeAfter - 1);

                assertOcrResultAllPropertiesEquals(testOcrResultSearch, updatedOcrResult);
            });
    }

    @Test
    @Transactional
    void putNonExistingOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, ocrResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(ocrResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateOcrResultWithPatch() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrResult using partial update
        OcrResult partialUpdatedOcrResult = new OcrResult();
        partialUpdatedOcrResult.setId(ocrResult.getId());

        partialUpdatedOcrResult
            .pageNumber(UPDATED_PAGE_NUMBER)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .language(UPDATED_LANGUAGE)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .processedDate(UPDATED_PROCESSED_DATE);

        restOcrResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrResult))
            )
            .andExpect(status().isOk());

        // Validate the OcrResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedOcrResult, ocrResult),
            getPersistedOcrResult(ocrResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateOcrResultWithPatch() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the ocrResult using partial update
        OcrResult partialUpdatedOcrResult = new OcrResult();
        partialUpdatedOcrResult.setId(ocrResult.getId());

        partialUpdatedOcrResult
            .pageNumber(UPDATED_PAGE_NUMBER)
            .pageSha256(UPDATED_PAGE_SHA_256)
            .confidence(UPDATED_CONFIDENCE)
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3BoundingBoxKey(UPDATED_S_3_BOUNDING_BOX_KEY)
            .boundingBoxes(UPDATED_BOUNDING_BOXES)
            .metadata(UPDATED_METADATA)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .processingTime(UPDATED_PROCESSING_TIME)
            .rawResponse(UPDATED_RAW_RESPONSE)
            .rawResponseS3Key(UPDATED_RAW_RESPONSE_S_3_KEY)
            .processedDate(UPDATED_PROCESSED_DATE);

        restOcrResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedOcrResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedOcrResult))
            )
            .andExpect(status().isOk());

        // Validate the OcrResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertOcrResultUpdatableFieldsEquals(partialUpdatedOcrResult, getPersistedOcrResult(partialUpdatedOcrResult));
    }

    @Test
    @Transactional
    void patchNonExistingOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, ocrResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(ocrResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteOcrResult() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);
        ocrResultRepository.save(ocrResult);
        ocrResultSearchRepository.save(ocrResult);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the ocrResult
        restOcrResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(ocrResultSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchOcrResult() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);
        ocrResultSearchRepository.save(ocrResult);

        // Search the ocrResult
        restOcrResultMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + ocrResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(ocrResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].pageSha256").value(hasItem(DEFAULT_PAGE_SHA_256)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].s3ResultKey").value(hasItem(DEFAULT_S_3_RESULT_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3BoundingBoxKey").value(hasItem(DEFAULT_S_3_BOUNDING_BOX_KEY)))
            .andExpect(jsonPath("$.[*].boundingBoxes").value(hasItem(DEFAULT_BOUNDING_BOXES.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE.toString())))
            .andExpect(jsonPath("$.[*].processingTime").value(hasItem(DEFAULT_PROCESSING_TIME.intValue())))
            .andExpect(jsonPath("$.[*].rawResponse").value(hasItem(DEFAULT_RAW_RESPONSE.toString())))
            .andExpect(jsonPath("$.[*].rawResponseS3Key").value(hasItem(DEFAULT_RAW_RESPONSE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return ocrResultRepository.count();
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

    protected OcrResult getPersistedOcrResult(OcrResult ocrResult) {
        return ocrResultRepository.findById(ocrResult.getId()).orElseThrow();
    }

    protected void assertPersistedOcrResultToMatchAllProperties(OcrResult expectedOcrResult) {
        assertOcrResultAllPropertiesEquals(expectedOcrResult, getPersistedOcrResult(expectedOcrResult));
    }

    protected void assertPersistedOcrResultToMatchUpdatableProperties(OcrResult expectedOcrResult) {
        assertOcrResultAllUpdatablePropertiesEquals(expectedOcrResult, getPersistedOcrResult(expectedOcrResult));
    }
}
