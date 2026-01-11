package com.gasfgrv.dynamodbapi.infrastructure.adapters

import com.gasfgrv.dynamodbapi.domain.models.Employee
import com.gasfgrv.dynamodbapi.domain.ports.outputs.EmployeeRepositoryPort
import com.gasfgrv.dynamodbapi.infrastructure.entities.EmployeeEntity
import com.gasfgrv.dynamodbapi.infrastructure.mappers.EmployeeMapper
import java.util.Optional
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Repository
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable
import software.amazon.awssdk.enhanced.dynamodb.Key
import software.amazon.awssdk.enhanced.dynamodb.model.PutItemEnhancedRequest
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional
import software.amazon.awssdk.enhanced.dynamodb.model.UpdateItemEnhancedRequest

@Repository
class EmployeeRepositoryAdapter(
  private val table: DynamoDbTable<EmployeeEntity>,
  private val mapper: EmployeeMapper
) : EmployeeRepositoryPort {
  companion object {
    private val log = LoggerFactory.getLogger(EmployeeRepositoryAdapter::class.java)
  }

  override fun save(employee: Employee): Employee {
    val entity = mapper.toEntity(employee)

    val request = PutItemEnhancedRequest
      .builder(EmployeeEntity::class.java)
      .item(entity)
      .build()

    table.putItem(request)

    log.info("Saved employee: ${employee.employeeId()}")
    return mapper.toDomain(entity)
  }

  override fun getEmployeeById(id: String): Optional<Employee> {
    val key = Key
      .builder()
      .addPartitionValue(id)
      .build()

    val request = QueryConditional
      .keyEqualTo(key)

    val entity = table
      .query(request)
      .items()
      .first()

    val employee = mapper.toDomain(entity)

    log.info("Got employee: ${employee.employeeId()}")
    return Optional.ofNullable(employee)
  }

  override fun deleteEmployee(employee: Employee) {
    val key = Key
      .builder()
      .addPartitionValue(employee.employeeId())
      .addSortValue(employee.email())
      .build()

    table.deleteItem(key)
    log.info("Deleted employee: ${employee.employeeId()}")
  }

  override fun updateEmployee(employee: Employee): Employee {
    val entity = mapper.toEntity(employee)

    val request = UpdateItemEnhancedRequest
      .builder(EmployeeEntity::class.java)
      .item(entity)
      .build()

    val response = table.updateItem(request)

    log.info("Updated employee: ${employee.employeeId()}")
    return mapper.toDomain(response)
  }
}