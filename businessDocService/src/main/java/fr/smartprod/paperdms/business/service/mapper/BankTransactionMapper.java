package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.BankStatement;
import fr.smartprod.paperdms.business.domain.BankTransaction;
import fr.smartprod.paperdms.business.service.dto.BankStatementDTO;
import fr.smartprod.paperdms.business.service.dto.BankTransactionDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link BankTransaction} and its DTO {@link BankTransactionDTO}.
 */
@Mapper(componentModel = "spring")
public interface BankTransactionMapper extends EntityMapper<BankTransactionDTO, BankTransaction> {
    @Mapping(target = "statement", source = "statement", qualifiedByName = "bankStatementId")
    BankTransactionDTO toDto(BankTransaction s);

    @Named("bankStatementId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    BankStatementDTO toDtoBankStatementId(BankStatement bankStatement);
}
