
#!/usr/bin/env bash
# Usage:
#   ./print_texte.sh val1 val2 ...
#   ./print_texte.sh -f liste.txt
# Affiche un texte multi-ligne pour chaque valeur, avec sa version en majuscules.

set -euo pipefail

show_usage() {
  cat <<USAGE
Usage:
  $0 val1 val2 ...
  $0 -f fichier.txt
Options:
  -f <fichier>   Lire les valeurs depuis un fichier (une valeur par ligne).
  -h             Afficher l'aide.
USAGE
}

# --- Parsing minimal des options ---
FILE=""
ARGS=()

while (( $# )); do
  case "${1}" in
    -h|--help) show_usage; exit 0 ;;
    -f) shift; FILE="${1:-}"; [[ -n "$FILE" ]] || { echo "Erreur: fichier manquant après -f"; exit 1; }; shift ;;
    --) shift; break ;;
    -*) echo "Option inconnue: $1"; show_usage; exit 1 ;;
    *)  ARGS+=("$1"); shift ;;
  esac
done

# Si -f est fourni, lire le fichier et ajouter les lignes aux ARGS
if [[ -n "$FILE" ]]; then
  [[ -r "$FILE" ]] || { echo "Erreur: fichier non lisible: $FILE"; exit 1; }
  # Lecture sûre: ignore les lignes vides
  while IFS= read -r line; do
    [[ -n "$line" ]] && ARGS+=("$line")
  done < "$FILE"
fi

# Vérification qu'on a au moins une valeur
if [[ ${#ARGS[@]} -eq 0 ]]; then
  echo "Erreur: aucune valeur fournie."
  show_usage
  exit 1
fi

# --- Fonction d'affichage pour une valeur donnée ---
print_block() {

  local input="$1"
  local upper="${input^^}"



  # On utilise un heredoc littéral + sed pour éviter les expansions avant remplacement
  # et pour gérer proprement les slashs dans la valeur.
  
  cat <<'EOF' | sed "s/{{var}}/${input//\//\\/}/g; s/{{VAR}}/${upper//\//\\/}/g"

  {{var}}:
    image: {{var}}
    environment:
      - _JAVA_OPTIONS=-Xmx512m -Xms256m
      - SPRING_PROFILES_ACTIVE=prod,api-docs
      - MANAGEMENT_PROMETHEUS_METRICS_EXPORT_ENABLED=true
 
      - SPRING_LIQUIBASE_URL=jdbc:postgresql://postgresql:5432/paperdms
      - JHIPSTER_CACHE_REDIS_SERVER=redis://redis:6379/{{var}}
      - JHIPSTER_CACHE_REDIS_CLUSTER=false
      - SPRING_CLOUD_STREAM_KAFKA_BINDER_BROKERS=kafka:9092# Database Configuration
      - SPRING_DATASOURCE_URL=jdbc:postgresql://postgresql:5432/${{{VAR}}_DB_NAME:-paperdms}
      - SPRING_DATASOURCE_USERNAME=${{{VAR}}_DB_USER:-paperdms}
      - SPRING_DATASOURCE_PASSWORD=${{{VAR}}_DB_PASSWORD:-paperdms}
      
      # Redis Configuration
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
      - SPRING_REDIS_PASSWORD=${REDIS_PASSWORD:-}
      
      # Kafka Configuration
      - SPRING_KAFKA_BOOTSTRAP_SERVERS=kafka:9092
      
      # Elasticsearch Configuration
      - SPRING_ELASTICSEARCH_URIS=http://elasticsearch:9200
      
      # Consul Configuration
      - SPRING_CLOUD_CONSUL_HOST=consul
      - SPRING_CLOUD_CONSUL_PORT=8500
      
      # S3/MinIO Configuration
      - S3_BUCKET=${S3_BUCKET:-paperdms-documents}
      - S3_REGION=${S3_REGION:-us-east-1}
      - S3_ENDPOINT=${S3_ENDPOINT:-http://minio:9000}
      - S3_ACCESS_KEY=${S3_ACCESS_KEY:-minioadmin}
      - S3_SECRET_KEY=${S3_SECRET_KEY:-minioadmin}
      
      # Kafka Topics
      - KAFKA_DOCUMENT_EVENTS_TOPIC=${KAFKA_{{VAR}}_EVENTS_TOPIC:-paperdms.{{var}}.events}
      - KAFKA_SERVICE_STATUS_TOPIC=${KAFKA_{{VAR}}_STATUS_TOPIC:-paperdms.{{var}}.service-status}
      
      # JHipster Configuration
      - JHIPSTER_CACHE_REDIS_SERVER=redis://redis:6379
      - JHIPSTER_SECURITY_AUTHENTICATION_JWT_BASE64_SECRET=${JWT_SECRET:-NzhiYTRhNzdiNjc5ZDIzODNmMmYxODMyYTlhY2U4MWFkYTc0MmE0YmJlM2QyZTcyZTE0Y2UwODA4YjhmM2YwMTBjMjgxMTVhZjY4YjA1ODU5YWU3ZjMwNjNkZDM4NjkzMzQ2OWI1NDYzZGU2NTI5MjExNzhlNjEwYzUwY2NkYWM=}
      

    healthcheck:
      test:
        - CMD
        - curl
        - -f
        - http://localhost:8081/management/health
      interval: 5s
      timeout: 5s
      retries: 40
      
EOF


}

# --- Boucle sur toutes les valeurs ---
for val in "${ARGS[@]}"; do
  print_block "$val"
done