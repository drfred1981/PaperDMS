package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AICache;
import fr.smartprod.paperdms.ai.service.dto.AICacheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AICache} and its DTO {@link AICacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface AICacheMapper extends EntityMapper<AICacheDTO, AICache> {}
