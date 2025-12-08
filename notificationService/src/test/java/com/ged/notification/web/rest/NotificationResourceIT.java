package com.ged.notification.web.rest;

import static com.ged.notification.domain.NotificationAsserts.*;
import static com.ged.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.notification.IntegrationTest;
import com.ged.notification.domain.Notification;
import com.ged.notification.domain.NotificationTemplate;
import com.ged.notification.domain.enumeration.NotificationChannel;
import com.ged.notification.domain.enumeration.NotificationPriority;
import com.ged.notification.domain.enumeration.NotificationType;
import com.ged.notification.repository.NotificationRepository;
import com.ged.notification.service.dto.NotificationDTO;
import com.ged.notification.service.mapper.NotificationMapper;
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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final NotificationType DEFAULT_TYPE = NotificationType.DOCUMENT_UPLOADED;
    private static final NotificationType UPDATED_TYPE = NotificationType.DOCUMENT_PROCESSED;

    private static final NotificationPriority DEFAULT_PRIORITY = NotificationPriority.LOW;
    private static final NotificationPriority UPDATED_PRIORITY = NotificationPriority.NORMAL;

    private static final String DEFAULT_RECIPIENT_ID = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_READ = false;
    private static final Boolean UPDATED_IS_READ = true;

    private static final Instant DEFAULT_READ_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_READ_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final NotificationChannel DEFAULT_CHANNEL = NotificationChannel.EMAIL;
    private static final NotificationChannel UPDATED_CHANNEL = NotificationChannel.PUSH;

    private static final String DEFAULT_RELATED_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_RELATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final Long DEFAULT_RELATED_ENTITY_ID = 1L;
    private static final Long UPDATED_RELATED_ENTITY_ID = 2L;
    private static final Long SMALLER_RELATED_ENTITY_ID = 1L - 1L;

    private static final String DEFAULT_ACTION_URL = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_URL = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_EXPIRATION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPIRATION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notifications";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    private Notification insertedNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity() {
        return new Notification()
            .title(DEFAULT_TITLE)
            .message(DEFAULT_MESSAGE)
            .type(DEFAULT_TYPE)
            .priority(DEFAULT_PRIORITY)
            .recipientId(DEFAULT_RECIPIENT_ID)
            .isRead(DEFAULT_IS_READ)
            .readDate(DEFAULT_READ_DATE)
            .channel(DEFAULT_CHANNEL)
            .relatedEntityType(DEFAULT_RELATED_ENTITY_TYPE)
            .relatedEntityId(DEFAULT_RELATED_ENTITY_ID)
            .actionUrl(DEFAULT_ACTION_URL)
            .metadata(DEFAULT_METADATA)
            .expirationDate(DEFAULT_EXPIRATION_DATE)
            .sentDate(DEFAULT_SENT_DATE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity() {
        return new Notification()
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isRead(UPDATED_IS_READ)
            .readDate(UPDATED_READ_DATE)
            .channel(UPDATED_CHANNEL)
            .relatedEntityType(UPDATED_RELATED_ENTITY_TYPE)
            .relatedEntityId(UPDATED_RELATED_ENTITY_ID)
            .actionUrl(UPDATED_ACTION_URL)
            .metadata(UPDATED_METADATA)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .sentDate(UPDATED_SENT_DATE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        notification = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotification != null) {
            notificationRepository.delete(insertedNotification);
            insertedNotification = null;
        }
    }

    @Test
    @Transactional
    void createNotification() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);
        var returnedNotificationDTO = om.readValue(
            restNotificationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationDTO.class
        );

        // Validate the Notification in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotification = notificationMapper.toEntity(returnedNotificationDTO);
        assertNotificationUpdatableFieldsEquals(returnedNotification, getPersistedNotification(returnedNotification));

        insertedNotification = returnedNotification;
    }

    @Test
    @Transactional
    void createNotificationWithExistingId() throws Exception {
        // Create the Notification with an existing ID
        notification.setId(1L);
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setTitle(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setType(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setPriority(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setRecipientId(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsReadIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setIsRead(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChannelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setChannel(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setSentDate(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notification.setCreatedDate(null);

        // Create the Notification, which fails.
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        restNotificationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotifications() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].recipientId").value(hasItem(DEFAULT_RECIPIENT_ID)))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ)))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].relatedEntityType").value(hasItem(DEFAULT_RELATED_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].relatedEntityId").value(hasItem(DEFAULT_RELATED_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].actionUrl").value(hasItem(DEFAULT_ACTION_URL)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL_ID, notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.recipientId").value(DEFAULT_RECIPIENT_ID))
            .andExpect(jsonPath("$.isRead").value(DEFAULT_IS_READ))
            .andExpect(jsonPath("$.readDate").value(DEFAULT_READ_DATE.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.relatedEntityType").value(DEFAULT_RELATED_ENTITY_TYPE))
            .andExpect(jsonPath("$.relatedEntityId").value(DEFAULT_RELATED_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.actionUrl").value(DEFAULT_ACTION_URL))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.expirationDate").value(DEFAULT_EXPIRATION_DATE.toString()))
            .andExpect(jsonPath("$.sentDate").value(DEFAULT_SENT_DATE.toString()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title equals to
        defaultNotificationFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title in
        defaultNotificationFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title is not null
        defaultNotificationFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title contains
        defaultNotificationFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where title does not contain
        defaultNotificationFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type equals to
        defaultNotificationFiltering("type.equals=" + DEFAULT_TYPE, "type.equals=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type in
        defaultNotificationFiltering("type.in=" + DEFAULT_TYPE + "," + UPDATED_TYPE, "type.in=" + UPDATED_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationsByTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where type is not null
        defaultNotificationFiltering("type.specified=true", "type.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where priority equals to
        defaultNotificationFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllNotificationsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where priority in
        defaultNotificationFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllNotificationsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where priority is not null
        defaultNotificationFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRecipientIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipientId equals to
        defaultNotificationFiltering("recipientId.equals=" + DEFAULT_RECIPIENT_ID, "recipientId.equals=" + UPDATED_RECIPIENT_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByRecipientIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipientId in
        defaultNotificationFiltering(
            "recipientId.in=" + DEFAULT_RECIPIENT_ID + "," + UPDATED_RECIPIENT_ID,
            "recipientId.in=" + UPDATED_RECIPIENT_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRecipientIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipientId is not null
        defaultNotificationFiltering("recipientId.specified=true", "recipientId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRecipientIdContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipientId contains
        defaultNotificationFiltering("recipientId.contains=" + DEFAULT_RECIPIENT_ID, "recipientId.contains=" + UPDATED_RECIPIENT_ID);
    }

    @Test
    @Transactional
    void getAllNotificationsByRecipientIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where recipientId does not contain
        defaultNotificationFiltering(
            "recipientId.doesNotContain=" + UPDATED_RECIPIENT_ID,
            "recipientId.doesNotContain=" + DEFAULT_RECIPIENT_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByIsReadIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isRead equals to
        defaultNotificationFiltering("isRead.equals=" + DEFAULT_IS_READ, "isRead.equals=" + UPDATED_IS_READ);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsReadIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isRead in
        defaultNotificationFiltering("isRead.in=" + DEFAULT_IS_READ + "," + UPDATED_IS_READ, "isRead.in=" + UPDATED_IS_READ);
    }

    @Test
    @Transactional
    void getAllNotificationsByIsReadIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where isRead is not null
        defaultNotificationFiltering("isRead.specified=true", "isRead.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByReadDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readDate equals to
        defaultNotificationFiltering("readDate.equals=" + DEFAULT_READ_DATE, "readDate.equals=" + UPDATED_READ_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByReadDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readDate in
        defaultNotificationFiltering("readDate.in=" + DEFAULT_READ_DATE + "," + UPDATED_READ_DATE, "readDate.in=" + UPDATED_READ_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByReadDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where readDate is not null
        defaultNotificationFiltering("readDate.specified=true", "readDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel equals to
        defaultNotificationFiltering("channel.equals=" + DEFAULT_CHANNEL, "channel.equals=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel in
        defaultNotificationFiltering("channel.in=" + DEFAULT_CHANNEL + "," + UPDATED_CHANNEL, "channel.in=" + UPDATED_CHANNEL);
    }

    @Test
    @Transactional
    void getAllNotificationsByChannelIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where channel is not null
        defaultNotificationFiltering("channel.specified=true", "channel.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityType equals to
        defaultNotificationFiltering(
            "relatedEntityType.equals=" + DEFAULT_RELATED_ENTITY_TYPE,
            "relatedEntityType.equals=" + UPDATED_RELATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityType in
        defaultNotificationFiltering(
            "relatedEntityType.in=" + DEFAULT_RELATED_ENTITY_TYPE + "," + UPDATED_RELATED_ENTITY_TYPE,
            "relatedEntityType.in=" + UPDATED_RELATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityType is not null
        defaultNotificationFiltering("relatedEntityType.specified=true", "relatedEntityType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityType contains
        defaultNotificationFiltering(
            "relatedEntityType.contains=" + DEFAULT_RELATED_ENTITY_TYPE,
            "relatedEntityType.contains=" + UPDATED_RELATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityType does not contain
        defaultNotificationFiltering(
            "relatedEntityType.doesNotContain=" + UPDATED_RELATED_ENTITY_TYPE,
            "relatedEntityType.doesNotContain=" + DEFAULT_RELATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId equals to
        defaultNotificationFiltering(
            "relatedEntityId.equals=" + DEFAULT_RELATED_ENTITY_ID,
            "relatedEntityId.equals=" + UPDATED_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId in
        defaultNotificationFiltering(
            "relatedEntityId.in=" + DEFAULT_RELATED_ENTITY_ID + "," + UPDATED_RELATED_ENTITY_ID,
            "relatedEntityId.in=" + UPDATED_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId is not null
        defaultNotificationFiltering("relatedEntityId.specified=true", "relatedEntityId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId is greater than or equal to
        defaultNotificationFiltering(
            "relatedEntityId.greaterThanOrEqual=" + DEFAULT_RELATED_ENTITY_ID,
            "relatedEntityId.greaterThanOrEqual=" + UPDATED_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId is less than or equal to
        defaultNotificationFiltering(
            "relatedEntityId.lessThanOrEqual=" + DEFAULT_RELATED_ENTITY_ID,
            "relatedEntityId.lessThanOrEqual=" + SMALLER_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId is less than
        defaultNotificationFiltering(
            "relatedEntityId.lessThan=" + UPDATED_RELATED_ENTITY_ID,
            "relatedEntityId.lessThan=" + DEFAULT_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByRelatedEntityIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where relatedEntityId is greater than
        defaultNotificationFiltering(
            "relatedEntityId.greaterThan=" + SMALLER_RELATED_ENTITY_ID,
            "relatedEntityId.greaterThan=" + DEFAULT_RELATED_ENTITY_ID
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByActionUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where actionUrl equals to
        defaultNotificationFiltering("actionUrl.equals=" + DEFAULT_ACTION_URL, "actionUrl.equals=" + UPDATED_ACTION_URL);
    }

    @Test
    @Transactional
    void getAllNotificationsByActionUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where actionUrl in
        defaultNotificationFiltering("actionUrl.in=" + DEFAULT_ACTION_URL + "," + UPDATED_ACTION_URL, "actionUrl.in=" + UPDATED_ACTION_URL);
    }

    @Test
    @Transactional
    void getAllNotificationsByActionUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where actionUrl is not null
        defaultNotificationFiltering("actionUrl.specified=true", "actionUrl.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByActionUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where actionUrl contains
        defaultNotificationFiltering("actionUrl.contains=" + DEFAULT_ACTION_URL, "actionUrl.contains=" + UPDATED_ACTION_URL);
    }

    @Test
    @Transactional
    void getAllNotificationsByActionUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where actionUrl does not contain
        defaultNotificationFiltering("actionUrl.doesNotContain=" + UPDATED_ACTION_URL, "actionUrl.doesNotContain=" + DEFAULT_ACTION_URL);
    }

    @Test
    @Transactional
    void getAllNotificationsByExpirationDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expirationDate equals to
        defaultNotificationFiltering(
            "expirationDate.equals=" + DEFAULT_EXPIRATION_DATE,
            "expirationDate.equals=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByExpirationDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expirationDate in
        defaultNotificationFiltering(
            "expirationDate.in=" + DEFAULT_EXPIRATION_DATE + "," + UPDATED_EXPIRATION_DATE,
            "expirationDate.in=" + UPDATED_EXPIRATION_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByExpirationDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where expirationDate is not null
        defaultNotificationFiltering("expirationDate.specified=true", "expirationDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsBySentDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentDate equals to
        defaultNotificationFiltering("sentDate.equals=" + DEFAULT_SENT_DATE, "sentDate.equals=" + UPDATED_SENT_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsBySentDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentDate in
        defaultNotificationFiltering("sentDate.in=" + DEFAULT_SENT_DATE + "," + UPDATED_SENT_DATE, "sentDate.in=" + UPDATED_SENT_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsBySentDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where sentDate is not null
        defaultNotificationFiltering("sentDate.specified=true", "sentDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdDate equals to
        defaultNotificationFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdDate in
        defaultNotificationFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where createdDate is not null
        defaultNotificationFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationsByTemplateIsEqualToSomething() throws Exception {
        NotificationTemplate template;
        if (TestUtil.findAll(em, NotificationTemplate.class).isEmpty()) {
            notificationRepository.saveAndFlush(notification);
            template = NotificationTemplateResourceIT.createEntity();
        } else {
            template = TestUtil.findAll(em, NotificationTemplate.class).get(0);
        }
        em.persist(template);
        em.flush();
        notification.setTemplate(template);
        notificationRepository.saveAndFlush(notification);
        Long templateId = template.getId();
        // Get all the notificationList where template equals to templateId
        defaultNotificationShouldBeFound("templateId.equals=" + templateId);

        // Get all the notificationList where template equals to (templateId + 1)
        defaultNotificationShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    private void defaultNotificationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationShouldBeFound(shouldBeFound);
        defaultNotificationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].recipientId").value(hasItem(DEFAULT_RECIPIENT_ID)))
            .andExpect(jsonPath("$.[*].isRead").value(hasItem(DEFAULT_IS_READ)))
            .andExpect(jsonPath("$.[*].readDate").value(hasItem(DEFAULT_READ_DATE.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].relatedEntityType").value(hasItem(DEFAULT_RELATED_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].relatedEntityId").value(hasItem(DEFAULT_RELATED_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].actionUrl").value(hasItem(DEFAULT_ACTION_URL)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].expirationDate").value(hasItem(DEFAULT_EXPIRATION_DATE.toString())))
            .andExpect(jsonPath("$.[*].sentDate").value(hasItem(DEFAULT_SENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isRead(UPDATED_IS_READ)
            .readDate(UPDATED_READ_DATE)
            .channel(UPDATED_CHANNEL)
            .relatedEntityType(UPDATED_RELATED_ENTITY_TYPE)
            .relatedEntityId(UPDATED_RELATED_ENTITY_ID)
            .actionUrl(UPDATED_ACTION_URL)
            .metadata(UPDATED_METADATA)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .sentDate(UPDATED_SENT_DATE)
            .createdDate(UPDATED_CREATED_DATE);
        NotificationDTO notificationDTO = notificationMapper.toDto(updatedNotification);

        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationToMatchAllProperties(updatedNotification);
    }

    @Test
    @Transactional
    void putNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .title(UPDATED_TITLE)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isRead(UPDATED_IS_READ)
            .metadata(UPDATED_METADATA)
            .createdDate(UPDATED_CREATED_DATE);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotification, notification),
            getPersistedNotification(notification)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationWithPatch() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notification using partial update
        Notification partialUpdatedNotification = new Notification();
        partialUpdatedNotification.setId(notification.getId());

        partialUpdatedNotification
            .title(UPDATED_TITLE)
            .message(UPDATED_MESSAGE)
            .type(UPDATED_TYPE)
            .priority(UPDATED_PRIORITY)
            .recipientId(UPDATED_RECIPIENT_ID)
            .isRead(UPDATED_IS_READ)
            .readDate(UPDATED_READ_DATE)
            .channel(UPDATED_CHANNEL)
            .relatedEntityType(UPDATED_RELATED_ENTITY_TYPE)
            .relatedEntityId(UPDATED_RELATED_ENTITY_ID)
            .actionUrl(UPDATED_ACTION_URL)
            .metadata(UPDATED_METADATA)
            .expirationDate(UPDATED_EXPIRATION_DATE)
            .sentDate(UPDATED_SENT_DATE)
            .createdDate(UPDATED_CREATED_DATE);

        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotification.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotification))
            )
            .andExpect(status().isOk());

        // Validate the Notification in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationUpdatableFieldsEquals(partialUpdatedNotification, getPersistedNotification(partialUpdatedNotification));
    }

    @Test
    @Transactional
    void patchNonExistingNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotification() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notification.setId(longCount.incrementAndGet());

        // Create the Notification
        NotificationDTO notificationDTO = notificationMapper.toDto(notification);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Notification in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotification() throws Exception {
        // Initialize the database
        insertedNotification = notificationRepository.saveAndFlush(notification);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notification
        restNotificationMockMvc
            .perform(delete(ENTITY_API_URL_ID, notification.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationRepository.count();
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

    protected Notification getPersistedNotification(Notification notification) {
        return notificationRepository.findById(notification.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationToMatchAllProperties(Notification expectedNotification) {
        assertNotificationAllPropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
    }

    protected void assertPersistedNotificationToMatchUpdatableProperties(Notification expectedNotification) {
        assertNotificationAllUpdatablePropertiesEquals(expectedNotification, getPersistedNotification(expectedNotification));
    }
}
