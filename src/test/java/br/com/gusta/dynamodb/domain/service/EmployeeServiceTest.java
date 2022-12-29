package br.com.gusta.dynamodb.domain.service;


import br.com.gusta.dynamodb.domain.exception.EmployeeException;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.repository.EmployeeRepository;
import br.com.gusta.dynamodb.mocks.EmployeeMock;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;

@ExtendWith({MockitoExtension.class, OutputCaptureExtension.class})
class EmployeeServiceTest {

    private static String EMPLOYEE_ID;
    private static Employee EMPLOYEE;
    @Mock
    private EmployeeRepository employeeRepository;

    @Autowired
    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void initTest() {
        EMPLOYEE_ID = EmployeeMock.get().getEmployeeId();
        EMPLOYEE = EmployeeMock.get();
    }

    @Test
    void saveTest(CapturedOutput output) {
        doReturn(EMPLOYEE)
                .when(employeeRepository)
                .save(EMPLOYEE);

        assertThat(employeeService.save(EMPLOYEE))
                .usingRecursiveComparison()
                .isEqualTo(EMPLOYEE);

        assertThat(output.getOut())
                .contains(String.format("Employee %s was saved in DynamoDB", EMPLOYEE_ID));
    }

    @Test
    void getEmployeeByIdTest() {
        doReturn(EMPLOYEE)
                .when(employeeRepository)
                .getEmployeeById(EMPLOYEE_ID);

        assertThatCode(() ->
                assertThat(employeeService.getEmployeeById(EMPLOYEE_ID))
                        .usingRecursiveComparison()
                        .isEqualTo(EMPLOYEE)
        ).doesNotThrowAnyException();

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);
    }

    @Test
    void getEmployeeByIdThrowingExceptionTest() {
        doReturn(null)
                .when(employeeRepository)
                .getEmployeeById(EMPLOYEE_ID);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.getEmployeeById(EMPLOYEE_ID))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee doesn't exists");

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);
    }

    @Test
    void deleteTest(CapturedOutput output) {
        doReturn(EMPLOYEE)
                .when(employeeRepository)
                .getEmployeeById(EMPLOYEE_ID);

        assertThatCode(() -> employeeService.delete(EMPLOYEE))
                .doesNotThrowAnyException();

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);

        verify(employeeRepository, times(1))
                .delete(any(Employee.class));

        assertThat(output.getOut())
                .contains(String.format("Employee %s was deleted data in DynamoDB", EMPLOYEE_ID));
    }

    @Test
    void deleteThrowingExceptionTest() {
        doReturn(null)
                .when(employeeRepository)
                .getEmployeeById(EMPLOYEE_ID);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.delete(EMPLOYEE))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee can't be delected");

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);
    }

    @Test
    void updateTest(CapturedOutput output) {
        doReturn(EMPLOYEE)
                .when(employeeRepository).getEmployeeById(EMPLOYEE_ID);

        EMPLOYEE.setEmail("novo_email@email.com");

        assertThatCode(() -> assertThat(employeeService.update(EMPLOYEE_ID, EMPLOYEE))
                .usingDefaultComparator()
                .hasFieldOrPropertyWithValue("email", "novo_email@email.com")
                .isNotEqualTo(EMPLOYEE.getEmail())
        ).doesNotThrowAnyException();

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);

        verify(employeeRepository, times(1))
                .update(anyString(), any(Employee.class));

        assertThat(output.getOut())
                .contains(String.format("Employee %s was update data in DynamoDB", EMPLOYEE_ID));
    }

    @Test
    void updateThrowingExceptionTest() {
        doReturn(null)
                .when(employeeRepository).getEmployeeById(EMPLOYEE_ID);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.update(EMPLOYEE_ID, EMPLOYEE))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee can't update his data");

        verify(employeeRepository, times(1))
                .getEmployeeById(EMPLOYEE_ID);
    }

}