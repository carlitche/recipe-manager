package com.cookin.recipemanager.intgr;

import com.cookin.recipemanager.domain.RecipeDto;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.net.URI;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private RestTemplate restTemplate;

    private final String BASE_URL = "http://localhost";

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));


//    @Test
    void connectionStatus() {
        assertThat(postgres.isCreated()).isTrue();
        assertThat(postgres.isRunning()).isTrue();
    }

    @Test
    void post_new_recipe(){
        RecipeDto recipeDto = new RecipeDto(null, "recipe test 6", false, 4, List.of( "ingredient 10", "ingredient 12", "ingredient 14"), "recipe 1 instructions, boil, oven ...");

        ResponseEntity<Void> createResponse = restTemplate.postForEntity(BASE_URL + ":" + port + "/api/recipes", recipeDto, Void.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        URI locationOfNewRecipe = createResponse.getHeaders().getLocation();

        ResponseEntity<String> getResponse = restTemplate.getForEntity(locationOfNewRecipe, String.class);
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "TRUNCATE RECIPE CASCADE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void get_all_recipes_pageSize2(){
        int pageSize = 2;
        int pageNumber = 0;
        String response = restTemplate.getForObject(BASE_URL + ":" + port + "/api/recipes?pageNumber=" + pageNumber + "&pageSize=" + pageSize, String.class);

        Integer length = JsonPath.read(response, "$.content.length()");
        assertThat(length).isEqualTo(2);
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "TRUNCATE RECIPE CASCADE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void get_all_recipes_with_that_is_Vegetarian_less3serving_withIngredient5_airFryerInstruction(){
        int pageSize = 2;
        int pageNumber = 0;
        String isVegetarian = "true";
        String ingredients = "ingredient 5";
        String instructions = "airFryer";
        String urlFilter = BASE_URL + ":" + port + "/api/recipes?isVegetarian=" + isVegetarian +
                "&ingredients="+ ingredients + "&instructions=" + instructions + "&pageNumber=" + pageNumber + "&pageSize=" + pageSize;

        String response = restTemplate.getForObject(urlFilter, String.class);

        Integer length = JsonPath.read(response, "$.content.length()");
        assertThat(length).isEqualTo(1);
    }

    @Test
    @Sql(scripts = "/data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(statements = "TRUNCATE RECIPE CASCADE;", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void delete_recipe(){
        int recipeId = 1;
        String url = BASE_URL + ":" + port + "/api/recipes/" + recipeId;
        restTemplate.delete(url);

        try {
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        } catch (HttpClientErrorException e) {
            String errorResponseBody = e.getResponseBodyAsString();
            String message = JsonPath.read(errorResponseBody, "$.message");
            assertThat(message).isEqualTo("Recipe Not Found");
        }
    }


}
