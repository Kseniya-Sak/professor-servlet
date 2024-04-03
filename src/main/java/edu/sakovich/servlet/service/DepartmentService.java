package edu.sakovich.servlet.service;

import edu.sakovich.servlet.servlet.dto.DepartmentIncomingDto;
import edu.sakovich.servlet.servlet.dto.DepartmentOutGoingDto;
import edu.sakovich.servlet.servlet.dto.FindByIdDepartmentOutGoingDto;

import java.util.Set;

public interface DepartmentService {

    DepartmentOutGoingDto save(DepartmentIncomingDto departmentIncomingDto);

    boolean update(DepartmentIncomingDto departmentIncomingDto);

    FindByIdDepartmentOutGoingDto findById(Integer id);

    Set<DepartmentOutGoingDto> findAll();

    boolean deleteById(Integer id);

}
