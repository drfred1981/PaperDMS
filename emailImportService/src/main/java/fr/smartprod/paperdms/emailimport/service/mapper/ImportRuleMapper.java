package fr.smartprod.paperdms.emailimport.service.mapper;

import fr.smartprod.paperdms.emailimport.domain.ImportRule;
import fr.smartprod.paperdms.emailimport.service.dto.ImportRuleDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ImportRule} and its DTO {@link ImportRuleDTO}.
 */
@Mapper(componentModel = "spring")
public interface ImportRuleMapper extends EntityMapper<ImportRuleDTO, ImportRule> {}
