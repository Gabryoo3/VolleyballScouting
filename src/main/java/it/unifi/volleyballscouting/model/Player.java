package it.unifi.volleyballscouting.model;


import it.unifi.volleyballscouting.dto.PlayerFormDto;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
public class Player extends BaseModel{
    //attributes
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private LocalDate birthdate;
    private Integer number;
    @Enumerated(EnumType.STRING)
    private PlayerRole role;
    private String phone;
    private String email;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;


    //constructor
    public Player(String surname, String name, String username, String password, LocalDate birthdate, Integer number, PlayerRole role) {
        this.surname = surname;
        this.name = name;
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
        this.number = number;
        this.role = role;
        setCreatedAt(LocalDateTime.now());
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

    public PlayerRole getRole() {
        return role;
    }

    public void setRole(PlayerRole role) {
        this.role = role;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password){
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {this.username = username;}

    public LocalDate getBirthdate() {return birthdate;}

    public void setBirthdate(LocalDate birthdate) {this.birthdate = birthdate;}

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
