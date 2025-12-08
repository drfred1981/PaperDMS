package com.ged.search.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.ged.search.domain.SearchIndex;
import com.ged.search.repository.SearchIndexRepository;
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
 * Spring Data Elasticsearch repository for the {@link SearchIndex} entity.
 */
public interface SearchIndexSearchRepository extends ElasticsearchRepository<SearchIndex, Long>, SearchIndexSearchRepositoryInternal {}

interface SearchIndexSearchRepositoryInternal {
    Page<SearchIndex> search(String query, Pageable pageable);

    Page<SearchIndex> search(Query query);

    @Async
    void index(SearchIndex entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SearchIndexSearchRepositoryInternalImpl implements SearchIndexSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SearchIndexRepository repository;

    SearchIndexSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SearchIndexRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SearchIndex> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SearchIndex> search(Query query) {
        SearchHits<SearchIndex> searchHits = elasticsearchTemplate.search(query, SearchIndex.class);
        List<SearchIndex> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SearchIndex entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SearchIndex.class);
    }
}
