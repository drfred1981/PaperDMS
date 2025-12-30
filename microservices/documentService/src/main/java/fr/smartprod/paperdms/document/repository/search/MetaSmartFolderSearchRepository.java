package fr.smartprod.paperdms.document.repository.search;

import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import fr.smartprod.paperdms.document.domain.MetaSmartFolder;
import fr.smartprod.paperdms.document.repository.MetaSmartFolderRepository;
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
 * Spring Data Elasticsearch repository for the {@link MetaSmartFolder} entity.
 */
public interface MetaSmartFolderSearchRepository
    extends ElasticsearchRepository<MetaSmartFolder, Long>, MetaSmartFolderSearchRepositoryInternal {}

interface MetaSmartFolderSearchRepositoryInternal {
    Page<MetaSmartFolder> search(String query, Pageable pageable);

    Page<MetaSmartFolder> search(Query query);

    @Async
    void index(MetaSmartFolder entity);

    @Async
    void deleteFromIndexById(Long id);
}

class MetaSmartFolderSearchRepositoryInternalImpl implements MetaSmartFolderSearchRepositoryInternal {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final MetaSmartFolderRepository repository;

    MetaSmartFolderSearchRepositoryInternalImpl(ElasticsearchTemplate elasticsearchTemplate, MetaSmartFolderRepository repository) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.repository = repository;
    }

    @Override
    public Page<MetaSmartFolder> search(String query, Pageable pageable) {
        NativeQuery nativeQuery = new NativeQuery(QueryStringQuery.of(qs -> qs.query(query))._toQuery());
        return search(nativeQuery.setPageable(pageable));
    }

    @Override
    public Page<MetaSmartFolder> search(Query query) {
        SearchHits<MetaSmartFolder> searchHits = elasticsearchTemplate.search(query, MetaSmartFolder.class);
        List<MetaSmartFolder> hits = searchHits.map(SearchHit::getContent).stream().toList();
        return new PageImpl<>(hits, query.getPageable(), searchHits.getTotalHits());
    }

    @Override
    public void index(MetaSmartFolder entity) {
        repository.findById(entity.getId()).ifPresent(elasticsearchTemplate::save);
    }

    @Override
    public void deleteFromIndexById(Long id) {
        elasticsearchTemplate.delete(String.valueOf(id), MetaSmartFolder.class);
    }
}
