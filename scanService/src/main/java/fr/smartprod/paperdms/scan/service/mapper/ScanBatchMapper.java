package fr.smartprod.paperdms.scan.service.mapper;

import fr.smartprod.paperdms.scan.domain.ScanBatch;
import fr.smartprod.paperdms.scan.service.dto.ScanBatchDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ScanBatch} and its DTO {@link ScanBatchDTO}.
 */
@Mapper(componentModel = "spring")
public interface ScanBatchMapper extends EntityMapper<ScanBatchDTO, ScanBatch> {}
