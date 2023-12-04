package com.cookin.recipemanager.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NumericStringValidator implements ConstraintValidator<IsNumeric, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }

        if(value != null && value.contains(":")){
            int colonIndex = value.indexOf(":");
            if (colonIndex + 1 < value.length()) {
                value = value.substring(colonIndex + 1);
            } else {
                value = "";
            }
        }
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}
