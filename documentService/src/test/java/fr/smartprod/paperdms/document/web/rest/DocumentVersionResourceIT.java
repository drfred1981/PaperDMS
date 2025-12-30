package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentVersionAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentVersionSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentVersionMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.assertj.core.util.IterableUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.util.Streamable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DocumentVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentVersionResourceIT {

    private static final Integer DEFAULT_VERSION_NUMBER = 1;
    private static final Integer UPDATED_VERSION_NUMBER = 2;
    private static final Integer SMALLER_VERSION_NUMBER = 1 - 1;

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-versions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentVersionRepository documentVersionRepository;

    @Autowired
    private DocumentVersionMapper documentVersionMapper;

    @Autowired
    private DocumentVersionSearchRepository documentVersionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentVersionMockMvc;

    private DocumentVersion documentVersion;

    private DocumentVersion insertedDocumentVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentVersion createEntity() {
        return new DocumentVersion()
            .versionNumber(DEFAULT_VERSION_NUMBER)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentVersion createUpdatedEntity() {
        return new DocumentVersion()
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        documentVersion = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentVersion != null) {
            documentVersionRepository.delete(insertedDocumentVersion);
            documentVersionSearchRepository.delete(insertedDocumentVersion);
            insertedDocumentVersion = null;
        }
    }

    @Test
    @Transactional
    void createDocumentVersion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);
        var returnedDocumentVersionDTO = om.readValue(
            restDocumentVersionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentVersionDTO.class
        );

        // Validate the DocumentVersion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentVersion = documentVersionMapper.toEntity(returnedDocumentVersionDTO);
        assertDocumentVersionUpdatableFieldsEquals(returnedDocumentVersion, getPersistedDocumentVersion(returnedDocumentVersion));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentVersion = returnedDocumentVersion;
    }

    @Test
    @Transactional
    void createDocumentVersionWithExistingId() throws Exception {
        // Create the DocumentVersion with an existing ID
        documentVersion.setId(1L);
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkVersionNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setVersionNumber(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setSha256(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.sets3Key(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setFileSize(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUploadDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setUploadDate(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setIsActive(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        // set the field null
        documentVersion.setCreatedBy(null);

        // Create the DocumentVersion, which fails.
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        restDocumentVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentVersions() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get the documentVersion
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, documentVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentVersion.getId().intValue()))
            .andExpect(jsonPath("$.versionNumber").value(DEFAULT_VERSION_NUMBER))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getDocumentVersionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        Long id = documentVersion.getId();

        defaultDocumentVersionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentVersionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentVersionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber equals to
        defaultDocumentVersionFiltering("versionNumber.equals=" + DEFAULT_VERSION_NUMBER, "versionNumber.equals=" + UPDATED_VERSION_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber in
        defaultDocumentVersionFiltering(
            "versionNumber.in=" + DEFAULT_VERSION_NUMBER + "," + UPDATED_VERSION_NUMBER,
            "versionNumber.in=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber is not null
        defaultDocumentVersionFiltering("versionNumber.specified=true", "versionNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber is greater than or equal to
        defaultDocumentVersionFiltering(
            "versionNumber.greaterThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.greaterThanOrEqual=" + UPDATED_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber is less than or equal to
        defaultDocumentVersionFiltering(
            "versionNumber.lessThanOrEqual=" + DEFAULT_VERSION_NUMBER,
            "versionNumber.lessThanOrEqual=" + SMALLER_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber is less than
        defaultDocumentVersionFiltering(
            "versionNumber.lessThan=" + UPDATED_VERSION_NUMBER,
            "versionNumber.lessThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByVersionNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where versionNumber is greater than
        defaultDocumentVersionFiltering(
            "versionNumber.greaterThan=" + SMALLER_VERSION_NUMBER,
            "versionNumber.greaterThan=" + DEFAULT_VERSION_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBySha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where sha256 equals to
        defaultDocumentVersionFiltering("sha256.equals=" + DEFAULT_SHA_256, "sha256.equals=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBySha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where sha256 in
        defaultDocumentVersionFiltering("sha256.in=" + DEFAULT_SHA_256 + "," + UPDATED_SHA_256, "sha256.in=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBySha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where sha256 is not null
        defaultDocumentVersionFiltering("sha256.specified=true", "sha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBySha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where sha256 contains
        defaultDocumentVersionFiltering("sha256.contains=" + DEFAULT_SHA_256, "sha256.contains=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBySha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where sha256 does not contain
        defaultDocumentVersionFiltering("sha256.doesNotContain=" + UPDATED_SHA_256, "sha256.doesNotContain=" + DEFAULT_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where s3Key equals to
        defaultDocumentVersionFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where s3Key in
        defaultDocumentVersionFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where s3Key is not null
        defaultDocumentVersionFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where s3Key contains
        defaultDocumentVersionFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where s3Key does not contain
        defaultDocumentVersionFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize equals to
        defaultDocumentVersionFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize in
        defaultDocumentVersionFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize is not null
        defaultDocumentVersionFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize is greater than or equal to
        defaultDocumentVersionFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize is less than or equal to
        defaultDocumentVersionFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize is less than
        defaultDocumentVersionFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where fileSize is greater than
        defaultDocumentVersionFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByUploadDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where uploadDate equals to
        defaultDocumentVersionFiltering("uploadDate.equals=" + DEFAULT_UPLOAD_DATE, "uploadDate.equals=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByUploadDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where uploadDate in
        defaultDocumentVersionFiltering(
            "uploadDate.in=" + DEFAULT_UPLOAD_DATE + "," + UPDATED_UPLOAD_DATE,
            "uploadDate.in=" + UPDATED_UPLOAD_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByUploadDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where uploadDate is not null
        defaultDocumentVersionFiltering("uploadDate.specified=true", "uploadDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where isActive equals to
        defaultDocumentVersionFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where isActive in
        defaultDocumentVersionFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where isActive is not null
        defaultDocumentVersionFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where createdBy equals to
        defaultDocumentVersionFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where createdBy in
        defaultDocumentVersionFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where createdBy is not null
        defaultDocumentVersionFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where createdBy contains
        defaultDocumentVersionFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        // Get all the documentVersionList where createdBy does not contain
        defaultDocumentVersionFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentVersionsByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentVersionRepository.saveAndFlush(documentVersion);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentVersion.setDocument(document);
        documentVersionRepository.saveAndFlush(documentVersion);
        Long documentId = document.getId();
        // Get all the documentVersionList where document equals to documentId
        defaultDocumentVersionShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentVersionList where document equals to (documentId + 1)
        defaultDocumentVersionShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    private void defaultDocumentVersionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentVersionShouldBeFound(shouldBeFound);
        defaultDocumentVersionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentVersionShouldBeFound(String filter) throws Exception {
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentVersionShouldNotBeFound(String filter) throws Exception {
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentVersionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentVersion() throws Exception {
        // Get the documentVersion
        restDocumentVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentVersionSearchRepository.save(documentVersion);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());

        // Update the documentVersion
        DocumentVersion updatedDocumentVersion = documentVersionRepository.findById(documentVersion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentVersion are not directly saved in db
        em.detach(updatedDocumentVersion);
        updatedDocumentVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(updatedDocumentVersion);

        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentVersionToMatchAllProperties(updatedDocumentVersion);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentVersion> documentVersionSearchList = Streamable.of(documentVersionSearchRepository.findAll()).toList();
                DocumentVersion testDocumentVersionSearch = documentVersionSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentVersionAllPropertiesEquals(testDocumentVersionSearch, updatedDocumentVersion);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentVersionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentVersion using partial update
        DocumentVersion partialUpdatedDocumentVersion = new DocumentVersion();
        partialUpdatedDocumentVersion.setId(documentVersion.getId());

        partialUpdatedDocumentVersion.sha256(UPDATED_SHA_256).s3Key(UPDATED_S_3_KEY).createdBy(UPDATED_CREATED_BY);

        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentVersion))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentVersionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentVersion, documentVersion),
            getPersistedDocumentVersion(documentVersion)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentVersionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentVersion using partial update
        DocumentVersion partialUpdatedDocumentVersion = new DocumentVersion();
        partialUpdatedDocumentVersion.setId(documentVersion.getId());

        partialUpdatedDocumentVersion
            .versionNumber(UPDATED_VERSION_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY);

        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentVersion))
            )
            .andExpect(status().isOk());

        // Validate the DocumentVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentVersionUpdatableFieldsEquals(
            partialUpdatedDocumentVersion,
            getPersistedDocumentVersion(partialUpdatedDocumentVersion)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentVersionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        documentVersion.setId(longCount.incrementAndGet());

        // Create the DocumentVersion
        DocumentVersionDTO documentVersionDTO = documentVersionMapper.toDto(documentVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentVersionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);
        documentVersionRepository.save(documentVersion);
        documentVersionSearchRepository.save(documentVersion);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentVersion
        restDocumentVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentVersionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentVersion() throws Exception {
        // Initialize the database
        insertedDocumentVersion = documentVersionRepository.saveAndFlush(documentVersion);
        documentVersionSearchRepository.save(documentVersion);

        // Search the documentVersion
        restDocumentVersionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].versionNumber").value(hasItem(DEFAULT_VERSION_NUMBER)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return documentVersionRepository.count();
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

    protected DocumentVersion getPersistedDocumentVersion(DocumentVersion documentVersion) {
        return documentVersionRepository.findById(documentVersion.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentVersionToMatchAllProperties(DocumentVersion expectedDocumentVersion) {
        assertDocumentVersionAllPropertiesEquals(expectedDocumentVersion, getPersistedDocumentVersion(expectedDocumentVersion));
    }

    protected void assertPersistedDocumentVersionToMatchUpdatableProperties(DocumentVersion expectedDocumentVersion) {
        assertDocumentVersionAllUpdatablePropertiesEquals(expectedDocumentVersion, getPersistedDocumentVersion(expectedDocumentVersion));
    }
}
