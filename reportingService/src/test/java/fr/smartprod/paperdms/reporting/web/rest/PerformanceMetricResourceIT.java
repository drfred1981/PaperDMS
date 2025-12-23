package fr.smartprod.paperdms.reporting.web.rest;

import static fr.smartprod.paperdms.reporting.domain.PerformanceMetricAsserts.*;
import static fr.smartprod.paperdms.reporting.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.reporting.IntegrationTest;
import fr.smartprod.paperdms.reporting.domain.PerformanceMetric;
import fr.smartprod.paperdms.reporting.domain.enumeration.MetricType;
import fr.smartprod.paperdms.reporting.repository.PerformanceMetricRepository;
import fr.smartprod.paperdms.reporting.service.dto.PerformanceMetricDTO;
import fr.smartprod.paperdms.reporting.service.mapper.PerformanceMetricMapper;
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
 * Integration tests for the {@link PerformanceMetricResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PerformanceMetricResourceIT {

    private static final String DEFAULT_METRIC_NAME = "AAAAAAAAAA";
    private static final String UPDATED_METRIC_NAME = "BBBBBBBBBB";

    private static final MetricType DEFAULT_METRIC_TYPE = MetricType.UPLOAD_SPEED;
    private static final MetricType UPDATED_METRIC_TYPE = MetricType.OCR_DURATION;

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_SERVICE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SERVICE_NAME = "BBBBBBBBBB";

    private static final Instant DEFAULT_TIMESTAMP = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TIMESTAMP = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/performance-metrics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PerformanceMetricRepository performanceMetricRepository;

    @Autowired
    private PerformanceMetricMapper performanceMetricMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPerformanceMetricMockMvc;

    private PerformanceMetric performanceMetric;

    private PerformanceMetric insertedPerformanceMetric;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerformanceMetric createEntity() {
        return new PerformanceMetric()
            .metricName(DEFAULT_METRIC_NAME)
            .metricType(DEFAULT_METRIC_TYPE)
            .value(DEFAULT_VALUE)
            .unit(DEFAULT_UNIT)
            .serviceName(DEFAULT_SERVICE_NAME)
            .timestamp(DEFAULT_TIMESTAMP);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PerformanceMetric createUpdatedEntity() {
        return new PerformanceMetric()
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);
    }

    @BeforeEach
    void initTest() {
        performanceMetric = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPerformanceMetric != null) {
            performanceMetricRepository.delete(insertedPerformanceMetric);
            insertedPerformanceMetric = null;
        }
    }

    @Test
    @Transactional
    void createPerformanceMetric() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);
        var returnedPerformanceMetricDTO = om.readValue(
            restPerformanceMetricMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PerformanceMetricDTO.class
        );

        // Validate the PerformanceMetric in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPerformanceMetric = performanceMetricMapper.toEntity(returnedPerformanceMetricDTO);
        assertPerformanceMetricUpdatableFieldsEquals(returnedPerformanceMetric, getPersistedPerformanceMetric(returnedPerformanceMetric));

        insertedPerformanceMetric = returnedPerformanceMetric;
    }

    @Test
    @Transactional
    void createPerformanceMetricWithExistingId() throws Exception {
        // Create the PerformanceMetric with an existing ID
        performanceMetric.setId(1L);
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMetricNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        performanceMetric.setMetricName(null);

        // Create the PerformanceMetric, which fails.
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMetricTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        performanceMetric.setMetricType(null);

        // Create the PerformanceMetric, which fails.
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkValueIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        performanceMetric.setValue(null);

        // Create the PerformanceMetric, which fails.
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        performanceMetric.setUnit(null);

        // Create the PerformanceMetric, which fails.
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTimestampIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        performanceMetric.setTimestamp(null);

        // Create the PerformanceMetric, which fails.
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        restPerformanceMetricMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPerformanceMetrics() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        // Get all the performanceMetricList
        restPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(performanceMetric.getId().intValue())))
            .andExpect(jsonPath("$.[*].metricName").value(hasItem(DEFAULT_METRIC_NAME)))
            .andExpect(jsonPath("$.[*].metricType").value(hasItem(DEFAULT_METRIC_TYPE.toString())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].serviceName").value(hasItem(DEFAULT_SERVICE_NAME)))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(DEFAULT_TIMESTAMP.toString())));
    }

    @Test
    @Transactional
    void getPerformanceMetric() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        // Get the performanceMetric
        restPerformanceMetricMockMvc
            .perform(get(ENTITY_API_URL_ID, performanceMetric.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(performanceMetric.getId().intValue()))
            .andExpect(jsonPath("$.metricName").value(DEFAULT_METRIC_NAME))
            .andExpect(jsonPath("$.metricType").value(DEFAULT_METRIC_TYPE.toString()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.serviceName").value(DEFAULT_SERVICE_NAME))
            .andExpect(jsonPath("$.timestamp").value(DEFAULT_TIMESTAMP.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPerformanceMetric() throws Exception {
        // Get the performanceMetric
        restPerformanceMetricMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPerformanceMetric() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the performanceMetric
        PerformanceMetric updatedPerformanceMetric = performanceMetricRepository.findById(performanceMetric.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPerformanceMetric are not directly saved in db
        em.detach(updatedPerformanceMetric);
        updatedPerformanceMetric
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(updatedPerformanceMetric);

        restPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, performanceMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(performanceMetricDTO))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPerformanceMetricToMatchAllProperties(updatedPerformanceMetric);
    }

    @Test
    @Transactional
    void putNonExistingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, performanceMetricDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(performanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(performanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePerformanceMetricWithPatch() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the performanceMetric using partial update
        PerformanceMetric partialUpdatedPerformanceMetric = new PerformanceMetric();
        partialUpdatedPerformanceMetric.setId(performanceMetric.getId());

        partialUpdatedPerformanceMetric.value(UPDATED_VALUE).timestamp(UPDATED_TIMESTAMP);

        restPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerformanceMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerformanceMetric))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerformanceMetricUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPerformanceMetric, performanceMetric),
            getPersistedPerformanceMetric(performanceMetric)
        );
    }

    @Test
    @Transactional
    void fullUpdatePerformanceMetricWithPatch() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the performanceMetric using partial update
        PerformanceMetric partialUpdatedPerformanceMetric = new PerformanceMetric();
        partialUpdatedPerformanceMetric.setId(performanceMetric.getId());

        partialUpdatedPerformanceMetric
            .metricName(UPDATED_METRIC_NAME)
            .metricType(UPDATED_METRIC_TYPE)
            .value(UPDATED_VALUE)
            .unit(UPDATED_UNIT)
            .serviceName(UPDATED_SERVICE_NAME)
            .timestamp(UPDATED_TIMESTAMP);

        restPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPerformanceMetric.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPerformanceMetric))
            )
            .andExpect(status().isOk());

        // Validate the PerformanceMetric in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPerformanceMetricUpdatableFieldsEquals(
            partialUpdatedPerformanceMetric,
            getPersistedPerformanceMetric(partialUpdatedPerformanceMetric)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, performanceMetricDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(performanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(performanceMetricDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPerformanceMetric() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        performanceMetric.setId(longCount.incrementAndGet());

        // Create the PerformanceMetric
        PerformanceMetricDTO performanceMetricDTO = performanceMetricMapper.toDto(performanceMetric);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPerformanceMetricMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(performanceMetricDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PerformanceMetric in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePerformanceMetric() throws Exception {
        // Initialize the database
        insertedPerformanceMetric = performanceMetricRepository.saveAndFlush(performanceMetric);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the performanceMetric
        restPerformanceMetricMockMvc
            .perform(delete(ENTITY_API_URL_ID, performanceMetric.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return performanceMetricRepository.count();
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

    protected PerformanceMetric getPersistedPerformanceMetric(PerformanceMetric performanceMetric) {
        return performanceMetricRepository.findById(performanceMetric.getId()).orElseThrow();
    }

    protected void assertPersistedPerformanceMetricToMatchAllProperties(PerformanceMetric expectedPerformanceMetric) {
        assertPerformanceMetricAllPropertiesEquals(expectedPerformanceMetric, getPersistedPerformanceMetric(expectedPerformanceMetric));
    }

    protected void assertPersistedPerformanceMetricToMatchUpdatableProperties(PerformanceMetric expectedPerformanceMetric) {
        assertPerformanceMetricAllUpdatablePropertiesEquals(
            expectedPerformanceMetric,
            getPersistedPerformanceMetric(expectedPerformanceMetric)
        );
    }
}
