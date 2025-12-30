package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTagAsserts.*;
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
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.domain.enumeration.MetaTagSource;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTagSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTagMapper;
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
 * Integration tests for the {@link DocumentTagResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTagResourceIT {

    private static final Instant DEFAULT_ASSIGNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ASSIGNED_BY = "AAAAAAAAAA";
    private static final String UPDATED_ASSIGNED_BY = "BBBBBBBBBB";

    private static final Double DEFAULT_CONFIDENCE = 0D;
    private static final Double UPDATED_CONFIDENCE = 1D;
    private static final Double SMALLER_CONFIDENCE = 0D - 1D;

    private static final Boolean DEFAULT_IS_AUTO_META_TAGGED = false;
    private static final Boolean UPDATED_IS_AUTO_META_TAGGED = true;

    private static final MetaTagSource DEFAULT_SOURCE = MetaTagSource.MANUAL;
    private static final MetaTagSource UPDATED_SOURCE = MetaTagSource.AI_SUGGESTED;

    private static final String ENTITY_API_URL = "/api/document-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-tags/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTagRepository documentTagRepository;

    @Autowired
    private DocumentTagMapper documentTagMapper;

    @Autowired
    private DocumentTagSearchRepository documentTagSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTagMockMvc;

    private DocumentTag documentTag;

    private DocumentTag insertedDocumentTag;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTag createEntity() {
        return new DocumentTag()
            .assignedDate(DEFAULT_ASSIGNED_DATE)
            .assignedBy(DEFAULT_ASSIGNED_BY)
            .confidence(DEFAULT_CONFIDENCE)
            .isAutoMetaTagged(DEFAULT_IS_AUTO_META_TAGGED)
            .source(DEFAULT_SOURCE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTag createUpdatedEntity() {
        return new DocumentTag()
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoMetaTagged(UPDATED_IS_AUTO_META_TAGGED)
            .source(UPDATED_SOURCE);
    }

    @BeforeEach
    void initTest() {
        documentTag = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentTag != null) {
            documentTagRepository.delete(insertedDocumentTag);
            documentTagSearchRepository.delete(insertedDocumentTag);
            insertedDocumentTag = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);
        var returnedDocumentTagDTO = om.readValue(
            restDocumentTagMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentTagDTO.class
        );

        // Validate the DocumentTag in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentTag = documentTagMapper.toEntity(returnedDocumentTagDTO);
        assertDocumentTagUpdatableFieldsEquals(returnedDocumentTag, getPersistedDocumentTag(returnedDocumentTag));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentTag = returnedDocumentTag;
    }

    @Test
    @Transactional
    void createDocumentTagWithExistingId() throws Exception {
        // Create the DocumentTag with an existing ID
        documentTag.setId(1L);
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssignedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        // set the field null
        documentTag.setAssignedDate(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkAssignedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        // set the field null
        documentTag.setAssignedBy(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsAutoMetaTaggedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        // set the field null
        documentTag.setIsAutoMetaTagged(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentTags() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedBy").value(hasItem(DEFAULT_ASSIGNED_BY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isAutoMetaTagged").value(hasItem(DEFAULT_IS_AUTO_META_TAGGED)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    @Test
    @Transactional
    void getDocumentTag() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get the documentTag
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL_ID, documentTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentTag.getId().intValue()))
            .andExpect(jsonPath("$.assignedDate").value(DEFAULT_ASSIGNED_DATE.toString()))
            .andExpect(jsonPath("$.assignedBy").value(DEFAULT_ASSIGNED_BY))
            .andExpect(jsonPath("$.confidence").value(DEFAULT_CONFIDENCE))
            .andExpect(jsonPath("$.isAutoMetaTagged").value(DEFAULT_IS_AUTO_META_TAGGED))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
    }

    @Test
    @Transactional
    void getDocumentTagsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        Long id = documentTag.getId();

        defaultDocumentTagFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentTagFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentTagFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedDate equals to
        defaultDocumentTagFiltering("assignedDate.equals=" + DEFAULT_ASSIGNED_DATE, "assignedDate.equals=" + UPDATED_ASSIGNED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedDate in
        defaultDocumentTagFiltering(
            "assignedDate.in=" + DEFAULT_ASSIGNED_DATE + "," + UPDATED_ASSIGNED_DATE,
            "assignedDate.in=" + UPDATED_ASSIGNED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedDate is not null
        defaultDocumentTagFiltering("assignedDate.specified=true", "assignedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedBy equals to
        defaultDocumentTagFiltering("assignedBy.equals=" + DEFAULT_ASSIGNED_BY, "assignedBy.equals=" + UPDATED_ASSIGNED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedBy in
        defaultDocumentTagFiltering(
            "assignedBy.in=" + DEFAULT_ASSIGNED_BY + "," + UPDATED_ASSIGNED_BY,
            "assignedBy.in=" + UPDATED_ASSIGNED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedBy is not null
        defaultDocumentTagFiltering("assignedBy.specified=true", "assignedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedBy contains
        defaultDocumentTagFiltering("assignedBy.contains=" + DEFAULT_ASSIGNED_BY, "assignedBy.contains=" + UPDATED_ASSIGNED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByAssignedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where assignedBy does not contain
        defaultDocumentTagFiltering("assignedBy.doesNotContain=" + UPDATED_ASSIGNED_BY, "assignedBy.doesNotContain=" + DEFAULT_ASSIGNED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence equals to
        defaultDocumentTagFiltering("confidence.equals=" + DEFAULT_CONFIDENCE, "confidence.equals=" + UPDATED_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence in
        defaultDocumentTagFiltering(
            "confidence.in=" + DEFAULT_CONFIDENCE + "," + UPDATED_CONFIDENCE,
            "confidence.in=" + UPDATED_CONFIDENCE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence is not null
        defaultDocumentTagFiltering("confidence.specified=true", "confidence.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence is greater than or equal to
        defaultDocumentTagFiltering(
            "confidence.greaterThanOrEqual=" + DEFAULT_CONFIDENCE,
            "confidence.greaterThanOrEqual=" + (DEFAULT_CONFIDENCE + 1)
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence is less than or equal to
        defaultDocumentTagFiltering("confidence.lessThanOrEqual=" + DEFAULT_CONFIDENCE, "confidence.lessThanOrEqual=" + SMALLER_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence is less than
        defaultDocumentTagFiltering("confidence.lessThan=" + (DEFAULT_CONFIDENCE + 1), "confidence.lessThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByConfidenceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where confidence is greater than
        defaultDocumentTagFiltering("confidence.greaterThan=" + SMALLER_CONFIDENCE, "confidence.greaterThan=" + DEFAULT_CONFIDENCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsByIsAutoMetaTaggedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where isAutoMetaTagged equals to
        defaultDocumentTagFiltering(
            "isAutoMetaTagged.equals=" + DEFAULT_IS_AUTO_META_TAGGED,
            "isAutoMetaTagged.equals=" + UPDATED_IS_AUTO_META_TAGGED
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByIsAutoMetaTaggedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where isAutoMetaTagged in
        defaultDocumentTagFiltering(
            "isAutoMetaTagged.in=" + DEFAULT_IS_AUTO_META_TAGGED + "," + UPDATED_IS_AUTO_META_TAGGED,
            "isAutoMetaTagged.in=" + UPDATED_IS_AUTO_META_TAGGED
        );
    }

    @Test
    @Transactional
    void getAllDocumentTagsByIsAutoMetaTaggedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where isAutoMetaTagged is not null
        defaultDocumentTagFiltering("isAutoMetaTagged.specified=true", "isAutoMetaTagged.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTagsBySourceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where source equals to
        defaultDocumentTagFiltering("source.equals=" + DEFAULT_SOURCE, "source.equals=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsBySourceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where source in
        defaultDocumentTagFiltering("source.in=" + DEFAULT_SOURCE + "," + UPDATED_SOURCE, "source.in=" + UPDATED_SOURCE);
    }

    @Test
    @Transactional
    void getAllDocumentTagsBySourceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        // Get all the documentTagList where source is not null
        defaultDocumentTagFiltering("source.specified=true", "source.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTagsByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentTagRepository.saveAndFlush(documentTag);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentTag.setDocument(document);
        documentTagRepository.saveAndFlush(documentTag);
        Long documentId = document.getId();
        // Get all the documentTagList where document equals to documentId
        defaultDocumentTagShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentTagList where document equals to (documentId + 1)
        defaultDocumentTagShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentTagsByMetaTagIsEqualToSomething() throws Exception {
        MetaTag metaTag;
        if (TestUtil.findAll(em, MetaTag.class).isEmpty()) {
            documentTagRepository.saveAndFlush(documentTag);
            metaTag = MetaTagResourceIT.createEntity();
        } else {
            metaTag = TestUtil.findAll(em, MetaTag.class).get(0);
        }
        em.persist(metaTag);
        em.flush();
        documentTag.setMetaTag(metaTag);
        documentTagRepository.saveAndFlush(documentTag);
        Long metaTagId = metaTag.getId();
        // Get all the documentTagList where metaTag equals to metaTagId
        defaultDocumentTagShouldBeFound("metaTagId.equals=" + metaTagId);

        // Get all the documentTagList where metaTag equals to (metaTagId + 1)
        defaultDocumentTagShouldNotBeFound("metaTagId.equals=" + (metaTagId + 1));
    }

    private void defaultDocumentTagFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentTagShouldBeFound(shouldBeFound);
        defaultDocumentTagShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTagShouldBeFound(String filter) throws Exception {
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedBy").value(hasItem(DEFAULT_ASSIGNED_BY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isAutoMetaTagged").value(hasItem(DEFAULT_IS_AUTO_META_TAGGED)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));

        // Check, that the count call also returns 1
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTagShouldNotBeFound(String filter) throws Exception {
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTagMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentTag() throws Exception {
        // Get the documentTag
        restDocumentTagMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentTag() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTagSearchRepository.save(documentTag);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());

        // Update the documentTag
        DocumentTag updatedDocumentTag = documentTagRepository.findById(documentTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentTag are not directly saved in db
        em.detach(updatedDocumentTag);
        updatedDocumentTag
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoMetaTagged(UPDATED_IS_AUTO_META_TAGGED)
            .source(UPDATED_SOURCE);
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(updatedDocumentTag);

        restDocumentTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTagDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentTagToMatchAllProperties(updatedDocumentTag);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentTag> documentTagSearchList = Streamable.of(documentTagSearchRepository.findAll()).toList();
                DocumentTag testDocumentTagSearch = documentTagSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentTagAllPropertiesEquals(testDocumentTagSearch, updatedDocumentTag);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTagDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTagWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTag using partial update
        DocumentTag partialUpdatedDocumentTag = new DocumentTag();
        partialUpdatedDocumentTag.setId(documentTag.getId());

        partialUpdatedDocumentTag.source(UPDATED_SOURCE);

        restDocumentTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTag))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTagUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentTag, documentTag),
            getPersistedDocumentTag(documentTag)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentTagWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTag using partial update
        DocumentTag partialUpdatedDocumentTag = new DocumentTag();
        partialUpdatedDocumentTag.setId(documentTag.getId());

        partialUpdatedDocumentTag
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoMetaTagged(UPDATED_IS_AUTO_META_TAGGED)
            .source(UPDATED_SOURCE);

        restDocumentTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTag.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTag))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTag in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTagUpdatableFieldsEquals(partialUpdatedDocumentTag, getPersistedDocumentTag(partialUpdatedDocumentTag));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentTagDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTagDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentTag() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);
        documentTagRepository.save(documentTag);
        documentTagSearchRepository.save(documentTag);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentTag
        restDocumentTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTagSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentTag() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);
        documentTagSearchRepository.save(documentTag);

        // Search the documentTag
        restDocumentTagMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentTag.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTag.getId().intValue())))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].assignedBy").value(hasItem(DEFAULT_ASSIGNED_BY)))
            .andExpect(jsonPath("$.[*].confidence").value(hasItem(DEFAULT_CONFIDENCE)))
            .andExpect(jsonPath("$.[*].isAutoMetaTagged").value(hasItem(DEFAULT_IS_AUTO_META_TAGGED)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE.toString())));
    }

    protected long getRepositoryCount() {
        return documentTagRepository.count();
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

    protected DocumentTag getPersistedDocumentTag(DocumentTag documentTag) {
        return documentTagRepository.findById(documentTag.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentTagToMatchAllProperties(DocumentTag expectedDocumentTag) {
        assertDocumentTagAllPropertiesEquals(expectedDocumentTag, getPersistedDocumentTag(expectedDocumentTag));
    }

    protected void assertPersistedDocumentTagToMatchUpdatableProperties(DocumentTag expectedDocumentTag) {
        assertDocumentTagAllUpdatablePropertiesEquals(expectedDocumentTag, getPersistedDocumentTag(expectedDocumentTag));
    }
}
