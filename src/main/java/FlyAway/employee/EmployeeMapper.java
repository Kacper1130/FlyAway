package FlyAway.employee;

import FlyAway.employee.dto.DisplayEmployeeDto;
import FlyAway.user.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "hireDate", target = "hireDate")
    DisplayEmployeeDto employeeToDisplayEmployeeDto(User employee);

}
