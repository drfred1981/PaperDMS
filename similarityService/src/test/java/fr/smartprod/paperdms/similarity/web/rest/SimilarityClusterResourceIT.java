package fr.smartprod.paperdms.similarity.web.rest;

import static fr.smartprod.paperdms.similarity.domain.SimilarityClusterAsserts.*;
import static fr.smartprod.paperdms.similarity.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.similarity.IntegrationTest;
import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.domain.enumeration.SimilarityAlgorithm;
import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import fr.smartprod.paperdms.similarity.service.mapper.SimilarityClusterMapper;
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
 * Integration tests for the {@link SimilarityClusterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SimilarityClusterResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final SimilarityAlgorithm DEFAULT_ALGORITHM = SimilarityAlgorithm.COSINE;
    private static final SimilarityAlgorithm UPDATED_ALGORITHM = SimilarityAlgorithm.JACCARD;

    private static final String DEFAULT_CENTROID = "AAAAAAAAAA";
    private static final String UPDATED_CENTROID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DOCUMENT_COUNT = 1;
    private static final Integer UPDATED_DOCUMENT_COUNT = 2;

    private static final Double DEFAULT_AVG_SIMILARITY = 0D;
    private static final Double UPDATED_AVG_SIMILARITY = 1D;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/similarity-clusters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SimilarityClusterRepository similarityClusterRepository;

    @Autowired
    private SimilarityClusterMapper similarityClusterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSimilarityClusterMockMvc;

    private SimilarityCluster similarityCluster;

    private SimilarityCluster insertedSimilarityCluster;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityCluster createEntity() {
        return new SimilarityCluster()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .algorithm(DEFAULT_ALGORITHM)
            .centroid(DEFAULT_CENTROID)
            .documentCount(DEFAULT_DOCUMENT_COUNT)
            .avgSimilarity(DEFAULT_AVG_SIMILARITY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SimilarityCluster createUpdatedEntity() {
        return new SimilarityCluster()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        similarityCluster = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedSimilarityCluster != null) {
            similarityClusterRepository.delete(insertedSimilarityCluster);
            insertedSimilarityCluster = null;
        }
    }

    @Test
    @Transactional
    void createSimilarityCluster() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);
        var returnedSimilarityClusterDTO = om.readValue(
            restSimilarityClusterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SimilarityClusterDTO.class
        );

        // Validate the SimilarityCluster in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSimilarityCluster = similarityClusterMapper.toEntity(returnedSimilarityClusterDTO);
        assertSimilarityClusterUpdatableFieldsEquals(returnedSimilarityCluster, getPersistedSimilarityCluster(returnedSimilarityCluster));

        insertedSimilarityCluster = returnedSimilarityCluster;
    }

    @Test
    @Transactional
    void createSimilarityClusterWithExistingId() throws Exception {
        // Create the SimilarityCluster with an existing ID
        similarityCluster.setId(1L);
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSimilarityClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        similarityCluster.setCreatedDate(null);

        // Create the SimilarityCluster, which fails.
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        restSimilarityClusterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllSimilarityClusters() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get all the similarityClusterList
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(similarityCluster.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].algorithm").value(hasItem(DEFAULT_ALGORITHM.toString())))
            .andExpect(jsonPath("$.[*].centroid").value(hasItem(DEFAULT_CENTROID)))
            .andExpect(jsonPath("$.[*].documentCount").value(hasItem(DEFAULT_DOCUMENT_COUNT)))
            .andExpect(jsonPath("$.[*].avgSimilarity").value(hasItem(DEFAULT_AVG_SIMILARITY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        // Get the similarityCluster
        restSimilarityClusterMockMvc
            .perform(get(ENTITY_API_URL_ID, similarityCluster.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(similarityCluster.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.algorithm").value(DEFAULT_ALGORITHM.toString()))
            .andExpect(jsonPath("$.centroid").value(DEFAULT_CENTROID))
            .andExpect(jsonPath("$.documentCount").value(DEFAULT_DOCUMENT_COUNT))
            .andExpect(jsonPath("$.avgSimilarity").value(DEFAULT_AVG_SIMILARITY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingSimilarityCluster() throws Exception {
        // Get the similarityCluster
        restSimilarityClusterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityCluster
        SimilarityCluster updatedSimilarityCluster = similarityClusterRepository.findById(similarityCluster.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedSimilarityCluster are not directly saved in db
        em.detach(updatedSimilarityCluster);
        updatedSimilarityCluster
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(updatedSimilarityCluster);

        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedSimilarityClusterToMatchAllProperties(updatedSimilarityCluster);
    }

    @Test
    @Transactional
    void putNonExistingSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateSimilarityClusterWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityCluster using partial update
        SimilarityCluster partialUpdatedSimilarityCluster = new SimilarityCluster();
        partialUpdatedSimilarityCluster.setId(similarityCluster.getId());

        partialUpdatedSimilarityCluster.centroid(UPDATED_CENTROID).documentCount(UPDATED_DOCUMENT_COUNT).createdDate(UPDATED_CREATED_DATE);

        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityCluster))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityClusterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedSimilarityCluster, similarityCluster),
            getPersistedSimilarityCluster(similarityCluster)
        );
    }

    @Test
    @Transactional
    void fullUpdateSimilarityClusterWithPatch() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the similarityCluster using partial update
        SimilarityCluster partialUpdatedSimilarityCluster = new SimilarityCluster();
        partialUpdatedSimilarityCluster.setId(similarityCluster.getId());

        partialUpdatedSimilarityCluster
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .algorithm(UPDATED_ALGORITHM)
            .centroid(UPDATED_CENTROID)
            .documentCount(UPDATED_DOCUMENT_COUNT)
            .avgSimilarity(UPDATED_AVG_SIMILARITY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedSimilarityCluster.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedSimilarityCluster))
            )
            .andExpect(status().isOk());

        // Validate the SimilarityCluster in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertSimilarityClusterUpdatableFieldsEquals(
            partialUpdatedSimilarityCluster,
            getPersistedSimilarityCluster(partialUpdatedSimilarityCluster)
        );
    }

    @Test
    @Transactional
    void patchNonExistingSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, similarityClusterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(similarityClusterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamSimilarityCluster() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        similarityCluster.setId(longCount.incrementAndGet());

        // Create the SimilarityCluster
        SimilarityClusterDTO similarityClusterDTO = similarityClusterMapper.toDto(similarityCluster);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restSimilarityClusterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(similarityClusterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the SimilarityCluster in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteSimilarityCluster() throws Exception {
        // Initialize the database
        insertedSimilarityCluster = similarityClusterRepository.saveAndFlush(similarityCluster);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the similarityCluster
        restSimilarityClusterMockMvc
            .perform(delete(ENTITY_API_URL_ID, similarityCluster.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return similarityClusterRepository.count();
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

    protected SimilarityCluster getPersistedSimilarityCluster(SimilarityCluster similarityCluster) {
        return similarityClusterRepository.findById(similarityCluster.getId()).orElseThrow();
    }

    protected void assertPersistedSimilarityClusterToMatchAllProperties(SimilarityCluster expectedSimilarityCluster) {
        assertSimilarityClusterAllPropertiesEquals(expectedSimilarityCluster, getPersistedSimilarityCluster(expectedSimilarityCluster));
    }

    protected void assertPersistedSimilarityClusterToMatchUpdatableProperties(SimilarityCluster expectedSimilarityCluster) {
        assertSimilarityClusterAllUpdatablePropertiesEquals(
            expectedSimilarityCluster,
            getPersistedSimilarityCluster(expectedSimilarityCluster)
        );
    }
}
