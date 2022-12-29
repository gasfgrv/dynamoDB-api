package br.com.gusta.dynamodb.mocks;

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

}
