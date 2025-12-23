package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.LanguageDetectionAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.LanguageDetection;
import fr.smartprod.paperdms.ai.domain.enumeration.LanguageDetectionMethod;
import fr.smartprod.paperdms.ai.repository.LanguageDetectionRepository;
import fr.smartprod.paperdms.ai.service.dto.LanguageDetectionDTO;
import fr.smartprod.paperdms.ai.service.mapper.LanguageDetectionMapper;
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
 * Integration tests for the {@link LanguageDetectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class LanguageDetectionResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_DETECTED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_DETECTED_LANGUAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;

    private static final LanguageDetectionMethod DEFAULT_DETECTION_METHOD = LanguageDetectionMethod.TIKA;
    private static final LanguageDetectionMethod UPDATED_DETECTION_METHOD = LanguageDetectionMethod.LANGDETECT;

    private static final String DEFAULT_ALTERNATIVE_LANGUAGES = "AAAAAAAAAA";
    private static final String UPDATED_ALTERNATIVE_LANGUAGES = "BBBBBBBBBB";

    private static final String DEFAULT_TEXT_SAMPLE = "AAAAAAAAAA";
    private static final String UPDATED_TEXT_SAMPLE = "BBBBBBBBBB";

    private static final String DEFAULT_RESULT_CACHE_KEY = "AAAAAAAAAA";
    private static final String UPDATED_RESULT_CACHE_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CACHED = false;
    private static final Boolean UPDATED_IS_CACHED = true;

    private static final Instant DEFAULT_DETECTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DETECTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/language-detections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LanguageDetectionRepository languageDetectionRepository;

    @Autowired
    private LanguageDetectionMapper languageDetectionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLanguageDetectionMockMvc;

    private LanguageDetection languageDetection;

    private LanguageDetection insertedLanguageDetection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanguageDetection createEntity() {
        return new LanguageDetection()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .detectedLanguage(DEFAULT_DETECTED_LANGUAGE)
            .confidence(DEFAULT_CONFIDENCE)
            .detectionMethod(DEFAULT_DETECTION_METHOD)
            .alternativeLanguages(DEFAULT_ALTERNATIVE_LANGUAGES)
            .textSample(DEFAULT_TEXT_SAMPLE)
            .resultCacheKey(DEFAULT_RESULT_CACHE_KEY)
            .isCached(DEFAULT_IS_CACHED)
            .detectedDate(DEFAULT_DETECTED_DATE)
            .modelVersion(DEFAULT_MODEL_VERSION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LanguageDetection createUpdatedEntity() {
        return new LanguageDetection()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);
    }

    @BeforeEach
    void initTest() {
        languageDetection = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedLanguageDetection != null) {
            languageDetectionRepository.delete(insertedLanguageDetection);
            insertedLanguageDetection = null;
        }
    }

    @Test
    @Transactional
    void createLanguageDetection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);
        var returnedLanguageDetectionDTO = om.readValue(
            restLanguageDetectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LanguageDetectionDTO.class
        );

        // Validate the LanguageDetection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedLanguageDetection = languageDetectionMapper.toEntity(returnedLanguageDetectionDTO);
        assertLanguageDetectionUpdatableFieldsEquals(returnedLanguageDetection, getPersistedLanguageDetection(returnedLanguageDetection));

        insertedLanguageDetection = returnedLanguageDetection;
    }

    @Test
    @Transactional
    void createLanguageDetectionWithExistingId() throws Exception {
        // Create the LanguageDetection with an existing ID
        languageDetection.setId(1L);
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setDocumentId(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setDocumentSha256(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDetectedLanguageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setDetectedLanguage(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setConfidence(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsCachedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setIsCached(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDetectedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        languageDetection.setDetectedDate(null);

        // Create the LanguageDetection, which fails.
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        restLanguageDetectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLanguageDetections() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        // Get all the languageDetectionList
        restLanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(languageDetection.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].detectionMethod").value(hasItem(DEFAULT_DETECTION_METHOD.toString())))
            .andExpect(jsonPath("$.[*].alternativeLanguages").value(hasItem(DEFAULT_ALTERNATIVE_LANGUAGES)))
            .andExpect(jsonPath("$.[*].textSample").value(hasItem(DEFAULT_TEXT_SAMPLE)))
            .andExpect(jsonPath("$.[*].resultCacheKey").value(hasItem(DEFAULT_RESULT_CACHE_KEY)))
            .andExpect(jsonPath("$.[*].isCached").value(hasItem(DEFAULT_IS_CACHED)))
            .andExpect(jsonPath("$.[*].detectedDate").value(hasItem(DEFAULT_DETECTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)));
    }

    @Test
    @Transactional
    void getLanguageDetection() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        // Get the languageDetection
        restLanguageDetectionMockMvc
            .perform(get(ENTITY_API_URL_ID, languageDetection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(languageDetection.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.detectedLanguage").value(DEFAULT_DETECTED_LANGUAGE))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.detectionMethod").value(DEFAULT_DETECTION_METHOD.toString()))
            .andExpect(jsonPath("$.alternativeLanguages").value(DEFAULT_ALTERNATIVE_LANGUAGES))
            .andExpect(jsonPath("$.textSample").value(DEFAULT_TEXT_SAMPLE))
            .andExpect(jsonPath("$.resultCacheKey").value(DEFAULT_RESULT_CACHE_KEY))
            .andExpect(jsonPath("$.isCached").value(DEFAULT_IS_CACHED))
            .andExpect(jsonPath("$.detectedDate").value(DEFAULT_DETECTED_DATE.toString()))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION));
    }

    @Test
    @Transactional
    void getNonExistingLanguageDetection() throws Exception {
        // Get the languageDetection
        restLanguageDetectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLanguageDetection() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the languageDetection
        LanguageDetection updatedLanguageDetection = languageDetectionRepository.findById(languageDetection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLanguageDetection are not directly saved in db
        em.detach(updatedLanguageDetection);
        updatedLanguageDetection
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(updatedLanguageDetection);

        restLanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDetectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDetectionDTO))
            )
            .andExpect(status().isOk());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLanguageDetectionToMatchAllProperties(updatedLanguageDetection);
    }

    @Test
    @Transactional
    void putNonExistingLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, languageDetectionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(languageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLanguageDetectionWithPatch() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the languageDetection using partial update
        LanguageDetection partialUpdatedLanguageDetection = new LanguageDetection();
        partialUpdatedLanguageDetection.setId(languageDetection.getId());

        partialUpdatedLanguageDetection
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .modelVersion(UPDATED_MODEL_VERSION);

        restLanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguageDetection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguageDetection))
            )
            .andExpect(status().isOk());

        // Validate the LanguageDetection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageDetectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLanguageDetection, languageDetection),
            getPersistedLanguageDetection(languageDetection)
        );
    }

    @Test
    @Transactional
    void fullUpdateLanguageDetectionWithPatch() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the languageDetection using partial update
        LanguageDetection partialUpdatedLanguageDetection = new LanguageDetection();
        partialUpdatedLanguageDetection.setId(languageDetection.getId());

        partialUpdatedLanguageDetection
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .confidence(UPDATED_CONFIDENCE)
            .detectionMethod(UPDATED_DETECTION_METHOD)
            .alternativeLanguages(UPDATED_ALTERNATIVE_LANGUAGES)
            .textSample(UPDATED_TEXT_SAMPLE)
            .resultCacheKey(UPDATED_RESULT_CACHE_KEY)
            .isCached(UPDATED_IS_CACHED)
            .detectedDate(UPDATED_DETECTED_DATE)
            .modelVersion(UPDATED_MODEL_VERSION);

        restLanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLanguageDetection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLanguageDetection))
            )
            .andExpect(status().isOk());

        // Validate the LanguageDetection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLanguageDetectionUpdatableFieldsEquals(
            partialUpdatedLanguageDetection,
            getPersistedLanguageDetection(partialUpdatedLanguageDetection)
        );
    }

    @Test
    @Transactional
    void patchNonExistingLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, languageDetectionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(languageDetectionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLanguageDetection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        languageDetection.setId(longCount.incrementAndGet());

        // Create the LanguageDetection
        LanguageDetectionDTO languageDetectionDTO = languageDetectionMapper.toDto(languageDetection);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLanguageDetectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(languageDetectionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LanguageDetection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLanguageDetection() throws Exception {
        // Initialize the database
        insertedLanguageDetection = languageDetectionRepository.saveAndFlush(languageDetection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the languageDetection
        restLanguageDetectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, languageDetection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return languageDetectionRepository.count();
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

    protected LanguageDetection getPersistedLanguageDetection(LanguageDetection languageDetection) {
        return languageDetectionRepository.findById(languageDetection.getId()).orElseThrow();
    }

    protected void assertPersistedLanguageDetectionToMatchAllProperties(LanguageDetection expectedLanguageDetection) {
        assertLanguageDetectionAllPropertiesEquals(expectedLanguageDetection, getPersistedLanguageDetection(expectedLanguageDetection));
    }

    protected void assertPersistedLanguageDetectionToMatchUpdatableProperties(LanguageDetection expectedLanguageDetection) {
        assertLanguageDetectionAllUpdatablePropertiesEquals(
            expectedLanguageDetection,
            getPersistedLanguageDetection(expectedLanguageDetection)
        );
    }
}
