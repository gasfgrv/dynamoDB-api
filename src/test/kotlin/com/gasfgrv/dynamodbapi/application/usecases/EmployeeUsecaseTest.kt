package com.gasfgrv.dynamodbapi.application.usecases

import com.gasfgrv.dynamodbapi.domain.exceptions.EmployeeException
import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.domain.models.enums.EmployeeErrorStatus
import com.gasfgrv.dynamodbapi.domain.ports.outputs.EmployeeRepositoryPort
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.verify
import java.util.Optional
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

@ExtendWith(MockKExtension::class)
class EmployeeUsecaseTest {
  @MockK
  private lateinit var repository: EmployeeRepositoryPort

  @InjectMockKs
  private lateinit var usecase: EmployeeUsecase

  private val employeeId = "123"
  private val employee = Employee(
    employeeId,
    "João",
    "Silva",
    "joao@email.com",
    Department("TI", "D01")
  )

  @Test
  fun `should save employee successfully`() {
    every { repository.save(any()) } returns employee

    val result = usecase.save(employee)

    assertEquals(employee, result)
    verify(exactly = 1) { repository.save(employee) }
  }

  @Test
  fun `should return employee when found by id`() {
    every { repository.getEmployeeById(employeeId) } returns Optional.of(employee)

    val result = usecase.getEmployeeById(employeeId)

    assertEquals(employee, result)
    verify(exactly = 1) { repository.getEmployeeById(employeeId) }
  }

  @Test
  fun `should throw NOT_FOUND exception when employee does not exist`() {
    every { repository.getEmployeeById(employeeId) } returns Optional.empty()

    val exception = assertThrows(EmployeeException::class.java) {
      usecase.getEmployeeById(employeeId)
    }

    assertEquals(EmployeeErrorStatus.NOT_FOUND.getMessage(), exception.message)
    verify(exactly = 1) { repository.getEmployeeById(employeeId) }
  }

  @Test
  fun `should delete employee when exists`() {
    every { repository.getEmployeeById(employeeId) } returns Optional.of(employee)
    every { repository.deleteEmployee(any()) } returns Unit

    usecase.delete(employeeId)

    verify { repository.deleteEmployee(employee) }
    verify(exactly = 1) { repository.deleteEmployee(employee) }
    verify(exactly = 1) { repository.getEmployeeById(employeeId) }
  }

  @Test
  fun `should throw CANT_BE_DELETED when deleting non-existent employee`() {
    every { repository.getEmployeeById(employeeId) } returns Optional.empty()

    val exception = assertThrows(EmployeeException::class.java) {
      usecase.delete(employeeId)
    }

    assertEquals(EmployeeErrorStatus.CANT_BE_DELETED.getMessage(), exception.message)
    verify(exactly = 0) { repository.deleteEmployee(any()) }
    verify(exactly = 1) { repository.getEmployeeById(employeeId) }
  }

  @Test
  fun `should update employee when exists`() {
    val updatedData = Employee(
      "any",
      "João Atualizado",
      "Silva",
      "joao.novo@email.com",
      Department("RH", "D02")
    )

    every { repository.getEmployeeById(employeeId) } returns Optional.of(employee)
    every { repository.updateEmployee(any()) } returns updatedData

    val result = usecase.update(employeeId, updatedData)

    assertEquals("João Atualizado", result.firstName())
    verify { repository.updateEmployee(match { it.employeeId() == employeeId }) }
  }

  @Test
  fun `should throw CANT_BE_UPDATED when updating non-existent employee`() {
    every { repository.getEmployeeById(employeeId) } returns Optional.empty()

    val exception = assertThrows(EmployeeException::class.java) {
      usecase.update(employeeId, employee)
    }

    assertEquals(EmployeeErrorStatus.CANT_BE_UPDATED.getMessage(), exception.message)
    verify(exactly = 0) { repository.updateEmployee(any()) }
  }
}