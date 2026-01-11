package com.gasfgrv.dynamodbapi.domain.ports.inputs

import com.gasfgrv.dynamodbapi.domain.models.Employee

interface EmployeeServicePort {
  fun save(employee: Employee): Employee
  fun getEmployeeById(employeeId: String): Employee
  fun delete(employeeId: String)
  fun update(employeeId: String, employee: Employee): Employee
}