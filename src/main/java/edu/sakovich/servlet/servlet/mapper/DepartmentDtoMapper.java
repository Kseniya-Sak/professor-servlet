package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;

public interface DepartmentDtoMapper {
    Department map(DepartmentIncomingDto incomingDto);

    DepartmentOutGoingDto map(Department department);

    FindByIdDepartmentOutGoingDto mapToFindByIdDepartmentOutGoingDto(Department department);
}
