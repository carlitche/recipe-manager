package com.cookin.recipemanager.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.Arrays;

public class PrefixAndColonValidator implements ConstraintValidator<ValidOperation, String> {
    private String[] prefixes;

    @Override
    public void initialize(ValidOperation constraintAnnotation) {
        prefixes = constraintAnnotation.prefixes();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if(value != null && value.contains(":")){
            return Arrays.stream(prefixes).anyMatch(s -> value.startsWith(s));
        }
        return true;
    }
}
