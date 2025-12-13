package it.unifi.volleyballscouting.match;

import it.unifi.volleyballscouting.teams.Address;
import it.unifi.volleyballscouting.teams.Team;

import jakarta.persistence.*;

@Entity
public class Match {
    //attributes
    @Id
    @GeneratedValue(strategy     = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    Team teamHome;
    @ManyToOne
    Team teamGuest;
    @ManyToOne
    Referee r1;
    @ManyToOne
    Referee r2;
    @ManyToOne
    Address address;

    //constructor
    public Match(Team home, Team guest) {
        this.teamHome = home;
        this.teamGuest = guest;
        address=teamHome.getAddress();
    }
    protected Match(){}

    //methods
    public Team getTeamHome() {
        return teamHome;
    }

    public Team getTeamGuest() {
        return teamGuest;
    }

    public Referee getR1() {
        return r1;
    }

    public void setR1(Referee r1) {
        this.r1 = r1;
    }

    public Referee getR2() {
        return r2;
    }

    public void setR2(Referee r2) {
        this.r2 = r2;
    }
}
