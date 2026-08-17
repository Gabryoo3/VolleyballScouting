package it.unifi.volleyballscouting.model;


import it.unifi.volleyballscouting.dto.PlayerFormDto;
import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Player extends BaseModel{
    //attributes
    private String name;
    private String surname;
    private String username;
    @Column(nullable = true)
    private Date birthdate;
    @Column(nullable = true)
    private int number;
    @Column(nullable = true)
    private String role;
    @Column(nullable = true)
    private String phone;
    @Column(nullable = true)
    private String email;
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;


    //constructor
    public Player(String surname, String name, String username, Date birthdate, int number, String role) {
        this.surname = surname;
        this.name = name;
        this.username = username;
        this.birthdate = birthdate;
        this.number = number;
        this.role = role;
    }
    protected Player(){}

    //methods
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public Date getBirthdate() {return birthdate;}

    public void setBirthdate(Date birthdate) {this.birthdate = birthdate;}

    public String getPhone() {return phone;}

    public void setPhone(String phone) {this.phone = phone;}

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public void updateFromDto(PlayerFormDto form){
        this.name = form.name();
        this.surname = form.surname();
        this.username = form.username();
        this.birthdate = form.birthdate();
        this.number = form.number();
        this.role = form.role();
        this.phone = form.phone();
        this.email = form.email();
    }

}
