package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTemplateAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTemplateSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTemplateMapper;
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
 * Integration tests for the {@link DocumentTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_TEMPLATE_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_TEMPLATE_S_3_KEY = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-templates/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTemplateRepository documentTemplateRepository;

    @Autowired
    private DocumentTemplateMapper documentTemplateMapper;

    @Autowired
    private DocumentTemplateSearchRepository documentTemplateSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTemplateMockMvc;

    private DocumentTemplate documentTemplate;

    private DocumentTemplate insertedDocumentTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTemplate createEntity() {
        return new DocumentTemplate()
            .name(DEFAULT_NAME)
            .templateSha256(DEFAULT_TEMPLATE_SHA_256)
            .templateS3Key(DEFAULT_TEMPLATE_S_3_KEY)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTemplate createUpdatedEntity() {
        return new DocumentTemplate()
            .name(UPDATED_NAME)
            .templateSha256(UPDATED_TEMPLATE_SHA_256)
            .templateS3Key(UPDATED_TEMPLATE_S_3_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentTemplate = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentTemplate != null) {
            documentTemplateRepository.delete(insertedDocumentTemplate);
            documentTemplateSearchRepository.delete(insertedDocumentTemplate);
            insertedDocumentTemplate = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);
        var returnedDocumentTemplateDTO = om.readValue(
            restDocumentTemplateMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentTemplateDTO.class
        );

        // Validate the DocumentTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentTemplate = documentTemplateMapper.toEntity(returnedDocumentTemplateDTO);
        assertDocumentTemplateUpdatableFieldsEquals(returnedDocumentTemplate, getPersistedDocumentTemplate(returnedDocumentTemplate));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentTemplate = returnedDocumentTemplate;
    }

    @Test
    @Transactional
    void createDocumentTemplateWithExistingId() throws Exception {
        // Create the DocumentTemplate with an existing ID
        documentTemplate.setId(1L);
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setName(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTemplateSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setTemplateSha256(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkTemplateS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setTemplateS3Key(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setIsActive(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setCreatedBy(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        // set the field null
        documentTemplate.setCreatedDate(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentTemplates() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateSha256").value(hasItem(DEFAULT_TEMPLATE_SHA_256)))
            .andExpect(jsonPath("$.[*].templateS3Key").value(hasItem(DEFAULT_TEMPLATE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentTemplate() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get the documentTemplate
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, documentTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.templateSha256").value(DEFAULT_TEMPLATE_SHA_256))
            .andExpect(jsonPath("$.templateS3Key").value(DEFAULT_TEMPLATE_S_3_KEY))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        Long id = documentTemplate.getId();

        defaultDocumentTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where name equals to
        defaultDocumentTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where name in
        defaultDocumentTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where name is not null
        defaultDocumentTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where name contains
        defaultDocumentTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where name does not contain
        defaultDocumentTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateSha256IsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateSha256 equals to
        defaultDocumentTemplateFiltering(
            "templateSha256.equals=" + DEFAULT_TEMPLATE_SHA_256,
            "templateSha256.equals=" + UPDATED_TEMPLATE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateSha256IsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateSha256 in
        defaultDocumentTemplateFiltering(
            "templateSha256.in=" + DEFAULT_TEMPLATE_SHA_256 + "," + UPDATED_TEMPLATE_SHA_256,
            "templateSha256.in=" + UPDATED_TEMPLATE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateSha256IsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateSha256 is not null
        defaultDocumentTemplateFiltering("templateSha256.specified=true", "templateSha256.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateSha256ContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateSha256 contains
        defaultDocumentTemplateFiltering(
            "templateSha256.contains=" + DEFAULT_TEMPLATE_SHA_256,
            "templateSha256.contains=" + UPDATED_TEMPLATE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateSha256NotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateSha256 does not contain
        defaultDocumentTemplateFiltering(
            "templateSha256.doesNotContain=" + UPDATED_TEMPLATE_SHA_256,
            "templateSha256.doesNotContain=" + DEFAULT_TEMPLATE_SHA_256
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateS3KeyIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateS3Key equals to
        defaultDocumentTemplateFiltering(
            "templateS3Key.equals=" + DEFAULT_TEMPLATE_S_3_KEY,
            "templateS3Key.equals=" + UPDATED_TEMPLATE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateS3KeyIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateS3Key in
        defaultDocumentTemplateFiltering(
            "templateS3Key.in=" + DEFAULT_TEMPLATE_S_3_KEY + "," + UPDATED_TEMPLATE_S_3_KEY,
            "templateS3Key.in=" + UPDATED_TEMPLATE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateS3KeyIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateS3Key is not null
        defaultDocumentTemplateFiltering("templateS3Key.specified=true", "templateS3Key.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateS3KeyContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateS3Key contains
        defaultDocumentTemplateFiltering(
            "templateS3Key.contains=" + DEFAULT_TEMPLATE_S_3_KEY,
            "templateS3Key.contains=" + UPDATED_TEMPLATE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByTemplateS3KeyNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where templateS3Key does not contain
        defaultDocumentTemplateFiltering(
            "templateS3Key.doesNotContain=" + UPDATED_TEMPLATE_S_3_KEY,
            "templateS3Key.doesNotContain=" + DEFAULT_TEMPLATE_S_3_KEY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where isActive equals to
        defaultDocumentTemplateFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where isActive in
        defaultDocumentTemplateFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where isActive is not null
        defaultDocumentTemplateFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdBy equals to
        defaultDocumentTemplateFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdBy in
        defaultDocumentTemplateFiltering(
            "createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY,
            "createdBy.in=" + UPDATED_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdBy is not null
        defaultDocumentTemplateFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdBy contains
        defaultDocumentTemplateFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdBy does not contain
        defaultDocumentTemplateFiltering(
            "createdBy.doesNotContain=" + UPDATED_CREATED_BY,
            "createdBy.doesNotContain=" + DEFAULT_CREATED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdDate equals to
        defaultDocumentTemplateFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdDate in
        defaultDocumentTemplateFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        // Get all the documentTemplateList where createdDate is not null
        defaultDocumentTemplateFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTemplatesByDocumentTypeIsEqualToSomething() throws Exception {
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentTemplateRepository.saveAndFlush(documentTemplate);
            documentType = DocumentTypeResourceIT.createEntity();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        em.persist(documentType);
        em.flush();
        documentTemplate.setDocumentType(documentType);
        documentTemplateRepository.saveAndFlush(documentTemplate);
        Long documentTypeId = documentType.getId();
        // Get all the documentTemplateList where documentType equals to documentTypeId
        defaultDocumentTemplateShouldBeFound("documentTypeId.equals=" + documentTypeId);

        // Get all the documentTemplateList where documentType equals to (documentTypeId + 1)
        defaultDocumentTemplateShouldNotBeFound("documentTypeId.equals=" + (documentTypeId + 1));
    }

    private void defaultDocumentTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentTemplateShouldBeFound(shouldBeFound);
        defaultDocumentTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTemplateShouldBeFound(String filter) throws Exception {
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateSha256").value(hasItem(DEFAULT_TEMPLATE_SHA_256)))
            .andExpect(jsonPath("$.[*].templateS3Key").value(hasItem(DEFAULT_TEMPLATE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTemplateShouldNotBeFound(String filter) throws Exception {
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentTemplate() throws Exception {
        // Get the documentTemplate
        restDocumentTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentTemplate() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTemplateSearchRepository.save(documentTemplate);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());

        // Update the documentTemplate
        DocumentTemplate updatedDocumentTemplate = documentTemplateRepository.findById(documentTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentTemplate are not directly saved in db
        em.detach(updatedDocumentTemplate);
        updatedDocumentTemplate
            .name(UPDATED_NAME)
            .templateSha256(UPDATED_TEMPLATE_SHA_256)
            .templateS3Key(UPDATED_TEMPLATE_S_3_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(updatedDocumentTemplate);

        restDocumentTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentTemplateToMatchAllProperties(updatedDocumentTemplate);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentTemplate> documentTemplateSearchList = Streamable.of(documentTemplateSearchRepository.findAll()).toList();
                DocumentTemplate testDocumentTemplateSearch = documentTemplateSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentTemplateAllPropertiesEquals(testDocumentTemplateSearch, updatedDocumentTemplate);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTemplateDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTemplate using partial update
        DocumentTemplate partialUpdatedDocumentTemplate = new DocumentTemplate();
        partialUpdatedDocumentTemplate.setId(documentTemplate.getId());

        partialUpdatedDocumentTemplate
            .templateS3Key(UPDATED_TEMPLATE_S_3_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTemplate))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentTemplate, documentTemplate),
            getPersistedDocumentTemplate(documentTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentTemplate using partial update
        DocumentTemplate partialUpdatedDocumentTemplate = new DocumentTemplate();
        partialUpdatedDocumentTemplate.setId(documentTemplate.getId());

        partialUpdatedDocumentTemplate
            .name(UPDATED_NAME)
            .templateSha256(UPDATED_TEMPLATE_SHA_256)
            .templateS3Key(UPDATED_TEMPLATE_S_3_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentTemplate.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentTemplate))
            )
            .andExpect(status().isOk());

        // Validate the DocumentTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTemplateUpdatableFieldsEquals(
            partialUpdatedDocumentTemplate,
            getPersistedDocumentTemplate(partialUpdatedDocumentTemplate)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentTemplateDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentTemplate() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);
        documentTemplateRepository.save(documentTemplate);
        documentTemplateSearchRepository.save(documentTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentTemplate
        restDocumentTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTemplateSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentTemplate() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);
        documentTemplateSearchRepository.save(documentTemplate);

        // Search the documentTemplate
        restDocumentTemplateMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].templateSha256").value(hasItem(DEFAULT_TEMPLATE_SHA_256)))
            .andExpect(jsonPath("$.[*].templateS3Key").value(hasItem(DEFAULT_TEMPLATE_S_3_KEY)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentTemplateRepository.count();
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

    protected DocumentTemplate getPersistedDocumentTemplate(DocumentTemplate documentTemplate) {
        return documentTemplateRepository.findById(documentTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentTemplateToMatchAllProperties(DocumentTemplate expectedDocumentTemplate) {
        assertDocumentTemplateAllPropertiesEquals(expectedDocumentTemplate, getPersistedDocumentTemplate(expectedDocumentTemplate));
    }

    protected void assertPersistedDocumentTemplateToMatchUpdatableProperties(DocumentTemplate expectedDocumentTemplate) {
        assertDocumentTemplateAllUpdatablePropertiesEquals(
            expectedDocumentTemplate,
            getPersistedDocumentTemplate(expectedDocumentTemplate)
        );
    }
}
