package fr.smartprod.paperdms.emailimport.web.rest;

import static fr.smartprod.paperdms.emailimport.domain.EmailAttachmentAsserts.*;
import static fr.smartprod.paperdms.emailimport.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.emailimport.IntegrationTest;
import fr.smartprod.paperdms.emailimport.domain.EmailAttachment;
import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.domain.enumeration.AttachmentStatus;
import fr.smartprod.paperdms.emailimport.repository.EmailAttachmentRepository;
import fr.smartprod.paperdms.emailimport.service.dto.EmailAttachmentDTO;
import fr.smartprod.paperdms.emailimport.service.mapper.EmailAttachmentMapper;
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
 * Integration tests for the {@link EmailAttachmentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class EmailAttachmentResourceIT {

    private static final Long DEFAULT_EMAIL_IMPORT_ID = 1L;
    private static final Long UPDATED_EMAIL_IMPORT_ID = 2L;

    private static final String DEFAULT_FILE_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FILE_NAME = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final String DEFAULT_MIME_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_MIME_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final AttachmentStatus DEFAULT_STATUS = AttachmentStatus.PENDING;
    private static final AttachmentStatus UPDATED_STATUS = AttachmentStatus.PROCESSING;

    private static final String DEFAULT_ERROR_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_ERROR_MESSAGE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/email-attachments";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private EmailAttachmentRepository emailAttachmentRepository;

    @Autowired
    private EmailAttachmentMapper emailAttachmentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restEmailAttachmentMockMvc;

    private EmailAttachment emailAttachment;

    private EmailAttachment insertedEmailAttachment;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailAttachment createEntity(EntityManager em) {
        EmailAttachment emailAttachment = new EmailAttachment()
            .emailImportId(DEFAULT_EMAIL_IMPORT_ID)
            .fileName(DEFAULT_FILE_NAME)
            .fileSize(DEFAULT_FILE_SIZE)
            .mimeType(DEFAULT_MIME_TYPE)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .documentId(DEFAULT_DOCUMENT_ID)
            .status(DEFAULT_STATUS)
            .errorMessage(DEFAULT_ERROR_MESSAGE);
        // Add required entity
        EmailImport emailImport;
        if (TestUtil.findAll(em, EmailImport.class).isEmpty()) {
            emailImport = EmailImportResourceIT.createEntity();
            em.persist(emailImport);
            em.flush();
        } else {
            emailImport = TestUtil.findAll(em, EmailImport.class).get(0);
        }
        emailAttachment.setEmailImport(emailImport);
        return emailAttachment;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static EmailAttachment createUpdatedEntity(EntityManager em) {
        EmailAttachment updatedEmailAttachment = new EmailAttachment()
            .emailImportId(UPDATED_EMAIL_IMPORT_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        // Add required entity
        EmailImport emailImport;
        if (TestUtil.findAll(em, EmailImport.class).isEmpty()) {
            emailImport = EmailImportResourceIT.createUpdatedEntity();
            em.persist(emailImport);
            em.flush();
        } else {
            emailImport = TestUtil.findAll(em, EmailImport.class).get(0);
        }
        updatedEmailAttachment.setEmailImport(emailImport);
        return updatedEmailAttachment;
    }

    @BeforeEach
    void initTest() {
        emailAttachment = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedEmailAttachment != null) {
            emailAttachmentRepository.delete(insertedEmailAttachment);
            insertedEmailAttachment = null;
        }
    }

    @Test
    @Transactional
    void createEmailAttachment() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);
        var returnedEmailAttachmentDTO = om.readValue(
            restEmailAttachmentMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            EmailAttachmentDTO.class
        );

        // Validate the EmailAttachment in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedEmailAttachment = emailAttachmentMapper.toEntity(returnedEmailAttachmentDTO);
        assertEmailAttachmentUpdatableFieldsEquals(returnedEmailAttachment, getPersistedEmailAttachment(returnedEmailAttachment));

        insertedEmailAttachment = returnedEmailAttachment;
    }

    @Test
    @Transactional
    void createEmailAttachmentWithExistingId() throws Exception {
        // Create the EmailAttachment with an existing ID
        emailAttachment.setId(1L);
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkEmailImportIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setEmailImportId(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setFileName(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFileSizeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setFileSize(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMimeTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setMimeType(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setSha256(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        emailAttachment.setStatus(null);

        // Create the EmailAttachment, which fails.
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        restEmailAttachmentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllEmailAttachments() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        // Get all the emailAttachmentList
        restEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(emailAttachment.getId().intValue())))
            .andExpect(jsonPath("$.[*].emailImportId").value(hasItem(DEFAULT_EMAIL_IMPORT_ID.intValue())))
            .andExpect(jsonPath("$.[*].fileName").value(hasItem(DEFAULT_FILE_NAME)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].mimeType").value(hasItem(DEFAULT_MIME_TYPE)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].errorMessage").value(hasItem(DEFAULT_ERROR_MESSAGE)));
    }

    @Test
    @Transactional
    void getEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        // Get the emailAttachment
        restEmailAttachmentMockMvc
            .perform(get(ENTITY_API_URL_ID, emailAttachment.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(emailAttachment.getId().intValue()))
            .andExpect(jsonPath("$.emailImportId").value(DEFAULT_EMAIL_IMPORT_ID.intValue()))
            .andExpect(jsonPath("$.fileName").value(DEFAULT_FILE_NAME))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.mimeType").value(DEFAULT_MIME_TYPE))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.errorMessage").value(DEFAULT_ERROR_MESSAGE));
    }

    @Test
    @Transactional
    void getNonExistingEmailAttachment() throws Exception {
        // Get the emailAttachment
        restEmailAttachmentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailAttachment
        EmailAttachment updatedEmailAttachment = emailAttachmentRepository.findById(emailAttachment.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedEmailAttachment are not directly saved in db
        em.detach(updatedEmailAttachment);
        updatedEmailAttachment
            .emailImportId(UPDATED_EMAIL_IMPORT_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE);
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(updatedEmailAttachment);

        restEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailAttachmentDTO))
            )
            .andExpect(status().isOk());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedEmailAttachmentToMatchAllProperties(updatedEmailAttachment);
    }

    @Test
    @Transactional
    void putNonExistingEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, emailAttachmentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(emailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateEmailAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailAttachment using partial update
        EmailAttachment partialUpdatedEmailAttachment = new EmailAttachment();
        partialUpdatedEmailAttachment.setId(emailAttachment.getId());

        partialUpdatedEmailAttachment.emailImportId(UPDATED_EMAIL_IMPORT_ID).s3Key(UPDATED_S_3_KEY).documentId(UPDATED_DOCUMENT_ID);

        restEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EmailAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailAttachmentUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedEmailAttachment, emailAttachment),
            getPersistedEmailAttachment(emailAttachment)
        );
    }

    @Test
    @Transactional
    void fullUpdateEmailAttachmentWithPatch() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the emailAttachment using partial update
        EmailAttachment partialUpdatedEmailAttachment = new EmailAttachment();
        partialUpdatedEmailAttachment.setId(emailAttachment.getId());

        partialUpdatedEmailAttachment
            .emailImportId(UPDATED_EMAIL_IMPORT_ID)
            .fileName(UPDATED_FILE_NAME)
            .fileSize(UPDATED_FILE_SIZE)
            .mimeType(UPDATED_MIME_TYPE)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .documentId(UPDATED_DOCUMENT_ID)
            .status(UPDATED_STATUS)
            .errorMessage(UPDATED_ERROR_MESSAGE);

        restEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedEmailAttachment.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedEmailAttachment))
            )
            .andExpect(status().isOk());

        // Validate the EmailAttachment in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertEmailAttachmentUpdatableFieldsEquals(
            partialUpdatedEmailAttachment,
            getPersistedEmailAttachment(partialUpdatedEmailAttachment)
        );
    }

    @Test
    @Transactional
    void patchNonExistingEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, emailAttachmentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(emailAttachmentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamEmailAttachment() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        emailAttachment.setId(longCount.incrementAndGet());

        // Create the EmailAttachment
        EmailAttachmentDTO emailAttachmentDTO = emailAttachmentMapper.toDto(emailAttachment);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restEmailAttachmentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(emailAttachmentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the EmailAttachment in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteEmailAttachment() throws Exception {
        // Initialize the database
        insertedEmailAttachment = emailAttachmentRepository.saveAndFlush(emailAttachment);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the emailAttachment
        restEmailAttachmentMockMvc
            .perform(delete(ENTITY_API_URL_ID, emailAttachment.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return emailAttachmentRepository.count();
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

    protected EmailAttachment getPersistedEmailAttachment(EmailAttachment emailAttachment) {
        return emailAttachmentRepository.findById(emailAttachment.getId()).orElseThrow();
    }

    protected void assertPersistedEmailAttachmentToMatchAllProperties(EmailAttachment expectedEmailAttachment) {
        assertEmailAttachmentAllPropertiesEquals(expectedEmailAttachment, getPersistedEmailAttachment(expectedEmailAttachment));
    }

    protected void assertPersistedEmailAttachmentToMatchUpdatableProperties(EmailAttachment expectedEmailAttachment) {
        assertEmailAttachmentAllUpdatablePropertiesEquals(expectedEmailAttachment, getPersistedEmailAttachment(expectedEmailAttachment));
    }
}
