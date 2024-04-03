package edu.sakovich.servlet.model;

import java.util.Set;

public class Subject {
    private int id;
    private String name;
    private int valueOfHours;
    private Set<Professor> professors;

    public Subject() {
    }

    public Subject(String name, int valueOfHours) {
        this.name = name;
        this.valueOfHours = valueOfHours;
    }

    public Subject(int id, String name, int valueOfHours) {
        this.id = id;
        this.name = name;
        this.valueOfHours = valueOfHours;
    }

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

    public Set<Professor> getProfessors() {
        return professors;
    }

    public void setProfessors(Set<Professor> professors) {
        this.professors = professors;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Subject subject = (Subject) o;

        if (id != subject.id) return false;
        if (valueOfHours != subject.valueOfHours) return false;
        return name != null ? name.equals(subject.name) : subject.name == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + valueOfHours;
        return result;
    }
}
