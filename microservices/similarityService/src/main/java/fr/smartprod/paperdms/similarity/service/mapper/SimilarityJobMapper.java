package fr.smartprod.paperdms.similarity.service.mapper;

import fr.smartprod.paperdms.similarity.domain.SimilarityJob;
import fr.smartprod.paperdms.similarity.service.dto.SimilarityJobDTO;
import org.mapstruct.*;

/**
 * Mapper for the entity {@link SimilarityJob} and its DTO {@link SimilarityJobDTO}.
 */
@Mapper(componentModel = "spring")
public interface SimilarityJobMapper extends EntityMapper<SimilarityJobDTO, SimilarityJob> {}
