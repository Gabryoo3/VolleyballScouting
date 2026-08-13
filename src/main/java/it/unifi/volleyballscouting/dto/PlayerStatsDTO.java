package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Player;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public class PlayerStatsDTO implements Serializable {
    private final long playerId;
    private final long totalAces;
    private final long totalServeErrors;
    private final long totalAttacksGood;
    private final long totalAttacksBad;
    private final long totalBlocksGood;
    private final long totalBlocksBad;
    private final long totalPointsScored;
    private final long totalErrors;
    private final long totalSetsPlayed;

    public PlayerStatsDTO(Long playerId, long totalAces,
                          long totalServeErrors, long totalAttacksGood,
                          long totalAttacksBad, long totalBlocksGood,
                          long totalBlocksBad, long totalPolongsScored,
                          long totalErrors, long totalSetsPlayed) {
        this.playerId = playerId;
        this.totalAces = totalAces;
        this.totalServeErrors = totalServeErrors;
        this.totalAttacksGood = totalAttacksGood;
        this.totalAttacksBad = totalAttacksBad;
        this.totalBlocksGood = totalBlocksGood;
        this.totalBlocksBad = totalBlocksBad;
        this.totalPointsScored = totalPolongsScored;
        this.totalErrors = totalErrors;
        this.totalSetsPlayed = totalSetsPlayed;
    }

    public long getPlayerId() {
        return playerId;
    }

    public long getTotalAces() {
        return totalAces;
    }

    public long getTotalServeErrors() {
        return totalServeErrors;
    }

    public long getTotalAttacksGood() {
        return totalAttacksGood;
    }

    public long getTotalAttacksBad() {
        return totalAttacksBad;
    }

    public long getTotalBlocksGood() {
        return totalBlocksGood;
    }

    public long getTotalBlocksBad() {
        return totalBlocksBad;
    }

    public long getTotalPointsScored() {
        return totalPointsScored;
    }

    public long getTotalErrors() {
        return totalErrors;
    }

    public long getTotalSetsPlayed(){return totalSetsPlayed;}
}