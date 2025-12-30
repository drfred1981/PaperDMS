package fr.smartprod.paperdms.archive.web.rest;

import static fr.smartprod.paperdms.archive.domain.ArchiveDocumentAsserts.*;
import static fr.smartprod.paperdms.archive.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.archive.IntegrationTest;
import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveDocumentMapper;
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
 * Integration tests for the {@link ArchiveDocumentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ArchiveDocumentResourceIT {

    private static final String DEFAULT_DOCUMENT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_ORIGINAL_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ORIGINAL_PATH = "BBBBBBBBBB";

    private static final String DEFAULT_ARCHIVE_PATH = "AAAAAAAAAA";
    private static final String UPDATED_ARCHIVE_PATH = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;
    private static final Long SMALLER_FILE_SIZE = 1L - 1L;

    private static final Instant DEFAULT_ADDED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ADDED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/archive-documents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ArchiveDocumentRepository archiveDocumentRepository;

    @Autowired
    private ArchiveDocumentMapper archiveDocumentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restArchiveDocumentMockMvc;

    private ArchiveDocument archiveDocument;

    private ArchiveDocument insertedArchiveDocument;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveDocument createEntity() {
        return new ArchiveDocument()
            .documentSha256(DEFAULT_DOCUMENT_SHA_256)
            .originalPath(DEFAULT_ORIGINAL_PATH)
            .archivePath(DEFAULT_ARCHIVE_PATH)
            .fileSize(DEFAULT_FILE_SIZE)
            .addedDate(DEFAULT_ADDED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ArchiveDocument createUpdatedEntity() {
        return new ArchiveDocument()
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);
    }

    @BeforeEach
    void initTest() {
        archiveDocument = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedArchiveDocument != null) {
            archiveDocumentRepository.delete(insertedArchiveDocument);
            insertedArchiveDocument = null;
        }
    }

    @Test
    @Transactional
    void createArchiveDocument() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);
        var returnedArchiveDocumentDTO = om.readValue(
            restArchiveDocumentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ArchiveDocumentDTO.class
        );

        // Validate the ArchiveDocument in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedArchiveDocument = archiveDocumentMapper.toEntity(returnedArchiveDocumentDTO);
        assertArchiveDocumentUpdatableFieldsEquals(returnedArchiveDocument, getPersistedArchiveDocument(returnedArchiveDocument));

        insertedArchiveDocument = returnedArchiveDocument;
    }

    @Test
    @Transactional
    void createArchiveDocumentWithExistingId() throws Exception {
        // Create the ArchiveDocument with an existing ID
        archiveDocument.setId(1L);
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setDocumentSha256(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        archiveDocument.setAddedDate(null);

        // Create the ArchiveDocument, which fails.
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        restArchiveDocumentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllArchiveDocuments() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].originalPath").value(hasItem(DEFAULT_ORIGINAL_PATH)))
            .andExpect(jsonPath("$.[*].archivePath").value(hasItem(DEFAULT_ARCHIVE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));
    }

    @Test
    @Transactional
    void getArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get the archiveDocument
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL_ID, archiveDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(archiveDocument.getId().intValue()))
            .andExpect(jsonPath("$.documentSha256").value(DEFAULT_DOCUMENT_SHA_256))
            .andExpect(jsonPath("$.originalPath").value(DEFAULT_ORIGINAL_PATH))
            .andExpect(jsonPath("$.archivePath").value(DEFAULT_ARCHIVE_PATH))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.addedDate").value(DEFAULT_ADDED_DATE.toString()));
    }

    @Test
    @Transactional
    void getArchiveDocumentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        Long id = archiveDocument.getId();

        defaultArchiveDocumentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultArchiveDocumentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultArchiveDocumentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByDocumentSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where documentSha256 equals to
        defaultArchiveDocumentFiltering(
            "documentSha256.equals=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.equals=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByDocumentSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where documentSha256 in
        defaultArchiveDocumentFiltering(
            "documentSha256.in=" + DEFAULT_DOCUMENT_SHA_256 + "," + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.in=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByDocumentSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where documentSha256 is not null
        defaultArchiveDocumentFiltering("documentSha256.specified=true", "documentSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByDocumentSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where documentSha256 contains
        defaultArchiveDocumentFiltering(
            "documentSha256.contains=" + DEFAULT_DOCUMENT_SHA_256,
            "documentSha256.contains=" + UPDATED_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByDocumentSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where documentSha256 does not contain
        defaultArchiveDocumentFiltering(
            "documentSha256.doesNotContain=" + UPDATED_DOCUMENT_SHA_256,
            "documentSha256.doesNotContain=" + DEFAULT_DOCUMENT_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByOriginalPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where originalPath equals to
        defaultArchiveDocumentFiltering("originalPath.equals=" + DEFAULT_ORIGINAL_PATH, "originalPath.equals=" + UPDATED_ORIGINAL_PATH);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByOriginalPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where originalPath in
        defaultArchiveDocumentFiltering(
            "originalPath.in=" + DEFAULT_ORIGINAL_PATH + "," + UPDATED_ORIGINAL_PATH,
            "originalPath.in=" + UPDATED_ORIGINAL_PATH
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByOriginalPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where originalPath is not null
        defaultArchiveDocumentFiltering("originalPath.specified=true", "originalPath.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByOriginalPathContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where originalPath contains
        defaultArchiveDocumentFiltering("originalPath.contains=" + DEFAULT_ORIGINAL_PATH, "originalPath.contains=" + UPDATED_ORIGINAL_PATH);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByOriginalPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where originalPath does not contain
        defaultArchiveDocumentFiltering(
            "originalPath.doesNotContain=" + UPDATED_ORIGINAL_PATH,
            "originalPath.doesNotContain=" + DEFAULT_ORIGINAL_PATH
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchivePathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where archivePath equals to
        defaultArchiveDocumentFiltering("archivePath.equals=" + DEFAULT_ARCHIVE_PATH, "archivePath.equals=" + UPDATED_ARCHIVE_PATH);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchivePathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where archivePath in
        defaultArchiveDocumentFiltering(
            "archivePath.in=" + DEFAULT_ARCHIVE_PATH + "," + UPDATED_ARCHIVE_PATH,
            "archivePath.in=" + UPDATED_ARCHIVE_PATH
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchivePathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where archivePath is not null
        defaultArchiveDocumentFiltering("archivePath.specified=true", "archivePath.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchivePathContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where archivePath contains
        defaultArchiveDocumentFiltering("archivePath.contains=" + DEFAULT_ARCHIVE_PATH, "archivePath.contains=" + UPDATED_ARCHIVE_PATH);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchivePathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where archivePath does not contain
        defaultArchiveDocumentFiltering(
            "archivePath.doesNotContain=" + UPDATED_ARCHIVE_PATH,
            "archivePath.doesNotContain=" + DEFAULT_ARCHIVE_PATH
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize equals to
        defaultArchiveDocumentFiltering("fileSize.equals=" + DEFAULT_FILE_SIZE, "fileSize.equals=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize in
        defaultArchiveDocumentFiltering("fileSize.in=" + DEFAULT_FILE_SIZE + "," + UPDATED_FILE_SIZE, "fileSize.in=" + UPDATED_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize is not null
        defaultArchiveDocumentFiltering("fileSize.specified=true", "fileSize.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize is greater than or equal to
        defaultArchiveDocumentFiltering(
            "fileSize.greaterThanOrEqual=" + DEFAULT_FILE_SIZE,
            "fileSize.greaterThanOrEqual=" + UPDATED_FILE_SIZE
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize is less than or equal to
        defaultArchiveDocumentFiltering("fileSize.lessThanOrEqual=" + DEFAULT_FILE_SIZE, "fileSize.lessThanOrEqual=" + SMALLER_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize is less than
        defaultArchiveDocumentFiltering("fileSize.lessThan=" + UPDATED_FILE_SIZE, "fileSize.lessThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByFileSizeIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where fileSize is greater than
        defaultArchiveDocumentFiltering("fileSize.greaterThan=" + SMALLER_FILE_SIZE, "fileSize.greaterThan=" + DEFAULT_FILE_SIZE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByAddedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where addedDate equals to
        defaultArchiveDocumentFiltering("addedDate.equals=" + DEFAULT_ADDED_DATE, "addedDate.equals=" + UPDATED_ADDED_DATE);
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByAddedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where addedDate in
        defaultArchiveDocumentFiltering(
            "addedDate.in=" + DEFAULT_ADDED_DATE + "," + UPDATED_ADDED_DATE,
            "addedDate.in=" + UPDATED_ADDED_DATE
        );
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByAddedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        // Get all the archiveDocumentList where addedDate is not null
        defaultArchiveDocumentFiltering("addedDate.specified=true", "addedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllArchiveDocumentsByArchiveJobIsEqualToSomething() throws Exception {
        ArchiveJob archiveJob;
        if (TestUtil.findAll(em, ArchiveJob.class).isEmpty()) {
            archiveDocumentRepository.saveAndFlush(archiveDocument);
            archiveJob = ArchiveJobResourceIT.createEntity();
        } else {
            archiveJob = TestUtil.findAll(em, ArchiveJob.class).get(0);
        }
        em.persist(archiveJob);
        em.flush();
        archiveDocument.setArchiveJob(archiveJob);
        archiveDocumentRepository.saveAndFlush(archiveDocument);
        Long archiveJobId = archiveJob.getId();
        // Get all the archiveDocumentList where archiveJob equals to archiveJobId
        defaultArchiveDocumentShouldBeFound("archiveJobId.equals=" + archiveJobId);

        // Get all the archiveDocumentList where archiveJob equals to (archiveJobId + 1)
        defaultArchiveDocumentShouldNotBeFound("archiveJobId.equals=" + (archiveJobId + 1));
    }

    private void defaultArchiveDocumentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultArchiveDocumentShouldBeFound(shouldBeFound);
        defaultArchiveDocumentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultArchiveDocumentShouldBeFound(String filter) throws Exception {
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(archiveDocument.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentSha256").value(hasItem(DEFAULT_DOCUMENT_SHA_256)))
            .andExpect(jsonPath("$.[*].originalPath").value(hasItem(DEFAULT_ORIGINAL_PATH)))
            .andExpect(jsonPath("$.[*].archivePath").value(hasItem(DEFAULT_ARCHIVE_PATH)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].addedDate").value(hasItem(DEFAULT_ADDED_DATE.toString())));

        // Check, that the count call also returns 1
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultArchiveDocumentShouldNotBeFound(String filter) throws Exception {
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restArchiveDocumentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingArchiveDocument() throws Exception {
        // Get the archiveDocument
        restArchiveDocumentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument
        ArchiveDocument updatedArchiveDocument = archiveDocumentRepository.findById(archiveDocument.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedArchiveDocument are not directly saved in db
        em.detach(updatedArchiveDocument);
        updatedArchiveDocument
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(updatedArchiveDocument);

        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedArchiveDocumentToMatchAllProperties(updatedArchiveDocument);
    }

    @Test
    @Transactional
    void putNonExistingArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateArchiveDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument using partial update
        ArchiveDocument partialUpdatedArchiveDocument = new ArchiveDocument();
        partialUpdatedArchiveDocument.setId(archiveDocument.getId());

        partialUpdatedArchiveDocument.originalPath(UPDATED_ORIGINAL_PATH).archivePath(UPDATED_ARCHIVE_PATH);

        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveDocument))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveDocumentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedArchiveDocument, archiveDocument),
            getPersistedArchiveDocument(archiveDocument)
        );
    }

    @Test
    @Transactional
    void fullUpdateArchiveDocumentWithPatch() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the archiveDocument using partial update
        ArchiveDocument partialUpdatedArchiveDocument = new ArchiveDocument();
        partialUpdatedArchiveDocument.setId(archiveDocument.getId());

        partialUpdatedArchiveDocument
            .documentSha256(UPDATED_DOCUMENT_SHA_256)
            .originalPath(UPDATED_ORIGINAL_PATH)
            .archivePath(UPDATED_ARCHIVE_PATH)
            .fileSize(UPDATED_FILE_SIZE)
            .addedDate(UPDATED_ADDED_DATE);

        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedArchiveDocument.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedArchiveDocument))
            )
            .andExpect(status().isOk());

        // Validate the ArchiveDocument in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertArchiveDocumentUpdatableFieldsEquals(
            partialUpdatedArchiveDocument,
            getPersistedArchiveDocument(partialUpdatedArchiveDocument)
        );
    }

    @Test
    @Transactional
    void patchNonExistingArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, archiveDocumentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(archiveDocumentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamArchiveDocument() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        archiveDocument.setId(longCount.incrementAndGet());

        // Create the ArchiveDocument
        ArchiveDocumentDTO archiveDocumentDTO = archiveDocumentMapper.toDto(archiveDocument);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restArchiveDocumentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(archiveDocumentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ArchiveDocument in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteArchiveDocument() throws Exception {
        // Initialize the database
        insertedArchiveDocument = archiveDocumentRepository.saveAndFlush(archiveDocument);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the archiveDocument
        restArchiveDocumentMockMvc
            .perform(delete(ENTITY_API_URL_ID, archiveDocument.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return archiveDocumentRepository.count();
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

    protected ArchiveDocument getPersistedArchiveDocument(ArchiveDocument archiveDocument) {
        return archiveDocumentRepository.findById(archiveDocument.getId()).orElseThrow();
    }

    protected void assertPersistedArchiveDocumentToMatchAllProperties(ArchiveDocument expectedArchiveDocument) {
        assertArchiveDocumentAllPropertiesEquals(expectedArchiveDocument, getPersistedArchiveDocument(expectedArchiveDocument));
    }

    protected void assertPersistedArchiveDocumentToMatchUpdatableProperties(ArchiveDocument expectedArchiveDocument) {
        assertArchiveDocumentAllUpdatablePropertiesEquals(expectedArchiveDocument, getPersistedArchiveDocument(expectedArchiveDocument));
    }
}
