package fr.smartprod.paperdms.emailimport.web.rest;

import static fr.smartprod.paperdms.emailimport.domain.EmailImportAsserts.*;
import static fr.smartprod.paperdms.emailimport.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimport.IntegrationTest;
import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.domain.enumeration.ImportStatus;
import fr.smartprod.paperdms.emailimport.repository.EmailImportRepository;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.EmailImportMapper;
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
 * Integration tests for the {@link EmailImportResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailImportResourceIT {

    private static final String DEFAULT_FROM_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_FROM_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_TO_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_TO_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final String DEFAULT_BODY = "AAAAAAAAAA";
    private static final String UPDATED_BODY = "BBBBBBBBBB";

    private static final String DEFAULT_BODY_HTML = "AAAAAAAAAA";
    private static final String UPDATED_BODY_HTML = "BBBBBBBBBB";

    private static final Instant DEFAULT_RECEIVED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_RECEIVED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_PROCESSED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PROCESSED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ImportStatus DEFAULT_STATUS = ImportStatus.PENDING;
    private static final ImportStatus UPDATED_STATUS = ImportStatus.PROCESSING;

    private static final Long DEFAULT_FOLDER_ID = 1L;
    private static final Long UPDATED_FOLDER_ID = 2L;
    private static final Long SMALLER_FOLDER_ID = 1L - 1L;

    private static final Long DEFAULT_DOCUMENT_TYPE_ID = 1L;
    private static final Long UPDATED_DOCUMENT_TYPE_ID = 2L;
    private static final Long SMALLER_DOCUMENT_TYPE_ID = 1L - 1L;

    private static final Integer DEFAULT_ATTACHMENT_COUNT = 1;
    private static final Integer UPDATED_ATTACHMENT_COUNT = 2;
    private static final Integer SMALLER_ATTACHMENT_COUNT = 1 - 1;

    private static final Integer DEFAULT_DOCUMENTS_CREATED = 1;
    private static final Integer UPDATED_DOCUMENTS_CREATED = 2;
    private static final Integer SMALLER_DOCUMENTS_CREATED = 1 - 1;

    private static final Long DEFAULT_APPLIED_RULE_ID = 1L;
    private static final Long UPDATED_APPLIED_RULE_ID = 2L;
    private static final Long SMALLER_APPLIED_RULE_ID = 1L - 1L;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-imports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailImportRepository emailImportRepository;

    @Autowired
    private EmailImportMapper emailImportMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailImportMockMvc;

    private EmailImport emailImport;

    private EmailImport insertedEmailImport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImport createEntity() {
        return new EmailImport()
            .fromEmail(DEFAULT_FROM_EMAIL)
            .toEmail(DEFAULT_TO_EMAIL)
            .subject(DEFAULT_SUBJECT)
            .body(DEFAULT_BODY)
            .bodyHtml(DEFAULT_BODY_HTML)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .processedDate(DEFAULT_PROCESSED_DATE)
            .status(DEFAULT_STATUS)
            .folderId(DEFAULT_FOLDER_ID)
            .documentTypeId(DEFAULT_DOCUMENT_TYPE_ID)
            .attachmentCount(DEFAULT_ATTACHMENT_COUNT)
            .documentsCreated(DEFAULT_DOCUMENTS_CREATED)
            .appliedRuleId(DEFAULT_APPLIED_RULE_ID)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .metadata(DEFAULT_METADATA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImport createUpdatedEntity() {
        return new EmailImport()
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .appliedRuleId(UPDATED_APPLIED_RULE_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA);
    }

    @BeforeEach
    void initTest() {
        emailImport = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailImport != null) {
            emailImportRepository.delete(insertedEmailImport);
            insertedEmailImport = null;
        }
    }

    @Test
    @Transactional
    void createEmailImport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);
        var returnedEmailImportDTO = om.readValue(
            restEmailImportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailImportDTO.class
        );

        // Validate the EmailImport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailImport = emailImportMapper.toEntity(returnedEmailImportDTO);
        assertEmailImportUpdatableFieldsEquals(returnedEmailImport, getPersistedEmailImport(returnedEmailImport));

        insertedEmailImport = returnedEmailImport;
    }

    @Test
    @Transactional
    void createEmailImportWithExistingId() throws Exception {
        // Create the EmailImport with an existing ID
        emailImport.setId(1L);
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFromEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImport.setFromEmail(null);

        // Create the EmailImport, which fails.
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        restEmailImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImport.setToEmail(null);

        // Create the EmailImport, which fails.
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        restEmailImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceivedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImport.setReceivedDate(null);

        // Create the EmailImport, which fails.
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        restEmailImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImport.setStatus(null);

        // Create the EmailImport, which fails.
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        restEmailImportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailImports() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromEmail").value(hasItem(DEFAULT_FROM_EMAIL)))
            .andExpect(jsonPath("$.[*].toEmail").value(hasItem(DEFAULT_TO_EMAIL)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].bodyHtml").value(hasItem(DEFAULT_BODY_HTML)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].attachmentCount").value(hasItem(DEFAULT_ATTACHMENT_COUNT)))
            .andExpect(jsonPath("$.[*].documentsCreated").value(hasItem(DEFAULT_DOCUMENTS_CREATED)))
            .andExpect(jsonPath("$.[*].appliedRuleId").value(hasItem(DEFAULT_APPLIED_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));
    }

    @Test
    @Transactional
    void getEmailImport() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get the emailImport
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL_ID, emailImport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailImport.getId().intValue()))
            .andExpect(jsonPath("$.fromEmail").value(DEFAULT_FROM_EMAIL))
            .andExpect(jsonPath("$.toEmail").value(DEFAULT_TO_EMAIL))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.bodyHtml").value(DEFAULT_BODY_HTML))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.folderId").value(DEFAULT_FOLDER_ID.intValue()))
            .andExpect(jsonPath("$.documentTypeId").value(DEFAULT_DOCUMENT_TYPE_ID.intValue()))
            .andExpect(jsonPath("$.attachmentCount").value(DEFAULT_ATTACHMENT_COUNT))
            .andExpect(jsonPath("$.documentsCreated").value(DEFAULT_DOCUMENTS_CREATED))
            .andExpect(jsonPath("$.appliedRuleId").value(DEFAULT_APPLIED_RULE_ID.intValue()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA));
    }

    @Test
    @Transactional
    void getEmailImportsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        Long id = emailImport.getId();

        defaultEmailImportFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmailImportFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmailImportFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFromEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where fromEmail equals to
        defaultEmailImportFiltering("fromEmail.equals=" + DEFAULT_FROM_EMAIL, "fromEmail.equals=" + UPDATED_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFromEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where fromEmail in
        defaultEmailImportFiltering("fromEmail.in=" + DEFAULT_FROM_EMAIL + "," + UPDATED_FROM_EMAIL, "fromEmail.in=" + UPDATED_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFromEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where fromEmail is not null
        defaultEmailImportFiltering("fromEmail.specified=true", "fromEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByFromEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where fromEmail contains
        defaultEmailImportFiltering("fromEmail.contains=" + DEFAULT_FROM_EMAIL, "fromEmail.contains=" + UPDATED_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFromEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where fromEmail does not contain
        defaultEmailImportFiltering("fromEmail.doesNotContain=" + UPDATED_FROM_EMAIL, "fromEmail.doesNotContain=" + DEFAULT_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByToEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where toEmail equals to
        defaultEmailImportFiltering("toEmail.equals=" + DEFAULT_TO_EMAIL, "toEmail.equals=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByToEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where toEmail in
        defaultEmailImportFiltering("toEmail.in=" + DEFAULT_TO_EMAIL + "," + UPDATED_TO_EMAIL, "toEmail.in=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByToEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where toEmail is not null
        defaultEmailImportFiltering("toEmail.specified=true", "toEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByToEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where toEmail contains
        defaultEmailImportFiltering("toEmail.contains=" + DEFAULT_TO_EMAIL, "toEmail.contains=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsByToEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where toEmail does not contain
        defaultEmailImportFiltering("toEmail.doesNotContain=" + UPDATED_TO_EMAIL, "toEmail.doesNotContain=" + DEFAULT_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where subject equals to
        defaultEmailImportFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where subject in
        defaultEmailImportFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where subject is not null
        defaultEmailImportFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where subject contains
        defaultEmailImportFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where subject does not contain
        defaultEmailImportFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where receivedDate equals to
        defaultEmailImportFiltering("receivedDate.equals=" + DEFAULT_RECEIVED_DATE, "receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    void getAllEmailImportsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where receivedDate in
        defaultEmailImportFiltering(
            "receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE,
            "receivedDate.in=" + UPDATED_RECEIVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where receivedDate is not null
        defaultEmailImportFiltering("receivedDate.specified=true", "receivedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where processedDate equals to
        defaultEmailImportFiltering("processedDate.equals=" + DEFAULT_PROCESSED_DATE, "processedDate.equals=" + UPDATED_PROCESSED_DATE);
    }

    @Test
    @Transactional
    void getAllEmailImportsByProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where processedDate in
        defaultEmailImportFiltering(
            "processedDate.in=" + DEFAULT_PROCESSED_DATE + "," + UPDATED_PROCESSED_DATE,
            "processedDate.in=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where processedDate is not null
        defaultEmailImportFiltering("processedDate.specified=true", "processedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where status equals to
        defaultEmailImportFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where status in
        defaultEmailImportFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where status is not null
        defaultEmailImportFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId equals to
        defaultEmailImportFiltering("folderId.equals=" + DEFAULT_FOLDER_ID, "folderId.equals=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId in
        defaultEmailImportFiltering("folderId.in=" + DEFAULT_FOLDER_ID + "," + UPDATED_FOLDER_ID, "folderId.in=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId is not null
        defaultEmailImportFiltering("folderId.specified=true", "folderId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId is greater than or equal to
        defaultEmailImportFiltering("folderId.greaterThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.greaterThanOrEqual=" + UPDATED_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId is less than or equal to
        defaultEmailImportFiltering("folderId.lessThanOrEqual=" + DEFAULT_FOLDER_ID, "folderId.lessThanOrEqual=" + SMALLER_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId is less than
        defaultEmailImportFiltering("folderId.lessThan=" + UPDATED_FOLDER_ID, "folderId.lessThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByFolderIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where folderId is greater than
        defaultEmailImportFiltering("folderId.greaterThan=" + SMALLER_FOLDER_ID, "folderId.greaterThan=" + DEFAULT_FOLDER_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId equals to
        defaultEmailImportFiltering(
            "documentTypeId.equals=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.equals=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId in
        defaultEmailImportFiltering(
            "documentTypeId.in=" + DEFAULT_DOCUMENT_TYPE_ID + "," + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.in=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId is not null
        defaultEmailImportFiltering("documentTypeId.specified=true", "documentTypeId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId is greater than or equal to
        defaultEmailImportFiltering(
            "documentTypeId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThanOrEqual=" + UPDATED_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId is less than or equal to
        defaultEmailImportFiltering(
            "documentTypeId.lessThanOrEqual=" + DEFAULT_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThanOrEqual=" + SMALLER_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId is less than
        defaultEmailImportFiltering(
            "documentTypeId.lessThan=" + UPDATED_DOCUMENT_TYPE_ID,
            "documentTypeId.lessThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentTypeIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentTypeId is greater than
        defaultEmailImportFiltering(
            "documentTypeId.greaterThan=" + SMALLER_DOCUMENT_TYPE_ID,
            "documentTypeId.greaterThan=" + DEFAULT_DOCUMENT_TYPE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount equals to
        defaultEmailImportFiltering(
            "attachmentCount.equals=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.equals=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount in
        defaultEmailImportFiltering(
            "attachmentCount.in=" + DEFAULT_ATTACHMENT_COUNT + "," + UPDATED_ATTACHMENT_COUNT,
            "attachmentCount.in=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount is not null
        defaultEmailImportFiltering("attachmentCount.specified=true", "attachmentCount.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount is greater than or equal to
        defaultEmailImportFiltering(
            "attachmentCount.greaterThanOrEqual=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.greaterThanOrEqual=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount is less than or equal to
        defaultEmailImportFiltering(
            "attachmentCount.lessThanOrEqual=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.lessThanOrEqual=" + SMALLER_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount is less than
        defaultEmailImportFiltering(
            "attachmentCount.lessThan=" + UPDATED_ATTACHMENT_COUNT,
            "attachmentCount.lessThan=" + DEFAULT_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAttachmentCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where attachmentCount is greater than
        defaultEmailImportFiltering(
            "attachmentCount.greaterThan=" + SMALLER_ATTACHMENT_COUNT,
            "attachmentCount.greaterThan=" + DEFAULT_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated equals to
        defaultEmailImportFiltering(
            "documentsCreated.equals=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.equals=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated in
        defaultEmailImportFiltering(
            "documentsCreated.in=" + DEFAULT_DOCUMENTS_CREATED + "," + UPDATED_DOCUMENTS_CREATED,
            "documentsCreated.in=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated is not null
        defaultEmailImportFiltering("documentsCreated.specified=true", "documentsCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated is greater than or equal to
        defaultEmailImportFiltering(
            "documentsCreated.greaterThanOrEqual=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.greaterThanOrEqual=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated is less than or equal to
        defaultEmailImportFiltering(
            "documentsCreated.lessThanOrEqual=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.lessThanOrEqual=" + SMALLER_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated is less than
        defaultEmailImportFiltering(
            "documentsCreated.lessThan=" + UPDATED_DOCUMENTS_CREATED,
            "documentsCreated.lessThan=" + DEFAULT_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByDocumentsCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where documentsCreated is greater than
        defaultEmailImportFiltering(
            "documentsCreated.greaterThan=" + SMALLER_DOCUMENTS_CREATED,
            "documentsCreated.greaterThan=" + DEFAULT_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId equals to
        defaultEmailImportFiltering("appliedRuleId.equals=" + DEFAULT_APPLIED_RULE_ID, "appliedRuleId.equals=" + UPDATED_APPLIED_RULE_ID);
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId in
        defaultEmailImportFiltering(
            "appliedRuleId.in=" + DEFAULT_APPLIED_RULE_ID + "," + UPDATED_APPLIED_RULE_ID,
            "appliedRuleId.in=" + UPDATED_APPLIED_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId is not null
        defaultEmailImportFiltering("appliedRuleId.specified=true", "appliedRuleId.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId is greater than or equal to
        defaultEmailImportFiltering(
            "appliedRuleId.greaterThanOrEqual=" + DEFAULT_APPLIED_RULE_ID,
            "appliedRuleId.greaterThanOrEqual=" + UPDATED_APPLIED_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId is less than or equal to
        defaultEmailImportFiltering(
            "appliedRuleId.lessThanOrEqual=" + DEFAULT_APPLIED_RULE_ID,
            "appliedRuleId.lessThanOrEqual=" + SMALLER_APPLIED_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId is less than
        defaultEmailImportFiltering(
            "appliedRuleId.lessThan=" + UPDATED_APPLIED_RULE_ID,
            "appliedRuleId.lessThan=" + DEFAULT_APPLIED_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        // Get all the emailImportList where appliedRuleId is greater than
        defaultEmailImportFiltering(
            "appliedRuleId.greaterThan=" + SMALLER_APPLIED_RULE_ID,
            "appliedRuleId.greaterThan=" + DEFAULT_APPLIED_RULE_ID
        );
    }

    @Test
    @Transactional
    void getAllEmailImportsByAppliedRuleIsEqualToSomething() throws Exception {
        ImportRule appliedRule;
        if (TestUtil.findAll(em, ImportRule.class).isEmpty()) {
            emailImportRepository.saveAndFlush(emailImport);
            appliedRule = ImportRuleResourceIT.createEntity();
        } else {
            appliedRule = TestUtil.findAll(em, ImportRule.class).get(0);
        }
        em.persist(appliedRule);
        em.flush();
        emailImport.setAppliedRule(appliedRule);
        emailImportRepository.saveAndFlush(emailImport);
        Long appliedRuleId = appliedRule.getId();
        // Get all the emailImportList where appliedRule equals to appliedRuleId
        defaultEmailImportShouldBeFound("appliedRuleId.equals=" + appliedRuleId);

        // Get all the emailImportList where appliedRule equals to (appliedRuleId + 1)
        defaultEmailImportShouldNotBeFound("appliedRuleId.equals=" + (appliedRuleId + 1));
    }

    private void defaultEmailImportFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmailImportShouldBeFound(shouldBeFound);
        defaultEmailImportShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailImportShouldBeFound(String filter) throws Exception {
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImport.getId().intValue())))
            .andExpect(jsonPath("$.[*].fromEmail").value(hasItem(DEFAULT_FROM_EMAIL)))
            .andExpect(jsonPath("$.[*].toEmail").value(hasItem(DEFAULT_TO_EMAIL)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].bodyHtml").value(hasItem(DEFAULT_BODY_HTML)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].folderId").value(hasItem(DEFAULT_FOLDER_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentTypeId").value(hasItem(DEFAULT_DOCUMENT_TYPE_ID.intValue())))
            .andExpect(jsonPath("$.[*].attachmentCount").value(hasItem(DEFAULT_ATTACHMENT_COUNT)))
            .andExpect(jsonPath("$.[*].documentsCreated").value(hasItem(DEFAULT_DOCUMENTS_CREATED)))
            .andExpect(jsonPath("$.[*].appliedRuleId").value(hasItem(DEFAULT_APPLIED_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)));

        // Check, that the count call also returns 1
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailImportShouldNotBeFound(String filter) throws Exception {
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailImportMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailImport() throws Exception {
        // Get the emailImport
        restEmailImportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailImport() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImport
        EmailImport updatedEmailImport = emailImportRepository.findById(emailImport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmailImport are not directly saved in db
        em.detach(updatedEmailImport);
        updatedEmailImport
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .appliedRuleId(UPDATED_APPLIED_RULE_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA);
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(updatedEmailImport);

        restEmailImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailImportToMatchAllProperties(updatedEmailImport);
    }

    @Test
    @Transactional
    void putNonExistingEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailImportWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImport using partial update
        EmailImport partialUpdatedEmailImport = new EmailImport();
        partialUpdatedEmailImport.setId(emailImport.getId());

        partialUpdatedEmailImport
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .status(UPDATED_STATUS)
            .folderId(UPDATED_FOLDER_ID)
            .metadata(UPDATED_METADATA);

        restEmailImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImport))
            )
            .andExpect(status().isOk());

        // Validate the EmailImport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailImport, emailImport),
            getPersistedEmailImport(emailImport)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailImportWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImport using partial update
        EmailImport partialUpdatedEmailImport = new EmailImport();
        partialUpdatedEmailImport.setId(emailImport.getId());

        partialUpdatedEmailImport
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .folderId(UPDATED_FOLDER_ID)
            .documentTypeId(UPDATED_DOCUMENT_TYPE_ID)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .appliedRuleId(UPDATED_APPLIED_RULE_ID)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA);

        restEmailImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImport))
            )
            .andExpect(status().isOk());

        // Validate the EmailImport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportUpdatableFieldsEquals(partialUpdatedEmailImport, getPersistedEmailImport(partialUpdatedEmailImport));
    }

    @Test
    @Transactional
    void patchNonExistingEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailImportDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailImport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImport.setId(longCount.incrementAndGet());

        // Create the EmailImport
        EmailImportDTO emailImportDTO = emailImportMapper.toDto(emailImport);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailImportDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailImport() throws Exception {
        // Initialize the database
        insertedEmailImport = emailImportRepository.saveAndFlush(emailImport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailImport
        restEmailImportMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailImport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailImportRepository.count();
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

    protected EmailImport getPersistedEmailImport(EmailImport emailImport) {
        return emailImportRepository.findById(emailImport.getId()).orElseThrow();
    }

    protected void assertPersistedEmailImportToMatchAllProperties(EmailImport expectedEmailImport) {
        assertEmailImportAllPropertiesEquals(expectedEmailImport, getPersistedEmailImport(expectedEmailImport));
    }

    protected void assertPersistedEmailImportToMatchUpdatableProperties(EmailImport expectedEmailImport) {
        assertEmailImportAllUpdatablePropertiesEquals(expectedEmailImport, getPersistedEmailImport(expectedEmailImport));
    }
}
