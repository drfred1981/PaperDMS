# PaperDMS - Testing Guide

## Testing Strategy

This document describes the comprehensive testing approach for the document upload feature, including unit tests, integration tests, end-to-end tests, and smoke tests.

## Test Coverage Requirements

- **Unit Test Coverage**: > 80%
- **Integration Test Coverage**: All API endpoints
- **End-to-End Tests**: All user workflows
- **Smoke Tests**: Critical production paths

## Unit Tests

### Running Unit Tests

```bash
cd documentService
mvn test

# Run specific test class
mvn test -Dtest=S3StorageServiceTest

# Run with coverage report
mvn test jacoco:report

# View coverage report
open target/site/jacoco/index.html
```

### Test Structure

#### S3StorageServiceTest

**Purpose**: Test S3 storage operations

**Test Cases**:
1. ✅ `calculateSha256_shouldReturnCorrectHash`
   - Verifies SHA-256 calculation correctness
   - Checks hash format (64 hex characters)

2. ✅ `calculateSha256_shouldBeIdempotent`
   - Ensures same input produces same hash
   - Tests idempotency

3. ✅ `uploadFile_shouldUploadSuccessfully`
   - Tests successful S3 upload
   - Verifies metadata storage

4. ✅ `uploadFile_shouldSkipIfFileExists`
   - Tests deduplication logic
   - Ensures no duplicate uploads

5. ✅ `fileExists_shouldReturnTrueWhenFileExists`
   - Tests file existence check
   - Verifies S3 HEAD request

6. ✅ `fileExists_shouldReturnFalseWhenFileDoesNotExist`
   - Tests non-existent file handling
   - Verifies proper exception handling

7. ✅ `deleteFile_shouldDeleteSuccessfully`
   - Tests file deletion
   - Verifies S3 DELETE request

8. ✅ `downloadFile_shouldDownloadSuccessfully`
   - Tests file download
   - Verifies data integrity

9. ✅ `uploadFile_shouldThrowExceptionOnS3Error`
   - Tests error handling
   - Verifies exception propagation

#### DocumentUploadServiceTest

**Purpose**: Test document upload workflow

**Test Cases**:
1. ✅ `uploadDocument_shouldUploadSuccessfully`
   - Tests complete upload workflow
   - Verifies event publishing

2. ✅ `uploadDocument_shouldReturnExistingDocumentIfSha256Matches`
   - Tests deduplication at service level
   - Verifies database query

3. ✅ `uploadDocument_shouldRejectEmptyFile`
   - Tests empty file validation
   - Verifies error message

4. ✅ `uploadDocument_shouldRejectNonPdfFile`
   - Tests file type validation
   - Verifies MIME type check

5. ✅ `uploadDocument_shouldRejectFileWithoutName`
   - Tests filename validation
   - Verifies error handling

6. ✅ `uploadDocument_shouldPublishFailureEventOnError`
   - Tests error event publishing
   - Verifies rollback handling

#### DocumentServiceStatusServiceTest

**Purpose**: Test service status tracking

**Test Cases**:
1. ✅ `updateServiceStatus_shouldUpdateSuccessfully`
   - Tests status update
   - Verifies database locking

2. ✅ `updateServiceStatus_shouldBeIdempotent`
   - Tests idempotent updates
   - Verifies no duplicate changes

3. ✅ `updateServiceStatusWithError_shouldRecordError`
   - Tests error recording
   - Verifies retry count increment

4. ✅ `markProcessingStarted_shouldSetStartTime`
   - Tests processing start tracking
   - Verifies timestamp recording

5. ✅ `markProcessingCompleted_shouldCalculateDuration`
   - Tests completion tracking
   - Verifies duration calculation

### Writing New Unit Tests

**Template**:
```java
@ExtendWith(MockitoExtension.class)
class MyServiceTest {

    @Mock
    private DependencyService dependencyService;

    private MyService myService;

    @BeforeEach
    void setUp() {
        myService = new MyService(dependencyService);
    }

    /**
     * Test: Feature should behave as expected.
     */
    @Test
    void methodName_shouldExpectedBehavior() {
        // Given
        when(dependencyService.method()).thenReturn(value);

        // When
        Result result = myService.methodUnderTest();

        // Then
        assertThat(result).isNotNull();
        verify(dependencyService, times(1)).method();
    }
}
```

## Integration Tests

### Running Integration Tests

```bash
cd documentService
mvn verify

# Run specific integration test
mvn verify -Dit.test=DocumentUploadResourceIT

# With test containers for real infrastructure
mvn verify -Pintegration-tests
```

### Integration Test Examples

#### DocumentUploadResourceIT

**Purpose**: Test REST API endpoints with real HTTP

**Setup**:
```java
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(replace = Replace.NONE)
class DocumentUploadResourceIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private DocumentRepository documentRepository;
}
```

**Test Cases**:
1. ✅ `uploadDocument_shouldReturn201Created`
   - Tests POST /api/documents/upload
   - Verifies HTTP 201 response
   - Checks database persistence

2. ✅ `uploadDocument_shouldReturnExistingDocumentOnDuplicate`
   - Tests deduplication via API
   - Verifies idempotent behavior

3. ✅ `uploadDocument_shouldReturn400ForInvalidFile`
   - Tests validation error handling
   - Verifies HTTP 400 response

4. ✅ `uploadBatch_shouldUploadMultipleFiles`
   - Tests batch upload endpoint
   - Verifies partial success handling

### Kafka Integration Tests

**Purpose**: Test event publishing and consumption

**Test Cases**:
```java
@SpringBootTest
@EmbeddedKafka(topics = {"paperdms.document.events"})
class KafkaIntegrationTest {

    @Test
    void uploadDocument_shouldPublishKafkaEvent() {
        // Upload document
        // Verify event consumed
    }
}
```

### Database Integration Tests

**Purpose**: Test database operations with real transactions

**Test Cases**:
```java
@DataJpaTest
class DocumentRepositoryIT {

    @Test
    void findBySha256_shouldReturnDocument() {
        // Test custom repository methods
    }
}
```

## End-to-End Tests

### Running E2E Tests

```bash
cd gateway
npm run e2e

# Run specific test suite
npm run e2e -- --spec=document-upload.e2e-spec.ts
```

### E2E Test Structure

#### document-upload.e2e-spec.ts

**Purpose**: Test complete user workflows in browser

**Test Cases**:
1. ✅ `should load upload page`
   - Navigates to upload page
   - Verifies page elements

2. ✅ `should upload single document`
   - Selects file
   - Monitors progress
   - Verifies success

3. ✅ `should upload multiple documents`
   - Tests batch upload
   - Verifies all files processed

4. ✅ `should show error for invalid file`
   - Tests validation
   - Verifies error message

5. ✅ `should calculate SHA-256 client-side`
   - Tests hash calculation
   - Verifies correct hash displayed

**Example**:
```typescript
describe('Document Upload', () => {
  beforeEach(() => {
    cy.visit('/document/upload');
  });

  it('should upload single document', () => {
    cy.get('input[type="file"]').attachFile('test.pdf');
    cy.get('select#documentType').select('Invoice');
    cy.get('button').contains('Upload All').click();
    cy.get('.status').should('contain', 'completed');
  });
});
```

## Smoke Tests

### Purpose

Smoke tests verify critical functionality in production-like environments.

### Running Smoke Tests

```bash
# Set environment
export BASE_URL=http://localhost:8080
export API_TOKEN=your_jwt_token

# Run smoke tests
./smoke-tests.sh
```

### Smoke Test Script

**smoke-tests.sh**:
```bash
#!/bin/bash

set -e

BASE_URL=${BASE_URL:-http://localhost:8080}
API_URL=${API_URL:-http://localhost:8081}

echo "Running PaperDMS Smoke Tests..."

# Test 1: Service Health
echo "✓ Testing service health..."
curl -f $API_URL/management/health || exit 1

# Test 2: Gateway Health
echo "✓ Testing gateway health..."
curl -f $BASE_URL/management/health || exit 1

# Test 3: Kafka Connectivity
echo "✓ Testing Kafka connectivity..."
docker exec paperdms-kafka \
  kafka-broker-api-versions \
  --bootstrap-server localhost:9092 || exit 1

# Test 4: S3 Connectivity
echo "✓ Testing S3 connectivity..."
docker exec paperdms-minio \
  mc ls /data/paperdms-documents || exit 1

# Test 5: Database Connectivity
echo "✓ Testing database connectivity..."
docker exec paperdms-postgresql \
  pg_isready -U paperdms || exit 1

# Test 6: Document Upload
echo "✓ Testing document upload..."
curl -f -X POST $API_URL/api/documents/upload \
  -H "Authorization: Bearer $API_TOKEN" \
  -F "file=@test.pdf" \
  -F "documentTypeId=1" || exit 1

echo "✓ All smoke tests passed!"
```

## Performance Tests

### Load Testing with Apache JMeter

**Setup**:
```bash
# Install JMeter
brew install jmeter  # macOS
apt-get install jmeter  # Ubuntu

# Run test plan
jmeter -n -t upload-load-test.jmx -l results.jtl
```

**Test Plan** (upload-load-test.jmx):
- **Thread Group**: 100 concurrent users
- **Ramp-up**: 10 seconds
- **Loop Count**: 10
- **HTTP Request**: POST /api/documents/upload
- **Assertions**: Response time < 3000ms, Status code = 201

**Metrics**:
- Average response time
- 95th percentile response time
- Throughput (requests/second)
- Error rate

### Stress Testing

**Purpose**: Find system breaking point

**Script**:
```bash
# Gradually increase load
for users in 10 50 100 200 500 1000; do
  echo "Testing with $users concurrent users..."
  jmeter -n -t stress-test.jmx \
    -Jusers=$users \
    -l results-$users.jtl
done
```

## Test Data Management

### Test Data Generation

**generate-test-pdfs.sh**:
```bash
#!/bin/bash

# Generate test PDFs of various sizes
for size in 100KB 1MB 10MB 50MB; do
  dd if=/dev/urandom of=test-$size.pdf bs=1024 count=$size
done

# Generate valid PDF with text
echo "%PDF-1.4
1 0 obj<</Type/Catalog/Pages 2 0 R>>endobj
2 0 obj<</Type/Pages/Kids[3 0 R]/Count 1>>endobj
3 0 obj<</Type/Page/MediaBox[0 0 612 792]/Parent 2 0 R/Resources<<>>>>endobj
xref
0 4
0000000000 65535 f
0000000010 00000 n
0000000053 00000 n
0000000102 00000 n
trailer<</Size 4/Root 1 0 R>>
startxref
178
%%EOF" > valid-test.pdf
```

### Test Database Reset

**reset-test-db.sh**:
```bash
#!/bin/bash

# Reset test database
docker exec paperdms-postgresql psql -U paperdms -c "
  TRUNCATE TABLE document CASCADE;
  TRUNCATE TABLE document_service_status CASCADE;
"

# Reset S3
docker exec paperdms-minio \
  mc rm --recursive --force /data/paperdms-documents/documents/
```

## Continuous Integration

### GitHub Actions Workflow

**.github/workflows/test.yml**:
```yaml
name: Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:15
        env:
          POSTGRES_PASSWORD: paperdms
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5
      
      redis:
        image: redis:7-alpine
        options: >-
          --health-cmd "redis-cli ping"
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v3
      
      - name: Set up JDK 17
        uses: actions/setup-java@v3
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Run unit tests
        run: mvn test
      
      - name: Run integration tests
        run: mvn verify
      
      - name: Upload coverage
        uses: codecov/codecov-action@v3
        with:
          files: ./target/site/jacoco/jacoco.xml
```

## Test Reporting

### Coverage Report

```bash
# Generate coverage report
mvn jacoco:report

# View HTML report
open target/site/jacoco/index.html

# Check coverage threshold
mvn jacoco:check
```

### Test Results

```bash
# View test results
cat target/surefire-reports/TEST-*.xml

# Generate HTML report
mvn surefire-report:report

# View report
open target/site/surefire-report.html
```

## Best Practices

### Test Naming Convention

```java
// Pattern: methodName_shouldExpectedBehavior_whenCondition
@Test
void uploadDocument_shouldReturnExisting_whenSha256Matches() { }

@Test
void calculateSha256_shouldBeIdempotent() { }
```

### Test Organization

```java
// Group related tests
@Nested
@DisplayName("Upload Validation Tests")
class UploadValidationTests {
    @Test
    void shouldRejectEmptyFile() { }
    
    @Test
    void shouldRejectNonPdfFile() { }
}
```

### Test Data Builders

```java
// Use builders for test data
class DocumentBuilder {
    public static Document aDocument() {
        return new Document()
            .title("test.pdf")
            .sha256("abc123...")
            .fileSize(1024L);
    }
}
```

### Assertions

```java
// Use AssertJ for fluent assertions
assertThat(result)
    .isNotNull()
    .extracting(Document::getSha256)
    .isEqualTo(expectedSha256);

// Use custom matchers
assertThat(document).hasValidSha256();
```

## Troubleshooting Tests

### Common Issues

1. **Flaky Tests**
   - Add proper waits for async operations
   - Use test containers for consistent environment
   - Reset state between tests

2. **Slow Tests**
   - Use @MockBean instead of real components
   - Disable autoconfiguration not needed
   - Use test profiles

3. **Test Isolation**
   - Use @DirtiesContext when needed
   - Clean up resources in @AfterEach
   - Use unique test data

---

For questions about testing, contact the development team.
