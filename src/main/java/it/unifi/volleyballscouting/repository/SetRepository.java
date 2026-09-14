package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.GameSet;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.UUID;

public interface SetRepository extends JpaRepository<GameSet, UUID> {
    List<GameSet> findSetsByMatchIdOrderBySetNumberAsc(UUID MatchId);
    //find bad sets, where the team makes under a certain threshold
    @Query("SELECT s FROM GameSet s WHERE "+
            "(s.match.teamHome = :team AND s.teamHomePoints < :scoreTreshold) OR "+
            "(s.match.teamGuest = :team AND s.teamGuestPoints < :scoreTreshold )")
    List<GameSet> findBadSetsForTeam(@Param("team") Team t, @Param("scoreThreshold") int scoreThreshold);
    //find tied sets where the total points are above a certain minimum (like, 25-23 sets, 26-24 and so on)
    @Query("SELECT s FROM GameSet s WHERE (s.match.teamHome = :team OR s.match.teamGuest = :team )"+
    "AND (s.teamHomePoints + s.teamGuestPoints) >= :minTotalPoints")
    List<GameSet> findTiedSets(@Param("team") Team t, @Param("minTotalPoints") int minTotalPoints);

}
