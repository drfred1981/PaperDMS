package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.TransformCompressionJob;
import fr.smartprod.paperdms.transform.service.dto.TransformCompressionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransformCompressionJob} and its DTO {@link TransformCompressionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransformCompressionJobMapper extends EntityMapper<TransformCompressionJobDTO, TransformCompressionJob> {}
