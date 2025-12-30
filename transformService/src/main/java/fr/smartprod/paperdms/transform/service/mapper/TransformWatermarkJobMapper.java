package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.TransformWatermarkJob;
import fr.smartprod.paperdms.transform.service.dto.TransformWatermarkJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransformWatermarkJob} and its DTO {@link TransformWatermarkJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransformWatermarkJobMapper extends EntityMapper<TransformWatermarkJobDTO, TransformWatermarkJob> {}
