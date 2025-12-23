package fr.smartprod.paperdms.ocr.web.rest;

import static fr.smartprod.paperdms.ocr.domain.OcrComparisonAsserts.*;
import static fr.smartprod.paperdms.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ocr.IntegrationTest;
import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.domain.enumeration.OcrEngine;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
import fr.smartprod.paperdms.ocr.service.dto.OcrComparisonDTO;
import fr.smartprod.paperdms.ocr.service.mapper.OcrComparisonMapper;
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
 * Integration tests for the {@link OcrComparisonResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class OcrComparisonResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;

    private static final String DEFAULT_TIKA_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_TIKA_TEXT = "BBBBBBBBBB";

    private static final Double DEFAULT_TIKA_CONFIDENCE = 0D;
    private static final Double UPDATED_TIKA_CONFIDENCE = 1D;

    private static final String DEFAULT_AI_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_AI_TEXT = "BBBBBBBBBB";

    private static final Double DEFAULT_AI_CONFIDENCE = 0D;
    private static final Double UPDATED_AI_CONFIDENCE = 1D;

    private static final Double DEFAULT_SIMILARITY = 0D;
    private static final Double UPDATED_SIMILARITY = 1D;

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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private OcrComparisonRepository ocrComparisonRepository;

    @Autowired
    private OcrComparisonMapper ocrComparisonMapper;

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
            .documentId(DEFAULT_DOCUMENT_ID)
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
            .documentId(UPDATED_DOCUMENT_ID)
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
            insertedOcrComparison = null;
        }
    }

    @Test
    @Transactional
    void createOcrComparison() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedOcrComparison = returnedOcrComparison;
    }

    @Test
    @Transactional
    void createOcrComparisonWithExistingId() throws Exception {
        // Create the OcrComparison with an existing ID
        ocrComparison.setId(1L);
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrComparison.setDocumentId(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrComparison.setDocumentSha256(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrComparison.setPageNumber(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComparisonDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        ocrComparison.setComparisonDate(null);

        // Create the OcrComparison, which fails.
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        restOcrComparisonMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
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
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
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

        // Update the ocrComparison
        OcrComparison updatedOcrComparison = ocrComparisonRepository.findById(ocrComparison.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedOcrComparison are not directly saved in db
        em.detach(updatedOcrComparison);
        updatedOcrComparison
            .documentId(UPDATED_DOCUMENT_ID)
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
    }

    @Test
    @Transactional
    void putNonExistingOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .tikaText(UPDATED_TIKA_TEXT)
            .aiText(UPDATED_AI_TEXT)
            .aiConfidence(UPDATED_AI_CONFIDENCE)
            .similarity(UPDATED_SIMILARITY)
            .differences(UPDATED_DIFFERENCES)
            .differencesS3Key(UPDATED_DIFFERENCES_S_3_KEY)
            .selectedBy(UPDATED_SELECTED_BY)
            .comparisonDate(UPDATED_COMPARISON_DATE);

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
            .documentId(UPDATED_DOCUMENT_ID)
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamOcrComparison() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        ocrComparison.setId(longCount.incrementAndGet());

        // Create the OcrComparison
        OcrComparisonDTO ocrComparisonDTO = ocrComparisonMapper.toDto(ocrComparison);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restOcrComparisonMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(ocrComparisonDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the OcrComparison in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteOcrComparison() throws Exception {
        // Initialize the database
        insertedOcrComparison = ocrComparisonRepository.saveAndFlush(ocrComparison);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the ocrComparison
        restOcrComparisonMockMvc
            .perform(delete(ENTITY_API_URL_ID, ocrComparison.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
