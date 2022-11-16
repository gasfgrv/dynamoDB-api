package br.com.gusta.dynamodb.domain.repository;

import br.com.gusta.dynamodb.domain.model.Employee;

public interface EmployeeRepository {

    Employee save(Employee employee);

    Employee getEmployeeById(String employeeId);

    void delete(Employee employee);

    Employee update(String employeeId, Employee employee);

}
