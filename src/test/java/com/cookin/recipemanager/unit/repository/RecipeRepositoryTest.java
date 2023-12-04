package com.cookin.recipemanager.unit.repository;


import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.repository.RecipeRepository;
import com.cookin.recipemanager.repository.specification.Operation;
import com.cookin.recipemanager.repository.specification.RecipeSpecification;
import com.cookin.recipemanager.repository.specification.SearchCriteria;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RecipeRepositoryTest {

    @Autowired
    RecipeRepository recipeRepository;

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));

    @Test
    void connectionStatus() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @BeforeEach
    void setup(){
        List<Recipe> recipes = List.of(
                new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ..."),
                new Recipe(2L, "recipe test 2", true, 2, List.of( "ingredient 2", "ingredient 3", "ingredient 5"), "recipe 2 instructions, chop, airFryer..."),
                new Recipe(3L, "recipe test 3", true, 6, List.of( "ingredient 1", "ingredient 4", "ingredient 5"), "recipe 3 instructions, marinate, oven..."),
                new Recipe(4L, "recipe test 4", false, 4, List.of( "ingredient 7", "ingredient 8", "ingredient 9"), "recipe 4 instructions, bake, oven..."),
                new Recipe(5L, "recipe test 5", true, 8, List.of( "ingredient 3", "ingredient 5", "ingredient 9"), "recipe 5 instructions, roast, reduce...")
        );

        recipeRepository.saveAll(recipes);
    }

    @AfterEach
    void tearDow(){
        recipeRepository.deleteAll();
    }

    @Test
    void add_new_recipe(){
        Recipe recipe = new Recipe(null, "recipe test 10", true, 8, List.of( "ingredient 13", "ingredient 15", "ingredient 19"), "recipe 6 instructions, roast, reduce...");

        Recipe newRecipe = recipeRepository.save(recipe);

        System.out.println(newRecipe);

        assertThat(newRecipe.getId()).isNotNull();
        assertThat(newRecipe.getName()).isEqualTo(recipe.getName());
    }

    @Test
    void get_all_recipe_paged(){
        int pageNumber = 1;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        Page<Recipe> allRecipes = recipeRepository.findAll(pageable);

        assertThat(allRecipes.getTotalPages()).isEqualTo(3);
        assertThat(allRecipes.getContent().size()).isEqualTo(pageSize);
        assertThat(allRecipes.getTotalElements()).isEqualTo(5);
    }

    @Test
    void delete_recipe(){
        String recipeName = "recipe test 1";
        Recipe recipe = recipeRepository.findByName(recipeName);

        assertThat(recipe).isNotNull();

        recipeRepository.delete(recipe);

        assertThat(recipeRepository.findByName(recipeName)).isNull();
        assertThat(recipeRepository.findAll().size()).isEqualTo(4);
    }

    @Test
    void update_isVegetarian_false_to_true_recipe(){
        Boolean isVegetarian = true;
        String recipeName = "recipe test 1";
        Recipe recipe = recipeRepository.findByName(recipeName);

        assertThat(recipe.getIsVegetarian()).isFalse();

        recipe.setIsVegetarian(isVegetarian);
        Recipe newRecipe = recipeRepository.save(recipe);

        System.out.println(newRecipe);

        assertThat(recipe.getIsVegetarian()).isTrue();
    }

    @Test
    void updating_recipe_ingredient(){
        String ingrt1 = "ingredient 10";
        String ingrt2 = "ingredient 11";
        String ingrt3 = "ingredient 12";

        String recipeName = "recipe test 1";
        Recipe recipe = recipeRepository.findByName(recipeName);
        List<String>  listIngredients = recipe.getIngredients();
        listIngredients.set(0, ingrt1);
        listIngredients.set(1, ingrt2);
        listIngredients.set(2, ingrt3);

        Recipe newRecipe = recipeRepository.save(recipe);

        System.out.println(newRecipe.getIngredients());

        assertThat(newRecipe.getIngredients().size()).isEqualTo(3);
        assertThat(newRecipe.getIngredients()).contains(ingrt1, ingrt2, ingrt3);
    }

    @Test
    void replace_one_recipe_ingredient(){
        String oldIngredient = "ingredient 1";
        String newIngredient = "ingredient 10";
        String recipeName = "recipe test 1";

        Recipe recipe = recipeRepository.findByName(recipeName);

        List<String>  listIngredients = recipe.getIngredients();
        int index = listIngredients.indexOf(oldIngredient);

        assertThat(index).isNotNegative();

        listIngredients.set(index, newIngredient);
        Recipe newRecipe = recipeRepository.save(recipe);

        System.out.println(newRecipe.getIngredients());

        assertThat(newRecipe.getIngredients().size()).isEqualTo(3);
        assertThat(newRecipe.getIngredients()).contains(newIngredient);
    }

    @Test
    void delete_recipe_ingredient(){
        String ingredient = "ingredient 1";
        String recipeName = "recipe test 1";

        Recipe recipe = recipeRepository.findByName(recipeName);

        List<String>  listIngredients = recipe.getIngredients();
        int index = listIngredients.indexOf(ingredient);

        assertThat(index).isNotNegative();

        listIngredients.remove(ingredient);
        Recipe newRecipe = recipeRepository.save(recipe);

        System.out.println(newRecipe.getIngredients());

        assertThat(newRecipe.getIngredients().size()).isEqualTo(2);
        assertThat(newRecipe.getIngredients()).doesNotContainSequence(ingredient);
    }

    @Test
    void return_recipe_that_is_Vegetarian(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("isVegetarian", true, Operation.EQUAL));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        assertThat(myRecipeList.size()).isEqualTo(3);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 2", "recipe test 3", "recipe test 5");
    }

    @Test
    void return_recipe_that_have_less3serving(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("serving", 3, Operation.LESS_THAN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        assertThat(myRecipeList.size()).isEqualTo(1);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 2");
    }

    @Test
    void return_recipe_with_ingredient9(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("ingredients", "ingredient 9", Operation.IN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(2);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 4", "recipe test 5");
    }

    @Test
    void return_recipe_with_no_ingredient9(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("ingredients", "ingredient 9", Operation.NOT_IN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(3);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 1", "recipe test 2", "recipe test 3");
    }

    @Test
    void return_recipe_contain_oven_on_instruction(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("instructions", "oven", Operation.CONTAIN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(3);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 1", "recipe test 3", "recipe test 4");
    }


    @Test
    void return_recipe_that_is_Vegetarian_less3serving_withIngredient5_airFryerInstruction(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("isVegetarian", true, Operation.EQUAL));
        specification.add(new SearchCriteria("serving", 3, Operation.LESS_THAN));
        specification.add(new SearchCriteria("ingredients", "ingredient 5", Operation.IN));
        specification.add(new SearchCriteria("instructions", "airFryer", Operation.CONTAIN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(1);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 2");
    }

    @Test
    void return_recipe_filter_by_name_withIngredient1(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("name", "recipe test 1", Operation.EQUAL));
        specification.add(new SearchCriteria("ingredients", "ingredient 1", Operation.IN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(1);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 1");
    }

    @Test
    void return_recipe_that_dont_have_ingredient5_ovenInstruction(){
        RecipeSpecification specification= new RecipeSpecification();
        specification.add(new SearchCriteria("ingredients", "ingredient 5", Operation.NOT_IN));
        specification.add(new SearchCriteria("instructions", "oven", Operation.CONTAIN));

        List<Recipe> myRecipeList = recipeRepository.findAll(specification);

        System.out.println(myRecipeList);

        assertThat(myRecipeList.size()).isEqualTo(2);
        List<String> listIds = myRecipeList.stream().map(value -> value.getName()).toList();
        assertThat(listIds).contains("recipe test 1", "recipe test 4");
    }
}


