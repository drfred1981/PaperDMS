package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ScheduledReportAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ScheduledReport;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportFormat;
import fr.smartprod.paperdms.reporting.domain.enumeration.ReportType;
import fr.smartprod.paperdms.reporting.repository.ScheduledReportRepository;
import fr.smartprod.paperdms.reporting.service.dto.ScheduledReportDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ScheduledReportMapper;
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
 * Integration tests for the {@link ScheduledReportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScheduledReportResourceIT {

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

    private static final String ENTITY_API_URL = "/api/scheduled-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScheduledReportRepository scheduledReportRepository;

    @Autowired
    private ScheduledReportMapper scheduledReportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScheduledReportMockMvc;

    private ScheduledReport scheduledReport;

    private ScheduledReport insertedScheduledReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScheduledReport createEntity() {
        return new ScheduledReport()
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
    public static ScheduledReport createUpdatedEntity() {
        return new ScheduledReport()
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
        scheduledReport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScheduledReport != null) {
            scheduledReportRepository.delete(insertedScheduledReport);
            insertedScheduledReport = null;
        }
    }

    @Test
    @Transactional
    void createScheduledReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);
        var returnedScheduledReportDTO = om.readValue(
            restScheduledReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScheduledReportDTO.class
        );

        // Validate the ScheduledReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScheduledReport = scheduledReportMapper.toEntity(returnedScheduledReportDTO);
        assertScheduledReportUpdatableFieldsEquals(returnedScheduledReport, getPersistedScheduledReport(returnedScheduledReport));

        insertedScheduledReport = returnedScheduledReport;
    }

    @Test
    @Transactional
    void createScheduledReportWithExistingId() throws Exception {
        // Create the ScheduledReport with an existing ID
        scheduledReport.setId(1L);
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setName(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setReportType(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScheduleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setSchedule(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormatIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setFormat(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setIsActive(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setCreatedBy(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scheduledReport.setCreatedDate(null);

        // Create the ScheduledReport, which fails.
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        restScheduledReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScheduledReports() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledReport.getId().intValue())))
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
    void getScheduledReport() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get the scheduledReport
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL_ID, scheduledReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scheduledReport.getId().intValue()))
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
    void getScheduledReportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        Long id = scheduledReport.getId();

        defaultScheduledReportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultScheduledReportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultScheduledReportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where name equals to
        defaultScheduledReportFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where name in
        defaultScheduledReportFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where name is not null
        defaultScheduledReportFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where name contains
        defaultScheduledReportFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where name does not contain
        defaultScheduledReportFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByReportTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where reportType equals to
        defaultScheduledReportFiltering("reportType.equals=" + DEFAULT_REPORT_TYPE, "reportType.equals=" + UPDATED_REPORT_TYPE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByReportTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where reportType in
        defaultScheduledReportFiltering(
            "reportType.in=" + DEFAULT_REPORT_TYPE + "," + UPDATED_REPORT_TYPE,
            "reportType.in=" + UPDATED_REPORT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllScheduledReportsByReportTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where reportType is not null
        defaultScheduledReportFiltering("reportType.specified=true", "reportType.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByScheduleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where schedule equals to
        defaultScheduledReportFiltering("schedule.equals=" + DEFAULT_SCHEDULE, "schedule.equals=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByScheduleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where schedule in
        defaultScheduledReportFiltering("schedule.in=" + DEFAULT_SCHEDULE + "," + UPDATED_SCHEDULE, "schedule.in=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByScheduleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where schedule is not null
        defaultScheduledReportFiltering("schedule.specified=true", "schedule.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByScheduleContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where schedule contains
        defaultScheduledReportFiltering("schedule.contains=" + DEFAULT_SCHEDULE, "schedule.contains=" + UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByScheduleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where schedule does not contain
        defaultScheduledReportFiltering("schedule.doesNotContain=" + UPDATED_SCHEDULE, "schedule.doesNotContain=" + DEFAULT_SCHEDULE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByFormatIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where format equals to
        defaultScheduledReportFiltering("format.equals=" + DEFAULT_FORMAT, "format.equals=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByFormatIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where format in
        defaultScheduledReportFiltering("format.in=" + DEFAULT_FORMAT + "," + UPDATED_FORMAT, "format.in=" + UPDATED_FORMAT);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByFormatIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where format is not null
        defaultScheduledReportFiltering("format.specified=true", "format.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where isActive equals to
        defaultScheduledReportFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where isActive in
        defaultScheduledReportFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where isActive is not null
        defaultScheduledReportFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByLastRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where lastRun equals to
        defaultScheduledReportFiltering("lastRun.equals=" + DEFAULT_LAST_RUN, "lastRun.equals=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByLastRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where lastRun in
        defaultScheduledReportFiltering("lastRun.in=" + DEFAULT_LAST_RUN + "," + UPDATED_LAST_RUN, "lastRun.in=" + UPDATED_LAST_RUN);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByLastRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where lastRun is not null
        defaultScheduledReportFiltering("lastRun.specified=true", "lastRun.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNextRunIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where nextRun equals to
        defaultScheduledReportFiltering("nextRun.equals=" + DEFAULT_NEXT_RUN, "nextRun.equals=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNextRunIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where nextRun in
        defaultScheduledReportFiltering("nextRun.in=" + DEFAULT_NEXT_RUN + "," + UPDATED_NEXT_RUN, "nextRun.in=" + UPDATED_NEXT_RUN);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByNextRunIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where nextRun is not null
        defaultScheduledReportFiltering("nextRun.specified=true", "nextRun.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdBy equals to
        defaultScheduledReportFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdBy in
        defaultScheduledReportFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdBy is not null
        defaultScheduledReportFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdBy contains
        defaultScheduledReportFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdBy does not contain
        defaultScheduledReportFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdDate equals to
        defaultScheduledReportFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdDate in
        defaultScheduledReportFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllScheduledReportsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        // Get all the scheduledReportList where createdDate is not null
        defaultScheduledReportFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultScheduledReportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultScheduledReportShouldBeFound(shouldBeFound);
        defaultScheduledReportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScheduledReportShouldBeFound(String filter) throws Exception {
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scheduledReport.getId().intValue())))
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
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScheduledReportShouldNotBeFound(String filter) throws Exception {
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScheduledReportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScheduledReport() throws Exception {
        // Get the scheduledReport
        restScheduledReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScheduledReport() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledReport
        ScheduledReport updatedScheduledReport = scheduledReportRepository.findById(scheduledReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScheduledReport are not directly saved in db
        em.detach(updatedScheduledReport);
        updatedScheduledReport
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
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(updatedScheduledReport);

        restScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduledReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledReportDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScheduledReportToMatchAllProperties(updatedScheduledReport);
    }

    @Test
    @Transactional
    void putNonExistingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scheduledReportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScheduledReportWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledReport using partial update
        ScheduledReport partialUpdatedScheduledReport = new ScheduledReport();
        partialUpdatedScheduledReport.setId(scheduledReport.getId());

        partialUpdatedScheduledReport.lastRun(UPDATED_LAST_RUN).createdDate(UPDATED_CREATED_DATE);

        restScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledReport))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScheduledReport, scheduledReport),
            getPersistedScheduledReport(scheduledReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateScheduledReportWithPatch() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scheduledReport using partial update
        ScheduledReport partialUpdatedScheduledReport = new ScheduledReport();
        partialUpdatedScheduledReport.setId(scheduledReport.getId());

        partialUpdatedScheduledReport
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

        restScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScheduledReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScheduledReport))
            )
            .andExpect(status().isOk());

        // Validate the ScheduledReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScheduledReportUpdatableFieldsEquals(
            partialUpdatedScheduledReport,
            getPersistedScheduledReport(partialUpdatedScheduledReport)
        );
    }

    @Test
    @Transactional
    void patchNonExistingScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scheduledReportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scheduledReportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScheduledReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scheduledReport.setId(longCount.incrementAndGet());

        // Create the ScheduledReport
        ScheduledReportDTO scheduledReportDTO = scheduledReportMapper.toDto(scheduledReport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScheduledReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scheduledReportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScheduledReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScheduledReport() throws Exception {
        // Initialize the database
        insertedScheduledReport = scheduledReportRepository.saveAndFlush(scheduledReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scheduledReport
        restScheduledReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, scheduledReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scheduledReportRepository.count();
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

    protected ScheduledReport getPersistedScheduledReport(ScheduledReport scheduledReport) {
        return scheduledReportRepository.findById(scheduledReport.getId()).orElseThrow();
    }

    protected void assertPersistedScheduledReportToMatchAllProperties(ScheduledReport expectedScheduledReport) {
        assertScheduledReportAllPropertiesEquals(expectedScheduledReport, getPersistedScheduledReport(expectedScheduledReport));
    }

    protected void assertPersistedScheduledReportToMatchUpdatableProperties(ScheduledReport expectedScheduledReport) {
        assertScheduledReportAllUpdatablePropertiesEquals(expectedScheduledReport, getPersistedScheduledReport(expectedScheduledReport));
    }
}
