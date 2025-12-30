package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaBookmark;
import fr.smartprod.paperdms.document.service.dto.MetaBookmarkDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaBookmark} and its DTO {@link MetaBookmarkDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaBookmarkMapper extends EntityMapper<MetaBookmarkDTO, MetaBookmark> {}
