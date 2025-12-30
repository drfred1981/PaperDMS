package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentType;
import fr.smartprod.paperdms.document.repository.DocumentTypeRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentType} entity.
 */
public interface DocumentTypeSearchRepository extends ElasticsearchRepository<DocumentType, Long>, DocumentTypeSearchRepositoryInternal {}

interface DocumentTypeSearchRepositoryInternal {
    Page<DocumentType> search(String query, Pageable pageable);

    Page<DocumentType> search(Query query);

    @Async
    void index(DocumentType entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentTypeSearchRepositoryInternalImpl implements DocumentTypeSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentTypeRepository repository;

    DocumentTypeSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentTypeRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentType> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentType> search(Query query) {
        SearchHits<DocumentType> searchHits = elasticsearchTemplate.search(query, DocumentType.class);
        List<DocumentType> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentType entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentType.class);
    }
}
