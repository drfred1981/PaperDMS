#!/bin/bash

################################################################################
# Script de Nettoyage des Fichiers Temporaires de Build
################################################################################
#
# Nettoie les répertoires et fichiers temporaires des projets :
# - target/ (Maven)
# - node_modules/ (npm/yarn)
# - dist/ (builds frontend)
# - build/ (Gradle)
# - .gradle/ (cache Gradle)
# - Et autres fichiers temporaires
#
# Usage: ./clean-projects.sh [chemin]
#
################################################################################

# Couleurs
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
START_DIR="${1:-.}"  # Par défaut : répertoire courant

################################################################################
# Fonctions
################################################################################

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

print_info() {
    echo -e "${BLUE}[i]${NC} $1"
}

# Fonction pour formater la taille
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

# Fonction pour calculer la taille d'un répertoire
get_dir_size() {
    local dir="$1"
    if [ -d "$dir" ]; then
        du -sb "$dir" 2>/dev/null | cut -f1
    else
        echo 0
    fi
}

################################################################################
# Vérifications
################################################################################

print_header "Nettoyage des Projets"

# Vérifier que le répertoire existe
if [ ! -d "$START_DIR" ]; then
    print_warning "Le répertoire $START_DIR n'existe pas"
    exit 1
fi

# Convertir en chemin absolu
START_DIR=$(cd "$START_DIR" && pwd)
print_info "Répertoire de départ: $START_DIR"

################################################################################
# Analyse Avant Nettoyage
################################################################################

print_header "Analyse..."

TOTAL_SIZE_BEFORE=0
DIRS_TO_CLEAN=()

# Chercher les répertoires target/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}target${NC}        $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name "target" -not -path "*/\.*" -print0 2>/dev/null)

# Chercher les répertoires node_modules/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}node_modules${NC}  $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name "node_modules" -not -path "*/\.*" -print0 2>/dev/null)

# Chercher les répertoires dist/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}dist${NC}          $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name "dist" -not -path "*/\.*" -not -path "*/node_modules/*" -print0 2>/dev/null)

# Chercher les répertoires build/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}build${NC}         $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name "build" -not -path "*/\.*" -not -path "*/node_modules/*" -print0 2>/dev/null)

# Chercher les répertoires .gradle/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}.gradle${NC}       $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name ".gradle" -print0 2>/dev/null)

# Chercher les répertoires .cache/
while IFS= read -r -d '' dir; do
    size=$(get_dir_size "$dir")
    TOTAL_SIZE_BEFORE=$((TOTAL_SIZE_BEFORE + size))
    DIRS_TO_CLEAN+=("$dir")
    echo -e "  ${YELLOW}.cache${NC}        $(format_size $size) - $dir"
done < <(find "$START_DIR" -type d -name ".cache" -not -path "*/node_modules/*" -print0 2>/dev/null)

echo ""
print_info "Nombre total de répertoires trouvés: ${#DIRS_TO_CLEAN[@]}"
print_info "Espace disque à libérer: $(format_size $TOTAL_SIZE_BEFORE)"

################################################################################
# Confirmation
################################################################################

if [ ${#DIRS_TO_CLEAN[@]} -eq 0 ]; then
    print_success "Aucun répertoire à nettoyer. Le projet est déjà propre !"
    exit 0
fi

echo ""
read -p "Voulez-vous supprimer ces répertoires ? (y/n): " confirm

if [[ ! "$confirm" =~ ^[Yy]$ ]]; then
    print_warning "Nettoyage annulé"
    exit 0
fi

################################################################################
# Nettoyage
################################################################################

print_header "Nettoyage en cours..."

CLEANED_COUNT=0
FAILED_COUNT=0
CLEANED_SIZE=0

for dir in "${DIRS_TO_CLEAN[@]}"; do
    if [ -d "$dir" ]; then
        size=$(get_dir_size "$dir")
        dir_name=$(basename "$dir")
        parent_dir=$(dirname "$dir")
        
        if rm -rf "$dir" 2>/dev/null; then
            CLEANED_COUNT=$((CLEANED_COUNT + 1))
            CLEANED_SIZE=$((CLEANED_SIZE + size))
            echo -e "  ${GREEN}?${NC} Supprimé: ${YELLOW}$dir_name${NC} ($(format_size $size))"
        else
            FAILED_COUNT=$((FAILED_COUNT + 1))
            echo -e "  ${RED}?${NC} Échec: $dir"
        fi
    fi
done

################################################################################
# Nettoyage Additionnel
################################################################################

print_header "Nettoyage Additionnel"

# Nettoyer les fichiers .log
print_info "Suppression des fichiers .log..."
LOG_COUNT=$(find "$START_DIR" -type f -name "*.log" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
if [ $LOG_COUNT -gt 0 ]; then
    find "$START_DIR" -type f -name "*.log" -not -path "*/node_modules/*" -delete 2>/dev/null
    print_success "Supprimé $LOG_COUNT fichier(s) .log"
else
    print_info "Aucun fichier .log trouvé"
fi

# Nettoyer les fichiers .tmp
print_info "Suppression des fichiers .tmp..."
TMP_COUNT=$(find "$START_DIR" -type f -name "*.tmp" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
if [ $TMP_COUNT -gt 0 ]; then
    find "$START_DIR" -type f -name "*.tmp" -not -path "*/node_modules/*" -delete 2>/dev/null
    print_success "Supprimé $TMP_COUNT fichier(s) .tmp"
else
    print_info "Aucun fichier .tmp trouvé"
fi

# Nettoyer les fichiers *~ (backups éditeurs)
print_info "Suppression des fichiers de backup éditeurs (*~)..."
BACKUP_COUNT=$(find "$START_DIR" -type f -name "*~" -not -path "*/node_modules/*" 2>/dev/null | wc -l)
if [ $BACKUP_COUNT -gt 0 ]; then
    find "$START_DIR" -type f -name "*~" -not -path "*/node_modules/*" -delete 2>/dev/null
    print_success "Supprimé $BACKUP_COUNT fichier(s) de backup"
else
    print_info "Aucun fichier de backup trouvé"
fi

################################################################################
# Résumé
################################################################################

print_header "Résumé du Nettoyage"

echo -e "${GREEN}? Répertoires supprimés:${NC}   $CLEANED_COUNT"
if [ $FAILED_COUNT -gt 0 ]; then
    echo -e "${RED}? Échecs:${NC}                 $FAILED_COUNT"
fi
echo -e "${BLUE}?? Espace libéré:${NC}          $(format_size $CLEANED_SIZE)"

# Espace disque total disponible
DISK_FREE=$(df -k "$START_DIR" | tail -1 | awk '{print $4}')
DISK_FREE_BYTES=$((DISK_FREE * 1024))
echo -e "${BLUE}?? Espace disque disponible:${NC} $(format_size $DISK_FREE_BYTES)"

echo ""
print_success "Nettoyage terminé !"

################################################################################
# Suggestions
################################################################################

print_header "Suggestions"

echo "Pour automatiser le nettoyage, vous pouvez :"
echo ""
echo "1. Créer un alias dans ~/.bashrc :"
echo "   alias clean-projects='$0'"
echo ""
echo "2. Ajouter à un cron job hebdomadaire :"
echo "   0 0 * * 0 $0 ~/workspace/paperdms"
echo ""
echo "3. Créer un script Git pre-commit pour nettoyer avant chaque commit"
echo ""

exit 0