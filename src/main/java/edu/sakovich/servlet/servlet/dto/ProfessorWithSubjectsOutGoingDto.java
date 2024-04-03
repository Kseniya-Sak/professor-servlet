package edu.sakovich.servlet.servlet.dto;

import java.util.Set;

public class ProfessorWithSubjectsOutGoingDto {
    private int id;
    private String name;
    private String surname;
    private DepartmentOutGoingDto department;
    private Set<SubjectOutGoingDto> subjects;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public DepartmentOutGoingDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentOutGoingDto department) {
        this.department = department;
    }

    public Set<SubjectOutGoingDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectOutGoingDto> subjects) {
        this.subjects = subjects;
    }
}
