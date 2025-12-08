package com.ged.workflow.repository;

import com.ged.workflow.domain.WorkflowInstance;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkflowInstance entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowInstanceRepository extends JpaRepository<WorkflowInstance, Long>, JpaSpecificationExecutor<WorkflowInstance> {}
