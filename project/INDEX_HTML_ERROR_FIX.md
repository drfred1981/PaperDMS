# Résolution Erreur "Failed to locate function 'index.html'" - Gateway

## ❌ Erreur

```
Failed to locate function 'index.html' for function definition 'index.html'. 
Returning null.
```

## 🔍 Cause

Le gateway Spring Boot ne trouve pas les fichiers statiques du frontend Angular. Cela arrive quand :

1. Le frontend Angular n'a **pas été compilé**
2. Les fichiers statiques sont **au mauvais endroit**
3. Le profil Spring Boot n'est **pas configuré correctement**

---

## ✅ Solutions (3 méthodes)

---

## Solution 1 : Compiler le Frontend (Méthode Standard)

### Étape 1 : Compile le Frontend Angular

```bash
cd gateway

# Compile le frontend pour la production
npm install
npm run webapp:build:prod

# OU avec Maven (recommandé)
./mvnw -Pprod clean package -DskipTests
```

### Étape 2 : Vérifie que les Fichiers sont Créés

```bash
# Les fichiers doivent être dans target/classes/static
ls -la target/classes/static/

# Tu dois voir :
# index.html
# *.js
# *.css
# assets/
```

### Étape 3 : Redémarre le Gateway

```bash
# En développement
./mvnw spring-boot:run

# En production
./mvnw -Pprod spring-boot:run
```

### Étape 4 : Accède à l'Application

Ouvre http://localhost:8080

✅ **Ça devrait fonctionner !**

---

## Solution 2 : Mode Développement avec npm run start

En développement, il est plus pratique de lancer le frontend et le backend séparément.

### Terminal 1 : Backend (Gateway)

```bash
cd gateway
./mvnw spring-boot:run
```

Le backend démarre sur : http://localhost:8080

### Terminal 2 : Frontend (Angular)

```bash
cd gateway
npm install
npm start
```

Le frontend démarre sur : http://localhost:9000

### Configuration

Le frontend Angular (port 9000) fait des appels au backend (port 8080) via un proxy configuré dans `webpack/proxy.conf.js`.

### Accès

- **Frontend** : http://localhost:9000 ← **Utilise celui-ci en dev**
- **Backend API** : http://localhost:8080/api

✅ **Mode développement avec hot reload !**

---

## Solution 3 : Build Production avec Docker

### Étape 1 : Build l'Image Docker

```bash
cd gateway

# Build avec Maven + Jib
./mvnw -Pprod clean verify jib:dockerBuild

# OU avec Dockerfile
./mvnw -Pprod clean package
docker build -t paperdms/gateway:latest .
```

### Étape 2 : Lance avec Docker Compose

```bash
# Depuis la racine du projet
docker-compose -f docker-compose.apps.yml up -d gateway
```

### Étape 3 : Accède à l'Application

Ouvre http://localhost:8080

---

## 🔧 Vérifications et Diagnostics

### Vérification 1 : Fichiers Statiques Présents

```bash
cd gateway

# Après build, vérifie que les fichiers existent
ls -la target/classes/static/

# Tu dois voir :
# drwxr-xr-x  index.html
# drwxr-xr-x  main.*.js
# drwxr-xr-x  polyfills.*.js
# drwxr-xr-x  runtime.*.js
# drwxr-xr-x  styles.*.css
# drwxr-xr-x  assets/
```

### Vérification 2 : Configuration Spring Boot

Dans `application.yml`, vérifie :

```yaml
spring:
  web:
    resources:
      static-locations: classpath:/static/
```

### Vérification 3 : Profil Spring Boot

```bash
# Vérifie le profil actif
./mvnw spring-boot:run

# Dans les logs, tu dois voir :
# The following profiles are active: dev (ou prod)
```

### Vérification 4 : Structure du Projet

```
gateway/
├── src/
│   ├── main/
│   │   ├── java/          # Code backend
│   │   ├── resources/     # Config backend
│   │   │   ├── static/    # Fichiers statiques (si pas de webapp)
│   │   └── webapp/        # Code frontend Angular
│   │       ├── app/
│   │       ├── content/
│   │       └── index.html
│   └── test/
├── target/
│   └── classes/
│       └── static/        # Frontend compilé (après build)
│           └── index.html
├── package.json
├── angular.json
└── pom.xml
```

---

## 📋 Processus de Build Détaillé

### Développement (dev profile)

```bash
# 1. Backend seul
./mvnw spring-boot:run

# 2. Frontend seul
npm start

# Le frontend (9000) appelle le backend (8080)
# Accède à : http://localhost:9000
```

### Production (prod profile)

```bash
# 1. Compile tout (frontend + backend)
./mvnw -Pprod clean package

# Cela fait :
# a) npm install
# b) npm run webapp:build:prod
# c) Copie les fichiers dans target/classes/static/
# d) Package le JAR

# 2. Lance le JAR
java -jar target/*.jar --spring.profiles.active=prod

# OU avec Maven
./mvnw -Pprod spring-boot:run

# Accède à : http://localhost:8080
```

---

## 🐛 Problèmes Courants

### Problème 1 : "npm not found"

```bash
# Installe Node.js et npm
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt-get install -y nodejs

# Vérifie
node --version
npm --version
```

### Problème 2 : "Build failed" avec npm

```bash
# Nettoie le cache npm
rm -rf node_modules package-lock.json
npm cache clean --force
npm install

# Rebuild
npm run webapp:build:prod
```

### Problème 3 : Port 9000 déjà utilisé

```bash
# Change le port dans package.json
"scripts": {
  "start": "ng serve --port 9001"
}

# OU tue le processus
lsof -ti:9000 | xargs kill -9
```

### Problème 4 : CORS errors en dev

Dans `application-dev.yml`, vérifie :

```yaml
jhipster:
  cors:
    allowed-origins: 'http://localhost:9000,http://localhost:9001'
    allowed-methods: '*'
    allowed-headers: '*'
    exposed-headers: 'Authorization,Link,X-Total-Count'
    allow-credentials: true
    max-age: 1800
```

---

## 🎯 Configurations Recommandées

### Pour le Développement

**Méthode 1 : Deux terminaux** (Recommandé)

```bash
# Terminal 1 - Backend
cd gateway
./mvnw spring-boot:run

# Terminal 2 - Frontend
cd gateway
npm start

# Utilise : http://localhost:9000
```

**Avantages** :
- ✅ Hot reload frontend (modifications instantanées)
- ✅ Hot reload backend (Spring DevTools)
- ✅ Logs séparés (facile à débugger)

**Méthode 2 : Tout en un**

```bash
cd gateway
./mvnw -Pprod clean package
./mvnw spring-boot:run

# Utilise : http://localhost:8080
```

**Inconvénient** :
- ❌ Pas de hot reload frontend
- ❌ Rebuild complet à chaque changement

### Pour la Production

```bash
# Build l'image Docker
./mvnw -Pprod clean verify jib:dockerBuild

# Lance avec Docker Compose
docker-compose -f docker-compose.apps.yml up -d gateway

# Utilise : http://localhost:8080
```

---

## 📝 Configuration Maven (pom.xml)

Le `pom.xml` doit avoir le plugin frontend-maven-plugin :

```xml
<plugin>
    <groupId>com.github.eirslett</groupId>
    <artifactId>frontend-maven-plugin</artifactId>
    <version>${frontend-maven-plugin.version}</version>
    <executions>
        <execution>
            <id>install node and npm</id>
            <goals>
                <goal>install-node-and-npm</goal>
            </goals>
        </execution>
        <execution>
            <id>npm install</id>
            <goals>
                <goal>npm</goal>
            </goals>
        </execution>
        <execution>
            <id>webapp build prod</id>
            <goals>
                <goal>npm</goal>
            </goals>
            <configuration>
                <arguments>run webapp:build:prod</arguments>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

## 🔍 Debug : Logs Utiles

### Activer les Logs Spring Boot

Dans `application-dev.yml` :

```yaml
logging:
  level:
    org.springframework.web: DEBUG
    org.springframework.web.servlet.mvc: TRACE
```

### Logs à Surveiller

```bash
# Démarrage du gateway
./mvnw spring-boot:run | grep -i "static\|webapp\|resource"

# Tu devrais voir :
# Mapped URL path [/**] onto handler of type [class o.s.w.s.r.ResourceHttpRequestHandler]
# Locations: [class path resource [static/]]
```

---

## 🚀 Solution Rapide (1 minute)

Si tu veux juste que ça fonctionne **maintenant** :

```bash
cd gateway

# 1. Build complet
./mvnw -Pprod clean package -DskipTests

# 2. Lance le gateway
./mvnw -Pprod spring-boot:run

# 3. Ouvre le navigateur
open http://localhost:8080
```

✅ **Ça devrait marcher !**

---

## 📦 Fichiers à Vérifier

### package.json

```json
{
  "scripts": {
    "start": "ng serve --port 9000",
    "webapp:build:prod": "ng build --configuration production"
  }
}
```

### angular.json

```json
{
  "projects": {
    "gateway": {
      "architect": {
        "build": {
          "options": {
            "outputPath": "target/classes/static"
          }
        }
      }
    }
  }
}
```

### .yo-rc.json (JHipster config)

```json
{
  "generator-jhipster": {
    "clientFramework": "angular",
    "clientPackageManager": "npm"
  }
}
```

---

## 🔧 Alternatives si Rien ne Marche

### Option 1 : Copie Manuelle des Fichiers

```bash
# Compile le frontend
cd gateway
npm install
npm run build

# Les fichiers sont dans dist/
# Copie-les dans target/classes/static/
mkdir -p target/classes/static
cp -r dist/* target/classes/static/

# Redémarre
./mvnw spring-boot:run
```

### Option 2 : Utilise un Serveur Statique Séparé

```bash
# Lance le backend
cd gateway
./mvnw spring-boot:run

# Lance le frontend avec serveur HTTP simple
cd gateway/src/main/webapp
npx http-server -p 9000 -P http://localhost:8080

# Accède à : http://localhost:9000
```

### Option 3 : Utilise le Mode Dev avec Proxy

**webpack/proxy.conf.js** (déjà configuré par JHipster) :

```javascript
module.exports = {
  '/api': {
    target: 'http://localhost:8080',
    secure: false
  },
  '/services': {
    target: 'http://localhost:8080',
    secure: false
  }
};
```

```bash
# Frontend avec proxy
npm start

# Le frontend (9000) proxie les appels API vers le backend (8080)
```

---

## ✅ Checklist de Résolution

- [ ] Node.js et npm installés (`node --version`)
- [ ] Dépendances npm installées (`npm install`)
- [ ] Frontend compilé (`npm run webapp:build:prod`)
- [ ] Fichiers dans `target/classes/static/` (`ls target/classes/static/`)
- [ ] Profil Spring Boot correct (`dev` ou `prod`)
- [ ] Port 8080 libre (`lsof -ti:8080`)
- [ ] Logs Spring Boot sans erreur
- [ ] CORS configuré pour dev (`allowed-origins`)

---

## 📚 Résumé

### Le Problème
Spring Boot ne trouve pas `index.html` car le frontend n'est pas compilé.

### La Solution Rapide
```bash
./mvnw -Pprod clean package -DskipTests
./mvnw spring-boot:run
```

### La Solution Dev (Recommandée)
```bash
# Terminal 1
./mvnw spring-boot:run

# Terminal 2
npm start

# Utilise http://localhost:9000
```

### La Solution Prod
```bash
./mvnw -Pprod clean verify jib:dockerBuild
docker-compose -f docker-compose.apps.yml up -d
```

---

✅ **Avec ces solutions, ton frontend Angular devrait s'afficher correctement !** 🚀
