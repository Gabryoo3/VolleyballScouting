package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.TeamStatsDTO;
import it.unifi.volleyballscouting.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PerformanceRepository extends JpaRepository<Performance, PerformanceId>, JpaSpecificationExecutor<Performance> {

    //Global stats for player
    @Query("SELECT new it.unifi.volleyballscouting.dto.PlayerStatsDTO(" +
            "p.player.id, " +
            "SUM(p.aces), " +
            "SUM(p.serveErrors), " +
            "SUM(p.attacksGood), " +
            "SUM(p.attacksBad), " +
            "SUM(p.blocksGood), " +
            "SUM(p.blocksBad), " +
            "SUM(p.receiveGood)," +
            "SUM (p.receiveBad)," +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " + // Points Scored
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad), " + // Errors
            "COUNT(p.player.id)) " + // Counts sets played by counting player IDs
            "FROM Performance p WHERE p.player = :player " +
            "GROUP BY p.player.id")
    Optional<PlayerStatsDTO> getPlayerCareerStats(@Param("player") Player player);
    //Match stats for player
    @Query("SELECT new it.unifi.volleyballscouting.dto.PlayerStatsDTO(" +
            "p.player.id, " +
            "SUM(p.aces), " +
            "SUM(p.serveErrors), " +
            "SUM(p.attacksGood), " +
            "SUM(p.attacksBad), " +
            "SUM(p.blocksGood), " +
            "SUM(p.blocksBad), " +
            "SUM(p.receiveGood)," +
            "SUM (p.receiveBad)," +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " +
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad), " +
            "COUNT(p.player.id)) " +
            "FROM Performance p " +
            "WHERE p.player = :player AND p.set.match = :match " +
            "GROUP BY p.player.id")
    Optional<PlayerStatsDTO> getPlayerMatchStats(@Param("player") Player player, @Param("match") Match match);
    //Match stats for Team
    @Query("SELECT new it.unifi.volleyballscouting.dto.TeamStatsDTO(" +
            "p.player.team.id, " +
            "SUM(p.aces), SUM(p.serveErrors), " +
            "SUM(p.attacksGood), SUM(p.attacksBad), " +
            "SUM(p.blocksGood), SUM(p.blocksBad), " +
            "SUM(p.receiveGood), SUM(p.receiveBad), " +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " +
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad))" +
            "FROM Performance p " +
            "WHERE p.player.team = :team AND p.set.match = :match " +
            "GROUP BY p.player.team.id")
    Optional<TeamStatsDTO> getTeamMatchStats(@Param("team") Team team, @Param("match") Match match);
    //Match stats for both Teams
    @Query("SELECT new it.unifi.volleyballscouting.dto.TeamStatsDTO(" +
            "p.player.team.id, " +
            "SUM(p.aces), SUM(p.serveErrors), " +
            "SUM(p.attacksGood), SUM(p.attacksBad), " +
            "SUM(p.blocksGood), SUM(p.blocksBad), " +
            "SUM(p.receiveGood), SUM(p.receiveBad), " +
            "SUM(p.aces + p.attacksGood + p.blocksGood)," +
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad))" +
            "FROM Performance p " +
            "WHERE p.set.match = :match " +
            "GROUP BY p.player.team.id")
    List<TeamStatsDTO> getBothTeamsStatsForMatch(@Param("match") Match match);
    //Global stats for Team
    @Query("SELECT new it.unifi.volleyballscouting.dto.TeamStatsDTO(" +
            "p.player.team.id, " +
            "SUM(p.aces), SUM(p.serveErrors), " +
            "SUM(p.attacksGood), SUM(p.attacksBad), " +
            "SUM(p.blocksGood), SUM(p.blocksBad), " +
            "SUM(p.receiveGood), SUM(p.receiveBad)," +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " +
            "SUM(p.serveErrors + p.attacksBad + p.receiveBad + p.blocksBad)) "+
            "FROM Performance p " +
            "WHERE p.player.team = :team " +
            "GROUP BY p.player.team.id")
    Optional<TeamStatsDTO> getTeamGlobalStats(@Param("team") Team team);
    Optional<Performance> findByPlayerIdAndSetId(UUID playerId, UUID setId);

}