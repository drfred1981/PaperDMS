package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.TransformConversionJob;
import fr.smartprod.paperdms.transform.service.dto.TransformConversionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransformConversionJob} and its DTO {@link TransformConversionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransformConversionJobMapper extends EntityMapper<TransformConversionJobDTO, TransformConversionJob> {}
