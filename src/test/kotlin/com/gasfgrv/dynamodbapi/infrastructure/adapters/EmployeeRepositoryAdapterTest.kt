package com.gasfgrv.dynamodbapi.infrastructure.adapters

import com.gasfgrv.dynamodbapi.domain.models.Department
import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity
import com.gasfgrv.dynamodbapi.infrastructure.mappers.EmployeeMapper
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import software.amazon.awssdk.core.pagination.sync.SdkIterable
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest

@ExtendWith(MockKExtension::class)
class EmployeeRepositoryAdapterTest {

  @MockK
  private lateinit var table: DynamoDbTable<EmployeeEntity>

  @MockK
  private lateinit var mapper: EmployeeMapper

  @InjectMockKs
  private lateinit var adapter: EmployeeRepositoryAdapter

  private val employee = Employee(
    "123",
    "João",
    "Silva",
    "joao@email.com",
    Department("TI", "D01")
  )

  private val entity = EmployeeEntity().apply {
    employeeId = employee.employeeId()
    email = employee.email()
  }

  @Test
  fun `should save employee successfully`() {
    every { mapper.toEntity(employee) } returns entity
    every { table.putItem(any<PutItemEnhancedRequest<EmployeeEntity>>()) } returns Unit
    every { mapper.toDomain(entity) } returns employee

    val result = adapter.save(employee)

    assertEquals(employee, result)
    verify { table.putItem(match<PutItemEnhancedRequest<EmployeeEntity>> { it.item() == entity }) }
  }

  @Test
  fun `should get employee by id successfully`() {
    val mockPageIterable = mockk<PageIterable<EmployeeEntity>>()
    val mockSdkIterable = mockk<SdkIterable<EmployeeEntity>>()
    val mockIterator = mutableListOf(entity).iterator()

    every { table.query(any<QueryConditional>()) } returns mockPageIterable
    every { mockPageIterable.items() } returns mockSdkIterable
    every { mockSdkIterable.iterator() } returns mockIterator
    every { mapper.toDomain(entity) } returns employee

    val result = adapter.getEmployeeById("123")

    assertTrue(result.isPresent)
    assertEquals(employee, result.get())
  }

  @Test
  fun `should delete employee successfully`() {
    every { table.deleteItem(any<Key>()) } returns entity

    adapter.deleteEmployee(employee)

    verify {
      table.deleteItem(match<Key> {
        it.partitionKeyValue().s() == "123" &&
        it.sortKeyValue().get().s() == employee.email()
      })
    }
  }

  @Test
  fun `should update employee successfully`() {
    every { mapper.toEntity(employee) } returns entity
    every { table.updateItem(any<UpdateItemEnhancedRequest<EmployeeEntity>>()) } returns entity
    every { mapper.toDomain(entity) } returns employee

    val result = adapter.updateEmployee(employee)

    assertEquals(employee, result)
    verify { table.updateItem(match<UpdateItemEnhancedRequest<EmployeeEntity>> { it.item() == entity  }) }
  }
}