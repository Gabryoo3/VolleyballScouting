package it.unifi.volleyballscouting.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;

@Entity
public class Admin extends BaseModel{
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String surname;
    @Column(nullable=false)
    private String username;
    @Column(nullable = false)
    private String password;

    protected Admin(){}

    public Admin(String name, String surname, String username, String password) {
        this.password = password;
        this.username = username;
        this.surname = surname;
        this.name = name;
    }

    public String getName() {return name;}

    public void setName(String name) {this.name = name;}

    public String getSurname() {return surname;}

    public void setSurname(String surname) {this.surname = surname;}

    public String getUsername() {return username;}

    public void setUsername(String username) {this.username = username;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}
}
