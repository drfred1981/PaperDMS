package fr.smartprod.paperdms.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ocr.domain.OcrResult;
import fr.smartprod.paperdms.ocr.repository.OcrResultRepository;
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
 * Spring Data Elasticsearch repository for the {@link OcrResult} entity.
 */
public interface OcrResultSearchRepository extends ElasticsearchRepository<OcrResult, Long>, OcrResultSearchRepositoryInternal {}

interface OcrResultSearchRepositoryInternal {
    Page<OcrResult> search(String query, Pageable pageable);

    Page<OcrResult> search(Query query);

    @Async
    void index(OcrResult entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OcrResultSearchRepositoryInternalImpl implements OcrResultSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OcrResultRepository repository;

    OcrResultSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OcrResultRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OcrResult> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OcrResult> search(Query query) {
        SearchHits<OcrResult> searchHits = elasticsearchTemplate.search(query, OcrResult.class);
        List<OcrResult> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OcrResult entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OcrResult.class);
    }
}
