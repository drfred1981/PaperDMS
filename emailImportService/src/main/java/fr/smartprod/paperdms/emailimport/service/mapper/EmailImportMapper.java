package fr.smartprod.paperdms.emailimport.service.mapper;

import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailImport} and its DTO {@link EmailImportDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailImportMapper extends EntityMapper<EmailImportDTO, EmailImport> {
    @Mapping(target = "appliedRule", source = "appliedRule", qualifiedByName = "importRuleId")
    EmailImportDTO toDto(EmailImport s);

    @Named("importRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ImportRuleDTO toDtoImportRuleId(ImportRule importRule);
}
