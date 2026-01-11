package com.gasfgrv.dynamodbapi.infrastructure.entities

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean

@DynamoDbBean
data class DepartmentEntity(
  @get:DynamoDbAttribute("name")
  var departmentName: String? = null,

  @get:DynamoDbAttribute("code")
  var departmentCode: String? = null,
)