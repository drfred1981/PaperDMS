package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.service.dto.MetaMetaTagCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaMetaTagCategory} and its DTO {@link MetaMetaTagCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaMetaTagCategoryMapper extends EntityMapper<MetaMetaTagCategoryDTO, MetaMetaTagCategory> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "metaMetaTagCategoryId")
    MetaMetaTagCategoryDTO toDto(MetaMetaTagCategory s);

    @Named("metaMetaTagCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaMetaTagCategoryDTO toDtoMetaMetaTagCategoryId(MetaMetaTagCategory metaMetaTagCategory);
}
