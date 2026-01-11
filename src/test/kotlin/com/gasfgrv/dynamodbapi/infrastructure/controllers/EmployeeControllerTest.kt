package com.gasfgrv.dynamodbapi.infrastructure.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.gasfgrv.dynamodbapi.application.usecases.EmployeeUsecase
import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.infrastructure.configurations.TestsConfiguration
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeInputDto
import com.gasfgrv.dynamodbapi.infrastructure.dtos.EmployeeResponseDto
import com.gasfgrv.dynamodbapi.infrastructure.mappers.EmployeeMapper
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(EmployeeController::class)
@Import(TestsConfiguration::class)
class EmployeeControllerTest {
  @Autowired
  private lateinit var mockMvc: MockMvc

  @Autowired
  private lateinit var objectMapper: ObjectMapper

  @MockkBean
  private lateinit var employeeUsecase: EmployeeUsecase

  @MockkBean
  private lateinit var employeeMapper: EmployeeMapper

  private val version = "v1"
  private val employeeId = "123-abc"

  @Test
  fun `should save employee and return created`() {
    val inputDto = EmployeeInputDto(
      employeeId,
      "João",
      "Silva",
      "joao@email.com",
      "TI",
      "D01"
    )
    val domainEmployee = Employee(
      employeeId,
      "João",
      "Silva",
      "joao@email.com",
      Department("TI", "D01")
    )
    val responseDto = EmployeeResponseDto(
      "João",
      "Silva",
      "joao@email.com",
      "TI",
      "D01"
    )

    every { employeeMapper.toDomain(any<EmployeeInputDto>()) } returns domainEmployee
    every { employeeUsecase.save(any<Employee>()) } returns domainEmployee
    every { employeeMapper.toDto(any<Employee>()) } returns responseDto

    mockMvc.perform(
      post("/$version/employees")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(inputDto)))
      .andExpect(status().isCreated)
      .andExpect(header().exists("Location"))

    verify(exactly = 1) { employeeUsecase.save(domainEmployee) }
  }

  @Test
  fun `should get employee by id`() {
    val domainEmployee = Employee(
      employeeId,
      "João",
      "Silva",
      "joao@email.com",
      Department("TI", "D01")
    )
    val responseDto = EmployeeResponseDto(
      "João",
      "Silva",
      "joao@email.com",
      "TI",
      "D01"
    )

    every { employeeUsecase.getEmployeeById(employeeId) } returns domainEmployee
    every { employeeMapper.toDto(domainEmployee) } returns responseDto

    mockMvc.perform(get("/$version/employees/$employeeId"))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.first_name").value("João"))
      .andExpect(jsonPath("$.last_name").value("Silva"))

    verify(exactly = 1) { employeeUsecase.getEmployeeById(employeeId) }
  }

  @Test
  fun `should delete employee`() {
    every { employeeUsecase.delete(employeeId) } returns Unit

    mockMvc.perform(delete("/$version/employees/$employeeId"))
      .andExpect(status().isNoContent)

    verify(exactly = 1) { employeeUsecase.delete(employeeId) }
  }

  @Test
  fun `should update employee`() {
    val inputDto = EmployeeInputDto(
      employeeId,
      "João",
      "Silva",
      "joao_v2@email.com",
      "TI",
      "D01"
    )
    val domainEmployee = Employee(
      employeeId,
      "João",
      "Silva",
      "joao@email.com",
      Department("TI", "D01")
    )
    val responseDto = EmployeeResponseDto(
      "João",
      "Silva",
      "joao_v2@email.com",
      "TI",
      "D01"
    )

    every { employeeMapper.toDomain(inputDto) } returns domainEmployee
    every { employeeUsecase.update(employeeId, domainEmployee) } returns domainEmployee
    every { employeeMapper.toDto(domainEmployee) } returns responseDto

    mockMvc.perform(
      put("/$version/employees/$employeeId")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(inputDto)))
      .andExpect(status().isOk)
      .andExpect(jsonPath("$.first_name").value("João"))
      .andExpect(jsonPath("$.last_name").value("Silva"))
      .andExpect(jsonPath("$.email").value("joao_v2@email.com"))

    verify(exactly = 1) { employeeUsecase.update(employeeId, domainEmployee) }
  }
}