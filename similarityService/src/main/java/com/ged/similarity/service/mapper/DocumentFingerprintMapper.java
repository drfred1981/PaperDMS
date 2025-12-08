package com.ged.similarity.service.mapper;

import com.ged.similarity.domain.DocumentFingerprint;
import com.ged.similarity.service.dto.DocumentFingerprintDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentFingerprint} and its DTO {@link DocumentFingerprintDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentFingerprintMapper extends EntityMapper<DocumentFingerprintDTO, DocumentFingerprint> {}
