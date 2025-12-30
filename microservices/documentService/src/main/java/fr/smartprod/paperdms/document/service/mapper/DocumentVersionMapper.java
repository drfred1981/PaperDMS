package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentVersionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentVersion} and its DTO {@link DocumentVersionDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentVersionMapper extends EntityMapper<DocumentVersionDTO, DocumentVersion> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    DocumentVersionDTO toDto(DocumentVersion s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
