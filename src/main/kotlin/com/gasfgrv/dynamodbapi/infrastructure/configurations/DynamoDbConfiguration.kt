package com.gasfgrv.dynamodbapi.infrastructure.configurations

import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity
import com.gasfgrv.dynamodbapi.infrastructure.properties.DynamoDbProperties
import java.net.URI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.DefaultCredentialsProvider
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.TableSchema
import software.amazon.awssdk.http.apache.ApacheHttpClient
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient


@Configuration
class DynamoDbConfiguration(private val properties: DynamoDbProperties) {
  @Bean
  fun dynamoDbClient(properties: DynamoDbProperties): DynamoDbClient {
    return DynamoDbClient.builder()
      .endpointOverride(URI.create(properties.serviceEndpoint))
      .region(Region.of(properties.region))
      .credentialsProvider(DefaultCredentialsProvider.builder().build())
      .httpClientBuilder(ApacheHttpClient.builder())
      .build()
  }

  @Bean
  fun dynamoDbEnhancedClient(dynamoDbClient: DynamoDbClient): DynamoDbEnhancedClient {
    return DynamoDbEnhancedClient.builder()
      .dynamoDbClient(dynamoDbClient)
      .build()
  }

  @Bean
  fun employeeTable(dynamoDbEnhancedClient: DynamoDbEnhancedClient): DynamoDbTable<EmployeeEntity?>? {
    return dynamoDbEnhancedClient.table("employee", TableSchema.fromBean(EmployeeEntity::class.java))
  }
}