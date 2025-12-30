package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentStatistics;
import fr.smartprod.paperdms.document.repository.DocumentStatisticsRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentStatistics} entity.
 */
public interface DocumentStatisticsSearchRepository
    extends ElasticsearchRepository<DocumentStatistics, Long>, DocumentStatisticsSearchRepositoryInternal {}

interface DocumentStatisticsSearchRepositoryInternal {
    Page<DocumentStatistics> search(String query, Pageable pageable);

    Page<DocumentStatistics> search(Query query);

    @Async
    void index(DocumentStatistics entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentStatisticsSearchRepositoryInternalImpl implements DocumentStatisticsSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentStatisticsRepository repository;

    DocumentStatisticsSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentStatisticsRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentStatistics> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentStatistics> search(Query query) {
        SearchHits<DocumentStatistics> searchHits = elasticsearchTemplate.search(query, DocumentStatistics.class);
        List<DocumentStatistics> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentStatistics entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentStatistics.class);
    }
}
