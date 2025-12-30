package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentMetadataAsserts.*;
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
import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import fr.smartprod.paperdms.document.domain.enumeration.MetadataType;
import fr.smartprod.paperdms.document.repository.DocumentMetadataRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentMetadataSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentMetadataMapper;
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
 * Integration tests for the {@link DocumentMetadataResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentMetadataResourceIT {

    private static final String DEFAULT_KEY = "AAAAAAAAAA";
    private static final String UPDATED_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_VALUE = "BBBBBBBBBB";

    private static final MetadataType DEFAULT_DATA_TYPE = MetadataType.STRING;
    private static final MetadataType UPDATED_DATA_TYPE = MetadataType.NUMBER;

    private static final Boolean DEFAULT_IS_SEARCHABLE = false;
    private static final Boolean UPDATED_IS_SEARCHABLE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-metadata";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-metadata/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentMetadataRepository documentMetadataRepository;

    @Autowired
    private DocumentMetadataMapper documentMetadataMapper;

    @Autowired
    private DocumentMetadataSearchRepository documentMetadataSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentMetadataMockMvc;

    private DocumentMetadata documentMetadata;

    private DocumentMetadata insertedDocumentMetadata;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentMetadata createEntity() {
        return new DocumentMetadata()
            .key(DEFAULT_KEY)
            .value(DEFAULT_VALUE)
            .dataType(DEFAULT_DATA_TYPE)
            .isSearchable(DEFAULT_IS_SEARCHABLE)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentMetadata createUpdatedEntity() {
        return new DocumentMetadata()
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentMetadata = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentMetadata != null) {
            documentMetadataRepository.delete(insertedDocumentMetadata);
            documentMetadataSearchRepository.delete(insertedDocumentMetadata);
            insertedDocumentMetadata = null;
        }
    }

    @Test
    @Transactional
    void createDocumentMetadata() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);
        var returnedDocumentMetadataDTO = om.readValue(
            restDocumentMetadataMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentMetadataDTO.class
        );

        // Validate the DocumentMetadata in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentMetadata = documentMetadataMapper.toEntity(returnedDocumentMetadataDTO);
        assertDocumentMetadataUpdatableFieldsEquals(returnedDocumentMetadata, getPersistedDocumentMetadata(returnedDocumentMetadata));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentMetadata = returnedDocumentMetadata;
    }

    @Test
    @Transactional
    void createDocumentMetadataWithExistingId() throws Exception {
        // Create the DocumentMetadata with an existing ID
        documentMetadata.setId(1L);
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkKeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        // set the field null
        documentMetadata.setKey(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsSearchableIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        // set the field null
        documentMetadata.setIsSearchable(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        // set the field null
        documentMetadata.setCreatedDate(null);

        // Create the DocumentMetadata, which fails.
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        restDocumentMetadataMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get the documentMetadata
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL_ID, documentMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentMetadata.getId().intValue()))
            .andExpect(jsonPath("$.key").value(DEFAULT_KEY))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.isSearchable").value(DEFAULT_IS_SEARCHABLE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentMetadataByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        Long id = documentMetadata.getId();

        defaultDocumentMetadataFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentMetadataFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentMetadataFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByKeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where key equals to
        defaultDocumentMetadataFiltering("key.equals=" + DEFAULT_KEY, "key.equals=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByKeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where key in
        defaultDocumentMetadataFiltering("key.in=" + DEFAULT_KEY + "," + UPDATED_KEY, "key.in=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByKeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where key is not null
        defaultDocumentMetadataFiltering("key.specified=true", "key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByKeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where key contains
        defaultDocumentMetadataFiltering("key.contains=" + DEFAULT_KEY, "key.contains=" + UPDATED_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByKeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where key does not contain
        defaultDocumentMetadataFiltering("key.doesNotContain=" + UPDATED_KEY, "key.doesNotContain=" + DEFAULT_KEY);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByDataTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where dataType equals to
        defaultDocumentMetadataFiltering("dataType.equals=" + DEFAULT_DATA_TYPE, "dataType.equals=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByDataTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where dataType in
        defaultDocumentMetadataFiltering("dataType.in=" + DEFAULT_DATA_TYPE + "," + UPDATED_DATA_TYPE, "dataType.in=" + UPDATED_DATA_TYPE);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByDataTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where dataType is not null
        defaultDocumentMetadataFiltering("dataType.specified=true", "dataType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByIsSearchableIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where isSearchable equals to
        defaultDocumentMetadataFiltering("isSearchable.equals=" + DEFAULT_IS_SEARCHABLE, "isSearchable.equals=" + UPDATED_IS_SEARCHABLE);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByIsSearchableIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where isSearchable in
        defaultDocumentMetadataFiltering(
            "isSearchable.in=" + DEFAULT_IS_SEARCHABLE + "," + UPDATED_IS_SEARCHABLE,
            "isSearchable.in=" + UPDATED_IS_SEARCHABLE
        );
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByIsSearchableIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where isSearchable is not null
        defaultDocumentMetadataFiltering("isSearchable.specified=true", "isSearchable.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where createdDate equals to
        defaultDocumentMetadataFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where createdDate in
        defaultDocumentMetadataFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        // Get all the documentMetadataList where createdDate is not null
        defaultDocumentMetadataFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentMetadataByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentMetadataRepository.saveAndFlush(documentMetadata);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentMetadata.setDocument(document);
        documentMetadataRepository.saveAndFlush(documentMetadata);
        Long documentId = document.getId();
        // Get all the documentMetadataList where document equals to documentId
        defaultDocumentMetadataShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentMetadataList where document equals to (documentId + 1)
        defaultDocumentMetadataShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    private void defaultDocumentMetadataFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentMetadataShouldBeFound(shouldBeFound);
        defaultDocumentMetadataShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentMetadataShouldBeFound(String filter) throws Exception {
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentMetadataShouldNotBeFound(String filter) throws Exception {
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentMetadata() throws Exception {
        // Get the documentMetadata
        restDocumentMetadataMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentMetadataSearchRepository.save(documentMetadata);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());

        // Update the documentMetadata
        DocumentMetadata updatedDocumentMetadata = documentMetadataRepository.findById(documentMetadata.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentMetadata are not directly saved in db
        em.detach(updatedDocumentMetadata);
        updatedDocumentMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(updatedDocumentMetadata);

        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentMetadataToMatchAllProperties(updatedDocumentMetadata);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentMetadata> documentMetadataSearchList = Streamable.of(documentMetadataSearchRepository.findAll()).toList();
                DocumentMetadata testDocumentMetadataSearch = documentMetadataSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentMetadataAllPropertiesEquals(testDocumentMetadataSearch, updatedDocumentMetadata);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentMetadata using partial update
        DocumentMetadata partialUpdatedDocumentMetadata = new DocumentMetadata();
        partialUpdatedDocumentMetadata.setId(documentMetadata.getId());

        partialUpdatedDocumentMetadata.isSearchable(UPDATED_IS_SEARCHABLE).createdDate(UPDATED_CREATED_DATE);

        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentMetadata))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentMetadataUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentMetadata, documentMetadata),
            getPersistedDocumentMetadata(documentMetadata)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentMetadataWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentMetadata using partial update
        DocumentMetadata partialUpdatedDocumentMetadata = new DocumentMetadata();
        partialUpdatedDocumentMetadata.setId(documentMetadata.getId());

        partialUpdatedDocumentMetadata
            .key(UPDATED_KEY)
            .value(UPDATED_VALUE)
            .dataType(UPDATED_DATA_TYPE)
            .isSearchable(UPDATED_IS_SEARCHABLE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentMetadata.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentMetadata))
            )
            .andExpect(status().isOk());

        // Validate the DocumentMetadata in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentMetadataUpdatableFieldsEquals(
            partialUpdatedDocumentMetadata,
            getPersistedDocumentMetadata(partialUpdatedDocumentMetadata)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentMetadataDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentMetadataDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentMetadata() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        documentMetadata.setId(longCount.incrementAndGet());

        // Create the DocumentMetadata
        DocumentMetadataDTO documentMetadataDTO = documentMetadataMapper.toDto(documentMetadata);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentMetadataMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentMetadataDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentMetadata in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);
        documentMetadataRepository.save(documentMetadata);
        documentMetadataSearchRepository.save(documentMetadata);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentMetadata
        restDocumentMetadataMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentMetadata.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentMetadataSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentMetadata() throws Exception {
        // Initialize the database
        insertedDocumentMetadata = documentMetadataRepository.saveAndFlush(documentMetadata);
        documentMetadataSearchRepository.save(documentMetadata);

        // Search the documentMetadata
        restDocumentMetadataMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentMetadata.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentMetadata.getId().intValue())))
            .andExpect(jsonPath("$.[*].key").value(hasItem(DEFAULT_KEY)))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].isSearchable").value(hasItem(DEFAULT_IS_SEARCHABLE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentMetadataRepository.count();
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

    protected DocumentMetadata getPersistedDocumentMetadata(DocumentMetadata documentMetadata) {
        return documentMetadataRepository.findById(documentMetadata.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentMetadataToMatchAllProperties(DocumentMetadata expectedDocumentMetadata) {
        assertDocumentMetadataAllPropertiesEquals(expectedDocumentMetadata, getPersistedDocumentMetadata(expectedDocumentMetadata));
    }

    protected void assertPersistedDocumentMetadataToMatchUpdatableProperties(DocumentMetadata expectedDocumentMetadata) {
        assertDocumentMetadataAllUpdatablePropertiesEquals(
            expectedDocumentMetadata,
            getPersistedDocumentMetadata(expectedDocumentMetadata)
        );
    }
}
