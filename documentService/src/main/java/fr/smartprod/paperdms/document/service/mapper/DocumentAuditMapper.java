package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentAudit} and its DTO {@link DocumentAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentAuditMapper extends EntityMapper<DocumentAuditDTO, DocumentAudit> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    DocumentAuditDTO toDto(DocumentAudit s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
