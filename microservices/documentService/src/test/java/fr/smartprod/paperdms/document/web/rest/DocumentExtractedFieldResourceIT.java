package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentExtractedFieldAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import fr.smartprod.paperdms.document.domain.enumeration.ExtractionMethod;
import fr.smartprod.paperdms.document.repository.DocumentExtractedFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentExtractedFieldSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentExtractedFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentExtractedFieldMapper;
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
 * Integration tests for the {@link DocumentExtractedFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentExtractedFieldResourceIT {

    private static final String DEFAULT_FIELD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_VALUE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final ExtractionMethod DEFAULT_EXTRACTION_METHOD = ExtractionMethod.OCR;
    private static final ExtractionMethod UPDATED_EXTRACTION_METHOD = ExtractionMethod.NLP;

    private static final Boolean DEFAULT_IS_VERIFIED = false;
    private static final Boolean UPDATED_IS_VERIFIED = true;

    private static final Instant DEFAULT_EXTRACTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXTRACTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-extracted-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-extracted-fields/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentExtractedFieldRepository documentExtractedFieldRepository;

    @Autowired
    private DocumentExtractedFieldMapper documentExtractedFieldMapper;

    @Autowired
    private DocumentExtractedFieldSearchRepository documentExtractedFieldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentExtractedFieldMockMvc;

    private DocumentExtractedField documentExtractedField;

    private DocumentExtractedField insertedDocumentExtractedField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentExtractedField createEntity() {
        return new DocumentExtractedField()
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
    public static DocumentExtractedField createUpdatedEntity() {
        return new DocumentExtractedField()
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentExtractedField = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentExtractedField != null) {
            documentExtractedFieldRepository.delete(insertedDocumentExtractedField);
            documentExtractedFieldSearchRepository.delete(insertedDocumentExtractedField);
            insertedDocumentExtractedField = null;
        }
    }

    @Test
    @Transactional
    void createDocumentExtractedField() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);
        var returnedDocumentExtractedFieldDTO = om.readValue(
            restDocumentExtractedFieldMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentExtractedFieldDTO.class
        );

        // Validate the DocumentExtractedField in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentExtractedField = documentExtractedFieldMapper.toEntity(returnedDocumentExtractedFieldDTO);
        assertDocumentExtractedFieldUpdatableFieldsEquals(
            returnedDocumentExtractedField,
            getPersistedDocumentExtractedField(returnedDocumentExtractedField)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentExtractedField = returnedDocumentExtractedField;
    }

    @Test
    @Transactional
    void createDocumentExtractedFieldWithExistingId() throws Exception {
        // Create the DocumentExtractedField with an existing ID
        documentExtractedField.setId(1L);
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFieldKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        // set the field null
        documentExtractedField.setFieldKey(null);

        // Create the DocumentExtractedField, which fails.
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        restDocumentExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsVerifiedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        // set the field null
        documentExtractedField.setIsVerified(null);

        // Create the DocumentExtractedField, which fails.
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        restDocumentExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkExtractedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        // set the field null
        documentExtractedField.setExtractedDate(null);

        // Create the DocumentExtractedField, which fails.
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        restDocumentExtractedFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFields() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentExtractedField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].extractionMethod").value(hasItem(DEFAULT_EXTRACTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentExtractedField() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get the documentExtractedField
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, documentExtractedField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentExtractedField.getId().intValue()))
            .andExpect(jsonPath("$.fieldKey").value(DEFAULT_FIELD_KEY))
            .andExpect(jsonPath("$.fieldValue").value(DEFAULT_FIELD_VALUE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.extractionMethod").value(DEFAULT_EXTRACTION_METHOD.toString()))
            .andExpect(jsonPath("$.isVerified").value(DEFAULT_IS_VERIFIED))
            .andExpect(jsonPath("$.extractedDate").value(DEFAULT_EXTRACTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentExtractedFieldsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        Long id = documentExtractedField.getId();

        defaultDocumentExtractedFieldFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentExtractedFieldFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentExtractedFieldFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByFieldKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where fieldKey equals to
        defaultDocumentExtractedFieldFiltering("fieldKey.equals=" + DEFAULT_FIELD_KEY, "fieldKey.equals=" + UPDATED_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByFieldKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where fieldKey in
        defaultDocumentExtractedFieldFiltering(
            "fieldKey.in=" + DEFAULT_FIELD_KEY + "," + UPDATED_FIELD_KEY,
            "fieldKey.in=" + UPDATED_FIELD_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByFieldKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where fieldKey is not null
        defaultDocumentExtractedFieldFiltering("fieldKey.specified=true", "fieldKey.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByFieldKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where fieldKey contains
        defaultDocumentExtractedFieldFiltering("fieldKey.contains=" + DEFAULT_FIELD_KEY, "fieldKey.contains=" + UPDATED_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByFieldKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where fieldKey does not contain
        defaultDocumentExtractedFieldFiltering(
            "fieldKey.doesNotContain=" + UPDATED_FIELD_KEY,
            "fieldKey.doesNotContain=" + DEFAULT_FIELD_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence equals to
        defaultDocumentExtractedFieldFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence in
        defaultDocumentExtractedFieldFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence is not null
        defaultDocumentExtractedFieldFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence is greater than or equal to
        defaultDocumentExtractedFieldFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence is less than or equal to
        defaultDocumentExtractedFieldFiltering(
            "confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence is less than
        defaultDocumentExtractedFieldFiltering(
            "confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1),
            "confidence.lessThan=" + DEFAULT_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where confidence is greater than
        defaultDocumentExtractedFieldFiltering(
            "confidence.greaterThan=" + SMALLER_CONFIDENCE,
            "confidence.greaterThan=" + DEFAULT_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractionMethodIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractionMethod equals to
        defaultDocumentExtractedFieldFiltering(
            "extractionMethod.equals=" + DEFAULT_EXTRACTION_METHOD,
            "extractionMethod.equals=" + UPDATED_EXTRACTION_METHOD
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractionMethodIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractionMethod in
        defaultDocumentExtractedFieldFiltering(
            "extractionMethod.in=" + DEFAULT_EXTRACTION_METHOD + "," + UPDATED_EXTRACTION_METHOD,
            "extractionMethod.in=" + UPDATED_EXTRACTION_METHOD
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractionMethodIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractionMethod is not null
        defaultDocumentExtractedFieldFiltering("extractionMethod.specified=true", "extractionMethod.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByIsVerifiedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where isVerified equals to
        defaultDocumentExtractedFieldFiltering("isVerified.equals=" + DEFAULT_IS_VERIFIED, "isVerified.equals=" + UPDATED_IS_VERIFIED);
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByIsVerifiedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where isVerified in
        defaultDocumentExtractedFieldFiltering(
            "isVerified.in=" + DEFAULT_IS_VERIFIED + "," + UPDATED_IS_VERIFIED,
            "isVerified.in=" + UPDATED_IS_VERIFIED
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByIsVerifiedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where isVerified is not null
        defaultDocumentExtractedFieldFiltering("isVerified.specified=true", "isVerified.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractedDate equals to
        defaultDocumentExtractedFieldFiltering(
            "extractedDate.equals=" + DEFAULT_EXTRACTED_DATE,
            "extractedDate.equals=" + UPDATED_EXTRACTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractedDate in
        defaultDocumentExtractedFieldFiltering(
            "extractedDate.in=" + DEFAULT_EXTRACTED_DATE + "," + UPDATED_EXTRACTED_DATE,
            "extractedDate.in=" + UPDATED_EXTRACTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByExtractedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        // Get all the documentExtractedFieldList where extractedDate is not null
        defaultDocumentExtractedFieldFiltering("extractedDate.specified=true", "extractedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentExtractedFieldsByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentExtractedFieldRepository.saveAndFlush(documentExtractedField);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentExtractedField.setDocument(document);
        documentExtractedFieldRepository.saveAndFlush(documentExtractedField);
        Long documentId = document.getId();
        // Get all the documentExtractedFieldList where document equals to documentId
        defaultDocumentExtractedFieldShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentExtractedFieldList where document equals to (documentId + 1)
        defaultDocumentExtractedFieldShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    private void defaultDocumentExtractedFieldFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentExtractedFieldShouldBeFound(shouldBeFound);
        defaultDocumentExtractedFieldShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentExtractedFieldShouldBeFound(String filter) throws Exception {
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentExtractedField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].extractionMethod").value(hasItem(DEFAULT_EXTRACTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentExtractedFieldShouldNotBeFound(String filter) throws Exception {
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentExtractedField() throws Exception {
        // Get the documentExtractedField
        restDocumentExtractedFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentExtractedField() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentExtractedFieldSearchRepository.save(documentExtractedField);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());

        // Update the documentExtractedField
        DocumentExtractedField updatedDocumentExtractedField = documentExtractedFieldRepository
            .findById(documentExtractedField.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentExtractedField are not directly saved in db
        em.detach(updatedDocumentExtractedField);
        updatedDocumentExtractedField
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(updatedDocumentExtractedField);

        restDocumentExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentExtractedFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentExtractedFieldToMatchAllProperties(updatedDocumentExtractedField);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentExtractedField> documentExtractedFieldSearchList = Streamable.of(
                    documentExtractedFieldSearchRepository.findAll()
                ).toList();
                DocumentExtractedField testDocumentExtractedFieldSearch = documentExtractedFieldSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentExtractedFieldAllPropertiesEquals(testDocumentExtractedFieldSearch, updatedDocumentExtractedField);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentExtractedFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentExtractedFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentExtractedFieldWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentExtractedField using partial update
        DocumentExtractedField partialUpdatedDocumentExtractedField = new DocumentExtractedField();
        partialUpdatedDocumentExtractedField.setId(documentExtractedField.getId());

        partialUpdatedDocumentExtractedField.fieldValue(UPDATED_FIELD_VALUE);

        restDocumentExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentExtractedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentExtractedField))
            )
            .andExpect(status().isOk());

        // Validate the DocumentExtractedField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentExtractedFieldUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentExtractedField, documentExtractedField),
            getPersistedDocumentExtractedField(documentExtractedField)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentExtractedFieldWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentExtractedField using partial update
        DocumentExtractedField partialUpdatedDocumentExtractedField = new DocumentExtractedField();
        partialUpdatedDocumentExtractedField.setId(documentExtractedField.getId());

        partialUpdatedDocumentExtractedField
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldValue(UPDATED_FIELD_VALUE)
            .confidence(UPDATED_CONFIDENCE)
            .extractionMethod(UPDATED_EXTRACTION_METHOD)
            .isVerified(UPDATED_IS_VERIFIED)
            .extractedDate(UPDATED_EXTRACTED_DATE);

        restDocumentExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentExtractedField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentExtractedField))
            )
            .andExpect(status().isOk());

        // Validate the DocumentExtractedField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentExtractedFieldUpdatableFieldsEquals(
            partialUpdatedDocumentExtractedField,
            getPersistedDocumentExtractedField(partialUpdatedDocumentExtractedField)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentExtractedFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentExtractedField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        documentExtractedField.setId(longCount.incrementAndGet());

        // Create the DocumentExtractedField
        DocumentExtractedFieldDTO documentExtractedFieldDTO = documentExtractedFieldMapper.toDto(documentExtractedField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentExtractedFieldMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentExtractedFieldDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentExtractedField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentExtractedField() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);
        documentExtractedFieldRepository.save(documentExtractedField);
        documentExtractedFieldSearchRepository.save(documentExtractedField);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentExtractedField
        restDocumentExtractedFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentExtractedField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentExtractedFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentExtractedField() throws Exception {
        // Initialize the database
        insertedDocumentExtractedField = documentExtractedFieldRepository.saveAndFlush(documentExtractedField);
        documentExtractedFieldSearchRepository.save(documentExtractedField);

        // Search the documentExtractedField
        restDocumentExtractedFieldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentExtractedField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentExtractedField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldValue").value(hasItem(DEFAULT_FIELD_VALUE.toString())))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].extractionMethod").value(hasItem(DEFAULT_EXTRACTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].isVerified").value(hasItem(DEFAULT_IS_VERIFIED)))
            .andExpect(jsonPath("$.[*].extractedDate").value(hasItem(DEFAULT_EXTRACTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentExtractedFieldRepository.count();
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

    protected DocumentExtractedField getPersistedDocumentExtractedField(DocumentExtractedField documentExtractedField) {
        return documentExtractedFieldRepository.findById(documentExtractedField.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentExtractedFieldToMatchAllProperties(DocumentExtractedField expectedDocumentExtractedField) {
        assertDocumentExtractedFieldAllPropertiesEquals(
            expectedDocumentExtractedField,
            getPersistedDocumentExtractedField(expectedDocumentExtractedField)
        );
    }

    protected void assertPersistedDocumentExtractedFieldToMatchUpdatableProperties(DocumentExtractedField expectedDocumentExtractedField) {
        assertDocumentExtractedFieldAllUpdatablePropertiesEquals(
            expectedDocumentExtractedField,
            getPersistedDocumentExtractedField(expectedDocumentExtractedField)
        );
    }
}
