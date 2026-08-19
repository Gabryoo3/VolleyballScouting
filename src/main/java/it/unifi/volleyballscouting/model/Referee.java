package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Referee extends BaseModel {
    //attributes
    private String name;
    private String surname;
    private String province;
    private int licenseNumber;
    //constructor
    protected Referee(){}

    public Referee(String name, String surname, String province, int licenseNumber) {
        this.name = name;
        this.surname = surname;
        this.province = province;
        this.licenseNumber = licenseNumber;
        setCreatedAt(LocalDateTime.now());
    }
    //methods
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public int getLicenseNumber() {
        return licenseNumber;
    }
}
