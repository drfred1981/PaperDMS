package fr.smartprod.paperdms.document.service.mapper;

import static fr.smartprod.paperdms.document.domain.BookmarkAsserts.*;
import static fr.smartprod.paperdms.document.domain.BookmarkTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BookmarkMapperTest {

    private BookmarkMapper bookmarkMapper;

    @BeforeEach
    void setUp() {
        bookmarkMapper = new BookmarkMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getBookmarkSample1();
        var actual = bookmarkMapper.toEntity(bookmarkMapper.toDto(expected));
        assertBookmarkAllPropertiesEquals(expected, actual);
    }
}
