package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.RedactionJob;
import fr.smartprod.paperdms.transform.service.dto.RedactionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link RedactionJob} and its DTO {@link RedactionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface RedactionJobMapper extends EntityMapper<RedactionJobDTO, RedactionJob> {}
