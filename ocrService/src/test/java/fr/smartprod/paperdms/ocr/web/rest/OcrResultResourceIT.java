package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrResultAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrResultDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrResultMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
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

    private static final String DEFAULT_PAGE_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_PAGE_SHA_256 = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;

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

    private static final OcrEngine DEFAULT_OCR_ENGINE = OcrEngine.TIKA_TESSERACT;
    private static final OcrEngine UPDATED_OCR_ENGINE = OcrEngine.TIKA_NATIVE;

    private static final Long DEFAULT_PROCESSING_TIME = 1L;
    private static final Long UPDATED_PROCESSING_TIME = 2L;

    private static final String DEFAULT_RAW_RESPONSE = "AAAAAAAAAA";
    private static final String UPDATED_RAW_RESPONSE = "BBBBBBBBBB";

    private static final String DEFAULT_RAW_RESPONSE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RAW_RESPONSE_S_3_KEY = "BBBBBBBBBB";

    private static final Instant DEFAULT_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ocr-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrResultRepository ocrResultRepository;

    @Autowired
    private OcrResultMapper ocrResultMapper;

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
    public static OcrResult createEntity(EntityManager em) {
        OcrResult ocrResult = new OcrResult()
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
        // Add required entity
        OcrJob ocrJob;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            ocrJob = OcrJobResourceIT.createEntity();
            em.persist(ocrJob);
            em.flush();
        } else {
            ocrJob = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        ocrResult.setJob(ocrJob);
        return ocrResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static OcrResult createUpdatedEntity(EntityManager em) {
        OcrResult updatedOcrResult = new OcrResult()
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
        // Add required entity
        OcrJob ocrJob;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            ocrJob = OcrJobResourceIT.createUpdatedEntity();
            em.persist(ocrJob);
            em.flush();
        } else {
            ocrJob = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        updatedOcrResult.setJob(ocrJob);
        return updatedOcrResult;
    }

    @BeforeEach
    void initTest() {
        ocrResult = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedOcrResult != null) {
            ocrResultRepository.delete(insertedOcrResult);
            insertedOcrResult = null;
        }
    }

    @Test
    @Transactional
    void createOcrResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedOcrResult = returnedOcrResult;
    }

    @Test
    @Transactional
    void createOcrResultWithExistingId() throws Exception {
        // Create the OcrResult with an existing ID
        ocrResult.setId(1L);
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrResult.setPageNumber(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrResult.sets3Bucket(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProcessedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrResult.setProcessedDate(null);

        // Create the OcrResult, which fails.
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        restOcrResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
    }

    @Test
    @Transactional
    void putNonExistingOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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
            .s3ResultKey(UPDATED_S_3_RESULT_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .boundingBoxes(UPDATED_BOUNDING_BOXES)
            .metadata(UPDATED_METADATA)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .processingTime(UPDATED_PROCESSING_TIME)
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrResult.setId(longCount.incrementAndGet());

        // Create the OcrResult
        OcrResultDTO ocrResultDTO = ocrResultMapper.toDto(ocrResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOcrResult() throws Exception {
        // Initialize the database
        insertedOcrResult = ocrResultRepository.saveAndFlush(ocrResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ocrResult
        restOcrResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
