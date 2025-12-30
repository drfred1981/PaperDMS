# Fix Docker Image Name Uppercase Error

## 🐛 Erreur

```
Failed to execute goal com.google.cloud.tools:jib-maven-plugin:3.4.5:build
Invalid image reference ghcr.io/drfred1981/paperdms-archiveService:0.0.0
For example, slash-separated name components cannot have uppercase letters: 
Invalid image reference: ghcr.io/drfred1981/paperdms-archiveService:0.0.0
```

---

## 🎯 Cause

**Docker n'accepte PAS de majuscules dans les noms d'images.**

### Noms Invalides (avec majuscules)

```
❌ paperdms/documentService
❌ paperdms/archiveService
❌ ghcr.io/user/MyApp
❌ docker.io/Company/ProductName
```

### Noms Valides (tout en minuscules)

```
✅ paperdms/documentservice
✅ paperdms/archiveservice
✅ ghcr.io/user/myapp
✅ docker.io/company/productname
```

---

## ✅ Solution

### Solution 1 : Conversion en Minuscules (Workflow GitHub Actions)

Dans les workflows, convertis le nom du service en minuscules avant de créer l'image.

#### ci-cd-optimized.yml (GHCR)

```yaml
- name: Build and push Docker image
  if: startsWith(github.ref, 'refs/tags/')
  working-directory: ${{ matrix.service }}
  run: |
    # Convert service name to lowercase
    SERVICE_LOWER=$(echo "${{ matrix.service }}" | tr '[:upper:]' '[:lower:]')
    
    mvn -Pprod compile jib:build \
      -Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${SERVICE_LOWER}:${{ steps.version.outputs.version }} \
      -Djib.to.tags=latest \
      -Djib.to.auth.username=${{ github.actor }} \
      -Djib.to.auth.password=${{ secrets.GITHUB_TOKEN }}
```

#### ci-cd-pipeline.yml (Docker Hub)

```yaml
- name: Build and push Docker image
  if: startsWith(github.ref, 'refs/tags/')
  working-directory: ${{ matrix.service }}
  run: |
    # Convert service name to lowercase
    SERVICE_LOWER=$(echo "${{ matrix.service }}" | tr '[:upper:]' '[:lower:]')
    
    mvn -Pprod compile jib:build \
      -Djib.to.image=paperdms/${SERVICE_LOWER}:${{ steps.version.outputs.version }} \
      -Djib.to.tags=latest \
      -Djib.to.auth.username=${{ secrets.DOCKER_USERNAME }} \
      -Djib.to.auth.password=${{ secrets.DOCKER_PASSWORD }}
```

---

### Solution 2 : Renommer les Services (pom.xml)

Si tu veux des noms Docker spécifiques, configure-les directement dans le `pom.xml` de chaque service.

#### Dans documentService/pom.xml

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.5</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre</image>
        </from>
        <to>
            <!-- Nom explicite en minuscules -->
            <image>paperdms/document-service</image>
            <tags>
                <tag>latest</tag>
                <tag>${project.version}</tag>
            </tags>
        </to>
        <container>
            <ports>
                <port>8081</port>
            </ports>
        </container>
    </configuration>
</plugin>
```

**Avantages** :
- ✅ Noms explicites et lisibles
- ✅ Contrôle total sur le naming
- ✅ Peut utiliser des tirets : `document-service` au lieu de `documentservice`

**Inconvénients** :
- ❌ Besoin de configurer chaque service individuellement
- ❌ Plus de maintenance

---

### Solution 3 : Variables Maven (Centralisé)

Configure le nom de l'image via une propriété Maven.

#### Dans pom.xml

```xml
<properties>
    <docker.image.name>document-service</docker.image.name>
</properties>

<build>
    <plugins>
        <plugin>
            <groupId>com.google.cloud.tools</groupId>
            <artifactId>jib-maven-plugin</artifactId>
            <configuration>
                <to>
                    <image>paperdms/${docker.image.name}</image>
                </to>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Puis dans le workflow :

```yaml
mvn compile jib:build \
  -Ddocker.image.name=documentservice
```

---

## 🎯 Nomenclature Docker Recommandée

### Convention de Nommage

```
registry/namespace/name:tag
```

**Exemples** :

```
# GitHub Container Registry
ghcr.io/smartprod/paperdms-documentservice:v1.0.0
ghcr.io/smartprod/paperdms-documentservice:latest

# Docker Hub
paperdms/documentservice:v1.0.0
paperdms/documentservice:latest

# Avec tirets (plus lisible)
paperdms/document-service:v1.0.0
paperdms/archive-service:latest
```

### Règles Docker

1. **Minuscules uniquement** : `a-z`, `0-9`
2. **Caractères autorisés** : tirets `-`, underscores `_`, points `.`
3. **Format** : `[namespace/]name[:tag]`
4. **Longueur** : Max 255 caractères au total

### ❌ Caractères Interdits

```
❌ Majuscules : A-Z
❌ Espaces
❌ Caractères spéciaux : @, #, $, %, etc.
```

---

## 📊 Conversion des Noms

### Services PaperDMS

| Service Original | Nom Docker (camelCase → lowercase) |
|------------------|------------------------------------|
| `gateway` | `gateway` ✅ (pas de changement) |
| `documentService` | `documentservice` |
| `ocrService` | `ocrservice` |
| `searchService` | `searchservice` |
| `aiService` | `aiservice` |
| `workflowService` | `workflowservice` |
| `notificationService` | `notificationservice` |
| `emailService` | `emailservice` |
| `invoiceService` | `invoiceservice` |
| `receiptService` | `receiptservice` |
| `contractService` | `contractservice` |
| `reportService` | `reportservice` |
| `archiveService` | `archiveservice` |
| `auditService` | `auditservice` |
| `backupService` | `backupservice` |

### Images Finales (GHCR)

```
ghcr.io/drfred1981/paperdms-gateway:v1.0.0
ghcr.io/drfred1981/paperdms-documentservice:v1.0.0
ghcr.io/drfred1981/paperdms-ocrservice:v1.0.0
ghcr.io/drfred1981/paperdms-searchservice:v1.0.0
...
```

---

## 🔧 Commandes de Test

### Test Local

```bash
# Build une image localement
cd documentService
mvn compile jib:dockerBuild \
  -Djib.to.image=paperdms/documentservice:test

# Vérifie l'image
docker images | grep paperdms

# Run
docker run -p 8081:8081 paperdms/documentservice:test
```

### Test avec Conversion

```bash
# Simule la conversion
SERVICE="documentService"
SERVICE_LOWER=$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')
echo "Service: $SERVICE"
echo "Image: paperdms/$SERVICE_LOWER"

# Output:
# Service: documentService
# Image: paperdms/documentservice
```

---

## 🐛 Erreurs Connexes

### Erreur : "repository name must be lowercase"

```
Error: repository name must be lowercase
```

**Solution** : Même problème, utilise `tr '[:upper:]' '[:lower:]'`

### Erreur : "invalid reference format"

```
Error: invalid reference format
```

**Causes possibles** :
- Majuscules dans le nom
- Caractères spéciaux interdits
- Format incorrect (ex: manque `https://`)

**Solution** :
```bash
# Bon format
paperdms/service:tag

# Mauvais formats
❌ paperdms/Service:tag
❌ paperdms/service:Tag
❌ paperdms service:tag
❌ https://paperdms/service:tag
```

---

## 📝 Modifications dans PaperDMS

### Fichiers Modifiés

#### 1. `.github/workflows/ci-cd-optimized.yml`

```yaml
# Avant
-Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${{ matrix.service }}

# Après
SERVICE_LOWER=$(echo "${{ matrix.service }}" | tr '[:upper:]' '[:lower:]')
-Djib.to.image=ghcr.io/${{ github.repository_owner }}/paperdms-${SERVICE_LOWER}
```

#### 2. `.github/workflows/ci-cd-pipeline.yml`

```yaml
# Avant
-Djib.to.image=paperdms/${{ matrix.service }}

# Après
SERVICE_LOWER=$(echo "${{ matrix.service }}" | tr '[:upper:]' '[:lower:]')
-Djib.to.image=paperdms/${SERVICE_LOWER}
```

---

## ✅ Résultat

### Avant (Erreur)

```bash
# Service: archiveService
mvn jib:build -Djib.to.image=ghcr.io/user/paperdms-archiveService:1.0.0
# ❌ Error: Invalid image reference (uppercase S)
```

### Après (Succès)

```bash
# Service: archiveService
SERVICE_LOWER=$(echo "archiveService" | tr '[:upper:]' '[:lower:]')
# SERVICE_LOWER = "archiveservice"

mvn jib:build -Djib.to.image=ghcr.io/user/paperdms-archiveservice:1.0.0
# ✅ Success!
```

---

## 🎓 Best Practices

### 1. Toujours en Minuscules

```bash
# Dans les scripts
IMAGE_NAME=$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')
```

### 2. Utilise des Tirets pour la Lisibilité

```
# Moins lisible
paperdms/documentservice

# Plus lisible
paperdms/document-service
```

### 3. Convention Cohérente

Choisis une convention et applique-la partout :

**Option A** : `paperdms-documentservice`
**Option B** : `paperdms/document-service`

### 4. Documente les Noms

Dans ton README :

```markdown
## Images Docker

- `ghcr.io/drfred1981/paperdms-gateway:latest`
- `ghcr.io/drfred1981/paperdms-documentservice:latest`
- `ghcr.io/drfred1981/paperdms-ocrservice:latest`
```

---

## 📦 Pull des Images

### Après Correction

```bash
# Pull depuis GHCR
docker pull ghcr.io/drfred1981/paperdms-documentservice:latest
docker pull ghcr.io/drfred1981/paperdms-archiveservice:latest

# Pull depuis Docker Hub
docker pull paperdms/documentservice:latest
docker pull paperdms/archiveservice:latest
```

### Docker Compose

```yaml
version: '3.8'

services:
  gateway:
    image: ghcr.io/drfred1981/paperdms-gateway:latest
  
  documentservice:
    image: ghcr.io/drfred1981/paperdms-documentservice:latest
  
  archiveservice:
    image: ghcr.io/drfred1981/paperdms-archiveservice:latest
```

---

## 🔍 Vérification

### Dans GitHub Actions Logs

```
🐳 Building and pushing Docker image for tag v1.0.0
[INFO] Building and pushing image 'ghcr.io/drfred1981/paperdms-documentservice:v1.0.0'
[INFO] Using credentials from Docker config for ghcr.io/drfred1981/paperdms-documentservice:v1.0.0
[INFO] Container entrypoint set to [java, -cp, @/app/jib-classpath-file, fr.smartprod.paperdms.DocumentServiceApp]
[INFO] Built and pushed image as ghcr.io/drfred1981/paperdms-documentservice:v1.0.0
[INFO] Executing tasks:
[INFO] [==============================] 100.0% complete
✅ Successfully pushed ghcr.io/drfred1981/paperdms-documentservice:v1.0.0
```

### Dans GitHub Packages

Tu verras les images avec les noms corrects :
```
📦 paperdms-gateway
📦 paperdms-documentservice    ← Tout en minuscules
📦 paperdms-archiveservice     ← Tout en minuscules
```

---

## ✅ Résumé

### Problème

```
archiveService → paperdms-archiveService
❌ Majuscule "S" → Erreur Docker
```

### Solution

```bash
SERVICE_LOWER=$(echo "$SERVICE" | tr '[:upper:]' '[:lower:]')
archiveService → archiveservice
✅ Tout minuscules → Succès
```

### Commande de Conversion

```bash
tr '[:upper:]' '[:lower:]'
```

### Règle Docker

**Les noms d'images Docker doivent être EN MINUSCULES uniquement.**

---

✅ **Avec cette correction, toutes les images Docker seront créées avec succès !** 🐳🎉
