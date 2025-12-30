package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentAudit;
import fr.smartprod.paperdms.document.repository.DocumentAuditRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.scheduling.annotation.Async;

/**
 * Spring Data Elasticsearch repository for the {@link DocumentAudit} entity.
 */
public interface DocumentAuditSearchRepository
    extends ElasticsearchRepository<DocumentAudit, Long>, DocumentAuditSearchRepositoryInternal {}

interface DocumentAuditSearchRepositoryInternal {
    Page<DocumentAudit> search(String query, Pageable pageable);

    Page<DocumentAudit> search(Query query);

    @Async
    void index(DocumentAudit entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentAuditSearchRepositoryInternalImpl implements DocumentAuditSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentAuditRepository repository;

    DocumentAuditSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentAuditRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentAudit> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentAudit> search(Query query) {
        SearchHits<DocumentAudit> searchHits = elasticsearchTemplate.search(query, DocumentAudit.class);
        List<DocumentAudit> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentAudit entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentAudit.class);
    }
}
