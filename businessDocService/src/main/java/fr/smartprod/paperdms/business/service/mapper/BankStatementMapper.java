package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankStatement} and its DTO {@link BankStatementDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankStatementMapper extends EntityMapper<BankStatementDTO, BankStatement> {}
