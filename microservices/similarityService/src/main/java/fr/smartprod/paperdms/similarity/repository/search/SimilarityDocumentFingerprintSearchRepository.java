package fr.smartprod.paperdms.similarity.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.similarity.domain.SimilarityDocumentFingerprint;
import fr.smartprod.paperdms.similarity.repository.SimilarityDocumentFingerprintRepository;
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
 * Spring Data Elasticsearch repository for the {@link SimilarityDocumentFingerprint} entity.
 */
public interface SimilarityDocumentFingerprintSearchRepository
    extends ElasticsearchRepository<SimilarityDocumentFingerprint, Long>, SimilarityDocumentFingerprintSearchRepositoryInternal {}

interface SimilarityDocumentFingerprintSearchRepositoryInternal {
    Page<SimilarityDocumentFingerprint> search(String query, Pageable pageable);

    Page<SimilarityDocumentFingerprint> search(Query query);

    @Async
    void index(SimilarityDocumentFingerprint entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SimilarityDocumentFingerprintSearchRepositoryInternalImpl implements SimilarityDocumentFingerprintSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SimilarityDocumentFingerprintRepository repository;

    SimilarityDocumentFingerprintSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        SimilarityDocumentFingerprintRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SimilarityDocumentFingerprint> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SimilarityDocumentFingerprint> search(Query query) {
        SearchHits<SimilarityDocumentFingerprint> searchHits = elasticsearchTemplate.search(query, SimilarityDocumentFingerprint.class);
        List<SimilarityDocumentFingerprint> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SimilarityDocumentFingerprint entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SimilarityDocumentFingerprint.class);
    }
}
