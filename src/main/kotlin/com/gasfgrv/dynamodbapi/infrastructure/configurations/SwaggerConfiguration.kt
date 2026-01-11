package com.gasfgrv.dynamodbapi.infrastructure.configurations

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@OpenAPIDefinition(
  info = Info(
    title = "dynamodb-api",
    description = "API for DynamoDB testing",
    version = "v2",
    contact = Contact(
      name = "gasfgrv",
      email = "gustavo_almeida11@hotmail.com",
      url = "https://github.com/gasfgrv"
    )
  )
)
class SwaggerConfiguration {
  @Bean
  fun openApi(): GroupedOpenApi {
    return GroupedOpenApi.builder()
      .group("api-controllers")
      .packagesToScan("com.gasfgrv.dynamodbapi.infrastructure.controllers")
      .pathsToMatch("/**")
      .build();
  }
}