package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentComment;
import fr.smartprod.paperdms.document.repository.DocumentCommentRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentComment} entity.
 */
public interface DocumentCommentSearchRepository
    extends ElasticsearchRepository<DocumentComment, Long>, DocumentCommentSearchRepositoryInternal {}

interface DocumentCommentSearchRepositoryInternal {
    Page<DocumentComment> search(String query, Pageable pageable);

    Page<DocumentComment> search(Query query);

    @Async
    void index(DocumentComment entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentCommentSearchRepositoryInternalImpl implements DocumentCommentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentCommentRepository repository;

    DocumentCommentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentCommentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentComment> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentComment> search(Query query) {
        SearchHits<DocumentComment> searchHits = elasticsearchTemplate.search(query, DocumentComment.class);
        List<DocumentComment> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentComment entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentComment.class);
    }
}
