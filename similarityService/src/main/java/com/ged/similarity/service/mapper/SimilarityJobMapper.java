package com.ged.similarity.service.mapper;

import com.ged.similarity.domain.SimilarityJob;
import com.ged.similarity.service.dto.SimilarityJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityJob} and its DTO {@link SimilarityJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityJobMapper extends EntityMapper<SimilarityJobDTO, SimilarityJob> {}
