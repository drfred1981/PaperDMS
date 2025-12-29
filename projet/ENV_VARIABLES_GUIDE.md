# Guide Variables d'Environnement - PaperDMS

## 🎯 Objectif

Configurer les identifiants de base de données et autres paramètres via des variables d'environnement pour faciliter le déploiement et améliorer la sécurité.

---

## 📋 Table des Matières

1. [Configuration Spring Boot](#configuration-spring-boot)
2. [Variables d'Environnement](#variables-denvironnement)
3. [Docker Compose](#docker-compose)
4. [Fichier .env](#fichier-env)
5. [Déploiement](#déploiement)
6. [Exemples](#exemples)

---

## 🔧 Configuration Spring Boot

### Syntaxe des Variables

Dans les fichiers `application.yml` et `application-prod.yml`, utilise cette syntaxe :

```yaml
property: ${VARIABLE_NAME:default_value}
```

**Exemple** :
```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/documentService}
    username: ${SPRING_DATASOURCE_USERNAME:documentService}
    password: ${SPRING_DATASOURCE_PASSWORD:}
```

**Explication** :
- `${SPRING_DATASOURCE_URL:...}` → Utilise la variable d'environnement `SPRING_DATASOURCE_URL`
- Si la variable n'existe pas, utilise la valeur par défaut après `:`
- Si pas de valeur par défaut, met une chaîne vide

### Fichier application-prod.yml Complet

```yaml
spring:
  cloud:
    consul:
      host: ${SPRING_CLOUD_CONSUL_HOST:localhost}
      port: ${SPRING_CLOUD_CONSUL_PORT:8500}
  
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/documentService}
    username: ${SPRING_DATASOURCE_USERNAME:documentService}
    password: ${SPRING_DATASOURCE_PASSWORD:}
  
  redis:
    host: ${SPRING_REDIS_HOST:localhost}
    port: ${SPRING_REDIS_PORT:6379}
    password: ${SPRING_REDIS_PASSWORD:}
  
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
  
  elasticsearch:
    uris: ${SPRING_ELASTICSEARCH_URIS:http://localhost:9200}

jhipster:
  cache:
    redis:
      server: ${JHIPSTER_CACHE_REDIS_SERVER:redis://localhost:6379}
  
  security:
    authentication:
      jwt:
        base64-secret: ${JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET:default_secret_here}

# Configuration S3 personnalisée
paperdms:
  s3:
    bucket: ${S3_BUCKET:paperdms-documents}
    region: ${S3_REGION:us-east-1}
    endpoint: ${S3_ENDPOINT:}
    access-key: ${S3_ACCESS_KEY:}
    secret-key: ${S3_SECRET_KEY:}
  
  kafka:
    topics:
      document-events: ${KAFKA_DOCUMENT_EVENTS_TOPIC:paperdms.document.events}
      service-status: ${KAFKA_SERVICE_STATUS_TOPIC:paperdms.document.service-status}
```

---

## 📦 Variables d'Environnement

### Variables Essentielles

#### Base de Données
```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/documentService
SPRING_DATASOURCE_USERNAME=documentService
SPRING_DATASOURCE_PASSWORD=secure_password_here
```

#### Redis
```bash
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=
```

#### Kafka
```bash
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
KAFKA_DOCUMENT_EVENTS_TOPIC=paperdms.document.events
KAFKA_SERVICE_STATUS_TOPIC=paperdms.document.service-status
```

#### S3/MinIO
```bash
S3_BUCKET=paperdms-documents
S3_REGION=us-east-1
S3_ENDPOINT=http://minio:9000
S3_ACCESS_KEY=minioadmin
S3_SECRET_KEY=minioadmin
```

#### Consul
```bash
SPRING_CLOUD_CONSUL_HOST=consul
SPRING_CLOUD_CONSUL_PORT=8500
```

#### Sécurité
```bash
JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=your_base64_encoded_secret
```

---

## 🐳 Docker Compose

### Approche 1 : docker-compose.apps.yml (Recommandé)

Fichier séparé pour les applications :

```yaml
services:
  documentservice:
    image: paperdms/documentservice:latest
    environment:
      # Base de données
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/${DOCUMENTSERVICE_DB_NAME:-documentService}
      - SPRING_DATASOURCE_USERNAME=${DOCUMENTSERVICE_DB_USER:-documentService}
      - SPRING_DATASOURCE_PASSWORD=${DOCUMENTSERVICE_DB_PASSWORD:-documentService}
      
      # Redis
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
      
      # Kafka
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
      
      # S3
      - S3_BUCKET=${S3_BUCKET:-paperdms-documents}
      - S3_ENDPOINT=${S3_ENDPOINT:-http://minio:9000}
      - S3_ACCESS_KEY=${S3_ACCESS_KEY:-minioadmin}
      - S3_SECRET_KEY=${S3_SECRET_KEY:-minioadmin}
    ports:
      - "8081:8081"
    depends_on:
      - postgresql
      - redis
      - kafka
      - minio
```

### Approche 2 : Fichier env_file

```yaml
services:
  documentservice:
    image: paperdms/documentservice:latest
    env_file:
      - .env
      - .env.documentservice
    ports:
      - "8081:8081"
```

### Approche 3 : Variables Inline

```yaml
services:
  documentservice:
    image: paperdms/documentservice:latest
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgresql:5432/documentService
      SPRING_DATASOURCE_USERNAME: documentService
      SPRING_DATASOURCE_PASSWORD: secure_password
```

---

## 📝 Fichier .env

### Création du Fichier

```bash
# Copie le template
cp .env.example .env

# Édite avec tes valeurs
nano .env
```

### Contenu du Fichier .env

```bash
# =============================================================================
# PaperDMS - Environment Variables
# =============================================================================

# -----------------------------------------------------------------------------
# PostgreSQL Infrastructure
# -----------------------------------------------------------------------------
POSTGRES_USER=paperdms
POSTGRES_PASSWORD=paperdms_secure_pass
POSTGRES_DB=paperdms

# -----------------------------------------------------------------------------
# documentService Database
# -----------------------------------------------------------------------------
DOCUMENTSERVICE_DB_NAME=documentService
DOCUMENTSERVICE_DB_USER=documentService
DOCUMENTSERVICE_DB_PASSWORD=documentService_secure_pass

# -----------------------------------------------------------------------------
# gateway Database
# -----------------------------------------------------------------------------
GATEWAY_DB_NAME=gateway
GATEWAY_DB_USER=gateway
GATEWAY_DB_PASSWORD=gateway_secure_pass

# -----------------------------------------------------------------------------
# Redis
# -----------------------------------------------------------------------------
REDIS_PASSWORD=

# -----------------------------------------------------------------------------
# MinIO / S3
# -----------------------------------------------------------------------------
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin_secure_pass

S3_BUCKET=paperdms-documents
S3_REGION=us-east-1
S3_ENDPOINT=http://minio:9000
S3_ACCESS_KEY=minioadmin
S3_SECRET_KEY=minioadmin_secure_pass

# -----------------------------------------------------------------------------
# Kafka
# -----------------------------------------------------------------------------
KAFKA_DOCUMENT_EVENTS_TOPIC=paperdms.document.events
KAFKA_SERVICE_STATUS_TOPIC=paperdms.document.service-status

# -----------------------------------------------------------------------------
# Security
# -----------------------------------------------------------------------------
JWT_SECRET=NzhiYTRhNzdiNjc5ZDIzODNmMmYxODMyYTlhY2U4MWFkYTc0MmE0YmJlM2QyZTcyZTE0Y2UwODA4YjhmM2YwMTBjMjgxMTVhZjY4YjA1ODU5YWU3ZjMwNjNkZDM4NjkzMzQ2OWI1NDYzZGU2NTI5MjExNzhlNjEwYzUwY2NkYWM=

# -----------------------------------------------------------------------------
# Application Ports
# -----------------------------------------------------------------------------
GATEWAY_PORT=8080
DOCUMENTSERVICE_PORT=8081
```

### ⚠️ Sécurité du Fichier .env

```bash
# Ajoute .env au .gitignore
echo ".env" >> .gitignore

# Change les permissions (lecture seule pour le propriétaire)
chmod 600 .env
```

---

## 🚀 Déploiement

### Scénario 1 : Développement Local

Applications lancées localement, infrastructure dans Docker :

```bash
# 1. Démarre l'infrastructure
docker-compose up -d

# 2. Exporte les variables d'environnement
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/documentService
export SPRING_DATASOURCE_USERNAME=documentService
export SPRING_DATASOURCE_PASSWORD=documentService
export S3_ENDPOINT=http://localhost:9000
export S3_ACCESS_KEY=minioadmin
export S3_SECRET_KEY=minioadmin
export SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092

# 3. Lance l'application
cd documentService
./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
```

### Scénario 2 : Tout dans Docker

Infrastructure + applications dans Docker :

```bash
# 1. Crée le fichier .env
cp .env.example .env
nano .env  # Édite les valeurs

# 2. Démarre l'infrastructure
docker-compose up -d

# 3. Attends que tout soit healthy
docker-compose ps

# 4. Build les images des applications
cd documentService
./mvnw -Pprod clean verify jib:dockerBuild

cd ../gateway
./mvnw -Pprod clean verify jib:dockerBuild

# 5. Démarre les applications
docker-compose -f docker-compose.apps.yml up -d
```

### Scénario 3 : Production avec Secrets

```bash
# 1. Utilise des secrets managers (AWS Secrets Manager, Vault, etc.)

# 2. Injecte les secrets comme variables d'environnement
docker run -d \
  --name documentservice \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://db.prod:5432/documentService \
  -e SPRING_DATASOURCE_USERNAME=$(aws secretsmanager get-secret-value --secret-id db-username --query SecretString --output text) \
  -e SPRING_DATASOURCE_PASSWORD=$(aws secretsmanager get-secret-value --secret-id db-password --query SecretString --output text) \
  paperdms/documentservice:latest
```

---

## 📚 Exemples Pratiques

### Exemple 1 : Plusieurs Bases de Données

Chaque microservice a sa propre base de données :

**.env**
```bash
# documentService
DOCUMENTSERVICE_DB_NAME=documentService
DOCUMENTSERVICE_DB_USER=doc_user
DOCUMENTSERVICE_DB_PASSWORD=doc_pass_123

# ocrService
OCRSERVICE_DB_NAME=ocrService
OCRSERVICE_DB_USER=ocr_user
OCRSERVICE_DB_PASSWORD=ocr_pass_456

# searchService
SEARCHSERVICE_DB_NAME=searchService
SEARCHSERVICE_DB_USER=search_user
SEARCHSERVICE_DB_PASSWORD=search_pass_789
```

**docker-compose.apps.yml**
```yaml
services:
  documentservice:
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/${DOCUMENTSERVICE_DB_NAME}
      - SPRING_DATASOURCE_USERNAME=${DOCUMENTSERVICE_DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${DOCUMENTSERVICE_DB_PASSWORD}
  
  ocrservice:
    environment:
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/${OCRSERVICE_DB_NAME}
      - SPRING_DATASOURCE_USERNAME=${OCRSERVICE_DB_USER}
      - SPRING_DATASOURCE_PASSWORD=${OCRSERVICE_DB_PASSWORD}
```

### Exemple 2 : Environnements Multiples

Différents fichiers .env pour dev, staging, prod :

**.env.dev**
```bash
SPRING_DATASOURCE_PASSWORD=dev_password
S3_ENDPOINT=http://localhost:9000
SPRING_PROFILES_ACTIVE=dev
```

**.env.prod**
```bash
SPRING_DATASOURCE_PASSWORD=very_secure_prod_password
S3_ENDPOINT=
S3_REGION=eu-west-1
SPRING_PROFILES_ACTIVE=prod
```

**Usage**
```bash
# Dev
docker-compose --env-file .env.dev up -d

# Prod
docker-compose --env-file .env.prod up -d
```

### Exemple 3 : AWS RDS + S3

```bash
# Base de données RDS
SPRING_DATASOURCE_URL=jdbc:postgresql://paperdms.abc123.eu-west-1.rds.amazonaws.com:5432/documentService
SPRING_DATASOURCE_USERNAME=admin
SPRING_DATASOURCE_PASSWORD=super_secure_rds_password

# S3 (pas de endpoint, utilise AWS S3 directement)
S3_BUCKET=prod-paperdms-documents
S3_REGION=eu-west-1
S3_ACCESS_KEY=
S3_SECRET_KEY=
# Note: Laisse vides pour utiliser IAM role
```

---

## 🔍 Vérification

### Vérifier les Variables dans le Container

```bash
# Entre dans le container
docker exec -it paperdms-documentservice bash

# Affiche les variables d'environnement
env | grep SPRING
env | grep S3
env | grep KAFKA
```

### Vérifier la Configuration Chargée

```bash
# Endpoint Spring Boot Actuator
curl http://localhost:8081/management/env

# Avec jq pour filtrer
curl -s http://localhost:8081/management/env | jq '.propertySources[] | select(.name | contains("systemEnvironment"))'
```

### Logs de Démarrage

```bash
# Vérifier que les variables sont bien chargées
docker logs paperdms-documentservice | grep datasource
docker logs paperdms-documentservice | grep "S3 configuration"
```

---

## ❌ Erreurs Courantes

### Erreur 1 : Variable Non Trouvée

```
Could not resolve placeholder 'SPRING_DATASOURCE_URL' in value "${SPRING_DATASOURCE_URL}"
```

**Solution** : Ajoute une valeur par défaut
```yaml
url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/documentService}
```

### Erreur 2 : Mauvais Format

```yaml
# ❌ MAUVAIS
url: $SPRING_DATASOURCE_URL

# ✅ BON
url: ${SPRING_DATASOURCE_URL:default_value}
```

### Erreur 3 : Variable dans .env Non Chargée

```bash
# Vérifie que docker-compose utilise bien le .env
docker-compose config | grep DATASOURCE

# Ou spécifie explicitement
docker-compose --env-file .env up -d
```

---

## 🎓 Best Practices

### 1. Valeurs par Défaut

Toujours fournir des valeurs par défaut pour le développement :
```yaml
url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/documentService}
```

### 2. Secrets en Production

Ne JAMAIS mettre de mots de passe en clair dans `docker-compose.yml`.
Utilise `.env` ou secrets managers.

### 3. Nomenclature

Utilise des préfixes cohérents :
- `SPRING_*` pour Spring Boot
- `JHIPSTER_*` pour JHipster
- `S3_*` pour S3
- `KAFKA_*` pour Kafka

### 4. Documentation

Documente chaque variable dans `.env.example` avec :
- Description
- Valeur par défaut
- Valeur recommandée pour prod

### 5. Gitignore

```bash
# .gitignore
.env
.env.local
.env.*.local
```

---

## ✅ Checklist

- [ ] `application-prod.yml` utilise `${VAR:default}`
- [ ] `.env.example` créé et documenté
- [ ] `.env` ajouté au `.gitignore`
- [ ] Variables testées en dev local
- [ ] Variables testées dans Docker
- [ ] Secrets sécurisés en production
- [ ] Documentation à jour

---

## 📖 Résumé

### Pour Développer

```bash
# 1. Copie .env.example
cp .env.example .env

# 2. Démarre l'infra
docker-compose up -d

# 3. Lance l'app localement
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/documentService
./mvnw spring-boot:run
```

### Pour Déployer

```bash
# 1. Configure .env pour prod
nano .env

# 2. Build les images
./mvnw -Pprod clean verify jib:dockerBuild

# 3. Démarre tout
docker-compose up -d
docker-compose -f docker-compose.apps.yml up -d
```

---

✅ **Avec cette configuration, tu peux gérer facilement les credentials via des variables d'environnement !**
