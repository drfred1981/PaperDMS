package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AITypePrediction;
import fr.smartprod.paperdms.ai.repository.AITypePredictionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AITypePrediction} entity.
 */
public interface AITypePredictionSearchRepository
    extends ElasticsearchRepository<AITypePrediction, Long>, AITypePredictionSearchRepositoryInternal {}

interface AITypePredictionSearchRepositoryInternal {
    Page<AITypePrediction> search(String query, Pageable pageable);

    Page<AITypePrediction> search(Query query);

    @Async
    void index(AITypePrediction entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AITypePredictionSearchRepositoryInternalImpl implements AITypePredictionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AITypePredictionRepository repository;

    AITypePredictionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AITypePredictionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AITypePrediction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AITypePrediction> search(Query query) {
        SearchHits<AITypePrediction> searchHits = elasticsearchTemplate.search(query, AITypePrediction.class);
        List<AITypePrediction> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AITypePrediction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AITypePrediction.class);
    }
}
