package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.DashboardAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.repository.DashboardRepository;
import fr.smartprod.paperdms.reporting.service.dto.DashboardDTO;
import fr.smartprod.paperdms.reporting.service.mapper.DashboardMapper;
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
 * Integration tests for the {@link DashboardResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DashboardResourceIT {

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

    private static final String ENTITY_API_URL = "/api/dashboards";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DashboardRepository dashboardRepository;

    @Autowired
    private DashboardMapper dashboardMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardMockMvc;

    private Dashboard dashboard;

    private Dashboard insertedDashboard;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Dashboard createEntity() {
        return new Dashboard()
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
    public static Dashboard createUpdatedEntity() {
        return new Dashboard()
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
        dashboard = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDashboard != null) {
            dashboardRepository.delete(insertedDashboard);
            insertedDashboard = null;
        }
    }

    @Test
    @Transactional
    void createDashboard() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);
        var returnedDashboardDTO = om.readValue(
            restDashboardMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DashboardDTO.class
        );

        // Validate the Dashboard in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDashboard = dashboardMapper.toEntity(returnedDashboardDTO);
        assertDashboardUpdatableFieldsEquals(returnedDashboard, getPersistedDashboard(returnedDashboard));

        insertedDashboard = returnedDashboard;
    }

    @Test
    @Transactional
    void createDashboardWithExistingId() throws Exception {
        // Create the Dashboard with an existing ID
        dashboard.setId(1L);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboard.setName(null);

        // Create the Dashboard, which fails.
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboard.setIsPublic(null);

        // Create the Dashboard, which fails.
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboard.setCreatedDate(null);

        // Create the Dashboard, which fails.
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        restDashboardMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDashboards() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())))
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
    void getDashboard() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get the dashboard
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboard.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboard.getId().intValue()))
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
    void getDashboardsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        Long id = dashboard.getId();

        defaultDashboardFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDashboardFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDashboardFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDashboardsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where name equals to
        defaultDashboardFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDashboardsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where name in
        defaultDashboardFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDashboardsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where name is not null
        defaultDashboardFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDashboardsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where name contains
        defaultDashboardFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDashboardsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where name does not contain
        defaultDashboardFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDashboardsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where userId equals to
        defaultDashboardFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDashboardsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where userId in
        defaultDashboardFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDashboardsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where userId is not null
        defaultDashboardFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllDashboardsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where userId contains
        defaultDashboardFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDashboardsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where userId does not contain
        defaultDashboardFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllDashboardsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isPublic equals to
        defaultDashboardFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllDashboardsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isPublic in
        defaultDashboardFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllDashboardsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isPublic is not null
        defaultDashboardFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval equals to
        defaultDashboardFiltering(
            "refreshInterval.equals=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.equals=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval in
        defaultDashboardFiltering(
            "refreshInterval.in=" + DEFAULT_REFRESH_INTERVAL + "," + UPDATED_REFRESH_INTERVAL,
            "refreshInterval.in=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval is not null
        defaultDashboardFiltering("refreshInterval.specified=true", "refreshInterval.specified=false");
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval is greater than or equal to
        defaultDashboardFiltering(
            "refreshInterval.greaterThanOrEqual=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.greaterThanOrEqual=" + UPDATED_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval is less than or equal to
        defaultDashboardFiltering(
            "refreshInterval.lessThanOrEqual=" + DEFAULT_REFRESH_INTERVAL,
            "refreshInterval.lessThanOrEqual=" + SMALLER_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval is less than
        defaultDashboardFiltering(
            "refreshInterval.lessThan=" + UPDATED_REFRESH_INTERVAL,
            "refreshInterval.lessThan=" + DEFAULT_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByRefreshIntervalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where refreshInterval is greater than
        defaultDashboardFiltering(
            "refreshInterval.greaterThan=" + SMALLER_REFRESH_INTERVAL,
            "refreshInterval.greaterThan=" + DEFAULT_REFRESH_INTERVAL
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByIsDefaultIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isDefault equals to
        defaultDashboardFiltering("isDefault.equals=" + DEFAULT_IS_DEFAULT, "isDefault.equals=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllDashboardsByIsDefaultIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isDefault in
        defaultDashboardFiltering("isDefault.in=" + DEFAULT_IS_DEFAULT + "," + UPDATED_IS_DEFAULT, "isDefault.in=" + UPDATED_IS_DEFAULT);
    }

    @Test
    @Transactional
    void getAllDashboardsByIsDefaultIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where isDefault is not null
        defaultDashboardFiltering("isDefault.specified=true", "isDefault.specified=false");
    }

    @Test
    @Transactional
    void getAllDashboardsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where createdDate equals to
        defaultDashboardFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDashboardsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where createdDate in
        defaultDashboardFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDashboardsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        // Get all the dashboardList where createdDate is not null
        defaultDashboardFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultDashboardFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDashboardShouldBeFound(shouldBeFound);
        defaultDashboardShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDashboardShouldBeFound(String filter) throws Exception {
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboard.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].layout").value(hasItem(DEFAULT_LAYOUT)))
            .andExpect(jsonPath("$.[*].refreshInterval").value(hasItem(DEFAULT_REFRESH_INTERVAL)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDashboardShouldNotBeFound(String filter) throws Exception {
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDashboardMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDashboard() throws Exception {
        // Get the dashboard
        restDashboardMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDashboard() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboard
        Dashboard updatedDashboard = dashboardRepository.findById(dashboard.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDashboard are not directly saved in db
        em.detach(updatedDashboard);
        updatedDashboard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userId(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .layout(UPDATED_LAYOUT)
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);
        DashboardDTO dashboardDTO = dashboardMapper.toDto(updatedDashboard);

        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardDTO))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDashboardToMatchAllProperties(updatedDashboard);
    }

    @Test
    @Transactional
    void putNonExistingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard
            .description(UPDATED_DESCRIPTION)
            .isPublic(UPDATED_IS_PUBLIC)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDashboardUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDashboard, dashboard),
            getPersistedDashboard(dashboard)
        );
    }

    @Test
    @Transactional
    void fullUpdateDashboardWithPatch() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboard using partial update
        Dashboard partialUpdatedDashboard = new Dashboard();
        partialUpdatedDashboard.setId(dashboard.getId());

        partialUpdatedDashboard
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .userId(UPDATED_USER_ID)
            .isPublic(UPDATED_IS_PUBLIC)
            .layout(UPDATED_LAYOUT)
            .refreshInterval(UPDATED_REFRESH_INTERVAL)
            .isDefault(UPDATED_IS_DEFAULT)
            .createdDate(UPDATED_CREATED_DATE);

        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboard.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDashboard))
            )
            .andExpect(status().isOk());

        // Validate the Dashboard in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDashboardUpdatableFieldsEquals(partialUpdatedDashboard, getPersistedDashboard(partialUpdatedDashboard));
    }

    @Test
    @Transactional
    void patchNonExistingDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dashboardDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dashboardDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDashboard() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboard.setId(longCount.incrementAndGet());

        // Create the Dashboard
        DashboardDTO dashboardDTO = dashboardMapper.toDto(dashboard);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dashboardDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Dashboard in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDashboard() throws Exception {
        // Initialize the database
        insertedDashboard = dashboardRepository.saveAndFlush(dashboard);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dashboard
        restDashboardMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboard.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dashboardRepository.count();
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

    protected Dashboard getPersistedDashboard(Dashboard dashboard) {
        return dashboardRepository.findById(dashboard.getId()).orElseThrow();
    }

    protected void assertPersistedDashboardToMatchAllProperties(Dashboard expectedDashboard) {
        assertDashboardAllPropertiesEquals(expectedDashboard, getPersistedDashboard(expectedDashboard));
    }

    protected void assertPersistedDashboardToMatchUpdatableProperties(Dashboard expectedDashboard) {
        assertDashboardAllUpdatablePropertiesEquals(expectedDashboard, getPersistedDashboard(expectedDashboard));
    }
}
