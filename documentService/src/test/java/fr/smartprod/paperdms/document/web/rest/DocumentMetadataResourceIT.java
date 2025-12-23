package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentMetadataAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import fr.smartprod.paperdms.document.repository.DocumentMetadataRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMetadataMapper;
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
 * Integration tests for the {@link DocumentMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentMetadataResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final MetadataType DEFAULT_DATA_TYPE = MetadataType.STRING;
    private static final MetadataType UPDATED_DATA_TYPE = MetadataType.NUMBER;

    private static final Boolean DEFAULT_IS_SEARCHABLE = false;
    private static final Boolean UPDATED_IS_SEARCHABLE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    @Autowired
    private DocumentMetadataMapper documentMetadataMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMetadataMockMvc;

    private DocumentMetadata documentMetadata;

    private DocumentMetadata insertedDocumentMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentMetadata createEntity(EntityManager em) {
        DocumentMetadata documentMetadata = new DocumentMetadata()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .dataType(DEFAULT_DATA_TYPE)
            .isSearchable(DEFAULT_IS_SEARCHABLE)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        documentMetadata.setDocument(document);
        return documentMetadata;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentMetadata createUpdatedEntity(EntityManager em) {
        DocumentMetadata updatedDocumentMetadata = new DocumentMetadata()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createUpdatedEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        updatedDocumentMetadata.setDocument(document);
        return updatedDocumentMetadata;
    }

    @BeforeEach
    void initTest() {
        documentMetadata = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentMetadata != null) {
            documentMetadataRepository.delete(insertedDocumentMetadata);
            insertedDocumentMetadata = null;
        }
    }

    @Test
    @Transactional
    void createDocumentMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);
        var returnedDocumentMetadataDTO = om.readValue(
            restDocumentMetadataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentMetadataDTO.class
        );

        // Validate the DocumentMetadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentMetadata = documentMetadataMapper.toEntity(returnedDocumentMetadataDTO);
        assertDocumentMetadataUpdatableFieldsEquals(returnedDocumentMetadata, getPersistedDocumentMetadata(returnedDocumentMetadata));

        insertedDocumentMetadata = returnedDocumentMetadata;
    }

    @Test
    @Transactional
    void createDocumentMetadataWithExistingId() throws Exception {
        // Create the DocumentMetadata with an existing ID
        documentMetadata.setId(1L);
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentMetadata.setKey(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSearchableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentMetadata.setIsSearchable(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentMetadata.setCreatedDate(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get the documentMetadata
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, documentMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentMetadata.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.isSearchable").value(DEFAULT_IS_SEARCHABLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentMetadata() throws Exception {
        // Get the documentMetadata
        restDocumentMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentMetadata
        DocumentMetadata updatedDocumentMetadata = documentMetadataRepository.findById(documentMetadata.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentMetadata are not directly saved in db
        em.detach(updatedDocumentMetadata);
        updatedDocumentMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(updatedDocumentMetadata);

        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentMetadataToMatchAllProperties(updatedDocumentMetadata);
    }

    @Test
    @Transactional
    void putNonExistingDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentMetadata using partial update
        DocumentMetadata partialUpdatedDocumentMetadata = new DocumentMetadata();
        partialUpdatedDocumentMetadata.setId(documentMetadata.getId());

        partialUpdatedDocumentMetadata.value(UPDATED_VALUE);

        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentMetadata))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentMetadataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentMetadata, documentMetadata),
            getPersistedDocumentMetadata(documentMetadata)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentMetadata using partial update
        DocumentMetadata partialUpdatedDocumentMetadata = new DocumentMetadata();
        partialUpdatedDocumentMetadata.setId(documentMetadata.getId());

        partialUpdatedDocumentMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentMetadata))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentMetadataUpdatableFieldsEquals(
            partialUpdatedDocumentMetadata,
            getPersistedDocumentMetadata(partialUpdatedDocumentMetadata)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentMetadata
        restDocumentMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentMetadataRepository.count();
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

    protected DocumentMetadata getPersistedDocumentMetadata(DocumentMetadata documentMetadata) {
        return documentMetadataRepository.findById(documentMetadata.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentMetadataToMatchAllProperties(DocumentMetadata expectedDocumentMetadata) {
        assertDocumentMetadataAllPropertiesEquals(expectedDocumentMetadata, getPersistedDocumentMetadata(expectedDocumentMetadata));
    }

    protected void assertPersistedDocumentMetadataToMatchUpdatableProperties(DocumentMetadata expectedDocumentMetadata) {
        assertDocumentMetadataAllUpdatablePropertiesEquals(
            expectedDocumentMetadata,
            getPersistedDocumentMetadata(expectedDocumentMetadata)
        );
    }
}
