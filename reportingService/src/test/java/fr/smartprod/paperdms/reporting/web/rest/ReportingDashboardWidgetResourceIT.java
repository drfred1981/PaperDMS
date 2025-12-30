package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidgetAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.ReportingDashboard;
import fr.smartprod.paperdms.reporting.domain.ReportingDashboardWidget;
import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import fr.smartprod.paperdms.reporting.repository.ReportingDashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.dto.ReportingDashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.service.mapper.ReportingDashboardWidgetMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ReportingDashboardWidgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ReportingDashboardWidgetResourceIT {

    private static final WidgetType DEFAULT_WIDGET_TYPE = WidgetType.DOCUMENT_COUNT;
    private static final WidgetType UPDATED_WIDGET_TYPE = WidgetType.STORAGE_USAGE;

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final String DEFAULT_DATA_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_DATA_SOURCE = "BBBBBBBBBB";

    private static final Integer DEFAULT_POSITION = 1;
    private static final Integer UPDATED_POSITION = 2;
    private static final Integer SMALLER_POSITION = 1 - 1;

    private static final Integer DEFAULT_SIZE_X = 1;
    private static final Integer UPDATED_SIZE_X = 2;
    private static final Integer SMALLER_SIZE_X = 1 - 1;

    private static final Integer DEFAULT_SIZE_Y = 1;
    private static final Integer UPDATED_SIZE_Y = 2;
    private static final Integer SMALLER_SIZE_Y = 1 - 1;

    private static final String ENTITY_API_URL = "/api/reporting-dashboard-widgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ReportingDashboardWidgetRepository reportingDashboardWidgetRepository;

    @Autowired
    private ReportingDashboardWidgetMapper reportingDashboardWidgetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restReportingDashboardWidgetMockMvc;

    private ReportingDashboardWidget reportingDashboardWidget;

    private ReportingDashboardWidget insertedReportingDashboardWidget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingDashboardWidget createEntity() {
        return new ReportingDashboardWidget()
            .widgetType(DEFAULT_WIDGET_TYPE)
            .title(DEFAULT_TITLE)
            .configuration(DEFAULT_CONFIGURATION)
            .dataSource(DEFAULT_DATA_SOURCE)
            .position(DEFAULT_POSITION)
            .sizeX(DEFAULT_SIZE_X)
            .sizeY(DEFAULT_SIZE_Y);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ReportingDashboardWidget createUpdatedEntity() {
        return new ReportingDashboardWidget()
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);
    }

    @BeforeEach
    void initTest() {
        reportingDashboardWidget = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedReportingDashboardWidget != null) {
            reportingDashboardWidgetRepository.delete(insertedReportingDashboardWidget);
            insertedReportingDashboardWidget = null;
        }
    }

    @Test
    @Transactional
    void createReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);
        var returnedReportingDashboardWidgetDTO = om.readValue(
            restReportingDashboardWidgetMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ReportingDashboardWidgetDTO.class
        );

        // Validate the ReportingDashboardWidget in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedReportingDashboardWidget = reportingDashboardWidgetMapper.toEntity(returnedReportingDashboardWidgetDTO);
        assertReportingDashboardWidgetUpdatableFieldsEquals(
            returnedReportingDashboardWidget,
            getPersistedReportingDashboardWidget(returnedReportingDashboardWidget)
        );

        insertedReportingDashboardWidget = returnedReportingDashboardWidget;
    }

    @Test
    @Transactional
    void createReportingDashboardWidgetWithExistingId() throws Exception {
        // Create the ReportingDashboardWidget with an existing ID
        reportingDashboardWidget.setId(1L);
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkWidgetTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboardWidget.setWidgetType(null);

        // Create the ReportingDashboardWidget, which fails.
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboardWidget.setTitle(null);

        // Create the ReportingDashboardWidget, which fails.
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboardWidget.setPosition(null);

        // Create the ReportingDashboardWidget, which fails.
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeXIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboardWidget.setSizeX(null);

        // Create the ReportingDashboardWidget, which fails.
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeYIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        reportingDashboardWidget.setSizeY(null);

        // Create the ReportingDashboardWidget, which fails.
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgets() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboardWidget.getId().intValue())))
            .andExpect(jsonPath("$.[*].widgetType").value(hasItem(DEFAULT_WIDGET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].dataSource").value(hasItem(DEFAULT_DATA_SOURCE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].sizeX").value(hasItem(DEFAULT_SIZE_X)))
            .andExpect(jsonPath("$.[*].sizeY").value(hasItem(DEFAULT_SIZE_Y)));
    }

    @Test
    @Transactional
    void getReportingDashboardWidget() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get the reportingDashboardWidget
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL_ID, reportingDashboardWidget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(reportingDashboardWidget.getId().intValue()))
            .andExpect(jsonPath("$.widgetType").value(DEFAULT_WIDGET_TYPE.toString()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION))
            .andExpect(jsonPath("$.dataSource").value(DEFAULT_DATA_SOURCE))
            .andExpect(jsonPath("$.position").value(DEFAULT_POSITION))
            .andExpect(jsonPath("$.sizeX").value(DEFAULT_SIZE_X))
            .andExpect(jsonPath("$.sizeY").value(DEFAULT_SIZE_Y));
    }

    @Test
    @Transactional
    void getReportingDashboardWidgetsByIdFiltering() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        Long id = reportingDashboardWidget.getId();

        defaultReportingDashboardWidgetFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultReportingDashboardWidgetFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultReportingDashboardWidgetFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByWidgetTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where widgetType equals to
        defaultReportingDashboardWidgetFiltering("widgetType.equals=" + DEFAULT_WIDGET_TYPE, "widgetType.equals=" + UPDATED_WIDGET_TYPE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByWidgetTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where widgetType in
        defaultReportingDashboardWidgetFiltering(
            "widgetType.in=" + DEFAULT_WIDGET_TYPE + "," + UPDATED_WIDGET_TYPE,
            "widgetType.in=" + UPDATED_WIDGET_TYPE
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByWidgetTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where widgetType is not null
        defaultReportingDashboardWidgetFiltering("widgetType.specified=true", "widgetType.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where title equals to
        defaultReportingDashboardWidgetFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where title in
        defaultReportingDashboardWidgetFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where title is not null
        defaultReportingDashboardWidgetFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where title contains
        defaultReportingDashboardWidgetFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where title does not contain
        defaultReportingDashboardWidgetFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDataSourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where dataSource equals to
        defaultReportingDashboardWidgetFiltering("dataSource.equals=" + DEFAULT_DATA_SOURCE, "dataSource.equals=" + UPDATED_DATA_SOURCE);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDataSourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where dataSource in
        defaultReportingDashboardWidgetFiltering(
            "dataSource.in=" + DEFAULT_DATA_SOURCE + "," + UPDATED_DATA_SOURCE,
            "dataSource.in=" + UPDATED_DATA_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDataSourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where dataSource is not null
        defaultReportingDashboardWidgetFiltering("dataSource.specified=true", "dataSource.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDataSourceContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where dataSource contains
        defaultReportingDashboardWidgetFiltering(
            "dataSource.contains=" + DEFAULT_DATA_SOURCE,
            "dataSource.contains=" + UPDATED_DATA_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDataSourceNotContainsSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where dataSource does not contain
        defaultReportingDashboardWidgetFiltering(
            "dataSource.doesNotContain=" + UPDATED_DATA_SOURCE,
            "dataSource.doesNotContain=" + DEFAULT_DATA_SOURCE
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position equals to
        defaultReportingDashboardWidgetFiltering("position.equals=" + DEFAULT_POSITION, "position.equals=" + UPDATED_POSITION);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position in
        defaultReportingDashboardWidgetFiltering(
            "position.in=" + DEFAULT_POSITION + "," + UPDATED_POSITION,
            "position.in=" + UPDATED_POSITION
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position is not null
        defaultReportingDashboardWidgetFiltering("position.specified=true", "position.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position is greater than or equal to
        defaultReportingDashboardWidgetFiltering(
            "position.greaterThanOrEqual=" + DEFAULT_POSITION,
            "position.greaterThanOrEqual=" + UPDATED_POSITION
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position is less than or equal to
        defaultReportingDashboardWidgetFiltering(
            "position.lessThanOrEqual=" + DEFAULT_POSITION,
            "position.lessThanOrEqual=" + SMALLER_POSITION
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position is less than
        defaultReportingDashboardWidgetFiltering("position.lessThan=" + UPDATED_POSITION, "position.lessThan=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByPositionIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where position is greater than
        defaultReportingDashboardWidgetFiltering("position.greaterThan=" + SMALLER_POSITION, "position.greaterThan=" + DEFAULT_POSITION);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX equals to
        defaultReportingDashboardWidgetFiltering("sizeX.equals=" + DEFAULT_SIZE_X, "sizeX.equals=" + UPDATED_SIZE_X);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX in
        defaultReportingDashboardWidgetFiltering("sizeX.in=" + DEFAULT_SIZE_X + "," + UPDATED_SIZE_X, "sizeX.in=" + UPDATED_SIZE_X);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX is not null
        defaultReportingDashboardWidgetFiltering("sizeX.specified=true", "sizeX.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX is greater than or equal to
        defaultReportingDashboardWidgetFiltering(
            "sizeX.greaterThanOrEqual=" + DEFAULT_SIZE_X,
            "sizeX.greaterThanOrEqual=" + UPDATED_SIZE_X
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX is less than or equal to
        defaultReportingDashboardWidgetFiltering("sizeX.lessThanOrEqual=" + DEFAULT_SIZE_X, "sizeX.lessThanOrEqual=" + SMALLER_SIZE_X);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX is less than
        defaultReportingDashboardWidgetFiltering("sizeX.lessThan=" + UPDATED_SIZE_X, "sizeX.lessThan=" + DEFAULT_SIZE_X);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeXIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeX is greater than
        defaultReportingDashboardWidgetFiltering("sizeX.greaterThan=" + SMALLER_SIZE_X, "sizeX.greaterThan=" + DEFAULT_SIZE_X);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY equals to
        defaultReportingDashboardWidgetFiltering("sizeY.equals=" + DEFAULT_SIZE_Y, "sizeY.equals=" + UPDATED_SIZE_Y);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsInShouldWork() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY in
        defaultReportingDashboardWidgetFiltering("sizeY.in=" + DEFAULT_SIZE_Y + "," + UPDATED_SIZE_Y, "sizeY.in=" + UPDATED_SIZE_Y);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY is not null
        defaultReportingDashboardWidgetFiltering("sizeY.specified=true", "sizeY.specified=false");
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY is greater than or equal to
        defaultReportingDashboardWidgetFiltering(
            "sizeY.greaterThanOrEqual=" + DEFAULT_SIZE_Y,
            "sizeY.greaterThanOrEqual=" + UPDATED_SIZE_Y
        );
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY is less than or equal to
        defaultReportingDashboardWidgetFiltering("sizeY.lessThanOrEqual=" + DEFAULT_SIZE_Y, "sizeY.lessThanOrEqual=" + SMALLER_SIZE_Y);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY is less than
        defaultReportingDashboardWidgetFiltering("sizeY.lessThan=" + UPDATED_SIZE_Y, "sizeY.lessThan=" + DEFAULT_SIZE_Y);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsBySizeYIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        // Get all the reportingDashboardWidgetList where sizeY is greater than
        defaultReportingDashboardWidgetFiltering("sizeY.greaterThan=" + SMALLER_SIZE_Y, "sizeY.greaterThan=" + DEFAULT_SIZE_Y);
    }

    @Test
    @Transactional
    void getAllReportingDashboardWidgetsByDashboarIsEqualToSomething() throws Exception {
        ReportingDashboard dashboar;
        if (TestUtil.findAll(em, ReportingDashboard.class).isEmpty()) {
            reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);
            dashboar = ReportingDashboardResourceIT.createEntity();
        } else {
            dashboar = TestUtil.findAll(em, ReportingDashboard.class).get(0);
        }
        em.persist(dashboar);
        em.flush();
        reportingDashboardWidget.setDashboar(dashboar);
        reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);
        Long dashboarId = dashboar.getId();
        // Get all the reportingDashboardWidgetList where dashboar equals to dashboarId
        defaultReportingDashboardWidgetShouldBeFound("dashboarId.equals=" + dashboarId);

        // Get all the reportingDashboardWidgetList where dashboar equals to (dashboarId + 1)
        defaultReportingDashboardWidgetShouldNotBeFound("dashboarId.equals=" + (dashboarId + 1));
    }

    private void defaultReportingDashboardWidgetFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultReportingDashboardWidgetShouldBeFound(shouldBeFound);
        defaultReportingDashboardWidgetShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultReportingDashboardWidgetShouldBeFound(String filter) throws Exception {
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(reportingDashboardWidget.getId().intValue())))
            .andExpect(jsonPath("$.[*].widgetType").value(hasItem(DEFAULT_WIDGET_TYPE.toString())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)))
            .andExpect(jsonPath("$.[*].dataSource").value(hasItem(DEFAULT_DATA_SOURCE)))
            .andExpect(jsonPath("$.[*].position").value(hasItem(DEFAULT_POSITION)))
            .andExpect(jsonPath("$.[*].sizeX").value(hasItem(DEFAULT_SIZE_X)))
            .andExpect(jsonPath("$.[*].sizeY").value(hasItem(DEFAULT_SIZE_Y)));

        // Check, that the count call also returns 1
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultReportingDashboardWidgetShouldNotBeFound(String filter) throws Exception {
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restReportingDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingReportingDashboardWidget() throws Exception {
        // Get the reportingDashboardWidget
        restReportingDashboardWidgetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingReportingDashboardWidget() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboardWidget
        ReportingDashboardWidget updatedReportingDashboardWidget = reportingDashboardWidgetRepository
            .findById(reportingDashboardWidget.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedReportingDashboardWidget are not directly saved in db
        em.detach(updatedReportingDashboardWidget);
        updatedReportingDashboardWidget
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(updatedReportingDashboardWidget);

        restReportingDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDashboardWidgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedReportingDashboardWidgetToMatchAllProperties(updatedReportingDashboardWidget);
    }

    @Test
    @Transactional
    void putNonExistingReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, reportingDashboardWidgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(reportingDashboardWidgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateReportingDashboardWidgetWithPatch() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboardWidget using partial update
        ReportingDashboardWidget partialUpdatedReportingDashboardWidget = new ReportingDashboardWidget();
        partialUpdatedReportingDashboardWidget.setId(reportingDashboardWidget.getId());

        partialUpdatedReportingDashboardWidget
            .title(UPDATED_TITLE)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);

        restReportingDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingDashboardWidget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingDashboardWidget))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboardWidget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingDashboardWidgetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedReportingDashboardWidget, reportingDashboardWidget),
            getPersistedReportingDashboardWidget(reportingDashboardWidget)
        );
    }

    @Test
    @Transactional
    void fullUpdateReportingDashboardWidgetWithPatch() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the reportingDashboardWidget using partial update
        ReportingDashboardWidget partialUpdatedReportingDashboardWidget = new ReportingDashboardWidget();
        partialUpdatedReportingDashboardWidget.setId(reportingDashboardWidget.getId());

        partialUpdatedReportingDashboardWidget
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);

        restReportingDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedReportingDashboardWidget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedReportingDashboardWidget))
            )
            .andExpect(status().isOk());

        // Validate the ReportingDashboardWidget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertReportingDashboardWidgetUpdatableFieldsEquals(
            partialUpdatedReportingDashboardWidget,
            getPersistedReportingDashboardWidget(partialUpdatedReportingDashboardWidget)
        );
    }

    @Test
    @Transactional
    void patchNonExistingReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, reportingDashboardWidgetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamReportingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        reportingDashboardWidget.setId(longCount.incrementAndGet());

        // Create the ReportingDashboardWidget
        ReportingDashboardWidgetDTO reportingDashboardWidgetDTO = reportingDashboardWidgetMapper.toDto(reportingDashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restReportingDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(reportingDashboardWidgetDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ReportingDashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteReportingDashboardWidget() throws Exception {
        // Initialize the database
        insertedReportingDashboardWidget = reportingDashboardWidgetRepository.saveAndFlush(reportingDashboardWidget);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the reportingDashboardWidget
        restReportingDashboardWidgetMockMvc
            .perform(delete(ENTITY_API_URL_ID, reportingDashboardWidget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return reportingDashboardWidgetRepository.count();
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

    protected ReportingDashboardWidget getPersistedReportingDashboardWidget(ReportingDashboardWidget reportingDashboardWidget) {
        return reportingDashboardWidgetRepository.findById(reportingDashboardWidget.getId()).orElseThrow();
    }

    protected void assertPersistedReportingDashboardWidgetToMatchAllProperties(ReportingDashboardWidget expectedReportingDashboardWidget) {
        assertReportingDashboardWidgetAllPropertiesEquals(
            expectedReportingDashboardWidget,
            getPersistedReportingDashboardWidget(expectedReportingDashboardWidget)
        );
    }

    protected void assertPersistedReportingDashboardWidgetToMatchUpdatableProperties(
        ReportingDashboardWidget expectedReportingDashboardWidget
    ) {
        assertReportingDashboardWidgetAllUpdatablePropertiesEquals(
            expectedReportingDashboardWidget,
            getPersistedReportingDashboardWidget(expectedReportingDashboardWidget)
        );
    }
}
