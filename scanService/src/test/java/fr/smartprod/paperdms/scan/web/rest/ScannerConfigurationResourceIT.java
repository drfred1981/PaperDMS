package fr.smartprod.paperdms.scan.web.rest;

import static fr.smartprod.paperdms.scan.domain.ScannerConfigurationAsserts.*;
import static fr.smartprod.paperdms.scan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.scan.IntegrationTest;
import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import fr.smartprod.paperdms.scan.domain.enumeration.ColorMode;
import fr.smartprod.paperdms.scan.domain.enumeration.ScanFormat;
import fr.smartprod.paperdms.scan.domain.enumeration.ScannerType;
import fr.smartprod.paperdms.scan.repository.ScannerConfigurationRepository;
import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScannerConfigurationMapper;
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
 * Integration tests for the {@link ScannerConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScannerConfigurationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ScannerType DEFAULT_SCANNER_TYPE = ScannerType.NETWORK;
    private static final ScannerType UPDATED_SCANNER_TYPE = ScannerType.LOCAL;

    private static final String DEFAULT_IP_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_IP_ADDRESS = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    private static final String DEFAULT_PROTOCOL = "AAAAAAAAAA";
    private static final String UPDATED_PROTOCOL = "BBBBBBBBBB";

    private static final String DEFAULT_MANUFACTURER = "AAAAAAAAAA";
    private static final String UPDATED_MANUFACTURER = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL = "AAAAAAAAAA";
    private static final String UPDATED_MODEL = "BBBBBBBBBB";

    private static final ColorMode DEFAULT_DEFAULT_COLOR_MODE = ColorMode.BLACK_WHITE;
    private static final ColorMode UPDATED_DEFAULT_COLOR_MODE = ColorMode.GRAYSCALE;

    private static final Integer DEFAULT_DEFAULT_RESOLUTION = 1;
    private static final Integer UPDATED_DEFAULT_RESOLUTION = 2;

    private static final ScanFormat DEFAULT_DEFAULT_FORMAT = ScanFormat.PDF;
    private static final ScanFormat UPDATED_DEFAULT_FORMAT = ScanFormat.TIFF;

    private static final String DEFAULT_CAPABILITIES = "AAAAAAAAAA";
    private static final String UPDATED_CAPABILITIES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/scanner-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScannerConfigurationRepository scannerConfigurationRepository;

    @Autowired
    private ScannerConfigurationMapper scannerConfigurationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScannerConfigurationMockMvc;

    private ScannerConfiguration scannerConfiguration;

    private ScannerConfiguration insertedScannerConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScannerConfiguration createEntity() {
        return new ScannerConfiguration()
            .name(DEFAULT_NAME)
            .scannerType(DEFAULT_SCANNER_TYPE)
            .ipAddress(DEFAULT_IP_ADDRESS)
            .port(DEFAULT_PORT)
            .protocol(DEFAULT_PROTOCOL)
            .manufacturer(DEFAULT_MANUFACTURER)
            .model(DEFAULT_MODEL)
            .defaultColorMode(DEFAULT_DEFAULT_COLOR_MODE)
            .defaultResolution(DEFAULT_DEFAULT_RESOLUTION)
            .defaultFormat(DEFAULT_DEFAULT_FORMAT)
            .capabilities(DEFAULT_CAPABILITIES)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScannerConfiguration createUpdatedEntity() {
        return new ScannerConfiguration()
            .name(UPDATED_NAME)
            .scannerType(UPDATED_SCANNER_TYPE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .port(UPDATED_PORT)
            .protocol(UPDATED_PROTOCOL)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .defaultColorMode(UPDATED_DEFAULT_COLOR_MODE)
            .defaultResolution(UPDATED_DEFAULT_RESOLUTION)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .capabilities(UPDATED_CAPABILITIES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        scannerConfiguration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedScannerConfiguration != null) {
            scannerConfigurationRepository.delete(insertedScannerConfiguration);
            insertedScannerConfiguration = null;
        }
    }

    @Test
    @Transactional
    void createScannerConfiguration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);
        var returnedScannerConfigurationDTO = om.readValue(
            restScannerConfigurationMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScannerConfigurationDTO.class
        );

        // Validate the ScannerConfiguration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScannerConfiguration = scannerConfigurationMapper.toEntity(returnedScannerConfigurationDTO);
        assertScannerConfigurationUpdatableFieldsEquals(
            returnedScannerConfiguration,
            getPersistedScannerConfiguration(returnedScannerConfiguration)
        );

        insertedScannerConfiguration = returnedScannerConfiguration;
    }

    @Test
    @Transactional
    void createScannerConfigurationWithExistingId() throws Exception {
        // Create the ScannerConfiguration with an existing ID
        scannerConfiguration.setId(1L);
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScannerConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannerConfiguration.setName(null);

        // Create the ScannerConfiguration, which fails.
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        restScannerConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScannerTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannerConfiguration.setScannerType(null);

        // Create the ScannerConfiguration, which fails.
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        restScannerConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannerConfiguration.setIsActive(null);

        // Create the ScannerConfiguration, which fails.
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        restScannerConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannerConfiguration.setCreatedDate(null);

        // Create the ScannerConfiguration, which fails.
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        restScannerConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScannerConfigurations() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        // Get all the scannerConfigurationList
        restScannerConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scannerConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].scannerType").value(hasItem(DEFAULT_SCANNER_TYPE.toString())))
            .andExpect(jsonPath("$.[*].ipAddress").value(hasItem(DEFAULT_IP_ADDRESS)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)))
            .andExpect(jsonPath("$.[*].protocol").value(hasItem(DEFAULT_PROTOCOL)))
            .andExpect(jsonPath("$.[*].manufacturer").value(hasItem(DEFAULT_MANUFACTURER)))
            .andExpect(jsonPath("$.[*].model").value(hasItem(DEFAULT_MODEL)))
            .andExpect(jsonPath("$.[*].defaultColorMode").value(hasItem(DEFAULT_DEFAULT_COLOR_MODE.toString())))
            .andExpect(jsonPath("$.[*].defaultResolution").value(hasItem(DEFAULT_DEFAULT_RESOLUTION)))
            .andExpect(jsonPath("$.[*].defaultFormat").value(hasItem(DEFAULT_DEFAULT_FORMAT.toString())))
            .andExpect(jsonPath("$.[*].capabilities").value(hasItem(DEFAULT_CAPABILITIES)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getScannerConfiguration() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        // Get the scannerConfiguration
        restScannerConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, scannerConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scannerConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.scannerType").value(DEFAULT_SCANNER_TYPE.toString()))
            .andExpect(jsonPath("$.ipAddress").value(DEFAULT_IP_ADDRESS))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT))
            .andExpect(jsonPath("$.protocol").value(DEFAULT_PROTOCOL))
            .andExpect(jsonPath("$.manufacturer").value(DEFAULT_MANUFACTURER))
            .andExpect(jsonPath("$.model").value(DEFAULT_MODEL))
            .andExpect(jsonPath("$.defaultColorMode").value(DEFAULT_DEFAULT_COLOR_MODE.toString()))
            .andExpect(jsonPath("$.defaultResolution").value(DEFAULT_DEFAULT_RESOLUTION))
            .andExpect(jsonPath("$.defaultFormat").value(DEFAULT_DEFAULT_FORMAT.toString()))
            .andExpect(jsonPath("$.capabilities").value(DEFAULT_CAPABILITIES))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingScannerConfiguration() throws Exception {
        // Get the scannerConfiguration
        restScannerConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScannerConfiguration() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannerConfiguration
        ScannerConfiguration updatedScannerConfiguration = scannerConfigurationRepository
            .findById(scannerConfiguration.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedScannerConfiguration are not directly saved in db
        em.detach(updatedScannerConfiguration);
        updatedScannerConfiguration
            .name(UPDATED_NAME)
            .scannerType(UPDATED_SCANNER_TYPE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .port(UPDATED_PORT)
            .protocol(UPDATED_PROTOCOL)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .defaultColorMode(UPDATED_DEFAULT_COLOR_MODE)
            .defaultResolution(UPDATED_DEFAULT_RESOLUTION)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .capabilities(UPDATED_CAPABILITIES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(updatedScannerConfiguration);

        restScannerConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scannerConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScannerConfigurationToMatchAllProperties(updatedScannerConfiguration);
    }

    @Test
    @Transactional
    void putNonExistingScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scannerConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannerConfigurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScannerConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannerConfiguration using partial update
        ScannerConfiguration partialUpdatedScannerConfiguration = new ScannerConfiguration();
        partialUpdatedScannerConfiguration.setId(scannerConfiguration.getId());

        partialUpdatedScannerConfiguration
            .name(UPDATED_NAME)
            .manufacturer(UPDATED_MANUFACTURER)
            .defaultColorMode(UPDATED_DEFAULT_COLOR_MODE)
            .defaultResolution(UPDATED_DEFAULT_RESOLUTION)
            .capabilities(UPDATED_CAPABILITIES)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restScannerConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScannerConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScannerConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the ScannerConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScannerConfigurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScannerConfiguration, scannerConfiguration),
            getPersistedScannerConfiguration(scannerConfiguration)
        );
    }

    @Test
    @Transactional
    void fullUpdateScannerConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannerConfiguration using partial update
        ScannerConfiguration partialUpdatedScannerConfiguration = new ScannerConfiguration();
        partialUpdatedScannerConfiguration.setId(scannerConfiguration.getId());

        partialUpdatedScannerConfiguration
            .name(UPDATED_NAME)
            .scannerType(UPDATED_SCANNER_TYPE)
            .ipAddress(UPDATED_IP_ADDRESS)
            .port(UPDATED_PORT)
            .protocol(UPDATED_PROTOCOL)
            .manufacturer(UPDATED_MANUFACTURER)
            .model(UPDATED_MODEL)
            .defaultColorMode(UPDATED_DEFAULT_COLOR_MODE)
            .defaultResolution(UPDATED_DEFAULT_RESOLUTION)
            .defaultFormat(UPDATED_DEFAULT_FORMAT)
            .capabilities(UPDATED_CAPABILITIES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restScannerConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScannerConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScannerConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the ScannerConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScannerConfigurationUpdatableFieldsEquals(
            partialUpdatedScannerConfiguration,
            getPersistedScannerConfiguration(partialUpdatedScannerConfiguration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scannerConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScannerConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannerConfiguration.setId(longCount.incrementAndGet());

        // Create the ScannerConfiguration
        ScannerConfigurationDTO scannerConfigurationDTO = scannerConfigurationMapper.toDto(scannerConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannerConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scannerConfigurationDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScannerConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScannerConfiguration() throws Exception {
        // Initialize the database
        insertedScannerConfiguration = scannerConfigurationRepository.saveAndFlush(scannerConfiguration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scannerConfiguration
        restScannerConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, scannerConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scannerConfigurationRepository.count();
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

    protected ScannerConfiguration getPersistedScannerConfiguration(ScannerConfiguration scannerConfiguration) {
        return scannerConfigurationRepository.findById(scannerConfiguration.getId()).orElseThrow();
    }

    protected void assertPersistedScannerConfigurationToMatchAllProperties(ScannerConfiguration expectedScannerConfiguration) {
        assertScannerConfigurationAllPropertiesEquals(
            expectedScannerConfiguration,
            getPersistedScannerConfiguration(expectedScannerConfiguration)
        );
    }

    protected void assertPersistedScannerConfigurationToMatchUpdatableProperties(ScannerConfiguration expectedScannerConfiguration) {
        assertScannerConfigurationAllUpdatablePropertiesEquals(
            expectedScannerConfiguration,
            getPersistedScannerConfiguration(expectedScannerConfiguration)
        );
    }
}
