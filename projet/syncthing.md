# ?? Guide de Configuration Syncthing - Windows ? Ubuntu

## ?? Vue d'Ensemble

Configuration de la synchronisation bidirectionnelle entre :
- **Machine Windows** : Développement avec Eclipse / VSCode
- **Machine Ubuntu** : Tests et déploiement local

## ?? Prérequis

### Sur Windows
- Syncthing installé : https://syncthing.net/downloads/
- Dossier de code : `C:\Users\YourName\workspace\paperdms`

### Sur Ubuntu
- Syncthing installé (? fait par le script)
- Dossier de sync : `~/Sync/paperdms`

---

## ?? Installation Syncthing sur Windows

### Méthode 1 : Installateur Windows (Recommandé)

1. **Télécharger**
   ```
   https://github.com/syncthing/syncthing/releases/latest
   ? SyncthingSetup-x.x.x.exe
   ```

2. **Installer**
   - Exécuter l'installateur
   - Laisser les options par défaut
   - Cocher "Start Syncthing automatically"

3. **Lancer**
   - L'interface web s'ouvre automatiquement
   - URL : http://localhost:8384

### Méthode 2 : Installation Portable

1. **Télécharger**
   ```
   https://github.com/syncthing/syncthing/releases/latest
   ? syncthing-windows-amd64-vX.X.X.zip
   ```

2. **Extraire**
   ```
   Extraire dans: C:\Program Files\Syncthing
   ```

3. **Créer un service**
   - Utiliser NSSM : https://nssm.cc/download
   ```cmd
   nssm install Syncthing "C:\Program Files\Syncthing\syncthing.exe"
   nssm start Syncthing
   ```

---

## ?? Configuration Initiale

### Sur Windows (Machine de Développement)

#### 1. Ouvrir l'Interface Web
```
http://localhost:8384
```

#### 2. Configuration de Base

**Actions ? Paramètres ? Général**
- Nom de l'appareil : `Windows-Dev`
- Cocher "Démarrer au démarrage du navigateur"

**Actions ? Paramètres ? GUI**
- Nom d'utilisateur : (optionnel)
- Mot de passe : (optionnel mais recommandé)

#### 3. Noter l'ID de l'Appareil

Dans l'interface : **Actions ? Afficher l'ID**
```
Copier l'ID (format: XXXXXXX-XXXXXXX-XXXXXXX-...)
```

### Sur Ubuntu (Machine de Test/Déploiement)

#### 1. Ouvrir l'Interface Web
```bash
# Depuis Ubuntu
xdg-open http://localhost:8384

# Ou depuis Windows (si SSH tunneling)
ssh -L 8385:localhost:8384 user@ubuntu-machine
# Puis ouvrir: http://localhost:8385
```

#### 2. Configuration de Base

**Actions ? Settings ? General**
- Device Name : `Ubuntu-Test`

#### 3. Noter l'ID de l'Appareil

**Actions ? Show ID**
```
Copier l'ID
```

---

## ?? Connexion des Appareils

### Option 1 : Connexion Automatique (LAN)

Si les deux machines sont sur le même réseau local :

1. **Sur Windows**, dans l'interface Syncthing :
   - Attendre quelques secondes
   - Un message apparaît : "Nouvel appareil Ubuntu-Test détecté"
   - Cliquer "Ajouter l'appareil"

2. **Sur Ubuntu**, faire de même quand Windows-Dev est détecté

### Option 2 : Connexion Manuelle

#### Sur Windows

1. **Actions ? Afficher l'ID**
2. **Copier l'ID de Ubuntu**
3. **Ajouter un appareil :**
   - Cliquer "+ Ajouter un appareil distant"
   - Coller l'ID de Ubuntu
   - Nom : `Ubuntu-Test`
   - Adresses : `dynamic` (ou IP si connue : `tcp://192.168.1.100:22000`)
   - Cliquer "Enregistrer"

#### Sur Ubuntu

Faire de même avec l'ID de Windows

---

## ?? Configuration du Dossier Partagé

### Sur Windows (Dossier Source)

#### 1. Créer le Dossier

```cmd
mkdir C:\Users\YourName\workspace\paperdms
```

#### 2. Ajouter le Dossier dans Syncthing

1. **Cliquer "+ Ajouter un dossier"**

2. **Onglet Général :**
   - Libellé du dossier : `PaperDMS Source`
   - ID du dossier : `paperdms-source` (auto-généré)
   - Chemin : `C:\Users\YourName\workspace\paperdms`

3. **Onglet Partage :**
   - Cocher `Ubuntu-Test`

4. **Onglet Type de dossier :**
   - Type : `Envoi et réception` (bidirectionnel)

5. **Onglet Avancé :**
   - Ignorer les fichiers : (voir section Patterns ci-dessous)
   - Watch for Changes : ? Activé
   - Délai de scan des fichiers : `60` secondes

6. **Enregistrer**

### Sur Ubuntu (Dossier Destination)

#### 1. Attendre l'Invitation

Un message apparaît : "Windows-Dev veut partager le dossier PaperDMS Source"

#### 2. Accepter et Configurer

1. **Cliquer "Ajouter"**

2. **Modifier le chemin :**
   ```
   /home/votre-user/Sync/paperdms
   ```

3. **Type de dossier :**
   - `Envoi et réception` (bidirectionnel)

4. **Enregistrer**

---

## ?? Patterns d'Ignorage

Pour éviter de synchroniser les fichiers inutiles :

### Fichier .stignore (Windows et Ubuntu)

Créer ce fichier dans le dossier synchronisé :

**Windows** : `C:\Users\YourName\workspace\paperdms\.stignore`
**Ubuntu** : `~/Sync/paperdms/.stignore`

```
# Build artifacts
**/target/
**/node_modules/
**/dist/
**/build/
**/.gradle/

# IDE files
**/.idea/
**/.vscode/
**/.settings/
**/.project
**/.classpath
**/*.iml

# OS files
.DS_Store
Thumbs.db
desktop.ini

# Logs
**/*.log
**/logs/

# Temporary files
**/*.tmp
**/*.temp
**/.cache/

# Environment files (NE PAS SYNC)
**/.env
**/.env.local

# Docker volumes data
**/docker/volumes/

# Large binary files
**/*.jar
**/*.war
**/*.ear

# Database dumps
**/*.sql
**/*.dump
```

Ou via l'interface Syncthing :

**Dossier ? Modifier ? Avancé ? Ignorer les fichiers**

---

## ?? Configuration Avancée

### Performance Optimale

#### Sur Windows

**Actions ? Paramètres ? Connexions**
- Limite de débit d'envoi : `0` (illimité en LAN)
- Limite de débit de réception : `0`
- NAT : Activé
- Relais : Désactivé (si LAN)

#### Sur Ubuntu

Même configuration

### Résolution de Conflits

**Dossier ? Modifier ? Avancé**
- En cas de conflit : `Versions simples`
- Nombre de versions : `5`

### Notifications

**Actions ? Paramètres ? GUI**
- Cocher "Afficher les notifications"

---

## ?? Vérification du Fonctionnement

### 1. Test de Synchronisation

#### Sur Windows
```cmd
cd C:\Users\YourName\workspace\paperdms
echo test > test-sync.txt
```

#### Sur Ubuntu
```bash
cd ~/Sync/paperdms
# Attendre 10-60 secondes
ls -la test-sync.txt
cat test-sync.txt
```

### 2. Vérifier le Status

Dans l'interface Syncthing :
- **Dossier doit afficher** : "Synchronisé, à jour"
- **Appareil doit afficher** : "Synchronisé"

### 3. Logs en Temps Réel

```bash
# Sur Ubuntu
journalctl --user -u syncthing -f

# Ou dans l'interface web
Actions ? Logs
```

---

## ?? Workflow de Développement

### Scénario 1 : Développement sur Windows

```
1. Coder sur Windows (Eclipse/VSCode)
2. Sauvegarder (Ctrl+S)
3. Syncthing sync automatiquement ? Ubuntu
4. Sur Ubuntu: tester/compiler/déployer
```

### Scénario 2 : Modification sur Ubuntu

```
1. Modifier un fichier sur Ubuntu (vim/nano)
2. Syncthing sync automatiquement ? Windows
3. Eclipse/VSCode détecte le changement
4. Recharger si nécessaire
```

### Workflow Complet

```
[Windows - Dev]
      ?
  Syncthing
      ?
[Ubuntu - Test]
      ?
   Scripts
      ?
 Build/Deploy
```

---

## ??? Scripts d'Intégration

### Sur Ubuntu : Auto-build après Sync

Créer un script qui surveille les changements :

```bash
#!/bin/bash
# ~/Sync/paperdms/watch-and-build.sh

inotifywait -m -r -e modify,create,delete ~/Sync/paperdms \
  --exclude '(target|node_modules|\.git)' | while read path action file; do
    
    echo "Changement détecté: $file dans $path"
    
    # Attendre que la sync soit terminée
    sleep 5
    
    # Rebuild si fichier Java
    if [[ "$file" == *.java ]]; then
        echo "Rebuild Maven..."
        cd ~/workspace/paperdms/documentService
        ./mvnw clean compile
    fi
    
    # Rebuild si fichier TypeScript
    if [[ "$file" == *.ts ]]; then
        echo "Rebuild Angular..."
        cd ~/workspace/paperdms/gateway
        npm run build
    fi
done
```

Installer inotify-tools :
```bash
sudo apt-get install inotify-tools
```

---

## ?? Dépannage

### Problème : Pas de Connexion

**Vérifier le firewall :**

Windows :
```cmd
netsh advfirewall firewall add rule name="Syncthing" dir=in action=allow protocol=TCP localport=22000
netsh advfirewall firewall add rule name="Syncthing Discovery" dir=in action=allow protocol=UDP localport=21027
```

Ubuntu :
```bash
sudo ufw allow 22000/tcp
sudo ufw allow 21027/udp
```

### Problème : Synchronisation Lente

**Vérifier l'utilisation du relais :**

Dans l'interface ? Appareil ? Adresses
- Doit afficher une adresse locale : `tcp://192.168.x.x:22000`
- Si affiche `relay://...` ? Problème de connexion directe

**Solution** : Ajouter l'IP locale manuellement
```
tcp://192.168.1.100:22000
```

### Problème : Conflits Fréquents

**Cause** : Modifications simultanées

**Solutions** :
1. Utiliser des branches Git différentes
2. Communiquer avant de modifier
3. Configurer les versions de fichiers

### Problème : Fichiers Non Synchronisés

**Vérifier les patterns d'ignorage :**
```bash
# Sur Ubuntu
cat ~/Sync/paperdms/.stignore
```

**Forcer la resynchronisation :**
```
Dossier ? Actions ? Rebalayage avancé
```

---

## ?? Accès à Distance

### Syncthing via Internet

Si les machines ne sont pas sur le même réseau :

1. **Activer les relais** (lent mais fonctionne partout)
   - Actions ? Paramètres ? Connexions
   - Relais : ? Activé

2. **Ou VPN** (recommandé)
   - WireGuard, OpenVPN, ZeroTier
   - Connexion directe après

### SSH Tunnel pour l'Interface Web

Depuis Windows, accéder à l'interface Ubuntu :

```bash
ssh -L 8385:localhost:8384 user@ubuntu-ip
```

Puis ouvrir : http://localhost:8385

---

## ?? Monitoring

### Dashboard

Interface Syncthing affiche :
- **Taux de transfert** en temps réel
- **Fichiers synchronisés** / Total
- **Dernière synchronisation**
- **Conflits** (si existants)

### Commandes Utiles

```bash
# Status du service
systemctl --user status syncthing

# Logs
journalctl --user -u syncthing -n 100

# Redémarrer
systemctl --user restart syncthing
```

---

## ? Checklist de Configuration

### Configuration Initiale
- [ ] Syncthing installé sur Windows
- [ ] Syncthing installé sur Ubuntu
- [ ] Les deux interfaces web accessibles
- [ ] IDs des appareils notés
- [ ] Appareils connectés

### Configuration du Dossier
- [ ] Dossier partagé créé sur Windows
- [ ] Dossier accepté sur Ubuntu
- [ ] Type : Envoi et réception (bidirectionnel)
- [ ] .stignore configuré
- [ ] Test de sync réussi

### Optimisations
- [ ] Watch for changes activé
- [ ] Firewall configuré
- [ ] Connexion directe (pas de relais)
- [ ] Scripts d'auto-build configurés

---

## ?? Résumé

```
[Windows Eclipse/VSCode]
         ? (save)
    [Syncthing]
         ? (sync 10-60s)
  [Ubuntu ~/Sync]
         ? (inotify)
   [Auto-build]
         ?
 [Tests/Déploiement]
```

**Synchronisation bidirectionnelle automatique entre Windows et Ubuntu ! ??**

---

## ?? Ressources

- Documentation Syncthing : https://docs.syncthing.net/
- Forum : https://forum.syncthing.net/
- GitHub : https://github.com/syncthing/syncthing

---

**Configuration complète pour un workflow de développement fluide ! ??**