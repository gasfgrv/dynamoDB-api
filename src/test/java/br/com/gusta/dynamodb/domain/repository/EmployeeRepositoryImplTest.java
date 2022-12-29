package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.mocks.EmployeeMock;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

@ExtendWith(MockitoExtension.class)
class EmployeeRepositoryImplTest {

    @Mock
    private DynamoDBMapper dynamoDBMapper;
    @Autowired
    @InjectMocks
    private EmployeeRepositoryImpl employeeRepository;

    @Test
    void saveTest() {
        assertThatCode(() -> employeeRepository.save(EmployeeMock.get()))
                .doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1))
                .save(any(Employee.class));
    }

    @Test
    void getEmployeeByIdTest() {
        doReturn(EmployeeMock.get()).when(dynamoDBMapper)
                .load(Employee.class, EmployeeMock.get().getEmployeeId());

        var expected = EmployeeMock.get();

        var actual = employeeRepository.getEmployeeById(EmployeeMock.get().getEmployeeId());

        verify(dynamoDBMapper, times(1))
                .load(Employee.class, EmployeeMock.get().getEmployeeId());

        assertThat(actual)
                .usingRecursiveComparison()
                .isEqualTo(expected);

    }

    @Test
    void getEmployeeByIdNotExistingTest() {
        doReturn(null).when(dynamoDBMapper)
                .load(Employee.class, EmployeeMock.get().getEmployeeId());

        var employee = employeeRepository.getEmployeeById(EmployeeMock.get().getEmployeeId());

        verify(dynamoDBMapper, times(1))
                .load(Employee.class, EmployeeMock.get().getEmployeeId());

        assertThat(employee).isNull();
    }

    @Test
    void deleteTest() {
        assertThatCode(() -> employeeRepository.delete(EmployeeMock.get()))
                .doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1))
                .delete(any(Employee.class));
    }

    @Test
    void updateTest() {
        var newEmail = "novoemailgusta@email.com";

        var updatedEmployee = EmployeeMock.get();
        updatedEmployee.setEmail(newEmail);

        var employee = employeeRepository.update(EmployeeMock.get().getEmployeeId(), updatedEmployee);

        verify(dynamoDBMapper, times(1))
                .save(any(Employee.class), any(DynamoDBSaveExpression.class));

        assertThat(employee.getEmail()).isEqualTo(newEmail);
    }
}