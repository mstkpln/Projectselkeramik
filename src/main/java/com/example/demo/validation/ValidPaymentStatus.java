package com.example.demo.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PaymentStatusValidator.class)
@Documented
public @interface ValidPaymentStatus {
    String message() default "Invalid payment status. Must be one of: paid, unpaid, no_payment_required";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
