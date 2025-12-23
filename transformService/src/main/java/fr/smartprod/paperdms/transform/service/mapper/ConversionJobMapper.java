package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.ConversionJob;
import fr.smartprod.paperdms.transform.service.dto.ConversionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ConversionJob} and its DTO {@link ConversionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface ConversionJobMapper extends EntityMapper<ConversionJobDTO, ConversionJob> {}
