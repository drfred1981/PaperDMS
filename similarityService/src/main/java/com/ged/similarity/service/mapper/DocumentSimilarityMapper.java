package com.ged.similarity.service.mapper;

import com.ged.similarity.domain.DocumentSimilarity;
import com.ged.similarity.domain.SimilarityJob;
import com.ged.similarity.service.dto.DocumentSimilarityDTO;
import com.ged.similarity.service.dto.SimilarityJobDTO;
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
