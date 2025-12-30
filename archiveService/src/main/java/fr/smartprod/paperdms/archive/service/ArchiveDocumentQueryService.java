package fr.smartprod.paperdms.archive.service;

import fr.smartprod.paperdms.archive.domain.*; // for static metamodels
import fr.smartprod.paperdms.archive.domain.ArchiveDocument;
import fr.smartprod.paperdms.archive.repository.ArchiveDocumentRepository;
import fr.smartprod.paperdms.archive.service.criteria.ArchiveDocumentCriteria;
import fr.smartprod.paperdms.archive.service.dto.ArchiveDocumentDTO;
import fr.smartprod.paperdms.archive.service.mapper.ArchiveDocumentMapper;
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
 * Service for executing complex queries for {@link ArchiveDocument} entities in the database.
 * The main input is a {@link ArchiveDocumentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link ArchiveDocumentDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class ArchiveDocumentQueryService extends QueryService<ArchiveDocument> {

    private static final Logger LOG = LoggerFactory.getLogger(ArchiveDocumentQueryService.class);

    private final ArchiveDocumentRepository archiveDocumentRepository;

    private final ArchiveDocumentMapper archiveDocumentMapper;

    public ArchiveDocumentQueryService(ArchiveDocumentRepository archiveDocumentRepository, ArchiveDocumentMapper archiveDocumentMapper) {
        this.archiveDocumentRepository = archiveDocumentRepository;
        this.archiveDocumentMapper = archiveDocumentMapper;
    }

    /**
     * Return a {@link Page} of {@link ArchiveDocumentDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<ArchiveDocumentDTO> findByCriteria(ArchiveDocumentCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<ArchiveDocument> specification = createSpecification(criteria);
        return archiveDocumentRepository.findAll(specification, page).map(archiveDocumentMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(ArchiveDocumentCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<ArchiveDocument> specification = createSpecification(criteria);
        return archiveDocumentRepository.count(specification);
    }

    /**
     * Function to convert {@link ArchiveDocumentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<ArchiveDocument> createSpecification(ArchiveDocumentCriteria criteria) {
        Specification<ArchiveDocument> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            specification = Specification.allOf(
                Boolean.TRUE.equals(criteria.getDistinct()) ? distinct(criteria.getDistinct()) : null,
                buildRangeSpecification(criteria.getId(), ArchiveDocument_.id),
                buildStringSpecification(criteria.getDocumentSha256(), ArchiveDocument_.documentSha256),
                buildStringSpecification(criteria.getOriginalPath(), ArchiveDocument_.originalPath),
                buildStringSpecification(criteria.getArchivePath(), ArchiveDocument_.archivePath),
                buildRangeSpecification(criteria.getFileSize(), ArchiveDocument_.fileSize),
                buildRangeSpecification(criteria.getAddedDate(), ArchiveDocument_.addedDate),
                buildSpecification(criteria.getArchiveJobId(), root ->
                    root.join(ArchiveDocument_.archiveJob, JoinType.LEFT).get(ArchiveJob_.id)
                )
            );
        }
        return specification;
    }
}
