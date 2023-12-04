package com.cookin.recipemanager.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PrefixAndColonValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidOperation {

    String message() default "String must start with one of the valid prefixes {prefixes} before ':'";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] prefixes();
}
