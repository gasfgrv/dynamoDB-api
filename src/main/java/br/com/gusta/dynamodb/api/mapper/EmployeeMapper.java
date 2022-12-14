package br.com.gusta.dynamodb.api.mapper;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMapper {

    private final ModelMapper mapper;

    public Employee toEntity(EmployeeInput input) {
        return mapper.map(input, Employee.class);
    }

    public EmployeeDto toDto(Employee input) {
        return mapper.map(input, EmployeeDto.class);
    }

}
