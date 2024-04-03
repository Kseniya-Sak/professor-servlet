package edu.sakovich.servlet.servlet.dto;

import java.util.Set;

public class ProfessorWithSubjectsIncomingDto {
    private int id;
    private String name;
    private String surname;
    private DepartmentIncomingDto department;
    private Set<SubjectIncomingDto> subjects;

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

    public DepartmentIncomingDto getDepartment() {
        return department;
    }

    public void setDepartment(DepartmentIncomingDto department) {
        this.department = department;
    }

    public Set<SubjectIncomingDto> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<SubjectIncomingDto> subjects) {
        this.subjects = subjects;
    }
}
