package fr.smartprod.paperdms.emailimport.service.mapper;

import fr.smartprod.paperdms.emailimport.domain.ImportMapping;
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.service.dto.ImportMappingDTO;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImportMapping} and its DTO {@link ImportMappingDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImportMappingMapper extends EntityMapper<ImportMappingDTO, ImportMapping> {
    @Mapping(target = "rule", source = "rule", qualifiedByName = "importRuleId")
    ImportMappingDTO toDto(ImportMapping s);

    @Named("importRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImportRuleDTO toDtoImportRuleId(ImportRule importRule);
}
