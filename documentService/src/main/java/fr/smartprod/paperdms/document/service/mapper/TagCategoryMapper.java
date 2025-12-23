package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TagCategory} and its DTO {@link TagCategoryDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagCategoryMapper extends EntityMapper<TagCategoryDTO, TagCategory> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "tagCategoryId")
    TagCategoryDTO toDto(TagCategory s);

    @Named("tagCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagCategoryDTO toDtoTagCategoryId(TagCategory tagCategory);
}
