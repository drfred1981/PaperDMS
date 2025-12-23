package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.PermissionGroup;
import fr.smartprod.paperdms.document.service.dto.PermissionGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link PermissionGroup} and its DTO {@link PermissionGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface PermissionGroupMapper extends EntityMapper<PermissionGroupDTO, PermissionGroup> {}
