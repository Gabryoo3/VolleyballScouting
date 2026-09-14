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

    private int aces = 0;
    private int serveErrors = 0;

    private int attacksGood = 0;
    private int attacksBad = 0;

    private int blocksGood = 0;
    private int blocksBad = 0;

    private int receiveGood = 0;
    private int receiveBad = 0;


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
    public void addAce() {this.aces++;}
    public void addServeError() { this.serveErrors++; }
    public void addAttackGood() { this.attacksGood++; }
    public void addAttackBad() { this.attacksBad++; }
    public void addBlockGood() { this.blocksGood++; }
    public void addBlockBad() { this.blocksBad++; }
    public void addReceiveGood() { this.receiveGood++; }
    public void addReceiveBad() { this.receiveBad++; }

    //getters-setters

    public PerformanceId getId() {return pId;}

    public void set(PerformanceId id) {this.pId = id;}

    public Player getPlayer() {return player;}

    public void setPlayer(Player player) {this.player = player;}

    public GameSet getSet() {return gameSet;}

    public void setSet(GameSet gameSet) {this.gameSet = gameSet;}

    public int getAces() {return aces;}

    public void setAces(int aces) {this.aces = aces;}

    public int getServeErrors() {return serveErrors;}

    public void setServeErrors(int serveErrors) {this.serveErrors = serveErrors;}

    public int getAttacksGood() {return attacksGood;}

    public void setAttacksGood(int attacksGood) {this.attacksGood = attacksGood;}

    public int getAttacksBad() {return attacksBad;}

    public void setAttacksBad(int attacksBad) {this.attacksBad = attacksBad;}

    public int getBlocksGood() {return blocksGood;}

    public void setBlocksGood(int blocksGood) {this.blocksGood = blocksGood;}

    public int getBlocksBad() {return blocksBad;}

    public void setBlocksBad(int blocksBad) {this.blocksBad = blocksBad;}

    public int getReceiveGood() {return receiveGood;}

    public void setReceiveGood(int receiveGood) {this.receiveGood = receiveGood;}

    public int getReceiveBad() {return receiveBad;}

    public void setReceiveBad(int receiveBad) {this.receiveBad = receiveBad;}

    //Transients
    @Transient
    public int getTotalPointsScored(){
        return aces+attacksGood+blocksGood;
    }
    @Transient
    public int getTotalErrors(){
        return serveErrors+ attacksBad + blocksBad + receiveBad;
    }
}
