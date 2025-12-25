package fr.smartprod.paperdms.scan.web.rest;

import static fr.smartprod.paperdms.scan.domain.ScanBatchAsserts.*;
import static fr.smartprod.paperdms.scan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.scan.IntegrationTest;
import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.domain.enumeration.BatchStatus;
import fr.smartprod.paperdms.scan.repository.ScanBatchRepository;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScanBatchMapper;
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
 * Integration tests for the {@link ScanBatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScanBatchResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Integer DEFAULT_TOTAL_JOBS = 1;
    private static final Integer UPDATED_TOTAL_JOBS = 2;
    private static final Integer SMALLER_TOTAL_JOBS = 1 - 1;

    private static final Integer DEFAULT_COMPLETED_JOBS = 1;
    private static final Integer UPDATED_COMPLETED_JOBS = 2;
    private static final Integer SMALLER_COMPLETED_JOBS = 1 - 1;

    private static final Integer DEFAULT_TOTAL_PAGES = 1;
    private static final Integer UPDATED_TOTAL_PAGES = 2;
    private static final Integer SMALLER_TOTAL_PAGES = 1 - 1;

    private static final BatchStatus DEFAULT_STATUS = BatchStatus.PENDING;
    private static final BatchStatus UPDATED_STATUS = BatchStatus.IN_PROGRESS;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/scan-batches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScanBatchRepository scanBatchRepository;

    @Autowired
    private ScanBatchMapper scanBatchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScanBatchMockMvc;

    private ScanBatch scanBatch;

    private ScanBatch insertedScanBatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScanBatch createEntity() {
        return new ScanBatch()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .totalJobs(DEFAULT_TOTAL_JOBS)
            .completedJobs(DEFAULT_COMPLETED_JOBS)
            .totalPages(DEFAULT_TOTAL_PAGES)
            .status(DEFAULT_STATUS)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScanBatch createUpdatedEntity() {
        return new ScanBatch()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalJobs(UPDATED_TOTAL_JOBS)
            .completedJobs(UPDATED_COMPLETED_JOBS)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        scanBatch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScanBatch != null) {
            scanBatchRepository.delete(insertedScanBatch);
            insertedScanBatch = null;
        }
    }

    @Test
    @Transactional
    void createScanBatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);
        var returnedScanBatchDTO = om.readValue(
            restScanBatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScanBatchDTO.class
        );

        // Validate the ScanBatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScanBatch = scanBatchMapper.toEntity(returnedScanBatchDTO);
        assertScanBatchUpdatableFieldsEquals(returnedScanBatch, getPersistedScanBatch(returnedScanBatch));

        insertedScanBatch = returnedScanBatch;
    }

    @Test
    @Transactional
    void createScanBatchWithExistingId() throws Exception {
        // Create the ScanBatch with an existing ID
        scanBatch.setId(1L);
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScanBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanBatch.setName(null);

        // Create the ScanBatch, which fails.
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        restScanBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanBatch.setStatus(null);

        // Create the ScanBatch, which fails.
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        restScanBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanBatch.setCreatedBy(null);

        // Create the ScanBatch, which fails.
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        restScanBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scanBatch.setCreatedDate(null);

        // Create the ScanBatch, which fails.
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        restScanBatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScanBatches() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scanBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalJobs").value(hasItem(DEFAULT_TOTAL_JOBS)))
            .andExpect(jsonPath("$.[*].completedJobs").value(hasItem(DEFAULT_COMPLETED_JOBS)))
            .andExpect(jsonPath("$.[*].totalPages").value(hasItem(DEFAULT_TOTAL_PAGES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getScanBatch() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get the scanBatch
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL_ID, scanBatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scanBatch.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.totalJobs").value(DEFAULT_TOTAL_JOBS))
            .andExpect(jsonPath("$.completedJobs").value(DEFAULT_COMPLETED_JOBS))
            .andExpect(jsonPath("$.totalPages").value(DEFAULT_TOTAL_PAGES))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getScanBatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        Long id = scanBatch.getId();

        defaultScanBatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultScanBatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultScanBatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllScanBatchesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where name equals to
        defaultScanBatchFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanBatchesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where name in
        defaultScanBatchFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanBatchesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where name is not null
        defaultScanBatchFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where name contains
        defaultScanBatchFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllScanBatchesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where name does not contain
        defaultScanBatchFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs equals to
        defaultScanBatchFiltering("totalJobs.equals=" + DEFAULT_TOTAL_JOBS, "totalJobs.equals=" + UPDATED_TOTAL_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs in
        defaultScanBatchFiltering("totalJobs.in=" + DEFAULT_TOTAL_JOBS + "," + UPDATED_TOTAL_JOBS, "totalJobs.in=" + UPDATED_TOTAL_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs is not null
        defaultScanBatchFiltering("totalJobs.specified=true", "totalJobs.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs is greater than or equal to
        defaultScanBatchFiltering(
            "totalJobs.greaterThanOrEqual=" + DEFAULT_TOTAL_JOBS,
            "totalJobs.greaterThanOrEqual=" + UPDATED_TOTAL_JOBS
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs is less than or equal to
        defaultScanBatchFiltering("totalJobs.lessThanOrEqual=" + DEFAULT_TOTAL_JOBS, "totalJobs.lessThanOrEqual=" + SMALLER_TOTAL_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs is less than
        defaultScanBatchFiltering("totalJobs.lessThan=" + UPDATED_TOTAL_JOBS, "totalJobs.lessThan=" + DEFAULT_TOTAL_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalJobsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalJobs is greater than
        defaultScanBatchFiltering("totalJobs.greaterThan=" + SMALLER_TOTAL_JOBS, "totalJobs.greaterThan=" + DEFAULT_TOTAL_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs equals to
        defaultScanBatchFiltering("completedJobs.equals=" + DEFAULT_COMPLETED_JOBS, "completedJobs.equals=" + UPDATED_COMPLETED_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs in
        defaultScanBatchFiltering(
            "completedJobs.in=" + DEFAULT_COMPLETED_JOBS + "," + UPDATED_COMPLETED_JOBS,
            "completedJobs.in=" + UPDATED_COMPLETED_JOBS
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs is not null
        defaultScanBatchFiltering("completedJobs.specified=true", "completedJobs.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs is greater than or equal to
        defaultScanBatchFiltering(
            "completedJobs.greaterThanOrEqual=" + DEFAULT_COMPLETED_JOBS,
            "completedJobs.greaterThanOrEqual=" + UPDATED_COMPLETED_JOBS
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs is less than or equal to
        defaultScanBatchFiltering(
            "completedJobs.lessThanOrEqual=" + DEFAULT_COMPLETED_JOBS,
            "completedJobs.lessThanOrEqual=" + SMALLER_COMPLETED_JOBS
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs is less than
        defaultScanBatchFiltering("completedJobs.lessThan=" + UPDATED_COMPLETED_JOBS, "completedJobs.lessThan=" + DEFAULT_COMPLETED_JOBS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCompletedJobsIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where completedJobs is greater than
        defaultScanBatchFiltering(
            "completedJobs.greaterThan=" + SMALLER_COMPLETED_JOBS,
            "completedJobs.greaterThan=" + DEFAULT_COMPLETED_JOBS
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages equals to
        defaultScanBatchFiltering("totalPages.equals=" + DEFAULT_TOTAL_PAGES, "totalPages.equals=" + UPDATED_TOTAL_PAGES);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages in
        defaultScanBatchFiltering(
            "totalPages.in=" + DEFAULT_TOTAL_PAGES + "," + UPDATED_TOTAL_PAGES,
            "totalPages.in=" + UPDATED_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages is not null
        defaultScanBatchFiltering("totalPages.specified=true", "totalPages.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages is greater than or equal to
        defaultScanBatchFiltering(
            "totalPages.greaterThanOrEqual=" + DEFAULT_TOTAL_PAGES,
            "totalPages.greaterThanOrEqual=" + UPDATED_TOTAL_PAGES
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages is less than or equal to
        defaultScanBatchFiltering("totalPages.lessThanOrEqual=" + DEFAULT_TOTAL_PAGES, "totalPages.lessThanOrEqual=" + SMALLER_TOTAL_PAGES);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages is less than
        defaultScanBatchFiltering("totalPages.lessThan=" + UPDATED_TOTAL_PAGES, "totalPages.lessThan=" + DEFAULT_TOTAL_PAGES);
    }

    @Test
    @Transactional
    void getAllScanBatchesByTotalPagesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where totalPages is greater than
        defaultScanBatchFiltering("totalPages.greaterThan=" + SMALLER_TOTAL_PAGES, "totalPages.greaterThan=" + DEFAULT_TOTAL_PAGES);
    }

    @Test
    @Transactional
    void getAllScanBatchesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where status equals to
        defaultScanBatchFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where status in
        defaultScanBatchFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllScanBatchesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where status is not null
        defaultScanBatchFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdBy equals to
        defaultScanBatchFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdBy in
        defaultScanBatchFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdBy is not null
        defaultScanBatchFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdBy contains
        defaultScanBatchFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdBy does not contain
        defaultScanBatchFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdDate equals to
        defaultScanBatchFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdDate in
        defaultScanBatchFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllScanBatchesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        // Get all the scanBatchList where createdDate is not null
        defaultScanBatchFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultScanBatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultScanBatchShouldBeFound(shouldBeFound);
        defaultScanBatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultScanBatchShouldBeFound(String filter) throws Exception {
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scanBatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].totalJobs").value(hasItem(DEFAULT_TOTAL_JOBS)))
            .andExpect(jsonPath("$.[*].completedJobs").value(hasItem(DEFAULT_COMPLETED_JOBS)))
            .andExpect(jsonPath("$.[*].totalPages").value(hasItem(DEFAULT_TOTAL_PAGES)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultScanBatchShouldNotBeFound(String filter) throws Exception {
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restScanBatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingScanBatch() throws Exception {
        // Get the scanBatch
        restScanBatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScanBatch() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanBatch
        ScanBatch updatedScanBatch = scanBatchRepository.findById(scanBatch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScanBatch are not directly saved in db
        em.detach(updatedScanBatch);
        updatedScanBatch
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalJobs(UPDATED_TOTAL_JOBS)
            .completedJobs(UPDATED_COMPLETED_JOBS)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(updatedScanBatch);

        restScanBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scanBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scanBatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScanBatchToMatchAllProperties(updatedScanBatch);
    }

    @Test
    @Transactional
    void putNonExistingScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scanBatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scanBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scanBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScanBatchWithPatch() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanBatch using partial update
        ScanBatch partialUpdatedScanBatch = new ScanBatch();
        partialUpdatedScanBatch.setId(scanBatch.getId());

        partialUpdatedScanBatch
            .description(UPDATED_DESCRIPTION)
            .totalJobs(UPDATED_TOTAL_JOBS)
            .completedJobs(UPDATED_COMPLETED_JOBS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restScanBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScanBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScanBatch))
            )
            .andExpect(status().isOk());

        // Validate the ScanBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScanBatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScanBatch, scanBatch),
            getPersistedScanBatch(scanBatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateScanBatchWithPatch() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scanBatch using partial update
        ScanBatch partialUpdatedScanBatch = new ScanBatch();
        partialUpdatedScanBatch.setId(scanBatch.getId());

        partialUpdatedScanBatch
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .totalJobs(UPDATED_TOTAL_JOBS)
            .completedJobs(UPDATED_COMPLETED_JOBS)
            .totalPages(UPDATED_TOTAL_PAGES)
            .status(UPDATED_STATUS)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restScanBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScanBatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScanBatch))
            )
            .andExpect(status().isOk());

        // Validate the ScanBatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScanBatchUpdatableFieldsEquals(partialUpdatedScanBatch, getPersistedScanBatch(partialUpdatedScanBatch));
    }

    @Test
    @Transactional
    void patchNonExistingScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scanBatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scanBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scanBatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScanBatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scanBatch.setId(longCount.incrementAndGet());

        // Create the ScanBatch
        ScanBatchDTO scanBatchDTO = scanBatchMapper.toDto(scanBatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScanBatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scanBatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScanBatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScanBatch() throws Exception {
        // Initialize the database
        insertedScanBatch = scanBatchRepository.saveAndFlush(scanBatch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scanBatch
        restScanBatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, scanBatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scanBatchRepository.count();
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

    protected ScanBatch getPersistedScanBatch(ScanBatch scanBatch) {
        return scanBatchRepository.findById(scanBatch.getId()).orElseThrow();
    }

    protected void assertPersistedScanBatchToMatchAllProperties(ScanBatch expectedScanBatch) {
        assertScanBatchAllPropertiesEquals(expectedScanBatch, getPersistedScanBatch(expectedScanBatch));
    }

    protected void assertPersistedScanBatchToMatchUpdatableProperties(ScanBatch expectedScanBatch) {
        assertScanBatchAllUpdatablePropertiesEquals(expectedScanBatch, getPersistedScanBatch(expectedScanBatch));
    }
}
