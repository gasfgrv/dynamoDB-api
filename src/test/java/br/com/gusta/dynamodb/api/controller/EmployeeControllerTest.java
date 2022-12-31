package br.com.gusta.dynamodb.api.controller;

import br.com.gusta.dynamodb.api.mapper.EmployeeMapper;
import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.exception.EmployeeException;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.service.EmployeeService;
import br.com.gusta.dynamodb.mocks.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.system.CapturedOutput;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.BDDMockito.verify;
import static org.mockito.BDDMockito.willThrow;
import static org.mockito.Mockito.doNothing;


@Profile("test")
@ExtendWith({SpringExtension.class, OutputCaptureExtension.class})
class EmployeeControllerTest {

    @InjectMocks
    private EmployeeController employeeController;

    @Mock
    private EmployeeService employeeService;

    @Mock
    private EmployeeMapper employeeMapper;

    private Employee employee;

    private String employeeId;

    private EmployeeInput employeeInput;

    private EmployeeDto employeeDto;

    @BeforeEach
    void setUp() {
        employee = MockUtils.getEmployeeEntity();
        employeeId = MockUtils.getEmployeeEntity().getEmployeeId();
        employeeInput = MockUtils.getEmployeeInput();
        employeeDto = MockUtils.getEmployeeDto();
    }

    @Test
    void saveEmployeeTest(CapturedOutput output) {
        given(employeeMapper.toEntity(any(EmployeeInput.class))).willReturn(employee);

        given(employeeService.save(any(Employee.class))).willReturn(employee);

        var employeeResponseEntity = employeeController.saveEmployee(employeeInput);

        assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(employeeResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(employee);

        assertThat(output.getOut()).contains("Salving employee in dynamoDB");

        verify(employeeMapper, times(1)).toEntity(any(EmployeeInput.class));

        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void getEmployeeTest(CapturedOutput output) {
        given(employeeService.getEmployeeById(employeeId)).willReturn(employee);

        given(employeeMapper.toDto(employee)).willReturn(employeeDto);

        var employeeResponseEntity = employeeController.getEmployee(employeeId);

        assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(employeeResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(employeeDto);

        assertThat(output.getOut()).contains(String.format("Getting info of employee %s in dynamoDB", employeeId));

        verify(employeeService, times(1)).getEmployeeById(employeeId);

        verify(employeeMapper, times(1)).toDto(employee);
    }

    @Test
    void getEmployeeThrowingExceptionTest(CapturedOutput output) {
        employeeId = "123456789";

        given(employeeService.getEmployeeById(employeeId))
                .willThrow(new EmployeeException("This employee doesn't exists"));

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeController.getEmployee(employeeId))
                .withMessage("This employee doesn't exists");

        assertThat(output.getOut()).contains(String.format("Getting info of employee %s in dynamoDB", employeeId));

        verify(employeeService, times(1)).getEmployeeById(employeeId);

        verify(employeeMapper, times(0)).toDto(any(Employee.class));
    }

    @Test
    void deleteEmployeeTest(CapturedOutput output) {
        doNothing().when(employeeService).delete(employeeId);

        var employeeResponseEntity = employeeController.deleteEmployee(employeeId);

        assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

        assertThat(output.getOut()).contains(String.format("Deleting info of employee %s in dynamoDB", employeeId));

        verify(employeeService, times(1)).delete(employeeId);
    }

    @Test
    void deleteThrowingExceptionTest() {
        willThrow(new EmployeeException("This employee can't be deleted"))
                .given(employeeService).delete(anyString());

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeController.deleteEmployee(employeeId))
                .withMessage("This employee can't be deleted");

        verify(employeeService, times(1)).delete(anyString());
    }

    @Test
    void updateEmployeeTest(CapturedOutput output) {
        var newEmail = "novo_email@email.com";

        employeeInput.setEmail(newEmail);

        employee.setEmail(newEmail);

        employeeDto.setEmail(newEmail);

        given(employeeMapper.toEntity(any(EmployeeInput.class))).willReturn(employee);

        given(employeeService.update(anyString(), any(Employee.class))).willReturn(employee);

        given(employeeMapper.toDto(any(Employee.class))).willReturn(employeeDto);

        var employeeResponseEntity = employeeController.updateEmployee(employeeId, employeeInput);

        assertThat(employeeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        assertThat(employeeResponseEntity.getBody())
                .isNotNull()
                .usingRecursiveComparison()
                .isEqualTo(employeeDto);

        assertThat(output.getOut()).contains(String.format("Update info of employee %s in dynamoDB", employeeId));

        verify(employeeMapper, times(1)).toEntity(any(EmployeeInput.class));

        verify(employeeService, times(1)).update(anyString(), any(Employee.class));

        verify(employeeMapper, times(1)).toDto(any(Employee.class));
    }

    @Test
    void updateThrowingExceptionTest() {
        given(employeeMapper.toEntity(any(EmployeeInput.class))).willReturn(employee);

        given(employeeService.update(anyString(), any(Employee.class)))
                .willThrow(new EmployeeException("This employee can't update his data"));

        assertThatExceptionOfType(EmployeeException.class)
                .isThrownBy(() -> employeeController.updateEmployee(employeeId, employeeInput))
                .withMessage("This employee can't update his data");

        verify(employeeMapper, times(1)).toEntity(any(EmployeeInput.class));

        verify(employeeService, times(1)).update(anyString(), any(Employee.class));

        verify(employeeMapper, times(0)).toDto(any(Employee.class));

    }
}