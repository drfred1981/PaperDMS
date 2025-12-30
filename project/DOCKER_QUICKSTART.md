# 🐳 Docker Compose - Récapitulatif Rapide

## 📦 Fichiers Fournis

### 1. docker-compose.yml ✅ PRÊT À L'EMPLOI
**Usage**: Lance l'infrastructure complète
```bash
docker-compose up -d
```

**Services inclus** (8 services):
- ✅ PostgreSQL (5432)
- ✅ Redis (6379)
- ✅ Zookeeper (2181)
- ✅ Kafka (9092, 29092)
- ✅ Elasticsearch (9200, 9300)
- ✅ Consul (8500, 8600)
- ✅ MinIO (9000, 9001)
- ✅ Kafka UI (8090)

### 2. .env.example 📝 TEMPLATE
**Usage**: Variables d'environnement
```bash
cp .env.example .env
# Édite .env selon tes besoins
```

### 3. docker-config.jdl 📚 RÉFÉRENCE
**Usage**: Configuration JDL pour JHipster
```bash
jhipster docker-compose
```

### 4. DOCKER_COMPOSE_GUIDE.md 📖 GUIDE COMPLET
**Usage**: Documentation détaillée de tout

---

## ⚡ Démarrage Rapide (5 minutes)

### Étape 1: Démarrer l'infrastructure
```bash
cd paperdms-upload-feature
docker-compose up -d
```

### Étape 2: Vérifier que tout fonctionne
```bash
docker-compose ps
```

Tous les services doivent être "Up" et "healthy".

### Étape 3: Créer le bucket S3
```bash
# Option A: Via l'interface web
# Ouvre http://localhost:9001
# Login: minioadmin / minioadmin
# Crée le bucket: paperdms-documents

# Option B: Via CLI
docker exec paperdms-minio \
  mc mb /data/paperdms-documents
```

### Étape 4: Vérifier les services

```bash
# PostgreSQL
docker exec -it paperdms-postgresql psql -U paperdms -c "SELECT version();"

# Redis
docker exec paperdms-redis redis-cli ping

# Kafka
docker exec paperdms-kafka kafka-topics --bootstrap-server localhost:9092 --list

# Elasticsearch
curl http://localhost:9200/_cluster/health

# MinIO
curl http://localhost:9000/minio/health/live

# Consul
curl http://localhost:8500/v1/status/leader
```

### Étape 5: Lancer les applications
```bash
# Terminal 1: documentService
cd documentService
./mvnw spring-boot:run

# Terminal 2: gateway
cd gateway
./mvnw spring-boot:run
```

---

## 🎯 Scénarios d'Utilisation

### Développement Local (Recommandé)

```bash
# Infrastructure dans Docker
docker-compose up -d

# Applications localement (hot reload)
cd documentService && ./mvnw spring-boot:run
cd gateway && ./mvnw spring-boot:run
```

**Avantages**:
- ✅ Hot reload
- ✅ Debug facile
- ✅ Logs clairs
- ✅ IDE intégration

**Configuration**:
- Endpoints: `localhost` (ex: `localhost:5432`)
- Kafka: `localhost:29092` (port externe)

### Tout dans Docker

```bash
# Génère la config Docker des apps
jhipster docker-compose

# Lance tout
cd docker-compose
docker-compose up -d
```

**Avantages**:
- ✅ Proche production
- ✅ Isolation complète
- ✅ Facile à partager

**Configuration**:
- Endpoints: noms de services (ex: `postgresql`)
- Kafka: `kafka:9092` (port interne)

---

## 📋 Configuration JDL

### Qu'est-ce que c'est ?

Le fichier `docker-config.jdl` est un **template** pour JHipster. Il ne remplace PAS `docker-compose.yml`.

### Quand l'utiliser ?

#### ❌ NE L'UTILISE PAS si tu veux juste :
- Démarrer l'infrastructure
- Développer localement
- Tester le système

#### ✅ UTILISE-LE si tu veux :
- Générer une config Docker complète avec JHipster
- Déployer en production (Kubernetes)
- Configuration avancée

### Comment l'utiliser ?

```bash
# Place tous tes JDL dans un dossier
cd paperdms-jdl

# Génère les apps
jhipster jdl *.jdl

# Génère la config Docker
jhipster docker-compose

# Sélectionne:
# - Applications à inclure
# - Gateway type
# - Monitoring (Prometheus)
# - Service discovery (Consul)

# Résultat:
# docker-compose/ (nouveau dossier)
# ├── docker-compose.yml
# ├── .env
# └── README-DOCKER-COMPOSE.md
```

---

## 🔧 Variables d'Environnement

### Fichier .env

Crée `.env` à partir de `.env.example`:

```bash
cp .env.example .env
```

### Principales Variables

#### Pour l'Infrastructure
```bash
POSTGRES_PASSWORD=paperdms
MINIO_ROOT_USER=minioadmin
MINIO_ROOT_PASSWORD=minioadmin
```

#### Pour les Applications
```bash
# Dev local (apps hors Docker)
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/paperdms
S3_ENDPOINT=http://localhost:9000
SPRING_KAFKA_BOOTSTRAP_SERVERS=localhost:29092

# Tout dans Docker
SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/paperdms
S3_ENDPOINT=http://minio:9000
SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
```

---

## 🆘 Commandes Utiles

### Gestion des Services

```bash
# Démarrer
docker-compose up -d

# Arrêter
docker-compose down

# Redémarrer un service
docker-compose restart kafka

# Voir les logs
docker-compose logs -f
docker-compose logs -f kafka

# Status
docker-compose ps

# Supprimer tout (⚠️ efface les données)
docker-compose down -v
```

### Inspection

```bash
# Entrer dans un container
docker exec -it paperdms-postgresql bash

# Exécuter une commande
docker exec paperdms-postgresql psql -U paperdms -c "SELECT * FROM document;"

# Voir les ressources
docker stats
```

### Kafka

```bash
# Lister les topics
docker exec paperdms-kafka \
  kafka-topics --bootstrap-server localhost:9092 --list

# Créer un topic
docker exec paperdms-kafka \
  kafka-topics --bootstrap-server localhost:9092 \
  --create --topic test --partitions 1 --replication-factor 1

# Consommer des messages
docker exec paperdms-kafka \
  kafka-console-consumer \
  --bootstrap-server localhost:9092 \
  --topic paperdms.document.events \
  --from-beginning
```

### PostgreSQL

```bash
# Console SQL
docker exec -it paperdms-postgresql \
  psql -U paperdms -d paperdms

# Backup
docker exec paperdms-postgresql \
  pg_dump -U paperdms paperdms > backup.sql

# Restore
cat backup.sql | docker exec -i paperdms-postgresql \
  psql -U paperdms paperdms
```

---

## 🔍 Troubleshooting

### Port déjà utilisé

```bash
# Trouve le processus
lsof -ti:5432 | xargs kill -9

# OU change le port dans docker-compose.yml
ports:
  - "5433:5432"
```

### Service ne démarre pas

```bash
# Vérifier les logs
docker-compose logs [service-name]

# Recréer le service
docker-compose up -d --force-recreate [service-name]
```

### Problème de réseau

```bash
# Recréer le réseau
docker-compose down
docker network prune
docker-compose up -d
```

### Manque d'espace disque

```bash
# Nettoyer Docker
docker system prune -a --volumes

# Puis redémarrer
docker-compose up -d
```

---

## 📊 URLs de Monitoring

| Service | URL | Usage |
|---------|-----|-------|
| Consul UI | http://localhost:8500 | Service discovery |
| MinIO Console | http://localhost:9001 | Gestion S3 |
| Kafka UI | http://localhost:8090 | Monitoring Kafka |
| Elasticsearch | http://localhost:9200 | API REST |

---

## ✅ Checklist de Vérification

Après `docker-compose up -d`, vérifie:

- [ ] `docker-compose ps` → tous les services "Up"
- [ ] PostgreSQL: `docker exec paperdms-postgresql pg_isready`
- [ ] Redis: `docker exec paperdms-redis redis-cli ping`
- [ ] Kafka: Topics créés automatiquement
- [ ] Elasticsearch: `curl localhost:9200/_cluster/health`
- [ ] MinIO: Console accessible http://localhost:9001
- [ ] Consul: UI accessible http://localhost:8500
- [ ] Bucket créé: `paperdms-documents`

---

## 🎓 Différence Clé

| Aspect | docker-compose.yml | docker-config.jdl |
|--------|-------------------|-------------------|
| **Type** | Fichier Docker Compose | Fichier JDL |
| **Usage** | Lancer l'infrastructure | Template pour JHipster |
| **État** | ✅ Prêt à l'emploi | 📝 Référence optionnelle |
| **Commande** | `docker-compose up -d` | `jhipster docker-compose` |
| **Contenu** | Infrastructure (DB, Kafka, etc.) | Config apps JHipster |
| **Quand utiliser** | Toujours (développement) | Avancé (production) |

---

## 🚀 Résumé

### Pour Développer

1. ✅ Utilise `docker-compose.yml` fourni
2. ✅ Lance avec `docker-compose up -d`
3. ✅ Crée le bucket MinIO
4. ✅ Lance les apps localement

### Pour la Production

1. ✅ Commence par `docker-compose.yml`
2. 📝 Optionnel: utilise `docker-config.jdl`
3. 🔧 Configure `.env` avec secrets sécurisés
4. 🚀 Déploie avec Kubernetes/Docker Swarm

---

**Le docker-compose.yml fourni est complet et prêt !** 🎉

Lance simplement:
```bash
docker-compose up -d
```

Et tu es prêt à développer ! 🚀
