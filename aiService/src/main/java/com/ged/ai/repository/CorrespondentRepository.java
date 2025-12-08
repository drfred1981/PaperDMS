package com.ged.ai.repository;

import com.ged.ai.domain.Correspondent;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Correspondent entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CorrespondentRepository extends JpaRepository<Correspondent, Long>, JpaSpecificationExecutor<Correspondent> {}
