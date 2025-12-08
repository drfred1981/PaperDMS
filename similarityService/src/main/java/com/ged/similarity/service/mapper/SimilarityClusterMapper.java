package com.ged.similarity.service.mapper;

import com.ged.similarity.domain.SimilarityCluster;
import com.ged.similarity.service.dto.SimilarityClusterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityCluster} and its DTO {@link SimilarityClusterDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityClusterMapper extends EntityMapper<SimilarityClusterDTO, SimilarityCluster> {}
