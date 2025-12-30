package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentServiceStatus} and its DTO {@link DocumentServiceStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentServiceStatusMapper extends EntityMapper<DocumentServiceStatusDTO, DocumentServiceStatus> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    DocumentServiceStatusDTO toDto(DocumentServiceStatus s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
