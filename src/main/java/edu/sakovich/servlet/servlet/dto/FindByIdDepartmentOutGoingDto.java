package edu.sakovich.servlet.servlet.dto;

import java.util.Set;

public class FindByIdDepartmentOutGoingDto {
    private int id;
    private String name;
    private Set<ProfessorShortOutGoingDto> professors;


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

    public Set<ProfessorShortOutGoingDto> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<ProfessorShortOutGoingDto> professors) {
        this.professors = professors;
    }
}
