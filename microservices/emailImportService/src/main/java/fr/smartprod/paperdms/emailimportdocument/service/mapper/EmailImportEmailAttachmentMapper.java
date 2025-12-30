package fr.smartprod.paperdms.emailimportdocument.service.mapper;

import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportDocument;
import fr.smartprod.paperdms.emailimportdocument.domain.EmailImportEmailAttachment;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportDocumentDTO;
import fr.smartprod.paperdms.emailimportdocument.service.dto.EmailImportEmailAttachmentDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailImportEmailAttachment} and its DTO {@link EmailImportEmailAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailImportEmailAttachmentMapper extends EntityMapper<EmailImportEmailAttachmentDTO, EmailImportEmailAttachment> {
    @Mapping(target = "emailImportDocument", source = "emailImportDocument", qualifiedByName = "emailImportDocumentId")
    EmailImportEmailAttachmentDTO toDto(EmailImportEmailAttachment s);

    @Named("emailImportDocumentId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmailImportDocumentDTO toDtoEmailImportDocumentId(EmailImportDocument emailImportDocument);
}
