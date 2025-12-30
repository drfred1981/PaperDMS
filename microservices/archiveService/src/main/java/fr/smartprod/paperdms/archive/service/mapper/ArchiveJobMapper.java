package fr.smartprod.paperdms.archive.service.mapper;

import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArchiveJob} and its DTO {@link ArchiveJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArchiveJobMapper extends EntityMapper<ArchiveJobDTO, ArchiveJob> {}
