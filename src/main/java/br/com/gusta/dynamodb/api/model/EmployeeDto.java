package br.com.gusta.dynamodb.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeDto {

    private String firstName;
    private String lastName;
    private String email;
    private String departamentName;
    private String departamentCode;

}
