package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.domain.ContractClause;
import fr.smartprod.paperdms.business.service.dto.ContractClauseDTO;
import fr.smartprod.paperdms.business.service.dto.ContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link ContractClause} and its DTO {@link ContractClauseDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractClauseMapper extends EntityMapper<ContractClauseDTO, ContractClause> {
    @Mapping(target = "contract", source = "contract", qualifiedByName = "contractId")
    ContractClauseDTO toDto(ContractClause s);

    @Named("contractId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    ContractDTO toDtoContractId(Contract contract);
}
