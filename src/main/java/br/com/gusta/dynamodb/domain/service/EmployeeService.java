package br.com.gusta.dynamodb.domain.service;

import br.com.gusta.dynamodb.domain.exception.EmployeeException;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.repository.EmployeeRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public Employee save(Employee employee) {
        employeeRepository.save(employee);

        LOGGER.info("Employee {} was saved in DynamoDB", employee.getEmployeeId());

        return employee;
    }

    public Employee getEmployeeById(String employeeId) {
        return Optional.ofNullable(employeeRepository.getEmployeeById(employeeId))
                .orElseThrow(() -> new EmployeeException("This employee doesn't exists"));
    }

    public void delete(String employeeId) {
        var employee = employeeExists(employeeId);

        if (employee.isEmpty()) {
            throw new EmployeeException("This employee can't be deleted");
        }

        employeeRepository.delete(employee.get());

        LOGGER.info("Employee {} was deleted data in DynamoDB", employee.get().getEmployeeId());
    }

    public Employee update(String employeeId, Employee employee) {
        if (employeeExists(employeeId).isEmpty()) {
            throw new EmployeeException("This employee can't update his data");
        }

        employeeRepository.update(employeeId, employee);

        LOGGER.info("Employee {} was update data in DynamoDB", employee.getEmployeeId());

        return employee;
    }

    private Optional<Employee> employeeExists(String employeeId) {
        Employee employeeById = employeeRepository.getEmployeeById(employeeId);

        return Optional.ofNullable(employeeById);
    }

}
