package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Player;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public record PlayerStatsDTO(
        UUID playerId,
        long totalAces,
        long totalServeErrors,
        long totalAttacksGood,
        long totalAttacksBad,
        long totalBlocksGood,
        long totalBlocksBad,
        long totalReceiveGood,
        long totalReceiveBad,
        long totalPointsScored,
        long totalErrors,
        long totalSetsPlayed
) implements Serializable {
    private double successPercentage(long good, long bad) {
        long totalAttempts = good + bad;
        if (totalAttempts == 0) return 0.0;
        return ((double) good / totalAttempts) * 100;
    }
    public double getAttackSuccessPercentage() {
        return successPercentage(totalAttacksGood, totalAttacksBad);
    }

    public double getBlockSuccessPercentage() {
        return successPercentage(totalBlocksGood, totalBlocksBad);
    }

    public double getReceiveSuccessPercentage() {
        return successPercentage(totalReceiveGood, totalReceiveBad);
    }

    public static PlayerStatsDTO empty(UUID playerId){
        return new PlayerStatsDTO(playerId, 0,0,0,0,0,
                0,0,0,0,0,0);
    }
}