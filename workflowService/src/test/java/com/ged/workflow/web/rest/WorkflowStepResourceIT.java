package com.ged.workflow.web.rest;

import static com.ged.workflow.domain.WorkflowStepAsserts.*;
import static com.ged.workflow.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.workflow.IntegrationTest;
import com.ged.workflow.domain.Workflow;
import com.ged.workflow.domain.WorkflowStep;
import com.ged.workflow.domain.enumeration.AssigneeType;
import com.ged.workflow.domain.enumeration.WorkflowStepType;
import com.ged.workflow.repository.WorkflowStepRepository;
import com.ged.workflow.service.dto.WorkflowStepDTO;
import com.ged.workflow.service.mapper.WorkflowStepMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link WorkflowStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WorkflowStepResourceIT {

    private static final Integer DEFAULT_STEP_NUMBER = 1;
    private static final Integer UPDATED_STEP_NUMBER = 2;

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final WorkflowStepType DEFAULT_STEP_TYPE = WorkflowStepType.APPROVAL;
    private static final WorkflowStepType UPDATED_STEP_TYPE = WorkflowStepType.REVIEW;

    private static final AssigneeType DEFAULT_ASSIGNEE_TYPE = AssigneeType.USER;
    private static final AssigneeType UPDATED_ASSIGNEE_TYPE = AssigneeType.GROUP;

    private static final String DEFAULT_ASSIGNEE_ID = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ASSIGNEE_GROUP = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNEE_GROUP = "BBBBBBBBBB";

    private static final Integer DEFAULT_DUE_IN_DAYS = 1;
    private static final Integer UPDATED_DUE_IN_DAYS = 2;

    private static final Boolean DEFAULT_IS_REQUIRED = false;
    private static final Boolean UPDATED_IS_REQUIRED = true;

    private static final Boolean DEFAULT_CAN_DELEGATE = false;
    private static final Boolean UPDATED_CAN_DELEGATE = true;

    private static final Boolean DEFAULT_CAN_REJECT = false;
    private static final Boolean UPDATED_CAN_REJECT = true;

    private static final String DEFAULT_CONFIGURATION = "AAAAAAAAAA";
    private static final String UPDATED_CONFIGURATION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/workflow-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private WorkflowStepRepository workflowStepRepository;

    @Autowired
    private WorkflowStepMapper workflowStepMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWorkflowStepMockMvc;

    private WorkflowStep workflowStep;

    private WorkflowStep insertedWorkflowStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createEntity(EntityManager em) {
        WorkflowStep workflowStep = new WorkflowStep()
            .stepNumber(DEFAULT_STEP_NUMBER)
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .stepType(DEFAULT_STEP_TYPE)
            .assigneeType(DEFAULT_ASSIGNEE_TYPE)
            .assigneeId(DEFAULT_ASSIGNEE_ID)
            .assigneeGroup(DEFAULT_ASSIGNEE_GROUP)
            .dueInDays(DEFAULT_DUE_IN_DAYS)
            .isRequired(DEFAULT_IS_REQUIRED)
            .canDelegate(DEFAULT_CAN_DELEGATE)
            .canReject(DEFAULT_CAN_REJECT)
            .configuration(DEFAULT_CONFIGURATION);
        // Add required entity
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflow = WorkflowResourceIT.createEntity();
            em.persist(workflow);
            em.flush();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        workflowStep.setWorkflow(workflow);
        return workflowStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WorkflowStep createUpdatedEntity(EntityManager em) {
        WorkflowStep updatedWorkflowStep = new WorkflowStep()
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);
        // Add required entity
        Workflow workflow;
        if (TestUtil.findAll(em, Workflow.class).isEmpty()) {
            workflow = WorkflowResourceIT.createUpdatedEntity();
            em.persist(workflow);
            em.flush();
        } else {
            workflow = TestUtil.findAll(em, Workflow.class).get(0);
        }
        updatedWorkflowStep.setWorkflow(workflow);
        return updatedWorkflowStep;
    }

    @BeforeEach
    void initTest() {
        workflowStep = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedWorkflowStep != null) {
            workflowStepRepository.delete(insertedWorkflowStep);
            insertedWorkflowStep = null;
        }
    }

    @Test
    @Transactional
    void createWorkflowStep() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);
        var returnedWorkflowStepDTO = om.readValue(
            restWorkflowStepMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            WorkflowStepDTO.class
        );

        // Validate the WorkflowStep in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedWorkflowStep = workflowStepMapper.toEntity(returnedWorkflowStepDTO);
        assertWorkflowStepUpdatableFieldsEquals(returnedWorkflowStep, getPersistedWorkflowStep(returnedWorkflowStep));

        insertedWorkflowStep = returnedWorkflowStep;
    }

    @Test
    @Transactional
    void createWorkflowStepWithExistingId() throws Exception {
        // Create the WorkflowStep with an existing ID
        workflowStep.setId(1L);
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkStepNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setStepNumber(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setName(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStepTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setStepType(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssigneeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setAssigneeType(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setIsRequired(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanDelegateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setCanDelegate(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCanRejectIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        workflowStep.setCanReject(null);

        // Create the WorkflowStep, which fails.
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        restWorkflowStepMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWorkflowSteps() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get all the workflowStepList
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(workflowStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].stepNumber").value(hasItem(DEFAULT_STEP_NUMBER)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].stepType").value(hasItem(DEFAULT_STEP_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeType").value(hasItem(DEFAULT_ASSIGNEE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].assigneeId").value(hasItem(DEFAULT_ASSIGNEE_ID)))
            .andExpect(jsonPath("$.[*].assigneeGroup").value(hasItem(DEFAULT_ASSIGNEE_GROUP)))
            .andExpect(jsonPath("$.[*].dueInDays").value(hasItem(DEFAULT_DUE_IN_DAYS)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].canReject").value(hasItem(DEFAULT_CAN_REJECT)))
            .andExpect(jsonPath("$.[*].configuration").value(hasItem(DEFAULT_CONFIGURATION)));
    }

    @Test
    @Transactional
    void getWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        // Get the workflowStep
        restWorkflowStepMockMvc
            .perform(get(ENTITY_API_URL_ID, workflowStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(workflowStep.getId().intValue()))
            .andExpect(jsonPath("$.stepNumber").value(DEFAULT_STEP_NUMBER))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.stepType").value(DEFAULT_STEP_TYPE.toString()))
            .andExpect(jsonPath("$.assigneeType").value(DEFAULT_ASSIGNEE_TYPE.toString()))
            .andExpect(jsonPath("$.assigneeId").value(DEFAULT_ASSIGNEE_ID))
            .andExpect(jsonPath("$.assigneeGroup").value(DEFAULT_ASSIGNEE_GROUP))
            .andExpect(jsonPath("$.dueInDays").value(DEFAULT_DUE_IN_DAYS))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.canDelegate").value(DEFAULT_CAN_DELEGATE))
            .andExpect(jsonPath("$.canReject").value(DEFAULT_CAN_REJECT))
            .andExpect(jsonPath("$.configuration").value(DEFAULT_CONFIGURATION));
    }

    @Test
    @Transactional
    void getNonExistingWorkflowStep() throws Exception {
        // Get the workflowStep
        restWorkflowStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowStep
        WorkflowStep updatedWorkflowStep = workflowStepRepository.findById(workflowStep.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedWorkflowStep are not directly saved in db
        em.detach(updatedWorkflowStep);
        updatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(updatedWorkflowStep);

        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedWorkflowStepToMatchAllProperties(updatedWorkflowStep);
    }

    @Test
    @Transactional
    void putNonExistingWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWorkflowStepWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowStep using partial update
        WorkflowStep partialUpdatedWorkflowStep = new WorkflowStep();
        partialUpdatedWorkflowStep.setId(workflowStep.getId());

        partialUpdatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);

        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowStepUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedWorkflowStep, workflowStep),
            getPersistedWorkflowStep(workflowStep)
        );
    }

    @Test
    @Transactional
    void fullUpdateWorkflowStepWithPatch() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the workflowStep using partial update
        WorkflowStep partialUpdatedWorkflowStep = new WorkflowStep();
        partialUpdatedWorkflowStep.setId(workflowStep.getId());

        partialUpdatedWorkflowStep
            .stepNumber(UPDATED_STEP_NUMBER)
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .stepType(UPDATED_STEP_TYPE)
            .assigneeType(UPDATED_ASSIGNEE_TYPE)
            .assigneeId(UPDATED_ASSIGNEE_ID)
            .assigneeGroup(UPDATED_ASSIGNEE_GROUP)
            .dueInDays(UPDATED_DUE_IN_DAYS)
            .isRequired(UPDATED_IS_REQUIRED)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .canReject(UPDATED_CAN_REJECT)
            .configuration(UPDATED_CONFIGURATION);

        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWorkflowStep.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedWorkflowStep))
            )
            .andExpect(status().isOk());

        // Validate the WorkflowStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertWorkflowStepUpdatableFieldsEquals(partialUpdatedWorkflowStep, getPersistedWorkflowStep(partialUpdatedWorkflowStep));
    }

    @Test
    @Transactional
    void patchNonExistingWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, workflowStepDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(workflowStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWorkflowStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        workflowStep.setId(longCount.incrementAndGet());

        // Create the WorkflowStep
        WorkflowStepDTO workflowStepDTO = workflowStepMapper.toDto(workflowStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWorkflowStepMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(workflowStepDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WorkflowStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWorkflowStep() throws Exception {
        // Initialize the database
        insertedWorkflowStep = workflowStepRepository.saveAndFlush(workflowStep);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the workflowStep
        restWorkflowStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, workflowStep.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return workflowStepRepository.count();
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

    protected WorkflowStep getPersistedWorkflowStep(WorkflowStep workflowStep) {
        return workflowStepRepository.findById(workflowStep.getId()).orElseThrow();
    }

    protected void assertPersistedWorkflowStepToMatchAllProperties(WorkflowStep expectedWorkflowStep) {
        assertWorkflowStepAllPropertiesEquals(expectedWorkflowStep, getPersistedWorkflowStep(expectedWorkflowStep));
    }

    protected void assertPersistedWorkflowStepToMatchUpdatableProperties(WorkflowStep expectedWorkflowStep) {
        assertWorkflowStepAllUpdatablePropertiesEquals(expectedWorkflowStep, getPersistedWorkflowStep(expectedWorkflowStep));
    }
}
