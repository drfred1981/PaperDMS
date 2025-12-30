package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentTypeField;
import fr.smartprod.paperdms.document.repository.DocumentTypeFieldRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentTypeField} entity.
 */
public interface DocumentTypeFieldSearchRepository
    extends ElasticsearchRepository<DocumentTypeField, Long>, DocumentTypeFieldSearchRepositoryInternal {}

interface DocumentTypeFieldSearchRepositoryInternal {
    Page<DocumentTypeField> search(String query, Pageable pageable);

    Page<DocumentTypeField> search(Query query);

    @Async
    void index(DocumentTypeField entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentTypeFieldSearchRepositoryInternalImpl implements DocumentTypeFieldSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentTypeFieldRepository repository;

    DocumentTypeFieldSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentTypeFieldRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentTypeField> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentTypeField> search(Query query) {
        SearchHits<DocumentTypeField> searchHits = elasticsearchTemplate.search(query, DocumentTypeField.class);
        List<DocumentTypeField> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentTypeField entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentTypeField.class);
    }
}
