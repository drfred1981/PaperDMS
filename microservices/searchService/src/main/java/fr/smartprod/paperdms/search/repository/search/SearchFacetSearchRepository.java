package fr.smartprod.paperdms.search.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.search.domain.SearchFacet;
import fr.smartprod.paperdms.search.repository.SearchFacetRepository;
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
 * Spring Data Elasticsearch repository for the {@link SearchFacet} entity.
 */
public interface SearchFacetSearchRepository extends ElasticsearchRepository<SearchFacet, Long>, SearchFacetSearchRepositoryInternal {}

interface SearchFacetSearchRepositoryInternal {
    Page<SearchFacet> search(String query, Pageable pageable);

    Page<SearchFacet> search(Query query);

    @Async
    void index(SearchFacet entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SearchFacetSearchRepositoryInternalImpl implements SearchFacetSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SearchFacetRepository repository;

    SearchFacetSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SearchFacetRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SearchFacet> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SearchFacet> search(Query query) {
        SearchHits<SearchFacet> searchHits = elasticsearchTemplate.search(query, SearchFacet.class);
        List<SearchFacet> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SearchFacet entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SearchFacet.class);
    }
}
