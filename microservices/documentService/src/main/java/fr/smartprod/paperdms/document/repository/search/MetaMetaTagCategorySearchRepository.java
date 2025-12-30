package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaMetaTagCategory;
import fr.smartprod.paperdms.document.repository.MetaMetaTagCategoryRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaMetaTagCategory} entity.
 */
public interface MetaMetaTagCategorySearchRepository
    extends ElasticsearchRepository<MetaMetaTagCategory, Long>, MetaMetaTagCategorySearchRepositoryInternal {}

interface MetaMetaTagCategorySearchRepositoryInternal {
    Page<MetaMetaTagCategory> search(String query, Pageable pageable);

    Page<MetaMetaTagCategory> search(Query query);

    @Async
    void index(MetaMetaTagCategory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaMetaTagCategorySearchRepositoryInternalImpl implements MetaMetaTagCategorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaMetaTagCategoryRepository repository;

    MetaMetaTagCategorySearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaMetaTagCategoryRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaMetaTagCategory> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaMetaTagCategory> search(Query query) {
        SearchHits<MetaMetaTagCategory> searchHits = elasticsearchTemplate.search(query, MetaMetaTagCategory.class);
        List<MetaMetaTagCategory> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaMetaTagCategory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaMetaTagCategory.class);
    }
}
