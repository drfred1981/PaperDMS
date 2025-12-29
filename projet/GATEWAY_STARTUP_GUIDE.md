# 🚀 Guide Démarrage Gateway - Sans Erreurs

## ⚡ Démarrage Rapide (5 minutes)

Voici comment démarrer le gateway **sans erreurs** dès le premier lancement.

---

## 📋 Prérequis

```bash
# Vérifie les versions
node --version    # >= 18.x
npm --version     # >= 9.x
java --version    # >= 17
mvn --version     # >= 3.8
```

Si manquant :
```bash
# Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Java 17
sudo apt install openjdk-17-jdk

# Maven (si pas inclus dans ./mvnw)
sudo apt install maven
```

---

## 🎯 Méthode 1 : Mode Développement (Recommandé)

### Configuration Initiale (Une fois seulement)

#### 1. Configure la Mail

Crée `gateway/src/main/resources/config/application-dev.yml` (s'il n'existe pas) :

```yaml
spring:
  mail:
    host: localhost
    port: 1025
```

#### 2. Démarre l'Infrastructure

```bash
# Depuis la racine du projet
docker-compose up -d
```

Attends que tous les services soient UP :
```bash
docker-compose ps
```

#### 3. Crée le Bucket MinIO

```bash
# Via l'interface web
open http://localhost:9001
# Login: minioadmin / minioadmin
# Crée le bucket: paperdms-documents

# OU via CLI
docker exec paperdms-minio mc mb /data/paperdms-documents
```

### Démarrage Quotidien

#### Terminal 1 : Backend Gateway

```bash
cd gateway
./mvnw spring-boot:run
```

Attends le message : `Started GatewayApp in X.XX seconds`

#### Terminal 2 : Frontend Gateway

```bash
cd gateway
npm install  # Première fois seulement
npm start
```

Attends le message : `Compiled successfully`

### ✅ Accès

- **Application** : http://localhost:9000
- **API Backend** : http://localhost:8080/api
- **Swagger** : http://localhost:8080/api/v3/api-docs

---

## 🎯 Méthode 2 : Tout-en-Un (Production-like)

### 1. Build Complet

```bash
cd gateway

# Build frontend + backend
./mvnw -Pprod clean package -DskipTests

# Cela prend 2-3 minutes la première fois
```

### 2. Démarre l'Infrastructure

```bash
cd ..
docker-compose up -d
```

### 3. Lance le Gateway

```bash
cd gateway
./mvnw -Pprod spring-boot:run
```

### ✅ Accès

- **Application** : http://localhost:8080

---

## 🎯 Méthode 3 : Tout dans Docker

### 1. Build l'Image Docker

```bash
cd gateway

# Build avec Jib
./mvnw -Pprod clean verify jib:dockerBuild

# Vérifie l'image
docker images | grep gateway
```

### 2. Lance avec Docker Compose

```bash
cd ..

# Infrastructure
docker-compose up -d

# Applications
docker-compose -f docker-compose.apps.yml up -d
```

### ✅ Accès

- **Application** : http://localhost:8080
- **Logs** : `docker logs -f paperdms-gateway`

---

## 🔧 Configuration Minimale Requise

### application-dev.yml (Development)

Fichier : `gateway/src/main/resources/config/application-dev.yml`

```yaml
spring:
  mail:
    host: localhost
    port: 1025
    from: noreply@paperdms.local
    base-url: http://localhost:8080

jhipster:
  cors:
    allowed-origins: 'http://localhost:9000,http://localhost:4200'
    allowed-methods: '*'
    allowed-headers: '*'
    allow-credentials: true
```

### application-prod.yml (Production)

Fichier : `gateway/src/main/resources/config/application-prod.yml`

```yaml
spring:
  mail:
    host: ${SPRING_MAIL_HOST:maildev}
    port: ${SPRING_MAIL_PORT:1025}
    username: ${SPRING_MAIL_USERNAME:}
    password: ${SPRING_MAIL_PASSWORD:}
    from: ${SPRING_MAIL_FROM:noreply@paperdms.com}
    base-url: ${SPRING_MAIL_BASE_URL:http://localhost:8080}
```

---

## ❌ Erreurs Courantes et Solutions

### Erreur 1 : "Failed to locate function 'index.html'"

**Cause** : Frontend pas compilé

**Solution** :
```bash
cd gateway
npm install
npm run webapp:build:prod
./mvnw spring-boot:run
```

**OU utilise le mode dev** :
```bash
# Terminal 1
./mvnw spring-boot:run

# Terminal 2
npm start

# Accède à http://localhost:9000
```

### Erreur 2 : "JavaMailSender could not be found"

**Cause** : Configuration mail manquante

**Solution** : Ajoute dans `application.yml` :
```yaml
spring:
  mail:
    host: localhost
    port: 1025
```

Et démarre MailDev :
```bash
docker-compose up -d maildev
```

### Erreur 3 : Port 8080 déjà utilisé

```bash
# Trouve et tue le processus
lsof -ti:8080 | xargs kill -9

# OU change le port
./mvnw spring-boot:run -Dserver.port=8081
```

### Erreur 4 : Port 9000 déjà utilisé

```bash
# Trouve et tue le processus
lsof -ti:9000 | xargs kill -9

# OU dans package.json change le port
"start": "ng serve --port 9001"
```

### Erreur 5 : "Cannot connect to PostgreSQL"

```bash
# Vérifie que PostgreSQL tourne
docker-compose ps postgresql

# Redémarre si nécessaire
docker-compose restart postgresql

# Vérifie la connexion
docker exec paperdms-postgresql pg_isready
```

### Erreur 6 : "npm not found"

```bash
# Installe Node.js
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs
```

---

## 📊 Services Requis

Avant de lancer le gateway, assure-toi que ces services tournent :

```bash
# Vérifie tous les services
docker-compose ps

# Tous doivent être "Up" et "healthy" :
✅ postgresql    (5432)
✅ redis         (6379)
✅ kafka         (9092)
✅ consul        (8500)
✅ maildev       (1025, 1080)
```

Si un service n'est pas healthy :
```bash
# Redémarre le service
docker-compose restart [service-name]

# Vois les logs
docker-compose logs -f [service-name]
```

---

## 🔍 Vérifications Post-Démarrage

### 1. Backend Gateway Démarré

```bash
# Health check
curl http://localhost:8080/management/health

# Résultat attendu :
{"status":"UP"}
```

### 2. Frontend Accessible

```bash
# Dev mode
curl http://localhost:9000

# Prod mode
curl http://localhost:8080

# Résultat : HTML de l'application
```

### 3. Services Connectés

```bash
# Consul
curl http://localhost:8500/v1/catalog/services

# Tu dois voir : gateway, documentservice, etc.
```

### 4. Logs Sans Erreurs

```bash
# Backend
./mvnw spring-boot:run | grep -i error

# Frontend
npm start | grep -i error

# Aucune erreur = ✅
```

---

## 📁 Structure de Projet Attendue

```
gateway/
├── src/
│   ├── main/
│   │   ├── java/                    # Backend Spring Boot
│   │   ├── resources/
│   │   │   └── config/
│   │   │       ├── application.yml
│   │   │       ├── application-dev.yml   # ← Ajoute config mail ici
│   │   │       └── application-prod.yml  # ← Ajoute config mail ici
│   │   └── webapp/                  # Frontend Angular
│   │       ├── app/
│   │       ├── index.html
│   │       └── main.ts
│   └── test/
├── target/
│   └── classes/
│       └── static/                  # Frontend compilé (après build)
├── node_modules/                    # Dépendances npm
├── package.json
├── angular.json
├── tsconfig.json
└── pom.xml
```

---

## 🎓 Best Practices

### Développement

✅ **Utilise le mode 2 terminaux** :
- Terminal 1 : `./mvnw spring-boot:run`
- Terminal 2 : `npm start`
- Accède à : http://localhost:9000

**Avantages** :
- Hot reload frontend instantané
- Hot reload backend avec DevTools
- Logs séparés, facile à débugger

### Production

✅ **Build complet avant déploiement** :
```bash
./mvnw -Pprod clean verify jib:dockerBuild
```

### Infrastructure

✅ **Démarre toujours l'infrastructure en premier** :
```bash
docker-compose up -d
docker-compose ps  # Vérifie que tout est UP
```

---

## 🚨 Checklist Avant de Lancer

- [ ] Docker Desktop / Docker Engine démarré
- [ ] `docker-compose up -d` exécuté
- [ ] Tous les services "healthy" (`docker-compose ps`)
- [ ] Bucket MinIO créé (`paperdms-documents`)
- [ ] Configuration mail ajoutée dans `application.yml`
- [ ] Node.js >= 18 installé
- [ ] Java >= 17 installé
- [ ] Ports libres : 8080, 9000, 5432, 6379, 9092

---

## 💡 Commandes Utiles

### Nettoyage

```bash
# Nettoie Maven
./mvnw clean

# Nettoie npm
rm -rf node_modules package-lock.json
npm cache clean --force

# Nettoie Docker
docker-compose down
docker system prune -a
```

### Redémarrage Complet

```bash
# 1. Arrête tout
docker-compose down
pkill -f "spring-boot:run"
pkill -f "ng serve"

# 2. Redémarre infrastructure
docker-compose up -d
sleep 30  # Attends que tout démarre

# 3. Redémarre gateway
cd gateway
./mvnw spring-boot:run  # Terminal 1
npm start               # Terminal 2
```

### Rebuild Complet

```bash
cd gateway

# Full clean + build
./mvnw clean
rm -rf node_modules
npm install
./mvnw -Pprod package
```

---

## 📖 Résumé

### Mode Dev (Développement Quotidien)

```bash
# Infrastructure (une fois)
docker-compose up -d

# Terminal 1 - Backend
cd gateway && ./mvnw spring-boot:run

# Terminal 2 - Frontend  
cd gateway && npm start

# Accès : http://localhost:9000
```

### Mode Prod (Test Production)

```bash
# Infrastructure
docker-compose up -d

# Build + Run
cd gateway
./mvnw -Pprod clean package
./mvnw -Pprod spring-boot:run

# Accès : http://localhost:8080
```

### Mode Docker (Production)

```bash
# Build image
./mvnw -Pprod clean verify jib:dockerBuild

# Run
docker-compose up -d
docker-compose -f docker-compose.apps.yml up -d

# Accès : http://localhost:8080
```

---

✅ **Avec ce guide, tu devrais démarrer sans aucune erreur !** 🚀

**En cas de problème** :
1. Consulte `INDEX_HTML_ERROR_FIX.md`
2. Consulte `JAVAMAIL_ERROR_FIX.md`
3. Vérifie `docker-compose ps`
4. Regarde les logs : `docker-compose logs -f`
