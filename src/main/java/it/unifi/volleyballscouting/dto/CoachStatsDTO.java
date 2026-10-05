package it.unifi.volleyballscouting.dto;


import java.io.Serializable;
import java.util.UUID;

/**
 * DTO for {@link it.unifi.volleyballscouting.model.Coach}
 */
public record CoachStatsDTO(
        UUID id,
        long totalWins,
        long totalLosses,
        long totalMatches
        ) implements Serializable {
    public double winPercentage(long totalWins, long totalMatches){
      if(totalMatches == 0) return 0.0;
      return Math.round(((double) totalWins / totalMatches)*1000)/10.0;
    }

    public static CoachStatsDTO empty(UUID coachId){
        return new CoachStatsDTO(coachId, 0,0,0);
    }
}