# Exemple pom.xml avec JPA Metamodel - PaperDMS

## 🎯 Configuration Maven Complète

Voici un exemple complet de `pom.xml` pour un microservice JHipster avec génération des classes métamodel.

---

## 📦 pom.xml Complet

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <!-- ========================================== -->
    <!-- Parent POM (Spring Boot) -->
    <!-- ========================================== -->
    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
        <relativePath/>
    </parent>

    <!-- ========================================== -->
    <!-- Project Information -->
    <!-- ========================================== -->
    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>documentService</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>
    <name>documentService</name>
    <description>Document Management Service for PaperDMS</description>

    <!-- ========================================== -->
    <!-- Properties -->
    <!-- ========================================== -->
    <properties>
        <!-- Java Version -->
        <java.version>17</java.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        
        <!-- Hibernate (for JPA Metamodel) -->
        <hibernate.version>6.2.13.Final</hibernate.version>
        
        <!-- MapStruct -->
        <mapstruct.version>1.5.5.Final</mapstruct.version>
        
        <!-- JHipster -->
        <jhipster.version>7.9.4</jhipster.version>
        
        <!-- Other -->
        <jib-maven-plugin.version>3.4.0</jib-maven-plugin.version>
        <maven-compiler-plugin.version>3.11.0</maven-compiler-plugin.version>
    </properties>

    <!-- ========================================== -->
    <!-- Dependencies -->
    <!-- ========================================== -->
    <dependencies>
        <!-- Spring Boot Starters -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-data-jpa</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-web</artifactId>
        </dependency>
        
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>

        <!-- Database -->
        <dependency>
            <groupId>org.postgresql</groupId>
            <artifactId>postgresql</artifactId>
            <scope>runtime</scope>
        </dependency>

        <!-- JHipster Framework -->
        <dependency>
            <groupId>tech.jhipster</groupId>
            <artifactId>jhipster-framework</artifactId>
            <version>${jhipster.version}</version>
        </dependency>

        <!-- MapStruct (for DTOs) -->
        <dependency>
            <groupId>org.mapstruct</groupId>
            <artifactId>mapstruct</artifactId>
            <version>${mapstruct.version}</version>
        </dependency>

        <!-- Hibernate JPA Metamodel Generator -->
        <!-- IMPORTANT: scope must be 'provided' -->
        <dependency>
            <groupId>org.hibernate.orm</groupId>
            <artifactId>hibernate-jpamodelgen</artifactId>
            <version>${hibernate.version}</version>
            <scope>provided</scope>
        </dependency>

        <!-- Lombok (optional) -->
        <dependency>
            <groupId>org.projectlombok</groupId>
            <artifactId>lombok</artifactId>
            <optional>true</optional>
        </dependency>

        <!-- Testing -->
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-test</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <!-- ========================================== -->
    <!-- Build Configuration -->
    <!-- ========================================== -->
    <build>
        <defaultGoal>spring-boot:run</defaultGoal>
        
        <plugins>
            <!-- ====================================== -->
            <!-- Spring Boot Maven Plugin -->
            <!-- ====================================== -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
                <configuration>
                    <excludes>
                        <exclude>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                        </exclude>
                    </excludes>
                </configuration>
            </plugin>

            <!-- ====================================== -->
            <!-- Maven Compiler Plugin -->
            <!-- WITH ANNOTATION PROCESSORS -->
            <!-- ====================================== -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>${maven-compiler-plugin.version}</version>
                <configuration>
                    <source>${java.version}</source>
                    <target>${java.version}</target>
                    <release>${java.version}</release>
                    
                    <!-- Annotation Processor Paths -->
                    <annotationProcessorPaths>
                        <!-- 1. Hibernate JPA Metamodel Generator -->
                        <path>
                            <groupId>org.hibernate.orm</groupId>
                            <artifactId>hibernate-jpamodelgen</artifactId>
                            <version>${hibernate.version}</version>
                        </path>
                        
                        <!-- 2. MapStruct Processor -->
                        <path>
                            <groupId>org.mapstruct</groupId>
                            <artifactId>mapstruct-processor</artifactId>
                            <version>${mapstruct.version}</version>
                        </path>
                        
                        <!-- 3. Lombok (if used with MapStruct) -->
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok</artifactId>
                            <version>${lombok.version}</version>
                        </path>
                        
                        <!-- 4. Lombok MapStruct Binding -->
                        <path>
                            <groupId>org.projectlombok</groupId>
                            <artifactId>lombok-mapstruct-binding</artifactId>
                            <version>0.2.0</version>
                        </path>
                        
                        <!-- 5. Spring Boot Configuration Processor -->
                        <path>
                            <groupId>org.springframework.boot</groupId>
                            <artifactId>spring-boot-configuration-processor</artifactId>
                            <version>${spring-boot.version}</version>
                        </path>
                    </annotationProcessorPaths>
                    
                    <!-- Compiler Arguments -->
                    <compilerArgs>
                        <!-- JPA Metamodel options -->
                        <arg>-Ahibernate.jpamodelgen.suppressGenerationDate=true</arg>
                        
                        <!-- MapStruct options -->
                        <arg>-Amapstruct.defaultComponentModel=spring</arg>
                        <arg>-Amapstruct.suppressGeneratorTimestamp=true</arg>
                        
                        <!-- Java parameters for reflection -->
                        <arg>-parameters</arg>
                    </compilerArgs>
                    
                    <!-- Generated Sources Directory -->
                    <generatedSourcesDirectory>${project.build.directory}/generated-sources/annotations</generatedSourcesDirectory>
                </configuration>
            </plugin>

            <!-- ====================================== -->
            <!-- Build Helper Plugin -->
            <!-- Adds generated sources to classpath -->
            <!-- ====================================== -->
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
                                <source>${project.build.directory}/generated-sources/annotations</source>
                            </sources>
                        </configuration>
                    </execution>
                </executions>
            </plugin>

            <!-- ====================================== -->
            <!-- Jib Plugin (Docker Images) -->
            <!-- ====================================== -->
            <plugin>
                <groupId>com.google.cloud.tools</groupId>
                <artifactId>jib-maven-plugin</artifactId>
                <version>${jib-maven-plugin.version}</version>
                <configuration>
                    <from>
                        <image>eclipse-temurin:17-jre</image>
                    </from>
                    <to>
                        <image>paperdms/${project.artifactId}</image>
                        <tags>
                            <tag>latest</tag>
                            <tag>${project.version}</tag>
                        </tags>
                    </to>
                    <container>
                        <jvmFlags>
                            <jvmFlag>-Xms256m</jvmFlag>
                            <jvmFlag>-Xmx512m</jvmFlag>
                            <jvmFlag>-XX:+UseContainerSupport</jvmFlag>
                        </jvmFlags>
                        <ports>
                            <port>8081</port>
                        </ports>
                        <environment>
                            <SPRING_PROFILES_ACTIVE>prod</SPRING_PROFILES_ACTIVE>
                        </environment>
                        <creationTime>USE_CURRENT_TIMESTAMP</creationTime>
                    </container>
                </configuration>
            </plugin>

            <!-- ====================================== -->
            <!-- Maven Surefire (Tests) -->
            <!-- ====================================== -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <configuration>
                    <argLine>-Xmx1024m</argLine>
                    <runOrder>alphabetical</runOrder>
                </configuration>
            </plugin>
        </plugins>
    </build>

    <!-- ========================================== -->
    <!-- Profiles -->
    <!-- ========================================== -->
    <profiles>
        <!-- Production Profile -->
        <profile>
            <id>prod</id>
            <properties>
                <spring.profiles.active>prod</spring.profiles.active>
            </properties>
            <build>
                <plugins>
                    <!-- Additional production plugins here -->
                </plugins>
            </build>
        </profile>
        
        <!-- Development Profile -->
        <profile>
            <id>dev</id>
            <activation>
                <activeByDefault>true</activeByDefault>
            </activation>
            <properties>
                <spring.profiles.active>dev</spring.profiles.active>
            </properties>
        </profile>
    </profiles>
</project>
```

---

## 🎯 Points Clés de la Configuration

### 1. Dépendance hibernate-jpamodelgen

```xml
<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <version>${hibernate.version}</version>
    <scope>provided</scope>  <!-- IMPORTANT: provided scope -->
</dependency>
```

**Pourquoi `provided`** : Car c'est seulement utilisé à la compilation pour générer les classes, pas au runtime.

### 2. Annotation Processor Paths

```xml
<annotationProcessorPaths>
    <!-- Hibernate pour les classes *_.java -->
    <path>
        <groupId>org.hibernate.orm</groupId>
        <artifactId>hibernate-jpamodelgen</artifactId>
        <version>${hibernate.version}</version>
    </path>
    
    <!-- MapStruct pour les mappers -->
    <path>
        <groupId>org.mapstruct</groupId>
        <artifactId>mapstruct-processor</artifactId>
        <version>${mapstruct.version}</version>
    </path>
</annotationProcessorPaths>
```

### 3. Compiler Args

```xml
<compilerArgs>
    <!-- Supprime la date de génération (pour Git) -->
    <arg>-Ahibernate.jpamodelgen.suppressGenerationDate=true</arg>
    
    <!-- MapStruct en tant que Spring Bean -->
    <arg>-Amapstruct.defaultComponentModel=spring</arg>
    
    <!-- Préserve les noms de paramètres -->
    <arg>-parameters</arg>
</compilerArgs>
```

---

## 🔧 Utilisation

### Build Local

```bash
# Clean et génère les métamodels
mvn clean process-sources

# Compile
mvn compile

# Package complet
mvn clean package

# Run
mvn spring-boot:run
```

### Vérifier la Génération

```bash
# Vérifie que les classes sont générées
ls -la target/generated-sources/annotations/fr/smartprod/paperdms/document/domain/

# Devrait afficher :
# DocumentType_.java
# Document_.java
# Tag_.java
# etc.
```

### Dans GitHub Actions

```yaml
- name: Build documentService
  working-directory: documentService
  run: mvn clean package -Pprod -DskipTests
```

---

## 📝 Structure des Fichiers Générés

### Avant Build

```
documentService/
├── src/
│   └── main/
│       └── java/
│           └── fr/smartprod/paperdms/document/
│               ├── domain/
│               │   ├── DocumentType.java
│               │   └── Document.java
│               └── service/
│                   └── DocumentTypeQueryService.java
└── target/
    └── (vide)
```

### Après Build

```
documentService/
├── src/
│   └── main/
│       └── java/
│           └── fr/smartprod/paperdms/document/
│               ├── domain/
│               │   ├── DocumentType.java
│               │   └── Document.java
│               └── service/
│                   └── DocumentTypeQueryService.java
└── target/
    └── generated-sources/
        └── annotations/
            └── fr/smartprod/paperdms/document/domain/
                ├── DocumentType_.java    ← Généré !
                └── Document_.java        ← Généré !
```

---

## 🎓 Versions par JHipster

### JHipster 7.x (Spring Boot 2.7.x)

```xml
<properties>
    <hibernate.version>6.2.13.Final</hibernate.version>
</properties>

<dependency>
    <groupId>org.hibernate.orm</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <version>${hibernate.version}</version>
    <scope>provided</scope>
</dependency>
```

### JHipster 6.x (Spring Boot 2.2.x)

```xml
<properties>
    <hibernate.version>5.6.15.Final</hibernate.version>
</properties>

<dependency>
    <groupId>org.hibernate</groupId>
    <artifactId>hibernate-jpamodelgen</artifactId>
    <version>${hibernate.version}</version>
    <scope>provided</scope>
</dependency>
```

**Note** : Utilise `org.hibernate` au lieu de `org.hibernate.orm` pour les versions 5.x.

---

## ✅ Checklist d'Implémentation

- [ ] Copier la configuration `maven-compiler-plugin` dans pom.xml
- [ ] Ajouter la dépendance `hibernate-jpamodelgen` avec scope `provided`
- [ ] Vérifier la version de Hibernate (doit correspondre à Spring Boot)
- [ ] Tester : `mvn clean process-sources`
- [ ] Vérifier que `target/generated-sources/annotations` contient les classes `*_.java`
- [ ] Tester : `mvn clean package`
- [ ] Commit et push
- [ ] Vérifier le build dans GitHub Actions

---

## 🔄 Appliquer à Tous les Services

Pour appliquer cette configuration à tous les microservices :

```bash
# Pour chaque service
for service in gateway documentService ocrService searchService; do
    echo "Updating $service/pom.xml"
    # Ajoute la configuration maven-compiler-plugin
    # Ajoute la dépendance hibernate-jpamodelgen
done

# Test
mvn clean package -pl gateway,documentService,ocrService,searchService
```

---

✅ **Avec cette configuration, les métamodels JPA seront générés automatiquement !** 🎉
