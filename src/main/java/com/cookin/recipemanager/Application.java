package com.cookin.recipemanager;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.JdbcConnectionDetails;
import org.springframework.context.annotation.Bean;


@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Bean
	CommandLineRunner commandLineRunner(JdbcConnectionDetails jdbc){
		return args -> {
			var details = "class: " + jdbc.getClass().getName() + "\n" +
					"JDBC URL: " + jdbc.getJdbcUrl() + "\n" +
					"Username: " + jdbc.getUsername() + "\n" +
					"Password: " + jdbc.getPassword() + "\n";

			System.out.println(details);
;		};
	}

}
