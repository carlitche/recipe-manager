package com.cookin.recipemanager;

import com.cookin.recipemanager.entity.Recipe;
import com.cookin.recipemanager.repository.RecipeRepository;
import com.cookin.recipemanager.repository.specification.RecipeSpecification;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	@Profile("local")
	CommandLineRunner commandLineRunner(JdbcConnectionDetails jdbc, RecipeRepository recipeRepository){
		return args -> {
			var details = "class: " + jdbc.getClass().getName() + "\n" +
					"JDBC URL: " + jdbc.getJdbcUrl() + "\n" +
					"Username: " + jdbc.getUsername() + "\n" +
					"Password: " + jdbc.getPassword() + "\n";

			System.out.println(details);

			List<Recipe> recipes = List.of(
					new Recipe(1L, "recipe test 1", false, 4, List.of( "ingredient 1", "ingredient 2", "ingredient 4"), "recipe 1 instructions, boil, oven ..."),
					new Recipe(2L, "recipe test 2", true, 2, List.of( "ingredient 2", "ingredient 3", "ingredient 5"), "recipe 2 instructions, chop, airFair..."),
					new Recipe(3L, "recipe test 3", true, 6, List.of( "ingredient 1", "ingredient 4", "ingredient 5"), "recipe 3 instructions, marinate, oven..."),
					new Recipe(4L, "recipe test 4", false, 4, List.of( "ingredient 7", "ingredient 8", "ingredient 9"), "recipe 4 instructions, bake, oven..."),
					new Recipe(5L, "recipe test 5", true, 8, List.of( "ingredient 3", "ingredient 5", "ingredient 9"), "recipe 5 instructions, roast, reduce...")
			);
			if(recipeRepository.findAll().isEmpty())
				recipeRepository.saveAll(recipes);
;		};
	}

}
