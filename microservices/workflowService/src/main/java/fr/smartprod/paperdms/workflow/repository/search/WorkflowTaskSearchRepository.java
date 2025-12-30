package fr.smartprod.paperdms.workflow.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.workflow.domain.WorkflowTask;
import fr.smartprod.paperdms.workflow.repository.WorkflowTaskRepository;
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
 * Spring Data Elasticsearch repository for the {@link WorkflowTask} entity.
 */
public interface WorkflowTaskSearchRepository extends ElasticsearchRepository<WorkflowTask, Long>, WorkflowTaskSearchRepositoryInternal {}

interface WorkflowTaskSearchRepositoryInternal {
    Page<WorkflowTask> search(String query, Pageable pageable);

    Page<WorkflowTask> search(Query query);

    @Async
    void index(WorkflowTask entity);

    @Async
    void deleteFromIndexById(Long id);
}

class WorkflowTaskSearchRepositoryInternalImpl implements WorkflowTaskSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkflowTaskRepository repository;

    WorkflowTaskSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorkflowTaskRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WorkflowTask> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<WorkflowTask> search(Query query) {
        SearchHits<WorkflowTask> searchHits = elasticsearchTemplate.search(query, WorkflowTask.class);
        List<WorkflowTask> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WorkflowTask entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), WorkflowTask.class);
    }
}
