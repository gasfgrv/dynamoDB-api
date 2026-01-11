package com.gasfgrv.dynamodbapi.infrastructure.dtos

import com.fasterxml.jackson.annotation.JsonInclude
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmployeeResponseDto(
  val firstName: String,
  val lastName: String,
  val email: String,
  val departmentName: String,
  val departmentCode: String
)