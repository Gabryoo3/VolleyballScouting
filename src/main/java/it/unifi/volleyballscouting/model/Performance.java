package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;
@Entity
@Table(name = "performance")
public class Performance {
    //attributes
    @EmbeddedId
    private PerformanceId pId;

    @MapsId("playerId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player playerId;

    @MapsId("setId")
    @ManyToOne(optional = false)
    @JoinColumn(name = "set_id")
    private Set setId;

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
    public Performance(Player player, Set set) {
        this.playerId = player;
        this.setId = set;

        if (player != null && set != null && player.getId() != null && set.getId() != null)
            this.pId = new PerformanceId(player.getId(), set.getId());
    }

    //methods

    //live adding
    public void addAce() {this.aces++};
    public void addServeError() { this.serveErrors++; }
    public void addAttackGood() { this.attacksGood++; }
    public void addAttackBad() { this.attacksBad++; }
    public void addBlockGood() { this.blocksGood++; }
    public void addBlockBad() { this.blocksBad++; }
    public void addReceiveGood() { this.receiveGood++; }
    public void addReceiveBad() { this.receiveBad++; }

    //getters-setters

    public PerformanceId getId() {return id;}

    public void setId(PerformanceId id) {this.id = id;}

    public Player getPlayerId() {return playerId;}

    public void setPlayerId(Player playerId) {this.playerId = playerId;}

    public Set getSetId() {return setId;}

    public void setSetId(Set setId) {this.setId = setId;}

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
