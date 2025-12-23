package fr.smartprod.paperdms.scan.service.mapper;

import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.domain.ScannedPage;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.service.dto.ScannedPageDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScannedPage} and its DTO {@link ScannedPageDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScannedPageMapper extends EntityMapper<ScannedPageDTO, ScannedPage> {
    @Mapping(target = "scanJob", source = "scanJob", qualifiedByName = "scanJobId")
    ScannedPageDTO toDto(ScannedPage s);

    @Named("scanJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScanJobDTO toDtoScanJobId(ScanJob scanJob);
}
