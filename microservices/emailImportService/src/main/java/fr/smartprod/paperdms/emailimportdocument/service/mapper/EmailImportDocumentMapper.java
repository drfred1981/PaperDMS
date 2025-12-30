package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailImportDocument} and its DTO {@link EmailImportDocumentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailImportDocumentMapper extends EntityMapper<EmailImportDocumentDTO, EmailImportDocument> {
    @Mapping(target = "appliedRule", source = "appliedRule", qualifiedByName = "emailImportImportRuleId")
    EmailImportDocumentDTO toDto(EmailImportDocument s);

    @Named("emailImportImportRuleId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmailImportImportRuleDTO toDtoEmailImportImportRuleId(EmailImportImportRule emailImportImportRule);
}
