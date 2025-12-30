package fr.smartprod.paperdms.pdftoimage.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.pdftoimage.domain.ImageGeneratedImage;
import fr.smartprod.paperdms.pdftoimage.repository.ImageGeneratedImageRepository;
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
 * Spring Data Elasticsearch repository for the {@link ImageGeneratedImage} entity.
 */
public interface ImageGeneratedImageSearchRepository
    extends ElasticsearchRepository<ImageGeneratedImage, Long>, ImageGeneratedImageSearchRepositoryInternal {}

interface ImageGeneratedImageSearchRepositoryInternal {
    Page<ImageGeneratedImage> search(String query, Pageable pageable);

    Page<ImageGeneratedImage> search(Query query);

    @Async
    void index(ImageGeneratedImage entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ImageGeneratedImageSearchRepositoryInternalImpl implements ImageGeneratedImageSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ImageGeneratedImageRepository repository;

    ImageGeneratedImageSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ImageGeneratedImageRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ImageGeneratedImage> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ImageGeneratedImage> search(Query query) {
        SearchHits<ImageGeneratedImage> searchHits = elasticsearchTemplate.search(query, ImageGeneratedImage.class);
        List<ImageGeneratedImage> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ImageGeneratedImage entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ImageGeneratedImage.class);
    }
}
