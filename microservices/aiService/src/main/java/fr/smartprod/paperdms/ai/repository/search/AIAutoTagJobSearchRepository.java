package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AIAutoTagJob;
import fr.smartprod.paperdms.ai.repository.AIAutoTagJobRepository;
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
 * Spring Data Elasticsearch repository for the {@link AIAutoTagJob} entity.
 */
public interface AIAutoTagJobSearchRepository extends ElasticsearchRepository<AIAutoTagJob, Long>, AIAutoTagJobSearchRepositoryInternal {}

interface AIAutoTagJobSearchRepositoryInternal {
    Page<AIAutoTagJob> search(String query, Pageable pageable);

    Page<AIAutoTagJob> search(Query query);

    @Async
    void index(AIAutoTagJob entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AIAutoTagJobSearchRepositoryInternalImpl implements AIAutoTagJobSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AIAutoTagJobRepository repository;

    AIAutoTagJobSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AIAutoTagJobRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AIAutoTagJob> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AIAutoTagJob> search(Query query) {
        SearchHits<AIAutoTagJob> searchHits = elasticsearchTemplate.search(query, AIAutoTagJob.class);
        List<AIAutoTagJob> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AIAutoTagJob entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AIAutoTagJob.class);
    }
}
