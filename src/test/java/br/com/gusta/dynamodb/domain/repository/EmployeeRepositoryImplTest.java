package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.mocks.MockUtils;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBSaveExpression;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@Profile("test")
@ExtendWith(SpringExtension.class)
class EmployeeRepositoryImplTest {

    @InjectMocks
    private EmployeeRepositoryImpl employeeRepository;

    @Mock
    private DynamoDBMapper dynamoDBMapper;

    public Employee employee;

    private String employeeId;

    @BeforeEach
    void initTest() {
        employeeId = MockUtils.getEmployeeEntity().getEmployeeId();
        employee = MockUtils.getEmployeeEntity();
    }

    @Test
    void saveTest() {
        assertThatCode(() -> employeeRepository.save(employee)).doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1)).save(any(Employee.class));
    }

    @Test
    void getEmployeeByIdTest() {
        given(dynamoDBMapper.load(Employee.class, employeeId)).willReturn(employee);

        assertThat(employeeRepository.getEmployeeById(employeeId))
                .usingRecursiveComparison()
                .isEqualTo(employee);

        verify(dynamoDBMapper, times(1)).load(Employee.class, employeeId);
    }

    @Test
    void getEmployeeByIdNotExistingTest() {
        given(dynamoDBMapper.load(Employee.class, employeeId)).willReturn(null);

        assertThat(employeeRepository.getEmployeeById(employeeId)).isNull();

        verify(dynamoDBMapper, times(1)).load(Employee.class, employeeId);
    }

    @Test
    void deleteTest() {
        assertThatCode(() -> employeeRepository.delete(employee)).doesNotThrowAnyException();

        verify(dynamoDBMapper, times(1)).delete(any(Employee.class));
    }

    @Test
    void updateTest() {
        employee.setEmail("novoemailgusta@email.com");

        assertThat(employeeRepository.update(employeeId, employee))
                .usingDefaultComparator()
                .hasFieldOrPropertyWithValue("email", "novoemailgusta@email.com");

        verify(dynamoDBMapper, times(1)).save(any(Employee.class), any(DynamoDBSaveExpression.class));
    }
}