package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Team;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public record TeamStatsDTO (
     UUID teamId,
     long totalAces,
     long totalServeErrors,
     long totalAttacksGood,
     long totalAttacksBad,
     long totalBlocksGood,
     long totalBlocksBad,
     long totalReceiveGood,
     long totalReceiveBad,
     long totalPointsScored,
     long totalErrors
     ) implements Serializable{

    private double successPercentage(long good, long bad) {
        long totalAttempts = good + bad;
        if (totalAttempts == 0) return 0.0;
        return Math.round((double) good / totalAttempts) * 100;
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

    public static TeamStatsDTO empty(UUID teamId){
        return new TeamStatsDTO(teamId,0,0,0,0,
                0,0,0,0,0,0);
    }

}