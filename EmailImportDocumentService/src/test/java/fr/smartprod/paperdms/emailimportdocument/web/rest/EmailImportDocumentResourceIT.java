package fr.smartprod.paperdms.emailimportdocument.web.rest;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocumentAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimportdocument.IntegrationTest;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.ImportStatus;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportDocumentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportDocumentMapper;
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
 * Integration tests for the {@link EmailImportDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailImportDocumentResourceIT {

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

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

    private static final Integer DEFAULT_ATTACHMENT_COUNT = 1;
    private static final Integer UPDATED_ATTACHMENT_COUNT = 2;
    private static final Integer SMALLER_ATTACHMENT_COUNT = 1 - 1;

    private static final Integer DEFAULT_DOCUMENTS_CREATED = 1;
    private static final Integer UPDATED_DOCUMENTS_CREATED = 2;
    private static final Integer SMALLER_DOCUMENTS_CREATED = 1 - 1;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_METADATA = "AAAAAAAAAA";
    private static final String UPDATED_METADATA = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-import-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailImportDocumentRepository emailImportDocumentRepository;

    @Autowired
    private EmailImportDocumentMapper emailImportDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailImportDocumentMockMvc;

    private EmailImportDocument emailImportDocument;

    private EmailImportDocument insertedEmailImportDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportDocument createEntity() {
        return new EmailImportDocument()
            .sha256(DEFAULT_SHA_256)
            .fromEmail(DEFAULT_FROM_EMAIL)
            .toEmail(DEFAULT_TO_EMAIL)
            .subject(DEFAULT_SUBJECT)
            .body(DEFAULT_BODY)
            .bodyHtml(DEFAULT_BODY_HTML)
            .receivedDate(DEFAULT_RECEIVED_DATE)
            .processedDate(DEFAULT_PROCESSED_DATE)
            .status(DEFAULT_STATUS)
            .attachmentCount(DEFAULT_ATTACHMENT_COUNT)
            .documentsCreated(DEFAULT_DOCUMENTS_CREATED)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .metadata(DEFAULT_METADATA)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportDocument createUpdatedEntity() {
        return new EmailImportDocument()
            .sha256(UPDATED_SHA_256)
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);
    }

    @BeforeEach
    void initTest() {
        emailImportDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailImportDocument != null) {
            emailImportDocumentRepository.delete(insertedEmailImportDocument);
            insertedEmailImportDocument = null;
        }
    }

    @Test
    @Transactional
    void createEmailImportDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);
        var returnedEmailImportDocumentDTO = om.readValue(
            restEmailImportDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailImportDocumentDTO.class
        );

        // Validate the EmailImportDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailImportDocument = emailImportDocumentMapper.toEntity(returnedEmailImportDocumentDTO);
        assertEmailImportDocumentUpdatableFieldsEquals(
            returnedEmailImportDocument,
            getPersistedEmailImportDocument(returnedEmailImportDocument)
        );

        insertedEmailImportDocument = returnedEmailImportDocument;
    }

    @Test
    @Transactional
    void createEmailImportDocumentWithExistingId() throws Exception {
        // Create the EmailImportDocument with an existing ID
        emailImportDocument.setId(1L);
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportDocument.setSha256(null);

        // Create the EmailImportDocument, which fails.
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFromEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportDocument.setFromEmail(null);

        // Create the EmailImportDocument, which fails.
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkToEmailIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportDocument.setToEmail(null);

        // Create the EmailImportDocument, which fails.
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReceivedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportDocument.setReceivedDate(null);

        // Create the EmailImportDocument, which fails.
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportDocument.setStatus(null);

        // Create the EmailImportDocument, which fails.
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailImportDocuments() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].fromEmail").value(hasItem(DEFAULT_FROM_EMAIL)))
            .andExpect(jsonPath("$.[*].toEmail").value(hasItem(DEFAULT_TO_EMAIL)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].bodyHtml").value(hasItem(DEFAULT_BODY_HTML)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].attachmentCount").value(hasItem(DEFAULT_ATTACHMENT_COUNT)))
            .andExpect(jsonPath("$.[*].documentsCreated").value(hasItem(DEFAULT_DOCUMENTS_CREATED)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));
    }

    @Test
    @Transactional
    void getEmailImportDocument() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get the emailImportDocument
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, emailImportDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailImportDocument.getId().intValue()))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.fromEmail").value(DEFAULT_FROM_EMAIL))
            .andExpect(jsonPath("$.toEmail").value(DEFAULT_TO_EMAIL))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.body").value(DEFAULT_BODY))
            .andExpect(jsonPath("$.bodyHtml").value(DEFAULT_BODY_HTML))
            .andExpect(jsonPath("$.receivedDate").value(DEFAULT_RECEIVED_DATE.toString()))
            .andExpect(jsonPath("$.processedDate").value(DEFAULT_PROCESSED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.attachmentCount").value(DEFAULT_ATTACHMENT_COUNT))
            .andExpect(jsonPath("$.documentsCreated").value(DEFAULT_DOCUMENTS_CREATED))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.metadata").value(DEFAULT_METADATA))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256));
    }

    @Test
    @Transactional
    void getEmailImportDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        Long id = emailImportDocument.getId();

        defaultEmailImportDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmailImportDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmailImportDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where sha256 equals to
        defaultEmailImportDocumentFiltering("sha256.equals=" + DEFAULT_SHA_256, "sha256.equals=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where sha256 in
        defaultEmailImportDocumentFiltering("sha256.in=" + DEFAULT_SHA_256 + "," + UPDATED_SHA_256, "sha256.in=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where sha256 is not null
        defaultEmailImportDocumentFiltering("sha256.specified=true", "sha256.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where sha256 contains
        defaultEmailImportDocumentFiltering("sha256.contains=" + DEFAULT_SHA_256, "sha256.contains=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where sha256 does not contain
        defaultEmailImportDocumentFiltering("sha256.doesNotContain=" + UPDATED_SHA_256, "sha256.doesNotContain=" + DEFAULT_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByFromEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where fromEmail equals to
        defaultEmailImportDocumentFiltering("fromEmail.equals=" + DEFAULT_FROM_EMAIL, "fromEmail.equals=" + UPDATED_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByFromEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where fromEmail in
        defaultEmailImportDocumentFiltering(
            "fromEmail.in=" + DEFAULT_FROM_EMAIL + "," + UPDATED_FROM_EMAIL,
            "fromEmail.in=" + UPDATED_FROM_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByFromEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where fromEmail is not null
        defaultEmailImportDocumentFiltering("fromEmail.specified=true", "fromEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByFromEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where fromEmail contains
        defaultEmailImportDocumentFiltering("fromEmail.contains=" + DEFAULT_FROM_EMAIL, "fromEmail.contains=" + UPDATED_FROM_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByFromEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where fromEmail does not contain
        defaultEmailImportDocumentFiltering(
            "fromEmail.doesNotContain=" + UPDATED_FROM_EMAIL,
            "fromEmail.doesNotContain=" + DEFAULT_FROM_EMAIL
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByToEmailIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where toEmail equals to
        defaultEmailImportDocumentFiltering("toEmail.equals=" + DEFAULT_TO_EMAIL, "toEmail.equals=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByToEmailIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where toEmail in
        defaultEmailImportDocumentFiltering("toEmail.in=" + DEFAULT_TO_EMAIL + "," + UPDATED_TO_EMAIL, "toEmail.in=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByToEmailIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where toEmail is not null
        defaultEmailImportDocumentFiltering("toEmail.specified=true", "toEmail.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByToEmailContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where toEmail contains
        defaultEmailImportDocumentFiltering("toEmail.contains=" + DEFAULT_TO_EMAIL, "toEmail.contains=" + UPDATED_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByToEmailNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where toEmail does not contain
        defaultEmailImportDocumentFiltering("toEmail.doesNotContain=" + UPDATED_TO_EMAIL, "toEmail.doesNotContain=" + DEFAULT_TO_EMAIL);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where subject equals to
        defaultEmailImportDocumentFiltering("subject.equals=" + DEFAULT_SUBJECT, "subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where subject in
        defaultEmailImportDocumentFiltering("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT, "subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where subject is not null
        defaultEmailImportDocumentFiltering("subject.specified=true", "subject.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySubjectContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where subject contains
        defaultEmailImportDocumentFiltering("subject.contains=" + DEFAULT_SUBJECT, "subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where subject does not contain
        defaultEmailImportDocumentFiltering("subject.doesNotContain=" + UPDATED_SUBJECT, "subject.doesNotContain=" + DEFAULT_SUBJECT);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByReceivedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where receivedDate equals to
        defaultEmailImportDocumentFiltering("receivedDate.equals=" + DEFAULT_RECEIVED_DATE, "receivedDate.equals=" + UPDATED_RECEIVED_DATE);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByReceivedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where receivedDate in
        defaultEmailImportDocumentFiltering(
            "receivedDate.in=" + DEFAULT_RECEIVED_DATE + "," + UPDATED_RECEIVED_DATE,
            "receivedDate.in=" + UPDATED_RECEIVED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByReceivedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where receivedDate is not null
        defaultEmailImportDocumentFiltering("receivedDate.specified=true", "receivedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByProcessedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where processedDate equals to
        defaultEmailImportDocumentFiltering(
            "processedDate.equals=" + DEFAULT_PROCESSED_DATE,
            "processedDate.equals=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByProcessedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where processedDate in
        defaultEmailImportDocumentFiltering(
            "processedDate.in=" + DEFAULT_PROCESSED_DATE + "," + UPDATED_PROCESSED_DATE,
            "processedDate.in=" + UPDATED_PROCESSED_DATE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByProcessedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where processedDate is not null
        defaultEmailImportDocumentFiltering("processedDate.specified=true", "processedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where status equals to
        defaultEmailImportDocumentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where status in
        defaultEmailImportDocumentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where status is not null
        defaultEmailImportDocumentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount equals to
        defaultEmailImportDocumentFiltering(
            "attachmentCount.equals=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.equals=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount in
        defaultEmailImportDocumentFiltering(
            "attachmentCount.in=" + DEFAULT_ATTACHMENT_COUNT + "," + UPDATED_ATTACHMENT_COUNT,
            "attachmentCount.in=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount is not null
        defaultEmailImportDocumentFiltering("attachmentCount.specified=true", "attachmentCount.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount is greater than or equal to
        defaultEmailImportDocumentFiltering(
            "attachmentCount.greaterThanOrEqual=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.greaterThanOrEqual=" + UPDATED_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount is less than or equal to
        defaultEmailImportDocumentFiltering(
            "attachmentCount.lessThanOrEqual=" + DEFAULT_ATTACHMENT_COUNT,
            "attachmentCount.lessThanOrEqual=" + SMALLER_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount is less than
        defaultEmailImportDocumentFiltering(
            "attachmentCount.lessThan=" + UPDATED_ATTACHMENT_COUNT,
            "attachmentCount.lessThan=" + DEFAULT_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAttachmentCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where attachmentCount is greater than
        defaultEmailImportDocumentFiltering(
            "attachmentCount.greaterThan=" + SMALLER_ATTACHMENT_COUNT,
            "attachmentCount.greaterThan=" + DEFAULT_ATTACHMENT_COUNT
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated equals to
        defaultEmailImportDocumentFiltering(
            "documentsCreated.equals=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.equals=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated in
        defaultEmailImportDocumentFiltering(
            "documentsCreated.in=" + DEFAULT_DOCUMENTS_CREATED + "," + UPDATED_DOCUMENTS_CREATED,
            "documentsCreated.in=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated is not null
        defaultEmailImportDocumentFiltering("documentsCreated.specified=true", "documentsCreated.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated is greater than or equal to
        defaultEmailImportDocumentFiltering(
            "documentsCreated.greaterThanOrEqual=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.greaterThanOrEqual=" + UPDATED_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated is less than or equal to
        defaultEmailImportDocumentFiltering(
            "documentsCreated.lessThanOrEqual=" + DEFAULT_DOCUMENTS_CREATED,
            "documentsCreated.lessThanOrEqual=" + SMALLER_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated is less than
        defaultEmailImportDocumentFiltering(
            "documentsCreated.lessThan=" + UPDATED_DOCUMENTS_CREATED,
            "documentsCreated.lessThan=" + DEFAULT_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentsCreatedIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentsCreated is greater than
        defaultEmailImportDocumentFiltering(
            "documentsCreated.greaterThan=" + SMALLER_DOCUMENTS_CREATED,
            "documentsCreated.greaterThan=" + DEFAULT_DOCUMENTS_CREATED
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentSha256 equals to
        defaultEmailImportDocumentFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentSha256 in
        defaultEmailImportDocumentFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentSha256 is not null
        defaultEmailImportDocumentFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentSha256 contains
        defaultEmailImportDocumentFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        // Get all the emailImportDocumentList where documentSha256 does not contain
        defaultEmailImportDocumentFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportDocumentsByAppliedRuleIsEqualToSomething() throws Exception {
        EmailImportImportRule appliedRule;
        if (TestUtil.findAll(em, EmailImportImportRule.class).isEmpty()) {
            emailImportDocumentRepository.saveAndFlush(emailImportDocument);
            appliedRule = EmailImportImportRuleResourceIT.createEntity();
        } else {
            appliedRule = TestUtil.findAll(em, EmailImportImportRule.class).get(0);
        }
        em.persist(appliedRule);
        em.flush();
        emailImportDocument.setAppliedRule(appliedRule);
        emailImportDocumentRepository.saveAndFlush(emailImportDocument);
        Long appliedRuleId = appliedRule.getId();
        // Get all the emailImportDocumentList where appliedRule equals to appliedRuleId
        defaultEmailImportDocumentShouldBeFound("appliedRuleId.equals=" + appliedRuleId);

        // Get all the emailImportDocumentList where appliedRule equals to (appliedRuleId + 1)
        defaultEmailImportDocumentShouldNotBeFound("appliedRuleId.equals=" + (appliedRuleId + 1));
    }

    private void defaultEmailImportDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmailImportDocumentShouldBeFound(shouldBeFound);
        defaultEmailImportDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailImportDocumentShouldBeFound(String filter) throws Exception {
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].fromEmail").value(hasItem(DEFAULT_FROM_EMAIL)))
            .andExpect(jsonPath("$.[*].toEmail").value(hasItem(DEFAULT_TO_EMAIL)))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].body").value(hasItem(DEFAULT_BODY)))
            .andExpect(jsonPath("$.[*].bodyHtml").value(hasItem(DEFAULT_BODY_HTML)))
            .andExpect(jsonPath("$.[*].receivedDate").value(hasItem(DEFAULT_RECEIVED_DATE.toString())))
            .andExpect(jsonPath("$.[*].processedDate").value(hasItem(DEFAULT_PROCESSED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].attachmentCount").value(hasItem(DEFAULT_ATTACHMENT_COUNT)))
            .andExpect(jsonPath("$.[*].documentsCreated").value(hasItem(DEFAULT_DOCUMENTS_CREATED)))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].metadata").value(hasItem(DEFAULT_METADATA)))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));

        // Check, that the count call also returns 1
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailImportDocumentShouldNotBeFound(String filter) throws Exception {
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailImportDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailImportDocument() throws Exception {
        // Get the emailImportDocument
        restEmailImportDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailImportDocument() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportDocument
        EmailImportDocument updatedEmailImportDocument = emailImportDocumentRepository.findById(emailImportDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmailImportDocument are not directly saved in db
        em.detach(updatedEmailImportDocument);
        updatedEmailImportDocument
            .sha256(UPDATED_SHA_256)
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(updatedEmailImportDocument);

        restEmailImportDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailImportDocumentToMatchAllProperties(updatedEmailImportDocument);
    }

    @Test
    @Transactional
    void putNonExistingEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailImportDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportDocument using partial update
        EmailImportDocument partialUpdatedEmailImportDocument = new EmailImportDocument();
        partialUpdatedEmailImportDocument.setId(emailImportDocument.getId());

        partialUpdatedEmailImportDocument
            .fromEmail(UPDATED_FROM_EMAIL)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA);

        restEmailImportDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportDocument))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailImportDocument, emailImportDocument),
            getPersistedEmailImportDocument(emailImportDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailImportDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportDocument using partial update
        EmailImportDocument partialUpdatedEmailImportDocument = new EmailImportDocument();
        partialUpdatedEmailImportDocument.setId(emailImportDocument.getId());

        partialUpdatedEmailImportDocument
            .sha256(UPDATED_SHA_256)
            .fromEmail(UPDATED_FROM_EMAIL)
            .toEmail(UPDATED_TO_EMAIL)
            .subject(UPDATED_SUBJECT)
            .body(UPDATED_BODY)
            .bodyHtml(UPDATED_BODY_HTML)
            .receivedDate(UPDATED_RECEIVED_DATE)
            .processedDate(UPDATED_PROCESSED_DATE)
            .status(UPDATED_STATUS)
            .attachmentCount(UPDATED_ATTACHMENT_COUNT)
            .documentsCreated(UPDATED_DOCUMENTS_CREATED)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .metadata(UPDATED_METADATA)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);

        restEmailImportDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportDocument))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportDocumentUpdatableFieldsEquals(
            partialUpdatedEmailImportDocument,
            getPersistedEmailImportDocument(partialUpdatedEmailImportDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailImportDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailImportDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportDocument.setId(longCount.incrementAndGet());

        // Create the EmailImportDocument
        EmailImportDocumentDTO emailImportDocumentDTO = emailImportDocumentMapper.toDto(emailImportDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailImportDocumentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailImportDocument() throws Exception {
        // Initialize the database
        insertedEmailImportDocument = emailImportDocumentRepository.saveAndFlush(emailImportDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailImportDocument
        restEmailImportDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailImportDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailImportDocumentRepository.count();
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

    protected EmailImportDocument getPersistedEmailImportDocument(EmailImportDocument emailImportDocument) {
        return emailImportDocumentRepository.findById(emailImportDocument.getId()).orElseThrow();
    }

    protected void assertPersistedEmailImportDocumentToMatchAllProperties(EmailImportDocument expectedEmailImportDocument) {
        assertEmailImportDocumentAllPropertiesEquals(
            expectedEmailImportDocument,
            getPersistedEmailImportDocument(expectedEmailImportDocument)
        );
    }

    protected void assertPersistedEmailImportDocumentToMatchUpdatableProperties(EmailImportDocument expectedEmailImportDocument) {
        assertEmailImportDocumentAllUpdatablePropertiesEquals(
            expectedEmailImportDocument,
            getPersistedEmailImportDocument(expectedEmailImportDocument)
        );
    }
}
