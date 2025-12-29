# Fix HikariCP "Pool Configuration is Sealed" Error

## 🐛 Erreur

```
Failed to bind properties under 'spring.datasource.hikari'
Property: spring.datasource.hikari.auto-commit
Reason: java.lang.IllegalStateException: 
The configuration of the pool is sealed once started. 
Use HikariConfigMXBean for runtime changes.
```

---

## 🎯 Cause

**Problèmes dans ta configuration** :

### 1. Double Définition de poolName

```yaml
hikari:
  poolName: Hikari              # ❌ Défini ici
  pool-name: ${spring.application.name}-pool  # ❌ Et ici aussi !
```

### 2. auto-commit: false

```yaml
hikari:
  auto-commit: false  # ❌ Propriété problématique
```

`auto-commit: false` ne peut pas être modifié après le démarrage du pool et pose des problèmes avec Liquibase.

---

## ✅ Configuration Corrigée

### application.yml (Version Correcte)

```yaml
spring:
  application:
    name: aiService
  
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://localhost:5432/aiService
    username: aiService
    password: ${DB_PASSWORD:}
    hikari:
      # ⭐ UN SEUL pool-name
      pool-name: ${spring.application.name}-pool
      
      # ⭐ SUPPRIME auto-commit (laisse la valeur par défaut = true)
      # auto-commit: false  # ← ENLÈVE CETTE LIGNE
      
      # Pool de connexions
      maximum-pool-size: 5
      minimum-idle: 2
      
      # Timeouts
      connection-timeout: 30000      # 30 secondes
      idle-timeout: 600000           # 10 minutes
      max-lifetime: 1800000          # 30 minutes
      
      # Optimisations
      leak-detection-threshold: 60000
      connection-test-query: SELECT 1
      validation-timeout: 5000
```

---

## 📝 Configuration Production vs Développement

### application.yml (Base - Tous les Environnements)

```yaml
spring:
  application:
    name: aiService
  
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    hikari:
      pool-name: ${spring.application.name}-pool
      maximum-pool-size: 5
      minimum-idle: 2
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      leak-detection-threshold: 60000
```

### application-dev.yml (Développement)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/aiService
    username: aiService
    password: aiService
    hikari:
      # Pool plus petit en dev
      maximum-pool-size: 3
      minimum-idle: 1
      # Logs plus verbeux
      leak-detection-threshold: 30000
  
  jpa:
    show-sql: true
    properties:
      hibernate:
        format_sql: true
  
  liquibase:
    enabled: true
    drop-first: false
```

### application-prod.yml (Production)

```yaml
spring:
  datasource:
    url: jdbc:postgresql://postgres:5432/paperdms
    username: ${DB_USERNAME:paperdms}
    password: ${DB_PASSWORD:paperdms}
    hikari:
      # Pool production
      maximum-pool-size: 5
      minimum-idle: 2
      # Timeouts plus stricts
      connection-timeout: 20000
      validation-timeout: 3000
  
  jpa:
    show-sql: false
    properties:
      hibernate:
        format_sql: false
  
  liquibase:
    enabled: true
    drop-first: false
```

---

## 🔧 Configuration Complète Recommandée

### Pour TOUS les 16 Services

```yaml
spring:
  application:
    name: ${SERVICE_NAME}  # Ex: aiService, documentService, etc.
  
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:paperdms}
    username: ${DB_USERNAME:paperdms}
    password: ${DB_PASSWORD:paperdms}
    
    hikari:
      # Identité du pool
      pool-name: ${spring.application.name}-pool
      
      # Taille du pool (ajusté pour 16 services)
      maximum-pool-size: 5
      minimum-idle: 2
      
      # Timeouts
      connection-timeout: 30000        # 30 sec pour obtenir une connexion
      idle-timeout: 600000             # 10 min avant de fermer une connexion idle
      max-lifetime: 1800000            # 30 min durée de vie max d'une connexion
      validation-timeout: 5000         # 5 sec pour valider une connexion
      
      # Health check
      connection-test-query: SELECT 1
      
      # Détection de fuites
      leak-detection-threshold: 60000  # 60 sec pour détecter une fuite
      
      # Optimisations
      initialization-fail-timeout: -1  # Ne pas échouer si DB pas prête au démarrage
      
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
    open-in-view: false
    properties:
      hibernate:
        jdbc:
          time_zone: UTC
        id:
          new_generator_mappings: true
        connection:
          provider_disables_autocommit: false  # ⭐ Importante pour Liquibase
        cache:
          use_second_level_cache: true
          use_query_cache: false
          region:
            factory_class: jcache
  
  liquibase:
    enabled: true
    change-log: classpath:config/liquibase/master.xml
    # Tables par service (évite les locks entre services)
    database-change-log-table: DATABASECHANGELOG_${spring.application.name}
    database-change-log-lock-table: DATABASECHANGELOGLOCK_${spring.application.name}
    default-schema: public
    liquibase-schema: public
    drop-first: false
```

---

## 🚫 Propriétés à ÉVITER

### ❌ NE PAS UTILISER

```yaml
hikari:
  # ❌ ÉVITER - Cause des problèmes avec Liquibase
  auto-commit: false
  
  # ❌ ÉVITER - Double définition
  poolName: Hikari
  pool-name: my-pool  # Utilise pool-name OU poolName, pas les deux
  
  # ❌ ÉVITER - Trop restrictif
  maximum-pool-size: 1
  
  # ❌ ÉVITER - Trop long
  connection-timeout: 300000  # 5 minutes est trop long
```

---

## 🔍 Pourquoi auto-commit: false Pose Problème

### Le Problème

```yaml
hikari:
  auto-commit: false  # ❌ Problématique
```

**Raisons** :

1. **Liquibase** a besoin de `auto-commit: true` pour ses migrations
2. HikariCP ne permet pas de changer cette propriété après le démarrage
3. Spring essaie de reconfigurer le pool après son initialisation → ERREUR

### La Solution

```yaml
hikari:
  # ⭐ NE PAS définir auto-commit
  # Laisse la valeur par défaut (true)
```

Si tu as vraiment besoin de transactions manuelles, gère-les au niveau **@Transactional** :

```java
@Service
public class MyService {
    
    @Transactional  // ⭐ Gestion de transaction Spring
    public void myMethod() {
        // auto-commit géré par Spring, pas par HikariCP
    }
}
```

---

## 🐛 Autres Erreurs Courantes

### Erreur 1 : poolName vs pool-name

```yaml
# ❌ MAUVAIS - Double définition
hikari:
  poolName: Hikari
  pool-name: my-pool

# ✅ BON - Une seule définition
hikari:
  pool-name: ${spring.application.name}-pool
```

### Erreur 2 : Valeurs Invalides

```yaml
# ❌ MAUVAIS
hikari:
  maximum-pool-size: 0  # Doit être > 0
  minimum-idle: 10
  maximum-pool-size: 5  # minimum-idle > maximum-pool-size !

# ✅ BON
hikari:
  maximum-pool-size: 5
  minimum-idle: 2  # minimum-idle < maximum-pool-size
```

### Erreur 3 : Propriétés qui N'Existent Pas

```yaml
# ❌ MAUVAIS - Propriétés inventées
hikari:
  max-connections: 10  # N'existe pas, utilise maximum-pool-size
  min-connections: 2   # N'existe pas, utilise minimum-idle

# ✅ BON
hikari:
  maximum-pool-size: 10
  minimum-idle: 2
```

---

## 📊 Configuration par Type de Service

### Services Légers (notification, email)

```yaml
hikari:
  maximum-pool-size: 3
  minimum-idle: 1
```

### Services Standard (document, search, workflow)

```yaml
hikari:
  maximum-pool-size: 5
  minimum-idle: 2
```

### Services Lourds (OCR, AI, similarity)

```yaml
hikari:
  maximum-pool-size: 8
  minimum-idle: 3
```

### Gateway

```yaml
hikari:
  maximum-pool-size: 10
  minimum-idle: 5
```

---

## ✅ Validation de la Configuration

### Test 1 : Vérifier la Syntaxe

```bash
# Teste que le service démarre
./mvnw spring-boot:run

# Ou avec Docker
docker-compose up aiservice
```

### Test 2 : Vérifier les Connexions

```bash
# Logs du service
docker logs paperdms-aiservice | grep -i hikari

# Devrait afficher:
# HikariPool-1 - Starting...
# HikariPool-1 - Added connection ...
# HikariPool-1 - Start completed.
```

### Test 3 : Vérifier dans PostgreSQL

```sql
-- Connexions pour aiService
SELECT application_name, count(*) 
FROM pg_stat_activity 
WHERE application_name LIKE '%aiService%'
GROUP BY application_name;

-- Devrait afficher: 2-5 connexions
```

---

## 🔧 Template Application.yml Final

### application.yml (Copie pour Chaque Service)

```yaml
spring:
  application:
    name: SERVICE_NAME_HERE  # ⭐ Change pour chaque service
  
  profiles:
    active: ${SPRING_PROFILES_ACTIVE:dev}
  
  datasource:
    type: com.zaxxer.hikari.HikariDataSource
    url: jdbc:postgresql://${DB_HOST:localhost}:${DB_PORT:5432}/${DB_NAME:paperdms}
    username: ${DB_USERNAME:paperdms}
    password: ${DB_PASSWORD:paperdms}
    
    hikari:
      pool-name: ${spring.application.name}-pool
      maximum-pool-size: 5
      minimum-idle: 2
      connection-timeout: 30000
      idle-timeout: 600000
      max-lifetime: 1800000
      validation-timeout: 5000
      connection-test-query: SELECT 1
      leak-detection-threshold: 60000
  
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
    show-sql: false
    open-in-view: false
    properties:
      hibernate:
        jdbc:
          time_zone: UTC
        connection:
          provider_disables_autocommit: false
  
  liquibase:
    enabled: true
    change-log: classpath:config/liquibase/master.xml
    database-change-log-table: DATABASECHANGELOG_${spring.application.name}
    database-change-log-lock-table: DATABASECHANGELOGLOCK_${spring.application.name}

server:
  port: ${SERVER_PORT:8081}  # Change pour chaque service
```

---

## ✅ Checklist de Correction

Pour **chaque service** :

- [ ] Supprime `auto-commit: false`
- [ ] Garde seulement `pool-name` (supprime `poolName`)
- [ ] Vérifie `maximum-pool-size: 5`
- [ ] Vérifie `minimum-idle: 2`
- [ ] Ajoute les tables Liquibase par service
- [ ] Teste le démarrage
- [ ] Vérifie les logs HikariCP
- [ ] Vérifie les connexions PostgreSQL

---

## ✅ Résumé

### Problème

```yaml
hikari:
  poolName: Hikari           # ❌ Doublon
  pool-name: ...             # ❌ Doublon
  auto-commit: false         # ❌ Cause l'erreur
```

### Solution

```yaml
hikari:
  pool-name: ${spring.application.name}-pool  # ✅ Un seul
  # auto-commit supprimé                      # ✅ Valeur par défaut
  maximum-pool-size: 5
  minimum-idle: 2
```

### Résultat

```
✅ Service démarre sans erreur
✅ HikariCP configuré correctement
✅ Liquibase fonctionne
✅ Connexions optimisées
```

---

✅ **Supprime auto-commit et le doublon poolName, l'erreur disparaîtra !** 🎉
