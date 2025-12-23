package fr.smartprod.paperdms.monitoring.service.mapper;

import fr.smartprod.paperdms.monitoring.domain.DocumentWatch;
import fr.smartprod.paperdms.monitoring.service.dto.DocumentWatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentWatch} and its DTO {@link DocumentWatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentWatchMapper extends EntityMapper<DocumentWatchDTO, DocumentWatch> {}
