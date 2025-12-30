# Docker Compose et JDL - Guide Complet

## 📋 Table des Matières

1. [Docker Compose Fourni](#docker-compose-fourni)
2. [Configuration JDL pour Docker](#configuration-jdl-pour-docker)
3. [Différence entre les deux](#différence-entre-les-deux)
4. [Utilisation](#utilisation)
5. [Variables d'environnement](#variables-denvironnement)

---

## 🐳 Docker Compose Fourni

Le fichier `docker-compose.yml` fourni configure l'**infrastructure** nécessaire pour PaperDMS.

### Services Inclus (8 services)

#### 1. PostgreSQL (Port 5432)
```yaml
postgresql:
  image: postgres:15-alpine
  environment:
    POSTGRES_USER: paperdms
    POSTGRES_PASSWORD: paperdms
    POSTGRES_DB: paperdms
```

**Usage**: Base de données principale pour toutes les applications

#### 2. Redis (Port 6379)
```yaml
redis:
  image: redis:7-alpine
  command: redis-server --appendonly yes
```

**Usage**: Cache distribué et sessions

#### 3. Zookeeper (Port 2181)
```yaml
zookeeper:
  image: confluentinc/cp-zookeeper:7.5.0
```

**Usage**: Coordination pour Kafka

#### 4. Kafka (Ports 9092, 29092)
```yaml
kafka:
  image: confluentinc/cp-kafka:7.5.0
  depends_on:
    - zookeeper
```

**Usage**: Message broker pour événements asynchrones

#### 5. Elasticsearch (Ports 9200, 9300)
```yaml
elasticsearch:
  image: docker.elastic.co/elasticsearch/elasticsearch:8.11.0
  environment:
    - discovery.type=single-node
    - xpack.security.enabled=false
```

**Usage**: Moteur de recherche full-text

#### 6. Consul (Ports 8500, 8600)
```yaml
consul:
  image: consul:1.16
  command: agent -server -ui -bootstrap-expect=1
```

**Usage**: Service discovery et configuration centralisée

#### 7. MinIO (Ports 9000, 9001)
```yaml
minio:
  image: minio/minio:latest
  command: server /data --console-address ":9001"
```

**Usage**: Stockage S3-compatible pour les fichiers PDF

#### 8. Kafka UI (Port 8090)
```yaml
kafka-ui:
  image: provectuslabs/kafka-ui:latest
```

**Usage**: Interface web pour monitorer Kafka (optionnel)

### Démarrage Rapide

```bash
# Démarrer tous les services
docker-compose up -d

# Vérifier le statut
docker-compose ps

# Voir les logs
docker-compose logs -f

# Arrêter tous les services
docker-compose down

# Arrêter et supprimer les volumes (⚠️ efface les données)
docker-compose down -v
```

### URLs d'Accès

| Service | URL | Credentials |
|---------|-----|------------|
| PostgreSQL | localhost:5432 | paperdms / paperdms |
| Redis | localhost:6379 | - |
| Kafka | localhost:9092 | - |
| Kafka (externe) | localhost:29092 | - |
| Elasticsearch | http://localhost:9200 | - |
| Consul UI | http://localhost:8500 | - |
| MinIO API | http://localhost:9000 | minioadmin / minioadmin |
| MinIO Console | http://localhost:9001 | minioadmin / minioadmin |
| Kafka UI | http://localhost:8090 | - |

---

## 📝 Configuration JDL pour Docker

### Qu'est-ce que c'est ?

Le fichier JDL `docker-config.jdl` configure comment **JHipster génère** les fichiers Docker pour tes **applications**.

### Différence Importante

| docker-compose.yml | docker-config.jdl |
|-------------------|-------------------|
| Infrastructure (PostgreSQL, Redis, etc.) | Applications JHipster |
| Utilisé directement | Génère des fichiers Docker |
| Fourni et prêt à l'emploi | Template pour JHipster |

### Contenu de docker-config.jdl

```jdl
deployment {
  deploymentType docker-compose
  
  // Liste des applications à déployer
  appsFolders [gateway, documentService, ocrService, ...]
  
  // Service discovery
  serviceDiscoveryType consul
  
  // Monitoring
  monitoring prometheus
}
```

### Génération avec JHipster

```bash
# 1. Place tous tes fichiers JDL dans un dossier
cd paperdms-jdl

# 2. Génère les applications
jhipster jdl *.jdl

# 3. Génère la configuration Docker
jhipster docker-compose

# JHipster te posera des questions:
# - Quelles applications inclure ?
# - Type de gateway ?
# - Monitoring ?
# - Clustering ?

# 4. Résultat: nouveau docker-compose.yml pour les apps
```

### Ce que JHipster Génère

```
docker-compose/
├── docker-compose.yml         # Pour les applications JHipster
├── .env                       # Variables d'environnement
├── README-DOCKER-COMPOSE.md   # Guide
└── central-server-config/     # Config Consul
```

---

## 🔄 Différence entre les deux

### Scénario 1: Développement (Recommandé)

```bash
# 1. Démarre l'infrastructure
docker-compose up -d

# 2. Lance les applications localement
cd gateway && ./mvnw
cd documentService && ./mvnw
```

**Avantages**:
- Hot reload pour le développement
- Debug facile
- Logs clairs

### Scénario 2: Production-like

```bash
# 1. Génère la config Docker des applications
jhipster docker-compose

# 2. Démarre tout (infra + apps)
cd docker-compose
docker-compose up -d
```

**Avantages**:
- Proche de la production
- Tout dans Docker
- Facile à déployer

---

## 📦 Utilisation Complète

### Étape 1: Infrastructure

```bash
# Utilise le docker-compose.yml fourni
docker-compose up -d

# Attends que tout soit UP (healthcheck)
docker-compose ps
```

### Étape 2: Crée le Bucket MinIO

```bash
# Via l'interface web
# http://localhost:9001
# Login: minioadmin / minioadmin
# Crée le bucket: paperdms-documents

# OU via CLI
docker exec paperdms-minio \
  mc mb /data/paperdms-documents
```

### Étape 3: Lance les Applications

```bash
# Développement local
cd documentService
./mvnw spring-boot:run

cd gateway
./mvnw spring-boot:run
```

### Étape 4: Teste l'Upload

```bash
# Ouvre http://localhost:8080
# Navigue vers /document/upload
# Upload un PDF
```

---

## 🔧 Variables d'Environnement

### Fichier .env (Optionnel)

Crée un fichier `.env` à côté de `docker-compose.yml` :

```bash
# PostgreSQL
POSTGRES_USER=paperdms
POSTGRES_PASSWORD=paperdms_secure_pass
POSTGRES_DB=paperdms

# Redis
REDIS_PASSWORD=redis_secure_pass

# MinIO
MINIO_ROOT_USER=minio_admin
MINIO_ROOT_PASSWORD=minio_secure_pass

# Kafka
KAFKA_BROKER_ID=1
KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181

# Elasticsearch
ES_JAVA_OPTS=-Xms1g -Xmx1g
```

### Variables pour les Applications

Dans `documentService/src/main/resources/config/application-prod.yml` :

```yaml
spring:
  datasource:
    url: ${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/paperdms}
    username: ${SPRING_DATASOURCE_USERNAME:paperdms}
    password: ${SPRING_DATASOURCE_PASSWORD:paperdms}
  
  redis:
    host: ${SPRING_REDIS_HOST:localhost}
    port: ${SPRING_REDIS_PORT:6379}
  
  kafka:
    bootstrap-servers: ${SPRING_KAFKA_BOOTSTRAP_SERVERS:localhost:9092}

paperdms:
  s3:
    bucket: ${S3_BUCKET:paperdms-documents}
    region: ${S3_REGION:us-east-1}
    endpoint: ${S3_ENDPOINT:http://localhost:9000}
    access-key: ${S3_ACCESS_KEY:minioadmin}
    secret-key: ${S3_SECRET_KEY:minioadmin}
```

---

## 🎯 Scénarios d'Utilisation

### Développement Local

```bash
# Terminal 1: Infrastructure
docker-compose up -d

# Terminal 2: documentService
cd documentService
export S3_ENDPOINT=http://localhost:9000
./mvnw spring-boot:run

# Terminal 3: gateway
cd gateway
./mvnw spring-boot:run
```

### Test Intégration

```bash
# Tout dans Docker
jhipster docker-compose
cd docker-compose
docker-compose up -d
```

### Production

```bash
# Kubernetes
jhipster kubernetes
kubectl apply -f kubernetes/

# Ou Docker Swarm
docker stack deploy -c docker-compose.yml paperdms
```

---

## 📊 Monitoring

### Vérifier les Services

```bash
# Status
docker-compose ps

# Logs
docker-compose logs -f postgresql
docker-compose logs -f kafka
docker-compose logs -f minio

# Health checks
curl http://localhost:9200/_cluster/health
curl http://localhost:8500/v1/status/leader
curl http://localhost:9000/minio/health/live
```

### Kafka Topics

```bash
# Liste les topics
docker exec paperdms-kafka \
  kafka-topics --bootstrap-server localhost:9092 --list

# Consomme les messages
docker exec paperdms-kafka \
  kafka-console-consumer \
    --bootstrap-server localhost:9092 \
    --topic paperdms.document.events \
    --from-beginning
```

### Base de Données

```bash
# Connecte-toi à PostgreSQL
docker exec -it paperdms-postgresql \
  psql -U paperdms -d paperdms

# Requête
SELECT * FROM document LIMIT 10;
```

---

## 🆘 Troubleshooting

### Port déjà utilisé

```bash
# Trouve le processus
lsof -ti:5432 | xargs kill -9

# Change le port dans docker-compose.yml
ports:
  - "5433:5432"  # Utilise 5433 au lieu de 5432
```

### Kafka ne démarre pas

```bash
# Vérifie Zookeeper
docker-compose logs zookeeper

# Redémarre Kafka
docker-compose restart kafka

# Attends 30 secondes
sleep 30
```

### MinIO inaccessible

```bash
# Vérifie le container
docker ps | grep minio

# Logs
docker-compose logs minio

# Recréer
docker-compose up -d --force-recreate minio
```

### Elasticsearch mémoire insuffisante

```bash
# Augmente la limite
# Dans docker-compose.yml:
environment:
  - "ES_JAVA_OPTS=-Xms2g -Xmx2g"  # Plus de RAM
```

---

## 📚 Résumé

### Fichiers

1. **docker-compose.yml** ✅ Fourni, prêt à l'emploi
   - Infrastructure complète
   - 8 services
   - Configuration optimale

2. **docker-config.jdl** 📝 Template optionnel
   - Pour génération JHipster
   - Configuration avancée
   - Kubernetes/Production

### Commandes Essentielles

```bash
# Démarrer
docker-compose up -d

# Status
docker-compose ps

# Logs
docker-compose logs -f

# Arrêter
docker-compose down

# Reset complet
docker-compose down -v
docker-compose up -d
```

### Ordre de Démarrage

1. ✅ `docker-compose up -d` (Infrastructure)
2. ✅ Attendre que tout soit healthy
3. ✅ Créer le bucket MinIO
4. ✅ Lancer les applications
5. ✅ Tester l'upload

---

## 🎓 Aller Plus Loin

### Production

- Ajoute des secrets managers (Vault)
- Configure SSL/TLS
- Mets en place des backups
- Configure le monitoring (Prometheus/Grafana)
- Utilise des Load Balancers

### JDL Avancé

```jdl
deployment {
  deploymentType kubernetes
  monitoring prometheus
  serviceDiscoveryType consul
  kubernetesNamespace paperdms-prod
  ingressDomain paperdms.example.com
  istio true
}
```

### Documentation Officielle

- JHipster Docker Compose: https://www.jhipster.tech/docker-compose/
- JDL Deployment: https://www.jhipster.tech/jdl/deployments

---

✅ **Le docker-compose.yml fourni est prêt à l'emploi !** Lance simplement `docker-compose up -d` et tu es prêt ! 🚀
