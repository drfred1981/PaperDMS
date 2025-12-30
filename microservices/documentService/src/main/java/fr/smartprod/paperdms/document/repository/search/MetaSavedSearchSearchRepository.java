package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaSavedSearch;
import fr.smartprod.paperdms.document.repository.MetaSavedSearchRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaSavedSearch} entity.
 */
public interface MetaSavedSearchSearchRepository
    extends ElasticsearchRepository<MetaSavedSearch, Long>, MetaSavedSearchSearchRepositoryInternal {}

interface MetaSavedSearchSearchRepositoryInternal {
    Page<MetaSavedSearch> search(String query, Pageable pageable);

    Page<MetaSavedSearch> search(Query query);

    @Async
    void index(MetaSavedSearch entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaSavedSearchSearchRepositoryInternalImpl implements MetaSavedSearchSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaSavedSearchRepository repository;

    MetaSavedSearchSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaSavedSearchRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaSavedSearch> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaSavedSearch> search(Query query) {
        SearchHits<MetaSavedSearch> searchHits = elasticsearchTemplate.search(query, MetaSavedSearch.class);
        List<MetaSavedSearch> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaSavedSearch entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaSavedSearch.class);
    }
}
