package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeFieldDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentTypeField} and its DTO {@link DocumentTypeFieldDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentTypeFieldMapper extends EntityMapper<DocumentTypeFieldDTO, DocumentTypeField> {
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "documentTypeId")
    DocumentTypeFieldDTO toDto(DocumentTypeField s);

    @Named("documentTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentTypeDTO toDtoDocumentTypeId(DocumentType documentType);
}
