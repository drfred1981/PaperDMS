package fr.smartprod.paperdms.business.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.business.domain.Manual;
import fr.smartprod.paperdms.business.repository.ManualRepository;
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
 * Spring Data Elasticsearch repository for the {@link Manual} entity.
 */
public interface ManualSearchRepository extends ElasticsearchRepository<Manual, Long>, ManualSearchRepositoryInternal {}

interface ManualSearchRepositoryInternal {
    Page<Manual> search(String query, Pageable pageable);

    Page<Manual> search(Query query);

    @Async
    void index(Manual entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ManualSearchRepositoryInternalImpl implements ManualSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ManualRepository repository;

    ManualSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ManualRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Manual> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Manual> search(Query query) {
        SearchHits<Manual> searchHits = elasticsearchTemplate.search(query, Manual.class);
        List<Manual> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Manual entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Manual.class);
    }
}
