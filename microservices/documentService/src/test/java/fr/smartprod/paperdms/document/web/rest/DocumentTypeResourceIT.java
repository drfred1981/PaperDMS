package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTypeAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTypeRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentTypeSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTypeMapper;
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
 * Integration tests for the {@link DocumentTypeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentTypeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBB";

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/document-types";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-types/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTypeRepository documentTypeRepository;

    @Autowired
    private DocumentTypeMapper documentTypeMapper;

    @Autowired
    private DocumentTypeSearchRepository documentTypeSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentTypeMockMvc;

    private DocumentType documentType;

    private DocumentType insertedDocumentType;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createEntity() {
        return new DocumentType()
            .name(DEFAULT_NAME)
            .code(DEFAULT_CODE)
            .icon(DEFAULT_ICON)
            .color(DEFAULT_COLOR)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentType createUpdatedEntity() {
        return new DocumentType()
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        documentType = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentType != null) {
            documentTypeRepository.delete(insertedDocumentType);
            documentTypeSearchRepository.delete(insertedDocumentType);
            insertedDocumentType = null;
        }
    }

    @Test
    @Transactional
    void createDocumentType() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);
        var returnedDocumentTypeDTO = om.readValue(
            restDocumentTypeMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentTypeDTO.class
        );

        // Validate the DocumentType in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentType = documentTypeMapper.toEntity(returnedDocumentTypeDTO);
        assertDocumentTypeUpdatableFieldsEquals(returnedDocumentType, getPersistedDocumentType(returnedDocumentType));

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentType = returnedDocumentType;
    }

    @Test
    @Transactional
    void createDocumentTypeWithExistingId() throws Exception {
        // Create the DocumentType with an existing ID
        documentType.setId(1L);
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // set the field null
        documentType.setName(null);

        // Create the DocumentType, which fails.
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // set the field null
        documentType.setCode(null);

        // Create the DocumentType, which fails.
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // set the field null
        documentType.setIsActive(null);

        // Create the DocumentType, which fails.
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // set the field null
        documentType.setCreatedDate(null);

        // Create the DocumentType, which fails.
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        // set the field null
        documentType.setCreatedBy(null);

        // Create the DocumentType, which fails.
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        restDocumentTypeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentTypes() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get the documentType
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL_ID, documentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentType.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.code").value(DEFAULT_CODE))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getDocumentTypesByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        Long id = documentType.getId();

        defaultDocumentTypeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentTypeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentTypeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name equals to
        defaultDocumentTypeFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name in
        defaultDocumentTypeFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name is not null
        defaultDocumentTypeFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name contains
        defaultDocumentTypeFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where name does not contain
        defaultDocumentTypeFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCodeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where code equals to
        defaultDocumentTypeFiltering("code.equals=" + DEFAULT_CODE, "code.equals=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCodeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where code in
        defaultDocumentTypeFiltering("code.in=" + DEFAULT_CODE + "," + UPDATED_CODE, "code.in=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCodeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where code is not null
        defaultDocumentTypeFiltering("code.specified=true", "code.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCodeContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where code contains
        defaultDocumentTypeFiltering("code.contains=" + DEFAULT_CODE, "code.contains=" + UPDATED_CODE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCodeNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where code does not contain
        defaultDocumentTypeFiltering("code.doesNotContain=" + UPDATED_CODE, "code.doesNotContain=" + DEFAULT_CODE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIconIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where icon equals to
        defaultDocumentTypeFiltering("icon.equals=" + DEFAULT_ICON, "icon.equals=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIconIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where icon in
        defaultDocumentTypeFiltering("icon.in=" + DEFAULT_ICON + "," + UPDATED_ICON, "icon.in=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIconIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where icon is not null
        defaultDocumentTypeFiltering("icon.specified=true", "icon.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIconContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where icon contains
        defaultDocumentTypeFiltering("icon.contains=" + DEFAULT_ICON, "icon.contains=" + UPDATED_ICON);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIconNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where icon does not contain
        defaultDocumentTypeFiltering("icon.doesNotContain=" + UPDATED_ICON, "icon.doesNotContain=" + DEFAULT_ICON);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where color equals to
        defaultDocumentTypeFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where color in
        defaultDocumentTypeFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where color is not null
        defaultDocumentTypeFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where color contains
        defaultDocumentTypeFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where color does not contain
        defaultDocumentTypeFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive equals to
        defaultDocumentTypeFiltering("isActive.equals=" + DEFAULT_IS_ACTIVE, "isActive.equals=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive in
        defaultDocumentTypeFiltering("isActive.in=" + DEFAULT_IS_ACTIVE + "," + UPDATED_IS_ACTIVE, "isActive.in=" + UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByIsActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where isActive is not null
        defaultDocumentTypeFiltering("isActive.specified=true", "isActive.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdDate equals to
        defaultDocumentTypeFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdDate in
        defaultDocumentTypeFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdDate is not null
        defaultDocumentTypeFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdBy equals to
        defaultDocumentTypeFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdBy in
        defaultDocumentTypeFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdBy is not null
        defaultDocumentTypeFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdBy contains
        defaultDocumentTypeFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentTypesByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        // Get all the documentTypeList where createdBy does not contain
        defaultDocumentTypeFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    private void defaultDocumentTypeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentTypeShouldBeFound(shouldBeFound);
        defaultDocumentTypeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentTypeShouldBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentTypeShouldNotBeFound(String filter) throws Exception {
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentTypeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentType() throws Exception {
        // Get the documentType
        restDocumentTypeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTypeSearchRepository.save(documentType);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());

        // Update the documentType
        DocumentType updatedDocumentType = documentTypeRepository.findById(documentType.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentType are not directly saved in db
        em.detach(updatedDocumentType);
        updatedDocumentType
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(updatedDocumentType);

        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentTypeToMatchAllProperties(updatedDocumentType);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentType> documentTypeSearchList = Streamable.of(documentTypeSearchRepository.findAll()).toList();
                DocumentType testDocumentTypeSearch = documentTypeSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentTypeAllPropertiesEquals(testDocumentTypeSearch, updatedDocumentType);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentTypeDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentType, documentType),
            getPersistedDocumentType(documentType)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentTypeWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentType using partial update
        DocumentType partialUpdatedDocumentType = new DocumentType();
        partialUpdatedDocumentType.setId(documentType.getId());

        partialUpdatedDocumentType
            .name(UPDATED_NAME)
            .code(UPDATED_CODE)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .isActive(UPDATED_IS_ACTIVE)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentType.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentType))
            )
            .andExpect(status().isOk());

        // Validate the DocumentType in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentTypeUpdatableFieldsEquals(partialUpdatedDocumentType, getPersistedDocumentType(partialUpdatedDocumentType));
    }

    @Test
    @Transactional
    void patchNonExistingDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentTypeDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentTypeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentType() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        documentType.setId(longCount.incrementAndGet());

        // Create the DocumentType
        DocumentTypeDTO documentTypeDTO = documentTypeMapper.toDto(documentType);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTypeMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTypeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentType in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);
        documentTypeRepository.save(documentType);
        documentTypeSearchRepository.save(documentType);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentType
        restDocumentTypeMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentType.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentTypeSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentType() throws Exception {
        // Initialize the database
        insertedDocumentType = documentTypeRepository.saveAndFlush(documentType);
        documentTypeSearchRepository.save(documentType);

        // Search the documentType
        restDocumentTypeMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentType.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].code").value(hasItem(DEFAULT_CODE)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    protected long getRepositoryCount() {
        return documentTypeRepository.count();
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

    protected DocumentType getPersistedDocumentType(DocumentType documentType) {
        return documentTypeRepository.findById(documentType.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentTypeToMatchAllProperties(DocumentType expectedDocumentType) {
        assertDocumentTypeAllPropertiesEquals(expectedDocumentType, getPersistedDocumentType(expectedDocumentType));
    }

    protected void assertPersistedDocumentTypeToMatchUpdatableProperties(DocumentType expectedDocumentType) {
        assertDocumentTypeAllUpdatablePropertiesEquals(expectedDocumentType, getPersistedDocumentType(expectedDocumentType));
    }
}
