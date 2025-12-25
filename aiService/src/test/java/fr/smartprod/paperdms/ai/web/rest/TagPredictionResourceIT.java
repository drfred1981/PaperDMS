package fr.smartprod.paperdms.ai.web.rest;

import static fr.smartprod.paperdms.ai.domain.TagPredictionAsserts.*;
import static fr.smartprod.paperdms.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.ai.IntegrationTest;
import fr.smartprod.paperdms.ai.domain.AutoTagJob;
import fr.smartprod.paperdms.ai.domain.TagPrediction;
import fr.smartprod.paperdms.ai.repository.TagPredictionRepository;
import fr.smartprod.paperdms.ai.service.dto.TagPredictionDTO;
import fr.smartprod.paperdms.ai.service.mapper.TagPredictionMapper;
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
 * Integration tests for the {@link TagPredictionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TagPredictionResourceIT {

    private static final String DEFAULT_TAG_NAME = "AAAAAAAAAA";
    private static final String UPDATED_TAG_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;

    private static final String DEFAULT_REASON = "AAAAAAAAAA";
    private static final String UPDATED_REASON = "BBBBBBBBBB";

    private static final String DEFAULT_MODEL_VERSION = "AAAAAAAAAA";
    private static final String UPDATED_MODEL_VERSION = "BBBBBBBBBB";

    private static final String DEFAULT_PREDICTION_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PREDICTION_S_3_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACCEPTED = false;
    private static final Boolean UPDATED_IS_ACCEPTED = true;

    private static final String DEFAULT_ACCEPTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACCEPTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACCEPTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACCEPTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PREDICTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PREDICTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/tag-predictions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private TagPredictionRepository tagPredictionRepository;

    @Autowired
    private TagPredictionMapper tagPredictionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTagPredictionMockMvc;

    private TagPrediction tagPrediction;

    private TagPrediction insertedTagPrediction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagPrediction createEntity(EntityManager em) {
        TagPrediction tagPrediction = new TagPrediction()
            .tagName(DEFAULT_TAG_NAME)
            .confidence(DEFAULT_CONFIDENCE)
            .reason(DEFAULT_REASON)
            .modelVersion(DEFAULT_MODEL_VERSION)
            .predictionS3Key(DEFAULT_PREDICTION_S_3_KEY)
            .isAccepted(DEFAULT_IS_ACCEPTED)
            .acceptedBy(DEFAULT_ACCEPTED_BY)
            .acceptedDate(DEFAULT_ACCEPTED_DATE)
            .predictionDate(DEFAULT_PREDICTION_DATE);
        // Add required entity
        AutoTagJob autoTagJob;
        if (TestUtil.findAll(em, AutoTagJob.class).isEmpty()) {
            autoTagJob = AutoTagJobResourceIT.createEntity();
            em.persist(autoTagJob);
            em.flush();
        } else {
            autoTagJob = TestUtil.findAll(em, AutoTagJob.class).get(0);
        }
        tagPrediction.setJob(autoTagJob);
        return tagPrediction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TagPrediction createUpdatedEntity(EntityManager em) {
        TagPrediction updatedTagPrediction = new TagPrediction()
            .tagName(UPDATED_TAG_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
        // Add required entity
        AutoTagJob autoTagJob;
        if (TestUtil.findAll(em, AutoTagJob.class).isEmpty()) {
            autoTagJob = AutoTagJobResourceIT.createUpdatedEntity();
            em.persist(autoTagJob);
            em.flush();
        } else {
            autoTagJob = TestUtil.findAll(em, AutoTagJob.class).get(0);
        }
        updatedTagPrediction.setJob(autoTagJob);
        return updatedTagPrediction;
    }

    @BeforeEach
    void initTest() {
        tagPrediction = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedTagPrediction != null) {
            tagPredictionRepository.delete(insertedTagPrediction);
            insertedTagPrediction = null;
        }
    }

    @Test
    @Transactional
    void createTagPrediction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);
        var returnedTagPredictionDTO = om.readValue(
            restTagPredictionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            TagPredictionDTO.class
        );

        // Validate the TagPrediction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedTagPrediction = tagPredictionMapper.toEntity(returnedTagPredictionDTO);
        assertTagPredictionUpdatableFieldsEquals(returnedTagPrediction, getPersistedTagPrediction(returnedTagPrediction));

        insertedTagPrediction = returnedTagPrediction;
    }

    @Test
    @Transactional
    void createTagPredictionWithExistingId() throws Exception {
        // Create the TagPrediction with an existing ID
        tagPrediction.setId(1L);
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTagNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagPrediction.setTagName(null);

        // Create the TagPrediction, which fails.
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        restTagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConfidenceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagPrediction.setConfidence(null);

        // Create the TagPrediction, which fails.
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        restTagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPredictionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        tagPrediction.setPredictionDate(null);

        // Create the TagPrediction, which fails.
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        restTagPredictionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTagPredictions() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        // Get all the tagPredictionList
        restTagPredictionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(tagPrediction.getId().intValue())))
            .andExpect(jsonPath("$.[*].tagName").value(hasItem(DEFAULT_TAG_NAME)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].reason").value(hasItem(DEFAULT_REASON)))
            .andExpect(jsonPath("$.[*].modelVersion").value(hasItem(DEFAULT_MODEL_VERSION)))
            .andExpect(jsonPath("$.[*].predictionS3Key").value(hasItem(DEFAULT_PREDICTION_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isAccepted").value(hasItem(DEFAULT_IS_ACCEPTED)))
            .andExpect(jsonPath("$.[*].acceptedBy").value(hasItem(DEFAULT_ACCEPTED_BY)))
            .andExpect(jsonPath("$.[*].acceptedDate").value(hasItem(DEFAULT_ACCEPTED_DATE.toString())))
            .andExpect(jsonPath("$.[*].predictionDate").value(hasItem(DEFAULT_PREDICTION_DATE.toString())));
    }

    @Test
    @Transactional
    void getTagPrediction() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        // Get the tagPrediction
        restTagPredictionMockMvc
            .perform(get(ENTITY_API_URL_ID, tagPrediction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(tagPrediction.getId().intValue()))
            .andExpect(jsonPath("$.tagName").value(DEFAULT_TAG_NAME))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.reason").value(DEFAULT_REASON))
            .andExpect(jsonPath("$.modelVersion").value(DEFAULT_MODEL_VERSION))
            .andExpect(jsonPath("$.predictionS3Key").value(DEFAULT_PREDICTION_S_3_KEY))
            .andExpect(jsonPath("$.isAccepted").value(DEFAULT_IS_ACCEPTED))
            .andExpect(jsonPath("$.acceptedBy").value(DEFAULT_ACCEPTED_BY))
            .andExpect(jsonPath("$.acceptedDate").value(DEFAULT_ACCEPTED_DATE.toString()))
            .andExpect(jsonPath("$.predictionDate").value(DEFAULT_PREDICTION_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingTagPrediction() throws Exception {
        // Get the tagPrediction
        restTagPredictionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingTagPrediction() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tagPrediction
        TagPrediction updatedTagPrediction = tagPredictionRepository.findById(tagPrediction.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedTagPrediction are not directly saved in db
        em.detach(updatedTagPrediction);
        updatedTagPrediction
            .tagName(UPDATED_TAG_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(updatedTagPrediction);

        restTagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagPredictionDTO))
            )
            .andExpect(status().isOk());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedTagPredictionToMatchAllProperties(updatedTagPrediction);
    }

    @Test
    @Transactional
    void putNonExistingTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, tagPredictionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(tagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTagPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tagPrediction using partial update
        TagPrediction partialUpdatedTagPrediction = new TagPrediction();
        partialUpdatedTagPrediction.setId(tagPrediction.getId());

        partialUpdatedTagPrediction
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restTagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagPrediction))
            )
            .andExpect(status().isOk());

        // Validate the TagPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagPredictionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedTagPrediction, tagPrediction),
            getPersistedTagPrediction(tagPrediction)
        );
    }

    @Test
    @Transactional
    void fullUpdateTagPredictionWithPatch() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the tagPrediction using partial update
        TagPrediction partialUpdatedTagPrediction = new TagPrediction();
        partialUpdatedTagPrediction.setId(tagPrediction.getId());

        partialUpdatedTagPrediction
            .tagName(UPDATED_TAG_NAME)
            .confidence(UPDATED_CONFIDENCE)
            .reason(UPDATED_REASON)
            .modelVersion(UPDATED_MODEL_VERSION)
            .predictionS3Key(UPDATED_PREDICTION_S_3_KEY)
            .isAccepted(UPDATED_IS_ACCEPTED)
            .acceptedBy(UPDATED_ACCEPTED_BY)
            .acceptedDate(UPDATED_ACCEPTED_DATE)
            .predictionDate(UPDATED_PREDICTION_DATE);

        restTagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTagPrediction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedTagPrediction))
            )
            .andExpect(status().isOk());

        // Validate the TagPrediction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertTagPredictionUpdatableFieldsEquals(partialUpdatedTagPrediction, getPersistedTagPrediction(partialUpdatedTagPrediction));
    }

    @Test
    @Transactional
    void patchNonExistingTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, tagPredictionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(tagPredictionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTagPrediction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        tagPrediction.setId(longCount.incrementAndGet());

        // Create the TagPrediction
        TagPredictionDTO tagPredictionDTO = tagPredictionMapper.toDto(tagPrediction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTagPredictionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(tagPredictionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TagPrediction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTagPrediction() throws Exception {
        // Initialize the database
        insertedTagPrediction = tagPredictionRepository.saveAndFlush(tagPrediction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the tagPrediction
        restTagPredictionMockMvc
            .perform(delete(ENTITY_API_URL_ID, tagPrediction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return tagPredictionRepository.count();
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

    protected TagPrediction getPersistedTagPrediction(TagPrediction tagPrediction) {
        return tagPredictionRepository.findById(tagPrediction.getId()).orElseThrow();
    }

    protected void assertPersistedTagPredictionToMatchAllProperties(TagPrediction expectedTagPrediction) {
        assertTagPredictionAllPropertiesEquals(expectedTagPrediction, getPersistedTagPrediction(expectedTagPrediction));
    }

    protected void assertPersistedTagPredictionToMatchUpdatableProperties(TagPrediction expectedTagPrediction) {
        assertTagPredictionAllUpdatablePropertiesEquals(expectedTagPrediction, getPersistedTagPrediction(expectedTagPrediction));
    }
}
