package com.gasfgrv.dynamodbapi.infrastructure.mappers

import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeInputDto
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeResponseDto
import com.gasfgrv.dynamodbapi.infrastructure.entities.DepartmentEntity
import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity
import java.util.UUID
import org.springframework.stereotype.Component

@Component
class EmployeeMapperImpl : EmployeeMapper {
  override fun toEntity(employee: Employee): EmployeeEntity {
    return EmployeeEntity(
      employee.employeeId(),
      employee.firstName(),
      employee.lastName(),
      employee.email(),
      DepartmentEntity(
        employee.departmentName(),
        employee.departmentCode(),
      )
    )
  }

  override fun toDomain(entity: EmployeeEntity): Employee {
    return Employee(
      entity.employeeId!!,
      entity.firstName!!,
      entity.lastName!!,
      entity.email!!,
      Department(
        entity.departmentName(),
        entity.departmentCode()
      )
    )
  }

  override fun toDomain(input: EmployeeInputDto): Employee {
    return Employee(
      input.employeeId ?: UUID.randomUUID().toString(),
      input.firstName,
      input.lastName,
      input.email,
      Department(
        input.departmentName,
        input.departmentCode
      )
    )
  }

  override fun toDto(input: Employee): EmployeeResponseDto {
    return EmployeeResponseDto(
      input.firstName(),
      input.lastName(),
      input.email(),
      input.departmentName(),
      input.departmentCode(),
    )
  }
}