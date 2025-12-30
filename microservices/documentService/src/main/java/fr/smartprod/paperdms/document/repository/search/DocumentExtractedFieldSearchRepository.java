package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentExtractedField;
import fr.smartprod.paperdms.document.repository.DocumentExtractedFieldRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentExtractedField} entity.
 */
public interface DocumentExtractedFieldSearchRepository
    extends ElasticsearchRepository<DocumentExtractedField, Long>, DocumentExtractedFieldSearchRepositoryInternal {}

interface DocumentExtractedFieldSearchRepositoryInternal {
    Page<DocumentExtractedField> search(String query, Pageable pageable);

    Page<DocumentExtractedField> search(Query query);

    @Async
    void index(DocumentExtractedField entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentExtractedFieldSearchRepositoryInternalImpl implements DocumentExtractedFieldSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentExtractedFieldRepository repository;

    DocumentExtractedFieldSearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        DocumentExtractedFieldRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentExtractedField> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentExtractedField> search(Query query) {
        SearchHits<DocumentExtractedField> searchHits = elasticsearchTemplate.search(query, DocumentExtractedField.class);
        List<DocumentExtractedField> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentExtractedField entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentExtractedField.class);
    }
}
