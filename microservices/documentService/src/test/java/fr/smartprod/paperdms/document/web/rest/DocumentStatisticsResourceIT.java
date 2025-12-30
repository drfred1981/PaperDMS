package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentStatisticsAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentStatisticsSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentStatisticsMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
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

    private static final Integer DEFAULT_VIEWS_TOTAL = 1;
    private static final Integer UPDATED_VIEWS_TOTAL = 2;
    private static final Integer SMALLER_VIEWS_TOTAL = 1 - 1;

    private static final Integer DEFAULT_DOWNLOADS_TOTAL = 1;
    private static final Integer UPDATED_DOWNLOADS_TOTAL = 2;
    private static final Integer SMALLER_DOWNLOADS_TOTAL = 1 - 1;

    private static final Integer DEFAULT_UNIQUE_VIEWERS = 1;
    private static final Integer UPDATED_UNIQUE_VIEWERS = 2;
    private static final Integer SMALLER_UNIQUE_VIEWERS = 1 - 1;

    private static final Instant DEFAULT_LAST_UPDATED = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATED = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-statistics";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-statistics/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentStatisticsRepository documentStatisticsRepository;

    @Autowired
    private DocumentStatisticsMapper documentStatisticsMapper;

    @Autowired
    private DocumentStatisticsSearchRepository documentStatisticsSearchRepository;

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
            documentStatisticsSearchRepository.delete(insertedDocumentStatistics);
            insertedDocumentStatistics = null;
        }
    }

    @Test
    @Transactional
    void createDocumentStatistics() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentStatistics = returnedDocumentStatistics;
    }

    @Test
    @Transactional
    void createDocumentStatisticsWithExistingId() throws Exception {
        // Create the DocumentStatistics with an existing ID
        documentStatistics.setId(1L);
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkLastUpdatedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        // set the field null
        documentStatistics.setLastUpdated(null);

        // Create the DocumentStatistics, which fails.
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        restDocumentStatisticsMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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
            .andExpect(jsonPath("$.viewsTotal").value(DEFAULT_VIEWS_TOTAL))
            .andExpect(jsonPath("$.downloadsTotal").value(DEFAULT_DOWNLOADS_TOTAL))
            .andExpect(jsonPath("$.uniqueViewers").value(DEFAULT_UNIQUE_VIEWERS))
            .andExpect(jsonPath("$.lastUpdated").value(DEFAULT_LAST_UPDATED.toString()));
    }

    @Test
    @Transactional
    void getDocumentStatisticsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        Long id = documentStatistics.getId();

        defaultDocumentStatisticsFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentStatisticsFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentStatisticsFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal equals to
        defaultDocumentStatisticsFiltering("viewsTotal.equals=" + DEFAULT_VIEWS_TOTAL, "viewsTotal.equals=" + UPDATED_VIEWS_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal in
        defaultDocumentStatisticsFiltering(
            "viewsTotal.in=" + DEFAULT_VIEWS_TOTAL + "," + UPDATED_VIEWS_TOTAL,
            "viewsTotal.in=" + UPDATED_VIEWS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal is not null
        defaultDocumentStatisticsFiltering("viewsTotal.specified=true", "viewsTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal is greater than or equal to
        defaultDocumentStatisticsFiltering(
            "viewsTotal.greaterThanOrEqual=" + DEFAULT_VIEWS_TOTAL,
            "viewsTotal.greaterThanOrEqual=" + UPDATED_VIEWS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal is less than or equal to
        defaultDocumentStatisticsFiltering(
            "viewsTotal.lessThanOrEqual=" + DEFAULT_VIEWS_TOTAL,
            "viewsTotal.lessThanOrEqual=" + SMALLER_VIEWS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal is less than
        defaultDocumentStatisticsFiltering("viewsTotal.lessThan=" + UPDATED_VIEWS_TOTAL, "viewsTotal.lessThan=" + DEFAULT_VIEWS_TOTAL);
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByViewsTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where viewsTotal is greater than
        defaultDocumentStatisticsFiltering(
            "viewsTotal.greaterThan=" + SMALLER_VIEWS_TOTAL,
            "viewsTotal.greaterThan=" + DEFAULT_VIEWS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal equals to
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.equals=" + DEFAULT_DOWNLOADS_TOTAL,
            "downloadsTotal.equals=" + UPDATED_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal in
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.in=" + DEFAULT_DOWNLOADS_TOTAL + "," + UPDATED_DOWNLOADS_TOTAL,
            "downloadsTotal.in=" + UPDATED_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal is not null
        defaultDocumentStatisticsFiltering("downloadsTotal.specified=true", "downloadsTotal.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal is greater than or equal to
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.greaterThanOrEqual=" + DEFAULT_DOWNLOADS_TOTAL,
            "downloadsTotal.greaterThanOrEqual=" + UPDATED_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal is less than or equal to
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.lessThanOrEqual=" + DEFAULT_DOWNLOADS_TOTAL,
            "downloadsTotal.lessThanOrEqual=" + SMALLER_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal is less than
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.lessThan=" + UPDATED_DOWNLOADS_TOTAL,
            "downloadsTotal.lessThan=" + DEFAULT_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDownloadsTotalIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where downloadsTotal is greater than
        defaultDocumentStatisticsFiltering(
            "downloadsTotal.greaterThan=" + SMALLER_DOWNLOADS_TOTAL,
            "downloadsTotal.greaterThan=" + DEFAULT_DOWNLOADS_TOTAL
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers equals to
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.equals=" + DEFAULT_UNIQUE_VIEWERS,
            "uniqueViewers.equals=" + UPDATED_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers in
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.in=" + DEFAULT_UNIQUE_VIEWERS + "," + UPDATED_UNIQUE_VIEWERS,
            "uniqueViewers.in=" + UPDATED_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers is not null
        defaultDocumentStatisticsFiltering("uniqueViewers.specified=true", "uniqueViewers.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers is greater than or equal to
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.greaterThanOrEqual=" + DEFAULT_UNIQUE_VIEWERS,
            "uniqueViewers.greaterThanOrEqual=" + UPDATED_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers is less than or equal to
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.lessThanOrEqual=" + DEFAULT_UNIQUE_VIEWERS,
            "uniqueViewers.lessThanOrEqual=" + SMALLER_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers is less than
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.lessThan=" + UPDATED_UNIQUE_VIEWERS,
            "uniqueViewers.lessThan=" + DEFAULT_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByUniqueViewersIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where uniqueViewers is greater than
        defaultDocumentStatisticsFiltering(
            "uniqueViewers.greaterThan=" + SMALLER_UNIQUE_VIEWERS,
            "uniqueViewers.greaterThan=" + DEFAULT_UNIQUE_VIEWERS
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByLastUpdatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where lastUpdated equals to
        defaultDocumentStatisticsFiltering("lastUpdated.equals=" + DEFAULT_LAST_UPDATED, "lastUpdated.equals=" + UPDATED_LAST_UPDATED);
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByLastUpdatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where lastUpdated in
        defaultDocumentStatisticsFiltering(
            "lastUpdated.in=" + DEFAULT_LAST_UPDATED + "," + UPDATED_LAST_UPDATED,
            "lastUpdated.in=" + UPDATED_LAST_UPDATED
        );
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByLastUpdatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);

        // Get all the documentStatisticsList where lastUpdated is not null
        defaultDocumentStatisticsFiltering("lastUpdated.specified=true", "lastUpdated.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentStatisticsByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentStatisticsRepository.saveAndFlush(documentStatistics);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentStatistics.setDocument(document);
        documentStatisticsRepository.saveAndFlush(documentStatistics);
        Long documentId = document.getId();
        // Get all the documentStatisticsList where document equals to documentId
        defaultDocumentStatisticsShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentStatisticsList where document equals to (documentId + 1)
        defaultDocumentStatisticsShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    private void defaultDocumentStatisticsFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentStatisticsShouldBeFound(shouldBeFound);
        defaultDocumentStatisticsShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentStatisticsShouldBeFound(String filter) throws Exception {
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewsTotal").value(hasItem(DEFAULT_VIEWS_TOTAL)))
            .andExpect(jsonPath("$.[*].downloadsTotal").value(hasItem(DEFAULT_DOWNLOADS_TOTAL)))
            .andExpect(jsonPath("$.[*].uniqueViewers").value(hasItem(DEFAULT_UNIQUE_VIEWERS)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));

        // Check, that the count call also returns 1
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentStatisticsShouldNotBeFound(String filter) throws Exception {
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
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
        documentStatisticsSearchRepository.save(documentStatistics);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());

        // Update the documentStatistics
        DocumentStatistics updatedDocumentStatistics = documentStatisticsRepository.findById(documentStatistics.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentStatistics are not directly saved in db
        em.detach(updatedDocumentStatistics);
        updatedDocumentStatistics
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

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentStatistics> documentStatisticsSearchList = Streamable.of(
                    documentStatisticsSearchRepository.findAll()
                ).toList();
                DocumentStatistics testDocumentStatisticsSearch = documentStatisticsSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentStatisticsAllPropertiesEquals(testDocumentStatisticsSearch, updatedDocumentStatistics);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
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
            .viewsTotal(UPDATED_VIEWS_TOTAL)
            .downloadsTotal(UPDATED_DOWNLOADS_TOTAL)
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
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
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
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentStatistics() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        documentStatistics.setId(longCount.incrementAndGet());

        // Create the DocumentStatistics
        DocumentStatisticsDTO documentStatisticsDTO = documentStatisticsMapper.toDto(documentStatistics);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentStatisticsMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentStatisticsDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentStatistics in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);
        documentStatisticsRepository.save(documentStatistics);
        documentStatisticsSearchRepository.save(documentStatistics);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentStatistics
        restDocumentStatisticsMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentStatistics.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentStatisticsSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentStatistics() throws Exception {
        // Initialize the database
        insertedDocumentStatistics = documentStatisticsRepository.saveAndFlush(documentStatistics);
        documentStatisticsSearchRepository.save(documentStatistics);

        // Search the documentStatistics
        restDocumentStatisticsMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentStatistics.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentStatistics.getId().intValue())))
            .andExpect(jsonPath("$.[*].viewsTotal").value(hasItem(DEFAULT_VIEWS_TOTAL)))
            .andExpect(jsonPath("$.[*].downloadsTotal").value(hasItem(DEFAULT_DOWNLOADS_TOTAL)))
            .andExpect(jsonPath("$.[*].uniqueViewers").value(hasItem(DEFAULT_UNIQUE_VIEWERS)))
            .andExpect(jsonPath("$.[*].lastUpdated").value(hasItem(DEFAULT_LAST_UPDATED.toString())));
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
