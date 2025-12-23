package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.service.dto.DocumentRelationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentRelation} and its DTO {@link DocumentRelationDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentRelationMapper extends EntityMapper<DocumentRelationDTO, DocumentRelation> {}
