package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;

@Entity
@Table(name = "performance")
public class Performance{
    //attributes
    @EmbeddedId
    private PerformanceId pId;

    @MapsId("playerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player player;

    @MapsId("setId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id")
    private GameSet gameSet;

    private int attackPoint = 0, attackInPlay = 0, attackError = 0;
    private int servePoint = 0, serveInPlay = 0, serveError = 0;
    private int blockPoint = 0, blockInPlay = 0, blockError = 0;
    private int receiveGood = 0, receiveError = 0;
    private int digGood = 0, digError = 0;

    protected Performance() {}

    //constructor
    public Performance(Player player, GameSet gameSet) {
        this.player = player;
        this.gameSet = gameSet;
        if (player != null && gameSet != null && player.getId() != null && gameSet.getId() != null)
            this.pId = new PerformanceId(player.getId(), gameSet.getId());
    }

    //methods

    //live adding
    public void addDigGood()     { this.digGood++; }
    public void addDigError()    { this.digError++; }
    public void addAttackPoint() { this.attackPoint++; }
    public void addAttackInPlay(){ this.attackInPlay++; }
    public void addAttackError() { this.attackError++; }
    public void addServePoint()  { this.servePoint++; }
    public void addServeInPlay() { this.serveInPlay++; }
    public void addServeError()  { this.serveError++; }
    public void addBlockPoint()  { this.blockPoint++; }
    public void addBlockInPlay() { this.blockInPlay++; }
    public void addBlockError()  { this.blockError++; }
    public void addReceiveGood() { this.receiveGood++; }
    public void addReceiveError(){ this.receiveError++; }

    //getters-setters
    public PerformanceId getId() { return pId; }
    public Player getPlayer() { return player; }
    public void setPlayer(Player player) { this.player = player; }
    public GameSet getGameSet() { return gameSet; }
    public void setGameSet(GameSet gameSet) { this.gameSet = gameSet; }

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
