package edu.sakovich.servlet.servlet.dto;

public class ProfessorOutGoingDto {
    private int id;
    private String name;
    private String surname;
    private DepartmentOutGoingDto department;

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
}
