#!/bin/bash

################################################################################
# PaperDMS - Script d'Installation Environnement de Développement Ubuntu
################################################################################
#
# Ce script installe et configure un environnement complet pour:
# - Développement local
# - Tests
# - Déploiement local
# - Synchronisation avec machine Windows (Syncthing)
#
# Usage: ./setup-dev-environment.sh
#
################################################################################

set -e

# Couleurs
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
CYAN='\033[0;36m'
NC='\033[0m' # No Color

# Configuration
WORKSPACE_DIR="$HOME/paperdms"
SYNCTHING_DIR="$HOME/paperdms"
DOCKER_COMPOSE_VERSION="2.23.0"
MAVEN_VERSION="3.9.6"
NODE_VERSION="20"

################################################################################
# Fonctions Utilitaires
################################################################################

print_header() {
    echo -e "\n${BLUE}=========================================="
    echo -e "$1"
    echo -e "==========================================${NC}\n"
}

print_step() {
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

command_exists() {
    command -v "$1" &> /dev/null
}

################################################################################
# Vérifications Préliminaires
################################################################################

print_header "Vérifications Préliminaires"

# Vérifier Ubuntu
if ! grep -qi "ubuntu" /etc/os-release; then
    print_error "Ce script est conçu pour Ubuntu. Votre système: $(cat /etc/os-release | grep PRETTY_NAME)"
    exit 1
fi

print_step "Système: Ubuntu $(lsb_release -rs)"

# Vérifier sudo
if ! sudo -n true 2>/dev/null; then
    print_info "Ce script nécessite des privilèges sudo"
    sudo -v
fi

################################################################################
# Mise à Jour du Système
################################################################################

print_header "Mise à Jour du Système"

print_info "Mise à jour des paquets..."
sudo apt-get update -qq
sudo apt-get upgrade -y -qq

print_step "Système mis à jour"

################################################################################
# Installation des Outils de Base
################################################################################

print_header "Installation des Outils de Base"

print_info "Installation de curl, wget, git, build-essential..."
sudo apt-get install -y -qq \
    curl \
    wget \
    git \
    build-essential \
    software-properties-common \
    apt-transport-https \
    ca-certificates \
    gnupg \
    lsb-release \
    unzip \
    zip \
    htop \
    net-tools \
    dos2unix

print_step "Outils de base installés"

################################################################################
# Installation de Java (OpenJDK 17)
################################################################################

print_header "Installation de Java 17"

if command_exists java; then
    JAVA_VERSION=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2 | cut -d'.' -f1)
    if [ "$JAVA_VERSION" == "17" ]; then
        print_step "Java 17 déjà installé"
    else
        print_warning "Java $JAVA_VERSION installé, installation de Java 17..."
        sudo apt-get install -y openjdk-17-jdk
    fi
else
    print_info "Installation de Java 17..."
    sudo apt-get install -y openjdk-17-jdk
fi

# Configuration JAVA_HOME
if ! grep -q "JAVA_HOME" ~/.bashrc; then
    echo "" >> ~/.bashrc
    echo "# Java Configuration" >> ~/.bashrc
    echo "export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64" >> ~/.bashrc
    echo "export PATH=\$JAVA_HOME/bin:\$PATH" >> ~/.bashrc
    print_step "JAVA_HOME configuré dans ~/.bashrc"
fi

export JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH

print_step "Java $(java -version 2>&1 | head -n 1 | cut -d'"' -f2) installé"

################################################################################
# Installation de Maven
################################################################################

print_header "Installation de Maven $MAVEN_VERSION"

if command_exists mvn; then
    MVN_VERSION=$(mvn -version | head -n 1 | cut -d' ' -f3)
    print_step "Maven $MVN_VERSION déjà installé"
else
    print_info "Installation de Maven $MAVEN_VERSION..."
    cd /tmp
    wget -q https://dlcdn.apache.org/maven/maven-3/$MAVEN_VERSION/binaries/apache-maven-$MAVEN_VERSION-bin.tar.gz
    sudo tar -xzf apache-maven-$MAVEN_VERSION-bin.tar.gz -C /opt
    sudo ln -sf /opt/apache-maven-$MAVEN_VERSION /opt/maven
    
    if ! grep -q "M2_HOME" ~/.bashrc; then
        echo "" >> ~/.bashrc
        echo "# Maven Configuration" >> ~/.bashrc
        echo "export M2_HOME=/opt/maven" >> ~/.bashrc
        echo "export PATH=\$M2_HOME/bin:\$PATH" >> ~/.bashrc
    fi
    
    export M2_HOME=/opt/maven
    export PATH=$M2_HOME/bin:$PATH
    
    print_step "Maven $MAVEN_VERSION installé"
fi

################################################################################
# Installation de Node.js et npm
################################################################################

print_header "Installation de Node.js $NODE_VERSION"

if command_exists node; then
    NODE_VER=$(node -v | cut -d'v' -f2 | cut -d'.' -f1)
    if [ "$NODE_VER" == "$NODE_VERSION" ]; then
        print_step "Node.js v$(node -v) déjà installé"
    else
        print_info "Mise à jour de Node.js vers v$NODE_VERSION..."
        curl -fsSL https://deb.nodesource.com/setup_$NODE_VERSION.x | sudo -E bash -
        sudo apt-get install -y nodejs
    fi
else
    print_info "Installation de Node.js v$NODE_VERSION..."
    curl -fsSL https://deb.nodesource.com/setup_$NODE_VERSION.x | sudo -E bash -
    sudo apt-get install -y nodejs
fi

print_step "Node.js $(node -v) et npm $(npm -v) installés"

################################################################################
# Installation de Docker
################################################################################

print_header "Installation de Docker"

if command_exists docker; then
    print_step "Docker $(docker --version | cut -d' ' -f3 | tr -d ',') déjà installé"
else
    print_info "Installation de Docker..."
    
    # Ajouter le dépôt Docker
    sudo install -m 0755 -d /etc/apt/keyrings
    curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
    sudo chmod a+r /etc/apt/keyrings/docker.gpg
    
    echo \
      "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/ubuntu \
      $(lsb_release -cs) stable" | \
      sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
    
    sudo apt-get update -qq
    sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
    
    # Ajouter l'utilisateur au groupe docker
    sudo usermod -aG docker $USER
    
    print_step "Docker installé"
    print_warning "IMPORTANT: Vous devez vous déconnecter et reconnecter pour que le groupe docker soit actif"
fi

################################################################################
# Installation de Docker Compose
################################################################################

print_header "Installation de Docker Compose"

if command_exists docker && docker compose version &>/dev/null; then
    print_step "Docker Compose $(docker compose version --short) déjà installé"
else
    print_step "Docker Compose installé via plugin Docker"
fi


################################################################################
# Installation de Syncthing
################################################################################

print_header "Installation de Syncthing"

if command_exists syncthing; then
    print_step "Syncthing déjà installé"
else
    print_info "Installation de Syncthing..."
    
    # Ajouter le dépôt Syncthing
    sudo curl -o /usr/share/keyrings/syncthing-archive-keyring.gpg \
        https://syncthing.net/release-key.gpg
    
    echo "deb [signed-by=/usr/share/keyrings/syncthing-archive-keyring.gpg] \
        https://apt.syncthing.net/ syncthing stable" | \
        sudo tee /etc/apt/sources.list.d/syncthing.list
    
    sudo apt-get update -qq
    sudo apt-get install -y syncthing
    
    print_step "Syncthing installé"
fi

# Créer le service systemd pour Syncthing
if [ ! -f ~/.config/systemd/user/syncthing.service ]; then
    print_info "Configuration du service Syncthing..."
    mkdir -p ~/.config/systemd/user
    
    cat > ~/.config/systemd/user/syncthing.service << 'EOF'
[Unit]
Description=Syncthing - Open Source Continuous File Synchronization
Documentation=man:syncthing(1)
After=network.target

[Service]
ExecStart=/usr/bin/syncthing serve --no-browser --no-restart --logflags=0
Restart=on-failure
RestartSec=5
SuccessExitStatus=3 4
RestartForceExitStatus=3 4

[Install]
WantedBy=default.target
EOF
    
    systemctl --user enable syncthing.service
    systemctl --user start syncthing.service
    
    print_step "Service Syncthing configuré et démarré"
    print_info "Interface web: http://localhost:8384"
fi


# Script d'arrêt
cat > "$WORKSPACE_DIR/scripts/stop-services.sh" << 'EOF'
#!/bin/bash
cd "$(dirname "$0")/../docker"
echo "Arrêt des services Docker..."
docker compose down
echo "Services arrêtés"
EOF

# Script de logs
cat > "$WORKSPACE_DIR/scripts/logs.sh" << 'EOF'
#!/bin/bash
cd "$(dirname "$0")/../docker"
if [ -z "$1" ]; then
    docker compose logs -f
else
    docker compose logs -f "$1"
fi
EOF

# Script de reset
cat > "$WORKSPACE_DIR/scripts/reset-all.sh" << 'EOF'
#!/bin/bash
cd "$(dirname "$0")/../docker"
echo "ATTENTION: Ceci va supprimer toutes les données!"
read -p "Êtes-vous sûr? (yes/no): " confirm
if [ "$confirm" == "yes" ]; then
    docker compose down -v
    echo "Toutes les données ont été supprimées"
else
    echo "Opération annulée"
fi
EOF

# Script de build Maven
cat > "$WORKSPACE_DIR/scripts/build-all.sh" << 'EOF'
#!/bin/bash
cd "$(dirname "$0")/.."
echo "Build de tous les services Maven..."
for service in documentService gateway ocrService aiService; do
    if [ -d "$service" ]; then
        echo ""
        echo "===== Building $service ====="
        cd "$service"
        ./mvnw clean install -DskipTests
        cd ..
    fi
done
echo ""
echo "Build terminé!"
EOF

# Script de run d'un service
cat > "$WORKSPACE_DIR/scripts/run-service.sh" << 'EOF'
#!/bin/bash
if [ -z "$1" ]; then
    echo "Usage: $0 <service-name>"
    echo "Services disponibles: documentService, gateway, ocrService, aiService"
    exit 1
fi

cd "$(dirname "$0")/../$1"
if [ ! -d "." ]; then
    echo "Service $1 non trouvé"
    exit 1
fi

echo "Démarrage de $1..."
source ../docker/.env
./mvnw spring-boot:run
EOF

# Rendre les scripts exécutables
chmod +x "$WORKSPACE_DIR"/scripts/*.sh

print_step "Scripts utilitaires créés"

################################################################################
# Configuration du fichier hosts
################################################################################

print_header "Configuration du fichier hosts"

if ! grep -q "paperdms.local" /etc/hosts; then
    print_info "Ajout des entrées dans /etc/hosts..."
    echo "" | sudo tee -a /etc/hosts
    echo "# PaperDMS Local Development" | sudo tee -a /etc/hosts
    
    print_step "Entrées ajoutées dans /etc/hosts"
else
    print_step "Entrées déjà présentes dans /etc/hosts"
fi

################################################################################
# Configuration de l'Autocompletion
################################################################################

print_header "Configuration de l'Autocompletion"

if ! grep -q "docker compose completion" ~/.bashrc; then
    echo "" >> ~/.bashrc
    echo "# Docker Compose Autocompletion" >> ~/.bashrc
    echo "source <(docker compose completion bash)" >> ~/.bashrc 2>/dev/null || true
    print_step "Autocompletion Docker configurée"
fi

################################################################################
# Alias Utiles
################################################################################

print_header "Création des Alias"

if ! grep -q "alias paperdms-" ~/.bashrc; then
    cat >> ~/.bashrc << 'EOF'

# PaperDMS Aliases
alias paperdms-start='cd ~/workspace/paperdms/scripts && ./start-services.sh'
alias paperdms-stop='cd ~/workspace/paperdms/scripts && ./stop-services.sh'
alias paperdms-logs='cd ~/workspace/paperdms/scripts && ./logs.sh'
alias paperdms-build='cd ~/workspace/paperdms/scripts && ./build-all.sh'
alias paperdms-cd='cd ~/workspace/paperdms'
EOF
    print_step "Alias créés"
fi

################################################################################
# RÉSUMÉ FINAL
################################################################################

print_header "Installation Terminée ! ??"

echo -e "${GREEN}? Environnement de développement PaperDMS installé avec succès !${NC}\n"

echo -e "${CYAN}?? Outils Installés:${NC}"
echo "  ? Java:           $(java -version 2>&1 | head -n 1 | cut -d'"' -f2)"
echo "  ? Maven:          $(mvn -version 2>&1 | head -n 1 | cut -d' ' -f3)"
echo "  ? Node.js:        $(node -v)"
echo "  ? npm:            $(npm -v)"
echo "  ? Docker:         $(docker --version | cut -d' ' -f3 | tr -d ',')"
echo "  ? Docker Compose: $(docker compose version --short 2>/dev/null || echo 'Plugin installé')"
echo "  ? Syncthing:      $(syncthing --version | head -n 1 | cut -d' ' -f2)"
echo ""

echo -e "${CYAN}?? Répertoires:${NC}"
echo "  ? Workspace:      $WORKSPACE_DIR"
echo "  ? Syncthing:      $SYNCTHING_DIR"
echo "  ? Docker:         $WORKSPACE_DIR/docker"
echo "  ? Scripts:        $WORKSPACE_DIR/scripts"
echo ""

echo -e "${CYAN}?? URLs Utiles:${NC}"
echo "  ? Syncthing:      http://localhost:8384"
echo "  ? Consul:         http://localhost:8500"
echo ""

echo -e "${CYAN}?? Prochaines Étapes:${NC}"
echo "  1. Redémarrer votre session (ou: source ~/.bashrc)"
echo "  2. Configurer Syncthing:"
echo "     - Ouvrir http://localhost:8384"
echo "     - Ajouter votre machine Windows comme appareil"
echo "     - Partager le dossier $SYNCTHING_DIR"
echo "  3. Démarrer les services Docker:"
echo "     paperdms-start"
echo "  4. Cloner vos projets dans: $WORKSPACE_DIR"
echo ""

echo -e "${CYAN}?? Commandes Utiles:${NC}"
echo "  paperdms-start    - Démarrer les services"
echo "  paperdms-stop     - Arrêter les services"
echo "  paperdms-logs     - Voir les logs"
echo "  paperdms-build    - Builder tous les services"
echo "  paperdms-cd       - Aller dans le workspace"
echo ""

echo -e "${YELLOW}??  IMPORTANT:${NC}"
echo "  ? Vous devez vous déconnecter et reconnecter pour que le groupe 'docker' soit actif"
echo "  ? Configurez Syncthing pour synchroniser avec votre machine Windows"
echo "  ? Le fichier .env contient des secrets (ne pas commiter!)"
echo ""

echo -e "${GREEN}Documentation complète: $WORKSPACE_DIR/README.md${NC}"
echo ""

print_step "Profitez de votre environnement de développement PaperDMS ! ??"