package fr.smartprod.paperdms.business.service.mapper;

import fr.smartprod.paperdms.business.domain.Contract;
import fr.smartprod.paperdms.business.service.dto.ContractDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link Contract} and its DTO {@link ContractDTO}.
 */
@Mapper(componentModel = "spring")
public interface ContractMapper extends EntityMapper<ContractDTO, Contract> {}
