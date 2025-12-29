# Fix JPA Metamodel Errors - PaperDMS

## 🐛 Problème

Erreur de compilation dans GitHub Actions (et parfois en local) :

```
cannot find symbol
symbol: variable DocumentType_
location: class fr.smartprod.paperdms.document.service.DocumentTypeQueryService
```

---

## 🎯 Cause

Les classes **JPA Metamodel** (`DocumentType_`, `Document_`, etc.) ne sont pas générées avant la compilation.

Ces classes sont auto-générées par le **Hibernate JPA Modelgen** et sont nécessaires pour les requêtes Criteria API dans les `QueryService`.

---

## ✅ Solutions

### Solution 1 : Configuration Maven (Recommandée)

Ajoute le plugin `maven-compiler-plugin` avec le bon processor dans le `pom.xml` de chaque microservice.

#### Dans `documentService/pom.xml`

Ajoute ou modifie dans `<build><plugins>` :

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <annotationProcessorPaths>
            <!-- JPA Metamodel Generator -->
            <path>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-jpamodelgen</artifactId>
                <version>${hibernate.version}</version>
            </path>
            <!-- MapStruct (si utilisé) -->
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${mapstruct.version}</version>
            </path>
        </annotationProcessorPaths>
        <compilerArgs>
            <arg>-Ahibernate.jpamodelgen.suppressGenerationDate=true</arg>
        </compilerArgs>
    </configuration>
</plugin>
```

#### Version Complète avec toutes les Options

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>${java.version}</source>
        <target>${java.version}</target>
        <release>${java.version}</release>
        
        <!-- Annotation Processors -->
        <annotationProcessorPaths>
            <!-- Hibernate JPA Modelgen for Metamodel -->
            <path>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-jpamodelgen</artifactId>
                <version>${hibernate.version}</version>
            </path>
            
            <!-- MapStruct -->
            <path>
                <groupId>org.mapstruct</groupId>
                <artifactId>mapstruct-processor</artifactId>
                <version>${mapstruct.version}</version>
            </path>
            
            <!-- Spring Boot Configuration Processor -->
            <path>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-configuration-processor</artifactId>
                <version>${spring-boot.version}</version>
            </path>
        </annotationProcessorPaths>
        
        <!-- Compiler Arguments -->
        <compilerArgs>
            <!-- JPA Modelgen options -->
            <arg>-Ahibernate.jpamodelgen.suppressGenerationDate=true</arg>
            <arg>-parameters</arg>
        </compilerArgs>
        
        <!-- Generate metamodel in target/generated-sources -->
        <generatedSourcesDirectory>target/generated-sources/annotations</generatedSourcesDirectory>
    </configuration>
</plugin>
```

---

### Solution 2 : Ajouter la Dépendance

Vérifie que la dépendance est présente dans `pom.xml` :

```xml
<dependencies>
    <!-- JPA Metamodel Generator -->
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>${hibernate.version}</version>
        <scope>provided</scope>
    </dependency>
    
    <!-- Ou si tu utilises une version plus ancienne -->
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>6.2.13.Final</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

**Note** : Le `scope` doit être `provided` car c'est seulement utilisé à la compilation.

---

### Solution 3 : Génération Explicite des Sources

Ajoute le plugin `build-helper-maven-plugin` pour inclure les sources générées :

```xml
<plugin>
    <groupId>org.codehaus.mojo</groupId>
    <artifactId>build-helper-maven-plugin</artifactId>
    <version>3.4.0</version>
    <executions>
        <execution>
            <id>add-source</id>
            <phase>generate-sources</phase>
            <goals>
                <goal>add-source</goal>
            </goals>
            <configuration>
                <sources>
                    <source>target/generated-sources/annotations</source>
                </sources>
            </configuration>
        </execution>
    </executions>
</plugin>
```

---

### Solution 4 : Fix pour GitHub Actions

Dans ton workflow GitHub Actions, assure-toi de builder avec la phase `process-sources` :

#### Dans `.github/workflows/ci-cd-pipeline.yml`

```yaml
- name: Build ${{ matrix.service }}
  working-directory: ${{ matrix.service }}
  run: |
    echo "Building service: ${{ matrix.service }}"
    
    # Generate sources first, then compile
    mvn clean process-sources compile -Pprod -DskipTests
```

Ou utilise le cycle complet :

```yaml
- name: Build ${{ matrix.service }}
  working-directory: ${{ matrix.service }}
  run: |
    # Full build with source generation
    mvn clean package -Pprod -DskipTests
```

---

## 🔧 Configuration par Service

### Pour JHipster >= 7.x

Utilise `org.hibernate.orm:hibernate-jpamodelgen` :

```xml
<properties>
    <hibernate.version>6.2.13.Final</hibernate.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>${hibernate.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

### Pour JHipster < 7.x

Utilise `org.hibernate:hibernate-jpamodelgen` :

```xml
<properties>
    <hibernate.version>5.6.15.Final</hibernate.version>
</properties>

<dependencies>
    <dependency>
        <groupId>org.hibernate</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>${hibernate.version}</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

---

## 🎯 Configuration Complète Recommandée

### `pom.xml` Complet pour un Microservice

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0">
    
    <properties>
        <java.version>17</java.version>
        <hibernate.version>6.2.13.Final</hibernate.version>
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        <spring-boot.version>2.7.18</spring-boot.version>
    </properties>

    <dependencies>
        <!-- Spring Boot -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>

        <!-- JPA Metamodel Generator (provided scope) -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-jpamodelgen</artifactId>
            <version>${hibernate.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- MapStruct -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <!-- Compiler Plugin with Annotation Processors -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <annotationProcessorPaths>
                        <!-- Hibernate JPA Modelgen -->
                        <path>
                            <groupId>org.hibernate.orm</groupId>
                            <artifactId>hibernate-jpamodelgen</artifactId>
                            <version>${hibernate.version}</version>
                        </path>
                        <!-- MapStruct Processor -->
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    <compilerArgs>
                        <arg>-Ahibernate.jpamodelgen.suppressGenerationDate=true</arg>
                        <arg>-parameters</arg>
                    </compilerArgs>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## 🚀 Commandes de Test

### Test en Local

```bash
# Clean et régénère tout
cd documentService
mvn clean process-sources

# Vérifie que les classes sont générées
ls -la target/generated-sources/annotations/fr/smartprod/paperdms/document/domain/

# Tu devrais voir :
# DocumentType_.java
# Document_.java
# etc.

# Compile
mvn compile

# Build complet
mvn clean package
```

### Test dans GitHub Actions

```bash
# Commit et push
git add pom.xml
git commit -m "fix: add JPA metamodel generation"
git push

# Vérifie les logs GitHub Actions
# Les classes métamodel devraient être générées
```

---

## 🐛 Troubleshooting

### Problème 1 : Classes toujours pas générées

**Solution** : Vérifie la version de Hibernate

```xml
<!-- Vérifie la version dans pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Affiche la version utilisée -->
mvn dependency:tree | grep hibernate
```

### Problème 2 : Erreur "processor not found"

**Solution** : Ajoute la dépendance avec scope `provided`

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <version>${hibernate.version}</version>
    <scope>provided</scope>  <!-- Important ! -->
</dependency>
```

### Problème 3 : Fonctionne en local mais pas en CI/CD

**Solution** : Nettoie le cache Maven dans GitHub Actions

```yaml
- name: Cache Maven packages
  uses: actions/cache@v3
  with:
    path: ~/.m2/repository
    key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
    restore-keys: |
      ${{ runner.os }}-maven-

- name: Build with clean
  run: mvn clean compile  # Force clean avant compile
```

### Problème 4 : IntelliJ ne voit pas les classes

**Solution** : Configure IntelliJ

```
1. File → Settings → Build, Execution, Deployment → Compiler → Annotation Processors
2. ✅ Enable annotation processing
3. Store generated sources relative to: Module content root
4. Production sources directory: target/generated-sources/annotations

5. Rebuild project:
   Build → Rebuild Project
```

### Problème 5 : Conflit de versions

**Solution** : Force la version de hibernate-jpamodelgen

```xml
<properties>
    <!-- Force une version spécifique -->
    <hibernate.version>6.2.13.Final</hibernate.version>
</properties>

<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-jpamodelgen</artifactId>
            <version>${hibernate.version}</version>
        </dependency>
    </dependencies>
</dependencyManagement>
```

---

## 📝 Exemple de Classes Générées

### Avant Génération

```java
// DocumentTypeQueryService.java
public class DocumentTypeQueryService {
    public Page<DocumentTypeDTO> findByCriteria(DocumentTypeCriteria criteria, Pageable page) {
        Specification<DocumentType> specification = createSpecification(criteria);
        return documentTypeRepository.findAll(specification, page)
            .map(documentTypeMapper::toDto);
    }
    
    protected Specification<DocumentType> createSpecification(DocumentTypeCriteria criteria) {
        Specification<DocumentType> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getName() != null) {
                // ❌ Erreur: cannot find symbol DocumentType_
                specification = specification.and(
                    (root, query, builder) -> builder.equal(root.get(DocumentType_.name), criteria.getName())
                );
            }
        }
        return specification;
    }
}
```

### Après Génération (target/generated-sources/annotations/)

```java
// DocumentType_.java (auto-généré)
package fr.smartprod.paperdms.document.domain;

import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import java.time.Instant;

@StaticMetamodel(DocumentType.class)
public abstract class DocumentType_ {
    public static volatile SingularAttribute<DocumentType, Long> id;
    public static volatile SingularAttribute<DocumentType, String> name;
    public static volatile SingularAttribute<DocumentType, String> description;
    public static volatile SingularAttribute<DocumentType, Instant> createdDate;
    public static volatile SingularAttribute<DocumentType, Instant> lastModifiedDate;
    
    public static final String ID = "id";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String CREATED_DATE = "createdDate";
    public static final String LAST_MODIFIED_DATE = "lastModifiedDate";
}
```

---

## 🎯 Workflow GitHub Actions Mis à Jour

### Modification dans `.github/workflows/ci-cd-pipeline.yml`

```yaml
- name: Build ${{ matrix.service }}
  working-directory: ${{ matrix.service }}
  run: |
    echo "=========================================="
    echo "Building service: ${{ matrix.service }}"
    echo "=========================================="
    
    # Generate JPA metamodel classes, then build
    mvn clean package -Pprod -DskipTests -Drevision=${{ steps.version.outputs.version }}
```

Le `mvn clean package` va automatiquement :
1. Nettoyer (`clean`)
2. Générer les sources (`process-sources`)
3. Compiler (`compile`)
4. Packager (`package`)

---

## ✅ Checklist

- [ ] Ajouter `maven-compiler-plugin` avec `annotationProcessorPaths` dans tous les pom.xml
- [ ] Ajouter dépendance `hibernate-jpamodelgen` avec scope `provided`
- [ ] Vérifier la version de Hibernate (6.x pour JHipster 7+)
- [ ] Tester en local : `mvn clean package`
- [ ] Vérifier que `target/generated-sources/annotations` contient les classes `*_.java`
- [ ] Commit et push
- [ ] Vérifier que GitHub Actions build sans erreur
- [ ] (Optionnel) Configurer IntelliJ pour reconnaître les sources générées

---

## 📖 Résumé

### Cause du Problème

Les classes JPA Metamodel (`DocumentType_`, `Document_`, etc.) ne sont pas générées.

### Solution Rapide

Ajoute dans **chaque microservice** `pom.xml` :

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.11.0</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <annotationProcessorPaths>
            <path>
                <groupId>org.hibernate.orm</groupId>
                <artifactId>hibernate-jpamodelgen</artifactId>
                <version>${hibernate.version}</version>
            </path>
        </annotationProcessorPaths>
    </configuration>
</plugin>
```

Et :

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <version>${hibernate.version}</version>
    <scope>provided</scope>
</dependency>
```

### Test

```bash
cd documentService
mvn clean package
# ✅ Doit compiler sans erreur
```

---

✅ **Avec cette configuration, les classes métamodel seront générées automatiquement !** 🎉
