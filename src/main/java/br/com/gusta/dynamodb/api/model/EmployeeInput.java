package br.com.gusta.dynamodb.api.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import br.com.gusta.dynamodb.domain.model.Departament;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeInput {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @NotBlank
    private String email;

    @NotNull
    private Departament departament;
}
