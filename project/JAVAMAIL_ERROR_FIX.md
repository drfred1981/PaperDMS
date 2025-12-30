# Résolution Erreur JavaMailSender - Gateway

## ❌ Erreur

```
Parameter 1 of constructor in fr.smartprod.paperdms.gateway.service.MailService 
required a bean of type 'org.springframework.mail.javamail.JavaMailSender' 
that could not be found
```

## 🔍 Cause

Spring Boot a besoin de la configuration SMTP pour créer le bean `JavaMailSender`. Sans cette configuration, le bean n'est pas créé automatiquement, même si la dépendance `spring-boot-starter-mail` est présente.

## ✅ Solutions (3 options)

---

## Solution 1 : Ajouter la Configuration Mail (Recommandé)

### Dans application.yml

Ajoute cette configuration dans `gateway/src/main/resources/config/application.yml` :

```yaml
spring:
  mail:
    host: localhost
    port: 25
    username:
    password:
    protocol: smtp
    tls: false
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false
    from: noreply@paperdms.local
    base-url: http://localhost:8080
```

### Dans application-prod.yml (avec variables d'environnement)

Pour la production, utilise des variables d'environnement :

```yaml
spring:
  mail:
    host: ${SPRING_MAIL_HOST:localhost}
    port: ${SPRING_MAIL_PORT:587}
    username: ${SPRING_MAIL_USERNAME:}
    password: ${SPRING_MAIL_PASSWORD:}
    protocol: smtp
    tls: true
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
          ssl:
            trust: ${SPRING_MAIL_HOST:localhost}
    from: ${SPRING_MAIL_FROM:noreply@paperdms.com}
    base-url: ${SPRING_MAIL_BASE_URL:https://paperdms.com}

jhipster:
  mail:
    from: ${SPRING_MAIL_FROM:noreply@paperdms.com}
    base-url: ${SPRING_MAIL_BASE_URL:https://paperdms.com}
```

### Variables d'Environnement (.env)

Ajoute dans ton fichier `.env` :

```bash
# Mail Configuration (SMTP)
SPRING_MAIL_HOST=smtp.gmail.com
SPRING_MAIL_PORT=587
SPRING_MAIL_USERNAME=your-email@gmail.com
SPRING_MAIL_PASSWORD=your-app-password
SPRING_MAIL_FROM=noreply@paperdms.com
SPRING_MAIL_BASE_URL=http://localhost:8080
```

### Docker Compose

Dans `docker-compose.apps.yml`, ajoute pour le gateway :

```yaml
services:
  gateway:
    environment:
      # Mail Configuration
      - SPRING_MAIL_HOST=${SPRING_MAIL_HOST:-localhost}
      - SPRING_MAIL_PORT=${SPRING_MAIL_PORT:-25}
      - SPRING_MAIL_USERNAME=${SPRING_MAIL_USERNAME:-}
      - SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD:-}
      - SPRING_MAIL_FROM=${SPRING_MAIL_FROM:-noreply@paperdms.local}
```

---

## Solution 2 : Utiliser MailDev (Serveur Mail de Dev)

MailDev est un serveur SMTP local parfait pour le développement.

### docker-compose.yml

Ajoute ce service :

```yaml
services:
  # ... autres services ...

  maildev:
    image: maildev/maildev
    container_name: paperdms-maildev
    ports:
      - "1080:1080"  # Interface web
      - "1025:1025"  # SMTP server
    networks:
      - paperdms-network
```

### Configuration Gateway

```yaml
spring:
  mail:
    host: maildev
    port: 1025
    username:
    password:
    protocol: smtp
    tls: false
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false
```

### Accès

- Interface web : http://localhost:1080
- Tous les emails envoyés s'affichent dans l'interface

---

## Solution 3 : Désactiver le MailService (Temporaire)

Si tu n'as pas besoin des emails pour l'instant, tu peux désactiver `MailService`.

### Option A : Exclure l'Auto-Configuration

Dans `gateway/src/main/resources/config/application.yml` :

```yaml
spring:
  autoconfigure:
    exclude:
      - org.springframework.boot.autoconfigure.mail.MailSenderAutoConfiguration
```

### Option B : Rendre MailService Optionnel

Si tu as accès au code de `MailService`, modifie le constructeur :

```java
@Service
public class MailService {
    
    private final JavaMailSender javaMailSender;
    
    // Rend le paramètre optionnel
    public MailService(@Autowired(required = false) JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }
    
    public void sendEmail(...) {
        if (javaMailSender == null) {
            log.warn("Mail not configured, skipping email");
            return;
        }
        // ... envoi email
    }
}
```

### Option C : Configuration Conditionnelle

Crée un bean conditionnel :

```java
@Configuration
public class MailConfiguration {
    
    @Bean
    @ConditionalOnMissingBean
    public JavaMailSender javaMailSender() {
        // Retourne un mock ou une implémentation vide
        return new JavaMailSenderImpl();
    }
}
```

---

## 🎯 Solution Recommandée (Développement)

### Étape 1 : Ajoute MailDev au docker-compose.yml

```yaml
  maildev:
    image: maildev/maildev
    container_name: paperdms-maildev
    ports:
      - "1080:1080"
      - "1025:1025"
    networks:
      - paperdms-network
```

### Étape 2 : Configure application.yml

```yaml
spring:
  mail:
    host: localhost  # ou maildev si gateway dans Docker
    port: 1025
    username:
    password:
    protocol: smtp
    tls: false
    properties:
      mail:
        smtp:
          auth: false
          starttls:
            enable: false
    from: noreply@paperdms.local
    base-url: http://localhost:8080
```

### Étape 3 : Redémarre

```bash
# Infrastructure avec MailDev
docker-compose up -d

# Gateway
cd gateway
./mvnw spring-boot:run
```

### Étape 4 : Vérifie les Emails

Ouvre http://localhost:1080 pour voir tous les emails envoyés.

---

## 📧 Configuration Mail Providers

### Gmail

```yaml
spring:
  mail:
    host: smtp.gmail.com
    port: 587
    username: your-email@gmail.com
    password: your-app-password  # Pas le mot de passe Gmail !
    protocol: smtp
    tls: true
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
            required: true
```

**Note** : Génère un "App Password" dans Gmail, pas le mot de passe principal.

### SendGrid

```yaml
spring:
  mail:
    host: smtp.sendgrid.net
    port: 587
    username: apikey
    password: your-sendgrid-api-key
    protocol: smtp
    tls: true
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### Mailgun

```yaml
spring:
  mail:
    host: smtp.mailgun.org
    port: 587
    username: postmaster@your-domain.mailgun.org
    password: your-mailgun-password
    protocol: smtp
    tls: true
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

### AWS SES

```yaml
spring:
  mail:
    host: email-smtp.eu-west-1.amazonaws.com
    port: 587
    username: your-ses-smtp-username
    password: your-ses-smtp-password
    protocol: smtp
    tls: true
    properties:
      mail:
        smtp:
          auth: true
          starttls:
            enable: true
```

---

## 🐳 Configuration Docker Complète

### docker-compose.yml (Infrastructure)

```yaml
version: '3.8'

services:
  # ... services existants ...

  # Serveur Mail de développement
  maildev:
    image: maildev/maildev
    container_name: paperdms-maildev
    ports:
      - "1080:1080"  # Web UI
      - "1025:1025"  # SMTP
    networks:
      - paperdms-network
    healthcheck:
      test: ["CMD", "wget", "-q", "--spider", "http://localhost:1080"]
      interval: 10s
      timeout: 5s
      retries: 3
```

### docker-compose.apps.yml (Gateway)

```yaml
services:
  gateway:
    image: paperdms/gateway:latest
    container_name: paperdms-gateway
    environment:
      # ... autres variables ...
      
      # Mail Configuration
      - SPRING_MAIL_HOST=${SPRING_MAIL_HOST:-maildev}
      - SPRING_MAIL_PORT=${SPRING_MAIL_PORT:-1025}
      - SPRING_MAIL_USERNAME=${SPRING_MAIL_USERNAME:-}
      - SPRING_MAIL_PASSWORD=${SPRING_MAIL_PASSWORD:-}
      - SPRING_MAIL_FROM=${SPRING_MAIL_FROM:-noreply@paperdms.local}
      - SPRING_MAIL_BASE_URL=${SPRING_MAIL_BASE_URL:-http://localhost:8080}
    depends_on:
      - maildev
```

### .env

```bash
# Mail Configuration (Development - MailDev)
SPRING_MAIL_HOST=maildev
SPRING_MAIL_PORT=1025
SPRING_MAIL_USERNAME=
SPRING_MAIL_PASSWORD=
SPRING_MAIL_FROM=noreply@paperdms.local
SPRING_MAIL_BASE_URL=http://localhost:8080

# Production - Gmail
# SPRING_MAIL_HOST=smtp.gmail.com
# SPRING_MAIL_PORT=587
# SPRING_MAIL_USERNAME=your-email@gmail.com
# SPRING_MAIL_PASSWORD=your-app-password
# SPRING_MAIL_FROM=noreply@paperdms.com
# SPRING_MAIL_BASE_URL=https://paperdms.com
```

---

## 🔍 Vérification

### Test de la Configuration Mail

```bash
# Logs du gateway
docker logs paperdms-gateway | grep mail

# Ou en local
./mvnw spring-boot:run | grep mail
```

### Envoyer un Email de Test

Via Spring Boot Actuator (si activé) :

```bash
curl -X POST http://localhost:8080/api/account/reset-password/init \
  -H "Content-Type: text/plain" \
  -d "test@example.com"
```

Vérifie dans MailDev : http://localhost:1080

---

## ❌ Erreurs Courantes

### Erreur 1 : Connection Refused

```
Failed to connect to SMTP host: localhost, port: 25
```

**Solution** : Vérifie que le serveur SMTP est démarré
```bash
docker ps | grep maildev
```

### Erreur 2 : Authentication Failed (Gmail)

```
535-5.7.8 Username and Password not accepted
```

**Solution** : Utilise un "App Password", pas ton mot de passe Gmail
- https://myaccount.google.com/apppasswords

### Erreur 3 : Port Déjà Utilisé

```
Bind for 0.0.0.0:1025 failed: port is already allocated
```

**Solution** : Change le port dans docker-compose.yml
```yaml
ports:
  - "1026:1025"
```

---

## 📚 Fichiers à Créer/Modifier

### 1. gateway/src/main/resources/config/application.yml

```yaml
spring:
  mail:
    host: localhost
    port: 1025
    from: noreply@paperdms.local
    base-url: http://localhost:8080
```

### 2. gateway/src/main/resources/config/application-prod.yml

```yaml
spring:
  mail:
    host: ${SPRING_MAIL_HOST:localhost}
    port: ${SPRING_MAIL_PORT:587}
    username: ${SPRING_MAIL_USERNAME:}
    password: ${SPRING_MAIL_PASSWORD:}
    from: ${SPRING_MAIL_FROM:noreply@paperdms.com}
    base-url: ${SPRING_MAIL_BASE_URL:https://paperdms.com}
```

### 3. docker-compose.yml

```yaml
services:
  maildev:
    image: maildev/maildev
    ports:
      - "1080:1080"
      - "1025:1025"
```

### 4. .env

```bash
SPRING_MAIL_HOST=maildev
SPRING_MAIL_PORT=1025
SPRING_MAIL_FROM=noreply@paperdms.local
```

---

## ✅ Solution Rapide (1 minute)

```bash
# 1. Ajoute MailDev au docker-compose.yml
cat >> docker-compose.yml << 'EOF'

  maildev:
    image: maildev/maildev
    container_name: paperdms-maildev
    ports:
      - "1080:1080"
      - "1025:1025"
    networks:
      - paperdms-network
EOF

# 2. Démarre MailDev
docker-compose up -d maildev

# 3. Ajoute la config mail dans application.yml
cat >> gateway/src/main/resources/config/application.yml << 'EOF'
spring:
  mail:
    host: localhost
    port: 1025
    from: noreply@paperdms.local
    base-url: http://localhost:8080
EOF

# 4. Redémarre le gateway
cd gateway
./mvnw spring-boot:run
```

---

## 🎓 Best Practices

### Développement

- ✅ Utilise MailDev (simple, visuel, aucune config)
- ✅ Configure `host: localhost` et `port: 1025`
- ✅ Pas d'authentification requise

### Production

- ✅ Utilise un service SMTP professionnel (Gmail, SendGrid, SES)
- ✅ Stocke les credentials dans des variables d'environnement
- ✅ Active TLS/SSL
- ✅ Utilise des secrets managers (Vault, AWS Secrets Manager)

### Sécurité

- ❌ Ne jamais commiter les mots de passe SMTP
- ❌ Ne jamais utiliser le mot de passe principal Gmail
- ✅ Toujours utiliser des App Passwords ou API Keys
- ✅ Ajoute `.env` au `.gitignore`

---

## 📖 Résumé

### L'Erreur

Spring Boot ne peut pas créer `JavaMailSender` sans configuration SMTP.

### La Solution la Plus Simple

1. Ajoute MailDev à docker-compose.yml
2. Configure `spring.mail` dans application.yml
3. Redémarre le gateway

### Pour la Production

1. Configure un vrai service SMTP (Gmail, SendGrid, etc.)
2. Utilise des variables d'environnement
3. Active TLS et l'authentification

---

✅ **Avec MailDev, tu as un serveur mail complet en 1 minute !** 🚀
