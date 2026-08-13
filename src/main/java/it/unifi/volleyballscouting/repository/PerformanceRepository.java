package it.unifi.volleyballscouting.repository;

import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
import it.unifi.volleyballscouting.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PerformanceRepository extends JpaRepository<Performance, PerformanceId> {
    List<Performance> findPerformanceByPlayerId(Player p);
    List<Performance> findPerformanceBySetIdMatch(Match m);
    List<Performance> findPerformanceBySetId(Set s);
    List<Performance> findPerformanceBypId(PerformanceId PId);
    List<Performance> findPerformanceBySetIdAndAcesAfter(Set set, int acesThreshold); //players that made at least a certain amount of aces
    List<Performance> findPerformancesBySetIdMatchMatchesAndAcesAfter(Match m, int acesThreshold);
    List<Performance> findByPlayerIdTeamAndSetIdMatchAndAcesAfter(Team t, Match m, int acesThreshold);



    //Global stats
    @Query("SELECT new it.unifi.volleyballscouting.dto.PlayerStatsDTO(" +
            "p.playerId.id, " +
            "SUM(p.aces), " +
            "SUM(p.serveErrors), " +
            "SUM(p.attacksGood), " +
            "SUM(p.attacksBad), " +
            "SUM(p.blocksGood), " +
            "SUM(p.blocksBad), " +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " + // Points Scored
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad), " + // Errors
            "COUNT(p.playerId.id)) " + // Counts sets played by counting player IDs
            "FROM Performance p WHERE p.playerId = :player " +
            "GROUP BY p.playerId.id")
    Optional<PlayerStatsDTO> getPlayerCareerStats(@Param("player") Player player);
    //Match stats
    @Query("SELECT new it.unifi.volleyballscouting.dto.PlayerStatsDTO(" +
            "p.playerId.id, " +
            "SUM(p.aces), " +
            "SUM(p.serveErrors), " +
            "SUM(p.attacksGood), " +
            "SUM(p.attacksBad), " +
            "SUM(p.blocksGood), " +
            "SUM(p.blocksBad), " +
            "SUM(p.aces + p.attacksGood + p.blocksGood), " +
            "SUM(p.serveErrors + p.attacksBad + p.blocksBad + p.receiveBad), " +
            "COUNT(p.playerId.id)) " +
            "FROM Performance p " +
            "WHERE p.playerId = :player AND p.setId.match = :match " +
            "GROUP BY p.playerId.id")
    Optional<PlayerStatsDTO> getPlayerMatchStats(@Param("player") Player player, @Param("match") Match match);

}