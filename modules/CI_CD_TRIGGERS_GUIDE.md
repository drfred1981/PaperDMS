# Guide Déclencheurs CI/CD - PaperDMS

## 🎯 Comportement des Workflows

Ce guide explique **exactement** ce qui se passe dans chaque scénario.

---

## 📋 Résumé Rapide

| Action | Build Maven | Build Services | Tests | Docker Images | GitHub Release |
|--------|-------------|----------------|-------|---------------|----------------|
| **Tag** `v1.0.0` | ✅ | ✅ | ✅ | ✅ | ✅ |
| **Push** `main` | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Push** `develop` | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Pull Request** | ✅ | ✅ | ✅ | ❌ | ❌ |
| **Manuel** | ✅ | ✅ | ✅ | ❌ | ❌ |

---

## 🎯 Scénarios Détaillés

### Scénario 1 : Push d'un Tag (Production Release)

```bash
git tag v1.0.0
git push origin v1.0.0
```

**Ce qui se passe** :

#### Étape 1 : Build Maven Packages
```
✅ Build paperdms-common
✅ Run tests
✅ Upload artifacts Maven
```

#### Étape 2 : Build Services (15 services en parallèle)
```
✅ Build gateway (avec frontend Angular)
✅ Build documentService
✅ Build ocrService
✅ Build searchService
✅ Build aiService
✅ Build workflowService
✅ Build notificationService
✅ Build emailService
✅ Build invoiceService
✅ Build receiptService
✅ Build contractService
✅ Build reportService
✅ Build archiveService
✅ Build auditService
✅ Build backupService

Pour chaque service :
  ✅ Download Maven artifacts
  ✅ Build with Maven (-Pprod)
  ✅ Run tests
  ✅ Upload JAR artifact
  ✅ 🐳 Build Docker image
  ✅ 🐳 Push to registry (GHCR ou Docker Hub)
```

#### Étape 3 : Create Release
```
✅ Download all JARs
✅ Create GitHub Release v1.0.0
✅ Attach all JAR files
✅ Generate release notes
```

**Résultat Final** :
- ✅ 15 JARs créés et uploadés
- ✅ 15 images Docker créées :
  - `ghcr.io/username/paperdms-gateway:v1.0.0`
  - `ghcr.io/username/paperdms-gateway:latest`
  - `ghcr.io/username/paperdms-documentservice:v1.0.0`
  - `ghcr.io/username/paperdms-documentservice:latest`
  - etc.
- ✅ GitHub Release créée avec tous les JARs
- ✅ Prêt pour déploiement en production

---

### Scénario 2 : Push sur main (CI/CD Continu)

```bash
git push origin main
```

**Ce qui se passe** :

#### Étape 1 : Build Maven Packages
```
✅ Build paperdms-common
✅ Run tests
✅ Upload artifacts Maven
```

#### Étape 2 : Build Services
```
✅ Build tous les services (ou seulement modifiés avec optimized.yml)
✅ Run tests sur tous les services
✅ Upload JARs

Pour chaque service :
  ✅ Download Maven artifacts
  ✅ Build with Maven (-Pprod)
  ✅ Run tests
  ✅ Upload JAR artifact
  ❌ Pas de build Docker
  ❌ Pas de push registry
```

#### Étape 3 : Pas de Release
```
❌ Pas de GitHub Release créée
```

**Résultat Final** :
- ✅ Code compilé et testé
- ✅ JARs disponibles en artifacts (7 jours)
- ❌ Pas d'images Docker
- ❌ Pas de release
- ✅ Validation que tout compile et passe les tests

**Usage** : Vérification continue, détection précoce des bugs

---

### Scénario 3 : Push sur develop

```bash
git push origin develop
```

**Ce qui se passe** :

Identique au push sur `main` :

```
✅ Build Maven packages
✅ Build services
✅ Run tests
✅ Upload JARs (artifacts temporaires)
❌ Pas d'images Docker
❌ Pas de release
```

**Usage** : Validation avant merge dans main

---

### Scénario 4 : Pull Request

```bash
# Crée une PR vers main ou develop
```

**Ce qui se passe** :

```
✅ Build Maven packages
✅ Build services modifiés (avec optimized.yml)
✅ Run tests
✅ Upload test results
❌ Pas d'images Docker
❌ Pas de release
❌ Pas d'upload JARs (PR peut être rejetée)
```

**Résultat Final** :
- ✅ Validation que le code compile
- ✅ Tests passent
- ✅ Prêt pour review et merge
- ❌ Pas d'artifacts persistants

**Usage** : Validation avant merge, code review

---

### Scénario 5 : Déclenchement Manuel

```
GitHub → Actions → Select workflow → Run workflow
```

**Ce qui se passe** :

```
✅ Build Maven packages
✅ Build services (tous ou sélectionnés)
✅ Run tests (ou skip si option cochée)
✅ Upload JARs
❌ Pas d'images Docker
❌ Pas de release
```

**Options disponibles** :
- Services à builder (vide = tous)
- Skip tests (true/false)

**Usage** : Tests manuels, debugging, builds spécifiques

---

## 🐳 Création des Images Docker

### Condition Stricte : Tags Uniquement

Les images Docker ne sont créées **QUE** si :

```yaml
if: startsWith(github.ref, 'refs/tags/')
```

**Cela signifie** :
- ✅ `git push origin v1.0.0` → Images créées
- ✅ `git push origin v2.1.5` → Images créées
- ✅ `git push origin release-1.0` → Images créées (si tag)
- ❌ `git push origin main` → Pas d'images
- ❌ `git push origin develop` → Pas d'images
- ❌ Pull Request → Pas d'images
- ❌ Déclenchement manuel → Pas d'images

### Pourquoi ?

1. **Images Docker = Déploiement Production**
   - Les tags représentent des versions stables
   - Évite la pollution du registry
   - Économie d'espace et de bande passante

2. **Versioning Sémantique**
   - Chaque image a un tag de version précis
   - Traçabilité : `v1.0.0` = commit exact

3. **Performance**
   - Build Docker prend du temps (~5-10 min par service)
   - Sur main, on veut juste valider rapidement

---

## 📊 Temps d'Exécution Estimés

### Push Tag (Full Pipeline)

```
Build Maven Packages:  ~3 minutes
Build Services (x15):  ~15 minutes (parallèle)
Docker Images (x15):   ~10 minutes (parallèle)
Create Release:        ~1 minute
────────────────────────────────────────
Total:                 ~20-25 minutes
```

### Push Main (Sans Docker)

```
Build Maven Packages:  ~3 minutes
Build Services (x15):  ~15 minutes (parallèle)
────────────────────────────────────────
Total:                 ~10-15 minutes
```

### Pull Request (Optimized - Services Modifiés)

```
Build Maven Packages:  ~3 minutes (si modifié)
Build Services (x2):   ~3 minutes (seulement 2 services)
────────────────────────────────────────
Total:                 ~5-8 minutes
```

---

## 🎯 Workflows et Comportements

### ci-cd-pipeline.yml (Standard)

**Build Services** :
- ✅ Tous les services à chaque fois
- ✅ Build complet, garanti

**Docker** :
- ✅ Uniquement sur tags

**Usage** : Production, releases

### ci-cd-optimized.yml (Optimisé)

**Build Services** :
- ✅ Détection automatique des changements
- ✅ Build uniquement services modifiés
- ✅ Cache Maven et npm

**Docker** :
- ✅ Uniquement sur tags

**Usage** : Développement quotidien, PRs

---

## 🔍 Vérification des Builds

### Voir ce qui a été exécuté

```
GitHub → Actions → Clique sur un run
```

**Pour un Tag** :
```
✅ build-maven-packages (completed)
✅ build-services (completed)
   ├── gateway (completed) + 🐳 Docker built
   ├── documentService (completed) + 🐳 Docker built
   ├── ocrService (completed) + 🐳 Docker built
   └── ... (15 services total)
✅ publish-release (completed)
```

**Pour un Push Main** :
```
✅ build-maven-packages (completed)
✅ build-services (completed)
   ├── gateway (completed) - No Docker
   ├── documentService (completed) - No Docker
   ├── ocrService (completed) - No Docker
   └── ... (15 services total)
❌ publish-release (skipped - not a tag)
```

**Pour une PR** :
```
✅ detect-changes (completed)
✅ build-maven-packages (skipped - no changes)
✅ build-services (completed)
   ├── gateway (completed) - No Docker
   ├── documentService (skipped - no changes)
   ├── ocrService (skipped - no changes)
   └── ... 
❌ publish-release (skipped - not a tag)
```

---

## 📦 Artifacts Disponibles

### Tags

**Durée** : Permanent (dans GitHub Release)

**Contenu** :
- JARs de tous les services
- Images Docker dans registry
- Release notes

**Accès** :
- GitHub → Releases → v1.0.0
- GitHub → Packages (images Docker)

### Push Main/Develop

**Durée** : 7 jours (artifacts temporaires)

**Contenu** :
- JARs de tous les services
- Test results

**Accès** :
- GitHub → Actions → Specific run → Artifacts

### Pull Request

**Durée** : 7 jours

**Contenu** :
- Test results seulement

**Accès** :
- GitHub → Actions → PR run → Artifacts

---

## 🎓 Best Practices

### 1. Développement Quotidien

```bash
# Travaille sur une feature branch
git checkout -b feature/my-feature

# Push régulièrement (pas de CI/CD déclenché)
git push origin feature/my-feature

# Crée une PR quand prêt
# → CI/CD teste uniquement services modifiés
```

### 2. Merge dans Main

```bash
# Après review et approval
git checkout main
git merge feature/my-feature
git push origin main

# → CI/CD build et teste tout
# → Validation complète
# → Pas d'images Docker (pas encore en prod)
```

### 3. Release en Production

```bash
# Quand prêt pour production
git checkout main
git pull

# Crée un tag sémantique
git tag v1.2.0

# Push le tag
git push origin v1.2.0

# → CI/CD complet :
#   - Build tout
#   - Teste tout
#   - Crée images Docker
#   - Publie release GitHub
#   - Prêt pour déploiement
```

### 4. Hotfix Urgent

```bash
# Fix directement sur main
git checkout main
git checkout -b hotfix/critical-bug

# Fix et commit
git commit -m "fix: critical security issue"

# Merge
git push origin hotfix/critical-bug
# → PR → Review rapide → Merge

# Tag immédiatement
git checkout main
git pull
git tag v1.2.1
git push origin v1.2.1

# → Images Docker créées
# → Deploy immédiatement
```

---

## ✅ Checklist

### Pour Développer
- [ ] Crée une feature branch
- [ ] Développe et commit
- [ ] Crée une PR
- [ ] CI/CD teste automatiquement
- [ ] Review et merge

### Pour Deployer
- [ ] Code mergé dans main
- [ ] Tous les tests passent
- [ ] Crée un tag sémantique (`v1.0.0`)
- [ ] Push le tag
- [ ] CI/CD crée images Docker
- [ ] Vérifie la release GitHub
- [ ] Deploy en production

---

## 📖 Résumé

### Build et Tests

**Tous les événements** (tags, main, develop, PR) :
- ✅ Build Maven packages
- ✅ Build services
- ✅ Run tests

### Images Docker

**Uniquement sur tags** :
- ✅ Build images Docker
- ✅ Push to registry

### GitHub Release

**Uniquement sur tags** :
- ✅ Create release
- ✅ Attach JARs

---

## 🎯 Commandes Rapides

```bash
# Test local (sans CI/CD)
./mvnw clean install

# Push develop (build + tests)
git push origin develop

# Push main (build + tests)
git push origin main

# Release production (build + tests + Docker)
git tag v1.0.0
git push origin v1.0.0
```

---

✅ **Avec cette configuration, tu as un workflow CI/CD propre et efficace !** 🚀

**Les images Docker ne sont créées que pour les releases (tags)**, ce qui est exactement ce que tu veux ! 🎉
