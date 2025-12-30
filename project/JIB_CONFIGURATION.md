# Configuration Jib pour pom.xml

## 🎯 Plugin Jib - Configuration Maven

Voici la configuration à ajouter dans le `pom.xml` de **chaque microservice** pour permettre la génération d'images Docker.

---

## 📦 Configuration Complète

### Ajoute dans `<build><plugins>` de ton pom.xml

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <!-- Image de base -->
        <from>
            <image>eclipse-temurin:17-jre</image>
            <platforms>
                <platform>
                    <architecture>amd64</architecture>
                    <os>linux</os>
                </platform>
            </platforms>
        </from>
        
        <!-- Image de destination -->
        <to>
            <image>paperdms/${project.artifactId}</image>
            <tags>
                <tag>latest</tag>
                <tag>${project.version}</tag>
            </tags>
        </to>
        
        <!-- Configuration du container -->
        <container>
            <!-- JVM flags -->
            <jvmFlags>
                <jvmFlag>-Xms256m</jvmFlag>
                <jvmFlag>-Xmx512m</jvmFlag>
                <jvmFlag>-Djava.security.egd=file:/dev/./urandom</jvmFlag>
                <jvmFlag>-XX:+UseContainerSupport</jvmFlag>
            </jvmFlags>
            
            <!-- Ports exposés -->
            <ports>
                <port>8080</port>
                <port>8081</port>
            </ports>
            
            <!-- Labels -->
            <labels>
                <maintainer>PaperDMS Team</maintainer>
                <service>${project.artifactId}</service>
                <version>${project.version}</version>
            </labels>
            
            <!-- Format timestamp -->
            <creationTime>USE_CURRENT_TIMESTAMP</creationTime>
            
            <!-- User (non-root pour sécurité) -->
            <user>1000:1000</user>
            
            <!-- Working directory -->
            <workingDirectory>/app</workingDirectory>
            
            <!-- Environment variables -->
            <environment>
                <SPRING_OUTPUT_ANSI_ENABLED>ALWAYS</SPRING_OUTPUT_ANSI_ENABLED>
                <JHIPSTER_SLEEP>0</JHIPSTER_SLEEP>
            </environment>
        </container>
        
        <!-- Performance -->
        <allowInsecureRegistries>false</allowInsecureRegistries>
    </configuration>
</plugin>
```

---

## 🔧 Configurations Spécifiques par Service

### Gateway (avec Frontend Angular)

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre</image>
        </from>
        <to>
            <image>paperdms/gateway</image>
            <tags>
                <tag>latest</tag>
                <tag>${project.version}</tag>
            </tags>
        </to>
        <container>
            <jvmFlags>
                <jvmFlag>-Xms512m</jvmFlag>
                <jvmFlag>-Xmx1024m</jvmFlag>  <!-- Plus de RAM pour gateway -->
            </jvmFlags>
            <ports>
                <port>8080</port>  <!-- Port gateway -->
            </ports>
            <environment>
                <SPRING_PROFILES_ACTIVE>prod</SPRING_PROFILES_ACTIVE>
            </environment>
        </container>
    </configuration>
</plugin>
```

### DocumentService

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre</image>
        </from>
        <to>
            <image>paperdms/documentservice</image>
        </to>
        <container>
            <ports>
                <port>8081</port>  <!-- Port documentService -->
            </ports>
            <environment>
                <SPRING_PROFILES_ACTIVE>prod</SPRING_PROFILES_ACTIVE>
            </environment>
        </container>
    </configuration>
</plugin>
```

### OCR Service (plus de RAM pour OCR)

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre</image>
        </from>
        <to>
            <image>paperdms/ocrservice</image>
        </to>
        <container>
            <jvmFlags>
                <jvmFlag>-Xms1024m</jvmFlag>
                <jvmFlag>-Xmx2048m</jvmFlag>  <!-- Plus de RAM pour OCR -->
            </jvmFlags>
            <ports>
                <port>8082</port>
            </ports>
        </container>
    </configuration>
</plugin>
```

---

## 🚀 Commandes Maven avec Jib

### Build Local (sans Docker daemon)

```bash
# Build l'image sans Docker installé
mvn compile jib:build

# Build dans le registry local Docker
mvn compile jib:dockerBuild
```

### Build pour Docker Hub

```bash
# Build et push vers Docker Hub
mvn compile jib:build \
  -Djib.to.image=dockerhub-username/paperdms-gateway:1.0.0 \
  -Djib.to.auth.username=your-username \
  -Djib.to.auth.password=your-password
```

### Build pour GitHub Container Registry

```bash
# Build et push vers GHCR
mvn compile jib:build \
  -Djib.to.image=ghcr.io/username/paperdms-gateway:1.0.0 \
  -Djib.to.auth.username=github-username \
  -Djib.to.auth.password=github-token
```

### Build avec Profil Production

```bash
# Avec frontend compilé pour gateway
mvn -Pprod clean package jib:build
```

---

## 🔑 Configuration Registry

### Docker Hub

Dans `pom.xml` :
```xml
<to>
    <image>docker.io/your-username/${project.artifactId}</image>
</to>
```

### GitHub Container Registry

Dans `pom.xml` :
```xml
<to>
    <image>ghcr.io/your-username/paperdms-${project.artifactId}</image>
</to>
```

### Registry Privé

Dans `pom.xml` :
```xml
<to>
    <image>registry.example.com/paperdms/${project.artifactId}</image>
</to>
```

---

## 🎯 Multi-Architecture (ARM64 + AMD64)

Pour supporter plusieurs architectures :

```xml
<plugin>
    <groupId>com.google.cloud.tools</groupId>
    <artifactId>jib-maven-plugin</artifactId>
    <version>3.4.0</version>
    <configuration>
        <from>
            <image>eclipse-temurin:17-jre</image>
            <platforms>
                <platform>
                    <architecture>amd64</architecture>
                    <os>linux</os>
                </platform>
                <platform>
                    <architecture>arm64</architecture>
                    <os>linux</os>
                </platform>
            </platforms>
        </from>
    </configuration>
</plugin>
```

---

## 📦 Exemple pom.xml Complet

Voici un exemple de `pom.xml` avec Jib configuré :

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>documentService</artifactId>
    <version>1.0.0-SNAPSHOT</version>
    <packaging>jar</packaging>

    <parent>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-parent</artifactId>
        <version>2.7.18</version>
    </parent>

    <properties>
        <java.version>17</java.version>
        <jib-maven-plugin.version>3.4.0</jib-maven-plugin.version>
    </properties>

    <dependencies>
        <!-- Dependencies here -->
    </dependencies>

    <build>
        <plugins>
            <!-- Spring Boot Plugin -->
            <plugin>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-maven-plugin</artifactId>
            </plugin>

            <!-- Jib Plugin for Docker -->
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
                        </jvmFlags>
                        <ports>
                            <port>8081</port>
                        </ports>
                        <environment>
                            <SPRING_PROFILES_ACTIVE>prod</SPRING_PROFILES_ACTIVE>
                        </environment>
                        <labels>
                            <maintainer>PaperDMS Team</maintainer>
                        </labels>
                        <creationTime>USE_CURRENT_TIMESTAMP</creationTime>
                    </container>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## ⚙️ Variables d'Environnement dans GitHub Actions

Le workflow utilise ces variables :

```yaml
# Dans ci-cd-pipeline.yml
mvn -Pprod compile jib:build \
  -Djib.to.image=paperdms/${{ matrix.service }}:${{ steps.version.outputs.version }} \
  -Djib.to.tags=latest \
  -Djib.to.auth.username=${{ secrets.DOCKER_USERNAME }} \
  -Djib.to.auth.password=${{ secrets.DOCKER_PASSWORD }}
```

---

## 🎓 Best Practices

### 1. Utilise des Tags Semantiques

```xml
<to>
    <tags>
        <tag>latest</tag>
        <tag>${project.version}</tag>
        <tag>${git.commit.id.abbrev}</tag>
    </tags>
</to>
```

### 2. Optimise la Taille des Images

```xml
<!-- Utilise JRE au lieu de JDK -->
<from>
    <image>eclipse-temurin:17-jre-alpine</image>  <!-- Alpine = plus léger -->
</from>
```

### 3. Configure les Resources Limits

```xml
<container>
    <jvmFlags>
        <jvmFlag>-XX:MaxRAMPercentage=75.0</jvmFlag>
        <jvmFlag>-XX:+UseContainerSupport</jvmFlag>
    </jvmFlags>
</container>
```

### 4. Ajoute des Health Checks

Dans ton `application.yml` Spring Boot :
```yaml
management:
  health:
    probes:
      enabled: true
  endpoint:
    health:
      show-details: always
```

### 5. Utilise des Layers pour Cache

Jib optimise automatiquement les layers Docker pour un meilleur caching.

---

## 🐛 Troubleshooting

### Erreur : "Unauthorized" lors du push

```bash
# Vérifie les credentials
mvn jib:build -X  # Mode debug

# Ou configure dans settings.xml
~/.m2/settings.xml
```

### Erreur : "Base image pull failed"

```bash
# Vérifie que l'image de base existe
docker pull eclipse-temurin:17-jre

# Ou change l'image de base
<from>
    <image>openjdk:17-jre-slim</image>
</from>
```

### Image trop grosse

```bash
# Utilise Alpine
<from>
    <image>eclipse-temurin:17-jre-alpine</image>
</from>

# Ou
<from>
    <image>amazoncorretto:17-alpine</image>
</from>
```

---

## ✅ Checklist

- [ ] Plugin Jib ajouté dans `pom.xml` de chaque service
- [ ] Version du plugin : `3.4.0` ou plus récent
- [ ] Image de base configurée : `eclipse-temurin:17-jre`
- [ ] Ports exposés configurés correctement
- [ ] JVM flags optimisés
- [ ] Tags configurés (latest + version)
- [ ] Registry configuré (Docker Hub ou GHCR)
- [ ] Testé localement avec `mvn jib:dockerBuild`

---

## 📖 Résumé

### Setup dans chaque service

```bash
# 1. Ajoute le plugin Jib dans pom.xml
# (copie la configuration ci-dessus)

# 2. Teste localement
cd documentService
mvn compile jib:dockerBuild

# 3. Vérifie l'image
docker images | grep paperdms

# 4. Run l'image
docker run -p 8081:8081 paperdms/documentservice:latest
```

### Utilisation dans GitHub Actions

Pas de configuration supplémentaire ! Le workflow utilise automatiquement Jib si le plugin est configuré.

---

✅ **Avec Jib, la génération d'images Docker est automatique et optimisée !** 🐳
