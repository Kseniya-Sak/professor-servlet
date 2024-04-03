package edu.sakovich.servlet.service.impl;

import edu.sakovich.servlet.exception.NotFoundException;
import edu.sakovich.servlet.model.Department;
import edu.sakovich.servlet.repository.DepartmentRepository;
import edu.sakovich.servlet.service.DepartmentService;
import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.mapper.DepartmentDtoMapper;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

public class DepartmentServiceImpl implements DepartmentService {
    private final DepartmentRepository repository;
    private final DepartmentDtoMapper mapper;

    public DepartmentServiceImpl(DepartmentRepository repository, DepartmentDtoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public DepartmentOutGoingDto save(DepartmentIncomingDto departmentIncomingDto) {
        Department department = mapper.map(departmentIncomingDto);
        Department savedDepartment = repository.save(department);

        return mapper.map(savedDepartment);
    }

    @Override
    public boolean update(DepartmentIncomingDto departmentIncomingDto) {
        boolean result = repository.update(mapper.map(departmentIncomingDto));
        if (!result) {
            throw new NotFoundException("Department with such ID = " + departmentIncomingDto.getId() +
                    " doesn't exist");
        }
        return true;
    }

    @Override
    public FindByIdDepartmentOutGoingDto findById(Integer id) {
        Optional<Department> departmentOptional = repository.findById(id);
        if (departmentOptional.isEmpty()) {
            throw new NotFoundException("Department with ID = " + id + " does not exist in the database");
        }

        return mapper.mapToFindByIdDepartmentOutGoingDto(departmentOptional.get());
    }

    @Override
    public Set<DepartmentOutGoingDto> findAll() {
        Set<Department> departments = repository.findAll();
        Set<DepartmentOutGoingDto> departmentOutGoingDtos = new LinkedHashSet<>();

        for (Department department : departments) {
            departmentOutGoingDtos.add(mapper.map(department));
        }

        return departmentOutGoingDtos;
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = repository.deleteById(id);
        if (!result) {
            throw new NotFoundException("Department with such ID = " + id +
                    " doesn't exist");
        }
        return true;
    }
}
