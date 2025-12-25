package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.ServiceStatusAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.ServiceStatus;
import fr.smartprod.paperdms.monitoring.domain.enumeration.ServiceStatusType;
import fr.smartprod.paperdms.monitoring.repository.ServiceStatusRepository;
import fr.smartprod.paperdms.monitoring.service.dto.ServiceStatusDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.ServiceStatusMapper;
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
 * Integration tests for the {@link ServiceStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ServiceStatusResourceIT {

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_TYPE = "BBBBBBBBBB";

    private static final ServiceStatusType DEFAULT_STATUS = ServiceStatusType.RUNNING;
    private static final ServiceStatusType UPDATED_STATUS = ServiceStatusType.STARTING;

    private static final String DEFAULT_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final String DEFAULT_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_VERSION = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_PING = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_PING = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_HEALTHY = false;
    private static final Boolean UPDATED_IS_HEALTHY = true;

    private static final String ENTITY_API_URL = "/api/service-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ServiceStatusRepository serviceStatusRepository;

    @Autowired
    private ServiceStatusMapper serviceStatusMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restServiceStatusMockMvc;

    private ServiceStatus serviceStatus;

    private ServiceStatus insertedServiceStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceStatus createEntity() {
        return new ServiceStatus()
            .serviceName(DEFAULT_SERVICE_NAME)
            .serviceType(DEFAULT_SERVICE_TYPE)
            .status(DEFAULT_STATUS)
            .endpoint(DEFAULT_ENDPOINT)
            .port(DEFAULT_PORT)
            .version(DEFAULT_VERSION)
            .lastPing(DEFAULT_LAST_PING)
            .isHealthy(DEFAULT_IS_HEALTHY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ServiceStatus createUpdatedEntity() {
        return new ServiceStatus()
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);
    }

    @BeforeEach
    void initTest() {
        serviceStatus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedServiceStatus != null) {
            serviceStatusRepository.delete(insertedServiceStatus);
            insertedServiceStatus = null;
        }
    }

    @Test
    @Transactional
    void createServiceStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);
        var returnedServiceStatusDTO = om.readValue(
            restServiceStatusMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ServiceStatusDTO.class
        );

        // Validate the ServiceStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedServiceStatus = serviceStatusMapper.toEntity(returnedServiceStatusDTO);
        assertServiceStatusUpdatableFieldsEquals(returnedServiceStatus, getPersistedServiceStatus(returnedServiceStatus));

        insertedServiceStatus = returnedServiceStatus;
    }

    @Test
    @Transactional
    void createServiceStatusWithExistingId() throws Exception {
        // Create the ServiceStatus with an existing ID
        serviceStatus.setId(1L);
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkServiceNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceStatus.setServiceName(null);

        // Create the ServiceStatus, which fails.
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        restServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceStatus.setStatus(null);

        // Create the ServiceStatus, which fails.
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        restServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsHealthyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        serviceStatus.setIsHealthy(null);

        // Create the ServiceStatus, which fails.
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        restServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllServiceStatuses() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        // Get all the serviceStatusList
        restServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(serviceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].lastPing").value(hasItem(DEFAULT_LAST_PING.toString())))
            .andExpect(jsonPath("$.[*].isHealthy").value(hasItem(DEFAULT_IS_HEALTHY)));
    }

    @Test
    @Transactional
    void getServiceStatus() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        // Get the serviceStatus
        restServiceStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, serviceStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(serviceStatus.getId().intValue()))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.lastPing").value(DEFAULT_LAST_PING.toString()))
            .andExpect(jsonPath("$.isHealthy").value(DEFAULT_IS_HEALTHY));
    }

    @Test
    @Transactional
    void getNonExistingServiceStatus() throws Exception {
        // Get the serviceStatus
        restServiceStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingServiceStatus() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceStatus
        ServiceStatus updatedServiceStatus = serviceStatusRepository.findById(serviceStatus.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedServiceStatus are not directly saved in db
        em.detach(updatedServiceStatus);
        updatedServiceStatus
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(updatedServiceStatus);

        restServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedServiceStatusToMatchAllProperties(updatedServiceStatus);
    }

    @Test
    @Transactional
    void putNonExistingServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, serviceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(serviceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceStatus using partial update
        ServiceStatus partialUpdatedServiceStatus = new ServiceStatus();
        partialUpdatedServiceStatus.setId(serviceStatus.getId());

        partialUpdatedServiceStatus.status(UPDATED_STATUS).port(UPDATED_PORT).isHealthy(UPDATED_IS_HEALTHY);

        restServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the ServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedServiceStatus, serviceStatus),
            getPersistedServiceStatus(serviceStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the serviceStatus using partial update
        ServiceStatus partialUpdatedServiceStatus = new ServiceStatus();
        partialUpdatedServiceStatus.setId(serviceStatus.getId());

        partialUpdatedServiceStatus
            .serviceName(UPDATED_SERVICE_NAME)
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .endpoint(UPDATED_ENDPOINT)
            .port(UPDATED_PORT)
            .version(UPDATED_VERSION)
            .lastPing(UPDATED_LAST_PING)
            .isHealthy(UPDATED_IS_HEALTHY);

        restServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the ServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertServiceStatusUpdatableFieldsEquals(partialUpdatedServiceStatus, getPersistedServiceStatus(partialUpdatedServiceStatus));
    }

    @Test
    @Transactional
    void patchNonExistingServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, serviceStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(serviceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        serviceStatus.setId(longCount.incrementAndGet());

        // Create the ServiceStatus
        ServiceStatusDTO serviceStatusDTO = serviceStatusMapper.toDto(serviceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restServiceStatusMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(serviceStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteServiceStatus() throws Exception {
        // Initialize the database
        insertedServiceStatus = serviceStatusRepository.saveAndFlush(serviceStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the serviceStatus
        restServiceStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, serviceStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return serviceStatusRepository.count();
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

    protected ServiceStatus getPersistedServiceStatus(ServiceStatus serviceStatus) {
        return serviceStatusRepository.findById(serviceStatus.getId()).orElseThrow();
    }

    protected void assertPersistedServiceStatusToMatchAllProperties(ServiceStatus expectedServiceStatus) {
        assertServiceStatusAllPropertiesEquals(expectedServiceStatus, getPersistedServiceStatus(expectedServiceStatus));
    }

    protected void assertPersistedServiceStatusToMatchUpdatableProperties(ServiceStatus expectedServiceStatus) {
        assertServiceStatusAllUpdatablePropertiesEquals(expectedServiceStatus, getPersistedServiceStatus(expectedServiceStatus));
    }
}
