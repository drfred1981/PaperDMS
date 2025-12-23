package fr.smartprod.paperdms.document.web.rest;

import fr.smartprod.paperdms.document.repository.BookmarkRepository;
import fr.smartprod.paperdms.document.service.BookmarkService;
import fr.smartprod.paperdms.document.service.dto.BookmarkDTO;
import fr.smartprod.paperdms.document.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link fr.smartprod.paperdms.document.domain.Bookmark}.
 */
@RestController
@RequestMapping("/api/bookmarks")
public class BookmarkResource {

    private static final Logger LOG = LoggerFactory.getLogger(BookmarkResource.class);

    private static final String ENTITY_NAME = "documentServiceBookmark";

    @Value("${jhipster.clientApp.name:documentService}")
    private String applicationName;

    private final BookmarkService bookmarkService;

    private final BookmarkRepository bookmarkRepository;

    public BookmarkResource(BookmarkService bookmarkService, BookmarkRepository bookmarkRepository) {
        this.bookmarkService = bookmarkService;
        this.bookmarkRepository = bookmarkRepository;
    }

    /**
     * {@code POST  /bookmarks} : Create a new bookmark.
     *
     * @param bookmarkDTO the bookmarkDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new bookmarkDTO, or with status {@code 400 (Bad Request)} if the bookmark has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BookmarkDTO> createBookmark(@Valid @RequestBody BookmarkDTO bookmarkDTO) throws URISyntaxException {
        LOG.debug("REST request to save Bookmark : {}", bookmarkDTO);
        if (bookmarkDTO.getId() != null) {
            throw new BadRequestAlertException("A new bookmark cannot already have an ID", ENTITY_NAME, "idexists");
        }
        bookmarkDTO = bookmarkService.save(bookmarkDTO);
        return ResponseEntity.created(new URI("/api/bookmarks/" + bookmarkDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, bookmarkDTO.getId().toString()))
            .body(bookmarkDTO);
    }

    /**
     * {@code PUT  /bookmarks/:id} : Updates an existing bookmark.
     *
     * @param id the id of the bookmarkDTO to save.
     * @param bookmarkDTO the bookmarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookmarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookmarkDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the bookmarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BookmarkDTO> updateBookmark(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BookmarkDTO bookmarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update Bookmark : {}, {}", id, bookmarkDTO);
        if (bookmarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookmarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookmarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        bookmarkDTO = bookmarkService.update(bookmarkDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookmarkDTO.getId().toString()))
            .body(bookmarkDTO);
    }

    /**
     * {@code PATCH  /bookmarks/:id} : Partial updates given fields of an existing bookmark, field will ignore if it is null
     *
     * @param id the id of the bookmarkDTO to save.
     * @param bookmarkDTO the bookmarkDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated bookmarkDTO,
     * or with status {@code 400 (Bad Request)} if the bookmarkDTO is not valid,
     * or with status {@code 404 (Not Found)} if the bookmarkDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the bookmarkDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BookmarkDTO> partialUpdateBookmark(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BookmarkDTO bookmarkDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Bookmark partially : {}, {}", id, bookmarkDTO);
        if (bookmarkDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, bookmarkDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!bookmarkRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BookmarkDTO> result = bookmarkService.partialUpdate(bookmarkDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, bookmarkDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /bookmarks} : get all the bookmarks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of bookmarks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<BookmarkDTO>> getAllBookmarks(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of Bookmarks");
        Page<BookmarkDTO> page = bookmarkService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bookmarks/:id} : get the "id" bookmark.
     *
     * @param id the id of the bookmarkDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the bookmarkDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<BookmarkDTO> getBookmark(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Bookmark : {}", id);
        Optional<BookmarkDTO> bookmarkDTO = bookmarkService.findOne(id);
        return ResponseUtil.wrapOrNotFound(bookmarkDTO);
    }

    /**
     * {@code DELETE  /bookmarks/:id} : delete the "id" bookmark.
     *
     * @param id the id of the bookmarkDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookmark(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Bookmark : {}", id);
        bookmarkService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
