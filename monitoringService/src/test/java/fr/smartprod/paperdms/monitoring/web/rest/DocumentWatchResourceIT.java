package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.DocumentWatchAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.DocumentWatch;
import fr.smartprod.paperdms.monitoring.domain.enumeration.WatchType;
import fr.smartprod.paperdms.monitoring.repository.DocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.DocumentWatchMapper;
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
 * Integration tests for the {@link DocumentWatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentWatchResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final WatchType DEFAULT_WATCH_TYPE = WatchType.AUTHOR;
    private static final WatchType UPDATED_WATCH_TYPE = WatchType.REVIEWER;

    private static final Boolean DEFAULT_NOTIFY_ON_VIEW = false;
    private static final Boolean UPDATED_NOTIFY_ON_VIEW = true;

    private static final Boolean DEFAULT_NOTIFY_ON_DOWNLOAD = false;
    private static final Boolean UPDATED_NOTIFY_ON_DOWNLOAD = true;

    private static final Boolean DEFAULT_NOTIFY_ON_MODIFY = false;
    private static final Boolean UPDATED_NOTIFY_ON_MODIFY = true;

    private static final Boolean DEFAULT_NOTIFY_ON_SHARE = false;
    private static final Boolean UPDATED_NOTIFY_ON_SHARE = true;

    private static final Boolean DEFAULT_NOTIFY_ON_DELETE = false;
    private static final Boolean UPDATED_NOTIFY_ON_DELETE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-watches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentWatchRepository documentWatchRepository;

    @Autowired
    private DocumentWatchMapper documentWatchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentWatchMockMvc;

    private DocumentWatch documentWatch;

    private DocumentWatch insertedDocumentWatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentWatch createEntity() {
        return new DocumentWatch()
            .documentId(DEFAULT_DOCUMENT_ID)
            .userId(DEFAULT_USER_ID)
            .watchType(DEFAULT_WATCH_TYPE)
            .notifyOnView(DEFAULT_NOTIFY_ON_VIEW)
            .notifyOnDownload(DEFAULT_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(DEFAULT_NOTIFY_ON_MODIFY)
            .notifyOnShare(DEFAULT_NOTIFY_ON_SHARE)
            .notifyOnDelete(DEFAULT_NOTIFY_ON_DELETE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentWatch createUpdatedEntity() {
        return new DocumentWatch()
            .documentId(UPDATED_DOCUMENT_ID)
            .userId(UPDATED_USER_ID)
            .watchType(UPDATED_WATCH_TYPE)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnDownload(UPDATED_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentWatch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentWatch != null) {
            documentWatchRepository.delete(insertedDocumentWatch);
            insertedDocumentWatch = null;
        }
    }

    @Test
    @Transactional
    void createDocumentWatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);
        var returnedDocumentWatchDTO = om.readValue(
            restDocumentWatchMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentWatchDTO.class
        );

        // Validate the DocumentWatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentWatch = documentWatchMapper.toEntity(returnedDocumentWatchDTO);
        assertDocumentWatchUpdatableFieldsEquals(returnedDocumentWatch, getPersistedDocumentWatch(returnedDocumentWatch));

        insertedDocumentWatch = returnedDocumentWatch;
    }

    @Test
    @Transactional
    void createDocumentWatchWithExistingId() throws Exception {
        // Create the DocumentWatch with an existing ID
        documentWatch.setId(1L);
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setDocumentId(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setUserId(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWatchTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setWatchType(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnViewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setNotifyOnView(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnDownloadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setNotifyOnDownload(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnModifyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setNotifyOnModify(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnShareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setNotifyOnShare(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnDeleteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setNotifyOnDelete(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentWatch.setCreatedDate(null);

        // Create the DocumentWatch, which fails.
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        restDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentWatches() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentWatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].watchType").value(hasItem(DEFAULT_WATCH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notifyOnView").value(hasItem(DEFAULT_NOTIFY_ON_VIEW)))
            .andExpect(jsonPath("$.[*].notifyOnDownload").value(hasItem(DEFAULT_NOTIFY_ON_DOWNLOAD)))
            .andExpect(jsonPath("$.[*].notifyOnModify").value(hasItem(DEFAULT_NOTIFY_ON_MODIFY)))
            .andExpect(jsonPath("$.[*].notifyOnShare").value(hasItem(DEFAULT_NOTIFY_ON_SHARE)))
            .andExpect(jsonPath("$.[*].notifyOnDelete").value(hasItem(DEFAULT_NOTIFY_ON_DELETE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentWatch() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get the documentWatch
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL_ID, documentWatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentWatch.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.watchType").value(DEFAULT_WATCH_TYPE.toString()))
            .andExpect(jsonPath("$.notifyOnView").value(DEFAULT_NOTIFY_ON_VIEW))
            .andExpect(jsonPath("$.notifyOnDownload").value(DEFAULT_NOTIFY_ON_DOWNLOAD))
            .andExpect(jsonPath("$.notifyOnModify").value(DEFAULT_NOTIFY_ON_MODIFY))
            .andExpect(jsonPath("$.notifyOnShare").value(DEFAULT_NOTIFY_ON_SHARE))
            .andExpect(jsonPath("$.notifyOnDelete").value(DEFAULT_NOTIFY_ON_DELETE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentWatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        Long id = documentWatch.getId();

        defaultDocumentWatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentWatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentWatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId equals to
        defaultDocumentWatchFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId in
        defaultDocumentWatchFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId is not null
        defaultDocumentWatchFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId is greater than or equal to
        defaultDocumentWatchFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId is less than or equal to
        defaultDocumentWatchFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId is less than
        defaultDocumentWatchFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where documentId is greater than
        defaultDocumentWatchFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where userId equals to
        defaultDocumentWatchFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where userId in
        defaultDocumentWatchFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where userId is not null
        defaultDocumentWatchFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where userId contains
        defaultDocumentWatchFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where userId does not contain
        defaultDocumentWatchFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByWatchTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where watchType equals to
        defaultDocumentWatchFiltering("watchType.equals=" + DEFAULT_WATCH_TYPE, "watchType.equals=" + UPDATED_WATCH_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByWatchTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where watchType in
        defaultDocumentWatchFiltering(
            "watchType.in=" + DEFAULT_WATCH_TYPE + "," + UPDATED_WATCH_TYPE,
            "watchType.in=" + UPDATED_WATCH_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByWatchTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where watchType is not null
        defaultDocumentWatchFiltering("watchType.specified=true", "watchType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnViewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnView equals to
        defaultDocumentWatchFiltering("notifyOnView.equals=" + DEFAULT_NOTIFY_ON_VIEW, "notifyOnView.equals=" + UPDATED_NOTIFY_ON_VIEW);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnViewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnView in
        defaultDocumentWatchFiltering(
            "notifyOnView.in=" + DEFAULT_NOTIFY_ON_VIEW + "," + UPDATED_NOTIFY_ON_VIEW,
            "notifyOnView.in=" + UPDATED_NOTIFY_ON_VIEW
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnViewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnView is not null
        defaultDocumentWatchFiltering("notifyOnView.specified=true", "notifyOnView.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDownloadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDownload equals to
        defaultDocumentWatchFiltering(
            "notifyOnDownload.equals=" + DEFAULT_NOTIFY_ON_DOWNLOAD,
            "notifyOnDownload.equals=" + UPDATED_NOTIFY_ON_DOWNLOAD
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDownloadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDownload in
        defaultDocumentWatchFiltering(
            "notifyOnDownload.in=" + DEFAULT_NOTIFY_ON_DOWNLOAD + "," + UPDATED_NOTIFY_ON_DOWNLOAD,
            "notifyOnDownload.in=" + UPDATED_NOTIFY_ON_DOWNLOAD
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDownloadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDownload is not null
        defaultDocumentWatchFiltering("notifyOnDownload.specified=true", "notifyOnDownload.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnModifyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnModify equals to
        defaultDocumentWatchFiltering(
            "notifyOnModify.equals=" + DEFAULT_NOTIFY_ON_MODIFY,
            "notifyOnModify.equals=" + UPDATED_NOTIFY_ON_MODIFY
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnModifyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnModify in
        defaultDocumentWatchFiltering(
            "notifyOnModify.in=" + DEFAULT_NOTIFY_ON_MODIFY + "," + UPDATED_NOTIFY_ON_MODIFY,
            "notifyOnModify.in=" + UPDATED_NOTIFY_ON_MODIFY
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnModifyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnModify is not null
        defaultDocumentWatchFiltering("notifyOnModify.specified=true", "notifyOnModify.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnShareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnShare equals to
        defaultDocumentWatchFiltering("notifyOnShare.equals=" + DEFAULT_NOTIFY_ON_SHARE, "notifyOnShare.equals=" + UPDATED_NOTIFY_ON_SHARE);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnShareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnShare in
        defaultDocumentWatchFiltering(
            "notifyOnShare.in=" + DEFAULT_NOTIFY_ON_SHARE + "," + UPDATED_NOTIFY_ON_SHARE,
            "notifyOnShare.in=" + UPDATED_NOTIFY_ON_SHARE
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnShareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnShare is not null
        defaultDocumentWatchFiltering("notifyOnShare.specified=true", "notifyOnShare.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDelete equals to
        defaultDocumentWatchFiltering(
            "notifyOnDelete.equals=" + DEFAULT_NOTIFY_ON_DELETE,
            "notifyOnDelete.equals=" + UPDATED_NOTIFY_ON_DELETE
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDelete in
        defaultDocumentWatchFiltering(
            "notifyOnDelete.in=" + DEFAULT_NOTIFY_ON_DELETE + "," + UPDATED_NOTIFY_ON_DELETE,
            "notifyOnDelete.in=" + UPDATED_NOTIFY_ON_DELETE
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByNotifyOnDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where notifyOnDelete is not null
        defaultDocumentWatchFiltering("notifyOnDelete.specified=true", "notifyOnDelete.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where createdDate equals to
        defaultDocumentWatchFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where createdDate in
        defaultDocumentWatchFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentWatchesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        // Get all the documentWatchList where createdDate is not null
        defaultDocumentWatchFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultDocumentWatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentWatchShouldBeFound(shouldBeFound);
        defaultDocumentWatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentWatchShouldBeFound(String filter) throws Exception {
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentWatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].watchType").value(hasItem(DEFAULT_WATCH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notifyOnView").value(hasItem(DEFAULT_NOTIFY_ON_VIEW)))
            .andExpect(jsonPath("$.[*].notifyOnDownload").value(hasItem(DEFAULT_NOTIFY_ON_DOWNLOAD)))
            .andExpect(jsonPath("$.[*].notifyOnModify").value(hasItem(DEFAULT_NOTIFY_ON_MODIFY)))
            .andExpect(jsonPath("$.[*].notifyOnShare").value(hasItem(DEFAULT_NOTIFY_ON_SHARE)))
            .andExpect(jsonPath("$.[*].notifyOnDelete").value(hasItem(DEFAULT_NOTIFY_ON_DELETE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentWatchShouldNotBeFound(String filter) throws Exception {
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentWatch() throws Exception {
        // Get the documentWatch
        restDocumentWatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentWatch() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentWatch
        DocumentWatch updatedDocumentWatch = documentWatchRepository.findById(documentWatch.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentWatch are not directly saved in db
        em.detach(updatedDocumentWatch);
        updatedDocumentWatch
            .documentId(UPDATED_DOCUMENT_ID)
            .userId(UPDATED_USER_ID)
            .watchType(UPDATED_WATCH_TYPE)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnDownload(UPDATED_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(updatedDocumentWatch);

        restDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentWatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentWatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentWatchToMatchAllProperties(updatedDocumentWatch);
    }

    @Test
    @Transactional
    void putNonExistingDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentWatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWatchWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentWatch using partial update
        DocumentWatch partialUpdatedDocumentWatch = new DocumentWatch();
        partialUpdatedDocumentWatch.setId(documentWatch.getId());

        partialUpdatedDocumentWatch
            .documentId(UPDATED_DOCUMENT_ID)
            .userId(UPDATED_USER_ID)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE);

        restDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentWatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentWatch))
            )
            .andExpect(status().isOk());

        // Validate the DocumentWatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentWatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentWatch, documentWatch),
            getPersistedDocumentWatch(documentWatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentWatchWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentWatch using partial update
        DocumentWatch partialUpdatedDocumentWatch = new DocumentWatch();
        partialUpdatedDocumentWatch.setId(documentWatch.getId());

        partialUpdatedDocumentWatch
            .documentId(UPDATED_DOCUMENT_ID)
            .userId(UPDATED_USER_ID)
            .watchType(UPDATED_WATCH_TYPE)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnDownload(UPDATED_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentWatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentWatch))
            )
            .andExpect(status().isOk());

        // Validate the DocumentWatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentWatchUpdatableFieldsEquals(partialUpdatedDocumentWatch, getPersistedDocumentWatch(partialUpdatedDocumentWatch));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentWatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentWatch.setId(longCount.incrementAndGet());

        // Create the DocumentWatch
        DocumentWatchDTO documentWatchDTO = documentWatchMapper.toDto(documentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentWatchMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentWatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentWatch() throws Exception {
        // Initialize the database
        insertedDocumentWatch = documentWatchRepository.saveAndFlush(documentWatch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentWatch
        restDocumentWatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentWatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentWatchRepository.count();
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

    protected DocumentWatch getPersistedDocumentWatch(DocumentWatch documentWatch) {
        return documentWatchRepository.findById(documentWatch.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentWatchToMatchAllProperties(DocumentWatch expectedDocumentWatch) {
        assertDocumentWatchAllPropertiesEquals(expectedDocumentWatch, getPersistedDocumentWatch(expectedDocumentWatch));
    }

    protected void assertPersistedDocumentWatchToMatchUpdatableProperties(DocumentWatch expectedDocumentWatch) {
        assertDocumentWatchAllUpdatablePropertiesEquals(expectedDocumentWatch, getPersistedDocumentWatch(expectedDocumentWatch));
    }
}
