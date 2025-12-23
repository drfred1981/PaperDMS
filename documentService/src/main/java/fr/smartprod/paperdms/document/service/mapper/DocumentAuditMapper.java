package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.service.dto.DocumentAuditDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentAudit} and its DTO {@link DocumentAuditDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentAuditMapper extends EntityMapper<DocumentAuditDTO, DocumentAudit> {}
