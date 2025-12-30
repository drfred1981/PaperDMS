package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaBookmark;
import fr.smartprod.paperdms.document.repository.MetaBookmarkRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaBookmark} entity.
 */
public interface MetaBookmarkSearchRepository extends ElasticsearchRepository<MetaBookmark, Long>, MetaBookmarkSearchRepositoryInternal {}

interface MetaBookmarkSearchRepositoryInternal {
    Page<MetaBookmark> search(String query, Pageable pageable);

    Page<MetaBookmark> search(Query query);

    @Async
    void index(MetaBookmark entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaBookmarkSearchRepositoryInternalImpl implements MetaBookmarkSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaBookmarkRepository repository;

    MetaBookmarkSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaBookmarkRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaBookmark> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaBookmark> search(Query query) {
        SearchHits<MetaBookmark> searchHits = elasticsearchTemplate.search(query, MetaBookmark.class);
        List<MetaBookmark> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaBookmark entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaBookmark.class);
    }
}
