package it.unifi.volleyballscouting.teams;

import jakarta.persistence.*;

@Entity
public class Team {
    //attributes
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Address address;

    //constructor
    public Team(Address address){
        this.address=address;
    }
    protected Team(){}

    //methods
    public Address getAddress(){
        return address;
    }
}