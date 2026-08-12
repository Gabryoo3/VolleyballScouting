package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Set;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SetRepository extends JpaRepository<Set, Long> {
    List<Set> findSetsByMatchIdOrderBySetNumberAsc(Long MatchId);
    //find bad sets, where the team makes under a certain threshold
    @Query("SELECT s FROM Set s WHERE "+
            "(s.match.teamHome = :team AND s.teamHomePoints < :scoreTreshold) OR "+
            "(s.match.teamGuest = :team AND s.teamGuestPoints < :scoreTreshold )")
    List<Set> findBadSetsForTeam(@Param("team") Team t, @Param("scoreThreshold") int scoreThreshold);
    //find tied sets where the total points are above a certain minimum (like, 25-23 sets, 26-24 and so on)
    @Query("SELECT s FROM Set s WHERE (s.match.teamHome = :team OR s.match.teamGuest = :team )"+
    "AND (s.teamHomePoints + s.teamGuestPoints) >= :minTotalPoints")
    List<Set> findTiedSets(@Param("team") Team t, @Param("minTotalPoints") int minTotalPoints);

}
