package br.com.gusta.dynamodb.api.mapper;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.mocks.MockUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@Profile("test")
@ExtendWith(SpringExtension.class)
class EmployeeMapperTest {

    @InjectMocks
    private EmployeeMapper mapper;

    @Mock
    private ModelMapper modelMapper;

    public Employee employee;

    public EmployeeDto employeeDto;

    public EmployeeInput employeeInput;

    @BeforeEach
    void setUp() {
        employee = MockUtils.getEmployeeEntity();
        employeeDto = MockUtils.getEmployeeDto();
        employeeInput = MockUtils.getEmployeeInput();
    }

    @Test
    void toEntityTest() {
        given(modelMapper.map(employeeInput, Employee.class)).willReturn(employee);

        assertThat(mapper.toEntity(employeeInput)).isInstanceOf(Employee.class);

        verify(modelMapper, times(1)).map(employeeInput, Employee.class);
    }

    @Test
    void toDtoTest() {
        given(modelMapper.map(employee, EmployeeDto.class)).willReturn(employeeDto);

        assertThat(mapper.toDto(employee)).isInstanceOf(EmployeeDto.class);

        verify(modelMapper, times(1)).map(employee, EmployeeDto.class);
    }
}