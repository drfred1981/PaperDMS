package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AITagPrediction;
import fr.smartprod.paperdms.ai.repository.AITagPredictionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AITagPrediction} entity.
 */
public interface AITagPredictionSearchRepository
    extends ElasticsearchRepository<AITagPrediction, Long>, AITagPredictionSearchRepositoryInternal {}

interface AITagPredictionSearchRepositoryInternal {
    Page<AITagPrediction> search(String query, Pageable pageable);

    Page<AITagPrediction> search(Query query);

    @Async
    void index(AITagPrediction entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AITagPredictionSearchRepositoryInternalImpl implements AITagPredictionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AITagPredictionRepository repository;

    AITagPredictionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AITagPredictionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AITagPrediction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AITagPrediction> search(Query query) {
        SearchHits<AITagPrediction> searchHits = elasticsearchTemplate.search(query, AITagPrediction.class);
        List<AITagPrediction> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AITagPrediction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AITagPrediction.class);
    }
}
