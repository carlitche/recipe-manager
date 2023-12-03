package com.cookin.recipemanager.repository;

import com.cookin.recipemanager.entity.Recipe;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;

public interface RecipeRepository extends ListCrudRepository<Recipe, Long>, ListPagingAndSortingRepository<Recipe, Long>, JpaSpecificationExecutor<Recipe> {
    Recipe findByName(String name);
}
