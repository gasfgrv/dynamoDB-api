package com.gasfgrv.dynamodbapi.infrastructure.mappers

import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeInputDto
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeResponseDto
import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity


interface EmployeeMapper {
  fun toEntity(employee: Employee): EmployeeEntity
  fun toDomain(entity: EmployeeEntity): Employee
  fun toDomain(input: EmployeeInputDto): Employee
  fun toDto(saved: Employee): EmployeeResponseDto
}