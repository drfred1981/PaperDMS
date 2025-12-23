package fr.smartprod.paperdms.scan.service;

import fr.smartprod.paperdms.scan.service.dto.ScannerConfigurationDTO;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Service Interface for managing {@link fr.smartprod.paperdms.scan.domain.ScannerConfiguration}.
 */
public interface ScannerConfigurationService {
    /**
     * Save a scannerConfiguration.
     *
     * @param scannerConfigurationDTO the entity to save.
     * @return the persisted entity.
     */
    ScannerConfigurationDTO save(ScannerConfigurationDTO scannerConfigurationDTO);

    /**
     * Updates a scannerConfiguration.
     *
     * @param scannerConfigurationDTO the entity to update.
     * @return the persisted entity.
     */
    ScannerConfigurationDTO update(ScannerConfigurationDTO scannerConfigurationDTO);

    /**
     * Partially updates a scannerConfiguration.
     *
     * @param scannerConfigurationDTO the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ScannerConfigurationDTO> partialUpdate(ScannerConfigurationDTO scannerConfigurationDTO);

    /**
     * Get all the scannerConfigurations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ScannerConfigurationDTO> findAll(Pageable pageable);

    /**
     * Get the "id" scannerConfiguration.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ScannerConfigurationDTO> findOne(Long id);

    /**
     * Delete the "id" scannerConfiguration.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
