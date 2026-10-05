package it.unifi.volleyballscouting.dto;

import it.unifi.volleyballscouting.model.Player;

import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Performance}
 */
public record PlayerStatsDTO(
        UUID playerId,
        long totalDigGood,
        long totalDigError,
        long totalAttackPoint,
        long totalAttackInPlay,
        long totalAttackError,
        long totalServePoint,
        long totalServeInPlay,
        long totalServeError,
        long totalBlockPoint,
        long totalBlockInPlay,
        long totalBlockError,
        long totalReceiveGood,
        long totalReceiveError,
        long totalPointsScored,
        long totalErrors,
        long totalSetsPlayed
) implements Serializable {
    private double percent(long part, long total) {
        if (total == 0) return 0.0;
        return (double) part / total * 100;
    }

    public double getAttackSuccessPercentage()  { return percent(totalAttackPoint, totalAttackPoint + totalAttackInPlay + totalAttackError); }
    public double getServeSuccessPercentage()   { return percent(totalServePoint, totalServePoint + totalServeInPlay + totalServeError); }
    public double getBlockSuccessPercentage()   { return percent(totalBlockPoint, totalBlockPoint + totalBlockInPlay + totalBlockError); }
    public double getReceivePositivePercentage(){ return percent(totalReceiveGood, totalReceiveGood + totalReceiveError); }
    public double getDigPositivePercentage()    { return percent(totalDigGood, totalDigGood + totalDigError); }


    public static PlayerStatsDTO empty(UUID playerId){
        return new PlayerStatsDTO(playerId, 0,0,0,0,0, 0,0,
                0, 0,0,0,0,0,0,0,0);
    }
}