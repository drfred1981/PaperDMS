package fr.smartprod.paperdms.export.web.rest;

import static fr.smartprod.paperdms.export.domain.ExportResultAsserts.*;
import static fr.smartprod.paperdms.export.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.export.IntegrationTest;
import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.domain.ExportResult;
import fr.smartprod.paperdms.export.domain.enumeration.ExportResultStatus;
import fr.smartprod.paperdms.export.repository.ExportResultRepository;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import fr.smartprod.paperdms.export.service.mapper.ExportResultMapper;
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
 * Integration tests for the {@link ExportResultResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ExportResultResourceIT {

    private static final Long DEFAULT_EXPORT_JOB_ID = 1L;
    private static final Long UPDATED_EXPORT_JOB_ID = 2L;

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EXPORTED_PATH = "AAAAAAAAAA";
    private static final String UPDATED_EXPORTED_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_EXPORTED_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_EXPORTED_FILE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_EXPORT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_EXPORT_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final ExportResultStatus DEFAULT_STATUS = ExportResultStatus.SUCCESS;
    private static final ExportResultStatus UPDATED_STATUS = ExportResultStatus.FAILED;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPORTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPORTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/export-results";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ExportResultRepository exportResultRepository;

    @Autowired
    private ExportResultMapper exportResultMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restExportResultMockMvc;

    private ExportResult exportResult;

    private ExportResult insertedExportResult;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExportResult createEntity(EntityManager em) {
        ExportResult exportResult = new ExportResult()
            .exportJobId(DEFAULT_EXPORT_JOB_ID)
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .originalFileName(DEFAULT_ORIGINAL_FILE_NAME)
            .exportedPath(DEFAULT_EXPORTED_PATH)
            .exportedFileName(DEFAULT_EXPORTED_FILE_NAME)
            .s3ExportKey(DEFAULT_S_3_EXPORT_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .status(DEFAULT_STATUS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .exportedDate(DEFAULT_EXPORTED_DATE);
        // Add required entity
        ExportJob exportJob;
        if (TestUtil.findAll(em, ExportJob.class).isEmpty()) {
            exportJob = ExportJobResourceIT.createEntity(em);
            em.persist(exportJob);
            em.flush();
        } else {
            exportJob = TestUtil.findAll(em, ExportJob.class).get(0);
        }
        exportResult.setExportJob(exportJob);
        return exportResult;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ExportResult createUpdatedEntity(EntityManager em) {
        ExportResult updatedExportResult = new ExportResult()
            .exportJobId(UPDATED_EXPORT_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .exportedPath(UPDATED_EXPORTED_PATH)
            .exportedFileName(UPDATED_EXPORTED_FILE_NAME)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .exportedDate(UPDATED_EXPORTED_DATE);
        // Add required entity
        ExportJob exportJob;
        if (TestUtil.findAll(em, ExportJob.class).isEmpty()) {
            exportJob = ExportJobResourceIT.createUpdatedEntity(em);
            em.persist(exportJob);
            em.flush();
        } else {
            exportJob = TestUtil.findAll(em, ExportJob.class).get(0);
        }
        updatedExportResult.setExportJob(exportJob);
        return updatedExportResult;
    }

    @BeforeEach
    void initTest() {
        exportResult = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedExportResult != null) {
            exportResultRepository.delete(insertedExportResult);
            insertedExportResult = null;
        }
    }

    @Test
    @Transactional
    void createExportResult() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);
        var returnedExportResultDTO = om.readValue(
            restExportResultMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ExportResultDTO.class
        );

        // Validate the ExportResult in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedExportResult = exportResultMapper.toEntity(returnedExportResultDTO);
        assertExportResultUpdatableFieldsEquals(returnedExportResult, getPersistedExportResult(returnedExportResult));

        insertedExportResult = returnedExportResult;
    }

    @Test
    @Transactional
    void createExportResultWithExistingId() throws Exception {
        // Create the ExportResult with an existing ID
        exportResult.setId(1L);
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restExportResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkExportJobIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportResult.setExportJobId(null);

        // Create the ExportResult, which fails.
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        restExportResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportResult.setDocumentId(null);

        // Create the ExportResult, which fails.
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        restExportResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportResult.setDocumentSha256(null);

        // Create the ExportResult, which fails.
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        restExportResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExportedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        exportResult.setExportedDate(null);

        // Create the ExportResult, which fails.
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        restExportResultMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllExportResults() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        // Get all the exportResultList
        restExportResultMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(exportResult.getId().intValue())))
            .andExpect(jsonPath("$.[*].exportJobId").value(hasItem(DEFAULT_EXPORT_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].originalFileName").value(hasItem(DEFAULT_ORIGINAL_FILE_NAME)))
            .andExpect(jsonPath("$.[*].exportedPath").value(hasItem(DEFAULT_EXPORTED_PATH)))
            .andExpect(jsonPath("$.[*].exportedFileName").value(hasItem(DEFAULT_EXPORTED_FILE_NAME)))
            .andExpect(jsonPath("$.[*].s3ExportKey").value(hasItem(DEFAULT_S_3_EXPORT_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].exportedDate").value(hasItem(DEFAULT_EXPORTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getExportResult() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        // Get the exportResult
        restExportResultMockMvc
            .perform(get(ENTITY_API_URL_ID, exportResult.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(exportResult.getId().intValue()))
            .andExpect(jsonPath("$.exportJobId").value(DEFAULT_EXPORT_JOB_ID.intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.originalFileName").value(DEFAULT_ORIGINAL_FILE_NAME))
            .andExpect(jsonPath("$.exportedPath").value(DEFAULT_EXPORTED_PATH))
            .andExpect(jsonPath("$.exportedFileName").value(DEFAULT_EXPORTED_FILE_NAME))
            .andExpect(jsonPath("$.s3ExportKey").value(DEFAULT_S_3_EXPORT_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.exportedDate").value(DEFAULT_EXPORTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingExportResult() throws Exception {
        // Get the exportResult
        restExportResultMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingExportResult() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportResult
        ExportResult updatedExportResult = exportResultRepository.findById(exportResult.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedExportResult are not directly saved in db
        em.detach(updatedExportResult);
        updatedExportResult
            .exportJobId(UPDATED_EXPORT_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .exportedPath(UPDATED_EXPORTED_PATH)
            .exportedFileName(UPDATED_EXPORTED_FILE_NAME)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .exportedDate(UPDATED_EXPORTED_DATE);
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(updatedExportResult);

        restExportResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportResultDTO))
            )
            .andExpect(status().isOk());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedExportResultToMatchAllProperties(updatedExportResult);
    }

    @Test
    @Transactional
    void putNonExistingExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, exportResultDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(exportResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateExportResultWithPatch() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportResult using partial update
        ExportResult partialUpdatedExportResult = new ExportResult();
        partialUpdatedExportResult.setId(exportResult.getId());

        partialUpdatedExportResult.originalFileName(UPDATED_ORIGINAL_FILE_NAME).exportedPath(UPDATED_EXPORTED_PATH).status(UPDATED_STATUS);

        restExportResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportResult))
            )
            .andExpect(status().isOk());

        // Validate the ExportResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportResultUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedExportResult, exportResult),
            getPersistedExportResult(exportResult)
        );
    }

    @Test
    @Transactional
    void fullUpdateExportResultWithPatch() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the exportResult using partial update
        ExportResult partialUpdatedExportResult = new ExportResult();
        partialUpdatedExportResult.setId(exportResult.getId());

        partialUpdatedExportResult
            .exportJobId(UPDATED_EXPORT_JOB_ID)
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalFileName(UPDATED_ORIGINAL_FILE_NAME)
            .exportedPath(UPDATED_EXPORTED_PATH)
            .exportedFileName(UPDATED_EXPORTED_FILE_NAME)
            .s3ExportKey(UPDATED_S_3_EXPORT_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .exportedDate(UPDATED_EXPORTED_DATE);

        restExportResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedExportResult.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedExportResult))
            )
            .andExpect(status().isOk());

        // Validate the ExportResult in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertExportResultUpdatableFieldsEquals(partialUpdatedExportResult, getPersistedExportResult(partialUpdatedExportResult));
    }

    @Test
    @Transactional
    void patchNonExistingExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, exportResultDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(exportResultDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamExportResult() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        exportResult.setId(longCount.incrementAndGet());

        // Create the ExportResult
        ExportResultDTO exportResultDTO = exportResultMapper.toDto(exportResult);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restExportResultMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(exportResultDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ExportResult in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteExportResult() throws Exception {
        // Initialize the database
        insertedExportResult = exportResultRepository.saveAndFlush(exportResult);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the exportResult
        restExportResultMockMvc
            .perform(delete(ENTITY_API_URL_ID, exportResult.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return exportResultRepository.count();
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

    protected ExportResult getPersistedExportResult(ExportResult exportResult) {
        return exportResultRepository.findById(exportResult.getId()).orElseThrow();
    }

    protected void assertPersistedExportResultToMatchAllProperties(ExportResult expectedExportResult) {
        assertExportResultAllPropertiesEquals(expectedExportResult, getPersistedExportResult(expectedExportResult));
    }

    protected void assertPersistedExportResultToMatchUpdatableProperties(ExportResult expectedExportResult) {
        assertExportResultAllUpdatablePropertiesEquals(expectedExportResult, getPersistedExportResult(expectedExportResult));
    }
}
