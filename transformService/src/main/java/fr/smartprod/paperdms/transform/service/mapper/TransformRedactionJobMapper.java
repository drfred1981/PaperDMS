package fr.smartprod.paperdms.transform.service.mapper;

import fr.smartprod.paperdms.transform.domain.TransformRedactionJob;
import fr.smartprod.paperdms.transform.service.dto.TransformRedactionJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link TransformRedactionJob} and its DTO {@link TransformRedactionJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface TransformRedactionJobMapper extends EntityMapper<TransformRedactionJobDTO, TransformRedactionJob> {}
