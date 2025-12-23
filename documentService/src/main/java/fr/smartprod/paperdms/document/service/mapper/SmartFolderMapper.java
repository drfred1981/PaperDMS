package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.SmartFolder;
import fr.smartprod.paperdms.document.service.dto.SmartFolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SmartFolder} and its DTO {@link SmartFolderDTO}.
 */
@Mapper(componentModel = "spring")
public interface SmartFolderMapper extends EntityMapper<SmartFolderDTO, SmartFolder> {}
