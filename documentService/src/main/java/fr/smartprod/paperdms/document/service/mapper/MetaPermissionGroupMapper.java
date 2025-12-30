package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaPermissionGroup} and its DTO {@link MetaPermissionGroupDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaPermissionGroupMapper extends EntityMapper<MetaPermissionGroupDTO, MetaPermissionGroup> {}
