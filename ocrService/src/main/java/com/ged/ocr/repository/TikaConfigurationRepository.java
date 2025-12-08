package com.ged.ocr.repository;

import com.ged.ocr.domain.TikaConfiguration;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the TikaConfiguration entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TikaConfigurationRepository extends JpaRepository<TikaConfiguration, Long> {}
