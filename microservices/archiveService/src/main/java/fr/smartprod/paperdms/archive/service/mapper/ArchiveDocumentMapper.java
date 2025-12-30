package fr.smartprod.paperdms.archive.service.mapper;

import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ArchiveDocument} and its DTO {@link ArchiveDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface ArchiveDocumentMapper extends EntityMapper<ArchiveDocumentDTO, ArchiveDocument> {
    @Mapping(target = "archiveJob", source = "archiveJob", qualifiedByName = "archiveJobId")
    ArchiveDocumentDTO toDto(ArchiveDocument s);

    @Named("archiveJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ArchiveJobDTO toDtoArchiveJobId(ArchiveJob archiveJob);
}
