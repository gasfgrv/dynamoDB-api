package com.gasfgrv.dynamodbapi.application.usecases

import com.gasfgrv.dynamodbapi.domain.exceptions.EmployeeException
import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus
import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus.CANT_BE_DELETED
import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus.CANT_BE_UPDATED
import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus.NOT_FOUND
import com.gasfgrv.dynamodbapi.domain.ports.inputs.EmployeeServicePort
import com.gasfgrv.dynamodbapi.domain.ports.outputs.EmployeeRepositoryPort
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmployeeUsecase(private val repository: EmployeeRepositoryPort) : EmployeeServicePort {
  companion object {
    private val log = LoggerFactory.getLogger(EmployeeUsecase::class.java)
  }

  override fun save(employee: Employee): Employee {
    log.info("Saving Employee")
    repository.save(employee)
    return employee
  }

  override fun getEmployeeById(employeeId: String): Employee {
    log.info("Getting Employee with id: $employeeId")
    return repository.getEmployeeById(employeeId)
      .orElseThrow { EmployeeException(NOT_FOUND) }
  }

  override fun delete(employeeId: String) {
    log.info("Deleting Employee")
    val employeeById = repository.getEmployeeById(employeeId)

    checkIfPresent(employeeById, employeeId, CANT_BE_DELETED)

    val employee = employeeById.get()
    repository.deleteEmployee(employee)
  }

  override fun update(employeeId: String, employee: Employee): Employee {
    log.info("Updating Employee")
    val employeeById = repository.getEmployeeById(employeeId)

    checkIfPresent(employeeById, employeeId, CANT_BE_UPDATED)

    val oldEmployee = employeeById.get()

    val updatedEmployee = Employee(
      oldEmployee.employeeId(),
      employee.firstName(),
      employee.lastName(),
      employee.email(),
      Department(
        employee.departmentName(),
        employee.departmentCode()
      )
    )

    return repository.updateEmployee(updatedEmployee)
  }

  private fun checkIfPresent(
    employeeById: Optional<Employee>,
    employeeId: String,
    statusOnError: EmployeeErrorStatus
  ) {
    if (employeeById.isEmpty) {
      log.error("Employee with id: $employeeId not found")
      throw EmployeeException(statusOnError)
    }
  }
}