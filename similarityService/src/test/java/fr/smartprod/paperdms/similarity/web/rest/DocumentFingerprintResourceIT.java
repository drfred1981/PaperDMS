package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.DocumentFingerprintAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.DocumentFingerprint;
import fr.smartprod.paperdms.similarity.domain.enumeration.FingerprintType;
import fr.smartprod.paperdms.similarity.repository.DocumentFingerprintRepository;
import fr.smartprod.paperdms.similarity.repository.search.DocumentFingerprintSearchRepository;
import fr.smartprod.paperdms.similarity.service.dto.DocumentFingerprintDTO;
import fr.smartprod.paperdms.similarity.service.mapper.DocumentFingerprintMapper;
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
 * Integration tests for the {@link DocumentFingerprintResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentFingerprintResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final FingerprintType DEFAULT_FINGERPRINT_TYPE = FingerprintType.HASH;
    private static final FingerprintType UPDATED_FINGERPRINT_TYPE = FingerprintType.PERCEPTUAL;

    private static final String DEFAULT_FINGERPRINT = "AAAAAAAAAA";
    private static final String UPDATED_FINGERPRINT = "BBBBBBBBBB";

    private static final String DEFAULT_VECTOR_EMBEDDING = "AAAAAAAAAA";
    private static final String UPDATED_VECTOR_EMBEDDING = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_COMPUTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPUTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-fingerprints";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-fingerprints/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentFingerprintRepository documentFingerprintRepository;

    @Autowired
    private DocumentFingerprintMapper documentFingerprintMapper;

    @Autowired
    private DocumentFingerprintSearchRepository documentFingerprintSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentFingerprintMockMvc;

    private DocumentFingerprint documentFingerprint;

    private DocumentFingerprint insertedDocumentFingerprint;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentFingerprint createEntity() {
        return new DocumentFingerprint()
            .documentId(DEFAULT_DOCUMENT_ID)
            .fingerprintType(DEFAULT_FINGERPRINT_TYPE)
            .fingerprint(DEFAULT_FINGERPRINT)
            .vectorEmbedding(DEFAULT_VECTOR_EMBEDDING)
            .metadata(DEFAULT_METADATA)
            .computedDate(DEFAULT_COMPUTED_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentFingerprint createUpdatedEntity() {
        return new DocumentFingerprint()
            .documentId(UPDATED_DOCUMENT_ID)
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        documentFingerprint = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentFingerprint != null) {
            documentFingerprintRepository.delete(insertedDocumentFingerprint);
            documentFingerprintSearchRepository.delete(insertedDocumentFingerprint);
            insertedDocumentFingerprint = null;
        }
    }

    @Test
    @Transactional
    void createDocumentFingerprint() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);
        var returnedDocumentFingerprintDTO = om.readValue(
            restDocumentFingerprintMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentFingerprintDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentFingerprintDTO.class
        );

        // Validate the DocumentFingerprint in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentFingerprint = documentFingerprintMapper.toEntity(returnedDocumentFingerprintDTO);
        assertDocumentFingerprintUpdatableFieldsEquals(
            returnedDocumentFingerprint,
            getPersistedDocumentFingerprint(returnedDocumentFingerprint)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentFingerprint = returnedDocumentFingerprint;
    }

    @Test
    @Transactional
    void createDocumentFingerprintWithExistingId() throws Exception {
        // Create the DocumentFingerprint with an existing ID
        documentFingerprint.setId(1L);
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentFingerprintDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        // set the field null
        documentFingerprint.setDocumentId(null);

        // Create the DocumentFingerprint, which fails.
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        restDocumentFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentFingerprintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkComputedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        // set the field null
        documentFingerprint.setComputedDate(null);

        // Create the DocumentFingerprint, which fails.
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        restDocumentFingerprintMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentFingerprintDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentFingerprints() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);

        // Get all the documentFingerprintList
        restDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentFingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fingerprintType").value(hasItem(DEFAULT_FINGERPRINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT)))
            .andExpect(jsonPath("$.[*].vectorEmbedding").value(hasItem(DEFAULT_VECTOR_EMBEDDING)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);

        // Get the documentFingerprint
        restDocumentFingerprintMockMvc
            .perform(get(ENTITY_API_URL_ID, documentFingerprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentFingerprint.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.fingerprintType").value(DEFAULT_FINGERPRINT_TYPE.toString()))
            .andExpect(jsonPath("$.fingerprint").value(DEFAULT_FINGERPRINT))
            .andExpect(jsonPath("$.vectorEmbedding").value(DEFAULT_VECTOR_EMBEDDING))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.computedDate").value(DEFAULT_COMPUTED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentFingerprint() throws Exception {
        // Get the documentFingerprint
        restDocumentFingerprintMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentFingerprintSearchRepository.save(documentFingerprint);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());

        // Update the documentFingerprint
        DocumentFingerprint updatedDocumentFingerprint = documentFingerprintRepository.findById(documentFingerprint.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentFingerprint are not directly saved in db
        em.detach(updatedDocumentFingerprint);
        updatedDocumentFingerprint
            .documentId(UPDATED_DOCUMENT_ID)
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(updatedDocumentFingerprint);

        restDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentFingerprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentFingerprintToMatchAllProperties(updatedDocumentFingerprint);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentFingerprint> documentFingerprintSearchList = Streamable.of(
                    documentFingerprintSearchRepository.findAll()
                ).toList();
                DocumentFingerprint testDocumentFingerprintSearch = documentFingerprintSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentFingerprintAllPropertiesEquals(testDocumentFingerprintSearch, updatedDocumentFingerprint);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentFingerprintDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentFingerprintDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentFingerprintWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentFingerprint using partial update
        DocumentFingerprint partialUpdatedDocumentFingerprint = new DocumentFingerprint();
        partialUpdatedDocumentFingerprint.setId(documentFingerprint.getId());

        partialUpdatedDocumentFingerprint
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA);

        restDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFingerprint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentFingerprintUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentFingerprint, documentFingerprint),
            getPersistedDocumentFingerprint(documentFingerprint)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentFingerprintWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentFingerprint using partial update
        DocumentFingerprint partialUpdatedDocumentFingerprint = new DocumentFingerprint();
        partialUpdatedDocumentFingerprint.setId(documentFingerprint.getId());

        partialUpdatedDocumentFingerprint
            .documentId(UPDATED_DOCUMENT_ID)
            .fingerprintType(UPDATED_FINGERPRINT_TYPE)
            .fingerprint(UPDATED_FINGERPRINT)
            .vectorEmbedding(UPDATED_VECTOR_EMBEDDING)
            .metadata(UPDATED_METADATA)
            .computedDate(UPDATED_COMPUTED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentFingerprint.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentFingerprint))
            )
            .andExpect(status().isOk());

        // Validate the DocumentFingerprint in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentFingerprintUpdatableFieldsEquals(
            partialUpdatedDocumentFingerprint,
            getPersistedDocumentFingerprint(partialUpdatedDocumentFingerprint)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentFingerprintDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentFingerprint() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        documentFingerprint.setId(longCount.incrementAndGet());

        // Create the DocumentFingerprint
        DocumentFingerprintDTO documentFingerprintDTO = documentFingerprintMapper.toDto(documentFingerprint);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentFingerprintMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentFingerprintDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentFingerprint in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);
        documentFingerprintRepository.save(documentFingerprint);
        documentFingerprintSearchRepository.save(documentFingerprint);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentFingerprint
        restDocumentFingerprintMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentFingerprint.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentFingerprintSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentFingerprint() throws Exception {
        // Initialize the database
        insertedDocumentFingerprint = documentFingerprintRepository.saveAndFlush(documentFingerprint);
        documentFingerprintSearchRepository.save(documentFingerprint);

        // Search the documentFingerprint
        restDocumentFingerprintMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentFingerprint.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentFingerprint.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fingerprintType").value(hasItem(DEFAULT_FINGERPRINT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].fingerprint").value(hasItem(DEFAULT_FINGERPRINT.toString())))
            .andExpect(jsonPath("$.[*].vectorEmbedding").value(hasItem(DEFAULT_VECTOR_EMBEDDING.toString())))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA.toString())))
            .andExpect(jsonPath("$.[*].computedDate").value(hasItem(DEFAULT_COMPUTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    protected long getRepositoryCount() {
        return documentFingerprintRepository.count();
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

    protected DocumentFingerprint getPersistedDocumentFingerprint(DocumentFingerprint documentFingerprint) {
        return documentFingerprintRepository.findById(documentFingerprint.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentFingerprintToMatchAllProperties(DocumentFingerprint expectedDocumentFingerprint) {
        assertDocumentFingerprintAllPropertiesEquals(
            expectedDocumentFingerprint,
            getPersistedDocumentFingerprint(expectedDocumentFingerprint)
        );
    }

    protected void assertPersistedDocumentFingerprintToMatchUpdatableProperties(DocumentFingerprint expectedDocumentFingerprint) {
        assertDocumentFingerprintAllUpdatablePropertiesEquals(
            expectedDocumentFingerprint,
            getPersistedDocumentFingerprint(expectedDocumentFingerprint)
        );
    }
}
