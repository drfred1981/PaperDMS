package fr.smartprod.paperdms.business.web.rest;

import static fr.smartprod.paperdms.business.domain.ManualChapterAsserts.*;
import static fr.smartprod.paperdms.business.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.business.IntegrationTest;
import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.domain.ManualChapter;
import fr.smartprod.paperdms.business.repository.ManualChapterRepository;
import fr.smartprod.paperdms.business.service.dto.ManualChapterDTO;
import fr.smartprod.paperdms.business.service.mapper.ManualChapterMapper;
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
 * Integration tests for the {@link ManualChapterResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ManualChapterResourceIT {

    private static final Long DEFAULT_MANUAL_ID = 1L;
    private static final Long UPDATED_MANUAL_ID = 2L;

    private static final String DEFAULT_CHAPTER_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_CHAPTER_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Integer DEFAULT_PAGE_START = 1;
    private static final Integer UPDATED_PAGE_START = 2;

    private static final Integer DEFAULT_PAGE_END = 1;
    private static final Integer UPDATED_PAGE_END = 2;

    private static final Integer DEFAULT_LEVEL = 1;
    private static final Integer UPDATED_LEVEL = 2;

    private static final Integer DEFAULT_DISPLAY_ORDER = 1;
    private static final Integer UPDATED_DISPLAY_ORDER = 2;

    private static final String ENTITY_API_URL = "/api/manual-chapters";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2L * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ManualChapterRepository manualChapterRepository;

    @Autowired
    private ManualChapterMapper manualChapterMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restManualChapterMockMvc;

    private ManualChapter manualChapter;

    private ManualChapter insertedManualChapter;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualChapter createEntity(EntityManager em) {
        ManualChapter manualChapter = new ManualChapter()
            .manualId(DEFAULT_MANUAL_ID)
            .chapterNumber(DEFAULT_CHAPTER_NUMBER)
            .title(DEFAULT_TITLE)
            .content(DEFAULT_CONTENT)
            .pageStart(DEFAULT_PAGE_START)
            .pageEnd(DEFAULT_PAGE_END)
            .level(DEFAULT_LEVEL)
            .displayOrder(DEFAULT_DISPLAY_ORDER);
        // Add required entity
        Manual manual;
        if (TestUtil.findAll(em, Manual.class).isEmpty()) {
            manual = ManualResourceIT.createEntity();
            em.persist(manual);
            em.flush();
        } else {
            manual = TestUtil.findAll(em, Manual.class).get(0);
        }
        manualChapter.setManual(manual);
        return manualChapter;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ManualChapter createUpdatedEntity(EntityManager em) {
        ManualChapter updatedManualChapter = new ManualChapter()
            .manualId(UPDATED_MANUAL_ID)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .pageStart(UPDATED_PAGE_START)
            .pageEnd(UPDATED_PAGE_END)
            .level(UPDATED_LEVEL)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        // Add required entity
        Manual manual;
        if (TestUtil.findAll(em, Manual.class).isEmpty()) {
            manual = ManualResourceIT.createUpdatedEntity();
            em.persist(manual);
            em.flush();
        } else {
            manual = TestUtil.findAll(em, Manual.class).get(0);
        }
        updatedManualChapter.setManual(manual);
        return updatedManualChapter;
    }

    @BeforeEach
    void initTest() {
        manualChapter = createEntity(em);
    }

    @AfterEach
    void cleanup() {
        if (insertedManualChapter != null) {
            manualChapterRepository.delete(insertedManualChapter);
            insertedManualChapter = null;
        }
    }

    @Test
    @Transactional
    void createManualChapter() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);
        var returnedManualChapterDTO = om.readValue(
            restManualChapterMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ManualChapterDTO.class
        );

        // Validate the ManualChapter in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedManualChapter = manualChapterMapper.toEntity(returnedManualChapterDTO);
        assertManualChapterUpdatableFieldsEquals(returnedManualChapter, getPersistedManualChapter(returnedManualChapter));

        insertedManualChapter = returnedManualChapter;
    }

    @Test
    @Transactional
    void createManualChapterWithExistingId() throws Exception {
        // Create the ManualChapter with an existing ID
        manualChapter.setId(1L);
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restManualChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkManualIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualChapter.setManualId(null);

        // Create the ManualChapter, which fails.
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        restManualChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkChapterNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualChapter.setChapterNumber(null);

        // Create the ManualChapter, which fails.
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        restManualChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualChapter.setTitle(null);

        // Create the ManualChapter, which fails.
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        restManualChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        manualChapter.setLevel(null);

        // Create the ManualChapter, which fails.
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        restManualChapterMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllManualChapters() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        // Get all the manualChapterList
        restManualChapterMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(manualChapter.getId().intValue())))
            .andExpect(jsonPath("$.[*].manualId").value(hasItem(DEFAULT_MANUAL_ID.intValue())))
            .andExpect(jsonPath("$.[*].chapterNumber").value(hasItem(DEFAULT_CHAPTER_NUMBER)))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)))
            .andExpect(jsonPath("$.[*].pageStart").value(hasItem(DEFAULT_PAGE_START)))
            .andExpect(jsonPath("$.[*].pageEnd").value(hasItem(DEFAULT_PAGE_END)))
            .andExpect(jsonPath("$.[*].level").value(hasItem(DEFAULT_LEVEL)))
            .andExpect(jsonPath("$.[*].displayOrder").value(hasItem(DEFAULT_DISPLAY_ORDER)));
    }

    @Test
    @Transactional
    void getManualChapter() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        // Get the manualChapter
        restManualChapterMockMvc
            .perform(get(ENTITY_API_URL_ID, manualChapter.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(manualChapter.getId().intValue()))
            .andExpect(jsonPath("$.manualId").value(DEFAULT_MANUAL_ID.intValue()))
            .andExpect(jsonPath("$.chapterNumber").value(DEFAULT_CHAPTER_NUMBER))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT))
            .andExpect(jsonPath("$.pageStart").value(DEFAULT_PAGE_START))
            .andExpect(jsonPath("$.pageEnd").value(DEFAULT_PAGE_END))
            .andExpect(jsonPath("$.level").value(DEFAULT_LEVEL))
            .andExpect(jsonPath("$.displayOrder").value(DEFAULT_DISPLAY_ORDER));
    }

    @Test
    @Transactional
    void getNonExistingManualChapter() throws Exception {
        // Get the manualChapter
        restManualChapterMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingManualChapter() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualChapter
        ManualChapter updatedManualChapter = manualChapterRepository.findById(manualChapter.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedManualChapter are not directly saved in db
        em.detach(updatedManualChapter);
        updatedManualChapter
            .manualId(UPDATED_MANUAL_ID)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .pageStart(UPDATED_PAGE_START)
            .pageEnd(UPDATED_PAGE_END)
            .level(UPDATED_LEVEL)
            .displayOrder(UPDATED_DISPLAY_ORDER);
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(updatedManualChapter);

        restManualChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualChapterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualChapterDTO))
            )
            .andExpect(status().isOk());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedManualChapterToMatchAllProperties(updatedManualChapter);
    }

    @Test
    @Transactional
    void putNonExistingManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, manualChapterDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(manualChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateManualChapterWithPatch() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualChapter using partial update
        ManualChapter partialUpdatedManualChapter = new ManualChapter();
        partialUpdatedManualChapter.setId(manualChapter.getId());

        partialUpdatedManualChapter
            .manualId(UPDATED_MANUAL_ID)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .pageEnd(UPDATED_PAGE_END)
            .level(UPDATED_LEVEL)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restManualChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManualChapter))
            )
            .andExpect(status().isOk());

        // Validate the ManualChapter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualChapterUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedManualChapter, manualChapter),
            getPersistedManualChapter(manualChapter)
        );
    }

    @Test
    @Transactional
    void fullUpdateManualChapterWithPatch() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the manualChapter using partial update
        ManualChapter partialUpdatedManualChapter = new ManualChapter();
        partialUpdatedManualChapter.setId(manualChapter.getId());

        partialUpdatedManualChapter
            .manualId(UPDATED_MANUAL_ID)
            .chapterNumber(UPDATED_CHAPTER_NUMBER)
            .title(UPDATED_TITLE)
            .content(UPDATED_CONTENT)
            .pageStart(UPDATED_PAGE_START)
            .pageEnd(UPDATED_PAGE_END)
            .level(UPDATED_LEVEL)
            .displayOrder(UPDATED_DISPLAY_ORDER);

        restManualChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedManualChapter.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedManualChapter))
            )
            .andExpect(status().isOk());

        // Validate the ManualChapter in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertManualChapterUpdatableFieldsEquals(partialUpdatedManualChapter, getPersistedManualChapter(partialUpdatedManualChapter));
    }

    @Test
    @Transactional
    void patchNonExistingManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, manualChapterDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(manualChapterDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamManualChapter() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        manualChapter.setId(longCount.incrementAndGet());

        // Create the ManualChapter
        ManualChapterDTO manualChapterDTO = manualChapterMapper.toDto(manualChapter);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restManualChapterMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(manualChapterDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ManualChapter in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteManualChapter() throws Exception {
        // Initialize the database
        insertedManualChapter = manualChapterRepository.saveAndFlush(manualChapter);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the manualChapter
        restManualChapterMockMvc
            .perform(delete(ENTITY_API_URL_ID, manualChapter.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return manualChapterRepository.count();
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

    protected ManualChapter getPersistedManualChapter(ManualChapter manualChapter) {
        return manualChapterRepository.findById(manualChapter.getId()).orElseThrow();
    }

    protected void assertPersistedManualChapterToMatchAllProperties(ManualChapter expectedManualChapter) {
        assertManualChapterAllPropertiesEquals(expectedManualChapter, getPersistedManualChapter(expectedManualChapter));
    }

    protected void assertPersistedManualChapterToMatchUpdatableProperties(ManualChapter expectedManualChapter) {
        assertManualChapterAllUpdatablePropertiesEquals(expectedManualChapter, getPersistedManualChapter(expectedManualChapter));
    }
}
