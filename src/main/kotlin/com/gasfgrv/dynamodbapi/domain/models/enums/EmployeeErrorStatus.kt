package com.gasfgrv.dynamodbapi.domain.models.enums

enum class EmployeeErrorStatus(private val message: String) {
  NOT_FOUND("This employee doesn't exists"),
  CANT_BE_DELETED("This employee can't be deleted"),
  CANT_BE_UPDATED("This employee can't update his data");

  fun getMessage(): String {
    return message
  }
}
