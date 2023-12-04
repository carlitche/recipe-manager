package com.cookin.recipemanager.unit.controller;

import com.cookin.recipemanager.controller.RecipeController;
import com.cookin.recipemanager.domain.RecipeDto;
import com.cookin.recipemanager.domain.RecipePage;
import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.exception.ResourceNotFoundException;
import com.cookin.recipemanager.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
public class RecipeControllerTest {

    String SERVICE_PATH = "/api/recipes";

    @Autowired
    private MockMvc mvc;

    @MockBean
    private RecipeService recipeService;

    @Captor
    ArgumentCaptor<RecipePage> recipePageCaptor;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void add_new_recipe_return201() throws Exception {

        String jsonRecipe = """
                { 
                    "name": "recipe test 1",
                    "isVegetarian": false,
                    "serving": 4,
                    "ingredients": ["ingredient 1", "ingredient 2", "ingredient 4"],
                    "instructions": "recipe 1 instructions, boil, oven ..."
                }
                """;
        Long newRecipeId = 1L;
        when(recipeService.createNewRecipe(objectMapper.readValue(jsonRecipe, RecipeDto.class))).thenReturn(newRecipeId);

        mvc.perform(post(SERVICE_PATH).content(jsonRecipe).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/recipes/" + newRecipeId)));

    }

    @Test
    void add_new_recipe_return4XX_bad_request() throws Exception {
        String jsonRecipe = """
                {  
                    "isVegetarian": false,
                    "serving": 4,
                    "instructions": "..."
                }
                """;
        Long newRecipeId = 1L;
        mvc.perform(post(SERVICE_PATH).content(jsonRecipe).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsString("instructions=Length must be greater than 5")))
                .andExpect(jsonPath("$.message", containsString("name=must not be blank")))
                .andExpect(jsonPath("$.message", containsString("ingredients=must not be empty")));
    }

    @Test
    void get_all_recipe_return_first_page() throws Exception {
        int pageSize = 2;
        int pageNumber = 0;

        RecipePage recipePage = new RecipePage();
        recipePage.setPageNumber(pageNumber);
        recipePage.setPageSize(pageSize);

        List<Recipe> recipeList = List.of(
                new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ..."),
                new Recipe(2L, "recipe test 2", true, 2, List.of( "ingredient 2", "ingredient 3", "ingredient 5"), "recipe 2 instructions, chop, airFryer...")
        );

        Pageable page = PageRequest.of(recipePage.getPageNumber(), recipePage.getPageSize());
        Page<Recipe> recipes = new PageImpl<>(recipeList, page, 5L);

        when(recipeService.getAllRecipe(any(), any())).thenReturn(recipes);

        mvc.perform(get(SERVICE_PATH).param("pageNumber", String.valueOf(pageNumber))
                                     .param("pageSize", String.valueOf(pageSize))
                 ).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.pageable.pageSize").value(2))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(2)));
    }

    @Test
    void get_all_recipe_return_first_page_return4XX_no_data_found() throws Exception {

        when(recipeService.getAllRecipe(any(), any())).thenThrow(new ResourceNotFoundException("No Data Found."));

        mvc.perform(get(SERVICE_PATH))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("No Data Found.")));

    }

    @Test
    void get_recipe_by_id() throws Exception {

        long recipeId = 1L;
        Recipe recipe = new Recipe(1L, "recipe test 1", false, 4, List.of("ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ...");

        when(recipeService.getRecipeById(recipeId)).thenReturn(recipe);

        mvc.perform(get(SERVICE_PATH + "/" + recipeId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(recipeId));
    }

    @Test
    void get_recipe_by_id_return4XX_not_found() throws Exception {
        long recipeId = 99L;
        when(recipeService.getRecipeById(recipeId)).thenThrow(new ResourceNotFoundException("Recipe Not Found"));

        mvc.perform(get(SERVICE_PATH + "/" + recipeId))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message", containsString("Recipe Not Found")));
    }

    @Test
    void update_recipe() throws Exception {
        String jsonRecipe = """
                { 
                    "name": "recipe test 1",
                    "isVegetarian": false,
                    "serving": 4,
                    "ingredients": ["ingredient 1", "ingredient 2", "ingredient 4"],
                    "instructions": "recipe 1 instructions, boil, oven ..."
                }
                """;

        long recipeId = 1L;
        Recipe recipe = new Recipe(1L, "recipe test 1.2", false, 8, List.of("ingredient 1", "ingredient 2.2" ), "recipe 1.2 instructions, boil, oven ...");

        when(recipeService.getRecipeById(recipeId)).thenReturn(recipe);

        mvc.perform(put(SERVICE_PATH + "/" + recipeId).content(jsonRecipe).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isNoContent());
    }

    @Test
    void delete_recipe() throws Exception {
        long recipeId = 1L;

        doNothing().when(recipeService).deleteRecipe(recipeId);

        mvc.perform(delete(SERVICE_PATH + "/" + recipeId))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(recipeService, times(1)).deleteRecipe(recipeId);
    }
}
