package com.cookin.recipemanager.service;

import com.cookin.recipemanager.domain.RecipeDto;
import com.cookin.recipemanager.domain.RecipeFilters;
import com.cookin.recipemanager.domain.RecipePage;
import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.exception.ResourceNotFoundException;
import com.cookin.recipemanager.repository.RecipeRepository;
import com.cookin.recipemanager.repository.specification.Operation;
import com.cookin.recipemanager.repository.specification.RecipeSpecification;
import com.cookin.recipemanager.repository.specification.SearchCriteria;
import com.cookin.recipemanager.util.Util;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecipeServiceImpl implements RecipeService{

    private final RecipeRepository recipeRepository;


    public RecipeServiceImpl(RecipeRepository recipeRepository) {
        this.recipeRepository = recipeRepository;
    }

    @Override
    @Transactional
    public Long createNewRecipe(RecipeDto recipeDto) {

        Recipe recipe = Recipe.builder()
                .name(recipeDto.name())
                .isVegetarian(recipeDto.isVegetarian())
                .serving(recipeDto.serving())
                .ingredients(recipeDto.ingredients())
                .instructions(recipeDto.instructions())
                .build();

        return recipeRepository.save(recipe).getId();
    }

    @Override
    @Transactional
    public Recipe updateRecipe(Long recipeId, RecipeDto recipeDto) {
        if(!recipeRepository.existsById(recipeId))
            throw new ResourceNotFoundException("Recipe Not Found");

        Recipe recipe = recipeRepository.findById(recipeId).orElseThrow(() -> new ResourceNotFoundException("Recipe Not Found"));

        recipe.setName(recipeDto.name());
        recipe.setIsVegetarian(recipeDto.isVegetarian());
        recipe.setServing(recipeDto.serving());
        recipe.setIngredients(recipeDto.ingredients());
        recipe.setInstructions(recipeDto.instructions());

        return recipeRepository.save(recipe);
    }

    @Override
    @Transactional
    public void deleteRecipe(Long recipeId) {
        if(!recipeRepository.existsById(recipeId))
            throw new ResourceNotFoundException("Recipe Not Found");

        recipeRepository.deleteById(recipeId);
    }

    @Override
    public Recipe getRecipeById(Long id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe Not Found"));
    }

    @Override
    public Page<Recipe> getAllRecipe(RecipeFilters recipeFilters, RecipePage page) {


        RecipeSpecification specification = extractedSpecification(processRecipeFilters(recipeFilters));

        Pageable paged = PageRequest.of(page.getPageNumber(), page.getPageSize());

        Page<Recipe> allRecipes = recipeRepository.findAll(specification ,paged);

        if(allRecipes.getTotalElements() == 0)
            throw new ResourceNotFoundException("No Data Found.");

        return allRecipes;
    }

    private RecipeSpecification extractedSpecification(RecipeFilters recipeFilters) {
        RecipeSpecification specification= new RecipeSpecification();
        if(recipeFilters.getName() != null){
            String[] str =  Util.parseOperationValue(recipeFilters.getName());
            specification.add(new SearchCriteria("name", str[1], Operation.getOperation(str[0])));
        }
        if(recipeFilters.getIsVegetarian() != null){
            String[] str =  Util.parseOperationValue(recipeFilters.getIsVegetarian());
            Boolean isVegetarian = str[1].equalsIgnoreCase("true") ? true : false;
            specification.add(new SearchCriteria("isVegetarian", isVegetarian, Operation.getOperation(str[0])));
        }
        if(recipeFilters.getServing() != null){
            String[] str =  Util.parseOperationValue(recipeFilters.getServing());
            specification.add(new SearchCriteria("serving", Integer.valueOf(str[1]), Operation.getOperation(str[0])));
        }
        if(recipeFilters.getIngredients() != null){
            String[] str =  Util.parseOperationValue(recipeFilters.getIngredients());
            specification.add(new SearchCriteria("ingredients", str[1], Operation.getOperation(str[0])));
        }
        if(recipeFilters.getInstructions() != null){
            String[] str =  Util.parseOperationValue(recipeFilters.getInstructions());
            specification.add(new SearchCriteria("instructions", str[1], Operation.getOperation(str[0])));
        }
        return specification;
    }

    public RecipeFilters processRecipeFilters(RecipeFilters filters) {
        return new RecipeFilters(
                ensurePrefix(filters.getName(), "eq"),
                ensurePrefix(filters.getIsVegetarian(), "eq"),
                ensurePrefix(filters.getServing(), "eq"),
                ensurePrefix(filters.getIngredients(), "in"),
                ensurePrefix(filters.getInstructions(), "cn")
        );
    }

    private String ensurePrefix(String value, String defaultPrefix) {
        if (value != null && !value.contains(":")) {
            return defaultPrefix + ":" + value;
        }
        return value;
    }

}
