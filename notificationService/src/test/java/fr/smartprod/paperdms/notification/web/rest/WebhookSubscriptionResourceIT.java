package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.WebhookSubscriptionAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.WebhookSubscription;
import fr.smartprod.paperdms.notification.repository.WebhookSubscriptionRepository;
import fr.smartprod.paperdms.notification.service.dto.WebhookSubscriptionDTO;
import fr.smartprod.paperdms.notification.service.mapper.WebhookSubscriptionMapper;
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
 * Integration tests for the {@link WebhookSubscriptionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WebhookSubscriptionResourceIT {

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

    private static final String ENTITY_API_URL = "/api/webhook-subscriptions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebhookSubscriptionRepository webhookSubscriptionRepository;

    @Autowired
    private WebhookSubscriptionMapper webhookSubscriptionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebhookSubscriptionMockMvc;

    private WebhookSubscription webhookSubscription;

    private WebhookSubscription insertedWebhookSubscription;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebhookSubscription createEntity() {
        return new WebhookSubscription()
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
    public static WebhookSubscription createUpdatedEntity() {
        return new WebhookSubscription()
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
        webhookSubscription = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedWebhookSubscription != null) {
            webhookSubscriptionRepository.delete(insertedWebhookSubscription);
            insertedWebhookSubscription = null;
        }
    }

    @Test
    @Transactional
    void createWebhookSubscription() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);
        var returnedWebhookSubscriptionDTO = om.readValue(
            restWebhookSubscriptionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WebhookSubscriptionDTO.class
        );

        // Validate the WebhookSubscription in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWebhookSubscription = webhookSubscriptionMapper.toEntity(returnedWebhookSubscriptionDTO);
        assertWebhookSubscriptionUpdatableFieldsEquals(
            returnedWebhookSubscription,
            getPersistedWebhookSubscription(returnedWebhookSubscription)
        );

        insertedWebhookSubscription = returnedWebhookSubscription;
    }

    @Test
    @Transactional
    void createWebhookSubscriptionWithExistingId() throws Exception {
        // Create the WebhookSubscription with an existing ID
        webhookSubscription.setId(1L);
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookSubscription.setName(null);

        // Create the WebhookSubscription, which fails.
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookSubscription.setUrl(null);

        // Create the WebhookSubscription, which fails.
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookSubscription.setIsActive(null);

        // Create the WebhookSubscription, which fails.
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookSubscription.setCreatedBy(null);

        // Create the WebhookSubscription, which fails.
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookSubscription.setCreatedDate(null);

        // Create the WebhookSubscription, which fails.
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptions() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webhookSubscription.getId().intValue())))
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
    void getWebhookSubscription() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get the webhookSubscription
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL_ID, webhookSubscription.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webhookSubscription.getId().intValue()))
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
    void getWebhookSubscriptionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        Long id = webhookSubscription.getId();

        defaultWebhookSubscriptionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultWebhookSubscriptionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultWebhookSubscriptionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where name equals to
        defaultWebhookSubscriptionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where name in
        defaultWebhookSubscriptionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where name is not null
        defaultWebhookSubscriptionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where name contains
        defaultWebhookSubscriptionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where name does not contain
        defaultWebhookSubscriptionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where url equals to
        defaultWebhookSubscriptionFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where url in
        defaultWebhookSubscriptionFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where url is not null
        defaultWebhookSubscriptionFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where url contains
        defaultWebhookSubscriptionFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where url does not contain
        defaultWebhookSubscriptionFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsBySecretIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where secret equals to
        defaultWebhookSubscriptionFiltering("secret.equals=" + DEFAULT_SECRET, "secret.equals=" + UPDATED_SECRET);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsBySecretIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where secret in
        defaultWebhookSubscriptionFiltering("secret.in=" + DEFAULT_SECRET + "," + UPDATED_SECRET, "secret.in=" + UPDATED_SECRET);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsBySecretIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where secret is not null
        defaultWebhookSubscriptionFiltering("secret.specified=true", "secret.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsBySecretContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where secret contains
        defaultWebhookSubscriptionFiltering("secret.contains=" + DEFAULT_SECRET, "secret.contains=" + UPDATED_SECRET);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsBySecretNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where secret does not contain
        defaultWebhookSubscriptionFiltering("secret.doesNotContain=" + UPDATED_SECRET, "secret.doesNotContain=" + DEFAULT_SECRET);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where isActive equals to
        defaultWebhookSubscriptionFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where isActive in
        defaultWebhookSubscriptionFiltering(
            "isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE,
            "isActive.in=" + UPDATED_IS_ACTIVE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where isActive is not null
        defaultWebhookSubscriptionFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount equals to
        defaultWebhookSubscriptionFiltering("retryCount.equals=" + DEFAULT_RETRY_COUNT, "retryCount.equals=" + UPDATED_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount in
        defaultWebhookSubscriptionFiltering(
            "retryCount.in=" + DEFAULT_RETRY_COUNT + "," + UPDATED_RETRY_COUNT,
            "retryCount.in=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount is not null
        defaultWebhookSubscriptionFiltering("retryCount.specified=true", "retryCount.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount is greater than or equal to
        defaultWebhookSubscriptionFiltering(
            "retryCount.greaterThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.greaterThanOrEqual=" + UPDATED_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount is less than or equal to
        defaultWebhookSubscriptionFiltering(
            "retryCount.lessThanOrEqual=" + DEFAULT_RETRY_COUNT,
            "retryCount.lessThanOrEqual=" + SMALLER_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount is less than
        defaultWebhookSubscriptionFiltering("retryCount.lessThan=" + UPDATED_RETRY_COUNT, "retryCount.lessThan=" + DEFAULT_RETRY_COUNT);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryCount is greater than
        defaultWebhookSubscriptionFiltering(
            "retryCount.greaterThan=" + SMALLER_RETRY_COUNT,
            "retryCount.greaterThan=" + DEFAULT_RETRY_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries equals to
        defaultWebhookSubscriptionFiltering("maxRetries.equals=" + DEFAULT_MAX_RETRIES, "maxRetries.equals=" + UPDATED_MAX_RETRIES);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries in
        defaultWebhookSubscriptionFiltering(
            "maxRetries.in=" + DEFAULT_MAX_RETRIES + "," + UPDATED_MAX_RETRIES,
            "maxRetries.in=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries is not null
        defaultWebhookSubscriptionFiltering("maxRetries.specified=true", "maxRetries.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries is greater than or equal to
        defaultWebhookSubscriptionFiltering(
            "maxRetries.greaterThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.greaterThanOrEqual=" + UPDATED_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries is less than or equal to
        defaultWebhookSubscriptionFiltering(
            "maxRetries.lessThanOrEqual=" + DEFAULT_MAX_RETRIES,
            "maxRetries.lessThanOrEqual=" + SMALLER_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries is less than
        defaultWebhookSubscriptionFiltering("maxRetries.lessThan=" + UPDATED_MAX_RETRIES, "maxRetries.lessThan=" + DEFAULT_MAX_RETRIES);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByMaxRetriesIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where maxRetries is greater than
        defaultWebhookSubscriptionFiltering(
            "maxRetries.greaterThan=" + SMALLER_MAX_RETRIES,
            "maxRetries.greaterThan=" + DEFAULT_MAX_RETRIES
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay equals to
        defaultWebhookSubscriptionFiltering("retryDelay.equals=" + DEFAULT_RETRY_DELAY, "retryDelay.equals=" + UPDATED_RETRY_DELAY);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay in
        defaultWebhookSubscriptionFiltering(
            "retryDelay.in=" + DEFAULT_RETRY_DELAY + "," + UPDATED_RETRY_DELAY,
            "retryDelay.in=" + UPDATED_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay is not null
        defaultWebhookSubscriptionFiltering("retryDelay.specified=true", "retryDelay.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay is greater than or equal to
        defaultWebhookSubscriptionFiltering(
            "retryDelay.greaterThanOrEqual=" + DEFAULT_RETRY_DELAY,
            "retryDelay.greaterThanOrEqual=" + UPDATED_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay is less than or equal to
        defaultWebhookSubscriptionFiltering(
            "retryDelay.lessThanOrEqual=" + DEFAULT_RETRY_DELAY,
            "retryDelay.lessThanOrEqual=" + SMALLER_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay is less than
        defaultWebhookSubscriptionFiltering("retryDelay.lessThan=" + UPDATED_RETRY_DELAY, "retryDelay.lessThan=" + DEFAULT_RETRY_DELAY);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByRetryDelayIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where retryDelay is greater than
        defaultWebhookSubscriptionFiltering(
            "retryDelay.greaterThan=" + SMALLER_RETRY_DELAY,
            "retryDelay.greaterThan=" + DEFAULT_RETRY_DELAY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastTriggerDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastTriggerDate equals to
        defaultWebhookSubscriptionFiltering(
            "lastTriggerDate.equals=" + DEFAULT_LAST_TRIGGER_DATE,
            "lastTriggerDate.equals=" + UPDATED_LAST_TRIGGER_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastTriggerDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastTriggerDate in
        defaultWebhookSubscriptionFiltering(
            "lastTriggerDate.in=" + DEFAULT_LAST_TRIGGER_DATE + "," + UPDATED_LAST_TRIGGER_DATE,
            "lastTriggerDate.in=" + UPDATED_LAST_TRIGGER_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastTriggerDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastTriggerDate is not null
        defaultWebhookSubscriptionFiltering("lastTriggerDate.specified=true", "lastTriggerDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastSuccessDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastSuccessDate equals to
        defaultWebhookSubscriptionFiltering(
            "lastSuccessDate.equals=" + DEFAULT_LAST_SUCCESS_DATE,
            "lastSuccessDate.equals=" + UPDATED_LAST_SUCCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastSuccessDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastSuccessDate in
        defaultWebhookSubscriptionFiltering(
            "lastSuccessDate.in=" + DEFAULT_LAST_SUCCESS_DATE + "," + UPDATED_LAST_SUCCESS_DATE,
            "lastSuccessDate.in=" + UPDATED_LAST_SUCCESS_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastSuccessDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastSuccessDate is not null
        defaultWebhookSubscriptionFiltering("lastSuccessDate.specified=true", "lastSuccessDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount equals to
        defaultWebhookSubscriptionFiltering("failureCount.equals=" + DEFAULT_FAILURE_COUNT, "failureCount.equals=" + UPDATED_FAILURE_COUNT);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount in
        defaultWebhookSubscriptionFiltering(
            "failureCount.in=" + DEFAULT_FAILURE_COUNT + "," + UPDATED_FAILURE_COUNT,
            "failureCount.in=" + UPDATED_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount is not null
        defaultWebhookSubscriptionFiltering("failureCount.specified=true", "failureCount.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount is greater than or equal to
        defaultWebhookSubscriptionFiltering(
            "failureCount.greaterThanOrEqual=" + DEFAULT_FAILURE_COUNT,
            "failureCount.greaterThanOrEqual=" + UPDATED_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount is less than or equal to
        defaultWebhookSubscriptionFiltering(
            "failureCount.lessThanOrEqual=" + DEFAULT_FAILURE_COUNT,
            "failureCount.lessThanOrEqual=" + SMALLER_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount is less than
        defaultWebhookSubscriptionFiltering(
            "failureCount.lessThan=" + UPDATED_FAILURE_COUNT,
            "failureCount.lessThan=" + DEFAULT_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByFailureCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where failureCount is greater than
        defaultWebhookSubscriptionFiltering(
            "failureCount.greaterThan=" + SMALLER_FAILURE_COUNT,
            "failureCount.greaterThan=" + DEFAULT_FAILURE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdBy equals to
        defaultWebhookSubscriptionFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdBy in
        defaultWebhookSubscriptionFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdBy is not null
        defaultWebhookSubscriptionFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdBy contains
        defaultWebhookSubscriptionFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdBy does not contain
        defaultWebhookSubscriptionFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdDate equals to
        defaultWebhookSubscriptionFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdDate in
        defaultWebhookSubscriptionFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where createdDate is not null
        defaultWebhookSubscriptionFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastModifiedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastModifiedDate equals to
        defaultWebhookSubscriptionFiltering(
            "lastModifiedDate.equals=" + DEFAULT_LAST_MODIFIED_DATE,
            "lastModifiedDate.equals=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastModifiedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastModifiedDate in
        defaultWebhookSubscriptionFiltering(
            "lastModifiedDate.in=" + DEFAULT_LAST_MODIFIED_DATE + "," + UPDATED_LAST_MODIFIED_DATE,
            "lastModifiedDate.in=" + UPDATED_LAST_MODIFIED_DATE
        );
    }

    @Test
    @Transactional
    void getAllWebhookSubscriptionsByLastModifiedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        // Get all the webhookSubscriptionList where lastModifiedDate is not null
        defaultWebhookSubscriptionFiltering("lastModifiedDate.specified=true", "lastModifiedDate.specified=false");
    }

    private void defaultWebhookSubscriptionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultWebhookSubscriptionShouldBeFound(shouldBeFound);
        defaultWebhookSubscriptionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultWebhookSubscriptionShouldBeFound(String filter) throws Exception {
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webhookSubscription.getId().intValue())))
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
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultWebhookSubscriptionShouldNotBeFound(String filter) throws Exception {
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restWebhookSubscriptionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingWebhookSubscription() throws Exception {
        // Get the webhookSubscription
        restWebhookSubscriptionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWebhookSubscription() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookSubscription
        WebhookSubscription updatedWebhookSubscription = webhookSubscriptionRepository.findById(webhookSubscription.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWebhookSubscription are not directly saved in db
        em.detach(updatedWebhookSubscription);
        updatedWebhookSubscription
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
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(updatedWebhookSubscription);

        restWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webhookSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isOk());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWebhookSubscriptionToMatchAllProperties(updatedWebhookSubscription);
    }

    @Test
    @Transactional
    void putNonExistingWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webhookSubscriptionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookSubscriptionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebhookSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookSubscription using partial update
        WebhookSubscription partialUpdatedWebhookSubscription = new WebhookSubscription();
        partialUpdatedWebhookSubscription.setId(webhookSubscription.getId());

        partialUpdatedWebhookSubscription
            .name(UPDATED_NAME)
            .headers(UPDATED_HEADERS)
            .isActive(UPDATED_IS_ACTIVE)
            .maxRetries(UPDATED_MAX_RETRIES)
            .retryDelay(UPDATED_RETRY_DELAY)
            .failureCount(UPDATED_FAILURE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebhookSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWebhookSubscription))
            )
            .andExpect(status().isOk());

        // Validate the WebhookSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWebhookSubscriptionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWebhookSubscription, webhookSubscription),
            getPersistedWebhookSubscription(webhookSubscription)
        );
    }

    @Test
    @Transactional
    void fullUpdateWebhookSubscriptionWithPatch() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookSubscription using partial update
        WebhookSubscription partialUpdatedWebhookSubscription = new WebhookSubscription();
        partialUpdatedWebhookSubscription.setId(webhookSubscription.getId());

        partialUpdatedWebhookSubscription
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

        restWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebhookSubscription.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWebhookSubscription))
            )
            .andExpect(status().isOk());

        // Validate the WebhookSubscription in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWebhookSubscriptionUpdatableFieldsEquals(
            partialUpdatedWebhookSubscription,
            getPersistedWebhookSubscription(partialUpdatedWebhookSubscription)
        );
    }

    @Test
    @Transactional
    void patchNonExistingWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, webhookSubscriptionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebhookSubscription() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookSubscription.setId(longCount.incrementAndGet());

        // Create the WebhookSubscription
        WebhookSubscriptionDTO webhookSubscriptionDTO = webhookSubscriptionMapper.toDto(webhookSubscription);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookSubscriptionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(webhookSubscriptionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebhookSubscription in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebhookSubscription() throws Exception {
        // Initialize the database
        insertedWebhookSubscription = webhookSubscriptionRepository.saveAndFlush(webhookSubscription);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the webhookSubscription
        restWebhookSubscriptionMockMvc
            .perform(delete(ENTITY_API_URL_ID, webhookSubscription.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return webhookSubscriptionRepository.count();
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

    protected WebhookSubscription getPersistedWebhookSubscription(WebhookSubscription webhookSubscription) {
        return webhookSubscriptionRepository.findById(webhookSubscription.getId()).orElseThrow();
    }

    protected void assertPersistedWebhookSubscriptionToMatchAllProperties(WebhookSubscription expectedWebhookSubscription) {
        assertWebhookSubscriptionAllPropertiesEquals(
            expectedWebhookSubscription,
            getPersistedWebhookSubscription(expectedWebhookSubscription)
        );
    }

    protected void assertPersistedWebhookSubscriptionToMatchUpdatableProperties(WebhookSubscription expectedWebhookSubscription) {
        assertWebhookSubscriptionAllUpdatablePropertiesEquals(
            expectedWebhookSubscription,
            getPersistedWebhookSubscription(expectedWebhookSubscription)
        );
    }
}
