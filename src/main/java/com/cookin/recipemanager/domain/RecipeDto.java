package com.cookin.recipemanager.domain;

import java.util.List;

public record RecipeDto (
        Long id,
        String name,
        Boolean isVegetarian,
        int serving,
        List<String> ingredients,
        String instructions
) {}
