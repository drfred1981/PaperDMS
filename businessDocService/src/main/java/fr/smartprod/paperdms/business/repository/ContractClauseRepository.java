package fr.smartprod.paperdms.business.repository;

import fr.smartprod.paperdms.business.domain.ContractClause;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ContractClause entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContractClauseRepository extends JpaRepository<ContractClause, Long> {}
