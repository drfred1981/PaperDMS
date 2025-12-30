package fr.smartprod.paperdms.similarity.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentComparison;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentComparisonRepository;
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
 * Spring Data Elasticsearch repository for the {@link SimilarityDocumentComparison} entity.
 */
public interface SimilarityDocumentComparisonSearchRepository
    extends ElasticsearchRepository<SimilarityDocumentComparison, Long>, SimilarityDocumentComparisonSearchRepositoryInternal {}

interface SimilarityDocumentComparisonSearchRepositoryInternal {
    Page<SimilarityDocumentComparison> search(String query, Pageable pageable);

    Page<SimilarityDocumentComparison> search(Query query);

    @Async
    void index(SimilarityDocumentComparison entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SimilarityDocumentComparisonSearchRepositoryInternalImpl implements SimilarityDocumentComparisonSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SimilarityDocumentComparisonRepository repository;

    SimilarityDocumentComparisonSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        SimilarityDocumentComparisonRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SimilarityDocumentComparison> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SimilarityDocumentComparison> search(Query query) {
        SearchHits<SimilarityDocumentComparison> searchHits = elasticsearchTemplate.search(query, SimilarityDocumentComparison.class);
        List<SimilarityDocumentComparison> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SimilarityDocumentComparison entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SimilarityDocumentComparison.class);
    }
}
