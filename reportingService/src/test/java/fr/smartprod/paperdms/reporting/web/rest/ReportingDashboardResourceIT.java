package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import fr.smartprod.paperdms.reporting.repository.ReportingDashboardRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingDashboardMapper;
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
 * Integration tests for the {@link ReportingDashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingDashboardResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final String DEFAULT_LAYOUT = "AAAAAAAAAA";
    private static final String UPDATED_LAYOUT = "BBBBBBBBBB";

    private static final Integer DEFAULT_REFRESH_INTERVAL = 1;
    private static final Integer UPDATED_REFRESH_INTERVAL = 2;
    private static final Integer SMALLER_REFRESH_INTERVAL = 1 - 1;

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/reporting-dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingDashboardRepository reportingDashboardRepository;

    @Autowired
    private ReportingDashboardMapper reportingDashboardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingDashboardMockMvc;

    private ReportingDashboard reportingDashboard;

    private ReportingDashboard insertedReportingDashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingDashboard createEntity() {
        return new ReportingDashboard()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .userId(DEFAULT_USER_ID)
            .isPublic(DEFAULT_IS_PUBLIC)
            .layout(DEFAULT_LAYOUT)
            .refreshInterval(DEFAULT_REFRESH_INTERVAL)
            .isDefault(DEFAULT_IS_DEFAULT)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingDashboard createUpdatedEntity() {
        return new ReportingDashboard()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userId(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .layout(UPDATED_LAYOUT)
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        reportingDashboard = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingDashboard != null) {
            reportingDashboardRepository.delete(insertedReportingDashboard);
            insertedReportingDashboard = null;
        }
    }

    @Test
    @Transactional
    void createReportingDashboard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);
        var returnedReportingDashboardDTO = om.readValue(
            restReportingDashboardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingDashboardDTO.class
        );

        // Validate the ReportingDashboard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingDashboard = reportingDashboardMapper.toEntity(returnedReportingDashboardDTO);
        assertReportingDashboardUpdatableFieldsEquals(
            returnedReportingDashboard,
            getPersistedReportingDashboard(returnedReportingDashboard)
        );

        insertedReportingDashboard = returnedReportingDashboard;
    }

    @Test
    @Transactional
    void createReportingDashboardWithExistingId() throws Exception {
        // Create the ReportingDashboard with an existing ID
        reportingDashboard.setId(1L);
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboard.setName(null);

        // Create the ReportingDashboard, which fails.
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        restReportingDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboard.setIsPublic(null);

        // Create the ReportingDashboard, which fails.
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        restReportingDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboard.setCreatedDate(null);

        // Create the ReportingDashboard, which fails.
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        restReportingDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingDashboards() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT)))
            .andExpect(jsonPath("$.[*].refreshInterval").value(hasItem(DEFAULT_REFRESH_INTERVAL)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getReportingDashboard() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get the reportingDashboard
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingDashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingDashboard.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.layout").value(DEFAULT_LAYOUT))
            .andExpect(jsonPath("$.refreshInterval").value(DEFAULT_REFRESH_INTERVAL))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getReportingDashboardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        Long id = reportingDashboard.getId();

        defaultReportingDashboardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingDashboardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingDashboardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where name equals to
        defaultReportingDashboardFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where name in
        defaultReportingDashboardFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where name is not null
        defaultReportingDashboardFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where name contains
        defaultReportingDashboardFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where name does not contain
        defaultReportingDashboardFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where userId equals to
        defaultReportingDashboardFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where userId in
        defaultReportingDashboardFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where userId is not null
        defaultReportingDashboardFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where userId contains
        defaultReportingDashboardFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where userId does not contain
        defaultReportingDashboardFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isPublic equals to
        defaultReportingDashboardFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isPublic in
        defaultReportingDashboardFiltering(
            "isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC,
            "isPublic.in=" + UPDATED_IS_PUBLIC
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isPublic is not null
        defaultReportingDashboardFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval equals to
        defaultReportingDashboardFiltering(
            "refreshInterval.equals=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.equals=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval in
        defaultReportingDashboardFiltering(
            "refreshInterval.in=" + DEFAULT_REFRESH_INTERVAL + "," + UPDATED_REFRESH_INTERVAL,
            "refreshInterval.in=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval is not null
        defaultReportingDashboardFiltering("refreshInterval.specified=true", "refreshInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval is greater than or equal to
        defaultReportingDashboardFiltering(
            "refreshInterval.greaterThanOrEqual=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.greaterThanOrEqual=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval is less than or equal to
        defaultReportingDashboardFiltering(
            "refreshInterval.lessThanOrEqual=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.lessThanOrEqual=" + SMALLER_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval is less than
        defaultReportingDashboardFiltering(
            "refreshInterval.lessThan=" + UPDATED_REFRESH_INTERVAL,
            "refreshInterval.lessThan=" + DEFAULT_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByRefreshIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where refreshInterval is greater than
        defaultReportingDashboardFiltering(
            "refreshInterval.greaterThan=" + SMALLER_REFRESH_INTERVAL,
            "refreshInterval.greaterThan=" + DEFAULT_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isDefault equals to
        defaultReportingDashboardFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isDefault in
        defaultReportingDashboardFiltering(
            "isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT,
            "isDefault.in=" + UPDATED_IS_DEFAULT
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where isDefault is not null
        defaultReportingDashboardFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where createdDate equals to
        defaultReportingDashboardFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where createdDate in
        defaultReportingDashboardFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        // Get all the reportingDashboardList where createdDate is not null
        defaultReportingDashboardFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultReportingDashboardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingDashboardShouldBeFound(shouldBeFound);
        defaultReportingDashboardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingDashboardShouldBeFound(String filter) throws Exception {
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT)))
            .andExpect(jsonPath("$.[*].refreshInterval").value(hasItem(DEFAULT_REFRESH_INTERVAL)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingDashboardShouldNotBeFound(String filter) throws Exception {
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingDashboardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingDashboard() throws Exception {
        // Get the reportingDashboard
        restReportingDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingDashboard() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboard
        ReportingDashboard updatedReportingDashboard = reportingDashboardRepository.findById(reportingDashboard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedReportingDashboard are not directly saved in db
        em.detach(updatedReportingDashboard);
        updatedReportingDashboard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userId(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .layout(UPDATED_LAYOUT)
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(updatedReportingDashboard);

        restReportingDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingDashboardToMatchAllProperties(updatedReportingDashboard);
    }

    @Test
    @Transactional
    void putNonExistingReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboard using partial update
        ReportingDashboard partialUpdatedReportingDashboard = new ReportingDashboard();
        partialUpdatedReportingDashboard.setId(reportingDashboard.getId());

        partialUpdatedReportingDashboard
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);

        restReportingDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingDashboard))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingDashboardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingDashboard, reportingDashboard),
            getPersistedReportingDashboard(reportingDashboard)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboard using partial update
        ReportingDashboard partialUpdatedReportingDashboard = new ReportingDashboard();
        partialUpdatedReportingDashboard.setId(reportingDashboard.getId());

        partialUpdatedReportingDashboard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userId(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .layout(UPDATED_LAYOUT)
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);

        restReportingDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingDashboard))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingDashboardUpdatableFieldsEquals(
            partialUpdatedReportingDashboard,
            getPersistedReportingDashboard(partialUpdatedReportingDashboard)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingDashboardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingDashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboard.setId(longCount.incrementAndGet());

        // Create the ReportingDashboard
        ReportingDashboardDTO reportingDashboardDTO = reportingDashboardMapper.toDto(reportingDashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportingDashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingDashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingDashboard() throws Exception {
        // Initialize the database
        insertedReportingDashboard = reportingDashboardRepository.saveAndFlush(reportingDashboard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingDashboard
        restReportingDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingDashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingDashboardRepository.count();
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

    protected ReportingDashboard getPersistedReportingDashboard(ReportingDashboard reportingDashboard) {
        return reportingDashboardRepository.findById(reportingDashboard.getId()).orElseThrow();
    }

    protected void assertPersistedReportingDashboardToMatchAllProperties(ReportingDashboard expectedReportingDashboard) {
        assertReportingDashboardAllPropertiesEquals(expectedReportingDashboard, getPersistedReportingDashboard(expectedReportingDashboard));
    }

    protected void assertPersistedReportingDashboardToMatchUpdatableProperties(ReportingDashboard expectedReportingDashboard) {
        assertReportingDashboardAllUpdatablePropertiesEquals(
            expectedReportingDashboard,
            getPersistedReportingDashboard(expectedReportingDashboard)
        );
    }
}
