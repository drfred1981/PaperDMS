package fr.smartprod.paperdms.export.service.mapper;

import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.domain.ExportPattern;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.service.dto.ExportPatternDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExportJob} and its DTO {@link ExportJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExportJobMapper extends EntityMapper<ExportJobDTO, ExportJob> {
    @Mapping(target = "exportPattern", source = "exportPattern", qualifiedByName = "exportPatternId")
    ExportJobDTO toDto(ExportJob s);

    @Named("exportPatternId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExportPatternDTO toDtoExportPatternId(ExportPattern exportPattern);
}
