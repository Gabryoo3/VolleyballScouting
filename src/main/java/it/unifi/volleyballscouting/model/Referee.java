package it.unifi.volleyballscouting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

import java.time.LocalDate;

@Entity
public class Referee extends BaseModel{
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
    private String phone;
    private String email;

    public Referee(String name, String surname, String username, String password, LocalDate birthdate) {
        this.name = name;
        this.surname = surname;
        this.username = username;
        this.password = password;
        this.birthdate = birthdate;
    }

    protected Referee(){};

    //methods


    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getSurname() { return surname; }

    public void setSurname(String surname) { this.surname = surname; }

    public String getUsername() { return username; }

    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }

    public void setPassword(String password) { this.password = password; }

    public LocalDate getBirthdate() { return birthdate; }

    public void setBirthdate(LocalDate birthdate) { this.birthdate = birthdate; }

    public String getPhone() { return phone; }

    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }

    public void setEmail(String email) { this.email = email; }

    public void updateFromDto (RefereeFormDTO form){
        this.name = form.name();
        this.surname = form.surname();
        this.username = form.username();
        this.birthdate = form.birthdate();
        this.phone = form.phone();
        this.email = form.email();
    }
}
