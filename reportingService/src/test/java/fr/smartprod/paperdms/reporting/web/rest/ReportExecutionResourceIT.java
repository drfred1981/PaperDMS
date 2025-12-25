package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportExecutionAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportExecution;
import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportExecutionStatus;
import fr.smartprod.paperdms.reporting.repository.ReportExecutionRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportExecutionDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportExecutionMapper;
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
 * Integration tests for the {@link ReportExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportExecutionResourceIT {

    private static final Long DEFAULT_SCHEDULED_REPORT_ID = 1L;
    private static final Long UPDATED_SCHEDULED_REPORT_ID = 2L;

    private static final ReportExecutionStatus DEFAULT_STATUS = ReportExecutionStatus.PENDING;
    private static final ReportExecutionStatus UPDATED_STATUS = ReportExecutionStatus.RUNNING;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RECORDS_PROCESSED = 1;
    private static final Integer UPDATED_RECORDS_PROCESSED = 2;

    private static final String DEFAULT_OUTPUT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_OUTPUT_SIZE = 1L;
    private static final Long UPDATED_OUTPUT_SIZE = 2L;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/report-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportExecutionRepository reportExecutionRepository;

    @Autowired
    private ReportExecutionMapper reportExecutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportExecutionMockMvc;

    private ReportExecution reportExecution;

    private ReportExecution insertedReportExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createEntity(EntityManager em) {
        ReportExecution reportExecution = new ReportExecution()
            .scheduledReportId(DEFAULT_SCHEDULED_REPORT_ID)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .recordsProcessed(DEFAULT_RECORDS_PROCESSED)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputSize(DEFAULT_OUTPUT_SIZE)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
        // Add required entity
        ScheduledReport scheduledReport;
        if (TestUtil.findAll(em, ScheduledReport.class).isEmpty()) {
            scheduledReport = ScheduledReportResourceIT.createEntity();
            em.persist(scheduledReport);
            em.flush();
        } else {
            scheduledReport = TestUtil.findAll(em, ScheduledReport.class).get(0);
        }
        reportExecution.setScheduledReport(scheduledReport);
        return reportExecution;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportExecution createUpdatedEntity(EntityManager em) {
        ReportExecution updatedReportExecution = new ReportExecution()
            .scheduledReportId(UPDATED_SCHEDULED_REPORT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        // Add required entity
        ScheduledReport scheduledReport;
        if (TestUtil.findAll(em, ScheduledReport.class).isEmpty()) {
            scheduledReport = ScheduledReportResourceIT.createUpdatedEntity();
            em.persist(scheduledReport);
            em.flush();
        } else {
            scheduledReport = TestUtil.findAll(em, ScheduledReport.class).get(0);
        }
        updatedReportExecution.setScheduledReport(scheduledReport);
        return updatedReportExecution;
    }

    @BeforeEach
    void initTest() {
        reportExecution = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedReportExecution != null) {
            reportExecutionRepository.delete(insertedReportExecution);
            insertedReportExecution = null;
        }
    }

    @Test
    @Transactional
    void createReportExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);
        var returnedReportExecutionDTO = om.readValue(
            restReportExecutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportExecutionDTO.class
        );

        // Validate the ReportExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportExecution = reportExecutionMapper.toEntity(returnedReportExecutionDTO);
        assertReportExecutionUpdatableFieldsEquals(returnedReportExecution, getPersistedReportExecution(returnedReportExecution));

        insertedReportExecution = returnedReportExecution;
    }

    @Test
    @Transactional
    void createReportExecutionWithExistingId() throws Exception {
        // Create the ReportExecution with an existing ID
        reportExecution.setId(1L);
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScheduledReportIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportExecution.setScheduledReportId(null);

        // Create the ReportExecution, which fails.
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportExecution.setStatus(null);

        // Create the ReportExecution, which fails.
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportExecution.setStartDate(null);

        // Create the ReportExecution, which fails.
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        restReportExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportExecutions() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        // Get all the reportExecutionList
        restReportExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].scheduledReportId").value(hasItem(DEFAULT_SCHEDULED_REPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordsProcessed").value(hasItem(DEFAULT_RECORDS_PROCESSED)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputSize").value(hasItem(DEFAULT_OUTPUT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getReportExecution() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        // Get the reportExecution
        restReportExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, reportExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportExecution.getId().intValue()))
            .andExpect(jsonPath("$.scheduledReportId").value(DEFAULT_SCHEDULED_REPORT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.recordsProcessed").value(DEFAULT_RECORDS_PROCESSED))
            .andExpect(jsonPath("$.outputS3Key").value(DEFAULT_OUTPUT_S_3_KEY))
            .andExpect(jsonPath("$.outputSize").value(DEFAULT_OUTPUT_SIZE.intValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingReportExecution() throws Exception {
        // Get the reportExecution
        restReportExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportExecution() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution
        ReportExecution updatedReportExecution = reportExecutionRepository.findById(reportExecution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportExecution are not directly saved in db
        em.detach(updatedReportExecution);
        updatedReportExecution
            .scheduledReportId(UPDATED_SCHEDULED_REPORT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(updatedReportExecution);

        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportExecutionToMatchAllProperties(updatedReportExecution);
    }

    @Test
    @Transactional
    void putNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setId(reportExecution.getId());

        partialUpdatedReportExecution
            .status(UPDATED_STATUS)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE);

        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportExecution, reportExecution),
            getPersistedReportExecution(reportExecution)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportExecution using partial update
        ReportExecution partialUpdatedReportExecution = new ReportExecution();
        partialUpdatedReportExecution.setId(reportExecution.getId());

        partialUpdatedReportExecution
            .scheduledReportId(UPDATED_SCHEDULED_REPORT_ID)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportExecutionUpdatableFieldsEquals(
            partialUpdatedReportExecution,
            getPersistedReportExecution(partialUpdatedReportExecution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportExecutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportExecution.setId(longCount.incrementAndGet());

        // Create the ReportExecution
        ReportExecutionDTO reportExecutionDTO = reportExecutionMapper.toDto(reportExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportExecutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportExecution() throws Exception {
        // Initialize the database
        insertedReportExecution = reportExecutionRepository.saveAndFlush(reportExecution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportExecution
        restReportExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportExecution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportExecutionRepository.count();
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

    protected ReportExecution getPersistedReportExecution(ReportExecution reportExecution) {
        return reportExecutionRepository.findById(reportExecution.getId()).orElseThrow();
    }

    protected void assertPersistedReportExecutionToMatchAllProperties(ReportExecution expectedReportExecution) {
        assertReportExecutionAllPropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }

    protected void assertPersistedReportExecutionToMatchUpdatableProperties(ReportExecution expectedReportExecution) {
        assertReportExecutionAllUpdatablePropertiesEquals(expectedReportExecution, getPersistedReportExecution(expectedReportExecution));
    }
}
