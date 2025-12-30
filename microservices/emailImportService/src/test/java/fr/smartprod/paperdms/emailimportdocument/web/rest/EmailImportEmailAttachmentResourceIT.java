package fr.smartprod.paperdms.emailimportdocument.web.rest;

import static fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachmentAsserts.*;
import static fr.smartprod.paperdms.emailimportdocument.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimportdocument.IntegrationTest;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment;
import fr.smartprod.paperdms.emailimportdocument.domain.enumeration.AttachmentStatus;
import fr.smartprod.paperdms.emailimportdocument.repository.EmailImportEmailAttachmentRepository;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportEmailAttachmentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.mapper.EmailImportEmailAttachmentMapper;
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
 * Integration tests for the {@link EmailImportEmailAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailImportEmailAttachmentResourceIT {

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final AttachmentStatus DEFAULT_STATUS = AttachmentStatus.PENDING;
    private static final AttachmentStatus UPDATED_STATUS = AttachmentStatus.PROCESSING;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-import-email-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailImportEmailAttachmentRepository emailImportEmailAttachmentRepository;

    @Autowired
    private EmailImportEmailAttachmentMapper emailImportEmailAttachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailImportEmailAttachmentMockMvc;

    private EmailImportEmailAttachment emailImportEmailAttachment;

    private EmailImportEmailAttachment insertedEmailImportEmailAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportEmailAttachment createEntity() {
        return new EmailImportEmailAttachment()
            .fileName(DEFAULT_FILE_NAME)
            .fileSize(DEFAULT_FILE_SIZE)
            .mimeType(DEFAULT_MIME_TYPE)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .status(DEFAULT_STATUS)
            .errorMessage(DEFAULT_ERROR_MESSAGE)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailImportEmailAttachment createUpdatedEntity() {
        return new EmailImportEmailAttachment()
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);
    }

    @BeforeEach
    void initTest() {
        emailImportEmailAttachment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailImportEmailAttachment != null) {
            emailImportEmailAttachmentRepository.delete(insertedEmailImportEmailAttachment);
            insertedEmailImportEmailAttachment = null;
        }
    }

    @Test
    @Transactional
    void createEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);
        var returnedEmailImportEmailAttachmentDTO = om.readValue(
            restEmailImportEmailAttachmentMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailImportEmailAttachmentDTO.class
        );

        // Validate the EmailImportEmailAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailImportEmailAttachment = emailImportEmailAttachmentMapper.toEntity(returnedEmailImportEmailAttachmentDTO);
        assertEmailImportEmailAttachmentUpdatableFieldsEquals(
            returnedEmailImportEmailAttachment,
            getPersistedEmailImportEmailAttachment(returnedEmailImportEmailAttachment)
        );

        insertedEmailImportEmailAttachment = returnedEmailImportEmailAttachment;
    }

    @Test
    @Transactional
    void createEmailImportEmailAttachmentWithExistingId() throws Exception {
        // Create the EmailImportEmailAttachment with an existing ID
        emailImportEmailAttachment.setId(1L);
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportEmailAttachment.setFileName(null);

        // Create the EmailImportEmailAttachment, which fails.
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportEmailAttachment.setFileSize(null);

        // Create the EmailImportEmailAttachment, which fails.
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMimeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportEmailAttachment.setMimeType(null);

        // Create the EmailImportEmailAttachment, which fails.
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportEmailAttachment.setSha256(null);

        // Create the EmailImportEmailAttachment, which fails.
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailImportEmailAttachment.setStatus(null);

        // Create the EmailImportEmailAttachment, which fails.
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachments() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportEmailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));
    }

    @Test
    @Transactional
    void getEmailImportEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get the emailImportEmailAttachment
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, emailImportEmailAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailImportEmailAttachment.getId().intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256));
    }

    @Test
    @Transactional
    void getEmailImportEmailAttachmentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        Long id = emailImportEmailAttachment.getId();

        defaultEmailImportEmailAttachmentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultEmailImportEmailAttachmentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultEmailImportEmailAttachmentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileName equals to
        defaultEmailImportEmailAttachmentFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileName in
        defaultEmailImportEmailAttachmentFiltering(
            "fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME,
            "fileName.in=" + UPDATED_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileName is not null
        defaultEmailImportEmailAttachmentFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileName contains
        defaultEmailImportEmailAttachmentFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileName does not contain
        defaultEmailImportEmailAttachmentFiltering(
            "fileName.doesNotContain=" + UPDATED_FILE_NAME,
            "fileName.doesNotContain=" + DEFAULT_FILE_NAME
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize equals to
        defaultEmailImportEmailAttachmentFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize in
        defaultEmailImportEmailAttachmentFiltering(
            "fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE,
            "fileSize.in=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize is not null
        defaultEmailImportEmailAttachmentFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize is greater than or equal to
        defaultEmailImportEmailAttachmentFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize is less than or equal to
        defaultEmailImportEmailAttachmentFiltering(
            "fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize is less than
        defaultEmailImportEmailAttachmentFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where fileSize is greater than
        defaultEmailImportEmailAttachmentFiltering(
            "fileSize.greaterThan=" + SMALLER_FILE_SIZE,
            "fileSize.greaterThan=" + DEFAULT_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByMimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where mimeType equals to
        defaultEmailImportEmailAttachmentFiltering("mimeType.equals=" + DEFAULT_MIME_TYPE, "mimeType.equals=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByMimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where mimeType in
        defaultEmailImportEmailAttachmentFiltering(
            "mimeType.in=" + DEFAULT_MIME_TYPE + "," + UPDATED_MIME_TYPE,
            "mimeType.in=" + UPDATED_MIME_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByMimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where mimeType is not null
        defaultEmailImportEmailAttachmentFiltering("mimeType.specified=true", "mimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByMimeTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where mimeType contains
        defaultEmailImportEmailAttachmentFiltering("mimeType.contains=" + DEFAULT_MIME_TYPE, "mimeType.contains=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByMimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where mimeType does not contain
        defaultEmailImportEmailAttachmentFiltering(
            "mimeType.doesNotContain=" + UPDATED_MIME_TYPE,
            "mimeType.doesNotContain=" + DEFAULT_MIME_TYPE
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBySha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where sha256 equals to
        defaultEmailImportEmailAttachmentFiltering("sha256.equals=" + DEFAULT_SHA_256, "sha256.equals=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBySha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where sha256 in
        defaultEmailImportEmailAttachmentFiltering("sha256.in=" + DEFAULT_SHA_256 + "," + UPDATED_SHA_256, "sha256.in=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBySha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where sha256 is not null
        defaultEmailImportEmailAttachmentFiltering("sha256.specified=true", "sha256.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBySha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where sha256 contains
        defaultEmailImportEmailAttachmentFiltering("sha256.contains=" + DEFAULT_SHA_256, "sha256.contains=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBySha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where sha256 does not contain
        defaultEmailImportEmailAttachmentFiltering("sha256.doesNotContain=" + UPDATED_SHA_256, "sha256.doesNotContain=" + DEFAULT_SHA_256);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where s3Key equals to
        defaultEmailImportEmailAttachmentFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where s3Key in
        defaultEmailImportEmailAttachmentFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where s3Key is not null
        defaultEmailImportEmailAttachmentFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where s3Key contains
        defaultEmailImportEmailAttachmentFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where s3Key does not contain
        defaultEmailImportEmailAttachmentFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where status equals to
        defaultEmailImportEmailAttachmentFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where status in
        defaultEmailImportEmailAttachmentFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where status is not null
        defaultEmailImportEmailAttachmentFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where documentSha256 equals to
        defaultEmailImportEmailAttachmentFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where documentSha256 in
        defaultEmailImportEmailAttachmentFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where documentSha256 is not null
        defaultEmailImportEmailAttachmentFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where documentSha256 contains
        defaultEmailImportEmailAttachmentFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        // Get all the emailImportEmailAttachmentList where documentSha256 does not contain
        defaultEmailImportEmailAttachmentFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllEmailImportEmailAttachmentsByEmailImportDocumentIsEqualToSomething() throws Exception {
        EmailImportDocument emailImportDocument;
        if (TestUtil.findAll(em, EmailImportDocument.class).isEmpty()) {
            emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);
            emailImportDocument = EmailImportDocumentResourceIT.createEntity();
        } else {
            emailImportDocument = TestUtil.findAll(em, EmailImportDocument.class).get(0);
        }
        em.persist(emailImportDocument);
        em.flush();
        emailImportEmailAttachment.setEmailImportDocument(emailImportDocument);
        emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);
        Long emailImportDocumentId = emailImportDocument.getId();
        // Get all the emailImportEmailAttachmentList where emailImportDocument equals to emailImportDocumentId
        defaultEmailImportEmailAttachmentShouldBeFound("emailImportDocumentId.equals=" + emailImportDocumentId);

        // Get all the emailImportEmailAttachmentList where emailImportDocument equals to (emailImportDocumentId + 1)
        defaultEmailImportEmailAttachmentShouldNotBeFound("emailImportDocumentId.equals=" + (emailImportDocumentId + 1));
    }

    private void defaultEmailImportEmailAttachmentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultEmailImportEmailAttachmentShouldBeFound(shouldBeFound);
        defaultEmailImportEmailAttachmentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultEmailImportEmailAttachmentShouldBeFound(String filter) throws Exception {
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailImportEmailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)));

        // Check, that the count call also returns 1
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultEmailImportEmailAttachmentShouldNotBeFound(String filter) throws Exception {
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restEmailImportEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingEmailImportEmailAttachment() throws Exception {
        // Get the emailImportEmailAttachment
        restEmailImportEmailAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailImportEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportEmailAttachment
        EmailImportEmailAttachment updatedEmailImportEmailAttachment = emailImportEmailAttachmentRepository
            .findById(emailImportEmailAttachment.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedEmailImportEmailAttachment are not directly saved in db
        em.detach(updatedEmailImportEmailAttachment);
        updatedEmailImportEmailAttachment
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(
            updatedEmailImportEmailAttachment
        );

        restEmailImportEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportEmailAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailImportEmailAttachmentToMatchAllProperties(updatedEmailImportEmailAttachment);
    }

    @Test
    @Transactional
    void putNonExistingEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailImportEmailAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailImportEmailAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportEmailAttachment using partial update
        EmailImportEmailAttachment partialUpdatedEmailImportEmailAttachment = new EmailImportEmailAttachment();
        partialUpdatedEmailImportEmailAttachment.setId(emailImportEmailAttachment.getId());

        partialUpdatedEmailImportEmailAttachment
            .fileName(UPDATED_FILE_NAME)
            .sha256(UPDATED_SHA_256)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportEmailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportEmailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportEmailAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportEmailAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailImportEmailAttachment, emailImportEmailAttachment),
            getPersistedEmailImportEmailAttachment(emailImportEmailAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailImportEmailAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailImportEmailAttachment using partial update
        EmailImportEmailAttachment partialUpdatedEmailImportEmailAttachment = new EmailImportEmailAttachment();
        partialUpdatedEmailImportEmailAttachment.setId(emailImportEmailAttachment.getId());

        partialUpdatedEmailImportEmailAttachment
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE)
            .documentSha256(UPDATED_DOCUMENT_SHA_256);

        restEmailImportEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailImportEmailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailImportEmailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EmailImportEmailAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailImportEmailAttachmentUpdatableFieldsEquals(
            partialUpdatedEmailImportEmailAttachment,
            getPersistedEmailImportEmailAttachment(partialUpdatedEmailImportEmailAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailImportEmailAttachmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailImportEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailImportEmailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailImportEmailAttachment
        EmailImportEmailAttachmentDTO emailImportEmailAttachmentDTO = emailImportEmailAttachmentMapper.toDto(emailImportEmailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailImportEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailImportEmailAttachmentDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailImportEmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailImportEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailImportEmailAttachment = emailImportEmailAttachmentRepository.saveAndFlush(emailImportEmailAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailImportEmailAttachment
        restEmailImportEmailAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailImportEmailAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailImportEmailAttachmentRepository.count();
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

    protected EmailImportEmailAttachment getPersistedEmailImportEmailAttachment(EmailImportEmailAttachment emailImportEmailAttachment) {
        return emailImportEmailAttachmentRepository.findById(emailImportEmailAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedEmailImportEmailAttachmentToMatchAllProperties(
        EmailImportEmailAttachment expectedEmailImportEmailAttachment
    ) {
        assertEmailImportEmailAttachmentAllPropertiesEquals(
            expectedEmailImportEmailAttachment,
            getPersistedEmailImportEmailAttachment(expectedEmailImportEmailAttachment)
        );
    }

    protected void assertPersistedEmailImportEmailAttachmentToMatchUpdatableProperties(
        EmailImportEmailAttachment expectedEmailImportEmailAttachment
    ) {
        assertEmailImportEmailAttachmentAllUpdatablePropertiesEquals(
            expectedEmailImportEmailAttachment,
            getPersistedEmailImportEmailAttachment(expectedEmailImportEmailAttachment)
        );
    }
}
