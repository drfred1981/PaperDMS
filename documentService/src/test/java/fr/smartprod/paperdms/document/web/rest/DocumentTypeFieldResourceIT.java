package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTypeFieldAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeFieldMapper;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTypeFieldRepository documentTypeFieldRepository;

    @Autowired
    private DocumentTypeFieldMapper documentTypeFieldMapper;

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
            insertedDocumentTypeField = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTypeField() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedDocumentTypeField = returnedDocumentTypeField;
    }

    @Test
    @Transactional
    void createDocumentTypeFieldWithExistingId() throws Exception {
        // Create the DocumentTypeField with an existing ID
        documentTypeField.setId(1L);
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFieldKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTypeField.setFieldKey(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFieldLabelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTypeField.setFieldLabel(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTypeField.setIsRequired(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSearchableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTypeField.setIsSearchable(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTypeField.setCreatedDate(null);

        // Create the DocumentTypeField, which fails.
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        restDocumentTypeFieldMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
    }

    @Test
    @Transactional
    void putNonExistingDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTypeField() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTypeField.setId(longCount.incrementAndGet());

        // Create the DocumentTypeField
        DocumentTypeFieldDTO documentTypeFieldDTO = documentTypeFieldMapper.toDto(documentTypeField);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeFieldMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTypeFieldDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTypeField in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentTypeField() throws Exception {
        // Initialize the database
        insertedDocumentTypeField = documentTypeFieldRepository.saveAndFlush(documentTypeField);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentTypeField
        restDocumentTypeFieldMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTypeField.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
