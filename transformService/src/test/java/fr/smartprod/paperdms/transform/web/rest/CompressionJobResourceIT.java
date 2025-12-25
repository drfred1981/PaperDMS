package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.CompressionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.CompressionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.CompressionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.CompressionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.CompressionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.CompressionJobMapper;
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
 * Integration tests for the {@link CompressionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CompressionJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final CompressionType DEFAULT_COMPRESSION_TYPE = CompressionType.LOSSLESS;
    private static final CompressionType UPDATED_COMPRESSION_TYPE = CompressionType.LOSSY;

    private static final Integer DEFAULT_QUALITY = 0;
    private static final Integer UPDATED_QUALITY = 1;

    private static final Long DEFAULT_TARGET_SIZE_KB = 1L;
    private static final Long UPDATED_TARGET_SIZE_KB = 2L;

    private static final Long DEFAULT_ORIGINAL_SIZE = 1L;
    private static final Long UPDATED_ORIGINAL_SIZE = 2L;

    private static final Long DEFAULT_COMPRESSED_SIZE = 1L;
    private static final Long UPDATED_COMPRESSED_SIZE = 2L;

    private static final Double DEFAULT_COMPRESSION_RATIO = 1D;
    private static final Double UPDATED_COMPRESSION_RATIO = 2D;

    private static final String DEFAULT_OUTPUT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_OUTPUT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_OUTPUT_DOCUMENT_ID = 2L;

    private static final TransformStatus DEFAULT_STATUS = TransformStatus.PENDING;
    private static final TransformStatus UPDATED_STATUS = TransformStatus.PROCESSING;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/compression-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CompressionJobRepository compressionJobRepository;

    @Autowired
    private CompressionJobMapper compressionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCompressionJobMockMvc;

    private CompressionJob compressionJob;

    private CompressionJob insertedCompressionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompressionJob createEntity() {
        return new CompressionJob()
            .documentId(DEFAULT_DOCUMENT_ID)
            .compressionType(DEFAULT_COMPRESSION_TYPE)
            .quality(DEFAULT_QUALITY)
            .targetSizeKb(DEFAULT_TARGET_SIZE_KB)
            .originalSize(DEFAULT_ORIGINAL_SIZE)
            .compressedSize(DEFAULT_COMPRESSED_SIZE)
            .compressionRatio(DEFAULT_COMPRESSION_RATIO)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputDocumentId(DEFAULT_OUTPUT_DOCUMENT_ID)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CompressionJob createUpdatedEntity() {
        return new CompressionJob()
            .documentId(UPDATED_DOCUMENT_ID)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        compressionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCompressionJob != null) {
            compressionJobRepository.delete(insertedCompressionJob);
            insertedCompressionJob = null;
        }
    }

    @Test
    @Transactional
    void createCompressionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);
        var returnedCompressionJobDTO = om.readValue(
            restCompressionJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CompressionJobDTO.class
        );

        // Validate the CompressionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCompressionJob = compressionJobMapper.toEntity(returnedCompressionJobDTO);
        assertCompressionJobUpdatableFieldsEquals(returnedCompressionJob, getPersistedCompressionJob(returnedCompressionJob));

        insertedCompressionJob = returnedCompressionJob;
    }

    @Test
    @Transactional
    void createCompressionJobWithExistingId() throws Exception {
        // Create the CompressionJob with an existing ID
        compressionJob.setId(1L);
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compressionJob.setDocumentId(null);

        // Create the CompressionJob, which fails.
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCompressionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compressionJob.setCompressionType(null);

        // Create the CompressionJob, which fails.
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compressionJob.setStatus(null);

        // Create the CompressionJob, which fails.
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compressionJob.setCreatedBy(null);

        // Create the CompressionJob, which fails.
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        compressionJob.setCreatedDate(null);

        // Create the CompressionJob, which fails.
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        restCompressionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCompressionJobs() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        // Get all the compressionJobList
        restCompressionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(compressionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].compressionType").value(hasItem(DEFAULT_COMPRESSION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].quality").value(hasItem(DEFAULT_QUALITY)))
            .andExpect(jsonPath("$.[*].targetSizeKb").value(hasItem(DEFAULT_TARGET_SIZE_KB.intValue())))
            .andExpect(jsonPath("$.[*].originalSize").value(hasItem(DEFAULT_ORIGINAL_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressedSize").value(hasItem(DEFAULT_COMPRESSED_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].compressionRatio").value(hasItem(DEFAULT_COMPRESSION_RATIO)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputDocumentId").value(hasItem(DEFAULT_OUTPUT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCompressionJob() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        // Get the compressionJob
        restCompressionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, compressionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(compressionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.compressionType").value(DEFAULT_COMPRESSION_TYPE.toString()))
            .andExpect(jsonPath("$.quality").value(DEFAULT_QUALITY))
            .andExpect(jsonPath("$.targetSizeKb").value(DEFAULT_TARGET_SIZE_KB.intValue()))
            .andExpect(jsonPath("$.originalSize").value(DEFAULT_ORIGINAL_SIZE.intValue()))
            .andExpect(jsonPath("$.compressedSize").value(DEFAULT_COMPRESSED_SIZE.intValue()))
            .andExpect(jsonPath("$.compressionRatio").value(DEFAULT_COMPRESSION_RATIO))
            .andExpect(jsonPath("$.outputS3Key").value(DEFAULT_OUTPUT_S_3_KEY))
            .andExpect(jsonPath("$.outputDocumentId").value(DEFAULT_OUTPUT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingCompressionJob() throws Exception {
        // Get the compressionJob
        restCompressionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCompressionJob() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compressionJob
        CompressionJob updatedCompressionJob = compressionJobRepository.findById(compressionJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedCompressionJob are not directly saved in db
        em.detach(updatedCompressionJob);
        updatedCompressionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(updatedCompressionJob);

        restCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compressionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compressionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCompressionJobToMatchAllProperties(updatedCompressionJob);
    }

    @Test
    @Transactional
    void putNonExistingCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, compressionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(compressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCompressionJobWithPatch() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compressionJob using partial update
        CompressionJob partialUpdatedCompressionJob = new CompressionJob();
        partialUpdatedCompressionJob.setId(compressionJob.getId());

        partialUpdatedCompressionJob
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .startDate(UPDATED_START_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompressionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompressionJob))
            )
            .andExpect(status().isOk());

        // Validate the CompressionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompressionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCompressionJob, compressionJob),
            getPersistedCompressionJob(compressionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateCompressionJobWithPatch() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the compressionJob using partial update
        CompressionJob partialUpdatedCompressionJob = new CompressionJob();
        partialUpdatedCompressionJob.setId(compressionJob.getId());

        partialUpdatedCompressionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .compressionType(UPDATED_COMPRESSION_TYPE)
            .quality(UPDATED_QUALITY)
            .targetSizeKb(UPDATED_TARGET_SIZE_KB)
            .originalSize(UPDATED_ORIGINAL_SIZE)
            .compressedSize(UPDATED_COMPRESSED_SIZE)
            .compressionRatio(UPDATED_COMPRESSION_RATIO)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCompressionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCompressionJob))
            )
            .andExpect(status().isOk());

        // Validate the CompressionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCompressionJobUpdatableFieldsEquals(partialUpdatedCompressionJob, getPersistedCompressionJob(partialUpdatedCompressionJob));
    }

    @Test
    @Transactional
    void patchNonExistingCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, compressionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(compressionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCompressionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        compressionJob.setId(longCount.incrementAndGet());

        // Create the CompressionJob
        CompressionJobDTO compressionJobDTO = compressionJobMapper.toDto(compressionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCompressionJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(compressionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CompressionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCompressionJob() throws Exception {
        // Initialize the database
        insertedCompressionJob = compressionJobRepository.saveAndFlush(compressionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the compressionJob
        restCompressionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, compressionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return compressionJobRepository.count();
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

    protected CompressionJob getPersistedCompressionJob(CompressionJob compressionJob) {
        return compressionJobRepository.findById(compressionJob.getId()).orElseThrow();
    }

    protected void assertPersistedCompressionJobToMatchAllProperties(CompressionJob expectedCompressionJob) {
        assertCompressionJobAllPropertiesEquals(expectedCompressionJob, getPersistedCompressionJob(expectedCompressionJob));
    }

    protected void assertPersistedCompressionJobToMatchUpdatableProperties(CompressionJob expectedCompressionJob) {
        assertCompressionJobAllUpdatablePropertiesEquals(expectedCompressionJob, getPersistedCompressionJob(expectedCompressionJob));
    }
}
