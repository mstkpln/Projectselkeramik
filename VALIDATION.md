# Bean Validation Implementation

## Overview
This project uses Jakarta Bean Validation (JSR 380) for input validation with custom validators and comprehensive error handling.

## Validation Annotations Used

### Standard Annotations
- `@NotBlank` - Ensures string is not null or empty
- `@NotNull` - Ensures value is not null
- `@Email` - Validates email format
- `@Positive` - Ensures number is positive

### Custom Annotations
- `@ValidPaymentStatus` - Validates payment status against allowed values (paid, unpaid, no_payment_required)

## Validated DTOs

### WebhookEventDto
```java
@NotBlank(message = "Session ID is required")
private String sessionId;

@Email(message = "Invalid email format")
private String email;

@NotNull(message = "Amount is required")
@Positive(message = "Amount must be positive")
private Double amount;

@NotBlank(message = "Payment status is required")
@ValidPaymentStatus
private String paymentStatus;
```

## Error Response Format

All validation errors return a standardized JSON response:

```json
{
  "timestamp": "2026-02-18T10:30:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Invalid input data",
  "path": "/api/stripe/webhook",
  "validationErrors": [
    "sessionId: Session ID is required",
    "amount: Amount must be positive"
  ]
}
```

## Custom Validators

### PaymentStatusValidator
Validates payment status against allowed values:
- `paid`
- `unpaid`
- `no_payment_required`

## Exception Handling

### GlobalExceptionHandler
Handles:
- `MethodArgumentNotValidException` - Bean validation failures
- `WebhookVerificationException` - Webhook signature errors
- `IllegalArgumentException` - Invalid arguments
- `Exception` - Generic errors

## Testing

Run validation tests:
```bash
mvn test -Dtest=WebhookEventDtoValidationTest
```

## Benefits

✅ **Input Sanitization** - Prevents bad data from entering the system
✅ **Clear Error Messages** - Developers know exactly what's wrong
✅ **Type Safety** - Compile-time validation of constraints
✅ **Reusable** - Validators can be used across multiple DTOs
✅ **Testable** - Easy to unit test validation logic

## Next Steps

Consider adding:
- More custom validators (e.g., @ValidSessionId)
- Group validation for different scenarios
- Cross-field validation
- Internationalization (i18n) for error messages
