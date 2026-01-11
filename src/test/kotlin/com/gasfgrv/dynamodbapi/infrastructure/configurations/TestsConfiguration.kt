package com.gasfgrv.dynamodbapi.infrastructure.configurations

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategies
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import tools.jackson.module.kotlin.KotlinModule

@TestConfiguration
class TestsConfiguration {
  @Bean
  fun objectMapper(): ObjectMapper = ObjectMapper()
    .findAndRegisterModules()
    .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
}