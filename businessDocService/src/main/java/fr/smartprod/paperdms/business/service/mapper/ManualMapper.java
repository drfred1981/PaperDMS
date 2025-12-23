package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Manual} and its DTO {@link ManualDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualMapper extends EntityMapper<ManualDTO, Manual> {}
