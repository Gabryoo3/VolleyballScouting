package it.unifi.volleyballscouting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

import java.util.Date;

@Entity
public class Coach extends BaseModel{
    private String name;
    private String surname;
    private String username;
    @Column(nullable = true)
    private Date birthdate;
    private String phone;
    private String email;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    protected Coach (){}
    public Coach(String name, String surname, String username, Team team) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.team = team;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public Date getBirthdate() {return birthdate;}

    public void setBirthdate(Date birthdate) {this.birthdate = birthdate;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public Team getTeam() {return team;}

    public void setTeam(Team team) {this.team = team;}
}
