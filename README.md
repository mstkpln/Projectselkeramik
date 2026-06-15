# ProjectSelkeramik - Enterprise Spring Boot Application

A professionally refactored e-commerce application with Stripe payment integration, following enterprise-level architecture and best practices.

## 🏗️ Architecture

This application follows a **layered architecture** with clear separation of concerns:

```
src/main/java/com/example/demo/
├── controller/          # HTTP layer (REST endpoints)
├── service/            # Business logic layer
├── repository/         # Data access layer
├── model/
│   ├── entity/        # JPA entities
│   └── dto/           # Data Transfer Objects
├── config/            # Configuration classes
└── exception/         # Custom exceptions & handlers
```

## ✨ Key Improvements Over Original

### Security
- ✅ Environment variables for sensitive data (no hardcoded credentials)
- ✅ Proper input sanitization before logging
- ✅ Sensitive data masking in logs
- ✅ Webhook signature verification

### Architecture
- ✅ **3-layer architecture**: Controller → Service → Repository
- ✅ **SOLID principles** compliance
- ✅ **Dependency Injection** throughout
- ✅ **DTOs** for data transfer
- ✅ **Global exception handling** with @RestControllerAdvice

### Code Quality
- ✅ SLF4J logging instead of System.out.println
- ✅ Specific exception handling (no generic catch blocks)
- ✅ Lombok for boilerplate reduction
- ✅ Transaction management with @Transactional
- ✅ Duplicate order prevention

## 🚀 Getting Started

### Prerequisites
- Java 17+
- Maven 3.6+

### Environment Variables

Create a `.env` file or set these environment variables:

```bash
STRIPE_SECRET_KEY=sk_test_your_key_here
STRIPE_WEBHOOK_SECRET=whsec_your_secret_here
EMAIL_USERNAME=your-email@gmail.com
EMAIL_PASSWORD=your-app-password
```

### Running the Application

```bash
mvn clean install
mvn spring-boot:run
```

Application will start on `http://localhost:8080`

## 📋 API Endpoints

### Webhook Endpoint
```
POST /api/stripe/webhook
Headers: Stripe-Signature
Body: Raw webhook payload
```

## 🗄️ Database

- **Development**: H2 in-memory database
- **Console**: http://localhost:8080/h2-console
- **JDBC URL**: jdbc:h2:mem:testdb

## 📦 Project Structure

### Controllers
- `StripeWebhookController` - Handles HTTP requests only

### Services
- `StripeWebhookService` - Webhook processing business logic
- `OrderService` - Order management business logic
- `EmailService` - Email sending functionality

### Repositories
- `OrderRepository` - JPA repository for order persistence

### Models
- `OrderEntity` - JPA entity for orders
- `WebhookEventDto` - DTO for webhook data transfer

### Exception Handling
- `GlobalExceptionHandler` - Centralized exception handling
- `WebhookVerificationException` - Custom exception for webhook errors

## 🔒 Security Best Practices

1. **No Hardcoded Secrets**: All sensitive data uses environment variables
2. **Input Validation**: Webhook signatures verified before processing
3. **Secure Logging**: Sensitive data masked or excluded from logs
4. **Error Handling**: Generic error messages to clients, detailed logs server-side

## 🧪 Testing

```bash
mvn test
```

## 📝 Design Patterns Used

- **Repository Pattern**: Data access abstraction
- **Service Layer Pattern**: Business logic encapsulation
- **DTO Pattern**: Data transfer between layers
- **Dependency Injection**: Loose coupling
- **Builder Pattern**: Complex object construction (DTOs)

## 🔄 Comparison with Original

| Aspect | Original | Refactored |
|--------|----------|------------|
| Layers | 2 (Controller, Repository) | 3+ (Controller, Service, Repository) |
| Logging | System.out.println | SLF4J Logger |
| Secrets | Hardcoded | Environment variables |
| Error Handling | Generic catch blocks | Specific exceptions + Global handler |
| Code Duplication | High | Minimal (Lombok) |
| Testability | Low | High (DI + Service layer) |
| Security | Multiple vulnerabilities | Secure by design |

## 📚 Technologies

- Spring Boot 3.2.0
- Spring Data JPA
- Spring Mail
- Stripe Java SDK 24.16.0
- H2 Database
- Lombok
- SLF4J Logging

## 🎯 Future Enhancements

- Unit tests with JUnit 5 & Mockito
- Integration tests
- API documentation with Swagger/OpenAPI
- Docker containerization
- CI/CD pipeline
- Monitoring with Actuator

## 📄 License

MIT License

## 👨‍💻 Author

Leveraged AI-assisted engineering tools (Amazon Q) to evaluate architectural patterns and refactor legacy code structures into a highly optimized, multi-tier Maven architecture.
