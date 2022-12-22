package br.com.gusta.dynamodb.api.controller;

import br.com.gusta.dynamodb.api.mapper.EmployeeMapper;
import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.api.model.EmployeeUpdateInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody EmployeeInput employee) {
        log.info("Salving employee in dynamoDB");

        var employeeEntity = employeeMapper.toEntity(employee);

        return ResponseEntity.ok(employeeService.save(employeeEntity));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") String employeeId) {
        log.info("Getting info of employee {} in dynamoDB", employeeId);

        var dto = employeeMapper.toDto(employeeService.getEmployeeById(employeeId));

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") String employeeId) {
        log.info("Deleting info of employee {} in dynamoDB", employeeId);

        var employee = employeeService.getEmployeeById(employeeId);

        employeeService.delete(employee);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") String employeeId,
                                                   @Valid @RequestBody EmployeeUpdateInput employee) {
        log.info("Update info of employee {} in dynamoDB", employeeId);

        var employeeEntity = employeeMapper.toEntity(employee);

        return ResponseEntity.ok(employeeService.update(employeeId, employeeEntity));
    }

}
