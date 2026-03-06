package com.example.demo.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class PaymentStatusValidator implements ConstraintValidator<ValidPaymentStatus, String> {
    
    private static final List<String> VALID_STATUSES = Arrays.asList("paid", "unpaid", "no_payment_required");
    
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        return VALID_STATUSES.contains(value.toLowerCase());
    }
}
