package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentVersion;
import fr.smartprod.paperdms.document.repository.DocumentVersionRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentVersion} entity.
 */
public interface DocumentVersionSearchRepository
    extends ElasticsearchRepository<DocumentVersion, Long>, DocumentVersionSearchRepositoryInternal {}

interface DocumentVersionSearchRepositoryInternal {
    Page<DocumentVersion> search(String query, Pageable pageable);

    Page<DocumentVersion> search(Query query);

    @Async
    void index(DocumentVersion entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentVersionSearchRepositoryInternalImpl implements DocumentVersionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentVersionRepository repository;

    DocumentVersionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentVersionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentVersion> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentVersion> search(Query query) {
        SearchHits<DocumentVersion> searchHits = elasticsearchTemplate.search(query, DocumentVersion.class);
        List<DocumentVersion> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentVersion entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentVersion.class);
    }
}
