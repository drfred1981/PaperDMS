package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.DashboardWidgetAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.Dashboard;
import fr.smartprod.paperdms.reporting.domain.DashboardWidget;
import fr.smartprod.paperdms.reporting.domain.enumeration.WidgetType;
import fr.smartprod.paperdms.reporting.repository.DashboardWidgetRepository;
import fr.smartprod.paperdms.reporting.service.dto.DashboardWidgetDTO;
import fr.smartprod.paperdms.reporting.service.mapper.DashboardWidgetMapper;
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
 * Integration tests for the {@link DashboardWidgetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DashboardWidgetResourceIT {

    private static final Long DEFAULT_DASHBOARD_ID = 1L;
    private static final Long UPDATED_DASHBOARD_ID = 2L;

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

    private static final Integer DEFAULT_SIZE_X = 1;
    private static final Integer UPDATED_SIZE_X = 2;

    private static final Integer DEFAULT_SIZE_Y = 1;
    private static final Integer UPDATED_SIZE_Y = 2;

    private static final String ENTITY_API_URL = "/api/dashboard-widgets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DashboardWidgetRepository dashboardWidgetRepository;

    @Autowired
    private DashboardWidgetMapper dashboardWidgetMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDashboardWidgetMockMvc;

    private DashboardWidget dashboardWidget;

    private DashboardWidget insertedDashboardWidget;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DashboardWidget createEntity(EntityManager em) {
        DashboardWidget dashboardWidget = new DashboardWidget()
            .dashboardId(DEFAULT_DASHBOARD_ID)
            .widgetType(DEFAULT_WIDGET_TYPE)
            .title(DEFAULT_TITLE)
            .configuration(DEFAULT_CONFIGURATION)
            .dataSource(DEFAULT_DATA_SOURCE)
            .position(DEFAULT_POSITION)
            .sizeX(DEFAULT_SIZE_X)
            .sizeY(DEFAULT_SIZE_Y);
        // Add required entity
        Dashboard dashboard;
        if (TestUtil.findAll(em, Dashboard.class).isEmpty()) {
            dashboard = DashboardResourceIT.createEntity();
            em.persist(dashboard);
            em.flush();
        } else {
            dashboard = TestUtil.findAll(em, Dashboard.class).get(0);
        }
        dashboardWidget.setDashboard(dashboard);
        return dashboardWidget;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DashboardWidget createUpdatedEntity(EntityManager em) {
        DashboardWidget updatedDashboardWidget = new DashboardWidget()
            .dashboardId(UPDATED_DASHBOARD_ID)
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);
        // Add required entity
        Dashboard dashboard;
        if (TestUtil.findAll(em, Dashboard.class).isEmpty()) {
            dashboard = DashboardResourceIT.createUpdatedEntity();
            em.persist(dashboard);
            em.flush();
        } else {
            dashboard = TestUtil.findAll(em, Dashboard.class).get(0);
        }
        updatedDashboardWidget.setDashboard(dashboard);
        return updatedDashboardWidget;
    }

    @BeforeEach
    void initTest() {
        dashboardWidget = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDashboardWidget != null) {
            dashboardWidgetRepository.delete(insertedDashboardWidget);
            insertedDashboardWidget = null;
        }
    }

    @Test
    @Transactional
    void createDashboardWidget() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);
        var returnedDashboardWidgetDTO = om.readValue(
            restDashboardWidgetMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DashboardWidgetDTO.class
        );

        // Validate the DashboardWidget in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDashboardWidget = dashboardWidgetMapper.toEntity(returnedDashboardWidgetDTO);
        assertDashboardWidgetUpdatableFieldsEquals(returnedDashboardWidget, getPersistedDashboardWidget(returnedDashboardWidget));

        insertedDashboardWidget = returnedDashboardWidget;
    }

    @Test
    @Transactional
    void createDashboardWidgetWithExistingId() throws Exception {
        // Create the DashboardWidget with an existing ID
        dashboardWidget.setId(1L);
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDashboardIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setDashboardId(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWidgetTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setWidgetType(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setTitle(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPositionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setPosition(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeXIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setSizeX(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSizeYIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dashboardWidget.setSizeY(null);

        // Create the DashboardWidget, which fails.
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        restDashboardWidgetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDashboardWidgets() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        // Get all the dashboardWidgetList
        restDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dashboardWidget.getId().intValue())))
            .andExpect(jsonPath("$.[*].dashboardId").value(hasItem(DEFAULT_DASHBOARD_ID.intValue())))
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
    void getDashboardWidget() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        // Get the dashboardWidget
        restDashboardWidgetMockMvc
            .perform(get(ENTITY_API_URL_ID, dashboardWidget.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dashboardWidget.getId().intValue()))
            .andExpect(jsonPath("$.dashboardId").value(DEFAULT_DASHBOARD_ID.intValue()))
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
    void getNonExistingDashboardWidget() throws Exception {
        // Get the dashboardWidget
        restDashboardWidgetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDashboardWidget() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboardWidget
        DashboardWidget updatedDashboardWidget = dashboardWidgetRepository.findById(dashboardWidget.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDashboardWidget are not directly saved in db
        em.detach(updatedDashboardWidget);
        updatedDashboardWidget
            .dashboardId(UPDATED_DASHBOARD_ID)
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(updatedDashboardWidget);

        restDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardWidgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardWidgetDTO))
            )
            .andExpect(status().isOk());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDashboardWidgetToMatchAllProperties(updatedDashboardWidget);
    }

    @Test
    @Transactional
    void putNonExistingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dashboardWidgetDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDashboardWidgetWithPatch() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboardWidget using partial update
        DashboardWidget partialUpdatedDashboardWidget = new DashboardWidget();
        partialUpdatedDashboardWidget.setId(dashboardWidget.getId());

        partialUpdatedDashboardWidget
            .dashboardId(UPDATED_DASHBOARD_ID)
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);

        restDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboardWidget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDashboardWidget))
            )
            .andExpect(status().isOk());

        // Validate the DashboardWidget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDashboardWidgetUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDashboardWidget, dashboardWidget),
            getPersistedDashboardWidget(dashboardWidget)
        );
    }

    @Test
    @Transactional
    void fullUpdateDashboardWidgetWithPatch() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dashboardWidget using partial update
        DashboardWidget partialUpdatedDashboardWidget = new DashboardWidget();
        partialUpdatedDashboardWidget.setId(dashboardWidget.getId());

        partialUpdatedDashboardWidget
            .dashboardId(UPDATED_DASHBOARD_ID)
            .widgetType(UPDATED_WIDGET_TYPE)
            .title(UPDATED_TITLE)
            .configuration(UPDATED_CONFIGURATION)
            .dataSource(UPDATED_DATA_SOURCE)
            .position(UPDATED_POSITION)
            .sizeX(UPDATED_SIZE_X)
            .sizeY(UPDATED_SIZE_Y);

        restDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDashboardWidget.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDashboardWidget))
            )
            .andExpect(status().isOk());

        // Validate the DashboardWidget in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDashboardWidgetUpdatableFieldsEquals(
            partialUpdatedDashboardWidget,
            getPersistedDashboardWidget(partialUpdatedDashboardWidget)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dashboardWidgetDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dashboardWidgetDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDashboardWidget() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dashboardWidget.setId(longCount.incrementAndGet());

        // Create the DashboardWidget
        DashboardWidgetDTO dashboardWidgetDTO = dashboardWidgetMapper.toDto(dashboardWidget);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDashboardWidgetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dashboardWidgetDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DashboardWidget in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDashboardWidget() throws Exception {
        // Initialize the database
        insertedDashboardWidget = dashboardWidgetRepository.saveAndFlush(dashboardWidget);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dashboardWidget
        restDashboardWidgetMockMvc
            .perform(delete(ENTITY_API_URL_ID, dashboardWidget.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dashboardWidgetRepository.count();
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

    protected DashboardWidget getPersistedDashboardWidget(DashboardWidget dashboardWidget) {
        return dashboardWidgetRepository.findById(dashboardWidget.getId()).orElseThrow();
    }

    protected void assertPersistedDashboardWidgetToMatchAllProperties(DashboardWidget expectedDashboardWidget) {
        assertDashboardWidgetAllPropertiesEquals(expectedDashboardWidget, getPersistedDashboardWidget(expectedDashboardWidget));
    }

    protected void assertPersistedDashboardWidgetToMatchUpdatableProperties(DashboardWidget expectedDashboardWidget) {
        assertDashboardWidgetAllUpdatablePropertiesEquals(expectedDashboardWidget, getPersistedDashboardWidget(expectedDashboardWidget));
    }
}
