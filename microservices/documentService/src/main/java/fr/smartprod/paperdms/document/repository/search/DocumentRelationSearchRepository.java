package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentRelation;
import fr.smartprod.paperdms.document.repository.DocumentRelationRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentRelation} entity.
 */
public interface DocumentRelationSearchRepository
    extends ElasticsearchRepository<DocumentRelation, Long>, DocumentRelationSearchRepositoryInternal {}

interface DocumentRelationSearchRepositoryInternal {
    Page<DocumentRelation> search(String query, Pageable pageable);

    Page<DocumentRelation> search(Query query);

    @Async
    void index(DocumentRelation entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentRelationSearchRepositoryInternalImpl implements DocumentRelationSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentRelationRepository repository;

    DocumentRelationSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentRelationRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentRelation> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentRelation> search(Query query) {
        SearchHits<DocumentRelation> searchHits = elasticsearchTemplate.search(query, DocumentRelation.class);
        List<DocumentRelation> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentRelation entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentRelation.class);
    }
}
