package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentTemplate;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.service.dto.DocumentTemplateDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTypeDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentTemplate} and its DTO {@link DocumentTemplateDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentTemplateMapper extends EntityMapper<DocumentTemplateDTO, DocumentTemplate> {
    @Mapping(target = "documentType", source = "documentType", qualifiedByName = "documentTypeId")
    DocumentTemplateDTO toDto(DocumentTemplate s);

    @Named("documentTypeId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentTypeDTO toDtoDocumentTypeId(DocumentType documentType);
}
