package com.gasfgrv.dynamodbapi.infrastructure.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "aws.dynamo")
data class DynamoDbProperties(
  val serviceEndpoint: String,
  val region: String,
  val accessKey: String,
  val secretKey: String
)
