package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Team;

import java.io.Serializable;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public class TeamStatsDTO implements Serializable {
    private final long teamId;
    private final long totalAces;
    private final long totalServeErrors;
    private final long totalAttacksGood;
    private final long totalAttacksBad;
    private final long totalBlocksGood;
    private final long totalBlocksBad;
    private final long totalReceiveGood;
    private final long totalReceiveBad;
    private final long totalPointsScored;
    private final long totalErrors;

    public TeamStatsDTO(long teamId,
                        long totalAces, long totalServeErrors,
                        long totalAttacksGood, long totalAttacksBad,
                        long totalBlocksGood, long totalBlocksBad,
                        long totalReceiveGood, long TotalReceiveBad,
                        long totalPointsScored, long totalErrors) {
        this.teamId = teamId;
        this.totalAces = totalAces;
        this.totalServeErrors = totalServeErrors;
        this.totalAttacksGood = totalAttacksGood;
        this.totalAttacksBad = totalAttacksBad;
        this.totalBlocksGood = totalBlocksGood;
        this.totalBlocksBad = totalBlocksBad;
        this.totalReceiveGood = totalReceiveGood;
        this.totalReceiveBad = TotalReceiveBad;
        this.totalPointsScored = totalPointsScored;
        this.totalErrors = totalErrors;
    }

    public long getTeamId() {return teamId;}

    public long getTotalAces() {
        return totalAces;
    }

    public long getTotalServeErrors() {
        return totalServeErrors;
    }

    public long getTotalAttacksGood() {return totalAttacksGood;}

    public long getTotalAttacksBad() {
        return totalAttacksBad;
    }

    public long getTotalBlocksGood() {
        return totalBlocksGood;
    }

    public long getTotalBlocksBad() {return totalBlocksBad;}

    public long getTotalReceiveGood() {return totalReceiveGood;}

    public long getTotalReceiveBad() {return totalReceiveBad;}

    public long getTotalPointsScored() {
        return totalPointsScored;
    }

    public long getTotalErrors() {
        return totalErrors;
    }

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

}