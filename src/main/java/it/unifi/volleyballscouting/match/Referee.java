package it.unifi.volleyballscouting.match;

import jakarta.persistence.*;

@Entity
public class Referee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String surname;
    private String province;
    private int licenseNumber;


    public Referee(String name, String surname, String province, int licenseNumber) {
        this.name = name;
        this.surname = surname;
        this.province = province;
        this.licenseNumber = licenseNumber;
    }

    protected Referee(){}

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
