package fr.smartprod.paperdms.workflow.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.workflow.domain.Workflow;
import fr.smartprod.paperdms.workflow.repository.WorkflowRepository;
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
 * Spring Data Elasticsearch repository for the {@link Workflow} entity.
 */
public interface WorkflowSearchRepository extends ElasticsearchRepository<Workflow, Long>, WorkflowSearchRepositoryInternal {}

interface WorkflowSearchRepositoryInternal {
    Page<Workflow> search(String query, Pageable pageable);

    Page<Workflow> search(Query query);

    @Async
    void index(Workflow entity);

    @Async
    void deleteFromIndexById(Long id);
}

class WorkflowSearchRepositoryInternalImpl implements WorkflowSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkflowRepository repository;

    WorkflowSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorkflowRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<Workflow> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<Workflow> search(Query query) {
        SearchHits<Workflow> searchHits = elasticsearchTemplate.search(query, Workflow.class);
        List<Workflow> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(Workflow entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), Workflow.class);
    }
}
