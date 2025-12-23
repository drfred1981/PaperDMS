package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.domain.PermissionGroup;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentPermission} and its DTO {@link DocumentPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentPermissionMapper extends EntityMapper<DocumentPermissionDTO, DocumentPermission> {
    @Mapping(target = "permissionGroup", source = "permissionGroup", qualifiedByName = "permissionGroupId")
    DocumentPermissionDTO toDto(DocumentPermission s);

    @Named("permissionGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    PermissionGroupDTO toDtoPermissionGroupId(PermissionGroup permissionGroup);
}
