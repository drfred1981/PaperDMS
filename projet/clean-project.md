# ?? Guide des Scripts de Nettoyage

## ?? Deux Scripts Disponibles

### 1. `clean-projects.sh` - Simple et Interactif

Script simple avec confirmation interactive.

**Utilisation:**
```bash
./clean-projects.sh [chemin]
```

**Exemple:**
```bash
# Nettoyer le repertoire courant
./clean-projects.sh

# Nettoyer un repertoire specifique
./clean-projects.sh ~/workspace/paperdms
```

### 2. `clean-projects-advanced.sh` - Avance avec Options

Script complet avec multiples options et modes.

**Utilisation:**
```bash
./clean-projects-advanced.sh [options] [chemin]
```

---

## ?? Repertoires et Fichiers Nettoyes

| Type | Exemples | Taille Typique |
|------|----------|----------------|
| **Maven** | `target/` | 50-500 MB |
| **Node.js** | `node_modules/`, `dist/` | 100-800 MB |
| **Gradle** | `build/`, `.gradle/` | 50-300 MB |
| **Cache** | `.cache/` | 10-100 MB |
| **Logs** | `*.log` | 1-50 MB |
| **Temp** | `*.tmp`, `*~` | <1 MB |

---

## ?? Exemples d'Utilisation

### Script Simple

#### Nettoyage Basique
```bash
# Dans le repertoire courant
./clean-projects.sh

# Sortie:
# ==========================================
# Analyse...
# ==========================================
#
#   target        125.5 MB - /home/user/workspace/documentService/target
#   node_modules  450.2 MB - /home/user/workspace/gateway/node_modules
#   dist          15.8 MB - /home/user/workspace/gateway/dist
#
# [i] Nombre total de repertoires trouves: 3
# [i] Espace disque a liberer: 591.5 MB
#
# Voulez-vous supprimer ces repertoires ? (y/n): y
```

#### Nettoyage d'un Projet Specifique
```bash
./clean-projects.sh ~/workspace/paperdms
```

### Script Avance

#### 1. Simulation (Dry Run)
```bash
# Voir ce qui serait supprime sans rien supprimer
./clean-projects-advanced.sh --dry-run ~/workspace

# Sortie:
# ==========================================
# Mode Simulation (Dry Run)
# ==========================================
# [!] Aucun fichier ne sera supprime
#
#   [SIMULATION] Supprimerait: target (125.5 MB)
#   [SIMULATION] Supprimerait: node_modules (450.2 MB)
```

#### 2. Mode Automatique (Sans Confirmation)
```bash
# Pour scripts automatises
./clean-projects-advanced.sh --auto ~/workspace/paperdms
```

#### 3. Nettoyer Uniquement Maven
```bash
# Nettoie seulement les repertoires target/
./clean-projects-advanced.sh --maven ~/workspace
```

#### 4. Nettoyer Uniquement Node.js
```bash
# Nettoie seulement node_modules/ et dist/
./clean-projects-advanced.sh --node ~/workspace
```

#### 5. Nettoyer Tout (Maven + Node + Gradle + Logs)
```bash
./clean-projects-advanced.sh --all ~/workspace
```

#### 6. Mode Verbeux
```bash
# Affiche plus de details pendant le nettoyage
./clean-projects-advanced.sh --verbose --all ~/workspace
```

#### 7. Combinaisons
```bash
# Simulation + Verbeux + Tout
./clean-projects-advanced.sh --dry-run --verbose --all ~/workspace

# Auto + Node.js + Logs
./clean-projects-advanced.sh --auto --node --logs ~/workspace

# Maven + Logs
./clean-projects-advanced.sh --maven --logs ~/workspace
```

---

## ?? Integration dans l'Environnement

### Alias Bash

Ajoutez dans `~/.bashrc` :

```bash
# Alias de nettoyage simple
alias clean='~/scripts/clean-projects.sh'
alias clean-here='~/scripts/clean-projects.sh .'
alias clean-workspace='~/scripts/clean-projects.sh ~/workspace/paperdms'

# Alias de nettoyage avance
alias clean-dry='~/scripts/clean-projects-advanced.sh --dry-run'
alias clean-auto='~/scripts/clean-projects-advanced.sh --auto'
alias clean-maven='~/scripts/clean-projects-advanced.sh --maven'
alias clean-node='~/scripts/clean-projects-advanced.sh --node'
alias clean-all='~/scripts/clean-projects-advanced.sh --all'
```

Puis recharger :
```bash
source ~/.bashrc
```

### Script Git Pre-Commit

Pour nettoyer automatiquement avant chaque commit :

**Fichier : `.git/hooks/pre-commit`**

```bash
#!/bin/bash
echo "Nettoyage avant commit..."
~/scripts/clean-projects-advanced.sh --auto --all .
```

```bash
chmod +x .git/hooks/pre-commit
```

### Cron Job Hebdomadaire

Nettoyer automatiquement chaque dimanche a minuit :

```bash
# Editer la crontab
crontab -e

# Ajouter cette ligne
0 0 * * 0 ~/scripts/clean-projects-advanced.sh --auto --all ~/workspace/paperdms
```

### Script de Build

Integrer le nettoyage dans vos scripts de build :

```bash
#!/bin/bash
# build-all.sh

echo "Nettoyage avant build..."
~/scripts/clean-projects-advanced.sh --auto ~/workspace/paperdms

echo "Build Maven..."
cd ~/workspace/paperdms/documentService
./mvnw clean install

echo "Build Angular..."
cd ~/workspace/paperdms/gateway
npm run build
```

---

## ?? Comparaison des Scripts

| Fonctionnalite | Simple | Avance |
|----------------|--------|--------|
| Confirmation interactive | ? | ? (sauf --auto) |
| Mode simulation | ? | ? (--dry-run) |
| Selection par type | ? | ? (--maven, --node, etc.) |
| Mode automatique | ? | ? (--auto) |
| Mode verbeux | ? | ? (--verbose) |
| Nettoyage logs optionnel | ? | ? (--logs) |
| Affichage detaille | ? | ? |
| Suggestions | ? | ? |

---

## ?? Cas d'Usage

### Developpement Quotidien

```bash
# Avant de commencer a coder
clean-dry ~/workspace  # Voir ce qui peut être nettoye

# Apres plusieurs builds
clean-here  # Nettoyer le projet courant
```

### Avant un Commit

```bash
# Nettoyer automatiquement
./clean-projects-advanced.sh --auto --all .
git add .
git commit -m "Feature X"
```

### Liberer de l'Espace Disque

```bash
# Simulation pour voir l'espace recuperable
./clean-projects-advanced.sh --dry-run --all ~/workspace

# Sortie:
# ?? Espace a liberer: 2.5 GB

# Nettoyer effectivement
./clean-projects-advanced.sh --auto --all ~/workspace
```

### Avant Syncthing

```bash
# Nettoyer avant de synchroniser avec Windows
./clean-projects-advanced.sh --auto --all ~/Sync/paperdms
```

### CI/CD

```bash
# Dans votre pipeline
./clean-projects-advanced.sh --auto --all $WORKSPACE
./mvnw clean install
```

---

## ?? Attention

### ?? Fichiers Non Recuperables

Les fichiers supprimes ne vont **pas dans la corbeille**. Ils sont supprimes definitivement.

**Recommandation** : Utilisez `--dry-run` avant le premier nettoyage.

### ?? Fichiers Preserves

Ces fichiers ne sont **jamais** supprimes :
- Code source (`.java`, `.ts`, `.js`, etc.)
- Fichiers de configuration (`.yml`, `.properties`, etc.)
- Fichiers Git (`.git/`)
- Fichiers d'environnement (`.env`)

### ?? Repertoires Exclus

Les scripts excluent automatiquement :
- `.git/`
- Repertoires caches (sauf `.gradle/`, `.cache/`)
- `node_modules/` dans les sous-dossiers de `node_modules/`

---

## ?? Depannage

### Probleme : Permission Refusee

```bash
chmod +x clean-projects.sh
chmod +x clean-projects-advanced.sh
```

### Probleme : bc: command not found

```bash
sudo apt-get install bc
```

### Probleme : Script Trop Lent

```bash
# Limiter la profondeur de recherche
find ~/workspace -maxdepth 3 -type d -name "target"
```

### Probleme : Certains Repertoires Ne Sont Pas Nettoyes

Verifier que le script a les permissions :
```bash
ls -la clean-projects-advanced.sh
# Doit afficher: -rwxr-xr-x
```

---

## ?? Statistiques Typiques

### Projet Full-Stack (Maven + Angular)

**Avant nettoyage :**
```
documentService/target/           : 150 MB
gateway/node_modules/             : 600 MB
gateway/dist/                     : 20 MB
*.log                             : 5 MB
-------------------------------------------
Total                             : 775 MB
```

**Apres nettoyage :**
```
Espace libere : 775 MB
Temps         : 10-30 secondes
```

### Workspace Complet (5 Services)

**Avant nettoyage :**
```
5 x target/                       : 750 MB
3 x node_modules/                 : 1.8 GB
3 x dist/                         : 60 MB
2 x .gradle/                      : 200 MB
Logs et temp                      : 50 MB
-------------------------------------------
Total                             : 2.86 GB
```

**Apres nettoyage :**
```
Espace libere : 2.86 GB
Temps         : 1-2 minutes
```

---

## ? Checklist de Nettoyage

Avant de synchroniser ou avant un commit important :

- [ ] Simulation effectuee (`--dry-run`)
- [ ] Verifier qu'aucun build n'est en cours
- [ ] Sauvegarder les logs importants si necessaire
- [ ] Lancer le nettoyage
- [ ] Verifier l'espace disque libere
- [ ] Rebuild pour verifier que tout fonctionne

---

## ?? Bonnes Pratiques

### Quotidien

```bash
# En fin de journee
clean-here
```

### Hebdomadaire

```bash
# Le dimanche (automatique via cron)
clean-all ~/workspace
```

### Avant Commits Importants

```bash
clean-auto .
git status
git add .
git commit -m "..."
```

### Avant Syncthing

```bash
# Eviter de synchroniser les fichiers de build
clean-auto ~/Sync/paperdms
```

---

**Scripts de nettoyage pour garder vos projets propres et legers ! ??**