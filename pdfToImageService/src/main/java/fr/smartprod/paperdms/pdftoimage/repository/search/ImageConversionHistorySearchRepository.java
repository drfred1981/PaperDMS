package fr.smartprod.paperdms.pdftoimage.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionHistory;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionHistoryRepository;
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
 * Spring Data Elasticsearch repository for the {@link ImageConversionHistory} entity.
 */
public interface ImageConversionHistorySearchRepository
    extends ElasticsearchRepository<ImageConversionHistory, Long>, ImageConversionHistorySearchRepositoryInternal {}

interface ImageConversionHistorySearchRepositoryInternal {
    Page<ImageConversionHistory> search(String query, Pageable pageable);

    Page<ImageConversionHistory> search(Query query);

    @Async
    void index(ImageConversionHistory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ImageConversionHistorySearchRepositoryInternalImpl implements ImageConversionHistorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ImageConversionHistoryRepository repository;

    ImageConversionHistorySearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ImageConversionHistoryRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ImageConversionHistory> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ImageConversionHistory> search(Query query) {
        SearchHits<ImageConversionHistory> searchHits = elasticsearchTemplate.search(query, ImageConversionHistory.class);
        List<ImageConversionHistory> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ImageConversionHistory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ImageConversionHistory.class);
    }
}
