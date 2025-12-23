package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.DocumentFingerprint;
import fr.smartprod.paperdms.similarity.service.dto.DocumentFingerprintDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentFingerprint} and its DTO {@link DocumentFingerprintDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentFingerprintMapper extends EntityMapper<DocumentFingerprintDTO, DocumentFingerprint> {}
