package com.ged.ai.web.rest;

import static com.ged.ai.domain.CorrespondentExtractionAsserts.*;
import static com.ged.ai.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ged.ai.IntegrationTest;
import com.ged.ai.domain.CorrespondentExtraction;
import com.ged.ai.domain.enumeration.AiJobStatus;
import com.ged.ai.repository.CorrespondentExtractionRepository;
import com.ged.ai.service.dto.CorrespondentExtractionDTO;
import com.ged.ai.service.mapper.CorrespondentExtractionMapper;
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
 * Integration tests for the {@link CorrespondentExtractionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CorrespondentExtractionResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_EXTRACTED_TEXT = "AAAAAAAAAA";
    private static final String UPDATED_EXTRACTED_TEXT = "BBBBBBBBBB";

    private static final AiJobStatus DEFAULT_STATUS = AiJobStatus.PENDING;
    private static final AiJobStatus UPDATED_STATUS = AiJobStatus.IN_PROGRESS;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final Integer DEFAULT_SENDERS_COUNT = 1;
    private static final Integer UPDATED_SENDERS_COUNT = 2;
    private static final Integer SMALLER_SENDERS_COUNT = 1 - 1;

    private static final Integer DEFAULT_RECIPIENTS_COUNT = 1;
    private static final Integer UPDATED_RECIPIENTS_COUNT = 2;
    private static final Integer SMALLER_RECIPIENTS_COUNT = 1 - 1;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/correspondent-extractions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private CorrespondentExtractionRepository correspondentExtractionRepository;

    @Autowired
    private CorrespondentExtractionMapper correspondentExtractionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCorrespondentExtractionMockMvc;

    private CorrespondentExtraction correspondentExtraction;

    private CorrespondentExtraction insertedCorrespondentExtraction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorrespondentExtraction createEntity() {
        return new CorrespondentExtraction()
            .documentId(DEFAULT_DOCUMENT_ID)
            .extractedText(DEFAULT_EXTRACTED_TEXT)
            .status(DEFAULT_STATUS)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .sendersCount(DEFAULT_SENDERS_COUNT)
            .recipientsCount(DEFAULT_RECIPIENTS_COUNT)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CorrespondentExtraction createUpdatedEntity() {
        return new CorrespondentExtraction()
            .documentId(UPDATED_DOCUMENT_ID)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sendersCount(UPDATED_SENDERS_COUNT)
            .recipientsCount(UPDATED_RECIPIENTS_COUNT)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        correspondentExtraction = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedCorrespondentExtraction != null) {
            correspondentExtractionRepository.delete(insertedCorrespondentExtraction);
            insertedCorrespondentExtraction = null;
        }
    }

    @Test
    @Transactional
    void createCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);
        var returnedCorrespondentExtractionDTO = om.readValue(
            restCorrespondentExtractionMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            CorrespondentExtractionDTO.class
        );

        // Validate the CorrespondentExtraction in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedCorrespondentExtraction = correspondentExtractionMapper.toEntity(returnedCorrespondentExtractionDTO);
        assertCorrespondentExtractionUpdatableFieldsEquals(
            returnedCorrespondentExtraction,
            getPersistedCorrespondentExtraction(returnedCorrespondentExtraction)
        );

        insertedCorrespondentExtraction = returnedCorrespondentExtraction;
    }

    @Test
    @Transactional
    void createCorrespondentExtractionWithExistingId() throws Exception {
        // Create the CorrespondentExtraction with an existing ID
        correspondentExtraction.setId(1L);
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCorrespondentExtractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        correspondentExtraction.setDocumentId(null);

        // Create the CorrespondentExtraction, which fails.
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        restCorrespondentExtractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        correspondentExtraction.setStatus(null);

        // Create the CorrespondentExtraction, which fails.
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        restCorrespondentExtractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        correspondentExtraction.setCreatedDate(null);

        // Create the CorrespondentExtraction, which fails.
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        restCorrespondentExtractionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractions() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondentExtraction.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].sendersCount").value(hasItem(DEFAULT_SENDERS_COUNT)))
            .andExpect(jsonPath("$.[*].recipientsCount").value(hasItem(DEFAULT_RECIPIENTS_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getCorrespondentExtraction() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get the correspondentExtraction
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL_ID, correspondentExtraction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(correspondentExtraction.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.extractedText").value(DEFAULT_EXTRACTED_TEXT))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.sendersCount").value(DEFAULT_SENDERS_COUNT))
            .andExpect(jsonPath("$.recipientsCount").value(DEFAULT_RECIPIENTS_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getCorrespondentExtractionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        Long id = correspondentExtraction.getId();

        defaultCorrespondentExtractionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultCorrespondentExtractionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultCorrespondentExtractionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId equals to
        defaultCorrespondentExtractionFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId in
        defaultCorrespondentExtractionFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId is not null
        defaultCorrespondentExtractionFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId is greater than or equal to
        defaultCorrespondentExtractionFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId is less than or equal to
        defaultCorrespondentExtractionFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId is less than
        defaultCorrespondentExtractionFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where documentId is greater than
        defaultCorrespondentExtractionFiltering(
            "documentId.greaterThan=" + SMALLER_DOCUMENT_ID,
            "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where status equals to
        defaultCorrespondentExtractionFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where status in
        defaultCorrespondentExtractionFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where status is not null
        defaultCorrespondentExtractionFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where startDate equals to
        defaultCorrespondentExtractionFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where startDate in
        defaultCorrespondentExtractionFiltering(
            "startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE,
            "startDate.in=" + UPDATED_START_DATE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where startDate is not null
        defaultCorrespondentExtractionFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where endDate equals to
        defaultCorrespondentExtractionFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where endDate in
        defaultCorrespondentExtractionFiltering(
            "endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE,
            "endDate.in=" + UPDATED_END_DATE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where endDate is not null
        defaultCorrespondentExtractionFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount equals to
        defaultCorrespondentExtractionFiltering(
            "sendersCount.equals=" + DEFAULT_SENDERS_COUNT,
            "sendersCount.equals=" + UPDATED_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount in
        defaultCorrespondentExtractionFiltering(
            "sendersCount.in=" + DEFAULT_SENDERS_COUNT + "," + UPDATED_SENDERS_COUNT,
            "sendersCount.in=" + UPDATED_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount is not null
        defaultCorrespondentExtractionFiltering("sendersCount.specified=true", "sendersCount.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount is greater than or equal to
        defaultCorrespondentExtractionFiltering(
            "sendersCount.greaterThanOrEqual=" + DEFAULT_SENDERS_COUNT,
            "sendersCount.greaterThanOrEqual=" + UPDATED_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount is less than or equal to
        defaultCorrespondentExtractionFiltering(
            "sendersCount.lessThanOrEqual=" + DEFAULT_SENDERS_COUNT,
            "sendersCount.lessThanOrEqual=" + SMALLER_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount is less than
        defaultCorrespondentExtractionFiltering(
            "sendersCount.lessThan=" + UPDATED_SENDERS_COUNT,
            "sendersCount.lessThan=" + DEFAULT_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsBySendersCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where sendersCount is greater than
        defaultCorrespondentExtractionFiltering(
            "sendersCount.greaterThan=" + SMALLER_SENDERS_COUNT,
            "sendersCount.greaterThan=" + DEFAULT_SENDERS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount equals to
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.equals=" + DEFAULT_RECIPIENTS_COUNT,
            "recipientsCount.equals=" + UPDATED_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount in
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.in=" + DEFAULT_RECIPIENTS_COUNT + "," + UPDATED_RECIPIENTS_COUNT,
            "recipientsCount.in=" + UPDATED_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount is not null
        defaultCorrespondentExtractionFiltering("recipientsCount.specified=true", "recipientsCount.specified=false");
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount is greater than or equal to
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.greaterThanOrEqual=" + DEFAULT_RECIPIENTS_COUNT,
            "recipientsCount.greaterThanOrEqual=" + UPDATED_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount is less than or equal to
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.lessThanOrEqual=" + DEFAULT_RECIPIENTS_COUNT,
            "recipientsCount.lessThanOrEqual=" + SMALLER_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount is less than
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.lessThan=" + UPDATED_RECIPIENTS_COUNT,
            "recipientsCount.lessThan=" + DEFAULT_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByRecipientsCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where recipientsCount is greater than
        defaultCorrespondentExtractionFiltering(
            "recipientsCount.greaterThan=" + SMALLER_RECIPIENTS_COUNT,
            "recipientsCount.greaterThan=" + DEFAULT_RECIPIENTS_COUNT
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where createdDate equals to
        defaultCorrespondentExtractionFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where createdDate in
        defaultCorrespondentExtractionFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllCorrespondentExtractionsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        // Get all the correspondentExtractionList where createdDate is not null
        defaultCorrespondentExtractionFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultCorrespondentExtractionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultCorrespondentExtractionShouldBeFound(shouldBeFound);
        defaultCorrespondentExtractionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCorrespondentExtractionShouldBeFound(String filter) throws Exception {
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(correspondentExtraction.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].extractedText").value(hasItem(DEFAULT_EXTRACTED_TEXT)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].sendersCount").value(hasItem(DEFAULT_SENDERS_COUNT)))
            .andExpect(jsonPath("$.[*].recipientsCount").value(hasItem(DEFAULT_RECIPIENTS_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCorrespondentExtractionShouldNotBeFound(String filter) throws Exception {
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCorrespondentExtractionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingCorrespondentExtraction() throws Exception {
        // Get the correspondentExtraction
        restCorrespondentExtractionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingCorrespondentExtraction() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the correspondentExtraction
        CorrespondentExtraction updatedCorrespondentExtraction = correspondentExtractionRepository
            .findById(correspondentExtraction.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedCorrespondentExtraction are not directly saved in db
        em.detach(updatedCorrespondentExtraction);
        updatedCorrespondentExtraction
            .documentId(UPDATED_DOCUMENT_ID)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sendersCount(UPDATED_SENDERS_COUNT)
            .recipientsCount(UPDATED_RECIPIENTS_COUNT)
            .createdDate(UPDATED_CREATED_DATE);
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(updatedCorrespondentExtraction);

        restCorrespondentExtractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, correspondentExtractionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isOk());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedCorrespondentExtractionToMatchAllProperties(updatedCorrespondentExtraction);
    }

    @Test
    @Transactional
    void putNonExistingCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, correspondentExtractionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(correspondentExtractionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCorrespondentExtractionWithPatch() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the correspondentExtraction using partial update
        CorrespondentExtraction partialUpdatedCorrespondentExtraction = new CorrespondentExtraction();
        partialUpdatedCorrespondentExtraction.setId(correspondentExtraction.getId());

        partialUpdatedCorrespondentExtraction
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sendersCount(UPDATED_SENDERS_COUNT)
            .recipientsCount(UPDATED_RECIPIENTS_COUNT);

        restCorrespondentExtractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrespondentExtraction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorrespondentExtraction))
            )
            .andExpect(status().isOk());

        // Validate the CorrespondentExtraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorrespondentExtractionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedCorrespondentExtraction, correspondentExtraction),
            getPersistedCorrespondentExtraction(correspondentExtraction)
        );
    }

    @Test
    @Transactional
    void fullUpdateCorrespondentExtractionWithPatch() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the correspondentExtraction using partial update
        CorrespondentExtraction partialUpdatedCorrespondentExtraction = new CorrespondentExtraction();
        partialUpdatedCorrespondentExtraction.setId(correspondentExtraction.getId());

        partialUpdatedCorrespondentExtraction
            .documentId(UPDATED_DOCUMENT_ID)
            .extractedText(UPDATED_EXTRACTED_TEXT)
            .status(UPDATED_STATUS)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .sendersCount(UPDATED_SENDERS_COUNT)
            .recipientsCount(UPDATED_RECIPIENTS_COUNT)
            .createdDate(UPDATED_CREATED_DATE);

        restCorrespondentExtractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCorrespondentExtraction.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedCorrespondentExtraction))
            )
            .andExpect(status().isOk());

        // Validate the CorrespondentExtraction in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertCorrespondentExtractionUpdatableFieldsEquals(
            partialUpdatedCorrespondentExtraction,
            getPersistedCorrespondentExtraction(partialUpdatedCorrespondentExtraction)
        );
    }

    @Test
    @Transactional
    void patchNonExistingCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, correspondentExtractionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCorrespondentExtraction() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        correspondentExtraction.setId(longCount.incrementAndGet());

        // Create the CorrespondentExtraction
        CorrespondentExtractionDTO correspondentExtractionDTO = correspondentExtractionMapper.toDto(correspondentExtraction);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCorrespondentExtractionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(correspondentExtractionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CorrespondentExtraction in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCorrespondentExtraction() throws Exception {
        // Initialize the database
        insertedCorrespondentExtraction = correspondentExtractionRepository.saveAndFlush(correspondentExtraction);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the correspondentExtraction
        restCorrespondentExtractionMockMvc
            .perform(delete(ENTITY_API_URL_ID, correspondentExtraction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return correspondentExtractionRepository.count();
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

    protected CorrespondentExtraction getPersistedCorrespondentExtraction(CorrespondentExtraction correspondentExtraction) {
        return correspondentExtractionRepository.findById(correspondentExtraction.getId()).orElseThrow();
    }

    protected void assertPersistedCorrespondentExtractionToMatchAllProperties(CorrespondentExtraction expectedCorrespondentExtraction) {
        assertCorrespondentExtractionAllPropertiesEquals(
            expectedCorrespondentExtraction,
            getPersistedCorrespondentExtraction(expectedCorrespondentExtraction)
        );
    }

    protected void assertPersistedCorrespondentExtractionToMatchUpdatableProperties(
        CorrespondentExtraction expectedCorrespondentExtraction
    ) {
        assertCorrespondentExtractionAllUpdatablePropertiesEquals(
            expectedCorrespondentExtraction,
            getPersistedCorrespondentExtraction(expectedCorrespondentExtraction)
        );
    }
}
