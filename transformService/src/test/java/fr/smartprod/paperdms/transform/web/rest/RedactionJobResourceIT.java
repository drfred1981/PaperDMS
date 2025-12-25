package fr.smartprod.paperdms.transform.web.rest;

import static fr.smartprod.paperdms.transform.domain.RedactionJobAsserts.*;
import static fr.smartprod.paperdms.transform.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.transform.IntegrationTest;
import fr.smartprod.paperdms.transform.domain.RedactionJob;
import fr.smartprod.paperdms.transform.domain.enumeration.RedactionType;
import fr.smartprod.paperdms.transform.domain.enumeration.TransformStatus;
import fr.smartprod.paperdms.transform.repository.RedactionJobRepository;
import fr.smartprod.paperdms.transform.service.dto.RedactionJobDTO;
import fr.smartprod.paperdms.transform.service.mapper.RedactionJobMapper;
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
 * Integration tests for the {@link RedactionJobResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RedactionJobResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_REDACTION_AREAS = "AAAAAAAAAA";
    private static final String UPDATED_REDACTION_AREAS = "BBBBBBBBBB";

    private static final RedactionType DEFAULT_REDACTION_TYPE = RedactionType.BLACK_BOX;
    private static final RedactionType UPDATED_REDACTION_TYPE = RedactionType.WHITE_BOX;

    private static final String DEFAULT_REDACTION_COLOR = "AAAAAAA";
    private static final String UPDATED_REDACTION_COLOR = "BBBBBBB";

    private static final String DEFAULT_REPLACE_WITH = "AAAAAAAAAA";
    private static final String UPDATED_REPLACE_WITH = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/redaction-jobs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private RedactionJobRepository redactionJobRepository;

    @Autowired
    private RedactionJobMapper redactionJobMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRedactionJobMockMvc;

    private RedactionJob redactionJob;

    private RedactionJob insertedRedactionJob;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RedactionJob createEntity() {
        return new RedactionJob()
            .documentId(DEFAULT_DOCUMENT_ID)
            .redactionAreas(DEFAULT_REDACTION_AREAS)
            .redactionType(DEFAULT_REDACTION_TYPE)
            .redactionColor(DEFAULT_REDACTION_COLOR)
            .replaceWith(DEFAULT_REPLACE_WITH)
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
    public static RedactionJob createUpdatedEntity() {
        return new RedactionJob()
            .documentId(UPDATED_DOCUMENT_ID)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
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
        redactionJob = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedRedactionJob != null) {
            redactionJobRepository.delete(insertedRedactionJob);
            insertedRedactionJob = null;
        }
    }

    @Test
    @Transactional
    void createRedactionJob() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);
        var returnedRedactionJobDTO = om.readValue(
            restRedactionJobMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            RedactionJobDTO.class
        );

        // Validate the RedactionJob in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedRedactionJob = redactionJobMapper.toEntity(returnedRedactionJobDTO);
        assertRedactionJobUpdatableFieldsEquals(returnedRedactionJob, getPersistedRedactionJob(returnedRedactionJob));

        insertedRedactionJob = returnedRedactionJob;
    }

    @Test
    @Transactional
    void createRedactionJobWithExistingId() throws Exception {
        // Create the RedactionJob with an existing ID
        redactionJob.setId(1L);
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        redactionJob.setDocumentId(null);

        // Create the RedactionJob, which fails.
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRedactionTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        redactionJob.setRedactionType(null);

        // Create the RedactionJob, which fails.
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        redactionJob.setStatus(null);

        // Create the RedactionJob, which fails.
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        redactionJob.setCreatedBy(null);

        // Create the RedactionJob, which fails.
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        redactionJob.setCreatedDate(null);

        // Create the RedactionJob, which fails.
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        restRedactionJobMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllRedactionJobs() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        // Get all the redactionJobList
        restRedactionJobMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(redactionJob.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].redactionAreas").value(hasItem(DEFAULT_REDACTION_AREAS)))
            .andExpect(jsonPath("$.[*].redactionType").value(hasItem(DEFAULT_REDACTION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].redactionColor").value(hasItem(DEFAULT_REDACTION_COLOR)))
            .andExpect(jsonPath("$.[*].replaceWith").value(hasItem(DEFAULT_REPLACE_WITH)))
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
    void getRedactionJob() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        // Get the redactionJob
        restRedactionJobMockMvc
            .perform(get(ENTITY_API_URL_ID, redactionJob.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(redactionJob.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.redactionAreas").value(DEFAULT_REDACTION_AREAS))
            .andExpect(jsonPath("$.redactionType").value(DEFAULT_REDACTION_TYPE.toString()))
            .andExpect(jsonPath("$.redactionColor").value(DEFAULT_REDACTION_COLOR))
            .andExpect(jsonPath("$.replaceWith").value(DEFAULT_REPLACE_WITH))
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
    void getNonExistingRedactionJob() throws Exception {
        // Get the redactionJob
        restRedactionJobMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingRedactionJob() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the redactionJob
        RedactionJob updatedRedactionJob = redactionJobRepository.findById(redactionJob.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedRedactionJob are not directly saved in db
        em.detach(updatedRedactionJob);
        updatedRedactionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(updatedRedactionJob);

        restRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, redactionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(redactionJobDTO))
            )
            .andExpect(status().isOk());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedRedactionJobToMatchAllProperties(updatedRedactionJob);
    }

    @Test
    @Transactional
    void putNonExistingRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, redactionJobDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(redactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(redactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRedactionJobWithPatch() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the redactionJob using partial update
        RedactionJob partialUpdatedRedactionJob = new RedactionJob();
        partialUpdatedRedactionJob.setId(redactionJob.getId());

        partialUpdatedRedactionJob
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRedactionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRedactionJob))
            )
            .andExpect(status().isOk());

        // Validate the RedactionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRedactionJobUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedRedactionJob, redactionJob),
            getPersistedRedactionJob(redactionJob)
        );
    }

    @Test
    @Transactional
    void fullUpdateRedactionJobWithPatch() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the redactionJob using partial update
        RedactionJob partialUpdatedRedactionJob = new RedactionJob();
        partialUpdatedRedactionJob.setId(redactionJob.getId());

        partialUpdatedRedactionJob
            .documentId(UPDATED_DOCUMENT_ID)
            .redactionAreas(UPDATED_REDACTION_AREAS)
            .redactionType(UPDATED_REDACTION_TYPE)
            .redactionColor(UPDATED_REDACTION_COLOR)
            .replaceWith(UPDATED_REPLACE_WITH)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputDocumentId(UPDATED_OUTPUT_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRedactionJob.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedRedactionJob))
            )
            .andExpect(status().isOk());

        // Validate the RedactionJob in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertRedactionJobUpdatableFieldsEquals(partialUpdatedRedactionJob, getPersistedRedactionJob(partialUpdatedRedactionJob));
    }

    @Test
    @Transactional
    void patchNonExistingRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, redactionJobDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(redactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(redactionJobDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRedactionJob() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        redactionJob.setId(longCount.incrementAndGet());

        // Create the RedactionJob
        RedactionJobDTO redactionJobDTO = redactionJobMapper.toDto(redactionJob);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRedactionJobMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(redactionJobDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RedactionJob in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRedactionJob() throws Exception {
        // Initialize the database
        insertedRedactionJob = redactionJobRepository.saveAndFlush(redactionJob);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the redactionJob
        restRedactionJobMockMvc
            .perform(delete(ENTITY_API_URL_ID, redactionJob.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return redactionJobRepository.count();
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

    protected RedactionJob getPersistedRedactionJob(RedactionJob redactionJob) {
        return redactionJobRepository.findById(redactionJob.getId()).orElseThrow();
    }

    protected void assertPersistedRedactionJobToMatchAllProperties(RedactionJob expectedRedactionJob) {
        assertRedactionJobAllPropertiesEquals(expectedRedactionJob, getPersistedRedactionJob(expectedRedactionJob));
    }

    protected void assertPersistedRedactionJobToMatchUpdatableProperties(RedactionJob expectedRedactionJob) {
        assertRedactionJobAllUpdatablePropertiesEquals(expectedRedactionJob, getPersistedRedactionJob(expectedRedactionJob));
    }
}
