package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Tag;
import fr.smartprod.paperdms.document.domain.TagCategory;
import fr.smartprod.paperdms.document.service.dto.TagCategoryDTO;
import fr.smartprod.paperdms.document.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Tag} and its DTO {@link TagDTO}.
 */
@Mapper(componentModel = "spring")
public interface TagMapper extends EntityMapper<TagDTO, Tag> {
    @Mapping(target = "tagCategory", source = "tagCategory", qualifiedByName = "tagCategoryId")
    TagDTO toDto(Tag s);

    @Named("tagCategoryId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagCategoryDTO toDtoTagCategoryId(TagCategory tagCategory);
}
