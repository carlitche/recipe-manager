package com.cookin.recipemanager.config;

import com.cookin.recipemanager.repository.specification.RecipeSpecification;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Bean
    @Scope("prototype")
    RecipeSpecification RecipeSpecification(){
        return new RecipeSpecification();
    }
}
