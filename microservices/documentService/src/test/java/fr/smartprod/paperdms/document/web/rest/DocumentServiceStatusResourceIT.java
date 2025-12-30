package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentServiceStatusAsserts.*;
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
import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceStatus;
import fr.smartprod.paperdms.document.domain.enumeration.ServiceType;
import fr.smartprod.paperdms.document.repository.DocumentServiceStatusRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentServiceStatusSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentServiceStatusMapper;
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
 * Integration tests for the {@link DocumentServiceStatusResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentServiceStatusResourceIT {

    private static final ServiceType DEFAULT_SERVICE_TYPE = ServiceType.OCR_SERVICE;
    private static final ServiceType UPDATED_SERVICE_TYPE = ServiceType.AI_SERVICE;

    private static final ServiceStatus DEFAULT_STATUS = ServiceStatus.NOT_APPLICABLE;
    private static final ServiceStatus UPDATED_STATUS = ServiceStatus.PENDING;

    private static final String DEFAULT_STATUS_DETAILS = "AAAAAAAAAA";
    private static final String UPDATED_STATUS_DETAILS = "BBBBBBBBBB";

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_RETRY_COUNT = 1;
    private static final Integer UPDATED_RETRY_COUNT = 2;
    private static final Integer SMALLER_RETRY_COUNT = 1 - 1;

    private static final Instant DEFAULT_LAST_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSING_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSING_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSING_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSING_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Long DEFAULT_PROCESSING_DURATION = 1L;
    private static final Long UPDATED_PROCESSING_DURATION = 2L;
    private static final Long SMALLER_PROCESSING_DURATION = 1L - 1L;

    private static final String DEFAULT_JOB_ID = "AAAAAAAAAA";
    private static final String UPDATED_JOB_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_PRIORITY = 1;
    private static final Integer UPDATED_PRIORITY = 2;
    private static final Integer SMALLER_PRIORITY = 1 - 1;

    private static final String DEFAULT_UPDATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_UPDATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPDATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPDATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-service-statuses";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-service-statuses/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentServiceStatusRepository documentServiceStatusRepository;

    @Autowired
    private DocumentServiceStatusMapper documentServiceStatusMapper;

    @Autowired
    private DocumentServiceStatusSearchRepository documentServiceStatusSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentServiceStatusMockMvc;

    private DocumentServiceStatus documentServiceStatus;

    private DocumentServiceStatus insertedDocumentServiceStatus;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentServiceStatus createEntity() {
        return new DocumentServiceStatus()
            .serviceType(DEFAULT_SERVICE_TYPE)
            .status(DEFAULT_STATUS)
            .statusDetails(DEFAULT_STATUS_DETAILS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .retryCount(DEFAULT_RETRY_COUNT)
            .lastProcessedDate(DEFAULT_LAST_PROCESSED_DATE)
            .processingStartDate(DEFAULT_PROCESSING_START_DATE)
            .processingEndDate(DEFAULT_PROCESSING_END_DATE)
            .processingDuration(DEFAULT_PROCESSING_DURATION)
            .jobId(DEFAULT_JOB_ID)
            .priority(DEFAULT_PRIORITY)
            .updatedBy(DEFAULT_UPDATED_BY)
            .updatedDate(DEFAULT_UPDATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentServiceStatus createUpdatedEntity() {
        return new DocumentServiceStatus()
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .statusDetails(UPDATED_STATUS_DETAILS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .retryCount(UPDATED_RETRY_COUNT)
            .lastProcessedDate(UPDATED_LAST_PROCESSED_DATE)
            .processingStartDate(UPDATED_PROCESSING_START_DATE)
            .processingEndDate(UPDATED_PROCESSING_END_DATE)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .jobId(UPDATED_JOB_ID)
            .priority(UPDATED_PRIORITY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentServiceStatus = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentServiceStatus != null) {
            documentServiceStatusRepository.delete(insertedDocumentServiceStatus);
            documentServiceStatusSearchRepository.delete(insertedDocumentServiceStatus);
            insertedDocumentServiceStatus = null;
        }
    }

    @Test
    @Transactional
    void createDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);
        var returnedDocumentServiceStatusDTO = om.readValue(
            restDocumentServiceStatusMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentServiceStatusDTO.class
        );

        // Validate the DocumentServiceStatus in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentServiceStatus = documentServiceStatusMapper.toEntity(returnedDocumentServiceStatusDTO);
        assertDocumentServiceStatusUpdatableFieldsEquals(
            returnedDocumentServiceStatus,
            getPersistedDocumentServiceStatus(returnedDocumentServiceStatus)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentServiceStatus = returnedDocumentServiceStatus;
    }

    @Test
    @Transactional
    void createDocumentServiceStatusWithExistingId() throws Exception {
        // Create the DocumentServiceStatus with an existing ID
        documentServiceStatus.setId(1L);
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkServiceTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        // set the field null
        documentServiceStatus.setServiceType(null);

        // Create the DocumentServiceStatus, which fails.
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        restDocumentServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        // set the field null
        documentServiceStatus.setStatus(null);

        // Create the DocumentServiceStatus, which fails.
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        restDocumentServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUpdatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        // set the field null
        documentServiceStatus.setUpdatedDate(null);

        // Create the DocumentServiceStatus, which fails.
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        restDocumentServiceStatusMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatuses() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentServiceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDetails").value(hasItem(DEFAULT_STATUS_DETAILS)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].lastProcessedDate").value(hasItem(DEFAULT_LAST_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingStartDate").value(hasItem(DEFAULT_PROCESSING_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingEndDate").value(hasItem(DEFAULT_PROCESSING_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentServiceStatus() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get the documentServiceStatus
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL_ID, documentServiceStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentServiceStatus.getId().intValue()))
            .andExpect(jsonPath("$.serviceType").value(DEFAULT_SERVICE_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.statusDetails").value(DEFAULT_STATUS_DETAILS))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.retryCount").value(DEFAULT_RETRY_COUNT))
            .andExpect(jsonPath("$.lastProcessedDate").value(DEFAULT_LAST_PROCESSED_DATE.toString()))
            .andExpect(jsonPath("$.processingStartDate").value(DEFAULT_PROCESSING_START_DATE.toString()))
            .andExpect(jsonPath("$.processingEndDate").value(DEFAULT_PROCESSING_END_DATE.toString()))
            .andExpect(jsonPath("$.processingDuration").value(DEFAULT_PROCESSING_DURATION.intValue()))
            .andExpect(jsonPath("$.jobId").value(DEFAULT_JOB_ID))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY))
            .andExpect(jsonPath("$.updatedBy").value(DEFAULT_UPDATED_BY))
            .andExpect(jsonPath("$.updatedDate").value(DEFAULT_UPDATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentServiceStatusesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        Long id = documentServiceStatus.getId();

        defaultDocumentServiceStatusFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentServiceStatusFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentServiceStatusFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByServiceTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where serviceType equals to
        defaultDocumentServiceStatusFiltering("serviceType.equals=" + DEFAULT_SERVICE_TYPE, "serviceType.equals=" + UPDATED_SERVICE_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByServiceTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where serviceType in
        defaultDocumentServiceStatusFiltering(
            "serviceType.in=" + DEFAULT_SERVICE_TYPE + "," + UPDATED_SERVICE_TYPE,
            "serviceType.in=" + UPDATED_SERVICE_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByServiceTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where serviceType is not null
        defaultDocumentServiceStatusFiltering("serviceType.specified=true", "serviceType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where status equals to
        defaultDocumentServiceStatusFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where status in
        defaultDocumentServiceStatusFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where status is not null
        defaultDocumentServiceStatusFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount equals to
        defaultDocumentServiceStatusFiltering("retryCount.equals=" + DEFAULT_RETRY_COUNT, "retryCount.equals=" + UPDATED_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount in
        defaultDocumentServiceStatusFiltering(
            "retryCount.in=" + DEFAULT_RETRY_COUNT + "," + UPDATED_RETRY_COUNT,
            "retryCount.in=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount is not null
        defaultDocumentServiceStatusFiltering("retryCount.specified=true", "retryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount is greater than or equal to
        defaultDocumentServiceStatusFiltering(
            "retryCount.greaterThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.greaterThanOrEqual=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount is less than or equal to
        defaultDocumentServiceStatusFiltering(
            "retryCount.lessThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.lessThanOrEqual=" + SMALLER_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount is less than
        defaultDocumentServiceStatusFiltering("retryCount.lessThan=" + UPDATED_RETRY_COUNT, "retryCount.lessThan=" + DEFAULT_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByRetryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where retryCount is greater than
        defaultDocumentServiceStatusFiltering(
            "retryCount.greaterThan=" + SMALLER_RETRY_COUNT,
            "retryCount.greaterThan=" + DEFAULT_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByLastProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where lastProcessedDate equals to
        defaultDocumentServiceStatusFiltering(
            "lastProcessedDate.equals=" + DEFAULT_LAST_PROCESSED_DATE,
            "lastProcessedDate.equals=" + UPDATED_LAST_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByLastProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where lastProcessedDate in
        defaultDocumentServiceStatusFiltering(
            "lastProcessedDate.in=" + DEFAULT_LAST_PROCESSED_DATE + "," + UPDATED_LAST_PROCESSED_DATE,
            "lastProcessedDate.in=" + UPDATED_LAST_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByLastProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where lastProcessedDate is not null
        defaultDocumentServiceStatusFiltering("lastProcessedDate.specified=true", "lastProcessedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingStartDate equals to
        defaultDocumentServiceStatusFiltering(
            "processingStartDate.equals=" + DEFAULT_PROCESSING_START_DATE,
            "processingStartDate.equals=" + UPDATED_PROCESSING_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingStartDate in
        defaultDocumentServiceStatusFiltering(
            "processingStartDate.in=" + DEFAULT_PROCESSING_START_DATE + "," + UPDATED_PROCESSING_START_DATE,
            "processingStartDate.in=" + UPDATED_PROCESSING_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingStartDate is not null
        defaultDocumentServiceStatusFiltering("processingStartDate.specified=true", "processingStartDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingEndDate equals to
        defaultDocumentServiceStatusFiltering(
            "processingEndDate.equals=" + DEFAULT_PROCESSING_END_DATE,
            "processingEndDate.equals=" + UPDATED_PROCESSING_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingEndDate in
        defaultDocumentServiceStatusFiltering(
            "processingEndDate.in=" + DEFAULT_PROCESSING_END_DATE + "," + UPDATED_PROCESSING_END_DATE,
            "processingEndDate.in=" + UPDATED_PROCESSING_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingEndDate is not null
        defaultDocumentServiceStatusFiltering("processingEndDate.specified=true", "processingEndDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration equals to
        defaultDocumentServiceStatusFiltering(
            "processingDuration.equals=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.equals=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration in
        defaultDocumentServiceStatusFiltering(
            "processingDuration.in=" + DEFAULT_PROCESSING_DURATION + "," + UPDATED_PROCESSING_DURATION,
            "processingDuration.in=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration is not null
        defaultDocumentServiceStatusFiltering("processingDuration.specified=true", "processingDuration.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration is greater than or equal to
        defaultDocumentServiceStatusFiltering(
            "processingDuration.greaterThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.greaterThanOrEqual=" + UPDATED_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration is less than or equal to
        defaultDocumentServiceStatusFiltering(
            "processingDuration.lessThanOrEqual=" + DEFAULT_PROCESSING_DURATION,
            "processingDuration.lessThanOrEqual=" + SMALLER_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration is less than
        defaultDocumentServiceStatusFiltering(
            "processingDuration.lessThan=" + UPDATED_PROCESSING_DURATION,
            "processingDuration.lessThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByProcessingDurationIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where processingDuration is greater than
        defaultDocumentServiceStatusFiltering(
            "processingDuration.greaterThan=" + SMALLER_PROCESSING_DURATION,
            "processingDuration.greaterThan=" + DEFAULT_PROCESSING_DURATION
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByJobIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where jobId equals to
        defaultDocumentServiceStatusFiltering("jobId.equals=" + DEFAULT_JOB_ID, "jobId.equals=" + UPDATED_JOB_ID);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByJobIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where jobId in
        defaultDocumentServiceStatusFiltering("jobId.in=" + DEFAULT_JOB_ID + "," + UPDATED_JOB_ID, "jobId.in=" + UPDATED_JOB_ID);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByJobIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where jobId is not null
        defaultDocumentServiceStatusFiltering("jobId.specified=true", "jobId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByJobIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where jobId contains
        defaultDocumentServiceStatusFiltering("jobId.contains=" + DEFAULT_JOB_ID, "jobId.contains=" + UPDATED_JOB_ID);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByJobIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where jobId does not contain
        defaultDocumentServiceStatusFiltering("jobId.doesNotContain=" + UPDATED_JOB_ID, "jobId.doesNotContain=" + DEFAULT_JOB_ID);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority equals to
        defaultDocumentServiceStatusFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority in
        defaultDocumentServiceStatusFiltering(
            "priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY,
            "priority.in=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority is not null
        defaultDocumentServiceStatusFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority is greater than or equal to
        defaultDocumentServiceStatusFiltering(
            "priority.greaterThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.greaterThanOrEqual=" + UPDATED_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority is less than or equal to
        defaultDocumentServiceStatusFiltering(
            "priority.lessThanOrEqual=" + DEFAULT_PRIORITY,
            "priority.lessThanOrEqual=" + SMALLER_PRIORITY
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority is less than
        defaultDocumentServiceStatusFiltering("priority.lessThan=" + UPDATED_PRIORITY, "priority.lessThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByPriorityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where priority is greater than
        defaultDocumentServiceStatusFiltering("priority.greaterThan=" + SMALLER_PRIORITY, "priority.greaterThan=" + DEFAULT_PRIORITY);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedBy equals to
        defaultDocumentServiceStatusFiltering("updatedBy.equals=" + DEFAULT_UPDATED_BY, "updatedBy.equals=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedBy in
        defaultDocumentServiceStatusFiltering(
            "updatedBy.in=" + DEFAULT_UPDATED_BY + "," + UPDATED_UPDATED_BY,
            "updatedBy.in=" + UPDATED_UPDATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedBy is not null
        defaultDocumentServiceStatusFiltering("updatedBy.specified=true", "updatedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedBy contains
        defaultDocumentServiceStatusFiltering("updatedBy.contains=" + DEFAULT_UPDATED_BY, "updatedBy.contains=" + UPDATED_UPDATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedBy does not contain
        defaultDocumentServiceStatusFiltering(
            "updatedBy.doesNotContain=" + UPDATED_UPDATED_BY,
            "updatedBy.doesNotContain=" + DEFAULT_UPDATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedDate equals to
        defaultDocumentServiceStatusFiltering("updatedDate.equals=" + DEFAULT_UPDATED_DATE, "updatedDate.equals=" + UPDATED_UPDATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedDate in
        defaultDocumentServiceStatusFiltering(
            "updatedDate.in=" + DEFAULT_UPDATED_DATE + "," + UPDATED_UPDATED_DATE,
            "updatedDate.in=" + UPDATED_UPDATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByUpdatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        // Get all the documentServiceStatusList where updatedDate is not null
        defaultDocumentServiceStatusFiltering("updatedDate.specified=true", "updatedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentServiceStatusesByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentServiceStatusRepository.saveAndFlush(documentServiceStatus);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentServiceStatus.setDocument(document);
        documentServiceStatusRepository.saveAndFlush(documentServiceStatus);
        Long documentId = document.getId();
        // Get all the documentServiceStatusList where document equals to documentId
        defaultDocumentServiceStatusShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentServiceStatusList where document equals to (documentId + 1)
        defaultDocumentServiceStatusShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    private void defaultDocumentServiceStatusFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentServiceStatusShouldBeFound(shouldBeFound);
        defaultDocumentServiceStatusShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentServiceStatusShouldBeFound(String filter) throws Exception {
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentServiceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDetails").value(hasItem(DEFAULT_STATUS_DETAILS)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].lastProcessedDate").value(hasItem(DEFAULT_LAST_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingStartDate").value(hasItem(DEFAULT_PROCESSING_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingEndDate").value(hasItem(DEFAULT_PROCESSING_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentServiceStatusShouldNotBeFound(String filter) throws Exception {
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentServiceStatus() throws Exception {
        // Get the documentServiceStatus
        restDocumentServiceStatusMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentServiceStatus() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentServiceStatusSearchRepository.save(documentServiceStatus);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());

        // Update the documentServiceStatus
        DocumentServiceStatus updatedDocumentServiceStatus = documentServiceStatusRepository
            .findById(documentServiceStatus.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentServiceStatus are not directly saved in db
        em.detach(updatedDocumentServiceStatus);
        updatedDocumentServiceStatus
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .statusDetails(UPDATED_STATUS_DETAILS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .retryCount(UPDATED_RETRY_COUNT)
            .lastProcessedDate(UPDATED_LAST_PROCESSED_DATE)
            .processingStartDate(UPDATED_PROCESSING_START_DATE)
            .processingEndDate(UPDATED_PROCESSING_END_DATE)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .jobId(UPDATED_JOB_ID)
            .priority(UPDATED_PRIORITY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(updatedDocumentServiceStatus);

        restDocumentServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentServiceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentServiceStatusToMatchAllProperties(updatedDocumentServiceStatus);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentServiceStatus> documentServiceStatusSearchList = Streamable.of(
                    documentServiceStatusSearchRepository.findAll()
                ).toList();
                DocumentServiceStatus testDocumentServiceStatusSearch = documentServiceStatusSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentServiceStatusAllPropertiesEquals(testDocumentServiceStatusSearch, updatedDocumentServiceStatus);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentServiceStatusDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentServiceStatusDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentServiceStatus using partial update
        DocumentServiceStatus partialUpdatedDocumentServiceStatus = new DocumentServiceStatus();
        partialUpdatedDocumentServiceStatus.setId(documentServiceStatus.getId());

        partialUpdatedDocumentServiceStatus
            .retryCount(UPDATED_RETRY_COUNT)
            .priority(UPDATED_PRIORITY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restDocumentServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the DocumentServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentServiceStatusUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentServiceStatus, documentServiceStatus),
            getPersistedDocumentServiceStatus(documentServiceStatus)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentServiceStatusWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentServiceStatus using partial update
        DocumentServiceStatus partialUpdatedDocumentServiceStatus = new DocumentServiceStatus();
        partialUpdatedDocumentServiceStatus.setId(documentServiceStatus.getId());

        partialUpdatedDocumentServiceStatus
            .serviceType(UPDATED_SERVICE_TYPE)
            .status(UPDATED_STATUS)
            .statusDetails(UPDATED_STATUS_DETAILS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .retryCount(UPDATED_RETRY_COUNT)
            .lastProcessedDate(UPDATED_LAST_PROCESSED_DATE)
            .processingStartDate(UPDATED_PROCESSING_START_DATE)
            .processingEndDate(UPDATED_PROCESSING_END_DATE)
            .processingDuration(UPDATED_PROCESSING_DURATION)
            .jobId(UPDATED_JOB_ID)
            .priority(UPDATED_PRIORITY)
            .updatedBy(UPDATED_UPDATED_BY)
            .updatedDate(UPDATED_UPDATED_DATE);

        restDocumentServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentServiceStatus.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentServiceStatus))
            )
            .andExpect(status().isOk());

        // Validate the DocumentServiceStatus in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentServiceStatusUpdatableFieldsEquals(
            partialUpdatedDocumentServiceStatus,
            getPersistedDocumentServiceStatus(partialUpdatedDocumentServiceStatus)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentServiceStatusDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentServiceStatus() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        documentServiceStatus.setId(longCount.incrementAndGet());

        // Create the DocumentServiceStatus
        DocumentServiceStatusDTO documentServiceStatusDTO = documentServiceStatusMapper.toDto(documentServiceStatus);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentServiceStatusMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentServiceStatusDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentServiceStatus in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentServiceStatus() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);
        documentServiceStatusRepository.save(documentServiceStatus);
        documentServiceStatusSearchRepository.save(documentServiceStatus);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentServiceStatus
        restDocumentServiceStatusMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentServiceStatus.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentServiceStatusSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentServiceStatus() throws Exception {
        // Initialize the database
        insertedDocumentServiceStatus = documentServiceStatusRepository.saveAndFlush(documentServiceStatus);
        documentServiceStatusSearchRepository.save(documentServiceStatus);

        // Search the documentServiceStatus
        restDocumentServiceStatusMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentServiceStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentServiceStatus.getId().intValue())))
            .andExpect(jsonPath("$.[*].serviceType").value(hasItem(DEFAULT_SERVICE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].statusDetails").value(hasItem(DEFAULT_STATUS_DETAILS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE.toString())))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].lastProcessedDate").value(hasItem(DEFAULT_LAST_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingStartDate").value(hasItem(DEFAULT_PROCESSING_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingEndDate").value(hasItem(DEFAULT_PROCESSING_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].processingDuration").value(hasItem(DEFAULT_PROCESSING_DURATION.intValue())))
            .andExpect(jsonPath("$.[*].jobId").value(hasItem(DEFAULT_JOB_ID)))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY)))
            .andExpect(jsonPath("$.[*].updatedBy").value(hasItem(DEFAULT_UPDATED_BY)))
            .andExpect(jsonPath("$.[*].updatedDate").value(hasItem(DEFAULT_UPDATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentServiceStatusRepository.count();
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

    protected DocumentServiceStatus getPersistedDocumentServiceStatus(DocumentServiceStatus documentServiceStatus) {
        return documentServiceStatusRepository.findById(documentServiceStatus.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentServiceStatusToMatchAllProperties(DocumentServiceStatus expectedDocumentServiceStatus) {
        assertDocumentServiceStatusAllPropertiesEquals(
            expectedDocumentServiceStatus,
            getPersistedDocumentServiceStatus(expectedDocumentServiceStatus)
        );
    }

    protected void assertPersistedDocumentServiceStatusToMatchUpdatableProperties(DocumentServiceStatus expectedDocumentServiceStatus) {
        assertDocumentServiceStatusAllUpdatablePropertiesEquals(
            expectedDocumentServiceStatus,
            getPersistedDocumentServiceStatus(expectedDocumentServiceStatus)
        );
    }
}
