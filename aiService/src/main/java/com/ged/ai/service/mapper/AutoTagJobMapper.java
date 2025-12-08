package com.ged.ai.service.mapper;

import com.ged.ai.domain.AutoTagJob;
import com.ged.ai.service.dto.AutoTagJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link AutoTagJob} and its DTO {@link AutoTagJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface AutoTagJobMapper extends EntityMapper<AutoTagJobDTO, AutoTagJob> {}
