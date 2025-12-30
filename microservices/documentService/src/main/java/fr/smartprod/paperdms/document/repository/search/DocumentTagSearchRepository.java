package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.DocumentTag;
import fr.smartprod.paperdms.document.repository.DocumentTagRepository;
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
 * Spring Data Elasticsearch repository for the {@link DocumentTag} entity.
 */
public interface DocumentTagSearchRepository extends ElasticsearchRepository<DocumentTag, Long>, DocumentTagSearchRepositoryInternal {}

interface DocumentTagSearchRepositoryInternal {
    Page<DocumentTag> search(String query, Pageable pageable);

    Page<DocumentTag> search(Query query);

    @Async
    void index(DocumentTag entity);

    @Async
    void deleteFromIndexById(Long id);
}

class DocumentTagSearchRepositoryInternalImpl implements DocumentTagSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final DocumentTagRepository repository;

    DocumentTagSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, DocumentTagRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<DocumentTag> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<DocumentTag> search(Query query) {
        SearchHits<DocumentTag> searchHits = elasticsearchTemplate.search(query, DocumentTag.class);
        List<DocumentTag> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(DocumentTag entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), DocumentTag.class);
    }
}
