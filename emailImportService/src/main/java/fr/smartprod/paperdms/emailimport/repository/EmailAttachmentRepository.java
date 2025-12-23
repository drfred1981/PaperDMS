package fr.smartprod.paperdms.emailimport.repository;

import fr.smartprod.paperdms.emailimport.domain.EmailAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailAttachmentRepository extends JpaRepository<EmailAttachment, Long> {}
