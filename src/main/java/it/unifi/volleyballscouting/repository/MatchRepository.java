package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.model.Match;
import it.unifi.volleyballscouting.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface MatchRepository extends JpaSpecificationExecutor<Match>, JpaRepository<Match, UUID> {

    boolean existsByRefereeIdAndMatchDateTimeAfterAndMatchDateTimeBefore(UUID refereeId, LocalDateTime after, LocalDateTime before);
    @Query("select (count(m) > 0) from Match m where (m.teamHome.id = ?1 or m.teamGuest.id = ?1) and m.matchDateTime > ?2 and m.matchDateTime < ?3")
    boolean existsByTeamIdAndMatchDateTimeAfterAndMatchDateTimeBefore(UUID teamId, LocalDateTime after, LocalDateTime before);
}
