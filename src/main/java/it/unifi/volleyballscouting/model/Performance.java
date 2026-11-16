package it.unifi.volleyballscouting.model;

import it.unifi.volleyballscouting.dto.scouting.ScoutRowDTO;
import jakarta.persistence.*;

@Entity
@Table(name = "performance")
public class Performance{
    //attributes
    @EmbeddedId
    private PerformanceId pId;

    @MapsId("playerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @MapsId("setId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id", nullable = false)
    private GameSet gameSet;

	@ManyToOne(optional = false)
	@JoinColumn(name = "team_id", nullable = false)
	private Team team;

    private int attackPoint = 0, attackInPlay = 0, attackError = 0;
    private int servePoint = 0, serveInPlay = 0, serveError = 0;
    private int blockPoint = 0, blockInPlay = 0, blockError = 0;
    private int receiveGood = 0, receiveError = 0;
    private int digGood = 0, digError = 0;

    protected Performance() {}

    //constructor
    public Performance(Player player, GameSet gameSet, Team team, ScoutRowDTO row) {
        this.player = player;
        this.gameSet = gameSet;
		this.team = team;
		this.pId = new PerformanceId(player.getId(), gameSet.getId());
		setCounters(row);
    }

    //methods

    //getters-setters
    public PerformanceId getId() { return pId; }
    public Player getPlayer() { return player; }
	public GameSet getGameSet() { return gameSet; }
	public Team getTeam() { return team; }

    public int getAttackPoint() {return attackPoint;}
    public int getAttackInPlay() {return attackInPlay;}
    public int getAttackError() {return attackError;}

    public int getServePoint() {return servePoint;}
    public int getServeInPlay() {return serveInPlay;}
    public int getServeError() {return serveError;}

    public int getBlockPoint() {return blockPoint;}
    public int getBlockInPlay() {return blockInPlay;}
    public int getBlockError() {return blockError;}

    public int getReceiveGood() {return receiveGood;}
    public int getReceiveError() {return receiveError;}

    public int getDigGood() {return digGood;}
    public int getDigError() {return digError;}

	private void setCounters (ScoutRowDTO r){
		attackPoint = r.attackPoint(); attackInPlay = r.attackInPlay(); attackError = r.attackError();
		servePoint = r.servePoint();   serveInPlay = r.serveInPlay();   serveError = r.serveError();
		blockPoint = r.blockPoint();   blockInPlay = r.blockInPlay();   blockError = r.blockError();
		receiveGood = r.receiveGood(); receiveError = r.receiveError();
		digGood = r.digGood();         digError = r.digError();
	}

    //Transients
    @Transient
    public int getTotalPointsScored(){
        return attackPoint + servePoint + blockPoint;
    }
    @Transient
    public int getTotalErrors(){
        return attackError + serveError + blockError + receiveError + digError;
    }
}
