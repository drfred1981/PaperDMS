package fr.smartprod.paperdms.ocr.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ocr.domain.OrcExtractedText;
import fr.smartprod.paperdms.ocr.repository.OrcExtractedTextRepository;
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
 * Spring Data Elasticsearch repository for the {@link OrcExtractedText} entity.
 */
public interface OrcExtractedTextSearchRepository
    extends ElasticsearchRepository<OrcExtractedText, Long>, OrcExtractedTextSearchRepositoryInternal {}

interface OrcExtractedTextSearchRepositoryInternal {
    Page<OrcExtractedText> search(String query, Pageable pageable);

    Page<OrcExtractedText> search(Query query);

    @Async
    void index(OrcExtractedText entity);

    @Async
    void deleteFromIndexById(Long id);
}

class OrcExtractedTextSearchRepositoryInternalImpl implements OrcExtractedTextSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final OrcExtractedTextRepository repository;

    OrcExtractedTextSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, OrcExtractedTextRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<OrcExtractedText> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<OrcExtractedText> search(Query query) {
        SearchHits<OrcExtractedText> searchHits = elasticsearchTemplate.search(query, OrcExtractedText.class);
        List<OrcExtractedText> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(OrcExtractedText entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), OrcExtractedText.class);
    }
}
