package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingScheduledReportAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingScheduledReport;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportFormat;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportType;
import fr.smartprod.paperdms.reporting.repository.ReportingScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingScheduledReportMapper;
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
 * Integration tests for the {@link ReportingScheduledReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingScheduledReportResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final ReportType DEFAULT_REPORT_TYPE = ReportType.DOCUMENT_INVENTORY;
    private static final ReportType UPDATED_REPORT_TYPE = ReportType.USER_ACTIVITY;

    private static final String DEFAULT_QUERY = "AAAAAAAAAA";
    private static final String UPDATED_QUERY = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBBBBBBB";

    private static final ReportFormat DEFAULT_FORMAT = ReportFormat.PDF;
    private static final ReportFormat UPDATED_FORMAT = ReportFormat.EXCEL;

    private static final String DEFAULT_RECIPIENTS = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENTS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_LAST_RUN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_RUN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_NEXT_RUN = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_NEXT_RUN = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reporting-scheduled-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingScheduledReportRepository reportingScheduledReportRepository;

    @Autowired
    private ReportingScheduledReportMapper reportingScheduledReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingScheduledReportMockMvc;

    private ReportingScheduledReport reportingScheduledReport;

    private ReportingScheduledReport insertedReportingScheduledReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingScheduledReport createEntity() {
        return new ReportingScheduledReport()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .reportType(DEFAULT_REPORT_TYPE)
            .query(DEFAULT_QUERY)
            .schedule(DEFAULT_SCHEDULE)
            .format(DEFAULT_FORMAT)
            .recipients(DEFAULT_RECIPIENTS)
            .isActive(DEFAULT_IS_ACTIVE)
            .lastRun(DEFAULT_LAST_RUN)
            .nextRun(DEFAULT_NEXT_RUN)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingScheduledReport createUpdatedEntity() {
        return new ReportingScheduledReport()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reportType(UPDATED_REPORT_TYPE)
            .query(UPDATED_QUERY)
            .schedule(UPDATED_SCHEDULE)
            .format(UPDATED_FORMAT)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        reportingScheduledReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingScheduledReport != null) {
            reportingScheduledReportRepository.delete(insertedReportingScheduledReport);
            insertedReportingScheduledReport = null;
        }
    }

    @Test
    @Transactional
    void createReportingScheduledReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);
        var returnedReportingScheduledReportDTO = om.readValue(
            restReportingScheduledReportMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingScheduledReportDTO.class
        );

        // Validate the ReportingScheduledReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingScheduledReport = reportingScheduledReportMapper.toEntity(returnedReportingScheduledReportDTO);
        assertReportingScheduledReportUpdatableFieldsEquals(
            returnedReportingScheduledReport,
            getPersistedReportingScheduledReport(returnedReportingScheduledReport)
        );

        insertedReportingScheduledReport = returnedReportingScheduledReport;
    }

    @Test
    @Transactional
    void createReportingScheduledReportWithExistingId() throws Exception {
        // Create the ReportingScheduledReport with an existing ID
        reportingScheduledReport.setId(1L);
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setName(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setReportType(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setSchedule(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setFormat(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setIsActive(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setCreatedBy(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingScheduledReport.setCreatedDate(null);

        // Create the ReportingScheduledReport, which fails.
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReports() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingScheduledReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].recipients").value(hasItem(DEFAULT_RECIPIENTS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN.toString())))
            .andExpect(jsonPath("$.[*].nextRun").value(hasItem(DEFAULT_NEXT_RUN.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getReportingScheduledReport() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get the reportingScheduledReport
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingScheduledReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingScheduledReport.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.reportType").value(DEFAULT_REPORT_TYPE.toString()))
            .andExpect(jsonPath("$.query").value(DEFAULT_QUERY))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE))
            .andExpect(jsonPath("$.format").value(DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.recipients").value(DEFAULT_RECIPIENTS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.lastRun").value(DEFAULT_LAST_RUN.toString()))
            .andExpect(jsonPath("$.nextRun").value(DEFAULT_NEXT_RUN.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getReportingScheduledReportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        Long id = reportingScheduledReport.getId();

        defaultReportingScheduledReportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingScheduledReportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingScheduledReportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where name equals to
        defaultReportingScheduledReportFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where name in
        defaultReportingScheduledReportFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where name is not null
        defaultReportingScheduledReportFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where name contains
        defaultReportingScheduledReportFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where name does not contain
        defaultReportingScheduledReportFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByReportTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where reportType equals to
        defaultReportingScheduledReportFiltering("reportType.equals=" + DEFAULT_REPORT_TYPE, "reportType.equals=" + UPDATED_REPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByReportTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where reportType in
        defaultReportingScheduledReportFiltering(
            "reportType.in=" + DEFAULT_REPORT_TYPE + "," + UPDATED_REPORT_TYPE,
            "reportType.in=" + UPDATED_REPORT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByReportTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where reportType is not null
        defaultReportingScheduledReportFiltering("reportType.specified=true", "reportType.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where schedule equals to
        defaultReportingScheduledReportFiltering("schedule.equals=" + DEFAULT_SCHEDULE, "schedule.equals=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByScheduleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where schedule in
        defaultReportingScheduledReportFiltering(
            "schedule.in=" + DEFAULT_SCHEDULE + "," + UPDATED_SCHEDULE,
            "schedule.in=" + UPDATED_SCHEDULE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByScheduleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where schedule is not null
        defaultReportingScheduledReportFiltering("schedule.specified=true", "schedule.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByScheduleContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where schedule contains
        defaultReportingScheduledReportFiltering("schedule.contains=" + DEFAULT_SCHEDULE, "schedule.contains=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByScheduleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where schedule does not contain
        defaultReportingScheduledReportFiltering(
            "schedule.doesNotContain=" + UPDATED_SCHEDULE,
            "schedule.doesNotContain=" + DEFAULT_SCHEDULE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where format equals to
        defaultReportingScheduledReportFiltering("format.equals=" + DEFAULT_FORMAT, "format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where format in
        defaultReportingScheduledReportFiltering("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT, "format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where format is not null
        defaultReportingScheduledReportFiltering("format.specified=true", "format.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where isActive equals to
        defaultReportingScheduledReportFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where isActive in
        defaultReportingScheduledReportFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where isActive is not null
        defaultReportingScheduledReportFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByLastRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where lastRun equals to
        defaultReportingScheduledReportFiltering("lastRun.equals=" + DEFAULT_LAST_RUN, "lastRun.equals=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByLastRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where lastRun in
        defaultReportingScheduledReportFiltering(
            "lastRun.in=" + DEFAULT_LAST_RUN + "," + UPDATED_LAST_RUN,
            "lastRun.in=" + UPDATED_LAST_RUN
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByLastRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where lastRun is not null
        defaultReportingScheduledReportFiltering("lastRun.specified=true", "lastRun.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNextRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where nextRun equals to
        defaultReportingScheduledReportFiltering("nextRun.equals=" + DEFAULT_NEXT_RUN, "nextRun.equals=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNextRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where nextRun in
        defaultReportingScheduledReportFiltering(
            "nextRun.in=" + DEFAULT_NEXT_RUN + "," + UPDATED_NEXT_RUN,
            "nextRun.in=" + UPDATED_NEXT_RUN
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByNextRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where nextRun is not null
        defaultReportingScheduledReportFiltering("nextRun.specified=true", "nextRun.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdBy equals to
        defaultReportingScheduledReportFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdBy in
        defaultReportingScheduledReportFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdBy is not null
        defaultReportingScheduledReportFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdBy contains
        defaultReportingScheduledReportFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdBy does not contain
        defaultReportingScheduledReportFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdDate equals to
        defaultReportingScheduledReportFiltering(
            "createdDate.equals=" + DEFAULT_CREATED_DATE,
            "createdDate.equals=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdDate in
        defaultReportingScheduledReportFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportingScheduledReportsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        // Get all the reportingScheduledReportList where createdDate is not null
        defaultReportingScheduledReportFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultReportingScheduledReportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingScheduledReportShouldBeFound(shouldBeFound);
        defaultReportingScheduledReportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingScheduledReportShouldBeFound(String filter) throws Exception {
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingScheduledReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].query").value(hasItem(DEFAULT_QUERY)))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE)))
            .andExpect(jsonPath("$.[*].format").value(hasItem(DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].recipients").value(hasItem(DEFAULT_RECIPIENTS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].lastRun").value(hasItem(DEFAULT_LAST_RUN.toString())))
            .andExpect(jsonPath("$.[*].nextRun").value(hasItem(DEFAULT_NEXT_RUN.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingScheduledReportShouldNotBeFound(String filter) throws Exception {
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingScheduledReport() throws Exception {
        // Get the reportingScheduledReport
        restReportingScheduledReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingScheduledReport() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingScheduledReport
        ReportingScheduledReport updatedReportingScheduledReport = reportingScheduledReportRepository
            .findById(reportingScheduledReport.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedReportingScheduledReport are not directly saved in db
        em.detach(updatedReportingScheduledReport);
        updatedReportingScheduledReport
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reportType(UPDATED_REPORT_TYPE)
            .query(UPDATED_QUERY)
            .schedule(UPDATED_SCHEDULE)
            .format(UPDATED_FORMAT)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(updatedReportingScheduledReport);

        restReportingScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingScheduledReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingScheduledReportToMatchAllProperties(updatedReportingScheduledReport);
    }

    @Test
    @Transactional
    void putNonExistingReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingScheduledReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingScheduledReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingScheduledReportWithPatch() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingScheduledReport using partial update
        ReportingScheduledReport partialUpdatedReportingScheduledReport = new ReportingScheduledReport();
        partialUpdatedReportingScheduledReport.setId(reportingScheduledReport.getId());

        partialUpdatedReportingScheduledReport
            .name(UPDATED_NAME)
            .reportType(UPDATED_REPORT_TYPE)
            .format(UPDATED_FORMAT)
            .nextRun(UPDATED_NEXT_RUN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restReportingScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingScheduledReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingScheduledReport))
            )
            .andExpect(status().isOk());

        // Validate the ReportingScheduledReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingScheduledReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingScheduledReport, reportingScheduledReport),
            getPersistedReportingScheduledReport(reportingScheduledReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingScheduledReportWithPatch() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingScheduledReport using partial update
        ReportingScheduledReport partialUpdatedReportingScheduledReport = new ReportingScheduledReport();
        partialUpdatedReportingScheduledReport.setId(reportingScheduledReport.getId());

        partialUpdatedReportingScheduledReport
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .reportType(UPDATED_REPORT_TYPE)
            .query(UPDATED_QUERY)
            .schedule(UPDATED_SCHEDULE)
            .format(UPDATED_FORMAT)
            .recipients(UPDATED_RECIPIENTS)
            .isActive(UPDATED_IS_ACTIVE)
            .lastRun(UPDATED_LAST_RUN)
            .nextRun(UPDATED_NEXT_RUN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restReportingScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingScheduledReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingScheduledReport))
            )
            .andExpect(status().isOk());

        // Validate the ReportingScheduledReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingScheduledReportUpdatableFieldsEquals(
            partialUpdatedReportingScheduledReport,
            getPersistedReportingScheduledReport(partialUpdatedReportingScheduledReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingScheduledReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingScheduledReport.setId(longCount.incrementAndGet());

        // Create the ReportingScheduledReport
        ReportingScheduledReportDTO reportingScheduledReportDTO = reportingScheduledReportMapper.toDto(reportingScheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportingScheduledReportDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingScheduledReport() throws Exception {
        // Initialize the database
        insertedReportingScheduledReport = reportingScheduledReportRepository.saveAndFlush(reportingScheduledReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingScheduledReport
        restReportingScheduledReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingScheduledReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingScheduledReportRepository.count();
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

    protected ReportingScheduledReport getPersistedReportingScheduledReport(ReportingScheduledReport reportingScheduledReport) {
        return reportingScheduledReportRepository.findById(reportingScheduledReport.getId()).orElseThrow();
    }

    protected void assertPersistedReportingScheduledReportToMatchAllProperties(ReportingScheduledReport expectedReportingScheduledReport) {
        assertReportingScheduledReportAllPropertiesEquals(
            expectedReportingScheduledReport,
            getPersistedReportingScheduledReport(expectedReportingScheduledReport)
        );
    }

    protected void assertPersistedReportingScheduledReportToMatchUpdatableProperties(
        ReportingScheduledReport expectedReportingScheduledReport
    ) {
        assertReportingScheduledReportAllUpdatablePropertiesEquals(
            expectedReportingScheduledReport,
            getPersistedReportingScheduledReport(expectedReportingScheduledReport)
        );
    }
}
