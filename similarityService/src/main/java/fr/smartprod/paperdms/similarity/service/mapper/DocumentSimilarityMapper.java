package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.DocumentSimilarity;
import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.service.dto.DocumentSimilarityDTO;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link DocumentSimilarity} and its DTO {@link DocumentSimilarityDTO}.
 */
@Mapper(componentModel = "spring")
public interface DocumentSimilarityMapper extends EntityMapper<DocumentSimilarityDTO, DocumentSimilarity> {
    @Mapping(target = "job", source = "job", qualifiedByName = "similarityJobId")
    DocumentSimilarityDTO toDto(DocumentSimilarity s);

    @Named("similarityJobId")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "id", source = "id")
    SimilarityJobDTO toDtoSimilarityJobId(SimilarityJob similarityJob);
}
