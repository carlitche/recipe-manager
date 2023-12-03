package com.cookin.recipemanager.domain;

public record RecipeFiltersDto (
        Long id,
        String name,
        Boolean isVegetarian,
        int serving,
        String ingredients,
        String instructions
) {}
