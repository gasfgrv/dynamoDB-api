package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.mocks.EmployeeMock;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EmployeeRepositoryImplTest {

    public static Employee EMPLOYEE;

    private static String EMPLOYEE_ID;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    @InjectMocks
    private EmployeeRepositoryImpl employeeRepository;

    @BeforeEach
    void initTest() {
        EMPLOYEE_ID = EmployeeMock.get().getEmployeeId();
        EMPLOYEE = EmployeeMock.get();
    }

    @Test
    void saveTest() {
        assertThatCode(() -> employeeRepository.save(EMPLOYEE))
                .doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1))
                .save(any(Employee.class));
    }

    @Test
    void getEmployeeByIdTest() {
        doReturn(EMPLOYEE)
                .when(dynamoDBMapper)
                .load(Employee.class, EMPLOYEE_ID);

        assertThat(employeeRepository.getEmployeeById(EMPLOYEE_ID))
                .usingRecursiveComparison()
                .isEqualTo(EMPLOYEE);

        verify(dynamoDBMapper, times(1))
                .load(Employee.class, EMPLOYEE_ID);
    }

    @Test
    void getEmployeeByIdNotExistingTest() {
        doReturn(null)
                .when(dynamoDBMapper)
                .load(Employee.class, EMPLOYEE_ID);

        assertThat(employeeRepository.getEmployeeById(EMPLOYEE_ID)).isNull();

        verify(dynamoDBMapper, times(1))
                .load(Employee.class, EMPLOYEE_ID);
    }

    @Test
    void deleteTest() {
        assertThatCode(() -> employeeRepository.delete(EMPLOYEE))
                .doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1))
                .delete(any(Employee.class));
    }

    @Test
    void updateTest() {
        var newEmail = "novoemailgusta@email.com";

        var updatedEmployee = EMPLOYEE;
        updatedEmployee.setEmail(newEmail);

        assertThat(employeeRepository.update(EMPLOYEE_ID, updatedEmployee))
                .usingDefaultComparator()
                .hasFieldOrPropertyWithValue("email", newEmail);

        verify(dynamoDBMapper, times(1))
                .save(any(Employee.class), any(DynamoDBSaveExpression.class));
    }
}