package fr.smartprod.paperdms.notification.web.rest;

import static fr.smartprod.paperdms.notification.domain.WebhookLogAsserts.*;
import static fr.smartprod.paperdms.notification.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.notification.IntegrationTest;
import fr.smartprod.paperdms.notification.domain.WebhookLog;
import fr.smartprod.paperdms.notification.domain.WebhookSubscription;
import fr.smartprod.paperdms.notification.repository.WebhookLogRepository;
import fr.smartprod.paperdms.notification.service.dto.WebhookLogDTO;
import fr.smartprod.paperdms.notification.service.mapper.WebhookLogMapper;
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
 * Integration tests for the {@link WebhookLogResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WebhookLogResourceIT {

    private static final Long DEFAULT_SUBSCRIPTION_ID = 1L;
    private static final Long UPDATED_SUBSCRIPTION_ID = 2L;

    private static final String DEFAULT_EVENT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_EVENT_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_PAYLOAD = "AAAAAAAAAA";
    private static final String UPDATED_PAYLOAD = "BBBBBBBBBB";

    private static final Integer DEFAULT_RESPONSE_STATUS = 1;
    private static final Integer UPDATED_RESPONSE_STATUS = 2;

    private static final String DEFAULT_RESPONSE_BODY = "AAAAAAAAAA";
    private static final String UPDATED_RESPONSE_BODY = "BBBBBBBBBB";

    private static final Long DEFAULT_RESPONSE_TIME = 1L;
    private static final Long UPDATED_RESPONSE_TIME = 2L;

    private static final Integer DEFAULT_ATTEMPT_NUMBER = 1;
    private static final Integer UPDATED_ATTEMPT_NUMBER = 2;

    private static final Boolean DEFAULT_IS_SUCCESS = false;
    private static final Boolean UPDATED_IS_SUCCESS = true;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Instant DEFAULT_SENT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SENT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/webhook-logs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WebhookLogRepository webhookLogRepository;

    @Autowired
    private WebhookLogMapper webhookLogMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebhookLogMockMvc;

    private WebhookLog webhookLog;

    private WebhookLog insertedWebhookLog;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebhookLog createEntity(EntityManager em) {
        WebhookLog webhookLog = new WebhookLog()
            .subscriptionId(DEFAULT_SUBSCRIPTION_ID)
            .eventType(DEFAULT_EVENT_TYPE)
            .payload(DEFAULT_PAYLOAD)
            .responseStatus(DEFAULT_RESPONSE_STATUS)
            .responseBody(DEFAULT_RESPONSE_BODY)
            .responseTime(DEFAULT_RESPONSE_TIME)
            .attemptNumber(DEFAULT_ATTEMPT_NUMBER)
            .isSuccess(DEFAULT_IS_SUCCESS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .sentDate(DEFAULT_SENT_DATE);
        // Add required entity
        WebhookSubscription webhookSubscription;
        if (TestUtil.findAll(em, WebhookSubscription.class).isEmpty()) {
            webhookSubscription = WebhookSubscriptionResourceIT.createEntity();
            em.persist(webhookSubscription);
            em.flush();
        } else {
            webhookSubscription = TestUtil.findAll(em, WebhookSubscription.class).get(0);
        }
        webhookLog.setSubscription(webhookSubscription);
        return webhookLog;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebhookLog createUpdatedEntity(EntityManager em) {
        WebhookLog updatedWebhookLog = new WebhookLog()
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);
        // Add required entity
        WebhookSubscription webhookSubscription;
        if (TestUtil.findAll(em, WebhookSubscription.class).isEmpty()) {
            webhookSubscription = WebhookSubscriptionResourceIT.createUpdatedEntity();
            em.persist(webhookSubscription);
            em.flush();
        } else {
            webhookSubscription = TestUtil.findAll(em, WebhookSubscription.class).get(0);
        }
        updatedWebhookLog.setSubscription(webhookSubscription);
        return updatedWebhookLog;
    }

    @BeforeEach
    void initTest() {
        webhookLog = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWebhookLog != null) {
            webhookLogRepository.delete(insertedWebhookLog);
            insertedWebhookLog = null;
        }
    }

    @Test
    @Transactional
    void createWebhookLog() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);
        var returnedWebhookLogDTO = om.readValue(
            restWebhookLogMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WebhookLogDTO.class
        );

        // Validate the WebhookLog in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWebhookLog = webhookLogMapper.toEntity(returnedWebhookLogDTO);
        assertWebhookLogUpdatableFieldsEquals(returnedWebhookLog, getPersistedWebhookLog(returnedWebhookLog));

        insertedWebhookLog = returnedWebhookLog;
    }

    @Test
    @Transactional
    void createWebhookLogWithExistingId() throws Exception {
        // Create the WebhookLog with an existing ID
        webhookLog.setId(1L);
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSubscriptionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookLog.setSubscriptionId(null);

        // Create the WebhookLog, which fails.
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        restWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEventTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookLog.setEventType(null);

        // Create the WebhookLog, which fails.
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        restWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSuccessIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookLog.setIsSuccess(null);

        // Create the WebhookLog, which fails.
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        restWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSentDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        webhookLog.setSentDate(null);

        // Create the WebhookLog, which fails.
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        restWebhookLogMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWebhookLogs() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        // Get all the webhookLogList
        restWebhookLogMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webhookLog.getId().intValue())))
            .andExpect(jsonPath("$.[*].subscriptionId").value(hasItem(DEFAULT_SUBSCRIPTION_ID.intValue())))
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
    void getWebhookLog() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        // Get the webhookLog
        restWebhookLogMockMvc
            .perform(get(ENTITY_API_URL_ID, webhookLog.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webhookLog.getId().intValue()))
            .andExpect(jsonPath("$.subscriptionId").value(DEFAULT_SUBSCRIPTION_ID.intValue()))
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
    void getNonExistingWebhookLog() throws Exception {
        // Get the webhookLog
        restWebhookLogMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWebhookLog() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookLog
        WebhookLog updatedWebhookLog = webhookLogRepository.findById(webhookLog.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWebhookLog are not directly saved in db
        em.detach(updatedWebhookLog);
        updatedWebhookLog
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(updatedWebhookLog);

        restWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webhookLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookLogDTO))
            )
            .andExpect(status().isOk());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWebhookLogToMatchAllProperties(updatedWebhookLog);
    }

    @Test
    @Transactional
    void putNonExistingWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, webhookLogDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(webhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWebhookLogWithPatch() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookLog using partial update
        WebhookLog partialUpdatedWebhookLog = new WebhookLog();
        partialUpdatedWebhookLog.setId(webhookLog.getId());

        partialUpdatedWebhookLog
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS);

        restWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebhookLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWebhookLog))
            )
            .andExpect(status().isOk());

        // Validate the WebhookLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWebhookLogUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWebhookLog, webhookLog),
            getPersistedWebhookLog(webhookLog)
        );
    }

    @Test
    @Transactional
    void fullUpdateWebhookLogWithPatch() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the webhookLog using partial update
        WebhookLog partialUpdatedWebhookLog = new WebhookLog();
        partialUpdatedWebhookLog.setId(webhookLog.getId());

        partialUpdatedWebhookLog
            .subscriptionId(UPDATED_SUBSCRIPTION_ID)
            .eventType(UPDATED_EVENT_TYPE)
            .payload(UPDATED_PAYLOAD)
            .responseStatus(UPDATED_RESPONSE_STATUS)
            .responseBody(UPDATED_RESPONSE_BODY)
            .responseTime(UPDATED_RESPONSE_TIME)
            .attemptNumber(UPDATED_ATTEMPT_NUMBER)
            .isSuccess(UPDATED_IS_SUCCESS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sentDate(UPDATED_SENT_DATE);

        restWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWebhookLog.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWebhookLog))
            )
            .andExpect(status().isOk());

        // Validate the WebhookLog in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWebhookLogUpdatableFieldsEquals(partialUpdatedWebhookLog, getPersistedWebhookLog(partialUpdatedWebhookLog));
    }

    @Test
    @Transactional
    void patchNonExistingWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, webhookLogDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(webhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(webhookLogDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWebhookLog() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        webhookLog.setId(longCount.incrementAndGet());

        // Create the WebhookLog
        WebhookLogDTO webhookLogDTO = webhookLogMapper.toDto(webhookLog);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWebhookLogMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(webhookLogDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WebhookLog in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWebhookLog() throws Exception {
        // Initialize the database
        insertedWebhookLog = webhookLogRepository.saveAndFlush(webhookLog);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the webhookLog
        restWebhookLogMockMvc
            .perform(delete(ENTITY_API_URL_ID, webhookLog.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return webhookLogRepository.count();
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

    protected WebhookLog getPersistedWebhookLog(WebhookLog webhookLog) {
        return webhookLogRepository.findById(webhookLog.getId()).orElseThrow();
    }

    protected void assertPersistedWebhookLogToMatchAllProperties(WebhookLog expectedWebhookLog) {
        assertWebhookLogAllPropertiesEquals(expectedWebhookLog, getPersistedWebhookLog(expectedWebhookLog));
    }

    protected void assertPersistedWebhookLogToMatchUpdatableProperties(WebhookLog expectedWebhookLog) {
        assertWebhookLogAllUpdatablePropertiesEquals(expectedWebhookLog, getPersistedWebhookLog(expectedWebhookLog));
    }
}
