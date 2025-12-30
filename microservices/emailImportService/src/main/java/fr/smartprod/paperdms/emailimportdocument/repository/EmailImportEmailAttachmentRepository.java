package fr.smartprod.paperdms.emailimportdocument.repository;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the EmailImportEmailAttachment entity.
 */
@SuppressWarnings("unused")
@Repository
public interface EmailImportEmailAttachmentRepository
    extends JpaRepository<EmailImportEmailAttachment, Long>, JpaSpecificationExecutor<EmailImportEmailAttachment> {}
