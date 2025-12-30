package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.NotificationEventAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.NotificationEvent;
import fr.smartprod.paperdms.notification.repository.NotificationEventRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationEventDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationEventMapper;
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
 * Integration tests for the {@link NotificationEventResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationEventResourceIT {

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_ENTITY_NAME = "AAAAAAAAAA";
    private static final String UPDATED_ENTITY_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_EVENT_DATA = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_DATA = "BBBBBBBBBB";

    private static final Instant DEFAULT_EVENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EVENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_PROCESSED = false;
    private static final Boolean UPDATED_PROCESSED = true;

    private static final Instant DEFAULT_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-events";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationEventRepository notificationEventRepository;

    @Autowired
    private NotificationEventMapper notificationEventMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationEventMockMvc;

    private NotificationEvent notificationEvent;

    private NotificationEvent insertedNotificationEvent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEvent createEntity() {
        return new NotificationEvent()
            .eventType(DEFAULT_EVENT_TYPE)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityName(DEFAULT_ENTITY_NAME)
            .userId(DEFAULT_USER_ID)
            .eventData(DEFAULT_EVENT_DATA)
            .eventDate(DEFAULT_EVENT_DATE)
            .processed(DEFAULT_PROCESSED)
            .processedDate(DEFAULT_PROCESSED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationEvent createUpdatedEntity() {
        return new NotificationEvent()
            .eventType(UPDATED_EVENT_TYPE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .userId(UPDATED_USER_ID)
            .eventData(UPDATED_EVENT_DATA)
            .eventDate(UPDATED_EVENT_DATE)
            .processed(UPDATED_PROCESSED)
            .processedDate(UPDATED_PROCESSED_DATE);
    }

    @BeforeEach
    void initTest() {
        notificationEvent = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationEvent != null) {
            notificationEventRepository.delete(insertedNotificationEvent);
            insertedNotificationEvent = null;
        }
    }

    @Test
    @Transactional
    void createNotificationEvent() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);
        var returnedNotificationEventDTO = om.readValue(
            restNotificationEventMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationEventDTO.class
        );

        // Validate the NotificationEvent in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationEvent = notificationEventMapper.toEntity(returnedNotificationEventDTO);
        assertNotificationEventUpdatableFieldsEquals(returnedNotificationEvent, getPersistedNotificationEvent(returnedNotificationEvent));

        insertedNotificationEvent = returnedNotificationEvent;
    }

    @Test
    @Transactional
    void createNotificationEventWithExistingId() throws Exception {
        // Create the NotificationEvent with an existing ID
        notificationEvent.setId(1L);
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationEvent.setEventType(null);

        // Create the NotificationEvent, which fails.
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        restNotificationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationEvent.setEventDate(null);

        // Create the NotificationEvent, which fails.
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        restNotificationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkProcessedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationEvent.setProcessed(null);

        // Create the NotificationEvent, which fails.
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        restNotificationEventMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationEvents() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].eventData").value(hasItem(DEFAULT_EVENT_DATA)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED)))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotificationEvent() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get the notificationEvent
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationEvent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationEvent.getId().intValue()))
            .andExpect(jsonPath("$.eventType").value(DEFAULT_EVENT_TYPE))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE))
            .andExpect(jsonPath("$.entityName").value(DEFAULT_ENTITY_NAME))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.eventData").value(DEFAULT_EVENT_DATA))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNotificationEventsByIdFiltering() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        Long id = notificationEvent.getId();

        defaultNotificationEventFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultNotificationEventFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultNotificationEventFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventType equals to
        defaultNotificationEventFiltering("eventType.equals=" + DEFAULT_EVENT_TYPE, "eventType.equals=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventType in
        defaultNotificationEventFiltering(
            "eventType.in=" + DEFAULT_EVENT_TYPE + "," + UPDATED_EVENT_TYPE,
            "eventType.in=" + UPDATED_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventType is not null
        defaultNotificationEventFiltering("eventType.specified=true", "eventType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventType contains
        defaultNotificationEventFiltering("eventType.contains=" + DEFAULT_EVENT_TYPE, "eventType.contains=" + UPDATED_EVENT_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventType does not contain
        defaultNotificationEventFiltering(
            "eventType.doesNotContain=" + UPDATED_EVENT_TYPE,
            "eventType.doesNotContain=" + DEFAULT_EVENT_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityType equals to
        defaultNotificationEventFiltering("entityType.equals=" + DEFAULT_ENTITY_TYPE, "entityType.equals=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityType in
        defaultNotificationEventFiltering(
            "entityType.in=" + DEFAULT_ENTITY_TYPE + "," + UPDATED_ENTITY_TYPE,
            "entityType.in=" + UPDATED_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityType is not null
        defaultNotificationEventFiltering("entityType.specified=true", "entityType.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityType contains
        defaultNotificationEventFiltering("entityType.contains=" + DEFAULT_ENTITY_TYPE, "entityType.contains=" + UPDATED_ENTITY_TYPE);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityType does not contain
        defaultNotificationEventFiltering(
            "entityType.doesNotContain=" + UPDATED_ENTITY_TYPE,
            "entityType.doesNotContain=" + DEFAULT_ENTITY_TYPE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityName equals to
        defaultNotificationEventFiltering("entityName.equals=" + DEFAULT_ENTITY_NAME, "entityName.equals=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityName in
        defaultNotificationEventFiltering(
            "entityName.in=" + DEFAULT_ENTITY_NAME + "," + UPDATED_ENTITY_NAME,
            "entityName.in=" + UPDATED_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityName is not null
        defaultNotificationEventFiltering("entityName.specified=true", "entityName.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityNameContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityName contains
        defaultNotificationEventFiltering("entityName.contains=" + DEFAULT_ENTITY_NAME, "entityName.contains=" + UPDATED_ENTITY_NAME);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEntityNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where entityName does not contain
        defaultNotificationEventFiltering(
            "entityName.doesNotContain=" + UPDATED_ENTITY_NAME,
            "entityName.doesNotContain=" + DEFAULT_ENTITY_NAME
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where userId equals to
        defaultNotificationEventFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where userId in
        defaultNotificationEventFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where userId is not null
        defaultNotificationEventFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where userId contains
        defaultNotificationEventFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where userId does not contain
        defaultNotificationEventFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventDate equals to
        defaultNotificationEventFiltering("eventDate.equals=" + DEFAULT_EVENT_DATE, "eventDate.equals=" + UPDATED_EVENT_DATE);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventDate in
        defaultNotificationEventFiltering(
            "eventDate.in=" + DEFAULT_EVENT_DATE + "," + UPDATED_EVENT_DATE,
            "eventDate.in=" + UPDATED_EVENT_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByEventDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where eventDate is not null
        defaultNotificationEventFiltering("eventDate.specified=true", "eventDate.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processed equals to
        defaultNotificationEventFiltering("processed.equals=" + DEFAULT_PROCESSED, "processed.equals=" + UPDATED_PROCESSED);
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processed in
        defaultNotificationEventFiltering(
            "processed.in=" + DEFAULT_PROCESSED + "," + UPDATED_PROCESSED,
            "processed.in=" + UPDATED_PROCESSED
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processed is not null
        defaultNotificationEventFiltering("processed.specified=true", "processed.specified=false");
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processedDate equals to
        defaultNotificationEventFiltering(
            "processedDate.equals=" + DEFAULT_PROCESSED_DATE,
            "processedDate.equals=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processedDate in
        defaultNotificationEventFiltering(
            "processedDate.in=" + DEFAULT_PROCESSED_DATE + "," + UPDATED_PROCESSED_DATE,
            "processedDate.in=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllNotificationEventsByProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        // Get all the notificationEventList where processedDate is not null
        defaultNotificationEventFiltering("processedDate.specified=true", "processedDate.specified=false");
    }

    private void defaultNotificationEventFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultNotificationEventShouldBeFound(shouldBeFound);
        defaultNotificationEventShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationEventShouldBeFound(String filter) throws Exception {
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationEvent.getId().intValue())))
            .andExpect(jsonPath("$.[*].eventType").value(hasItem(DEFAULT_EVENT_TYPE)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE)))
            .andExpect(jsonPath("$.[*].entityName").value(hasItem(DEFAULT_ENTITY_NAME)))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].eventData").value(hasItem(DEFAULT_EVENT_DATA)))
            .andExpect(jsonPath("$.[*].eventDate").value(hasItem(DEFAULT_EVENT_DATE.toString())))
            .andExpect(jsonPath("$.[*].processed").value(hasItem(DEFAULT_PROCESSED)))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())));

        // Check, that the count call also returns 1
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationEventShouldNotBeFound(String filter) throws Exception {
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationEventMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingNotificationEvent() throws Exception {
        // Get the notificationEvent
        restNotificationEventMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationEvent() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationEvent
        NotificationEvent updatedNotificationEvent = notificationEventRepository.findById(notificationEvent.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationEvent are not directly saved in db
        em.detach(updatedNotificationEvent);
        updatedNotificationEvent
            .eventType(UPDATED_EVENT_TYPE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .userId(UPDATED_USER_ID)
            .eventData(UPDATED_EVENT_DATA)
            .eventDate(UPDATED_EVENT_DATE)
            .processed(UPDATED_PROCESSED)
            .processedDate(UPDATED_PROCESSED_DATE);
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(updatedNotificationEvent);

        restNotificationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationEventDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationEventToMatchAllProperties(updatedNotificationEvent);
    }

    @Test
    @Transactional
    void putNonExistingNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationEventDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationEventWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationEvent using partial update
        NotificationEvent partialUpdatedNotificationEvent = new NotificationEvent();
        partialUpdatedNotificationEvent.setId(notificationEvent.getId());

        partialUpdatedNotificationEvent
            .eventType(UPDATED_EVENT_TYPE)
            .entityType(UPDATED_ENTITY_TYPE)
            .userId(UPDATED_USER_ID)
            .eventDate(UPDATED_EVENT_DATE)
            .processed(UPDATED_PROCESSED)
            .processedDate(UPDATED_PROCESSED_DATE);

        restNotificationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationEvent))
            )
            .andExpect(status().isOk());

        // Validate the NotificationEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationEventUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationEvent, notificationEvent),
            getPersistedNotificationEvent(notificationEvent)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationEventWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationEvent using partial update
        NotificationEvent partialUpdatedNotificationEvent = new NotificationEvent();
        partialUpdatedNotificationEvent.setId(notificationEvent.getId());

        partialUpdatedNotificationEvent
            .eventType(UPDATED_EVENT_TYPE)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityName(UPDATED_ENTITY_NAME)
            .userId(UPDATED_USER_ID)
            .eventData(UPDATED_EVENT_DATA)
            .eventDate(UPDATED_EVENT_DATE)
            .processed(UPDATED_PROCESSED)
            .processedDate(UPDATED_PROCESSED_DATE);

        restNotificationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationEvent.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationEvent))
            )
            .andExpect(status().isOk());

        // Validate the NotificationEvent in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationEventUpdatableFieldsEquals(
            partialUpdatedNotificationEvent,
            getPersistedNotificationEvent(partialUpdatedNotificationEvent)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationEventDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationEventDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationEvent() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationEvent.setId(longCount.incrementAndGet());

        // Create the NotificationEvent
        NotificationEventDTO notificationEventDTO = notificationEventMapper.toDto(notificationEvent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationEventMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationEventDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationEvent in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationEvent() throws Exception {
        // Initialize the database
        insertedNotificationEvent = notificationEventRepository.saveAndFlush(notificationEvent);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationEvent
        restNotificationEventMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationEvent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationEventRepository.count();
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

    protected NotificationEvent getPersistedNotificationEvent(NotificationEvent notificationEvent) {
        return notificationEventRepository.findById(notificationEvent.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationEventToMatchAllProperties(NotificationEvent expectedNotificationEvent) {
        assertNotificationEventAllPropertiesEquals(expectedNotificationEvent, getPersistedNotificationEvent(expectedNotificationEvent));
    }

    protected void assertPersistedNotificationEventToMatchUpdatableProperties(NotificationEvent expectedNotificationEvent) {
        assertNotificationEventAllUpdatablePropertiesEquals(
            expectedNotificationEvent,
            getPersistedNotificationEvent(expectedNotificationEvent)
        );
    }
}
