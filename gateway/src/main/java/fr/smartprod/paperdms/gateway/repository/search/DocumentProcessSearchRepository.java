package fr.smartprod.paperdms.gateway.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.gateway.domain.DocumentProcess;
import fr.smartprod.paperdms.gateway.repository.DocumentProcessRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentProcess} entity.
 */
public interface DocumentProcessSearchRepository
    extends ElasticsearchRepository<DocumentProcess, Long>, DocumentProcessSearchRepositoryInternal {}

interface DocumentProcessSearchRepositoryInternal {
    Page<DocumentProcess> search(String query, Pageable pageable);

    Page<DocumentProcess> search(Query query);

    @Async
    void index(DocumentProcess entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentProcessSearchRepositoryInternalImpl implements DocumentProcessSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentProcessRepository repository;

    DocumentProcessSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentProcessRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentProcess> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentProcess> search(Query query) {
        SearchHits<DocumentProcess> searchHits = elasticsearchTemplate.search(query, DocumentProcess.class);
        List<DocumentProcess> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentProcess entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentProcess.class);
    }
}
