package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.stats.TeamStatsDTO;
import it.unifi.volleyballscouting.model.Performance;
import it.unifi.volleyballscouting.model.PerformanceId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, PerformanceId>, JpaSpecificationExecutor<Performance> {

	// Statistiche globali del giocatore
	@Query("SELECT new it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO(" +
			"p.player.id, " +
			"COALESCE(SUM(p.digGood), 0L), COALESCE(SUM(p.digError), 0L), " +
			"COALESCE(SUM(p.attackPoint), 0L), COALESCE(SUM(p.attackInPlay), 0L), COALESCE(SUM(p.attackError), 0L), " +
			"COALESCE(SUM(p.servePoint), 0L), COALESCE(SUM(p.serveInPlay), 0L), COALESCE(SUM(p.serveError), 0L), " +
			"COALESCE(SUM(p.blockPoint), 0L), COALESCE(SUM(p.blockInPlay), 0L), COALESCE(SUM(p.blockError), 0L), " +
			"COALESCE(SUM(p.receiveGood), 0L), COALESCE(SUM(p.receiveError), 0L), " +
			"COALESCE(SUM(p.attackPoint + p.servePoint + p.blockPoint), 0L), " +
			"COALESCE(SUM(p.attackError + p.serveError + p.blockError + p.digError + p.receiveError), 0L), " +
			"COUNT(p.player.id)) " +
			"FROM Performance p WHERE p.player.id = :playerId " +
			"GROUP BY p.player.id")
	Optional<PlayerStatsDTO> getPlayerCareerStats(@Param("playerId") UUID playerId);

	// Statistiche del giocatore in una partita
	@Query("SELECT new it.unifi.volleyballscouting.dto.stats.PlayerStatsDTO(" +
			"p.player.id, " +
			"COALESCE(SUM(p.digGood), 0L), COALESCE(SUM(p.digError), 0L), " +
			"COALESCE(SUM(p.attackPoint), 0L), COALESCE(SUM(p.attackInPlay), 0L), COALESCE(SUM(p.attackError), 0L), " +
			"COALESCE(SUM(p.servePoint), 0L), COALESCE(SUM(p.serveInPlay), 0L), COALESCE(SUM(p.serveError), 0L), " +
			"COALESCE(SUM(p.blockPoint), 0L), COALESCE(SUM(p.blockInPlay), 0L), COALESCE(SUM(p.blockError), 0L), " +
			"COALESCE(SUM(p.receiveGood), 0L), COALESCE(SUM(p.receiveError), 0L), " +
			"COALESCE(SUM(p.attackPoint + p.servePoint + p.blockPoint), 0L), " +
			"COALESCE(SUM(p.attackError + p.serveError + p.blockError + p.digError + p.receiveError), 0L), " +
			"COUNT(p.player.id)) " +
			"FROM Performance p WHERE p.player.id = :playerId AND p.gameSet.match.id = :matchId " +
			"GROUP BY p.player.id")
	Optional<PlayerStatsDTO> getPlayerMatchStats(@Param("playerId") UUID playerId, @Param("matchId") UUID matchId);

	// Statistiche di una squadra in una partita
	@Query("SELECT new it.unifi.volleyballscouting.dto.stats.TeamStatsDTO(" +
			"p.player.team.id, " +
			"COALESCE(SUM(p.digGood), 0L), COALESCE(SUM(p.digError), 0L), " +
			"COALESCE(SUM(p.attackPoint), 0L), COALESCE(SUM(p.attackInPlay), 0L), COALESCE(SUM(p.attackError), 0L), " +
			"COALESCE(SUM(p.servePoint), 0L), COALESCE(SUM(p.serveInPlay), 0L), COALESCE(SUM(p.serveError), 0L), " +
			"COALESCE(SUM(p.blockPoint), 0L), COALESCE(SUM(p.blockInPlay), 0L), COALESCE(SUM(p.blockError), 0L), " +
			"COALESCE(SUM(p.receiveGood), 0L), COALESCE(SUM(p.receiveError), 0L), " +
			"COALESCE(SUM(p.attackPoint + p.servePoint + p.blockPoint), 0L), " +
			"COALESCE(SUM(p.attackError + p.serveError + p.blockError + p.digError + p.receiveError), 0L)) " +
			"FROM Performance p WHERE p.player.team.id = :teamId AND p.gameSet.match.id = :matchId " +
			"GROUP BY p.player.team.id")
	Optional<TeamStatsDTO> getTeamMatchStats(@Param("teamId") UUID teamId, @Param("matchId") UUID matchId);

	// Statistiche di entrambe le squadre in una partita
	@Query("SELECT new it.unifi.volleyballscouting.dto.stats.TeamStatsDTO(" +
			"p.player.team.id, " +
			"COALESCE(SUM(p.digGood), 0L), COALESCE(SUM(p.digError), 0L), " +
			"COALESCE(SUM(p.attackPoint), 0L), COALESCE(SUM(p.attackInPlay), 0L), COALESCE(SUM(p.attackError), 0L), " +
			"COALESCE(SUM(p.servePoint), 0L), COALESCE(SUM(p.serveInPlay), 0L), COALESCE(SUM(p.serveError), 0L), " +
			"COALESCE(SUM(p.blockPoint), 0L), COALESCE(SUM(p.blockInPlay), 0L), COALESCE(SUM(p.blockError), 0L), " +
			"COALESCE(SUM(p.receiveGood), 0L), COALESCE(SUM(p.receiveError), 0L), " +
			"COALESCE(SUM(p.attackPoint + p.servePoint + p.blockPoint), 0L), " +
			"COALESCE(SUM(p.attackError + p.serveError + p.blockError + p.digError + p.receiveError), 0L)) " +
			"FROM Performance p WHERE p.gameSet.match.id = :matchId " +
			"GROUP BY p.player.team.id")
	List<TeamStatsDTO> getBothTeamsStatsForMatch(@Param("matchId") UUID matchId);

	// Statistiche globali di una squadra
	@Query("SELECT new it.unifi.volleyballscouting.dto.stats.TeamStatsDTO(" +
			"p.player.team.id, " +
			"COALESCE(SUM(p.digGood), 0L), COALESCE(SUM(p.digError), 0L), " +
			"COALESCE(SUM(p.attackPoint), 0L), COALESCE(SUM(p.attackInPlay), 0L), COALESCE(SUM(p.attackError), 0L), " +
			"COALESCE(SUM(p.servePoint), 0L), COALESCE(SUM(p.serveInPlay), 0L), COALESCE(SUM(p.serveError), 0L), " +
			"COALESCE(SUM(p.blockPoint), 0L), COALESCE(SUM(p.blockInPlay), 0L), COALESCE(SUM(p.blockError), 0L), " +
			"COALESCE(SUM(p.receiveGood), 0L), COALESCE(SUM(p.receiveError), 0L), " +
			"COALESCE(SUM(p.attackPoint + p.servePoint + p.blockPoint), 0L), " +
			"COALESCE(SUM(p.attackError + p.serveError + p.blockError + p.digError + p.receiveError), 0L)) " +
			"FROM Performance p WHERE p.player.team.id = :teamId " +
			"GROUP BY p.player.team.id")
	Optional<TeamStatsDTO> getTeamGlobalStats(@Param("teamId") UUID teamId);

	Optional<Performance> findByPlayerIdAndGameSetId(UUID playerId, UUID gameSetId);
}