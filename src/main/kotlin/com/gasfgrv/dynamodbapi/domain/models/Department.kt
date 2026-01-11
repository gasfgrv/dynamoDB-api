package com.gasfgrv.dynamodbapi.domain.models

class Department(
  private val name: String,
  private val code: String
) {
  fun name(): String = name
  fun code(): String = code
}