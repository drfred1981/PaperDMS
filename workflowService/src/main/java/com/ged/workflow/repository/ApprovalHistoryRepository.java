package com.ged.workflow.repository;

import com.ged.workflow.domain.ApprovalHistory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the ApprovalHistory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ApprovalHistoryRepository extends JpaRepository<ApprovalHistory, Long> {}
