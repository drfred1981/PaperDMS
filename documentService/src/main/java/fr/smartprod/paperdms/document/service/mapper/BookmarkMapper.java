package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Bookmark;
import fr.smartprod.paperdms.document.service.dto.BookmarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bookmark} and its DTO {@link BookmarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface BookmarkMapper extends EntityMapper<BookmarkDTO, Bookmark> {}
