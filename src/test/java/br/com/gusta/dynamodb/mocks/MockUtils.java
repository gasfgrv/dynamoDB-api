package br.com.gusta.dynamodb.mocks;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Department;
import br.com.gusta.dynamodb.domain.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.experimental.UtilityClass;

@UtilityClass
public class MockUtils {

    public Employee getEmployeeEntity() {
        return new Employee(
                "a735b69d-d6e9-45af-a876-3f3a545ea612",
                "Gustavo",
                "Silva",
                "gustavo.silva@email.com",
                new Department("IT", "DEP_0042")
        );
    }

    public EmployeeDto getEmployeeDto() {
        var employee = getEmployeeEntity();

        var dto = new EmployeeDto();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartamentCode(employee.getDepartment().getDepartmentCode());
        dto.setDepartamentName(employee.getDepartment().getDepartmentName());

        return dto;
    }

    public EmployeeInput getEmployeeInput() {
        var employee = getEmployeeEntity();

        var input = new EmployeeInput();
        input.setEmployeeId(employee.getEmployeeId());
        input.setFirstName(employee.getFirstName());
        input.setLastName(employee.getLastName());
        input.setEmail(employee.getEmail());
        input.setDepartment(employee.getDepartment());

        return input;
    }

    public String getJson(Object content) {
        try {
            return new ObjectMapper().writeValueAsString(content);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

}
