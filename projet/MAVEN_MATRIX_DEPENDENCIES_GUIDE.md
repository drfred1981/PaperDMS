# Guide Matrice Maven Multi-Packages avec Dépendances

## 🎯 Objectif

Gérer le build de plusieurs packages Maven avec dépendances inter-packages, en gérant intelligemment l'ordre de build et les retries.

---

## 📋 Architecture du Pipeline

### Vue d'Ensemble

```
┌─────────────────────────────────────────────────────────┐
│                   Push / Tag Trigger                     │
└────────────────────────┬────────────────────────────────┘
                         │
        ┌────────────────┴────────────────┐
        │                                 │
┌───────▼────────────┐         ┌─────────▼──────────┐
│ Build Maven Base   │         │ Build Maven Base   │
│ (paperdms-common)  │         │ (package2)         │
│                    │         │                    │
│ Retry si échec     │         │ Retry si échec     │
└───────┬────────────┘         └─────────┬──────────┘
        │                                 │
        └────────────────┬────────────────┘
                         │
              ┌──────────▼───────────┐
              │ Build Maven          │
              │ Dependent            │
              │ (paperdms-shared,    │
              │  paperdms-dto)       │
              │                      │
              │ Retry si échec       │
              └──────────┬───────────┘
                         │
              ┌──────────▼───────────┐
              │ Consolidate Maven    │
              │ (merge artifacts)    │
              └──────────┬───────────┘
                         │
        ┌────────────────┴────────────────┐
        │                                 │
┌───────▼────────────┐         ┌─────────▼──────────┐
│ Build Service 1    │   ...   │ Build Service 16   │
│ (gateway)          │         │ (workflowService)  │
│                    │         │                    │
│ Retry si échec     │         │ Retry si échec     │
│ Docker (tags only) │         │ Docker (tags only) │
└────────────────────┘         └────────────────────┘
                         │
              ┌──────────▼───────────┐
              │ Create Release       │
              │ (tags only)          │
              └──────────────────────┘
```

---

## 🔧 Structure des Jobs

### Job 1 : Build Maven Base (Phase 1)

**Objectif** : Builder les packages sans dépendances internes

```yaml
build-maven-base:
  strategy:
    matrix:
      package:
        - paperdms-common      # Pas de dépendances
        # - paperdms-utils     # Pas de dépendances
        # - paperdms-core      # Pas de dépendances
```

**Caractéristiques** :
- ✅ Build en parallèle (max 3 simultanés)
- ✅ Retry automatique en cas d'échec
- ✅ Upload artifacts individuels
- ✅ Tests exécutés

**Artifacts produits** :
```
maven-paperdms-common/
  └── ~/.m2/repository/fr/smartprod/paperdms/paperdms-common/
```

---

### Job 2 : Build Maven Dependent (Phase 2)

**Objectif** : Builder les packages qui dépendent des packages de Phase 1

```yaml
build-maven-dependent:
  needs: build-maven-base
  strategy:
    matrix:
      package:
        - paperdms-shared     # Dépend de paperdms-common
        - paperdms-dto        # Dépend de paperdms-common
        - paperdms-domain     # Dépend de paperdms-common
```

**Caractéristiques** :
- ✅ Attend que `build-maven-base` réussisse
- ✅ Télécharge TOUS les artifacts de Phase 1
- ✅ Build en parallèle (Phase 2 packages entre eux)
- ✅ Retry automatique
- ✅ Upload artifacts individuels

**Artifacts produits** :
```
maven-paperdms-shared/
maven-paperdms-dto/
maven-paperdms-domain/
```

---

### Job 3 : Consolidate Maven

**Objectif** : Fusionner tous les artifacts Maven en un seul

```yaml
consolidate-maven:
  needs: [build-maven-base, build-maven-dependent]
```

**Processus** :
1. Télécharge TOUS les artifacts `maven-*`
2. Les fusionne dans un seul répertoire
3. Upload le repository Maven complet

**Artifact produit** :
```
maven-repository-complete/
  └── ~/.m2/repository/fr/smartprod/paperdms/
      ├── paperdms-common/
      ├── paperdms-shared/
      ├── paperdms-dto/
      └── paperdms-domain/
```

---

### Job 4 : Build Services

**Objectif** : Builder les 16 microservices

```yaml
build-services:
  needs: consolidate-maven
  strategy:
    matrix:
      service: [gateway, documentService, ...]
```

**Caractéristiques** :
- ✅ Utilise le repository Maven complet
- ✅ Build en parallèle (max 5)
- ✅ Retry automatique
- ✅ Docker uniquement sur tags

---

## 🔄 Gestion des Dépendances

### Scénario 1 : Aucune Dépendance (Actuel)

```yaml
build-maven-base:
  matrix:
    package:
      - paperdms-common  # Seul package
```

**Résultat** :
- Phase 1 : Build `paperdms-common`
- Phase 2 : Skipped (aucun package dépendant)
- Consolidation : Crée `maven-repository-complete`
- Services : Utilisent `paperdms-common`

---

### Scénario 2 : Ajout d'un Package Dépendant

```yaml
# Phase 1
build-maven-base:
  matrix:
    package:
      - paperdms-common

# Phase 2
build-maven-dependent:
  matrix:
    package:
      - paperdms-shared  # Dépend de paperdms-common
```

**Flux** :
```
paperdms-common (Phase 1)
    ↓
paperdms-shared (Phase 2) - télécharge paperdms-common
    ↓
Consolidation - fusionne les deux
    ↓
Services - utilisent les deux
```

---

### Scénario 3 : Hiérarchie Complexe (Futur)

```yaml
# Phase 1 (aucune dépendance)
build-maven-base:
  matrix:
    package:
      - paperdms-common
      - paperdms-utils

# Phase 2 (dépendent de Phase 1)
build-maven-dependent:
  matrix:
    package:
      - paperdms-shared     # Dépend de common
      - paperdms-dto        # Dépend de common
      - paperdms-domain     # Dépend de common + shared
```

**Problème** : `paperdms-domain` dépend de `paperdms-shared` qui est aussi en Phase 2 !

**Solution** : Créer une Phase 3

---

## 🎯 Ajouter une Phase 3 (Si Nécessaire)

### Structure à 3 Phases

```yaml
# Phase 1 : Base (no dependencies)
build-maven-base:
  matrix:
    package:
      - paperdms-common
      - paperdms-utils

# Phase 2 : Depends on Phase 1
build-maven-phase2:
  needs: build-maven-base
  matrix:
    package:
      - paperdms-shared     # Depends on common
      - paperdms-dto        # Depends on common

# Phase 3 : Depends on Phase 1 + 2
build-maven-phase3:
  needs: [build-maven-base, build-maven-phase2]
  matrix:
    package:
      - paperdms-domain     # Depends on common + shared

# Consolidation
consolidate-maven:
  needs: [build-maven-base, build-maven-phase2, build-maven-phase3]
```

---

## 🔁 Mécanisme de Retry

### Retry Automatique en Cas d'Échec

Chaque job a un retry intégré :

```yaml
- name: Build ${{ matrix.package }}
  id: build
  run: |
    cd ${{ matrix.package }}
    mvn clean install -DskipTests

# Si le step précédent échoue, retry
- name: Retry build on failure
  if: failure() && steps.build.outcome == 'failure'
  run: |
    echo "Retrying build for ${{ matrix.package }}..."
    cd ${{ matrix.package }}
    mvn clean install -DskipTests
```

**Fonctionnement** :
1. Essaye de builder le package
2. Si échec, attends quelques secondes
3. Retry une fois automatiquement
4. Si échec encore, le job échoue

**Avantages** :
- ✅ Résout les erreurs temporaires (réseau, cache, etc.)
- ✅ Pas besoin de relancer manuellement
- ✅ Augmente la fiabilité

---

## 📊 Temps d'Exécution Estimés

### Scénario 1 : Configuration Actuelle (1 package)

```
Build Maven Base (paperdms-common):     2-3 min
Consolidate Maven:                      30 sec
Build Services (16 services × 5):       12-15 min
Docker (tags only):                     8-10 min
────────────────────────────────────────────────
Total (sans Docker):                    15-18 min
Total (avec Docker - tags):             23-28 min
```

### Scénario 2 : Avec 5 Packages

```
Build Maven Base (3 packages):          3-4 min  (parallèle)
Build Maven Dependent (2 packages):     2-3 min  (parallèle)
Consolidate Maven:                      1 min
Build Services:                         12-15 min
Docker (tags):                          8-10 min
────────────────────────────────────────────────
Total (sans Docker):                    18-23 min
Total (avec Docker - tags):             26-33 min
```

---

## 🎓 Comment Ajouter un Nouveau Package Maven

### Étape 1 : Identifier les Dépendances

```bash
# Dans le pom.xml de ton nouveau package
<dependencies>
    <dependency>
        <groupId>fr.smartprod.paperdms</groupId>
        <artifactId>paperdms-common</artifactId>  # ← Dépend de common
    </dependency>
</dependencies>
```

### Étape 2 : Choisir la Phase

**Si le package ne dépend d'aucun autre package PaperDMS** :
```yaml
# Ajoute dans Phase 1
build-maven-base:
  matrix:
    package:
      - paperdms-common
      - mon-nouveau-package  # ← Ajoute ici
```

**Si le package dépend de `paperdms-common` uniquement** :
```yaml
# Ajoute dans Phase 2
build-maven-dependent:
  matrix:
    package:
      - mon-nouveau-package  # ← Ajoute ici
```

**Si le package dépend d'autres packages de Phase 2** :
```yaml
# Crée une Phase 3 (voir exemple ci-dessus)
```

### Étape 3 : Tester

```bash
# Push et vérifie les logs
git add .
git commit -m "chore: add new maven package"
git push origin main

# Vérifie GitHub Actions
# → Le nouveau package doit être buildé dans la bonne phase
```

---

## 🐛 Troubleshooting

### Problème 1 : "Cannot resolve dependency"

```
[ERROR] Failed to execute goal ... could not resolve dependencies
[ERROR] The following artifacts could not be resolved:
[ERROR]   fr.smartprod.paperdms:paperdms-common:jar:1.0.0
```

**Cause** : Le package dépendant est buildé avant sa dépendance

**Solution** : Déplace le package dans une phase ultérieure

```yaml
# ❌ MAUVAIS - paperdms-shared en Phase 1
build-maven-base:
  matrix:
    package:
      - paperdms-shared  # Dépend de common mais en Phase 1

# ✅ BON - paperdms-shared en Phase 2
build-maven-dependent:
  matrix:
    package:
      - paperdms-shared  # Dépend de common (Phase 1)
```

---

### Problème 2 : Retry ne résout pas l'erreur

```
Build failed
Retrying...
Build failed again
```

**Cause** : Erreur réelle de code/config, pas temporaire

**Solution** : Vérifie les logs pour identifier l'erreur réelle

```bash
# Teste localement
cd mon-package
mvn clean install -DskipTests

# Vérifie les dépendances
mvn dependency:tree
```

---

### Problème 3 : Consolidation échoue

```
Error: No artifacts found matching pattern maven-*
```

**Cause** : Tous les builds Maven ont échoué

**Solution** :
1. Vérifie les logs des jobs `build-maven-base` et `build-maven-dependent`
2. Fixe les erreurs de build
3. Relance le workflow

---

### Problème 4 : Services ne trouvent pas les packages Maven

```
[ERROR] Failed to execute goal: Could not resolve dependencies
```

**Cause** : Le job `consolidate-maven` a échoué ou est skipped

**Solution** : Vérifie que `consolidate-maven` a réussi

```yaml
# Le job consolidate-maven doit avoir cette condition
if: always() && (needs.build-maven-base.result == 'success' || needs.build-maven-dependent.result == 'success')
```

---

## 📝 Exemple Complet : Ajout de 4 Nouveaux Packages

### Structure des Dépendances

```
paperdms-common (Phase 1)
    ↓
    ├─→ paperdms-shared (Phase 2)
    ├─→ paperdms-dto (Phase 2)
    └─→ paperdms-utils (Phase 1 - indépendant)
              ↓
         paperdms-domain (Phase 3 - dépend de shared + dto)
```

### Configuration Workflow

```yaml
# Phase 1 : Base packages
build-maven-base:
  matrix:
    package:
      - paperdms-common
      - paperdms-utils    # Indépendant

# Phase 2 : Depends on Phase 1
build-maven-phase2:
  needs: build-maven-base
  matrix:
    package:
      - paperdms-shared   # Depends on common
      - paperdms-dto      # Depends on common

# Phase 3 : Depends on Phase 1 + 2
build-maven-phase3:
  needs: [build-maven-base, build-maven-phase2]
  matrix:
    package:
      - paperdms-domain   # Depends on shared + dto

# Consolidation
consolidate-maven:
  needs: [build-maven-base, build-maven-phase2, build-maven-phase3]
  # ... merge all
```

---

## ✅ Checklist Configuration

### Pour Chaque Nouveau Package Maven

- [ ] Créer le package avec `pom.xml`
- [ ] Définir les dépendances dans `pom.xml`
- [ ] Identifier la phase appropriée (1, 2, ou 3)
- [ ] Ajouter le package dans la matrice de la bonne phase
- [ ] Tester localement : `mvn clean install`
- [ ] Commit et push
- [ ] Vérifier le build dans GitHub Actions
- [ ] Vérifier que les services peuvent utiliser le package

---

## 🎯 Résumé

### Configuration Actuelle

```yaml
Phase 1: paperdms-common
Phase 2: (vide - skip)
Consolidation: paperdms-common
Services: 16 services utilisent paperdms-common
```

### Pour Ajouter des Packages

```yaml
# 1. Packages sans dépendances → Phase 1
build-maven-base:
  matrix:
    package:
      - paperdms-common
      - nouveau-package-1

# 2. Packages dépendant de Phase 1 → Phase 2
build-maven-dependent:
  matrix:
    package:
      - nouveau-package-2  # Dépend de common

# 3. Si packages dépendent de Phase 2 → Créer Phase 3
```

### Avantages de Cette Architecture

- ✅ **Gère automatiquement les dépendances** entre packages
- ✅ **Build parallèle** dans chaque phase
- ✅ **Retry automatique** en cas d'échec temporaire
- ✅ **Extensible** : ajouter facilement des packages
- ✅ **Robuste** : gère les erreurs de build
- ✅ **Optimisé** : artifacts consolidés en un seul

---

✅ **Avec cette architecture, tu peux ajouter autant de packages Maven que nécessaire !** 🚀

**Les dépendances sont gérées intelligemment et les retries automatiques augmentent la fiabilité.** 🎉
