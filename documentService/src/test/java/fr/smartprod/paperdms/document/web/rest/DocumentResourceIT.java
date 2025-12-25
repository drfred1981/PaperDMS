package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentAsserts.*;
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
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.repository.DocumentRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMapper;
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
 * Integration tests for the {@link DocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

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

    private static final String DEFAULT_S_3_BUCKET = "AAAAAAAAAA";
    private static final String UPDATED_S_3_BUCKET = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_REGION = "AAAAAAAAAA";
    private static final String UPDATED_S_3_REGION = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_ETAG = "AAAAAAAAAA";
    private static final String UPDATED_S_3_ETAG = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_THUMBNAIL_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_THUMBNAIL_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_WEBP_PREVIEW_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_WEBP_PREVIEW_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_WEBP_PREVIEW_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_WEBP_PREVIEW_SHA_256 = "BBBBBBBBBB";

    private static final Instant DEFAULT_UPLOAD_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_UPLOAD_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_PUBLIC = false;
    private static final Boolean UPDATED_IS_PUBLIC = true;

    private static final Integer DEFAULT_DOWNLOAD_COUNT = 1;
    private static final Integer UPDATED_DOWNLOAD_COUNT = 2;
    private static final Integer SMALLER_DOWNLOAD_COUNT = 1 - 1;

    private static final Integer DEFAULT_VIEW_COUNT = 1;
    private static final Integer UPDATED_VIEW_COUNT = 2;
    private static final Integer SMALLER_VIEW_COUNT = 1 - 1;

    private static final String DEFAULT_DETECTED_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_DETECTED_LANGUAGE = "BBBBBBBBBB";

    private static final String DEFAULT_MANUAL_LANGUAGE = "AAAAAAAAAA";
    private static final String UPDATED_MANUAL_LANGUAGE = "BBBBBBBBBB";

    private static final Double DEFAULT_LANGUAGE_CONFIDENCE = 0D;
    private static final Double UPDATED_LANGUAGE_CONFIDENCE = 1D;
    private static final Double SMALLER_LANGUAGE_CONFIDENCE = 0D - 1D;

    private static final Integer DEFAULT_PAGE_COUNT = 1;
    private static final Integer UPDATED_PAGE_COUNT = 2;
    private static final Integer SMALLER_PAGE_COUNT = 1 - 1;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/documents/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentRepository documentRepository;

    @Autowired
    private DocumentMapper documentMapper;

    @Autowired
    private DocumentSearchRepository documentSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMockMvc;

    private Document document;

    private Document insertedDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createEntity(EntityManager em) {
        Document document = new Document()
            .title(DEFAULT_TITLE)
            .fileName(DEFAULT_FILE_NAME)
            .fileSize(DEFAULT_FILE_SIZE)
            .mimeType(DEFAULT_MIME_TYPE)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .s3Bucket(DEFAULT_S_3_BUCKET)
            .s3Region(DEFAULT_S_3_REGION)
            .s3Etag(DEFAULT_S_3_ETAG)
            .thumbnailS3Key(DEFAULT_THUMBNAIL_S_3_KEY)
            .thumbnailSha256(DEFAULT_THUMBNAIL_SHA_256)
            .webpPreviewS3Key(DEFAULT_WEBP_PREVIEW_S_3_KEY)
            .webpPreviewSha256(DEFAULT_WEBP_PREVIEW_SHA_256)
            .uploadDate(DEFAULT_UPLOAD_DATE)
            .isPublic(DEFAULT_IS_PUBLIC)
            .downloadCount(DEFAULT_DOWNLOAD_COUNT)
            .viewCount(DEFAULT_VIEW_COUNT)
            .detectedLanguage(DEFAULT_DETECTED_LANGUAGE)
            .manualLanguage(DEFAULT_MANUAL_LANGUAGE)
            .languageConfidence(DEFAULT_LANGUAGE_CONFIDENCE)
            .pageCount(DEFAULT_PAGE_COUNT)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createEntity();
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        document.setDocumentType(documentType);
        return document;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Document createUpdatedEntity(EntityManager em) {
        Document updatedDocument = new Document()
            .title(UPDATED_TITLE)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Region(UPDATED_S_3_REGION)
            .s3Etag(UPDATED_S_3_ETAG)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnailSha256(UPDATED_THUMBNAIL_SHA_256)
            .webpPreviewS3Key(UPDATED_WEBP_PREVIEW_S_3_KEY)
            .webpPreviewSha256(UPDATED_WEBP_PREVIEW_SHA_256)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isPublic(UPDATED_IS_PUBLIC)
            .downloadCount(UPDATED_DOWNLOAD_COUNT)
            .viewCount(UPDATED_VIEW_COUNT)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .manualLanguage(UPDATED_MANUAL_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .pageCount(UPDATED_PAGE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createUpdatedEntity();
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        updatedDocument.setDocumentType(documentType);
        return updatedDocument;
    }

    @BeforeEach
    void initTest() {
        document = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocument != null) {
            documentRepository.delete(insertedDocument);
            documentSearchRepository.delete(insertedDocument);
            insertedDocument = null;
        }
    }

    @Test
    @Transactional
    void createDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);
        var returnedDocumentDTO = om.readValue(
            restDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentDTO.class
        );

        // Validate the Document in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocument = documentMapper.toEntity(returnedDocumentDTO);
        assertDocumentUpdatableFieldsEquals(returnedDocument, getPersistedDocument(returnedDocument));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocument = returnedDocument;
    }

    @Test
    @Transactional
    void createDocumentWithExistingId() throws Exception {
        // Create the Document with an existing ID
        document.setId(1L);
        DocumentDTO documentDTO = documentMapper.toDto(document);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setTitle(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setFileName(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setFileSize(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkMimeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setMimeType(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setSha256(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.sets3Key(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checks3BucketIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.sets3Bucket(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkUploadDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setUploadDate(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsPublicIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setIsPublic(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setCreatedDate(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        // set the field null
        document.setCreatedBy(null);

        // Create the Document, which fails.
        DocumentDTO documentDTO = documentMapper.toDto(document);

        restDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocuments() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3Region").value(hasItem(DEFAULT_S_3_REGION)))
            .andExpect(jsonPath("$.[*].s3Etag").value(hasItem(DEFAULT_S_3_ETAG)))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailSha256").value(hasItem(DEFAULT_THUMBNAIL_SHA_256)))
            .andExpect(jsonPath("$.[*].webpPreviewS3Key").value(hasItem(DEFAULT_WEBP_PREVIEW_S_3_KEY)))
            .andExpect(jsonPath("$.[*].webpPreviewSha256").value(hasItem(DEFAULT_WEBP_PREVIEW_SHA_256)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT)))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].manualLanguage").value(hasItem(DEFAULT_MANUAL_LANGUAGE)))
            .andExpect(jsonPath("$.[*].languageConfidence").value(hasItem(DEFAULT_LANGUAGE_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get the document
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(document.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.s3Bucket").value(DEFAULT_S_3_BUCKET))
            .andExpect(jsonPath("$.s3Region").value(DEFAULT_S_3_REGION))
            .andExpect(jsonPath("$.s3Etag").value(DEFAULT_S_3_ETAG))
            .andExpect(jsonPath("$.thumbnailS3Key").value(DEFAULT_THUMBNAIL_S_3_KEY))
            .andExpect(jsonPath("$.thumbnailSha256").value(DEFAULT_THUMBNAIL_SHA_256))
            .andExpect(jsonPath("$.webpPreviewS3Key").value(DEFAULT_WEBP_PREVIEW_S_3_KEY))
            .andExpect(jsonPath("$.webpPreviewSha256").value(DEFAULT_WEBP_PREVIEW_SHA_256))
            .andExpect(jsonPath("$.uploadDate").value(DEFAULT_UPLOAD_DATE.toString()))
            .andExpect(jsonPath("$.isPublic").value(DEFAULT_IS_PUBLIC))
            .andExpect(jsonPath("$.downloadCount").value(DEFAULT_DOWNLOAD_COUNT))
            .andExpect(jsonPath("$.viewCount").value(DEFAULT_VIEW_COUNT))
            .andExpect(jsonPath("$.detectedLanguage").value(DEFAULT_DETECTED_LANGUAGE))
            .andExpect(jsonPath("$.manualLanguage").value(DEFAULT_MANUAL_LANGUAGE))
            .andExpect(jsonPath("$.languageConfidence").value(DEFAULT_LANGUAGE_CONFIDENCE))
            .andExpect(jsonPath("$.pageCount").value(DEFAULT_PAGE_COUNT))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        Long id = document.getId();

        defaultDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentsByTitleIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where title equals to
        defaultDocumentFiltering("title.equals=" + DEFAULT_TITLE, "title.equals=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByTitleIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where title in
        defaultDocumentFiltering("title.in=" + DEFAULT_TITLE + "," + UPDATED_TITLE, "title.in=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByTitleIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where title is not null
        defaultDocumentFiltering("title.specified=true", "title.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByTitleContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where title contains
        defaultDocumentFiltering("title.contains=" + DEFAULT_TITLE, "title.contains=" + UPDATED_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByTitleNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where title does not contain
        defaultDocumentFiltering("title.doesNotContain=" + UPDATED_TITLE, "title.doesNotContain=" + DEFAULT_TITLE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileName equals to
        defaultDocumentFiltering("fileName.equals=" + DEFAULT_FILE_NAME, "fileName.equals=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileName in
        defaultDocumentFiltering("fileName.in=" + DEFAULT_FILE_NAME + "," + UPDATED_FILE_NAME, "fileName.in=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileName is not null
        defaultDocumentFiltering("fileName.specified=true", "fileName.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileName contains
        defaultDocumentFiltering("fileName.contains=" + DEFAULT_FILE_NAME, "fileName.contains=" + UPDATED_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileName does not contain
        defaultDocumentFiltering("fileName.doesNotContain=" + UPDATED_FILE_NAME, "fileName.doesNotContain=" + DEFAULT_FILE_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize equals to
        defaultDocumentFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize in
        defaultDocumentFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize is not null
        defaultDocumentFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize is greater than or equal to
        defaultDocumentFiltering("fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize is less than or equal to
        defaultDocumentFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize is less than
        defaultDocumentFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where fileSize is greater than
        defaultDocumentFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllDocumentsByMimeTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where mimeType equals to
        defaultDocumentFiltering("mimeType.equals=" + DEFAULT_MIME_TYPE, "mimeType.equals=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByMimeTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where mimeType in
        defaultDocumentFiltering("mimeType.in=" + DEFAULT_MIME_TYPE + "," + UPDATED_MIME_TYPE, "mimeType.in=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByMimeTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where mimeType is not null
        defaultDocumentFiltering("mimeType.specified=true", "mimeType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByMimeTypeContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where mimeType contains
        defaultDocumentFiltering("mimeType.contains=" + DEFAULT_MIME_TYPE, "mimeType.contains=" + UPDATED_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsByMimeTypeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where mimeType does not contain
        defaultDocumentFiltering("mimeType.doesNotContain=" + UPDATED_MIME_TYPE, "mimeType.doesNotContain=" + DEFAULT_MIME_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentsBySha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where sha256 equals to
        defaultDocumentFiltering("sha256.equals=" + DEFAULT_SHA_256, "sha256.equals=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentsBySha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where sha256 in
        defaultDocumentFiltering("sha256.in=" + DEFAULT_SHA_256 + "," + UPDATED_SHA_256, "sha256.in=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentsBySha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where sha256 is not null
        defaultDocumentFiltering("sha256.specified=true", "sha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsBySha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where sha256 contains
        defaultDocumentFiltering("sha256.contains=" + DEFAULT_SHA_256, "sha256.contains=" + UPDATED_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentsBySha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where sha256 does not contain
        defaultDocumentFiltering("sha256.doesNotContain=" + UPDATED_SHA_256, "sha256.doesNotContain=" + DEFAULT_SHA_256);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Key equals to
        defaultDocumentFiltering("s3Key.equals=" + DEFAULT_S_3_KEY, "s3Key.equals=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Key in
        defaultDocumentFiltering("s3Key.in=" + DEFAULT_S_3_KEY + "," + UPDATED_S_3_KEY, "s3Key.in=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Key is not null
        defaultDocumentFiltering("s3Key.specified=true", "s3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsBys3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Key contains
        defaultDocumentFiltering("s3Key.contains=" + DEFAULT_S_3_KEY, "s3Key.contains=" + UPDATED_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Key does not contain
        defaultDocumentFiltering("s3Key.doesNotContain=" + UPDATED_S_3_KEY, "s3Key.doesNotContain=" + DEFAULT_S_3_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3BucketIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Bucket equals to
        defaultDocumentFiltering("s3Bucket.equals=" + DEFAULT_S_3_BUCKET, "s3Bucket.equals=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3BucketIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Bucket in
        defaultDocumentFiltering("s3Bucket.in=" + DEFAULT_S_3_BUCKET + "," + UPDATED_S_3_BUCKET, "s3Bucket.in=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3BucketIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Bucket is not null
        defaultDocumentFiltering("s3Bucket.specified=true", "s3Bucket.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsBys3BucketContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Bucket contains
        defaultDocumentFiltering("s3Bucket.contains=" + DEFAULT_S_3_BUCKET, "s3Bucket.contains=" + UPDATED_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3BucketNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Bucket does not contain
        defaultDocumentFiltering("s3Bucket.doesNotContain=" + UPDATED_S_3_BUCKET, "s3Bucket.doesNotContain=" + DEFAULT_S_3_BUCKET);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3RegionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Region equals to
        defaultDocumentFiltering("s3Region.equals=" + DEFAULT_S_3_REGION, "s3Region.equals=" + UPDATED_S_3_REGION);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3RegionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Region in
        defaultDocumentFiltering("s3Region.in=" + DEFAULT_S_3_REGION + "," + UPDATED_S_3_REGION, "s3Region.in=" + UPDATED_S_3_REGION);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3RegionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Region is not null
        defaultDocumentFiltering("s3Region.specified=true", "s3Region.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsBys3RegionContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Region contains
        defaultDocumentFiltering("s3Region.contains=" + DEFAULT_S_3_REGION, "s3Region.contains=" + UPDATED_S_3_REGION);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3RegionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Region does not contain
        defaultDocumentFiltering("s3Region.doesNotContain=" + UPDATED_S_3_REGION, "s3Region.doesNotContain=" + DEFAULT_S_3_REGION);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3EtagIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Etag equals to
        defaultDocumentFiltering("s3Etag.equals=" + DEFAULT_S_3_ETAG, "s3Etag.equals=" + UPDATED_S_3_ETAG);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3EtagIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Etag in
        defaultDocumentFiltering("s3Etag.in=" + DEFAULT_S_3_ETAG + "," + UPDATED_S_3_ETAG, "s3Etag.in=" + UPDATED_S_3_ETAG);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3EtagIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Etag is not null
        defaultDocumentFiltering("s3Etag.specified=true", "s3Etag.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsBys3EtagContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Etag contains
        defaultDocumentFiltering("s3Etag.contains=" + DEFAULT_S_3_ETAG, "s3Etag.contains=" + UPDATED_S_3_ETAG);
    }

    @Test
    @Transactional
    void getAllDocumentsBys3EtagNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where s3Etag does not contain
        defaultDocumentFiltering("s3Etag.doesNotContain=" + UPDATED_S_3_ETAG, "s3Etag.doesNotContain=" + DEFAULT_S_3_ETAG);
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailS3Key equals to
        defaultDocumentFiltering(
            "thumbnailS3Key.equals=" + DEFAULT_THUMBNAIL_S_3_KEY,
            "thumbnailS3Key.equals=" + UPDATED_THUMBNAIL_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailS3Key in
        defaultDocumentFiltering(
            "thumbnailS3Key.in=" + DEFAULT_THUMBNAIL_S_3_KEY + "," + UPDATED_THUMBNAIL_S_3_KEY,
            "thumbnailS3Key.in=" + UPDATED_THUMBNAIL_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailS3Key is not null
        defaultDocumentFiltering("thumbnailS3Key.specified=true", "thumbnailS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailS3Key contains
        defaultDocumentFiltering(
            "thumbnailS3Key.contains=" + DEFAULT_THUMBNAIL_S_3_KEY,
            "thumbnailS3Key.contains=" + UPDATED_THUMBNAIL_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailS3Key does not contain
        defaultDocumentFiltering(
            "thumbnailS3Key.doesNotContain=" + UPDATED_THUMBNAIL_S_3_KEY,
            "thumbnailS3Key.doesNotContain=" + DEFAULT_THUMBNAIL_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailSha256 equals to
        defaultDocumentFiltering(
            "thumbnailSha256.equals=" + DEFAULT_THUMBNAIL_SHA_256,
            "thumbnailSha256.equals=" + UPDATED_THUMBNAIL_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailSha256 in
        defaultDocumentFiltering(
            "thumbnailSha256.in=" + DEFAULT_THUMBNAIL_SHA_256 + "," + UPDATED_THUMBNAIL_SHA_256,
            "thumbnailSha256.in=" + UPDATED_THUMBNAIL_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailSha256 is not null
        defaultDocumentFiltering("thumbnailSha256.specified=true", "thumbnailSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailSha256 contains
        defaultDocumentFiltering(
            "thumbnailSha256.contains=" + DEFAULT_THUMBNAIL_SHA_256,
            "thumbnailSha256.contains=" + UPDATED_THUMBNAIL_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByThumbnailSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where thumbnailSha256 does not contain
        defaultDocumentFiltering(
            "thumbnailSha256.doesNotContain=" + UPDATED_THUMBNAIL_SHA_256,
            "thumbnailSha256.doesNotContain=" + DEFAULT_THUMBNAIL_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewS3Key equals to
        defaultDocumentFiltering(
            "webpPreviewS3Key.equals=" + DEFAULT_WEBP_PREVIEW_S_3_KEY,
            "webpPreviewS3Key.equals=" + UPDATED_WEBP_PREVIEW_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewS3Key in
        defaultDocumentFiltering(
            "webpPreviewS3Key.in=" + DEFAULT_WEBP_PREVIEW_S_3_KEY + "," + UPDATED_WEBP_PREVIEW_S_3_KEY,
            "webpPreviewS3Key.in=" + UPDATED_WEBP_PREVIEW_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewS3Key is not null
        defaultDocumentFiltering("webpPreviewS3Key.specified=true", "webpPreviewS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewS3Key contains
        defaultDocumentFiltering(
            "webpPreviewS3Key.contains=" + DEFAULT_WEBP_PREVIEW_S_3_KEY,
            "webpPreviewS3Key.contains=" + UPDATED_WEBP_PREVIEW_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewS3Key does not contain
        defaultDocumentFiltering(
            "webpPreviewS3Key.doesNotContain=" + UPDATED_WEBP_PREVIEW_S_3_KEY,
            "webpPreviewS3Key.doesNotContain=" + DEFAULT_WEBP_PREVIEW_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewSha256 equals to
        defaultDocumentFiltering(
            "webpPreviewSha256.equals=" + DEFAULT_WEBP_PREVIEW_SHA_256,
            "webpPreviewSha256.equals=" + UPDATED_WEBP_PREVIEW_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewSha256 in
        defaultDocumentFiltering(
            "webpPreviewSha256.in=" + DEFAULT_WEBP_PREVIEW_SHA_256 + "," + UPDATED_WEBP_PREVIEW_SHA_256,
            "webpPreviewSha256.in=" + UPDATED_WEBP_PREVIEW_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewSha256 is not null
        defaultDocumentFiltering("webpPreviewSha256.specified=true", "webpPreviewSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewSha256 contains
        defaultDocumentFiltering(
            "webpPreviewSha256.contains=" + DEFAULT_WEBP_PREVIEW_SHA_256,
            "webpPreviewSha256.contains=" + UPDATED_WEBP_PREVIEW_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByWebpPreviewSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where webpPreviewSha256 does not contain
        defaultDocumentFiltering(
            "webpPreviewSha256.doesNotContain=" + UPDATED_WEBP_PREVIEW_SHA_256,
            "webpPreviewSha256.doesNotContain=" + DEFAULT_WEBP_PREVIEW_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByUploadDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate equals to
        defaultDocumentFiltering("uploadDate.equals=" + DEFAULT_UPLOAD_DATE, "uploadDate.equals=" + UPDATED_UPLOAD_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByUploadDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate in
        defaultDocumentFiltering(
            "uploadDate.in=" + DEFAULT_UPLOAD_DATE + "," + UPDATED_UPLOAD_DATE,
            "uploadDate.in=" + UPDATED_UPLOAD_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByUploadDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where uploadDate is not null
        defaultDocumentFiltering("uploadDate.specified=true", "uploadDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByIsPublicIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where isPublic equals to
        defaultDocumentFiltering("isPublic.equals=" + DEFAULT_IS_PUBLIC, "isPublic.equals=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllDocumentsByIsPublicIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where isPublic in
        defaultDocumentFiltering("isPublic.in=" + DEFAULT_IS_PUBLIC + "," + UPDATED_IS_PUBLIC, "isPublic.in=" + UPDATED_IS_PUBLIC);
    }

    @Test
    @Transactional
    void getAllDocumentsByIsPublicIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where isPublic is not null
        defaultDocumentFiltering("isPublic.specified=true", "isPublic.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount equals to
        defaultDocumentFiltering("downloadCount.equals=" + DEFAULT_DOWNLOAD_COUNT, "downloadCount.equals=" + UPDATED_DOWNLOAD_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount in
        defaultDocumentFiltering(
            "downloadCount.in=" + DEFAULT_DOWNLOAD_COUNT + "," + UPDATED_DOWNLOAD_COUNT,
            "downloadCount.in=" + UPDATED_DOWNLOAD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount is not null
        defaultDocumentFiltering("downloadCount.specified=true", "downloadCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount is greater than or equal to
        defaultDocumentFiltering(
            "downloadCount.greaterThanOrEqual=" + DEFAULT_DOWNLOAD_COUNT,
            "downloadCount.greaterThanOrEqual=" + UPDATED_DOWNLOAD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount is less than or equal to
        defaultDocumentFiltering(
            "downloadCount.lessThanOrEqual=" + DEFAULT_DOWNLOAD_COUNT,
            "downloadCount.lessThanOrEqual=" + SMALLER_DOWNLOAD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount is less than
        defaultDocumentFiltering("downloadCount.lessThan=" + UPDATED_DOWNLOAD_COUNT, "downloadCount.lessThan=" + DEFAULT_DOWNLOAD_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByDownloadCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where downloadCount is greater than
        defaultDocumentFiltering(
            "downloadCount.greaterThan=" + SMALLER_DOWNLOAD_COUNT,
            "downloadCount.greaterThan=" + DEFAULT_DOWNLOAD_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount equals to
        defaultDocumentFiltering("viewCount.equals=" + DEFAULT_VIEW_COUNT, "viewCount.equals=" + UPDATED_VIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount in
        defaultDocumentFiltering("viewCount.in=" + DEFAULT_VIEW_COUNT + "," + UPDATED_VIEW_COUNT, "viewCount.in=" + UPDATED_VIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount is not null
        defaultDocumentFiltering("viewCount.specified=true", "viewCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount is greater than or equal to
        defaultDocumentFiltering(
            "viewCount.greaterThanOrEqual=" + DEFAULT_VIEW_COUNT,
            "viewCount.greaterThanOrEqual=" + UPDATED_VIEW_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount is less than or equal to
        defaultDocumentFiltering("viewCount.lessThanOrEqual=" + DEFAULT_VIEW_COUNT, "viewCount.lessThanOrEqual=" + SMALLER_VIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount is less than
        defaultDocumentFiltering("viewCount.lessThan=" + UPDATED_VIEW_COUNT, "viewCount.lessThan=" + DEFAULT_VIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByViewCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where viewCount is greater than
        defaultDocumentFiltering("viewCount.greaterThan=" + SMALLER_VIEW_COUNT, "viewCount.greaterThan=" + DEFAULT_VIEW_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByDetectedLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where detectedLanguage equals to
        defaultDocumentFiltering(
            "detectedLanguage.equals=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.equals=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDetectedLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where detectedLanguage in
        defaultDocumentFiltering(
            "detectedLanguage.in=" + DEFAULT_DETECTED_LANGUAGE + "," + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.in=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDetectedLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where detectedLanguage is not null
        defaultDocumentFiltering("detectedLanguage.specified=true", "detectedLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByDetectedLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where detectedLanguage contains
        defaultDocumentFiltering(
            "detectedLanguage.contains=" + DEFAULT_DETECTED_LANGUAGE,
            "detectedLanguage.contains=" + UPDATED_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByDetectedLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where detectedLanguage does not contain
        defaultDocumentFiltering(
            "detectedLanguage.doesNotContain=" + UPDATED_DETECTED_LANGUAGE,
            "detectedLanguage.doesNotContain=" + DEFAULT_DETECTED_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByManualLanguageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where manualLanguage equals to
        defaultDocumentFiltering("manualLanguage.equals=" + DEFAULT_MANUAL_LANGUAGE, "manualLanguage.equals=" + UPDATED_MANUAL_LANGUAGE);
    }

    @Test
    @Transactional
    void getAllDocumentsByManualLanguageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where manualLanguage in
        defaultDocumentFiltering(
            "manualLanguage.in=" + DEFAULT_MANUAL_LANGUAGE + "," + UPDATED_MANUAL_LANGUAGE,
            "manualLanguage.in=" + UPDATED_MANUAL_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByManualLanguageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where manualLanguage is not null
        defaultDocumentFiltering("manualLanguage.specified=true", "manualLanguage.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByManualLanguageContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where manualLanguage contains
        defaultDocumentFiltering(
            "manualLanguage.contains=" + DEFAULT_MANUAL_LANGUAGE,
            "manualLanguage.contains=" + UPDATED_MANUAL_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByManualLanguageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where manualLanguage does not contain
        defaultDocumentFiltering(
            "manualLanguage.doesNotContain=" + UPDATED_MANUAL_LANGUAGE,
            "manualLanguage.doesNotContain=" + DEFAULT_MANUAL_LANGUAGE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence equals to
        defaultDocumentFiltering(
            "languageConfidence.equals=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.equals=" + UPDATED_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence in
        defaultDocumentFiltering(
            "languageConfidence.in=" + DEFAULT_LANGUAGE_CONFIDENCE + "," + UPDATED_LANGUAGE_CONFIDENCE,
            "languageConfidence.in=" + UPDATED_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence is not null
        defaultDocumentFiltering("languageConfidence.specified=true", "languageConfidence.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence is greater than or equal to
        defaultDocumentFiltering(
            "languageConfidence.greaterThanOrEqual=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.greaterThanOrEqual=" + (DEFAULT_LANGUAGE_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence is less than or equal to
        defaultDocumentFiltering(
            "languageConfidence.lessThanOrEqual=" + DEFAULT_LANGUAGE_CONFIDENCE,
            "languageConfidence.lessThanOrEqual=" + SMALLER_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence is less than
        defaultDocumentFiltering(
            "languageConfidence.lessThan=" + (DEFAULT_LANGUAGE_CONFIDENCE + 1),
            "languageConfidence.lessThan=" + DEFAULT_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByLanguageConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where languageConfidence is greater than
        defaultDocumentFiltering(
            "languageConfidence.greaterThan=" + SMALLER_LANGUAGE_CONFIDENCE,
            "languageConfidence.greaterThan=" + DEFAULT_LANGUAGE_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount equals to
        defaultDocumentFiltering("pageCount.equals=" + DEFAULT_PAGE_COUNT, "pageCount.equals=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount in
        defaultDocumentFiltering("pageCount.in=" + DEFAULT_PAGE_COUNT + "," + UPDATED_PAGE_COUNT, "pageCount.in=" + UPDATED_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount is not null
        defaultDocumentFiltering("pageCount.specified=true", "pageCount.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount is greater than or equal to
        defaultDocumentFiltering(
            "pageCount.greaterThanOrEqual=" + DEFAULT_PAGE_COUNT,
            "pageCount.greaterThanOrEqual=" + UPDATED_PAGE_COUNT
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount is less than or equal to
        defaultDocumentFiltering("pageCount.lessThanOrEqual=" + DEFAULT_PAGE_COUNT, "pageCount.lessThanOrEqual=" + SMALLER_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount is less than
        defaultDocumentFiltering("pageCount.lessThan=" + UPDATED_PAGE_COUNT, "pageCount.lessThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByPageCountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where pageCount is greater than
        defaultDocumentFiltering("pageCount.greaterThan=" + SMALLER_PAGE_COUNT, "pageCount.greaterThan=" + DEFAULT_PAGE_COUNT);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdDate equals to
        defaultDocumentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdDate in
        defaultDocumentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdDate is not null
        defaultDocumentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdBy equals to
        defaultDocumentFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdBy in
        defaultDocumentFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdBy is not null
        defaultDocumentFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdBy contains
        defaultDocumentFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        // Get all the documentList where createdBy does not contain
        defaultDocumentFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentsByFolderIsEqualToSomething() throws Exception {
        Folder folder;
        if (TestUtil.findAll(em, Folder.class).isEmpty()) {
            documentRepository.saveAndFlush(document);
            folder = FolderResourceIT.createEntity();
        } else {
            folder = TestUtil.findAll(em, Folder.class).get(0);
        }
        em.persist(folder);
        em.flush();
        document.setFolder(folder);
        documentRepository.saveAndFlush(document);
        Long folderId = folder.getId();
        // Get all the documentList where folder equals to folderId
        defaultDocumentShouldBeFound("folderId.equals=" + folderId);

        // Get all the documentList where folder equals to (folderId + 1)
        defaultDocumentShouldNotBeFound("folderId.equals=" + (folderId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentsByDocumentTypeIsEqualToSomething() throws Exception {
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentRepository.saveAndFlush(document);
            documentType = DocumentTypeResourceIT.createEntity();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        em.persist(documentType);
        em.flush();
        document.setDocumentType(documentType);
        documentRepository.saveAndFlush(document);
        Long documentTypeId = documentType.getId();
        // Get all the documentList where documentType equals to documentTypeId
        defaultDocumentShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the documentList where documentType equals to (documentTypeId + 1)
        defaultDocumentShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }

    private void defaultDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentShouldBeFound(shouldBeFound);
        defaultDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentShouldBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3Region").value(hasItem(DEFAULT_S_3_REGION)))
            .andExpect(jsonPath("$.[*].s3Etag").value(hasItem(DEFAULT_S_3_ETAG)))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailSha256").value(hasItem(DEFAULT_THUMBNAIL_SHA_256)))
            .andExpect(jsonPath("$.[*].webpPreviewS3Key").value(hasItem(DEFAULT_WEBP_PREVIEW_S_3_KEY)))
            .andExpect(jsonPath("$.[*].webpPreviewSha256").value(hasItem(DEFAULT_WEBP_PREVIEW_SHA_256)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT)))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].manualLanguage").value(hasItem(DEFAULT_MANUAL_LANGUAGE)))
            .andExpect(jsonPath("$.[*].languageConfidence").value(hasItem(DEFAULT_LANGUAGE_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentShouldNotBeFound(String filter) throws Exception {
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocument() throws Exception {
        // Get the document
        restDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentSearchRepository.save(document);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());

        // Update the document
        Document updatedDocument = documentRepository.findById(document.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocument are not directly saved in db
        em.detach(updatedDocument);
        updatedDocument
            .title(UPDATED_TITLE)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Region(UPDATED_S_3_REGION)
            .s3Etag(UPDATED_S_3_ETAG)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnailSha256(UPDATED_THUMBNAIL_SHA_256)
            .webpPreviewS3Key(UPDATED_WEBP_PREVIEW_S_3_KEY)
            .webpPreviewSha256(UPDATED_WEBP_PREVIEW_SHA_256)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isPublic(UPDATED_IS_PUBLIC)
            .downloadCount(UPDATED_DOWNLOAD_COUNT)
            .viewCount(UPDATED_VIEW_COUNT)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .manualLanguage(UPDATED_MANUAL_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .pageCount(UPDATED_PAGE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        DocumentDTO documentDTO = documentMapper.toDto(updatedDocument);

        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentToMatchAllProperties(updatedDocument);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<Document> documentSearchList = Streamable.of(documentSearchRepository.findAll()).toList();
                Document testDocumentSearch = documentSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentAllPropertiesEquals(testDocumentSearch, updatedDocument);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Region(UPDATED_S_3_REGION)
            .s3Etag(UPDATED_S_3_ETAG)
            .webpPreviewS3Key(UPDATED_WEBP_PREVIEW_S_3_KEY)
            .webpPreviewSha256(UPDATED_WEBP_PREVIEW_SHA_256)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedDocument, document), getPersistedDocument(document));
    }

    @Test
    @Transactional
    void fullUpdateDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the document using partial update
        Document partialUpdatedDocument = new Document();
        partialUpdatedDocument.setId(document.getId());

        partialUpdatedDocument
            .title(UPDATED_TITLE)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3Bucket(UPDATED_S_3_BUCKET)
            .s3Region(UPDATED_S_3_REGION)
            .s3Etag(UPDATED_S_3_ETAG)
            .thumbnailS3Key(UPDATED_THUMBNAIL_S_3_KEY)
            .thumbnailSha256(UPDATED_THUMBNAIL_SHA_256)
            .webpPreviewS3Key(UPDATED_WEBP_PREVIEW_S_3_KEY)
            .webpPreviewSha256(UPDATED_WEBP_PREVIEW_SHA_256)
            .uploadDate(UPDATED_UPLOAD_DATE)
            .isPublic(UPDATED_IS_PUBLIC)
            .downloadCount(UPDATED_DOWNLOAD_COUNT)
            .viewCount(UPDATED_VIEW_COUNT)
            .detectedLanguage(UPDATED_DETECTED_LANGUAGE)
            .manualLanguage(UPDATED_MANUAL_LANGUAGE)
            .languageConfidence(UPDATED_LANGUAGE_CONFIDENCE)
            .pageCount(UPDATED_PAGE_COUNT)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocument))
            )
            .andExpect(status().isOk());

        // Validate the Document in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentUpdatableFieldsEquals(partialUpdatedDocument, getPersistedDocument(partialUpdatedDocument));
    }

    @Test
    @Transactional
    void patchNonExistingDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        document.setId(longCount.incrementAndGet());

        // Create the Document
        DocumentDTO documentDTO = documentMapper.toDto(document);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Document in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);
        documentRepository.save(document);
        documentSearchRepository.save(document);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the document
        restDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, document.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocument() throws Exception {
        // Initialize the database
        insertedDocument = documentRepository.saveAndFlush(document);
        documentSearchRepository.save(document);

        // Search the document
        restDocumentMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + document.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(document.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3Bucket").value(hasItem(DEFAULT_S_3_BUCKET)))
            .andExpect(jsonPath("$.[*].s3Region").value(hasItem(DEFAULT_S_3_REGION)))
            .andExpect(jsonPath("$.[*].s3Etag").value(hasItem(DEFAULT_S_3_ETAG)))
            .andExpect(jsonPath("$.[*].thumbnailS3Key").value(hasItem(DEFAULT_THUMBNAIL_S_3_KEY)))
            .andExpect(jsonPath("$.[*].thumbnailSha256").value(hasItem(DEFAULT_THUMBNAIL_SHA_256)))
            .andExpect(jsonPath("$.[*].webpPreviewS3Key").value(hasItem(DEFAULT_WEBP_PREVIEW_S_3_KEY)))
            .andExpect(jsonPath("$.[*].webpPreviewSha256").value(hasItem(DEFAULT_WEBP_PREVIEW_SHA_256)))
            .andExpect(jsonPath("$.[*].uploadDate").value(hasItem(DEFAULT_UPLOAD_DATE.toString())))
            .andExpect(jsonPath("$.[*].isPublic").value(hasItem(DEFAULT_IS_PUBLIC)))
            .andExpect(jsonPath("$.[*].downloadCount").value(hasItem(DEFAULT_DOWNLOAD_COUNT)))
            .andExpect(jsonPath("$.[*].viewCount").value(hasItem(DEFAULT_VIEW_COUNT)))
            .andExpect(jsonPath("$.[*].detectedLanguage").value(hasItem(DEFAULT_DETECTED_LANGUAGE)))
            .andExpect(jsonPath("$.[*].manualLanguage").value(hasItem(DEFAULT_MANUAL_LANGUAGE)))
            .andExpect(jsonPath("$.[*].languageConfidence").value(hasItem(DEFAULT_LANGUAGE_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].pageCount").value(hasItem(DEFAULT_PAGE_COUNT)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return documentRepository.count();
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

    protected Document getPersistedDocument(Document document) {
        return documentRepository.findById(document.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentToMatchAllProperties(Document expectedDocument) {
        assertDocumentAllPropertiesEquals(expectedDocument, getPersistedDocument(expectedDocument));
    }

    protected void assertPersistedDocumentToMatchUpdatableProperties(Document expectedDocument) {
        assertDocumentAllUpdatablePropertiesEquals(expectedDocument, getPersistedDocument(expectedDocument));
    }
}
