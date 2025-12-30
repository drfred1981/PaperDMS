package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.Document;
import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import fr.smartprod.paperdms.document.service.dto.DocumentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentComment} and its DTO {@link DocumentCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentCommentMapper extends EntityMapper<DocumentCommentDTO, DocumentComment> {
    @Mapping(target = "document", source = "document", qualifiedByName = "documentId")
    @Mapping(target = "parentComment", source = "parentComment", qualifiedByName = "documentCommentId")
    DocumentCommentDTO toDto(DocumentComment s);

    @Named("documentCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentCommentDTO toDtoDocumentCommentId(DocumentComment documentComment);

    @Named("documentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentDTO toDtoDocumentId(Document document);
}
