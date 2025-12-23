package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.service.dto.DocumentStatisticsDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentStatistics} and its DTO {@link DocumentStatisticsDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentStatisticsMapper extends EntityMapper<DocumentStatisticsDTO, DocumentStatistics> {}
