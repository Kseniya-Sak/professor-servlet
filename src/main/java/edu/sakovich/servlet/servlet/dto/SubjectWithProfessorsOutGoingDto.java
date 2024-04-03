package edu.sakovich.servlet.servlet.dto;

import java.util.Set;

public class SubjectWithProfessorsOutGoingDto {
    private int id;
    private String name;
    private int valueOfHours;
    private Set<ProfessorOutGoingDto> professors;

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

    public int getValueOfHours() {
        return valueOfHours;
    }

    public void setValueOfHours(int valueOfHours) {
        this.valueOfHours = valueOfHours;
    }

    public Set<ProfessorOutGoingDto> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<ProfessorOutGoingDto> professors) {
        this.professors = professors;
    }
}
