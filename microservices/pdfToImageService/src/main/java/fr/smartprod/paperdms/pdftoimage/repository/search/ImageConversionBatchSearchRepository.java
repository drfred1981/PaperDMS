package fr.smartprod.paperdms.pdftoimage.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionBatch;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionBatchRepository;
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
 * Spring Data Elasticsearch repository for the {@link ImageConversionBatch} entity.
 */
public interface ImageConversionBatchSearchRepository
    extends ElasticsearchRepository<ImageConversionBatch, Long>, ImageConversionBatchSearchRepositoryInternal {}

interface ImageConversionBatchSearchRepositoryInternal {
    Page<ImageConversionBatch> search(String query, Pageable pageable);

    Page<ImageConversionBatch> search(Query query);

    @Async
    void index(ImageConversionBatch entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ImageConversionBatchSearchRepositoryInternalImpl implements ImageConversionBatchSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ImageConversionBatchRepository repository;

    ImageConversionBatchSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ImageConversionBatchRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ImageConversionBatch> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ImageConversionBatch> search(Query query) {
        SearchHits<ImageConversionBatch> searchHits = elasticsearchTemplate.search(query, ImageConversionBatch.class);
        List<ImageConversionBatch> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ImageConversionBatch entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ImageConversionBatch.class);
    }
}
