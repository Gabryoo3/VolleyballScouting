package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "vsets", uniqueConstraints = {
    @UniqueConstraint(
        name = "uk_match_set_number",
        columnNames = {"match_id", "setNumber"}
    )
})
public class Set extends BaseModel{
    //attributes
    @ManyToOne
    @JoinColumn(name = "match_id")
    private Match match;
    private int setNumber;
    private int teamHomePoints = 0;
    private int teamGuestPoints = 0;

    //constructor
    protected Set() {}

    public Set(Match match, int setNumber) {
        this.match = match;
        this.setNumber = setNumber;
        setCreatedAt(LocalDateTime.now());
    }
    //methods

    public Match getMatch() {
        return match;
    }

    public void setMatch(Match match) {
        this.match = match;
    }

    public int getTeamGuestPoints() {
        return teamGuestPoints;
    }

    public void addTeamGuestPoints(){
        this.teamGuestPoints++;
    }

    public void setTeamGuestPoints(int teamGuestPoints) {
        this.teamGuestPoints = teamGuestPoints;
    }

    public int getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(int setNumber) {
        this.setNumber = setNumber;
    }

    public int getTeamHomePoints() {
        return teamHomePoints;
    }

    public void addTeamHomePoints(){
        this.teamHomePoints++;
    }

    public void setTeamHomePoints(int teamHomePoints) {
        this.teamHomePoints = teamHomePoints;
    }

    @Transient
    public Team getLeading(){
        if(teamHomePoints > teamGuestPoints) return match.getTeamHome();
        else if(teamGuestPoints > teamHomePoints) return match.getTeamGuest();
        return null;
    }
    @Transient
    public boolean isFinished(){
        int maxPoints = (setNumber == 5) ? 15 : 25;
        int diff = Math.abs(teamHomePoints - teamGuestPoints);
        return (teamHomePoints >= maxPoints || teamGuestPoints >= maxPoints) && diff >=2;
    }

    @Transient
    public int getTotalPoints(){
        return teamHomePoints + teamGuestPoints;
    }

    @Transient
    public String getDisplayName(){
        if(match != null && match.getTeamHome() != null & match.getTeamGuest() != null){
            return String.format("Set %d (%s vd %s)",setNumber, match.getTeamHome().getName(), match.getTeamGuest().getName());
        }
        return "Set " + setNumber;
    }
    @Override
    public String toString(){
        return getDisplayName();
    }
}