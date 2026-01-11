package com.gasfgrv.dynamodbapi.infrastructure.controllers

import com.gasfgrv.dynamodbapi.domain.ports.inputs.EmployeeServicePort
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeInputDto
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeResponseDto
import com.gasfgrv.dynamodbapi.infrastructure.mappers.EmployeeMapper
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.servlet.support.ServletUriComponentsBuilder

@RestController
@RequestMapping("/{version}/employees")
class EmployeeController(
  private val employeeUsecase: EmployeeServicePort,
  private val employeeMapper: EmployeeMapper
) {
  companion object {
    private val log = LoggerFactory.getLogger(EmployeeController::class.java)
  }

  @PostMapping
  fun saveEmployee(
    @Valid @RequestBody input: EmployeeInputDto,
    request: HttpServletRequest
  ): ResponseEntity<EmployeeResponseDto> {
    log.info("[${request.method}] - ${request.requestURI}: Salving employee in dynamoDB")

    val employee = employeeMapper.toDomain(input)
    val savedEmployee = employeeUsecase.save(employee)
    val response = employeeMapper.toDto(savedEmployee)

    val location = ServletUriComponentsBuilder
      .fromCurrentRequest()
      .path("/{id}")
      .buildAndExpand(savedEmployee.employeeId())
      .toUri()

    return ResponseEntity
      .created(location)
      .body(response)
  }

  @GetMapping("/{id}")
  fun getEmployee(
    @PathVariable("id") employeeId: String,
    request: HttpServletRequest
  ): ResponseEntity<EmployeeResponseDto> {
    log.info("[${request.method}] - ${request.requestURI}: Getting info of employee $employeeId in dynamoDB")

    val employee = employeeUsecase.getEmployeeById(employeeId)
    val dto = employeeMapper.toDto(employee)
    return ResponseEntity.ok(dto)
  }

  @DeleteMapping("/{id}")
  fun deleteEmployee(
    @PathVariable("id") employeeId: String,
    request: HttpServletRequest
  ): ResponseEntity<Unit> {
    log.info("[${request.method}] - ${request.requestURI}: Deleting info of employee $employeeId in dynamoDB")

    employeeUsecase.delete(employeeId)
    return ResponseEntity
      .noContent()
      .build()
  }

  @PutMapping("/{id}")
  fun updateEmployee(
    @PathVariable("id") employeeId: String,
    @Valid @RequestBody input: EmployeeInputDto,
    request: HttpServletRequest
  ): ResponseEntity<EmployeeResponseDto> {
    log.info("[${request.method}] - ${request.requestURI}: Update info of employee $employeeId in dynamoDB")

    val employee = employeeMapper.toDomain(input)
    val updatedEmployee = employeeUsecase.update(employeeId, employee)
    val dto = employeeMapper.toDto(updatedEmployee)
    return ResponseEntity.ok(dto)
  }
}