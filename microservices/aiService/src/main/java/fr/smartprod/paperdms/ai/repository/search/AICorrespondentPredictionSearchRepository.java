package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AICorrespondentPrediction;
import fr.smartprod.paperdms.ai.repository.AICorrespondentPredictionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AICorrespondentPrediction} entity.
 */
public interface AICorrespondentPredictionSearchRepository
    extends ElasticsearchRepository<AICorrespondentPrediction, Long>, AICorrespondentPredictionSearchRepositoryInternal {}

interface AICorrespondentPredictionSearchRepositoryInternal {
    Page<AICorrespondentPrediction> search(String query, Pageable pageable);

    Page<AICorrespondentPrediction> search(Query query);

    @Async
    void index(AICorrespondentPrediction entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AICorrespondentPredictionSearchRepositoryInternalImpl implements AICorrespondentPredictionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AICorrespondentPredictionRepository repository;

    AICorrespondentPredictionSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        AICorrespondentPredictionRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AICorrespondentPrediction> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AICorrespondentPrediction> search(Query query) {
        SearchHits<AICorrespondentPrediction> searchHits = elasticsearchTemplate.search(query, AICorrespondentPrediction.class);
        List<AICorrespondentPrediction> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AICorrespondentPrediction entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AICorrespondentPrediction.class);
    }
}
