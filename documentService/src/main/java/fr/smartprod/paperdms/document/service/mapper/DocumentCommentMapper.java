package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.service.dto.DocumentCommentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentComment} and its DTO {@link DocumentCommentDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentCommentMapper extends EntityMapper<DocumentCommentDTO, DocumentComment> {
    @Mapping(target = "parentComment", source = "parentComment", qualifiedByName = "documentCommentId")
    DocumentCommentDTO toDto(DocumentComment s);

    @Named("documentCommentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    DocumentCommentDTO toDtoDocumentCommentId(DocumentComment documentComment);
}
