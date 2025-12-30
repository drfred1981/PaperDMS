package fr.smartprod.paperdms.document.repository;

import fr.smartprod.paperdms.document.domain.DocumentComment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the DocumentComment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface DocumentCommentRepository extends JpaRepository<DocumentComment, Long>, JpaSpecificationExecutor<DocumentComment> {}
