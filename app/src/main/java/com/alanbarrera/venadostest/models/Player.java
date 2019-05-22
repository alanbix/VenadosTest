package com.alanbarrera.venadostest.models;

import java.util.Date;

public class Player
{
    private String name;
    private String firstSurname;
    private String secondSurname;
    private Date birthday;
    private String birthPlace;
    private double weight;
    private double height;
    private String position;
    private int number;
    private String lastTeam;
    private String image;

    public Player(String name, String firstSurname, String secondSurname, Date birthday, String birthPlace, double weight, double height, String position, int number, String lastTeam, String image)
    {
        this.name = name;
        this.firstSurname = firstSurname;
        this.secondSurname = secondSurname;
        this.birthday = birthday;
        this.birthPlace = birthPlace;
        this.weight = weight;
        this.height = height;
        this.position = position;
        this.number = number;
        this.lastTeam = lastTeam;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public String getFirstSurname() {
        return firstSurname;
    }

    public String getSecondSurname() {
        return secondSurname;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getBirthPlace() {
        return birthPlace;
    }

    public double getWeight() {
        return weight;
    }

    public double getHeight() {
        return height;
    }

    public String getPosition() {
        return position;
    }

    public int getNumber() {
        return number;
    }

    public String getLastTeam() {
        return lastTeam;
    }

    public String getImage() {
        return image;
    }
}
