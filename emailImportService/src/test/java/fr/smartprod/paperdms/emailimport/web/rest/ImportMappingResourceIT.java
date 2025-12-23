package fr.smartprod.paperdms.emailimport.web.rest;

import static fr.smartprod.paperdms.emailimport.domain.ImportMappingAsserts.*;
import static fr.smartprod.paperdms.emailimport.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimport.IntegrationTest;
import fr.smartprod.paperdms.emailimport.domain.ImportMapping;
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.domain.enumeration.EmailField;
import fr.smartprod.paperdms.emailimport.domain.enumeration.MappingTransformation;
import fr.smartprod.paperdms.emailimport.repository.ImportMappingRepository;
import fr.smartprod.paperdms.emailimport.service.dto.ImportMappingDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.ImportMappingMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link ImportMappingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ImportMappingResourceIT {

    private static final Long DEFAULT_RULE_ID = 1L;
    private static final Long UPDATED_RULE_ID = 2L;

    private static final EmailField DEFAULT_EMAIL_FIELD = EmailField.FROM;
    private static final EmailField UPDATED_EMAIL_FIELD = EmailField.TO;

    private static final String DEFAULT_DOCUMENT_FIELD = "AAAAAAAAAA";
    private static final String UPDATED_DOCUMENT_FIELD = "BBBBBBBBBB";

    private static final MappingTransformation DEFAULT_TRANSFORMATION = MappingTransformation.NONE;
    private static final MappingTransformation UPDATED_TRANSFORMATION = MappingTransformation.UPPERCASE;

    private static final String DEFAULT_TRANSFORMATION_CONFIG = "AAAAAAAAAA";
    private static final String UPDATED_TRANSFORMATION_CONFIG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_REQUIRED = false;
    private static final Boolean UPDATED_IS_REQUIRED = true;

    private static final String DEFAULT_DEFAULT_VALUE = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_VALUE = "BBBBBBBBBB";

    private static final String DEFAULT_VALIDATION_REGEX = "AAAAAAAAAA";
    private static final String UPDATED_VALIDATION_REGEX = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/import-mappings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ImportMappingRepository importMappingRepository;

    @Autowired
    private ImportMappingMapper importMappingMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restImportMappingMockMvc;

    private ImportMapping importMapping;

    private ImportMapping insertedImportMapping;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImportMapping createEntity(EntityManager em) {
        ImportMapping importMapping = new ImportMapping()
            .ruleId(DEFAULT_RULE_ID)
            .emailField(DEFAULT_EMAIL_FIELD)
            .documentField(DEFAULT_DOCUMENT_FIELD)
            .transformation(DEFAULT_TRANSFORMATION)
            .transformationConfig(DEFAULT_TRANSFORMATION_CONFIG)
            .isRequired(DEFAULT_IS_REQUIRED)
            .defaultValue(DEFAULT_DEFAULT_VALUE)
            .validationRegex(DEFAULT_VALIDATION_REGEX);
        // Add required entity
        ImportRule importRule;
        if (TestUtil.findAll(em, ImportRule.class).isEmpty()) {
            importRule = ImportRuleResourceIT.createEntity();
            em.persist(importRule);
            em.flush();
        } else {
            importRule = TestUtil.findAll(em, ImportRule.class).get(0);
        }
        importMapping.setRule(importRule);
        return importMapping;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ImportMapping createUpdatedEntity(EntityManager em) {
        ImportMapping updatedImportMapping = new ImportMapping()
            .ruleId(UPDATED_RULE_ID)
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);
        // Add required entity
        ImportRule importRule;
        if (TestUtil.findAll(em, ImportRule.class).isEmpty()) {
            importRule = ImportRuleResourceIT.createUpdatedEntity();
            em.persist(importRule);
            em.flush();
        } else {
            importRule = TestUtil.findAll(em, ImportRule.class).get(0);
        }
        updatedImportMapping.setRule(importRule);
        return updatedImportMapping;
    }

    @BeforeEach
    void initTest() {
        importMapping = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedImportMapping != null) {
            importMappingRepository.delete(insertedImportMapping);
            insertedImportMapping = null;
        }
    }

    @Test
    @Transactional
    void createImportMapping() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);
        var returnedImportMappingDTO = om.readValue(
            restImportMappingMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ImportMappingDTO.class
        );

        // Validate the ImportMapping in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedImportMapping = importMappingMapper.toEntity(returnedImportMappingDTO);
        assertImportMappingUpdatableFieldsEquals(returnedImportMapping, getPersistedImportMapping(returnedImportMapping));

        insertedImportMapping = returnedImportMapping;
    }

    @Test
    @Transactional
    void createImportMappingWithExistingId() throws Exception {
        // Create the ImportMapping with an existing ID
        importMapping.setId(1L);
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restImportMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkRuleIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importMapping.setRuleId(null);

        // Create the ImportMapping, which fails.
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        restImportMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEmailFieldIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importMapping.setEmailField(null);

        // Create the ImportMapping, which fails.
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        restImportMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDocumentFieldIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importMapping.setDocumentField(null);

        // Create the ImportMapping, which fails.
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        restImportMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsRequiredIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        importMapping.setIsRequired(null);

        // Create the ImportMapping, which fails.
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        restImportMappingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllImportMappings() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        // Get all the importMappingList
        restImportMappingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(importMapping.getId().intValue())))
            .andExpect(jsonPath("$.[*].ruleId").value(hasItem(DEFAULT_RULE_ID.intValue())))
            .andExpect(jsonPath("$.[*].emailField").value(hasItem(DEFAULT_EMAIL_FIELD.toString())))
            .andExpect(jsonPath("$.[*].documentField").value(hasItem(DEFAULT_DOCUMENT_FIELD)))
            .andExpect(jsonPath("$.[*].transformation").value(hasItem(DEFAULT_TRANSFORMATION.toString())))
            .andExpect(jsonPath("$.[*].transformationConfig").value(hasItem(DEFAULT_TRANSFORMATION_CONFIG)))
            .andExpect(jsonPath("$.[*].isRequired").value(hasItem(DEFAULT_IS_REQUIRED)))
            .andExpect(jsonPath("$.[*].defaultValue").value(hasItem(DEFAULT_DEFAULT_VALUE)))
            .andExpect(jsonPath("$.[*].validationRegex").value(hasItem(DEFAULT_VALIDATION_REGEX)));
    }

    @Test
    @Transactional
    void getImportMapping() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        // Get the importMapping
        restImportMappingMockMvc
            .perform(get(ENTITY_API_URL_ID, importMapping.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(importMapping.getId().intValue()))
            .andExpect(jsonPath("$.ruleId").value(DEFAULT_RULE_ID.intValue()))
            .andExpect(jsonPath("$.emailField").value(DEFAULT_EMAIL_FIELD.toString()))
            .andExpect(jsonPath("$.documentField").value(DEFAULT_DOCUMENT_FIELD))
            .andExpect(jsonPath("$.transformation").value(DEFAULT_TRANSFORMATION.toString()))
            .andExpect(jsonPath("$.transformationConfig").value(DEFAULT_TRANSFORMATION_CONFIG))
            .andExpect(jsonPath("$.isRequired").value(DEFAULT_IS_REQUIRED))
            .andExpect(jsonPath("$.defaultValue").value(DEFAULT_DEFAULT_VALUE))
            .andExpect(jsonPath("$.validationRegex").value(DEFAULT_VALIDATION_REGEX));
    }

    @Test
    @Transactional
    void getNonExistingImportMapping() throws Exception {
        // Get the importMapping
        restImportMappingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingImportMapping() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importMapping
        ImportMapping updatedImportMapping = importMappingRepository.findById(importMapping.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedImportMapping are not directly saved in db
        em.detach(updatedImportMapping);
        updatedImportMapping
            .ruleId(UPDATED_RULE_ID)
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(updatedImportMapping);

        restImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, importMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importMappingDTO))
            )
            .andExpect(status().isOk());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedImportMappingToMatchAllProperties(updatedImportMapping);
    }

    @Test
    @Transactional
    void putNonExistingImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, importMappingDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(importMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateImportMappingWithPatch() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importMapping using partial update
        ImportMapping partialUpdatedImportMapping = new ImportMapping();
        partialUpdatedImportMapping.setId(importMapping.getId());

        partialUpdatedImportMapping
            .ruleId(UPDATED_RULE_ID)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .validationRegex(UPDATED_VALIDATION_REGEX);

        restImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImportMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImportMapping))
            )
            .andExpect(status().isOk());

        // Validate the ImportMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImportMappingUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedImportMapping, importMapping),
            getPersistedImportMapping(importMapping)
        );
    }

    @Test
    @Transactional
    void fullUpdateImportMappingWithPatch() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the importMapping using partial update
        ImportMapping partialUpdatedImportMapping = new ImportMapping();
        partialUpdatedImportMapping.setId(importMapping.getId());

        partialUpdatedImportMapping
            .ruleId(UPDATED_RULE_ID)
            .emailField(UPDATED_EMAIL_FIELD)
            .documentField(UPDATED_DOCUMENT_FIELD)
            .transformation(UPDATED_TRANSFORMATION)
            .transformationConfig(UPDATED_TRANSFORMATION_CONFIG)
            .isRequired(UPDATED_IS_REQUIRED)
            .defaultValue(UPDATED_DEFAULT_VALUE)
            .validationRegex(UPDATED_VALIDATION_REGEX);

        restImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedImportMapping.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedImportMapping))
            )
            .andExpect(status().isOk());

        // Validate the ImportMapping in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertImportMappingUpdatableFieldsEquals(partialUpdatedImportMapping, getPersistedImportMapping(partialUpdatedImportMapping));
    }

    @Test
    @Transactional
    void patchNonExistingImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, importMappingDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(importMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(importMappingDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamImportMapping() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        importMapping.setId(longCount.incrementAndGet());

        // Create the ImportMapping
        ImportMappingDTO importMappingDTO = importMappingMapper.toDto(importMapping);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restImportMappingMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(importMappingDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ImportMapping in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteImportMapping() throws Exception {
        // Initialize the database
        insertedImportMapping = importMappingRepository.saveAndFlush(importMapping);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the importMapping
        restImportMappingMockMvc
            .perform(delete(ENTITY_API_URL_ID, importMapping.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return importMappingRepository.count();
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

    protected ImportMapping getPersistedImportMapping(ImportMapping importMapping) {
        return importMappingRepository.findById(importMapping.getId()).orElseThrow();
    }

    protected void assertPersistedImportMappingToMatchAllProperties(ImportMapping expectedImportMapping) {
        assertImportMappingAllPropertiesEquals(expectedImportMapping, getPersistedImportMapping(expectedImportMapping));
    }

    protected void assertPersistedImportMappingToMatchUpdatableProperties(ImportMapping expectedImportMapping) {
        assertImportMappingAllUpdatablePropertiesEquals(expectedImportMapping, getPersistedImportMapping(expectedImportMapping));
    }
}
