package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.dto.MetaTagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentTag} and its DTO {@link DocumentTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentTagMapper extends EntityMapper<DocumentTagDTO, DocumentTag> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    @Mapping(target = "metaTag", source = "metaTag", qualifiedByName = "metaTagId")
    DocumentTagDTO toDto(DocumentTag s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);

    @Named("metaTagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    MetaTagDTO toDtoMetaTagId(MetaTag metaTag);
}
