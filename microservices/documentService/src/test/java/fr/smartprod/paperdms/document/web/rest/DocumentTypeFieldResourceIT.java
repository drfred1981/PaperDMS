package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeFieldSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeFieldMapper;
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
 * Integration tests for the {@link DocumentTypeFieldResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTypeFieldResourceIT {

    private static final String DEFAULT_FIELD_KEY = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_FIELD_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_FIELD_LABEL = "BBBBBBBBBB";

    private static final MetadataType DEFAULT_DATA_TYPE = MetadataType.STRING;
    private static final MetadataType UPDATED_DATA_TYPE = MetadataType.NUMBER;

    private static final Boolean DEFAULT_IS_REQUIRED = false;
    private static final Boolean UPDATED_IS_REQUIRED = true;

    private static final Boolean DEFAULT_IS_SEARCHABLE = false;
    private static final Boolean UPDATED_IS_SEARCHABLE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-type-fields";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-type-fields/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTypeFieldRepository documentTypeFieldRepository;

    @Autowired
    private DocumentTypeFieldMapper documentTypeFieldMapper;

    @Autowired
    private DocumentTypeFieldSearchRepository documentTypeFieldSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTypeFieldMockMvc;

    private DocumentTypeField documentTypeField;

    private DocumentTypeField insertedDocumentTypeField;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTypeField createEntity() {
        return new DocumentTypeField()
            .fieldKey(DEFAULT_FIELD_KEY)
            .fieldLabel(DEFAULT_FIELD_LABEL)
            .dataType(DEFAULT_DATA_TYPE)
            .isRequired(DEFAULT_IS_REQUIRED)
            .isSearchable(DEFAULT_IS_SEARCHABLE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTypeField createUpdatedEntity() {
        return new DocumentTypeField()
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .dataType(UPDATED_DATA_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentTypeField = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentTypeField != null) {
            documentTypeFieldRepository.delete(insertedDocumentTypeField);
            documentTypeFieldSearchRepository.delete(insertedDocumentTypeField);
            insertedDocumentTypeField = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTypeField() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);
        var returnedDocumentTypeFieldDTO = om.readValue(
            restDocumentTypeFieldMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentTypeFieldDTO.class
        );

        // Validate the DocumentTypeField in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentTypeField = documentTypeFieldMapper.toEntity(returnedDocumentTypeFieldDTO);
        assertDocumentTypeFieldUpdatableFieldsEquals(returnedDocumentTypeField, getPersistedDocumentTypeField(returnedDocumentTypeField));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentTypeField = returnedDocumentTypeField;
    }

    @Test
    @Transactional
    void createDocumentTypeFieldWithExistingId() throws Exception {
        // Create the DocumentTypeField with an existing ID
        documentTypeField.setId(1L);
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFieldKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // set the field null
        documentTypeField.setFieldKey(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFieldLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // set the field null
        documentTypeField.setFieldLabel(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // set the field null
        documentTypeField.setIsRequired(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSearchableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // set the field null
        documentTypeField.setIsSearchable(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        // set the field null
        documentTypeField.setCreatedDate(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFields() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTypeField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldLabel").value(hasItem(DEFAULT_FIELD_LABEL)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentTypeField() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get the documentTypeField
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL_ID, documentTypeField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentTypeField.getId().intValue()))
            .andExpect(jsonPath("$.fieldKey").value(DEFAULT_FIELD_KEY))
            .andExpect(jsonPath("$.fieldLabel").value(DEFAULT_FIELD_LABEL))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.isSearchable").value(DEFAULT_IS_SEARCHABLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentTypeFieldsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        Long id = documentTypeField.getId();

        defaultDocumentTypeFieldFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentTypeFieldFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentTypeFieldFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldKey equals to
        defaultDocumentTypeFieldFiltering("fieldKey.equals=" + DEFAULT_FIELD_KEY, "fieldKey.equals=" + UPDATED_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldKey in
        defaultDocumentTypeFieldFiltering("fieldKey.in=" + DEFAULT_FIELD_KEY + "," + UPDATED_FIELD_KEY, "fieldKey.in=" + UPDATED_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldKey is not null
        defaultDocumentTypeFieldFiltering("fieldKey.specified=true", "fieldKey.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldKey contains
        defaultDocumentTypeFieldFiltering("fieldKey.contains=" + DEFAULT_FIELD_KEY, "fieldKey.contains=" + UPDATED_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldKey does not contain
        defaultDocumentTypeFieldFiltering("fieldKey.doesNotContain=" + UPDATED_FIELD_KEY, "fieldKey.doesNotContain=" + DEFAULT_FIELD_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldLabel equals to
        defaultDocumentTypeFieldFiltering("fieldLabel.equals=" + DEFAULT_FIELD_LABEL, "fieldLabel.equals=" + UPDATED_FIELD_LABEL);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldLabelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldLabel in
        defaultDocumentTypeFieldFiltering(
            "fieldLabel.in=" + DEFAULT_FIELD_LABEL + "," + UPDATED_FIELD_LABEL,
            "fieldLabel.in=" + UPDATED_FIELD_LABEL
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldLabel is not null
        defaultDocumentTypeFieldFiltering("fieldLabel.specified=true", "fieldLabel.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldLabelContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldLabel contains
        defaultDocumentTypeFieldFiltering("fieldLabel.contains=" + DEFAULT_FIELD_LABEL, "fieldLabel.contains=" + UPDATED_FIELD_LABEL);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByFieldLabelNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where fieldLabel does not contain
        defaultDocumentTypeFieldFiltering(
            "fieldLabel.doesNotContain=" + UPDATED_FIELD_LABEL,
            "fieldLabel.doesNotContain=" + DEFAULT_FIELD_LABEL
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByDataTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where dataType equals to
        defaultDocumentTypeFieldFiltering("dataType.equals=" + DEFAULT_DATA_TYPE, "dataType.equals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByDataTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where dataType in
        defaultDocumentTypeFieldFiltering("dataType.in=" + DEFAULT_DATA_TYPE + "," + UPDATED_DATA_TYPE, "dataType.in=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByDataTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where dataType is not null
        defaultDocumentTypeFieldFiltering("dataType.specified=true", "dataType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsRequiredIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isRequired equals to
        defaultDocumentTypeFieldFiltering("isRequired.equals=" + DEFAULT_IS_REQUIRED, "isRequired.equals=" + UPDATED_IS_REQUIRED);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsRequiredIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isRequired in
        defaultDocumentTypeFieldFiltering(
            "isRequired.in=" + DEFAULT_IS_REQUIRED + "," + UPDATED_IS_REQUIRED,
            "isRequired.in=" + UPDATED_IS_REQUIRED
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsRequiredIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isRequired is not null
        defaultDocumentTypeFieldFiltering("isRequired.specified=true", "isRequired.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsSearchableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isSearchable equals to
        defaultDocumentTypeFieldFiltering("isSearchable.equals=" + DEFAULT_IS_SEARCHABLE, "isSearchable.equals=" + UPDATED_IS_SEARCHABLE);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsSearchableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isSearchable in
        defaultDocumentTypeFieldFiltering(
            "isSearchable.in=" + DEFAULT_IS_SEARCHABLE + "," + UPDATED_IS_SEARCHABLE,
            "isSearchable.in=" + UPDATED_IS_SEARCHABLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByIsSearchableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where isSearchable is not null
        defaultDocumentTypeFieldFiltering("isSearchable.specified=true", "isSearchable.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where createdDate equals to
        defaultDocumentTypeFieldFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where createdDate in
        defaultDocumentTypeFieldFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        // Get all the documentTypeFieldList where createdDate is not null
        defaultDocumentTypeFieldFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypeFieldsByDocumentTypeIsEqualToSomething() throws Exception {
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentTypeFieldRepository.saveAndFlush(documentTypeField);
            documentType = DocumentTypeResourceIT.createEntity();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        em.persist(documentType);
        em.flush();
        documentTypeField.setDocumentType(documentType);
        documentTypeFieldRepository.saveAndFlush(documentTypeField);
        Long documentTypeId = documentType.getId();
        // Get all the documentTypeFieldList where documentType equals to documentTypeId
        defaultDocumentTypeFieldShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the documentTypeFieldList where documentType equals to (documentTypeId + 1)
        defaultDocumentTypeFieldShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }

    private void defaultDocumentTypeFieldFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentTypeFieldShouldBeFound(shouldBeFound);
        defaultDocumentTypeFieldShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTypeFieldShouldBeFound(String filter) throws Exception {
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTypeField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldLabel").value(hasItem(DEFAULT_FIELD_LABEL)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTypeFieldShouldNotBeFound(String filter) throws Exception {
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentTypeField() throws Exception {
        // Get the documentTypeField
        restDocumentTypeFieldMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentTypeField() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTypeFieldSearchRepository.save(documentTypeField);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());

        // Update the documentTypeField
        DocumentTypeField updatedDocumentTypeField = documentTypeFieldRepository.findById(documentTypeField.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentTypeField are not directly saved in db
        em.detach(updatedDocumentTypeField);
        updatedDocumentTypeField
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .dataType(UPDATED_DATA_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(updatedDocumentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTypeFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeFieldDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentTypeFieldToMatchAllProperties(updatedDocumentTypeField);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentTypeField> documentTypeFieldSearchList = Streamable.of(documentTypeFieldSearchRepository.findAll()).toList();
                DocumentTypeField testDocumentTypeFieldSearch = documentTypeFieldSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentTypeFieldAllPropertiesEquals(testDocumentTypeFieldSearch, updatedDocumentTypeField);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTypeFieldDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTypeFieldWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTypeField using partial update
        DocumentTypeField partialUpdatedDocumentTypeField = new DocumentTypeField();
        partialUpdatedDocumentTypeField.setId(documentTypeField.getId());

        restDocumentTypeFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTypeField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTypeField))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTypeField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeFieldUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentTypeField, documentTypeField),
            getPersistedDocumentTypeField(documentTypeField)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentTypeFieldWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTypeField using partial update
        DocumentTypeField partialUpdatedDocumentTypeField = new DocumentTypeField();
        partialUpdatedDocumentTypeField.setId(documentTypeField.getId());

        partialUpdatedDocumentTypeField
            .fieldKey(UPDATED_FIELD_KEY)
            .fieldLabel(UPDATED_FIELD_LABEL)
            .dataType(UPDATED_DATA_TYPE)
            .isRequired(UPDATED_IS_REQUIRED)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentTypeFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTypeField.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTypeField))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTypeField in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeFieldUpdatableFieldsEquals(
            partialUpdatedDocumentTypeField,
            getPersistedDocumentTypeField(partialUpdatedDocumentTypeField)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentTypeFieldDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTypeFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTypeFieldDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentTypeField() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);
        documentTypeFieldRepository.save(documentTypeField);
        documentTypeFieldSearchRepository.save(documentTypeField);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentTypeField
        restDocumentTypeFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTypeField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeFieldSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentTypeField() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);
        documentTypeFieldSearchRepository.save(documentTypeField);

        // Search the documentTypeField
        restDocumentTypeFieldMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentTypeField.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTypeField.getId().intValue())))
            .andExpect(jsonPath("$.[*].fieldKey").value(hasItem(DEFAULT_FIELD_KEY)))
            .andExpect(jsonPath("$.[*].fieldLabel").value(hasItem(DEFAULT_FIELD_LABEL)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentTypeFieldRepository.count();
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

    protected DocumentTypeField getPersistedDocumentTypeField(DocumentTypeField documentTypeField) {
        return documentTypeFieldRepository.findById(documentTypeField.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentTypeFieldToMatchAllProperties(DocumentTypeField expectedDocumentTypeField) {
        assertDocumentTypeFieldAllPropertiesEquals(expectedDocumentTypeField, getPersistedDocumentTypeField(expectedDocumentTypeField));
    }

    protected void assertPersistedDocumentTypeFieldToMatchUpdatableProperties(DocumentTypeField expectedDocumentTypeField) {
        assertDocumentTypeFieldAllUpdatablePropertiesEquals(
            expectedDocumentTypeField,
            getPersistedDocumentTypeField(expectedDocumentTypeField)
        );
    }
}
