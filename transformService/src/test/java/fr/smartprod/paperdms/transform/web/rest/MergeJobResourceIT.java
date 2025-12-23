package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.MergeJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.MergeJob;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.MergeJobRepository;
import fr.smartprod.paperdms.transform.service.dto.MergeJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.MergeJobMapper;
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
 * Integration tests for the {@link MergeJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MergeJobResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE_DOCUMENT_IDS = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_DOCUMENT_IDS = "BBBBBBBBBB";

    private static final String DEFAULT_MERGE_ORDER = "AAAAAAAAAA";
    private static final String UPDATED_MERGE_ORDER = "BBBBBBBBBB";

    private static final Boolean DEFAULT_INCLUDE_BOOKMARKS = false;
    private static final Boolean UPDATED_INCLUDE_BOOKMARKS = true;

    private static final Boolean DEFAULT_INCLUDE_TOC = false;
    private static final Boolean UPDATED_INCLUDE_TOC = true;

    private static final Boolean DEFAULT_ADD_PAGE_NUMBERS = false;
    private static final Boolean UPDATED_ADD_PAGE_NUMBERS = true;

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

    private static final String ENTITY_API_URL = "/api/merge-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MergeJobRepository mergeJobRepository;

    @Autowired
    private MergeJobMapper mergeJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMergeJobMockMvc;

    private MergeJob mergeJob;

    private MergeJob insertedMergeJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MergeJob createEntity() {
        return new MergeJob()
            .name(DEFAULT_NAME)
            .sourceDocumentIds(DEFAULT_SOURCE_DOCUMENT_IDS)
            .mergeOrder(DEFAULT_MERGE_ORDER)
            .includeBookmarks(DEFAULT_INCLUDE_BOOKMARKS)
            .includeToc(DEFAULT_INCLUDE_TOC)
            .addPageNumbers(DEFAULT_ADD_PAGE_NUMBERS)
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
    public static MergeJob createUpdatedEntity() {
        return new MergeJob()
            .name(UPDATED_NAME)
            .sourceDocumentIds(UPDATED_SOURCE_DOCUMENT_IDS)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
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
        mergeJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMergeJob != null) {
            mergeJobRepository.delete(insertedMergeJob);
            insertedMergeJob = null;
        }
    }

    @Test
    @Transactional
    void createMergeJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);
        var returnedMergeJobDTO = om.readValue(
            restMergeJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MergeJobDTO.class
        );

        // Validate the MergeJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMergeJob = mergeJobMapper.toEntity(returnedMergeJobDTO);
        assertMergeJobUpdatableFieldsEquals(returnedMergeJob, getPersistedMergeJob(returnedMergeJob));

        insertedMergeJob = returnedMergeJob;
    }

    @Test
    @Transactional
    void createMergeJobWithExistingId() throws Exception {
        // Create the MergeJob with an existing ID
        mergeJob.setId(1L);
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mergeJob.setName(null);

        // Create the MergeJob, which fails.
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        restMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mergeJob.setStatus(null);

        // Create the MergeJob, which fails.
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        restMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mergeJob.setCreatedBy(null);

        // Create the MergeJob, which fails.
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        restMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        mergeJob.setCreatedDate(null);

        // Create the MergeJob, which fails.
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        restMergeJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMergeJobs() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        // Get all the mergeJobList
        restMergeJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(mergeJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].sourceDocumentIds").value(hasItem(DEFAULT_SOURCE_DOCUMENT_IDS)))
            .andExpect(jsonPath("$.[*].mergeOrder").value(hasItem(DEFAULT_MERGE_ORDER)))
            .andExpect(jsonPath("$.[*].includeBookmarks").value(hasItem(DEFAULT_INCLUDE_BOOKMARKS)))
            .andExpect(jsonPath("$.[*].includeToc").value(hasItem(DEFAULT_INCLUDE_TOC)))
            .andExpect(jsonPath("$.[*].addPageNumbers").value(hasItem(DEFAULT_ADD_PAGE_NUMBERS)))
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
    void getMergeJob() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        // Get the mergeJob
        restMergeJobMockMvc
            .perform(get(ENTITY_API_URL_ID, mergeJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(mergeJob.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.sourceDocumentIds").value(DEFAULT_SOURCE_DOCUMENT_IDS))
            .andExpect(jsonPath("$.mergeOrder").value(DEFAULT_MERGE_ORDER))
            .andExpect(jsonPath("$.includeBookmarks").value(DEFAULT_INCLUDE_BOOKMARKS))
            .andExpect(jsonPath("$.includeToc").value(DEFAULT_INCLUDE_TOC))
            .andExpect(jsonPath("$.addPageNumbers").value(DEFAULT_ADD_PAGE_NUMBERS))
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
    void getNonExistingMergeJob() throws Exception {
        // Get the mergeJob
        restMergeJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMergeJob() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mergeJob
        MergeJob updatedMergeJob = mergeJobRepository.findById(mergeJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedMergeJob are not directly saved in db
        em.detach(updatedMergeJob);
        updatedMergeJob
            .name(UPDATED_NAME)
            .sourceDocumentIds(UPDATED_SOURCE_DOCUMENT_IDS)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(updatedMergeJob);

        restMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mergeJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mergeJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMergeJobToMatchAllProperties(updatedMergeJob);
    }

    @Test
    @Transactional
    void putNonExistingMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, mergeJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(mergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMergeJobWithPatch() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mergeJob using partial update
        MergeJob partialUpdatedMergeJob = new MergeJob();
        partialUpdatedMergeJob.setId(mergeJob.getId());

        partialUpdatedMergeJob
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMergeJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMergeJob))
            )
            .andExpect(status().isOk());

        // Validate the MergeJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMergeJobUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedMergeJob, mergeJob), getPersistedMergeJob(mergeJob));
    }

    @Test
    @Transactional
    void fullUpdateMergeJobWithPatch() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the mergeJob using partial update
        MergeJob partialUpdatedMergeJob = new MergeJob();
        partialUpdatedMergeJob.setId(mergeJob.getId());

        partialUpdatedMergeJob
            .name(UPDATED_NAME)
            .sourceDocumentIds(UPDATED_SOURCE_DOCUMENT_IDS)
            .mergeOrder(UPDATED_MERGE_ORDER)
            .includeBookmarks(UPDATED_INCLUDE_BOOKMARKS)
            .includeToc(UPDATED_INCLUDE_TOC)
            .addPageNumbers(UPDATED_ADD_PAGE_NUMBERS)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMergeJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMergeJob))
            )
            .andExpect(status().isOk());

        // Validate the MergeJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMergeJobUpdatableFieldsEquals(partialUpdatedMergeJob, getPersistedMergeJob(partialUpdatedMergeJob));
    }

    @Test
    @Transactional
    void patchNonExistingMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, mergeJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(mergeJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMergeJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        mergeJob.setId(longCount.incrementAndGet());

        // Create the MergeJob
        MergeJobDTO mergeJobDTO = mergeJobMapper.toDto(mergeJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMergeJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(mergeJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MergeJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMergeJob() throws Exception {
        // Initialize the database
        insertedMergeJob = mergeJobRepository.saveAndFlush(mergeJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the mergeJob
        restMergeJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, mergeJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return mergeJobRepository.count();
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

    protected MergeJob getPersistedMergeJob(MergeJob mergeJob) {
        return mergeJobRepository.findById(mergeJob.getId()).orElseThrow();
    }

    protected void assertPersistedMergeJobToMatchAllProperties(MergeJob expectedMergeJob) {
        assertMergeJobAllPropertiesEquals(expectedMergeJob, getPersistedMergeJob(expectedMergeJob));
    }

    protected void assertPersistedMergeJobToMatchUpdatableProperties(MergeJob expectedMergeJob) {
        assertMergeJobAllUpdatablePropertiesEquals(expectedMergeJob, getPersistedMergeJob(expectedMergeJob));
    }
}
