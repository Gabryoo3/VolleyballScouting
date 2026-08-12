package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Match extends BaseModel{
    //attributes
    @ManyToOne(optional = false)
    @JoinColumn(name = "home_team_id", nullable = false)
    private Team teamHome;
    @ManyToOne(optional = false)
    @JoinColumn(name = "guest_team_id", nullable = false)
    private Team teamGuest;
    @ManyToOne(optional = false)
    @JoinColumn(name = "first_referee_id", nullable = false)
    private Referee r1;
    @ManyToOne(optional = false)
    @JoinColumn(name = "second_referee_id", nullable = false)
    private Referee r2;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private Address address;
    private LocalDateTime matchDateTime;
    private Integer homeScore = 0;
    private Integer guestScore = 0;
    private List<Set> sets = new ArrayList<>();
    protected Match(){}

    //constructor
    public Match(Team home, Team guest, LocalDateTime matchDateTime) {
        this.teamHome = home;
        this.teamGuest = guest;
        this.address=teamHome.getAddress();
        this.matchDateTime = matchDateTime;
    }

    //methods

    public Team getTeamHome() {return teamHome;}

    public void setTeamHome(Team teamHome) {this.teamHome = teamHome;}

    public Team getTeamGuest() {return teamGuest;}

    public void setTeamGuest(Team teamGuest) {this.teamGuest = teamGuest;}

    public Referee getR1() {return r1;}

    public void setR1(Referee r1) {this.r1 = r1;}

    public Referee getR2() {return r2;}

    public void setR2(Referee r2) {this.r2 = r2;}

    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

    public LocalDateTime getMatchDateTime() {return matchDateTime;}

    public void setMatchDateTime(LocalDateTime matchDateTime) {this.matchDateTime = matchDateTime;}

    public Integer getHomeScore() {return homeScore;}

    public void setHomeScore(Integer homeScore) {this.homeScore = homeScore;}

    public Integer getGuestScore() {return guestScore;}

    public void setGuestScore(Integer guestScore) {this.guestScore = guestScore;}

    public List<Set> getSets() {return sets;}

    @Transient
        public Team getWinner(){
        if(homeScore > guestScore)
            return teamHome;
        else if (homeScore < guestScore)
            return teamGuest;
        return null;
    }
    @Transient
    public boolean isFinished(){
        return homeScore == 3 || guestScore == 3;
    }
}
