package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaTag} and its DTO {@link MetaTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaTagMapper extends EntityMapper<MetaTagDTO, MetaTag> {
    @Mapping(target = "metaMetaTagCategory", source = "metaMetaTagCategory", qualifiedByName = "metaMetaTagCategoryId")
    MetaTagDTO toDto(MetaTag s);

    @Named("metaMetaTagCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaMetaTagCategoryDTO toDtoMetaMetaTagCategoryId(MetaMetaTagCategory metaMetaTagCategory);
}
