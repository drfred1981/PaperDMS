package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTemplateAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTemplateRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTemplateMapper;
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

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTemplateRepository documentTemplateRepository;

    @Autowired
    private DocumentTemplateMapper documentTemplateMapper;

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
    public static DocumentTemplate createEntity(EntityManager em) {
        DocumentTemplate documentTemplate = new DocumentTemplate()
            .name(DEFAULT_NAME)
            .templateSha256(DEFAULT_TEMPLATE_SHA_256)
            .templateS3Key(DEFAULT_TEMPLATE_S_3_KEY)
            .isActive(DEFAULT_IS_ACTIVE)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createEntity();
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        documentTemplate.setDocumentType(documentType);
        return documentTemplate;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTemplate createUpdatedEntity(EntityManager em) {
        DocumentTemplate updatedDocumentTemplate = new DocumentTemplate()
            .name(UPDATED_NAME)
            .templateSha256(UPDATED_TEMPLATE_SHA_256)
            .templateS3Key(UPDATED_TEMPLATE_S_3_KEY)
            .isActive(UPDATED_IS_ACTIVE)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE);
        // Add required entity
        DocumentType documentType;
        if (TestUtil.findAll(em, DocumentType.class).isEmpty()) {
            documentType = DocumentTypeResourceIT.createUpdatedEntity();
            em.persist(documentType);
            em.flush();
        } else {
            documentType = TestUtil.findAll(em, DocumentType.class).get(0);
        }
        updatedDocumentTemplate.setDocumentType(documentType);
        return updatedDocumentTemplate;
    }

    @BeforeEach
    void initTest() {
        documentTemplate = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentTemplate != null) {
            documentTemplateRepository.delete(insertedDocumentTemplate);
            insertedDocumentTemplate = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedDocumentTemplate = returnedDocumentTemplate;
    }

    @Test
    @Transactional
    void createDocumentTemplateWithExistingId() throws Exception {
        // Create the DocumentTemplate with an existing ID
        documentTemplate.setId(1L);
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setName(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemplateSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setTemplateSha256(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTemplateS3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setTemplateS3Key(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setIsActive(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setCreatedBy(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTemplate.setCreatedDate(null);

        // Create the DocumentTemplate, which fails.
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        restDocumentTemplateMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
    }

    @Test
    @Transactional
    void putNonExistingDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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

        partialUpdatedDocumentTemplate.createdBy(UPDATED_CREATED_BY).createdDate(UPDATED_CREATED_DATE);

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
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTemplate.setId(longCount.incrementAndGet());

        // Create the DocumentTemplate
        DocumentTemplateDTO documentTemplateDTO = documentTemplateMapper.toDto(documentTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTemplateMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentTemplate() throws Exception {
        // Initialize the database
        insertedDocumentTemplate = documentTemplateRepository.saveAndFlush(documentTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentTemplate
        restDocumentTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTemplate.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
