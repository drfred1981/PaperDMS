package com.ged.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.ged.ocr.domain.ExtractedText;
import com.ged.ocr.repository.ExtractedTextRepository;
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
 * Spring Data Elasticsearch repository for the {@link ExtractedText} entity.
 */
public interface ExtractedTextSearchRepository
    extends ElasticsearchRepository<ExtractedText, Long>, ExtractedTextSearchRepositoryInternal {}

interface ExtractedTextSearchRepositoryInternal {
    Page<ExtractedText> search(String query, Pageable pageable);

    Page<ExtractedText> search(Query query);

    @Async
    void index(ExtractedText entity);

    @Async
    void deleteFromIndexById(Long id);
}

class ExtractedTextSearchRepositoryInternalImpl implements ExtractedTextSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ExtractedTextRepository repository;

    ExtractedTextSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, ExtractedTextRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<ExtractedText> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<ExtractedText> search(Query query) {
        SearchHits<ExtractedText> searchHits = elasticsearchTemplate.search(query, ExtractedText.class);
        List<ExtractedText> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(ExtractedText entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), ExtractedText.class);
    }
}
