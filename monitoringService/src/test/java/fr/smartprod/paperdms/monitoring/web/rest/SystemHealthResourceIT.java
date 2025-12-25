package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.SystemHealthAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.SystemHealth;
import fr.smartprod.paperdms.monitoring.domain.enumeration.HealthStatus;
import fr.smartprod.paperdms.monitoring.repository.SystemHealthRepository;
import fr.smartprod.paperdms.monitoring.service.dto.SystemHealthDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.SystemHealthMapper;
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
 * Integration tests for the {@link SystemHealthResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SystemHealthResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final HealthStatus DEFAULT_STATUS = HealthStatus.HEALTHY;
    private static final HealthStatus UPDATED_STATUS = HealthStatus.DEGRADED;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Long DEFAULT_UPTIME = 1L;
    private static final Long UPDATED_UPTIME = 2L;

    private static final Double DEFAULT_CPU_USAGE = 1D;
    private static final Double UPDATED_CPU_USAGE = 2D;

    private static final Double DEFAULT_MEMORY_USAGE = 1D;
    private static final Double UPDATED_MEMORY_USAGE = 2D;

    private static final Double DEFAULT_ERROR_RATE = 1D;
    private static final Double UPDATED_ERROR_RATE = 2D;

    private static final Instant DEFAULT_LAST_CHECK = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_CHECK = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/system-healths";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SystemHealthRepository systemHealthRepository;

    @Autowired
    private SystemHealthMapper systemHealthMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSystemHealthMockMvc;

    private SystemHealth systemHealth;

    private SystemHealth insertedSystemHealth;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemHealth createEntity() {
        return new SystemHealth()
            .serviceName(DEFAULT_SERVICE_NAME)
            .status(DEFAULT_STATUS)
            .version(DEFAULT_VERSION)
            .uptime(DEFAULT_UPTIME)
            .cpuUsage(DEFAULT_CPU_USAGE)
            .memoryUsage(DEFAULT_MEMORY_USAGE)
            .errorRate(DEFAULT_ERROR_RATE)
            .lastCheck(DEFAULT_LAST_CHECK);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SystemHealth createUpdatedEntity() {
        return new SystemHealth()
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);
    }

    @BeforeEach
    void initTest() {
        systemHealth = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSystemHealth != null) {
            systemHealthRepository.delete(insertedSystemHealth);
            insertedSystemHealth = null;
        }
    }

    @Test
    @Transactional
    void createSystemHealth() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);
        var returnedSystemHealthDTO = om.readValue(
            restSystemHealthMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SystemHealthDTO.class
        );

        // Validate the SystemHealth in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSystemHealth = systemHealthMapper.toEntity(returnedSystemHealthDTO);
        assertSystemHealthUpdatableFieldsEquals(returnedSystemHealth, getPersistedSystemHealth(returnedSystemHealth));

        insertedSystemHealth = returnedSystemHealth;
    }

    @Test
    @Transactional
    void createSystemHealthWithExistingId() throws Exception {
        // Create the SystemHealth with an existing ID
        systemHealth.setId(1L);
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemHealth.setServiceName(null);

        // Create the SystemHealth, which fails.
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        restSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemHealth.setStatus(null);

        // Create the SystemHealth, which fails.
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        restSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastCheckIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        systemHealth.setLastCheck(null);

        // Create the SystemHealth, which fails.
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        restSystemHealthMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSystemHealths() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        // Get all the systemHealthList
        restSystemHealthMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(systemHealth.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].uptime").value(hasItem(DEFAULT_UPTIME.intValue())))
            .andExpect(jsonPath("$.[*].cpuUsage").value(hasItem(DEFAULT_CPU_USAGE)))
            .andExpect(jsonPath("$.[*].memoryUsage").value(hasItem(DEFAULT_MEMORY_USAGE)))
            .andExpect(jsonPath("$.[*].errorRate").value(hasItem(DEFAULT_ERROR_RATE)))
            .andExpect(jsonPath("$.[*].lastCheck").value(hasItem(DEFAULT_LAST_CHECK.toString())));
    }

    @Test
    @Transactional
    void getSystemHealth() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        // Get the systemHealth
        restSystemHealthMockMvc
            .perform(get(ENTITY_API_URL_ID, systemHealth.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(systemHealth.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.uptime").value(DEFAULT_UPTIME.intValue()))
            .andExpect(jsonPath("$.cpuUsage").value(DEFAULT_CPU_USAGE))
            .andExpect(jsonPath("$.memoryUsage").value(DEFAULT_MEMORY_USAGE))
            .andExpect(jsonPath("$.errorRate").value(DEFAULT_ERROR_RATE))
            .andExpect(jsonPath("$.lastCheck").value(DEFAULT_LAST_CHECK.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSystemHealth() throws Exception {
        // Get the systemHealth
        restSystemHealthMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSystemHealth() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemHealth
        SystemHealth updatedSystemHealth = systemHealthRepository.findById(systemHealth.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSystemHealth are not directly saved in db
        em.detach(updatedSystemHealth);
        updatedSystemHealth
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(updatedSystemHealth);

        restSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemHealthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemHealthDTO))
            )
            .andExpect(status().isOk());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSystemHealthToMatchAllProperties(updatedSystemHealth);
    }

    @Test
    @Transactional
    void putNonExistingSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, systemHealthDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(systemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSystemHealthWithPatch() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemHealth using partial update
        SystemHealth partialUpdatedSystemHealth = new SystemHealth();
        partialUpdatedSystemHealth.setId(systemHealth.getId());

        partialUpdatedSystemHealth
            .status(UPDATED_STATUS)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);

        restSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemHealth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemHealth))
            )
            .andExpect(status().isOk());

        // Validate the SystemHealth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemHealthUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSystemHealth, systemHealth),
            getPersistedSystemHealth(systemHealth)
        );
    }

    @Test
    @Transactional
    void fullUpdateSystemHealthWithPatch() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the systemHealth using partial update
        SystemHealth partialUpdatedSystemHealth = new SystemHealth();
        partialUpdatedSystemHealth.setId(systemHealth.getId());

        partialUpdatedSystemHealth
            .serviceName(UPDATED_SERVICE_NAME)
            .status(UPDATED_STATUS)
            .version(UPDATED_VERSION)
            .uptime(UPDATED_UPTIME)
            .cpuUsage(UPDATED_CPU_USAGE)
            .memoryUsage(UPDATED_MEMORY_USAGE)
            .errorRate(UPDATED_ERROR_RATE)
            .lastCheck(UPDATED_LAST_CHECK);

        restSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSystemHealth.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSystemHealth))
            )
            .andExpect(status().isOk());

        // Validate the SystemHealth in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSystemHealthUpdatableFieldsEquals(partialUpdatedSystemHealth, getPersistedSystemHealth(partialUpdatedSystemHealth));
    }

    @Test
    @Transactional
    void patchNonExistingSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, systemHealthDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(systemHealthDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSystemHealth() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        systemHealth.setId(longCount.incrementAndGet());

        // Create the SystemHealth
        SystemHealthDTO systemHealthDTO = systemHealthMapper.toDto(systemHealth);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSystemHealthMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(systemHealthDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SystemHealth in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSystemHealth() throws Exception {
        // Initialize the database
        insertedSystemHealth = systemHealthRepository.saveAndFlush(systemHealth);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the systemHealth
        restSystemHealthMockMvc
            .perform(delete(ENTITY_API_URL_ID, systemHealth.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return systemHealthRepository.count();
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

    protected SystemHealth getPersistedSystemHealth(SystemHealth systemHealth) {
        return systemHealthRepository.findById(systemHealth.getId()).orElseThrow();
    }

    protected void assertPersistedSystemHealthToMatchAllProperties(SystemHealth expectedSystemHealth) {
        assertSystemHealthAllPropertiesEquals(expectedSystemHealth, getPersistedSystemHealth(expectedSystemHealth));
    }

    protected void assertPersistedSystemHealthToMatchUpdatableProperties(SystemHealth expectedSystemHealth) {
        assertSystemHealthAllUpdatablePropertiesEquals(expectedSystemHealth, getPersistedSystemHealth(expectedSystemHealth));
    }
}
