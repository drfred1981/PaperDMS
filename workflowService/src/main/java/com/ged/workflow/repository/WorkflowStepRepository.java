package com.ged.workflow.repository;

import com.ged.workflow.domain.WorkflowStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkflowStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowStepRepository extends JpaRepository<WorkflowStep, Long> {}
