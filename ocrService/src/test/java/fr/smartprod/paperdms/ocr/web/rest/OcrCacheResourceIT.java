package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrCacheAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrCacheDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrCacheMapper;
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

    private static final Double DEFAULT_TOTAL_CONFIDENCE = 0D;
    private static final Double UPDATED_TOTAL_CONFIDENCE = 1D;

    private static final String DEFAULT_S_3_RESULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_RESULT_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_EXTRACTED_TEXT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Integer DEFAULT_HITS = 1;
    private static final Integer UPDATED_HITS = 2;

    private static final Instant DEFAULT_LAST_ACCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_ACCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/ocr-caches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrCacheRepository ocrCacheRepository;

    @Autowired
    private OcrCacheMapper ocrCacheMapper;

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
            .extractedTextS3Key(DEFAULT_EXTRACTED_TEXT_S_3_KEY)
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
            .extractedTextS3Key(UPDATED_EXTRACTED_TEXT_S_3_KEY)
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
            insertedOcrCache = null;
        }
    }

    @Test
    @Transactional
    void createOcrCache() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedOcrCache = returnedOcrCache;
    }

    @Test
    @Transactional
    void createOcrCacheWithExistingId() throws Exception {
        // Create the OcrCache with an existing ID
        ocrCache.setId(1L);
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrCache.setDocumentSha256(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageCountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrCache.setPageCount(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3ResultKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrCache.sets3ResultKey(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrCache.sets3Bucket(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrCache.setCreatedDate(null);

        // Create the OcrCache, which fails.
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        restOcrCacheMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].extractedTextS3Key").value(hasItem(DEFAULT_EXTRACTED_TEXT_S_3_KEY)))
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
            .andExpect(jsonPath("$.extractedTextS3Key").value(DEFAULT_EXTRACTED_TEXT_S_3_KEY))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.hits").value(DEFAULT_HITS))
            .andExpect(jsonPath("$.lastAccessDate").value(DEFAULT_LAST_ACCESS_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()));
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
            .extractedTextS3Key(UPDATED_EXTRACTED_TEXT_S_3_KEY)
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
    }

    @Test
    @Transactional
    void putNonExistingOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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
            .extractedTextS3Key(UPDATED_EXTRACTED_TEXT_S_3_KEY);

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
            .extractedTextS3Key(UPDATED_EXTRACTED_TEXT_S_3_KEY)
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrCache() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrCache.setId(longCount.incrementAndGet());

        // Create the OcrCache
        OcrCacheDTO ocrCacheDTO = ocrCacheMapper.toDto(ocrCache);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrCacheMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrCacheDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrCache in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOcrCache() throws Exception {
        // Initialize the database
        insertedOcrCache = ocrCacheRepository.saveAndFlush(ocrCache);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ocrCache
        restOcrCacheMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrCache.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
