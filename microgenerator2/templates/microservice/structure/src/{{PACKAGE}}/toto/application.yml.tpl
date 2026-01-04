spring:
  application:
    name: {{name}}
  profiles:
    # Profils actifs - Charge automatiquement les fichiers application-{profile}.yml
    active: {{active_profiles}}
    # Groupes de profils pour charger plusieurs configurations ensemble
    group:
      dev:
        - common
        - database
        - eureka
        - logging
      prod:
        - common
        - database
        - eureka
        - logging
        - security
      test:
        - common
        - database
        - logging

# Configuration de base (toujours chargée)
server:
  port: {{port}}

# Les autres configurations sont dans les fichiers séparés :
# - application-common.yml     : Configuration commune
# - application-database.yml   : Configuration base de données
# - application-eureka.yml     : Configuration Eureka
# - application-logging.yml    : Configuration logs
# - application-security.yml   : Configuration sécurité (prod)
{{elasticsearch_config}}
