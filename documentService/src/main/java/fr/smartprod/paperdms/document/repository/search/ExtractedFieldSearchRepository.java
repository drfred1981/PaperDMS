package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.ExtractedField;
import fr.smartprod.paperdms.document.repository.ExtractedFieldRepository;
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
 * Spring Data Elasticsearch repository for the {@link ExtractedField} entity.
 */
public interface ExtractedFieldSearchRepository
    extends ElasticsearchRepository<ExtractedField, Long>, ExtractedFieldSearchRepositoryInternal {}

interface ExtractedFieldSearchRepositoryInternal {
    Page<ExtractedField> search(String query, Pageable pageable);

    Page<ExtractedField> search(Query query);

    @Async
    void index(ExtractedField entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ExtractedFieldSearchRepositoryInternalImpl implements ExtractedFieldSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ExtractedFieldRepository repository;

    ExtractedFieldSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ExtractedFieldRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ExtractedField> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ExtractedField> search(Query query) {
        SearchHits<ExtractedField> searchHits = elasticsearchTemplate.search(query, ExtractedField.class);
        List<ExtractedField> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ExtractedField entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ExtractedField.class);
    }
}
