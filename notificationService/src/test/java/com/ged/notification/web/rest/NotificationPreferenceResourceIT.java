package com.ged.notification.web.rest;

import static com.ged.notification.domain.NotificationPreferenceAsserts.*;
import static com.ged.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.notification.IntegrationTest;
import com.ged.notification.domain.NotificationPreference;
import com.ged.notification.domain.enumeration.NotificationFrequency;
import com.ged.notification.repository.NotificationPreferenceRepository;
import com.ged.notification.service.dto.NotificationPreferenceDTO;
import com.ged.notification.service.mapper.NotificationPreferenceMapper;
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
 * Integration tests for the {@link NotificationPreferenceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationPreferenceResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final Boolean DEFAULT_EMAIL_ENABLED = false;
    private static final Boolean UPDATED_EMAIL_ENABLED = true;

    private static final Boolean DEFAULT_PUSH_ENABLED = false;
    private static final Boolean UPDATED_PUSH_ENABLED = true;

    private static final Boolean DEFAULT_IN_APP_ENABLED = false;
    private static final Boolean UPDATED_IN_APP_ENABLED = true;

    private static final String DEFAULT_NOTIFICATION_TYPES = "AAAAAAAAAA";
    private static final String UPDATED_NOTIFICATION_TYPES = "BBBBBBBBBB";

    private static final String DEFAULT_QUIET_HOURS_START = "AAAAA";
    private static final String UPDATED_QUIET_HOURS_START = "BBBBB";

    private static final String DEFAULT_QUIET_HOURS_END = "AAAAA";
    private static final String UPDATED_QUIET_HOURS_END = "BBBBB";

    private static final NotificationFrequency DEFAULT_FREQUENCY = NotificationFrequency.IMMEDIATELY;
    private static final NotificationFrequency UPDATED_FREQUENCY = NotificationFrequency.HOURLY;

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-preferences";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationPreferenceRepository notificationPreferenceRepository;

    @Autowired
    private NotificationPreferenceMapper notificationPreferenceMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationPreferenceMockMvc;

    private NotificationPreference notificationPreference;

    private NotificationPreference insertedNotificationPreference;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationPreference createEntity() {
        return new NotificationPreference()
            .userId(DEFAULT_USER_ID)
            .emailEnabled(DEFAULT_EMAIL_ENABLED)
            .pushEnabled(DEFAULT_PUSH_ENABLED)
            .inAppEnabled(DEFAULT_IN_APP_ENABLED)
            .notificationTypes(DEFAULT_NOTIFICATION_TYPES)
            .quietHoursStart(DEFAULT_QUIET_HOURS_START)
            .quietHoursEnd(DEFAULT_QUIET_HOURS_END)
            .frequency(DEFAULT_FREQUENCY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationPreference createUpdatedEntity() {
        return new NotificationPreference()
            .userId(UPDATED_USER_ID)
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED)
            .notificationTypes(UPDATED_NOTIFICATION_TYPES)
            .quietHoursStart(UPDATED_QUIET_HOURS_START)
            .quietHoursEnd(UPDATED_QUIET_HOURS_END)
            .frequency(UPDATED_FREQUENCY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        notificationPreference = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationPreference != null) {
            notificationPreferenceRepository.delete(insertedNotificationPreference);
            insertedNotificationPreference = null;
        }
    }

    @Test
    @Transactional
    void createNotificationPreference() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);
        var returnedNotificationPreferenceDTO = om.readValue(
            restNotificationPreferenceMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationPreferenceDTO.class
        );

        // Validate the NotificationPreference in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationPreference = notificationPreferenceMapper.toEntity(returnedNotificationPreferenceDTO);
        assertNotificationPreferenceUpdatableFieldsEquals(
            returnedNotificationPreference,
            getPersistedNotificationPreference(returnedNotificationPreference)
        );

        insertedNotificationPreference = returnedNotificationPreference;
    }

    @Test
    @Transactional
    void createNotificationPreferenceWithExistingId() throws Exception {
        // Create the NotificationPreference with an existing ID
        notificationPreference.setId(1L);
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPreference.setUserId(null);

        // Create the NotificationPreference, which fails.
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPreference.setEmailEnabled(null);

        // Create the NotificationPreference, which fails.
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPushEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPreference.setPushEnabled(null);

        // Create the NotificationPreference, which fails.
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkInAppEnabledIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPreference.setInAppEnabled(null);

        // Create the NotificationPreference, which fails.
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFrequencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationPreference.setFrequency(null);

        // Create the NotificationPreference, which fails.
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        restNotificationPreferenceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationPreferences() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        // Get all the notificationPreferenceList
        restNotificationPreferenceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationPreference.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].emailEnabled").value(hasItem(DEFAULT_EMAIL_ENABLED)))
            .andExpect(jsonPath("$.[*].pushEnabled").value(hasItem(DEFAULT_PUSH_ENABLED)))
            .andExpect(jsonPath("$.[*].inAppEnabled").value(hasItem(DEFAULT_IN_APP_ENABLED)))
            .andExpect(jsonPath("$.[*].notificationTypes").value(hasItem(DEFAULT_NOTIFICATION_TYPES)))
            .andExpect(jsonPath("$.[*].quietHoursStart").value(hasItem(DEFAULT_QUIET_HOURS_START)))
            .andExpect(jsonPath("$.[*].quietHoursEnd").value(hasItem(DEFAULT_QUIET_HOURS_END)))
            .andExpect(jsonPath("$.[*].frequency").value(hasItem(DEFAULT_FREQUENCY.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotificationPreference() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        // Get the notificationPreference
        restNotificationPreferenceMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationPreference.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationPreference.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.emailEnabled").value(DEFAULT_EMAIL_ENABLED))
            .andExpect(jsonPath("$.pushEnabled").value(DEFAULT_PUSH_ENABLED))
            .andExpect(jsonPath("$.inAppEnabled").value(DEFAULT_IN_APP_ENABLED))
            .andExpect(jsonPath("$.notificationTypes").value(DEFAULT_NOTIFICATION_TYPES))
            .andExpect(jsonPath("$.quietHoursStart").value(DEFAULT_QUIET_HOURS_START))
            .andExpect(jsonPath("$.quietHoursEnd").value(DEFAULT_QUIET_HOURS_END))
            .andExpect(jsonPath("$.frequency").value(DEFAULT_FREQUENCY.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotificationPreference() throws Exception {
        // Get the notificationPreference
        restNotificationPreferenceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationPreference() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPreference
        NotificationPreference updatedNotificationPreference = notificationPreferenceRepository
            .findById(notificationPreference.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationPreference are not directly saved in db
        em.detach(updatedNotificationPreference);
        updatedNotificationPreference
            .userId(UPDATED_USER_ID)
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED)
            .notificationTypes(UPDATED_NOTIFICATION_TYPES)
            .quietHoursStart(UPDATED_QUIET_HOURS_START)
            .quietHoursEnd(UPDATED_QUIET_HOURS_END)
            .frequency(UPDATED_FREQUENCY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(updatedNotificationPreference);

        restNotificationPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationPreferenceToMatchAllProperties(updatedNotificationPreference);
    }

    @Test
    @Transactional
    void putNonExistingNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationPreferenceDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationPreferenceDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPreference using partial update
        NotificationPreference partialUpdatedNotificationPreference = new NotificationPreference();
        partialUpdatedNotificationPreference.setId(notificationPreference.getId());

        partialUpdatedNotificationPreference
            .userId(UPDATED_USER_ID)
            .inAppEnabled(UPDATED_IN_APP_ENABLED)
            .quietHoursStart(UPDATED_QUIET_HOURS_START)
            .quietHoursEnd(UPDATED_QUIET_HOURS_END);

        restNotificationPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationPreference))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationPreferenceUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationPreference, notificationPreference),
            getPersistedNotificationPreference(notificationPreference)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationPreferenceWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationPreference using partial update
        NotificationPreference partialUpdatedNotificationPreference = new NotificationPreference();
        partialUpdatedNotificationPreference.setId(notificationPreference.getId());

        partialUpdatedNotificationPreference
            .userId(UPDATED_USER_ID)
            .emailEnabled(UPDATED_EMAIL_ENABLED)
            .pushEnabled(UPDATED_PUSH_ENABLED)
            .inAppEnabled(UPDATED_IN_APP_ENABLED)
            .notificationTypes(UPDATED_NOTIFICATION_TYPES)
            .quietHoursStart(UPDATED_QUIET_HOURS_START)
            .quietHoursEnd(UPDATED_QUIET_HOURS_END)
            .frequency(UPDATED_FREQUENCY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restNotificationPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationPreference.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationPreference))
            )
            .andExpect(status().isOk());

        // Validate the NotificationPreference in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationPreferenceUpdatableFieldsEquals(
            partialUpdatedNotificationPreference,
            getPersistedNotificationPreference(partialUpdatedNotificationPreference)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationPreferenceDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationPreference() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationPreference.setId(longCount.incrementAndGet());

        // Create the NotificationPreference
        NotificationPreferenceDTO notificationPreferenceDTO = notificationPreferenceMapper.toDto(notificationPreference);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationPreferenceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationPreferenceDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationPreference in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationPreference() throws Exception {
        // Initialize the database
        insertedNotificationPreference = notificationPreferenceRepository.saveAndFlush(notificationPreference);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationPreference
        restNotificationPreferenceMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationPreference.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationPreferenceRepository.count();
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

    protected NotificationPreference getPersistedNotificationPreference(NotificationPreference notificationPreference) {
        return notificationPreferenceRepository.findById(notificationPreference.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationPreferenceToMatchAllProperties(NotificationPreference expectedNotificationPreference) {
        assertNotificationPreferenceAllPropertiesEquals(
            expectedNotificationPreference,
            getPersistedNotificationPreference(expectedNotificationPreference)
        );
    }

    protected void assertPersistedNotificationPreferenceToMatchUpdatableProperties(NotificationPreference expectedNotificationPreference) {
        assertNotificationPreferenceAllUpdatablePropertiesEquals(
            expectedNotificationPreference,
            getPersistedNotificationPreference(expectedNotificationPreference)
        );
    }
}
