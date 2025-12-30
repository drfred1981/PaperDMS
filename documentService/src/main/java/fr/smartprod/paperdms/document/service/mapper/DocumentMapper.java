package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.domain.MetaFolder;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.dto.MetaFolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "documentTypeId")
    @Mapping(target = "folder", source = "folder", qualifiedByName = "metaFolderId")
    DocumentDTO toDto(Document s);

    @Named("documentTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentTypeDTO toDtoDocumentTypeId(DocumentType documentType);

    @Named("metaFolderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaFolderDTO toDtoMetaFolderId(MetaFolder metaFolder);
}
