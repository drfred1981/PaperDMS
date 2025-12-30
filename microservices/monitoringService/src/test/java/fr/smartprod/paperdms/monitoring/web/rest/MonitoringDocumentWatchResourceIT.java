package fr.smartprod.paperdms.monitoring.web.rest;

import static fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatchAsserts.*;
import static fr.smartprod.paperdms.monitoring.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.monitoring.IntegrationTest;
import fr.smartprod.paperdms.monitoring.domain.MonitoringDocumentWatch;
import fr.smartprod.paperdms.monitoring.domain.enumeration.WatchType;
import fr.smartprod.paperdms.monitoring.repository.MonitoringDocumentWatchRepository;
import fr.smartprod.paperdms.monitoring.service.dto.MonitoringDocumentWatchDTO;
import fr.smartprod.paperdms.monitoring.service.mapper.MonitoringDocumentWatchMapper;
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
 * Integration tests for the {@link MonitoringDocumentWatchResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class MonitoringDocumentWatchResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

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

    private static final String ENTITY_API_URL = "/api/monitoring-document-watches";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private MonitoringDocumentWatchRepository monitoringDocumentWatchRepository;

    @Autowired
    private MonitoringDocumentWatchMapper monitoringDocumentWatchMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMonitoringDocumentWatchMockMvc;

    private MonitoringDocumentWatch monitoringDocumentWatch;

    private MonitoringDocumentWatch insertedMonitoringDocumentWatch;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static MonitoringDocumentWatch createEntity() {
        return new MonitoringDocumentWatch()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
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
    public static MonitoringDocumentWatch createUpdatedEntity() {
        return new MonitoringDocumentWatch()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
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
        monitoringDocumentWatch = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedMonitoringDocumentWatch != null) {
            monitoringDocumentWatchRepository.delete(insertedMonitoringDocumentWatch);
            insertedMonitoringDocumentWatch = null;
        }
    }

    @Test
    @Transactional
    void createMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);
        var returnedMonitoringDocumentWatchDTO = om.readValue(
            restMonitoringDocumentWatchMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            MonitoringDocumentWatchDTO.class
        );

        // Validate the MonitoringDocumentWatch in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedMonitoringDocumentWatch = monitoringDocumentWatchMapper.toEntity(returnedMonitoringDocumentWatchDTO);
        assertMonitoringDocumentWatchUpdatableFieldsEquals(
            returnedMonitoringDocumentWatch,
            getPersistedMonitoringDocumentWatch(returnedMonitoringDocumentWatch)
        );

        insertedMonitoringDocumentWatch = returnedMonitoringDocumentWatch;
    }

    @Test
    @Transactional
    void createMonitoringDocumentWatchWithExistingId() throws Exception {
        // Create the MonitoringDocumentWatch with an existing ID
        monitoringDocumentWatch.setId(1L);
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setUserId(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWatchTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setWatchType(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnViewIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setNotifyOnView(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnDownloadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setNotifyOnDownload(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnModifyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setNotifyOnModify(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnShareIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setNotifyOnShare(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNotifyOnDeleteIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setNotifyOnDelete(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        monitoringDocumentWatch.setCreatedDate(null);

        // Create the MonitoringDocumentWatch, which fails.
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatches() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringDocumentWatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
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
    void getMonitoringDocumentWatch() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get the monitoringDocumentWatch
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL_ID, monitoringDocumentWatch.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(monitoringDocumentWatch.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
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
    void getMonitoringDocumentWatchesByIdFiltering() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        Long id = monitoringDocumentWatch.getId();

        defaultMonitoringDocumentWatchFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultMonitoringDocumentWatchFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultMonitoringDocumentWatchFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where documentSha256 equals to
        defaultMonitoringDocumentWatchFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where documentSha256 in
        defaultMonitoringDocumentWatchFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where documentSha256 is not null
        defaultMonitoringDocumentWatchFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where documentSha256 contains
        defaultMonitoringDocumentWatchFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where documentSha256 does not contain
        defaultMonitoringDocumentWatchFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where userId equals to
        defaultMonitoringDocumentWatchFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where userId in
        defaultMonitoringDocumentWatchFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where userId is not null
        defaultMonitoringDocumentWatchFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where userId contains
        defaultMonitoringDocumentWatchFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where userId does not contain
        defaultMonitoringDocumentWatchFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByWatchTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where watchType equals to
        defaultMonitoringDocumentWatchFiltering("watchType.equals=" + DEFAULT_WATCH_TYPE, "watchType.equals=" + UPDATED_WATCH_TYPE);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByWatchTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where watchType in
        defaultMonitoringDocumentWatchFiltering(
            "watchType.in=" + DEFAULT_WATCH_TYPE + "," + UPDATED_WATCH_TYPE,
            "watchType.in=" + UPDATED_WATCH_TYPE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByWatchTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where watchType is not null
        defaultMonitoringDocumentWatchFiltering("watchType.specified=true", "watchType.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnViewIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnView equals to
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnView.equals=" + DEFAULT_NOTIFY_ON_VIEW,
            "notifyOnView.equals=" + UPDATED_NOTIFY_ON_VIEW
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnViewIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnView in
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnView.in=" + DEFAULT_NOTIFY_ON_VIEW + "," + UPDATED_NOTIFY_ON_VIEW,
            "notifyOnView.in=" + UPDATED_NOTIFY_ON_VIEW
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnViewIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnView is not null
        defaultMonitoringDocumentWatchFiltering("notifyOnView.specified=true", "notifyOnView.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDownloadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDownload equals to
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnDownload.equals=" + DEFAULT_NOTIFY_ON_DOWNLOAD,
            "notifyOnDownload.equals=" + UPDATED_NOTIFY_ON_DOWNLOAD
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDownloadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDownload in
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnDownload.in=" + DEFAULT_NOTIFY_ON_DOWNLOAD + "," + UPDATED_NOTIFY_ON_DOWNLOAD,
            "notifyOnDownload.in=" + UPDATED_NOTIFY_ON_DOWNLOAD
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDownloadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDownload is not null
        defaultMonitoringDocumentWatchFiltering("notifyOnDownload.specified=true", "notifyOnDownload.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnModifyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnModify equals to
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnModify.equals=" + DEFAULT_NOTIFY_ON_MODIFY,
            "notifyOnModify.equals=" + UPDATED_NOTIFY_ON_MODIFY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnModifyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnModify in
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnModify.in=" + DEFAULT_NOTIFY_ON_MODIFY + "," + UPDATED_NOTIFY_ON_MODIFY,
            "notifyOnModify.in=" + UPDATED_NOTIFY_ON_MODIFY
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnModifyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnModify is not null
        defaultMonitoringDocumentWatchFiltering("notifyOnModify.specified=true", "notifyOnModify.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnShareIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnShare equals to
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnShare.equals=" + DEFAULT_NOTIFY_ON_SHARE,
            "notifyOnShare.equals=" + UPDATED_NOTIFY_ON_SHARE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnShareIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnShare in
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnShare.in=" + DEFAULT_NOTIFY_ON_SHARE + "," + UPDATED_NOTIFY_ON_SHARE,
            "notifyOnShare.in=" + UPDATED_NOTIFY_ON_SHARE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnShareIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnShare is not null
        defaultMonitoringDocumentWatchFiltering("notifyOnShare.specified=true", "notifyOnShare.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDeleteIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDelete equals to
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnDelete.equals=" + DEFAULT_NOTIFY_ON_DELETE,
            "notifyOnDelete.equals=" + UPDATED_NOTIFY_ON_DELETE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDeleteIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDelete in
        defaultMonitoringDocumentWatchFiltering(
            "notifyOnDelete.in=" + DEFAULT_NOTIFY_ON_DELETE + "," + UPDATED_NOTIFY_ON_DELETE,
            "notifyOnDelete.in=" + UPDATED_NOTIFY_ON_DELETE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByNotifyOnDeleteIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where notifyOnDelete is not null
        defaultMonitoringDocumentWatchFiltering("notifyOnDelete.specified=true", "notifyOnDelete.specified=false");
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where createdDate equals to
        defaultMonitoringDocumentWatchFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where createdDate in
        defaultMonitoringDocumentWatchFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllMonitoringDocumentWatchesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        // Get all the monitoringDocumentWatchList where createdDate is not null
        defaultMonitoringDocumentWatchFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultMonitoringDocumentWatchFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultMonitoringDocumentWatchShouldBeFound(shouldBeFound);
        defaultMonitoringDocumentWatchShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMonitoringDocumentWatchShouldBeFound(String filter) throws Exception {
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(monitoringDocumentWatch.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].watchType").value(hasItem(DEFAULT_WATCH_TYPE.toString())))
            .andExpect(jsonPath("$.[*].notifyOnView").value(hasItem(DEFAULT_NOTIFY_ON_VIEW)))
            .andExpect(jsonPath("$.[*].notifyOnDownload").value(hasItem(DEFAULT_NOTIFY_ON_DOWNLOAD)))
            .andExpect(jsonPath("$.[*].notifyOnModify").value(hasItem(DEFAULT_NOTIFY_ON_MODIFY)))
            .andExpect(jsonPath("$.[*].notifyOnShare").value(hasItem(DEFAULT_NOTIFY_ON_SHARE)))
            .andExpect(jsonPath("$.[*].notifyOnDelete").value(hasItem(DEFAULT_NOTIFY_ON_DELETE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMonitoringDocumentWatchShouldNotBeFound(String filter) throws Exception {
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMonitoringDocumentWatchMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingMonitoringDocumentWatch() throws Exception {
        // Get the monitoringDocumentWatch
        restMonitoringDocumentWatchMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingMonitoringDocumentWatch() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringDocumentWatch
        MonitoringDocumentWatch updatedMonitoringDocumentWatch = monitoringDocumentWatchRepository
            .findById(monitoringDocumentWatch.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedMonitoringDocumentWatch are not directly saved in db
        em.detach(updatedMonitoringDocumentWatch);
        updatedMonitoringDocumentWatch
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .userId(UPDATED_USER_ID)
            .watchType(UPDATED_WATCH_TYPE)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnDownload(UPDATED_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE)
            .createdDate(UPDATED_CREATED_DATE);
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(updatedMonitoringDocumentWatch);

        restMonitoringDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringDocumentWatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedMonitoringDocumentWatchToMatchAllProperties(updatedMonitoringDocumentWatch);
    }

    @Test
    @Transactional
    void putNonExistingMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, monitoringDocumentWatchDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(monitoringDocumentWatchDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateMonitoringDocumentWatchWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringDocumentWatch using partial update
        MonitoringDocumentWatch partialUpdatedMonitoringDocumentWatch = new MonitoringDocumentWatch();
        partialUpdatedMonitoringDocumentWatch.setId(monitoringDocumentWatch.getId());

        partialUpdatedMonitoringDocumentWatch
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE);

        restMonitoringDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringDocumentWatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringDocumentWatch))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringDocumentWatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringDocumentWatchUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedMonitoringDocumentWatch, monitoringDocumentWatch),
            getPersistedMonitoringDocumentWatch(monitoringDocumentWatch)
        );
    }

    @Test
    @Transactional
    void fullUpdateMonitoringDocumentWatchWithPatch() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the monitoringDocumentWatch using partial update
        MonitoringDocumentWatch partialUpdatedMonitoringDocumentWatch = new MonitoringDocumentWatch();
        partialUpdatedMonitoringDocumentWatch.setId(monitoringDocumentWatch.getId());

        partialUpdatedMonitoringDocumentWatch
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .userId(UPDATED_USER_ID)
            .watchType(UPDATED_WATCH_TYPE)
            .notifyOnView(UPDATED_NOTIFY_ON_VIEW)
            .notifyOnDownload(UPDATED_NOTIFY_ON_DOWNLOAD)
            .notifyOnModify(UPDATED_NOTIFY_ON_MODIFY)
            .notifyOnShare(UPDATED_NOTIFY_ON_SHARE)
            .notifyOnDelete(UPDATED_NOTIFY_ON_DELETE)
            .createdDate(UPDATED_CREATED_DATE);

        restMonitoringDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedMonitoringDocumentWatch.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedMonitoringDocumentWatch))
            )
            .andExpect(status().isOk());

        // Validate the MonitoringDocumentWatch in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertMonitoringDocumentWatchUpdatableFieldsEquals(
            partialUpdatedMonitoringDocumentWatch,
            getPersistedMonitoringDocumentWatch(partialUpdatedMonitoringDocumentWatch)
        );
    }

    @Test
    @Transactional
    void patchNonExistingMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, monitoringDocumentWatchDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamMonitoringDocumentWatch() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        monitoringDocumentWatch.setId(longCount.incrementAndGet());

        // Create the MonitoringDocumentWatch
        MonitoringDocumentWatchDTO monitoringDocumentWatchDTO = monitoringDocumentWatchMapper.toDto(monitoringDocumentWatch);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restMonitoringDocumentWatchMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(monitoringDocumentWatchDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the MonitoringDocumentWatch in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteMonitoringDocumentWatch() throws Exception {
        // Initialize the database
        insertedMonitoringDocumentWatch = monitoringDocumentWatchRepository.saveAndFlush(monitoringDocumentWatch);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the monitoringDocumentWatch
        restMonitoringDocumentWatchMockMvc
            .perform(delete(ENTITY_API_URL_ID, monitoringDocumentWatch.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return monitoringDocumentWatchRepository.count();
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

    protected MonitoringDocumentWatch getPersistedMonitoringDocumentWatch(MonitoringDocumentWatch monitoringDocumentWatch) {
        return monitoringDocumentWatchRepository.findById(monitoringDocumentWatch.getId()).orElseThrow();
    }

    protected void assertPersistedMonitoringDocumentWatchToMatchAllProperties(MonitoringDocumentWatch expectedMonitoringDocumentWatch) {
        assertMonitoringDocumentWatchAllPropertiesEquals(
            expectedMonitoringDocumentWatch,
            getPersistedMonitoringDocumentWatch(expectedMonitoringDocumentWatch)
        );
    }

    protected void assertPersistedMonitoringDocumentWatchToMatchUpdatableProperties(
        MonitoringDocumentWatch expectedMonitoringDocumentWatch
    ) {
        assertMonitoringDocumentWatchAllUpdatablePropertiesEquals(
            expectedMonitoringDocumentWatch,
            getPersistedMonitoringDocumentWatch(expectedMonitoringDocumentWatch)
        );
    }
}
