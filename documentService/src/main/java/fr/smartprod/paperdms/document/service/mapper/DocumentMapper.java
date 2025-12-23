package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.domain.Folder;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.dto.FolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Document} and its DTO {@link DocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMapper extends EntityMapper<DocumentDTO, Document> {
    @Mapping(target = "folder", source = "folder", qualifiedByName = "folderId")
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "documentTypeId")
    DocumentDTO toDto(Document s);

    @Named("folderId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    FolderDTO toDtoFolderId(Folder folder);

    @Named("documentTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentTypeDTO toDtoDocumentTypeId(DocumentType documentType);
}
