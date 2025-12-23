package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.MergeJob;
import fr.smartprod.paperdms.transform.service.dto.MergeJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MergeJob} and its DTO {@link MergeJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface MergeJobMapper extends EntityMapper<MergeJobDTO, MergeJob> {}
