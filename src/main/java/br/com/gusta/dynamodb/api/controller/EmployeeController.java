package br.com.gusta.dynamodb.api.controller;

import br.com.gusta.dynamodb.api.mapper.EmployeeMapper;
import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.api.model.EmployeeInput;
import br.com.gusta.dynamodb.domain.model.Employee;
import br.com.gusta.dynamodb.domain.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
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
@Api("Employee")
@RequestMapping("/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final EmployeeMapper employeeMapper;

    @PostMapping
    @ApiOperation(value = "Saves a new employee", notes = "Saves a new employee in dynamoDB")
    @ApiResponse(code = 200, message = "Employee saved", response = Employee.class)
    public ResponseEntity<Employee> saveEmployee(@Valid @RequestBody @ApiParam(value = "Form for creation of user", required = true) EmployeeInput employee) {
        log.info("Salving employee in dynamoDB");

        var employeeEntity = employeeMapper.toEntity(employee);

        return ResponseEntity.ok(employeeService.save(employeeEntity));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get data of specific employee", notes = "Get data of specific employee in dynamoDB")
    @ApiResponse(code = 200, message = "Data retrived", response = EmployeeDto.class)
    public ResponseEntity<EmployeeDto> getEmployee(@PathVariable("id") @ApiParam(value = "Employee id", required = true) String employeeId) {
        log.info("Getting info of employee {} in dynamoDB", employeeId);

        var dto = employeeMapper.toDto(employeeService.getEmployeeById(employeeId));

        return ResponseEntity.ok(dto);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Deletes specific employee", notes = "Deletes employee in dynamoDB")
    @ApiResponse(code = 204, message = "Employee deleted")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") @ApiParam(value = "Employee id", required = true) String employeeId) {
        log.info("Deleting info of employee {} in dynamoDB", employeeId);

        var employee = employeeService.getEmployeeById(employeeId);

        employeeService.delete(employee);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    @ApiOperation(value = "Update data of specific employee", notes = "Update data of specific employee in dynamoDB")
    @ApiResponse(code = 200, message = "Employee updated", response = Employee.class)
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") @ApiParam(value = "Employee id", required = true) String employeeId,
                                                   @Valid @RequestBody @ApiParam(value = "Form to update user", required = true) EmployeeInput employee) {
        log.info("Update info of employee {} in dynamoDB", employeeId);

        var employeeEntity = employeeMapper.toEntity(employee);

        return ResponseEntity.ok(employeeService.update(employeeId, employeeEntity));
    }

}
