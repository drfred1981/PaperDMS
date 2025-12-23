package fr.smartprod.paperdms.export.service.mapper;

import fr.smartprod.paperdms.export.domain.ExportJob;
import fr.smartprod.paperdms.export.domain.ExportResult;
import fr.smartprod.paperdms.export.service.dto.ExportJobDTO;
import fr.smartprod.paperdms.export.service.dto.ExportResultDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ExportResult} and its DTO {@link ExportResultDTO}.
 */
@Mapper(componentModel = "spring")
public interface ExportResultMapper extends EntityMapper<ExportResultDTO, ExportResult> {
    @Mapping(target = "exportJob", source = "exportJob", qualifiedByName = "exportJobId")
    ExportResultDTO toDto(ExportResult s);

    @Named("exportJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ExportJobDTO toDtoExportJobId(ExportJob exportJob);
}
