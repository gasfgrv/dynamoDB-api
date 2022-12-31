package br.com.gusta.dynamodb.common;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.domain.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.SourceGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class ModelMapperConfig {

    public static final SourceGetter<Employee> GETTER_DEPARTMENT_CODE = employee -> employee
            .getDepartment().getDepartmentCode();

    public static final SourceGetter<Employee> GETTER_DEPARTMENT_NAME = employee -> employee
            .getDepartment().getDepartmentName();

    public static final DestinationSetter<EmployeeDto, String> SETTER_DEPARTMENT_CODE = EmployeeDto::setDepartamentCode;

    public static final DestinationSetter<EmployeeDto, String> SETTER_DEPARTMENT_NAME = EmployeeDto::setDepartamentName;

    @Bean
    public ModelMapper modelMapper() {
        LOGGER.info("Creating bean ModelMapper");

        var mapper = new ModelMapper();

        mapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMapping(GETTER_DEPARTMENT_CODE, SETTER_DEPARTMENT_CODE)
                .addMapping(GETTER_DEPARTMENT_NAME, SETTER_DEPARTMENT_NAME);

        return mapper;
    }

}
