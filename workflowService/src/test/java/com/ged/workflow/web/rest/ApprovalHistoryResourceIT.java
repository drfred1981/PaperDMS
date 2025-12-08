package com.ged.workflow.web.rest;

import static com.ged.workflow.domain.ApprovalHistoryAsserts.*;
import static com.ged.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.workflow.IntegrationTest;
import com.ged.workflow.domain.ApprovalHistory;
import com.ged.workflow.domain.enumeration.TaskAction;
import com.ged.workflow.repository.ApprovalHistoryRepository;
import com.ged.workflow.service.dto.ApprovalHistoryDTO;
import com.ged.workflow.service.mapper.ApprovalHistoryMapper;
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
 * Integration tests for the {@link ApprovalHistoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ApprovalHistoryResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final Long DEFAULT_WORKFLOW_INSTANCE_ID = 1L;
    private static final Long UPDATED_WORKFLOW_INSTANCE_ID = 2L;

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;

    private static final TaskAction DEFAULT_ACTION = TaskAction.APPROVE;
    private static final TaskAction UPDATED_ACTION = TaskAction.REJECT;

    private static final String DEFAULT_COMMENT = "AAAAAAAAAA";
    private static final String UPDATED_COMMENT = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTION_BY = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_BY = "BBBBBBBBBB";

    private static final String DEFAULT_PREVIOUS_ASSIGNEE = "AAAAAAAAAA";
    private static final String UPDATED_PREVIOUS_ASSIGNEE = "BBBBBBBBBB";

    private static final Long DEFAULT_TIME_TAKEN = 1L;
    private static final Long UPDATED_TIME_TAKEN = 2L;

    private static final String ENTITY_API_URL = "/api/approval-histories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ApprovalHistoryRepository approvalHistoryRepository;

    @Autowired
    private ApprovalHistoryMapper approvalHistoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restApprovalHistoryMockMvc;

    private ApprovalHistory approvalHistory;

    private ApprovalHistory insertedApprovalHistory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalHistory createEntity() {
        return new ApprovalHistory()
            .documentId(DEFAULT_DOCUMENT_ID)
            .workflowInstanceId(DEFAULT_WORKFLOW_INSTANCE_ID)
            .stepNumber(DEFAULT_STEP_NUMBER)
            .action(DEFAULT_ACTION)
            .comment(DEFAULT_COMMENT)
            .actionDate(DEFAULT_ACTION_DATE)
            .actionBy(DEFAULT_ACTION_BY)
            .previousAssignee(DEFAULT_PREVIOUS_ASSIGNEE)
            .timeTaken(DEFAULT_TIME_TAKEN);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ApprovalHistory createUpdatedEntity() {
        return new ApprovalHistory()
            .documentId(UPDATED_DOCUMENT_ID)
            .workflowInstanceId(UPDATED_WORKFLOW_INSTANCE_ID)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);
    }

    @BeforeEach
    void initTest() {
        approvalHistory = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedApprovalHistory != null) {
            approvalHistoryRepository.delete(insertedApprovalHistory);
            insertedApprovalHistory = null;
        }
    }

    @Test
    @Transactional
    void createApprovalHistory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);
        var returnedApprovalHistoryDTO = om.readValue(
            restApprovalHistoryMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ApprovalHistoryDTO.class
        );

        // Validate the ApprovalHistory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedApprovalHistory = approvalHistoryMapper.toEntity(returnedApprovalHistoryDTO);
        assertApprovalHistoryUpdatableFieldsEquals(returnedApprovalHistory, getPersistedApprovalHistory(returnedApprovalHistory));

        insertedApprovalHistory = returnedApprovalHistory;
    }

    @Test
    @Transactional
    void createApprovalHistoryWithExistingId() throws Exception {
        // Create the ApprovalHistory with an existing ID
        approvalHistory.setId(1L);
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setDocumentId(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWorkflowInstanceIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setWorkflowInstanceId(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setStepNumber(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setAction(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setActionDate(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        approvalHistory.setActionBy(null);

        // Create the ApprovalHistory, which fails.
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        restApprovalHistoryMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllApprovalHistories() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        // Get all the approvalHistoryList
        restApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(approvalHistory.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].workflowInstanceId").value(hasItem(DEFAULT_WORKFLOW_INSTANCE_ID.intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].comment").value(hasItem(DEFAULT_COMMENT)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionBy").value(hasItem(DEFAULT_ACTION_BY)))
            .andExpect(jsonPath("$.[*].previousAssignee").value(hasItem(DEFAULT_PREVIOUS_ASSIGNEE)))
            .andExpect(jsonPath("$.[*].timeTaken").value(hasItem(DEFAULT_TIME_TAKEN.intValue())));
    }

    @Test
    @Transactional
    void getApprovalHistory() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        // Get the approvalHistory
        restApprovalHistoryMockMvc
            .perform(get(ENTITY_API_URL_ID, approvalHistory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(approvalHistory.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.workflowInstanceId").value(DEFAULT_WORKFLOW_INSTANCE_ID.intValue()))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.comment").value(DEFAULT_COMMENT))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.actionBy").value(DEFAULT_ACTION_BY))
            .andExpect(jsonPath("$.previousAssignee").value(DEFAULT_PREVIOUS_ASSIGNEE))
            .andExpect(jsonPath("$.timeTaken").value(DEFAULT_TIME_TAKEN.intValue()));
    }

    @Test
    @Transactional
    void getNonExistingApprovalHistory() throws Exception {
        // Get the approvalHistory
        restApprovalHistoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingApprovalHistory() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the approvalHistory
        ApprovalHistory updatedApprovalHistory = approvalHistoryRepository.findById(approvalHistory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedApprovalHistory are not directly saved in db
        em.detach(updatedApprovalHistory);
        updatedApprovalHistory
            .documentId(UPDATED_DOCUMENT_ID)
            .workflowInstanceId(UPDATED_WORKFLOW_INSTANCE_ID)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(updatedApprovalHistory);

        restApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approvalHistoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedApprovalHistoryToMatchAllProperties(updatedApprovalHistory);
    }

    @Test
    @Transactional
    void putNonExistingApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, approvalHistoryDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approvalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(approvalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateApprovalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the approvalHistory using partial update
        ApprovalHistory partialUpdatedApprovalHistory = new ApprovalHistory();
        partialUpdatedApprovalHistory.setId(approvalHistory.getId());

        partialUpdatedApprovalHistory
            .documentId(UPDATED_DOCUMENT_ID)
            .workflowInstanceId(UPDATED_WORKFLOW_INSTANCE_ID)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionBy(UPDATED_ACTION_BY)
            .timeTaken(UPDATED_TIME_TAKEN);

        restApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApprovalHistory))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApprovalHistoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedApprovalHistory, approvalHistory),
            getPersistedApprovalHistory(approvalHistory)
        );
    }

    @Test
    @Transactional
    void fullUpdateApprovalHistoryWithPatch() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the approvalHistory using partial update
        ApprovalHistory partialUpdatedApprovalHistory = new ApprovalHistory();
        partialUpdatedApprovalHistory.setId(approvalHistory.getId());

        partialUpdatedApprovalHistory
            .documentId(UPDATED_DOCUMENT_ID)
            .workflowInstanceId(UPDATED_WORKFLOW_INSTANCE_ID)
            .stepNumber(UPDATED_STEP_NUMBER)
            .action(UPDATED_ACTION)
            .comment(UPDATED_COMMENT)
            .actionDate(UPDATED_ACTION_DATE)
            .actionBy(UPDATED_ACTION_BY)
            .previousAssignee(UPDATED_PREVIOUS_ASSIGNEE)
            .timeTaken(UPDATED_TIME_TAKEN);

        restApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedApprovalHistory.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedApprovalHistory))
            )
            .andExpect(status().isOk());

        // Validate the ApprovalHistory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertApprovalHistoryUpdatableFieldsEquals(
            partialUpdatedApprovalHistory,
            getPersistedApprovalHistory(partialUpdatedApprovalHistory)
        );
    }

    @Test
    @Transactional
    void patchNonExistingApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, approvalHistoryDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(approvalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(approvalHistoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamApprovalHistory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        approvalHistory.setId(longCount.incrementAndGet());

        // Create the ApprovalHistory
        ApprovalHistoryDTO approvalHistoryDTO = approvalHistoryMapper.toDto(approvalHistory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restApprovalHistoryMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(approvalHistoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ApprovalHistory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteApprovalHistory() throws Exception {
        // Initialize the database
        insertedApprovalHistory = approvalHistoryRepository.saveAndFlush(approvalHistory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the approvalHistory
        restApprovalHistoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, approvalHistory.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return approvalHistoryRepository.count();
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

    protected ApprovalHistory getPersistedApprovalHistory(ApprovalHistory approvalHistory) {
        return approvalHistoryRepository.findById(approvalHistory.getId()).orElseThrow();
    }

    protected void assertPersistedApprovalHistoryToMatchAllProperties(ApprovalHistory expectedApprovalHistory) {
        assertApprovalHistoryAllPropertiesEquals(expectedApprovalHistory, getPersistedApprovalHistory(expectedApprovalHistory));
    }

    protected void assertPersistedApprovalHistoryToMatchUpdatableProperties(ApprovalHistory expectedApprovalHistory) {
        assertApprovalHistoryAllUpdatablePropertiesEquals(expectedApprovalHistory, getPersistedApprovalHistory(expectedApprovalHistory));
    }
}
