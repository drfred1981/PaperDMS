package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityDocumentComparisonDTO;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityDocumentComparison} and its DTO {@link SimilarityDocumentComparisonDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityDocumentComparisonMapper extends EntityMapper<SimilarityDocumentComparisonDTO, SimilarityDocumentComparison> {
    @Mapping(target = "job", source = "job", qualifiedByName = "similarityJobId")
    SimilarityDocumentComparisonDTO toDto(SimilarityDocumentComparison s);

    @Named("similarityJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SimilarityJobDTO toDtoSimilarityJobId(SimilarityJob similarityJob);
}
