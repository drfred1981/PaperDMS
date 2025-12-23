package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.domain.Tag;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentTagDTO;
import fr.smartprod.paperdms.document.service.dto.TagDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentTag} and its DTO {@link DocumentTagDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentTagMapper extends EntityMapper<DocumentTagDTO, DocumentTag> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    @Mapping(target = "tag", source = "tag", qualifiedByName = "tagId")
    DocumentTagDTO toDto(DocumentTag s);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);

    @Named("tagId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    TagDTO toDtoTagId(Tag tag);
}
