package com.gasfgrv.dynamodbapi.infrastructure.entities

import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey

@DynamoDbBean
data class EmployeeEntity(
  @get:DynamoDbPartitionKey
  @get:DynamoDbAttribute("employee_id")
  var employeeId: String? = null,

  @get:DynamoDbAttribute("first_name")
  var firstName: String? = null,

  @get:DynamoDbAttribute("last_name")
  var lastName: String? = null,

  @get:DynamoDbSortKey
  @get:DynamoDbAttribute("employee_email")
  var email: String? = null,

  @get:DynamoDbAttribute("employee_department")
  var department: DepartmentEntity? = null,
) {
  fun departmentName(): String = department?.departmentName ?: "Unknown"
  fun departmentCode(): String = department?.departmentCode ?: "Unknown"
}
