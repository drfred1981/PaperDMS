package fr.smartprod.paperdms.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ocr.domain.OcrComparison;
import fr.smartprod.paperdms.ocr.repository.OcrComparisonRepository;
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
 * Spring Data Elasticsearch repository for the {@link OcrComparison} entity.
 */
public interface OcrComparisonSearchRepository
    extends ElasticsearchRepository<OcrComparison, Long>, OcrComparisonSearchRepositoryInternal {}

interface OcrComparisonSearchRepositoryInternal {
    Page<OcrComparison> search(String query, Pageable pageable);

    Page<OcrComparison> search(Query query);

    @Async
    void index(OcrComparison entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OcrComparisonSearchRepositoryInternalImpl implements OcrComparisonSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OcrComparisonRepository repository;

    OcrComparisonSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OcrComparisonRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OcrComparison> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OcrComparison> search(Query query) {
        SearchHits<OcrComparison> searchHits = elasticsearchTemplate.search(query, OcrComparison.class);
        List<OcrComparison> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OcrComparison entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OcrComparison.class);
    }
}
