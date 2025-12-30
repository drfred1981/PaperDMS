package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityClusterDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityCluster} and its DTO {@link SimilarityClusterDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityClusterMapper extends EntityMapper<SimilarityClusterDTO, SimilarityCluster> {}
