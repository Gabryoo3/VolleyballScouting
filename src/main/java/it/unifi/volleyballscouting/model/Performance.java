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
    public Performance(Player player, GameSet gameSet, Team team) {
        this.player = player;
        this.gameSet = gameSet;
		this.team = team;
        if (player != null && gameSet != null && player.getId() != null && gameSet.getId() != null)
            this.pId = new PerformanceId(player.getId(), gameSet.getId());
    }

    //methods

    //getters-setters
    public PerformanceId getId() { return pId; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public GameSet getGameSet() { return gameSet; }
    public void setGameSet(GameSet gameSet) { this.gameSet = gameSet; }
	public Team getTeam() { return team; }

    public int getAttackPoint() {return attackPoint;}
    public void setAttackPoint(int attackPoint) {this.attackPoint = attackPoint;}
    public int getAttackInPlay() {return attackInPlay;}
    public void setAttackInPlay(int attackInPlay) {this.attackInPlay = attackInPlay;}
    public int getAttackError() {return attackError;}
    public void setAttackError(int attackError) {this.attackError = attackError;}

    public int getServePoint() {return servePoint;}
    public void setServePoint(int servePoint) {this.servePoint = servePoint;}
    public int getServeInPlay() {return serveInPlay;}
    public void setServeInPlay(int serveInPlay) {this.serveInPlay = serveInPlay;}
    public int getServeError() {return serveError;}
    public void setServeError(int serveError) {this.serveError = serveError;}

    public int getBlockPoint() {return blockPoint;}
    public void setBlockPoint(int blockPoint) {this.blockPoint = blockPoint;}
    public int getBlockInPlay() {return blockInPlay;}
    public void setBlockInPlay(int blockInPlay) {this.blockInPlay = blockInPlay;}
    public int getBlockError() {return blockError;}
    public void setBlockError(int blockError) {this.blockError = blockError;}

    public int getReceiveGood() {return receiveGood;}
    public void setReceiveGood(int receiveGood) {this.receiveGood = receiveGood;}
    public int getReceiveError() {return receiveError;}
    public void setReceiveError(int receiveError) {this.receiveError = receiveError;}

    public int getDigGood() {return digGood;}
    public void setDigGood(int digGood) {this.digGood = digGood;}
    public int getDigError() {return digError;}
    public void setDigError(int digError) {this.digError = digError;}
	public void setCounters (ScoutRowDTO r){
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
