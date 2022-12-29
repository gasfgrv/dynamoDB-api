package br.com.gusta.dynamodb.mocks;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Departament;
import br.com.gusta.dynamodb.domain.model.Employee;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EmployeeMock {

    public Employee get() {
        return new Employee(
                "a735b69d-d6e9-45af-a876-3f3a545ea612",
                "Gustavo",
                "Silva",
                "gustavo.silva@email.com",
                new Departament("DEP_0042", "IT")
        );
    }

    public EmployeeDto getDto() {
        var employee = get();

        var dto = new EmployeeDto();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setDepartamentCode(employee.getDepartament().getDepartamentCode());
        dto.setDepartamentName(employee.getDepartament().getDepartamentName());

        return dto;
    }

    public EmployeeInput getInput() {
        var employee = get();

        var input = new EmployeeInput();
        input.setFirstName(employee.getFirstName());
        input.setLastName(employee.getLastName());
        input.setEmail(employee.getEmail());
        input.setDepartament(employee.getDepartament());

        return input;
    }

}
