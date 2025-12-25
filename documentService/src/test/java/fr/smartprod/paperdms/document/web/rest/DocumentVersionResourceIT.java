package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentVersionAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentVersionMapper;
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
 * Integration tests for the {@link DocumentVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentVersionResourceIT {

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    private DocumentVersionMapper documentVersionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentVersionMockMvc;

    private DocumentVersion documentVersion;

    private DocumentVersion insertedDocumentVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentVersion createEntity(EntityManager em) {
        DocumentVersion documentVersion = new DocumentVersion()
            .versionNumber(DEFAULT_VERSION_NUMBER)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        documentVersion.setDocument(document);
        return documentVersion;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentVersion createUpdatedEntity(EntityManager em) {
        DocumentVersion updatedDocumentVersion = new DocumentVersion()
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createUpdatedEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        updatedDocumentVersion.setDocument(document);
        return updatedDocumentVersion;
    }

    @BeforeEach
    void initTest() {
        documentVersion = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentVersion != null) {
            documentVersionRepository.delete(insertedDocumentVersion);
            insertedDocumentVersion = null;
        }
    }

    @Test
    @Transactional
    void createDocumentVersion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);
        var returnedDocumentVersionDTO = om.readValue(
            restDocumentVersionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentVersionDTO.class
        );

        // Validate the DocumentVersion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentVersion = documentVersionMapper.toEntity(returnedDocumentVersionDTO);
        assertDocumentVersionUpdatableFieldsEquals(returnedDocumentVersion, getPersistedDocumentVersion(returnedDocumentVersion));

        insertedDocumentVersion = returnedDocumentVersion;
    }

    @Test
    @Transactional
    void createDocumentVersionWithExistingId() throws Exception {
        // Create the DocumentVersion with an existing ID
        documentVersion.setId(1L);
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVersionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setVersionNumber(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setSha256(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.sets3Key(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setFileSize(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUploadDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setUploadDate(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setIsActive(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentVersion.setCreatedBy(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentVersions() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get the documentVersion
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, documentVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentVersion.getId().intValue()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingDocumentVersion() throws Exception {
        // Get the documentVersion
        restDocumentVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentVersion
        DocumentVersion updatedDocumentVersion = documentVersionRepository.findById(documentVersion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentVersion are not directly saved in db
        em.detach(updatedDocumentVersion);
        updatedDocumentVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(updatedDocumentVersion);

        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentVersionToMatchAllProperties(updatedDocumentVersion);
    }

    @Test
    @Transactional
    void putNonExistingDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentVersionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentVersion using partial update
        DocumentVersion partialUpdatedDocumentVersion = new DocumentVersion();
        partialUpdatedDocumentVersion.setId(documentVersion.getId());

        partialUpdatedDocumentVersion.sha256(UPDATED_SHA_256).s3Key(UPDATED_S_3_KEY).createdBy(UPDATED_CREATED_BY);

        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentVersion))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentVersionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentVersion, documentVersion),
            getPersistedDocumentVersion(documentVersion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentVersionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentVersion using partial update
        DocumentVersion partialUpdatedDocumentVersion = new DocumentVersion();
        partialUpdatedDocumentVersion.setId(documentVersion.getId());

        partialUpdatedDocumentVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);

        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentVersion))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentVersionUpdatableFieldsEquals(
            partialUpdatedDocumentVersion,
            getPersistedDocumentVersion(partialUpdatedDocumentVersion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentVersion
        restDocumentVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentVersionRepository.count();
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

    protected DocumentVersion getPersistedDocumentVersion(DocumentVersion documentVersion) {
        return documentVersionRepository.findById(documentVersion.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentVersionToMatchAllProperties(DocumentVersion expectedDocumentVersion) {
        assertDocumentVersionAllPropertiesEquals(expectedDocumentVersion, getPersistedDocumentVersion(expectedDocumentVersion));
    }

    protected void assertPersistedDocumentVersionToMatchUpdatableProperties(DocumentVersion expectedDocumentVersion) {
        assertDocumentVersionAllUpdatablePropertiesEquals(expectedDocumentVersion, getPersistedDocumentVersion(expectedDocumentVersion));
    }
}
