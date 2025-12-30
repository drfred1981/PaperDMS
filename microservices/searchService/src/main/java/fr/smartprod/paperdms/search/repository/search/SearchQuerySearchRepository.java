package fr.smartprod.paperdms.search.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.search.domain.SearchQuery;
import fr.smartprod.paperdms.search.repository.SearchQueryRepository;
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
 * Spring Data Elasticsearch repository for the {@link SearchQuery} entity.
 */
public interface SearchQuerySearchRepository extends ElasticsearchRepository<SearchQuery, Long>, SearchQuerySearchRepositoryInternal {}

interface SearchQuerySearchRepositoryInternal {
    Page<SearchQuery> search(String query, Pageable pageable);

    Page<SearchQuery> search(Query query);

    @Async
    void index(SearchQuery entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SearchQuerySearchRepositoryInternalImpl implements SearchQuerySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SearchQueryRepository repository;

    SearchQuerySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SearchQueryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SearchQuery> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SearchQuery> search(Query query) {
        SearchHits<SearchQuery> searchHits = elasticsearchTemplate.search(query, SearchQuery.class);
        List<SearchQuery> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SearchQuery entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SearchQuery.class);
    }
}
