package fr.smartprod.paperdms.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ocr.domain.OcrCache;
import fr.smartprod.paperdms.ocr.repository.OcrCacheRepository;
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
 * Spring Data Elasticsearch repository for the {@link OcrCache} entity.
 */
public interface OcrCacheSearchRepository extends ElasticsearchRepository<OcrCache, Long>, OcrCacheSearchRepositoryInternal {}

interface OcrCacheSearchRepositoryInternal {
    Page<OcrCache> search(String query, Pageable pageable);

    Page<OcrCache> search(Query query);

    @Async
    void index(OcrCache entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OcrCacheSearchRepositoryInternalImpl implements OcrCacheSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OcrCacheRepository repository;

    OcrCacheSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OcrCacheRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OcrCache> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OcrCache> search(Query query) {
        SearchHits<OcrCache> searchHits = elasticsearchTemplate.search(query, OcrCache.class);
        List<OcrCache> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OcrCache entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OcrCache.class);
    }
}
