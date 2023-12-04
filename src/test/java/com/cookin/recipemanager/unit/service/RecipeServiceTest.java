package com.cookin.recipemanager.unit.service;

import com.cookin.recipemanager.domain.RecipeDto;
import com.cookin.recipemanager.domain.RecipeFilters;
import com.cookin.recipemanager.domain.RecipePage;
import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.exception.ResourceNotFoundException;
import com.cookin.recipemanager.repository.RecipeRepository;
import com.cookin.recipemanager.repository.specification.RecipeSpecification;
import com.cookin.recipemanager.service.RecipeService;
import com.cookin.recipemanager.service.RecipeServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = RecipeServiceImpl.class)
public class RecipeServiceTest {

    @Autowired
    RecipeService recipeService;

    @MockBean
    RecipeRepository recipeRepository;

    @Test
    void create_new_recipe(){
        Long recipeId = 1L;
        Recipe newRecipe = new Recipe(recipeId, "recipe test 1", true, 8, List.of( "ingredient 3", "ingredient 5", "ingredient 9"), "recipe 1 instructions, roast, reduce...");

        when(recipeRepository.save(any())).thenReturn(newRecipe);

        Long newRecipeId = recipeService.createNewRecipe(new RecipeDto( null, "recipe test 1",
                                                                true, 8, List.of( "ingredient 3", "ingredient 5", "ingredient 9"),
                                                                "recipe 1 instructions, roast, reduce..."));

        assertThat(newRecipeId).isNotNull();
        assertThat(newRecipeId).isEqualTo(recipeId);
    }

    @Test
    void get_all_recipe(){
        RecipePage paged = new RecipePage();
        paged.setPageNumber(0);
        paged.setPageSize(2);
        List<Recipe> recipeList = List.of(
                new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ..."),
                new Recipe(2L, "recipe test 2", true, 2, List.of( "ingredient 2", "ingredient 3", "ingredient 5"), "recipe 2 instructions, chop, airFryer...")
              );

        Pageable page = PageRequest.of(paged.getPageNumber(), paged.getPageSize());
        Page<Recipe> recipes = new PageImpl<>(recipeList, page, 5L);
        RecipeSpecification specification = new RecipeSpecification();

        when(recipeRepository.findAll(any(RecipeSpecification.class),any(Pageable.class))).thenReturn(recipes);

        Page<Recipe> allRecipes = recipeService.getAllRecipe(new RecipeFilters(), paged);

        assertThat(allRecipes.getTotalPages()).isEqualTo(3);
        assertThat(allRecipes.getContent().size()).isEqualTo(paged.getPageSize());
        assertThat(allRecipes.getTotalElements()).isEqualTo(5);
    }

    @Test
    void return_notFoundException_get_all_recipe_no_data(){
        RecipePage paged = new RecipePage();
        paged.setPageNumber(0);
        paged.setPageSize(2);
        Pageable page = PageRequest.of(paged.getPageNumber(), paged.getPageSize());
        Page<Recipe> recipes = new PageImpl<>(List.of(), page, 0L);

        when(recipeRepository.findAll(any(RecipeSpecification.class),any(Pageable.class))).thenReturn(recipes);

        assertThrows(ResourceNotFoundException.class, () -> {
            recipeService.getAllRecipe(new RecipeFilters(), paged);
        });
    }

    @Test
    void get_recipe_by_id(){
        Long recipeId = 1L;
        Recipe recipe = new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ...");

        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        Recipe recipeById = recipeService.getRecipeById(recipeId);

        assertThat(recipeById.getId()).isEqualTo(recipeId);
    }

    @Test
    void return_notFoundException_recipe_id_dont_exist(){
        Long recipeId = 99L;
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            recipeService.getRecipeById(recipeId);
        });
    }

    @Test
    void update_recipe(){
        Long recipeId = 1L;
        when(recipeRepository.existsById(recipeId)).thenReturn(true);

        Recipe recipe = new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ...");
        when(recipeRepository.findById(recipeId)).thenReturn(Optional.of(recipe));

        Recipe returnRecipe = new Recipe(1L, "recipe test 1.2", false, 8, List.of("ingredient 1", "ingredient 2.2" ), "recipe 1.2 instructions, boil, oven ...");
        when(recipeRepository.save(any())).thenReturn(returnRecipe);

        Recipe newRecipe = recipeService.updateRecipe(recipeId, new RecipeDto(null, "recipe test 1", true,
                8, List.of("ingredient 3", "ingredient 5", "ingredient 9"), "recipe 1 instructions, roast, reduce..."));

        assertThat(newRecipe.getName()).isEqualTo(returnRecipe.getName());
        assertThat(newRecipe.getIngredients().size()).isEqualTo(returnRecipe.getIngredients().size());
        assertThat(newRecipe.getIngredients()).containsAll(returnRecipe.getIngredients());
    }

    @Test
    void delete_recipe(){
        Long recipeId = 1L;
        when(recipeRepository.existsById(recipeId)).thenReturn(true);

        doNothing().when(recipeRepository).deleteById(recipeId);

        recipeService.deleteRecipe(recipeId);

        verify(recipeRepository, times(1)).deleteById(recipeId);
    }
}
