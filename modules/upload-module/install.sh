#!/bin/bash

# Script d'installation du module d'upload PaperDMS v2.0 (Standalone)
# Usage: ./install.sh /path/to/paperdms/project

set -e

# Couleurs pour les messages
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
RED='\033[0;31m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Fonction d'affichage
print_header() {
    echo -e "${BLUE}=========================================="
    echo -e "$1"
    echo -e "==========================================${NC}"
}

print_step() {
    echo -e "${GREEN}[✓]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[!]${NC} $1"
}

print_error() {
    echo -e "${RED}[✗]${NC} $1"
}

print_info() {
    echo -e "${BLUE}[i]${NC} $1"
}

# Vérifier les arguments
if [ $# -eq 0 ]; then
    print_error "Usage: $0 /path/to/paperdms/project"
    echo ""
    echo "Exemple:"
    echo "  $0 ~/projects/paperdms"
    echo "  $0 /home/user/workspace/paperdms"
    exit 1
fi

PROJECT_ROOT="$1"

# Vérifier que le projet existe
if [ ! -d "$PROJECT_ROOT" ]; then
    print_error "Le répertoire $PROJECT_ROOT n'existe pas"
    exit 1
fi

# Vérifier la structure du projet
if [ ! -d "$PROJECT_ROOT/documentService" ] || [ ! -d "$PROJECT_ROOT/gateway" ]; then
    print_error "Structure du projet invalide."
    echo ""
    echo "Le répertoire doit contenir:"
    echo "  - documentService/"
    echo "  - gateway/"
    echo ""
    echo "Structure détectée:"
    ls -la "$PROJECT_ROOT"
    exit 1
fi

print_header "Installation du Module d'Upload PaperDMS v2.0"
echo ""
print_info "Projet: $PROJECT_ROOT"
echo ""

# Variables
DOC_SERVICE="$PROJECT_ROOT/documentService"
GATEWAY="$PROJECT_ROOT/gateway"
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

# ==========================================
# VÉRIFICATION DES FICHIERS SOURCE
# ==========================================

print_step "Vérification des fichiers source..."

if [ ! -d "$SCRIPT_DIR/backend" ] || [ ! -d "$SCRIPT_DIR/frontend" ]; then
    print_error "Fichiers source manquants dans $SCRIPT_DIR"
    echo "Structure attendue:"
    echo "  - backend/"
    echo "  - frontend/"
    exit 1
fi

# Compter les fichiers
BACKEND_FILES=$(find "$SCRIPT_DIR/backend" -name "*.java" | wc -l)
FRONTEND_FILES=$(find "$SCRIPT_DIR/frontend" -type f | wc -l)

print_info "Fichiers backend trouvés: $BACKEND_FILES"
print_info "Fichiers frontend trouvés: $FRONTEND_FILES"

if [ "$BACKEND_FILES" -lt 7 ]; then
    print_warning "Nombre de fichiers backend insuffisant (attendu: 7, trouvé: $BACKEND_FILES)"
fi

echo ""

# ==========================================
# BACKEND - Document Service
# ==========================================

print_header "Installation du Backend (Document Service)"
echo ""

# Créer la structure de répertoires
UPLOAD_BASE="$DOC_SERVICE/src/main/java/fr/smartprod/paperdms/document/upload"

print_step "Création de la structure de répertoires..."
mkdir -p "$UPLOAD_BASE/web"
mkdir -p "$UPLOAD_BASE/service"
mkdir -p "$UPLOAD_BASE/dto"
mkdir -p "$UPLOAD_BASE/config"

# Copier les fichiers Java
print_step "Copie des fichiers Java..."

if [ -f "$SCRIPT_DIR/backend/DocumentUploadResource.java" ]; then
    cp "$SCRIPT_DIR/backend/DocumentUploadResource.java" "$UPLOAD_BASE/web/"
    print_info "  ✓ DocumentUploadResource.java"
else
    print_warning "  ✗ DocumentUploadResource.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/DocumentUploadService.java" ]; then
    cp "$SCRIPT_DIR/backend/DocumentUploadService.java" "$UPLOAD_BASE/service/"
    print_info "  ✓ DocumentUploadService.java"
else
    print_warning "  ✗ DocumentUploadService.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/S3StorageService.java" ]; then
    cp "$SCRIPT_DIR/backend/S3StorageService.java" "$UPLOAD_BASE/service/"
    print_info "  ✓ S3StorageService.java"
else
    print_warning "  ✗ S3StorageService.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/ThumbnailService.java" ]; then
    cp "$SCRIPT_DIR/backend/ThumbnailService.java" "$UPLOAD_BASE/service/"
    print_info "  ✓ ThumbnailService.java"
else
    print_warning "  ✗ ThumbnailService.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/DocumentUploadRequest.java" ]; then
    cp "$SCRIPT_DIR/backend/DocumentUploadRequest.java" "$UPLOAD_BASE/dto/"
    print_info "  ✓ DocumentUploadRequest.java"
else
    print_warning "  ✗ DocumentUploadRequest.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/DocumentUploadedEvent.java" ]; then
    cp "$SCRIPT_DIR/backend/DocumentUploadedEvent.java" "$UPLOAD_BASE/dto/"
    print_info "  ✓ DocumentUploadedEvent.java"
else
    print_warning "  ✗ DocumentUploadedEvent.java non trouvé"
fi

if [ -f "$SCRIPT_DIR/backend/S3Config.java" ]; then
    cp "$SCRIPT_DIR/backend/S3Config.java" "$UPLOAD_BASE/config/"
    print_info "  ✓ S3Config.java"
else
    print_warning "  ✗ S3Config.java non trouvé"
fi

print_step "Fichiers backend copiés: $UPLOAD_BASE"

echo ""

# Vérifier le pom.xml
print_step "Vérification du pom.xml..."
if [ -f "$DOC_SERVICE/pom.xml" ]; then
    if grep -q "aws-java-sdk-s3" "$DOC_SERVICE/pom.xml"; then
        print_step "  ✓ Dépendances Maven déjà présentes"
    else
        print_warning "  ! Dépendances Maven à ajouter manuellement"
        print_info "    Ajoutez dans pom.xml:"
        echo ""
        echo "    <dependency>"
        echo "        <groupId>com.amazonaws</groupId>"
        echo "        <artifactId>aws-java-sdk-s3</artifactId>"
        echo "        <version>1.12.583</version>"
        echo "    </dependency>"
        echo "    <dependency>"
        echo "        <groupId>org.apache.pdfbox</groupId>"
        echo "        <artifactId>pdfbox</artifactId>"
        echo "        <version>3.0.1</version>"
        echo "    </dependency>"
        echo ""
    fi
else
    print_error "  ✗ pom.xml non trouvé dans $DOC_SERVICE"
fi

echo ""

# Vérifier application-dev.yml
print_step "Vérification de application-dev.yml..."
APP_YML="$DOC_SERVICE/src/main/resources/config/application-dev.yml"
if [ -f "$APP_YML" ]; then
    if grep -q "paperdms.s3" "$APP_YML"; then
        print_step "  ✓ Configuration S3 déjà présente"
    else
        print_warning "  ! Configuration S3 à ajouter manuellement"
        print_info "    Ajoutez dans application-dev.yml:"
        echo ""
        echo "    spring:"
        echo "      servlet:"
        echo "        multipart:"
        echo "          max-file-size: 100MB"
        echo ""
        echo "    paperdms:"
        echo "      s3:"
        echo "        bucket: paperdms-documents"
        echo "        region: eu-west-1"
        echo "        access-key: minioadmin"
        echo "        secret-key: minioadmin"
        echo "        endpoint: http://localhost:9000"
        echo ""
    fi
else
    print_error "  ✗ application-dev.yml non trouvé"
fi

echo ""

# ==========================================
# FRONTEND - Gateway
# ==========================================

print_header "Installation du Frontend (Gateway) - Version Standalone"
echo ""

# Créer la structure de répertoires
WEBAPP="$GATEWAY/src/main/webapp/app"
UPLOAD_MODULE="$WEBAPP/document-upload"

print_step "Création de la structure de répertoires..."
mkdir -p "$UPLOAD_MODULE/upload"
mkdir -p "$UPLOAD_MODULE/services"
mkdir -p "$UPLOAD_MODULE/models"

# Copier les fichiers TypeScript
print_step "Copie des fichiers TypeScript..."

# Routes (version standalone)
if [ -f "$SCRIPT_DIR/frontend/document-upload.routes.ts" ]; then
    cp "$SCRIPT_DIR/frontend/document-upload.routes.ts" "$UPLOAD_MODULE/"
    print_info "  ✓ document-upload.routes.ts (standalone)"
else
    print_warning "  ✗ document-upload.routes.ts non trouvé"
fi

# Composant
if [ -f "$SCRIPT_DIR/frontend/upload/document-upload.component.ts" ]; then
    cp "$SCRIPT_DIR/frontend/upload/document-upload.component.ts" "$UPLOAD_MODULE/upload/"
    print_info "  ✓ document-upload.component.ts"
else
    print_warning "  ✗ document-upload.component.ts non trouvé"
fi

if [ -f "$SCRIPT_DIR/frontend/upload/document-upload.component.html" ]; then
    cp "$SCRIPT_DIR/frontend/upload/document-upload.component.html" "$UPLOAD_MODULE/upload/"
    print_info "  ✓ document-upload.component.html"
else
    print_warning "  ✗ document-upload.component.html non trouvé"
fi

if [ -f "$SCRIPT_DIR/frontend/upload/document-upload.component.scss" ]; then
    cp "$SCRIPT_DIR/frontend/upload/document-upload.component.scss" "$UPLOAD_MODULE/upload/"
    print_info "  ✓ document-upload.component.scss"
else
    print_warning "  ✗ document-upload.component.scss non trouvé"
fi

# Services
if [ -f "$SCRIPT_DIR/frontend/services/document-upload.service.ts" ]; then
    cp "$SCRIPT_DIR/frontend/services/document-upload.service.ts" "$UPLOAD_MODULE/services/"
    print_info "  ✓ document-upload.service.ts"
else
    print_warning "  ✗ document-upload.service.ts non trouvé"
fi

if [ -f "$SCRIPT_DIR/frontend/services/sha256.service.ts" ]; then
    cp "$SCRIPT_DIR/frontend/services/sha256.service.ts" "$UPLOAD_MODULE/services/"
    print_info "  ✓ sha256.service.ts"
else
    print_warning "  ✗ sha256.service.ts non trouvé"
fi

if [ -f "$SCRIPT_DIR/frontend/services/file-validation.service.ts" ]; then
    cp "$SCRIPT_DIR/frontend/services/file-validation.service.ts" "$UPLOAD_MODULE/services/"
    print_info "  ✓ file-validation.service.ts"
else
    print_warning "  ✗ file-validation.service.ts non trouvé"
fi

# Modèles
if [ -f "$SCRIPT_DIR/frontend/models/upload-progress.model.ts" ]; then
    cp "$SCRIPT_DIR/frontend/models/upload-progress.model.ts" "$UPLOAD_MODULE/models/"
    print_info "  ✓ upload-progress.model.ts"
else
    print_warning "  ✗ upload-progress.model.ts non trouvé"
fi

print_step "Fichiers frontend copiés: $UPLOAD_MODULE"

echo ""

# Vérifier app.routes.ts
print_step "Vérification de app.routes.ts..."
APP_ROUTES="$WEBAPP/app.routes.ts"
if [ -f "$APP_ROUTES" ]; then
    if grep -q "document-upload" "$APP_ROUTES"; then
        print_step "  ✓ Route déjà configurée"
    else
        print_warning "  ! Route à ajouter manuellement dans app.routes.ts"
        print_info "    Ajoutez dans le tableau routes:"
        echo ""
        echo "    {"
        echo "      path: 'document-upload',"
        echo "      loadChildren: () => import('./document-upload/document-upload.routes'),"
        echo "      canActivate: [UserRouteAccessService],"
        echo "    },"
        echo ""
    fi
else
    print_error "  ✗ app.routes.ts non trouvé"
    print_info "    Cherchez app-routing.module.ts ou entity.routes.ts"
fi

echo ""

# Vérifier les traductions
print_step "Vérification des traductions..."
I18N_FILE="$GATEWAY/src/main/webapp/i18n/fr/document.json"
if [ -f "$I18N_FILE" ]; then
    if grep -q "upload" "$I18N_FILE"; then
        print_step "  ✓ Traductions déjà présentes"
    else
        print_warning "  ! Traductions à ajouter dans i18n/fr/document.json"
        print_info "    Ajoutez:"
        echo ""
        echo '    "upload": {'
        echo '      "title": "Télécharger des Documents",'
        echo '      "dragDrop": "Glissez-déposez vos fichiers ici",'
        echo '      "orClick": "ou cliquez pour sélectionner"'
        echo '    }'
        echo ""
    fi
else
    print_warning "  ! Fichier de traduction non trouvé: $I18N_FILE"
fi

echo ""

# ==========================================
# RÉSUMÉ
# ==========================================

print_header "Installation Terminée !"
echo ""

print_step "Fichiers installés:"
echo ""
echo "  Backend:  $UPLOAD_BASE"
echo "            ├── web/DocumentUploadResource.java"
echo "            ├── service/DocumentUploadService.java"
echo "            ├── service/S3StorageService.java"
echo "            ├── service/ThumbnailService.java"
echo "            ├── dto/DocumentUploadRequest.java"
echo "            ├── dto/DocumentUploadedEvent.java"
echo "            └── config/S3Config.java"
echo ""
echo "  Frontend: $UPLOAD_MODULE"
echo "            ├── document-upload.routes.ts (standalone)"
echo "            ├── upload/"
echo "            │   ├── document-upload.component.ts"
echo "            │   ├── document-upload.component.html"
echo "            │   └── document-upload.component.scss"
echo "            ├── services/"
echo "            │   ├── document-upload.service.ts"
echo "            │   ├── sha256.service.ts"
echo "            │   └── file-validation.service.ts"
echo "            └── models/"
echo "                └── upload-progress.model.ts"
echo ""

print_warning "Actions manuelles requises:"
echo ""

# Backend
echo "${BLUE}1. Backend - Dépendances Maven${NC}"
if ! grep -q "aws-java-sdk-s3" "$DOC_SERVICE/pom.xml" 2>/dev/null; then
    echo "   Fichier: $DOC_SERVICE/pom.xml"
    echo "   Action:  Ajouter les dépendances AWS SDK, PDFBox, WebP"
    echo "   Voir:    INTEGRATION-GUIDE-STANDALONE.md section 'Dépendances Maven'"
    echo ""
fi

echo "${BLUE}2. Backend - Configuration S3${NC}"
if ! grep -q "paperdms.s3" "$APP_YML" 2>/dev/null; then
    echo "   Fichier: $APP_YML"
    echo "   Action:  Ajouter la configuration S3/MinIO"
    echo "   Voir:    INTEGRATION-GUIDE-STANDALONE.md section 'Configuration'"
    echo ""
fi

# Frontend
echo "${BLUE}3. Frontend - Route Standalone${NC}"
if [ -f "$APP_ROUTES" ] && ! grep -q "document-upload" "$APP_ROUTES"; then
    echo "   Fichier: $APP_ROUTES"
    echo "   Action:  Ajouter la route 'document-upload'"
    echo "   Code:"
    echo "   {"
    echo "     path: 'document-upload',"
    echo "     loadChildren: () => import('./document-upload/document-upload.routes')"
    echo "   }"
    echo ""
fi

echo "${BLUE}4. Frontend - Traductions${NC}"
if [ -f "$I18N_FILE" ] && ! grep -q "upload" "$I18N_FILE"; then
    echo "   Fichier: $I18N_FILE"
    echo "   Action:  Ajouter les traductions pour l'upload"
    echo "   Voir:    INTEGRATION-GUIDE-STANDALONE.md section 'Traductions'"
    echo ""
fi

# Services
echo "${BLUE}5. Services Externes${NC}"
echo "   MinIO:   Démarrer sur le port 9000"
echo "   Kafka:   Démarrer sur le port 9092"
echo "   PostgreSQL: Vérifier qu'il est démarré"
echo ""
echo "   Commandes rapides:"
echo "   docker run -d -p 9000:9000 -p 9001:9001 \\"
echo "     -e MINIO_ROOT_USER=minioadmin \\"
echo "     -e MINIO_ROOT_PASSWORD=minioadmin \\"
echo "     minio/minio server /data --console-address ':9001'"
echo ""
echo "   mc alias set local http://localhost:9000 minioadmin minioadmin"
echo "   mc mb local/paperdms-documents"
echo ""

echo "${BLUE}6. Données de Test${NC}"
echo "   Créer un DocumentType en base de données:"
echo "   INSERT INTO document_type (id, name, code, is_active, created_date, created_by)"
echo "   VALUES (1, 'Document Général', 'GENERAL', true, NOW(), 'system');"
echo ""

print_header "Documentation"
echo ""
echo "Consultez les guides pour plus de détails:"
echo "  - README.md                        Vue d'ensemble"
echo "  - INTEGRATION-GUIDE-STANDALONE.md  Guide d'intégration complet"
echo "  - TESTING.md                       Scénarios de test"
echo ""

print_step "Installation des fichiers terminée avec succès !"
print_info "Complétez les actions manuelles ci-dessus pour finaliser l'installation."
echo ""
