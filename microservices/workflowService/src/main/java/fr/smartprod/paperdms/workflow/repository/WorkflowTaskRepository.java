package fr.smartprod.paperdms.workflow.repository;

import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkflowTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowTaskRepository extends JpaRepository<WorkflowTask, Long>, JpaSpecificationExecutor<WorkflowTask> {}
