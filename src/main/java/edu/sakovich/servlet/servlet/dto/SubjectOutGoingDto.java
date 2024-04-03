package edu.sakovich.servlet.servlet.dto;

public class SubjectOutGoingDto {
    private int id;
    private String name;
    private int valueOfHours;

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
}
