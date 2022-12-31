package br.com.gusta.dynamodb.domain.service;


import br.com.gusta.dynamodb.domain.exception.EmployeeException;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.repository.EmployeeRepository;
import br.com.gusta.dynamodb.mocks.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;

@Profile("test")
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    private String employeeId;

    private Employee employee;

    @BeforeEach
    void initTest() {
        employeeId = MockUtils.getEmployeeEntity().getEmployeeId();
        employee = MockUtils.getEmployeeEntity();
    }

    @Test
    void saveTest(CapturedOutput output) {
        given(employeeRepository.save(employee)).willReturn(employee);

        assertThat(employeeService.save(employee))
                .usingRecursiveComparison()
                .isEqualTo(employee);

        assertThat(output.getOut()).contains(String.format("Employee %s was saved in DynamoDB", employeeId));

        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void getEmployeeByIdTest() {
        given(employeeRepository.getEmployeeById(employeeId)).willReturn(employee);

        assertThatCode(() -> assertThat(employeeService.getEmployeeById(employeeId))
                .usingRecursiveComparison()
                .isEqualTo(employee))
                .doesNotThrowAnyException();

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void getEmployeeByIdThrowingExceptionTest() {
        given(employeeRepository.getEmployeeById(employeeId)).willReturn(null);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.getEmployeeById(employeeId))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee doesn't exists");

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void deleteTest(CapturedOutput output) {
        given(employeeRepository.getEmployeeById(employeeId)).willReturn(employee);

        assertThatCode(() -> employeeService.delete(employeeId)).doesNotThrowAnyException();

        assertThat(output.getOut()).contains(String.format("Employee %s was deleted data in DynamoDB", employeeId));

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);

        verify(employeeRepository, times(1)).delete(any(Employee.class));
    }

    @Test
    void deleteThrowingExceptionTest() {
        given(employeeRepository.getEmployeeById(employeeId)).willReturn(null);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.delete(employeeId))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee can't be deleted");

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

    @Test
    void updateTest(CapturedOutput output) {
        employee.setEmail("novo_email@email.com");

        given(employeeRepository.getEmployeeById(employeeId)).willReturn(employee);

        assertThatCode(() -> assertThat(employeeService.update(employeeId, employee))
                .usingDefaultComparator()
                .hasFieldOrPropertyWithValue("email", "novo_email@email.com")
                .isNotEqualTo(employee.getEmail()))
                .doesNotThrowAnyException();

        assertThat(output.getOut()).contains(String.format("Employee %s was update data in DynamoDB", employeeId));

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);

        verify(employeeRepository, times(1)).update(anyString(), any(Employee.class));
    }

    @Test
    void updateThrowingExceptionTest() {
        given(employeeRepository.getEmployeeById(employeeId)).willReturn(null);

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeService.update(employeeId, employee))
                .isInstanceOf(RuntimeException.class)
                .withMessage("This employee can't update his data");

        verify(employeeRepository, times(1)).getEmployeeById(employeeId);
    }

}