package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {
    // find by date time
    List<Match> findByMatchDateTimeBetween (LocalDateTime start, LocalDateTime end);
    List<Match> findByMatchDateTimeAfterOrderByMatchDateTimeAsc(LocalDateTime now);
    List<Match> findByMatchDateTimeBeforeOrderByMatchDateTimeDesc(LocalDateTime now);

    // find by address
    List<Match> findByAddressCityContaining(String addressCity);

    // find by team, winning or losing matches
    @Query("SELECT DISTINCT m FROM Match m WHERE " +
            "(m.teamHome.id = :teamId OR m.teamGuest.id = :teamId)" +
            "ORDER BY m.matchDateTime DESC")
    List<Match> findByTeam(@Param("teamId")UUID teamId);

    @Query("SELECT m FROM Match m WHERE" +
    "(m.teamHome.id = :teamId AND m.homeScore > m.guestScore) OR"+
    "(m.teamGuest.id = :teamId AND m.guestScore > m.homeScore)" +
    "ORDER BY m.matchDateTime DESC")
    List<Match> findByTeamWins(@Param("teamId")UUID teamId);

    @Query("SELECT m FROM Match m WHERE" +
            "(m.teamHome.id = :teamId AND m.homeScore < m.guestScore) OR"+
            "(m.teamGuest.id = :teamId AND m.guestScore < m.homeScore)" +
            "ORDER BY m.matchDateTime DESC")
    List<Match> findByTeamLosses(@Param("teamId")UUID teamId);

    @Query("SELECT m FROM Match m WHERE"+
            "(m.teamHome.id = :team1Id AND m.teamGuest.id = :team2Id) OR"+
            "(m.teamGuest.id = :team1Id AND m.teamHome.id = :team2Id)" +
            "ORDER BY m.matchDateTime DESC")
    List<Match> findByTeam1vsTeam2(@Param("team1Id") UUID teamId1, @Param("team2Id") UUID teamId2);
}
