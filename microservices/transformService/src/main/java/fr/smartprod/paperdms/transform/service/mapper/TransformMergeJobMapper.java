package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.TransformMergeJob;
import fr.smartprod.paperdms.transform.service.dto.TransformMergeJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransformMergeJob} and its DTO {@link TransformMergeJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransformMergeJobMapper extends EntityMapper<TransformMergeJobDTO, TransformMergeJob> {}
