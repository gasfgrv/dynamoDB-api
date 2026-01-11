package com.gasfgrv.dynamodbapi.infrastructure.dtos

import com.fasterxml.jackson.annotation.JsonInclude
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import tools.jackson.databind.PropertyNamingStrategies
import tools.jackson.databind.annotation.JsonNaming

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy::class)
data class EmployeeInputDto(
  val employeeId: String?,

  @NotBlank
  val firstName: String,

  @NotBlank
  val lastName: String,

  @NotBlank
  @Email
  val email: String,

  @NotBlank
  val departmentName: String,

  @NotBlank
  val departmentCode: String
)
