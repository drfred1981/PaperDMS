package {{category}}.listener;

import {{category}}.entity.{{entity.name}};
import {{package_name}}.service.{{entity.name}}SearchService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import lombok.extern.slf4j.Slf4j;

import jakarta.persistence.PostPersist;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PostRemove;

/**
 * Listener pour indexation automatique dans Elasticsearch
 * Indexe automatiquement les entités {{entity_name}} lors des opérations CRUD
 * Générée automatiquement à partir du template elasticsearch-entity-listener.java.tpl
 */
@Component
@Slf4j
public class {{entity_name}}ElasticsearchListener {

    @Autowired
    private {{entity_name}}SearchService {{entity_name_lower}}SearchService;

    /**
     * Indexe l'entité après sa création
     */
    @PostPersist
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostPersist({{entity_name}} entity) {
        log.debug("Indexation après création de {{entity_name}}: {}", entity.getId());
        try {
            {{entity_name_lower}}SearchService.index(entity);
        } catch (Exception e) {
            log.error("Erreur lors de l'indexation de {{entity_name}} après création: {}", entity.getId(), e);
        }
    }

    /**
     * Réindexe l'entité après sa mise à jour
     */
    @PostUpdate
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostUpdate({{entity_name}} entity) {
        log.debug("Réindexation après mise à jour de {{entity_name}}: {}", entity.getId());
        try {
            {{entity_name_lower}}SearchService.index(entity);
        } catch (Exception e) {
            log.error("Erreur lors de la réindexation de {{entity_name}} après mise à jour: {}", entity.getId(), e);
        }
    }

    /**
     * Supprime l'entité de l'index après sa suppression
     */
    @PostRemove
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onPostRemove({{entity_name}} entity) {
        log.debug("Suppression de l'index après suppression de {{entity_name}}: {}", entity.getId());
        try {
            {{entity_name_lower}}SearchService.deleteFromIndex(entity.getId());
        } catch (Exception e) {
            log.error("Erreur lors de la suppression de {{entity_name}} de l'index: {}", entity.getId(), e);
        }
    }
}
