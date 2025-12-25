package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.ExtractedFieldAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.ExtractedField;
import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import fr.smartprod.paperdms.document.repository.ExtractedFieldRepository;
import fr.smartprod.paperdms.document.repository.search.ExtractedFieldSearchRepository;
import fr.smartprod.paperdms.document.service.dto.ExtractedFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.ExtractedFieldMapper;
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
 * Integration tests for the {@link ExtractedFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExtractedFieldResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_FIELD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;

    private static final ExtractionMethod DEFAULT_EXTRACTION_METHOD = ExtractionMethod.OCR;
    private static final ExtractionMethod UPDATED_EXTRACTION_METHOD = ExtractionMethod.NLP;

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final Instant DEFAULT_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/extracted-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/extracted-fields/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExtractedFieldRepository extractedFieldRepository;

    @Autowired
    private ExtractedFieldMapper extractedFieldMapper;

    @Autowired
    private ExtractedFieldSearchRepository extractedFieldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExtractedFieldMockMvc;

    private ExtractedField extractedField;

    private ExtractedField insertedExtractedField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtractedField createEntity() {
        return new ExtractedField()
            .documentId(DEFAULT_DOCUMENT_ID)
            .fieldKey(DEFAULT_FIELD_KEY)
            .fieldValue(DEFAULT_FIELD_VALUE)
            .confidence(DEFAULT_CONFIDENCE)
            .extractionMethod(DEFAULT_EXTRACTION_METHOD)
            .isVerified(DEFAULT_IS_VERIFIED)
            .extractedDate(DEFAULT_EXTRACTED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExtractedField createUpdatedEntity() {
        return new ExtractedField()
            .documentId(UPDATED_DOCUMENT_ID)
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);
    }

    @BeforeEach
    void initTest() {
        extractedField = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedExtractedField != null) {
            extractedFieldRepository.delete(insertedExtractedField);
            extractedFieldSearchRepository.delete(insertedExtractedField);
            insertedExtractedField = null;
        }
    }

    @Test
    @Transactional
    void createExtractedField() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);
        var returnedExtractedFieldDTO = om.readValue(
            restExtractedFieldMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExtractedFieldDTO.class
        );

        // Validate the ExtractedField in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExtractedField = extractedFieldMapper.toEntity(returnedExtractedFieldDTO);
        assertExtractedFieldUpdatableFieldsEquals(returnedExtractedField, getPersistedExtractedField(returnedExtractedField));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedExtractedField = returnedExtractedField;
    }

    @Test
    @Transactional
    void createExtractedFieldWithExistingId() throws Exception {
        // Create the ExtractedField with an existing ID
        extractedField.setId(1L);
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        // set the field null
        extractedField.setDocumentId(null);

        // Create the ExtractedField, which fails.
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        restExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFieldKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        // set the field null
        extractedField.setFieldKey(null);

        // Create the ExtractedField, which fails.
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        restExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        // set the field null
        extractedField.setIsVerified(null);

        // Create the ExtractedField, which fails.
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        restExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExtractedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        // set the field null
        extractedField.setExtractedDate(null);

        // Create the ExtractedField, which fails.
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        restExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllExtractedFields() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);

        // Get all the extractedFieldList
        restExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extractedField.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].extractionMethod").value(hasItem(DEFAULT_EXTRACTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExtractedField() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);

        // Get the extractedField
        restExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, extractedField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(extractedField.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.fieldKey").value(DEFAULT_FIELD_KEY))
            .andExpect(jsonPath("$.fieldValue").value(DEFAULT_FIELD_VALUE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.extractionMethod").value(DEFAULT_EXTRACTION_METHOD.toString()))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.extractedDate").value(DEFAULT_EXTRACTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExtractedField() throws Exception {
        // Get the extractedField
        restExtractedFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExtractedField() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        extractedFieldSearchRepository.save(extractedField);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());

        // Update the extractedField
        ExtractedField updatedExtractedField = extractedFieldRepository.findById(extractedField.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExtractedField are not directly saved in db
        em.detach(updatedExtractedField);
        updatedExtractedField
            .documentId(UPDATED_DOCUMENT_ID)
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(updatedExtractedField);

        restExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extractedFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExtractedFieldToMatchAllProperties(updatedExtractedField);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<ExtractedField> extractedFieldSearchList = Streamable.of(extractedFieldSearchRepository.findAll()).toList();
                ExtractedField testExtractedFieldSearch = extractedFieldSearchList.get(searchDatabaseSizeAfter - 1);

                assertExtractedFieldAllPropertiesEquals(testExtractedFieldSearch, updatedExtractedField);
            });
    }

    @Test
    @Transactional
    void putNonExistingExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, extractedFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(extractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateExtractedFieldWithPatch() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extractedField using partial update
        ExtractedField partialUpdatedExtractedField = new ExtractedField();
        partialUpdatedExtractedField.setId(extractedField.getId());

        partialUpdatedExtractedField
            .documentId(UPDATED_DOCUMENT_ID)
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtractedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtractedField))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtractedFieldUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExtractedField, extractedField),
            getPersistedExtractedField(extractedField)
        );
    }

    @Test
    @Transactional
    void fullUpdateExtractedFieldWithPatch() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the extractedField using partial update
        ExtractedField partialUpdatedExtractedField = new ExtractedField();
        partialUpdatedExtractedField.setId(extractedField.getId());

        partialUpdatedExtractedField
            .documentId(UPDATED_DOCUMENT_ID)
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExtractedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExtractedField))
            )
            .andExpect(status().isOk());

        // Validate the ExtractedField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExtractedFieldUpdatableFieldsEquals(partialUpdatedExtractedField, getPersistedExtractedField(partialUpdatedExtractedField));
    }

    @Test
    @Transactional
    void patchNonExistingExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, extractedFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(extractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        extractedField.setId(longCount.incrementAndGet());

        // Create the ExtractedField
        ExtractedFieldDTO extractedFieldDTO = extractedFieldMapper.toDto(extractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExtractedFieldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(extractedFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteExtractedField() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);
        extractedFieldRepository.save(extractedField);
        extractedFieldSearchRepository.save(extractedField);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the extractedField
        restExtractedFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, extractedField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(extractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchExtractedField() throws Exception {
        // Initialize the database
        insertedExtractedField = extractedFieldRepository.saveAndFlush(extractedField);
        extractedFieldSearchRepository.save(extractedField);

        // Search the extractedField
        restExtractedFieldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + extractedField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(extractedField.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].extractionMethod").value(hasItem(DEFAULT_EXTRACTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return extractedFieldRepository.count();
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

    protected ExtractedField getPersistedExtractedField(ExtractedField extractedField) {
        return extractedFieldRepository.findById(extractedField.getId()).orElseThrow();
    }

    protected void assertPersistedExtractedFieldToMatchAllProperties(ExtractedField expectedExtractedField) {
        assertExtractedFieldAllPropertiesEquals(expectedExtractedField, getPersistedExtractedField(expectedExtractedField));
    }

    protected void assertPersistedExtractedFieldToMatchUpdatableProperties(ExtractedField expectedExtractedField) {
        assertExtractedFieldAllUpdatablePropertiesEquals(expectedExtractedField, getPersistedExtractedField(expectedExtractedField));
    }
}
