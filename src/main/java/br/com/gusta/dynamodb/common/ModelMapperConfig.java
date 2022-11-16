package br.com.gusta.dynamodb.common;

import br.com.gusta.dynamodb.api.model.EmployeeDto;
import br.com.gusta.dynamodb.domain.model.Employee;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.DestinationSetter;
import org.modelmapper.spi.SourceGetter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModelMapperConfig {

    public static final SourceGetter<Employee> GETTER_DEPARTAMENT_CODE = employee -> employee
            .getDepartament().getDepartamentCode();
    public static final SourceGetter<Employee> GETTER_DEPARTAMENT_NAME = employee -> employee
            .getDepartament().getDepartamentName();
    public static final DestinationSetter<EmployeeDto, String> SETTER_DEPARTAMENT_CODE = EmployeeDto::setDepartamentCode;
    public static final DestinationSetter<EmployeeDto, String> SETTER_DEPARTAMENT_NAME = EmployeeDto::setDepartamentName;

    @Bean
    public ModelMapper modelMapper() {
        var mapper = new ModelMapper();

        mapper.createTypeMap(Employee.class, EmployeeDto.class)
                .addMapping(GETTER_DEPARTAMENT_CODE, SETTER_DEPARTAMENT_CODE)
                .addMapping(GETTER_DEPARTAMENT_NAME, SETTER_DEPARTAMENT_NAME);

        return mapper;
    }

}
