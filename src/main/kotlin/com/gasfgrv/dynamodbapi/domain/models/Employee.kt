package com.gasfgrv.dynamodbapi.domain.models

class Employee(
  private val id: String,
  private val firstName: String,
  private val lastName: String,
  private val email: String,
  private val department: Department
) {
  fun employeeId(): String = id
  fun firstName(): String = firstName
  fun lastName(): String = lastName
  fun email(): String = email
  fun departmentName(): String = department.name()
  fun departmentCode(): String = department.code()
}