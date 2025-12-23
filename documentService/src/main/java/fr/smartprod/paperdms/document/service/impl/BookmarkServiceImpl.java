package fr.smartprod.paperdms.document.service.impl;

import fr.smartprod.paperdms.document.domain.Bookmark;
import fr.smartprod.paperdms.document.repository.BookmarkRepository;
import fr.smartprod.paperdms.document.service.BookmarkService;
import fr.smartprod.paperdms.document.service.dto.BookmarkDTO;
import fr.smartprod.paperdms.document.service.mapper.BookmarkMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link fr.smartprod.paperdms.document.domain.Bookmark}.
 */
@Service
@Transactional
public class BookmarkServiceImpl implements BookmarkService {

    private static final Logger LOG = LoggerFactory.getLogger(BookmarkServiceImpl.class);

    private final BookmarkRepository bookmarkRepository;

    private final BookmarkMapper bookmarkMapper;

    public BookmarkServiceImpl(BookmarkRepository bookmarkRepository, BookmarkMapper bookmarkMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.bookmarkMapper = bookmarkMapper;
    }

    @Override
    public BookmarkDTO save(BookmarkDTO bookmarkDTO) {
        LOG.debug("Request to save Bookmark : {}", bookmarkDTO);
        Bookmark bookmark = bookmarkMapper.toEntity(bookmarkDTO);
        bookmark = bookmarkRepository.save(bookmark);
        return bookmarkMapper.toDto(bookmark);
    }

    @Override
    public BookmarkDTO update(BookmarkDTO bookmarkDTO) {
        LOG.debug("Request to update Bookmark : {}", bookmarkDTO);
        Bookmark bookmark = bookmarkMapper.toEntity(bookmarkDTO);
        bookmark = bookmarkRepository.save(bookmark);
        return bookmarkMapper.toDto(bookmark);
    }

    @Override
    public Optional<BookmarkDTO> partialUpdate(BookmarkDTO bookmarkDTO) {
        LOG.debug("Request to partially update Bookmark : {}", bookmarkDTO);

        return bookmarkRepository
            .findById(bookmarkDTO.getId())
            .map(existingBookmark -> {
                bookmarkMapper.partialUpdate(existingBookmark, bookmarkDTO);

                return existingBookmark;
            })
            .map(bookmarkRepository::save)
            .map(bookmarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BookmarkDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Bookmarks");
        return bookmarkRepository.findAll(pageable).map(bookmarkMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<BookmarkDTO> findOne(Long id) {
        LOG.debug("Request to get Bookmark : {}", id);
        return bookmarkRepository.findById(id).map(bookmarkMapper::toDto);
    }

    @Override
    public void delete(Long id) {
        LOG.debug("Request to delete Bookmark : {}", id);
        bookmarkRepository.deleteById(id);
    }
}
