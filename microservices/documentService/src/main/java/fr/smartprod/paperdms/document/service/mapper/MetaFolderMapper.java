package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaFolder} and its DTO {@link MetaFolderDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaFolderMapper extends EntityMapper<MetaFolderDTO, MetaFolder> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "metaFolderId")
    MetaFolderDTO toDto(MetaFolder s);

    @Named("metaFolderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaFolderDTO toDtoMetaFolderId(MetaFolder metaFolder);
}
