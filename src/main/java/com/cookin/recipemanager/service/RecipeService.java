package com.cookin.recipemanager.service;

import com.cookin.recipemanager.domain.RecipeDto;
import com.cookin.recipemanager.domain.RecipeFilters;
import com.cookin.recipemanager.domain.RecipePage;
import com.cookin.recipemanager.entity.Recipe;
import org.springframework.data.domain.Page;

public interface RecipeService {

    Long createNewRecipe(RecipeDto recipeDto);

    Recipe updateRecipe(Long recipeId, RecipeDto recipeDto);

    void deleteRecipe(Long recipeId);

    Recipe getRecipeById(Long recipeId);

    Page<Recipe> getAllRecipe(RecipeFilters recipeFilters, RecipePage page);

}
