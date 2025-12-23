package fr.smartprod.paperdms.workflow.service;

import fr.smartprod.paperdms.workflow.service.dto.ApprovalHistoryDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.workflow.domain.ApprovalHistory}.
 */
public interface ApprovalHistoryService {
    /**
     * Save a approvalHistory.
     *
     * @param approvalHistoryDTO the entity to save.
     * @return the persisted entity.
     */
    ApprovalHistoryDTO save(ApprovalHistoryDTO approvalHistoryDTO);

    /**
     * Updates a approvalHistory.
     *
     * @param approvalHistoryDTO the entity to update.
     * @return the persisted entity.
     */
    ApprovalHistoryDTO update(ApprovalHistoryDTO approvalHistoryDTO);

    /**
     * Partially updates a approvalHistory.
     *
     * @param approvalHistoryDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ApprovalHistoryDTO> partialUpdate(ApprovalHistoryDTO approvalHistoryDTO);

    /**
     * Get all the approvalHistories.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ApprovalHistoryDTO> findAll(Pageable pageable);

    /**
     * Get the "id" approvalHistory.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ApprovalHistoryDTO> findOne(Long id);

    /**
     * Delete the "id" approvalHistory.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
