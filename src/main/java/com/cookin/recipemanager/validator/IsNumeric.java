package com.cookin.recipemanager.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = NumericStringValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface IsNumeric {
    String message() default "The field must contain a numeric value";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
