package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentPermissionAsserts.*;
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
import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.domain.enumeration.PermissionType;
import fr.smartprod.paperdms.document.domain.enumeration.PrincipalType;
import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
import fr.smartprod.paperdms.document.repository.search.DocumentPermissionSearchRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentPermissionMapper;
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
 * Integration tests for the {@link DocumentPermissionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DocumentPermissionResourceIT {

    private static final PrincipalType DEFAULT_PRINCIPAL_TYPE = PrincipalType.USER;
    private static final PrincipalType UPDATED_PRINCIPAL_TYPE = PrincipalType.GROUP;

    private static final String DEFAULT_PRINCIPAL_ID = "AAAAAAAAAA";
    private static final String UPDATED_PRINCIPAL_ID = "BBBBBBBBBB";

    private static final PermissionType DEFAULT_PERMISSION = PermissionType.VIEW;
    private static final PermissionType UPDATED_PERMISSION = PermissionType.DOWNLOAD;

    private static final Boolean DEFAULT_CAN_DELEGATE = false;
    private static final Boolean UPDATED_CAN_DELEGATE = true;

    private static final String DEFAULT_GRANTED_BY = "AAAAAAAAAA";
    private static final String UPDATED_GRANTED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_GRANTED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_GRANTED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/document-permissions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";
    private static final String ENTITY_SEARCH_API_URL = "/api/document-permissions/_search";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentPermissionRepository documentPermissionRepository;

    @Autowired
    private DocumentPermissionMapper documentPermissionMapper;

    @Autowired
    private DocumentPermissionSearchRepository documentPermissionSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDocumentPermissionMockMvc;

    private DocumentPermission documentPermission;

    private DocumentPermission insertedDocumentPermission;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentPermission createEntity() {
        return new DocumentPermission()
            .principalType(DEFAULT_PRINCIPAL_TYPE)
            .principalId(DEFAULT_PRINCIPAL_ID)
            .permission(DEFAULT_PERMISSION)
            .canDelegate(DEFAULT_CAN_DELEGATE)
            .grantedBy(DEFAULT_GRANTED_BY)
            .grantedDate(DEFAULT_GRANTED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentPermission createUpdatedEntity() {
        return new DocumentPermission()
            .principalType(UPDATED_PRINCIPAL_TYPE)
            .principalId(UPDATED_PRINCIPAL_ID)
            .permission(UPDATED_PERMISSION)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .grantedBy(UPDATED_GRANTED_BY)
            .grantedDate(UPDATED_GRANTED_DATE);
    }

    @BeforeEach
    void initTest() {
        documentPermission = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentPermission != null) {
            documentPermissionRepository.delete(insertedDocumentPermission);
            documentPermissionSearchRepository.delete(insertedDocumentPermission);
            insertedDocumentPermission = null;
        }
    }

    @Test
    @Transactional
    void createDocumentPermission() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);
        var returnedDocumentPermissionDTO = om.readValue(
            restDocumentPermissionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DocumentPermissionDTO.class
        );

        // Validate the DocumentPermission in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedDocumentPermission = documentPermissionMapper.toEntity(returnedDocumentPermissionDTO);
        assertDocumentPermissionUpdatableFieldsEquals(
            returnedDocumentPermission,
            getPersistedDocumentPermission(returnedDocumentPermission)
        );

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore + 1);
            });

        insertedDocumentPermission = returnedDocumentPermission;
    }

    @Test
    @Transactional
    void createDocumentPermissionWithExistingId() throws Exception {
        // Create the DocumentPermission with an existing ID
        documentPermission.setId(1L);
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        long databaseSizeBeforeCreate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPrincipalTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setPrincipalType(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPrincipalIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setPrincipalId(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkPermissionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setPermission(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkCanDelegateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setCanDelegate(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGrantedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setGrantedBy(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void checkGrantedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        // set the field null
        documentPermission.setGrantedDate(null);

        // Create the DocumentPermission, which fails.
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        restDocumentPermissionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);

        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void getAllDocumentPermissions() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].principalType").value(hasItem(DEFAULT_PRINCIPAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].principalId").value(hasItem(DEFAULT_PRINCIPAL_ID)))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].grantedBy").value(hasItem(DEFAULT_GRANTED_BY)))
            .andExpect(jsonPath("$.[*].grantedDate").value(hasItem(DEFAULT_GRANTED_DATE.toString())));
    }

    @Test
    @Transactional
    void getDocumentPermission() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get the documentPermission
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL_ID, documentPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(documentPermission.getId().intValue()))
            .andExpect(jsonPath("$.principalType").value(DEFAULT_PRINCIPAL_TYPE.toString()))
            .andExpect(jsonPath("$.principalId").value(DEFAULT_PRINCIPAL_ID))
            .andExpect(jsonPath("$.permission").value(DEFAULT_PERMISSION.toString()))
            .andExpect(jsonPath("$.canDelegate").value(DEFAULT_CAN_DELEGATE))
            .andExpect(jsonPath("$.grantedBy").value(DEFAULT_GRANTED_BY))
            .andExpect(jsonPath("$.grantedDate").value(DEFAULT_GRANTED_DATE.toString()));
    }

    @Test
    @Transactional
    void getDocumentPermissionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        Long id = documentPermission.getId();

        defaultDocumentPermissionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultDocumentPermissionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultDocumentPermissionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalTypeIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalType equals to
        defaultDocumentPermissionFiltering(
            "principalType.equals=" + DEFAULT_PRINCIPAL_TYPE,
            "principalType.equals=" + UPDATED_PRINCIPAL_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalTypeIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalType in
        defaultDocumentPermissionFiltering(
            "principalType.in=" + DEFAULT_PRINCIPAL_TYPE + "," + UPDATED_PRINCIPAL_TYPE,
            "principalType.in=" + UPDATED_PRINCIPAL_TYPE
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalTypeIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalType is not null
        defaultDocumentPermissionFiltering("principalType.specified=true", "principalType.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalIdIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalId equals to
        defaultDocumentPermissionFiltering("principalId.equals=" + DEFAULT_PRINCIPAL_ID, "principalId.equals=" + UPDATED_PRINCIPAL_ID);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalIdIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalId in
        defaultDocumentPermissionFiltering(
            "principalId.in=" + DEFAULT_PRINCIPAL_ID + "," + UPDATED_PRINCIPAL_ID,
            "principalId.in=" + UPDATED_PRINCIPAL_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalIdIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalId is not null
        defaultDocumentPermissionFiltering("principalId.specified=true", "principalId.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalIdContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalId contains
        defaultDocumentPermissionFiltering("principalId.contains=" + DEFAULT_PRINCIPAL_ID, "principalId.contains=" + UPDATED_PRINCIPAL_ID);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPrincipalIdNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where principalId does not contain
        defaultDocumentPermissionFiltering(
            "principalId.doesNotContain=" + UPDATED_PRINCIPAL_ID,
            "principalId.doesNotContain=" + DEFAULT_PRINCIPAL_ID
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPermissionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where permission equals to
        defaultDocumentPermissionFiltering("permission.equals=" + DEFAULT_PERMISSION, "permission.equals=" + UPDATED_PERMISSION);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPermissionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where permission in
        defaultDocumentPermissionFiltering(
            "permission.in=" + DEFAULT_PERMISSION + "," + UPDATED_PERMISSION,
            "permission.in=" + UPDATED_PERMISSION
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByPermissionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where permission is not null
        defaultDocumentPermissionFiltering("permission.specified=true", "permission.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByCanDelegateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where canDelegate equals to
        defaultDocumentPermissionFiltering("canDelegate.equals=" + DEFAULT_CAN_DELEGATE, "canDelegate.equals=" + UPDATED_CAN_DELEGATE);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByCanDelegateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where canDelegate in
        defaultDocumentPermissionFiltering(
            "canDelegate.in=" + DEFAULT_CAN_DELEGATE + "," + UPDATED_CAN_DELEGATE,
            "canDelegate.in=" + UPDATED_CAN_DELEGATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByCanDelegateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where canDelegate is not null
        defaultDocumentPermissionFiltering("canDelegate.specified=true", "canDelegate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedBy equals to
        defaultDocumentPermissionFiltering("grantedBy.equals=" + DEFAULT_GRANTED_BY, "grantedBy.equals=" + UPDATED_GRANTED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedBy in
        defaultDocumentPermissionFiltering(
            "grantedBy.in=" + DEFAULT_GRANTED_BY + "," + UPDATED_GRANTED_BY,
            "grantedBy.in=" + UPDATED_GRANTED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedBy is not null
        defaultDocumentPermissionFiltering("grantedBy.specified=true", "grantedBy.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedByContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedBy contains
        defaultDocumentPermissionFiltering("grantedBy.contains=" + DEFAULT_GRANTED_BY, "grantedBy.contains=" + UPDATED_GRANTED_BY);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedBy does not contain
        defaultDocumentPermissionFiltering(
            "grantedBy.doesNotContain=" + UPDATED_GRANTED_BY,
            "grantedBy.doesNotContain=" + DEFAULT_GRANTED_BY
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedDate equals to
        defaultDocumentPermissionFiltering("grantedDate.equals=" + DEFAULT_GRANTED_DATE, "grantedDate.equals=" + UPDATED_GRANTED_DATE);
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedDate in
        defaultDocumentPermissionFiltering(
            "grantedDate.in=" + DEFAULT_GRANTED_DATE + "," + UPDATED_GRANTED_DATE,
            "grantedDate.in=" + UPDATED_GRANTED_DATE
        );
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByGrantedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        // Get all the documentPermissionList where grantedDate is not null
        defaultDocumentPermissionFiltering("grantedDate.specified=true", "grantedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByDocumentIsEqualToSomething() throws Exception {
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            documentPermissionRepository.saveAndFlush(documentPermission);
            document = DocumentResourceIT.createEntity();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        em.persist(document);
        em.flush();
        documentPermission.setDocument(document);
        documentPermissionRepository.saveAndFlush(documentPermission);
        Long documentId = document.getId();
        // Get all the documentPermissionList where document equals to documentId
        defaultDocumentPermissionShouldBeFound("documentId.equals=" + documentId);

        // Get all the documentPermissionList where document equals to (documentId + 1)
        defaultDocumentPermissionShouldNotBeFound("documentId.equals=" + (documentId + 1));
    }

    @Test
    @Transactional
    void getAllDocumentPermissionsByMetaPermissionGroupIsEqualToSomething() throws Exception {
        MetaPermissionGroup metaPermissionGroup;
        if (TestUtil.findAll(em, MetaPermissionGroup.class).isEmpty()) {
            documentPermissionRepository.saveAndFlush(documentPermission);
            metaPermissionGroup = MetaPermissionGroupResourceIT.createEntity();
        } else {
            metaPermissionGroup = TestUtil.findAll(em, MetaPermissionGroup.class).get(0);
        }
        em.persist(metaPermissionGroup);
        em.flush();
        documentPermission.setMetaPermissionGroup(metaPermissionGroup);
        documentPermissionRepository.saveAndFlush(documentPermission);
        Long metaPermissionGroupId = metaPermissionGroup.getId();
        // Get all the documentPermissionList where metaPermissionGroup equals to metaPermissionGroupId
        defaultDocumentPermissionShouldBeFound("metaPermissionGroupId.equals=" + metaPermissionGroupId);

        // Get all the documentPermissionList where metaPermissionGroup equals to (metaPermissionGroupId + 1)
        defaultDocumentPermissionShouldNotBeFound("metaPermissionGroupId.equals=" + (metaPermissionGroupId + 1));
    }

    private void defaultDocumentPermissionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultDocumentPermissionShouldBeFound(shouldBeFound);
        defaultDocumentPermissionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultDocumentPermissionShouldBeFound(String filter) throws Exception {
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].principalType").value(hasItem(DEFAULT_PRINCIPAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].principalId").value(hasItem(DEFAULT_PRINCIPAL_ID)))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].grantedBy").value(hasItem(DEFAULT_GRANTED_BY)))
            .andExpect(jsonPath("$.[*].grantedDate").value(hasItem(DEFAULT_GRANTED_DATE.toString())));

        // Check, that the count call also returns 1
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultDocumentPermissionShouldNotBeFound(String filter) throws Exception {
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingDocumentPermission() throws Exception {
        // Get the documentPermission
        restDocumentPermissionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDocumentPermission() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentPermissionSearchRepository.save(documentPermission);
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());

        // Update the documentPermission
        DocumentPermission updatedDocumentPermission = documentPermissionRepository.findById(documentPermission.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentPermission are not directly saved in db
        em.detach(updatedDocumentPermission);
        updatedDocumentPermission
            .principalType(UPDATED_PRINCIPAL_TYPE)
            .principalId(UPDATED_PRINCIPAL_ID)
            .permission(UPDATED_PERMISSION)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .grantedBy(UPDATED_GRANTED_BY)
            .grantedDate(UPDATED_GRANTED_DATE);
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(updatedDocumentPermission);

        restDocumentPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentPermissionDTO))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDocumentPermissionToMatchAllProperties(updatedDocumentPermission);

        await()
            .atMost(5, TimeUnit.SECONDS)
            .untilAsserted(() -> {
                int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
                assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
                List<DocumentPermission> documentPermissionSearchList = Streamable.of(
                    documentPermissionSearchRepository.findAll()
                ).toList();
                DocumentPermission testDocumentPermissionSearch = documentPermissionSearchList.get(searchDatabaseSizeAfter - 1);

                assertDocumentPermissionAllPropertiesEquals(testDocumentPermissionSearch, updatedDocumentPermission);
            });
    }

    @Test
    @Transactional
    void putNonExistingDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, documentPermissionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(documentPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void partialUpdateDocumentPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentPermission using partial update
        DocumentPermission partialUpdatedDocumentPermission = new DocumentPermission();
        partialUpdatedDocumentPermission.setId(documentPermission.getId());

        partialUpdatedDocumentPermission.permission(UPDATED_PERMISSION);

        restDocumentPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentPermission))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentPermissionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDocumentPermission, documentPermission),
            getPersistedDocumentPermission(documentPermission)
        );
    }

    @Test
    @Transactional
    void fullUpdateDocumentPermissionWithPatch() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the documentPermission using partial update
        DocumentPermission partialUpdatedDocumentPermission = new DocumentPermission();
        partialUpdatedDocumentPermission.setId(documentPermission.getId());

        partialUpdatedDocumentPermission
            .principalType(UPDATED_PRINCIPAL_TYPE)
            .principalId(UPDATED_PRINCIPAL_ID)
            .permission(UPDATED_PERMISSION)
            .canDelegate(UPDATED_CAN_DELEGATE)
            .grantedBy(UPDATED_GRANTED_BY)
            .grantedDate(UPDATED_GRANTED_DATE);

        restDocumentPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDocumentPermission.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDocumentPermission))
            )
            .andExpect(status().isOk());

        // Validate the DocumentPermission in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDocumentPermissionUpdatableFieldsEquals(
            partialUpdatedDocumentPermission,
            getPersistedDocumentPermission(partialUpdatedDocumentPermission)
        );
    }

    @Test
    @Transactional
    void patchNonExistingDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, documentPermissionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(documentPermissionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentPermission() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        documentPermission.setId(longCount.incrementAndGet());

        // Create the DocumentPermission
        DocumentPermissionDTO documentPermissionDTO = documentPermissionMapper.toDto(documentPermission);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentPermissionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentPermissionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentPermission in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore);
    }

    @Test
    @Transactional
    void deleteDocumentPermission() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);
        documentPermissionRepository.save(documentPermission);
        documentPermissionSearchRepository.save(documentPermission);

        long databaseSizeBeforeDelete = getRepositoryCount();
        int searchDatabaseSizeBefore = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeBefore).isEqualTo(databaseSizeBeforeDelete);

        // Delete the documentPermission
        restDocumentPermissionMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentPermission.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
        int searchDatabaseSizeAfter = IterableUtil.sizeOf(documentPermissionSearchRepository.findAll());
        assertThat(searchDatabaseSizeAfter).isEqualTo(searchDatabaseSizeBefore - 1);
    }

    @Test
    @Transactional
    void searchDocumentPermission() throws Exception {
        // Initialize the database
        insertedDocumentPermission = documentPermissionRepository.saveAndFlush(documentPermission);
        documentPermissionSearchRepository.save(documentPermission);

        // Search the documentPermission
        restDocumentPermissionMockMvc
            .perform(get(ENTITY_SEARCH_API_URL + "?query=id:" + documentPermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(documentPermission.getId().intValue())))
            .andExpect(jsonPath("$.[*].principalType").value(hasItem(DEFAULT_PRINCIPAL_TYPE.toString())))
            .andExpect(jsonPath("$.[*].principalId").value(hasItem(DEFAULT_PRINCIPAL_ID)))
            .andExpect(jsonPath("$.[*].permission").value(hasItem(DEFAULT_PERMISSION.toString())))
            .andExpect(jsonPath("$.[*].canDelegate").value(hasItem(DEFAULT_CAN_DELEGATE)))
            .andExpect(jsonPath("$.[*].grantedBy").value(hasItem(DEFAULT_GRANTED_BY)))
            .andExpect(jsonPath("$.[*].grantedDate").value(hasItem(DEFAULT_GRANTED_DATE.toString())));
    }

    protected long getRepositoryCount() {
        return documentPermissionRepository.count();
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

    protected DocumentPermission getPersistedDocumentPermission(DocumentPermission documentPermission) {
        return documentPermissionRepository.findById(documentPermission.getId()).orElseThrow();
    }

    protected void assertPersistedDocumentPermissionToMatchAllProperties(DocumentPermission expectedDocumentPermission) {
        assertDocumentPermissionAllPropertiesEquals(expectedDocumentPermission, getPersistedDocumentPermission(expectedDocumentPermission));
    }

    protected void assertPersistedDocumentPermissionToMatchUpdatableProperties(DocumentPermission expectedDocumentPermission) {
        assertDocumentPermissionAllUpdatablePropertiesEquals(
            expectedDocumentPermission,
            getPersistedDocumentPermission(expectedDocumentPermission)
        );
    }
}
