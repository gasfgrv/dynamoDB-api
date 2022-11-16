package br.com.gusta.dynamodb.api.mapper;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.api.model.EmployeeUpdateInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeMapper {

    private final ModelMapper mapper;

    public Employee toEntity(EmployeeInput input) {
        return mapper.map(input, Employee.class);
    }

    public Employee toEntity(EmployeeUpdateInput input) {
        return mapper.map(input, Employee.class);
    }

    public EmployeeDto toDto(Employee input) {
        return mapper.map(input, EmployeeDto.class);
    }

}
