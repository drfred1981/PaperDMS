package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Folder} and its DTO {@link FolderDTO}.
 */
@Mapper(componentModel = "spring")
public interface FolderMapper extends EntityMapper<FolderDTO, Folder> {
    @Mapping(target = "parent", source = "parent", qualifiedByName = "folderId")
    FolderDTO toDto(Folder s);

    @Named("folderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FolderDTO toDtoFolderId(Folder folder);
}
