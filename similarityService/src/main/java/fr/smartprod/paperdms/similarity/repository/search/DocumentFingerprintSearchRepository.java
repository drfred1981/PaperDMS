package fr.smartprod.paperdms.similarity.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.similarity.domain.DocumentFingerprint;
import fr.smartprod.paperdms.similarity.repository.DocumentFingerprintRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentFingerprint} entity.
 */
public interface DocumentFingerprintSearchRepository
    extends ElasticsearchRepository<DocumentFingerprint, Long>, DocumentFingerprintSearchRepositoryInternal {}

interface DocumentFingerprintSearchRepositoryInternal {
    Page<DocumentFingerprint> search(String query, Pageable pageable);

    Page<DocumentFingerprint> search(Query query);

    @Async
    void index(DocumentFingerprint entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentFingerprintSearchRepositoryInternalImpl implements DocumentFingerprintSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentFingerprintRepository repository;

    DocumentFingerprintSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentFingerprintRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentFingerprint> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentFingerprint> search(Query query) {
        SearchHits<DocumentFingerprint> searchHits = elasticsearchTemplate.search(query, DocumentFingerprint.class);
        List<DocumentFingerprint> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentFingerprint entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentFingerprint.class);
    }
}
