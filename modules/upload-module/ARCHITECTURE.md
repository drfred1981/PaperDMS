# PaperDMS Architecture Overview

## System Architecture

```
┌─────────────────────────────────────────────────────────────────┐
│                        CLIENT BROWSER                            │
│                     (Angular Application)                        │
└────────────────────────┬────────────────────────────────────────┘
                         │ HTTP/WebSocket
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                    GATEWAY SERVICE (8080)                        │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Angular    │  │     API      │  │  WebSocket   │          │
│  │   Frontend   │  │   Gateway    │  │   Support    │          │
│  └──────────────┘  └──────────────┘  └──────────────┘          │
└────────────────────────┬────────────────────────────────────────┘
                         │ HTTP
                         ▼
┌─────────────────────────────────────────────────────────────────┐
│                 DOCUMENT SERVICE (8081)                          │
│  ┌──────────────────────────────────────────────────┐           │
│  │         DocumentUploadResource (REST API)        │           │
│  └─────────────────────┬────────────────────────────┘           │
│                        │                                         │
│  ┌─────────────────────▼────────────────────────────┐           │
│  │         DocumentUploadService (Business)         │           │
│  └──┬──────────────┬───────────────┬────────────────┘           │
│     │              │               │                             │
│  ┌──▼──────────┐ ┌─▼──────────┐ ┌─▼─────────────────┐          │
│  │ S3Storage   │ │  Document  │ │ DocumentService   │          │
│  │   Service   │ │    Event   │ │  StatusService    │          │
│  │             │ │  Publisher │ │                   │          │
│  └──┬──────────┘ └─┬──────────┘ └─┬─────────────────┘          │
│     │              │               │                             │
└─────┼──────────────┼───────────────┼─────────────────────────────┘
      │              │               │
      │              │               │
      ▼              ▼               ▼
┌──────────┐  ┌──────────┐  ┌──────────────┐
│   S3/    │  │  Apache  │  │  PostgreSQL  │
│  MinIO   │  │  Kafka   │  │   Database   │
└──────────┘  └──────────┘  └──────────────┘
```

## Component Flow

### Upload Workflow

```
1. User Action
   ↓
2. Angular Component
   • Calculate SHA-256 client-side
   • Display progress
   ↓
3. DocumentUploadResource
   • Validate request
   • Authentication
   ↓
4. DocumentUploadService
   • Validate file (PDF only)
   • Calculate SHA-256 server-side
   • Check for duplicates
   ↓
5. S3StorageService
   • Upload to S3 with deduplication
   • Generate hierarchical key
   ↓
6. Database Operations
   • Save Document entity
   • Create DocumentServiceStatus
   • Pessimistic locking
   ↓
7. DocumentEventPublisher
   • Publish DOCUMENT_UPLOADED event
   • Publish DOCUMENT_READY_FOR_OCR event
   • Publish SERVICE_STATUS_CHANGED event
   ↓
8. Kafka Topics
   • paperdms.document.events
   • paperdms.document.service-status
   ↓
9. Downstream Services (subscribe to events)
   • ocrService → Process OCR
   • aiService → Auto-tagging
   • searchService → Index document
   • transformService → Generate previews
```

## Event Flow Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                      DOCUMENT LIFECYCLE                           │
└──────────────────────────────────────────────────────────────────┘

UPLOAD_STARTED
     │
     ├─→ SHA-256 Calculation
     │
     ├─→ Deduplication Check
     │
     ├─→ S3 Upload
     │
     ▼
DOCUMENT_UPLOADED ────────┬─────────────────────────────────────┐
     │                    │                                     │
     │                    │                                     │
     ▼                    ▼                                     ▼
READY_FOR_OCR     READY_FOR_PREVIEW              READY_FOR_INDEXING
     │                    │                                     │
     │                    │                                     │
     ▼                    ▼                                     ▼
OCR_COMPLETED      PREVIEW_GENERATED                   INDEXED
     │                    │                                     │
     │                    │                                     │
     ▼                    ▼                                     ▼
READY_FOR_AI_TAG  THUMBNAIL_GENERATED         SEMANTIC_ANALYZED
     │                    │                                     │
     │                    │                                     │
     ▼                    ▼                                     ▼
AI_TAG_COMPLETED   ───────┴─────────────────────────────────────┤
     │                                                          │
     └──────────────────────────────────────────────────────────┤
                                                                │
                                                                ▼
                                               PROCESSING_COMPLETED
```

## Service Status Tracking

Each service tracks its processing status in the `DocumentServiceStatus` table:

```
DocumentServiceStatus
├── documentId (FK to Document)
├── serviceType (OCR_SERVICE, AI_SERVICE, etc.)
├── status (PENDING, IN_PROGRESS, COMPLETED, FAILED)
├── statusDetails (additional information)
├── errorMessage (if failed)
├── retryCount
├── processingStartDate
├── processingEndDate
├── processingDuration
└── jobId (reference to processing job)
```

**Locking Strategy:**
- Pessimistic locking prevents concurrent updates
- Each service instance acquires lock before processing
- Lock released after status update
- Prevents duplicate processing

## Data Storage

### PostgreSQL Schema

```sql
-- Document table
CREATE TABLE document (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(500) NOT NULL,
    file_name VARCHAR(500) NOT NULL,
    file_size BIGINT NOT NULL,
    mime_type VARCHAR(100) NOT NULL,
    sha256 VARCHAR(64) NOT NULL UNIQUE,
    s3_key VARCHAR(1000) NOT NULL,
    s3_bucket VARCHAR(255) NOT NULL,
    s3_region VARCHAR(50),
    s3_etag VARCHAR(100),
    thumbnail_s3_key VARCHAR(1000),
    webp_preview_s3_key VARCHAR(1000),
    upload_date TIMESTAMP NOT NULL,
    page_count INTEGER,
    created_date TIMESTAMP NOT NULL,
    created_by VARCHAR(50) NOT NULL,
    -- ... other fields
);

-- Service status tracking table
CREATE TABLE document_service_status (
    id BIGSERIAL PRIMARY KEY,
    document_id BIGINT NOT NULL REFERENCES document(id),
    service_type VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    status_details TEXT,
    error_message TEXT,
    retry_count INTEGER,
    processing_start_date TIMESTAMP,
    processing_end_date TIMESTAMP,
    processing_duration BIGINT,
    job_id VARCHAR(100),
    updated_by VARCHAR(50),
    updated_date TIMESTAMP NOT NULL,
    UNIQUE(document_id, service_type)
);

-- Index for performance
CREATE INDEX idx_document_sha256 ON document(sha256);
CREATE INDEX idx_service_status_lookup ON document_service_status(document_id, service_type);
```

### S3 Storage Structure

```
s3://paperdms-documents/
└── documents/
    └── [first 2 chars of SHA-256]/
        └── [next 2 chars]/
            └── [full SHA-256].[extension]

Example:
documents/ab/c1/abc123def456...xyz.pdf
documents/ab/c1/abc123def456...xyz.webp (preview)
documents/ab/c1/abc123def456...xyz_thumb.jpg (thumbnail)
```

## Security Considerations

### Authentication & Authorization
- JWT tokens for API access
- Role-based access control (RBAC)
- Document-level permissions

### Data Security
- SHA-256 for file integrity
- S3 encryption at rest
- HTTPS for all communications
- Kafka SSL/TLS in production

### Audit Trail
- All operations logged in `DocumentAudit` table
- User IP tracking
- Timestamp for all actions

## Scalability

### Horizontal Scaling
- Multiple instances of each microservice
- Load balancing via gateway
- Database connection pooling
- Kafka consumer groups

### Performance Optimization
- Redis caching for frequently accessed data
- S3 deduplication reduces storage
- Elasticsearch for fast search
- Async event processing via Kafka

### Resource Management
- Configurable file size limits
- Concurrent upload limits
- Kafka partition strategy
- Database connection limits

## Monitoring & Observability

### Metrics (Actuator)
- Upload throughput
- Processing duration
- Error rates
- S3 operations
- Kafka lag

### Health Checks
- Service health endpoints
- Database connectivity
- S3 availability
- Kafka connectivity

### Logging
- Structured JSON logs
- Multi-level logging (DEBUG, INFO, WARN, ERROR)
- Correlation IDs for request tracking
- Centralized log aggregation ready

## Future Enhancements

### Planned Features
1. **Chunked Upload**: For very large files (>100MB)
2. **Resume Support**: Resume interrupted uploads
3. **Thumbnail Generation**: Automatic thumbnail creation
4. **WebP Preview**: Generate WebP previews for web display
5. **Virus Scanning**: Integrate antivirus scanning
6. **Watermarking**: Apply watermarks to documents
7. **Digital Signatures**: Support for signed documents
8. **Version Control**: Enhanced version management

### Integration Points
- OCR Service: Text extraction from PDFs
- AI Service: Automatic tagging and classification
- Search Service: Full-text search indexing
- Transform Service: PDF manipulation
- Workflow Service: Document approval workflows
- Business Doc Service: Invoice/contract processing

---

This architecture provides a solid foundation for a scalable, enterprise-grade document management system with comprehensive event-driven integration capabilities.
