package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaPermissionGroup;
import fr.smartprod.paperdms.document.repository.MetaPermissionGroupRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaPermissionGroup} entity.
 */
public interface MetaPermissionGroupSearchRepository
    extends ElasticsearchRepository<MetaPermissionGroup, Long>, MetaPermissionGroupSearchRepositoryInternal {}

interface MetaPermissionGroupSearchRepositoryInternal {
    Page<MetaPermissionGroup> search(String query, Pageable pageable);

    Page<MetaPermissionGroup> search(Query query);

    @Async
    void index(MetaPermissionGroup entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaPermissionGroupSearchRepositoryInternalImpl implements MetaPermissionGroupSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaPermissionGroupRepository repository;

    MetaPermissionGroupSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaPermissionGroupRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaPermissionGroup> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaPermissionGroup> search(Query query) {
        SearchHits<MetaPermissionGroup> searchHits = elasticsearchTemplate.search(query, MetaPermissionGroup.class);
        List<MetaPermissionGroup> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaPermissionGroup entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaPermissionGroup.class);
    }
}
