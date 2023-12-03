package com.cookin.recipemanager.service;

import com.cookin.recipemanager.domain.RecipeDto;
import org.springframework.data.domain.Page;

public interface RecipeService {

    Long createNewRecipe(RecipeDto recipeDto);

    void updateRecipe(RecipeDto recipeDto);

    void deleteRecipe(Long id);

    RecipeDto getRecipeById(Long id);

    Page<RecipeDto> getAllRecipe();

    Page<RecipeDto> getRecipeWithSearchFilter();
}
