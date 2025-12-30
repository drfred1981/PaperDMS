# ?? Module d'Upload PaperDMS

![Version](https://img.shields.io/badge/version-1.0.0-blue)
![JHipster](https://img.shields.io/badge/JHipster-8.x-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-green)
![Angular](https://img.shields.io/badge/Angular-17-red)

## ?? Vue d'Ensemble

Module complet d'upload de documents pour PaperDMS qui **s'intègre parfaitement** avec votre application JHipster **sans modifier** les fichiers générés.

### ? Fonctionnalités

- ? **Upload de fichiers** (PDF, Word, Images)
- ? **Drag & Drop** avec interface moderne
- ? **Déduplication automatique** par SHA-256
- ? **Génération de thumbnails WebP** automatique
- ? **Stockage S3** (AWS ou MinIO)
- ? **Événements Kafka** pour traitement asynchrone
- ? **Validation côté client et serveur**
- ? **Barre de progression** en temps réel
- ? **Support multi-fichiers**
- ? **Gestion des métadonnées** (dossiers, tags, description)

### ??? Architecture

```
???????????????????????????????????????????????
?  JHIPSTER GÉNÉRÉ (inchangé)                ?
?  ? DocumentRepository                       ?
?  ? DocumentMapper                           ?
?  ? DocumentDTO                              ?
?  ? DocumentResource (CRUD)                 ?
???????????????????????????????????????????????
                  ? utilise (composition)
                  ?
???????????????????????????????????????????????
?  MODULE D'UPLOAD (nouveau)                  ?
?  ? DocumentUploadResource                   ?
?  ? DocumentUploadService                    ?
?  ? S3StorageService                         ?
?  ? ThumbnailService                         ?
?  ? Composants Angular                       ?
???????????????????????????????????????????????
```

---

## ?? Contenu du Package

```
paperdms-upload-module/
??? README.md                      # Ce fichier
??? INTEGRATION-GUIDE.md           # Guide d'intégration détaillé
??? FILE-STRUCTURE.md              # Structure des fichiers
??? TESTING.md                     # Guide de test complet
??? install.sh                     # Script d'installation automatique
??? backend/                       # Fichiers Java (7 fichiers)
?   ??? DocumentUploadResource.java
?   ??? DocumentUploadService.java
?   ??? S3StorageService.java
?   ??? ThumbnailService.java
?   ??? DocumentUploadRequest.java
?   ??? DocumentUploadedEvent.java
?   ??? S3Config.java
??? frontend/                      # Fichiers TypeScript (9 fichiers)
    ??? document-upload.module.ts
    ??? document-upload-routing.module.ts
    ??? services/
    ?   ??? document-upload.service.ts
    ?   ??? sha256.service.ts
    ?   ??? file-validation.service.ts
    ??? upload/
    ?   ??? document-upload.component.ts
    ?   ??? document-upload.component.html
    ?   ??? document-upload.component.scss
    ??? models/
        ??? upload-progress.model.ts
```

---

## ?? Installation Rapide

### Option 1: Installation Automatique (Recommandée)

```bash
# 1. Extraire l'archive
tar -xzf paperdms-upload-module.tar.gz
cd paperdms-upload-module

# 2. Exécuter le script d'installation
./install.sh /path/to/your/paperdms/project

# 3. Suivre les instructions affichées
```

### Option 2: Installation Manuelle

Consultez [INTEGRATION-GUIDE.md](INTEGRATION-GUIDE.md) pour les instructions détaillées.

---

## ?? Pré-requis

### Backend
- Java 17+
- Maven 3.8+
- Spring Boot 3.x
- JHipster 8.x
- PostgreSQL 15+
- Kafka 3.x
- MinIO ou AWS S3

### Frontend
- Node.js 18+
- Angular 17
- npm 9+

---

## ?? Configuration Minimale

### Backend (`application-dev.yml`)

```yaml
spring:
  servlet:
    multipart:
      max-file-size: 100MB

paperdms:
  s3:
    bucket: paperdms-documents
    region: eu-west-1
    access-key: minioadmin
    secret-key: minioadmin
    endpoint: http://localhost:9000
```

### Frontend (`app-routing.module.ts`)

```typescript
{
  path: 'document-upload',
  loadChildren: () => import('./document-upload/document-upload.module')
    .then(m => m.DocumentUploadModule)
}
```

---

## ?? Test Rapide

```bash
# 1. Démarrer MinIO
docker run -d -p 9000:9000 -p 9001:9001 \
  -e "MINIO_ROOT_USER=minioadmin" \
  -e "MINIO_ROOT_PASSWORD=minioadmin" \
  minio/minio server /data --console-address ":9001"

# 2. Créer le bucket
mc alias set local http://localhost:9000 minioadmin minioadmin
mc mb local/paperdms-documents

# 3. Démarrer l'application
cd documentService && ./mvnw spring-boot:run

# 4. Tester l'upload
curl -X POST http://localhost:8081/api/documents/upload \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -F "file=@test.pdf" \
  -F "documentTypeId=1" \
  -F "sha256=$(sha256sum test.pdf | cut -d' ' -f1)"
```

Consultez [TESTING.md](TESTING.md) pour plus de scénarios de test.

---

## ?? Documentation

| Document | Description |
|----------|-------------|
| [INTEGRATION-GUIDE.md](INTEGRATION-GUIDE.md) | Guide d'intégration complet étape par étape |
| [FILE-STRUCTURE.md](FILE-STRUCTURE.md) | Structure détaillée des fichiers et emplacements |
| [TESTING.md](TESTING.md) | Guide de test avec exemples curl et scénarios |

---

## ? Checklist d'Installation

- [ ] Archive extraite
- [ ] Script d'installation exécuté OU fichiers copiés manuellement
- [ ] Dépendances Maven ajoutées
- [ ] Configuration S3 ajoutée
- [ ] Route Angular ajoutée
- [ ] Traductions ajoutées
- [ ] MinIO démarré et bucket créé
- [ ] Type de document créé en base
- [ ] Upload testé avec succès

---

**Module développé avec ?? pour PaperDMS**

Version 1.0.0 - Décembre 2024