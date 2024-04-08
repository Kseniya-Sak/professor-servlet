package edu.sakovich.servlet.model;

import java.util.Set;

public class Professor {
    private int id;
    private String name;
    private String surname;
    private Department department;
    private Set<Subject> subjects;

    public Professor(int id, String name, String surname) {
        this.id = id;
        this.name = name;
        this.surname = surname;
    }

    public Professor(String name, String surname, Department department) {
        this.name = name;
        this.surname = surname;
        this.department = department;
    }

    public Professor(int id, String name, String surname, Department department) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.department = department;
    }

    public Professor(String name, String surname, Department department, Set<Subject> subjects) {
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.subjects = subjects;
    }

    public Professor(int id, String name, String surname, Department department, Set<Subject> subjects) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.department = department;
        this.subjects = subjects;
    }

    public Professor() {
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Set<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(Set<Subject> subjects) {
        this.subjects = subjects;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Professor professor = (Professor) o;

        if (id != professor.id) return false;
        if (name != null ? !name.equals(professor.name) : professor.name != null) return false;
        return surname != null ? surname.equals(professor.surname) : professor.surname == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surname != null ? surname.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Professor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", department=" + department +
                '}';
    }
}
