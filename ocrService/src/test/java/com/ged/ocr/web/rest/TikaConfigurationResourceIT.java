package com.ged.ocr.web.rest;

import static com.ged.ocr.domain.TikaConfigurationAsserts.*;
import static com.ged.ocr.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ocr.IntegrationTest;
import com.ged.ocr.domain.TikaConfiguration;
import com.ged.ocr.repository.TikaConfigurationRepository;
import com.ged.ocr.service.dto.TikaConfigurationDTO;
import com.ged.ocr.service.mapper.TikaConfigurationMapper;
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
 * Integration tests for the {@link TikaConfigurationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TikaConfigurationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ENDPOINT = "AAAAAAAAAA";
    private static final String UPDATED_ENDPOINT = "BBBBBBBBBB";

    private static final String DEFAULT_API_KEY = "AAAAAAAAAA";
    private static final String UPDATED_API_KEY = "BBBBBBBBBB";

    private static final Integer DEFAULT_TIMEOUT = 1;
    private static final Integer UPDATED_TIMEOUT = 2;

    private static final Long DEFAULT_MAX_FILE_SIZE = 1L;
    private static final Long UPDATED_MAX_FILE_SIZE = 2L;

    private static final String DEFAULT_SUPPORTED_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_SUPPORTED_LANGUAGES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_ENABLE_OCR = false;
    private static final Boolean UPDATED_ENABLE_OCR = true;

    private static final String DEFAULT_OCR_ENGINE = "AAAAAAAAAA";
    private static final String UPDATED_OCR_ENGINE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DEFAULT = false;
    private static final Boolean UPDATED_IS_DEFAULT = true;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tika-configurations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TikaConfigurationRepository tikaConfigurationRepository;

    @Autowired
    private TikaConfigurationMapper tikaConfigurationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTikaConfigurationMockMvc;

    private TikaConfiguration tikaConfiguration;

    private TikaConfiguration insertedTikaConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TikaConfiguration createEntity() {
        return new TikaConfiguration()
            .name(DEFAULT_NAME)
            .endpoint(DEFAULT_ENDPOINT)
            .apiKey(DEFAULT_API_KEY)
            .timeout(DEFAULT_TIMEOUT)
            .maxFileSize(DEFAULT_MAX_FILE_SIZE)
            .supportedLanguages(DEFAULT_SUPPORTED_LANGUAGES)
            .enableOcr(DEFAULT_ENABLE_OCR)
            .ocrEngine(DEFAULT_OCR_ENGINE)
            .isDefault(DEFAULT_IS_DEFAULT)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TikaConfiguration createUpdatedEntity() {
        return new TikaConfiguration()
            .name(UPDATED_NAME)
            .endpoint(UPDATED_ENDPOINT)
            .apiKey(UPDATED_API_KEY)
            .timeout(UPDATED_TIMEOUT)
            .maxFileSize(UPDATED_MAX_FILE_SIZE)
            .supportedLanguages(UPDATED_SUPPORTED_LANGUAGES)
            .enableOcr(UPDATED_ENABLE_OCR)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        tikaConfiguration = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedTikaConfiguration != null) {
            tikaConfigurationRepository.delete(insertedTikaConfiguration);
            insertedTikaConfiguration = null;
        }
    }

    @Test
    @Transactional
    void createTikaConfiguration() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);
        var returnedTikaConfigurationDTO = om.readValue(
            restTikaConfigurationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TikaConfigurationDTO.class
        );

        // Validate the TikaConfiguration in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTikaConfiguration = tikaConfigurationMapper.toEntity(returnedTikaConfigurationDTO);
        assertTikaConfigurationUpdatableFieldsEquals(returnedTikaConfiguration, getPersistedTikaConfiguration(returnedTikaConfiguration));

        insertedTikaConfiguration = returnedTikaConfiguration;
    }

    @Test
    @Transactional
    void createTikaConfigurationWithExistingId() throws Exception {
        // Create the TikaConfiguration with an existing ID
        tikaConfiguration.setId(1L);
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setName(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndpointIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setEndpoint(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEnableOcrIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setEnableOcr(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsDefaultIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setIsDefault(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setIsActive(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tikaConfiguration.setCreatedDate(null);

        // Create the TikaConfiguration, which fails.
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTikaConfigurations() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        // Get all the tikaConfigurationList
        restTikaConfigurationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tikaConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].endpoint").value(hasItem(DEFAULT_ENDPOINT)))
            .andExpect(jsonPath("$.[*].apiKey").value(hasItem(DEFAULT_API_KEY)))
            .andExpect(jsonPath("$.[*].timeout").value(hasItem(DEFAULT_TIMEOUT)))
            .andExpect(jsonPath("$.[*].maxFileSize").value(hasItem(DEFAULT_MAX_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].supportedLanguages").value(hasItem(DEFAULT_SUPPORTED_LANGUAGES)))
            .andExpect(jsonPath("$.[*].enableOcr").value(hasItem(DEFAULT_ENABLE_OCR)))
            .andExpect(jsonPath("$.[*].ocrEngine").value(hasItem(DEFAULT_OCR_ENGINE)))
            .andExpect(jsonPath("$.[*].isDefault").value(hasItem(DEFAULT_IS_DEFAULT)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getTikaConfiguration() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        // Get the tikaConfiguration
        restTikaConfigurationMockMvc
            .perform(get(ENTITY_API_URL_ID, tikaConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tikaConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.endpoint").value(DEFAULT_ENDPOINT))
            .andExpect(jsonPath("$.apiKey").value(DEFAULT_API_KEY))
            .andExpect(jsonPath("$.timeout").value(DEFAULT_TIMEOUT))
            .andExpect(jsonPath("$.maxFileSize").value(DEFAULT_MAX_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.supportedLanguages").value(DEFAULT_SUPPORTED_LANGUAGES))
            .andExpect(jsonPath("$.enableOcr").value(DEFAULT_ENABLE_OCR))
            .andExpect(jsonPath("$.ocrEngine").value(DEFAULT_OCR_ENGINE))
            .andExpect(jsonPath("$.isDefault").value(DEFAULT_IS_DEFAULT))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTikaConfiguration() throws Exception {
        // Get the tikaConfiguration
        restTikaConfigurationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTikaConfiguration() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tikaConfiguration
        TikaConfiguration updatedTikaConfiguration = tikaConfigurationRepository.findById(tikaConfiguration.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTikaConfiguration are not directly saved in db
        em.detach(updatedTikaConfiguration);
        updatedTikaConfiguration
            .name(UPDATED_NAME)
            .endpoint(UPDATED_ENDPOINT)
            .apiKey(UPDATED_API_KEY)
            .timeout(UPDATED_TIMEOUT)
            .maxFileSize(UPDATED_MAX_FILE_SIZE)
            .supportedLanguages(UPDATED_SUPPORTED_LANGUAGES)
            .enableOcr(UPDATED_ENABLE_OCR)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE);
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(updatedTikaConfiguration);

        restTikaConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tikaConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tikaConfigurationDTO))
            )
            .andExpect(status().isOk());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTikaConfigurationToMatchAllProperties(updatedTikaConfiguration);
    }

    @Test
    @Transactional
    void putNonExistingTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tikaConfigurationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tikaConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tikaConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTikaConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tikaConfiguration using partial update
        TikaConfiguration partialUpdatedTikaConfiguration = new TikaConfiguration();
        partialUpdatedTikaConfiguration.setId(tikaConfiguration.getId());

        partialUpdatedTikaConfiguration
            .name(UPDATED_NAME)
            .endpoint(UPDATED_ENDPOINT)
            .apiKey(UPDATED_API_KEY)
            .timeout(UPDATED_TIMEOUT)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE);

        restTikaConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTikaConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTikaConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the TikaConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTikaConfigurationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTikaConfiguration, tikaConfiguration),
            getPersistedTikaConfiguration(tikaConfiguration)
        );
    }

    @Test
    @Transactional
    void fullUpdateTikaConfigurationWithPatch() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tikaConfiguration using partial update
        TikaConfiguration partialUpdatedTikaConfiguration = new TikaConfiguration();
        partialUpdatedTikaConfiguration.setId(tikaConfiguration.getId());

        partialUpdatedTikaConfiguration
            .name(UPDATED_NAME)
            .endpoint(UPDATED_ENDPOINT)
            .apiKey(UPDATED_API_KEY)
            .timeout(UPDATED_TIMEOUT)
            .maxFileSize(UPDATED_MAX_FILE_SIZE)
            .supportedLanguages(UPDATED_SUPPORTED_LANGUAGES)
            .enableOcr(UPDATED_ENABLE_OCR)
            .ocrEngine(UPDATED_OCR_ENGINE)
            .isDefault(UPDATED_IS_DEFAULT)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE);

        restTikaConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTikaConfiguration.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTikaConfiguration))
            )
            .andExpect(status().isOk());

        // Validate the TikaConfiguration in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTikaConfigurationUpdatableFieldsEquals(
            partialUpdatedTikaConfiguration,
            getPersistedTikaConfiguration(partialUpdatedTikaConfiguration)
        );
    }

    @Test
    @Transactional
    void patchNonExistingTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tikaConfigurationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tikaConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tikaConfigurationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTikaConfiguration() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tikaConfiguration.setId(longCount.incrementAndGet());

        // Create the TikaConfiguration
        TikaConfigurationDTO tikaConfigurationDTO = tikaConfigurationMapper.toDto(tikaConfiguration);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTikaConfigurationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tikaConfigurationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TikaConfiguration in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTikaConfiguration() throws Exception {
        // Initialize the database
        insertedTikaConfiguration = tikaConfigurationRepository.saveAndFlush(tikaConfiguration);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tikaConfiguration
        restTikaConfigurationMockMvc
            .perform(delete(ENTITY_API_URL_ID, tikaConfiguration.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tikaConfigurationRepository.count();
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

    protected TikaConfiguration getPersistedTikaConfiguration(TikaConfiguration tikaConfiguration) {
        return tikaConfigurationRepository.findById(tikaConfiguration.getId()).orElseThrow();
    }

    protected void assertPersistedTikaConfigurationToMatchAllProperties(TikaConfiguration expectedTikaConfiguration) {
        assertTikaConfigurationAllPropertiesEquals(expectedTikaConfiguration, getPersistedTikaConfiguration(expectedTikaConfiguration));
    }

    protected void assertPersistedTikaConfigurationToMatchUpdatableProperties(TikaConfiguration expectedTikaConfiguration) {
        assertTikaConfigurationAllUpdatablePropertiesEquals(
            expectedTikaConfiguration,
            getPersistedTikaConfiguration(expectedTikaConfiguration)
        );
    }
}
