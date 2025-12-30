package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AICache;
import fr.smartprod.paperdms.ai.repository.AICacheRepository;
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
 * Spring Data Elasticsearch repository for the {@link AICache} entity.
 */
public interface AICacheSearchRepository extends ElasticsearchRepository<AICache, Long>, AICacheSearchRepositoryInternal {}

interface AICacheSearchRepositoryInternal {
    Page<AICache> search(String query, Pageable pageable);

    Page<AICache> search(Query query);

    @Async
    void index(AICache entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AICacheSearchRepositoryInternalImpl implements AICacheSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AICacheRepository repository;

    AICacheSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AICacheRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AICache> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AICache> search(Query query) {
        SearchHits<AICache> searchHits = elasticsearchTemplate.search(query, AICache.class);
        List<AICache> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AICache entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AICache.class);
    }
}
