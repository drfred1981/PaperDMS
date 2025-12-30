package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingExecutionAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingExecution;
import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportingExecutionStatus;
import fr.smartprod.paperdms.reporting.repository.ReportingExecutionRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingExecutionDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingExecutionMapper;
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
 * Integration tests for the {@link ReportingExecutionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingExecutionResourceIT {

    private static final ReportingExecutionStatus DEFAULT_STATUS = ReportingExecutionStatus.PENDING;
    private static final ReportingExecutionStatus UPDATED_STATUS = ReportingExecutionStatus.RUNNING;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Integer DEFAULT_RECORDS_PROCESSED = 1;
    private static final Integer UPDATED_RECORDS_PROCESSED = 2;
    private static final Integer SMALLER_RECORDS_PROCESSED = 1 - 1;

    private static final String DEFAULT_OUTPUT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_OUTPUT_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_OUTPUT_SIZE = 1L;
    private static final Long UPDATED_OUTPUT_SIZE = 2L;
    private static final Long SMALLER_OUTPUT_SIZE = 1L - 1L;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/reporting-executions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingExecutionRepository reportingExecutionRepository;

    @Autowired
    private ReportingExecutionMapper reportingExecutionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingExecutionMockMvc;

    private ReportingExecution reportingExecution;

    private ReportingExecution insertedReportingExecution;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingExecution createEntity() {
        return new ReportingExecution()
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .recordsProcessed(DEFAULT_RECORDS_PROCESSED)
            .outputS3Key(DEFAULT_OUTPUT_S_3_KEY)
            .outputSize(DEFAULT_OUTPUT_SIZE)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingExecution createUpdatedEntity() {
        return new ReportingExecution()
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
    }

    @BeforeEach
    void initTest() {
        reportingExecution = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingExecution != null) {
            reportingExecutionRepository.delete(insertedReportingExecution);
            insertedReportingExecution = null;
        }
    }

    @Test
    @Transactional
    void createReportingExecution() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);
        var returnedReportingExecutionDTO = om.readValue(
            restReportingExecutionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingExecutionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingExecutionDTO.class
        );

        // Validate the ReportingExecution in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingExecution = reportingExecutionMapper.toEntity(returnedReportingExecutionDTO);
        assertReportingExecutionUpdatableFieldsEquals(
            returnedReportingExecution,
            getPersistedReportingExecution(returnedReportingExecution)
        );

        insertedReportingExecution = returnedReportingExecution;
    }

    @Test
    @Transactional
    void createReportingExecutionWithExistingId() throws Exception {
        // Create the ReportingExecution with an existing ID
        reportingExecution.setId(1L);
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingExecutionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingExecution.setStatus(null);

        // Create the ReportingExecution, which fails.
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        restReportingExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingExecution.setStartDate(null);

        // Create the ReportingExecution, which fails.
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        restReportingExecutionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingExecutionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingExecutions() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingExecution.getId().intValue())))
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
    void getReportingExecution() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get the reportingExecution
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingExecution.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingExecution.getId().intValue()))
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
    void getReportingExecutionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        Long id = reportingExecution.getId();

        defaultReportingExecutionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingExecutionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingExecutionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where status equals to
        defaultReportingExecutionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where status in
        defaultReportingExecutionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where status is not null
        defaultReportingExecutionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where startDate equals to
        defaultReportingExecutionFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where startDate in
        defaultReportingExecutionFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where startDate is not null
        defaultReportingExecutionFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where endDate equals to
        defaultReportingExecutionFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where endDate in
        defaultReportingExecutionFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where endDate is not null
        defaultReportingExecutionFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed equals to
        defaultReportingExecutionFiltering(
            "recordsProcessed.equals=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.equals=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed in
        defaultReportingExecutionFiltering(
            "recordsProcessed.in=" + DEFAULT_RECORDS_PROCESSED + "," + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.in=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed is not null
        defaultReportingExecutionFiltering("recordsProcessed.specified=true", "recordsProcessed.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed is greater than or equal to
        defaultReportingExecutionFiltering(
            "recordsProcessed.greaterThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.greaterThanOrEqual=" + UPDATED_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed is less than or equal to
        defaultReportingExecutionFiltering(
            "recordsProcessed.lessThanOrEqual=" + DEFAULT_RECORDS_PROCESSED,
            "recordsProcessed.lessThanOrEqual=" + SMALLER_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed is less than
        defaultReportingExecutionFiltering(
            "recordsProcessed.lessThan=" + UPDATED_RECORDS_PROCESSED,
            "recordsProcessed.lessThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByRecordsProcessedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where recordsProcessed is greater than
        defaultReportingExecutionFiltering(
            "recordsProcessed.greaterThan=" + SMALLER_RECORDS_PROCESSED,
            "recordsProcessed.greaterThan=" + DEFAULT_RECORDS_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputS3Key equals to
        defaultReportingExecutionFiltering("outputS3Key.equals=" + DEFAULT_OUTPUT_S_3_KEY, "outputS3Key.equals=" + UPDATED_OUTPUT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputS3Key in
        defaultReportingExecutionFiltering(
            "outputS3Key.in=" + DEFAULT_OUTPUT_S_3_KEY + "," + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.in=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputS3Key is not null
        defaultReportingExecutionFiltering("outputS3Key.specified=true", "outputS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputS3Key contains
        defaultReportingExecutionFiltering(
            "outputS3Key.contains=" + DEFAULT_OUTPUT_S_3_KEY,
            "outputS3Key.contains=" + UPDATED_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputS3Key does not contain
        defaultReportingExecutionFiltering(
            "outputS3Key.doesNotContain=" + UPDATED_OUTPUT_S_3_KEY,
            "outputS3Key.doesNotContain=" + DEFAULT_OUTPUT_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize equals to
        defaultReportingExecutionFiltering("outputSize.equals=" + DEFAULT_OUTPUT_SIZE, "outputSize.equals=" + UPDATED_OUTPUT_SIZE);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize in
        defaultReportingExecutionFiltering(
            "outputSize.in=" + DEFAULT_OUTPUT_SIZE + "," + UPDATED_OUTPUT_SIZE,
            "outputSize.in=" + UPDATED_OUTPUT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize is not null
        defaultReportingExecutionFiltering("outputSize.specified=true", "outputSize.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize is greater than or equal to
        defaultReportingExecutionFiltering(
            "outputSize.greaterThanOrEqual=" + DEFAULT_OUTPUT_SIZE,
            "outputSize.greaterThanOrEqual=" + UPDATED_OUTPUT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize is less than or equal to
        defaultReportingExecutionFiltering(
            "outputSize.lessThanOrEqual=" + DEFAULT_OUTPUT_SIZE,
            "outputSize.lessThanOrEqual=" + SMALLER_OUTPUT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize is less than
        defaultReportingExecutionFiltering("outputSize.lessThan=" + UPDATED_OUTPUT_SIZE, "outputSize.lessThan=" + DEFAULT_OUTPUT_SIZE);
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByOutputSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        // Get all the reportingExecutionList where outputSize is greater than
        defaultReportingExecutionFiltering(
            "outputSize.greaterThan=" + SMALLER_OUTPUT_SIZE,
            "outputSize.greaterThan=" + DEFAULT_OUTPUT_SIZE
        );
    }

    @Test
    @Transactional
    void getAllReportingExecutionsByScheduledReportIsEqualToSomething() throws Exception {
        ReportingScheduledReport scheduledReport;
        if (TestUtil.findAll(em, ReportingScheduledReport.class).isEmpty()) {
            reportingExecutionRepository.saveAndFlush(reportingExecution);
            scheduledReport = ReportingScheduledReportResourceIT.createEntity();
        } else {
            scheduledReport = TestUtil.findAll(em, ReportingScheduledReport.class).get(0);
        }
        em.persist(scheduledReport);
        em.flush();
        reportingExecution.setScheduledReport(scheduledReport);
        reportingExecutionRepository.saveAndFlush(reportingExecution);
        Long scheduledReportId = scheduledReport.getId();
        // Get all the reportingExecutionList where scheduledReport equals to scheduledReportId
        defaultReportingExecutionShouldBeFound("scheduledReportId.equals=" + scheduledReportId);

        // Get all the reportingExecutionList where scheduledReport equals to (scheduledReportId + 1)
        defaultReportingExecutionShouldNotBeFound("scheduledReportId.equals=" + (scheduledReportId + 1));
    }

    private void defaultReportingExecutionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingExecutionShouldBeFound(shouldBeFound);
        defaultReportingExecutionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingExecutionShouldBeFound(String filter) throws Exception {
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingExecution.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].recordsProcessed").value(hasItem(DEFAULT_RECORDS_PROCESSED)))
            .andExpect(jsonPath("$.[*].outputS3Key").value(hasItem(DEFAULT_OUTPUT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].outputSize").value(hasItem(DEFAULT_OUTPUT_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));

        // Check, that the count call also returns 1
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingExecutionShouldNotBeFound(String filter) throws Exception {
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingExecutionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingExecution() throws Exception {
        // Get the reportingExecution
        restReportingExecutionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingExecution() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingExecution
        ReportingExecution updatedReportingExecution = reportingExecutionRepository.findById(reportingExecution.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportingExecution are not directly saved in db
        em.detach(updatedReportingExecution);
        updatedReportingExecution
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(updatedReportingExecution);

        restReportingExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingExecutionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingExecutionToMatchAllProperties(updatedReportingExecution);
    }

    @Test
    @Transactional
    void putNonExistingReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingExecutionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingExecution using partial update
        ReportingExecution partialUpdatedReportingExecution = new ReportingExecution();
        partialUpdatedReportingExecution.setId(reportingExecution.getId());

        partialUpdatedReportingExecution.endDate(UPDATED_END_DATE).outputSize(UPDATED_OUTPUT_SIZE).errorMessage(UPDATED_ERROR_MESSAGE);

        restReportingExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportingExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingExecutionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingExecution, reportingExecution),
            getPersistedReportingExecution(reportingExecution)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingExecutionWithPatch() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingExecution using partial update
        ReportingExecution partialUpdatedReportingExecution = new ReportingExecution();
        partialUpdatedReportingExecution.setId(reportingExecution.getId());

        partialUpdatedReportingExecution
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .recordsProcessed(UPDATED_RECORDS_PROCESSED)
            .outputS3Key(UPDATED_OUTPUT_S_3_KEY)
            .outputSize(UPDATED_OUTPUT_SIZE)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restReportingExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingExecution.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingExecution))
            )
            .andExpect(status().isOk());

        // Validate the ReportingExecution in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingExecutionUpdatableFieldsEquals(
            partialUpdatedReportingExecution,
            getPersistedReportingExecution(partialUpdatedReportingExecution)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingExecutionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingExecutionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingExecution() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingExecution.setId(longCount.incrementAndGet());

        // Create the ReportingExecution
        ReportingExecutionDTO reportingExecutionDTO = reportingExecutionMapper.toDto(reportingExecution);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingExecutionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportingExecutionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingExecution in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingExecution() throws Exception {
        // Initialize the database
        insertedReportingExecution = reportingExecutionRepository.saveAndFlush(reportingExecution);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingExecution
        restReportingExecutionMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingExecution.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingExecutionRepository.count();
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

    protected ReportingExecution getPersistedReportingExecution(ReportingExecution reportingExecution) {
        return reportingExecutionRepository.findById(reportingExecution.getId()).orElseThrow();
    }

    protected void assertPersistedReportingExecutionToMatchAllProperties(ReportingExecution expectedReportingExecution) {
        assertReportingExecutionAllPropertiesEquals(expectedReportingExecution, getPersistedReportingExecution(expectedReportingExecution));
    }

    protected void assertPersistedReportingExecutionToMatchUpdatableProperties(ReportingExecution expectedReportingExecution) {
        assertReportingExecutionAllUpdatablePropertiesEquals(
            expectedReportingExecution,
            getPersistedReportingExecution(expectedReportingExecution)
        );
    }
}
