package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookLogAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.NotificationWebhookLog;
import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.repository.NotificationWebhookLogRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookLogDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationWebhookLogMapper;
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
 * Integration tests for the {@link NotificationWebhookLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationWebhookLogResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESPONSE_STATUS = 1;
    private static final Integer UPDATED_RESPONSE_STATUS = 2;
    private static final Integer SMALLER_RESPONSE_STATUS = 1 - 1;

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Long DEFAULT_RESPONSE_TIME = 1L;
    private static final Long UPDATED_RESPONSE_TIME = 2L;
    private static final Long SMALLER_RESPONSE_TIME = 1L - 1L;

    private static final Integer DEFAULT_ATTEMPT_NUMBER = 1;
    private static final Integer UPDATED_ATTEMPT_NUMBER = 2;
    private static final Integer SMALLER_ATTEMPT_NUMBER = 1 - 1;

    private static final Boolean DEFAULT_IS_SUCCESS = false;
    private static final Boolean UPDATED_IS_SUCCESS = true;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-webhook-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationWebhookLogRepository notificationWebhookLogRepository;

    @Autowired
    private NotificationWebhookLogMapper notificationWebhookLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationWebhookLogMockMvc;

    private NotificationWebhookLog notificationWebhookLog;

    private NotificationWebhookLog insertedNotificationWebhookLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationWebhookLog createEntity() {
        return new NotificationWebhookLog()
            .eventType(DEFAULT_EVENT_TYPE)
            .payload(DEFAULT_PAYLOAD)
            .responseStatus(DEFAULT_RESPONSE_STATUS)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .responseTime(DEFAULT_RESPONSE_TIME)
            .attemptNumber(DEFAULT_ATTEMPT_NUMBER)
            .isSuccess(DEFAULT_IS_SUCCESS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .sentDate(DEFAULT_SENT_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationWebhookLog createUpdatedEntity() {
        return new NotificationWebhookLog()
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);
    }

    @BeforeEach
    void initTest() {
        notificationWebhookLog = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationWebhookLog != null) {
            notificationWebhookLogRepository.delete(insertedNotificationWebhookLog);
            insertedNotificationWebhookLog = null;
        }
    }

    @Test
    @Transactional
    void createNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);
        var returnedNotificationWebhookLogDTO = om.readValue(
            restNotificationWebhookLogMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationWebhookLogDTO.class
        );

        // Validate the NotificationWebhookLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationWebhookLog = notificationWebhookLogMapper.toEntity(returnedNotificationWebhookLogDTO);
        assertNotificationWebhookLogUpdatableFieldsEquals(
            returnedNotificationWebhookLog,
            getPersistedNotificationWebhookLog(returnedNotificationWebhookLog)
        );

        insertedNotificationWebhookLog = returnedNotificationWebhookLog;
    }

    @Test
    @Transactional
    void createNotificationWebhookLogWithExistingId() throws Exception {
        // Create the NotificationWebhookLog with an existing ID
        notificationWebhookLog.setId(1L);
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookLog.setEventType(null);

        // Create the NotificationWebhookLog, which fails.
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        restNotificationWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSuccessIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookLog.setIsSuccess(null);

        // Create the NotificationWebhookLog, which fails.
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        restNotificationWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookLog.setSentDate(null);

        // Create the NotificationWebhookLog, which fails.
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        restNotificationWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogs() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationWebhookLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].responseStatus").value(hasItem(DEFAULT_RESPONSE_STATUS)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].attemptNumber").value(hasItem(DEFAULT_ATTEMPT_NUMBER)))
            .andExpect(jsonPath("$.[*].isSuccess").value(hasItem(DEFAULT_IS_SUCCESS)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotificationWebhookLog() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get the notificationWebhookLog
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationWebhookLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationWebhookLog.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.payload").value(DEFAULT_PAYLOAD))
            .andExpect(jsonPath("$.responseStatus").value(DEFAULT_RESPONSE_STATUS))
            .andExpect(jsonPath("$.responseBody").value(DEFAULT_RESPONSE_BODY))
            .andExpect(jsonPath("$.responseTime").value(DEFAULT_RESPONSE_TIME.intValue()))
            .andExpect(jsonPath("$.attemptNumber").value(DEFAULT_ATTEMPT_NUMBER))
            .andExpect(jsonPath("$.isSuccess").value(DEFAULT_IS_SUCCESS))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.sentDate").value(DEFAULT_SENT_DATE.toString()));
    }

    @Test
    @Transactional
    void getNotificationWebhookLogsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        Long id = notificationWebhookLog.getId();

        defaultNotificationWebhookLogFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationWebhookLogFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationWebhookLogFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where eventType equals to
        defaultNotificationWebhookLogFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where eventType in
        defaultNotificationWebhookLogFiltering(
            "eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE,
            "eventType.in=" + UPDATED_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where eventType is not null
        defaultNotificationWebhookLogFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where eventType contains
        defaultNotificationWebhookLogFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where eventType does not contain
        defaultNotificationWebhookLogFiltering(
            "eventType.doesNotContain=" + UPDATED_EVENT_TYPE,
            "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus equals to
        defaultNotificationWebhookLogFiltering(
            "responseStatus.equals=" + DEFAULT_RESPONSE_STATUS,
            "responseStatus.equals=" + UPDATED_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus in
        defaultNotificationWebhookLogFiltering(
            "responseStatus.in=" + DEFAULT_RESPONSE_STATUS + "," + UPDATED_RESPONSE_STATUS,
            "responseStatus.in=" + UPDATED_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus is not null
        defaultNotificationWebhookLogFiltering("responseStatus.specified=true", "responseStatus.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus is greater than or equal to
        defaultNotificationWebhookLogFiltering(
            "responseStatus.greaterThanOrEqual=" + DEFAULT_RESPONSE_STATUS,
            "responseStatus.greaterThanOrEqual=" + UPDATED_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus is less than or equal to
        defaultNotificationWebhookLogFiltering(
            "responseStatus.lessThanOrEqual=" + DEFAULT_RESPONSE_STATUS,
            "responseStatus.lessThanOrEqual=" + SMALLER_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus is less than
        defaultNotificationWebhookLogFiltering(
            "responseStatus.lessThan=" + UPDATED_RESPONSE_STATUS,
            "responseStatus.lessThan=" + DEFAULT_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseStatusIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseStatus is greater than
        defaultNotificationWebhookLogFiltering(
            "responseStatus.greaterThan=" + SMALLER_RESPONSE_STATUS,
            "responseStatus.greaterThan=" + DEFAULT_RESPONSE_STATUS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime equals to
        defaultNotificationWebhookLogFiltering(
            "responseTime.equals=" + DEFAULT_RESPONSE_TIME,
            "responseTime.equals=" + UPDATED_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime in
        defaultNotificationWebhookLogFiltering(
            "responseTime.in=" + DEFAULT_RESPONSE_TIME + "," + UPDATED_RESPONSE_TIME,
            "responseTime.in=" + UPDATED_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime is not null
        defaultNotificationWebhookLogFiltering("responseTime.specified=true", "responseTime.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime is greater than or equal to
        defaultNotificationWebhookLogFiltering(
            "responseTime.greaterThanOrEqual=" + DEFAULT_RESPONSE_TIME,
            "responseTime.greaterThanOrEqual=" + UPDATED_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime is less than or equal to
        defaultNotificationWebhookLogFiltering(
            "responseTime.lessThanOrEqual=" + DEFAULT_RESPONSE_TIME,
            "responseTime.lessThanOrEqual=" + SMALLER_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime is less than
        defaultNotificationWebhookLogFiltering(
            "responseTime.lessThan=" + UPDATED_RESPONSE_TIME,
            "responseTime.lessThan=" + DEFAULT_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByResponseTimeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where responseTime is greater than
        defaultNotificationWebhookLogFiltering(
            "responseTime.greaterThan=" + SMALLER_RESPONSE_TIME,
            "responseTime.greaterThan=" + DEFAULT_RESPONSE_TIME
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber equals to
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.equals=" + DEFAULT_ATTEMPT_NUMBER,
            "attemptNumber.equals=" + UPDATED_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber in
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.in=" + DEFAULT_ATTEMPT_NUMBER + "," + UPDATED_ATTEMPT_NUMBER,
            "attemptNumber.in=" + UPDATED_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber is not null
        defaultNotificationWebhookLogFiltering("attemptNumber.specified=true", "attemptNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber is greater than or equal to
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.greaterThanOrEqual=" + DEFAULT_ATTEMPT_NUMBER,
            "attemptNumber.greaterThanOrEqual=" + UPDATED_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber is less than or equal to
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.lessThanOrEqual=" + DEFAULT_ATTEMPT_NUMBER,
            "attemptNumber.lessThanOrEqual=" + SMALLER_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber is less than
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.lessThan=" + UPDATED_ATTEMPT_NUMBER,
            "attemptNumber.lessThan=" + DEFAULT_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByAttemptNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where attemptNumber is greater than
        defaultNotificationWebhookLogFiltering(
            "attemptNumber.greaterThan=" + SMALLER_ATTEMPT_NUMBER,
            "attemptNumber.greaterThan=" + DEFAULT_ATTEMPT_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByIsSuccessIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where isSuccess equals to
        defaultNotificationWebhookLogFiltering("isSuccess.equals=" + DEFAULT_IS_SUCCESS, "isSuccess.equals=" + UPDATED_IS_SUCCESS);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByIsSuccessIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where isSuccess in
        defaultNotificationWebhookLogFiltering(
            "isSuccess.in=" + DEFAULT_IS_SUCCESS + "," + UPDATED_IS_SUCCESS,
            "isSuccess.in=" + UPDATED_IS_SUCCESS
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsByIsSuccessIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where isSuccess is not null
        defaultNotificationWebhookLogFiltering("isSuccess.specified=true", "isSuccess.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsBySentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where sentDate equals to
        defaultNotificationWebhookLogFiltering("sentDate.equals=" + DEFAULT_SENT_DATE, "sentDate.equals=" + UPDATED_SENT_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsBySentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where sentDate in
        defaultNotificationWebhookLogFiltering(
            "sentDate.in=" + DEFAULT_SENT_DATE + "," + UPDATED_SENT_DATE,
            "sentDate.in=" + UPDATED_SENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsBySentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        // Get all the notificationWebhookLogList where sentDate is not null
        defaultNotificationWebhookLogFiltering("sentDate.specified=true", "sentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookLogsBySubscriptionIsEqualToSomething() throws Exception {
        NotificationWebhookSubscription subscription;
        if (TestUtil.findAll(em, NotificationWebhookSubscription.class).isEmpty()) {
            notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);
            subscription = NotificationWebhookSubscriptionResourceIT.createEntity();
        } else {
            subscription = TestUtil.findAll(em, NotificationWebhookSubscription.class).get(0);
        }
        em.persist(subscription);
        em.flush();
        notificationWebhookLog.setSubscription(subscription);
        notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);
        Long subscriptionId = subscription.getId();
        // Get all the notificationWebhookLogList where subscription equals to subscriptionId
        defaultNotificationWebhookLogShouldBeFound("subscriptionId.equals=" + subscriptionId);

        // Get all the notificationWebhookLogList where subscription equals to (subscriptionId + 1)
        defaultNotificationWebhookLogShouldNotBeFound("subscriptionId.equals=" + (subscriptionId + 1));
    }

    private void defaultNotificationWebhookLogFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationWebhookLogShouldBeFound(shouldBeFound);
        defaultNotificationWebhookLogShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationWebhookLogShouldBeFound(String filter) throws Exception {
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationWebhookLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].payload").value(hasItem(DEFAULT_PAYLOAD)))
            .andExpect(jsonPath("$.[*].responseStatus").value(hasItem(DEFAULT_RESPONSE_STATUS)))
            .andExpect(jsonPath("$.[*].responseBody").value(hasItem(DEFAULT_RESPONSE_BODY)))
            .andExpect(jsonPath("$.[*].responseTime").value(hasItem(DEFAULT_RESPONSE_TIME.intValue())))
            .andExpect(jsonPath("$.[*].attemptNumber").value(hasItem(DEFAULT_ATTEMPT_NUMBER)))
            .andExpect(jsonPath("$.[*].isSuccess").value(hasItem(DEFAULT_IS_SUCCESS)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationWebhookLogShouldNotBeFound(String filter) throws Exception {
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationWebhookLog() throws Exception {
        // Get the notificationWebhookLog
        restNotificationWebhookLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationWebhookLog() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookLog
        NotificationWebhookLog updatedNotificationWebhookLog = notificationWebhookLogRepository
            .findById(notificationWebhookLog.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationWebhookLog are not directly saved in db
        em.detach(updatedNotificationWebhookLog);
        updatedNotificationWebhookLog
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(updatedNotificationWebhookLog);

        restNotificationWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationWebhookLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationWebhookLogToMatchAllProperties(updatedNotificationWebhookLog);
    }

    @Test
    @Transactional
    void putNonExistingNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationWebhookLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationWebhookLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWebhookLogWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookLog using partial update
        NotificationWebhookLog partialUpdatedNotificationWebhookLog = new NotificationWebhookLog();
        partialUpdatedNotificationWebhookLog.setId(notificationWebhookLog.getId());

        partialUpdatedNotificationWebhookLog
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .sentDate(UPDATED_SENT_DATE);

        restNotificationWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationWebhookLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationWebhookLog))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationWebhookLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationWebhookLog, notificationWebhookLog),
            getPersistedNotificationWebhookLog(notificationWebhookLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationWebhookLogWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookLog using partial update
        NotificationWebhookLog partialUpdatedNotificationWebhookLog = new NotificationWebhookLog();
        partialUpdatedNotificationWebhookLog.setId(notificationWebhookLog.getId());

        partialUpdatedNotificationWebhookLog
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);

        restNotificationWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationWebhookLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationWebhookLog))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationWebhookLogUpdatableFieldsEquals(
            partialUpdatedNotificationWebhookLog,
            getPersistedNotificationWebhookLog(partialUpdatedNotificationWebhookLog)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationWebhookLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookLog.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookLog
        NotificationWebhookLogDTO notificationWebhookLogDTO = notificationWebhookLogMapper.toDto(notificationWebhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationWebhookLogDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationWebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationWebhookLog() throws Exception {
        // Initialize the database
        insertedNotificationWebhookLog = notificationWebhookLogRepository.saveAndFlush(notificationWebhookLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationWebhookLog
        restNotificationWebhookLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationWebhookLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationWebhookLogRepository.count();
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

    protected NotificationWebhookLog getPersistedNotificationWebhookLog(NotificationWebhookLog notificationWebhookLog) {
        return notificationWebhookLogRepository.findById(notificationWebhookLog.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationWebhookLogToMatchAllProperties(NotificationWebhookLog expectedNotificationWebhookLog) {
        assertNotificationWebhookLogAllPropertiesEquals(
            expectedNotificationWebhookLog,
            getPersistedNotificationWebhookLog(expectedNotificationWebhookLog)
        );
    }

    protected void assertPersistedNotificationWebhookLogToMatchUpdatableProperties(NotificationWebhookLog expectedNotificationWebhookLog) {
        assertNotificationWebhookLogAllUpdatablePropertiesEquals(
            expectedNotificationWebhookLog,
            getPersistedNotificationWebhookLog(expectedNotificationWebhookLog)
        );
    }
}
