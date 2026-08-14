package it.unifi.volleyballscouting.service;

import it.unifi.volleyballscouting.dto.PlayerStatsDTO;
import it.unifi.volleyballscouting.dto.TeamStatsDTO;
import it.unifi.volleyballscouting.model.*;
import it.unifi.volleyballscouting.query.PerformanceQuery;
import it.unifi.volleyballscouting.repository.PerformanceRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PerformanceService {

    private final PerformanceRepository perfRepo;

    public PerformanceService(PerformanceRepository repo){
        this.perfRepo = repo;
    }

    public List<Performance> findFiltered(PerformanceQuery q){
        return perfRepo.findAll(q.toSpecification());

    }

    //Aggregated Queries

    public Optional<PlayerStatsDTO> getPlayerCareerStats(Player player){
        return perfRepo.getPlayerCareerStats(player);
    }

    public Optional<PlayerStatsDTO> getPlayerMatchStats(Player player, Match match){
        return perfRepo.getPlayerMatchStats(player, match);
    }

    public Optional <Performance> getPlayerSetPerformance(Player player, Set set){
        return perfRepo.findByPlayerIdAndSetId(player.getId(), set.getId());
    }

    public Optional<TeamStatsDTO> getTeamMatchStats(Team team, Match match){
        return perfRepo.getTeamMatchStats(team , match);
    }

    public Optional<TeamStatsDTO> getTeamStats(Team team){
        return perfRepo.getTeamGlobalStats(team);
    }

    public List<TeamStatsDTO> getBothTeamMatchStats(Match match){
        return perfRepo.getBothTeamsStatsForMatch(match);
    }
}

