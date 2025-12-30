package fr.smartprod.paperdms.similarity.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.similarity.domain.SimilarityCluster;
import fr.smartprod.paperdms.similarity.repository.SimilarityClusterRepository;
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
 * Spring Data Elasticsearch repository for the {@link SimilarityCluster} entity.
 */
public interface SimilarityClusterSearchRepository
    extends ElasticsearchRepository<SimilarityCluster, Long>, SimilarityClusterSearchRepositoryInternal {}

interface SimilarityClusterSearchRepositoryInternal {
    Page<SimilarityCluster> search(String query, Pageable pageable);

    Page<SimilarityCluster> search(Query query);

    @Async
    void index(SimilarityCluster entity);

    @Async
    void deleteFromIndexById(Long id);
}

class SimilarityClusterSearchRepositoryInternalImpl implements SimilarityClusterSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final SimilarityClusterRepository repository;

    SimilarityClusterSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, SimilarityClusterRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<SimilarityCluster> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<SimilarityCluster> search(Query query) {
        SearchHits<SimilarityCluster> searchHits = elasticsearchTemplate.search(query, SimilarityCluster.class);
        List<SimilarityCluster> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(SimilarityCluster entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), SimilarityCluster.class);
    }
}
