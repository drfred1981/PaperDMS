package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentExtractedFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentExtractedField} and its DTO {@link DocumentExtractedFieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentExtractedFieldMapper extends EntityMapper<DocumentExtractedFieldDTO, DocumentExtractedField> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    DocumentExtractedFieldDTO toDto(DocumentExtractedField s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
