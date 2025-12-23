package fr.smartprod.paperdms.ai.service.mapper;

import fr.smartprod.paperdms.ai.domain.AiCache;
import fr.smartprod.paperdms.ai.service.dto.AiCacheDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AiCache} and its DTO {@link AiCacheDTO}.
 */
@Mapper(componentModel = "spring")
public interface AiCacheMapper extends EntityMapper<AiCacheDTO, AiCache> {}
