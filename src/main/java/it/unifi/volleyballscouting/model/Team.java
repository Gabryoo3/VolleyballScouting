package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Team extends BaseModel{
    //attributes
    @OneToOne
    @JoinColumn(name = "address_id", referencedColumnName = "id")
    private Address address;

    @Column(length = 100)
    private String name;
    @OneToOne
    @JoinColumn(name = "coach_id")
    private Coach coach;

    //constructor
    public Team(){}
    public Team(String name, Address address, Coach coach) {
        this.name = name;
        this.address=address;
        this.coach = coach;
    }
    //methods
    public Address getAddress(){
        return address;
    }
    public void setAddress(Address a){address=a;}
    public String getName() {return name;}
    public void setName(String n){name=n;}

    public Coach getCoach() {return coach;}

    public void setCoach(Coach coach) {this.coach = coach;}

}