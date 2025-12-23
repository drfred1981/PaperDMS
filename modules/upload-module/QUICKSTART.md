# PaperDMS - Quick Start Guide

## 5-Minute Setup (Development)

This guide will get you up and running with the document upload feature in 5 minutes.

### Prerequisites Check

```bash
# Verify installations
java -version          # Should be 17+
node -version          # Should be 18+
docker --version       # Any recent version
mvn -version          # Should be 3.9+
```

### Step 1: Start Infrastructure (2 minutes)

```bash
# Start all infrastructure services
docker-compose up -d

# Wait for services to be healthy
docker-compose ps

# Expected output: All services should show "healthy" or "running"
```

### Step 2: Create S3 Bucket (30 seconds)

```bash
# Option 1: Use MinIO Web Console
# Navigate to http://localhost:9001
# Login: minioadmin / minioadmin
# Click "Create Bucket" → Name: paperdms-documents

# Option 2: Use MinIO CLI
docker exec paperdms-minio \
  mc mb /data/paperdms-documents
```

### Step 3: Install Shared Library (30 seconds)

```bash
cd paperdms-common
mvn clean install -DskipTests
cd ..
```

### Step 4: Start Services (2 minutes)

```bash
# Terminal 1 - Document Service
cd documentService
./mvnw spring-boot:run

# Terminal 2 - Gateway (wait for documentService to start)
cd gateway
./mvnw spring-boot:run
```

### Step 5: Test Upload (30 seconds)

```bash
# Open browser
http://localhost:8080

# Login
Username: admin
Password: admin

# Navigate to
http://localhost:8080/#/document/upload

# Drag and drop a PDF file
# Select document type
# Click "Upload All"
```

## Verify Installation

### Check Service Health

```bash
# Document Service
curl http://localhost:8081/management/health

# Gateway
curl http://localhost:8080/management/health

# Expected: {"status":"UP"}
```

### Check Kafka Events

```bash
# Install kafkacat if not already installed
# brew install kafkacat  # macOS
# apt-get install kafkacat  # Ubuntu

# Monitor events
kafkacat -C -b localhost:29092 \
  -t paperdms.document.events \
  -f '\nKey: %k\nValue: %s\n'

# Upload a document and watch events appear
```

### Check S3 Storage

```bash
# Using MinIO CLI
docker exec paperdms-minio \
  mc ls /data/paperdms-documents/documents/

# Using MinIO Web Console
http://localhost:9001
→ Browse → paperdms-documents → documents
```

### Check Database

```bash
# Connect to PostgreSQL
docker exec -it paperdms-postgresql psql -U paperdms

# Query documents
SELECT id, file_name, sha256, upload_date FROM document;

# Query service status
SELECT document_id, service_type, status FROM document_service_status;

# Exit
\q
```

## Common Issues & Solutions

### Issue: "Port already in use"

```bash
# Find and kill process using port 8080/8081
lsof -ti:8080 | xargs kill -9
lsof -ti:8081 | xargs kill -9
```

### Issue: "Cannot connect to S3"

```bash
# Check MinIO is running
docker ps | grep minio

# Restart MinIO
docker-compose restart minio

# Check logs
docker logs paperdms-minio
```

### Issue: "Kafka connection refused"

```bash
# Check Kafka is running
docker ps | grep kafka

# Restart Kafka and Zookeeper
docker-compose restart zookeeper kafka

# Wait 30 seconds for startup
sleep 30
```

### Issue: "Database connection failed"

```bash
# Check PostgreSQL is running
docker ps | grep postgresql

# Restart PostgreSQL
docker-compose restart postgresql

# Check logs
docker logs paperdms-postgresql
```

## Development Workflow

### Code Changes

```bash
# After modifying Java code
cd documentService
mvn clean package -DskipTests
./mvnw spring-boot:run

# After modifying Angular code
cd gateway
# Angular hot-reload is automatic
```

### View Logs

```bash
# Document Service logs
tail -f documentService/target/spring.log

# Gateway logs
tail -f gateway/target/spring.log

# Filter for specific component
grep "DocumentUploadService" documentService/target/spring.log
```

### Reset Environment

```bash
# Stop all services
docker-compose down

# Remove volumes (CAUTION: Deletes all data)
docker-compose down -v

# Restart from scratch
docker-compose up -d
```

## Production Deployment Checklist

- [ ] Configure production S3 bucket (not MinIO)
- [ ] Set up SSL/TLS for Kafka
- [ ] Configure PostgreSQL connection pooling
- [ ] Enable Redis persistence
- [ ] Set up monitoring (Prometheus/Grafana)
- [ ] Configure log aggregation (ELK stack)
- [ ] Set up automated backups
- [ ] Configure reverse proxy (nginx)
- [ ] Enable CORS restrictions
- [ ] Set up CDN for static assets
- [ ] Configure rate limiting
- [ ] Set up health check monitoring
- [ ] Enable database SSL
- [ ] Configure secrets management
- [ ] Set up CI/CD pipeline

## Next Steps

### Explore Features

1. **Upload multiple files**: Test batch upload
2. **Check deduplication**: Upload same file twice
3. **Monitor events**: Watch Kafka events in real-time
4. **Test error handling**: Upload invalid file
5. **Check service status**: Query DocumentServiceStatus

### Integrate Other Services

1. **OCR Service**: Subscribe to DOCUMENT_READY_FOR_OCR events
2. **AI Service**: Subscribe to OCR_COMPLETED events
3. **Search Service**: Subscribe to DOCUMENT_UPLOADED events
4. **Transform Service**: Generate previews and thumbnails

### Customize

1. **Change file size limit**: Modify `application.yml`
2. **Add custom validation**: Extend `DocumentUploadService`
3. **Custom S3 key format**: Modify `S3StorageService`
4. **Add metadata extraction**: Extend upload workflow

## Resources

- **Full Documentation**: See `README.md`
- **Architecture**: See `ARCHITECTURE.md`
- **API Reference**: http://localhost:8080/swagger-ui/
- **Kafka UI**: http://localhost:8090
- **MinIO Console**: http://localhost:9001
- **Consul UI**: http://localhost:8500

## Support

For issues or questions:
1. Check logs for error messages
2. Review troubleshooting section in README.md
3. Check GitHub issues (if applicable)
4. Contact development team

---

Happy coding! 🚀
