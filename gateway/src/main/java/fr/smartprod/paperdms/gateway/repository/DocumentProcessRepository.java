package fr.smartprod.paperdms.gateway.repository;

import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentProcess entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentProcessRepository extends JpaRepository<DocumentProcess, Long> {}
