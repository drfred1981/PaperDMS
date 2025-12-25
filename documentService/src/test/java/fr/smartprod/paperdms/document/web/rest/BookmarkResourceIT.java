package fr.smartprod.paperdms.document.web.rest;

import static fr.smartprod.paperdms.document.domain.BookmarkAsserts.*;
import static fr.smartprod.paperdms.document.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.smartprod.paperdms.document.IntegrationTest;
import fr.smartprod.paperdms.document.domain.Bookmark;
import fr.smartprod.paperdms.document.domain.enumeration.BookmarkType;
import fr.smartprod.paperdms.document.repository.BookmarkRepository;
import fr.smartprod.paperdms.document.service.dto.BookmarkDTO;
import fr.smartprod.paperdms.document.service.mapper.BookmarkMapper;
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
 * Integration tests for the {@link BookmarkResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class BookmarkResourceIT {

    private static final String DEFAULT_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_USER_ID = "BBBBBBBBBB";

    private static final BookmarkType DEFAULT_ENTITY_TYPE = BookmarkType.DOCUMENT;
    private static final BookmarkType UPDATED_ENTITY_TYPE = BookmarkType.FOLDER;

    private static final Long DEFAULT_ENTITY_ID = 1L;
    private static final Long UPDATED_ENTITY_ID = 2L;

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/bookmarks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private BookmarkRepository bookmarkRepository;

    @Autowired
    private BookmarkMapper bookmarkMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restBookmarkMockMvc;

    private Bookmark bookmark;

    private Bookmark insertedBookmark;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bookmark createEntity() {
        return new Bookmark()
            .userId(DEFAULT_USER_ID)
            .entityType(DEFAULT_ENTITY_TYPE)
            .entityId(DEFAULT_ENTITY_ID)
            .createdDate(DEFAULT_CREATED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bookmark createUpdatedEntity() {
        return new Bookmark()
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .createdDate(UPDATED_CREATED_DATE);
    }

    @BeforeEach
    void initTest() {
        bookmark = createEntity();
    }

    @AfterEach
    void cleanup() {
        if (insertedBookmark != null) {
            bookmarkRepository.delete(insertedBookmark);
            insertedBookmark = null;
        }
    }

    @Test
    @Transactional
    void createBookmark() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);
        var returnedBookmarkDTO = om.readValue(
            restBookmarkMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            BookmarkDTO.class
        );

        // Validate the Bookmark in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedBookmark = bookmarkMapper.toEntity(returnedBookmarkDTO);
        assertBookmarkUpdatableFieldsEquals(returnedBookmark, getPersistedBookmark(returnedBookmark));

        insertedBookmark = returnedBookmark;
    }

    @Test
    @Transactional
    void createBookmarkWithExistingId() throws Exception {
        // Create the Bookmark with an existing ID
        bookmark.setId(1L);
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUserIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookmark.setUserId(null);

        // Create the Bookmark, which fails.
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        restBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookmark.setEntityType(null);

        // Create the Bookmark, which fails.
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        restBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEntityIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookmark.setEntityId(null);

        // Create the Bookmark, which fails.
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        restBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        bookmark.setCreatedDate(null);

        // Create the Bookmark, which fails.
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        restBookmarkMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllBookmarks() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        // Get all the bookmarkList
        restBookmarkMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bookmark.getId().intValue())))
            .andExpect(jsonPath("$.[*].userId").value(hasItem(DEFAULT_USER_ID)))
            .andExpect(jsonPath("$.[*].entityType").value(hasItem(DEFAULT_ENTITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].entityId").value(hasItem(DEFAULT_ENTITY_ID.intValue())))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())));
    }

    @Test
    @Transactional
    void getBookmark() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        // Get the bookmark
        restBookmarkMockMvc
            .perform(get(ENTITY_API_URL_ID, bookmark.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(bookmark.getId().intValue()))
            .andExpect(jsonPath("$.userId").value(DEFAULT_USER_ID))
            .andExpect(jsonPath("$.entityType").value(DEFAULT_ENTITY_TYPE.toString()))
            .andExpect(jsonPath("$.entityId").value(DEFAULT_ENTITY_ID.intValue()))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingBookmark() throws Exception {
        // Get the bookmark
        restBookmarkMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingBookmark() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookmark
        Bookmark updatedBookmark = bookmarkRepository.findById(bookmark.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedBookmark are not directly saved in db
        em.detach(updatedBookmark);
        updatedBookmark
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .createdDate(UPDATED_CREATED_DATE);
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(updatedBookmark);

        restBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookmarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookmarkDTO))
            )
            .andExpect(status().isOk());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedBookmarkToMatchAllProperties(updatedBookmark);
    }

    @Test
    @Transactional
    void putNonExistingBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, bookmarkDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(bookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateBookmarkWithPatch() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookmark using partial update
        Bookmark partialUpdatedBookmark = new Bookmark();
        partialUpdatedBookmark.setId(bookmark.getId());

        partialUpdatedBookmark.createdDate(UPDATED_CREATED_DATE);

        restBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookmark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookmark))
            )
            .andExpect(status().isOk());

        // Validate the Bookmark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookmarkUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedBookmark, bookmark), getPersistedBookmark(bookmark));
    }

    @Test
    @Transactional
    void fullUpdateBookmarkWithPatch() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the bookmark using partial update
        Bookmark partialUpdatedBookmark = new Bookmark();
        partialUpdatedBookmark.setId(bookmark.getId());

        partialUpdatedBookmark
            .userId(UPDATED_USER_ID)
            .entityType(UPDATED_ENTITY_TYPE)
            .entityId(UPDATED_ENTITY_ID)
            .createdDate(UPDATED_CREATED_DATE);

        restBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedBookmark.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedBookmark))
            )
            .andExpect(status().isOk());

        // Validate the Bookmark in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertBookmarkUpdatableFieldsEquals(partialUpdatedBookmark, getPersistedBookmark(partialUpdatedBookmark));
    }

    @Test
    @Transactional
    void patchNonExistingBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, bookmarkDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(bookmarkDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamBookmark() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        bookmark.setId(longCount.incrementAndGet());

        // Create the Bookmark
        BookmarkDTO bookmarkDTO = bookmarkMapper.toDto(bookmark);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restBookmarkMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(bookmarkDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Bookmark in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteBookmark() throws Exception {
        // Initialize the database
        insertedBookmark = bookmarkRepository.saveAndFlush(bookmark);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the bookmark
        restBookmarkMockMvc
            .perform(delete(ENTITY_API_URL_ID, bookmark.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return bookmarkRepository.count();
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

    protected Bookmark getPersistedBookmark(Bookmark bookmark) {
        return bookmarkRepository.findById(bookmark.getId()).orElseThrow();
    }

    protected void assertPersistedBookmarkToMatchAllProperties(Bookmark expectedBookmark) {
        assertBookmarkAllPropertiesEquals(expectedBookmark, getPersistedBookmark(expectedBookmark));
    }

    protected void assertPersistedBookmarkToMatchUpdatableProperties(Bookmark expectedBookmark) {
        assertBookmarkAllUpdatablePropertiesEquals(expectedBookmark, getPersistedBookmark(expectedBookmark));
    }
}
