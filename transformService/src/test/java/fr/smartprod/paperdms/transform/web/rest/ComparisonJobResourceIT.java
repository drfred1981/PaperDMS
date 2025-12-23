package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.ComparisonJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.ComparisonJob;
import fr.smartprod.paperdms.transform.domain.enumeration.ComparisonType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.ComparisonJobRepository;
import fr.smartprod.paperdms.transform.service.dto.ComparisonJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.ComparisonJobMapper;
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
 * Integration tests for the {@link ComparisonJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ComparisonJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID_1 = 1L;
    private static final Long UPDATED_DOCUMENT_ID_1 = 2L;

    private static final Long DEFAULT_DOCUMENT_ID_2 = 1L;
    private static final Long UPDATED_DOCUMENT_ID_2 = 2L;

    private static final ComparisonType DEFAULT_COMPARISON_TYPE = ComparisonType.TEXT_DIFF;
    private static final ComparisonType UPDATED_COMPARISON_TYPE = ComparisonType.PDF_VISUAL;

    private static final String DEFAULT_DIFFERENCES = "AAAAAAAAAA";
    private static final String UPDATED_DIFFERENCES = "BBBBBBBBBB";

    private static final Integer DEFAULT_DIFFERENCE_COUNT = 1;
    private static final Integer UPDATED_DIFFERENCE_COUNT = 2;

    private static final Double DEFAULT_SIMILARITY_PERCENTAGE = 0D;
    private static final Double UPDATED_SIMILARITY_PERCENTAGE = 1D;

    private static final String DEFAULT_DIFF_REPORT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DIFF_REPORT_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_DIFF_VISUAL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_DIFF_VISUAL_S_3_KEY = "BBBBBBBBBB";

    private static final TransformStatus DEFAULT_STATUS = TransformStatus.PENDING;
    private static final TransformStatus UPDATED_STATUS = TransformStatus.PROCESSING;

    private static final Instant DEFAULT_COMPARED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPARED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_COMPARED_BY = "AAAAAAAAAA";
    private static final String UPDATED_COMPARED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/comparison-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ComparisonJobRepository comparisonJobRepository;

    @Autowired
    private ComparisonJobMapper comparisonJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restComparisonJobMockMvc;

    private ComparisonJob comparisonJob;

    private ComparisonJob insertedComparisonJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComparisonJob createEntity() {
        return new ComparisonJob()
            .documentId1(DEFAULT_DOCUMENT_ID_1)
            .documentId2(DEFAULT_DOCUMENT_ID_2)
            .comparisonType(DEFAULT_COMPARISON_TYPE)
            .differences(DEFAULT_DIFFERENCES)
            .differenceCount(DEFAULT_DIFFERENCE_COUNT)
            .similarityPercentage(DEFAULT_SIMILARITY_PERCENTAGE)
            .diffReportS3Key(DEFAULT_DIFF_REPORT_S_3_KEY)
            .diffVisualS3Key(DEFAULT_DIFF_VISUAL_S_3_KEY)
            .status(DEFAULT_STATUS)
            .comparedDate(DEFAULT_COMPARED_DATE)
            .comparedBy(DEFAULT_COMPARED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ComparisonJob createUpdatedEntity() {
        return new ComparisonJob()
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
            .comparisonType(UPDATED_COMPARISON_TYPE)
            .differences(UPDATED_DIFFERENCES)
            .differenceCount(UPDATED_DIFFERENCE_COUNT)
            .similarityPercentage(UPDATED_SIMILARITY_PERCENTAGE)
            .diffReportS3Key(UPDATED_DIFF_REPORT_S_3_KEY)
            .diffVisualS3Key(UPDATED_DIFF_VISUAL_S_3_KEY)
            .status(UPDATED_STATUS)
            .comparedDate(UPDATED_COMPARED_DATE)
            .comparedBy(UPDATED_COMPARED_BY);
    }

    @BeforeEach
    void initTest() {
        comparisonJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedComparisonJob != null) {
            comparisonJobRepository.delete(insertedComparisonJob);
            insertedComparisonJob = null;
        }
    }

    @Test
    @Transactional
    void createComparisonJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);
        var returnedComparisonJobDTO = om.readValue(
            restComparisonJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ComparisonJobDTO.class
        );

        // Validate the ComparisonJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedComparisonJob = comparisonJobMapper.toEntity(returnedComparisonJobDTO);
        assertComparisonJobUpdatableFieldsEquals(returnedComparisonJob, getPersistedComparisonJob(returnedComparisonJob));

        insertedComparisonJob = returnedComparisonJob;
    }

    @Test
    @Transactional
    void createComparisonJobWithExistingId() throws Exception {
        // Create the ComparisonJob with an existing ID
        comparisonJob.setId(1L);
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentId1IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setDocumentId1(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentId2IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setDocumentId2(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComparisonTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setComparisonType(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setStatus(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComparedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setComparedDate(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkComparedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        comparisonJob.setComparedBy(null);

        // Create the ComparisonJob, which fails.
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        restComparisonJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllComparisonJobs() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        // Get all the comparisonJobList
        restComparisonJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(comparisonJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId1").value(hasItem(DEFAULT_DOCUMENT_ID_1.intValue())))
            .andExpect(jsonPath("$.[*].documentId2").value(hasItem(DEFAULT_DOCUMENT_ID_2.intValue())))
            .andExpect(jsonPath("$.[*].comparisonType").value(hasItem(DEFAULT_COMPARISON_TYPE.toString())))
            .andExpect(jsonPath("$.[*].differences").value(hasItem(DEFAULT_DIFFERENCES)))
            .andExpect(jsonPath("$.[*].differenceCount").value(hasItem(DEFAULT_DIFFERENCE_COUNT)))
            .andExpect(jsonPath("$.[*].similarityPercentage").value(hasItem(DEFAULT_SIMILARITY_PERCENTAGE)))
            .andExpect(jsonPath("$.[*].diffReportS3Key").value(hasItem(DEFAULT_DIFF_REPORT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].diffVisualS3Key").value(hasItem(DEFAULT_DIFF_VISUAL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].comparedDate").value(hasItem(DEFAULT_COMPARED_DATE.toString())))
            .andExpect(jsonPath("$.[*].comparedBy").value(hasItem(DEFAULT_COMPARED_BY)));
    }

    @Test
    @Transactional
    void getComparisonJob() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        // Get the comparisonJob
        restComparisonJobMockMvc
            .perform(get(ENTITY_API_URL_ID, comparisonJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(comparisonJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId1").value(DEFAULT_DOCUMENT_ID_1.intValue()))
            .andExpect(jsonPath("$.documentId2").value(DEFAULT_DOCUMENT_ID_2.intValue()))
            .andExpect(jsonPath("$.comparisonType").value(DEFAULT_COMPARISON_TYPE.toString()))
            .andExpect(jsonPath("$.differences").value(DEFAULT_DIFFERENCES))
            .andExpect(jsonPath("$.differenceCount").value(DEFAULT_DIFFERENCE_COUNT))
            .andExpect(jsonPath("$.similarityPercentage").value(DEFAULT_SIMILARITY_PERCENTAGE))
            .andExpect(jsonPath("$.diffReportS3Key").value(DEFAULT_DIFF_REPORT_S_3_KEY))
            .andExpect(jsonPath("$.diffVisualS3Key").value(DEFAULT_DIFF_VISUAL_S_3_KEY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.comparedDate").value(DEFAULT_COMPARED_DATE.toString()))
            .andExpect(jsonPath("$.comparedBy").value(DEFAULT_COMPARED_BY));
    }

    @Test
    @Transactional
    void getNonExistingComparisonJob() throws Exception {
        // Get the comparisonJob
        restComparisonJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingComparisonJob() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comparisonJob
        ComparisonJob updatedComparisonJob = comparisonJobRepository.findById(comparisonJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedComparisonJob are not directly saved in db
        em.detach(updatedComparisonJob);
        updatedComparisonJob
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
            .comparisonType(UPDATED_COMPARISON_TYPE)
            .differences(UPDATED_DIFFERENCES)
            .differenceCount(UPDATED_DIFFERENCE_COUNT)
            .similarityPercentage(UPDATED_SIMILARITY_PERCENTAGE)
            .diffReportS3Key(UPDATED_DIFF_REPORT_S_3_KEY)
            .diffVisualS3Key(UPDATED_DIFF_VISUAL_S_3_KEY)
            .status(UPDATED_STATUS)
            .comparedDate(UPDATED_COMPARED_DATE)
            .comparedBy(UPDATED_COMPARED_BY);
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(updatedComparisonJob);

        restComparisonJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comparisonJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comparisonJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedComparisonJobToMatchAllProperties(updatedComparisonJob);
    }

    @Test
    @Transactional
    void putNonExistingComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, comparisonJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comparisonJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(comparisonJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateComparisonJobWithPatch() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comparisonJob using partial update
        ComparisonJob partialUpdatedComparisonJob = new ComparisonJob();
        partialUpdatedComparisonJob.setId(comparisonJob.getId());

        partialUpdatedComparisonJob
            .comparisonType(UPDATED_COMPARISON_TYPE)
            .differenceCount(UPDATED_DIFFERENCE_COUNT)
            .status(UPDATED_STATUS)
            .comparedDate(UPDATED_COMPARED_DATE);

        restComparisonJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComparisonJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComparisonJob))
            )
            .andExpect(status().isOk());

        // Validate the ComparisonJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComparisonJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedComparisonJob, comparisonJob),
            getPersistedComparisonJob(comparisonJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateComparisonJobWithPatch() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the comparisonJob using partial update
        ComparisonJob partialUpdatedComparisonJob = new ComparisonJob();
        partialUpdatedComparisonJob.setId(comparisonJob.getId());

        partialUpdatedComparisonJob
            .documentId1(UPDATED_DOCUMENT_ID_1)
            .documentId2(UPDATED_DOCUMENT_ID_2)
            .comparisonType(UPDATED_COMPARISON_TYPE)
            .differences(UPDATED_DIFFERENCES)
            .differenceCount(UPDATED_DIFFERENCE_COUNT)
            .similarityPercentage(UPDATED_SIMILARITY_PERCENTAGE)
            .diffReportS3Key(UPDATED_DIFF_REPORT_S_3_KEY)
            .diffVisualS3Key(UPDATED_DIFF_VISUAL_S_3_KEY)
            .status(UPDATED_STATUS)
            .comparedDate(UPDATED_COMPARED_DATE)
            .comparedBy(UPDATED_COMPARED_BY);

        restComparisonJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedComparisonJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedComparisonJob))
            )
            .andExpect(status().isOk());

        // Validate the ComparisonJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertComparisonJobUpdatableFieldsEquals(partialUpdatedComparisonJob, getPersistedComparisonJob(partialUpdatedComparisonJob));
    }

    @Test
    @Transactional
    void patchNonExistingComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, comparisonJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(comparisonJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(comparisonJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamComparisonJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        comparisonJob.setId(longCount.incrementAndGet());

        // Create the ComparisonJob
        ComparisonJobDTO comparisonJobDTO = comparisonJobMapper.toDto(comparisonJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restComparisonJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(comparisonJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ComparisonJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteComparisonJob() throws Exception {
        // Initialize the database
        insertedComparisonJob = comparisonJobRepository.saveAndFlush(comparisonJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the comparisonJob
        restComparisonJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, comparisonJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return comparisonJobRepository.count();
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

    protected ComparisonJob getPersistedComparisonJob(ComparisonJob comparisonJob) {
        return comparisonJobRepository.findById(comparisonJob.getId()).orElseThrow();
    }

    protected void assertPersistedComparisonJobToMatchAllProperties(ComparisonJob expectedComparisonJob) {
        assertComparisonJobAllPropertiesEquals(expectedComparisonJob, getPersistedComparisonJob(expectedComparisonJob));
    }

    protected void assertPersistedComparisonJobToMatchUpdatableProperties(ComparisonJob expectedComparisonJob) {
        assertComparisonJobAllUpdatablePropertiesEquals(expectedComparisonJob, getPersistedComparisonJob(expectedComparisonJob));
    }
}
