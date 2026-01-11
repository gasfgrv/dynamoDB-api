package com.gasfgrv.dynamodbapi.infrastructure.mappers

import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeInputDto
import com.gasfgrv.dynamodbapi.infrastructure.entities.DepartmentEntity
import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class EmployeeMapperImplTest {
  private val mapper = EmployeeMapperImpl()

  @Test
  fun `should mapping Domain to Entity`() {
    val domain = Employee(
      "ID-123", "João", "Silva", "joao@email.com",
      Department("TI", "DEPT-01")
    )

    val result = mapper.toEntity(domain)

    assertEquals(domain.employeeId(), result.employeeId)
    assertEquals(domain.firstName(), result.firstName)
    assertEquals(domain.lastName(), result.lastName)
    assertEquals(domain.email(), result.email)
    assertEquals("TI", result.department?.departmentName)
    assertEquals("DEPT-01", result.department?.departmentCode)
  }

  @Test
  fun `should mapping Entity to Domain`() {
    val entity = EmployeeEntity(
      "ID-123", "Maria", "Souza", "maria@email.com",
      DepartmentEntity("RH", "DEPT-02")
    )

    val result = mapper.toDomain(entity)

    assertEquals(entity.employeeId, result.employeeId())
    assertEquals(entity.firstName, result.firstName())
    assertEquals("RH", result.departmentName())
  }

  @Test
  fun `should generate new UUID when InputDto don't have employeeId`() {
    val input = EmployeeInputDto(
      employeeId = null,
      firstName = "Carlos",
      lastName = "Oliveira",
      email = "carlos@email.com",
      departmentName = "Vendas",
      departmentCode = "VND"
    )

    val result = mapper.toDomain(input)

    assertNotNull(result.employeeId())
    assertEquals(36, result.employeeId().length) // Tamanho padrão de um UUID String
    assertEquals("Carlos", result.firstName())
  }

  @Test
  fun `should mapping Domain to ResponseDto`() {
    val domain = Employee(
      "ID-789", "Ana", "Costa", "ana@email.com",
      Department("Financeiro", "FIN")
    )

    val result = mapper.toDto(domain)

    assertEquals(domain.firstName(), result.firstName)
    assertEquals(domain.departmentCode(), result.departmentCode)
  }
}