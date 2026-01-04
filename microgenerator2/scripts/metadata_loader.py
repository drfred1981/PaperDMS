#!/usr/bin/env python3
"""
Metadata Loader - Charge et fusionne les métadonnées depuis plusieurs fichiers
Permet le filtrage par catégorie, type, nom
"""

import sys
import yaml
from pathlib import Path
from typing import Dict, List, Any, Optional
import logging
from copy import deepcopy

logging.basicConfig(level=logging.INFO, format='%(asctime)s - %(levelname)s - %(message)s')
logger = logging.getLogger(__name__)


class MetadataLoader:
    """Charge et fusionne les métadonnées depuis plusieurs fichiers YAML"""
    
    def __init__(self, metadata_dir: str = './metadata'):
        self.metadata_dir = Path(metadata_dir)
        self.global_config = None
        self.infrastructure = {}
        self.services = {}
        self.all_metadata = {}
    
    def load_all(self):
        """Charge toutes les métadonnées"""
        logger.info(f"Chargement des métadonnées depuis {self.metadata_dir}")
        
        # 1. Charger la configuration globale
        self.load_global()
        
        # 2. Charger l'infrastructure
        self.load_infrastructure()
        
        # 3. Charger les services
        self.load_services()
        
        # 4. Fusionner tout
        self.merge_all()
        
        logger.info(f"✓ Métadonnées chargées: {len(self.infrastructure)} infra, {len(self.services)} services")
        
        return self.all_metadata
    
    def load_global(self):
        """Charge la configuration globale"""
        global_file = self.metadata_dir / 'global.yaml'
        
        if not global_file.exists():
            logger.warning(f"Fichier global non trouvé: {global_file}")
            self.global_config = {}
            return
        
        with open(global_file, 'r', encoding='utf-8') as f:
            self.global_config = yaml.safe_load(f)
        
        logger.info(f"✓ Configuration globale chargée")
    
    def load_infrastructure(self):
        """Charge tous les fichiers d'infrastructure"""
        infra_dir = self.metadata_dir / 'infrastructure'
        
        if not infra_dir.exists():
            logger.warning(f"Répertoire infrastructure non trouvé: {infra_dir}")
            return
        
        for yaml_file in infra_dir.glob('*.yaml'):
            with open(yaml_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)
                
                if not data:
                    continue
                
                # Enrichir avec la config globale
                data = self._enrich_with_global(data)
                
                name = data.get('name', yaml_file.stem)
                self.infrastructure[name] = data
                
                logger.info(f"  ✓ Infrastructure chargée: {name}")
    
    def load_services(self):
        """Charge tous les fichiers de services"""
        services_dir = self.metadata_dir / 'services'
        
        if not services_dir.exists():
            logger.warning(f"Répertoire services non trouvé: {services_dir}")
            return
        
        for yaml_file in services_dir.glob('*.yaml'):
            with open(yaml_file, 'r', encoding='utf-8') as f:
                data = yaml.safe_load(f)
                
                if not data:
                    continue
                
                # Enrichir avec la config globale
                data = self._enrich_with_global(data)
                
                name = data.get('name', yaml_file.stem)
                self.services[name] = data
                
                logger.info(f"  ✓ Service chargé: {name}")
    
    def _enrich_with_global(self, data: Dict) -> Dict:
        """Enrichit les métadonnées avec la config globale"""
        enriched = deepcopy(data)
        
        service_config = enriched.get('service', {})
        
        # Skip enrichment for standalone services (Zipkin, Kibana, Keycloak, etc.)
        if service_config.get('standalone', False):
            logger.debug(f"Skipping enrichment for standalone service: {enriched.get('name')}")
            return enriched
        
        # Ajouter le project info si absent
        if 'project' not in enriched and self.global_config:
            enriched['project'] = self.global_config.get('project', {})
        
        # Ajouter les dépendances communes si c'est un service
        if enriched.get('category') in ['service', 'infrastructure']:
            
            # Merge dependencies
            if 'dependencies' not in service_config and self.global_config:
                common_deps = self.global_config.get('commonDependencies', {})
                service_config['dependencies'] = []
                
                # Ajouter les dépendances Spring de base
                for dep_list in common_deps.values():
                    if isinstance(dep_list, list):
                        service_config['dependencies'].extend(dep_list)
                
                enriched['service'] = service_config
            
            # Ajouter les ressources par défaut si absentes
            if 'resources' not in service_config and self.global_config:
                default_resources = self.global_config.get('defaultResources', {})
                service_type = enriched.get('type', 'service')
                service_config['resources'] = default_resources.get(service_type, 
                                             default_resources.get('service', {}))
                enriched['service'] = service_config
            
            # Ajouter le health check par défaut
            if 'healthCheck' not in service_config and self.global_config:
                service_config['healthCheck'] = self.global_config.get('defaultHealthCheck', {})
                enriched['service'] = service_config
            
            # Ajouter packageName si absent
            if 'packageName' not in service_config and not service_config.get('standalone'):
                if self.global_config and 'project' in self.global_config:
                    base_package = self.global_config['project'].get('basePackage', 'com.example')
                    service_name = enriched.get('name', '').replace('-', '')
                    service_config['packageName'] = f"{base_package}.{service_name}"
                    enriched['service'] = service_config
        
        # Ajouter la config i18n globale
        if 'i18n' not in enriched and self.global_config:
            enriched['i18n'] = self.global_config.get('i18n', {})
        
        return enriched
    
    def merge_all(self):
        """Fusionne toutes les métadonnées en un seul dict"""
        self.all_metadata = {
            'global': self.global_config,
            'infrastructure': self.infrastructure,
            'services': self.services
        }
    
    def filter(self, 
               category: Optional[str] = None,
               type: Optional[str] = None,
               names: Optional[List[str]] = None,
               enabled_only: bool = True) -> Dict[str, Any]:
        """
        Filtre les métadonnées selon des critères
        
        Args:
            category: 'infrastructure', 'service', etc.
            type: 'discovery', 'gateway', 'business', 'security', etc.
            names: Liste de noms spécifiques
            enabled_only: Ne retourner que les éléments enabled=true
        
        Returns:
            Dict des métadonnées filtrées
        """
        filtered = {}
        
        # Combiner infrastructure et services pour le filtrage
        all_items = {}
        all_items.update(self.infrastructure)
        all_items.update(self.services)
        
        for name, metadata in all_items.items():
            # Filtrer par enabled
            if enabled_only and not metadata.get('enabled', True):
                continue
            
            # Filtrer par category
            if category and metadata.get('category') != category:
                continue
            
            # Filtrer par type
            if type and metadata.get('type') != type:
                continue
            
            # Filtrer par noms
            if names and name not in names:
                continue
            
            filtered[name] = metadata
        
        logger.info(f"Filtrage: {len(filtered)} éléments correspondent aux critères")
        return filtered
    
    def get_by_category(self, category: str) -> Dict[str, Any]:
        """Récupère toutes les métadonnées d'une catégorie"""
        return self.filter(category=category)
    
    def get_by_type(self, type: str) -> Dict[str, Any]:
        """Récupère toutes les métadonnées d'un type"""
        return self.filter(type=type)
    
    def get_by_name(self, name: str) -> Optional[Dict[str, Any]]:
        """Récupère les métadonnées d'un élément spécifique"""
        all_items = {}
        all_items.update(self.infrastructure)
        all_items.update(self.services)
        return all_items.get(name)
    
    def get_infrastructure_only(self) -> Dict[str, Any]:
        """Récupère uniquement l'infrastructure"""
        return self.filter(category='infrastructure')
    
    def get_business_services_only(self) -> Dict[str, Any]:
        """Récupère uniquement les services business"""
        return self.filter(category='service', type='business')
    
    def list_all_names(self) -> List[str]:
        """Liste tous les noms d'éléments"""
        all_items = {}
        all_items.update(self.infrastructure)
        all_items.update(self.services)
        return list(all_items.keys())
    
    def export_filtered(self, 
                       output_file: str,
                       category: Optional[str] = None,
                       type: Optional[str] = None,
                       names: Optional[List[str]] = None):
        """Exporte les métadonnées filtrées dans un fichier"""
        filtered = self.filter(category=category, type=type, names=names)
        
        output_path = Path(output_file)
        output_path.parent.mkdir(parents=True, exist_ok=True)
        
        with open(output_path, 'w', encoding='utf-8') as f:
            yaml.dump(filtered, f, default_flow_style=False, allow_unicode=True)
        
        logger.info(f"✓ Métadonnées exportées vers {output_file}")
    
    def to_legacy_format(self, items: Dict[str, Any]) -> Dict:
        """
        Convertit les métadonnées au format legacy (ancien format monolithique)
        Pour compatibilité avec les anciens générateurs
        """
        legacy = {
            'project': self.global_config.get('project', {}),
            'i18n': self.global_config.get('i18n', {}),
            'network': self.global_config.get('network', {}),
            'volumes': self.global_config.get('volumes', {}),
            'microservices': []
        }
        
        for name, metadata in items.items():
            if metadata.get('category') == 'service':
                # Convertir au format microservice
                microservice = {
                    'name': name,
                    'port': metadata.get('service', {}).get('port'),
                    'artifactId': metadata.get('service', {}).get('artifactId'),
                    'packageName': metadata.get('service', {}).get('packageName'),
                    'database': metadata.get('service', {}).get('database', {}),
                    'dependencies': metadata.get('service', {}).get('dependencies', []),
                    'frontend': metadata.get('frontend', {}),
                    'entities': metadata.get('entities', [])
                }
                legacy['microservices'].append(microservice)
        
        return legacy


def main():
    """Point d'entrée principal"""
    if len(sys.argv) < 2:
        print("Usage: python metadata_loader.py <command> [options]")
        print("")
        print("Commands:")
        print("  load                          - Charger toutes les métadonnées")
        print("  filter --category=<cat>       - Filtrer par catégorie")
        print("  filter --type=<type>          - Filtrer par type")
        print("  filter --names=<n1,n2>        - Filtrer par noms")
        print("  list                          - Lister tous les noms")
        print("  export --category=<cat> -o <file> - Exporter filtré")
        print("")
        print("Examples:")
        print("  python metadata_loader.py filter --category=infrastructure")
        print("  python metadata_loader.py filter --type=business")
        print("  python metadata_loader.py filter --names=user-service,product-service")
        sys.exit(1)
    
    loader = MetadataLoader('./metadata')
    loader.load_all()
    
    command = sys.argv[1]
    
    if command == 'load':
        print(yaml.dump(loader.all_metadata, default_flow_style=False))
    
    elif command == 'filter':
        category = None
        type_filter = None
        names = None
        
        for arg in sys.argv[2:]:
            if arg.startswith('--category='):
                category = arg.split('=')[1]
            elif arg.startswith('--type='):
                type_filter = arg.split('=')[1]
            elif arg.startswith('--names='):
                names = arg.split('=')[1].split(',')
        
        filtered = loader.filter(category=category, type=type_filter, names=names)
        print(yaml.dump(filtered, default_flow_style=False))
    
    elif command == 'list':
        names = loader.list_all_names()
        print("Éléments disponibles:")
        for name in sorted(names):
            metadata = loader.get_by_name(name)
            category = metadata.get('category', 'unknown')
            type_val = metadata.get('type', 'unknown')
            print(f"  - {name} ({category}/{type_val})")
    
    elif command == 'export':
        # Parse arguments
        category = None
        type_filter = None
        names = None
        output_file = 'metadata-export.yaml'
        
        for i, arg in enumerate(sys.argv[2:]):
            if arg.startswith('--category='):
                category = arg.split('=')[1]
            elif arg.startswith('--type='):
                type_filter = arg.split('=')[1]
            elif arg.startswith('--names='):
                names = arg.split('=')[1].split(',')
            elif arg == '-o' and i + 3 < len(sys.argv):
                output_file = sys.argv[i + 3]
        
        loader.export_filtered(output_file, category=category, type=type_filter, names=names)


if __name__ == '__main__':
    main()
