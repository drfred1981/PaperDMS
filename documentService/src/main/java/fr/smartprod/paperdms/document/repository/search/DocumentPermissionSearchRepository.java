package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentPermission;
import fr.smartprod.paperdms.document.repository.DocumentPermissionRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentPermission} entity.
 */
public interface DocumentPermissionSearchRepository
    extends ElasticsearchRepository<DocumentPermission, Long>, DocumentPermissionSearchRepositoryInternal {}

interface DocumentPermissionSearchRepositoryInternal {
    Page<DocumentPermission> search(String query, Pageable pageable);

    Page<DocumentPermission> search(Query query);

    @Async
    void index(DocumentPermission entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentPermissionSearchRepositoryInternalImpl implements DocumentPermissionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentPermissionRepository repository;

    DocumentPermissionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentPermissionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentPermission> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentPermission> search(Query query) {
        SearchHits<DocumentPermission> searchHits = elasticsearchTemplate.search(query, DocumentPermission.class);
        List<DocumentPermission> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentPermission entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentPermission.class);
    }
}
