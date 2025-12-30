package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.AILanguageDetection;
import fr.smartprod.paperdms.ai.repository.AILanguageDetectionRepository;
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
 * Spring Data Elasticsearch repository for the {@link AILanguageDetection} entity.
 */
public interface AILanguageDetectionSearchRepository
    extends ElasticsearchRepository<AILanguageDetection, Long>, AILanguageDetectionSearchRepositoryInternal {}

interface AILanguageDetectionSearchRepositoryInternal {
    Page<AILanguageDetection> search(String query, Pageable pageable);

    Page<AILanguageDetection> search(Query query);

    @Async
    void index(AILanguageDetection entity);

    @Async
    void deleteFromIndexById(Long id);
}

class AILanguageDetectionSearchRepositoryInternalImpl implements AILanguageDetectionSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AILanguageDetectionRepository repository;

    AILanguageDetectionSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, AILanguageDetectionRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<AILanguageDetection> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<AILanguageDetection> search(Query query) {
        SearchHits<AILanguageDetection> searchHits = elasticsearchTemplate.search(query, AILanguageDetection.class);
        List<AILanguageDetection> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(AILanguageDetection entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), AILanguageDetection.class);
    }
}
