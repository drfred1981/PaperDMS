package fr.smartprod.paperdms.workflow.repository;

import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the WorkflowApprovalHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WorkflowApprovalHistoryRepository
    extends JpaRepository<WorkflowApprovalHistory, Long>, JpaSpecificationExecutor<WorkflowApprovalHistory> {}
