package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentStatisticsAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentStatisticsMapper;
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
 * Integration tests for the {@link DocumentStatisticsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentStatisticsResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final Integer DEFAULT_VIEWS_TOTAL = 1;
    private static final Integer UPDATED_VIEWS_TOTAL = 2;

    private static final Integer DEFAULT_DOWNLOADS_TOTAL = 1;
    private static final Integer UPDATED_DOWNLOADS_TOTAL = 2;

    private static final Integer DEFAULT_UNIQUE_VIEWERS = 1;
    private static final Integer UPDATED_UNIQUE_VIEWERS = 2;

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentStatisticsRepository documentStatisticsRepository;

    @Autowired
    private DocumentStatisticsMapper documentStatisticsMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentStatisticsMockMvc;

    private DocumentStatistics documentStatistics;

    private DocumentStatistics insertedDocumentStatistics;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentStatistics createEntity() {
        return new DocumentStatistics()
            .documentId(DEFAULT_DOCUMENT_ID)
            .viewsTotal(DEFAULT_VIEWS_TOTAL)
            .downloadsTotal(DEFAULT_DOWNLOADS_TOTAL)
            .uniqueViewers(DEFAULT_UNIQUE_VIEWERS)
            .lastUpdated(DEFAULT_LAST_UPDATED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentStatistics createUpdatedEntity() {
        return new DocumentStatistics()
            .documentId(UPDATED_DOCUMENT_ID)
            .viewsTotal(UPDATED_VIEWS_TOTAL)
            .downloadsTotal(UPDATED_DOWNLOADS_TOTAL)
            .uniqueViewers(UPDATED_UNIQUE_VIEWERS)
            .lastUpdated(UPDATED_LAST_UPDATED);
    }

    @BeforeEach
    void initTest() {
        documentStatistics = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentStatistics != null) {
            documentStatisticsRepository.delete(insertedDocumentStatistics);
            insertedDocumentStatistics = null;
        }
    }

    @Test
    @Transactional
    void createDocumentStatistics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);
        var returnedDocumentStatisticsDTO = om.readValue(
            restDocumentStatisticsMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentStatisticsDTO.class
        );

        // Validate the DocumentStatistics in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentStatistics = documentStatisticsMapper.toEntity(returnedDocumentStatisticsDTO);
        assertDocumentStatisticsUpdatableFieldsEquals(
            returnedDocumentStatistics,
            getPersistedDocumentStatistics(returnedDocumentStatistics)
        );

        insertedDocumentStatistics = returnedDocumentStatistics;
    }

    @Test
    @Transactional
    void createDocumentStatisticsWithExistingId() throws Exception {
        // Create the DocumentStatistics with an existing ID
        documentStatistics.setId(1L);
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentStatistics.setDocumentId(null);

        // Create the DocumentStatistics, which fails.
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        restDocumentStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentStatistics.setLastUpdated(null);

        // Create the DocumentStatistics, which fails.
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        restDocumentStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].viewsTotal").value(hasItem(DEFAULT_VIEWS_TOTAL)))
            .andExpect(jsonPath("$.[*].downloadsTotal").value(hasItem(DEFAULT_DOWNLOADS_TOTAL)))
            .andExpect(jsonPath("$.[*].uniqueViewers").value(hasItem(DEFAULT_UNIQUE_VIEWERS)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
    }

    @Test
    @Transactional
    void getDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get the documentStatistics
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL_ID, documentStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentStatistics.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.viewsTotal").value(DEFAULT_VIEWS_TOTAL))
            .andExpect(jsonPath("$.downloadsTotal").value(DEFAULT_DOWNLOADS_TOTAL))
            .andExpect(jsonPath("$.uniqueViewers").value(DEFAULT_UNIQUE_VIEWERS))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDocumentStatistics() throws Exception {
        // Get the documentStatistics
        restDocumentStatisticsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatistics
        DocumentStatistics updatedDocumentStatistics = documentStatisticsRepository.findById(documentStatistics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentStatistics are not directly saved in db
        em.detach(updatedDocumentStatistics);
        updatedDocumentStatistics
            .documentId(UPDATED_DOCUMENT_ID)
            .viewsTotal(UPDATED_VIEWS_TOTAL)
            .downloadsTotal(UPDATED_DOWNLOADS_TOTAL)
            .uniqueViewers(UPDATED_UNIQUE_VIEWERS)
            .lastUpdated(UPDATED_LAST_UPDATED);
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(updatedDocumentStatistics);

        restDocumentStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatisticsDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentStatisticsToMatchAllProperties(updatedDocumentStatistics);
    }

    @Test
    @Transactional
    void putNonExistingDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentStatisticsDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatistics using partial update
        DocumentStatistics partialUpdatedDocumentStatistics = new DocumentStatistics();
        partialUpdatedDocumentStatistics.setId(documentStatistics.getId());

        partialUpdatedDocumentStatistics
            .documentId(UPDATED_DOCUMENT_ID)
            .viewsTotal(UPDATED_VIEWS_TOTAL)
            .uniqueViewers(UPDATED_UNIQUE_VIEWERS);

        restDocumentStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentStatistics))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentStatisticsUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentStatistics, documentStatistics),
            getPersistedDocumentStatistics(documentStatistics)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentStatisticsWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentStatistics using partial update
        DocumentStatistics partialUpdatedDocumentStatistics = new DocumentStatistics();
        partialUpdatedDocumentStatistics.setId(documentStatistics.getId());

        partialUpdatedDocumentStatistics
            .documentId(UPDATED_DOCUMENT_ID)
            .viewsTotal(UPDATED_VIEWS_TOTAL)
            .downloadsTotal(UPDATED_DOWNLOADS_TOTAL)
            .uniqueViewers(UPDATED_UNIQUE_VIEWERS)
            .lastUpdated(UPDATED_LAST_UPDATED);

        restDocumentStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentStatistics.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentStatistics))
            )
            .andExpect(status().isOk());

        // Validate the DocumentStatistics in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentStatisticsUpdatableFieldsEquals(
            partialUpdatedDocumentStatistics,
            getPersistedDocumentStatistics(partialUpdatedDocumentStatistics)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentStatisticsDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentStatisticsDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentStatistics
        restDocumentStatisticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentStatistics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentStatisticsRepository.count();
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

    protected DocumentStatistics getPersistedDocumentStatistics(DocumentStatistics documentStatistics) {
        return documentStatisticsRepository.findById(documentStatistics.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentStatisticsToMatchAllProperties(DocumentStatistics expectedDocumentStatistics) {
        assertDocumentStatisticsAllPropertiesEquals(expectedDocumentStatistics, getPersistedDocumentStatistics(expectedDocumentStatistics));
    }

    protected void assertPersistedDocumentStatisticsToMatchUpdatableProperties(DocumentStatistics expectedDocumentStatistics) {
        assertDocumentStatisticsAllUpdatablePropertiesEquals(
            expectedDocumentStatistics,
            getPersistedDocumentStatistics(expectedDocumentStatistics)
        );
    }
}
