package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Override
    @CacheEvict("employee")
    public Employee save(Employee employee) {
        if (employee.getEmployeeId() == null) {
            employee.setEmployeeId(UUID.randomUUID().toString());
        }

        dynamoDBMapper.save(employee);

        return employee;
    }

    @Override
    @Cacheable("employee")
    public Employee getEmployeeById(String employeeId) {
        return dynamoDBMapper.load(Employee.class, employeeId);
    }

    @Override
    @CacheEvict("employee")
    public void delete(Employee employee) {
        dynamoDBMapper.delete(employee);
    }

    @Override
    @CacheEvict("employee")
    public Employee update(String employeeId, Employee employee) {
        var attributeValue = new AttributeValue().withS(employeeId);

        var expectedAttributeValue = new ExpectedAttributeValue(attributeValue);

        var dynamoDBSaveExpression = new DynamoDBSaveExpression()
                .withExpectedEntry("employeeId", expectedAttributeValue);

        dynamoDBMapper.save(employee, dynamoDBSaveExpression);

        return employee;
    }
}