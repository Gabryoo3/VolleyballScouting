package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Referee;
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

    // find by referee
    @Query("SELECT DISTINCT m FROM Match m WHERE m.r1 = :referee OR m.r2 = :referee")
    List<Match> findByReferee(@Param("referee") Referee r);

    // find by team, winning or losing matches
    @Query("SELECT DISTINCT m FROM Match m WHERE m.teamHome = :team OR m.teamGuest = :team")
    List<Match> findByTeam(@Param("team")Team t);
    @Query("SELECT m FROM Match m WHERE" +
    "(m.teamHome = :team AND m.homeScore > m.guestScore) OR"+
    "(m.teamGuest = :team AND m.guestScore > m.homeScore)")
    List<Match> findByTeamWins(@Param("team")Team t);
    @Query("SELECT m FROM Match m WHERE" +
            "(m.teamHome = :team AND m.homeScore < m.guestScore) OR"+
            "(m.teamGuest = :team AND m.guestScore < m.homeScore)")
    List<Match> findByTeamLosses(@Param("team")Team t);
    @Query("SELECT m FROM Match m WHERE"+
            "(m.teamHome = :team1 AND m.teamGuest = :team2) OR"+
            "(m.teamGuest = :team1 AND m.teamHome = :team2)")
    List<Match> findByTeam1vsTeam2(@Param("team1") Team t1, @Param("team2") Team t2);
}
