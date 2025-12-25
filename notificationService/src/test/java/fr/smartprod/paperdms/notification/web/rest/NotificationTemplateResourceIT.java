package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.NotificationTemplateAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.NotificationTemplate;
import fr.smartprod.paperdms.notification.domain.enumeration.NotificationChannel;
import fr.smartprod.paperdms.notification.domain.enumeration.NotificationType;
import fr.smartprod.paperdms.notification.repository.NotificationTemplateRepository;
import fr.smartprod.paperdms.notification.service.dto.NotificationTemplateDTO;
import fr.smartprod.paperdms.notification.service.mapper.NotificationTemplateMapper;
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
 * Integration tests for the {@link NotificationTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NotificationTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY_TEMPLATE = "AAAAAAAAAA";
    private static final String UPDATED_BODY_TEMPLATE = "BBBBBBBBBB";

    private static final NotificationType DEFAULT_TYPE = NotificationType.DOCUMENT_UPLOADED;
    private static final NotificationType UPDATED_TYPE = NotificationType.DOCUMENT_PROCESSED;

    private static final NotificationChannel DEFAULT_CHANNEL = NotificationChannel.EMAIL;
    private static final NotificationChannel UPDATED_CHANNEL = NotificationChannel.PUSH;

    private static final String DEFAULT_VARIABLES = "AAAAAAAAAA";
    private static final String UPDATED_VARIABLES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/notification-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private NotificationTemplateRepository notificationTemplateRepository;

    @Autowired
    private NotificationTemplateMapper notificationTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationTemplateMockMvc;

    private NotificationTemplate notificationTemplate;

    private NotificationTemplate insertedNotificationTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTemplate createEntity() {
        return new NotificationTemplate()
            .name(DEFAULT_NAME)
            .subject(DEFAULT_SUBJECT)
            .bodyTemplate(DEFAULT_BODY_TEMPLATE)
            .type(DEFAULT_TYPE)
            .channel(DEFAULT_CHANNEL)
            .variables(DEFAULT_VARIABLES)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NotificationTemplate createUpdatedEntity() {
        return new NotificationTemplate()
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .variables(UPDATED_VARIABLES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    void initTest() {
        notificationTemplate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedNotificationTemplate != null) {
            notificationTemplateRepository.delete(insertedNotificationTemplate);
            insertedNotificationTemplate = null;
        }
    }

    @Test
    @Transactional
    void createNotificationTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);
        var returnedNotificationTemplateDTO = om.readValue(
            restNotificationTemplateMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            NotificationTemplateDTO.class
        );

        // Validate the NotificationTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedNotificationTemplate = notificationTemplateMapper.toEntity(returnedNotificationTemplateDTO);
        assertNotificationTemplateUpdatableFieldsEquals(
            returnedNotificationTemplate,
            getPersistedNotificationTemplate(returnedNotificationTemplate)
        );

        insertedNotificationTemplate = returnedNotificationTemplate;
    }

    @Test
    @Transactional
    void createNotificationTemplateWithExistingId() throws Exception {
        // Create the NotificationTemplate with an existing ID
        notificationTemplate.setId(1L);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setName(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSubjectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setSubject(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setIsActive(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        notificationTemplate.setCreatedDate(null);

        // Create the NotificationTemplate, which fails.
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        restNotificationTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllNotificationTemplates() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get all the notificationTemplateList
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notificationTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].bodyTemplate").value(hasItem(DEFAULT_BODY_TEMPLATE)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].channel").value(hasItem(DEFAULT_CHANNEL.toString())))
            .andExpect(jsonPath("$.[*].variables").value(hasItem(DEFAULT_VARIABLES)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        // Get the notificationTemplate
        restNotificationTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, notificationTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notificationTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.bodyTemplate").value(DEFAULT_BODY_TEMPLATE))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.channel").value(DEFAULT_CHANNEL.toString()))
            .andExpect(jsonPath("$.variables").value(DEFAULT_VARIABLES))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingNotificationTemplate() throws Exception {
        // Get the notificationTemplate
        restNotificationTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate
        NotificationTemplate updatedNotificationTemplate = notificationTemplateRepository
            .findById(notificationTemplate.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedNotificationTemplate are not directly saved in db
        em.detach(updatedNotificationTemplate);
        updatedNotificationTemplate
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .variables(UPDATED_VARIABLES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(updatedNotificationTemplate);

        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedNotificationTemplateToMatchAllProperties(updatedNotificationTemplate);
    }

    @Test
    @Transactional
    void putNonExistingNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(notificationTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNotificationTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate using partial update
        NotificationTemplate partialUpdatedNotificationTemplate = new NotificationTemplate();
        partialUpdatedNotificationTemplate.setId(notificationTemplate.getId());

        partialUpdatedNotificationTemplate
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTemplate))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedNotificationTemplate, notificationTemplate),
            getPersistedNotificationTemplate(notificationTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateNotificationTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the notificationTemplate using partial update
        NotificationTemplate partialUpdatedNotificationTemplate = new NotificationTemplate();
        partialUpdatedNotificationTemplate.setId(notificationTemplate.getId());

        partialUpdatedNotificationTemplate
            .name(UPDATED_NAME)
            .subject(UPDATED_SUBJECT)
            .bodyTemplate(UPDATED_BODY_TEMPLATE)
            .type(UPDATED_TYPE)
            .channel(UPDATED_CHANNEL)
            .variables(UPDATED_VARIABLES)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNotificationTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedNotificationTemplate))
            )
            .andExpect(status().isOk());

        // Validate the NotificationTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertNotificationTemplateUpdatableFieldsEquals(
            partialUpdatedNotificationTemplate,
            getPersistedNotificationTemplate(partialUpdatedNotificationTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, notificationTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNotificationTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        notificationTemplate.setId(longCount.incrementAndGet());

        // Create the NotificationTemplate
        NotificationTemplateDTO notificationTemplateDTO = notificationTemplateMapper.toDto(notificationTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNotificationTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(notificationTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NotificationTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNotificationTemplate() throws Exception {
        // Initialize the database
        insertedNotificationTemplate = notificationTemplateRepository.saveAndFlush(notificationTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the notificationTemplate
        restNotificationTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, notificationTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return notificationTemplateRepository.count();
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

    protected NotificationTemplate getPersistedNotificationTemplate(NotificationTemplate notificationTemplate) {
        return notificationTemplateRepository.findById(notificationTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedNotificationTemplateToMatchAllProperties(NotificationTemplate expectedNotificationTemplate) {
        assertNotificationTemplateAllPropertiesEquals(
            expectedNotificationTemplate,
            getPersistedNotificationTemplate(expectedNotificationTemplate)
        );
    }

    protected void assertPersistedNotificationTemplateToMatchUpdatableProperties(NotificationTemplate expectedNotificationTemplate) {
        assertNotificationTemplateAllUpdatablePropertiesEquals(
            expectedNotificationTemplate,
            getPersistedNotificationTemplate(expectedNotificationTemplate)
        );
    }
}
