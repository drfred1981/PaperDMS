package fr.smartprod.paperdms.search.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.search.domain.SearchSemantic;
import fr.smartprod.paperdms.search.repository.SearchSemanticRepository;
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
 * Spring Data Elasticsearch repository for the {@link SearchSemantic} entity.
 */
public interface SearchSemanticSearchRepository
    extends ElasticsearchRepository<SearchSemantic, Long>, SearchSemanticSearchRepositoryInternal {}

interface SearchSemanticSearchRepositoryInternal {
    Page<SearchSemantic> search(String query, Pageable pageable);

    Page<SearchSemantic> search(Query query);

    @Async
    void index(SearchSemantic entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SearchSemanticSearchRepositoryInternalImpl implements SearchSemanticSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SearchSemanticRepository repository;

    SearchSemanticSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SearchSemanticRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SearchSemantic> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SearchSemantic> search(Query query) {
        SearchHits<SearchSemantic> searchHits = elasticsearchTemplate.search(query, SearchSemantic.class);
        List<SearchSemantic> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SearchSemantic entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SearchSemantic.class);
    }
}
