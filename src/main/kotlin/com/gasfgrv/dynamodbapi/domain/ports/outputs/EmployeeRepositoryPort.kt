package com.gasfgrv.dynamodbapi.domain.ports.outputs

import com.gasfgrv.dynamodbapi.domain.models.Employee
import java.util.Optional

interface EmployeeRepositoryPort {
  fun save(employee: Employee): Employee
  fun getEmployeeById(id: String): Optional<Employee>
  fun deleteEmployee(employee: Employee)
  fun updateEmployee(employee: Employee): Employee
}