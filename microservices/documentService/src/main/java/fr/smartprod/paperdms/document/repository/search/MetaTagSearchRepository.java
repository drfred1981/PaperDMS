package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaTag;
import fr.smartprod.paperdms.document.repository.MetaTagRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaTag} entity.
 */
public interface MetaTagSearchRepository extends ElasticsearchRepository<MetaTag, Long>, MetaTagSearchRepositoryInternal {}

interface MetaTagSearchRepositoryInternal {
    Page<MetaTag> search(String query, Pageable pageable);

    Page<MetaTag> search(Query query);

    @Async
    void index(MetaTag entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaTagSearchRepositoryInternalImpl implements MetaTagSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaTagRepository repository;

    MetaTagSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaTagRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaTag> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaTag> search(Query query) {
        SearchHits<MetaTag> searchHits = elasticsearchTemplate.search(query, MetaTag.class);
        List<MetaTag> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaTag entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaTag.class);
    }
}
