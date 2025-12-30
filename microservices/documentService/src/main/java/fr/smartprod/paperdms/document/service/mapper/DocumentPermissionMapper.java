package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentPermissionDTO;
import fr.smartprod.paperdms.document.service.dto.MetaPermissionGroupDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentPermission} and its DTO {@link DocumentPermissionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentPermissionMapper extends EntityMapper<DocumentPermissionDTO, DocumentPermission> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    @Mapping(target = "metaPermissionGroup", source = "metaPermissionGroup", qualifiedByName = "metaPermissionGroupId")
    DocumentPermissionDTO toDto(DocumentPermission s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);

    @Named("metaPermissionGroupId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaPermissionGroupDTO toDtoMetaPermissionGroupId(MetaPermissionGroup metaPermissionGroup);
}
