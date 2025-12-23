package fr.smartprod.paperdms.scan.service.mapper;

import fr.smartprod.paperdms.scan.domain.ScannerConfiguration;
import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScannerConfiguration} and its DTO {@link ScannerConfigurationDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScannerConfigurationMapper extends EntityMapper<ScannerConfigurationDTO, ScannerConfiguration> {}
