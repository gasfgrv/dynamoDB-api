package br.com.gusta.dynamodb.api.model;

import br.com.gusta.dynamodb.domain.model.Departament;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EmployeeUpdateInput {

    @NotBlank
    private String employeeId;

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotBlank
    private Departament departament;

}
