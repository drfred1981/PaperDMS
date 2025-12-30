package fr.smartprod.paperdms.workflow.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.workflow.domain.WorkflowStep;
import fr.smartprod.paperdms.workflow.repository.WorkflowStepRepository;
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
 * Spring Data Elasticsearch repository for the {@link WorkflowStep} entity.
 */
public interface WorkflowStepSearchRepository extends ElasticsearchRepository<WorkflowStep, Long>, WorkflowStepSearchRepositoryInternal {}

interface WorkflowStepSearchRepositoryInternal {
    Page<WorkflowStep> search(String query, Pageable pageable);

    Page<WorkflowStep> search(Query query);

    @Async
    void index(WorkflowStep entity);

    @Async
    void deleteFromIndexById(Long id);
}

class WorkflowStepSearchRepositoryInternalImpl implements WorkflowStepSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkflowStepRepository repository;

    WorkflowStepSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, WorkflowStepRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WorkflowStep> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<WorkflowStep> search(Query query) {
        SearchHits<WorkflowStep> searchHits = elasticsearchTemplate.search(query, WorkflowStep.class);
        List<WorkflowStep> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WorkflowStep entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), WorkflowStep.class);
    }
}
