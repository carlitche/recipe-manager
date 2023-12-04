package com.cookin.recipemanager.controller;

import com.cookin.recipemanager.domain.RecipeDto;
import com.cookin.recipemanager.domain.RecipeFilters;
import com.cookin.recipemanager.domain.RecipePage;
import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.service.RecipeService;
import jakarta.validation.Valid;
import lombok.extern.log4j.Log4j2;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Log4j2
@RestController
@RequestMapping(RecipeController.BASE_PATH)
public class RecipeController {

    static final String BASE_PATH = "/api/recipes";

    private final RecipeService recipeService;

    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @PostMapping
    public ResponseEntity newRecipe(@Valid @RequestBody RecipeDto recipeDto, UriComponentsBuilder ucb){
        log.info("Request to Create New Recipe: {}", recipeDto);

        Long newRecipeId = recipeService.createNewRecipe(recipeDto);

        log.info("New Recipe created with id: {}", newRecipeId);

        URI locationOfNewAccount = ucb.path(BASE_PATH + "/{recipeId}")
                .buildAndExpand(newRecipeId)
                .toUri();
        return ResponseEntity.created(locationOfNewAccount).build();
    }

    @GetMapping
    public ResponseEntity<Page<Recipe>> getAllRecipe(@Valid @ParameterObject RecipeFilters recipeFilters, @ParameterObject RecipePage page){
        log.info("Request for all Recipes");

        Page<Recipe> allRecipe = recipeService.getAllRecipe(recipeFilters, page);

        return ResponseEntity.ok(allRecipe);
    }

    @GetMapping("/{recipeId}")
    public ResponseEntity<Recipe> getRecipeById(@PathVariable("recipeId") Long recipeId){
        log.info("Request Recipe with Id: {}", recipeId);
        return ResponseEntity.ok(recipeService.getRecipeById(recipeId));
    }

    @PutMapping("/{recipeId}")
    public ResponseEntity<?> updateRecipe(@PathVariable("recipeId") Long recipeId, @RequestBody RecipeDto recipeDto){
        log.info("Request to Update Recipe with Id: {}, recipe: {}", recipeId, recipeDto);
        recipeService.updateRecipe(recipeId, recipeDto);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{recipeId}")
    public ResponseEntity<?> deleteRecipe(@PathVariable("recipeId") Long recipeId) {
        log.info("Request to Delete Recipe with Id: {}", recipeId);
        recipeService.deleteRecipe(recipeId);
        return ResponseEntity.noContent().build();
    }

}
