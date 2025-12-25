package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentRelationAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.domain.enumeration.RelationType;
import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentRelationMapper;
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
 * Integration tests for the {@link DocumentRelationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentRelationResourceIT {

    private static final Long DEFAULT_SOURCE_DOCUMENT_ID = 1L;
    private static final Long UPDATED_SOURCE_DOCUMENT_ID = 2L;
    private static final Long SMALLER_SOURCE_DOCUMENT_ID = 1L - 1L;

    private static final Long DEFAULT_TARGET_DOCUMENT_ID = 1L;
    private static final Long UPDATED_TARGET_DOCUMENT_ID = 2L;
    private static final Long SMALLER_TARGET_DOCUMENT_ID = 1L - 1L;

    private static final RelationType DEFAULT_RELATION_TYPE = RelationType.RELATED_TO;
    private static final RelationType UPDATED_RELATION_TYPE = RelationType.REPLACES;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-relations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentRelationRepository documentRelationRepository;

    @Autowired
    private DocumentRelationMapper documentRelationMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentRelationMockMvc;

    private DocumentRelation documentRelation;

    private DocumentRelation insertedDocumentRelation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentRelation createEntity() {
        return new DocumentRelation()
            .sourceDocumentId(DEFAULT_SOURCE_DOCUMENT_ID)
            .targetDocumentId(DEFAULT_TARGET_DOCUMENT_ID)
            .relationType(DEFAULT_RELATION_TYPE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentRelation createUpdatedEntity() {
        return new DocumentRelation()
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .targetDocumentId(UPDATED_TARGET_DOCUMENT_ID)
            .relationType(UPDATED_RELATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentRelation = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentRelation != null) {
            documentRelationRepository.delete(insertedDocumentRelation);
            insertedDocumentRelation = null;
        }
    }

    @Test
    @Transactional
    void createDocumentRelation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);
        var returnedDocumentRelationDTO = om.readValue(
            restDocumentRelationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentRelationDTO.class
        );

        // Validate the DocumentRelation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentRelation = documentRelationMapper.toEntity(returnedDocumentRelationDTO);
        assertDocumentRelationUpdatableFieldsEquals(returnedDocumentRelation, getPersistedDocumentRelation(returnedDocumentRelation));

        insertedDocumentRelation = returnedDocumentRelation;
    }

    @Test
    @Transactional
    void createDocumentRelationWithExistingId() throws Exception {
        // Create the DocumentRelation with an existing ID
        documentRelation.setId(1L);
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSourceDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentRelation.setSourceDocumentId(null);

        // Create the DocumentRelation, which fails.
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTargetDocumentIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentRelation.setTargetDocumentId(null);

        // Create the DocumentRelation, which fails.
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRelationTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentRelation.setRelationType(null);

        // Create the DocumentRelation, which fails.
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentRelation.setCreatedBy(null);

        // Create the DocumentRelation, which fails.
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentRelation.setCreatedDate(null);

        // Create the DocumentRelation, which fails.
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        restDocumentRelationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDocumentRelations() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentId").value(hasItem(DEFAULT_SOURCE_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetDocumentId").value(hasItem(DEFAULT_TARGET_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].relationType").value(hasItem(DEFAULT_RELATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentRelation() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get the documentRelation
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL_ID, documentRelation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentRelation.getId().intValue()))
            .andExpect(jsonPath("$.sourceDocumentId").value(DEFAULT_SOURCE_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.targetDocumentId").value(DEFAULT_TARGET_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.relationType").value(DEFAULT_RELATION_TYPE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentRelationsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        Long id = documentRelation.getId();

        defaultDocumentRelationFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentRelationFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentRelationFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId equals to
        defaultDocumentRelationFiltering(
            "sourceDocumentId.equals=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.equals=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId in
        defaultDocumentRelationFiltering(
            "sourceDocumentId.in=" + DEFAULT_SOURCE_DOCUMENT_ID + "," + UPDATED_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.in=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId is not null
        defaultDocumentRelationFiltering("sourceDocumentId.specified=true", "sourceDocumentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId is greater than or equal to
        defaultDocumentRelationFiltering(
            "sourceDocumentId.greaterThanOrEqual=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.greaterThanOrEqual=" + UPDATED_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId is less than or equal to
        defaultDocumentRelationFiltering(
            "sourceDocumentId.lessThanOrEqual=" + DEFAULT_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.lessThanOrEqual=" + SMALLER_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId is less than
        defaultDocumentRelationFiltering(
            "sourceDocumentId.lessThan=" + UPDATED_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.lessThan=" + DEFAULT_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsBySourceDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where sourceDocumentId is greater than
        defaultDocumentRelationFiltering(
            "sourceDocumentId.greaterThan=" + SMALLER_SOURCE_DOCUMENT_ID,
            "sourceDocumentId.greaterThan=" + DEFAULT_SOURCE_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId equals to
        defaultDocumentRelationFiltering(
            "targetDocumentId.equals=" + DEFAULT_TARGET_DOCUMENT_ID,
            "targetDocumentId.equals=" + UPDATED_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId in
        defaultDocumentRelationFiltering(
            "targetDocumentId.in=" + DEFAULT_TARGET_DOCUMENT_ID + "," + UPDATED_TARGET_DOCUMENT_ID,
            "targetDocumentId.in=" + UPDATED_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId is not null
        defaultDocumentRelationFiltering("targetDocumentId.specified=true", "targetDocumentId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId is greater than or equal to
        defaultDocumentRelationFiltering(
            "targetDocumentId.greaterThanOrEqual=" + DEFAULT_TARGET_DOCUMENT_ID,
            "targetDocumentId.greaterThanOrEqual=" + UPDATED_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId is less than or equal to
        defaultDocumentRelationFiltering(
            "targetDocumentId.lessThanOrEqual=" + DEFAULT_TARGET_DOCUMENT_ID,
            "targetDocumentId.lessThanOrEqual=" + SMALLER_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId is less than
        defaultDocumentRelationFiltering(
            "targetDocumentId.lessThan=" + UPDATED_TARGET_DOCUMENT_ID,
            "targetDocumentId.lessThan=" + DEFAULT_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByTargetDocumentIdIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where targetDocumentId is greater than
        defaultDocumentRelationFiltering(
            "targetDocumentId.greaterThan=" + SMALLER_TARGET_DOCUMENT_ID,
            "targetDocumentId.greaterThan=" + DEFAULT_TARGET_DOCUMENT_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByRelationTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where relationType equals to
        defaultDocumentRelationFiltering("relationType.equals=" + DEFAULT_RELATION_TYPE, "relationType.equals=" + UPDATED_RELATION_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByRelationTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where relationType in
        defaultDocumentRelationFiltering(
            "relationType.in=" + DEFAULT_RELATION_TYPE + "," + UPDATED_RELATION_TYPE,
            "relationType.in=" + UPDATED_RELATION_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByRelationTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where relationType is not null
        defaultDocumentRelationFiltering("relationType.specified=true", "relationType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdBy equals to
        defaultDocumentRelationFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdBy in
        defaultDocumentRelationFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdBy is not null
        defaultDocumentRelationFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdBy contains
        defaultDocumentRelationFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdBy does not contain
        defaultDocumentRelationFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdDate equals to
        defaultDocumentRelationFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdDate in
        defaultDocumentRelationFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentRelationsByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        // Get all the documentRelationList where createdDate is not null
        defaultDocumentRelationFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    private void defaultDocumentRelationFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentRelationShouldBeFound(shouldBeFound);
        defaultDocumentRelationShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentRelationShouldBeFound(String filter) throws Exception {
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentRelation.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceDocumentId").value(hasItem(DEFAULT_SOURCE_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].targetDocumentId").value(hasItem(DEFAULT_TARGET_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].relationType").value(hasItem(DEFAULT_RELATION_TYPE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentRelationShouldNotBeFound(String filter) throws Exception {
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentRelationMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentRelation() throws Exception {
        // Get the documentRelation
        restDocumentRelationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentRelation() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentRelation
        DocumentRelation updatedDocumentRelation = documentRelationRepository.findById(documentRelation.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentRelation are not directly saved in db
        em.detach(updatedDocumentRelation);
        updatedDocumentRelation
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .targetDocumentId(UPDATED_TARGET_DOCUMENT_ID)
            .relationType(UPDATED_RELATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(updatedDocumentRelation);

        restDocumentRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentRelationDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentRelationToMatchAllProperties(updatedDocumentRelation);
    }

    @Test
    @Transactional
    void putNonExistingDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentRelationDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDocumentRelationWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentRelation using partial update
        DocumentRelation partialUpdatedDocumentRelation = new DocumentRelation();
        partialUpdatedDocumentRelation.setId(documentRelation.getId());

        partialUpdatedDocumentRelation
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .targetDocumentId(UPDATED_TARGET_DOCUMENT_ID)
            .relationType(UPDATED_RELATION_TYPE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentRelation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentRelationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentRelation, documentRelation),
            getPersistedDocumentRelation(documentRelation)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentRelationWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentRelation using partial update
        DocumentRelation partialUpdatedDocumentRelation = new DocumentRelation();
        partialUpdatedDocumentRelation.setId(documentRelation.getId());

        partialUpdatedDocumentRelation
            .sourceDocumentId(UPDATED_SOURCE_DOCUMENT_ID)
            .targetDocumentId(UPDATED_TARGET_DOCUMENT_ID)
            .relationType(UPDATED_RELATION_TYPE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentRelation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentRelation))
            )
            .andExpect(status().isOk());

        // Validate the DocumentRelation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentRelationUpdatableFieldsEquals(
            partialUpdatedDocumentRelation,
            getPersistedDocumentRelation(partialUpdatedDocumentRelation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentRelationDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentRelationDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentRelation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentRelation.setId(longCount.incrementAndGet());

        // Create the DocumentRelation
        DocumentRelationDTO documentRelationDTO = documentRelationMapper.toDto(documentRelation);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentRelationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentRelationDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentRelation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentRelation() throws Exception {
        // Initialize the database
        insertedDocumentRelation = documentRelationRepository.saveAndFlush(documentRelation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentRelation
        restDocumentRelationMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentRelation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return documentRelationRepository.count();
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

    protected DocumentRelation getPersistedDocumentRelation(DocumentRelation documentRelation) {
        return documentRelationRepository.findById(documentRelation.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentRelationToMatchAllProperties(DocumentRelation expectedDocumentRelation) {
        assertDocumentRelationAllPropertiesEquals(expectedDocumentRelation, getPersistedDocumentRelation(expectedDocumentRelation));
    }

    protected void assertPersistedDocumentRelationToMatchUpdatableProperties(DocumentRelation expectedDocumentRelation) {
        assertDocumentRelationAllUpdatablePropertiesEquals(
            expectedDocumentRelation,
            getPersistedDocumentRelation(expectedDocumentRelation)
        );
    }
}
