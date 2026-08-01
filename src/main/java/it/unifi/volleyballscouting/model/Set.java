package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "vsets")
public class Set extends BaseModel{
    //attributes
    @ManyToOne
    Match match;
    private int setNumber;
    private int team1Points;
    private int team2Points;

    //constructor
    public Set(Match match, int setNumber) {
        this.match = match;
        this.setNumber = setNumber;
        this.team1Points = 0;
        this.team2Points = 0;
    }
    protected Set() {}

    //methods
}