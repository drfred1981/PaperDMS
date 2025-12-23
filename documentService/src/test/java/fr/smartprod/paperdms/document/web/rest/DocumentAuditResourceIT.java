package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentAuditAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.domain.enumeration.AuditAction;
import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentAuditMapper;
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
 * Integration tests for the {@link DocumentAuditResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentAuditResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final AuditAction DEFAULT_ACTION = AuditAction.CREATED;
    private static final AuditAction UPDATED_ACTION = AuditAction.VIEWED;

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_USER_IP = "AAAAAAAAAA";
    private static final String UPDATED_USER_IP = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ADDITIONAL_INFO = "AAAAAAAAAA";
    private static final String UPDATED_ADDITIONAL_INFO = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-audits";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentAuditRepository documentAuditRepository;

    @Autowired
    private DocumentAuditMapper documentAuditMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentAuditMockMvc;

    private DocumentAudit documentAudit;

    private DocumentAudit insertedDocumentAudit;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentAudit createEntity() {
        return new DocumentAudit()
            .documentId(DEFAULT_DOCUMENT_ID)
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .action(DEFAULT_ACTION)
            .userId(DEFAULT_USER_ID)
            .userIp(DEFAULT_USER_IP)
            .actionDate(DEFAULT_ACTION_DATE)
            .additionalInfo(DEFAULT_ADDITIONAL_INFO);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentAudit createUpdatedEntity() {
        return new DocumentAudit()
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .action(UPDATED_ACTION)
            .userId(UPDATED_USER_ID)
            .userIp(UPDATED_USER_IP)
            .actionDate(UPDATED_ACTION_DATE)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
    }

    @BeforeEach
    void initTest() {
        documentAudit = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentAudit != null) {
            documentAuditRepository.delete(insertedDocumentAudit);
            insertedDocumentAudit = null;
        }
    }

    @Test
    @Transactional
    void createDocumentAudit() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);
        var returnedDocumentAuditDTO = om.readValue(
            restDocumentAuditMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentAuditDTO.class
        );

        // Validate the DocumentAudit in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentAudit = documentAuditMapper.toEntity(returnedDocumentAuditDTO);
        assertDocumentAuditUpdatableFieldsEquals(returnedDocumentAudit, getPersistedDocumentAudit(returnedDocumentAudit));

        insertedDocumentAudit = returnedDocumentAudit;
    }

    @Test
    @Transactional
    void createDocumentAuditWithExistingId() throws Exception {
        // Create the DocumentAudit with an existing ID
        documentAudit.setId(1L);
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentAudit.setDocumentId(null);

        // Create the DocumentAudit, which fails.
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentAudit.setDocumentSha256(null);

        // Create the DocumentAudit, which fails.
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentAudit.setAction(null);

        // Create the DocumentAudit, which fails.
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentAudit.setUserId(null);

        // Create the DocumentAudit, which fails.
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentAudit.setActionDate(null);

        // Create the DocumentAudit, which fails.
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        restDocumentAuditMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentAudits() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userIp").value(hasItem(DEFAULT_USER_IP)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)));
    }

    @Test
    @Transactional
    void getDocumentAudit() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get the documentAudit
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL_ID, documentAudit.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentAudit.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.action").value(DEFAULT_ACTION.toString()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.userIp").value(DEFAULT_USER_IP))
            .andExpect(jsonPath("$.actionDate").value(DEFAULT_ACTION_DATE.toString()))
            .andExpect(jsonPath("$.additionalInfo").value(DEFAULT_ADDITIONAL_INFO));
    }

    @Test
    @Transactional
    void getDocumentAuditsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        Long id = documentAudit.getId();

        defaultDocumentAuditFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentAuditFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentAuditFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId equals to
        defaultDocumentAuditFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId in
        defaultDocumentAuditFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId is not null
        defaultDocumentAuditFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId is greater than or equal to
        defaultDocumentAuditFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId is less than or equal to
        defaultDocumentAuditFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId is less than
        defaultDocumentAuditFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentId is greater than
        defaultDocumentAuditFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentSha256 equals to
        defaultDocumentAuditFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentSha256 in
        defaultDocumentAuditFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentSha256 is not null
        defaultDocumentAuditFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentSha256 contains
        defaultDocumentAuditFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where documentSha256 does not contain
        defaultDocumentAuditFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where action equals to
        defaultDocumentAuditFiltering("action.equals=" + DEFAULT_ACTION, "action.equals=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where action in
        defaultDocumentAuditFiltering("action.in=" + DEFAULT_ACTION + "," + UPDATED_ACTION, "action.in=" + UPDATED_ACTION);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where action is not null
        defaultDocumentAuditFiltering("action.specified=true", "action.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userId equals to
        defaultDocumentAuditFiltering("userId.equals=" + DEFAULT_USER_ID, "userId.equals=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userId in
        defaultDocumentAuditFiltering("userId.in=" + DEFAULT_USER_ID + "," + UPDATED_USER_ID, "userId.in=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userId is not null
        defaultDocumentAuditFiltering("userId.specified=true", "userId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userId contains
        defaultDocumentAuditFiltering("userId.contains=" + DEFAULT_USER_ID, "userId.contains=" + UPDATED_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userId does not contain
        defaultDocumentAuditFiltering("userId.doesNotContain=" + UPDATED_USER_ID, "userId.doesNotContain=" + DEFAULT_USER_ID);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIpIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userIp equals to
        defaultDocumentAuditFiltering("userIp.equals=" + DEFAULT_USER_IP, "userIp.equals=" + UPDATED_USER_IP);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIpIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userIp in
        defaultDocumentAuditFiltering("userIp.in=" + DEFAULT_USER_IP + "," + UPDATED_USER_IP, "userIp.in=" + UPDATED_USER_IP);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIpIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userIp is not null
        defaultDocumentAuditFiltering("userIp.specified=true", "userIp.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIpContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userIp contains
        defaultDocumentAuditFiltering("userIp.contains=" + DEFAULT_USER_IP, "userIp.contains=" + UPDATED_USER_IP);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByUserIpNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where userIp does not contain
        defaultDocumentAuditFiltering("userIp.doesNotContain=" + UPDATED_USER_IP, "userIp.doesNotContain=" + DEFAULT_USER_IP);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where actionDate equals to
        defaultDocumentAuditFiltering("actionDate.equals=" + DEFAULT_ACTION_DATE, "actionDate.equals=" + UPDATED_ACTION_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where actionDate in
        defaultDocumentAuditFiltering(
            "actionDate.in=" + DEFAULT_ACTION_DATE + "," + UPDATED_ACTION_DATE,
            "actionDate.in=" + UPDATED_ACTION_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentAuditsByActionDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        // Get all the documentAuditList where actionDate is not null
        defaultDocumentAuditFiltering("actionDate.specified=true", "actionDate.specified=false");
    }

    private void defaultDocumentAuditFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentAuditShouldBeFound(shouldBeFound);
        defaultDocumentAuditShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentAuditShouldBeFound(String filter) throws Exception {
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentAudit.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].action").value(hasItem(DEFAULT_ACTION.toString())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].userIp").value(hasItem(DEFAULT_USER_IP)))
            .andExpect(jsonPath("$.[*].actionDate").value(hasItem(DEFAULT_ACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].additionalInfo").value(hasItem(DEFAULT_ADDITIONAL_INFO)));

        // Check, that the count call also returns 1
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentAuditShouldNotBeFound(String filter) throws Exception {
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentAuditMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentAudit() throws Exception {
        // Get the documentAudit
        restDocumentAuditMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentAudit() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentAudit
        DocumentAudit updatedDocumentAudit = documentAuditRepository.findById(documentAudit.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentAudit are not directly saved in db
        em.detach(updatedDocumentAudit);
        updatedDocumentAudit
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .action(UPDATED_ACTION)
            .userId(UPDATED_USER_ID)
            .userIp(UPDATED_USER_IP)
            .actionDate(UPDATED_ACTION_DATE)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(updatedDocumentAudit);

        restDocumentAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentAuditDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentAuditToMatchAllProperties(updatedDocumentAudit);
    }

    @Test
    @Transactional
    void putNonExistingDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentAuditDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentAuditWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentAudit using partial update
        DocumentAudit partialUpdatedDocumentAudit = new DocumentAudit();
        partialUpdatedDocumentAudit.setId(documentAudit.getId());

        partialUpdatedDocumentAudit.documentSha256(UPDATED_DOCUMENT_SHA_256).action(UPDATED_ACTION).userId(UPDATED_USER_ID);

        restDocumentAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentAudit))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentAuditUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentAudit, documentAudit),
            getPersistedDocumentAudit(documentAudit)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentAuditWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentAudit using partial update
        DocumentAudit partialUpdatedDocumentAudit = new DocumentAudit();
        partialUpdatedDocumentAudit.setId(documentAudit.getId());

        partialUpdatedDocumentAudit
            .documentId(UPDATED_DOCUMENT_ID)
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .action(UPDATED_ACTION)
            .userId(UPDATED_USER_ID)
            .userIp(UPDATED_USER_IP)
            .actionDate(UPDATED_ACTION_DATE)
            .additionalInfo(UPDATED_ADDITIONAL_INFO);

        restDocumentAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentAudit.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentAudit))
            )
            .andExpect(status().isOk());

        // Validate the DocumentAudit in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentAuditUpdatableFieldsEquals(partialUpdatedDocumentAudit, getPersistedDocumentAudit(partialUpdatedDocumentAudit));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentAuditDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentAuditDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentAudit() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentAudit.setId(longCount.incrementAndGet());

        // Create the DocumentAudit
        DocumentAuditDTO documentAuditDTO = documentAuditMapper.toDto(documentAudit);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentAuditMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentAuditDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentAudit in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentAudit() throws Exception {
        // Initialize the database
        insertedDocumentAudit = documentAuditRepository.saveAndFlush(documentAudit);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentAudit
        restDocumentAuditMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentAudit.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentAuditRepository.count();
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

    protected DocumentAudit getPersistedDocumentAudit(DocumentAudit documentAudit) {
        return documentAuditRepository.findById(documentAudit.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentAuditToMatchAllProperties(DocumentAudit expectedDocumentAudit) {
        assertDocumentAuditAllPropertiesEquals(expectedDocumentAudit, getPersistedDocumentAudit(expectedDocumentAudit));
    }

    protected void assertPersistedDocumentAuditToMatchUpdatableProperties(DocumentAudit expectedDocumentAudit) {
        assertDocumentAuditAllUpdatablePropertiesEquals(expectedDocumentAudit, getPersistedDocumentAudit(expectedDocumentAudit));
    }
}
