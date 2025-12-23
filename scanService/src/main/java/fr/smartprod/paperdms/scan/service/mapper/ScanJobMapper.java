package fr.smartprod.paperdms.scan.service.mapper;

import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.domain.ScanJob;
import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import fr.smartprod.paperdms.scan.service.dto.ScanJobDTO;
import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScanJob} and its DTO {@link ScanJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScanJobMapper extends EntityMapper<ScanJobDTO, ScanJob> {
    @Mapping(target = "scannerConfig", source = "scannerConfig", qualifiedByName = "scannerConfigurationId")
    @Mapping(target = "batch", source = "batch", qualifiedByName = "scanBatchId")
    ScanJobDTO toDto(ScanJob s);

    @Named("scannerConfigurationId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScannerConfigurationDTO toDtoScannerConfigurationId(ScannerConfiguration scannerConfiguration);

    @Named("scanBatchId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ScanBatchDTO toDtoScanBatchId(ScanBatch scanBatch);
}
