# Fichiers Manquants - Vérification et Résolution

## ✅ Problème Résolu

Les classes manquantes ont été ajoutées. Voici le récapitulatif complet :

## 📦 Bibliothèque Partagée paperdms-common

### Localisation
`paperdms-common/src/main/java/fr/smartprod/paperdms/common/event/`

### Classes Présentes (6 fichiers Java)

✅ **DocumentEventType.java**
- Énumération des types d'événements Kafka
- 20+ types d'événements du cycle de vie des documents
- Exemples : DOCUMENT_UPLOADED, DOCUMENT_READY_FOR_OCR, etc.

✅ **DocumentEvent.java**
- Classe de base pour tous les événements
- Champs communs : eventId, eventType, documentId, timestamp, etc.
- Métadonnées extensibles avec Map<String, Object>

✅ **DocumentUploadEvent.java**
- Événement spécifique pour les uploads
- Extends DocumentEvent
- Champs supplémentaires : fileName, fileSize, s3Key, s3Bucket, etc.

✅ **DocumentServiceStatusEvent.java**
- Événement de changement de statut
- Extends DocumentEvent
- Champs : serviceType, status, errorMessage, retryCount, etc.

✅ **ServiceType.java** (NOUVEAU)
- Énumération des types de services
- 13 services : OCR_SERVICE, AI_SERVICE, WORKFLOW_SERVICE, etc.
- Correspond aux enums du fichier JDL

✅ **ServiceStatus.java** (NOUVEAU)
- Énumération des statuts de traitement
- 10 statuts : PENDING, IN_PROGRESS, COMPLETED, FAILED, etc.
- Correspond aux enums du fichier JDL

### Fichier de Configuration

✅ **pom.xml**
- Configuration Maven pour la bibliothèque partagée
- Dépendances Jackson pour JSON
- Version 1.0.0-SNAPSHOT

## 🔧 Dépendances Maven pour documentService

### Fichiers de Référence Créés

1. **MAVEN_DEPENDENCIES.md** (guide complet)
   - Explications détaillées de chaque dépendance
   - Exemples de section complète
   - Notes et conseils

2. **pom-snippet.xml** (snippet à copier-coller)
   - Extrait XML prêt à copier
   - Commentaires inclus
   - 4 dépendances essentielles

### Dépendances à Ajouter (4 au total)

```xml
<!-- 1. PaperDMS Common Library -->
<dependency>
    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>paperdms-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- 2. AWS SDK for S3 -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>

<!-- 3. AWS SDK Auth -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>auth</artifactId>
    <version>2.20.0</version>
</dependency>

<!-- 4. Apache PDFBox -->
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.0</version>
</dependency>
```

## 📋 Checklist d'Installation

### Étape 1 : Installer paperdms-common
```bash
cd paperdms-common
mvn clean install
```

Vérifier que l'installation est réussie :
```bash
ls ~/.m2/repository/fr/smartprod/paperdms/paperdms-common/1.0.0-SNAPSHOT/
```

Tu devrais voir :
- paperdms-common-1.0.0-SNAPSHOT.jar
- paperdms-common-1.0.0-SNAPSHOT.pom

### Étape 2 : Ajouter les Dépendances

Ouvre `documentService/pom.xml` et ajoute les 4 dépendances dans la section `<dependencies>`.

Tu peux copier-coller directement depuis `pom-snippet.xml`.

### Étape 3 : Vérifier les Dépendances

```bash
cd documentService
mvn dependency:tree
```

Tu devrais voir dans l'arbre :
```
[INFO] fr.smartprod.paperdms:documentService:jar:0.0.1-SNAPSHOT
[INFO] +- fr.smartprod.paperdms:paperdms-common:jar:1.0.0-SNAPSHOT:compile
[INFO] +- software.amazon.awssdk:s3:jar:2.20.0:compile
[INFO] +- software.amazon.awssdk:auth:jar:2.20.0:compile
[INFO] +- org.apache.pdfbox:pdfbox:jar:3.0.0:compile
```

### Étape 4 : Résoudre les Problèmes Potentiels

#### Problème : paperdms-common non trouvé
**Solution** :
```bash
cd paperdms-common
mvn clean install -DskipTests
```

#### Problème : Conflit de versions AWS SDK
**Solution** : Ajouter dans `<properties>` :
```xml
<aws.sdk.version>2.20.0</aws.sdk.version>
```

Puis dans les dépendances :
```xml
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>${aws.sdk.version}</version>
</dependency>
```

#### Problème : PDFBox conflit avec d'autres librairies
**Solution** : Exclure les dépendances transitives si nécessaire :
```xml
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.0</version>
    <exclusions>
        <exclusion>
            <groupId>commons-logging</groupId>
            <artifactId>commons-logging</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

## 🔍 Vérification Finale

### Test de Compilation

```bash
cd documentService
mvn clean compile
```

Si la compilation réussit, toutes les dépendances sont correctement installées.

### Test avec les Classes

Crée un fichier de test temporaire pour vérifier :

```java
// DocumentService/src/test/java/TestDependencies.java
import fr.smartprod.paperdms.common.event.DocumentEvent;
import fr.smartprod.paperdms.common.event.DocumentEventType;
import fr.smartprod.paperdms.common.event.ServiceType;
import fr.smartprod.paperdms.common.event.ServiceStatus;
import software.amazon.awssdk.services.s3.S3Client;
import org.apache.pdfbox.pdmodel.PDDocument;

public class TestDependencies {
    // Ce fichier compile = toutes les dépendances OK
}
```

Compile :
```bash
mvn test-compile
```

## 📊 Récapitulatif des Fichiers

### paperdms-common (7 fichiers)
- ✅ pom.xml
- ✅ DocumentEventType.java
- ✅ DocumentEvent.java
- ✅ DocumentUploadEvent.java
- ✅ DocumentServiceStatusEvent.java
- ✅ ServiceType.java (NOUVEAU)
- ✅ ServiceStatus.java (NOUVEAU)

### Guides de Dépendances (2 fichiers)
- ✅ MAVEN_DEPENDENCIES.md (guide détaillé)
- ✅ pom-snippet.xml (snippet à copier)

### Dépendances Maven (4 au total)
- ✅ paperdms-common:1.0.0-SNAPSHOT
- ✅ AWS SDK S3:2.20.0
- ✅ AWS SDK Auth:2.20.0
- ✅ Apache PDFBox:3.0.0

## 🎯 Prochaines Étapes

1. ✅ Installer paperdms-common
2. ✅ Ajouter les 4 dépendances au pom.xml
3. ✅ Vérifier avec `mvn dependency:tree`
4. ✅ Compiler avec `mvn clean compile`
5. ⏭️ Copier les fichiers d'implémentation
6. ⏭️ Démarrer les services

## 💡 Conseils

1. **Ordre d'installation** : Toujours installer paperdms-common en premier
2. **Cache Maven** : En cas de problème, vide le cache : `rm -rf ~/.m2/repository/fr/smartprod/paperdms`
3. **Version JDK** : Assure-toi d'utiliser Java 17+
4. **Version Maven** : Utilise Maven 3.9+

## 📞 En Cas de Problème

Si tu rencontres des erreurs :

1. Vérifie que paperdms-common est bien installé dans ~/.m2/repository
2. Vérifie que la version dans le pom correspond (1.0.0-SNAPSHOT)
3. Essaie `mvn clean` puis `mvn compile`
4. Vérifie les logs Maven pour identifier la dépendance manquante

---

✅ **Tous les fichiers sont maintenant présents et documentés !**
