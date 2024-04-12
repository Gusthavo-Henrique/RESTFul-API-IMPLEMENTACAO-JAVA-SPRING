package br.com.gusthavo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@Configuration
public class OpenAPIConfig {

	@Bean
	OpenAPI openApiConfig() {
		return new OpenAPI()
				.info(new Info()
						.title("SWAGGER API BOOKS")
						.description("Endpoints da web-api-books")
						.version("0.1.0"));
	}
	
}