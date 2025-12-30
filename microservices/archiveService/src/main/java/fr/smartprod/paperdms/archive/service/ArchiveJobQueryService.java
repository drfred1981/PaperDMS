package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.domain.*; // for static metamodels
import fr.smartprod.paperdms.archive.domain.ArchiveJob;
import fr.smartprod.paperdms.archive.repository.ArchiveJobRepository;
import fr.smartprod.paperdms.archive.service.criteria.ArchiveJobCriteria;
import fr.smartprod.paperdms.archive.service.dto.ArchiveJobDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveJobMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link ArchiveJob} entities in the database.
 * The main input is a {@link ArchiveJobCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArchiveJobDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArchiveJobQueryService extends QueryService<ArchiveJob> {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveJobQueryService.class);

    private final ArchiveJobRepository archiveJobRepository;

    private final ArchiveJobMapper archiveJobMapper;

    public ArchiveJobQueryService(ArchiveJobRepository archiveJobRepository, ArchiveJobMapper archiveJobMapper) {
        this.archiveJobRepository = archiveJobRepository;
        this.archiveJobMapper = archiveJobMapper;
    }

    /**
     * Return a {@link Page} of {@link ArchiveJobDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArchiveJobDTO> findByCriteria(ArchiveJobCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArchiveJob> specification = createSpecification(criteria);
        return archiveJobRepository.findAll(specification, page).map(archiveJobMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArchiveJobCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ArchiveJob> specification = createSpecification(criteria);
        return archiveJobRepository.count(specification);
    }

    /**
     * Function to convert {@link ArchiveJobCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArchiveJob> createSpecification(ArchiveJobCriteria criteria) {
        Specification<ArchiveJob> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ArchiveJob_.id),
                buildStringSpecification(criteria.getName(), ArchiveJob_.name),
                buildSpecification(criteria.getArchiveFormat(), ArchiveJob_.archiveFormat),
                buildRangeSpecification(criteria.getCompressionLevel(), ArchiveJob_.compressionLevel),
                buildSpecification(criteria.getEncryptionEnabled(), ArchiveJob_.encryptionEnabled),
                buildStringSpecification(criteria.getEncryptionAlgorithm(), ArchiveJob_.encryptionAlgorithm),
                buildStringSpecification(criteria.getPassword(), ArchiveJob_.password),
                buildStringSpecification(criteria.gets3ArchiveKey(), ArchiveJob_.s3ArchiveKey),
                buildStringSpecification(criteria.getArchiveSha256(), ArchiveJob_.archiveSha256),
                buildRangeSpecification(criteria.getArchiveSize(), ArchiveJob_.archiveSize),
                buildRangeSpecification(criteria.getDocumentCount(), ArchiveJob_.documentCount),
                buildSpecification(criteria.getStatus(), ArchiveJob_.status),
                buildRangeSpecification(criteria.getStartDate(), ArchiveJob_.startDate),
                buildRangeSpecification(criteria.getEndDate(), ArchiveJob_.endDate),
                buildStringSpecification(criteria.getCreatedBy(), ArchiveJob_.createdBy),
                buildRangeSpecification(criteria.getCreatedDate(), ArchiveJob_.createdDate),
                buildSpecification(criteria.getArchivesDocumentsId(), root ->
                    root.join(ArchiveJob_.archivesDocuments, JoinType.LEFT).get(ArchiveDocument_.id)
                )
            );
        }
        return specification;
    }
}
