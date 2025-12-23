package fr.smartprod.paperdms.business.repository;

import fr.smartprod.paperdms.business.domain.BankStatement;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the BankStatement entity.
 */
@SuppressWarnings("unused")
@Repository
public interface BankStatementRepository extends JpaRepository<BankStatement, Long>, JpaSpecificationExecutor<BankStatement> {}
