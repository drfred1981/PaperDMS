package fr.smartprod.paperdms.pdftoimage.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.pdftoimage.domain.ImageConversionConfig;
import fr.smartprod.paperdms.pdftoimage.repository.ImageConversionConfigRepository;
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
 * Spring Data Elasticsearch repository for the {@link ImageConversionConfig} entity.
 */
public interface ImageConversionConfigSearchRepository
    extends ElasticsearchRepository<ImageConversionConfig, Long>, ImageConversionConfigSearchRepositoryInternal {}

interface ImageConversionConfigSearchRepositoryInternal {
    Page<ImageConversionConfig> search(String query, Pageable pageable);

    Page<ImageConversionConfig> search(Query query);

    @Async
    void index(ImageConversionConfig entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ImageConversionConfigSearchRepositoryInternalImpl implements ImageConversionConfigSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ImageConversionConfigRepository repository;

    ImageConversionConfigSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        ImageConversionConfigRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ImageConversionConfig> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ImageConversionConfig> search(Query query) {
        SearchHits<ImageConversionConfig> searchHits = elasticsearchTemplate.search(query, ImageConversionConfig.class);
        List<ImageConversionConfig> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ImageConversionConfig entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ImageConversionConfig.class);
    }
}
