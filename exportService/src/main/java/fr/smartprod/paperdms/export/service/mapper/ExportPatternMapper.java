package fr.smartprod.paperdms.export.service.mapper;

import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExportPattern} and its DTO {@link ExportPatternDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExportPatternMapper extends EntityMapper<ExportPatternDTO, ExportPattern> {}
