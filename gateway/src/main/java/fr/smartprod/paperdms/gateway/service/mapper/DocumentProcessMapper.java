package fr.smartprod.paperdms.gateway.service.mapper;

import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.service.dto.DocumentProcessDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentProcess} and its DTO {@link DocumentProcessDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentProcessMapper extends EntityMapper<DocumentProcessDTO, DocumentProcess> {}
