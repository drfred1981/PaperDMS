package fr.smartprod.paperdms.archive.web.rest;

import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentAsserts.*;
import static fr.smartprod.paperdms.archive.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.archive.IntegrationTest;
import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveDocumentMapper;
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
 * Integration tests for the {@link ArchiveDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArchiveDocumentResourceIT {

    private static final Long DEFAULT_ARCHIVE_JOB_ID = 1L;
    private static final Long UPDATED_ARCHIVE_JOB_ID = 2L;

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ARCHIVE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final Instant DEFAULT_ADDED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/archive-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArchiveDocumentRepository archiveDocumentRepository;

    @Autowired
    private ArchiveDocumentMapper archiveDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchiveDocumentMockMvc;

    private ArchiveDocument archiveDocument;

    private ArchiveDocument insertedArchiveDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveDocument createEntity(EntityManager em) {
        ArchiveDocument archiveDocument = new ArchiveDocument()
            .archiveJobId(DEFAULT_ARCHIVE_JOB_ID)
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .originalPath(DEFAULT_ORIGINAL_PATH)
            .archivePath(DEFAULT_ARCHIVE_PATH)
            .fileSize(DEFAULT_FILE_SIZE)
            .addedDate(DEFAULT_ADDED_DATE);
        // Add required entity
        ArchiveJob archiveJob;
        if (TestUtil.findAll(em, ArchiveJob.class).isEmpty()) {
            archiveJob = ArchiveJobResourceIT.createEntity();
            em.persist(archiveJob);
            em.flush();
        } else {
            archiveJob = TestUtil.findAll(em, ArchiveJob.class).get(0);
        }
        archiveDocument.setArchiveJob(archiveJob);
        return archiveDocument;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveDocument createUpdatedEntity(EntityManager em) {
        ArchiveDocument updatedArchiveDocument = new ArchiveDocument()
            .archiveJobId(UPDATED_ARCHIVE_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);
        // Add required entity
        ArchiveJob archiveJob;
        if (TestUtil.findAll(em, ArchiveJob.class).isEmpty()) {
            archiveJob = ArchiveJobResourceIT.createUpdatedEntity();
            em.persist(archiveJob);
            em.flush();
        } else {
            archiveJob = TestUtil.findAll(em, ArchiveJob.class).get(0);
        }
        updatedArchiveDocument.setArchiveJob(archiveJob);
        return updatedArchiveDocument;
    }

    @BeforeEach
    void initTest() {
        archiveDocument = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedArchiveDocument != null) {
            archiveDocumentRepository.delete(insertedArchiveDocument);
            insertedArchiveDocument = null;
        }
    }

    @Test
    @Transactional
    void createArchiveDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);
        var returnedArchiveDocumentDTO = om.readValue(
            restArchiveDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArchiveDocumentDTO.class
        );

        // Validate the ArchiveDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArchiveDocument = archiveDocumentMapper.toEntity(returnedArchiveDocumentDTO);
        assertArchiveDocumentUpdatableFieldsEquals(returnedArchiveDocument, getPersistedArchiveDocument(returnedArchiveDocument));

        insertedArchiveDocument = returnedArchiveDocument;
    }

    @Test
    @Transactional
    void createArchiveDocumentWithExistingId() throws Exception {
        // Create the ArchiveDocument with an existing ID
        archiveDocument.setId(1L);
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkArchiveJobIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setArchiveJobId(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setDocumentId(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setDocumentSha256(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setAddedDate(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArchiveDocuments() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].archiveJobId").value(hasItem(DEFAULT_ARCHIVE_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].originalPath").value(hasItem(DEFAULT_ORIGINAL_PATH)))
            .andExpect(jsonPath("$.[*].archivePath").value(hasItem(DEFAULT_ARCHIVE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get the archiveDocument
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, archiveDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archiveDocument.getId().intValue()))
            .andExpect(jsonPath("$.archiveJobId").value(DEFAULT_ARCHIVE_JOB_ID.intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.originalPath").value(DEFAULT_ORIGINAL_PATH))
            .andExpect(jsonPath("$.archivePath").value(DEFAULT_ARCHIVE_PATH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingArchiveDocument() throws Exception {
        // Get the archiveDocument
        restArchiveDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument
        ArchiveDocument updatedArchiveDocument = archiveDocumentRepository.findById(archiveDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArchiveDocument are not directly saved in db
        em.detach(updatedArchiveDocument);
        updatedArchiveDocument
            .archiveJobId(UPDATED_ARCHIVE_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(updatedArchiveDocument);

        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArchiveDocumentToMatchAllProperties(updatedArchiveDocument);
    }

    @Test
    @Transactional
    void putNonExistingArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchiveDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument using partial update
        ArchiveDocument partialUpdatedArchiveDocument = new ArchiveDocument();
        partialUpdatedArchiveDocument.setId(archiveDocument.getId());

        partialUpdatedArchiveDocument.documentId(UPDATED_DOCUMENT_ID).documentSha256(UPDATED_DOCUMENT_SHA_256).fileSize(UPDATED_FILE_SIZE);

        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveDocument))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArchiveDocument, archiveDocument),
            getPersistedArchiveDocument(archiveDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateArchiveDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument using partial update
        ArchiveDocument partialUpdatedArchiveDocument = new ArchiveDocument();
        partialUpdatedArchiveDocument.setId(archiveDocument.getId());

        partialUpdatedArchiveDocument
            .archiveJobId(UPDATED_ARCHIVE_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);

        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveDocument))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveDocumentUpdatableFieldsEquals(
            partialUpdatedArchiveDocument,
            getPersistedArchiveDocument(partialUpdatedArchiveDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the archiveDocument
        restArchiveDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, archiveDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return archiveDocumentRepository.count();
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

    protected ArchiveDocument getPersistedArchiveDocument(ArchiveDocument archiveDocument) {
        return archiveDocumentRepository.findById(archiveDocument.getId()).orElseThrow();
    }

    protected void assertPersistedArchiveDocumentToMatchAllProperties(ArchiveDocument expectedArchiveDocument) {
        assertArchiveDocumentAllPropertiesEquals(expectedArchiveDocument, getPersistedArchiveDocument(expectedArchiveDocument));
    }

    protected void assertPersistedArchiveDocumentToMatchUpdatableProperties(ArchiveDocument expectedArchiveDocument) {
        assertArchiveDocumentAllUpdatablePropertiesEquals(expectedArchiveDocument, getPersistedArchiveDocument(expectedArchiveDocument));
    }
}
