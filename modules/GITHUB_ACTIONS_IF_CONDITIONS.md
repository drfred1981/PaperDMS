# Guide Conditions IF - GitHub Actions

## 🎯 Syntaxe des Conditions

Guide rapide sur l'utilisation des conditions `if` dans les workflows GitHub Actions.

---

## 📋 Syntaxe de Base

### Condition Simple (une ligne)

```yaml
jobs:
  my-job:
    if: github.ref == 'refs/heads/main'
    steps:
      # ...
```

### Condition Multi-lignes

```yaml
jobs:
  my-job:
    if: |
      github.ref == 'refs/heads/main' ||
      startsWith(github.ref, 'refs/tags/')
    steps:
      # ...
```

---

## ⚠️ Erreur Courante : Double IF

### ❌ INCORRECT (deux if dans le même job)

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    if: always() && needs.previous-job.result != 'failure'
    
    strategy:
      matrix:
        service: [gateway, documentService]
    
    # ❌ ERREUR : Deuxième if - sera ignoré ou causera une erreur
    if: needs.detect-changes.outputs[matrix.service] == 'true'
    
    steps:
      # ...
```

**Problème** : GitHub Actions n'accepte qu'**un seul `if` par job**.

### ✅ CORRECT (un seul if combiné)

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    
    # ✅ Un seul if avec toutes les conditions
    if: |
      always() && 
      needs.previous-job.result != 'failure' &&
      needs.detect-changes.outputs[matrix.service] == 'true'
    
    strategy:
      matrix:
        service: [gateway, documentService]
    
    steps:
      # ...
```

---

## 🔧 Opérateurs Logiques

### AND (&&)

```yaml
# Les DEUX conditions doivent être vraies
if: github.ref == 'refs/heads/main' && github.event_name == 'push'
```

### OR (||)

```yaml
# AU MOINS UNE condition doit être vraie
if: github.ref == 'refs/heads/main' || github.ref == 'refs/heads/develop'
```

### NOT (!)

```yaml
# Condition inversée
if: "!cancelled()"
```

### Parenthèses pour Grouper

```yaml
if: |
  (github.ref == 'refs/heads/main' || github.ref == 'refs/heads/develop') &&
  github.event_name == 'push'
```

---

## 📝 Exemples Pratiques

### Exemple 1 : Job Conditionnel Simple

```yaml
jobs:
  deploy:
    runs-on: ubuntu-latest
    # Seulement sur tags
    if: startsWith(github.ref, 'refs/tags/')
    steps:
      - run: echo "Deploying..."
```

### Exemple 2 : Job avec Dépendances

```yaml
jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - run: npm test
  
  deploy:
    needs: test
    runs-on: ubuntu-latest
    # Deploy si test réussi ET sur main
    if: |
      success() &&
      github.ref == 'refs/heads/main'
    steps:
      - run: echo "Deploying..."
```

### Exemple 3 : Matrix avec Condition

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    
    strategy:
      matrix:
        service: [gateway, api, worker]
    
    # Build uniquement si le service a changé
    if: needs.detect-changes.outputs[matrix.service] == 'true'
    
    steps:
      - run: echo "Building ${{ matrix.service }}"
```

### Exemple 4 : Conditions Multiples Complexes

```yaml
jobs:
  build:
    needs: [detect-changes, build-deps]
    runs-on: ubuntu-latest
    
    strategy:
      matrix:
        service: [gateway, api, worker]
    
    # Condition complexe combinée
    if: |
      always() &&
      needs.build-deps.result != 'failure' &&
      (
        needs.detect-changes.outputs.all-services == 'true' ||
        needs.detect-changes.outputs[matrix.service] == 'true'
      )
    
    steps:
      - run: echo "Building ${{ matrix.service }}"
```

---

## 🎯 Cas d'Usage PaperDMS

### Build Services Optimisé

```yaml
build-services:
  name: Build ${{ matrix.service }}
  needs: [detect-changes, build-maven-packages]
  runs-on: ubuntu-latest
  
  # Conditions combinées :
  # 1. Maven packages OK (ou skipped)
  # 2. ET (tous les services OU service spécifique changé)
  if: |
    always() && 
    needs.build-maven-packages.result != 'failure' &&
    (needs.detect-changes.outputs.all-services == 'true' ||
     needs.detect-changes.outputs[matrix.service] == 'true')
  
  strategy:
    matrix:
      service: [gateway, documentService, ocrService]
  
  steps:
    - run: echo "Building ${{ matrix.service }}"
```

**Explication** :
- `always()` : Execute même si jobs précédents skipped
- `needs.build-maven-packages.result != 'failure'` : Seulement si packages OK
- `needs.detect-changes.outputs.all-services == 'true'` : Build tous si tag/maven changed
- `needs.detect-changes.outputs[matrix.service] == 'true'` : Ou si service spécifique changé

---

## 🔍 Fonctions Utiles

### Status Checks

```yaml
# Job réussi
if: success()

# Job échoué
if: failure()

# Job annulé
if: cancelled()

# Toujours exécuter (même si précédents échoués)
if: always()
```

### Vérification Références Git

```yaml
# Branch spécifique
if: github.ref == 'refs/heads/main'

# Commence par (tags)
if: startsWith(github.ref, 'refs/tags/')

# Contient
if: contains(github.ref, 'release')

# Termine par
if: endsWith(github.ref, '/main')
```

### Event Types

```yaml
# Type d'événement
if: github.event_name == 'push'
if: github.event_name == 'pull_request'
if: github.event_name == 'workflow_dispatch'

# Combinaison
if: |
  github.event_name == 'push' &&
  startsWith(github.ref, 'refs/tags/')
```

### Outputs de Jobs Précédents

```yaml
# Vérifier un output
if: needs.previous-job.outputs.should-deploy == 'true'

# Vérifier le résultat
if: needs.test.result == 'success'
if: needs.build.result != 'failure'

# Matrix outputs
if: needs.detect-changes.outputs[matrix.service] == 'true'
```

---

## 🚫 Erreurs Communes

### Erreur 1 : Double IF

```yaml
# ❌ MAUVAIS
jobs:
  build:
    if: github.ref == 'refs/heads/main'
    strategy:
      matrix:
        service: [a, b]
    if: matrix.service == 'a'  # ERREUR !
```

```yaml
# ✅ BON
jobs:
  build:
    if: |
      github.ref == 'refs/heads/main' &&
      matrix.service == 'a'
    strategy:
      matrix:
        service: [a, b]
```

### Erreur 2 : Syntaxe Quotes

```yaml
# ❌ MAUVAIS (problème avec !)
if: !cancelled()

# ✅ BON
if: "!cancelled()"
```

### Erreur 3 : Accès Matrix avant Strategy

```yaml
# ❌ MAUVAIS (matrix pas encore défini)
jobs:
  build:
    if: matrix.service == 'gateway'
    strategy:
      matrix:
        service: [gateway, api]

# ✅ BON (utilise outputs d'un job précédent)
jobs:
  detect:
    outputs:
      service: gateway
  
  build:
    needs: detect
    if: needs.detect.outputs.service == 'gateway'
```

### Erreur 4 : Oublier always() avec needs

```yaml
# ❌ MAUVAIS (job ne s'exécutera pas si previous-job skip)
jobs:
  build:
    needs: previous-job
    if: needs.previous-job.result != 'failure'

# ✅ BON (s'exécute même si previous-job skip)
jobs:
  build:
    needs: previous-job
    if: always() && needs.previous-job.result != 'failure'
```

---

## 📚 Conditions sur Steps

Les conditions `if` fonctionnent aussi sur les steps individuels :

```yaml
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      # Toujours exécuté
      - name: Checkout
        uses: actions/checkout@v4
      
      # Seulement sur tags
      - name: Build Docker
        if: startsWith(github.ref, 'refs/tags/')
        run: docker build .
      
      # Seulement si step précédent réussi
      - name: Push Docker
        if: success()
        run: docker push
```

---

## ✅ Best Practices

### 1. Condition au Niveau Job (Recommandé)

```yaml
# ✅ BON : Condition au niveau job
jobs:
  deploy:
    if: startsWith(github.ref, 'refs/tags/')
    steps:
      - run: deploy-step-1
      - run: deploy-step-2
```

**Avantage** : Job apparaît comme "skipped" dans l'UI, plus clair

### 2. Condition au Niveau Step

```yaml
# ⚠️ OK mais moins clair
jobs:
  build:
    steps:
      - run: build
      - name: Deploy
        if: startsWith(github.ref, 'refs/tags/')
        run: deploy
```

**Inconvénient** : Job apparaît toujours, steps individuels skip

### 3. Combiner les Deux

```yaml
# ✅ OPTIMAL
jobs:
  build:
    steps:
      - run: build
      - run: test
  
  deploy:
    needs: build
    if: |
      success() &&
      startsWith(github.ref, 'refs/tags/')
    steps:
      - name: Deploy production
        if: "!contains(github.ref, 'beta')"
        run: deploy-prod
      
      - name: Deploy beta
        if: contains(github.ref, 'beta')
        run: deploy-beta
```

### 4. Documenter les Conditions Complexes

```yaml
jobs:
  build:
    # Build services if:
    # 1. Previous jobs succeeded (or skipped)
    # 2. AND (building all services OR specific service changed)
    if: |
      always() && 
      needs.build-maven.result != 'failure' &&
      (needs.detect.outputs.all == 'true' ||
       needs.detect.outputs[matrix.service] == 'true')
```

---

## 📖 Résumé

### Un Seul IF par Job

```yaml
# ✅ CORRECT
jobs:
  my-job:
    if: condition1 && condition2
    steps: [...]

# ❌ INCORRECT
jobs:
  my-job:
    if: condition1
    if: condition2  # ERREUR !
    steps: [...]
```

### Combiner Plusieurs Conditions

```yaml
# Multi-lignes pour lisibilité
if: |
  condition1 &&
  condition2 &&
  (condition3 || condition4)
```

### Opérateurs

- `&&` : ET
- `||` : OU
- `!` : NON (avec quotes : `"!cancelled()"`)
- `()` : Groupement

### Fonctions Utiles

- `always()`, `success()`, `failure()`, `cancelled()`
- `startsWith()`, `endsWith()`, `contains()`
- `github.ref`, `github.event_name`, `matrix.xxx`
- `needs.job-name.result`, `needs.job-name.outputs.xxx`

---

✅ **Un seul `if` par job, conditions combinées avec `&&` et `||` !** 🎯
