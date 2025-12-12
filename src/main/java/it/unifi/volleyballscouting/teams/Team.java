package it.unifi.volleyballscouting.teams;

import jakarta.persistence.*;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private Address address;
    public Address getAddress(){
        return address;
    }
    protected Team(){}


}
