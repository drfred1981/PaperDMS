package com.ged.workflow.repository;

import com.ged.workflow.domain.Workflow;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Workflow entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowRepository extends JpaRepository<Workflow, Long>, JpaSpecificationExecutor<Workflow> {}
