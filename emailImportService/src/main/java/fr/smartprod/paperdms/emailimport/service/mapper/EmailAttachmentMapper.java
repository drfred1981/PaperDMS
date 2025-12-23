package fr.smartprod.paperdms.emailimport.service.mapper;

import fr.smartprod.paperdms.emailimport.domain.EmailAttachment;
import fr.smartprod.paperdms.emailimport.domain.EmailImport;
import fr.smartprod.paperdms.emailimport.service.dto.EmailAttachmentDTO;
import fr.smartprod.paperdms.emailimport.service.dto.EmailImportDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link EmailAttachment} and its DTO {@link EmailAttachmentDTO}.
 */
@Mapper(componentModel = "spring")
public interface EmailAttachmentMapper extends EntityMapper<EmailAttachmentDTO, EmailAttachment> {
    @Mapping(target = "emailImport", source = "emailImport", qualifiedByName = "emailImportId")
    EmailAttachmentDTO toDto(EmailAttachment s);

    @Named("emailImportId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    EmailImportDTO toDtoEmailImportId(EmailImport emailImport);
}
