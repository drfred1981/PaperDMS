package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscriptionAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.NotificationWebhookSubscription;
import fr.smartprod.paperdms.notification.repository.NotificationWebhookSubscriptionRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationWebhookSubscriptionDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationWebhookSubscriptionMapper;
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
 * Integration tests for the {@link NotificationWebhookSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationWebhookSubscriptionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_SECRET = "AAAAAAAAAA";
    private static final String UPDATED_SECRET = "BBBBBBBBBB";

    private static final String DEFAULT_EVENTS = "AAAAAAAAAA";
    private static final String UPDATED_EVENTS = "BBBBBBBBBB";

    private static final String DEFAULT_HEADERS = "AAAAAAAAAA";
    private static final String UPDATED_HEADERS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Integer DEFAULT_RETRY_COUNT = 1;
    private static final Integer UPDATED_RETRY_COUNT = 2;
    private static final Integer SMALLER_RETRY_COUNT = 1 - 1;

    private static final Integer DEFAULT_MAX_RETRIES = 1;
    private static final Integer UPDATED_MAX_RETRIES = 2;
    private static final Integer SMALLER_MAX_RETRIES = 1 - 1;

    private static final Integer DEFAULT_RETRY_DELAY = 1;
    private static final Integer UPDATED_RETRY_DELAY = 2;
    private static final Integer SMALLER_RETRY_DELAY = 1 - 1;

    private static final Instant DEFAULT_LAST_TRIGGER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_TRIGGER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_SUCCESS_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_SUCCESS_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_LAST_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_FAILURE_COUNT = 1;
    private static final Integer UPDATED_FAILURE_COUNT = 2;
    private static final Integer SMALLER_FAILURE_COUNT = 1 - 1;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-webhook-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationWebhookSubscriptionRepository notificationWebhookSubscriptionRepository;

    @Autowired
    private NotificationWebhookSubscriptionMapper notificationWebhookSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationWebhookSubscriptionMockMvc;

    private NotificationWebhookSubscription notificationWebhookSubscription;

    private NotificationWebhookSubscription insertedNotificationWebhookSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationWebhookSubscription createEntity() {
        return new NotificationWebhookSubscription()
            .name(DEFAULT_NAME)
            .url(DEFAULT_URL)
            .secret(DEFAULT_SECRET)
            .events(DEFAULT_EVENTS)
            .headers(DEFAULT_HEADERS)
            .isActive(DEFAULT_IS_ACTIVE)
            .retryCount(DEFAULT_RETRY_COUNT)
            .maxRetries(DEFAULT_MAX_RETRIES)
            .retryDelay(DEFAULT_RETRY_DELAY)
            .lastTriggerDate(DEFAULT_LAST_TRIGGER_DATE)
            .lastSuccessDate(DEFAULT_LAST_SUCCESS_DATE)
            .lastErrorMessage(DEFAULT_LAST_ERROR_MESSAGE)
            .failureCount(DEFAULT_FAILURE_COUNT)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationWebhookSubscription createUpdatedEntity() {
        return new NotificationWebhookSubscription()
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .secret(UPDATED_SECRET)
            .events(UPDATED_EVENTS)
            .headers(UPDATED_HEADERS)
            .isActive(UPDATED_IS_ACTIVE)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .retryDelay(UPDATED_RETRY_DELAY)
            .lastTriggerDate(UPDATED_LAST_TRIGGER_DATE)
            .lastSuccessDate(UPDATED_LAST_SUCCESS_DATE)
            .lastErrorMessage(UPDATED_LAST_ERROR_MESSAGE)
            .failureCount(UPDATED_FAILURE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        notificationWebhookSubscription = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationWebhookSubscription != null) {
            notificationWebhookSubscriptionRepository.delete(insertedNotificationWebhookSubscription);
            insertedNotificationWebhookSubscription = null;
        }
    }

    @Test
    @Transactional
    void createNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );
        var returnedNotificationWebhookSubscriptionDTO = om.readValue(
            restNotificationWebhookSubscriptionMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationWebhookSubscriptionDTO.class
        );

        // Validate the NotificationWebhookSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationWebhookSubscription = notificationWebhookSubscriptionMapper.toEntity(
            returnedNotificationWebhookSubscriptionDTO
        );
        assertNotificationWebhookSubscriptionUpdatableFieldsEquals(
            returnedNotificationWebhookSubscription,
            getPersistedNotificationWebhookSubscription(returnedNotificationWebhookSubscription)
        );

        insertedNotificationWebhookSubscription = returnedNotificationWebhookSubscription;
    }

    @Test
    @Transactional
    void createNotificationWebhookSubscriptionWithExistingId() throws Exception {
        // Create the NotificationWebhookSubscription with an existing ID
        notificationWebhookSubscription.setId(1L);
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookSubscription.setName(null);

        // Create the NotificationWebhookSubscription, which fails.
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookSubscription.setUrl(null);

        // Create the NotificationWebhookSubscription, which fails.
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookSubscription.setIsActive(null);

        // Create the NotificationWebhookSubscription, which fails.
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookSubscription.setCreatedBy(null);

        // Create the NotificationWebhookSubscription, which fails.
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationWebhookSubscription.setCreatedDate(null);

        // Create the NotificationWebhookSubscription, which fails.
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptions() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationWebhookSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].secret").value(hasItem(DEFAULT_SECRET)))
            .andExpect(jsonPath("$.[*].events").value(hasItem(DEFAULT_EVENTS)))
            .andExpect(jsonPath("$.[*].headers").value(hasItem(DEFAULT_HEADERS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].maxRetries").value(hasItem(DEFAULT_MAX_RETRIES)))
            .andExpect(jsonPath("$.[*].retryDelay").value(hasItem(DEFAULT_RETRY_DELAY)))
            .andExpect(jsonPath("$.[*].lastTriggerDate").value(hasItem(DEFAULT_LAST_TRIGGER_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastSuccessDate").value(hasItem(DEFAULT_LAST_SUCCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastErrorMessage").value(hasItem(DEFAULT_LAST_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].failureCount").value(hasItem(DEFAULT_FAILURE_COUNT)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotificationWebhookSubscription() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get the notificationWebhookSubscription
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationWebhookSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationWebhookSubscription.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.secret").value(DEFAULT_SECRET))
            .andExpect(jsonPath("$.events").value(DEFAULT_EVENTS))
            .andExpect(jsonPath("$.headers").value(DEFAULT_HEADERS))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.retryCount").value(DEFAULT_RETRY_COUNT))
            .andExpect(jsonPath("$.maxRetries").value(DEFAULT_MAX_RETRIES))
            .andExpect(jsonPath("$.retryDelay").value(DEFAULT_RETRY_DELAY))
            .andExpect(jsonPath("$.lastTriggerDate").value(DEFAULT_LAST_TRIGGER_DATE.toString()))
            .andExpect(jsonPath("$.lastSuccessDate").value(DEFAULT_LAST_SUCCESS_DATE.toString()))
            .andExpect(jsonPath("$.lastErrorMessage").value(DEFAULT_LAST_ERROR_MESSAGE))
            .andExpect(jsonPath("$.failureCount").value(DEFAULT_FAILURE_COUNT))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNotificationWebhookSubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        Long id = notificationWebhookSubscription.getId();

        defaultNotificationWebhookSubscriptionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationWebhookSubscriptionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationWebhookSubscriptionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where name equals to
        defaultNotificationWebhookSubscriptionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where name in
        defaultNotificationWebhookSubscriptionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where name is not null
        defaultNotificationWebhookSubscriptionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where name contains
        defaultNotificationWebhookSubscriptionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where name does not contain
        defaultNotificationWebhookSubscriptionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where url equals to
        defaultNotificationWebhookSubscriptionFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where url in
        defaultNotificationWebhookSubscriptionFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where url is not null
        defaultNotificationWebhookSubscriptionFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where url contains
        defaultNotificationWebhookSubscriptionFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where url does not contain
        defaultNotificationWebhookSubscriptionFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsBySecretIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where secret equals to
        defaultNotificationWebhookSubscriptionFiltering("secret.equals=" + DEFAULT_SECRET, "secret.equals=" + UPDATED_SECRET);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsBySecretIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where secret in
        defaultNotificationWebhookSubscriptionFiltering(
            "secret.in=" + DEFAULT_SECRET + "," + UPDATED_SECRET,
            "secret.in=" + UPDATED_SECRET
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsBySecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where secret is not null
        defaultNotificationWebhookSubscriptionFiltering("secret.specified=true", "secret.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsBySecretContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where secret contains
        defaultNotificationWebhookSubscriptionFiltering("secret.contains=" + DEFAULT_SECRET, "secret.contains=" + UPDATED_SECRET);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsBySecretNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where secret does not contain
        defaultNotificationWebhookSubscriptionFiltering(
            "secret.doesNotContain=" + UPDATED_SECRET,
            "secret.doesNotContain=" + DEFAULT_SECRET
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where isActive equals to
        defaultNotificationWebhookSubscriptionFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where isActive in
        defaultNotificationWebhookSubscriptionFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where isActive is not null
        defaultNotificationWebhookSubscriptionFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.equals=" + DEFAULT_RETRY_COUNT,
            "retryCount.equals=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount in
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.in=" + DEFAULT_RETRY_COUNT + "," + UPDATED_RETRY_COUNT,
            "retryCount.in=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount is not null
        defaultNotificationWebhookSubscriptionFiltering("retryCount.specified=true", "retryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount is greater than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.greaterThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.greaterThanOrEqual=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount is less than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.lessThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.lessThanOrEqual=" + SMALLER_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount is less than
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.lessThan=" + UPDATED_RETRY_COUNT,
            "retryCount.lessThan=" + DEFAULT_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryCount is greater than
        defaultNotificationWebhookSubscriptionFiltering(
            "retryCount.greaterThan=" + SMALLER_RETRY_COUNT,
            "retryCount.greaterThan=" + DEFAULT_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.equals=" + DEFAULT_MAX_RETRIES,
            "maxRetries.equals=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries in
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.in=" + DEFAULT_MAX_RETRIES + "," + UPDATED_MAX_RETRIES,
            "maxRetries.in=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries is not null
        defaultNotificationWebhookSubscriptionFiltering("maxRetries.specified=true", "maxRetries.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries is greater than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.greaterThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.greaterThanOrEqual=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries is less than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.lessThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.lessThanOrEqual=" + SMALLER_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries is less than
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.lessThan=" + UPDATED_MAX_RETRIES,
            "maxRetries.lessThan=" + DEFAULT_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByMaxRetriesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where maxRetries is greater than
        defaultNotificationWebhookSubscriptionFiltering(
            "maxRetries.greaterThan=" + SMALLER_MAX_RETRIES,
            "maxRetries.greaterThan=" + DEFAULT_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.equals=" + DEFAULT_RETRY_DELAY,
            "retryDelay.equals=" + UPDATED_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay in
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.in=" + DEFAULT_RETRY_DELAY + "," + UPDATED_RETRY_DELAY,
            "retryDelay.in=" + UPDATED_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay is not null
        defaultNotificationWebhookSubscriptionFiltering("retryDelay.specified=true", "retryDelay.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay is greater than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.greaterThanOrEqual=" + DEFAULT_RETRY_DELAY,
            "retryDelay.greaterThanOrEqual=" + UPDATED_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay is less than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.lessThanOrEqual=" + DEFAULT_RETRY_DELAY,
            "retryDelay.lessThanOrEqual=" + SMALLER_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay is less than
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.lessThan=" + UPDATED_RETRY_DELAY,
            "retryDelay.lessThan=" + DEFAULT_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByRetryDelayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where retryDelay is greater than
        defaultNotificationWebhookSubscriptionFiltering(
            "retryDelay.greaterThan=" + SMALLER_RETRY_DELAY,
            "retryDelay.greaterThan=" + DEFAULT_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastTriggerDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastTriggerDate equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "lastTriggerDate.equals=" + DEFAULT_LAST_TRIGGER_DATE,
            "lastTriggerDate.equals=" + UPDATED_LAST_TRIGGER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastTriggerDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastTriggerDate in
        defaultNotificationWebhookSubscriptionFiltering(
            "lastTriggerDate.in=" + DEFAULT_LAST_TRIGGER_DATE + "," + UPDATED_LAST_TRIGGER_DATE,
            "lastTriggerDate.in=" + UPDATED_LAST_TRIGGER_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastTriggerDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastTriggerDate is not null
        defaultNotificationWebhookSubscriptionFiltering("lastTriggerDate.specified=true", "lastTriggerDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastSuccessDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastSuccessDate equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "lastSuccessDate.equals=" + DEFAULT_LAST_SUCCESS_DATE,
            "lastSuccessDate.equals=" + UPDATED_LAST_SUCCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastSuccessDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastSuccessDate in
        defaultNotificationWebhookSubscriptionFiltering(
            "lastSuccessDate.in=" + DEFAULT_LAST_SUCCESS_DATE + "," + UPDATED_LAST_SUCCESS_DATE,
            "lastSuccessDate.in=" + UPDATED_LAST_SUCCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastSuccessDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastSuccessDate is not null
        defaultNotificationWebhookSubscriptionFiltering("lastSuccessDate.specified=true", "lastSuccessDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.equals=" + DEFAULT_FAILURE_COUNT,
            "failureCount.equals=" + UPDATED_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount in
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.in=" + DEFAULT_FAILURE_COUNT + "," + UPDATED_FAILURE_COUNT,
            "failureCount.in=" + UPDATED_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount is not null
        defaultNotificationWebhookSubscriptionFiltering("failureCount.specified=true", "failureCount.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount is greater than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.greaterThanOrEqual=" + DEFAULT_FAILURE_COUNT,
            "failureCount.greaterThanOrEqual=" + UPDATED_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount is less than or equal to
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.lessThanOrEqual=" + DEFAULT_FAILURE_COUNT,
            "failureCount.lessThanOrEqual=" + SMALLER_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount is less than
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.lessThan=" + UPDATED_FAILURE_COUNT,
            "failureCount.lessThan=" + DEFAULT_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByFailureCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where failureCount is greater than
        defaultNotificationWebhookSubscriptionFiltering(
            "failureCount.greaterThan=" + SMALLER_FAILURE_COUNT,
            "failureCount.greaterThan=" + DEFAULT_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdBy equals to
        defaultNotificationWebhookSubscriptionFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdBy in
        defaultNotificationWebhookSubscriptionFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdBy is not null
        defaultNotificationWebhookSubscriptionFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdBy contains
        defaultNotificationWebhookSubscriptionFiltering(
            "createdBy.contains=" + DEFAULT_CREATED_BY,
            "createdBy.contains=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdBy does not contain
        defaultNotificationWebhookSubscriptionFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdDate equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "createdDate.equals=" + DEFAULT_CREATED_DATE,
            "createdDate.equals=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdDate in
        defaultNotificationWebhookSubscriptionFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where createdDate is not null
        defaultNotificationWebhookSubscriptionFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastModifiedDate equals to
        defaultNotificationWebhookSubscriptionFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastModifiedDate in
        defaultNotificationWebhookSubscriptionFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationWebhookSubscriptionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        // Get all the notificationWebhookSubscriptionList where lastModifiedDate is not null
        defaultNotificationWebhookSubscriptionFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultNotificationWebhookSubscriptionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationWebhookSubscriptionShouldBeFound(shouldBeFound);
        defaultNotificationWebhookSubscriptionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationWebhookSubscriptionShouldBeFound(String filter) throws Exception {
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationWebhookSubscription.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].secret").value(hasItem(DEFAULT_SECRET)))
            .andExpect(jsonPath("$.[*].events").value(hasItem(DEFAULT_EVENTS)))
            .andExpect(jsonPath("$.[*].headers").value(hasItem(DEFAULT_HEADERS)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].retryCount").value(hasItem(DEFAULT_RETRY_COUNT)))
            .andExpect(jsonPath("$.[*].maxRetries").value(hasItem(DEFAULT_MAX_RETRIES)))
            .andExpect(jsonPath("$.[*].retryDelay").value(hasItem(DEFAULT_RETRY_DELAY)))
            .andExpect(jsonPath("$.[*].lastTriggerDate").value(hasItem(DEFAULT_LAST_TRIGGER_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastSuccessDate").value(hasItem(DEFAULT_LAST_SUCCESS_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastErrorMessage").value(hasItem(DEFAULT_LAST_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].failureCount").value(hasItem(DEFAULT_FAILURE_COUNT)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationWebhookSubscriptionShouldNotBeFound(String filter) throws Exception {
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationWebhookSubscription() throws Exception {
        // Get the notificationWebhookSubscription
        restNotificationWebhookSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationWebhookSubscription() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookSubscription
        NotificationWebhookSubscription updatedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository
            .findById(notificationWebhookSubscription.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationWebhookSubscription are not directly saved in db
        em.detach(updatedNotificationWebhookSubscription);
        updatedNotificationWebhookSubscription
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .secret(UPDATED_SECRET)
            .events(UPDATED_EVENTS)
            .headers(UPDATED_HEADERS)
            .isActive(UPDATED_IS_ACTIVE)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .retryDelay(UPDATED_RETRY_DELAY)
            .lastTriggerDate(UPDATED_LAST_TRIGGER_DATE)
            .lastSuccessDate(UPDATED_LAST_SUCCESS_DATE)
            .lastErrorMessage(UPDATED_LAST_ERROR_MESSAGE)
            .failureCount(UPDATED_FAILURE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            updatedNotificationWebhookSubscription
        );

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationWebhookSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationWebhookSubscriptionToMatchAllProperties(updatedNotificationWebhookSubscription);
    }

    @Test
    @Transactional
    void putNonExistingNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationWebhookSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWebhookSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookSubscription using partial update
        NotificationWebhookSubscription partialUpdatedNotificationWebhookSubscription = new NotificationWebhookSubscription();
        partialUpdatedNotificationWebhookSubscription.setId(notificationWebhookSubscription.getId());

        partialUpdatedNotificationWebhookSubscription
            .url(UPDATED_URL)
            .secret(UPDATED_SECRET)
            .headers(UPDATED_HEADERS)
            .isActive(UPDATED_IS_ACTIVE)
            .retryCount(UPDATED_RETRY_COUNT)
            .lastSuccessDate(UPDATED_LAST_SUCCESS_DATE)
            .lastErrorMessage(UPDATED_LAST_ERROR_MESSAGE)
            .failureCount(UPDATED_FAILURE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationWebhookSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationWebhookSubscription))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationWebhookSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationWebhookSubscription, notificationWebhookSubscription),
            getPersistedNotificationWebhookSubscription(notificationWebhookSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationWebhookSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationWebhookSubscription using partial update
        NotificationWebhookSubscription partialUpdatedNotificationWebhookSubscription = new NotificationWebhookSubscription();
        partialUpdatedNotificationWebhookSubscription.setId(notificationWebhookSubscription.getId());

        partialUpdatedNotificationWebhookSubscription
            .name(UPDATED_NAME)
            .url(UPDATED_URL)
            .secret(UPDATED_SECRET)
            .events(UPDATED_EVENTS)
            .headers(UPDATED_HEADERS)
            .isActive(UPDATED_IS_ACTIVE)
            .retryCount(UPDATED_RETRY_COUNT)
            .maxRetries(UPDATED_MAX_RETRIES)
            .retryDelay(UPDATED_RETRY_DELAY)
            .lastTriggerDate(UPDATED_LAST_TRIGGER_DATE)
            .lastSuccessDate(UPDATED_LAST_SUCCESS_DATE)
            .lastErrorMessage(UPDATED_LAST_ERROR_MESSAGE)
            .failureCount(UPDATED_FAILURE_COUNT)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restNotificationWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationWebhookSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationWebhookSubscription))
            )
            .andExpect(status().isOk());

        // Validate the NotificationWebhookSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationWebhookSubscriptionUpdatableFieldsEquals(
            partialUpdatedNotificationWebhookSubscription,
            getPersistedNotificationWebhookSubscription(partialUpdatedNotificationWebhookSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationWebhookSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationWebhookSubscription.setId(longCount.incrementAndGet());

        // Create the NotificationWebhookSubscription
        NotificationWebhookSubscriptionDTO notificationWebhookSubscriptionDTO = notificationWebhookSubscriptionMapper.toDto(
            notificationWebhookSubscription
        );

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationWebhookSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationWebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationWebhookSubscription() throws Exception {
        // Initialize the database
        insertedNotificationWebhookSubscription = notificationWebhookSubscriptionRepository.saveAndFlush(notificationWebhookSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationWebhookSubscription
        restNotificationWebhookSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationWebhookSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationWebhookSubscriptionRepository.count();
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

    protected NotificationWebhookSubscription getPersistedNotificationWebhookSubscription(
        NotificationWebhookSubscription notificationWebhookSubscription
    ) {
        return notificationWebhookSubscriptionRepository.findById(notificationWebhookSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationWebhookSubscriptionToMatchAllProperties(
        NotificationWebhookSubscription expectedNotificationWebhookSubscription
    ) {
        assertNotificationWebhookSubscriptionAllPropertiesEquals(
            expectedNotificationWebhookSubscription,
            getPersistedNotificationWebhookSubscription(expectedNotificationWebhookSubscription)
        );
    }

    protected void assertPersistedNotificationWebhookSubscriptionToMatchUpdatableProperties(
        NotificationWebhookSubscription expectedNotificationWebhookSubscription
    ) {
        assertNotificationWebhookSubscriptionAllUpdatablePropertiesEquals(
            expectedNotificationWebhookSubscription,
            getPersistedNotificationWebhookSubscription(expectedNotificationWebhookSubscription)
        );
    }
}
