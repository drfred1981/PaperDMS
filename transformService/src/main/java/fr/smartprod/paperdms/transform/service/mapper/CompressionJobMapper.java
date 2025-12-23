package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.CompressionJob;
import fr.smartprod.paperdms.transform.service.dto.CompressionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link CompressionJob} and its DTO {@link CompressionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface CompressionJobMapper extends EntityMapper<CompressionJobDTO, CompressionJob> {}
