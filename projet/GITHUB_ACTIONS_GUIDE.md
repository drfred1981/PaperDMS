# Guide GitHub Actions CI/CD - PaperDMS

## 🎯 Objectif

Automatiser le build, les tests et le déploiement de tous les microservices PaperDMS avec GitHub Actions.

---

## 📋 Table des Matières

1. [Vue d'Ensemble](#vue-densemble)
2. [Workflows Disponibles](#workflows-disponibles)
3. [Configuration Requise](#configuration-requise)
4. [Utilisation](#utilisation)
5. [Déclencheurs](#déclencheurs)
6. [Personnalisation](#personnalisation)
7. [Troubleshooting](#troubleshooting)

---

## 🔍 Vue d'Ensemble

### Architecture du Pipeline

```
┌─────────────────────────────────────────────────────────────┐
│                    GitHub Actions Pipeline                   │
└─────────────────────────────────────────────────────────────┘
                              │
                ┌─────────────┴─────────────┐
                │                           │
        ┌───────▼────────┐          ┌──────▼──────┐
        │ Build Maven    │          │  Detect     │
        │ Packages       │          │  Changes    │
        │ (paperdms-     │          │  (Optional) │
        │  common)       │          └──────┬──────┘
        └───────┬────────┘                 │
                │                          │
                └──────────┬───────────────┘
                           │
                ┌──────────▼──────────────────────────┐
                │   Build Services (Parallel)         │
                │   ├── gateway                       │
                │   ├── documentService               │
                │   ├── ocrService                    │
                │   ├── searchService                 │
                │   ├── aiService                     │
                │   ├── workflowService               │
                │   └── ... (15 services total)       │
                └──────────┬──────────────────────────┘
                           │
                ┌──────────▼──────────┐
                │  Docker Images      │
                │  (Tag/Main only)    │
                └──────────┬──────────┘
                           │
                ┌──────────▼──────────┐
                │  GitHub Release     │
                │  (Tags only)        │
                └─────────────────────┘
```

---

## 📦 Workflows Disponibles

### 1. ci-cd-pipeline.yml (Standard)

**Caractéristiques** :
- ✅ Build complet de tous les services
- ✅ Build parallèle (jusqu'à 5 services simultanés)
- ✅ Génération d'images Docker
- ✅ Publication sur Docker Hub
- ✅ Création de releases GitHub

**Utilisation** : Production, releases, tags

### 2. ci-cd-optimized.yml (Optimisé)

**Caractéristiques** :
- ✅ Build conditionnel (uniquement services modifiés)
- ✅ Cache Maven et npm
- ✅ Docker layer caching
- ✅ Publication sur GitHub Container Registry
- ✅ Plus rapide pour développement

**Utilisation** : Développement, pull requests, builds fréquents

---

## ⚙️ Configuration Requise

### Secrets GitHub à Configurer

#### Pour Docker Hub (ci-cd-pipeline.yml)

```bash
# Dans GitHub: Settings → Secrets and variables → Actions → New repository secret

DOCKER_USERNAME: votre_username_dockerhub
DOCKER_PASSWORD: votre_token_dockerhub
```

**Comment obtenir le token Docker Hub** :
1. Connecte-toi sur https://hub.docker.com
2. Account Settings → Security → New Access Token
3. Copie le token généré

#### Pour GitHub Container Registry (ci-cd-optimized.yml)

Pas de secret requis ! GitHub utilise automatiquement `GITHUB_TOKEN`.

### Structure du Projet Requise

```
paperdms/
├── .github/
│   └── workflows/
│       ├── ci-cd-pipeline.yml
│       └── ci-cd-optimized.yml
│
├── paperdms-common/
│   ├── pom.xml
│   └── src/
│
├── gateway/
│   ├── pom.xml
│   ├── package.json
│   └── src/
│
├── documentService/
│   ├── pom.xml
│   └── src/
│
├── ocrService/
│   ├── pom.xml
│   └── src/
│
└── ... (autres services)
```

---

## 🚀 Utilisation

### Méthode 1 : Push avec Tag (Recommandé pour Production)

```bash
# 1. Commit tes changements
git add .
git commit -m "Release v1.0.0"

# 2. Crée un tag
git tag v1.0.0

# 3. Push le tag
git push origin v1.0.0
```

**Résultat** :
- ✅ Build de tous les services
- ✅ Tests exécutés
- ✅ Images Docker créées avec tag `v1.0.0` et `latest`
- ✅ Release GitHub créée
- ✅ Artifacts (JAR) attachés à la release

### Méthode 2 : Push sur Branch (Développement)

```bash
# Push sur main ou develop
git push origin main
```

**Résultat (ci-cd-optimized.yml)** :
- ✅ Build uniquement des services modifiés
- ✅ Tests exécutés
- ✅ Images Docker créées avec tag `latest`
- ❌ Pas de release créée

### Méthode 3 : Pull Request

```bash
# Crée une pull request vers main ou develop
```

**Résultat** :
- ✅ Build et tests des services modifiés
- ❌ Pas d'images Docker
- ❌ Pas de release

### Méthode 4 : Déclenchement Manuel

Dans GitHub :
1. Actions → Select workflow
2. Run workflow
3. (Optionnel) Choisis les services à builder

---

## 🎯 Déclencheurs

### ci-cd-pipeline.yml

| Événement | Action | Build | Docker | Release |
|-----------|--------|-------|--------|---------|
| Tag `v*.*.*` | Push | Tous | ✅ | ✅ |
| Push `main` | Push | Tous | ✅ | ❌ |
| Push `develop` | Push | Tous | ❌ | ❌ |
| Pull Request | PR | Tous | ❌ | ❌ |
| Manuel | Click | Personnalisé | ❌ | ❌ |

### ci-cd-optimized.yml

| Événement | Action | Build | Docker | Release |
|-----------|--------|-------|--------|---------|
| Tag `v*.*.*` | Push | Tous | ✅ | ✅ |
| Push `main` | Push | Modifiés | ✅ | ❌ |
| Push `develop` | Push | Modifiés | ❌ | ❌ |
| Pull Request | PR | Modifiés | ❌ | ❌ |

---

## 🔧 Personnalisation

### Changer la Liste des Services

Dans `.github/workflows/ci-cd-pipeline.yml` :

```yaml
env:
  # Ajoute ou supprime des services
  SERVICES: >-
    gateway
    documentService
    monNouveauService
    # Commente pour désactiver
    # ocrService
```

### Changer la Liste des Packages Maven

```yaml
env:
  # Ajoute des packages communs
  MAVEN_PACKAGES: 'paperdms-common,paperdms-shared,paperdms-utils'
```

### Changer les Versions Java/Node

```yaml
env:
  JAVA_VERSION: '21'    # Java 21 au lieu de 17
  NODE_VERSION: '20'    # Node 20 au lieu de 18
```

### Changer le Registre Docker

```yaml
env:
  # GitHub Container Registry
  REGISTRY: ghcr.io
  IMAGE_PREFIX: ${{ github.repository_owner }}/paperdms
  
  # Ou Docker Hub
  REGISTRY: docker.io
  IMAGE_PREFIX: votre-username
```

### Ajouter des Étapes Personnalisées

Ajoute une étape après le build :

```yaml
- name: Ma Nouvelle Étape
  working-directory: ${{ matrix.service }}
  run: |
    echo "Exécution de ma commande personnalisée"
    # Tes commandes ici
```

### Configurer les Notifications

Ajoute à la fin du workflow :

```yaml
  # Notification Slack
  notify-slack:
    name: Notify Slack
    needs: build-services
    if: always()
    runs-on: ubuntu-latest
    steps:
      - name: Send Slack notification
        uses: 8398a7/action-slack@v3
        with:
          status: ${{ job.status }}
          webhook_url: ${{ secrets.SLACK_WEBHOOK }}
          text: |
            Build ${{ job.status }}
            Commit: ${{ github.sha }}
            Author: ${{ github.actor }}
```

---

## 📊 Monitoring et Logs

### Voir l'Exécution

1. GitHub → Actions tab
2. Clique sur le workflow
3. Clique sur le run spécifique
4. Voir les détails de chaque job

### Télécharger les Artifacts

1. Dans un run terminé
2. Section "Artifacts"
3. Télécharge :
   - `maven-packages` : JAR des librairies communes
   - `[service]-jar` : JAR de chaque service
   - `test-results-[service]` : Résultats des tests

### Voir les Images Docker

#### Docker Hub
```bash
# Liste les images
docker pull paperdms/gateway:latest
docker pull paperdms/documentService:v1.0.0
```

#### GitHub Container Registry
```bash
# Login
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Pull
docker pull ghcr.io/username/paperdms-gateway:latest
```

---

## 🐛 Troubleshooting

### Problème 1 : "Failed to build paperdms-common"

**Cause** : Erreur dans le package commun

**Solution** :
```bash
# Teste localement
cd paperdms-common
mvn clean install

# Vérifie les logs GitHub Actions
```

### Problème 2 : "Docker push failed - unauthorized"

**Cause** : Secrets Docker mal configurés

**Solution** :
```bash
# Vérifie les secrets GitHub
Settings → Secrets → DOCKER_USERNAME et DOCKER_PASSWORD

# Teste le login localement
docker login
```

### Problème 3 : Service specific build failed

**Cause** : Erreur dans un service spécifique

**Solution** :
```bash
# Build localement pour debugger
cd serviceEnErreur
mvn clean package -Pprod

# Vérifie les dépendances Maven
mvn dependency:tree
```

### Problème 4 : "Out of memory" pendant le build

**Cause** : Trop de builds parallèles

**Solution** : Réduis `max-parallel`
```yaml
strategy:
  max-parallel: 3  # Au lieu de 5
```

### Problème 5 : Cache invalide

**Solution** : Clear le cache
```bash
# Dans GitHub Actions
# Settings → Actions → Caches → Delete cache

# Ou ajoute dans le workflow
- name: Clear cache
  run: |
    rm -rf ~/.m2/repository
    npm cache clean --force
```

### Problème 6 : Frontend build failed (gateway)

**Cause** : npm dependencies ou build Angular

**Solution** :
```bash
# Teste localement
cd gateway
npm install
npm run webapp:build:prod

# Vérifie Node version
node --version  # Doit être 18+
```

---

## 📝 Variables d'Environnement

### Variables Disponibles dans le Workflow

```yaml
# GitHub predefined
${{ github.ref }}              # refs/heads/main ou refs/tags/v1.0.0
${{ github.sha }}              # Commit SHA
${{ github.actor }}            # Username qui a déclenché
${{ github.event_name }}       # push, pull_request, etc.

# Custom
${{ env.JAVA_VERSION }}        # 17
${{ env.NODE_VERSION }}        # 18
${{ matrix.service }}          # gateway, documentService, etc.
${{ steps.version.outputs.version }}  # Version extraite
```

### Ajouter des Variables

```yaml
env:
  MA_VARIABLE: 'valeur'
  
jobs:
  mon-job:
    env:
      VARIABLE_JOB: 'autre valeur'
    steps:
      - name: Utilise les variables
        run: |
          echo ${{ env.MA_VARIABLE }}
          echo $VARIABLE_JOB
```

---

## 🎓 Best Practices

### 1. Utilise des Branches Protégées

```bash
# Settings → Branches → Add rule
# Branch name pattern: main
# ✅ Require pull request reviews
# ✅ Require status checks (CI/CD)
```

### 2. Utilise des Tags Semantiques

```bash
# Suit Semantic Versioning
git tag v1.0.0      # Major release
git tag v1.1.0      # Minor release
git tag v1.1.1      # Patch release
```

### 3. Commit Conventionnels

```bash
git commit -m "feat(documentService): add PDF upload"
git commit -m "fix(gateway): resolve CORS issue"
git commit -m "chore: update dependencies"
```

### 4. Tests Avant Push

```bash
# Build localement avant de pusher
mvn clean install

# Ou utilise pre-commit hooks
```

### 5. Cache Approprié

```yaml
# Cache Maven
- uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}

# Cache npm
- uses: actions/cache@v3
  with:
    path: node_modules
    key: ${{ runner.os }}-node-${{ hashFiles('**/package-lock.json') }}
```

---

## 📚 Exemples d'Utilisation

### Exemple 1 : Release Complète

```bash
# 1. Développe et teste
git checkout -b feature/new-upload
# ... code ...
git commit -m "feat(documentService): improve upload"
git push origin feature/new-upload

# 2. Crée PR → Tests automatiques

# 3. Merge dans main
# ... merge PR ...

# 4. Crée release
git checkout main
git pull
git tag v1.2.0
git push origin v1.2.0

# → Build complet + Docker images + GitHub release
```

### Exemple 2 : Hotfix Rapide

```bash
# 1. Fix sur main
git checkout main
git checkout -b hotfix/critical-bug
# ... fix ...
git commit -m "fix(gateway): critical security issue"

# 2. PR et merge
# ... PR → merge ...

# 3. Tag immédiatement
git tag v1.2.1
git push origin v1.2.1

# → Build + deploy automatique
```

### Exemple 3 : Développement Quotidien

```bash
# 1. Branch de feature
git checkout -b feature/my-feature

# 2. Commits réguliers
git commit -m "wip: add new feature"
git push origin feature/my-feature

# → Pas de CI/CD déclenché (pas de PR)

# 3. Crée PR quand prêt
# → Tests automatiques sur services modifiés uniquement
```

---

## ✅ Checklist de Configuration

- [ ] Fichiers workflow copiés dans `.github/workflows/`
- [ ] Secrets Docker configurés (si Docker Hub)
- [ ] Structure projet correcte (tous services présents)
- [ ] `pom.xml` contient le plugin Jib dans chaque service
- [ ] Tests unitaires présents dans chaque service
- [ ] Branches protégées configurées
- [ ] Notifications configurées (optionnel)

---

## 🔗 Ressources

- **GitHub Actions Docs** : https://docs.github.com/en/actions
- **Jib Maven Plugin** : https://github.com/GoogleContainerTools/jib
- **Docker Hub** : https://hub.docker.com
- **GitHub Container Registry** : https://ghcr.io

---

## 📖 Résumé

### Setup Initial

```bash
# 1. Copie les workflows
mkdir -p .github/workflows
cp ci-cd-pipeline.yml .github/workflows/
cp ci-cd-optimized.yml .github/workflows/

# 2. Configure les secrets
# GitHub → Settings → Secrets → New

# 3. Commit et push
git add .github/
git commit -m "ci: add GitHub Actions workflows"
git push
```

### Utilisation Quotidienne

```bash
# Développement : Push sur develop
git push origin develop

# Release : Crée un tag
git tag v1.0.0
git push origin v1.0.0
```

---

✅ **Avec ces workflows, ton pipeline CI/CD est 100% automatisé !** 🚀
