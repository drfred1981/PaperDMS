package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.FolderAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.repository.FolderRepository;
import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import fr.smartprod.paperdms.document.service.mapper.FolderMapper;
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
 * Integration tests for the {@link FolderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FolderResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_PATH = "AAAAAAAAAA";
    private static final String UPDATED_PATH = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SHARED = false;
    private static final Boolean UPDATED_IS_SHARED = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/folders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FolderRepository folderRepository;

    @Autowired
    private FolderMapper folderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFolderMockMvc;

    private Folder folder;

    private Folder insertedFolder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Folder createEntity() {
        return new Folder()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .path(DEFAULT_PATH)
            .isShared(DEFAULT_IS_SHARED)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Folder createUpdatedEntity() {
        return new Folder()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        folder = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedFolder != null) {
            folderRepository.delete(insertedFolder);
            insertedFolder = null;
        }
    }

    @Test
    @Transactional
    void createFolder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);
        var returnedFolderDTO = om.readValue(
            restFolderMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FolderDTO.class
        );

        // Validate the Folder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFolder = folderMapper.toEntity(returnedFolderDTO);
        assertFolderUpdatableFieldsEquals(returnedFolder, getPersistedFolder(returnedFolder));

        insertedFolder = returnedFolder;
    }

    @Test
    @Transactional
    void createFolderWithExistingId() throws Exception {
        // Create the Folder with an existing ID
        folder.setId(1L);
        FolderDTO folderDTO = folderMapper.toDto(folder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        folder.setName(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSharedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        folder.setIsShared(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        folder.setCreatedDate(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        folder.setCreatedBy(null);

        // Create the Folder, which fails.
        FolderDTO folderDTO = folderMapper.toDto(folder);

        restFolderMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFolders() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList
        restFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(folder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getFolder() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get the folder
        restFolderMockMvc
            .perform(get(ENTITY_API_URL_ID, folder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(folder.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH))
            .andExpect(jsonPath("$.isShared").value(DEFAULT_IS_SHARED))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getFoldersByIdFiltering() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        Long id = folder.getId();

        defaultFolderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultFolderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultFolderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllFoldersByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where name equals to
        defaultFolderFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFoldersByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where name in
        defaultFolderFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFoldersByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where name is not null
        defaultFolderFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllFoldersByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where name contains
        defaultFolderFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllFoldersByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where name does not contain
        defaultFolderFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllFoldersByPathIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where path equals to
        defaultFolderFiltering("path.equals=" + DEFAULT_PATH, "path.equals=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFoldersByPathIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where path in
        defaultFolderFiltering("path.in=" + DEFAULT_PATH + "," + UPDATED_PATH, "path.in=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFoldersByPathIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where path is not null
        defaultFolderFiltering("path.specified=true", "path.specified=false");
    }

    @Test
    @Transactional
    void getAllFoldersByPathContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where path contains
        defaultFolderFiltering("path.contains=" + DEFAULT_PATH, "path.contains=" + UPDATED_PATH);
    }

    @Test
    @Transactional
    void getAllFoldersByPathNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where path does not contain
        defaultFolderFiltering("path.doesNotContain=" + UPDATED_PATH, "path.doesNotContain=" + DEFAULT_PATH);
    }

    @Test
    @Transactional
    void getAllFoldersByIsSharedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where isShared equals to
        defaultFolderFiltering("isShared.equals=" + DEFAULT_IS_SHARED, "isShared.equals=" + UPDATED_IS_SHARED);
    }

    @Test
    @Transactional
    void getAllFoldersByIsSharedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where isShared in
        defaultFolderFiltering("isShared.in=" + DEFAULT_IS_SHARED + "," + UPDATED_IS_SHARED, "isShared.in=" + UPDATED_IS_SHARED);
    }

    @Test
    @Transactional
    void getAllFoldersByIsSharedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where isShared is not null
        defaultFolderFiltering("isShared.specified=true", "isShared.specified=false");
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdDate equals to
        defaultFolderFiltering("createdDate.equals=" + DEFAULT_CREATED_DATE, "createdDate.equals=" + UPDATED_CREATED_DATE);
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdDate in
        defaultFolderFiltering(
            "createdDate.in=" + DEFAULT_CREATED_DATE + "," + UPDATED_CREATED_DATE,
            "createdDate.in=" + UPDATED_CREATED_DATE
        );
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdDate is not null
        defaultFolderFiltering("createdDate.specified=true", "createdDate.specified=false");
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedByIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdBy equals to
        defaultFolderFiltering("createdBy.equals=" + DEFAULT_CREATED_BY, "createdBy.equals=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedByIsInShouldWork() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdBy in
        defaultFolderFiltering("createdBy.in=" + DEFAULT_CREATED_BY + "," + UPDATED_CREATED_BY, "createdBy.in=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedByIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdBy is not null
        defaultFolderFiltering("createdBy.specified=true", "createdBy.specified=false");
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedByContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdBy contains
        defaultFolderFiltering("createdBy.contains=" + DEFAULT_CREATED_BY, "createdBy.contains=" + UPDATED_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFoldersByCreatedByNotContainsSomething() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        // Get all the folderList where createdBy does not contain
        defaultFolderFiltering("createdBy.doesNotContain=" + UPDATED_CREATED_BY, "createdBy.doesNotContain=" + DEFAULT_CREATED_BY);
    }

    @Test
    @Transactional
    void getAllFoldersByParentIsEqualToSomething() throws Exception {
        Folder parent;
        if (TestUtil.findAll(em, Folder.class).isEmpty()) {
            folderRepository.saveAndFlush(folder);
            parent = FolderResourceIT.createEntity();
        } else {
            parent = TestUtil.findAll(em, Folder.class).get(0);
        }
        em.persist(parent);
        em.flush();
        folder.setParent(parent);
        folderRepository.saveAndFlush(folder);
        Long parentId = parent.getId();
        // Get all the folderList where parent equals to parentId
        defaultFolderShouldBeFound("parentId.equals=" + parentId);

        // Get all the folderList where parent equals to (parentId + 1)
        defaultFolderShouldNotBeFound("parentId.equals=" + (parentId + 1));
    }

    private void defaultFolderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultFolderShouldBeFound(shouldBeFound);
        defaultFolderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultFolderShouldBeFound(String filter) throws Exception {
        restFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(folder.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH)))
            .andExpect(jsonPath("$.[*].isShared").value(hasItem(DEFAULT_IS_SHARED)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));

        // Check, that the count call also returns 1
        restFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultFolderShouldNotBeFound(String filter) throws Exception {
        restFolderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restFolderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingFolder() throws Exception {
        // Get the folder
        restFolderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFolder() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the folder
        Folder updatedFolder = folderRepository.findById(folder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFolder are not directly saved in db
        em.detach(updatedFolder);
        updatedFolder
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        FolderDTO folderDTO = folderMapper.toDto(updatedFolder);

        restFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, folderDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO))
            )
            .andExpect(status().isOk());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFolderToMatchAllProperties(updatedFolder);
    }

    @Test
    @Transactional
    void putNonExistingFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, folderDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(folderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFolderWithPatch() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the folder using partial update
        Folder partialUpdatedFolder = new Folder();
        partialUpdatedFolder.setId(folder.getId());

        partialUpdatedFolder.description(UPDATED_DESCRIPTION).path(UPDATED_PATH).createdDate(UPDATED_CREATED_DATE);

        restFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFolder))
            )
            .andExpect(status().isOk());

        // Validate the Folder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFolderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedFolder, folder), getPersistedFolder(folder));
    }

    @Test
    @Transactional
    void fullUpdateFolderWithPatch() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the folder using partial update
        Folder partialUpdatedFolder = new Folder();
        partialUpdatedFolder.setId(folder.getId());

        partialUpdatedFolder
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .path(UPDATED_PATH)
            .isShared(UPDATED_IS_SHARED)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFolder.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFolder))
            )
            .andExpect(status().isOk());

        // Validate the Folder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFolderUpdatableFieldsEquals(partialUpdatedFolder, getPersistedFolder(partialUpdatedFolder));
    }

    @Test
    @Transactional
    void patchNonExistingFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, folderDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(folderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(folderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFolder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        folder.setId(longCount.incrementAndGet());

        // Create the Folder
        FolderDTO folderDTO = folderMapper.toDto(folder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFolderMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(folderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Folder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFolder() throws Exception {
        // Initialize the database
        insertedFolder = folderRepository.saveAndFlush(folder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the folder
        restFolderMockMvc
            .perform(delete(ENTITY_API_URL_ID, folder.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return folderRepository.count();
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

    protected Folder getPersistedFolder(Folder folder) {
        return folderRepository.findById(folder.getId()).orElseThrow();
    }

    protected void assertPersistedFolderToMatchAllProperties(Folder expectedFolder) {
        assertFolderAllPropertiesEquals(expectedFolder, getPersistedFolder(expectedFolder));
    }

    protected void assertPersistedFolderToMatchUpdatableProperties(Folder expectedFolder) {
        assertFolderAllUpdatablePropertiesEquals(expectedFolder, getPersistedFolder(expectedFolder));
    }
}
