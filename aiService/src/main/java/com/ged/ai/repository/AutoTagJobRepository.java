package com.ged.ai.repository;

import com.ged.ai.domain.AutoTagJob;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the AutoTagJob entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AutoTagJobRepository extends JpaRepository<AutoTagJob, Long>, JpaSpecificationExecutor<AutoTagJob> {}
