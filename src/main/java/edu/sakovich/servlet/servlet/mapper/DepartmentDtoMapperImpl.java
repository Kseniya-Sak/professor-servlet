package edu.sakovich.servlet.servlet.mapper;

import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.model.Professor;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.ProfessorShortOutGoingDto;

import java.util.LinkedHashSet;
import java.util.Set;

public class DepartmentDtoMapperImpl implements DepartmentDtoMapper {
    private static final ProfessorDtoMapper professorDtoMapper = new ProfessorDtoMapperImpl();
    @Override
    public Department map(DepartmentIncomingDto incomingDto) {
        Department department = new Department();
        department.setId(incomingDto.getId());
        department.setName(incomingDto.getName());
        return department;
    }

    @Override
    public DepartmentOutGoingDto map(Department department) {
        DepartmentOutGoingDto outGoingDto = new DepartmentOutGoingDto();
        outGoingDto.setId(department.getId());
        outGoingDto.setName(department.getName());
        return outGoingDto;
    }

    @Override
    public FindByIdDepartmentOutGoingDto mapToFindByIdDepartmentOutGoingDto(Department department) {
        FindByIdDepartmentOutGoingDto outGoingDto = new FindByIdDepartmentOutGoingDto();
        outGoingDto.setId(department.getId());
        outGoingDto.setName(department.getName());

        Set<Professor> professors = department.getProfessors();
        Set<ProfessorShortOutGoingDto> professorShortOutGoingDtos = new LinkedHashSet<>();
        for (Professor professor : professors) {
            professorShortOutGoingDtos.add(professorDtoMapper.mapToProfessorShortOutGoingDto(professor));
        }
        outGoingDto.setProfessors(professorShortOutGoingDtos);
        return outGoingDto;
    }


}
