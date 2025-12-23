package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.PermissionGroupAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.PermissionGroup;
import fr.smartprod.paperdms.document.repository.PermissionGroupRepository;
import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import fr.smartprod.paperdms.document.service.mapper.PermissionGroupMapper;
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
 * Integration tests for the {@link PermissionGroupResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PermissionGroupResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PERMISSIONS = "AAAAAAAAAA";
    private static final String UPDATED_PERMISSIONS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SYSTEM = false;
    private static final Boolean UPDATED_IS_SYSTEM = true;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/permission-groups";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private PermissionGroupRepository permissionGroupRepository;

    @Autowired
    private PermissionGroupMapper permissionGroupMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPermissionGroupMockMvc;

    private PermissionGroup permissionGroup;

    private PermissionGroup insertedPermissionGroup;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionGroup createEntity() {
        return new PermissionGroup()
            .name(DEFAULT_NAME)
            .permissions(DEFAULT_PERMISSIONS)
            .isSystem(DEFAULT_IS_SYSTEM)
            .createdDate(DEFAULT_CREATED_DATE)
            .createdBy(DEFAULT_CREATED_BY);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PermissionGroup createUpdatedEntity() {
        return new PermissionGroup()
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
    }

    @BeforeEach
    void initTest() {
        permissionGroup = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedPermissionGroup != null) {
            permissionGroupRepository.delete(insertedPermissionGroup);
            insertedPermissionGroup = null;
        }
    }

    @Test
    @Transactional
    void createPermissionGroup() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);
        var returnedPermissionGroupDTO = om.readValue(
            restPermissionGroupMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            PermissionGroupDTO.class
        );

        // Validate the PermissionGroup in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedPermissionGroup = permissionGroupMapper.toEntity(returnedPermissionGroupDTO);
        assertPermissionGroupUpdatableFieldsEquals(returnedPermissionGroup, getPersistedPermissionGroup(returnedPermissionGroup));

        insertedPermissionGroup = returnedPermissionGroup;
    }

    @Test
    @Transactional
    void createPermissionGroupWithExistingId() throws Exception {
        // Create the PermissionGroup with an existing ID
        permissionGroup.setId(1L);
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissionGroup.setName(null);

        // Create the PermissionGroup, which fails.
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        restPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSystemIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissionGroup.setIsSystem(null);

        // Create the PermissionGroup, which fails.
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        restPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissionGroup.setCreatedDate(null);

        // Create the PermissionGroup, which fails.
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        restPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        permissionGroup.setCreatedBy(null);

        // Create the PermissionGroup, which fails.
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        restPermissionGroupMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPermissionGroups() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        // Get all the permissionGroupList
        restPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(permissionGroup.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].permissions").value(hasItem(DEFAULT_PERMISSIONS)))
            .andExpect(jsonPath("$.[*].isSystem").value(hasItem(DEFAULT_IS_SYSTEM)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)));
    }

    @Test
    @Transactional
    void getPermissionGroup() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        // Get the permissionGroup
        restPermissionGroupMockMvc
            .perform(get(ENTITY_API_URL_ID, permissionGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(permissionGroup.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.permissions").value(DEFAULT_PERMISSIONS))
            .andExpect(jsonPath("$.isSystem").value(DEFAULT_IS_SYSTEM))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY));
    }

    @Test
    @Transactional
    void getNonExistingPermissionGroup() throws Exception {
        // Get the permissionGroup
        restPermissionGroupMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPermissionGroup() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionGroup
        PermissionGroup updatedPermissionGroup = permissionGroupRepository.findById(permissionGroup.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedPermissionGroup are not directly saved in db
        em.detach(updatedPermissionGroup);
        updatedPermissionGroup
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(updatedPermissionGroup);

        restPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionGroupDTO))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedPermissionGroupToMatchAllProperties(updatedPermissionGroup);
    }

    @Test
    @Transactional
    void putNonExistingPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, permissionGroupDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(permissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePermissionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionGroup using partial update
        PermissionGroup partialUpdatedPermissionGroup = new PermissionGroup();
        partialUpdatedPermissionGroup.setId(permissionGroup.getId());

        partialUpdatedPermissionGroup.permissions(UPDATED_PERMISSIONS).createdBy(UPDATED_CREATED_BY);

        restPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissionGroup))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissionGroupUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedPermissionGroup, permissionGroup),
            getPersistedPermissionGroup(permissionGroup)
        );
    }

    @Test
    @Transactional
    void fullUpdatePermissionGroupWithPatch() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the permissionGroup using partial update
        PermissionGroup partialUpdatedPermissionGroup = new PermissionGroup();
        partialUpdatedPermissionGroup.setId(permissionGroup.getId());

        partialUpdatedPermissionGroup
            .name(UPDATED_NAME)
            .permissions(UPDATED_PERMISSIONS)
            .isSystem(UPDATED_IS_SYSTEM)
            .createdDate(UPDATED_CREATED_DATE)
            .createdBy(UPDATED_CREATED_BY);

        restPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPermissionGroup.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedPermissionGroup))
            )
            .andExpect(status().isOk());

        // Validate the PermissionGroup in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPermissionGroupUpdatableFieldsEquals(
            partialUpdatedPermissionGroup,
            getPersistedPermissionGroup(partialUpdatedPermissionGroup)
        );
    }

    @Test
    @Transactional
    void patchNonExistingPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, permissionGroupDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(permissionGroupDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPermissionGroup() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        permissionGroup.setId(longCount.incrementAndGet());

        // Create the PermissionGroup
        PermissionGroupDTO permissionGroupDTO = permissionGroupMapper.toDto(permissionGroup);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPermissionGroupMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(permissionGroupDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PermissionGroup in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePermissionGroup() throws Exception {
        // Initialize the database
        insertedPermissionGroup = permissionGroupRepository.saveAndFlush(permissionGroup);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the permissionGroup
        restPermissionGroupMockMvc
            .perform(delete(ENTITY_API_URL_ID, permissionGroup.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return permissionGroupRepository.count();
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

    protected PermissionGroup getPersistedPermissionGroup(PermissionGroup permissionGroup) {
        return permissionGroupRepository.findById(permissionGroup.getId()).orElseThrow();
    }

    protected void assertPersistedPermissionGroupToMatchAllProperties(PermissionGroup expectedPermissionGroup) {
        assertPermissionGroupAllPropertiesEquals(expectedPermissionGroup, getPersistedPermissionGroup(expectedPermissionGroup));
    }

    protected void assertPersistedPermissionGroupToMatchUpdatableProperties(PermissionGroup expectedPermissionGroup) {
        assertPermissionGroupAllUpdatablePropertiesEquals(expectedPermissionGroup, getPersistedPermissionGroup(expectedPermissionGroup));
    }
}
