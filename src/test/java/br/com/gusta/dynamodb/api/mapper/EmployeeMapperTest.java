package br.com.gusta.dynamodb.api.mapper;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.mocks.EmployeeMock;
import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doReturn;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {EmployeeMapper.class, ModelMapper.class})
class EmployeeMapperTest {

    public static Employee EMPLOYEE;

    public static EmployeeDto EMPLOYEE_DTO;

    public static EmployeeInput EMPLOYEE_INPUT;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private EmployeeMapper mapper;

    @BeforeEach
    void setUp() {
        EMPLOYEE = EmployeeMock.get();
        EMPLOYEE_DTO = EmployeeMock.getDto();
        EMPLOYEE_INPUT = EmployeeMock.getInput();
    }

    @Test
    void toEntityTest() {
        doReturn(EMPLOYEE)
                .when(modelMapper)
                .map(EMPLOYEE_INPUT, EMPLOYEE.getClass());

        assertThat(mapper.toEntity(EMPLOYEE_INPUT))
                .isInstanceOf(Employee.class);
    }

    @Test
    void toDtoTest() {
        doReturn(EMPLOYEE_DTO)
                .when(modelMapper)
                .map(EMPLOYEE, EMPLOYEE_DTO.getClass());

        assertThat(mapper.toDto(EMPLOYEE))
                .isInstanceOf(EmployeeDto.class);
    }
}