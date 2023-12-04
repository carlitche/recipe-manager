package com.cookin.recipemanager.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public record RecipeDto (
        Long id,
        @NotBlank
        String name,
        @NotNull
        Boolean isVegetarian,
        @NotNull
        int serving,
        @NotEmpty
        List<String> ingredients,
        @NotBlank
        @Length(min = 5, message = "Length must be greater than {min}")
        String instructions
) {}
