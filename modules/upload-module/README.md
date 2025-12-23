# PaperDMS - Document Upload Feature

## Overview

This package contains the complete implementation of the PDF document upload feature for PaperDMS, a comprehensive Document Management System built with JHipster microservices architecture.

### Key Features

- **Drag-and-Drop Upload**: Modern Angular interface with drag-and-drop support
- **SHA-256 Deduplication**: Automatic deduplication based on file content hash
- **S3 Storage**: Scalable storage using AWS S3 or S3-compatible storage (MinIO)
- **Kafka Event Publishing**: Real-time event streaming for microservices integration
- **Service Status Tracking**: Comprehensive status tracking across all processing stages
- **Database Locking**: Pessimistic locking to prevent concurrent document processing
- **Idempotent Operations**: All operations are idempotent and retry-safe
- **Multi-language Support**: Fully internationalized (English)
- **Comprehensive Testing**: Unit tests, integration tests, and smoke tests included

## Architecture

### Microservices

1. **Gateway** (Port 8080)
   - Angular frontend
   - API gateway for routing requests
   - WebSocket support for real-time updates

2. **documentService** (Port 8081)
   - Document upload and management
   - S3 storage integration
   - Kafka event publishing
   - Document service status tracking

3. **Shared Library: paperdms-common**
   - Kafka event DTOs
   - Shared enums and constants
   - Used by all microservices

### Technology Stack

- **Backend**: Spring Boot 3.x, JHipster 8.x
- **Frontend**: Angular 17+
- **Database**: PostgreSQL 15+
- **Cache**: Redis 7+
- **Messaging**: Apache Kafka 3.x
- **Storage**: AWS S3 / MinIO
- **Search**: Elasticsearch 8.x
- **Service Discovery**: Consul
- **Build Tool**: Maven 3.9+

## Installation Guide

### Prerequisites

1. **Java Development Kit (JDK) 17+**
   ```bash
   java -version
   ```

2. **Node.js 18+ and npm**
   ```bash
   node --version
   npm --version
   ```

3. **Docker and Docker Compose**
   ```bash
   docker --version
   docker-compose --version
   ```

4. **Maven 3.9+**
   ```bash
   mvn --version
   ```

### Step 1: Extract and Prepare the Project

```bash
# Extract the JHipster-generated ZIP file
unzip paperdms-generated.zip
cd paperdms

# Copy the upload feature files into the appropriate locations
# (See file structure below for exact locations)
```

### Step 2: Install the Shared Library

```bash
# Navigate to the shared library
cd paperdms-common

# Install to local Maven repository
mvn clean install

# This makes the library available to all microservices
```

### Step 3: Add Dependency to Document Service

Add the following to `documentService/pom.xml` in the dependencies section:

```xml
<dependency>
    <groupId>fr.smartprod.paperdms</groupId>
    <artifactId>paperdms-common</artifactId>
    <version>1.0.0-SNAPSHOT</version>
</dependency>

<dependency>
    <groupId>software.amazon.awssdk</groupId>
    <artifactId>s3</artifactId>
    <version>2.20.0</version>
</dependency>

<dependency>
    <groupId>org.apache.pdfbox</groupId>
    <artifactId>pdfbox</artifactId>
    <version>3.0.0</version>
</dependency>
```

### Step 4: Copy Implementation Files

#### Backend Files (documentService)

```bash
# Service classes
cp -r documentService/src/main/java/fr/smartprod/paperdms/document/service/* \
   paperdms/documentService/src/main/java/fr/smartprod/paperdms/document/service/

# Configuration classes
cp -r documentService/src/main/java/fr/smartprod/paperdms/document/config/* \
   paperdms/documentService/src/main/java/fr/smartprod/paperdms/document/config/

# REST controller
cp -r documentService/src/main/java/fr/smartprod/paperdms/document/web/rest/* \
   paperdms/documentService/src/main/java/fr/smartprod/paperdms/document/web/rest/

# Repository
cp documentService/src/main/java/fr/smartprod/paperdms/document/repository/DocumentServiceStatusRepository.java \
   paperdms/documentService/src/main/java/fr/smartprod/paperdms/document/repository/

# Configuration
cp documentService/src/main/resources/config/application.yml \
   paperdms/documentService/src/main/resources/config/application.yml
```

#### Frontend Files (gateway)

```bash
# Angular component
cp -r gateway/src/main/webapp/app/entities/document/upload/* \
   paperdms/gateway/src/main/webapp/app/entities/document/upload/

# Routes
cp gateway/src/main/webapp/app/entities/document/document.routes.ts \
   paperdms/gateway/src/main/webapp/app/entities/document/
```

#### Test Files

```bash
# Unit tests
cp -r documentService/src/test/java/fr/smartprod/paperdms/document/service/* \
   paperdms/documentService/src/test/java/fr/smartprod/paperdms/document/service/
```

### Step 5: Start Infrastructure Services

```bash
# Start PostgreSQL, Redis, Kafka, Elasticsearch, Consul, MinIO
cd paperdms
docker-compose up -d

# Wait for all services to be healthy
docker-compose ps
```

### Step 6: Configure Environment Variables

Create `.env` file in the documentService directory:

```bash
# S3 Configuration (MinIO for local development)
S3_BUCKET=paperdms-documents
S3_REGION=us-east-1
S3_ENDPOINT=http://localhost:9000
S3_ACCESS_KEY=minioadmin
S3_SECRET_KEY=minioadmin

# Kafka Configuration
KAFKA_BOOTSTRAP_SERVERS=localhost:9092
KAFKA_DOCUMENT_EVENTS_TOPIC=paperdms.document.events
KAFKA_SERVICE_STATUS_TOPIC=paperdms.document.service-status

# Database Configuration (already in application.yml)
# Redis Configuration (already in application.yml)
```

### Step 7: Create S3 Bucket in MinIO

```bash
# Access MinIO console at http://localhost:9001
# Login: minioadmin / minioadmin
# Create bucket: paperdms-documents

# Or use MinIO client:
mc alias set local http://localhost:9000 minioadmin minioadmin
mc mb local/paperdms-documents
```

### Step 8: Build and Run Services

#### Build Document Service

```bash
cd documentService
mvn clean package -DskipTests
```

#### Build Gateway

```bash
cd ../gateway
mvn clean package -DskipTests
```

#### Start Services

```bash
# Terminal 1: Start documentService
cd documentService
./mvnw spring-boot:run

# Terminal 2: Start gateway
cd gateway
./mvnw spring-boot:run
```

### Step 9: Verify Installation

1. **Access the application**: http://localhost:8080
2. **Login** with default credentials (admin/admin)
3. **Navigate to Document Upload**: http://localhost:8080/#/document/upload
4. **Test upload**:
   - Drag and drop a PDF file
   - Select document type
   - Click "Upload All"
   - Verify successful upload

## File Structure

```
paperdms-upload-feature/
├── paperdms-common/                     # Shared library
│   ├── pom.xml
│   └── src/main/java/fr/smartprod/paperdms/common/event/
│       ├── DocumentEventType.java
│       ├── DocumentEvent.java
│       ├── DocumentUploadEvent.java
│       └── DocumentServiceStatusEvent.java
│
├── documentService/                     # Document microservice
│   ├── src/main/java/fr/smartprod/paperdms/document/
│   │   ├── config/
│   │   │   ├── S3Configuration.java
│   │   │   └── KafkaConfiguration.java
│   │   ├── repository/
│   │   │   └── DocumentServiceStatusRepository.java
│   │   ├── service/
│   │   │   ├── S3StorageService.java
│   │   │   ├── DocumentEventPublisher.java
│   │   │   ├── DocumentServiceStatusService.java
│   │   │   └── DocumentUploadService.java
│   │   └── web/rest/
│   │       └── DocumentUploadResource.java
│   ├── src/main/resources/config/
│   │   └── application.yml
│   └── src/test/java/fr/smartprod/paperdms/document/service/
│       ├── S3StorageServiceTest.java
│       └── DocumentUploadServiceTest.java
│
└── gateway/                              # Gateway with Angular frontend
    └── src/main/webapp/app/entities/document/
        ├── upload/
        │   ├── document-upload.component.ts
        │   ├── document-upload.component.html
        │   └── document-upload.component.scss
        └── document.routes.ts
```

## Configuration Reference

### application.yml (documentService)

```yaml
spring:
  kafka:
    bootstrap-servers: ${KAFKA_BOOTSTRAP_SERVERS:localhost:9092}
  servlet:
    multipart:
      max-file-size: 100MB
      max-request-size: 100MB

paperdms:
  s3:
    bucket: ${S3_BUCKET:paperdms-documents}
    region: ${S3_REGION:us-east-1}
    prefix: ${S3_PREFIX:documents/}
    endpoint: ${S3_ENDPOINT:}  # For MinIO
  kafka:
    topic:
      document-events: paperdms.document.events
      service-status: paperdms.document.service-status
```

## API Endpoints

### Upload Single Document

```http
POST /api/documents/upload
Content-Type: multipart/form-data

Parameters:
- file: PDF file (required)
- documentTypeId: Long (required)
- folderId: Long (optional)

Response: 201 Created
{
  "id": 1,
  "title": "document.pdf",
  "fileName": "document.pdf",
  "fileSize": 1024000,
  "sha256": "abc123...",
  "uploadDate": "2025-01-01T12:00:00Z",
  ...
}
```

### Upload Multiple Documents

```http
POST /api/documents/upload/batch
Content-Type: multipart/form-data

Parameters:
- files[]: PDF files array (required)
- documentTypeId: Long (required)
- folderId: Long (optional)

Response: 200 OK / 206 Partial Content
[
  { "id": 1, ... },
  { "id": 2, ... },
  null  // Failed upload
]
```

## Kafka Events

### Document Upload Events

**Topic**: `paperdms.document.events`

**Event Types**:
- `DOCUMENT_UPLOAD_STARTED`
- `DOCUMENT_UPLOADED`
- `DOCUMENT_UPLOAD_FAILED`
- `DOCUMENT_READY_FOR_OCR`

**Event Structure**:
```json
{
  "eventId": "uuid",
  "eventType": "DOCUMENT_UPLOADED",
  "documentId": 1,
  "sha256": "abc123...",
  "timestamp": "2025-01-01T12:00:00Z",
  "sourceService": "documentService",
  "userId": "admin",
  "fileName": "document.pdf",
  "fileSize": 1024000,
  "mimeType": "application/pdf",
  "s3Key": "documents/ab/c1/abc123.pdf",
  "s3Bucket": "paperdms-documents",
  "pageCount": 5
}
```

### Service Status Events

**Topic**: `paperdms.document.service-status`

**Event Structure**:
```json
{
  "eventId": "uuid",
  "eventType": "DOCUMENT_SERVICE_STATUS_CHANGED",
  "documentId": 1,
  "serviceType": "OCR_SERVICE",
  "status": "PENDING",
  "timestamp": "2025-01-01T12:00:00Z"
}
```

## Testing

### Run Unit Tests

```bash
cd documentService
mvn test
```

### Run Integration Tests

```bash
cd documentService
mvn verify
```

### Smoke Tests

```bash
# Upload a test document
curl -X POST http://localhost:8081/api/documents/upload \
  -H "Authorization: Bearer $TOKEN" \
  -F "file=@test.pdf" \
  -F "documentTypeId=1"

# Verify Kafka events
kafka-console-consumer --bootstrap-server localhost:9092 \
  --topic paperdms.document.events \
  --from-beginning

# Check S3 storage
mc ls local/paperdms-documents/documents/
```

## Monitoring and Logging

### Application Logs

```bash
# documentService logs
tail -f documentService/target/logs/application.log

# View specific log levels
grep "ERROR" documentService/target/logs/application.log
grep "DocumentUploadService" documentService/target/logs/application.log
```

### Metrics

Access metrics at:
- Document Service: http://localhost:8081/management/metrics
- Gateway: http://localhost:8080/management/metrics

### Health Checks

- Document Service: http://localhost:8081/management/health
- Gateway: http://localhost:8080/management/health

## Troubleshooting

### Upload Fails with "S3 error"

**Solution**: Check MinIO is running and accessible
```bash
docker-compose ps minio
curl http://localhost:9000/minio/health/live
```

### Kafka Events Not Publishing

**Solution**: Verify Kafka is running and topics exist
```bash
docker-compose ps kafka
kafka-topics --bootstrap-server localhost:9092 --list
```

### Database Lock Timeout

**Solution**: Check for long-running transactions
```sql
SELECT * FROM pg_locks WHERE NOT granted;
SELECT * FROM pg_stat_activity WHERE state != 'idle';
```

### PDF Page Count Extraction Fails

**Solution**: Ensure PDFBox dependency is included and PDF is valid
```bash
mvn dependency:tree | grep pdfbox
```

## Production Deployment

### Environment Configuration

```yaml
# Production application.yml
spring:
  kafka:
    bootstrap-servers: kafka-prod:9092
    ssl:
      enabled: true

paperdms:
  s3:
    bucket: paperdms-prod-documents
    region: eu-west-1
    # Use IAM roles instead of access keys
```

### Security Considerations

1. **Use IAM roles** for S3 access in production
2. **Enable SSL/TLS** for Kafka connections
3. **Configure PostgreSQL SSL**
4. **Use secrets management** (Vault, AWS Secrets Manager)
5. **Enable CORS** restrictions on the gateway

### Performance Tuning

```yaml
# Optimize for high-volume uploads
spring:
  servlet:
    multipart:
      max-file-size: 500MB
      max-request-size: 500MB
  
  kafka:
    producer:
      batch-size: 32768
      linger-ms: 10
      compression-type: lz4

paperdms:
  upload:
    max-concurrent-uploads: 10
    chunk-size: 5242880  # 5MB chunks
```

## Support and Maintenance

### Log Levels

Adjust in `application.yml`:
```yaml
logging:
  level:
    fr.smartprod.paperdms: DEBUG
    org.springframework.kafka: WARN
    software.amazon.awssdk: ERROR
```

### Backup Strategy

1. **PostgreSQL**: Daily automated backups
2. **S3**: Enable versioning and lifecycle policies
3. **Kafka**: Configure retention policies

## Contributing

### Code Style

- Follow JHipster conventions
- Use Javadoc for all public methods
- Write multi-level logs (DEBUG, INFO, WARN, ERROR)
- No code comments, only logs
- All code, logs, and documentation in English

### Testing Requirements

- Unit test coverage > 80%
- Integration tests for all endpoints
- Smoke tests for critical workflows

## License

Copyright © 2025 SmartProd. All rights reserved.

---

For questions or issues, please contact the PaperDMS development team.
