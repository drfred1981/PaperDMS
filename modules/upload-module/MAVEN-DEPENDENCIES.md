# Dépendances Maven à ajouter au pom.xml de documentService

## Dans la section <dependencies>

Ajoute ces dépendances dans le fichier `documentService/pom.xml` dans la section `<dependencies>` :

```xml
<!-- PaperDMS Common Library (Shared Kafka Events) -->
<dependency>
    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>paperdms-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<!-- AWS SDK for S3 Storage -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>

<!-- AWS SDK Core (required for S3) -->
<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>auth</artifactId>
    <version>2.20.0</version>
</dependency>

<!-- Apache PDFBox for PDF Processing -->
<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.0</version>
</dependency>

<!-- Spring Kafka (normalement déjà inclus par JHipster) -->
<!-- Si absent, décommenter : -->
<!--
<dependency>
    <groupId>org.springframework.kafka</groupId>
    <artifactId>spring-kafka</artifactId>
</dependency>
-->

<!-- Jackson Datatype JSR310 for Java 8 Date/Time -->
<!-- Normalement déjà inclus par JHipster, mais si absent : -->
<!--
<dependency>
    <groupId>com.fasterxml.jackson.datatype</groupId>
    <artifactId>jackson-datatype-jsr310</artifactId>
</dependency>
-->
```

## Exemple complet de section dependencies

Voici un exemple complet de la section `<dependencies>` dans ton `pom.xml` :

```xml
<dependencies>
    <!-- Spring Boot Starters (déjà générés par JHipster) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-jpa</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-security</artifactId>
    </dependency>
    
    <!-- PostgreSQL Driver (déjà généré par JHipster) -->
    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <scope>runtime</scope>
    </dependency>
    
    <!-- Redis (déjà généré par JHipster) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-redis</artifactId>
    </dependency>
    
    <!-- Kafka (déjà généré par JHipster) -->
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka</artifactId>
    </dependency>
    
    <!-- Elasticsearch (déjà généré par JHipster) -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-data-elasticsearch</artifactId>
    </dependency>
    
    <!-- ========================================== -->
    <!-- NOUVELLES DÉPENDANCES À AJOUTER ICI -->
    <!-- ========================================== -->
    
    <!-- PaperDMS Common Library -->
    <dependency>
        <groupId>fr.smartprod.paperdms</groupId>
        <artifactId>paperdms-common</artifactId>
        <version>1.0.0-SNAPSHOT</version>
    </dependency>
    
    <!-- AWS SDK for S3 -->
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>s3</artifactId>
        <version>2.20.0</version>
    </dependency>
    
    <dependency>
        <groupId>software.amazon.awssdk</groupId>
        <artifactId>auth</artifactId>
        <version>2.20.0</version>
    </dependency>
    
    <!-- Apache PDFBox -->
    <dependency>
        <groupId>org.apache.pdfbox</groupId>
        <artifactId>pdfbox</artifactId>
        <version>3.0.0</version>
    </dependency>
    
    <!-- ========================================== -->
    <!-- FIN DES NOUVELLES DÉPENDANCES -->
    <!-- ========================================== -->
    
    <!-- MapStruct (déjà généré par JHipster) -->
    <dependency>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct</artifactId>
        <version>${mapstruct.version}</version>
    </dependency>
    
    <!-- Test Dependencies -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.kafka</groupId>
        <artifactId>spring-kafka-test</artifactId>
        <scope>test</scope>
    </dependency>
</dependencies>
```

## Notes importantes

1. **Ordre des dépendances** : L'ordre n'est pas critique, mais par convention on met :
   - D'abord les dépendances Spring Boot
   - Ensuite les dépendances externes (AWS, PDFBox)
   - Puis les dépendances de test

2. **Versions** : 
   - Les versions Spring Boot sont gérées par le parent JHipster
   - Les versions AWS SDK et PDFBox sont spécifiées explicitement
   - La version de paperdms-common doit correspondre à celle définie dans son pom.xml

3. **Scope** :
   - Par défaut le scope est `compile`
   - `runtime` pour les drivers JDBC
   - `test` pour les dépendances de test uniquement

4. **Vérification** :
   Après avoir ajouté les dépendances, vérifie qu'elles sont bien résolues :
   ```bash
   cd documentService
   mvn dependency:tree
   ```

## Dépendances optionnelles (si besoin)

Si tu rencontres des problèmes, tu peux ajouter ces dépendances optionnelles :

```xml
<!-- SLF4J API (normalement déjà inclus) -->
<dependency>
    <groupId>org.slf4j</groupId>
    <artifactId>slf4j-api</artifactId>
</dependency>

<!-- Jakarta Persistence API (normalement déjà inclus) -->
<dependency>
    <groupId>jakarta.persistence</groupId>
    <artifactId>jakarta.persistence-api</artifactId>
</dependency>

<!-- Validation API (normalement déjà inclus) -->
<dependency>
    <groupId>jakarta.validation</groupId>
    <artifactId>jakarta.validation-api</artifactId>
</dependency>
```