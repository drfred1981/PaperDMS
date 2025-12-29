# Système de Statuts par Service - PaperDMS

## Vue d'ensemble

Ce système permet à chaque document d'avoir un statut indépendant pour chaque service qui le traite, offrant une granularité fine du suivi du cycle de vie documentaire.

## Modifications principales

### 1. Simplification de DocumentStatus

**Avant:**
```jdl
enum DocumentStatus {
  UPLOADING, UPLOADED, PROCESSING, PROCESSED, OCR_PENDING, OCR_IN_PROGRESS,
  OCR_COMPLETED, OCR_FAILED, AI_PROCESSING, INDEXED, WORKFLOW_PENDING,
  WORKFLOW_IN_PROGRESS, APPROVED, REJECTED, ARCHIVED, DELETED, ERROR
}
```

**Après:**
```jdl
enum DocumentStatus {
  UPLOADING, UPLOADED, ACTIVE, ARCHIVED, DELETED, ERROR
}
```

Le statut global du document est désormais simplifié et représente uniquement son état principal dans le système.

### 2. Nouveaux Enums

#### ServiceType
Identifie chaque service qui peut traiter un document:
```jdl
enum ServiceType {
  OCR_SERVICE, AI_SERVICE, WORKFLOW_SERVICE, SEARCH_SERVICE,
  TRANSFORM_SERVICE, ARCHIVE_SERVICE, EXPORT_SERVICE, 
  SIMILARITY_SERVICE, BUSINESS_SERVICE, SCAN_SERVICE,
  EMAIL_IMPORT_SERVICE, REPORTING_SERVICE, MONITORING_SERVICE
}
```

#### ServiceStatus
États génériques applicables à tous les services:
```jdl
enum ServiceStatus {
  NOT_APPLICABLE,      // Le service ne s'applique pas à ce document
  PENDING,             // En attente de traitement
  QUEUED,              // Dans la file d'attente
  IN_PROGRESS,         // Traitement en cours
  COMPLETED,           // Traitement terminé avec succès
  FAILED,              // Échec du traitement
  CANCELLED,           // Traitement annulé
  SKIPPED,             // Traitement ignoré volontairement
  RETRYING,            // En cours de nouvelle tentative
  PARTIALLY_COMPLETED  // Partiellement traité
}
```

### 3. Nouvelle Entité: DocumentServiceStatus

```jdl
entity DocumentServiceStatus {
  documentId Long required
  serviceType ServiceType required
  status ServiceStatus required
  statusDetails TextBlob              // Détails spécifiques au service
  errorMessage TextBlob                // Message d'erreur si échec
  retryCount Integer                   // Nombre de tentatives
  lastProcessedDate Instant            // Dernière date de traitement
  processingStartDate Instant          // Début du traitement
  processingEndDate Instant            // Fin du traitement
  processingDuration Long              // Durée en millisecondes
  jobId String maxlength(100)          // ID du job de traitement
  priority Integer                     // Priorité du traitement
  updatedBy String maxlength(50)
  updatedDate Instant required
}
```

## Utilisation

### Exemple 1: Document uploadé nécessitant OCR et AI

```json
Document {
  "id": 1234,
  "title": "Facture_2024.pdf",
  "status": "ACTIVE",
  ...
}

DocumentServiceStatus [
  {
    "documentId": 1234,
    "serviceType": "OCR_SERVICE",
    "status": "COMPLETED",
    "processingDuration": 15000,
    "lastProcessedDate": "2024-12-20T10:30:00Z"
  },
  {
    "documentId": 1234,
    "serviceType": "AI_SERVICE",
    "status": "IN_PROGRESS",
    "processingStartDate": "2024-12-20T10:30:15Z"
  },
  {
    "documentId": 1234,
    "serviceType": "WORKFLOW_SERVICE",
    "status": "PENDING"
  },
  {
    "documentId": 1234,
    "serviceType": "SEARCH_SERVICE",
    "status": "QUEUED"
  }
]
```

### Exemple 2: Document avec échec OCR

```json
DocumentServiceStatus {
  "documentId": 5678,
  "serviceType": "OCR_SERVICE",
  "status": "FAILED",
  "errorMessage": "Unsupported image format",
  "retryCount": 3,
  "lastProcessedDate": "2024-12-20T11:00:00Z"
}
```

### Exemple 3: Document non concerné par certains services

```json
DocumentServiceStatus {
  "documentId": 9012,
  "serviceType": "SCAN_SERVICE",
  "status": "NOT_APPLICABLE",
  "statusDetails": "Document déjà numérique, scan non requis"
}
```

## Avantages

1. **Granularité fine**: Chaque service peut avoir son propre état
2. **Traçabilité**: Historique complet du traitement par service
3. **Performance**: Possibilité de suivre la durée de traitement par service
4. **Retry logic**: Gestion des tentatives par service
5. **Parallélisation**: Services indépendants peuvent traiter en parallèle
6. **Flexibilité**: Ajout facile de nouveaux services

## Patterns d'intégration Kafka

### Publication d'événements de changement de statut

Chaque service doit publier sur le topic `document-service-status-changed`:

```java
public class DocumentServiceStatusEvent {
    private Long documentId;
    private ServiceType serviceType;
    private ServiceStatus oldStatus;
    private ServiceStatus newStatus;
    private String statusDetails;
    private Instant timestamp;
}
```

### Consommation par le DocumentService

Le DocumentService écoute et met à jour la table `document_service_status`:

```java
@KafkaListener(topics = "document-service-status-changed")
public void handleStatusChange(DocumentServiceStatusEvent event) {
    documentServiceStatusRepository.updateStatus(
        event.getDocumentId(),
        event.getServiceType(),
        event.getNewStatus(),
        event.getStatusDetails()
    );
}
```

## Requêtes courantes

### Trouver tous les documents en échec OCR

```sql
SELECT d.* 
FROM document d
JOIN document_service_status dss ON d.id = dss.document_id
WHERE dss.service_type = 'OCR_SERVICE' 
  AND dss.status = 'FAILED';
```

### Statistiques de traitement par service

```sql
SELECT 
    service_type,
    status,
    COUNT(*) as count,
    AVG(processing_duration) as avg_duration
FROM document_service_status
GROUP BY service_type, status;
```

### Documents en attente de traitement

```sql
SELECT d.*, dss.service_type
FROM document d
JOIN document_service_status dss ON d.id = dss.document_id
WHERE dss.status IN ('PENDING', 'QUEUED')
ORDER BY dss.priority DESC, dss.updated_date ASC;
```

## Migration depuis l'ancien système

Pour migrer les documents existants, créer un script qui:

1. Lit le `DocumentStatus` actuel
2. Crée les entrées `DocumentServiceStatus` correspondantes
3. Met à jour le statut global du document

Exemple de mapping:

- `OCR_PENDING` → Document.status=ACTIVE + OCR_SERVICE.status=PENDING
- `OCR_IN_PROGRESS` → Document.status=ACTIVE + OCR_SERVICE.status=IN_PROGRESS
- `OCR_COMPLETED` → Document.status=ACTIVE + OCR_SERVICE.status=COMPLETED
- `WORKFLOW_PENDING` → Document.status=ACTIVE + WORKFLOW_SERVICE.status=PENDING
- etc.

## Considérations techniques

### Index recommandés

```sql
CREATE INDEX idx_doc_service_status_doc_service 
ON document_service_status(document_id, service_type);

CREATE INDEX idx_doc_service_status_status 
ON document_service_status(status);

CREATE INDEX idx_doc_service_status_service_status 
ON document_service_status(service_type, status);

CREATE INDEX idx_doc_service_status_updated 
ON document_service_status(updated_date);
```

### Contrainte d'unicité

```sql
ALTER TABLE document_service_status 
ADD CONSTRAINT uk_document_service 
UNIQUE (document_id, service_type);
```

Cette contrainte garantit qu'il n'y a qu'un seul statut par document et par service.
