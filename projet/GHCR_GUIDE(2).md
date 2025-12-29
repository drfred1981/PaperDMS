# Guide GitHub Container Registry (GHCR) - PaperDMS

## 🎯 Utiliser GitHub Container Registry

GitHub Container Registry (ghcr.io) est le registre Docker intégré à GitHub, **gratuit** et **sans configuration de secrets supplémentaires**.

---

## 📋 Différences Registry

### Docker Hub vs GitHub Container Registry

| Caractéristique | Docker Hub | GitHub Container Registry |
|----------------|------------|---------------------------|
| **URL** | `docker.io/username/image` | `ghcr.io/username/repo-image` |
| **Secrets requis** | ✅ DOCKER_USERNAME, DOCKER_PASSWORD | ❌ Aucun (utilise GITHUB_TOKEN) |
| **Gratuit** | Limité (rate limiting) | ✅ Illimité pour repos publics |
| **Images privées** | 1 gratuitement | ✅ Illimitées |
| **Configuration** | Manuelle | ✅ Automatique |
| **Intégration GitHub** | ❌ Externe | ✅ Native |

---

## 🚀 Configuration pour GHCR

### Workflows Déjà Configurés

Les workflows fournis supportent **les deux registres** :

#### ci-cd-pipeline.yml → Docker Hub
```yaml
-Djib.to.image=paperdms/${{ matrix.service }}:${{ steps.version.outputs.version }}
-Djib.to.auth.username=${{ secrets.DOCKER_USERNAME }}
-Djib.to.auth.password=${{ secrets.DOCKER_PASSWORD }}
```

#### ci-cd-optimized.yml → GitHub Container Registry
```yaml
-Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}
-Djib.to.auth.username=${{ github.actor }}
-Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

---

## ✅ Ce Qu'il Faut Modifier

### Option 1 : Utiliser GHCR Partout (Recommandé)

Modifie **ci-cd-pipeline.yml** pour utiliser GHCR au lieu de Docker Hub :

```yaml
# Avant (Docker Hub)
- name: Login to Docker Hub
  uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}

- name: Build and push Docker image
  run: |
    mvn -Pprod compile jib:build \
      -Djib.to.image=paperdms/${{ matrix.service }}:${{ steps.version.outputs.version }}

# Après (GHCR)
- name: Login to GitHub Container Registry
  uses: docker/login-action@v3
  with:
    registry: ghcr.io
    username: ${{ github.actor }}
    password: ${{ secrets.GITHUB_TOKEN }}

- name: Build and push Docker image
  run: |
    mvn -Pprod compile jib:build \
      -Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}:${{ steps.version.outputs.version }} \
      -Djib.to.tags=latest \
      -Djib.to.auth.username=${{ github.actor }} \
      -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

### Option 2 : Utiliser les Deux (Dual Push)

Publie sur les deux registres en même temps :

```yaml
- name: Login to Docker Hub
  uses: docker/login-action@v3
  with:
    username: ${{ secrets.DOCKER_USERNAME }}
    password: ${{ secrets.DOCKER_PASSWORD }}

- name: Login to GitHub Container Registry
  uses: docker/login-action@v3
  with:
    registry: ghcr.io
    username: ${{ github.actor }}
    password: ${{ secrets.GITHUB_TOKEN }}

- name: Build and push to Docker Hub
  run: |
    mvn compile jib:build \
      -Djib.to.image=paperdms/${{ matrix.service }}:${{ steps.version.outputs.version }} \
      -Djib.to.auth.username=${{ secrets.DOCKER_USERNAME }} \
      -Djib.to.auth.password=${{ secrets.DOCKER_PASSWORD }}

- name: Build and push to GHCR
  run: |
    mvn compile jib:build \
      -Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}:${{ steps.version.outputs.version }} \
      -Djib.to.auth.username=${{ github.actor }} \
      -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

---

## 🔑 Permissions GITHUB_TOKEN

### Permissions Requises

GitHub Actions doit avoir les permissions d'écriture sur les packages.

#### Configuration Automatique (Recommandée)

Dans ton repo GitHub :

```
Settings → Actions → General → Workflow permissions
```

Sélectionne :
- ✅ **Read and write permissions**

#### Configuration par Workflow (Alternative)

Ajoute au début du workflow :

```yaml
name: PaperDMS CI/CD

on:
  push:
    tags:
      - 'v*.*.*'

permissions:
  contents: read
  packages: write  # ← Important pour GHCR

jobs:
  build-services:
    # ...
```

---

## 🌐 Format des URLs

### Comprendre les URLs GHCR

```
ghcr.io/OWNER/IMAGE_NAME:TAG
```

**Exemples** :

| Repo GitHub | Image GHCR |
|-------------|------------|
| `smartprod/paperdms` | `ghcr.io/smartprod/paperdms-gateway:latest` |
| `fred/my-project` | `ghcr.io/fred/my-project-documentservice:v1.0.0` |
| `company/repo` | `ghcr.io/company/repo-ocrservice:latest` |

### Variables GitHub Actions

```yaml
${{ github.repository_owner }}  # smartprod
${{ github.repository }}        # smartprod/paperdms
${{ github.actor }}             # fred (ton username)
${{ matrix.service }}           # gateway, documentService, etc.
```

### Résultat Final

Si ton repo est `smartprod/paperdms` :

```
ghcr.io/smartprod/paperdms-gateway:v1.0.0
ghcr.io/smartprod/paperdms-gateway:latest
ghcr.io/smartprod/paperdms-documentservice:v1.0.0
ghcr.io/smartprod/paperdms-documentservice:latest
```

---

## 🔍 Vérifier les Images

### Dans GitHub UI

1. Ton repo GitHub → Packages (à droite)
2. Tu verras toutes les images
3. Clique pour voir versions/tags

### En Ligne de Commande

```bash
# Login
echo $GITHUB_TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Pull une image
docker pull ghcr.io/smartprod/paperdms-gateway:latest

# Liste les tags
gh api /user/packages/container/paperdms-gateway/versions
```

---

## 🎯 Configuration Complète Recommandée

### ci-cd-pipeline.yml (Modifié pour GHCR)

```yaml
name: PaperDMS CI/CD Pipeline

on:
  push:
    tags:
      - 'v*.*.*'
    branches:
      - main
      - develop

permissions:
  contents: read
  packages: write  # Important !

env:
  JAVA_VERSION: '17'
  NODE_VERSION: '18'
  REGISTRY: ghcr.io
  IMAGE_PREFIX: ${{ github.repository_owner }}/paperdms

jobs:
  build-services:
    # ... (jobs précédents identiques)
    
    steps:
      # ... (steps précédents identiques)
      
      - name: Login to GitHub Container Registry
        if: github.event_name == 'push' && (startsWith(github.ref, 'refs/tags/') || github.ref == 'refs/heads/main')
        uses: docker/login-action@v3
        with:
          registry: ghcr.io
          username: ${{ github.actor }}
          password: ${{ secrets.GITHUB_TOKEN }}
      
      - name: Build and push Docker image
        if: github.event_name == 'push' && (startsWith(github.ref, 'refs/tags/') || github.ref == 'refs/heads/main')
        working-directory: ${{ matrix.service }}
        run: |
          mvn -Pprod compile jib:build \
            -Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}:${{ steps.version.outputs.version }} \
            -Djib.to.tags=latest \
            -Djib.to.auth.username=${{ github.actor }} \
            -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

---

## 🐳 Utiliser les Images GHCR

### Docker Compose

```yaml
version: '3.8'

services:
  gateway:
    image: ghcr.io/smartprod/paperdms-gateway:latest
    # OU avec version spécifique
    # image: ghcr.io/smartprod/paperdms-gateway:v1.0.0
    ports:
      - "8080:8080"

  documentservice:
    image: ghcr.io/smartprod/paperdms-documentservice:latest
    ports:
      - "8081:8081"
```

### Docker Run

```bash
# Pull l'image
docker pull ghcr.io/smartprod/paperdms-gateway:latest

# Run
docker run -p 8080:8080 ghcr.io/smartprod/paperdms-gateway:latest
```

### Kubernetes

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gateway
spec:
  replicas: 2
  template:
    spec:
      containers:
      - name: gateway
        image: ghcr.io/smartprod/paperdms-gateway:v1.0.0
        ports:
        - containerPort: 8080
```

---

## 🔒 Visibilité des Images

### Images Publiques (Par Défaut)

Par défaut, les images GHCR sont **liées à ton repo**.
- Si repo public → images publiques
- Si repo privé → images privées

### Rendre une Image Publique

1. GitHub → Packages
2. Clique sur ton image
3. Package settings → Change visibility → Public

### Utiliser des Images Privées

```bash
# Crée un token avec permissions packages:read
# GitHub → Settings → Developer settings → Personal access tokens

# Login
echo $TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Pull
docker pull ghcr.io/smartprod/paperdms-gateway:latest
```

---

## 🎓 Avantages GHCR pour PaperDMS

### ✅ Avantages

1. **Pas de secrets à configurer**
   - Utilise `GITHUB_TOKEN` automatiquement
   - Aucune config externe

2. **Gratuit et illimité**
   - Pas de rate limiting
   - Images privées illimitées

3. **Intégration native**
   - Lié au repo
   - Visible dans l'interface GitHub
   - Permissions héritées du repo

4. **Multi-architecture**
   - Support ARM64 + AMD64
   - Parfait pour dev local (M1/M2 Mac)

5. **Performance**
   - Hébergé par GitHub
   - CDN global
   - Rapide partout

### ❌ Inconvénients

1. **Moins connu**
   - Docker Hub plus populaire
   - Moins de docs communautaires

2. **Lié à GitHub**
   - Si tu changes de plateforme git
   - Vendor lock-in

---

## 🔄 Migration Docker Hub → GHCR

Si tu as déjà des images sur Docker Hub :

```bash
# 1. Pull depuis Docker Hub
docker pull paperdms/gateway:latest

# 2. Tag pour GHCR
docker tag paperdms/gateway:latest ghcr.io/smartprod/paperdms-gateway:latest

# 3. Login GHCR
echo $GITHUB_TOKEN | docker login ghcr.io -u smartprod --password-stdin

# 4. Push vers GHCR
docker push ghcr.io/smartprod/paperdms-gateway:latest
```

---

## 🐛 Troubleshooting

### Erreur : "Permission denied"

**Cause** : Permissions workflow insuffisantes

**Solution** :
```yaml
permissions:
  contents: read
  packages: write  # Ajoute cette ligne
```

Ou dans Settings → Actions → Workflow permissions → Read and write

### Erreur : "Image not found" lors du pull

**Cause** : Image privée ou mauvais nom

**Solution** :
```bash
# Vérifie le nom exact
# Format : ghcr.io/OWNER/IMAGE_NAME:TAG

# Login si privé
echo $TOKEN | docker login ghcr.io -u USERNAME --password-stdin

# Vérifie les packages dans GitHub UI
```

### Erreur : "Authentication required"

**Cause** : Pas de login ou token expiré

**Solution** :
```bash
# Génère un nouveau token
# GitHub → Settings → Developer settings → Personal access tokens
# Permissions : read:packages, write:packages

# Login
echo $NEW_TOKEN | docker login ghcr.io -u USERNAME --password-stdin
```

### Erreur : "Rate limit exceeded"

**Cause** : Impossible avec GHCR ! (pas de rate limit)

Si tu vois cette erreur, c'est que tu utilises encore Docker Hub.

---

## ✅ Checklist GHCR

- [ ] Repository GitHub créé
- [ ] Workflow permissions configurées (read/write)
- [ ] Workflow modifié pour utiliser `ghcr.io`
- [ ] Format image : `ghcr.io/OWNER/paperdms-SERVICE:TAG`
- [ ] Variables : `${{ github.repository_owner }}`
- [ ] Authentication : `${{ secrets.GITHUB_TOKEN }}`
- [ ] Testé avec un tag : `git tag v0.0.1 && git push origin v0.0.1`
- [ ] Images visibles dans Packages GitHub
- [ ] Pull fonctionne : `docker pull ghcr.io/...`

---

## 📖 Résumé

### Pour Utiliser GHCR (Recommandé)

**1. Configure les permissions** :
```
Settings → Actions → General → Workflow permissions → Read and write
```

**2. Utilise cette commande Jib** :
```yaml
mvn compile jib:build \
  -Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}:${{ steps.version.outputs.version }} \
  -Djib.to.tags=latest \
  -Djib.to.auth.username=${{ github.actor }} \
  -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

**3. Pull les images** :
```bash
docker pull ghcr.io/ton-username/paperdms-gateway:latest
```

### Différence Clé

**Docker Hub** :
```yaml
-Djib.to.image=paperdms/${{ matrix.service }}
-Djib.to.auth.username=${{ secrets.DOCKER_USERNAME }}
-Djib.to.auth.password=${{ secrets.DOCKER_PASSWORD }}
```

**GHCR** :
```yaml
-Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}
-Djib.to.auth.username=${{ github.actor }}
-Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

---

✅ **GHCR est plus simple et gratuit, parfait pour PaperDMS !** 🚀

**Aucune config supplémentaire nécessaire !** Les workflows fournis utilisent déjà GHCR correctement dans `ci-cd-optimized.yml`. 🎉
