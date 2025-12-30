# 📦 PaperDMS Common

Module partagé contenant les DTOs et Events Kafka communs à tous les microservices PaperDMS.

## 🎯 Objectif

Éviter la duplication de code entre les microservices en centralisant :
- 📨 **Events Kafka** - DTOs pour la communication asynchrone
- 📋 **DTOs communs** - Structures de données partagées
- 🔧 **Utilitaires** - Classes helper communes

## 📦 Contenu

### Events Kafka

| Event | Topic | Producteur | Consommateurs |
|-------|-------|------------|---------------|
| `DocumentUploadedEvent` | `document.uploaded` | documentService | ocrService, aiService, searchService, notificationService |
| `DocumentDeletedEvent` | `document.deleted` | documentService | searchService, notificationService |
| `DocumentUpdatedEvent` | `document.updated` | documentService | searchService |

### DTOs

_(À ajouter selon les besoins)_

## 🚀 Installation

### 1. Compiler et Installer Localement

```bash
cd paperdms-common
mvn clean install
```

Cela installe le JAR dans votre repository Maven local : `~/.m2/repository/`

### 2. Utiliser dans un Microservice

Ajoutez cette dépendance dans le `pom.xml` de votre microservice :

```xml
<dependency>
    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>paperdms-common</artifactId>
    <version>1.0.0</version>
</dependency>
```

## 📝 Utilisation

### Producer (documentService)

```java
import fr.smartprod.paperdms.common.events.DocumentUploadedEvent;

@Service
public class DocumentUploadService {
    
    private final KafkaTemplate<String, Object> kafkaTemplate;
    
    private void publishEvent(Document document) {
        DocumentUploadedEvent event = DocumentUploadedEvent.builder()
            .documentId(document.getId())
            .sha256(document.getSha256())
            .mimeType(document.getMimeType())
            .fileSize(document.getFileSize())
            .s3Key(document.getS3Key())
            .s3Bucket(document.getS3Bucket())
            .uploadDate(document.getUploadDate())
            .uploadedBy(SecurityUtils.getCurrentUserLogin().orElse("system"))
            .build();
        
        kafkaTemplate.send("document.uploaded", document.getId().toString(), event);
        log.info("Published document.uploaded event: {}", event);
    }
}
```

### Consumer (ocrService)

```java
import fr.smartprod.paperdms.common.events.DocumentUploadedEvent;

@Service
public class OcrConsumer {
    
    private final Logger log = LoggerFactory.getLogger(OcrConsumer.class);
    
    @KafkaListener(
        topics = "document.uploaded", 
        groupId = "ocr-service",
        containerFactory = "kafkaListenerContainerFactory"
    )
    public void handleDocumentUploaded(DocumentUploadedEvent event) {
        log.info("Received document.uploaded event: {}", event);
        
        // Filtrer uniquement les PDFs et images
        if (isPdfOrImage(event.getMimeType())) {
            // Télécharger depuis S3
            byte[] content = s3Service.download(event.getS3Bucket(), event.getS3Key());
            
            // Extraire le texte
            String extractedText = ocrService.extractText(content, event.getMimeType());
            
            // Sauvegarder
            documentRepository.updateExtractedText(event.getDocumentId(), extractedText);
            
            log.info("OCR completed for document: {}", event.getDocumentId());
        }
    }
    
    private boolean isPdfOrImage(String mimeType) {
        return mimeType != null && 
               (mimeType.equals("application/pdf") || mimeType.startsWith("image/"));
    }
}
```

### Consumer (aiService)

```java
import fr.smartprod.paperdms.common.events.DocumentUploadedEvent;

@Service
public class AutoTaggingConsumer {
    
    @KafkaListener(topics = "document.uploaded", groupId = "ai-service")
    public void handleDocumentUploaded(DocumentUploadedEvent event) {
        log.info("Auto-tagging document: {}", event.getDocumentId());
        
        // Récupérer le texte extrait par OCR
        String text = documentRepository.findExtractedText(event.getDocumentId());
        
        // Analyser avec IA
        List<String> suggestedTags = aiService.suggestTags(
            text, 
            event.getFileName(), 
            event.getMimeType()
        );
        
        // Appliquer les tags
        tagService.applyTags(event.getDocumentId(), suggestedTags);
    }
}
```

## 🔧 Développement

### Ajouter un Nouvel Event

1. Créer la classe dans `src/main/java/.../events/`
2. Implémenter `Serializable`
3. Ajouter les annotations Jackson pour JSON
4. Documenter le producteur et les consommateurs
5. Compiler : `mvn clean install`
6. Mettre à jour ce README

### Versioning

Suivez le [Semantic Versioning](https://semver.org/) :

- **1.0.0** → **1.0.1** : Bug fix, compatible
- **1.0.0** → **1.1.0** : Nouvelle fonctionnalité, compatible
- **1.0.0** → **2.0.0** : Breaking change, incompatible

### Tests

```bash
# Compiler
mvn clean compile

# Tester
mvn test

# Package
mvn package
```

## 📚 Structure du Projet

```
paperdms-common/
├── pom.xml
├── README.md
└── src/
    └── main/
        └── java/
            └── fr/smartprod/paperdms/common/
                ├── events/
                │   ├── DocumentUploadedEvent.java
                │   ├── DocumentDeletedEvent.java
                │   └── DocumentUpdatedEvent.java
                ├── dto/
                │   └── (DTOs communs)
                └── util/
                    └── (Classes utilitaires)
```

## 🔄 Workflow CI/CD

### Build Local

```bash
mvn clean install
```

### Build CI (GitHub Actions / GitLab CI)

```yaml
- name: Build paperdms-common
  run: |
    cd paperdms-common
    mvn clean install
```

### Publish to Nexus (Production)

```bash
mvn deploy
```

## 📋 Checklist d'Utilisation

### Pour Ajouter dans un Nouveau Service

- [ ] Ajouter la dépendance dans `pom.xml`
- [ ] Importer les events nécessaires
- [ ] Configurer Kafka dans `application.yml`
- [ ] Créer les consumers/producers
- [ ] Tester la communication

## 🆘 Dépannage

### Erreur: "Cannot resolve paperdms-common"

**Solution** : Installer le module dans votre repo local
```bash
cd paperdms-common
mvn clean install
```

### Erreur de Sérialisation JSON

**Solution** : Vérifier que Jackson est configuré
```yaml
spring:
  kafka:
    producer:
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer
    consumer:
      value-deserializer: org.springframework.kafka.support.serializer.JsonDeserializer
      properties:
        spring.json.trusted.packages: "fr.smartprod.paperdms.common.events"
```

### Event Non Reçu

**Solution** : Vérifier les topics et group IDs
```bash
# Lister les topics
kafka-topics --bootstrap-server localhost:9092 --list

# Consumer console
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic document.uploaded --from-beginning
```

## 🎓 Bonnes Pratiques

✅ **À FAIRE**
- Toujours incrémenter la version après modification
- Documenter chaque event (producteur, consommateurs, topic)
- Utiliser le builder pattern
- Implémenter `toString()`, `equals()`, `hashCode()`
- Tester la sérialisation/désérialisation

❌ **À ÉVITER**
- Ne pas modifier les events existants (créer une v2 si besoin)
- Ne pas inclure de logique métier dans les events
- Ne pas oublier `serialVersionUID`

## 📞 Support

En cas de problème avec le module common :
1. Vérifier que la version est à jour
2. Consulter la Javadoc générée
3. Vérifier les logs Kafka

---

**Module Common = Code DRY + Communication Standardisée ! 📦**

Version: 1.0.0
