package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.DocumentTagAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.domain.Tag;
import fr.smartprod.paperdms.document.domain.enumeration.TagSource;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.mapper.DocumentTagMapper;
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

    private static final Boolean DEFAULT_IS_AUTO_TAGGED = false;
    private static final Boolean UPDATED_IS_AUTO_TAGGED = true;

    private static final TagSource DEFAULT_SOURCE = TagSource.MANUAL;
    private static final TagSource UPDATED_SOURCE = TagSource.AI_SUGGESTED;

    private static final String ENTITY_API_URL = "/api/document-tags";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DocumentTagRepository documentTagRepository;

    @Autowired
    private DocumentTagMapper documentTagMapper;

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
    public static DocumentTag createEntity(EntityManager em) {
        DocumentTag documentTag = new DocumentTag()
            .assignedDate(DEFAULT_ASSIGNED_DATE)
            .assignedBy(DEFAULT_ASSIGNED_BY)
            .confidence(DEFAULT_CONFIDENCE)
            .isAutoTagged(DEFAULT_IS_AUTO_TAGGED)
            .source(DEFAULT_SOURCE);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        documentTag.setDocument(document);
        // Add required entity
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tag = TagResourceIT.createEntity();
            em.persist(tag);
            em.flush();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        documentTag.setTag(tag);
        return documentTag;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DocumentTag createUpdatedEntity(EntityManager em) {
        DocumentTag updatedDocumentTag = new DocumentTag()
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoTagged(UPDATED_IS_AUTO_TAGGED)
            .source(UPDATED_SOURCE);
        // Add required entity
        Document document;
        if (TestUtil.findAll(em, Document.class).isEmpty()) {
            document = DocumentResourceIT.createUpdatedEntity(em);
            em.persist(document);
            em.flush();
        } else {
            document = TestUtil.findAll(em, Document.class).get(0);
        }
        updatedDocumentTag.setDocument(document);
        // Add required entity
        Tag tag;
        if (TestUtil.findAll(em, Tag.class).isEmpty()) {
            tag = TagResourceIT.createUpdatedEntity();
            em.persist(tag);
            em.flush();
        } else {
            tag = TestUtil.findAll(em, Tag.class).get(0);
        }
        updatedDocumentTag.setTag(tag);
        return updatedDocumentTag;
    }

    @BeforeEach
    void initTest() {
        documentTag = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedDocumentTag != null) {
            documentTagRepository.delete(insertedDocumentTag);
            insertedDocumentTag = null;
        }
    }

    @Test
    @Transactional
    void createDocumentTag() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
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

        insertedDocumentTag = returnedDocumentTag;
    }

    @Test
    @Transactional
    void createDocumentTagWithExistingId() throws Exception {
        // Create the DocumentTag with an existing ID
        documentTag.setId(1L);
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkAssignedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTag.setAssignedDate(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssignedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTag.setAssignedBy(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsAutoTaggedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        documentTag.setIsAutoTagged(null);

        // Create the DocumentTag, which fails.
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        restDocumentTagMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
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
            .andExpect(jsonPath("$.[*].isAutoTagged").value(hasItem(DEFAULT_IS_AUTO_TAGGED)))
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
            .andExpect(jsonPath("$.isAutoTagged").value(DEFAULT_IS_AUTO_TAGGED))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE.toString()));
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

        // Update the documentTag
        DocumentTag updatedDocumentTag = documentTagRepository.findById(documentTag.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDocumentTag are not directly saved in db
        em.detach(updatedDocumentTag);
        updatedDocumentTag
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .assignedBy(UPDATED_ASSIGNED_BY)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoTagged(UPDATED_IS_AUTO_TAGGED)
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
    }

    @Test
    @Transactional
    void putNonExistingDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithIdMismatchDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
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

        partialUpdatedDocumentTag
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .confidence(UPDATED_CONFIDENCE)
            .isAutoTagged(UPDATED_IS_AUTO_TAGGED)
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
            .isAutoTagged(UPDATED_IS_AUTO_TAGGED)
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
    }

    @Test
    @Transactional
    void patchWithIdMismatchDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
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
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDocumentTag() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        documentTag.setId(longCount.incrementAndGet());

        // Create the DocumentTag
        DocumentTagDTO documentTagDTO = documentTagMapper.toDto(documentTag);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDocumentTagMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(documentTagDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DocumentTag in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDocumentTag() throws Exception {
        // Initialize the database
        insertedDocumentTag = documentTagRepository.saveAndFlush(documentTag);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the documentTag
        restDocumentTagMockMvc
            .perform(delete(ENTITY_API_URL_ID, documentTag.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
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
