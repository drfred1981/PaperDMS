#!/usr/bin/env python3
"""
Microservices Generator V2
Utilise metadata_loader pour charger les métadonnées
Génère les microservices Spring Boot (skip standalone services)
"""

import os
import sys
import shutil
from pathlib import Path
from jinja2 import Environment, FileSystemLoader, Template
import logging

# Importer le metadata_loader
from metadata_loader import MetadataLoader

logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)


class MicroservicesGenerator:
    """Générateur de microservices utilisant metadata_loader"""
    
    def __init__(self, metadata_dir: str, template_dir: str, output_dir: str = './generated/services'):
        self.metadata_dir = Path(metadata_dir)
        self.output_dir = Path(output_dir)
        self.templates_dir = Path(template_dir)
        
        # Charger les métadonnées
        self.loader = MetadataLoader(metadata_dir)
        self.loader.load_all()
        
        # Configuration Jinja2
        self.jinja_env = Environment(
            loader=FileSystemLoader(str(self.templates_dir)),
            trim_blocks=True,
            lstrip_blocks=True
        )
     
        logger.info(f"Templates directory: {self.templates_dir}")
        logger.info(f"Output directory: {self.output_dir}")
        self.generate_all()

    def replacements_pattern(self, metadata: dict):
        replacements = {
                "{{PACKAGE}}": self.package_name_to_path(metadata) ,
                "{{APPLICATION_CAMEL}}": self._to_camel_case(metadata.get("name")) ,
                "APPLICATION_CAMEL": self._to_camel_case(metadata.get("name")) ,
                "PACKAGE": self.package_name_to_path(metadata) ,
                "PACKAGE_JAVA": self.package(metadata),
                ".tpl": ""
        }
        return replacements
 
    def replace_path_patterns(self, path: str, metadata: dict, replacements_pattern: dict = None) -> str:
        """
        Remplace des patterns dans un chemin selon une liste de tuples de remplacement.
        
        Args:
            path: Chemin à modifier
            metadata: Dictionnaire de métadonnées (peut être utilisé pour des remplacements dynamiques)
            
        Returns:
            Le chemin avec les remplacements effectués
        """
        if replacements_pattern is None:
            replacements_pattern =  self.replacements_pattern(metadata)
        
        corrected_path = path
        
        for left_pattern, right_pattern in replacements_pattern.items():        
            if left_pattern in corrected_path:
                corrected_path = corrected_path.replace(left_pattern, right_pattern)
        return corrected_path

    def package(self, metadata: dict):
        package =  metadata.get("project", {}).get("basePackage", "com.example")+"."+metadata.get("name")
        return package.replace("-", "").lower()

    def package_name_to_path(self, metadata: dict):
        package =  self.package(metadata)
        return package.replace(".", "/").lower()
    
    def generate_all(self):
        """Génère tous les microservices (skip standalone)"""
        logger.info("=== Génération des microservices ===")
        
        # Récupérer tous les services activés
        all_services = self.loader.filter(enabled_only=True)
        
        generated_count = 0
        skipped_count = 0
        
        for name, metadata in all_services.items():
            service_config = metadata.get('service', {})
            metadata["services"] = all_services
            # Skip services standalone
            if service_config.get('standalone', False):
                logger.info(f"⏭️  Skipping standalone service: {name}")
                skipped_count += 1
                continue
            
            # Skip shared et monitoring (pas de code à générer)
            if name in ['shared', 'monitoring']:
                logger.info(f"⏭️  Skipping infrastructure config: {name}")
                skipped_count += 1
                continue
            
            # Générer le service
            try:
                self.generate_service(name, metadata, "microservice")
                generated_count += 1
            except Exception as e:
                logger.error(f"❌ Error generating {name}: {e}")
                import traceback
                traceback.print_exc()
        
        logger.info(f"\n✅ Generation complete: {generated_count} services generated, {skipped_count} skipped")
    
    def generate_service(self, name: str, metadata: dict, path_microservice: str):
        """Génère un microservice Spring Boot"""
        logger.info(f"\n📦 Generating service: {name} {self.templates_dir}")
        

        replace_patterns = self.replacements_pattern(metadata)
        for left_pattern, right_pattern in replace_patterns.items():
            metadata[left_pattern] = right_pattern
            logger.info(f"{left_pattern} {right_pattern}")
        logger.debug(f"\n📦 metadata: {metadata}")

        self.generate_template_subpath(name, metadata, path_microservice +"/structure", replace_patterns)

        entities = metadata.get('entities', [])
        if not entities:
            logger.info(f"⏭️  No entities for service: {name}")
            skipped_count += 1            
        try:
            for entity in entities:
                self.generate_template_with_entity(name, metadata, path_microservice+"/entities", entity, replace_patterns)
        except Exception as e:
            logger.error(f"❌ Error generating entities for {name}: {e}")
            import traceback
            traceback.print_exc()

    
     
        
    def generate_template_with_entity(self, name: str, metadata: dict, path_microservice_structure: str, entity: dict, replace_patterns: dict):
        entity_name = entity.get("name")
        metadata["entity"] = entity
        self.generate_template_subpath(name, metadata, path_microservice_structure , replace_patterns)



    def generate_template_subpath(self, name: str, metadata: dict, path_microservice_structure: str, replace_patterns: dict):
        template_path = str(self.templates_dir)+"/"+path_microservice_structure
        for tpl_name, full_path, subpath in self.list_files_recursive_generator(template_path):
            logger.info(f"\n\n📦 Using template ( {tpl_name}, {full_path}, {subpath})")
            filename = tpl_name.replace(".tpl", "")
            output_file = str(self.output_dir)+"/"+name+"/"+subpath+"/"+filename
            output_file = output_file.replace("/./", "/")
            output_file = self.replace_path_patterns(output_file, metadata, replace_patterns)
            logger.info(f"\n\n📦 Output will be ({output_file})")
            self._render_template(path_microservice_structure+"/"+subpath +"/"+ tpl_name, Path(output_file), metadata)



    def list_files_recursive_generator(self, directory):
        """
        Liste tous les fichiers d'un répertoire de manière récursive.
        
        Args:
            directory: Chemin du répertoire à parcourir
            
        Returns:
            Liste de tuples (nom_fichier, chemin_complet, sous_repertoire_relatif)
        """
        base_path = Path(directory).resolve()
        results = []
        
        # rglob('*') est bien récursif par défaut
        for file_path in base_path.rglob('*'):
            if file_path.is_file():
                nom_fichier = file_path.name
                chemin_complet = str(file_path)
                sous_repertoire = str(file_path.parent.relative_to(base_path))
                
                results.append((nom_fichier, chemin_complet, sous_repertoire))
        
        return results

    
    def _render_template(self, template_name: str, output_file: Path, context: dict):
        """Rend un template Jinja2 et écrit le résultat dans un fichier"""
        try:
            template = self.jinja_env.get_template(template_name)
            content = template.render(**context)
            
            output_file.parent.mkdir(parents=True, exist_ok=True)
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(content)
            
            logger.debug(f"    Generated: {output_file.relative_to(self.output_dir)}")
        except Exception as e:
            logger.error(f"    Error rendering template {template_name}: {e}")
            raise
    
    def _render_string_template(self, template_str: str, output_file: Path, context: dict):
        """Rend un template string et écrit le résultat"""
        try:
            template = Template(template_str)
            content = template.render(**context)
            
            output_file.parent.mkdir(parents=True, exist_ok=True)
            with open(output_file, 'w', encoding='utf-8') as f:
                f.write(content)
            
            logger.debug(f"    Generated: {output_file.relative_to(self.output_dir)}")
        except Exception as e:
            logger.error(f"    Error rendering string template: {e}")
            raise
    
    def _to_camel_case(self, snake_str: str) -> str:
        """Convertit snake-case en CamelCase"""
        components = snake_str.split('-')
        return ''.join(x.title() for x in components)

def main():
    """Point d'entrée principal"""
    if len(sys.argv) < 3:
        print("Usage: python microservices_generator.py <metadata_dir> <template_dir> <output_dir>")
        print("\nExamples:")
        print("  python microservices_generator.py metadata/ templates/ generated/services/")
        print("  python microservices_generator.py ./metadata templates/ ./output")
        sys.exit(1)
    
    metadata_dir = sys.argv[1]
    template_dir = sys.argv[2]
    output_dir = sys.argv[3]
    
    if not os.path.exists(metadata_dir):
        print(f"Error: Metadata directory '{metadata_dir}' does not exist")
        sys.exit(1)
    
    if not os.path.exists(template_dir):
        print(f"Error: Template directory '{template_dir}' does not exist")
        sys.exit(1)
    
    try:
        generator = MicroservicesGenerator(metadata_dir, template_dir, output_dir)
        generator.generate_all()
    except Exception as e:
        logger.error(f"Fatal error: {e}")
        import traceback
        traceback.print_exc()
        sys.exit(1)


if __name__ == '__main__':
    main()
