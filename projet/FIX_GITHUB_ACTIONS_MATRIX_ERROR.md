# Fix GitHub Actions Matrix Error

## 🐛 Erreur

```
Invalid workflow file: .github/workflows/ci-cd-optimized.yaml#L1
(Line: 168, Col: 9): Unrecognized named-value: 'matrix'. 
Located at position 150 within expression: 
needs.detect-changes.outputs[matrix.service] == 'true'
```

---

## 🎯 Cause

**La variable `matrix` n'est PAS accessible dans les conditions `if` au niveau du job.**

### Portée des Variables

```yaml
jobs:
  my-job:
    # ❌ ERREUR : matrix n'existe pas encore ici
    if: matrix.service == 'gateway'
    
    strategy:
      matrix:
        service: [gateway, api]  # ← matrix défini ICI
    
    steps:
      # ✅ OK : matrix accessible dans les steps
      - name: Build
        run: echo "Building ${{ matrix.service }}"
```

**Règle** : `matrix` n'est disponible que **DANS les steps**, pas **AU NIVEAU du job**.

---

## ✅ Solution

### Approche 1 : Condition dans les Steps (Recommandée)

Déplace la logique de filtrage dans un premier step, puis conditionne tous les steps suivants.

```yaml
jobs:
  build-services:
    runs-on: ubuntu-latest
    
    # Condition simple au niveau job (sans matrix)
    if: always() && needs.build-maven-packages.result != 'failure'
    
    strategy:
      matrix:
        service: [gateway, documentService, ocrService]
    
    steps:
      # Step 1 : Vérifie si ce service doit être buildé
      - name: Check if service should be built
        id: should-build
        run: |
          ALL_SERVICES="${{ needs.detect-changes.outputs.all-services }}"
          SERVICE_CHANGED="${{ needs.detect-changes.outputs[matrix.service] }}"
          
          if [[ "$ALL_SERVICES" == "true" ]] || [[ "$SERVICE_CHANGED" == "true" ]]; then
            echo "should-build=true" >> $GITHUB_OUTPUT
            echo "✅ Building ${{ matrix.service }}"
          else
            echo "should-build=false" >> $GITHUB_OUTPUT
            echo "⏭️  Skipping ${{ matrix.service }} (no changes detected)"
          fi
      
      # Steps suivants : conditionnés par should-build
      - uses: actions/checkout@v4
        if: steps.should-build.outputs.should-build == 'true'
      
      - name: Build
        if: steps.should-build.outputs.should-build == 'true'
        run: mvn clean package
      
      - name: Docker build
        if: steps.should-build.outputs.should-build == 'true' && startsWith(github.ref, 'refs/tags/')
        run: docker build .
```

**Avantages** :
- ✅ Fonctionne correctement
- ✅ Services skippés apparaissent dans l'UI (plus clair)
- ✅ Peut combiner plusieurs conditions (`should-build` + autres)

---

### Approche 2 : Build Tous, Filter Ailleurs

Si tu veux builder tous les services systématiquement (pas d'optimisation) :

```yaml
jobs:
  build-services:
    runs-on: ubuntu-latest
    
    # Pas de filtrage par service
    if: always() && needs.build-maven-packages.result != 'failure'
    
    strategy:
      matrix:
        service: [gateway, documentService, ocrService]
    
    steps:
      - uses: actions/checkout@v4
      
      - name: Build
        run: mvn clean package
      
      # Toujours build, mais Docker seulement sur tags
      - name: Docker build
        if: startsWith(github.ref, 'refs/tags/')
        run: docker build .
```

**Avantages** :
- ✅ Simple
- ✅ Garantit que tout compile

**Inconvénients** :
- ❌ Plus lent (build tout même si rien n'a changé)
- ❌ Pas d'optimisation

---

### Approche 3 : Separate Jobs par Service

Si vraiment besoin de filtrer au niveau job, crée un job séparé par service :

```yaml
jobs:
  build-gateway:
    if: |
      always() && 
      needs.build-maven-packages.result != 'failure' &&
      (needs.detect-changes.outputs.all-services == 'true' ||
       needs.detect-changes.outputs.gateway == 'true')
    steps:
      - name: Build gateway
        run: mvn clean package
  
  build-documentService:
    if: |
      always() && 
      needs.build-maven-packages.result != 'failure' &&
      (needs.detect-changes.outputs.all-services == 'true' ||
       needs.detect-changes.outputs.documentService == 'true')
    steps:
      - name: Build documentService
        run: mvn clean package
```

**Avantages** :
- ✅ Filtrage au niveau job
- ✅ Jobs skippés n'apparaissent pas

**Inconvénients** :
- ❌ Beaucoup de duplication
- ❌ 15 jobs séparés au lieu d'un seul avec matrix
- ❌ Workflow très long et difficile à maintenir

---

## 🎯 Solution Implémentée (PaperDMS)

### Structure

```yaml
build-services:
  runs-on: ubuntu-latest
  
  # Condition simple : Maven packages OK
  if: always() && needs.build-maven-packages.result != 'failure'
  
  strategy:
    matrix:
      include:
        - service: gateway
          needs-node: true
        - service: documentService
          needs-node: false
        # ... 15 services total
  
  steps:
    # Step 1 : Check si service à builder
    - name: Check if service should be built
      id: should-build
      run: |
        ALL_SERVICES="${{ needs.detect-changes.outputs.all-services }}"
        SERVICE_CHANGED="${{ needs.detect-changes.outputs[matrix.service] }}"
        
        if [[ "$ALL_SERVICES" == "true" ]] || [[ "$SERVICE_CHANGED" == "true" ]]; then
          echo "should-build=true" >> $GITHUB_OUTPUT
          echo "✅ Building ${{ matrix.service }}"
        else
          echo "should-build=false" >> $GITHUB_OUTPUT
          echo "⏭️  Skipping ${{ matrix.service }} (no changes detected)"
        fi
    
    # Tous les steps suivants conditionnés
    - uses: actions/checkout@v4
      if: steps.should-build.outputs.should-build == 'true'
    
    - name: Build
      if: steps.should-build.outputs.should-build == 'true'
      run: mvn clean package
    
    - name: Docker
      if: steps.should-build.outputs.should-build == 'true' && startsWith(github.ref, 'refs/tags/')
      run: mvn jib:build
```

---

## 📊 Comportement

### Scénario 1 : Tag (Build All)

```
detect-changes:
  └── all-services: true

build-services (matrix: 15 services):
  ├── gateway:
  │   ├── should-build: true  (all-services=true)
  │   ├── Build: ✅
  │   └── Docker: ✅
  ├── documentService:
  │   ├── should-build: true
  │   ├── Build: ✅
  │   └── Docker: ✅
  └── ... (all 15 services built)
```

### Scénario 2 : Push Main (Only Changed)

```
detect-changes:
  ├── all-services: false
  ├── gateway: true
  └── documentService: false

build-services (matrix: 15 services):
  ├── gateway:
  │   ├── should-build: true  (gateway=true)
  │   ├── Build: ✅
  │   └── Docker: ⏭️ (not a tag)
  ├── documentService:
  │   ├── should-build: false
  │   └── All steps: ⏭️
  └── ... (13 other services skipped)
```

---

## 🎓 Règles à Retenir

### 1. Portée des Variables

```yaml
# Au niveau workflow
env:
  MY_VAR: value  # ✅ Accessible partout

jobs:
  my-job:
    # Au niveau job
    env:
      JOB_VAR: value  # ✅ Accessible dans ce job
    
    # Condition job
    if: env.MY_VAR == 'value'  # ✅ OK
    if: matrix.service == 'x'  # ❌ ERREUR - matrix pas encore défini
    
    strategy:
      matrix:
        service: [a, b]  # ← matrix défini ICI
    
    steps:
      # Dans steps
      - run: echo ${{ matrix.service }}  # ✅ OK - matrix disponible
      - run: echo ${{ env.MY_VAR }}      # ✅ OK - env disponible
```

### 2. Variables Disponibles au Niveau Job

```yaml
jobs:
  my-job:
    needs: [previous-job]
    
    if: |
      # ✅ OK - Ces variables sont disponibles
      github.ref == 'refs/heads/main'
      github.event_name == 'push'
      needs.previous-job.result == 'success'
      needs.previous-job.outputs.my-output == 'value'
      
      # ❌ ERREUR - Ces variables ne sont PAS disponibles
      matrix.service == 'gateway'
      steps.my-step.outputs.value == 'x'
```

### 3. Solution Générale

**Pour filtrer avec matrix** :
1. Condition simple au niveau job
2. Premier step vérifie les conditions matrix
3. Tous les steps suivants conditionnés par le résultat

---

## 🐛 Autres Erreurs Similaires

### Erreur : "steps not defined"

```yaml
jobs:
  build:
    # ❌ ERREUR
    if: steps.my-step.outputs.value == 'true'
    
    steps:
      - id: my-step
        run: echo "value=true" >> $GITHUB_OUTPUT
```

**Solution** : `steps` n'existe pas au niveau job, seulement dans les steps suivants.

### Erreur : "secrets not allowed in if"

```yaml
jobs:
  build:
    # ❌ ERREUR (dans certains contextes)
    if: secrets.MY_SECRET == 'value'
```

**Solution** : Les secrets ne sont généralement pas accessibles dans les `if`. Utilise un step intermédiaire.

---

## ✅ Résumé

### Problème

```yaml
# ❌ ERREUR
build-services:
  if: matrix.service == 'gateway'
  strategy:
    matrix:
      service: [gateway, api]
```

### Solution

```yaml
# ✅ CORRECT
build-services:
  if: always() && needs.previous.result != 'failure'
  strategy:
    matrix:
      service: [gateway, api]
  steps:
    - name: Check condition
      id: check
      run: |
        if [[ "${{ matrix.service }}" == "gateway" ]]; then
          echo "proceed=true" >> $GITHUB_OUTPUT
        fi
    
    - name: Build
      if: steps.check.outputs.proceed == 'true'
      run: build
```

---

✅ **La variable `matrix` n'est accessible que dans les steps, pas au niveau du job !** 🎯
