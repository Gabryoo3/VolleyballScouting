package it.unifi.volleyballscouting.model;

import jakarta.persistence.*;
@Entity
public class Performance {
    //attributes
    @EmbeddedId
    private PerformanceId id;

    @MapsId("playerId")
    @ManyToOne
    private Player playerId;

    @MapsId("setId")
    @ManyToOne
    private Set setId;
    private int pointsScored;

    //constructor
    public Performance(Player player, Set set) {
        this.playerId = player;
        this.setId = set;
        this.pointsScored = 0;
    }
    protected Performance() {}

    //methods
}
