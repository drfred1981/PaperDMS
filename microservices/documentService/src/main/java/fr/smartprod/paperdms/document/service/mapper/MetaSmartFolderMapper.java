package fr.smartprod.paperdms.document.service.mapper;

import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import fr.smartprod.paperdms.document.service.dto.MetaSmartFolderDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link MetaSmartFolder} and its DTO {@link MetaSmartFolderDTO}.
 */
@Mapper(componentModel = "spring")
public interface MetaSmartFolderMapper extends EntityMapper<MetaSmartFolderDTO, MetaSmartFolder> {}
