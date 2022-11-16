package br.com.gusta.dynamodb.domain.service;

import br.com.gusta.dynamodb.domain.exception.EmployeeException;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        if (employeeExists(employee).isPresent()) {
            throw new EmployeeException("This employee already exists");
        }

        employeeRepository.save(employee);
        log.info("Employee {} was saved in DynamoDB", employee.getEmployeeId());
        return employee;
    }

    public Employee getEmployeeById(String employeeId) {
        return Optional.ofNullable(employeeRepository.getEmployeeById(employeeId))
                .orElseThrow(() -> new EmployeeException("This employee doesn't exists"));
    }

    public void delete(Employee employee) {
        if (employeeExists(employee).isEmpty()) {
            throw new EmployeeException("This employee can't be delected");
        }

        employeeRepository.delete(employee);
        log.info("Employee {} was deleted data in DynamoDB", employee.getEmployeeId());
    }

    public Employee update(String employeeId, Employee employee) {
        if (employeeExists(employee).isEmpty()) {
            throw new EmployeeException("This employee can't update his data");
        }

        employeeRepository.update(employeeId, employee);
        log.info("Employee {} was update data in DynamoDB", employee.getEmployeeId());
        return employee;
    }

    private Optional<Employee> employeeExists(Employee employee) {
        Employee employeeById = employeeRepository.getEmployeeById(employee.getEmployeeId());
        return Optional.ofNullable(employeeById);
    }

}
