# JHipster generated Docker-Compose configuration

## Usage

Launch all your infrastructure by running: `docker compose up -d`.

## Configured Docker services

### Service registry and configuration server:

- [Consul](http://localhost:8500)

### Applications and dependencies:

- gateway (gateway application)
- gateway's postgresql database
- gateway's elasticsearch search engine
- documentService (microservice application)
- documentService's postgresql database
- documentService's elasticsearch search engine
- ocrService (microservice application)
- ocrService's postgresql database
- ocrService's elasticsearch search engine

### Additional Services:

- Kafka
- [Prometheus server](http://localhost:9090)
- [Prometheus Alertmanager](http://localhost:9093)
- [Grafana](http://localhost:3000)
