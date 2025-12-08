package com.ged.notification.web.rest;

import static com.ged.notification.domain.NotificationEventAsserts.*;
import static com.ged.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.notification.IntegrationTest;
import com.ged.notification.domain.NotificationEvent;
import com.ged.notification.repository.NotificationEventRepository;
import com.ged.notification.service.dto.NotificationEventDTO;
import com.ged.notification.service.mapper.NotificationEventMapper;
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

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

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
            .entityId(DEFAULT_ENTITY_ID)
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
            .entityId(UPDATED_ENTITY_ID)
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
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
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
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.eventData").value(DEFAULT_EVENT_DATA))
            .andExpect(jsonPath("$.eventDate").value(DEFAULT_EVENT_DATE.toString()))
            .andExpect(jsonPath("$.processed").value(DEFAULT_PROCESSED))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()));
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
            .entityId(UPDATED_ENTITY_ID)
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
            .entityId(UPDATED_ENTITY_ID)
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
