package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportMapping;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportMappingDTO;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailImportImportMapping} and its DTO {@link EmailImportImportMappingDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailImportImportMappingMapper extends EntityMapper<EmailImportImportMappingDTO, EmailImportImportMapping> {
    @Mapping(target = "rule", source = "rule", qualifiedByName = "emailImportImportRuleId")
    EmailImportImportMappingDTO toDto(EmailImportImportMapping s);

    @Named("emailImportImportRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmailImportImportRuleDTO toDtoEmailImportImportRuleId(EmailImportImportRule emailImportImportRule);
}
