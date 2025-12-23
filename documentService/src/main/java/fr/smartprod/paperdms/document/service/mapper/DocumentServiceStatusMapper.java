package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.DocumentServiceStatus;
import fr.smartprod.paperdms.document.service.dto.DocumentServiceStatusDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentServiceStatus} and its DTO {@link DocumentServiceStatusDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentServiceStatusMapper extends EntityMapper<DocumentServiceStatusDTO, DocumentServiceStatus> {}
