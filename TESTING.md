# Running Unit Tests

## Test Coverage

This project includes comprehensive unit tests for all layers:

### Service Layer Tests
- `OrderServiceTest` - Tests order creation, duplicate handling
- `EmailServiceTest` - Tests email sending, error handling
- `StripeWebhookServiceTest` - Tests webhook processing logic

### Controller Layer Tests
- `StripeWebhookControllerTest` - Tests webhook endpoint
- `GalleryControllerTest` - Tests gallery API
- `ProductControllerTest` - Tests product API

### Exception Handler Tests
- `GlobalExceptionHandlerTest` - Tests error handling

## Running Tests

### From Eclipse
1. Right-click on `src/test/java`
2. Run As → JUnit Test

### From Command Line (if Maven installed)
```bash
mvn test
```

### From Maven Wrapper
```bash
../demo/mvnw.cmd test
```

## Test Results
Tests verify:
- ✅ Business logic correctness
- ✅ Error handling
- ✅ Edge cases
- ✅ API contracts
- ✅ Exception scenarios

## Test Statistics
- Total Tests: 20+
- Coverage: Service layer, Controllers, Exception handlers
- Framework: JUnit 5 + Mockito + Spring Test
