package fr.smartprod.paperdms.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ocr.domain.OcrJob;
import fr.smartprod.paperdms.ocr.repository.OcrJobRepository;
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
 * Spring Data Elasticsearch repository for the {@link OcrJob} entity.
 */
public interface OcrJobSearchRepository extends ElasticsearchRepository<OcrJob, Long>, OcrJobSearchRepositoryInternal {}

interface OcrJobSearchRepositoryInternal {
    Page<OcrJob> search(String query, Pageable pageable);

    Page<OcrJob> search(Query query);

    @Async
    void index(OcrJob entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OcrJobSearchRepositoryInternalImpl implements OcrJobSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OcrJobRepository repository;

    OcrJobSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OcrJobRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OcrJob> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OcrJob> search(Query query) {
        SearchHits<OcrJob> searchHits = elasticsearchTemplate.search(query, OcrJob.class);
        List<OcrJob> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OcrJob entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OcrJob.class);
    }
}
