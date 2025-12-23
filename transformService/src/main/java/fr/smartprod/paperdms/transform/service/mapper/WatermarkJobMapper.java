package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.WatermarkJob;
import fr.smartprod.paperdms.transform.service.dto.WatermarkJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link WatermarkJob} and its DTO {@link WatermarkJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface WatermarkJobMapper extends EntityMapper<WatermarkJobDTO, WatermarkJob> {}
