package it.unifi.volleyballscouting.match;

import it.unifi.volleyballscouting.teams.Address;
import it.unifi.volleyballscouting.teams.Team;

public class Match {
    private
    Team team1;
    Team team2;
    Referee r1;
    Referee r2;
    Address address;

    public Match(Team t1, Team t2) {
        this.team1 = t1;
        this.team2 = t2;
        address=team1.getAddress();
    }
    //genera get e setter

    public Team getTeam1() {
        return team1;
    }
    public Team getTeam2() {
        return team2;
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
