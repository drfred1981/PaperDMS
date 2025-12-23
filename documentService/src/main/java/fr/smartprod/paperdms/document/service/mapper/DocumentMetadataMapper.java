package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentMetadata;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentMetadataDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentMetadata} and its DTO {@link DocumentMetadataDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentMetadataMapper extends EntityMapper<DocumentMetadataDTO, DocumentMetadata> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    DocumentMetadataDTO toDto(DocumentMetadata s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
