package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AutoTagJob;
import fr.smartprod.paperdms.ai.service.dto.AutoTagJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AutoTagJob} and its DTO {@link AutoTagJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface AutoTagJobMapper extends EntityMapper<AutoTagJobDTO, AutoTagJob> {}
