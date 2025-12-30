package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentFingerprintDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityDocumentFingerprint} and its DTO {@link SimilarityDocumentFingerprintDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityDocumentFingerprintMapper
    extends EntityMapper<SimilarityDocumentFingerprintDTO, SimilarityDocumentFingerprint> {}
