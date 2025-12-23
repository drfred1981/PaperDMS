package fr.smartprod.paperdms.ai.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.ai.domain.Correspondent;
import fr.smartprod.paperdms.ai.repository.CorrespondentRepository;
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
 * Spring Data Elasticsearch repository for the {@link Correspondent} entity.
 */
public interface CorrespondentSearchRepository
    extends ElasticsearchRepository<Correspondent, Long>, CorrespondentSearchRepositoryInternal {}

interface CorrespondentSearchRepositoryInternal {
    Page<Correspondent> search(String query, Pageable pageable);

    Page<Correspondent> search(Query query);

    @Async
    void index(Correspondent entity);

    @Async
    void deleteFromIndexById(Long id);
}

class CorrespondentSearchRepositoryInternalImpl implements CorrespondentSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final CorrespondentRepository repository;

    CorrespondentSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, CorrespondentRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Correspondent> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Correspondent> search(Query query) {
        SearchHits<Correspondent> searchHits = elasticsearchTemplate.search(query, Correspondent.class);
        List<Correspondent> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Correspondent entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Correspondent.class);
    }
}
