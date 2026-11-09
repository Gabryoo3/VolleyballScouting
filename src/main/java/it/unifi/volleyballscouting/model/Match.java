package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.exception.BusinessRuleException;
import jakarta.persistence.*;

import java.sql.BatchUpdateException;
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
    @JoinColumn(name = "referee_1_id", nullable = false)
    private Referee referee;
    @Embedded
    private Address address;
    private LocalDateTime matchDateTime;
    private Integer homeScore = 0;
    private Integer guestScore = 0;
    // FIX: la collezione di Set (entità) era priva di annotazione di relazione:
    // Hibernate la trattava come "basic collection" -> MappingException.
    // Relazione 1-a-molti con Set, che possiede la FK match_id (lato proprietario).
    @OneToMany(mappedBy = "match", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("setNumber ASC")
    private List<GameSet> gameSets = new ArrayList<>();
    protected Match(){}

    //constructor
    public Match(Team home, Team guest, LocalDateTime matchDateTime, Referee r) {
        this.teamHome = home;
        this.teamGuest = guest;
        Address tempAddr = teamHome.getAddress();
        this.address = tempAddr;
        this.referee = r;
        this.matchDateTime = matchDateTime;
        setCreatedAt(LocalDateTime.now());
    }

    //methods

    public Team getTeamHome() {return teamHome;}

    public void setTeamHome(Team teamHome) {this.teamHome = teamHome;}

    public Team getTeamGuest() {return teamGuest;}

    public void setTeamGuest(Team teamGuest) {this.teamGuest = teamGuest;}

    public Referee getReferee() {return referee;}

    public void setReferee(Referee r) {this.referee = r;}


    public Address getAddress() {return address;}

    public void setAddress(Address address) {this.address = address;}

    public LocalDateTime getMatchDateTime() {return matchDateTime;}

    public void setMatchDateTime(LocalDateTime matchDateTime) {this.matchDateTime = matchDateTime;}

    public List<GameSet> getSets() {return gameSets;}

    //Functionality methods

    public void start(){
        if(!gameSets.isEmpty()) throw new BusinessRuleException("La partita è già avviata");
        gameSets.add(new GameSet(this, 1));
    }
    public GameSet currentSet(){
        return gameSets.isEmpty() ? null : gameSets.getLast();
    }

    public void recountSetsWon(){
        homeScore = 0;
        guestScore = 0;

        for (GameSet g : gameSets){
            if(g.isFinished()){
                if(g.getTeamHomePoints() > g.getTeamGuestPoints())
                    homeScore++;
                else
                    guestScore++;
            }
        }
    }

    public void closeCurrentSet(int homePoints, int guestPoints){
        if (isFinished()) throw new BusinessRuleException("La partita è già conclusa");
        GameSet set = currentSet();
        if (set==null) throw new BusinessRuleException("La partita non è ancora iniziata");
        set.setTeamHomePoints(homePoints);
        set.setTeamGuestPoints(guestPoints);
        if (!set.isFinished()) throw new BusinessRuleException("Punteggio non valido per concludere il set");
        recountSetsWon();
        if(!isFinished()) gameSets.add(new GameSet(this, set.getSetNumber() +1 ));
    }

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
