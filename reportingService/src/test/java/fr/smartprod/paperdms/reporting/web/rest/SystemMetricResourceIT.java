package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.SystemMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.SystemMetric;
import fr.smartprod.paperdms.reporting.repository.SystemMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.SystemMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.SystemMetricMapper;
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
 * Integration tests for the {@link SystemMetricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemMetricResourceIT {

    private static final String DEFAULT_METRIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CPU_USAGE = 1D;
    private static final Double UPDATED_CPU_USAGE = 2D;

    private static final Double DEFAULT_MEMORY_USAGE = 1D;
    private static final Double UPDATED_MEMORY_USAGE = 2D;

    private static final Double DEFAULT_DISK_USAGE = 1D;
    private static final Double UPDATED_DISK_USAGE = 2D;

    private static final Long DEFAULT_NETWORK_IN = 1L;
    private static final Long UPDATED_NETWORK_IN = 2L;

    private static final Long DEFAULT_NETWORK_OUT = 1L;
    private static final Long UPDATED_NETWORK_OUT = 2L;

    private static final Integer DEFAULT_ACTIVE_CONNECTIONS = 1;
    private static final Integer UPDATED_ACTIVE_CONNECTIONS = 2;

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemMetricRepository systemMetricRepository;

    @Autowired
    private SystemMetricMapper systemMetricMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemMetricMockMvc;

    private SystemMetric systemMetric;

    private SystemMetric insertedSystemMetric;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemMetric createEntity() {
        return new SystemMetric()
            .metricName(DEFAULT_METRIC_NAME)
            .cpuUsage(DEFAULT_CPU_USAGE)
            .memoryUsage(DEFAULT_MEMORY_USAGE)
            .diskUsage(DEFAULT_DISK_USAGE)
            .networkIn(DEFAULT_NETWORK_IN)
            .networkOut(DEFAULT_NETWORK_OUT)
            .activeConnections(DEFAULT_ACTIVE_CONNECTIONS)
            .timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemMetric createUpdatedEntity() {
        return new SystemMetric()
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        systemMetric = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemMetric != null) {
            systemMetricRepository.delete(insertedSystemMetric);
            insertedSystemMetric = null;
        }
    }

    @Test
    @Transactional
    void createSystemMetric() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);
        var returnedSystemMetricDTO = om.readValue(
            restSystemMetricMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemMetricDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemMetricDTO.class
        );

        // Validate the SystemMetric in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemMetric = systemMetricMapper.toEntity(returnedSystemMetricDTO);
        assertSystemMetricUpdatableFieldsEquals(returnedSystemMetric, getPersistedSystemMetric(returnedSystemMetric));

        insertedSystemMetric = returnedSystemMetric;
    }

    @Test
    @Transactional
    void createSystemMetricWithExistingId() throws Exception {
        // Create the SystemMetric with an existing ID
        systemMetric.setId(1L);
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemMetric.setMetricName(null);

        // Create the SystemMetric, which fails.
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        restSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemMetric.setTimestamp(null);

        // Create the SystemMetric, which fails.
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        restSystemMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemMetrics() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        // Get all the systemMetricList
        restSystemMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].diskUsage").value(hasItem(DEFAULT_DISK_USAGE)))
            .andExpect(jsonPath("$.[*].networkIn").value(hasItem(DEFAULT_NETWORK_IN.intValue())))
            .andExpect(jsonPath("$.[*].networkOut").value(hasItem(DEFAULT_NETWORK_OUT.intValue())))
            .andExpect(jsonPath("$.[*].activeConnections").value(hasItem(DEFAULT_ACTIVE_CONNECTIONS)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getSystemMetric() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        // Get the systemMetric
        restSystemMetricMockMvc
            .perform(get(ENTITY_API_URL_ID, systemMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemMetric.getId().intValue()))
            .andExpect(jsonPath("$.metricName").value(DEFAULT_METRIC_NAME))
            .andExpect(jsonPath("$.cpuUsage").value(DEFAULT_CPU_USAGE))
            .andExpect(jsonPath("$.memoryUsage").value(DEFAULT_MEMORY_USAGE))
            .andExpect(jsonPath("$.diskUsage").value(DEFAULT_DISK_USAGE))
            .andExpect(jsonPath("$.networkIn").value(DEFAULT_NETWORK_IN.intValue()))
            .andExpect(jsonPath("$.networkOut").value(DEFAULT_NETWORK_OUT.intValue()))
            .andExpect(jsonPath("$.activeConnections").value(DEFAULT_ACTIVE_CONNECTIONS))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemMetric() throws Exception {
        // Get the systemMetric
        restSystemMetricMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemMetric() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemMetric
        SystemMetric updatedSystemMetric = systemMetricRepository.findById(systemMetric.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemMetric are not directly saved in db
        em.detach(updatedSystemMetric);
        updatedSystemMetric
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(updatedSystemMetric);

        restSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemMetricDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemMetricToMatchAllProperties(updatedSystemMetric);
    }

    @Test
    @Transactional
    void putNonExistingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemMetricDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemMetricWithPatch() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemMetric using partial update
        SystemMetric partialUpdatedSystemMetric = new SystemMetric();
        partialUpdatedSystemMetric.setId(systemMetric.getId());

        partialUpdatedSystemMetric.networkOut(UPDATED_NETWORK_OUT).timestamp(UPDATED_TIMESTAMP);

        restSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemMetric))
            )
            .andExpect(status().isOk());

        // Validate the SystemMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemMetricUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemMetric, systemMetric),
            getPersistedSystemMetric(systemMetric)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemMetricWithPatch() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemMetric using partial update
        SystemMetric partialUpdatedSystemMetric = new SystemMetric();
        partialUpdatedSystemMetric.setId(systemMetric.getId());

        partialUpdatedSystemMetric
            .metricName(UPDATED_METRIC_NAME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .diskUsage(UPDATED_DISK_USAGE)
            .networkIn(UPDATED_NETWORK_IN)
            .networkOut(UPDATED_NETWORK_OUT)
            .activeConnections(UPDATED_ACTIVE_CONNECTIONS)
            .timestamp(UPDATED_TIMESTAMP);

        restSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemMetric))
            )
            .andExpect(status().isOk());

        // Validate the SystemMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemMetricUpdatableFieldsEquals(partialUpdatedSystemMetric, getPersistedSystemMetric(partialUpdatedSystemMetric));
    }

    @Test
    @Transactional
    void patchNonExistingSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemMetricDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemMetric.setId(longCount.incrementAndGet());

        // Create the SystemMetric
        SystemMetricDTO systemMetricDTO = systemMetricMapper.toDto(systemMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemMetricMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemMetricDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemMetric() throws Exception {
        // Initialize the database
        insertedSystemMetric = systemMetricRepository.saveAndFlush(systemMetric);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemMetric
        restSystemMetricMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemMetric.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemMetricRepository.count();
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

    protected SystemMetric getPersistedSystemMetric(SystemMetric systemMetric) {
        return systemMetricRepository.findById(systemMetric.getId()).orElseThrow();
    }

    protected void assertPersistedSystemMetricToMatchAllProperties(SystemMetric expectedSystemMetric) {
        assertSystemMetricAllPropertiesEquals(expectedSystemMetric, getPersistedSystemMetric(expectedSystemMetric));
    }

    protected void assertPersistedSystemMetricToMatchUpdatableProperties(SystemMetric expectedSystemMetric) {
        assertSystemMetricAllUpdatablePropertiesEquals(expectedSystemMetric, getPersistedSystemMetric(expectedSystemMetric));
    }
}
