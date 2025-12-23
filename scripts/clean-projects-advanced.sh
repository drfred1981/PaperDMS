#!/bin/bash

################################################################################
# Script de Nettoyage Avancé des Projets
################################################################################
#
# Nettoie les fichiers et répertoires temporaires avec options avancées
#
# Usage: ./clean-projects-advanced.sh [options] [chemin]
#
# Options:
#   -a, --auto          Mode automatique (pas de confirmation)
#   -d, --dry-run       Simulation (affiche sans supprimer)
#   -v, --verbose       Mode verbeux
#   -m, --maven         Nettoyer uniquement les projets Maven
#   -n, --node          Nettoyer uniquement les projets Node.js
#   -g, --gradle        Nettoyer uniquement les projets Gradle
#   -l, --logs          Inclure les fichiers .log
#   --all               Tout nettoyer (Maven + Node + Gradle + logs)
#   -h, --help          Afficher l'aide
#
################################################################################

# Couleurs
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m'

# Options par défaut
AUTO_MODE=false
DRY_RUN=false
VERBOSE=false
CLEAN_MAVEN=true
CLEAN_NODE=true
CLEAN_GRADLE=true
CLEAN_LOGS=false
START_DIR="."

################################################################################
# Fonctions
################################################################################

show_help() {
    cat << EOF
${BLUE}Nettoyage Avancé des Projets${NC}

Usage: $0 [options] [chemin]

${CYAN}Options:${NC}
  -a, --auto          Mode automatique (pas de confirmation)
  -d, --dry-run       Simulation (affiche sans supprimer)
  -v, --verbose       Mode verbeux
  -m, --maven         Nettoyer uniquement les projets Maven (target/)
  -n, --node          Nettoyer uniquement les projets Node.js (node_modules/)
  -g, --gradle        Nettoyer uniquement les projets Gradle (build/, .gradle/)
  -l, --logs          Inclure les fichiers .log dans le nettoyage
  --all               Tout nettoyer (Maven + Node + Gradle + logs)
  -h, --help          Afficher cette aide

${CYAN}Exemples:${NC}
  $0                              # Nettoyer le répertoire courant
  $0 ~/workspace/paperdms         # Nettoyer un répertoire spécifique
  $0 --maven ~/projects           # Nettoyer uniquement Maven dans ~/projects
  $0 --node --logs .              # Nettoyer Node.js et logs dans le répertoire courant
  $0 --dry-run ~/workspace        # Simuler le nettoyage (sans supprimer)
  $0 --auto --all ~/workspace     # Tout nettoyer sans confirmation

${CYAN}Répertoires nettoyés:${NC}
  - target/          (Maven)
  - node_modules/    (npm/yarn)
  - dist/            (builds frontend)
  - build/           (Gradle)
  - .gradle/         (cache Gradle)
  - .cache/          (caches divers)
  - *.log            (avec --logs)
  - *.tmp            (fichiers temporaires)
  - *~               (backups éditeurs)

EOF
    exit 0
}

print_header() {
    echo -e "\n${BLUE}=========================================="
    echo -e "$1"
    echo -e "==========================================${NC}\n"
}

print_success() {
    echo -e "${GREEN}[?]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

print_error() {
    echo -e "${RED}[?]${NC} $1"
}

print_info() {
    echo -e "${CYAN}[i]${NC} $1"
}

print_verbose() {
    if [ "$VERBOSE" = true ]; then
        echo -e "${CYAN}[v]${NC} $1"
    fi
}

format_size() {
    local size=$1
    if [ $size -ge 1073741824 ]; then
        echo "$(echo "scale=2; $size/1073741824" | bc) GB"
    elif [ $size -ge 1048576 ]; then
        echo "$(echo "scale=2; $size/1048576" | bc) MB"
    elif [ $size -ge 1024 ]; then
        echo "$(echo "scale=2; $size/1024" | bc) KB"
    else
        echo "$size bytes"
    fi
}

get_dir_size() {
    local dir="$1"
    if [ -d "$dir" ]; then
        du -sb "$dir" 2>/dev/null | cut -f1
    else
        echo 0
    fi
}

################################################################################
# Parsing des Arguments
################################################################################

while [[ $# -gt 0 ]]; do
    case $1 in
        -a|--auto)
            AUTO_MODE=true
            shift
            ;;
        -d|--dry-run)
            DRY_RUN=true
            shift
            ;;
        -v|--verbose)
            VERBOSE=true
            shift
            ;;
        -m|--maven)
            CLEAN_MAVEN=true
            CLEAN_NODE=false
            CLEAN_GRADLE=false
            shift
            ;;
        -n|--node)
            CLEAN_MAVEN=false
            CLEAN_NODE=true
            CLEAN_GRADLE=false
            shift
            ;;
        -g|--gradle)
            CLEAN_MAVEN=false
            CLEAN_NODE=false
            CLEAN_GRADLE=true
            shift
            ;;
        -l|--logs)
            CLEAN_LOGS=true
            shift
            ;;
        --all)
            CLEAN_MAVEN=true
            CLEAN_NODE=true
            CLEAN_GRADLE=true
            CLEAN_LOGS=true
            shift
            ;;
        -h|--help)
            show_help
            ;;
        *)
            START_DIR="$1"
            shift
            ;;
    esac
done

################################################################################
# Vérifications
################################################################################

if [ "$DRY_RUN" = true ]; then
    print_header "Mode Simulation (Dry Run)"
    print_warning "Aucun fichier ne sera supprimé"
fi

if [ ! -d "$START_DIR" ]; then
    print_error "Le répertoire $START_DIR n'existe pas"
    exit 1
fi

START_DIR=$(cd "$START_DIR" && pwd)
print_info "Répertoire: $START_DIR"

# Afficher les options actives
echo -e "${CYAN}Options actives:${NC}"
[ "$CLEAN_MAVEN" = true ] && echo "  ? Maven (target/)"
[ "$CLEAN_NODE" = true ] && echo "  ? Node.js (node_modules/, dist/)"
[ "$CLEAN_GRADLE" = true ] && echo "  ? Gradle (build/, .gradle/)"
[ "$CLEAN_LOGS" = true ] && echo "  ? Logs (*.log)"
echo ""

################################################################################
# Analyse
################################################################################

print_header "Analyse"

TOTAL_SIZE_BEFORE=0
DIRS_TO_CLEAN=()
declare -A DIR_TYPES

# Maven - target/
if [ "$CLEAN_MAVEN" = true ]; then
    print_verbose "Recherche des répertoires target/..."
    while IFS= read -r -d '' dir; do
        size=$(get_dir_size "$dir")
        TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
        DIRS_TO_CLEAN+=("$dir")
        DIR_TYPES["$dir"]="target"
        echo -e "  ${YELLOW}target${NC}        $(format_size $size) - $dir"
    done < <(find "$START_DIR" -type d -name "target" -not -path "*/\.*" -print0 2>/dev/null)
fi

# Node.js - node_modules/
if [ "$CLEAN_NODE" = true ]; then
    print_verbose "Recherche des répertoires node_modules/..."
    while IFS= read -r -d '' dir; do
        size=$(get_dir_size "$dir")
        TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
        DIRS_TO_CLEAN+=("$dir")
        DIR_TYPES["$dir"]="node_modules"
        echo -e "  ${YELLOW}node_modules${NC}  $(format_size $size) - $dir"
    done < <(find "$START_DIR" -type d -name "node_modules" -not -path "*/\.*" -print0 2>/dev/null)
fi

# Node.js - dist/
if [ "$CLEAN_NODE" = true ]; then
    print_verbose "Recherche des répertoires dist/..."
    while IFS= read -r -d '' dir; do
        size=$(get_dir_size "$dir")
        TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
        DIRS_TO_CLEAN+=("$dir")
        DIR_TYPES["$dir"]="dist"
        echo -e "  ${YELLOW}dist${NC}          $(format_size $size) - $dir"
    done < <(find "$START_DIR" -type d -name "dist" -not -path "*/\.*" -not -path "*/node_modules/*" -print0 2>/dev/null)
fi

# Gradle - build/
if [ "$CLEAN_GRADLE" = true ]; then
    print_verbose "Recherche des répertoires build/..."
    while IFS= read -r -d '' dir; do
        size=$(get_dir_size "$dir")
        TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
        DIRS_TO_CLEAN+=("$dir")
        DIR_TYPES["$dir"]="build"
        echo -e "  ${YELLOW}build${NC}         $(format_size $size) - $dir"
    done < <(find "$START_DIR" -type d -name "build" -not -path "*/\.*" -not -path "*/node_modules/*" -print0 2>/dev/null)
fi

# Gradle - .gradle/
if [ "$CLEAN_GRADLE" = true ]; then
    print_verbose "Recherche des répertoires .gradle/..."
    while IFS= read -r -d '' dir; do
        size=$(get_dir_size "$dir")
        TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
        DIRS_TO_CLEAN+=("$dir")
        DIR_TYPES["$dir"]=".gradle"
        echo -e "  ${YELLOW}.gradle${NC}       $(format_size $size) - $dir"
    done < <(find "$START_DIR" -type d -name ".gradle" -print0 2>/dev/null)
fi

# .cache/
print_verbose "Recherche des répertoires .cache/..."
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    DIR_TYPES["$dir"]=".cache"
    echo -e "  ${YELLOW}.cache${NC}        $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name ".cache" -not -path "*/node_modules/*" -print0 2>/dev/null)

echo ""
print_info "Répertoires trouvés: ${#DIRS_TO_CLEAN[@]}"
print_info "Espace à libérer: $(format_size $TOTAL_SIZE_BEFORE)"

################################################################################
# Confirmation
################################################################################

if [ ${#DIRS_TO_CLEAN[@]} -eq 0 ]; then
    print_success "Aucun répertoire à nettoyer !"
    
    # Vérifier les logs si demandé
    if [ "$CLEAN_LOGS" = true ]; then
        LOG_COUNT=$(find "$START_DIR" -type f -name "*.log" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
        if [ $LOG_COUNT -eq 0 ]; then
            exit 0
        fi
    else
        exit 0
    fi
fi

if [ "$AUTO_MODE" = false ] && [ "$DRY_RUN" = false ]; then
    echo ""
    read -p "Voulez-vous continuer ? (y/n): " confirm
    if [[ ! "$confirm" =~ ^[Yy]$ ]]; then
        print_warning "Nettoyage annulé"
        exit 0
    fi
fi

################################################################################
# Nettoyage
################################################################################

if [ "$DRY_RUN" = false ]; then
    print_header "Nettoyage"
else
    print_header "Simulation"
fi

CLEANED_COUNT=0
FAILED_COUNT=0
CLEANED_SIZE=0

for dir in "${DIRS_TO_CLEAN[@]}"; do
    if [ -d "$dir" ]; then
        size=$(get_dir_size "$dir")
        dir_name=$(basename "$dir")
        type="${DIR_TYPES[$dir]}"
        
        if [ "$DRY_RUN" = true ]; then
            echo -e "  ${BLUE}[SIMULATION]${NC} Supprimerait: ${YELLOW}$dir_name${NC} ($(format_size $size))"
            CLEANED_COUNT=$((CLEANED_COUNT + 1))
            CLEANED_SIZE=$((CLEANED_SIZE + size))
        else
            print_verbose "Suppression: $dir"
            if rm -rf "$dir" 2>/dev/null; then
                CLEANED_COUNT=$((CLEANED_COUNT + 1))
                CLEANED_SIZE=$((CLEANED_SIZE + size))
                echo -e "  ${GREEN}?${NC} Supprimé: ${YELLOW}$dir_name${NC} ($(format_size $size))"
            else
                FAILED_COUNT=$((FAILED_COUNT + 1))
                echo -e "  ${RED}?${NC} Échec: $dir"
            fi
        fi
    fi
done

################################################################################
# Nettoyage des Fichiers
################################################################################

if [ "$CLEAN_LOGS" = true ]; then
    echo ""
    print_info "Nettoyage des fichiers .log..."
    
    LOG_COUNT=$(find "$START_DIR" -type f -name "*.log" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
    
    if [ $LOG_COUNT -gt 0 ]; then
        if [ "$DRY_RUN" = true ]; then
            echo -e "  ${BLUE}[SIMULATION]${NC} Supprimerait $LOG_COUNT fichier(s) .log"
        else
            find "$START_DIR" -type f -name "*.log" -not -path "*/node_modules/*" -delete 2>/dev/null
            print_success "Supprimé $LOG_COUNT fichier(s) .log"
        fi
    else
        print_info "Aucun fichier .log trouvé"
    fi
fi

# Toujours nettoyer les fichiers temporaires
echo ""
print_info "Nettoyage des fichiers temporaires..."

# .tmp
TMP_COUNT=$(find "$START_DIR" -type f -name "*.tmp" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
if [ $TMP_COUNT -gt 0 ]; then
    if [ "$DRY_RUN" = false ]; then
        find "$START_DIR" -type f -name "*.tmp" -not -path "*/node_modules/*" -delete 2>/dev/null
    fi
    print_success "$([ "$DRY_RUN" = true ] && echo "Supprimerait" || echo "Supprimé") $TMP_COUNT fichier(s) .tmp"
fi

# Backups éditeurs (*~)
BACKUP_COUNT=$(find "$START_DIR" -type f -name "*~" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
if [ $BACKUP_COUNT -gt 0 ]; then
    if [ "$DRY_RUN" = false ]; then
        find "$START_DIR" -type f -name "*~" -not -path "*/node_modules/*" -delete 2>/dev/null
    fi
    print_success "$([ "$DRY_RUN" = true ] && echo "Supprimerait" || echo "Supprimé") $BACKUP_COUNT fichier(s) de backup"
fi

################################################################################
# Résumé
################################################################################

print_header "Résumé"

if [ "$DRY_RUN" = true ]; then
    echo -e "${BLUE}Mode simulation activé${NC}"
    echo -e "${CYAN}Répertoires qui seraient supprimés:${NC} $CLEANED_COUNT"
else
    echo -e "${GREEN}? Répertoires supprimés:${NC} $CLEANED_COUNT"
    if [ $FAILED_COUNT -gt 0 ]; then
        echo -e "${RED}? Échecs:${NC} $FAILED_COUNT"
    fi
fi

echo -e "${BLUE}?? Espace $([ "$DRY_RUN" = true ] && echo "à libérer" || echo "libéré"):${NC} $(format_size $CLEANED_SIZE)"

# Espace disque disponible
DISK_FREE=$(df -k "$START_DIR" | tail -1 | awk '{print $4}')
DISK_FREE_BYTES=$((DISK_FREE * 1024))
echo -e "${BLUE}?? Espace disque disponible:${NC} $(format_size $DISK_FREE_BYTES)"

echo ""
if [ "$DRY_RUN" = true ]; then
    print_info "Simulation terminée. Exécutez sans --dry-run pour effectuer le nettoyage."
else
    print_success "Nettoyage terminé !"
fi

exit 0