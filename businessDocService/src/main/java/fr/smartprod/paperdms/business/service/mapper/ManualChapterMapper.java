package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.domain.ManualChapter;
import fr.smartprod.paperdms.business.service.dto.ManualChapterDTO;
import fr.smartprod.paperdms.business.service.dto.ManualDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ManualChapter} and its DTO {@link ManualChapterDTO}.
 */
@Mapper(componentModel = "spring")
public interface ManualChapterMapper extends EntityMapper<ManualChapterDTO, ManualChapter> {
    @Mapping(target = "manual", source = "manual", qualifiedByName = "manualId")
    @Mapping(target = "parentChapter", source = "parentChapter", qualifiedByName = "manualChapterId")
    ManualChapterDTO toDto(ManualChapter s);

    @Named("manualChapterId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManualChapterDTO toDtoManualChapterId(ManualChapter manualChapter);

    @Named("manualId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ManualDTO toDtoManualId(Manual manual);
}
