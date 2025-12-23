package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.ComparisonJob;
import fr.smartprod.paperdms.transform.service.dto.ComparisonJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ComparisonJob} and its DTO {@link ComparisonJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface ComparisonJobMapper extends EntityMapper<ComparisonJobDTO, ComparisonJob> {}
