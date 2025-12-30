package fr.smartprod.paperdms.workflow.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.workflow.domain.WorkflowApprovalHistory;
import fr.smartprod.paperdms.workflow.repository.WorkflowApprovalHistoryRepository;
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
 * Spring Data Elasticsearch repository for the {@link WorkflowApprovalHistory} entity.
 */
public interface WorkflowApprovalHistorySearchRepository
    extends ElasticsearchRepository<WorkflowApprovalHistory, Long>, WorkflowApprovalHistorySearchRepositoryInternal {}

interface WorkflowApprovalHistorySearchRepositoryInternal {
    Page<WorkflowApprovalHistory> search(String query, Pageable pageable);

    Page<WorkflowApprovalHistory> search(Query query);

    @Async
    void index(WorkflowApprovalHistory entity);

    @Async
    void deleteFromIndexById(Long id);
}

class WorkflowApprovalHistorySearchRepositoryInternalImpl implements WorkflowApprovalHistorySearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final WorkflowApprovalHistoryRepository repository;

    WorkflowApprovalHistorySearchRepositoryInternalImpl(
        ElasticsearchTemplate elasticsearchTemplate,
        WorkflowApprovalHistoryRepository repository
    ) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<WorkflowApprovalHistory> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<WorkflowApprovalHistory> search(Query query) {
        SearchHits<WorkflowApprovalHistory> searchHits = elasticsearchTemplate.search(query, WorkflowApprovalHistory.class);
        List<WorkflowApprovalHistory> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(WorkflowApprovalHistory entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), WorkflowApprovalHistory.class);
    }
}
