package fr.smartprod.paperdms.gateway.web.rest;

import static fr.smartprod.paperdms.gateway.domain.DocumentProcessAsserts.*;
import static fr.smartprod.paperdms.gateway.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.gateway.IntegrationTest;
import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.domain.enumeration.WorkflowInstanceStatus;
import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
import fr.smartprod.paperdms.gateway.repository.search.DocumentProcessSearchRepository;
import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import fr.smartprod.paperdms.gateway.service.mapper.DocumentProcessMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link DocumentProcessResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentProcessResourceIT {

    private static final WorkflowInstanceStatus DEFAULT_STATUS = WorkflowInstanceStatus.PENDING;
    private static final WorkflowInstanceStatus UPDATED_STATUS = WorkflowInstanceStatus.IN_PROGRESS;

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-processes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-processes/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentProcessRepository documentProcessRepository;

    @Autowired
    private DocumentProcessMapper documentProcessMapper;

    @Autowired
    private DocumentProcessSearchRepository documentProcessSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentProcessMockMvc;

    private DocumentProcess documentProcess;

    private DocumentProcess insertedDocumentProcess;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentProcess createEntity() {
        return new DocumentProcess().status(DEFAULT_STATUS).documentId(DEFAULT_DOCUMENT_ID).documentSha256(DEFAULT_DOCUMENT_SHA_256);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentProcess createUpdatedEntity() {
        return new DocumentProcess().status(UPDATED_STATUS).documentId(UPDATED_DOCUMENT_ID).documentSha256(UPDATED_DOCUMENT_SHA_256);
    }

    @BeforeEach
    void initTest() {
        documentProcess = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentProcess != null) {
            documentProcessRepository.delete(insertedDocumentProcess);
            documentProcessSearchRepository.delete(insertedDocumentProcess);
            insertedDocumentProcess = null;
        }
    }

    @Test
    @Transactional
    void createDocumentProcess() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);
        var returnedDocumentProcessDTO = om.readValue(
            restDocumentProcessMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentProcessDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentProcessDTO.class
        );

        // Validate the DocumentProcess in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentProcess = documentProcessMapper.toEntity(returnedDocumentProcessDTO);
        assertDocumentProcessUpdatableFieldsEquals(returnedDocumentProcess, getPersistedDocumentProcess(returnedDocumentProcess));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentProcess = returnedDocumentProcess;
    }

    @Test
    @Transactional
    void createDocumentProcessWithExistingId() throws Exception {
        // Create the DocumentProcess with an existing ID
        documentProcess.setId(1L);
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentProcessDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        // set the field null
        documentProcess.setDocumentId(null);

        // Create the DocumentProcess, which fails.
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        restDocumentProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        // set the field null
        documentProcess.setDocumentSha256(null);

        // Create the DocumentProcess, which fails.
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        restDocumentProcessMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentProcessDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentProcesses() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);

        // Get all the documentProcessList
        restDocumentProcessMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));
    }

    @Test
    @Transactional
    void getDocumentProcess() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);

        // Get the documentProcess
        restDocumentProcessMockMvc
            .perform(get(ENTITY_API_URL_ID, documentProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentProcess.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256));
    }

    @Test
    @Transactional
    void getNonExistingDocumentProcess() throws Exception {
        // Get the documentProcess
        restDocumentProcessMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentProcess() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentProcessSearchRepository.save(documentProcess);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());

        // Update the documentProcess
        DocumentProcess updatedDocumentProcess = documentProcessRepository.findById(documentProcess.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentProcess are not directly saved in db
        em.detach(updatedDocumentProcess);
        updatedDocumentProcess.status(UPDATED_STATUS).documentId(UPDATED_DOCUMENT_ID).documentSha256(UPDATED_DOCUMENT_SHA_256);
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(updatedDocumentProcess);

        restDocumentProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentProcessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentProcessDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentProcessToMatchAllProperties(updatedDocumentProcess);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentProcess> documentProcessSearchList = Streamable.of(documentProcessSearchRepository.findAll()).toList();
                DocumentProcess testDocumentProcessSearch = documentProcessSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentProcessAllPropertiesEquals(testDocumentProcessSearch, updatedDocumentProcess);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentProcessDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentProcessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentProcessWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentProcess using partial update
        DocumentProcess partialUpdatedDocumentProcess = new DocumentProcess();
        partialUpdatedDocumentProcess.setId(documentProcess.getId());

        partialUpdatedDocumentProcess.status(UPDATED_STATUS);

        restDocumentProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentProcess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentProcess))
            )
            .andExpect(status().isOk());

        // Validate the DocumentProcess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentProcessUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentProcess, documentProcess),
            getPersistedDocumentProcess(documentProcess)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentProcessWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentProcess using partial update
        DocumentProcess partialUpdatedDocumentProcess = new DocumentProcess();
        partialUpdatedDocumentProcess.setId(documentProcess.getId());

        partialUpdatedDocumentProcess.status(UPDATED_STATUS).documentId(UPDATED_DOCUMENT_ID).documentSha256(UPDATED_DOCUMENT_SHA_256);

        restDocumentProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentProcess.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentProcess))
            )
            .andExpect(status().isOk());

        // Validate the DocumentProcess in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentProcessUpdatableFieldsEquals(
            partialUpdatedDocumentProcess,
            getPersistedDocumentProcess(partialUpdatedDocumentProcess)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentProcessDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentProcessDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentProcess() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        documentProcess.setId(longCount.incrementAndGet());

        // Create the DocumentProcess
        DocumentProcessDTO documentProcessDTO = documentProcessMapper.toDto(documentProcess);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentProcessMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentProcessDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentProcess in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentProcess() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);
        documentProcessRepository.save(documentProcess);
        documentProcessSearchRepository.save(documentProcess);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentProcess
        restDocumentProcessMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentProcess.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentProcessSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentProcess() throws Exception {
        // Initialize the database
        insertedDocumentProcess = documentProcessRepository.saveAndFlush(documentProcess);
        documentProcessSearchRepository.save(documentProcess);

        // Search the documentProcess
        restDocumentProcessMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentProcess.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentProcess.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));
    }

    protected long getRepositoryCount() {
        return documentProcessRepository.count();
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

    protected DocumentProcess getPersistedDocumentProcess(DocumentProcess documentProcess) {
        return documentProcessRepository.findById(documentProcess.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentProcessToMatchAllProperties(DocumentProcess expectedDocumentProcess) {
        assertDocumentProcessAllPropertiesEquals(expectedDocumentProcess, getPersistedDocumentProcess(expectedDocumentProcess));
    }

    protected void assertPersistedDocumentProcessToMatchUpdatableProperties(DocumentProcess expectedDocumentProcess) {
        assertDocumentProcessAllUpdatablePropertiesEquals(expectedDocumentProcess, getPersistedDocumentProcess(expectedDocumentProcess));
    }
}
