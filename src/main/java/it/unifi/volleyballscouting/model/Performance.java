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

    protected Performance() {}

    //constructor
    public Performance(Player player, Set set) {
        this.playerId = player;
        this.setId = set;

        if (player != null && set != null)
            this.id = new PerformanceId(player.getId(), set.getId());
        this.pointsScored = 0;
    }


    //methods
}
