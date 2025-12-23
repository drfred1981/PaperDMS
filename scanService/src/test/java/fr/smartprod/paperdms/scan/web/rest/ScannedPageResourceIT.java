package fr.smartprod.paperdms.scan.web.rest;

import static fr.smartprod.paperdms.scan.domain.ScannedPageAsserts.*;
import static fr.smartprod.paperdms.scan.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.scan.IntegrationTest;
import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.domain.ScannedPage;
import fr.smartprod.paperdms.scan.repository.ScannedPageRepository;
import fr.smartprod.paperdms.scan.service.dto.ScannedPageDTO;
import fr.smartprod.paperdms.scan.service.mapper.ScannedPageMapper;
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
 * Integration tests for the {@link ScannedPageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ScannedPageResourceIT {

    private static final Long DEFAULT_SCAN_JOB_ID = 1L;
    private static final Long UPDATED_SCAN_JOB_ID = 2L;

    private static final Integer DEFAULT_PAGE_NUMBER = 1;
    private static final Integer UPDATED_PAGE_NUMBER = 2;

    private static final String DEFAULT_SHA_256 = "AAAAAAAAAA";
    private static final String UPDATED_SHA_256 = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_S_3_PREVIEW_KEY = "AAAAAAAAAA";
    private static final String UPDATED_S_3_PREVIEW_KEY = "BBBBBBBBBB";

    private static final Long DEFAULT_FILE_SIZE = 1L;
    private static final Long UPDATED_FILE_SIZE = 2L;

    private static final Integer DEFAULT_WIDTH = 1;
    private static final Integer UPDATED_WIDTH = 2;

    private static final Integer DEFAULT_HEIGHT = 1;
    private static final Integer UPDATED_HEIGHT = 2;

    private static final Integer DEFAULT_DPI = 1;
    private static final Integer UPDATED_DPI = 2;

    private static final Long DEFAULT_DOCUMENT_ID = 1L;
    private static final Long UPDATED_DOCUMENT_ID = 2L;

    private static final Instant DEFAULT_SCANNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_SCANNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/scanned-pages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ScannedPageRepository scannedPageRepository;

    @Autowired
    private ScannedPageMapper scannedPageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restScannedPageMockMvc;

    private ScannedPage scannedPage;

    private ScannedPage insertedScannedPage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScannedPage createEntity(EntityManager em) {
        ScannedPage scannedPage = new ScannedPage()
            .scanJobId(DEFAULT_SCAN_JOB_ID)
            .pageNumber(DEFAULT_PAGE_NUMBER)
            .sha256(DEFAULT_SHA_256)
            .s3Key(DEFAULT_S_3_KEY)
            .s3PreviewKey(DEFAULT_S_3_PREVIEW_KEY)
            .fileSize(DEFAULT_FILE_SIZE)
            .width(DEFAULT_WIDTH)
            .height(DEFAULT_HEIGHT)
            .dpi(DEFAULT_DPI)
            .documentId(DEFAULT_DOCUMENT_ID)
            .scannedDate(DEFAULT_SCANNED_DATE);
        // Add required entity
        ScanJob scanJob;
        if (TestUtil.findAll(em, ScanJob.class).isEmpty()) {
            scanJob = ScanJobResourceIT.createEntity(em);
            em.persist(scanJob);
            em.flush();
        } else {
            scanJob = TestUtil.findAll(em, ScanJob.class).get(0);
        }
        scannedPage.setScanJob(scanJob);
        return scannedPage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ScannedPage createUpdatedEntity(EntityManager em) {
        ScannedPage updatedScannedPage = new ScannedPage()
            .scanJobId(UPDATED_SCAN_JOB_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3PreviewKey(UPDATED_S_3_PREVIEW_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .documentId(UPDATED_DOCUMENT_ID)
            .scannedDate(UPDATED_SCANNED_DATE);
        // Add required entity
        ScanJob scanJob;
        if (TestUtil.findAll(em, ScanJob.class).isEmpty()) {
            scanJob = ScanJobResourceIT.createUpdatedEntity(em);
            em.persist(scanJob);
            em.flush();
        } else {
            scanJob = TestUtil.findAll(em, ScanJob.class).get(0);
        }
        updatedScannedPage.setScanJob(scanJob);
        return updatedScannedPage;
    }

    @BeforeEach
    void initTest() {
        scannedPage = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedScannedPage != null) {
            scannedPageRepository.delete(insertedScannedPage);
            insertedScannedPage = null;
        }
    }

    @Test
    @Transactional
    void createScannedPage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);
        var returnedScannedPageDTO = om.readValue(
            restScannedPageMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ScannedPageDTO.class
        );

        // Validate the ScannedPage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedScannedPage = scannedPageMapper.toEntity(returnedScannedPageDTO);
        assertScannedPageUpdatableFieldsEquals(returnedScannedPage, getPersistedScannedPage(returnedScannedPage));

        insertedScannedPage = returnedScannedPage;
    }

    @Test
    @Transactional
    void createScannedPageWithExistingId() throws Exception {
        // Create the ScannedPage with an existing ID
        scannedPage.setId(1L);
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkScanJobIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannedPage.setScanJobId(null);

        // Create the ScannedPage, which fails.
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPageNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannedPage.setPageNumber(null);

        // Create the ScannedPage, which fails.
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSha256IsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannedPage.setSha256(null);

        // Create the ScannedPage, which fails.
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checks3KeyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannedPage.sets3Key(null);

        // Create the ScannedPage, which fails.
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkScannedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        scannedPage.setScannedDate(null);

        // Create the ScannedPage, which fails.
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        restScannedPageMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllScannedPages() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        // Get all the scannedPageList
        restScannedPageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(scannedPage.getId().intValue())))
            .andExpect(jsonPath("$.[*].scanJobId").value(hasItem(DEFAULT_SCAN_JOB_ID.intValue())))
            .andExpect(jsonPath("$.[*].pageNumber").value(hasItem(DEFAULT_PAGE_NUMBER)))
            .andExpect(jsonPath("$.[*].sha256").value(hasItem(DEFAULT_SHA_256)))
            .andExpect(jsonPath("$.[*].s3Key").value(hasItem(DEFAULT_S_3_KEY)))
            .andExpect(jsonPath("$.[*].s3PreviewKey").value(hasItem(DEFAULT_S_3_PREVIEW_KEY)))
            .andExpect(jsonPath("$.[*].fileSize").value(hasItem(DEFAULT_FILE_SIZE.intValue())))
            .andExpect(jsonPath("$.[*].width").value(hasItem(DEFAULT_WIDTH)))
            .andExpect(jsonPath("$.[*].height").value(hasItem(DEFAULT_HEIGHT)))
            .andExpect(jsonPath("$.[*].dpi").value(hasItem(DEFAULT_DPI)))
            .andExpect(jsonPath("$.[*].documentId").value(hasItem(DEFAULT_DOCUMENT_ID.intValue())))
            .andExpect(jsonPath("$.[*].scannedDate").value(hasItem(DEFAULT_SCANNED_DATE.toString())));
    }

    @Test
    @Transactional
    void getScannedPage() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        // Get the scannedPage
        restScannedPageMockMvc
            .perform(get(ENTITY_API_URL_ID, scannedPage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(scannedPage.getId().intValue()))
            .andExpect(jsonPath("$.scanJobId").value(DEFAULT_SCAN_JOB_ID.intValue()))
            .andExpect(jsonPath("$.pageNumber").value(DEFAULT_PAGE_NUMBER))
            .andExpect(jsonPath("$.sha256").value(DEFAULT_SHA_256))
            .andExpect(jsonPath("$.s3Key").value(DEFAULT_S_3_KEY))
            .andExpect(jsonPath("$.s3PreviewKey").value(DEFAULT_S_3_PREVIEW_KEY))
            .andExpect(jsonPath("$.fileSize").value(DEFAULT_FILE_SIZE.intValue()))
            .andExpect(jsonPath("$.width").value(DEFAULT_WIDTH))
            .andExpect(jsonPath("$.height").value(DEFAULT_HEIGHT))
            .andExpect(jsonPath("$.dpi").value(DEFAULT_DPI))
            .andExpect(jsonPath("$.documentId").value(DEFAULT_DOCUMENT_ID.intValue()))
            .andExpect(jsonPath("$.scannedDate").value(DEFAULT_SCANNED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingScannedPage() throws Exception {
        // Get the scannedPage
        restScannedPageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingScannedPage() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannedPage
        ScannedPage updatedScannedPage = scannedPageRepository.findById(scannedPage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedScannedPage are not directly saved in db
        em.detach(updatedScannedPage);
        updatedScannedPage
            .scanJobId(UPDATED_SCAN_JOB_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3PreviewKey(UPDATED_S_3_PREVIEW_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .documentId(UPDATED_DOCUMENT_ID)
            .scannedDate(UPDATED_SCANNED_DATE);
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(updatedScannedPage);

        restScannedPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scannedPageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannedPageDTO))
            )
            .andExpect(status().isOk());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedScannedPageToMatchAllProperties(updatedScannedPage);
    }

    @Test
    @Transactional
    void putNonExistingScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, scannedPageDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannedPageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(scannedPageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateScannedPageWithPatch() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannedPage using partial update
        ScannedPage partialUpdatedScannedPage = new ScannedPage();
        partialUpdatedScannedPage.setId(scannedPage.getId());

        partialUpdatedScannedPage
            .scanJobId(UPDATED_SCAN_JOB_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .s3Key(UPDATED_S_3_KEY)
            .s3PreviewKey(UPDATED_S_3_PREVIEW_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI);

        restScannedPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScannedPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScannedPage))
            )
            .andExpect(status().isOk());

        // Validate the ScannedPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScannedPageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedScannedPage, scannedPage),
            getPersistedScannedPage(scannedPage)
        );
    }

    @Test
    @Transactional
    void fullUpdateScannedPageWithPatch() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the scannedPage using partial update
        ScannedPage partialUpdatedScannedPage = new ScannedPage();
        partialUpdatedScannedPage.setId(scannedPage.getId());

        partialUpdatedScannedPage
            .scanJobId(UPDATED_SCAN_JOB_ID)
            .pageNumber(UPDATED_PAGE_NUMBER)
            .sha256(UPDATED_SHA_256)
            .s3Key(UPDATED_S_3_KEY)
            .s3PreviewKey(UPDATED_S_3_PREVIEW_KEY)
            .fileSize(UPDATED_FILE_SIZE)
            .width(UPDATED_WIDTH)
            .height(UPDATED_HEIGHT)
            .dpi(UPDATED_DPI)
            .documentId(UPDATED_DOCUMENT_ID)
            .scannedDate(UPDATED_SCANNED_DATE);

        restScannedPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedScannedPage.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedScannedPage))
            )
            .andExpect(status().isOk());

        // Validate the ScannedPage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertScannedPageUpdatableFieldsEquals(partialUpdatedScannedPage, getPersistedScannedPage(partialUpdatedScannedPage));
    }

    @Test
    @Transactional
    void patchNonExistingScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, scannedPageDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scannedPageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(scannedPageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamScannedPage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        scannedPage.setId(longCount.incrementAndGet());

        // Create the ScannedPage
        ScannedPageDTO scannedPageDTO = scannedPageMapper.toDto(scannedPage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restScannedPageMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(scannedPageDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ScannedPage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteScannedPage() throws Exception {
        // Initialize the database
        insertedScannedPage = scannedPageRepository.saveAndFlush(scannedPage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the scannedPage
        restScannedPageMockMvc
            .perform(delete(ENTITY_API_URL_ID, scannedPage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return scannedPageRepository.count();
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

    protected ScannedPage getPersistedScannedPage(ScannedPage scannedPage) {
        return scannedPageRepository.findById(scannedPage.getId()).orElseThrow();
    }

    protected void assertPersistedScannedPageToMatchAllProperties(ScannedPage expectedScannedPage) {
        assertScannedPageAllPropertiesEquals(expectedScannedPage, getPersistedScannedPage(expectedScannedPage));
    }

    protected void assertPersistedScannedPageToMatchUpdatableProperties(ScannedPage expectedScannedPage) {
        assertScannedPageAllUpdatablePropertiesEquals(expectedScannedPage, getPersistedScannedPage(expectedScannedPage));
    }
}
