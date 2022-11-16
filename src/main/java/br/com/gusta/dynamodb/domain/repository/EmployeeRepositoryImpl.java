package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ExpectedAttributeValue;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;


@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeRepositoryImpl implements EmployeeRepository {

    private final DynamoDBMapper dynamoDBMapper;

    @Override
    public Employee save(Employee employee) {
        dynamoDBMapper.save(employee);
        return employee;
    }

    @Override
    public Employee getEmployeeById(String employeeId) {
        return dynamoDBMapper.load(Employee.class, employeeId);
    }

    @Override
    public void delete(Employee employee) {
        dynamoDBMapper.delete(employee);
    }

    @Override
    public Employee update(String employeeId, Employee employee) {
        var attributeValue = new AttributeValue().withS(employeeId);
        var expectedAttributeValue = new ExpectedAttributeValue(attributeValue);
        var dynamoDBSaveExpression = new DynamoDBSaveExpression()
                .withExpectedEntry("employeeId", expectedAttributeValue);
        dynamoDBMapper.save(employee, dynamoDBSaveExpression);
        return employee;
    }
}