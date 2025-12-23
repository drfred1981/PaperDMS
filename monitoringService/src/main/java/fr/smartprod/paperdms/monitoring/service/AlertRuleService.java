package fr.smartprod.paperdms.monitoring.service;

import fr.smartprod.paperdms.monitoring.service.dto.AlertRuleDTO;
import java.util.Optional;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.monitoring.domain.AlertRule}.
 */
public interface AlertRuleService {
    /**
     * Save a alertRule.
     *
     * @param alertRuleDTO the entity to save.
     * @return the persisted entity.
     */
    AlertRuleDTO save(AlertRuleDTO alertRuleDTO);

    /**
     * Updates a alertRule.
     *
     * @param alertRuleDTO the entity to update.
     * @return the persisted entity.
     */
    AlertRuleDTO update(AlertRuleDTO alertRuleDTO);

    /**
     * Partially updates a alertRule.
     *
     * @param alertRuleDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<AlertRuleDTO> partialUpdate(AlertRuleDTO alertRuleDTO);

    /**
     * Get the "id" alertRule.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<AlertRuleDTO> findOne(Long id);

    /**
     * Delete the "id" alertRule.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
