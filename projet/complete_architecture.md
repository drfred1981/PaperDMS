# ??? Architecture Complète PaperDMS - 16 Microservices

## ?? Vue d'Ensemble

| Port | Service | Responsabilité | Entités |
|------|---------|----------------|---------|
| 8080 | **Gateway** | API Gateway + Angular UI | - |
| 8081 | **Document Service** | Gestion documents, permissions, audit | 20 entités |
| 8082 | **OCR Service** | OCR Tika + IA, cache | 6 entités |
| 8083 | **Search Service** | Elasticsearch, recherche sémantique | 4 entités |
| 8084 | **Notification Service** | Notifications multi-canal, webhooks | 6 entités |
| 8085 | **Workflow Service** | Workflows d'approbation | 5 entités |
| 8086 | **AI Service** | Auto-tagging, extraction, cache IA | 6 entités |
| 8087 | **Similarity Service** | Détection documents similaires | 4 entités |
| 8088 | **Archive Service** | Archivage long terme | 2 entités |
| 8089 | **Export Service** | Export avec patterns | 3 entités |
| 8090 | **Email Import Service** | Import automatique par email | 4 entités |
| 8091 | **Scan Service** | Numérisation documents | 4 entités |
| 8092 | **Transform Service** | Conversion, watermark, fusion | 6 entités |
| 8093 | **Business Doc Service** | Factures, contrats, relevés | 8 entités |
| 8094 | **Reporting Service** | Dashboards, rapports planifiés | 6 entités |
| 8095 | **Monitoring Service** | Alertes, surveillance, santé | 6 entités |

**Total : 90 entités réparties sur 16 microservices**

---

## ?? Document Service (Port 8081)

### Responsabilités
- Gestion centrale des documents
- Permissions granulaires
- Audit trail complet
- Commentaires et collaboration
- Relations entre documents
- Templates de documents
- Dossiers intelligents

### Entités (20)
1. **Document** - Document principal avec SHA-256
2. **DocumentVersion** - Versioning
3. **Folder** - Organisation hiérarchique
4. **Tag** - Classification
5. **TagCategory** - Catégories de tags hiérarchiques
6. **DocumentTag** - Association document-tag
7. **DocumentType** - Types de documents (Facture, Contrat, etc.)
8. **DocumentTypeField** - Définition des champs par type
9. **DocumentMetadata** - Métadonnées personnalisées
10. **ExtractedField** - Champs extraits (OCR/IA)
12. **DocumentPermission** - Permissions granulaires
13. **PermissionGroup** - Groupes de permissions réutilisables
14. **DocumentAudit** - Historique des actions
15. **DocumentComment** - Commentaires avec threading
16. **DocumentRelation** - Relations typées entre documents
17. **DocumentStatistics** - Statistiques d'usage
18. **DocumentTemplate** - Templates réutilisables
19. **SavedSearch** - Recherches sauvegardées avec alertes
20. **SmartFolder** - Dossiers dynamiques (requêtes)
21. **Bookmark** - Favoris utilisateur

### Relations Entre Documents
- `RELATED_TO` - Document lié
- `REPLACES` / `REPLACED_BY` - Remplacement
- `REFERENCES` / `REFERENCED_BY` - Référence
- `ATTACHMENT_OF` / `HAS_ATTACHMENT` - Pièce jointe
- `VERSION_OF` / `HAS_VERSION` - Version
- `PART_OF` / `HAS_PART` - Partie de
- `RESPONSE_TO` / `HAS_RESPONSE` - Réponse
- `AMENDMENT_OF` / `HAS_AMENDMENT` - Amendement

---

## ?? Search Service (Port 8083)

### Responsabilités
- Indexation Elasticsearch
- Recherche full-text
- **Recherche sémantique** (embeddings)
- Facettes de recherche
- Suggestions

### Entités (4)
1. **SearchIndex** - Index Elasticsearch
2. **SearchQuery** - Historique des recherches
3. **SemanticSearch** - Recherche sémantique par embeddings
4. **SearchFacet** - Facettes de recherche (filtres)

---

## ?? Email Import Service (Port 8090)

### Responsabilités
- Import automatique par email
- Règles d'import intelligentes
- Mapping champs email ? métadonnées
- Transformations de données

### Entités (4)
1. **EmailImport** - Email reçu
2. **EmailAttachment** - Pièces jointes
3. **ImportRule** - Règles d'import (conditions + actions)
4. **ImportMapping** - Mapping champs avec transformations

### Exemple de Règle
```json
{
  "name": "Factures Fournisseurs",
  "conditions": {
    "from": "*.@supplier.com",
    "subject": "contains:Facture"
  },
  "actions": {
    "folderId": 123,
    "documentTypeId": "INVOICE",
    "tags": ["Comptabilité", "Fournisseur"]
  },
  "mappings": [
    {
      "emailField": "SUBJECT",
      "documentField": "invoice_number",
      "transformation": "REGEX_EXTRACT",
      "config": "FAC-[0-9]+"
    }
  ]
}
```

---

## ?? Scan Service (Port 8091)

### Responsabilités
- Numérisation via scanners réseau
- Traitement par lots
- Support multi-scanners
- Génération de previews

### Entités (4)
1. **ScanJob** - Travail de numérisation
2. **ScanBatch** - Lot de numérisation
3. **ScannerConfiguration** - Configuration scanners
4. **ScannedPage** - Page numérisée

### Modes de Scan
- Noir & blanc
- Niveaux de gris
- Couleur
- Automatique

---

## ?? Transform Service (Port 8092)

### Responsabilités
- Conversion de formats (PDF?Word, etc.)
- Watermarking dynamique
- Caviardage (redaction)
- Compression optimisée
- Fusion de documents
- Comparaison de versions

### Entités (6)
1. **ConversionJob** - Conversion de formats
2. **WatermarkJob** - Ajout de watermarks
3. **RedactionJob** - Caviardage de zones
4. **CompressionJob** - Compression de fichiers
5. **MergeJob** - Fusion de documents
6. **ComparisonJob** - Comparaison de documents

### Capacités de Transformation

#### Conversion
- PDF ? Word, Excel, PowerPoint
- Images ? PDF
- Word ? PDF/A (archivage)
- HTML ? PDF

#### Watermarking
- **Types** : Texte, Image, QR Code, Invisible
- **Positions** : 9 positions + diagonal + tiled
- **Dynamique** : `{user}`, `{date}`, `{ip}`

#### Caviardage
- Boîtes noires/blanches
- Pixelisation
- Floutage
- Remplacement par pattern

#### Comparaison
- Diff textuel
- Comparaison visuelle PDF
- Comparaison de métadonnées
- Comparaison sémantique

---

## ?? Business Doc Service (Port 8093)

### Responsabilités
- **Gestion métier spécialisée**
- Factures avec lignes
- Contrats avec clauses
- Relevés bancaires avec transactions
- Manuels avec chapitres

### Entités (8)
1. **Invoice** - Facture
2. **InvoiceLine** - Ligne de facture
3. **Contract** - Contrat
4. **ContractClause** - Clause de contrat
5. **BankStatement** - Relevé bancaire
6. **BankTransaction** - Transaction bancaire
7. **Manual** - Manuel/Documentation
8. **ManualChapter** - Chapitre de manuel (hiérarchique)

### Facture
```java
- invoiceNumber (unique)
- invoiceType: PURCHASE, SALES, CREDIT_NOTE, DEBIT_NOTE
- Supplier/Customer info
- Montants HT, TVA, TTC
- Dates: issue, due, payment
- Status: DRAFT ? APPROVED ? SENT ? PAID
- Période comptable
- Exportation comptable
```

### Contrat
```java
- contractNumber (unique)
- contractType: SERVICE, PURCHASE, LEASE, NDA, SLA...
- Parties A & B
- Dates: start, end, renewal
- Renouvellement automatique
- Valeur, devise, fréquence facturation
- Status: DRAFT ? ACTIVE ? EXPIRED
- Alertes expiration
```

### Relevé Bancaire
```java
- IBAN, BIC, compte
- Période du relevé
- Soldes ouverture/clôture
- Transactions avec catégorisation
- Rapprochement bancaire
- Status: PENDING ? RECONCILED
```

### Manuel
```java
- Type: USER_MANUAL, TECHNICAL, INSTALLATION...
- Version, édition, ISBN
- Auteur, langue, publication
- Chapitres hiérarchiques
- Status: DRAFT ? PUBLISHED
```

---

## ?? Reporting Service (Port 8094)

### Responsabilités
- Dashboards personnalisés
- Rapports planifiés (CRON)
- Métriques de performance
- Analyses BI

### Entités (6)
1. **Dashboard** - Dashboard personnalisé
2. **DashboardWidget** - Widget de dashboard
3. **ScheduledReport** - Rapport planifié
4. **ReportExecution** - Exécution de rapport
5. **PerformanceMetric** - Métriques applicatives
6. **SystemMetric** - Métriques système

### Types de Widgets
- Document count
- Storage usage
- Recent uploads
- Top tags
- Workflow status
- User activity
- Charts (line, bar, pie, doughnut, area)
- Tables
- Metrics
- Heatmaps
- Gauges
- Timelines

### Types de Rapports
- Inventaire de documents
- Activité utilisateurs
- Analyse stockage
- Audit de conformité
- Performance workflows
- Utilisation des tags
- Résumé factures
- Expiration contrats
- Réconciliation bancaire
- Métriques de performance
- Santé système

### Formats d'Export
- PDF, Excel, CSV, JSON, HTML, XML

---

## ?? Monitoring Service (Port 8095)

### Responsabilités
- Alertes intelligentes
- Surveillance de documents
- Tâches de maintenance
- Santé du système
- Statut des services

### Entités (6)
1. **Alert** - Alerte déclenchée
2. **AlertRule** - Règle d'alerte
3. **DocumentWatch** - Surveillance de document
4. **MaintenanceTask** - Tâche de maintenance
5. **SystemHealth** - Santé du système
6. **ServiceStatus** - Statut des services

### Types d'Alertes
- **Documents** : Expiration, métadonnées manquantes, duplicatas
- **Stockage** : Quota dépassé
- **Sécurité** : Activité inhabituelle, violations permissions
- **Workflows** : Retards, échecs
- **Système** : Services down, erreurs élevées, surcharge
- **Métier** : Contrats expirant, factures impayées

### Surveillance de Documents
```java
DocumentWatch {
  notifyOnView: true
  notifyOnDownload: true
  notifyOnModify: true
  notifyOnShare: true
  notifyOnDelete: true
  notifyOnComment: true
}
```

### Tâches de Maintenance
- Nettoyage fichiers supprimés
- Reconstruction index
- Optimisation stockage
- Vérification checksums
- Expiration fichiers temporaires
- Élagage logs d'audit
- Mise à jour statistiques
- Vacuum base de données
- Rotation logs
- Backup base de données

---

## ?? Flux de Traitement Complet

```
1. Upload Document
   ?? Document Service (créé avec SHA-256)
   ?? S3 (stockage)
   ?? Kafka: document.uploaded
   
2. OCR Processing
   ?? OCR Service (Tika ou IA)
   ?? Cache vérifié (SHA-256)
   ?? Texte extrait
   ?? Kafka: ocr.completed
   
3. AI Processing (parallèle)
   ?? AI Service: Auto-tagging
   ?? AI Service: Extraction correspondants
   ?? AI Service: Extraction champs (factures, etc.)
   ?? Kafka: ai.processing.completed
   
4. Business Processing (si type métier)
   ?? Business Doc Service
   ?? Création Invoice/Contract/BankStatement
   ?? Extraction lignes/clauses/transactions
   
5. Similarity Detection
   ?? Similarity Service
   ?? Calcul fingerprint
   ?? Recherche similaires
   ?? Kafka: similarity.detected
   
6. Indexation
   ?? Search Service
   ?? Index Elasticsearch
   ?? Embeddings sémantiques
   ?? Kafka: document.indexed
   
7. Workflow (si requis)
   ?? Workflow Service
   ?? Création instance
   ?? Affectation tâches
   ?? Kafka: workflow.task.assigned
   
8. Notifications
   ?? Notification Service
   ?? Email, Push, In-App
   ?? Webhooks externes
   
9. Monitoring
   ?? Monitoring Service
   ?? Vérification alertes
   ?? DocumentWatch triggered
   ?? Métriques enregistrées
   
10. Reporting
    ?? Reporting Service
        ?? Mise à jour dashboards
        ?? Métriques temps réel
```

---

## ?? Statistiques du Projet

- **16 microservices**
- **90 entités**
- **80+ énumérations**
- **Technologies** : 
  - Spring Boot
  - Angular
  - PostgreSQL
  - Redis
  - Kafka
  - Elasticsearch
  - Consul
  - Prometheus
  - S3 (AWS/MinIO)
- **Patterns** :
  - Microservices
  - Event-Driven (Kafka)
  - CQRS (Search Service)
  - Cache (Redis + Application)
  - API Gateway
  - Service Discovery (Consul)

---

## ?? Commandes de Génération

```bash
# Générer toute l'architecture
jhipster jdl paperdms-complete.jdl

# Démarrer avec Docker Compose
cd docker-compose
docker-compose up -d

# Services disponibles
http://localhost:8080  # Gateway (Angular UI)
http://localhost:8081  # Document Service
http://localhost:8082  # OCR Service
http://localhost:8083  # Search Service
http://localhost:8084  # Notification Service
http://localhost:8085  # Workflow Service
http://localhost:8086  # AI Service
http://localhost:8087  # Similarity Service
http://localhost:8088  # Archive Service
http://localhost:8089  # Export Service
http://localhost:8090  # Email Import Service
http://localhost:8091  # Scan Service
http://localhost:8092  # Transform Service
http://localhost:8093  # Business Doc Service
http://localhost:8094  # Reporting Service
http://localhost:8095  # Monitoring Service
```

---

## ?? Fonctionnalités Clés

### ? Sécurité
- Permissions granulaires (USER, GROUP, ROLE, DEPARTMENT)
- Audit trail complet
- Chiffrement documents
- Watermarking dynamique
- Caviardage intelligent

### ? Collaboration
- Commentaires threadés
- Surveillance de documents
- Notifications multi-canal
- Partage sécurisé

### ? Productivité
- Templates de documents
- Dossiers intelligents
- Recherche sémantique
- Favoris
- Dashboards personnalisés

### ? Automatisation
- Import par email avec règles
- Auto-tagging IA
- Extraction automatique de champs
- Workflows d'approbation
- Rapports planifiés
- Tâches de maintenance

### ? Métier
- Factures avec lignes
- Contrats avec clauses
- Relevés bancaires avec réconciliation
- Manuels structurés

### ? Performance
- Cache multi-niveaux (Redis + SHA-256)
- Déduplication automatique
- Compression intelligente
- Métriques temps réel

### ? Intégration
- Webhooks
- API REST complète
- Kafka events
- Scanners réseau
- Tika OCR
- IA (OpenAI, Google, AWS, Azure)

---

## ?? Évolutions Futures Possibles

1. **Mobile App** - Application mobile native
2. **Blockchain** - Horodatage blockchain pour conformité
3. **Signature Électronique** - Intégration DocuSign/Adobe Sign
4. **DLP Avancé** - Data Loss Prevention avec ML
5. **RPA** - Robotic Process Automation
6. **Knowledge Graph** - Graphe de connaissances
7. **Chatbot** - Assistant IA pour recherche

---

**Architecture conçue pour être évolutive, maintenable et production-ready ! ??**