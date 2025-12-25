package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentCommentAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentCommentMapper;
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
 * Integration tests for the {@link DocumentCommentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentCommentResourceIT {

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;
    private static final Long SMALLER_DOCUMENT_ID = 1L - 1L;

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;
    private static final Integer SMALLER_PAGE_NUMBER = 1 - 1;

    private static final Boolean DEFAULT_IS_RESOLVED = false;
    private static final Boolean UPDATED_IS_RESOLVED = true;

    private static final String DEFAULT_AUTHOR_ID = "AAAAAAAAAA";
    private static final String UPDATED_AUTHOR_ID = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-comments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentCommentRepository documentCommentRepository;

    @Autowired
    private DocumentCommentMapper documentCommentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentCommentMockMvc;

    private DocumentComment documentComment;

    private DocumentComment insertedDocumentComment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentComment createEntity() {
        return new DocumentComment()
            .documentId(DEFAULT_DOCUMENT_ID)
            .content(DEFAULT_CONTENT)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .isResolved(DEFAULT_IS_RESOLVED)
            .authorId(DEFAULT_AUTHOR_ID)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentComment createUpdatedEntity() {
        return new DocumentComment()
            .documentId(UPDATED_DOCUMENT_ID)
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .isResolved(UPDATED_IS_RESOLVED)
            .authorId(UPDATED_AUTHOR_ID)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentComment = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentComment != null) {
            documentCommentRepository.delete(insertedDocumentComment);
            insertedDocumentComment = null;
        }
    }

    @Test
    @Transactional
    void createDocumentComment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);
        var returnedDocumentCommentDTO = om.readValue(
            restDocumentCommentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentCommentDTO.class
        );

        // Validate the DocumentComment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentComment = documentCommentMapper.toEntity(returnedDocumentCommentDTO);
        assertDocumentCommentUpdatableFieldsEquals(returnedDocumentComment, getPersistedDocumentComment(returnedDocumentComment));

        insertedDocumentComment = returnedDocumentComment;
    }

    @Test
    @Transactional
    void createDocumentCommentWithExistingId() throws Exception {
        // Create the DocumentComment with an existing ID
        documentComment.setId(1L);
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentComment.setDocumentId(null);

        // Create the DocumentComment, which fails.
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        restDocumentCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsResolvedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentComment.setIsResolved(null);

        // Create the DocumentComment, which fails.
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        restDocumentCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAuthorIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentComment.setAuthorId(null);

        // Create the DocumentComment, which fails.
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        restDocumentCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentComment.setCreatedDate(null);

        // Create the DocumentComment, which fails.
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        restDocumentCommentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentComments() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].isResolved").value(hasItem(DEFAULT_IS_RESOLVED)))
            .andExpect(jsonPath("$.[*].authorId").value(hasItem(DEFAULT_AUTHOR_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentComment() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get the documentComment
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL_ID, documentComment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentComment.getId().intValue()))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.isResolved").value(DEFAULT_IS_RESOLVED))
            .andExpect(jsonPath("$.authorId").value(DEFAULT_AUTHOR_ID))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentCommentsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        Long id = documentComment.getId();

        defaultDocumentCommentFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentCommentFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentCommentFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId equals to
        defaultDocumentCommentFiltering("documentId.equals=" + DEFAULT_DOCUMENT_ID, "documentId.equals=" + UPDATED_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId in
        defaultDocumentCommentFiltering(
            "documentId.in=" + DEFAULT_DOCUMENT_ID + "," + UPDATED_DOCUMENT_ID,
            "documentId.in=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId is not null
        defaultDocumentCommentFiltering("documentId.specified=true", "documentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId is greater than or equal to
        defaultDocumentCommentFiltering(
            "documentId.greaterThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.greaterThanOrEqual=" + UPDATED_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId is less than or equal to
        defaultDocumentCommentFiltering(
            "documentId.lessThanOrEqual=" + DEFAULT_DOCUMENT_ID,
            "documentId.lessThanOrEqual=" + SMALLER_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId is less than
        defaultDocumentCommentFiltering("documentId.lessThan=" + UPDATED_DOCUMENT_ID, "documentId.lessThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where documentId is greater than
        defaultDocumentCommentFiltering("documentId.greaterThan=" + SMALLER_DOCUMENT_ID, "documentId.greaterThan=" + DEFAULT_DOCUMENT_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber equals to
        defaultDocumentCommentFiltering("pageNumber.equals=" + DEFAULT_PAGE_NUMBER, "pageNumber.equals=" + UPDATED_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber in
        defaultDocumentCommentFiltering(
            "pageNumber.in=" + DEFAULT_PAGE_NUMBER + "," + UPDATED_PAGE_NUMBER,
            "pageNumber.in=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber is not null
        defaultDocumentCommentFiltering("pageNumber.specified=true", "pageNumber.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber is greater than or equal to
        defaultDocumentCommentFiltering(
            "pageNumber.greaterThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.greaterThanOrEqual=" + UPDATED_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber is less than or equal to
        defaultDocumentCommentFiltering(
            "pageNumber.lessThanOrEqual=" + DEFAULT_PAGE_NUMBER,
            "pageNumber.lessThanOrEqual=" + SMALLER_PAGE_NUMBER
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber is less than
        defaultDocumentCommentFiltering("pageNumber.lessThan=" + UPDATED_PAGE_NUMBER, "pageNumber.lessThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByPageNumberIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where pageNumber is greater than
        defaultDocumentCommentFiltering("pageNumber.greaterThan=" + SMALLER_PAGE_NUMBER, "pageNumber.greaterThan=" + DEFAULT_PAGE_NUMBER);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByIsResolvedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where isResolved equals to
        defaultDocumentCommentFiltering("isResolved.equals=" + DEFAULT_IS_RESOLVED, "isResolved.equals=" + UPDATED_IS_RESOLVED);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByIsResolvedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where isResolved in
        defaultDocumentCommentFiltering(
            "isResolved.in=" + DEFAULT_IS_RESOLVED + "," + UPDATED_IS_RESOLVED,
            "isResolved.in=" + UPDATED_IS_RESOLVED
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByIsResolvedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where isResolved is not null
        defaultDocumentCommentFiltering("isResolved.specified=true", "isResolved.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByAuthorIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where authorId equals to
        defaultDocumentCommentFiltering("authorId.equals=" + DEFAULT_AUTHOR_ID, "authorId.equals=" + UPDATED_AUTHOR_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByAuthorIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where authorId in
        defaultDocumentCommentFiltering("authorId.in=" + DEFAULT_AUTHOR_ID + "," + UPDATED_AUTHOR_ID, "authorId.in=" + UPDATED_AUTHOR_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByAuthorIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where authorId is not null
        defaultDocumentCommentFiltering("authorId.specified=true", "authorId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByAuthorIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where authorId contains
        defaultDocumentCommentFiltering("authorId.contains=" + DEFAULT_AUTHOR_ID, "authorId.contains=" + UPDATED_AUTHOR_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByAuthorIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where authorId does not contain
        defaultDocumentCommentFiltering("authorId.doesNotContain=" + UPDATED_AUTHOR_ID, "authorId.doesNotContain=" + DEFAULT_AUTHOR_ID);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where createdDate equals to
        defaultDocumentCommentFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where createdDate in
        defaultDocumentCommentFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        // Get all the documentCommentList where createdDate is not null
        defaultDocumentCommentFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentCommentsByParentCommentIsEqualToSomething() throws Exception {
        DocumentComment parentComment;
        if (TestUtil.findAll(em, DocumentComment.class).isEmpty()) {
            documentCommentRepository.saveAndFlush(documentComment);
            parentComment = DocumentCommentResourceIT.createEntity();
        } else {
            parentComment = TestUtil.findAll(em, DocumentComment.class).get(0);
        }
        em.persist(parentComment);
        em.flush();
        documentComment.setParentComment(parentComment);
        documentCommentRepository.saveAndFlush(documentComment);
        Long parentCommentId = parentComment.getId();
        // Get all the documentCommentList where parentComment equals to parentCommentId
        defaultDocumentCommentShouldBeFound("parentCommentId.equals=" + parentCommentId);

        // Get all the documentCommentList where parentComment equals to (parentCommentId + 1)
        defaultDocumentCommentShouldNotBeFound("parentCommentId.equals=" + (parentCommentId + 1));
    }

    private void defaultDocumentCommentFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentCommentShouldBeFound(shouldBeFound);
        defaultDocumentCommentShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentCommentShouldBeFound(String filter) throws Exception {
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentComment.getId().intValue())))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].isResolved").value(hasItem(DEFAULT_IS_RESOLVED)))
            .andExpect(jsonPath("$.[*].authorId").value(hasItem(DEFAULT_AUTHOR_ID)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentCommentShouldNotBeFound(String filter) throws Exception {
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentCommentMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentComment() throws Exception {
        // Get the documentComment
        restDocumentCommentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentComment() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentComment
        DocumentComment updatedDocumentComment = documentCommentRepository.findById(documentComment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentComment are not directly saved in db
        em.detach(updatedDocumentComment);
        updatedDocumentComment
            .documentId(UPDATED_DOCUMENT_ID)
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .isResolved(UPDATED_IS_RESOLVED)
            .authorId(UPDATED_AUTHOR_ID)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(updatedDocumentComment);

        restDocumentCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentCommentDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentCommentToMatchAllProperties(updatedDocumentComment);
    }

    @Test
    @Transactional
    void putNonExistingDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentCommentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentCommentWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentComment using partial update
        DocumentComment partialUpdatedDocumentComment = new DocumentComment();
        partialUpdatedDocumentComment.setId(documentComment.getId());

        partialUpdatedDocumentComment.documentId(UPDATED_DOCUMENT_ID).isResolved(UPDATED_IS_RESOLVED);

        restDocumentCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentComment))
            )
            .andExpect(status().isOk());

        // Validate the DocumentComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentCommentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentComment, documentComment),
            getPersistedDocumentComment(documentComment)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentCommentWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentComment using partial update
        DocumentComment partialUpdatedDocumentComment = new DocumentComment();
        partialUpdatedDocumentComment.setId(documentComment.getId());

        partialUpdatedDocumentComment
            .documentId(UPDATED_DOCUMENT_ID)
            .content(UPDATED_CONTENT)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .isResolved(UPDATED_IS_RESOLVED)
            .authorId(UPDATED_AUTHOR_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentComment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentComment))
            )
            .andExpect(status().isOk());

        // Validate the DocumentComment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentCommentUpdatableFieldsEquals(
            partialUpdatedDocumentComment,
            getPersistedDocumentComment(partialUpdatedDocumentComment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentCommentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentCommentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentComment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentComment.setId(longCount.incrementAndGet());

        // Create the DocumentComment
        DocumentCommentDTO documentCommentDTO = documentCommentMapper.toDto(documentComment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentCommentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentCommentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentComment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentComment() throws Exception {
        // Initialize the database
        insertedDocumentComment = documentCommentRepository.saveAndFlush(documentComment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentComment
        restDocumentCommentMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentComment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentCommentRepository.count();
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

    protected DocumentComment getPersistedDocumentComment(DocumentComment documentComment) {
        return documentCommentRepository.findById(documentComment.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentCommentToMatchAllProperties(DocumentComment expectedDocumentComment) {
        assertDocumentCommentAllPropertiesEquals(expectedDocumentComment, getPersistedDocumentComment(expectedDocumentComment));
    }

    protected void assertPersistedDocumentCommentToMatchUpdatableProperties(DocumentComment expectedDocumentComment) {
        assertDocumentCommentAllUpdatablePropertiesEquals(expectedDocumentComment, getPersistedDocumentComment(expectedDocumentComment));
    }
}
