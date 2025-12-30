package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportImportRule;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailImportImportRule} and its DTO {@link EmailImportImportRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailImportImportRuleMapper extends EntityMapper<EmailImportImportRuleDTO, EmailImportImportRule> {}
