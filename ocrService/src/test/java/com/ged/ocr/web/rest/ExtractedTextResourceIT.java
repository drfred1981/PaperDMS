package com.ged.ocr.web.rest;

import static com.ged.ocr.domain.ExtractedTextAsserts.*;
import static com.ged.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ocr.IntegrationTest;
import com.ged.ocr.domain.ExtractedText;
import com.ged.ocr.domain.OcrJob;
import com.ged.ocr.repository.ExtractedTextRepository;
import com.ged.ocr.repository.search.ExtractedTextSearchRepository;
import com.ged.ocr.service.dto.ExtractedTextDTO;
import com.ged.ocr.service.mapper.ExtractedTextMapper;
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
 * Integration tests for the {@link ExtractedTextResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtractedTextResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;

    private static final String DEFAULT_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_LANGUAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_WORD_COUNT = 1;
    private static final Integer UPDATED_WORD_COUNT = 2;

    private static final Boolean DEFAULT_HAS_STRUCTURED_DATA = false;
    private static final Boolean UPDATED_HAS_STRUCTURED_DATA = true;

    private static final String DEFAULT_STRUCTURED_DATA = "AAAAAAAAAA";
    private static final String UPDATED_STRUCTURED_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/extracted-texts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/extracted-texts/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtractedTextRepository extractedTextRepository;

    @Autowired
    private ExtractedTextMapper extractedTextMapper;

    @Autowired
    private ExtractedTextSearchRepository extractedTextSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtractedTextMockMvc;

    private ExtractedText extractedText;

    private ExtractedText insertedExtractedText;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtractedText createEntity(EntityManager em) {
        ExtractedText extractedText = new ExtractedText()
            .content(DEFAULT_CONTENT)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .language(DEFAULT_LANGUAGE)
            .wordCount(DEFAULT_WORD_COUNT)
            .hasStructuredData(DEFAULT_HAS_STRUCTURED_DATA)
            .structuredData(DEFAULT_STRUCTURED_DATA)
            .extractedDate(DEFAULT_EXTRACTED_DATE);
        // Add required entity
        OcrJob ocrJob;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            ocrJob = OcrJobResourceIT.createEntity();
            em.persist(ocrJob);
            em.flush();
        } else {
            ocrJob = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        extractedText.setJob(ocrJob);
        return extractedText;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtractedText createUpdatedEntity(EntityManager em) {
        ExtractedText updatedExtractedText = new ExtractedText()
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        // Add required entity
        OcrJob ocrJob;
        if (TestUtil.findAll(em, OcrJob.class).isEmpty()) {
            ocrJob = OcrJobResourceIT.createUpdatedEntity();
            em.persist(ocrJob);
            em.flush();
        } else {
            ocrJob = TestUtil.findAll(em, OcrJob.class).get(0);
        }
        updatedExtractedText.setJob(ocrJob);
        return updatedExtractedText;
    }

    @BeforeEach
    void initTest() {
        extractedText = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExtractedText != null) {
            extractedTextRepository.delete(insertedExtractedText);
            extractedTextSearchRepository.delete(insertedExtractedText);
            insertedExtractedText = null;
        }
    }

    @Test
    @Transactional
    void createExtractedText() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);
        var returnedExtractedTextDTO = om.readValue(
            restExtractedTextMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedTextDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExtractedTextDTO.class
        );

        // Validate the ExtractedText in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExtractedText = extractedTextMapper.toEntity(returnedExtractedTextDTO);
        assertExtractedTextUpdatableFieldsEquals(returnedExtractedText, getPersistedExtractedText(returnedExtractedText));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedExtractedText = returnedExtractedText;
    }

    @Test
    @Transactional
    void createExtractedTextWithExistingId() throws Exception {
        // Create the ExtractedText with an existing ID
        extractedText.setId(1L);
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedTextDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        // set the field null
        extractedText.setPageNumber(null);

        // Create the ExtractedText, which fails.
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        restExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExtractedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        // set the field null
        extractedText.setExtractedDate(null);

        // Create the ExtractedText, which fails.
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        restExtractedTextMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedTextDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllExtractedTexts() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);

        // Get all the extractedTextList
        restExtractedTextMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extractedText.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].hasStructuredData").value(hasItem(DEFAULT_HAS_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredData").value(hasItem(DEFAULT_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExtractedText() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);

        // Get the extractedText
        restExtractedTextMockMvc
            .perform(get(ENTITY_API_URL_ID, extractedText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extractedText.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.language").value(DEFAULT_LANGUAGE))
            .andExpect(jsonPath("$.wordCount").value(DEFAULT_WORD_COUNT))
            .andExpect(jsonPath("$.hasStructuredData").value(DEFAULT_HAS_STRUCTURED_DATA))
            .andExpect(jsonPath("$.structuredData").value(DEFAULT_STRUCTURED_DATA))
            .andExpect(jsonPath("$.extractedDate").value(DEFAULT_EXTRACTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExtractedText() throws Exception {
        // Get the extractedText
        restExtractedTextMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtractedText() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        extractedTextSearchRepository.save(extractedText);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());

        // Update the extractedText
        ExtractedText updatedExtractedText = extractedTextRepository.findById(extractedText.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtractedText are not directly saved in db
        em.detach(updatedExtractedText);
        updatedExtractedText
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(updatedExtractedText);

        restExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extractedTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedTextDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtractedTextToMatchAllProperties(updatedExtractedText);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ExtractedText> extractedTextSearchList = Streamable.of(extractedTextSearchRepository.findAll()).toList();
                ExtractedText testExtractedTextSearch = extractedTextSearchList.get(searchDatabaseSizeAfter - 1);

                assertExtractedTextAllPropertiesEquals(testExtractedTextSearch, updatedExtractedText);
            });
    }

    @Test
    @Transactional
    void putNonExistingExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extractedTextDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateExtractedTextWithPatch() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extractedText using partial update
        ExtractedText partialUpdatedExtractedText = new ExtractedText();
        partialUpdatedExtractedText.setId(extractedText.getId());

        partialUpdatedExtractedText
            .language(UPDATED_LANGUAGE)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtractedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtractedText))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtractedTextUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExtractedText, extractedText),
            getPersistedExtractedText(extractedText)
        );
    }

    @Test
    @Transactional
    void fullUpdateExtractedTextWithPatch() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extractedText using partial update
        ExtractedText partialUpdatedExtractedText = new ExtractedText();
        partialUpdatedExtractedText.setId(extractedText.getId());

        partialUpdatedExtractedText
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .language(UPDATED_LANGUAGE)
            .wordCount(UPDATED_WORD_COUNT)
            .hasStructuredData(UPDATED_HAS_STRUCTURED_DATA)
            .structuredData(UPDATED_STRUCTURED_DATA)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtractedText.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtractedText))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedText in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtractedTextUpdatableFieldsEquals(partialUpdatedExtractedText, getPersistedExtractedText(partialUpdatedExtractedText));
    }

    @Test
    @Transactional
    void patchNonExistingExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extractedTextDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extractedTextDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtractedText() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        extractedText.setId(longCount.incrementAndGet());

        // Create the ExtractedText
        ExtractedTextDTO extractedTextDTO = extractedTextMapper.toDto(extractedText);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedTextMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extractedTextDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtractedText in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteExtractedText() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);
        extractedTextRepository.save(extractedText);
        extractedTextSearchRepository.save(extractedText);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the extractedText
        restExtractedTextMockMvc
            .perform(delete(ENTITY_API_URL_ID, extractedText.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedTextSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchExtractedText() throws Exception {
        // Initialize the database
        insertedExtractedText = extractedTextRepository.saveAndFlush(extractedText);
        extractedTextSearchRepository.save(extractedText);

        // Search the extractedText
        restExtractedTextMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + extractedText.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extractedText.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].language").value(hasItem(DEFAULT_LANGUAGE)))
            .andExpect(jsonPath("$.[*].wordCount").value(hasItem(DEFAULT_WORD_COUNT)))
            .andExpect(jsonPath("$.[*].hasStructuredData").value(hasItem(DEFAULT_HAS_STRUCTURED_DATA)))
            .andExpect(jsonPath("$.[*].structuredData").value(hasItem(DEFAULT_STRUCTURED_DATA.toString())))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return extractedTextRepository.count();
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

    protected ExtractedText getPersistedExtractedText(ExtractedText extractedText) {
        return extractedTextRepository.findById(extractedText.getId()).orElseThrow();
    }

    protected void assertPersistedExtractedTextToMatchAllProperties(ExtractedText expectedExtractedText) {
        assertExtractedTextAllPropertiesEquals(expectedExtractedText, getPersistedExtractedText(expectedExtractedText));
    }

    protected void assertPersistedExtractedTextToMatchUpdatableProperties(ExtractedText expectedExtractedText) {
        assertExtractedTextAllUpdatablePropertiesEquals(expectedExtractedText, getPersistedExtractedText(expectedExtractedText));
    }
}
